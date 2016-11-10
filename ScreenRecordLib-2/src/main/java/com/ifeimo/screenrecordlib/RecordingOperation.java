package com.ifeimo.screenrecordlib;

public interface RecordingOperation {

    void startScreenRecord(String path);

    void startScreenRecord(String path, Configuration configuration);

    void stopScreenRecord();

    void pauseScreenRecord();

    void resumeScreenRecord();

    void startScreenCapture(String path);
}
