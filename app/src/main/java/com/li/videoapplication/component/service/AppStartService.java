package com.li.videoapplication.component.service;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.baidu.push.example.BaiduPush;
import com.fmsysj.screeclibinvoke.data.preferences.Utils_Preferens;
import com.ifeimo.im.framwork.IMSdk;
import com.li.videoapplication.data.EventManager;
import com.li.videoapplication.data.HttpManager;
import com.li.videoapplication.data.database.FileDownloaderEntity;
import com.li.videoapplication.data.database.FileDownloaderManager;
import com.li.videoapplication.data.download.DownLoadManager;
import com.li.videoapplication.data.local.FileUtil;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.event.LogoutEvent;
import com.li.videoapplication.data.model.response.AdvertisementDto;
import com.li.videoapplication.data.model.entity.Download;
import com.li.videoapplication.data.model.response.GetDownloadAppEntity;
import com.li.videoapplication.data.model.response.GetDownloadOtherEntity;
import com.li.videoapplication.data.model.response.LoginEntity;
import com.li.videoapplication.data.model.response.SubmitChannelIdEntity;
import com.li.videoapplication.data.model.response.Token;
import com.li.videoapplication.framework.AppAccount;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.impl.SimpleHeadLineObservable;
import com.li.videoapplication.mvp.home.HomeContract;
import com.li.videoapplication.mvp.home.presenter.HomePresenter;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.network.RequestExecutor;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.BaseIntentService;
import com.li.videoapplication.tools.JSONHelper;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.URLUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.jpush.android.service.DaemonService;
import rx.Observer;

/**
 * 服务：初始化数据
 */
public class AppStartService extends BaseIntentService{

    public static final String TAG = AppStartService.class.getSimpleName();
    public static final String ACTION = AppStartService.class.getName();
    private HomeContract.IHomePresenter homePresenter;

    private static Handler mTokenHandler;

    //
    Runnable mRefreshTokenTask = new Runnable() {
        @Override
        public void run() {
            if (PreferencesHepler.getInstance().isLogin()) {
                Member member = PreferencesHepler.getInstance().getUserProfilePersonalInformation();
                //无token
                if (member.getSysj_token() == null) {
                    //退出当前登录
                    AppAccount.logout();
                }
            }
            if (mTokenHandler != null) {
                mTokenHandler.removeCallbacks(this);
                mTokenHandler = null;
            }
        }
    };


