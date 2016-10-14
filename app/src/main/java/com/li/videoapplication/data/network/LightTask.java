package com.li.videoapplication.data.network;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
/**
 * 功能：轻量级后台线程任务
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class LightTask {
	
	private static LightTask instance;

    private static LightTask getInstance() {
		if (instance == null) {
			synchronized (LightTask.class) {
				if (instance == null) {
					instance = new  LightTask();
				}
			}
		}
		return instance;
	}
	
	private static final String TAG = LightTask.class.getSimpleName();
	private String simpleName = this.getClass().getSimpleName();

	private String threadName = "Thread-" + simpleName;
	private HandlerThread thread;
	private Looper looper;
	private Handler handler;

	private LightTask() {
		super();

        thread = new HandlerThread(threadName, android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        looper = thread.getLooper();
        handler = new Handler(looper);
	}

    private final void _post(Runnable r) {
        handler.post(r);
    }

    private final void _postAtFrontOfQueue(Runnable r) {
        handler.postAtFrontOfQueue(r);
    }

    private final void _postAtTime(Runnable r, long uptimeMillis) {
        handler.postAtTime(r, uptimeMillis);
    }

    private final void _postAtTime(Runnable r, Object token, long uptimeMillis) {
        handler.postAtTime(r, token, uptimeMillis);
    }

    private final void _postDelayed(Runnable r, long delayMillis) {
        handler.postDelayed(r, delayMillis);
    }

    private final void _quit() {
        if (thread != null) {
            thread.quit();
            instance = null;
        }
    }

    private final void _quitSafely() {
        if (thread != null) {
            thread.quitSafely();
            instance = null;
        }
    }

    public synchronized final static void post(Runnable r) {
        getInstance()._post(r);
    }

    public synchronized final static void postAtFrontOfQueue(Runnable r) {
        getInstance()._postAtFrontOfQueue(r);
    }

    public synchronized final static void postAtTime(Runnable r, long uptimeMillis) {
        getInstance()._postAtTime(r, uptimeMillis);
    }

    public synchronized final static void postAtTime(Runnable r, Object token, long uptimeMillis) {
        getInstance()._postAtTime(r, token, uptimeMillis);
    }

    public synchronized final static void postDelayed(Runnable r, long delayMillis) {
        getInstance()._postDelayed(r, delayMillis);
    }

    public synchronized final static void quit() {
        getInstance()._quit();
    }

    public final void quitSafely() {
        getInstance()._quitSafely();
    }
}
