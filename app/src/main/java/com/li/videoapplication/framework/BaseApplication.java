package com.li.videoapplication.framework;


import android.annotation.TargetApi;
import android.app.Application;
import android.os.Build;
import android.util.Log;

/**
 * 基本应用程序
 */
@Deprecated
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public abstract class BaseApplication extends Application {

    protected final String action = this.getClass().getName();

    protected final String tag = this.getClass().getSimpleName();

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(tag, "onCreate: ");
		AppManager.getInstance().setApplication(this);
	}
}
