package com.li.videoapplication.data;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.fmsysj.screeclibinvoke.data.model.event.ScreenShotEvent;
import com.fmsysj.screeclibinvoke.data.model.event.VideoCaptureEvent;
import com.li.videoapplication.data.database.FileDownloaderEntity;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.database.VideoCaptureResponseObject;
import com.li.videoapplication.data.download.FileDownloaderResponseObject;
import com.li.videoapplication.data.local.ImageDirectoryEntity;
import com.li.videoapplication.data.local.ImageDirectoryResponseObject;
import com.li.videoapplication.data.local.ScreenShotEntity;
import com.li.videoapplication.data.local.ScreenShotResponseObject;
import com.li.videoapplication.data.model.entity.Associate;
import com.li.videoapplication.data.model.event.CloudVideoRecommendEvent;
import com.li.videoapplication.data.model.event.ConnectivityChangeEvent;
import com.li.videoapplication.data.model.event.DownloadCompleteEvent;
import com.li.videoapplication.data.model.event.FileDownloaderEvent;
import com.li.videoapplication.data.model.event.ImageView2ImageShareEvent;
import com.li.videoapplication.data.model.event.LoginEvent;
import com.li.videoapplication.data.model.event.LogoutEvent;
import com.li.videoapplication.data.model.event.MatchListFliterEvent;
import com.li.videoapplication.data.model.event.SearchGame2VideoShareEvent;
import com.li.videoapplication.data.model.event.Share2VideoShareEvent;
import com.li.videoapplication.data.model.event.Tag2VideoShareEvent;
import com.li.videoapplication.data.model.event.UploadMatchPicEvent;
import com.li.videoapplication.data.model.event.UserInfomationEvent;
import com.li.videoapplication.data.model.event.VideoCutEvent;
import com.li.videoapplication.data.model.event.VideoUploadCompleteEvent;
import com.li.videoapplication.data.model.event.postConnectedTVSuccessEvent;

import java.util.List;

import io.rong.eventbus.EventBus;


/**
 * 功能：EnventBus管理类
 */
public class EventManager {

    protected static final String TAG = EventManager.class.getSimpleName();
    /**
     * 更新下载应用信息
     */
    public static void postFileDownloaderEvent() {
        Log.d(TAG, "postFileDownloaderEvent: ");
        FileDownloaderEvent event = new FileDownloaderEvent();
        EventBus.getDefault().post(event);
    }

    /**
     * 发布下载完成事件
     */
    public static void postDownloadCompleteEvent(Intent intent) {
        DownloadCompleteEvent event = new DownloadCompleteEvent();
        event.setIntent(intent);
        EventBus.getDefault().post(event);
    }

    /**
     * 发布云端视频申请推荐位
     */
    public static void postCloudVideoRecommendEvent(String video_id) {
        CloudVideoRecommendEvent event = new CloudVideoRecommendEvent();
        event.setVideo_id(video_id);
        EventBus.getDefault().post(event);
    }

    /**
     * 发布加载下载文件事件
     */
    public static void postFileDownloaderResponseObject(boolean result, String msg, List<FileDownloaderEntity> data, String fileType) {
        FileDownloaderResponseObject event = new FileDownloaderResponseObject(result, msg, data, fileType);
        EventBus.getDefault().post(event);
    }

    /**
     * 发布赛事上传图片事件
     */
    public static void postUploadMatchPicEvent(List<ScreenShotEntity> data) {
        UploadMatchPicEvent event = new UploadMatchPicEvent();
        event.setData(data);
        EventBus.getDefault().post(event);
    }

    /**
     * 发布完成上传视频事件
     */
    public static void postVideoUploadCompleteEvent(String video_id) {
        VideoUploadCompleteEvent event = new VideoUploadCompleteEvent();
        event.setVideo_id(video_id);
        EventBus.getDefault().post(event);
    }

    /**
     * 发布赛事列表筛选完成更新事件
     */
    public static void postMatchListFliterEvent(String gameIds, String gameNames,
                                                String match_type, String match_type_names) {

        EventBus.getDefault().post(new MatchListFliterEvent(gameIds, gameNames,
                match_type, match_type_names));
    }

