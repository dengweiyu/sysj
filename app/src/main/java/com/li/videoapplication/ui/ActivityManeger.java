package com.li.videoapplication.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.entity.GroupType;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.entity.VideoImageGroup;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.ui.activity.AboutActivity;
import com.li.videoapplication.ui.activity.ActivityDetailActivity208;
import com.li.videoapplication.ui.activity.ActivityImageUploadActivity;
import com.li.videoapplication.ui.activity.ActivityListActivity;
import com.li.videoapplication.ui.activity.ActivityVideoActivity;
import com.li.videoapplication.ui.activity.BillboardActivity;
import com.li.videoapplication.ui.activity.CameraRecoedActivity;
import com.li.videoapplication.ui.activity.CollectionActivity;
import com.li.videoapplication.ui.activity.ConversationActivity;
import com.li.videoapplication.ui.activity.GameMatchDetailActivity;
import com.li.videoapplication.ui.activity.GiftDetailActivity;
import com.li.videoapplication.ui.activity.GiftListActivity;
import com.li.videoapplication.ui.activity.GroupDetailActivity;
import com.li.videoapplication.ui.activity.GroupGiftActivity;
import com.li.videoapplication.ui.activity.GroupListActivity;
import com.li.videoapplication.ui.activity.HelpActivity;
import com.li.videoapplication.ui.activity.HomeImageShareActivity;
import com.li.videoapplication.ui.activity.HomeMoreActivity;
import com.li.videoapplication.ui.activity.ImageDetailActivity;
import com.li.videoapplication.ui.activity.ImageShareActivity;
import com.li.videoapplication.ui.activity.ImageViewActivity;
import com.li.videoapplication.ui.activity.LoginActivity;
import com.li.videoapplication.ui.activity.MainActivity;
import com.li.videoapplication.ui.activity.MessageActivity;
import com.li.videoapplication.ui.activity.MessageListActivity;
import com.li.videoapplication.ui.activity.MyDynamicActivity;
import com.li.videoapplication.ui.activity.MyMatchActivity;
import com.li.videoapplication.ui.activity.MyMatchProcessActivity;
import com.li.videoapplication.ui.activity.MyPersonalCenterActivity;
import com.li.videoapplication.ui.activity.MyPersonalInfoActivity;
import com.li.videoapplication.ui.activity.MyPlayerActivity;
import com.li.videoapplication.ui.activity.MyTaskActivity;
import com.li.videoapplication.ui.activity.MyWelfareActivity;
import com.li.videoapplication.ui.activity.PlayerDynamicActivity;
import com.li.videoapplication.ui.activity.PlayerPersonalInfoActivity;
import com.li.videoapplication.ui.activity.PrivacyActivity;
import com.li.videoapplication.ui.activity.RecommendActivity;
import com.li.videoapplication.ui.activity.ReportActivity;
import com.li.videoapplication.ui.activity.ReportResultActivity;
import com.li.videoapplication.ui.activity.ScanQRCodeActivity;
import com.li.videoapplication.ui.activity.SearchActivity;
import com.li.videoapplication.ui.activity.SearchGameActivity;
import com.li.videoapplication.ui.activity.SearchLifeActivity;
import com.li.videoapplication.ui.activity.SearchResultActivity;
import com.li.videoapplication.ui.activity.SettingActivity;
import com.li.videoapplication.ui.activity.ShareActivity;
import com.li.videoapplication.ui.activity.SignUpActivity;
import com.li.videoapplication.ui.activity.SquareActivity;
import com.li.videoapplication.ui.activity.TagActivity;
import com.li.videoapplication.ui.activity.UploadMatchResultImageActivity;
import com.li.videoapplication.ui.activity.VideoChooseActivity;
import com.li.videoapplication.ui.activity.VideoEditorActivity;
import com.li.videoapplication.ui.activity.VideoMangerActivity;
import com.li.videoapplication.ui.activity.VideoPlayActivity;
import com.li.videoapplication.ui.activity.VideoShareActivity;
import com.li.videoapplication.ui.activity.VideoShareActivity210;
import com.li.videoapplication.ui.activity.VideoUploadActivity;
import com.li.videoapplication.ui.toast.ToastHelper;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.utils.StringUtil;

