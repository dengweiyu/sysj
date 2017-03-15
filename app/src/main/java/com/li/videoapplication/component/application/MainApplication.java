package com.li.videoapplication.component.application;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.happly.link.util.LogCat;
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.screenrecordlib.RecordingManager;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.cache.CacheManager;
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
    private static final String FEEDBACK_KEY = "23590443";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //乐播debug
        LogCat.setNotDebug(true);

        // 初始化主进程
        if (getApplicationInfo().packageName.equals(AppUtil.getCurrentProcessName(getApplicationContext()))) {

            RequestExecutor.start(new Runnable() {
                @Override
                public void run() {

                    x.Ext.init(MainApplication.this);
                    x.Ext.setDebug(DEBUG);

                    CacheManager.getInstance().initCache();

                    RecordingManager.getInstance().initialize(MainApplication.this);

                    // 极光推送
                    JPushHelper.initUPush(MainApplication.this, DEBUG);

                    if (PreferencesHepler.getInstance().isLogin()) {
                        JPushHelper.setAlias(PreferencesHepler.getInstance().getMember_id());
                    }

                    //友盟统计
                    MobclickAgent.setDebugMode(DEBUG);

                    //阿里百川反馈
                    FeedbackAPI.init(MainApplication.this, FEEDBACK_KEY);

                    //版本审核
                    DataManager.checkAndroidStatus(AppUtil.getVersionCode(getApplicationContext()));

                    //feimo im sdk
                    IMSdk.init(MainApplication.this);
                }
            });
        }

        // 初始化融云
        if (getApplicationInfo().packageName.equals(AppUtil.getCurrentProcessName(getApplicationContext())) ||
                "io.rong.push".equals(AppUtil.getCurrentProcessName(getApplicationContext()))) {

            RongIM.init(MainApplication.this);
        }
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
