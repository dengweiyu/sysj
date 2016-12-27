package com.li.videoapplication.ui.service;

import android.content.Context;
import android.content.Intent;

import com.fmsysj.screeclibinvoke.data.preferences.Utils_Preferens;
import com.li.videoapplication.data.model.entity.AdvertisementDto;
import com.li.videoapplication.mvp.home.HomeContract;
import com.li.videoapplication.mvp.home.presenter.HomePresenter;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.network.RequestExecutor;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.BaseIntentService;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.utils.StringUtil;

/**
 * 服务：初始化数据
 */
public class AppStartService extends BaseIntentService {

    public static final String TAG = AppStartService.class.getSimpleName();
    public static final String ACTION = AppStartService.class.getName();
    private HomeContract.IHomePresenter homePresenter;

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
        homePresenter = HomePresenter.getInstance();
        userProfilePersonalInformation();
        indexIndex();
        indexChangeGuess();
        advertisementAdLocation204();
        advertisementAdImage204();
        // 迁移并删除2.0.7以前录屏设置
        Utils_Preferens.copyRecordingSetting();
    }

    /**
     * 初始化数据
     */
    private void refreshData() {
        // 删除数据库
        com.li.videoapplication.data.database.Utils.deleteDatabase();

        // 把/LuPingDaShi目录下的录屏/截屏复制到/sysj
        com.li.videoapplication.data.local.Utils.copyLuPingDaShiToSysj();

        // 初始化数据库
        com.li.videoapplication.data.database.Utils.scanRecToDatabase();
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

    private void indexChangeGuess() {
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
        if (NetUtil.isConnect()) {
            homePresenter.loadHomeData(1, true);
            String member_id = PreferencesHepler.getInstance().getMember_id();
            if (!StringUtil.isNull(member_id))
                homePresenter.unfinishedTask(member_id, true);
        }
    }

    /**
     * 广告位置列表
     */
    private void advertisementAdLocation204() {
        // 广告位置列表
        DataManager.advertisementAdLocation204Asnyc();
    }

    /**
     * 位置广告图集
     */
    private void advertisementAdImage204() {
        // 首页-热门游戏下方通栏
        homePresenter.adImage208(AdvertisementDto.ADVERTISEMENT_8, true);
        // 首页-第四副轮播
        DataManager.ADVERTISEMENT.advertisement_7();
    }
}
