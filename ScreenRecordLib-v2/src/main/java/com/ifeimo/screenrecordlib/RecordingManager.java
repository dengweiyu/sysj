package com.ifeimo.screenrecordlib;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.ifeimo.screenrecordlib.constant.Configuration;
import com.ifeimo.screenrecordlib.constant.Constant;
import com.ifeimo.screenrecordlib.listener.RecordingListener;
import com.ifeimo.screenrecordlib.listener.RecordingOperation;
import com.ifeimo.screenrecordlib.listener.ScreenRecordPermissionListener;
import com.ifeimo.screenrecordlib.record.record43.RecordRunner43;
import com.ifeimo.screenrecordlib.record.record43.ScreenRecord43;
import com.ifeimo.screenrecordlib.record.record44.ScreenCapture44;
import com.ifeimo.screenrecordlib.record.record44.ScreenRecord44;
import com.ifeimo.screenrecordlib.record.record50.ScreenCaptureActivity;
import com.ifeimo.screenrecordlib.record.record50.ScreenRecordActivity;
import com.ifeimo.screenrecordlib.service.AppStartService;
import com.ifeimo.screenrecordlib.util.Utils;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 服务：录屏服务
 */
public class RecordingManager implements RecordingOperation {

    private static final String TAG = RecordingManager.class.getSimpleName();

    private static RecordingManager instance;

    public static RecordingManager getInstance() {
        if (instance == null)
            synchronized (RecordingManager.class) {
                if (instance == null)
                    instance = new RecordingManager();
            }
        return instance;
    }

    private RecordingManager() {
        super();
        Log.d(TAG, "**************************************************************************");
        Log.d(TAG, "**************************************************************************");
        Log.d(TAG, "**************************************************************************");
        Log.d(TAG, "**************************************************************************");
    }

    // ---------------------------------------------------------------------------
    // ---------------------------------------------------------------------------
    // ---------------------------------------------------------------------------
    // ---------------------------------------------------------------------------

    private RecordingListener listener;

    public RecordingListener listener() {
        return listener;
    }

    public void listener(RecordingListener listener) {
        this.listener = listener;
    }

    public boolean onRecordingStarted() {
        if (listener != null) {
            listener.onRecordingStarted();
            return true;
        }
        return false;
    }

    public boolean onRecordingStarted2() {
        if (listener != null) {
            listener.onRecordingStarted2();
            return true;
        }
        return false;
    }

    public boolean onRecordingStoped() {
        if (listener != null) {
            listener.onRecordingStoped();
            return true;
        }
        return false;
    }

    public boolean onRecordingPaused() {
        if (listener != null) {
            listener.onRecordingPaused();
            return true;
        }
        return false;
    }

    public boolean onRecordingResumed() {
        if (listener != null) {
            listener.onRecordingResumed();
            return true;
        }
        return false;
    }

    public boolean onRecordingCompleted() {
        if (listener != null) {
            listener.onRecordingCompleted();
            return true;
        }
        return false;
    }

    public boolean onCapturingCompleted() {
        if (listener != null) {
            listener.onCapturingCompleted();
            return true;
        }
        return false;
    }

    public boolean onRecordingTiming() {
        if (listener != null) {
            listener.onRecordingTiming();
            return true;
        }
        return false;
    }

    private ScreenRecordPermissionListener permissionListener;

    public void setPermissionListener(ScreenRecordPermissionListener permissionListener) {
        this.permissionListener = permissionListener;
    }

    public ScreenRecordPermissionListener getPermissionListener() {
        return permissionListener;
    }

    public void getPermissionSuccess(){
        if (permissionListener != null){
            permissionListener.getPermissionSuccess();
        }
    }

    public void getPermissionFail(){
        if (permissionListener != null){
            permissionListener.getPermissionFail();
        }
    }

    // ---------------------------------------------------------------------------
    // ---------------------------------------------------------------------------
    // ---------------------------------------------------------------------------
    // ---------------------------------------------------------------------------

    private Context context;
    private Resources resources;

    public Context context() {
        return context;
    }

    public void initialize(Context context) {
        if (this.context == null) {
            _initialize(context);
        }
    }

    private void _initialize(Context context) {
        this.context = context;
        if (context == null) {
            sendMessage(Constant.Msg.CONTEXT_IS_NULL);
            return;
        }
        resources = context.getResources();
        AppStartService.startAppStartService();
    }

