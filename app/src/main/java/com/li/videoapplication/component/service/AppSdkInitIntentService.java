package com.li.videoapplication.component.service;

import android.app.Application;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.screenrecordlib.RecordingManager;
import com.li.videoapplication.component.application.MainApplication;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.cache.CacheManager;
import com.li.videoapplication.data.download.DownLoadManager;
import com.li.videoapplication.data.network.RequestExecutor;
import com.li.videoapplication.data.network.RequestService;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.tools.JPushHelper;
import com.li.videoapplication.utils.AppUtil;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

import io.rong.imkit.RongIM;

/**
 * Created by liuwei on 2017/3/30 0030.
 */

public class AppSdkInitIntentService extends IntentService {
    private static final boolean DEBUG = false;
    private static final String FEEDBACK_KEY = "23590443";


    public AppSdkInitIntentService() {
        super("AppSdkInitIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Application app = AppManager.getInstance().getApplication();
        // 初始化融云
        if (getApplicationInfo().packageName.equals(AppUtil.getCurrentProcessName(getApplicationContext())) ||
                "io.rong.push".equals(AppUtil.getCurrentProcessName(getApplicationContext()))) {
            RongIM.init(app);
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Application app = AppManager.getInstance().getApplication();

        // 初始化下载器
        DownLoadManager.getInstance();
        // 网络请求服务
        RequestService.startRequestService();
        // 启动服务
        AppStartService.startAppStartService();

        CacheManager.getInstance().initCache();

        RecordingManager.getInstance().initialize(app);

        // 极光推送
        JPushHelper.initUPush(app, DEBUG);

        if (PreferencesHepler.getInstance().isLogin()) {
            JPushHelper.setAlias(PreferencesHepler.getInstance().getMember_id());
        }

        //友盟统计
        MobclickAgent.setDebugMode(DEBUG);

        //阿里百川反馈
        FeedbackAPI.init(app, FEEDBACK_KEY);

        //版本审核
        DataManager.checkAndroidStatus(AppUtil.getVersionCode(getApplicationContext()),
                AnalyticsConfig.getChannel(getApplicationContext()));

        //feimo im sdk
        IMSdk.init(app);

    }

}
