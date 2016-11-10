package com.ifeimo.screenrecordlib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import com.stericson.RootTools.RootTools;

public class RecordProcess43 implements Runnable {

    public static final String TAG = RecordProcess43.class.getSimpleName();

    private Timeout configureTimeout;
    private Timeout startTimeout;
    private Timeout stopTimeout;
    private boolean destroying;
    private String executable;
    private int exitValue;
    private Integer exitValueOverride;
    private volatile boolean forceKilled;
    private Float fps;
    private OnStateChangeListener onStateChangeListener;
    private Process process;
    private volatile ProcessState processState;
    private OutputStream outputStream;
    private InputStream inputStream;

    public RecordProcess43(String executable, OnStateChangeListener listener) {
        this.processState = ProcessState.NEW;
        this.exitValue = -1;
        this.fps = -1.0F;
        this.destroying = false;
        this.forceKilled = false;
        this.configureTimeout = new Timeout(5000, "recording_error", "configure_timeout", 301);
        this.startTimeout = new Timeout(10000, "recording_error", "start_timeout", 302);
        this.stopTimeout = new Timeout(10000, "stopping_error", "stop_timeout", 303);
        this.executable = executable;
        this.onStateChangeListener = listener;
    }

    private void checkStatus(String errType, String errName, int errCode) {
        Log.e(TAG, "Run Status : " + errType);
        Log.e(TAG, "Run Status : " + errName);
        Log.e(TAG, "Run Status : " + errCode);
        if ((this.forceKilled) || (this.destroying) || (errName == null))
            return;
        if (!errType.equals(errName)) {
            this.exitValueOverride = errCode;
            forceKill();
        } else {
            Log.e(TAG, errName);
        }
    }

    private void forceKill() {
        Log.d(TAG, "forceKill");
        if (this.forceKilled) {
            Log.d(TAG, "Already force killed");
        } else {
            this.forceKilled = true;
            killProcess(this.executable);
        }
    }

    private void killMediaServer() {
        Log.d(TAG, "restartMediaServer");
        killProcess("/system/bin/mediaserver");
    }

    private void killProcess(String paramString) {
        Log.d(TAG, "kill process " + paramString);
        RootTools.killProcess(paramString);
    }

    private boolean mediaServerRelatedError() {
        if (this.destroying) {
            if (-2 >= 0) {
                return true;
            }
        }
        return false;
    }

    private void parseFps(String fps) {
        if ((fps != null) && (fps.startsWith("fps ")) && (fps.length() > 4)) {
            try {
                this.fps = Float.parseFloat(fps.substring(4));
                if ((!this.destroying) && (this.fps < 0.0F)) {
                    Log.e(TAG, "Incorrect fps value received \"" + fps + "\"");
                }
            } catch (Exception ignored) {
            }
        }
    }

