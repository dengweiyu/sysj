package com.li.videoapplication.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.fmsysj.screeclibinvoke.data.model.event.ScreenShotEvent;
import com.fmsysj.screeclibinvoke.data.model.event.VideoCaptureEvent;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.database.VideoCaptureResponseObject;
import com.li.videoapplication.data.local.ImageDirectoryEntity;
import com.li.videoapplication.data.local.ImageDirectoryResponseObject;
import com.li.videoapplication.data.local.ScreenShotEntity;
import com.li.videoapplication.data.local.ScreenShotResponseObject;
import com.li.videoapplication.data.model.entity.Associate;
import com.li.videoapplication.data.model.event.ConnectivityChangeEvent;
import com.li.videoapplication.data.model.event.ImageView2ImageShareEvent;
import com.li.videoapplication.data.model.event.LoginEvent;
import com.li.videoapplication.data.model.event.LogoutEvent;
import com.li.videoapplication.data.model.event.RefreshCurrencyEvent;
import com.li.videoapplication.data.model.event.SearchGame2VideoShareEvent;
import com.li.videoapplication.data.model.event.Share2VideoShareEvent;
import com.li.videoapplication.data.model.event.Tag2VideoShareEvent;
import com.li.videoapplication.data.model.event.UserInfomationEvent;
import com.li.videoapplication.data.model.event.postConnectedTVSuccessEvent;

import java.util.List;

import io.rong.eventbus.EventBus;


/**
 * 功能：EnventBus管理类
 */
public class EventManager {

    protected static final String TAG = EventManager.class.getSimpleName();

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
