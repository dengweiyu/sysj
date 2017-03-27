package com.li.videoapplication.mvp.home.model;

import android.util.Log;

import com.li.videoapplication.data.EventManager;
import com.li.videoapplication.data.database.FileDownloaderEntity;
import com.li.videoapplication.data.database.FileDownloaderManager;
import com.li.videoapplication.data.download.DownLoadManager;
import com.li.videoapplication.data.local.FileUtil;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.data.model.entity.Download;
import com.li.videoapplication.data.model.entity.LaunchImage;
import com.li.videoapplication.data.model.response.ChangeGuessEntity;
import com.li.videoapplication.data.model.response.UnfinishedTaskEntity;
import com.li.videoapplication.data.model.response.AdvertisementDto;
import com.li.videoapplication.data.model.entity.HomeDto;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.mvp.OnLoadDataListener;
import com.li.videoapplication.mvp.home.HomeContract.onloadHomeDataListener;
import com.li.videoapplication.mvp.home.HomeContract.IHomeModel;
import com.li.videoapplication.data.HttpManager;
import com.li.videoapplication.framework.BaseHttpResult;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.URLUtil;

import java.io.File;
import java.util.List;

import rx.Observer;

/**
 * Model层: 首页
 */
public class HomeModel implements IHomeModel {

    private static final String TAG = HomeModel.class.getSimpleName();
    private static HomeModel homeModel;

    public static synchronized IHomeModel getInstance() {
        if (homeModel == null) {
            homeModel = new HomeModel();
        }
        return homeModel;
    }

