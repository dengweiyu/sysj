package com.li.videoapplication.ui.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.network.RequestExecutor;
import com.li.videoapplication.data.preferences.Constants;
import com.li.videoapplication.data.preferences.NormalPreferences;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.BaseIntentService;

/**
 * 服务：初始化数据
 */
public class AppStartService extends BaseIntentService {

    public static final String TAG = AppStartService.class.getSimpleName();
    public static final String ACTION = AppStartService.class.getName();

    /**
     * 启动服务
     */
    public synchronized final static void startAppStartService() {
        Context context = AppManager.getInstance().getContext();
        Intent intent = new Intent(context, AppStartService.class);
        context.startService(intent);
    }

    public AppStartService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        RequestExecutor.start(new Runnable() {
            @Override
            public void run() {
                refreshData();
            }
        });

        indexLaunchImage();
        userProfilePersonalInformation();
        indexIndex();
        indexChangeGuess();
        advertisementAdLocation204();
        advertisementAdImage204();
    }

    /**
     * 初始化数据
     */
    private void refreshData() {
        Log.d(TAG, "refreshData: ");

        // 删除数据库
        com.li.videoapplication.data.database.Utils.deleteDatabase();

        // 把/LuPingDaShi目录下的录屏/截屏复制到/sysj
        com.li.videoapplication.data.local.Utils.copyLuPingDaShiToSysj();

        // 初始化数据库
        com.li.videoapplication.data.database.Utils.scanRecToDatabase();
    }

    /**
     * 启动图片广告
     */
    private void indexLaunchImage() {
        Log.d(tag, "indexLaunchImage: ");
        final int changeTime = NormalPreferences.getInstance().getInt(Constants.INDEX_LAUNCH_IMAHE_CHANGETIME);
        Log.d(tag, "changeTime=" + changeTime);
        // 启动图片广告
        DataManager.indexLaunchImageAsync(changeTime);
    }

    /**
     * 用户资料
     */
    private void userProfilePersonalInformation() {
        if (PreferencesHepler.getInstance().isLogin()) {
            // 用户资料
            DataManager.userProfilePersonalInformationAsync(PreferencesHepler.getInstance().getMember_id(),
                    PreferencesHepler.getInstance().getMember_id());
        }
    }

    private void indexChangeGuess(){
        if (PreferencesHepler.getInstance().canVideoIdsTime()) {// 已过期
            // 首页猜你喜歡
            DataManager.indexChangeGuess(PreferencesHepler.getInstance().getGroupIds2());
        }
    }

    /**
     * 首页統一接口
     */
    private void indexIndex() {
        // 首页
        DataManager.indexIndex204Async(1);
    }

    /**
     * 广告位置列表
     */
    private void advertisementAdLocation204() {
        Log.d(TAG, "advertisementAdLocation204: ");
        // 广告位置列表
        DataManager.advertisementAdLocation204Asnyc();
    }

    /**
     * 位置广告图集
     */
    private void advertisementAdImage204() {
        // 首页-热门游戏下方通栏
        DataManager.ADVERTISEMENT.advertisement_8();
        // 首页-第四副轮播
        DataManager.ADVERTISEMENT.advertisement_7();
    }
}