    private void runCommand(String command) {
        try {
            Log.e(TAG, "Run Command : " + command);
            String str = command + "\n";
            this.outputStream.write(str.getBytes());
            this.outputStream.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, "Error running command");
        }
    }

    private void setErrorState() {
        setProcessState(ProcessState.ERROR);
        if (mediaServerRelatedError())
            killMediaServer();
    }

    private void setProcessState(ProcessState newState) {
        Log.d(TAG, "setProcessState " + newState);
        ProcessState oldState = this.processState;
        this.processState = newState;
        if (this.onStateChangeListener != null) {
            this.onStateChangeListener.onStateChange
                    (this, oldState, newState, this.exitValue, this.fps);
        }
    }

    private void stopProcess() {
        if (this.process != null) {
            try {
                this.outputStream.close();
            } catch (Exception localIOException) {
                localIOException.printStackTrace();
              
            }
        }
    }

    public void destroy() {
        if (this.process != null) {
            Log.d(TAG, "Destroying process");
            this.destroying = true;
            this.stopTimeout.start();
            stopProcess();
        }
    }

    boolean isRecording() {
        return this.processState == ProcessState.RECORDING;
    }

    boolean isStopped() {
        return this.processState == ProcessState.FINISHED || this.processState == ProcessState.ERROR;
    }

    public void run() {
        setProcessState(ProcessState.STARTING);
        BufferedReader reader;
        Log.d(TAG, "Starting native process");
        Runtime runtime = Runtime.getRuntime();
        try {
            this.process = runtime.exec(new String[]{"su", "-c", this.executable});
            Log.d(TAG, "Native process started");
            this.outputStream = this.process.getOutputStream();
            this.inputStream = this.process.getInputStream();
            reader = new BufferedReader(new InputStreamReader(this.inputStream));
            checkStatus("ready", reader.readLine(), 304);
            setProcessState(ProcessState.READY);
            checkStatus("configured", reader.readLine(), 305);
            this.configureTimeout.cancel();
            checkStatus("recording", reader.readLine(), 306);
            this.startTimeout.cancel();
            parseFps(reader.readLine());
            //////////////////////////////////
            Log.d(TAG, "Waiting for native process to exit");
            this.process.waitFor();
            Log.d(TAG, "Native process finished");
            this.stopTimeout.cancel();
            this.exitValue = this.process.exitValue();
        } catch (IOException exc) {
            Log.e(TAG, "Error starting a new native process");
            forceKill();
            if (mediaServerRelatedError()) {
                killMediaServer();
            }
        } catch (InterruptedException exc) {
            try {
                this.exitValue = this.process.exitValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (this.exitValueOverride != null) {
                if (this.exitValue < 200) {
                    this.exitValue = exitValueOverride;
                }
                setErrorState();
            } else if (this.processState == ProcessState.STOPPING) {
                setProcessState(ProcessState.FINISHED);
            } else {
                setErrorState();
            }
        } finally {
            if (this.exitValueOverride != null) {
                if (this.exitValue < 200) {
                    this.exitValue = this.exitValueOverride;
                }
                setProcessState(ProcessState.ERROR);
                if (!this.destroying) {
                    killMediaServer();
                }
                Log.d(TAG, "Return value: " + this.exitValue);
                this.exitValueOverride = 307;
                forceKill();
            } else {
                Log.d(TAG, "Success exit code : " + this.exitValue);
            }
            setProcessState(ProcessState.FINISHED);
        }
    }

    public void startRecording(String path, Configuration configuration) {
        Log.d(TAG, "startRecording " + path);
        if (this.processState != ProcessState.READY) {
            Log.e(TAG, "Can't start recording in current processState: " + this.processState);
            return;
        }

        int width;
        int height;
        Context context = RecordingManager.getInstance().context();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        if (configuration.getQuality() == Configuration.Quality.ULTRAHIGH) {
            width = (int) (metrics.widthPixels / 1.4);
            height = (int) (metrics.heightPixels / 1.4);
        } else if (configuration.getQuality() == Configuration.Quality.HIGH) {
            width = (int) (metrics.widthPixels / 1.4);
            height = (int) (metrics.heightPixels / 1.4);
        } else if (configuration.getQuality() == Configuration.Quality.STANDARD) {
            width = (int) (metrics.widthPixels / 1.7);
            height = (int) (metrics.heightPixels / 1.7);
        } else {
            width = (int) (metrics.widthPixels / 1.7);
            height = (int) (metrics.heightPixels / 1.7);
        }
        if (width > height) {
            int a = width;
            width = height;
            height = a;
        }
        int rotation;
        if (configuration.isLandscape())
            rotation = -90;
        else
            rotation = 0;
        String audio;
        if (configuration.isAudio())
            audio = "m";
        else
            audio = "x";

        setProcessState(ProcessState.RECORDING);
        this.configureTimeout.start();
        this.startTimeout.start();

        runCommand(path);
        runCommand(String.valueOf(rotation));
        runCommand(audio);
        runCommand(String.valueOf(width));
        runCommand(String.valueOf(height));
        runCommand("0");
        runCommand("0");
        runCommand("15");
        runCommand("CPU");
        runCommand("RGBA");
        runCommand("15000000");
        runCommand("32000");
        runCommand("-2");
        runCommand("0");
    }

    public void stopRecording() {
        if (this.processState != ProcessState.RECORDING) {
            Log.e(TAG, "Can't stop recording in current processState: " + this.processState);
        } else {
            Log.d(TAG, "stopRecording");
            setProcessState(ProcessState.STOPPING);
            runCommand("stop");
            this.stopTimeout.start();
        }
    }

    public interface OnStateChangeListener {
        void onStateChange(
                RecordProcess43 process,
                ProcessState newState,
                ProcessState oldState,
                int errCode, float fps);
    }

    public enum ProcessState {
        NEW,
        STARTING,
        READY,
        RECORDING,
        STOPPING,
        FINISHED,
        ERROR
    }

    private class Timeout {

        private String errType;
        private int errorCode;
        private String errorName;
        private int time;
        private Timer timer;

        public Timeout(int time, String errType, String errName, int errCode) {
            this.time = time;
            this.errType = errType;
            this.errorName = errName;
            this.errorCode = errCode;
        }

        public void cancel() {
            synchronized (RecordProcess43.this) {
                if (this.timer != null) {
                    this.timer.cancel();
                    this.timer = null;
                }
            }
        }

        public void start() {
            synchronized (RecordProcess43.this) {
                if (this.timer != null) {
                    Log.e(TAG, "Timeout already started");
                } else {
                    this.timer = new Timer();
                    this.timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (RecordProcess43.this.process != null) {
                                Log.d(TAG, "Timeout," + errorName + ", killing the native process");
                                RecordProcess43.this.forceKill();
                            }
                        }
                    }, this.time);
                }
            }
        }
    }
}