    @Override
    public void loadHomeData(int page, boolean isLoad, final onloadHomeDataListener listener) {

        HttpManager.getInstance().getHomeInfo(page, isLoad, new Observer<HomeDto>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(HomeDto homeDto) {
                PreferencesHepler.getInstance().saveHomeData(homeDto);//保存首页json
                listener.onLoadHomeSuccess(homeDto);
            }
        });
    }

    @Override
    public void unfinishedTask(String member_id, boolean update, final onloadHomeDataListener listener) {

        HttpManager.getInstance().unfinishedTask(member_id, update, new Observer<UnfinishedTaskEntity>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(UnfinishedTaskEntity unFinishTaskDto) {
                if (unFinishTaskDto != null && unFinishTaskDto.isResult())
                    listener.onLoadUnFinishTaskSuccess(unFinishTaskDto);
            }
        });
    }

    @Override
    public void changeGuess(String group_ids, final onloadHomeDataListener listener) {

        HttpManager.getInstance().changeGuess(group_ids, new Observer<ChangeGuessEntity>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(ChangeGuessEntity changeGuessDto) {
                if (changeGuessDto != null)
                    listener.onLoadChangeGuessSuccess(changeGuessDto);
            }
        });
    }

    @Override
    public void changeGuessSecond(String video_ids, final onloadHomeDataListener listener) {

        HttpManager.getInstance().changeGuessSecond(video_ids, new Observer<ChangeGuessEntity>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(ChangeGuessEntity changeGuessDto) {
                if (changeGuessDto != null)
                    listener.onLoadChangeGuessSuccess(changeGuessDto);
            }
        });
    }

    @Override
    public void adverImage(final int location_id, final OnLoadDataListener<AdvertisementDto> listener) {
        HttpManager.getInstance().adverImage(location_id, new Observer<AdvertisementDto>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(AdvertisementDto data) {
                if (data != null)
                    listener.onSuccess(data);
            }
        });
    }

    @Override
    public void getDownloadOther(String game_id, final OnLoadDataListener<Download> listener) {
        HttpManager.getInstance().getDownloadOther(game_id, new Observer<List<Download>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(List<Download> downloads) {
                if (downloads != null && downloads.size() > 0) {
                    listener.onSuccess(downloads.get(0));
                }
            }
        });
    }

    @Override
    public void adImage208(final int localtion_id, boolean isLoad, final onloadHomeDataListener listener) {

        HttpManager.getInstance().adImage208(localtion_id, isLoad, new Observer<AdvertisementDto>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(AdvertisementDto data) {
                Log.d(TAG, "onNext: AdvertisementDto == " + data);
                if (data != null) {
                    //回调 p更新 v
                    listener.onLoadAdvertisementSuccess(data);

                    /* 广告数据再处理：
                        启动图广告：1,保存json和图片到本地. 2,保存广告中的app下载信息
                        通栏广告：1，保存广告中的app下载信息 */
                    switch (localtion_id) {
                        case AdvertisementDto.ADVERTISEMENT_8://首页-热门游戏下方通栏
                            //保存通栏广告中的下载应用信息
                            saveHomeAdAPPInfo(data);
                            break;
                    }
                }
            }
        });
    }

    /**
     * 通栏广告载应用信息
     */
    private void saveHomeAdAPPInfo(AdvertisementDto entity) {
        Log.d(TAG, "saveHomeAdAPPInfo: // ------------------------------------------------------");
        synchronized (HomeModel.class) {
            if (entity != null && entity.isResult()) {
                if (entity.getData() != null && entity.getData().size() > 0) {
                    for (int i = 0; i < entity.getData().size(); i++) {
                        LaunchImage ad = entity.getData().get(i);
                        if (ad != null) {
                            ad(ad);
                        }
                    }
                }
            } else if (entity != null && !entity.isResult()) {
                FileDownloaderManager.deleteByAdLocationId(entity.getData().get(0).getAd_location_id());
            }
        }
    }

    /**
     * 通栏广告载应用信息
     */
    private void ad(LaunchImage ad) {
        Log.d(TAG, "ad: ad=" + ad);
        if (ad != null) {
            int ad_type = ad.getAd_type();
            String fileUrl = ad.getDownload_android();
            String game_id = ad.getGame_id();
            int ad_location_id = ad.getAd_location_id();
            Log.d(TAG, "ad: " + ad_location_id + "/ad_id=" + ad.getAd_id());
            Log.d(TAG, "ad: " + ad_location_id + "/ad_location_id=" + ad_location_id);
            Log.d(TAG, "ad: " + ad_location_id + "/fileUrl=" + fileUrl);
            Log.d(TAG, "ad: " + ad_location_id + "/game_id=" + game_id);
            Log.d(TAG, "ad: " + ad_location_id + "/0");

            if (ad_type == 2 && URLUtil.isURL(fileUrl) &&
                    !StringUtil.isNull(game_id) && !game_id.equals("0")) {

                if (ad.getDownload_desc() != null && ad.getDownload_desc().size() > 0) {

                    Download download = ad.getDownload_desc().get(0);
                    if (download != null) {

                        FileDownloaderEntity idEntity = FileDownloaderManager.findByAdLocationId(ad_location_id);
                        FileDownloaderEntity newEntity = new FileDownloaderEntity();
                        File apkFile = SYSJStorageUtil.createApkPath(fileUrl);

                        if (idEntity != null) {// 数据存在相同广告位数据
                            Log.d(TAG, "ad: " + ad_location_id + "/fileUrl=" + fileUrl);
                            Log.d(TAG, "ad: " + ad_location_id + "/idEntity=" + idEntity.getFileUrl());
                            if (fileUrl.equals(idEntity.getFileUrl())) {// fileUrl相同，更新应用信息

                                idEntity.setAd_location_id(ad_location_id);
                                idEntity.setA_download_url(download.getA_download_url());
                                idEntity.setApp_id(download.getApp_id());
                                idEntity.setApp_intro(download.getApp_intro());
                                idEntity.setApp_name(download.getApp_name());
                                idEntity.setDisplay(download.getDisplay());
                                if (!StringUtil.isNull(ad.getAd_id() + "")) {
                                    idEntity.setAd_id(ad.getAd_id() + "");
                                }
                                idEntity.setI_download_url(download.getI_download_url());
                                idEntity.setFlag(download.getFlag());
                                idEntity.setPlay_num(download.getPlay_num());
                                idEntity.setPlay_text(download.getPlay_text());
                                idEntity.setSize_num(download.getSize_num());
                                idEntity.setSize_text(download.getSize_text());
                                idEntity.setType_id(download.getType_id());

                                Log.d(TAG, "go===>BannerService:idEntity " + idEntity.toString());
                                // 保存或更新根据（game_id）
                                FileDownloaderManager.saveOrUdateByAdLocationId(idEntity);

                                Log.d(TAG, "ad: " + ad_location_id + "/2");
                            } else {// fileUrl不同，更新下载地址
                                if (apkFile != null) {
                                    FileUtil.deleteFile(apkFile.getPath());
                                }

                                idEntity.setFileSize(0);
                                idEntity.setDownloadSize(0);
                                idEntity.setFileUrl(fileUrl);
                                // idEntity.setGame_id(game_id);
                                idEntity.setFileType(FileDownloaderEntity.FILE_TYPE_ADVERTISEMENT);

                                idEntity.setAd_location_id(ad_location_id);
                                idEntity.setA_download_url(download.getA_download_url());
                                idEntity.setApp_id(download.getApp_id());
                                idEntity.setApp_intro(download.getApp_intro());
                                idEntity.setApp_name(download.getApp_name());
                                idEntity.setDisplay(download.getDisplay());
                                if (!StringUtil.isNull(ad.getAd_id() + "")) {
                                    idEntity.setAd_id(ad.getAd_id() + "");
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
                                Log.d(TAG, "ad: " + ad_location_id + "/3");
                            }
                        } else {// 数据不存在相同广告位数据
                            FileDownloaderEntity urlEntity = FileDownloaderManager.findByFileUrl(fileUrl);
                            if (urlEntity != null) {// 数据存在相同fileUrl数据，服务器出错
                                // TODO: 2016/7/3
                                Log.d(TAG, "ad: " + ad_location_id + "/4");
                            } else {// 数据不存在相同fileUrl数据，保存

                                Log.d(TAG, "ad: " + ad_location_id + "/5");

                                newEntity.setFileSize(0);
                                newEntity.setDownloadSize(0);
                                newEntity.setFileUrl(fileUrl);
                                newEntity.setGame_id(game_id);
                                newEntity.setFileType(FileDownloaderEntity.FILE_TYPE_ADVERTISEMENT);

                                newEntity.setAd_location_id(ad_location_id);
                                newEntity.setA_download_url(download.getA_download_url());
                                newEntity.setApp_id(download.getApp_id());
                                newEntity.setApp_intro(download.getApp_intro());
                                newEntity.setApp_name(download.getApp_name());
                                newEntity.setDisplay(download.getDisplay());
                                if (!StringUtil.isNull(ad.getAd_id() + "")) {
                                    newEntity.setAd_id(ad.getAd_id() + "");
                                }
                                newEntity.setI_download_url(download.getI_download_url());
                                newEntity.setFlag(download.getFlag());
                                newEntity.setPlay_num(download.getPlay_num());
                                newEntity.setPlay_text(download.getPlay_text());
                                newEntity.setSize_num(download.getSize_num());
                                newEntity.setSize_text(download.getSize_text());
                                newEntity.setType_id(download.getType_id());

                                // 保存或更新（fileUrl）
                                FileDownloaderManager.save(newEntity);
                                Log.d(TAG, "ad: " + ad_location_id + "/5");
                            }
                        }
                    }
                }
            } else {
                // 删除（ad_location_id）
                FileDownloaderManager.deleteByAdLocationId(ad_location_id);
                Log.d(TAG, "ad: " + ad_location_id + "/6");
            }
        }

        // 更新所有任务
        DownLoadManager.getInstance().updateFromDatabase();

        // 重置所有任务所有监听
        DownLoadManager.getInstance().resetAllDownLoadListener();

        // 更新下载应用信息
        EventManager.postFileDownloaderEvent();
        Log.d(TAG, "advertisement: true");
    }

    @Override
    public void adClick(long ad_id, int ad_click_state, String hardwarecode, final onloadHomeDataListener listener) {
        HttpManager.getInstance().adClick(ad_id, ad_click_state, hardwarecode, new Observer<BaseHttpResult>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(BaseHttpResult data) {
                if (data != null && data.isResult())
                    listener.onLoadAdClickSuccess(data);
            }
        });
    }

    @Override
    public void adImageDownload(List<String> downloadList, final OnLoadDataListener<Boolean> listener) {
        HttpManager.getInstance().downloadFiles(downloadList, new Observer<Boolean>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(Boolean aBoolean) {
                listener.onSuccess(aBoolean);
            }
        });
    }
}
