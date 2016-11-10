package com.fmsysj.screeclibinvoke.data.observe;

import android.util.Log;

import com.fmsysj.screeclibinvoke.data.observe.listener.FrontCameraObservable;
import com.fmsysj.screeclibinvoke.data.observe.listener.Recording2Observable;
import com.fmsysj.screeclibinvoke.data.observe.listener.RecordingObservable;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理：观察者
 */
public class ObserveManager {

    private static final String TAG = ObserveManager.class.getSimpleName();

    private static ObserveManager instance;

    public static ObserveManager getInstance() {
        if (instance == null)
            synchronized (ObserveManager.class) {
                if (instance == null)
                    instance = new ObserveManager();
            }
        return instance;
    }

    // -------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------

    private List<RecordingObservable> recordings = new ArrayList<>();

    public void addRecordingObservable(RecordingObservable listener) {
        if (!recordings.contains(listener)) {
            recordings.add(listener);
        }
        printObjects(recordings);
    }

    public void removeRecordingObservable(RecordingObservable listener) {
        if (recordings.contains(listener)) {
            recordings.remove(listener);
        }
        printObjects(recordings);
    }

    /**
     * 更新：发布录屏事件（录屏中）
     */
    public void notifyRecordingObservable() {
        for (RecordingObservable listener : recordings) {
            listener.onRecording();
        }
    }

    // -------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------

    private List<Recording2Observable> recordings2 = new ArrayList<>();

    public void addRecording2Observable(Recording2Observable listener) {
        if (!recordings2.contains(listener)) {
            recordings2.add(listener);
        }
        printObjects(recordings2);
    }

    public void removeRecording2Observable(Recording2Observable listener) {
        if (recordings2.contains(listener)) {
            recordings2.remove(listener);
        }
        printObjects(recordings2);
    }

    /**
     * 更新：录屏（开始，停止，暂停，继续）
     */
    public void notifyRecording2Observable() {
        for (Recording2Observable listener : recordings2) {
            listener.onRecording2();
        }
    }

    // -------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------

    private List<FrontCameraObservable> frontCameras = new ArrayList<>();

    public void addFrontCameraObservable(FrontCameraObservable listener) {
        if (!frontCameras.contains(listener)) {
            frontCameras.add(listener);
        }
        printObjects(frontCameras);
    }

    public void removeFrontCameraObservable(FrontCameraObservable listener) {
        if (frontCameras.contains(listener)) {
            frontCameras.remove(listener);
        }
        printObjects(frontCameras);
    }

    /**
     * 更新：前置摄像头
     */
    public void notifyFrontCameraObservable() {
        for (FrontCameraObservable listener : frontCameras) {
            listener.onCamera();
        }
    }

    // -------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------

    public void printObjects(List list) {
        Log.d(TAG, "printObjects: // ----------------------------------");
        if (list !=  null && list.size() > 0) {
            String name = "listener";
            if (list.get(0) instanceof RecordingObservable) {
                name = "RecordingObservable";
            } else if (list.get(0) instanceof Recording2Observable) {
                name = "Recording2Observable";
            } else if (list.get(0) instanceof FrontCameraObservable) {
                name = "FrontCameraObservable";
            }
            for (Object listener : list) {
                if (listener != null) {
                    Log.d(TAG, "printObjects: " + name + "=" + listener.getClass().getSimpleName());
                }
            }
        }
    }
}
