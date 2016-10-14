package com.fmsysj.zbqmcs.record;

/**
 * 录制
 * @author lin
 * Create：2014-12
 */
import android.util.Log;

public class NativeProcessRunner implements
		RecorderProcess.OnStateChangeListener {
	private static final String TAG = "RecorderProcess";
	private String executable;
	RecorderProcess process;

	private OnReadyListener mOnReadyListener;

	public void setOnReadyListener(OnReadyListener mOnReadyListener) {
		this.mOnReadyListener = mOnReadyListener;
	}

	public NativeProcessRunner() {
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

	public void initialize() {
		if ((this.process == null) || (this.process.isStopped())) {
			this.process = new RecorderProcess(this.executable, this);
			new Thread(this.process).start();
		}
	}

	public void onStateChange(RecorderProcess process,
			RecorderProcess.ProcessState oldState,
			RecorderProcess.ProcessState newState, int paramInt,
			float paramFloat) {
		if (process != this.process) {
			Log.w(TAG, "received state update from old process");
			return;
		}
		Log.w(TAG, "received new state " + newState);
		if (newState == RecorderProcess.ProcessState.READY
				&& null != mOnReadyListener) {
			mOnReadyListener.onReady();
		} else if (newState == RecorderProcess.ProcessState.FINISHED
				&& null != mOnReadyListener) {
			mOnReadyListener.onFinished();
		}
	}

	public void setExecutable(String paramString) {
		this.executable = paramString;
	}

	public void start(String outFile) {
		this.process.startRecording(outFile);
	}

	public void stop() {
		this.process.stopRecording();
	}

	public static interface OnReadyListener {
		void onReady();

		void onFinished();
	}
}