import io.rong.imkit.RongIM;

/**
 * 功能：活动管理
 */
public class ActivityManeger {

    private static final String TAG = ActivityManeger.class.getSimpleName();

    /**
     * 视频管理
     */
    public synchronized final static void startVideoMangerActivity(Context context) {
        startVideoMangerActivity(context, null, null);
    }

    /**
     * 视频管理
     */
    public synchronized final static void startVideoMangerActivity(Context context, Game game) {
        startVideoMangerActivity(context, game, null);
    }

    /**
     * 视频管理
     */
    public synchronized final static void startVideoMangerActivity(Context context, Match match) {
        startVideoMangerActivity(context, null, match);
    }

    /**
     * 视频管理
     */
    public synchronized final static void startVideoMangerActivity(Context context, Game game, Match match) {
        Intent intent = new Intent();
        intent.setClass(context, VideoMangerActivity.class);
        if (game != null)
            intent.putExtra("game", game);
        if (game != null)
            intent.putExtra("match", match);
        context.startActivity(intent);
    }

    /******************************************/

    /**
     * 视频分享
     */
    public synchronized final static void startVideoShareActivity(Context context, VideoCaptureEntity entity) {
        startVideoShareActivity(context, entity, null, null);
    }

    /**
     * 视频分享
     */
    public synchronized final static void startVideoShareActivity(Context context, VideoCaptureEntity entity, Game game) {
        startVideoShareActivity(context, entity, game, null);
    }

    /**
     * 视频分享
     */
    public synchronized final static void startVideoShareActivity(Context context, VideoCaptureEntity entity, Match match) {
        startVideoShareActivity(context, entity, null, match);
    }

    /**
     * 视频分享
     */
    public synchronized final static void startVideoShareActivity(Context context, VideoCaptureEntity entity, Game game, Match match) {
        Intent intent = new Intent();
        if (entity != null)
            intent.putExtra("entity", entity);
        if (game != null)
            intent.putExtra("game", game);
        if (match != null)
            intent.putExtra("match", match);
        intent.setClass(context, VideoShareActivity.class);
        context.startActivity(intent);
    }

    /**
     * 视频分享
     */
    public static void startVideoShareActivity210(Context context, VideoCaptureEntity entity) {
        Log.d(TAG, "startVideoShareActivity: ");
        Intent intent = new Intent();
        if (entity != null)
            intent.putExtra("entity", entity);
        intent.setClass(context, VideoShareActivity210.class);
        context.startActivity(intent);
    }


    /**
     * 视频上传208
     */
    public synchronized final static void startVideoUploadActivity(Context context, VideoCaptureEntity entity,
                                                                   Match match) {
        Intent intent = new Intent();
        if (entity != null)
            intent.putExtra("entity", entity);
        if (match != null)
            intent.putExtra("match", match);
        intent.setClass(context, VideoUploadActivity.class);
        context.startActivity(intent);
    }

    /******************************************/

    /**
     * 图文分享
     */
    public synchronized final static void startImageShareActivity(Context context, String imageUrl) {
        startImageShareActivity(context, imageUrl, null);
    }

    /**
     * 图文分享
     */
    public synchronized final static void startImageShareActivity(Context context, Game game) {
        startImageShareActivity(context, null, game);
    }