    /**
     * 启动服务
     */
    public static void startAppStartService() {
        Context context = AppManager.getInstance().getContext();
        Intent intent = new Intent(context, AppStartService.class);
        try {
            context.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AppStartService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //检查Token是否存在  针对版本更新的用户
        if (PreferencesHepler.getInstance().isLogin()){
            mTokenHandler = new Handler();
            mTokenHandler.post(mRefreshTokenTask);
        }

        //
        BaiduPush.getInstances().onCreate(getApplicationContext());

        //feimo im sdk
        IMSdk.init(getApplication());

        //IM推送
        IMSdk.setHeadLineMeesageListener(new SimpleHeadLineObservable(this));
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
    //    indexIndex();
        userProfilePersonalInformation();
        indexChangeGuess();
//        advertisementAdLocation204();
        if (AppConstant.SHOW_DOWNLOAD_AD)//普通渠道，加载广告
        advertisementAdImage();
        // 迁移并删除2.0.7以前录屏设置
        Utils_Preferens.copyRecordingSetting();
        statisticalOpenApp();

        // 飞磨下载应用信息
        feimo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    /**
     * 统计被邀请用户打开app的次数
     */
    private void statisticalOpenApp() {
        if (PreferencesHepler.getInstance().isLogin()) {
            DataManager.statisticalOpenAppAsync(PreferencesHepler.getInstance().getMember_id());
        }
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
            if (!StringUtil.isNull(member_id)) {
                homePresenter.unfinishedTask(member_id, true);
            }
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
    private void advertisementAdImage() {
        //处理启动页广告
        handleLaunchAD();
//        homePresenter.adverImage(AdvertisementDto.ADVERTISEMENT_6);
        // 首页-热门游戏下方通栏
        homePresenter.adImage208(AdvertisementDto.ADVERTISEMENT_8, true);
        // 首页-第四副轮播
        DataManager.ADVERTISEMENT.advertisement_7();
    }

    private void handleLaunchAD() {
        // 启动页同步接口
        AdvertisementDto data = DataManager.adverImageSync(AdvertisementDto.ADVERTISEMENT_6);
        if (data != null && data.getData() != null && data.getData().size() > 0) {
            //启动图广告：1,保存json和图片到本地. 2,保存广告中的app下载信息
            switch (data.getData().get(0).getAd_location_id()) {
                case AdvertisementDto.ADVERTISEMENT_6://启动海报
                    if (data.isResult()) {//有广告
                        //保存json
                        PreferencesHepler.getInstance().saveIndexLaunchImage(data);
                        //下载启动图到本地
                        downloadLunchImage(data);
                        //保存启动图广告中的下载应用信息
                        saveLunchImageAPPInfo(data);
                    } else {
                        if (PreferencesHepler.getInstance().getIndexLaunchImage() != null)
                            PreferencesHepler.getInstance().removeIndexLaunchImage();
                    }
                    break;
            }
        }
    }

    private void downloadLunchImage(AdvertisementDto data) {
        Log.d(TAG, "downloadLunchImage: // ------------------------------------------------------");
        String imageName = StringUtil.getFileNameWithExt(data.getData().get(0).getServer_pic_a());
        String path = SYSJStorageUtil.getSysjDownload() + File.separator + imageName;
        File file = new File(path);
        Log.d(TAG, "Lunch Image AD file.exists() == " + file.exists());
        if (!file.exists()) { //本地没下载过此启动图
            List<String> url = new ArrayList<>();
            url.add(data.getData().get(0).getServer_pic_a());
            //下载启动图
            HttpManager.getInstance().downloadFiles(url, new Observer<Boolean>() {

                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    Log.d(TAG, "adImageDownload: onFailure " + e);
                }

                @Override
                public void onNext(Boolean b) {
                    Log.d(TAG, "adImageDownload: " + b);
                }
            });
        }
    }

    private void saveLunchImageAPPInfo(AdvertisementDto data) {
        Log.d(TAG, "saveLunchImageAPPInfo: // ------------------------------------------------------");
        if (!NetUtil.isConnect())
            return;
        int ad_type = data.getData().get(0).getAd_type();//1-->页面展示,2-->下载
        String fileUrl = data.getData().get(0).getDownload_android();
        String game_id = data.getData().get(0).getGame_id();
        Log.d(TAG, "saveLunchImageAPPInfo: 0");

        if (ad_type == 2 && URLUtil.isURL(fileUrl) &&
                !StringUtil.isNull(game_id) && !game_id.equals("0")) {

            // 更多精彩（广告）（同步）
            GetDownloadOtherEntity resultEntity = DataManager.getDownloadOtherSync(game_id);
            Log.d(TAG, "saveLunchImageAPPInfo: 1");
            if (resultEntity != null && resultEntity.isResult() &&
                    resultEntity.getData() != null && resultEntity.getData().size() > 0) {

                Download download = resultEntity.getData().get(0);
                if (download != null) {

                    FileDownloaderEntity idEntity = FileDownloaderManager.findByAdLocationId(FileDownloaderEntity.AD_LOCATION_ID_1);
                    FileDownloaderEntity newEntity = new FileDownloaderEntity();
                    File apkFile = SYSJStorageUtil.createApkPath(fileUrl);

                    if (idEntity != null) {// 数据存在相同广告位数据
                        Log.d(TAG, "saveLunchImageAPPInfo: fileUrl=" + fileUrl);
                        Log.d(TAG, "saveLunchImageAPPInfo: idEntity=" + idEntity.getFileUrl());
                        if (fileUrl.equals(idEntity.getFileUrl())) {// fileUrl相同，更新应用信息

                            idEntity.setAd_location_id(FileDownloaderEntity.AD_LOCATION_ID_1);
                            idEntity.setA_download_url(download.getA_download_url());
                            idEntity.setApp_id(download.getApp_id());
                            idEntity.setApp_intro(download.getApp_intro());
                            idEntity.setApp_name(download.getApp_name());
                            idEntity.setDisplay(download.getDisplay());
                            if (!StringUtil.isNull(idEntity.getAd_id())) {
                                idEntity.setAd_id(data.getData().get(0).getAd_id() + "");
                            }
                            idEntity.setI_download_url(download.getI_download_url());
                            idEntity.setFlag(download.getFlag());
                            idEntity.setPlay_num(download.getPlay_num());
                            idEntity.setPlay_text(download.getPlay_text());
                            idEntity.setSize_num(download.getSize_num());
                            idEntity.setSize_text(download.getSize_text());
                            idEntity.setType_id(download.getType_id());

                            // 保存或更新根据（game_id）
                            FileDownloaderManager.saveOrUdateByAdLocationId(idEntity);

                            Log.d(TAG, "saveLunchImageAPPInfo: 2");
                        } else {// fileUrl不同，更新下载地址
                            if (apkFile != null) {
                                FileUtil.deleteFile(apkFile.getPath());
                            }

                            idEntity.setFileSize(0);
                            idEntity.setDownloadSize(0);
                            idEntity.setFileUrl(fileUrl);
                            // idEntity.setGame_id(game_id);
                            idEntity.setFileType(FileDownloaderEntity.FILE_TYPE_ADVERTISEMENT);

                            idEntity.setAd_location_id(FileDownloaderEntity.AD_LOCATION_ID_1);
                            idEntity.setA_download_url(download.getA_download_url());
                            idEntity.setApp_id(download.getApp_id());
                            idEntity.setApp_intro(download.getApp_intro());
                            idEntity.setApp_name(download.getApp_name());
                            idEntity.setDisplay(download.getDisplay());
                            if (!StringUtil.isNull(idEntity.getAd_id())) {
                                idEntity.setAd_id(data.getData().get(0).getAd_id() + "");
                            }
                            idEntity.setI_download_url(download.getI_download_url());
                            idEntity.setFlag(download.getFlag());
                            idEntity.setPlay_num(download.getPlay_num());
                            idEntity.setPlay_text(download.getPlay_text());
                            idEntity.setSize_num(download.getSize_num());
                            idEntity.setSize_text(download.getSize_text());
                            idEntity.setType_id(download.getType_id());

                            // 保存或更新根据（game_id）
                            FileDownloaderManager.saveOrUdateByAdLocationId(idEntity);
                            Log.d(TAG, "saveLunchImageAPPInfo: 3");
                        }
                    } else {// 数据不存在相同广告位数据
                        FileDownloaderEntity urlEntity = FileDownloaderManager.findByFileUrl(fileUrl);
                        if (urlEntity != null) {// 数据存在相同fileUrl数据，服务器出错
                            // TODO: 2017/3/16
                            Log.d(TAG, "saveLunchImageAPPInfo: 4");
                        } else {// 数据不存在相同fileUrl数据，保存

                            newEntity.setFileSize(0);
                            newEntity.setDownloadSize(0);
                            newEntity.setFileUrl(fileUrl);
                            newEntity.setGame_id(game_id);
                            newEntity.setFileType(FileDownloaderEntity.FILE_TYPE_ADVERTISEMENT);

                            newEntity.setAd_location_id(FileDownloaderEntity.AD_LOCATION_ID_1);
                            newEntity.setA_download_url(download.getA_download_url());
                            newEntity.setApp_id(download.getApp_id());
                            newEntity.setApp_intro(download.getApp_intro());
                            newEntity.setApp_name(download.getApp_name());
                            newEntity.setDisplay(download.getDisplay());
                            newEntity.setAd_id(data.getData().get(0).getAd_id() + "");

                            newEntity.setI_download_url(download.getI_download_url());
                            newEntity.setFlag(download.getFlag());
                            newEntity.setPlay_num(download.getPlay_num());
                            newEntity.setPlay_text(download.getPlay_text());
                            newEntity.setSize_num(download.getSize_num());
                            newEntity.setSize_text(download.getSize_text());
                            newEntity.setType_id(download.getType_id());

                            // 保存或更新（fileUrl）
                            int isSave = FileDownloaderManager.save(newEntity);
                            Log.d(TAG, "saveLunchImageAPPInfo: isSave == " + isSave);
                        }
                    }
                }
            }
        } else {
            // 删除（ad_location_id）
            FileDownloaderManager.deleteByAdLocationId(FileDownloaderEntity.AD_LOCATION_ID_1);
            Log.d(TAG, "saveLunchImageAPPInfo: 6");
        }

        // 查找全部
        List<FileDownloaderEntity> l = FileDownloaderManager.findAll();
        Log.d(TAG, "saveLunchImageAPPInfo: db All=" + l);

        // 更新所有任务
        DownLoadManager.getInstance().updateFromDatabase();

        // 重置所有任务所有监听
        DownLoadManager.getInstance().resetAllDownLoadListener();

        // 更新下载应用信息
        EventManager.postFileDownloaderEvent();
        Log.d(TAG, "saveLunchImageAPPInfo: true");
    }

    /**
     * 飞磨下载应用信息
     */
    private void feimo() {
        Log.d(TAG, "feimo: // ------------------------------------------------------");
        // 更多精彩（内部）（同步）
        GetDownloadAppEntity entity = DataManager.getDownloadAppSync();
        // 下载管理更换了手游视界下载地址，导致下载管理处手游视界出现了两个，暂时如此操作
        FileDownloaderManager.deleteByFileTypeFeiMo();
        // 查找全部
        List<FileDownloaderEntity> list = FileDownloaderManager.findAll();

        Log.e("entity size:", JSONHelper.to(entity)+"");
        if (entity != null && entity.isResult() &&
                entity.getData() != null && entity.getData().size() > 0) {
            List<Download> netDownloads = entity.getData();
            List<Download> newDownloads = new ArrayList<>();
            Iterator<Download> iterator = netDownloads.iterator();
            Log.d(TAG, "feimo: 1");
            if (list != null && list.size() > 0) {
                while (iterator.hasNext()) {//
                    Download d = iterator.next();
                    boolean flag = false;
                    b:
                    for (FileDownloaderEntity e : list) {
                        if (d.getA_download_url().equals(e.getFileUrl())) {// 更新

                            e.setApp_id(d.getApp_id());
                            e.setApp_intro(d.getApp_intro());
                            e.setApp_name(d.getApp_name());
                            e.setDisplay(d.getDisplay());
                            e.setGame_id(d.getGame_id());
                            e.setI_download_url(d.getI_download_url());
                            e.setFlag(d.getFlag());
                            e.setPlay_num(d.getPlay_num());
                            e.setPlay_text(d.getPlay_text());
                            e.setSize_num(d.getSize_num());
                            e.setSize_text(d.getSize_text());
                            e.setType_id(d.getType_id());

                            // 保存或更新
                            FileDownloaderManager.saveOrUdateByFileUrl(e);
                            flag = true;
                            break b;
                        }
                    }
                    if (!flag) {
                        newDownloads.add(d);
                    }
                    Log.d(TAG, "feimo: 2");
                }
                if (newDownloads != null && newDownloads.size() > 0) {// 插入
                    iterator = newDownloads.iterator();
                    while (iterator.hasNext()) {
                        Download d = iterator.next();

                        FileDownloaderEntity e = new FileDownloaderEntity();
                        e.setFileSize(0);
                        e.setDownloadSize(0);
                        e.setFileUrl(d.getA_download_url());
                        e.setFileType(FileDownloaderEntity.FILE_TYPE_FEIMO);

                        e.setA_download_url(d.getA_download_url());
                        e.setApp_id(d.getApp_id());
                        e.setApp_intro(d.getApp_intro());
                        e.setApp_name(d.getApp_name());
                        e.setDisplay(d.getDisplay());
                        e.setGame_id(d.getGame_id());
                        e.setI_download_url(d.getI_download_url());
                        e.setFlag(d.getFlag());
                        e.setPlay_num(d.getPlay_num());
                        e.setPlay_text(d.getPlay_text());
                        e.setSize_num(d.getSize_num());
                        e.setSize_text(d.getSize_text());
                        e.setType_id(d.getType_id());

                        // 保存或更新
                        FileDownloaderManager.saveOrUdateByFileUrl(e);
                    }
                }
                Log.d(TAG, "feimo: 3");
            } else {// 插入
                while (iterator.hasNext()) {
                    Download d = iterator.next();

                    FileDownloaderEntity e = new FileDownloaderEntity();
                    e.setFileSize(0);
                    e.setDownloadSize(0);
                    e.setFileUrl(d.getA_download_url());
                    e.setFileType(FileDownloaderEntity.FILE_TYPE_FEIMO);

                    e.setA_download_url(d.getA_download_url());
                    e.setApp_id(d.getApp_id());
                    e.setApp_intro(d.getApp_intro());
                    e.setApp_name(d.getApp_name());
                    e.setDisplay(d.getDisplay());
                    e.setGame_id(d.getGame_id());
                    e.setI_download_url(d.getI_download_url());
                    e.setFlag(d.getFlag());
                    e.setPlay_num(d.getPlay_num());
                    e.setPlay_text(d.getPlay_text());
                    e.setSize_num(d.getSize_num());
                    e.setSize_text(d.getSize_text());
                    e.setType_id(d.getType_id());

                    // 保存或更新
                    FileDownloaderManager.saveOrUdateByFileUrl(e);
                }
                Log.d(TAG, "feimo: true");
            }
        }

        // 查找全部
        List<FileDownloaderEntity> l = FileDownloaderManager.findAll();
        Log.d(TAG, "feimo: l=" + l);

        // 更新所有任务
        DownLoadManager.getInstance().updateFromDatabase();

        // 重置所有任务所有监听
        DownLoadManager.getInstance().resetAllDownLoadListener();

        // 更新下载应用信息
        EventManager.postFileDownloaderEvent();
        Log.d(TAG, "feimo: true");
    }

    /**
     * 回调:登录成功
     */
    public synchronized void onEventMainThread(LoginEntity event) {
        BaiduPush.getInstances().onCreate(getApplicationContext());
    }

    /**
     *回调：退出
     */
    public  void onEventMainThread(LogoutEvent event){
        BaiduPush.getInstances().onStop(getApplicationContext());
    }


    /**
     * channel id 提交结果
     */
    public void onEventMainThread(SubmitChannelIdEntity entity){
        if (entity == null || !entity.isResult()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    BaiduPush.getInstances().onCreate(getApplicationContext());
                }
            },120000);
        }else {

        }
    }
}
