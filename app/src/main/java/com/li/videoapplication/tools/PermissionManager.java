package com.li.videoapplication.tools;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import com.fmsysj.screeclibinvoke.utils.RootUtil;
import com.li.videoapplication.data.network.LightTask;
import com.li.videoapplication.data.network.UITask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 工具：录屏权限管理
 */
public class PermissionManager {

    private static final String TAG = PermissionManager.class.getSimpleName();

    public interface Finishable {
        void onFinish();
    }

    private Context context;
    private Finishable finishable;
    private MediaRecorder mediaRecorder;
    private AudioRecord audioRecord;
    private Camera camera;

    public PermissionManager(Context context, Finishable finishable) {
        this.context = context;
        this.finishable = finishable;
    }

    public void checkPermission() {
        LightTask.post(new Runnable() {
            @Override
            public void run() {
                // mediaRecorder();
                audioRecord();
            }
        });
    }

    private void mediaRecorder() {
        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setAudioChannels(2);
            // 128, 192, 256, 320
            try {
                mediaRecorder.setAudioEncodingBitRate(128 * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 8, 32, 44.1, 48, 96
            try {
                mediaRecorder.setAudioSamplingRate((int) (8f * 1000f));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");
        String fileName = format.format(new Date());

        try {
            mediaRecorder.setOutputFile(context.getCacheDir() + "/" + fileName);
            Log.d(TAG, "mediaRecorder: setOutputFile");
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        try {
            mediaRecorder.prepare();
            Log.d(TAG, "mediaRecorder: prepare");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaRecorder.start();
            Log.d(TAG, "mediaRecorder: start");
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "mediaRecorder: ---------------------------------");


        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
                Log.d(TAG, "mediaRecorder: stop");
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            try {
                mediaRecorder.reset();
                Log.d(TAG, "mediaRecorder: reset");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                mediaRecorder.release();
                Log.d(TAG, "mediaRecorder: release");
            } catch (Exception e) {
                e.printStackTrace();
            }
            mediaRecorder = null;
        }
        openCamera();
    }

    private void audioRecord() {

        if (audioRecord == null) {
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    44100,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT));
            if (audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
                audioRecord.startRecording();
                Log.d(TAG, "audioRecord: start");
            }
        }

        Log.d(TAG, "audioRecord: ---------------------------------");

        if (audioRecord != null) {
            audioRecord.setRecordPositionUpdateListener(null);
            if (audioRecord.getRecordingState() == AudioRecord.STATE_INITIALIZED &&
                    audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_STOPPED) {
                try {
                    audioRecord.stop();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
            audioRecord.release();
            Log.d(TAG, "audioRecord: stop");
        }

//		int a = checkPermission(Manifest.permission.RECORD_AUDIO, Process.myPid(), Process.myUid());
//		int b = checkCallingPermission(Manifest.permission.RECORD_AUDIO);
//		int c = checkCallingOrSelfPermission(Manifest.permission.RECORD_AUDIO);
//		Log.d(TAG, "audioRecord: checkPermission=" + a);
//		Log.d(TAG, "audioRecord: checkCallingPermission=" + b);
//		Log.d(TAG, "audioRecord: checkCallingOrSelfPermission=" + c);

        if (!RootUtil.getManufacturer().equals("Meizu")) {
            openCamera();
        }
    }

    private void openCamera() {

        if (camera == null) {
            int i = 0;
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            final int cameraCount = Camera.getNumberOfCameras();
            for (int index = 0; index < cameraCount; index++) {
                Camera.getCameraInfo(index, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    i = index;
                    break;
                }
            }
            try {
                camera = Camera.open(i);
                Log.d(TAG, "openCamera: open");
                camera.startPreview();
                Log.d(TAG, "openCamera: startPreview");
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            try {
                camera.stopPreview();
                Log.d(TAG, "openCamera: stopPreview");
                camera.release();
                Log.d(TAG, "openCamera: release");
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            camera = null;
        }
        if (finishable != null)
            UITask.post(new Runnable() {
                @Override
                public void run() {
                    finishable.onFinish();
                }
            });
    }

    /**`
     * 检查录音权限
     */
    public static boolean checkAudioRecordPermission(Context context){
        boolean isPermission = checkPermission(context, "android.permission.RECORD_AUDIO");
        Log.i(TAG, "AudioRecordPermission-->" + isPermission);
        return isPermission;
    }

    /**
     * 检查相机权限
     */
    public static boolean checkCameraPermission(Context context){
        boolean isPermission = checkPermission(context, "android.permission.CAMERA");
        Log.i(TAG, "CameraPermission-->" + isPermission);
        return isPermission;
    }

    /**
     * 检查权限
     * @param context
     * @param permission
     * @return
     */
    public static boolean checkPermission(Context context, String permission){
        PackageManager pm = context.getPackageManager();
        boolean isPermission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission(permission, context.getPackageName()));
        return isPermission;
    }
}
