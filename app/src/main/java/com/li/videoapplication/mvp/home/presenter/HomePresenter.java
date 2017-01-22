package com.li.videoapplication.mvp.home.presenter;

import android.text.method.NumberKeyListener;
import android.util.Log;

import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.data.model.response.ChangeGuessEntity;
import com.li.videoapplication.data.model.response.UnfinishedTaskEntity;
import com.li.videoapplication.data.model.entity.AdvertisementDto;
import com.li.videoapplication.data.model.entity.HomeDto;
import com.li.videoapplication.data.network.RequestExecutor;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.mvp.OnLoadDataListener;
import com.li.videoapplication.mvp.home.HomeContract.IHomeModel;
import com.li.videoapplication.mvp.home.HomeContract.IHomePresenter;
import com.li.videoapplication.mvp.home.HomeContract.IHomeView;
import com.li.videoapplication.mvp.home.HomeContract.onloadHomeDataListener;
import com.li.videoapplication.mvp.home.model.HomeModel;
import com.li.videoapplication.framework.BaseHttpResult;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Presenter实现类: 首页
 */
public class HomePresenter implements IHomePresenter, onloadHomeDataListener {
    private static final String TAG = HomePresenter.class.getSimpleName();

    private IHomeView homeView;
    private IHomeModel homeModel;
    private static IHomePresenter homePresenter;

    private HomePresenter() {
        homeModel = HomeModel.getInstance();
    }

    public static synchronized IHomePresenter getInstance() {
        if (homePresenter == null) {
            homePresenter = new HomePresenter();
        }
        return homePresenter;
    }

    @Override
    public void setHomeView(IHomeView homeView) {
        this.homeView = homeView;
    }

    @Override
    public void loadHomeData(int page, boolean isLoad) {
        Log.d(TAG, "loadHomeData: " + isLoad);
        homeModel.loadHomeData(page, isLoad, this);
    }

    @Override
    public void unfinishedTask(String member_id, boolean update) {
        Log.d(TAG, "unfinishedTask: " + update);
        homeModel.unfinishedTask(member_id, update, this);
    }

    @Override
    public void changeGuess(String group_ids) {
        homeModel.changeGuess(group_ids, this);
    }

    @Override
    public void changeGuessSecond(String video_ids) {
        homeModel.changeGuessSecond(video_ids, this);
    }

    @Override
    public void adImage208(int localtion_id, boolean isLoad) {
        Log.d(TAG, "adImage208: id == " + localtion_id + " ," + isLoad);
        homeModel.adImage208(localtion_id, isLoad, this);
    }

    @Override
    public void adClick(long ad_id, int ad_click_state, String hardwarecode) {
        Log.d(TAG, "adClick: ad_id == " + ad_id + ", state == " + ad_click_state + ", hardwarecode == " + hardwarecode);
        homeModel.adClick(ad_id, ad_click_state, hardwarecode, this);
    }

    //加载首页成功，通知view更新界面
    @Override
    public void onLoadHomeSuccess(HomeDto data) {
        Log.d(TAG, "onLoadHomeSuccess: "+ data);
        homeView.refreshHomeData(data);
        homeView.hideProgress();
        PreferencesHepler.getInstance().saveHomeData(data);//保存首页json
    }

    //加载每日任务成功
    @Override
    public void onLoadUnFinishTaskSuccess(UnfinishedTaskEntity data) {
        homeView.refreshUnFinishTaskView(data);
    }

    //加载猜你喜欢（换一换）、猜你喜欢详情 成功
    @Override
    public void onLoadChangeGuessSuccess(ChangeGuessEntity data) {
        homeView.refreshChangeGuessView(data);
    }

    //加载广告成功
    @Override
    public void onLoadAdvertisementSuccess(AdvertisementDto data) {
        //不管是否 isResult都返回给view再判断
        switch (data.getData().get(0).getAd_location_id()) {
            case AdvertisementDto.ADVERTISEMENT_8://首页-热门游戏下方通栏
                homeView.refreshAdvertisementView(data);
                break;
            case AdvertisementDto.ADVERTISEMENT_6://启动海报
                if (data.isResult()) {//有广告
                    PreferencesHepler.getInstance().saveIndexLaunchImage(data);//保存json

                    String imageName = StringUtil.getFileNameWithExt(data.getData().get(0).getServer_pic_a());
                    String path = SYSJStorageUtil.getSysjDownload() + File.separator + imageName;
                    File file = new File(path);
                    Log.d(TAG, "Lunch Image AD file.exists() == " + file.exists());
                    if (!file.exists()) { //启动图没下载到本地
                        List<String> url = new ArrayList<>();
                        url.add(data.getData().get(0).getServer_pic_a());
                        homeModel.adImageDownload(url, new OnLoadDataListener<Boolean>() { //下载启动图
                            @Override
                            public void onSuccess(Boolean b) {
                                Log.d(TAG, "adImageDownload: " + b);
                            }

                            @Override
                            public void onFailure(Throwable e) {
                                Log.d(TAG, "adImageDownload: onFailure");
                            }
                        });
                    }
                } else {
                    if (PreferencesHepler.getInstance().getIndexLaunchImage() != null)
                        PreferencesHepler.getInstance().removeIndexLaunchImage();
                }
                break;
        }
    }

    //广告点击统计成功
    @Override
    public void onLoadAdClickSuccess(BaseHttpResult data) {
        Log.d(TAG, "AdClick: " + data.getMsg());
    }

    // FIXME:loadHomeData onFailure: java.lang.ClassCastException: com.li.videoapplication.data.model.entity.HomeDto cannot be cast to io.rx_cache.Reply
    @Override
    public void onFailure(Throwable e) {
        Log.d(TAG, "onFailure: " + e.toString());
    }
}
