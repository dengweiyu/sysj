package com.fmsysj.zbqmcs.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class ExApplication {

	/**
	 * 是否停止计时服务
	 */
	public static boolean stopTimeService = false;

	/**
	 * 后台合成视频服务是否开启判断
	 * 
	 */
	public static boolean isCompositionVideo = false;
	/**
	 * 延迟关闭退出录屏大师
	 */
	public static boolean islaterCloaseApp = false;
	/** 记录浮窗窗口1的X坐标 ***/
	public static float moveX = -1;
	/** 记录浮窗窗口1的y坐标 ***/
	public static float moveY = -1;
	/**
	 * 悬浮窗宽度
	 */
	public static int tvWidth = 0;
	public static boolean firstExtandAnmin = true;
	/**
	 * 画中画是否已经关闭
	 */
	public static boolean floatCameraClose = true;

	/**
	 * 屏幕方向 横为true 竖为fals
	 */
	public static boolean mConfiguration = false;
	/**
	 * 是否暂停录制视频
	 * 
	 */
	public static boolean pauseRecVideo = false;
	/**
	 * 开始录屏时的时间毫秒数
	 */
	public static long recStartTime = 0;
	/**
	 * 视频清晰度
	 */
	public static String videoQuality = "-1";
	/**
	 * 视频清晰度_标清: 1<br/>
	 * 作为程序内部清晰度标示
	 */
	public static String SQuality = "1";
	/**
	 * 视频清晰度_超清: 0<br/>
	 * 作为程序内部清晰度标示
	 */
	public static String HQuality = "0";
	/**
	 * 是否直接跳转到视频管理页
	 */
	public static boolean isgotovideomange = false;

	/**
	 * 录屏时浮窗显示的时间
	 */
	public static String floatViewTime = "  0 :00";
	/**
	 * 5.0系统中resultCode返回码 -1表示开始录屏 0表示取消录屏
	 */
	public static int resultCode50 = -2;
	/**
	 * 状态栏高度
	 */
	public static int statusBarHeight = 0;
	/**
	 * 当前设备的分辨率宽度、高度
	 */
	public static int DEVW = 0;
	public static int DEVH = 0;
	/**
	 * 视频清晰度_超清<br/>
	 * 作为界面清晰度标示
	 */
	public static String HQualityVaule = "超清";
	/**
	 * 视频清晰度_标清:<br/>
	 * 作为界面清晰度标示
	 */
	public static String SQualityVaule = "标清";

	/** 是否已经计算过高度 */
	public static boolean ISCOUNT = false;
    /** 两个按钮所在区域的高度 */
	public static int moveHeight;
    /** 两个按钮所在区域的高度 */
	public static int height;

	public static int getAndroidSDKVersion() {
		int version = 1;
		try {
			version = android.os.Build.VERSION.SDK_INT;
		} catch (NumberFormatException e) {
			// Log.e(e.toString());
		}
		return version;
	}

	/**
	 * 检测是否安装某个应用
	 * 
	 * @param packageName
	 * @param context
	 * @return
	 */
	public static boolean isInstallApp(String packageName, Context context) {
		PackageInfo packageInfo;

		try {
			packageInfo = context.getPackageManager().getPackageInfo(
					packageName, 0);

		} catch (NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
		}
		if (packageInfo == null) {

			return false;
		} else {

			return true;
		}
	}

	// 将秒数转换HH:MM:SS
	public static String getTransformTime(String sec) {

		int videolength;
		int houes;
		int minute;
		int second;
		String time;

		videolength = Integer.valueOf(sec);
		houes = (int) (videolength / 3600);
		minute = (int) ((videolength - (houes * 3600)) / 60);
		second = (int) (videolength % 60);

		if (houes == 0) {// 如果没有小时数
			if (minute < 10) {// 分钟数小于10
				if (second < 10) {// 秒数小于10
					time = "0" + minute + ":" + "0" + second;
				} else {
					time = "0" + minute + ":" + second;
				}

			} else {// 分钟数大于10
				if (second < 10) {// 秒数小于10
					time = minute + ":" + "0" + second;
				} else {
					time = minute + ":" + second;
				}

			}
		} else {
			if (minute < 10) {// 分钟数小于10
				if (second < 10) {// 秒数小于10
					time = houes + ":" + "0" + minute + ":" + "0" + second;
				} else {
					time = houes + ":" + "0" + minute + ":" + second;
				}

			} else {// 分钟数大于10
				if (second < 10) {// 秒数小于10
					time = houes + ":" + minute + ":" + "0" + second;
				} else {
					time = houes + ":" + minute + ":" + second;
				}

			}
		}

		return time;

	}
}