    /**
     * 图文分享
     */
    public synchronized final static void startImageShareActivity(Context context, String imageUrl, Game game) {
        Intent intent = new Intent();
        intent.setClass(context, ImageShareActivity.class);
        intent.putExtra("imageUrl", imageUrl);
        if (game != null)
            intent.putExtra("game", game);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 图文分享208
     */
    public synchronized final static void startActivityImageUploadActivity(Context context, String match_id) {
        Intent intent = new Intent();
        intent.setClass(context, ActivityImageUploadActivity.class);
        intent.putExtra("match_id", match_id);
        context.startActivity(intent);
    }

    /******************************************/

    /**
     * 视频编辑
     */
    public synchronized final static void startVideoEditorActivity(Context context, VideoCaptureEntity entity) {
        Intent intnet = new Intent(context, VideoEditorActivity.class);
        if (entity != null)
            intnet.putExtra("entity", entity);
        context.startActivity(intnet);
    }

    /******************************************/

    /**
     * 查找游戏
     */
    public synchronized final static void startSearchGameActivity(Activity activity) {
        Intent intent = new Intent(activity, SearchGameActivity.class);
        intent.putExtra("type", "game");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    /******************************************/

    /**
     * 查找查找精彩生活
     */
    public synchronized final static void startSearchLifeActivity(Activity activity) {
        Intent intent = new Intent(activity, SearchLifeActivity.class);
        intent.putExtra("type", "life");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    /******************************************/

    /**
     * 图片选择
     */
    public synchronized final static void startImageViewActivity(Context context) {
        Intent intent = new Intent(context, ImageViewActivity.class);
        context.startActivity(intent);
    }

    /******************************************/

    public static final String LUPINGDASHI_URL = "http://lp.ifeimo.com/";
    public static final String LUPINGDASHI_ICONURL = "http://apps.ifeimo.com/Public/Uploads/Game/Flag/icon.png";
    public static final String LUPINGDASHI_TITLE = "录屏大师";
    public static final String LUPINGDASHI_CONTENT = "我刚刚发现了一个有趣的录屏软件,可以很方便地录制手机视频，推荐你也来试试";
    public static final String MYCLOUDVIDEO_CONTENT = "刚才我用《录屏大师》在手机上录制了一段精彩视频，大家快来欣赏吧";

    /**
     * 分享录屏大师
     */
    public synchronized final static void startActivityShareActivity4LuPingDaShi(Activity activity) {
        Intent intent = new Intent(activity, ShareActivity.class);
        intent.putExtra("videoUrl", LUPINGDASHI_URL);
        intent.putExtra("VideoTitle", LUPINGDASHI_TITLE);
        intent.putExtra("imageUrl", LUPINGDASHI_ICONURL);
        intent.putExtra("content", LUPINGDASHI_CONTENT);
        intent.putExtra("page", ShareActivity.PAGE_SYSJ);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_bottom_in, R.anim.activity_hold);

        // 推广手游视界APP
        DataManager.TASK.doTask_17(PreferencesHepler.getInstance().getMember_id());
    }

    /******************************************/

    public static final String SYSJ_URL = "http://sj.ifeimo.com/";
    public static final String SYSJ_ICONURL = "http://apps.ifeimo.com/Public/Uploads/Game/Flag/576b9a94632f9.png";
    public static final String SYSJ_TITLE = "手游视界";
    public static final String SYSJ_CONTENT = "我刚刚发现了一个有趣的手游视频平台，可以和方便的观看所有手游的视频、攻略等，推荐你也来试试。";

    /**
     * 分享手游视界
     */
    public synchronized final static void startActivityShareActivity4SYSJ(Activity activity) {
        Intent intent = new Intent(activity, ShareActivity.class);
        intent.putExtra("videoUrl", SYSJ_URL);
        intent.putExtra("VideoTitle", SYSJ_TITLE);
        intent.putExtra("imageUrl", SYSJ_ICONURL);
        intent.putExtra("content", SYSJ_CONTENT);
        intent.putExtra("page", ShareActivity.PAGE_SYSJ);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_bottom_in, R.anim.activity_hold);

        // 推广手游视界APP
        DataManager.TASK.doTask_17(PreferencesHepler.getInstance().getMember_id());
    }

    /**
     * 分享视频播放
     */
    public synchronized final static void startActivityShareActivity4VideoPlay(Activity activity, String videoUrl, String VideoTitle, String imageUrl, String content) {
        Intent intent = new Intent(activity, ShareActivity.class);
        intent.putExtra("videoUrl", videoUrl);
        intent.putExtra("VideoTitle", VideoTitle);
        intent.putExtra("imageUrl", imageUrl);
        intent.putExtra("content", content);
        intent.putExtra("page", ShareActivity.PAGE_VIDEOPLAY);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_bottom_in, R.anim.activity_hold);

        String member_id = PreferencesHepler.getInstance().getMember_id();
        // 缤分享客
        DataManager.TASK.doTask_22(member_id);
    }

