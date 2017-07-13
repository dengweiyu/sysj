package com.li.videoapplication.component.application;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.baidu.push.example.BaiduPush;
import com.baidu.push.example.entity.BaiduEntity;
import com.happly.link.util.LogCat;
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.screenrecordlib.RecordingManager;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.cache.CacheManager;
import com.li.videoapplication.data.network.RequestExecutor;
import com.li.videoapplication.data.network.RequestService;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.BaseApplication;
import com.li.videoapplication.tools.AppExceptionHandler;
import com.li.videoapplication.tools.JPushHelper;
import com.li.videoapplication.utils.AppUtil;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

import org.xutils.x;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * 应用:主应用程序
 */
public class MainApplication extends BaseApplication {

    private static final boolean DEBUG = false;
    private static final String FEEDBACK_KEY = "23590443";

    private BaiduEntity mBaiduEntity;

    private boolean isSubmitChannelId = true;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //异常处理
        AppExceptionHandler.getInstance().init();

        //乐播debug
        LogCat.setNotDebug(true);
        // 初始化主进程
        if (getApplicationInfo().packageName.equals(AppUtil.getCurrentProcessName(getApplicationContext()))) {
            // 初始化融云
            if (getApplicationInfo().packageName.equals(AppUtil.getCurrentProcessName(getApplicationContext())) ||
                    "io.rong.push".equals(AppUtil.getCurrentProcessName(getApplicationContext()))) {
                RongIM.init(MainApplication.this);
            }
          //  new Handler().postDelayed(new Runnable() {
           //     @Override
          //      public void run() {

                    RequestExecutor.start(new Runnable() {
                        @Override
                        public void run() {
                            //降低优先级
                            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
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
                            DataManager.checkAndroidStatus(AppUtil.getVersionCode(getApplicationContext()),
                                    AnalyticsConfig.getChannel(getApplicationContext()));
                        }
                    });
            //    }
        //    }, 1500);
        }
        //只能同步启动
        x.Ext.init(MainApplication.this);
        x.Ext.setDebug(DEBUG);

        //feimo im sdk
        IMSdk.init(MainApplication.this);

        //Baidu push
        BaiduPush.getInstances().init(this, new BaiduPush.OnSucceed() {
            @Override
            public void succeed(BaiduEntity b) {
                mBaiduEntity = b;

            }
        });
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


    //所有接收到的消息、通知、状态都经由此处设置的监听器处理。包括私聊消息、讨论组消息、群组消息、聊天室消息以及各种状态。
    private final static RongIMClient.OnReceiveMessageListener sRongIMMessageListener = new RongIMClient.OnReceiveMessageListener() {
        @Override
        public boolean onReceived(io.rong.imlib.model.Message message, int i) {
            Log.e("onReceived",message.getExtra());
            return false;
        }
    };

    public BaiduEntity getBaiduEntity() {
        return mBaiduEntity;
    }

    public boolean isSubmitChannelId() {
        return isSubmitChannelId;
    }

    public void setSubmitChannelId(boolean submitChannelId) {
        isSubmitChannelId = submitChannelId;
    }
}
