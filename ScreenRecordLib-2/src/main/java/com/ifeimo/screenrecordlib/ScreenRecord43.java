package com.ifeimo.screenrecordlib;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;

import com.stericson.RootTools.RootTools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 4.3以下录屏
 * @deprecated被弃用的
 */
public class ScreenRecord43 {

	private static final String TAG = ScreenRecord43.class.getSimpleName();

	public void _initialize() {
		Log.d(TAG, "_initialize: ----------------------------------------------------------");
		initialize();
	}

	public void _exec(Callback callback) {
		Log.d(TAG, "_initialize: ----------------------------------------------------------");
		exec(callback);
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

	public void _startRecording() {
		Log.d(TAG, "_startRecording: ----------------------------------------------------------");
		startRecording();
	}

	public void _stopRecording() {
		Log.d(TAG, "_stopRecording: ----------------------------------------------------------");
		stopRecording();
	}

	public interface Callback {

		void call();
	}

	private String corePath;
	private String path;
	private Configuration configuration;

	private Process process;
	private InputStream is;
	private InputStream es;
	private OutputStream os;
	private BufferedReader reader;

	private Handler handler;

	public ScreenRecord43(String path, Configuration configuration) {
		this.path = path;
		this.configuration = configuration;

		handler = new Handler(Looper.getMainLooper());

		Log.d(TAG, "ScreenRecord44: path=" + path);
		Log.d(TAG, "ScreenRecord44: configuration=" + configuration);
	}

	/**
	 * 开始录制（4.4以下）
	 */
	private void startRecording() {
		Log.d(TAG, "startRecording: ----------------------------------------------------------");
		Log.d(TAG, "startRecording: thread=" +  Thread.currentThread().getName());

		int width;
		int height;
		Context context = RecordingManager.getInstance().context();
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		if (configuration.getQuality() == Configuration.Quality.ULTRAHIGH) {
			width = (int) (metrics.widthPixels / 1.7);
			height = (int) (metrics.heightPixels / 1.7);
		} else if (configuration.getQuality() == Configuration.Quality.HIGH) {
			width = (int) (metrics.widthPixels / 1.7);
			height = (int) (metrics.heightPixels / 1.7);
		} else if (configuration.getQuality() == Configuration.Quality.STANDARD) {
			width = (int) (metrics.widthPixels / 1.4);
			height = (int) (metrics.heightPixels / 1.4);
		} else {
			width = (int) (metrics.widthPixels / 1.4);
			height = (int) (metrics.heightPixels / 1.4);
		}
		int rotation;
		if (configuration.isLandscape())
			rotation = -90;
		else
			rotation = 0;
		String audio;
		if (configuration.isAudio())
			audio = "m";
		else
			audio = "x";

		runCommand(path);
		runCommand(String.valueOf(rotation));// 0 90 180 270
		runCommand(audio);//MIC("m"), MUTE("x");
		runCommand(String.valueOf(width));
		runCommand(String.valueOf(height));
		runCommand("0");
		runCommand("0");
		runCommand(String.valueOf(15));// 15
		runCommand("CPU");// GPU CPU
		runCommand("BGRA");// BGRA RGBA
		runCommand(String.valueOf(15000000));// 1000000 5000000 10000000 15000000 30000000
		runCommand(String.valueOf(32000)); // 8000 16000 32000 48000 96000
		runCommand(String.valueOf(-2));// MPEG_4(-2), H264(2), MPEG_4_SP(3);
		runCommand("0");
	}

	/**
	 * 停止录制（4.3以下）
	 */
	private void stopRecording() {
		Log.d(TAG, "stopRecording: ----------------------------------------------------------");
		Log.d(TAG, "stopRecording: thread=" +  Thread.currentThread().getName());
		runCommand("stop");
		destroy();
		RecordingManager.getInstance().onRecordingCompleted();
	}

	private void initialize() {
		Log.d(TAG, "initialize: ----------------------------------------------------------");
		Log.d(TAG, "initialize: thread=" +  Thread.currentThread().getName());
		corePath = StorageUtil.getFfmpegV2sh();
		Log.d(TAG, "initialize: corePath=" + corePath);
		try {
			process = Runtime.getRuntime().exec(new String[]{"su", "-c", corePath});
		} catch (IOException e) {
			e.printStackTrace();
		}
		os = process.getOutputStream();
		is = process.getInputStream();
		es = process.getErrorStream();
	}

	private void waitFor() {
		Log.d(TAG, "waitFor: ----------------------------------------------------------");
		if (process != null) {
			try {
				int extValue = process.waitFor();
				Log.d(TAG, "waitFor: waitFor/extValue=" + extValue);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void destroy() {
		Log.d(TAG, "destroy: ----------------------------------------------------------");
		Log.d(TAG, "destroy: thread=" +  Thread.currentThread().getName());
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
		if (es != null)
			try {
				es.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	private void killProcess() {
		Log.d(TAG, "killProcess: ----------------------------------------------------------");
		killProcess(process);
	}

	public static void killProcess(Process process) {
		Log.d(TAG, "killProcess: ----------------------------------------------------------");
		if (process != null)
			try {
				process.destroy();
				Log.d(TAG, "killProcess: true");
			} catch (Exception e) {
				e.printStackTrace();
			}
		// 杀死底层
		Utils.command(StorageUtil.getBusybox() + " pkill -SIGINT fmOldCore");
		Utils.command(StorageUtil.getBusybox() + " pkill -SIGINT fmNewCore");
		Log.d(TAG, "killProcess: true");
	}

	private void runCommand(String command) {
		Log.d(TAG, "runCommand: ----------------------------------------------------------");
		Log.d(TAG, "runCommand: thread=" +  Thread.currentThread().getName());
		Log.d(TAG, "runCommand: " + command);
		if (command == null)
			return;
		String s = command + "\n";
		byte[] c = s.getBytes(Charset.defaultCharset());
		if (os != null) {
			try {
				os.write(c);
				os.flush();
				Log.d(TAG, "runCommand: true");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private void readInputStream() {
		Log.d(TAG, "readInputStream: ----------------------------------------------------------");
		Log.d(TAG, "readInputStream: thread=" +  Thread.currentThread().getName());
		if (is != null)
			readStream(is);
	}

	private void readErrorStream() {
		Log.d(TAG, "readErrorStream: ----------------------------------------------------------");
		Log.d(TAG, "readErrorStream: thread=" +  Thread.currentThread().getName());
		if (es != null)
			readStream(es);
	}

	private void readStream() {
		Log.d(TAG, "readStream: ----------------------------------------------------------");
		Log.d(TAG, "readStream: thread=" +  Thread.currentThread().getName());
		if (is != null)
			readStream(is);
		if (es != null)
			readStream(es);
	}

	public static void readStream(InputStream is) {
		Log.d(TAG, "readStream: ----------------------------------------------------------");
		if (is != null)
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(is), 4096);
				String line;
				int i = 0;
				while ((line = br.readLine()) != null) {
					if (0 != i)
						Log.d(TAG, "readStream: " + line);
					i++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	// -------------------------------------------------------------------------------------------

	private void exec(Callback callback) {
		Log.d(TAG, "exec: ----------------------------------------------------------");
		Log.d(TAG, "exec: thread=" +  Thread.currentThread().getName());
		String corePath = StorageUtil.getFfmpegV2sh();
		Log.d(TAG, "exec: corePath=" + corePath);
		try {
			process = Runtime.getRuntime().exec(new String[]{"su", "-c", corePath});
		} catch (IOException e) {
			e.printStackTrace();
		}
		os = process.getOutputStream();
		is = process.getInputStream();
		es = process.getErrorStream();
		InputStreamReader isr = new InputStreamReader(is);
		reader = new BufferedReader(isr);
		String a = null;
		try {
			a = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "exec: a=" + a);
		if (a != null && a.equals("ready")) {// 准备
			startRecording();
			String b = null;
			try {
				b = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.d(TAG, "exec: b=" + b);
			if (b != null && b.equals("configured")) {// 配置完成
				String c = "";
				try {
					c = reader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Log.d(TAG, "exec: c=" + c);
				while (true) {
					if (c != null && c.equals("recording")) {// 录屏中
						if (callback != null)
							callback.call();
						break;
					}
					try {
						c = reader.readLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					int extValue = process.waitFor();
					Log.d(TAG, "exec: extValue=" + extValue);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				kill();
			}
		} else {
			kill();
		}
	}

	private void kill() {
		Log.d(TAG, "kill: ----------------------------------------------------------");
		destroy();
		if (corePath != null)
			RootTools.killProcess(corePath);
		Log.d(TAG, "kill: true");
	}
}
