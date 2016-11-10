package com.ifeimo.screenrecordlib;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 4.4录屏
 */
public class ScreenRecord44 {

	private static final String TAG = ScreenRecord44.class.getSimpleName();

	public void _startRecording() {
		Log.d(TAG, "_startRecording: ----------------------------------------------------------");
		startRecording();
	}

	public void _readStream() {
		Log.d(TAG, "readStream: ----------------------------------------------------------");
		readStream();
	}

	public void _readInputStream() {
		Log.d(TAG, "_readInputStream: ----------------------------------------------------------");
		readInputStream();
	}

	public void _readErrorStream() {
		Log.d(TAG, "_readErrorStream: ----------------------------------------------------------");
		readErrorStream();
	}

	public void _waitFor() {
		Log.d(TAG, "_waitFor: ----------------------------------------------------------");
		waitFor();
	}

	public void _stopRecording() {
		Log.d(TAG, "_stopRecording: ----------------------------------------------------------");
		stopRecording();
		RecordingManager.getInstance().onRecordingCompleted();
	}

	public void _pauseRecording() {
		Log.d(TAG, "_pauseRecording: ----------------------------------------------------------");
		pauseRecording();
	}

	public void _restartRecording() {
		Log.d(TAG, "_restartRecording: ----------------------------------------------------------");
		restartRecording();
	}

	private String path;
	private Configuration configuration;

	private Process process;
	private InputStream is;
	private InputStream es;
	private OutputStream os;

	private Handler handler;

	public ScreenRecord44(String path, Configuration configuration) {
		this.path = path;
		this.configuration = configuration;

		handler = new Handler(Looper.getMainLooper());

		Log.d(TAG, "ScreenRecord44: path=" + path);
		Log.d(TAG, "ScreenRecord44: configuration=" + configuration);
	}

	/**
	 * 开始录制（4.4）
	 */
	private void startRecording() {
		Log.d(TAG, "startRecording: ----------------------------------------------------------");
		Log.d(TAG, "startRecording: ");
		String corePath;
		String audio;
		int bitrate;
		int width, height;

		if (configuration.isLandscape()) {// 横屏
			corePath = StorageUtil.getFmOldCore();// 旧核心
			width = configuration.getHeight();// 640
			height = configuration.getWidth();// 360
		} else {// 竖屏
			corePath = StorageUtil.getFmNewCore();// 新核心
			width = configuration.getWidth();// 360
			height = configuration.getHeight();// 640
		}

		bitrate = configuration.getBitrate();
		if (configuration.isAudio())
			audio = "--audio";
		else
			audio = "";

		initialize("." + corePath +
				" --key_fmkj_yes_yes" +
				" --bit-rate " + bitrate +
				" " + audio +
				" --size " + width + "x" + height +
				" " + path);
	}

	/**
	 * 停止录制（4.4）
	 */
	private void stopRecording() {
		Log.d(TAG, "stopRecording: ----------------------------------------------------------");
		runCommand("fmkj_yes_haha_s");
	}

	/**
	 * 暂停录制（4.4）
	 */
	private void pauseRecording() {
		Log.d(TAG, "pauseRecording: ----------------------------------------------------------");
		runCommand("fmkj_yes_haha_p");

	}

	/**
	 * 继续录制（4.4）
	 */
	private void restartRecording() {
		Log.d(TAG, "restartRecording: ----------------------------------------------------------");
		runCommand("fmkj_yes_haha_r");
	}

	private void initialize(String executable) {
		Log.d(TAG, "initialize: ----------------------------------------------------------");
		Log.d(TAG, "initialize: executable=" + executable);
		try {
			process = Runtime.getRuntime().exec(new String[]{"su", "-c", executable});
		} catch (IOException e) {
			e.printStackTrace();
		}
		is = process.getInputStream();
		es = process.getErrorStream();
		os = process.getOutputStream();
		Log.d(TAG, "initialize: true");
	}

	private void waitFor() {
		Log.d(TAG, "waitFor: ----------------------------------------------------------");
		if (process != null) {
			try {
                int extValue = process.waitFor();
                Log.d(TAG, "waitFor: waitFor/extValue=" + extValue);
				destroy();
				kill();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
		}
	}

	private void destroy() {
		Log.d(TAG, "destroy: ----------------------------------------------------------");
		if (os != null)
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		if (is != null)
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	private void kill() {
		Log.d(TAG, "killProcess: ----------------------------------------------------------");
		ScreenRecord43.killProcess(process);
	}

	private void runCommand(String command) {
		Log.d(TAG, "runCommand: ----------------------------------------------------------");
		Log.d(TAG, "runCommand: " + command);
		String s = command + "\n";
		byte[] b = s.getBytes(Charset.defaultCharset());
		if (os != null && command != null) {
			try {
				os.write(b);
				os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
		}
	}

	private void readInputStream() {
		Log.d(TAG, "readInputStream: ----------------------------------------------------------");
		Log.d(TAG, "readInputStream: thread=" +  Thread.currentThread().getName());
		if (is != null)
			ScreenRecord43.readStream(is);
	}

	private void readErrorStream() {
		Log.d(TAG, "readErrorStream: ----------------------------------------------------------");
		Log.d(TAG, "readErrorStream: thread=" +  Thread.currentThread().getName());
		if (es != null)
			ScreenRecord43.readStream(es);
	}

	private void readStream() {
		Log.d(TAG, "readStream: ----------------------------------------------------------");
		Log.d(TAG, "readStream: thread=" +  Thread.currentThread().getName());
		if (is != null)
			ScreenRecord43.readStream(is);
		if (es != null)
			ScreenRecord43.readStream(es);
	}
}
