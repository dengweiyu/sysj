package com.fmsysj.zbqmcs.record;

/**
 * 4.4录制
 * @author lin
 * Create：2014-12
 */

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fmsysj.zbqmcs.activity.ScreenRecordActivity;
import com.fmsysj.zbqmcs.utils.ExApplication;
import com.fmsysj.zbqmcs.utils.RUtils;
import com.fmsysj.zbqmcs.utils.RecordVideo;
import com.fmsysj.zbqmcs.utils.RootUtils;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.database.VideoCaptureManager;
import com.li.videoapplication.data.database.VideoInfoEntity;
import com.li.videoapplication.data.local.FileUtil;
import com.li.videoapplication.data.local.StorageUtil;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.ui.toast.ToastHelper;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Recorder44 implements Runnable {

	// 语音文件保存路径
	public static String audioName = null;

	private static OutputStream stdin;
	private static InputStream stdout;
	private static Process process;

	private static Context mContext;
	static SharedPreferences sp;
	/** 是否录制声音 */
	public static boolean isRecordAudio = true;
	/**
	 * 核心版本
	 */
	static String coreVersions;
	/**
	 * /data/data/com.li.videoapplication/files
	 */
	static String fileDir = AppManager.getInstance().getContext().getFilesDir()
			.getPath();

	@Override
	public void run() {

		ScreenRecordActivity.runCommand("./system/bin/screenrecord "
				+ ScreenRecordActivity.path_dir + "/"
				+ new SimpleDateFormat("yyyy-MM-dd_HHmmsss").format(new Date())
				+ ".mp4");
	}

	/** 开始录制视频 */
	@SuppressLint("SimpleDateFormat")
	public final static void StartRecordVideo(final String resolutions,
			Context context) {
		// 根据当前系统时间命名视频文件
		final String videoFileName = StorageUtil.createRecName();
		mContext = context;
		ScreenRecordActivity.videofilename = videoFileName;
		sp = PreferenceManager.getDefaultSharedPreferences(context
				.getApplicationContext());
		newRecordCore(resolutions, videoFileName);

	}

	/**
	 * 新版录制核心方法
	 */
	public final static void newRecordCore(final String resolutions,
			final String videoFileName) {

		new Thread() {
			public void run() {

				// 1280*720 3000 000 21M/minView
				// 720*480 2000 000 15M/minView
				// 640*360 1000 000 10M/minView
				/**
				 * 根据分辨率录制视频 <br/>
				 * 流畅分辨率已于2.0.2版本废弃
				 * */
				if (resolutions.equals("流畅")) {
					// 如果是横屏录制按钮，采用旧核心
					if (sp.getBoolean("horizontalRecord", false)) {
						coreVersions = "fmOldCore";
						Settings.width = 640;
						Settings.height = 360;
					} else {
						coreVersions = "fmNewCore";
						// 根据手机屏幕方向设定分辨率
						if (ScreenRecordActivity.getConfiguration()) {
							Settings.width = 640;
							Settings.height = 360;
						} else {
							Settings.width = 360;
							Settings.height = 640;
						}
					}

					if (isRecordAudio) {// 录制声音
						StartRecord("." + fileDir + "/" + coreVersions
								+ " --key_fmkj_yes_yes" + " --bit-rate 1000000"
								+ " --audio" + " --size " + Settings.width
								+ "x" + Settings.height + " "
								+ ScreenRecordActivity.path_dir + File.separator
								+ videoFileName);
					} else {
						StartRecord("." + fileDir + "/" + coreVersions
								+ " --key_fmkj_yes_yes" + " --bit-rate 1000000"
								+ " --size " + Settings.width + "x"
								+ Settings.height + " " + ScreenRecordActivity.path_dir
								+ File.separator + videoFileName);
					}

				} else if (resolutions.equals(ExApplication.SQuality)) {
					// 如果是横屏录制按钮，采用旧核心
					if (sp.getBoolean("horizontalRecord", false)) {
						coreVersions = "fmOldCore";
						Settings.width = 768;
						Settings.height = 432;
					} else {
						coreVersions = "fmNewCore";
						if (ScreenRecordActivity.getConfiguration()) {
							Settings.width = 768;
							Settings.height = 432;
						} else {
							Settings.width = 432;
							Settings.height = 768;
						}
					}
					if (isRecordAudio) {// 录制声音
						StartRecord("." + fileDir + "/" + coreVersions
								+ " --key_fmkj_yes_yes" + " --bit-rate 1200000"
								+ " --audio" + " --size " + Settings.width
								+ "x" + Settings.height + " "
								+ ScreenRecordActivity.path_dir + File.separator
								+ videoFileName);
					} else {
						StartRecord("." + fileDir + "/" + coreVersions
								+ " --key_fmkj_yes_yes" + " --bit-rate 1200000"
								+ " --size " + Settings.width + "x"
								+ Settings.height + " " + ScreenRecordActivity.path_dir
								+ File.separator + videoFileName);
					}
				} else {// 超清

					// 如果是横屏录制按钮，采用旧核心
					if (sp.getBoolean("horizontalRecord", false)) {
						coreVersions = "fmOldCore";
						Settings.width = 1280;
						Settings.height = 720;
					} else {
						coreVersions = "fmNewCore";
						if (ScreenRecordActivity.getConfiguration()) {
							Settings.width = 1280;
							Settings.height = 720;
						} else {
							Settings.width = 720;
							Settings.height = 1280;
						}
					}
					if (isRecordAudio) {// 录制声音

						StartRecord("." + fileDir + "/" + coreVersions
								+ " --key_fmkj_yes_yes" + " --bit-rate 2000000"
								+ " --audio" + " --size " + Settings.width
								+ "x" + Settings.height + " "
								+ ScreenRecordActivity.path_dir + File.separator
								+ videoFileName);

					} else {
						StartRecord("." + fileDir + "/" + coreVersions
								+ " --key_fmkj_yes_yes" + " --bit-rate 2000000"
								+ " --size " + Settings.width + "x"
								+ Settings.height + " " + ScreenRecordActivity.path_dir
								+ File.separator + videoFileName);
					}

				}

			}
		}.start();
	}

	private static void runCommand(String command) {
		try {
			String str = command + "\n";
			stdin.write(str.getBytes());
			stdin.flush();
		} catch (Exception ex) {

			ex.printStackTrace();
		}
	}

	public static void StartRecord(String executable) {
		Runtime runtime = Runtime.getRuntime();
		try {
			process = runtime.exec(new String[] { "su", "-c", executable });

			stdin = process.getOutputStream();
			stdout = process.getInputStream();
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			;
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	// 暂停录制
	public final static void PauseRecordVideo() {
		runCommand("fmkj_yes_haha_p");

	}

	// 录制重启
	public final static void RestartRecordVideo() {

		runCommand("fmkj_yes_haha_r");
	}

	/** 停止录制视频 ，写命令行 */
	public final static void StopRecordVideo() {

		runCommand("fmkj_yes_haha_s");
		open_record_file_action();
	}

    public static char charAt(int index) {
        return audioName.charAt(index);
    }

    public static String copyValueOf(char[] data, int start, int length) {
        return String.copyValueOf(data, start, length);
    }

    public static String concat(String string) {
        return audioName.concat(string);
    }

    public static String valueOf(long value) {
        return String.valueOf(value);
    }

    public static String substring(int start) {
        return audioName.substring(start);
    }

    public static String substring(int start, int end) {
        return audioName.substring(start, end);
    }

    public static String valueOf(char value) {
        return String.valueOf(value);
    }

    public static byte[] getBytes() {
        return audioName.getBytes();
    }

    public static int compareToIgnoreCase(String string) {
        return audioName.compareToIgnoreCase(string);
    }

    public static String valueOf(int value) {
        return String.valueOf(value);
    }

    public static int length() {
        return audioName.length();
    }

    public static boolean endsWith(String suffix) {
        return audioName.endsWith(suffix);
    }

    public static String valueOf(Object value) {
        return String.valueOf(value);
    }

    public static boolean contentEquals(CharSequence cs) {
        return audioName.contentEquals(cs);
    }

    public static int indexOf(int c, int start) {
        return audioName.indexOf(c, start);
    }

    public static int codePointAt(int index) {
        return audioName.codePointAt(index);
    }

    public static int compareTo(String string) {
        return audioName.compareTo(string);
    }

    public static int lastIndexOf(String subString, int start) {
        return audioName.lastIndexOf(subString, start);
    }

    public static String format(Locale locale, String format, Object... args) {
        return String.format(locale, format, args);
    }

    public static String toLowerCase() {
        return audioName.toLowerCase();
    }

    public static boolean isEmpty() {
        return audioName.isEmpty();
    }

    public static String[] split(String regularExpression, int limit) {
        return audioName.split(regularExpression, limit);
    }

    public static int indexOf(String string) {
        return audioName.indexOf(string);
    }

    public static int indexOf(String subString, int start) {
        return audioName.indexOf(subString, start);
    }

    public static int offsetByCodePoints(int index, int codePointOffset) {
        return audioName.offsetByCodePoints(index, codePointOffset);
    }

    public static int codePointCount(int start, int end) {
        return audioName.codePointCount(start, end);
    }

    public static int lastIndexOf(int c) {
        return audioName.lastIndexOf(c);
    }

    public static boolean startsWith(String prefix, int start) {
        return audioName.startsWith(prefix, start);
    }

    public static char[] toCharArray() {
        return audioName.toCharArray();
    }

    public static boolean contains(CharSequence cs) {
        return audioName.contains(cs);
    }

    public static String valueOf(char[] data) {
        return String.valueOf(data);
    }

    public static int indexOf(int c) {
        return audioName.indexOf(c);
    }

    public static boolean equalsIgnoreCase(String string) {
        return audioName.equalsIgnoreCase(string);
    }

    public static String replace(char oldChar, char newChar) {
        return audioName.replace(oldChar, newChar);
    }

    public static String[] split(String regularExpression) {
        return audioName.split(regularExpression);
    }

    public static String replaceFirst(String regularExpression, String replacement) {
        return audioName.replaceFirst(regularExpression, replacement);
    }

    public static String copyValueOf(char[] data) {
        return String.copyValueOf(data);
    }

    public static boolean contentEquals(StringBuffer sb) {
        return audioName.contentEquals(sb);
    }

    public static String valueOf(boolean value) {
        return String.valueOf(value);
    }

    public static boolean matches(String regularExpression) {
        return audioName.matches(regularExpression);
    }

    public static String format(String format, Object... args) {
        return String.format(format, args);
    }

    public static String trim() {
        return audioName.trim();
    }

    public static CharSequence subSequence(int start, int end) {
        return audioName.subSequence(start, end);
    }

    public static String replace(CharSequence target, CharSequence replacement) {
        return audioName.replace(target, replacement);
    }

    public static int codePointBefore(int index) {
        return audioName.codePointBefore(index);
    }

    public static int lastIndexOf(int c, int start) {
        return audioName.lastIndexOf(c, start);
    }

    public static byte[] getBytes(Charset charset) {
        return audioName.getBytes(charset);
    }

    public static String intern() {
        return audioName.intern();
    }

    public static boolean regionMatches(int thisStart, String string, int start, int length) {
        return audioName.regionMatches(thisStart, string, start, length);
    }

    public static String toUpperCase() {
        return audioName.toUpperCase();
    }

    public static String toLowerCase(Locale locale) {
        return audioName.toLowerCase(locale);
    }

    public static String toUpperCase(Locale locale) {
        return audioName.toUpperCase(locale);
    }

    public static int lastIndexOf(String string) {
        return audioName.lastIndexOf(string);
    }

    public static boolean regionMatches(boolean ignoreCase, int thisStart, String string, int start, int length) {
        return audioName.regionMatches(ignoreCase, thisStart, string, start, length);
    }

    public static String replaceAll(String regularExpression, String replacement) {
        return audioName.replaceAll(regularExpression, replacement);
    }

    public static void getChars(int start, int end, char[] buffer, int index) {
        audioName.getChars(start, end, buffer, index);
    }

    public static byte[] getBytes(String charsetName) throws UnsupportedEncodingException {
        return audioName.getBytes(charsetName);
    }

    @Deprecated
    public static void getBytes(int start, int end, byte[] data, int index) {
        audioName.getBytes(start, end, data, index);
    }

    public static String valueOf(double value) {
        return String.valueOf(value);
    }

    public static boolean startsWith(String prefix) {
        return audioName.startsWith(prefix);
    }

    public static String valueOf(char[] data, int start, int length) {
        return String.valueOf(data, start, length);
    }

    public static String valueOf(float value) {
        return String.valueOf(value);
    }

    /**
	 * 4.4录制结果通知
	 */
	// TODO
	@SuppressLint("SdCardPath")
	private final static void open_record_file_action() {
		// 设置录屏的视频信息
		VideoInfoEntity videoInfo = new VideoInfoEntity();
		videoInfo.setPath(ScreenRecordActivity.path_dir);
		videoInfo.setDisplayName(ScreenRecordActivity.videofilename);
		videoInfo.setTime(ScreenRecordActivity.videolong);

		int fileSize = (int) FileUtil.getFileSize(new File(videoInfo.getPath() + File.separator + videoInfo.getDisplayName()));

		File file = new File(videoInfo.getPath() + File.separator + videoInfo.getDisplayName());
		// 录制成功通知
		if (fileSize > 512) {
			// 对话框通知
            ToastHelper.s(RUtils.getRString("fm_record_succeed_and_toast_notif"));

			// 将视频路径和视频名称填入数据库
            String fileName = file.getName().split("\\.mp4")[0];
            VideoCaptureManager.save(file.getPath(),
                    VideoCaptureEntity.VIDEO_SOURCE_REC,
                    VideoCaptureEntity.VIDEO_STATION_LOCAL);
            RecordVideo.nofity(file.getPath(), mContext);
			// 判断是否跳转到视频管理页
			Boolean isgotoVideoManage = PreferenceManager
					.getDefaultSharedPreferences(mContext).getBoolean(
							"isGotoVideoManage", true);
			if (isgotoVideoManage) {

				ExApplication.isgotovideomange = true;

				if (isApplicationBroughtToBackground(mContext)) {
					// 跳转到视频管理页
                    ScreenRecordActivity.showNewTask();
				}
			}

		}
		// 录制失败通知
		else {

			if (RootUtils.appRoot1()) {// 判断是因为ROOT导致合成失败还是其他原因

				// "杀死底层");
				ScreenRecordActivity.runCommand(fileDir
						+ "/busybox pkill -SIGINT fmOldCore");
				ScreenRecordActivity.runCommand(fileDir
                        + "/busybox pkill -SIGINT fmNewCore");

				RecordVideo.RecordStateNofity("非常抱歉，录屏失败，请退出录屏大师再试", mContext);
				// 吐司通知录制失败
                ToastHelper.s("非常抱歉，录屏失败，请退出录屏大师再试");
			} else {

				RecordVideo.RecordStateNofity(
						RUtils.getRString("fm_record_fail_and_toast_notif"),
						mContext);
				// 吐司通知录制失败
                ToastHelper.s(RUtils.getRString("fm_record_fail_and_toast_notif"));
			}

		}
	}

	/**
	 * 判断录屏大师是否处于后台
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isApplicationBroughtToBackground(final Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				// 后台
				return true;
			}
		}
		// 前台
		return false;

	}
}