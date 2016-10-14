package com.li.videoapplication.ui.application;

import android.os.Debug;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.cache.CacheManager;
import com.li.videoapplication.data.image.ImageLoaderHelper;
import com.li.videoapplication.data.local.StorageUtil;
import com.li.videoapplication.data.network.RequestExecutor;
import com.li.videoapplication.data.network.RequestService;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.BaseApplication;
import com.li.videoapplication.tools.JPushHelper;
import com.li.videoapplication.utils.AppUtil;
import com.umeng.analytics.MobclickAgent;

import org.xutils.x;

import io.rong.imkit.RongIM;

/**
 * 应用:主应用程序
 */
public class MainApplication extends BaseApplication {

    private static final boolean DEBUG = false;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(tag, "onCreate: processName=" + AppUtil.getCurrentProcessName(getApplicationContext()));

        if (DEBUG) Debug.startMethodTracing(StorageUtil.createMethodTracingName("MainApplication", "onCreate"));

        MultiDex.install(MainApplication.this);

        // 初始化主进程
        if (getApplicationInfo().packageName.equals(AppUtil.getCurrentProcessName(getApplicationContext()))) {

            RequestExecutor.start(new Runnable() {
                @Override
                public void run() {

                    ImageLoaderHelper.init(MainApplication.this);

                    x.Ext.init(MainApplication.this);
                    x.Ext.setDebug(DEBUG);

                    CacheManager.getInstance().initCache();

                    //友盟推送
                    // UPushHelper.initUPush(MainApplication.this);

                    // 极光推送
                    JPushHelper.initUPush(MainApplication.this, DEBUG);

                    if (PreferencesHepler.getInstance().isLogin()) {
                        // UPushHelper.setAlias(PreferencesHepler.getInstance().getMember_id());
                        JPushHelper.setAlias(PreferencesHepler.getInstance().getMember_id());
                    }

                    //友盟统计
                    MobclickAgent.setDebugMode(DEBUG);

                    //版本审核
                    DataManager.checkAndroidStatus(AppUtil.getVersionCode(getApplicationContext()));
                }
            });
        }

        // 初始化融云
        if (getApplicationInfo().packageName.equals(AppUtil.getCurrentProcessName(getApplicationContext())) ||
                "io.rong.push".equals(AppUtil.getCurrentProcessName(getApplicationContext()))) {

            RongIM.init(MainApplication.this);
        }

        if (DEBUG) Debug.stopMethodTracing();
    }

    @Override
    public void onLowMemory() {
        try {
            RequestService.stopRequestService();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}
