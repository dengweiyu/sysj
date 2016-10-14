package com.fmsysj.zbqmcs.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

/**
 * 小工具集合
 * 
 * @author WYX
 * 
 */
public class MyUtils {
	static Toast mToast;

	public static void Mylog(String content) {
		Log.i("mylog", content);
	}

	/**
	 * 自定义toast，防止多次调用一个toast时多次弹出问题
	 * 
	 * @param context
	 * @param text
	 *            toast内容
	 */
	public static void showToast(Context context, String text) {
		if (mToast == null) {
			mToast = Toast.makeText(context.getApplicationContext(), text,
					Toast.LENGTH_SHORT);

		} else {
			mToast.setText(text);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.show();
	}

    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;

	/**
	 * 获取当前网络类型
	 * 
	 * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
	 */
	public static int getNetworkType(Context context) {
		// 获取当前网络环境信息
		ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		// 对网络环境进行判断
		if (networkInfo != null) {
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				return NETTYPE_WIFI;
			} else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				return NETTYPE_CMNET;
			}
		}
		return 0;
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

	/**
	 * 去除字符串中的回车、换行、空格符
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {

		String dest = "";

		if (str != null) {

			Pattern p = Pattern.compile("\\s*|\t|\r|\n");

			Matcher m = p.matcher(str);

			dest = m.replaceAll("");

		}

		return dest;

	}
}