    /**
     * 分享云端视频
     */
    public synchronized final static void startActivityShareActivity4MyCloudVideo(Activity activity, String url, String imageUrl, String videoTitle) {
        Intent intent = new Intent(activity, ShareActivity.class);
        intent.putExtra("videoUrl", url);
        intent.putExtra("imageUrl", imageUrl);
        intent.putExtra("VideoTitle", videoTitle);
        intent.putExtra("content", MYCLOUDVIDEO_CONTENT);
        intent.putExtra("page", ShareActivity.PAGE_MYCLOUDVIDEO);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_bottom_in, R.anim.activity_hold);
    }

    /**
     * 分享本地图片
     */
    public synchronized final static void startActivityShareActivity4MyScreenShot(Activity activity, String imageUrl, String videoTitle) {
        Intent intent = new Intent(activity, ShareActivity.class);
        intent.putExtra("imageUrl", imageUrl);
        intent.putExtra("VideoTitle", videoTitle);
        intent.putExtra("page", ShareActivity.PAGE_MYSCREENSHOT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_bottom_in, R.anim.activity_hold);
    }

    /**
     * 分享本地视频
     */
    public synchronized final static void startShareActivity4MyLocalVideo(Activity activity) {
        Intent intent = new Intent(activity, ShareActivity.class);
        intent.putExtra("page", ShareActivity.PAGE_MYCLOCALVIDEO);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_bottom_in, R.anim.activity_hold);
    }

    /******************************************/

    /**
     * 举报
     */
    public synchronized final static void startReportActivity(Context context, VideoImage videoImage) {
        Intent intent = new Intent();
        intent.setClass(context, ReportActivity.class);
        intent.putExtra("videoImage", videoImage);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /******************************************/

    /**
     * 视频详情208
     */
    public synchronized static void startVideoPlayActivity208(Context context, VideoImage item, boolean isLandscape) {
        if (!NetUtil.isConnect()) {
            ToastHelper.s("网络错误");
            return;
        }
        if (item == null) {
            return;
        }
        AppManager.getInstance().removeActivity(VideoPlayActivity.class);
        if (!StringUtil.isNull(item.getVideo_id())) {
            item.setId(item.getVideo_id());
        }
        Intent intent = new Intent();
        intent.setClass(context, VideoPlayActivity.class);
        intent.putExtra("item", item);
        intent.putExtra("isLandscape", isLandscape);
        context.startActivity(intent);

        // 视频播放数+1
        DataManager.TASK.videoClickVideo201(item.getVideo_id());
    }

    /**
     * 视频详情
     */
    public synchronized final static void startVideoPlayActivity(Context context, VideoImage item) {
        if (!NetUtil.isConnect()) {
            ToastHelper.s("网络错误");
            return;
        }
        if (item == null) {
            return;
        }
        AppManager.getInstance().removeActivity(VideoPlayActivity.class);
        if (!StringUtil.isNull(item.getVideo_id())) {
            item.setId(item.getVideo_id());
        }
        Intent intent = new Intent();
        intent.setClass(context, VideoPlayActivity.class);
        intent.putExtra("item", item);
        context.startActivity(intent);

        // 视频播放数+1
        DataManager.TASK.videoClickVideo201(item.getVideo_id());
    }

    /**
     * 视频详情
     */
    public synchronized final static void startVideoPlayActivityNewTask(Context context, VideoImage item) {
        if (!NetUtil.isConnect()) {
            ToastHelper.s("网络错误");
            return;
        }
        Intent intent = new Intent();
        intent.setClass(context, VideoPlayActivity.class);
        intent.putExtra("item", item);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        Log.d("MainApplication", "ActivityManeger startVideoPlayActivityNewTask");
    }

    /******************************************/

    /**
     * 举报结果
     */
    public synchronized final static void startReportResultActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, ReportResultActivity.class);
        context.startActivity(intent);
    }

    /**
     * 收藏
     */
    public synchronized final static void startCollectionActivity(Context context) {

        Intent intent = new Intent();
        intent.setClass(context, CollectionActivity.class);
        context.startActivity(intent);
    }

    /**
     * 关于
     */
    public synchronized final static void startAboutActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, AboutActivity.class);
        context.startActivity(intent);
    }

    /**
     * 活动详情208
     */
    public synchronized final static void startActivityDetailActivity208(Context context, String match_id) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("match_id", match_id);
        intent.setClass(context, ActivityDetailActivity208.class);
        context.startActivity(intent);
    }

    /**
     * 活动详情
     */
    public synchronized final static void startActivityDetailActivityNewTask(Context context, String match_id) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("match_id", match_id);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, ActivityDetailActivity208.class);
        context.startActivity(intent);
    }

    /**
     * 活动列表
     */
    public synchronized final static void startActivityListActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, ActivityListActivity.class);
        context.startActivity(intent);
    }

    /**
     * 活动列表
     */
    public synchronized final static void startGiftListActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, GiftListActivity.class);
        context.startActivity(intent);
    }

    /**
     * 精彩推荐
     */
    public synchronized final static void startRecommendActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, RecommendActivity.class);
        context.startActivity(intent);
    }

    /**
     * 精彩推荐
     */
    public synchronized final static void startRecommendActivityNewTask(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, RecommendActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 风云榜
     */
    public synchronized final static void startBillboardActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, BillboardActivity.class);
        context.startActivity(intent);
    }

    /**
     * 礼包详情
     */
    public synchronized final static void startGiftDetailActivity(Context context, String id) {
        Intent intent = new Intent();
        intent.setClass(context, GiftDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    /**
     * 礼包详情
     */
    public synchronized final static void startGiftDetailActivityNewTask(Context context, String id) {
        Intent intent = new Intent();
        intent.setClass(context, GiftDetailActivity.class);
        intent.putExtra("id", id);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 圈子详情
     */
    public synchronized final static void startGroupDetailActivity(Context context, String group_id) {

        Intent intent = new Intent();
        intent.setClass(context, GroupDetailActivity.class);
        intent.putExtra("group_id", group_id);
        context.startActivity(intent);
    }

    /**
     * 圈子详情
     */
    public synchronized final static void startGroupDetailActivityNewTask(Context context, String group_id) {

        Intent intent = new Intent();
        intent.setClass(context, GroupDetailActivity.class);
        intent.putExtra("group_id", group_id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 游戏圈子列表
     */
    public synchronized final static void startGroupListActivity(Context context, GroupType groupType) {

        Intent intent = new Intent();
        intent.setClass(context, GroupListActivity.class);
        intent.putExtra("groupType", groupType);
        context.startActivity(intent);
    }

    /**
     * 游戏圈子礼包
     */
    public synchronized final static void startGroupGiftActivity(Context context, Game game) {

        Intent intent = new Intent();
        intent.setClass(context, GroupGiftActivity.class);
        intent.putExtra("game", game);
        context.startActivity(intent);
    }

    /**
     * 首页更多
     */
    public synchronized final static void startHomeMoreActivity(Context context, VideoImageGroup group) {
        Intent intent = new Intent();
        intent.setClass(context, HomeMoreActivity.class);
        intent.putExtra("group", group);
        context.startActivity(intent);
    }

    /**
     * 首页更多
     */
    public synchronized final static void startHomeMoreActivityNewTask(Context context, VideoImageGroup group) {
        Intent intent = new Intent();
        intent.setClass(context, HomeMoreActivity.class);
        intent.putExtra("group", group);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 图文详情
     */
    public synchronized final static void startImageDetailActivity(Context context, VideoImage item) {
        if (!StringUtil.isNull(item.getPic_id())) {
            String pic_id = item.getPic_id();
            item.setId(pic_id);
        }
        Intent intent = new Intent();
        intent.setClass(context, ImageDetailActivity.class);
        intent.putExtra("item", item);
        context.startActivity(intent);
    }

    /**
     * 登录
     */
    public synchronized final static void startLoginActivity(Context context) {

        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        context.startActivity(intent);
    }

    /**
     * 登录
     */
    public synchronized final static void startLoginActivityFromChat(Context context, String event_id) {

        Intent intent = new Intent();
        intent.putExtra("isFromChat", true);
        intent.putExtra("event_id", event_id);
        intent.setClass(context, LoginActivity.class);
        context.startActivity(intent);
    }

    /**
     * 主页
     */
    public synchronized final static void startMainActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        context.startActivity(intent);
    }

    /**
     * 主页
     */
    public synchronized final static void startMainActivityBottom(Activity activity) {
        startMainActivity(activity);
        activity.overridePendingTransition(R.anim.push_bottom_in, R.anim.activity_hold);
    }

    /**
     * 二维码扫描
     */
    public synchronized final static void startScanQRCodeActivity(Context context) {

        Intent intent = new Intent();
        intent.setClass(context, ScanQRCodeActivity.class);
        context.startActivity(intent);
    }

    /**
     * 视频拍摄
     */
    public synchronized final static void startCameraRecoedActivity(Context context) {

        startCameraRecoedActivity(context, null);
    }

    /**
     * 视频拍摄
     */
    public synchronized final static void startCameraRecoedActivity(Context context, Game game) {

        Intent intent = new Intent();
        intent.setClass(context, CameraRecoedActivity.class);
        if (game != null)
            intent.putExtra("game", game);
        context.startActivity(intent);
    }

    /**
     * 主页图文上传
     */
    public synchronized final static void startHomeImageShareActivity(Context context, Game game) {

        Intent intent = new Intent();
        intent.setClass(context, HomeImageShareActivity.class);
        if (game != null)
            intent.putExtra("game", game);
        context.startActivity(intent);
    }

    /**
     * 主页图文上传
     */
    public synchronized final static void startHomeImageShareActivity(Context context, Game game, boolean isHideLife) {

        Intent intent = new Intent();
        intent.setClass(context, HomeImageShareActivity.class);
        intent.putExtra("isHideLife", isHideLife);
        if (game != null)
            intent.putExtra("game", game);
        context.startActivity(intent);
    }

    /**
     * 主页
     */
    public synchronized final static void startMainActivityNewTask() {
        Context context = AppManager.getInstance().getContext();
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, MainActivity.class);
        context.startActivity(intent);
    }

    /**
     * 消息
     */
    public synchronized final static void startMessageActivity(Context context) {
        if (!PreferencesHepler.getInstance().isLogin()) {
            ToastHelper.s("请先登录");
            return;
        }
        Intent intent = new Intent();
        intent.setClass(context, MessageActivity.class);
        context.startActivity(intent);
    }

    /**
     * 消息
     */
    public synchronized final static void startMessageListActivity(Context context, int message) {
        Intent intent = new Intent();
        intent.setClass(context, MessageListActivity.class);
        intent.putExtra("message", message);
        context.startActivity(intent);
    }

    /**
     * 个人中心
     */
    public synchronized final static void startMyPersonalCenterActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MyPersonalCenterActivity.class);
        context.startActivity(intent);
    }

    /**
     * 个人资料
     */
    public synchronized final static void startMyPersonalInfoActivity(Context context) {

        Intent intent = new Intent();
        intent.setClass(context, MyPersonalInfoActivity.class);
        context.startActivity(intent);
    }

    /**
     * 我的关注，我的粉丝
     */
    public synchronized final static void startMyPlayerActivity(Context context, int page, String member_id) {

        Intent intent = new Intent();
        intent.setClass(context, MyPlayerActivity.class);
        intent.putExtra("page", page);
        intent.putExtra("member_id", member_id);
        context.startActivity(intent);
    }

    /**
     * 我的赛事
     */
    public synchronized final static void startMyMatchActivity(Context context) {
        if (!PreferencesHepler.getInstance().isLogin()) {
            ToastHelper.s("请先登录");
            return;
        }
        Intent intent = new Intent();
        intent.setClass(context, MyMatchActivity.class);
        context.startActivity(intent);
    }

    /**
     * 我的消息
     */
    public synchronized final static void startMyMessageActivity(Context context) {
        if (!PreferencesHepler.getInstance().isLogin()) {
            ToastHelper.s("请先登录");
            return;
        }
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().startConversationList(context);
        }
    }

    /**
     * 我的任务
     */
    public synchronized final static void startMyTaskActivity(Context context) {

        Intent intent = new Intent();
        intent.setClass(context, MyTaskActivity.class);
        context.startActivity(intent);
    }

    /**
     * 我的福利
     */
    public synchronized final static void startMyWelfareActivity(Context context) {
        if (!PreferencesHepler.getInstance().isLogin()) {
            ToastHelper.s("请先登录");
            return;
        }
        Intent intent = new Intent();
        intent.setClass(context, MyWelfareActivity.class);
        context.startActivity(intent);
    }

    /**
     * 单聊
     */
    public synchronized final static void startConversationActivity(Context context, String id, String title, boolean isFromCombat, String qq) {
        if (!PreferencesHepler.getInstance().isLogin()) {
            ToastHelper.s("请先登录");
            return;
        }
        Intent intent = new Intent(context, ConversationActivity.class);
        intent.putExtra("targetId", id);
        intent.putExtra("title", title);
        intent.putExtra("isFromCombat", isFromCombat);
        intent.putExtra("qq", qq);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 单聊
     */
    public synchronized final static void startConversationActivity(Context context,
                                                                    String id,
                                                                    String title,
                                                                    int conversationType) {
        if (!PreferencesHepler.getInstance().isLogin()) {
            ToastHelper.s("请先登录");
            return;
        }
        Intent intent = new Intent(context, ConversationActivity.class);
        intent.putExtra("targetId", id);
        intent.putExtra("title", title);
        intent.putExtra("conversationType", conversationType);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 单聊
     */
    public synchronized final static void startConversationActivity(Context context,
                                                                    String id,
                                                                    String title,
                                                                    int conversationType,
                                                                    String customerServiceID,
                                                                    String customerServiceName) {
        if (!PreferencesHepler.getInstance().isLogin()) {
            ToastHelper.s("请先登录");
            return;
        }
        Intent intent = new Intent(context, ConversationActivity.class);
        intent.putExtra("targetId", id);
        intent.putExtra("title", title);
        intent.putExtra("conversationType", conversationType);
        intent.putExtra("customerServiceID", customerServiceID);
        intent.putExtra("customerServiceName", customerServiceName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 单聊
     */
    public synchronized final static void startConversationActivity(Context context, String id, String title, boolean isFromCombat) {
        if (!PreferencesHepler.getInstance().isLogin()) {
            ToastHelper.s("请先登录");
            return;
        }
        Intent intent = new Intent(context, ConversationActivity.class);
        intent.putExtra("targetId", id);
        intent.putExtra("title", title);
        intent.putExtra("isFromCombat", isFromCombat);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 游戏赛事详情
     */
    public synchronized final static void startGameMatchDetailActivity(Context context, String event_id) {
        if (context == null || event_id == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("event_id", event_id);
        intent.setClass(context, GameMatchDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 游戏赛事详情
     */
    public synchronized final static void startGameMatchDetailActivityNewTask(Context context, String event_id) {
        if (context == null || event_id == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("isFromLogin", true);
        intent.putExtra("event_id", event_id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, GameMatchDetailActivity.class);
        context.startActivity(intent);
    }

    /**
     * 赛事报名
     */
    public synchronized final static void startSignUpActivity(Context context, String event_id,
                                                              Match match, String customer_service,
                                                              String name) {
        if (!PreferencesHepler.getInstance().isLogin()) {
            ToastHelper.s("请先登录");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("event_id", event_id);
        intent.putExtra("match", match);
        intent.putExtra("customer_service", customer_service);
        intent.putExtra("name", name);
        intent.setClass(context, SignUpActivity.class);
        context.startActivity(intent);
    }

    /**
     * 我的赛程
     */
    public synchronized final static void startMyMatchProcessActivity(Context context, Match match,
                                                                      String customer_service,
                                                                      String name) {
        if (!PreferencesHepler.getInstance().isLogin()) {
            ToastHelper.s("请先登录");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("match", match);
        intent.putExtra("customer_service", customer_service);
        intent.putExtra("name", name);
        intent.setClass(context, MyMatchProcessActivity.class);
        context.startActivity(intent);
    }

    /**
     * 我的赛程
     */
    public synchronized final static void startMyMatchProcessActivityNewTask(Context context, Match match,
                                                                             String customer_service,
                                                                             String name) {

        Intent intent = new Intent();
        intent.putExtra("match", match);
        intent.putExtra("customer_service", customer_service);
        intent.putExtra("name", name);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, MyMatchProcessActivity.class);
        context.startActivity(intent);
    }

    /**
     * 上传比赛结果截图
     */
    public synchronized final static void startUploadMatchResultImageActivity(Context context,
                                                                              Match match,
                                                                              String pk_id,
                                                                              String team_id,
                                                                              String over_time,
                                                                              String is_last,
                                                                              String customer_service,
                                                                              String name) {
        if (!PreferencesHepler.getInstance().isLogin()) {
            ToastHelper.s("请先登录");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("match", match);
        intent.putExtra("pk_id", pk_id);
        intent.putExtra("team_id", team_id);
        intent.putExtra("over_time", over_time);
        intent.putExtra("is_last", is_last);
        intent.putExtra("customer_service", customer_service);
        intent.putExtra("name", name);
        intent.setClass(context, UploadMatchResultImageActivity.class);
        context.startActivity(intent);
    }

    /**
     * 选择上传比赛视频
     */
    public synchronized final static void startVideoChooseActivity(Context context, Match match) {
        if (!PreferencesHepler.getInstance().isLogin()) {
            ToastHelper.s("请先登录");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("match", match);
        intent.setClass(context, VideoChooseActivity.class);
        context.startActivity(intent);
    }

    /**
     * 玩家动态
     */
    public synchronized final static void startPlayerDynamicActivity(Context context, Member record) {
        if (!StringUtil.isNull(record.getMember_id())) {
            record.setId(record.getMember_id());
        }
        if (record.getMember_id().equals(PreferencesHepler.getInstance().getMember_id())) {
            startMyPersonalCenterActivity(context);
            return;
        }
        Intent intent = new Intent();
        intent.setClass(context, PlayerDynamicActivity.class);
        intent.putExtra("member", record);
        context.startActivity(intent);
    }

    /**
     * 玩家个人资料
     */
    public synchronized static void startPlayerPersonalInfoActivity(Context context, Member record) {
        if (!StringUtil.isNull(record.getMember_id())) {
            record.setId(record.getMember_id());
        }
        if (record.getMember_id().equals(PreferencesHepler.getInstance().getMember_id())) {
            startMyPersonalInfoActivity(context);
            return;
        }

        Intent intent = new Intent();
        intent.setClass(context, PlayerPersonalInfoActivity.class);
        intent.putExtra("member", record);
        context.startActivity(intent);
    }

    /**
     * 隐私及免责声明
     */
    public synchronized final static void startPrivacyActivity(Context context) {
        Intent intent = new Intent(context, PrivacyActivity.class);
        context.startActivity(intent);
    }

    /**
     * 搜索
     */
    public synchronized final static void startSearchActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SearchActivity.class);
        context.startActivity(intent);
    }

    /**
     * 搜索结果
     */
    public synchronized final static void startSearchResultActivity(Context context, String content) {
        Intent intent = new Intent();
        intent.setClass(context, SearchResultActivity.class);
        intent.putExtra("content", content);
        context.startActivity(intent);
    }

    /**
     * 设置
     */
    public synchronized final static void startSettingActivity(Context context) {

        Intent intent = new Intent();
        intent.setClass(context, SettingActivity.class);
        context.startActivity(intent);
    }

    /**
     * 广场
     */
    public synchronized final static void startSquareActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SquareActivity.class);
        context.startActivity(intent);
    }

    /**
     * 我的动态
     */
    public synchronized final static void startMyDynamicActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MyDynamicActivity.class);
        context.startActivity(intent);
    }

    /**
     * 帮助与教程
     */
    public synchronized final static void startHelpActivity(Context context) {

        Intent intent = new Intent();
        intent.setClass(context, HelpActivity.class);
        context.startActivity(intent);
    }

    /**
     * 参赛视频
     */
    public static synchronized final void startActivityVideoActivity(Context context, Match item) {
        Intent intent = new Intent();
        intent.setClass(context, ActivityVideoActivity.class);
        intent.putExtra("item", item);
        context.startActivity(intent);
    }

    /**
     * 标签
     */
    public static void startTagActivity(Context context) {
        Log.d(TAG, "startTagActivity: ");
        Intent intent = new Intent();
        intent.setClass(context, TagActivity.class);
        context.startActivity(intent);
    }

}
