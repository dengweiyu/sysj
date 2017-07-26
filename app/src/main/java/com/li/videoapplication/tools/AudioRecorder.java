package com.li.videoapplication.tools;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

public class AudioRecorder {

    public static final String TAG  = AudioRecorder.class.getSimpleName();

    // ---------------------------------------------------------------------------------

    private MediaRecorder mediaRecorder;
    /**
     * 开始录音
     */
    public void startAudioRecord(String path) {
        stopAudioRecord();
        Log.d(TAG, "----------------------------------[startAudioRecord]----------------------------------");
        Log.d(TAG, "path=" + path);
        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setAudioChannels(2);

            // 128, 192, 256, 320
            try {
                mediaRecorder.setAudioEncodingBitRate(256 * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 8, 32, 44.1, 48, 96
            try {
                mediaRecorder.setAudioSamplingRate((int) (96f * 1000f));
            } catch (Exception e) {
                e.printStackTrace();
            }

    /*        CamcorderProfile camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
            mediaRecorder.setProfile(camcorderProfile);*/
            try {
                mediaRecorder.setOutputFile(path);
                Log.d(TAG, "startAudioRecord: 1");
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            try {
                mediaRecorder.prepare();
                Log.d(TAG, "startAudioRecord: 2");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mediaRecorder.start();
                Log.d(TAG, "startAudioRecord: 3");
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "startAudioRecord: true");
        }
    }

    /**
     * 停止录音
     */
    public void stopAudioRecord() {
        Log.d(TAG, "----------------------------------[stopAudioRecord]----------------------------------");
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            try {
                mediaRecorder.reset();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                mediaRecorder.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mediaRecorder = null;
            Log.d(TAG, "stopAudioRecord: true");
        }
    }

    /**
     * 是否在录音
     */
    public boolean isAudioRecord() {
        Log.d(TAG, "----------------------------------[isAudioRecord]----------------------------------");
        if (mediaRecorder != null) {
            return true;
        } else {
            return false;
        }
    }
}