    /**
     * 发布视频剪辑完成事件
     */
    public static void postVideoCutEvent() {
        VideoCutEvent event = new VideoCutEvent();
        EventBus.getDefault().post(event);
    }

    /**
     * 发布登录事件
     */
    public static void postLoginEvent() {
        LoginEvent event = new LoginEvent();
        EventBus.getDefault().post(event);
    }

    /**
     * 发布登出事件
     */
    public static void postLogoutEvent() {
        LogoutEvent event = new LogoutEvent();
        EventBus.getDefault().post(event);
    }

    /**
     * 发布刷新个人资料事件
     */
    public static void postUserInfomationEvent() {
        UserInfomationEvent event = new UserInfomationEvent();
        EventBus.getDefault().post(event);
    }

    /**
     * 发布网络变化事件
     */
    public static void postConnectivityChangeEvent(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        try {
            Log.i(TAG, "connectivity=" + info);
            Log.i(TAG, "connectivity=" + info.getTypeName());
            Log.i(TAG, "connectivity=" + info.getSubtypeName());
            Log.i(TAG, "connectivity=" + info.getExtraInfo());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (info != null && info.isAvailable()) {
            //AppData.getInstance().setConnectivity(true);
        } else {
            //AppData.getInstance().setConnectivity(false);
        }
        ConnectivityChangeEvent event = new ConnectivityChangeEvent();
        event.setNetworkInfo(info);
        EventBus.getDefault().post(event);
    }

    public static void postTag2VideoShareEvent() {
        Tag2VideoShareEvent event = new Tag2VideoShareEvent();
        EventBus.getDefault().post(event);
    }

    public static void postSearchGame2VideoShareEvent(String gamaName, String gamaId, String groupId) {
        SearchGame2VideoShareEvent event = new SearchGame2VideoShareEvent();
        event.setGamaName(gamaName);
        event.setGamaId(gamaId);
        event.setGroupId(groupId);
        EventBus.getDefault().post(event);
    }

    public static void postSearchGame2VideoShareEvent(Associate associate) {
        SearchGame2VideoShareEvent event = new SearchGame2VideoShareEvent();
        event.setAssociate(associate);
        EventBus.getDefault().post(event);
    }

    public static void postImageView2ImageShareEvent() {
        ImageView2ImageShareEvent event = new ImageView2ImageShareEvent();
        EventBus.getDefault().post(event);
    }

    public static void postConnectedTVSuccessEvent() {
        postConnectedTVSuccessEvent event = new postConnectedTVSuccessEvent();
        EventBus.getDefault().post(event);
    }

    public static void postShare2VideoShareEvent(String shareChannel) {
        Share2VideoShareEvent event = new Share2VideoShareEvent();
        event.setShareChannel(shareChannel);
        EventBus.getDefault().post(event);
    }

    /**
     * 发布录屏事件（完成）
     */
    public static void postVideoCaptureEvent() {
        Log.d(TAG, "postVideoCaptureEvent: ");
        VideoCaptureEvent event = new VideoCaptureEvent();
        EventBus.getDefault().post(event);
    }

    /**
     * 发布截屏事件
     */
    public static void postScreenShotEvent() {
        Log.d(TAG, "postScreenShotEvent: ");
        ScreenShotEvent event = new ScreenShotEvent();
        EventBus.getDefault().post(event);
    }

    /**
     * 发布加载截图事件
     */
    public static void postScreenShotResponseObject(boolean result, String msg, List<ScreenShotEntity> data) {
        ScreenShotResponseObject event = new ScreenShotResponseObject(result, msg, data);
        EventBus.getDefault().post(event);
    }

    /**
     * 发布加载本地视频事件
     */
    public static void postVideoCaptureResponseObject(boolean result, int resultCode, String msg, List<VideoCaptureEntity> data) {
        VideoCaptureResponseObject event = new VideoCaptureResponseObject(result, resultCode, msg, data);
        EventBus.getDefault().post(event);
    }

    /**
     * 发布加载图片文件夹事件
     */
    public static void postImageDirectoryResponseObject(boolean result, String msg, List<ImageDirectoryEntity> data) {
        ImageDirectoryResponseObject event = new ImageDirectoryResponseObject(result, msg, data);
        EventBus.getDefault().post(event);
    }
}
