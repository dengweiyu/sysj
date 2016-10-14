package com.li.videoapplication.data.record;

import android.graphics.Bitmap;
import android.hardware.Camera;

public interface CameraListener {

    void onPictureTaken(byte[] data, Camera camera);

    void onPictureTaken(Bitmap bitmap);

    void onMinPictureTaken(Bitmap bitmap);

    void onRecorderUpdate(boolean isRecording, long seconds);

    void onRecorderFinish(Bitmap bitmap, String path);
}
