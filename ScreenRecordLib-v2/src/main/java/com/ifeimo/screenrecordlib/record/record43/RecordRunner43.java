package com.ifeimo.screenrecordlib.record.record43;

import android.util.Log;

import com.ifeimo.screenrecordlib.constant.Configuration;
import com.ifeimo.screenrecordlib.util.StorageUtil;

public class RecordRunner43 implements RecordProcess43.OnStateChangeListener {

	public static final String TAG = RecordRunner43.class.getSimpleName();

	private String executable;
	public RecordProcess43 process;

	private OnReadyListener onReadyListener;

	public RecordRunner43() {
		super();
		executable = StorageUtil.getFfmpegV2sh();
	}

	public void destroy() {
		if ((this.process == null) || (this.process.isStopped())) {
			return;
		}
		if (this.process.isRecording()) {
			this.process.stopRecording();
		} else {
			this.process.destroy();
		}
	}

	public void initialize(OnReadyListener mOnReadyListener) {
		this.onReadyListener = mOnReadyListener;
		if ((this.process == null) || (this.process.isStopped())) {
			this.process = new RecordProcess43(this.executable, this);
			new Thread(this.process, "newThread-RecordRunner43").start();
		}
	}

	public void start(String path, Configuration configuration) {
		process.startRecording(path, configuration);
	}

	public void stop() {
		process.stopRecording();
	}

	public void onStateChange(RecordProcess43 process,
							  RecordProcess43.ProcessState oldState,
							  RecordProcess43.ProcessState newState, int paramInt,
							  float paramFloat) {
		if (process != this.process) {
			Log.w(TAG, "received state update from old process");
			return;
		}
		Log.w(TAG, "received new state " + newState);
		if (newState == RecordProcess43.ProcessState.READY
				&& null != onReadyListener) {
			onReadyListener.onReady();
		} else if (newState == RecordProcess43.ProcessState.FINISHED
				&& null != onReadyListener) {
			onReadyListener.onFinished();
		}
	}

	public interface OnReadyListener {
		void onReady();
		void onFinished();
	}
}