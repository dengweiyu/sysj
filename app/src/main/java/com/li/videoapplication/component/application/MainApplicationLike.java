package com.li.videoapplication.component.application;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Process;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.baidu.push.example.BaiduPush;
import com.baidu.push.example.entity.BaiduEntity;
import com.google.gson.Gson;
import com.happly.link.util.LogCat;
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.screenrecordlib.RecordingManager;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.cache.CacheManager;
import com.li.videoapplication.data.model.entity.BaiduMessageEntity;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.network.RequestExecutor;
import com.li.videoapplication.data.network.RequestService;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.BaseApplication;
import com.li.videoapplication.tools.AppExceptionHandler;
import com.li.videoapplication.tools.JPushHelper;
import com.li.videoapplication.tools.MyLogImp;
import com.li.videoapplication.tools.TinkerManager;
import com.li.videoapplication.utils.AppUtil;
import com.li.videoapplication.utils.StringUtil;
import com.meituan.android.walle.WalleChannelReader;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

import org.xutils.x;

import java.lang.reflect.Field;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * 应用:主应用程序
 */
@SuppressWarnings("unused")
@DefaultLifeCycle(application = "com.li.videoapplication.component.MainApplication",
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)
public class MainApplicationLike extends DefaultApplicationLike {

    private static final boolean DEBUG = false;
    private static final String FEEDBACK_KEY = "23590443";

    private static BaiduEntity mBaiduEntity;


    public MainApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag,
                               long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }


    @Override
    public void  onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        MultiDex.install(base);
        TinkerManager.setTinkerApplicationLike(this);

        TinkerManager.initFastCrashProtect();
        //should set before tinker is installed
        TinkerManager.setUpgradeRetryEnable(true);

        //optional set logIml, or you can use default debug log
        TinkerInstaller.setLogIml(new MyLogImp());

        //installTinker after load multiDex
        //or you can put com.tencent.tinker.** to main dex
        TinkerManager.installTinker(this);
        Tinker tinker = Tinker.with(getApplication());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        final Application application = getApplication();

        AppManager.getInstance().setApplication(application);

        //异常处理
        AppExceptionHandler.getInstance().init();
        //乐播debug
        LogCat.setNotDebug(true);

        // 初始化主进程
        if (getApplication().getApplicationInfo().packageName.equals(AppUtil.getCurrentProcessName(application.getApplicationContext()))) {
            // 初始化融云
            if (application.getApplicationInfo().packageName.equals(AppUtil.getCurrentProcessName(application.getApplicationContext())) ||
                    "io.rong.push".equals(AppUtil.getCurrentProcessName(application.getApplicationContext()))) {
                RongIM.init(application);
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

                            RecordingManager.getInstance().initialize(application);

                            // 极光推送
                            JPushHelper.initUPush(application, DEBUG);

                            if (PreferencesHepler.getInstance().isLogin()) {
                                JPushHelper.setAlias(PreferencesHepler.getInstance().getMember_id());
                            }


                            //友盟统计
                            MobclickAgent.setDebugMode(DEBUG);

                            //阿里百川反馈
                            FeedbackAPI.init(application, FEEDBACK_KEY);

                        }
                    }) ;
            //    }
        //    }, 1500);
        }
        //只能同步启动
        x.Ext.init(application);
        x.Ext.setDebug(DEBUG);


        //Baidu push
        BaiduPush.getInstances().init(application, new BaiduPush.OnSucceed() {
            @Override
            public void succeed(BaiduEntity b) {

                mBaiduEntity = b;
                Member member =  PreferencesHepler.getInstance().getUserProfilePersonalInformation();
                if (PreferencesHepler.getInstance().isLogin()){
                        if (mBaiduEntity != null && !StringUtil.isNull(mBaiduEntity.getChannelId())) {
                            DataManager.submitChannelId(member.getId(),mBaiduEntity.getChannelId());
                    }
                }
            }

            @Override
            public void customContent(String content) {
                try {
                    Gson gson = new Gson();
                    BaiduMessageEntity entity = gson.fromJson(content, BaiduMessageEntity.class);
                    if (entity != null && entity.getCustom_content() != null){
                        if ("training".equals(entity.getCustom_content().getType())){
                            //更新一下个人资料
                            String memberId = PreferencesHepler.getInstance().getMember_id();
                            if (!StringUtil.isNull(memberId)){
                                DataManager.userProfilePersonalInformation(memberId, memberId);
                            }
                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        getApplication().registerActivityLifecycleCallbacks(callback);
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

    public static BaiduEntity getBaiduEntity() {
        return mBaiduEntity;
    }

}