    /**
     * 截屏
     *
     * @param path 截屏的文件
     */
    @Override
    public void startScreenCapture(String path) {
        if (context == null) {
            sendMessage(Constant.Msg.CONTEXT_IS_NULL);
            return;
        }
        if (Build.VERSION.SDK_INT < 21 &&
                !Utils.root()) {
            sendMessage(Constant.Msg.ROOT_IS_INVALID);
            return;
        }
        this.path = path;
        _startCapture(path);
    }

    /**
     * 开始录屏
     *
     * @param path 录屏保存的文件
     */
    @Override
    public void startScreenRecord(String path) {
        startScreenRecord(path, Configuration.DEFAULT);
    }

    /**
     * 开始录屏
     *
     * @param path 录屏保存的文件
     * @param configuration 录屏配置
     */
    @Override
    public void startScreenRecord(String path, Configuration configuration) {
        if (context == null) {
            sendMessage(Constant.Msg.CONTEXT_IS_NULL);
            return;
        }
        if (Build.VERSION.SDK_INT < 21 &&
                !Utils.root()) {
            sendMessage(Constant.Msg.ROOT_IS_INVALID);
            return;
        }
        if (isRecording.get() == true) {
            sendMessage(Constant.Msg.STATE_RECORDING);
            return;
        }
        this.path = path;
        this.configuration = configuration;
        Log.d(TAG, "startScreenRecord: ");
        _startRecord(path, configuration);
        isRecording.set(true);
    }

    /**
     * 停止录屏
     */
    @Override
    public void stopScreenRecord() {
        if (Build.VERSION.SDK_INT < 21 &&
                !Utils.root()) {
            sendMessage(Constant.Msg.ROOT_IS_INVALID);
            return;
        }
        if (isRecording.get() == false) {
            sendMessage(Constant.Msg.STATE_COMPLATED);
            return;
        }
        Log.d(TAG, "stopScreenRecord: ");
        _stopRecord();
        isRecording.set(false);
    }

    /**
     * 暂停录屏
     */
    @Override
    public void pauseScreenRecord() {
        if (context == null) {
            sendMessage(Constant.Msg.CONTEXT_IS_NULL);
            return;
        }
        if (Build.VERSION.SDK_INT < 21 &&
                !Utils.root()) {
            sendMessage(Constant.Msg.ROOT_IS_INVALID);
            return;
        }
        if (isRecording.get() == false) {
            sendMessage(Constant.Msg.STATE_COMPLATED);
            return;
        }
        if (isPausing.get() ==  false) {
            Log.d(TAG, "pauseScreenRecord: ");
            _pauseRecord();
            isPausing.set(true);
        }
    }

    /**
     * 继续录屏
     */
    @Override
    public void resumeScreenRecord() {
        if (context == null) {
            sendMessage(Constant.Msg.CONTEXT_IS_NULL);
            return;
        }
        if (Build.VERSION.SDK_INT < 21 &&
                !Utils.root()) {
            sendMessage(Constant.Msg.ROOT_IS_INVALID);
            return;
        }
        if (isRecording.get() == false) {
            sendMessage(Constant.Msg.STATE_COMPLATED);
            return;
        }
        if (isPausing.get() == true) {
            Log.d(TAG, "resumeScreenRecord: ");
            _resumeRecord();
            isPausing.set(false);
        }
    }

    // ---------------------------------------------------------------------------
    // ---------------------------------------------------------------------------
    // ---------------------------------------------------------------------------
    // ---------------------------------------------------------------------------

    /**
     * v
     */
    public String time() {
        int minutes = secs.get() / 60;
        int seconds = secs.get() % 60;
        String m, s;
        if (seconds / 10 == 0) {// 1位
            s = "0" + seconds;
        } else {
            s = "" + seconds;
        }
        if (minutes / 10 == 0) {// 1位
            m =  "0" + minutes;
        } else if (minutes / 100 == 0) {// 2位
            m =  "" + minutes;
        } else {
            m =  "" + minutes;
        }
        return m + ":" + s;
    }

    /**
     * 录屏时间
     */
    public long secs() {
        return secs.get();
    }

    /**
     * 录屏时间（包括暂停）
     */
    public long maxMillis() {
        return maxMillis;
    }

    /**
     * 是否录屏中
     */
    public boolean isRecording() {
        return isRecording.get();
    }

    /**
     * 是否暂停中
     */
    public boolean isPausing() {
        return isPausing.get();
    }

    /**
     * 录屏配置
     */
    public Configuration configuration() {
        return configuration;
    }

    /**
     * 录屏配置
     */
    public String path() {
        return path;
    }

