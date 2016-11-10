package com.ifeimo.screenrecordlib;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 4.4及4.4以下录屏
 */
public class ScreenCapture44 {

	private static final String TAG = ScreenCapture44.class.getSimpleName();

	public void _startCapture() {
		Log.d(TAG, "_startCapture: ----------------------------------------------------------");
		startCapture();
	}

	private String path;

	private Handler handler;

	public ScreenCapture44(String path) {
		this.path = path;
		handler = new Handler(Looper.getMainLooper());
	}

	/**
	 * 截屏（4.4以上，5.0以下）
	 */
	public void startCapture() {
		Log.d(TAG, "startCapture: ----------------------------------------------------------");
		runCommand("screencap -p " + path);
		RecordingManager.getInstance().onCapturingCompleted();
	}

	private void runCommand(String command) {
		Log.d(TAG, "command=" + command);
		Process process = null;
		DataOutputStream dos = null;
		try {
			process = Runtime.getRuntime().exec("su");
			dos = new DataOutputStream(process.getOutputStream());
			dos.writeBytes(command + "\n");
			dos.writeBytes("exit\n");
			dos.flush();
			process.waitFor();
			DataInputStream dis = new DataInputStream(process.getInputStream());
			DataInputStream des = new DataInputStream(process.getErrorStream());
			while (dis.available() > 0)
				Log.d(TAG, "dis: " + dis.readLine() + "\n");
			while (des.available() > 0)
				Log.d(TAG, "des: " + des.readLine() + "\n");
			Log.d(TAG, "exitValue: " + process.exitValue());
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, "exception: " + e);
		} finally {
			if (dos != null)
				try {
					dos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (process != null)
				try {
					process.destroy();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}
}