    // ---------------------------------------------------------------------------
    // ---------------------------------------------------------------------------
    // ---------------------------------------------------------------------------
    // ---------------------------------------------------------------------------

    private AtomicBoolean isRecording = new AtomicBoolean(false), isPausing = new AtomicBoolean(false);
    private String path;
    private Configuration configuration;

    private Handler handler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {

                case Constant.Msg.CONTEXT_IS_NULL:
                    isRecording.set(false);
                    path = null;
                    configuration = null;
                    Log.d(TAG, "handler: CONTEXT_IS_NULL");
                    break;

                case Constant.Msg.PATH_IS_INVALID:
                    isRecording.set(false);
                    path = null;
                    configuration = null;
                    Log.d(TAG, "handler: PATH_IS_INVALID");
                    break;

                case Constant.Msg.API44_DPWN_HAS_NOT_PAUSE:
                    isPausing.set(false);
                    Log.d(TAG, "handler: API44_DPWN_HAS_NOT_PAUSE");
                    break;

                case Constant.Msg.STATE_RECORDING:
                    isRecording.set(true);
                    isPausing.set(false);
                    Log.d(TAG, "handler: STATE_RECORDING");
                    break;

                case Constant.Msg.STATE_PAUSE:
                    isPausing.set(true);
                    Log.d(TAG, "handler: STATE_PAUSE");
                    break;

                case Constant.Msg.STATE_TIMEING:
                    gc();
                    break;

                case Constant.Msg.STATE_COMPLATED:
                    isRecording.set(false);
                    path = null;
                    configuration = null;
                    Log.d(TAG, "handler: STATE_TIMEING/maxMillis=" + maxMillis + "/seconds=" + secs.get());
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void sendMessage(int what) {
        if (handler != null)
            handler.obtainMessage(what).sendToTarget();
    }

    private void gc() {
        int remainder = secs.get() % 60;
        // Log.d(TAG, "gc: " + remainder);
        if (remainder == 0) {
            // 垃圾回收
            System.gc();
            Log.d(TAG, "gc: true");
        }
    }

    // ---------------------------------------------------------------------------
    // ---------------------------------------------------------------------------
    // ---------------------------------------------------------------------------
    // ---------------------------------------------------------------------------

    /**
     * 最大录制时间2小时（2 * 60 * 60秒）
     */
    private static final long MAX_SECS = 2 * 60 * 60;

    /**
     * 最大录制时间（包括暂停）2小时（2 * 60 * 60 * 1000毫秒）
     */
    private static final long MAX_MILLIS = 2 * 60 * 60 * 1000;

    /**
     * 录屏开始时间（毫秒）
     */
    private long startMillis;

    /**
     * 录屏时间（包括暂停）（毫秒）
     */
    private volatile int maxMillis;

    /**
     * 录屏计时（秒）
     */
    private AtomicInteger secs = new AtomicInteger();

    private final String timerName = "Timer-RecordingManager";
    private long timerDelay = 1000;
    private long timerPeriod = 1000;
    private Timer timer;
    private TimerTask timerTask;

    /**
     * 开始计时
     */
    public void startTiming() {
        stopTiming();
        Log.d(TAG, "startTiming: // -------------------------------------------------------------");

        startMillis = SystemClock.uptimeMillis();
        secs.set(0);
        ++maxMillis;
        sendMessage(Constant.Msg.STATE_TIMEING);

        timer = new Timer(timerName, false);
        timerTask = new TimerTask() {

            @Override
            public void run() {
                
                // Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
                // 计时
                if (isPausing.get() == false) {
                    secs.incrementAndGet();
                }
                maxMillis = (int) (SystemClock.uptimeMillis() - startMillis);
                // Log.d(TAG, "startTiming: secs=" + secs.get());
                // Log.d(TAG, "startTiming: maxMillis=" + maxMillis);
                // Log.d(TAG, "startTiming: time=" + time());

                // 发送消息
                sendMessage(Constant.Msg.STATE_TIMEING);

                onRecordingTiming();

                if (Build.VERSION.SDK_INT < 21) {
                    if (secs.get() >= MAX_SECS ||
                            maxMillis >= MAX_MILLIS) {
                        stopScreenRecord();
                    }
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, timerDelay, timerPeriod);
    }

    /**
     * 停止计时
     */
    private void stopTiming() {
        Log.d(TAG, "stopTiming: // -------------------------------------------------------------");
        if (timer != null)
            try {
                timer.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        secs.set(0);
        maxMillis = 0;
        sendMessage(Constant.Msg.STATE_TIMEING);
    }

    // ---------------------------------------------------------------------------

    private Handler h = new Handler(Looper.getMainLooper());

    private int index;

    private void newThread(Runnable r) {
        if (r != null) {
            new Thread(r, "newThread-RecordingManager-" + (++index)).start();
        }
    }

    // ---------------------------------------------------------------------------
    // ---------------------------------------------------------------------------
    // ---------------------------------------------------------------------------
    // ---------------------------------------------------------------------------

    private void _startRecord(final String path, final Configuration configuration) {
        newThread(new Runnable() {
            @Override
            public void run() {
                // Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
                startRecord(path, configuration);
            }
        });
    }

    private void _stopRecord() {
        newThread(new Runnable() {
            @Override
            public void run() {
                // Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
                stopRecord();
            }
        });
    }

    private void _pauseRecord() {
        newThread(new Runnable() {
            @Override
            public void run() {
                // Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
                pauseRecord();
            }
        });
    }

    private void _resumeRecord() {
        newThread(new Runnable() {
            @Override
            public void run() {
                // Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
                resumeRecord();
            }
        });
    }

    private void _startCapture(final String path) {
        newThread(new Runnable() {
            @Override
            public void run() {
                // Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
                startCapture(path);
            }
        });
    }

    // ---------------------------------------------------------------------------
    // ---------------------------------------------------------------------------
    // ---------------------------------------------------------------------------
    // ---------------------------------------------------------------------------

    private ScreenRecord44 record44;
    private ScreenRecord43 record43;
    private RecordRunner43 runner43;

    /**
     * 开始
     */
    private void startRecord(String path, Configuration configuration) {
        Log.d(TAG, "startRecord: // -------------------------------------------------------------");
        Log.d(TAG, "startRecord: path=" + path);
        Log.d(TAG, "startRecord: configuration=" + configuration);
        final Context context = RecordingManager.getInstance().context();
        if (path == null || path.length() == 0) {
            Log.d(TAG, "onStartCommand: 1");
            sendMessage(Constant.Msg.PATH_IS_INVALID);
            return;
        }
        File file = null;
        try {
            file = new File(path);
            Log.d(TAG, "onStartCommand: 2");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onStartCommand: 3");
        }
        if (file == null || file.isDirectory()) {
            Log.d(TAG, "onStartCommand: 4");
            sendMessage(Constant.Msg.PATH_IS_INVALID);
            return;
        }
        if (configuration == null)
            configuration = Configuration.DEFAULT;
        Log.d(TAG, "onStartCommand: 6");
        if (Build.VERSION.SDK_INT >= 21) {// 5.0以上
            // TODO: 2016/6/29 非UI线程调用容易引起严重卡顿
            // ScreenRecordActivity._startRecording(path, configuration);

            final String p = path;
            final Configuration c = configuration;
            h.post(new Runnable() {
                @Override
                public void run() {

                    ScreenRecordActivity.startRecording(context, p, c);
                }
            });
        } else if (Build.VERSION.SDK_INT >= 19) {// 4.4
            record44 = new ScreenRecord44(path, configuration);
            record44._startRecording();
            newThread(new Runnable() {
                @Override
                public void run() {
                    // Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);

                    record44._readInputStream();
                }
            });
            newThread(new Runnable() {
                @Override
                public void run() {
                    // Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);

                    record44._readErrorStream();
                }
            });
            onRecordingStarted();
            sendMessage(Constant.Msg.STATE_RECORDING);
            // 开始计时
            startTiming();
            record44._waitFor();
            onRecordingStarted2();
        } else {
            /*
            // 无效
            record43 = new ScreenRecord43(path, configuration);
            record43._initialize();
            newThread(new Runnable() {
                @Override
                public void run() {
                    // Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);

                    record43._readInputStream();
                }
            });
            newThread(new Runnable() {
                @Override
                public void run() {
                    // Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);

                    record43._readErrorStream();
                }
            });
            record43._startRecording();
            onRecordingStarted();
            // TODO: 2016/7/7
            record43._waitFor();
            onRecordingStarted2();*/

            /*
            // 无效
            record43 = new ScreenRecord43(path, configuration);
            record43._exec(new ScreenRecord43.Callback() {
                @Override
                public void call() {

                    onRecordingStarted();
                    sendMessage(Constant.Msg.STATE_RECORDING);
                    // 开始计时
                    startTiming();

                    newThread(new Runnable() {
                        @Override
                        public void run() {
                            // Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);

                            record43._readInputStream();
                        }
                    });
                    newThread(new Runnable() {
                        @Override
                        public void run() {
                            // Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);

                            record43._readErrorStream();
                        }
                    });
                    onRecordingStarted2();
                }
            });*/

            if (runner43 != null) {
                try {
                    runner43.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    runner43.destroy();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                runner43 = null;
            }

            final String p = path;
            final Configuration c = configuration;
            runner43 = new RecordRunner43();
            runner43.initialize(new RecordRunner43.OnReadyListener() {

                @Override
                public void onReady() {

                    try {
                        runner43.start(p, c);
                        onRecordingStarted();
                        sendMessage(Constant.Msg.STATE_RECORDING);
                        // 开始计时
                        startTiming();
                        onRecordingStarted2();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFinished() {
                    onRecordingCompleted();
                }
            });
        }
    }

    /**
     * 停止
     */
    private void stopRecord() {
        Log.d(TAG, "stopRecord: // -------------------------------------------------------------");
        if (Build.VERSION.SDK_INT >= 21) {// 5.0以上
            ScreenRecordActivity.stopRecording();
        } else if (Build.VERSION.SDK_INT >= 19) {// 4.4
            if (record44 != null) {
                record44._stopRecording();
                record44 = null;
                onRecordingStoped();
            }
        } else {
            /*
            if (record43 != null) {
                record43._stopRecording();
                record43 = null;
                RecordingManager.getInstance().onRecordingStoped();
            }*/

            if (runner43 != null) {
                try {
                    runner43.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    runner43.destroy();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                runner43 = null;
                onRecordingStoped();
            }
        }
        // 停止计时
        stopTiming();
        sendMessage(Constant.Msg.STATE_COMPLATED);
    }

    /**
     * 暂停
     */
    private void pauseRecord() {
        Log.d(TAG, "pauseRecord: // -------------------------------------------------------------");
        sendMessage(Constant.Msg.STATE_PAUSE);
        if (Build.VERSION.SDK_INT >= 21) {// 5.0以上
            ScreenRecordActivity.pauseRecording();
        } else if (Build.VERSION.SDK_INT >= 19) {// 4.4
            if (record44 != null) {
                record44._pauseRecording();
                RecordingManager.getInstance().onRecordingPaused();
            }
        } else {
            sendMessage(Constant.Msg.API44_DPWN_HAS_NOT_PAUSE);
        }
    }

    /**
     * 继续
     */
    private void resumeRecord() {
        Log.d(TAG, "resumeRecord: // -------------------------------------------------------------");
        sendMessage(Constant.Msg.STATE_RECORDING);
        if (Build.VERSION.SDK_INT >= 21) {// 5.0以上
            ScreenRecordActivity.restartRecording();
        } else if (Build.VERSION.SDK_INT >= 19) {// 4.4
            if (record44 != null) {
                record44._restartRecording();
                RecordingManager.getInstance().onRecordingResumed();
            }
        } else {
            sendMessage(Constant.Msg.API44_DPWN_HAS_NOT_PAUSE);
        }
    }

    /**
     * 截屏
     */
    private void startCapture(final String path) {
        Log.d(TAG, "startCapture: // -------------------------------------------------------------");
        Log.d(TAG, "startCapture: path=" + path);
        final Context context = RecordingManager.getInstance().context();
        if (path == null || path.length() == 0) {
            Log.d(TAG, "onStartCommand: 1");
            sendMessage(Constant.Msg.PATH_IS_INVALID);
            return;
        }
        File file = null;
        try {
            file = new File(path);
            Log.d(TAG, "onStartCommand: 2");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onStartCommand: 3");
        }
        if (file == null || file.isDirectory()) {
            Log.d(TAG, "onStartCommand: 4");
            sendMessage(Constant.Msg.PATH_IS_INVALID);
            return;
        }
        Log.d(TAG, "onStartCommand: 6");
        if (Build.VERSION.SDK_INT >= 21) {// 5.0以上
            // TODO: 2016/6/29 非UI线程调用容易引起严重卡顿
            // ScreenCaptureActivity._startCapture(path);

            h.post(new Runnable() {
                @Override
                public void run() {

                    ScreenCaptureActivity.startCapture(context, path);
                }
            });
        } else if (Build.VERSION.SDK_INT >= 19) {// 4.4
            ScreenCapture44 record = new ScreenCapture44(path);
            record._startCapture();
        } else {
            ScreenCapture44 record = new ScreenCapture44(path);
            record._startCapture();
        }
    }
}
