package com.li.videoapplication.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.View;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.entity.GroupType;
import com.li.videoapplication.data.model.entity.LaunchImage;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.SquareGameEntity;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.entity.VideoImageGroup;
import com.li.videoapplication.data.model.response.PlayWithOrderDetailEntity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.mvp.activity_gift.view.MyActivityListActivity;
import com.li.videoapplication.mvp.activity_gift.view.MyGiftListActivity;
import com.li.videoapplication.mvp.billboard.view.BillboardActivity;
import com.li.videoapplication.mvp.billboard.view.MatchRewardBillboardActivity;
import com.li.videoapplication.mvp.mall.view.MyBillDetailActivity;
import com.li.videoapplication.mvp.mall.view.PaymentWayActivity;
import com.li.videoapplication.mvp.mall.view.TopUpActivity;
import com.li.videoapplication.mvp.mall.view.TopUpRecordActivity;
import com.li.videoapplication.mvp.match.view.GameMatchDetailActivity;
import com.li.videoapplication.mvp.match.view.GroupMatchListActivity;
import com.li.videoapplication.mvp.match.view.MatchRecordActivity;
import com.li.videoapplication.mvp.match.view.MatchResultActivity;
import com.li.videoapplication.mvp.match.view.MyMatchActivity;
import com.li.videoapplication.mvp.match.view.MyMatchBettleActivity;
import com.li.videoapplication.mvp.match.view.PlayWithAndMatchActivity;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.activity.AboutActivity;
import com.li.videoapplication.ui.activity.ActivityDetailActivity;
import com.li.videoapplication.ui.activity.ActivityImageUploadActivity;
import com.li.videoapplication.ui.activity.ActivityListActivity;
import com.li.videoapplication.ui.activity.CameraRecoedActivity;
import com.li.videoapplication.ui.activity.CameraRecoedActivity50;
import com.li.videoapplication.ui.activity.ChoiceHomeTabActivity;
import com.li.videoapplication.ui.activity.CoachCommentDetailsActivity;
import com.li.videoapplication.ui.activity.CoachDetailActivity;
import com.li.videoapplication.ui.activity.CoachInfoEditActivity;
import com.li.videoapplication.ui.activity.CollectionActivity;
import com.li.videoapplication.ui.activity.CommentPlayWithOrderActivity;
import com.li.videoapplication.ui.activity.ConfirmOrderDoneActivity;
import com.li.videoapplication.ui.activity.ConversationActivity;
import com.li.videoapplication.ui.activity.CreatePlayWithOrderActivity;
import com.li.videoapplication.ui.activity.DownloadManagerActivity;
import com.li.videoapplication.ui.activity.ExchangeRecordActivity;
import com.li.videoapplication.ui.activity.FeedbackActivity;
import com.li.videoapplication.ui.activity.GiftDetailActivity;
import com.li.videoapplication.ui.activity.GiftListActivity;
import com.li.videoapplication.ui.activity.GroupDetailActivity;
import com.li.videoapplication.ui.activity.GroupDetailHybridActivity;
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
import com.li.videoapplication.ui.activity.MallActivity;
import com.li.videoapplication.ui.activity.MessageListActivity;
import com.li.videoapplication.ui.activity.MessageReplyActivity;
import com.li.videoapplication.ui.activity.MyCurrencyRecordActivity;
import com.li.videoapplication.ui.activity.MyDynamicActivity;
import com.li.videoapplication.ui.activity.MyGiftActivity;
import com.li.videoapplication.ui.activity.MyMatchProcessActivity;
import com.li.videoapplication.ui.activity.MyPersonalCenterActivity;
import com.li.videoapplication.ui.activity.MyPersonalInfoActivity;
import com.li.videoapplication.ui.activity.MyPlayerActivity;
import com.li.videoapplication.ui.activity.MyTaskActivity;
import com.li.videoapplication.ui.activity.MyWalletActivity;
import com.li.videoapplication.ui.activity.OrderDetailActivity;
import com.li.videoapplication.ui.activity.PersonalInfoEditActivity;
import com.li.videoapplication.ui.activity.PlayWithOrderDetailActivity;
import com.li.videoapplication.ui.activity.PlayerDynamicActivity;
import com.li.videoapplication.ui.activity.PlayerPersonalInfoActivity;
import com.li.videoapplication.ui.activity.PrivacyActivity;
import com.li.videoapplication.ui.activity.ProductsDetailActivity;
import com.li.videoapplication.ui.activity.RecommendActivity;
import com.li.videoapplication.ui.activity.RefundApplyActivity;
import com.li.videoapplication.ui.activity.ReportActivity;
import com.li.videoapplication.ui.activity.ReportResultActivity;
import com.li.videoapplication.ui.activity.RewardRankActivity;
import com.li.videoapplication.ui.activity.ScanQRCodeActivity;
import com.li.videoapplication.ui.activity.SearchActivity;
import com.li.videoapplication.ui.activity.SearchGameActivity;
import com.li.videoapplication.ui.activity.SearchLifeActivity;
import com.li.videoapplication.ui.activity.SettingActivity;
import com.li.videoapplication.ui.activity.ShareActivity;
import com.li.videoapplication.ui.activity.SignUpActivity;
import com.li.videoapplication.ui.activity.SquareActivity;
import com.li.videoapplication.ui.activity.SquareGameChoiceActivity;
import com.li.videoapplication.ui.activity.TagActivity;
import com.li.videoapplication.ui.activity.UploadMatchResultImageActivity;
import com.li.videoapplication.ui.activity.VideoChooseActivity;
import com.li.videoapplication.ui.activity.VideoEditorActivity2;
import com.li.videoapplication.ui.activity.VideoMangerActivity;
import com.li.videoapplication.ui.activity.VideoPlayActivity;
import com.li.videoapplication.ui.activity.VideoShare2Activity;
import com.li.videoapplication.ui.activity.VideoShareActivity;
import com.li.videoapplication.ui.activity.VideoUploadActivity;
import com.li.videoapplication.ui.fragment.GameFragment;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.utils.StringUtil;

import io.rong.imkit.RongIM;

/**
 * 功能：活动管理
 */
public class ActivityManager {

    private static String TAG = ActivityManager.class.getSimpleName();

    /**
     * 下载管理
     */
    public synchronized static void startDownloadManagerActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, DownloadManagerActivity.class);
        context.startActivity(intent);
    }

    /**
     * 下载管理
     */
    public synchronized static void startDownloadManagerActivity(Context context, String gameId, String location, String involveId) {
        Intent intent = new Intent();
        intent.putExtra("game_id", gameId);
        intent.putExtra("location", location);
        intent.putExtra("involve_Id", involveId);
        intent.setClass(context, DownloadManagerActivity.class);
        context.startActivity(intent);
    }

    /**
     * 下载管理
     */
    public synchronized static void startDownloadManagerActivity(Context context, LaunchImage entity) {
        Intent intent = new Intent();
        intent.setClass(context, DownloadManagerActivity.class);
        if (entity != null)
            intent.putExtra("entity", entity);
        context.startActivity(intent);
    }

    /**
     * 选择首页游戏
     */
    public synchronized static void startChoiceHomeTabActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, ChoiceHomeTabActivity.class);
        context.startActivity(intent);

    }

    /**
     * 视频管理
     */
    public synchronized static void startVideoMangerActivity(Context context) {
        startVideoMangerActivity(context, null, null);
    }

    /**
     * 视频管理
     */
    public synchronized static void startVideoMangerActivity(Context context, Game game) {
        startVideoMangerActivity(context, game, null);
    }

    /**
     * 视频管理
     */
    public synchronized static void startVideoMangerActivity(Context context, Match match) {
        startVideoMangerActivity(context, null, match);
    }

    /**
     * 视频管理
     */
    public synchronized static void startVideoMangerActivity(Context context, Game game, Match match) {
        Intent intent = new Intent();
        intent.setClass(context, VideoMangerActivity.class);
        if (game != null)
            intent.putExtra("game", game);
        if (game != null)
            intent.putExtra("match", match);
        context.startActivity(intent);
    }

    /**
     * 视频管理
     */
    public synchronized static void startVideoMangerActivityNewTask(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, VideoMangerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /******************************************/

    /**
     * 录屏后跳转 --> 视频分享
     */
    public static void startVideoShareActivity210NewTask(Context context, VideoCaptureEntity entity) {
        Intent intent = new Intent();
        if (entity != null)
            intent.putExtra("entity", entity);
        intent.setClass(context, VideoShareActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 视频上传
     */
    public synchronized static void startVideoShareActivity210(Context context, VideoCaptureEntity entity,
                                                               Game game, int to) {
        Intent intent = new Intent();
        if (entity != null)
            intent.putExtra("entity", entity);
        if (game != null)
            intent.putExtra("game", game);
        intent.putExtra("to", to);
        intent.setClass(context, VideoShareActivity.class);
        context.startActivity(intent);
    }

    public static void startVideoShare2Activity(Context context, VideoCaptureEntity entity) {
        Intent intent = new Intent(context, VideoShare2Activity.class);
        if (entity != null) {
            intent.putExtra("entity", entity);
        }
        context.startActivity(intent);
    }

    /**
     * 视频上传208
     */
    public synchronized static void startVideoUploadActivity(Context context, VideoCaptureEntity entity,
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
    public synchronized static void startImageShareActivity(Context context, String imageUrl) {
        startImageShareActivity(context, imageUrl, null);
    }

    /**
     * 图文分享
     */
    public synchronized static void startImageShareActivity(Context context, Game game) {
        startImageShareActivity(context, null, game);
    }

    /**
     * 图文分享
     */
    public synchronized static void startImageShareActivity(Context context, String imageUrl, Game game) {
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
    public synchronized static void startActivityImageUploadActivity(Context context, String match_id) {
        Intent intent = new Intent();
        intent.setClass(context, ActivityImageUploadActivity.class);
        intent.putExtra("match_id", match_id);
        context.startActivity(intent);
    }

    /******************************************/

    /**
     * 视频编辑
     */
    public synchronized static void startVideoEditorActivity(Context context, VideoCaptureEntity entity) {
        Intent intnet = new Intent(context, VideoEditorActivity2.class);
        if (entity != null)
            intnet.putExtra("entity", entity);
        context.startActivity(intnet);
    }

    /******************************************/

    /**
     * 查找游戏
     */
    public synchronized static void startSearchGameActivity(Activity activity) {
        Intent intent = new Intent(activity, SearchGameActivity.class);
        intent.putExtra("type", "game");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    /******************************************/

    /**
     * 查找查找精彩生活
     */
    public synchronized static void startSearchLifeActivity(Activity activity) {
        Intent intent = new Intent(activity, SearchLifeActivity.class);
        intent.putExtra("type", "life");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    /******************************************/

    /**
     * 图片选择
     */
    public synchronized static void startImageViewActivity(Context context) {
        Intent intent = new Intent(context, ImageViewActivity.class);
        context.startActivity(intent);
    }

    /******************************************/

    public static String SYSJ_URL = "http://sj.ifeimo.com/";
    public static String SYSJ_ICONURL = "http://apps.ifeimo.com/Public/Uploads/Game/Flag/576b9a94632f9.png";
    public static String SYSJ_TITLE = "手游视界";
    public static String SYSJ_CONTENT = "我刚刚发现了一个有趣的手游视频平台，可以和方便的观看所有手游的视频、攻略等，推荐你也来试试。";

    /**
     * 分享手游视界
     */
    public synchronized static void startActivityShareActivity4SYSJ(Activity activity) {
        Intent intent = new Intent(activity, ShareActivity.class);
        intent.putExtra("videoUrl", SYSJ_URL);
        intent.putExtra("VideoTitle", SYSJ_TITLE);
        intent.putExtra("imageUrl", SYSJ_ICONURL);
        intent.putExtra("content", SYSJ_CONTENT);
        intent.putExtra("page", ShareActivity.PAGE_SYSJ);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_bottom_in, R.anim.activity_hold);


    }

    /**
     * 分享视频播放
     */
    public synchronized static void startActivityShareActivity4VideoPlay(Activity activity, String videoUrl, String VideoTitle, String imageUrl, String content) {
        Intent intent = new Intent(activity, ShareActivity.class);
        intent.putExtra("videoUrl", videoUrl);
        intent.putExtra("VideoTitle", VideoTitle);
        intent.putExtra("imageUrl", imageUrl);
        intent.putExtra("content", content);
        intent.putExtra("page", ShareActivity.PAGE_VIDEOPLAY);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_bottom_in, R.anim.activity_hold);

    }

    /**
     * 分享云端视频
     */
    public synchronized static void startActivityShareActivity4MyCloudVideo(Activity activity, String url, String imageUrl, String videoTitle) {
        Intent intent = new Intent(activity, ShareActivity.class);
        intent.putExtra("videoUrl", url);
        intent.putExtra("imageUrl", imageUrl);
        intent.putExtra("VideoTitle", videoTitle);
        intent.putExtra("content", SYSJ_CONTENT);
        intent.putExtra("page", ShareActivity.PAGE_MYCLOUDVIDEO);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_bottom_in, R.anim.activity_hold);
    }

    /**
     * 分享本地图片
     */
    public synchronized static void startActivityShareActivity4MyScreenShot(Activity activity, String imageUrl, String videoTitle) {
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
    public synchronized static void startShareActivity4MyLocalVideo(Activity activity, boolean isLifeType) {
        Intent intent = new Intent(activity, ShareActivity.class);
        intent.putExtra("page", ShareActivity.PAGE_MYLOCALVIDEO);
        intent.putExtra("is_life_type", isLifeType);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_bottom_in, R.anim.activity_hold);
    }

    /******************************************/

    /**
     * 举报
     */
    public synchronized static void startReportActivity(Context context, VideoImage videoImage) {
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
        DataManager.TASK.videoClickVideo201(item.getVideo_id(), PreferencesHepler.getInstance().getMember_id());
    }

    /**
     * 视频详情
     */
    public synchronized static void startVideoPlayActivity(Context context, VideoImage item) {
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
        DataManager.TASK.videoClickVideo201(item.getVideo_id(), PreferencesHepler.getInstance().getMember_id());
    }

    /**
     * 视频详情
     */
    public synchronized static void startVideoPlayActivityNewTask(Context context, VideoImage item) {
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
    public synchronized static void startReportResultActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, ReportResultActivity.class);
        context.startActivity(intent);
    }

    /**
     * 收藏
     */
    public synchronized static void startCollectionActivity(Context context) {

        Intent intent = new Intent();
        intent.setClass(context, CollectionActivity.class);
        context.startActivity(intent);
    }

    /**
     * 关于
     */
    public synchronized static void startAboutActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, AboutActivity.class);
        context.startActivity(intent);
    }

    /**
     * 活动详情208
     */
    public synchronized static void startActivityDetailActivity(Context context, String match_id) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("match_id", match_id);
        intent.setClass(context, ActivityDetailActivity.class);
        context.startActivity(intent);
    }

    /**
     * 活动详情
     */
    public synchronized static void startActivityDetailActivityNewTask(Context context, String match_id) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("match_id", match_id);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, ActivityDetailActivity.class);
        context.startActivity(intent);
    }

    /**
     * 活动列表
     */
    public synchronized static void startActivityListActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, ActivityListActivity.class);
        context.startActivity(intent);
    }

    /**
     * 活动列表
     */
    public synchronized static void startGiftListActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, GiftListActivity.class);
        context.startActivity(intent);
    }

    /**
     * 精彩推荐
     */
    public synchronized static void startRecommendActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, RecommendActivity.class);
        context.startActivity(intent);
    }

    /**
     * 主播榜, 视频榜
     */
    public synchronized static void startBillboardActivity(Context context, int type) {
        Intent intent = new Intent();
        intent.setClass(context, BillboardActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    /**
     * 赛事奖金榜
     */
    public synchronized static void startMatchReswardBillboardActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MatchRewardBillboardActivity.class);
        context.startActivity(intent);
    }

    /**
     * 礼包详情
     */
    public synchronized static void startGiftDetailActivity(Context context, String id) {
        Intent intent = new Intent();
        intent.setClass(context, GiftDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    /**
     * 圈子详情
     */
    public synchronized static void startGroupDetailActivity(Context context, String group_id) {

        Intent intent = new Intent();
        if (GameFragment.isHybridPager(group_id)){
            intent.setClass(context, GroupDetailHybridActivity.class);
        }else {
            intent.setClass(context, GroupDetailActivity.class);
        }

        intent.putExtra("group_id", group_id);
        context.startActivity(intent);
    }


    /**
     * 圈子详情
     */
    public synchronized static void startGroupDetailActivityNewTask(Context context, String group_id) {

        Intent intent = new Intent();

        if (GameFragment.isHybridPager(group_id)){
            intent.setClass(context, GroupDetailHybridActivity.class);
        }else {
            intent.setClass(context, GroupDetailActivity.class);
        }

        intent.putExtra("group_id", group_id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 游戏圈子列表1
     */
    public synchronized static void startGroupListActivity(Context context, GroupType groupType) {

        Intent intent = new Intent();
        intent.setClass(context, GroupListActivity.class);
        intent.putExtra("groupType", groupType);
        context.startActivity(intent);

    }

    /**
     * 游戏圈子列表2
     */
    public synchronized static void startGroupListFragment(Context context, GroupType groupType) {
      

    }

    /**
     * 游戏圈子礼包
     */
    public synchronized static void startGroupGiftActivity(Context context, Game game) {

        Intent intent = new Intent();
        intent.setClass(context, GroupGiftActivity.class);
        intent.putExtra("game", game);
        context.startActivity(intent);
    }

    /**
     * 首页更多
     */
    public synchronized static void startHomeMoreActivity(Context context, VideoImageGroup group) {
        Intent intent = new Intent();
        intent.setClass(context, HomeMoreActivity.class);
        intent.putExtra("group", group);
        context.startActivity(intent);
    }

    /**
     * 首页更多
     */
    public synchronized static void startHomeMoreActivityNewTask(Context context, VideoImageGroup group) {
        Intent intent = new Intent();
        intent.setClass(context, HomeMoreActivity.class);
        intent.putExtra("group", group);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 图文详情
     */
    public synchronized static void startImageDetailActivity(Context context, VideoImage item) {
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
    public synchronized static void startLoginActivity(Context context) {

        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        context.startActivity(intent);
    }

    /**
     * 登录
     */
    public synchronized static void startLoginActivityFromChat(Context context, String event_id) {

        Intent intent = new Intent();
        intent.putExtra("isFromChat", true);
        intent.putExtra("event_id", event_id);
        intent.setClass(context, LoginActivity.class);
        context.startActivity(intent);
    }

    /**
     * 主页
     */
    public synchronized static void startMainActivity(Context context, int mainPosition, int matchPosition) {
        try {
            Intent intent = new Intent();
            intent.setClass(context, MainActivity.class);
            intent.putExtra("main_position", mainPosition);
            intent.putExtra("match_position", matchPosition);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 主页
     */
    public synchronized static void startMainActivity(Context context) {
        Log.d(TAG, "startMainActivity: ");
        try {
            Intent intent = new Intent();
            intent.setClass(context, MainActivity.class);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 主页
     */
    public synchronized static void startMainActivityBottom(Activity activity) {
        Log.d(TAG, "startMainActivityBottom: ");
        startMainActivity(activity);
        activity.overridePendingTransition(R.anim.push_bottom_in, R.anim.activity_hold);
    }

    /**
     * 二维码扫描
     */
    public synchronized static void startScanQRCodeActivity(Context context) {

        Intent intent = new Intent();
        intent.setClass(context, ScanQRCodeActivity.class);
        context.startActivity(intent);
    }

    /**
     * 视频拍摄4.+
     */
    public synchronized static void startCameraRecoedActivity(Context context) {

        startCameraRecoedActivity(context, null);
    }

    /**
     * 视频拍摄4.+
     */
    public synchronized static void startCameraRecoedActivity(Context context, Game game) {

        Intent intent = new Intent();
        intent.setClass(context, CameraRecoedActivity.class);
        if (game != null)
            intent.putExtra("game", game);
        context.startActivity(intent);
    }

    /**
     * 视频拍摄5.0以上
     */
    public synchronized static void startCameraRecoed50Activity(Context context) {

        startCameraRecoed50Activity(context, null);
    }

    /**
     * 视频拍摄5.0以上
     */
    public synchronized static void startCameraRecoed50Activity(Context context, Game game) {

        Intent intent = new Intent();
        intent.setClass(context, CameraRecoedActivity50.class);
        if (game != null)
            intent.putExtra("game", game);
        context.startActivity(intent);
    }

    /**
     * 主页图文上传
     */
    public synchronized static void startHomeImageShareActivity(Context context, Game game, boolean isHideLife) {

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
    public synchronized static void startMainActivityNewTask() {
        Context context = AppManager.getInstance().getContext();
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClass(context, MainActivity.class);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 消息
     */
    public synchronized static void startMessageListActivity(Context context, String title, String url, String type) {
        Intent intent = new Intent();
        intent.setClass(context, MessageListActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    /**
     * 消息
     */
    public synchronized static void startMessageReplyActivity(Context context, String relid) {
        Intent intent = new Intent();
        intent.setClass(context, MessageReplyActivity.class);
        intent.putExtra("relid", relid);
        context.startActivity(intent);
    }

    /**
     * 个人中心
     */
    public synchronized static void startMyPersonalCenterActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MyPersonalCenterActivity.class);
        context.startActivity(intent);
    }

    /**
     * 个人资料
     */
    public synchronized static void startMyPersonalInfoActivity(Context context) {

        Intent intent = new Intent();
        intent.setClass(context, MyPersonalInfoActivity.class);
        context.startActivity(intent);
    }

    /**
     * 编辑个人资料
     */
    public synchronized static void startPersonalInfoEditActivity(Context context, int editType) {

        Intent intent = new Intent();
        intent.setClass(context, PersonalInfoEditActivity.class);
        intent.putExtra("type", editType);
        context.startActivity(intent);
    }

    /**
     * 我的关注，我的粉丝
     */
    public synchronized static void startMyPlayerActivity(Context context, int page, String member_id) {
        Intent intent = new Intent();
        intent.setClass(context, MyPlayerActivity.class);
        intent.putExtra("page", page);
        intent.putExtra("member_id", member_id);
        context.startActivity(intent);
    }

    /**
     * 我的钱包
     */
    public synchronized static void startMyWalletActivity(Context context, int page) {
        if (!PreferencesHepler.getInstance().isLogin()) {
            DialogManager.showLogInDialog(context);
            return;
        }
        Intent intent = new Intent();
        intent.setClass(context, MyWalletActivity.class);
        intent.putExtra("page", page);
        context.startActivity(intent);
    }

    /**
     * 兑换记录
     */
    public synchronized static void startExchangeRecordActivity(Context context) {
        if (!PreferencesHepler.getInstance().isLogin()) {
            ToastHelper.s("请先登录");
            return;
        }
        Intent intent = new Intent();
        intent.setClass(context, ExchangeRecordActivity.class);
        context.startActivity(intent);
    }

    /**
     * 充值记录
     */
    public synchronized static void startTopUpRecordActivity(Context context) {
        if (!PreferencesHepler.getInstance().isLogin()) {
            ToastHelper.s("请先登录");
            return;
        }
        Intent intent = new Intent();
        intent.setClass(context, TopUpRecordActivity.class);
        context.startActivity(intent);
    }


    /**
     * @param context
     * @param money   充值金额
     * @param number  魔豆数量
     * @param entry   点击哪里的入口
     * @param use     用于魔豆充值还是会员开通
     * @param level   需要开通的VIP等级
     */
    public static void startPaymentWayActivity(Context context, float money, int number, int entry, int use, int level, int key) {
        if (!PreferencesHepler.getInstance().isLogin()) {
            ToastHelper.s("请先登录");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(PaymentWayActivity.MONEY, money);
        intent.putExtra(PaymentWayActivity.ENTRY, entry);
        intent.putExtra(PaymentWayActivity.NUMBER, number);
        intent.putExtra(PaymentWayActivity.USE, use);
        intent.putExtra(PaymentWayActivity.LEVEL, level);
        intent.putExtra(PaymentWayActivity.KEY, key);
        intent.setClass(context, PaymentWayActivity.class);
        context.startActivity(intent);
    }

    /**
     * 订单详情
     */
    public synchronized static void startOrderDetailActivity(Context context, String id, int tab) {
        if (!PreferencesHepler.getInstance().isLogin()) {
            ToastHelper.s("请先登录");
            return;
        }
        Intent intent = new Intent();
        intent.setClass(context, OrderDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("tab", tab);
        context.startActivity(intent);
    }

    /**
     * 商品详情
     */
    public synchronized static void startProductsDetailActivity(Context context, String goods_id, int showPage) {
        Intent intent = new Intent();
        intent.setClass(context, ProductsDetailActivity.class);
        intent.putExtra("goods_id", goods_id);
        intent.putExtra("showPage", showPage);

        context.startActivity(intent);
    }

    /**
     * 商城
     */
    public synchronized static void startMallActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MallActivity.class);
        context.startActivity(intent);
    }

    /**
     * 我的赛事
     */
    public synchronized static void startMyMatchActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MyMatchActivity.class);
        context.startActivity(intent);
    }

    /**
     * 我的活动
     */
    public synchronized static void startMyActivityListActivity(Context context) {
        if (!PreferencesHepler.getInstance().isLogin()) {
            DialogManager.showLogInDialog(context);
            return;
        }
        Intent intent = new Intent();
        intent.setClass(context, MyActivityListActivity.class);
        context.startActivity(intent);
    }

    /**
     * 我的礼包
     */
    public synchronized static void startMyGiftListActivity(Context context) {
        if (!PreferencesHepler.getInstance().isLogin()) {
            DialogManager.showLogInDialog(context);
            return;
        }
        Intent intent = new Intent();
        intent.setClass(context, MyGiftListActivity.class);
        context.startActivity(intent);
    }

    /**
     * 我的账单
     */
    public synchronized static void startMyCurrencyRecordActivity(Context context) {
        if (!PreferencesHepler.getInstance().isLogin()) {
            ToastHelper.s("请先登录");
            return;
        }
        Intent intent = new Intent();
        intent.setClass(context, MyCurrencyRecordActivity.class);
        context.startActivity(intent);
    }

    /**
     * 充值
     */
    public synchronized static void startTopUpActivity(Context context, int entry, int position) {
        if (!PreferencesHepler.getInstance().isLogin()) {
            ToastHelper.s("请先登录");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("entry", entry);
        intent.putExtra("position", position);
        intent.setClass(context, TopUpActivity.class);
        context.startActivity(intent);
    }


    /**
     * 魔豆/魔币账单
     */
    public synchronized static void startMyWalletBillActivity(Context context, int mode) {
        if (!PreferencesHepler.getInstance().isLogin()) {
            ToastHelper.s("请先登录");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("mode", mode);
        intent.setClass(context, MyBillDetailActivity.class);
        context.startActivity(intent);
    }

    /**
     * 我的消息
     */
    public synchronized static void startMyMessageActivity(Context context) {
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().startConversationList(context);
        }
    }

    /**
     * 我的任务
     */
    public synchronized static void startMyTaskActivity(Context context) {

        Intent intent = new Intent();
        intent.setClass(context, MyTaskActivity.class);
        context.startActivity(intent);
    }


    /**
     * 单聊
     */
    public synchronized static void startConversationActivity(Context context, String id, String title, boolean isFromCombat, String qq) {
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
    public synchronized static void startConversationActivity(Context context,
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
    public synchronized static void startConversationActivity(Context context,
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
    public synchronized static void startConversationActivity(Context context, String id, String title, boolean isFromCombat) {
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
     * 赛事结果214
     */
    public synchronized static void startMatchResultActivity(Context context, String event_id) {
        if (context == null || event_id == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("event_id", event_id);
        intent.setClass(context, MatchResultActivity.class);
        context.startActivity(intent);
    }

    /**
     * 历史战绩
     */
    public synchronized static void startMatchRecordActivity(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(context, MatchRecordActivity.class);
        context.startActivity(intent);
    }

    /**
     * 圈子赛事列表211
     */
    public synchronized static void startGroupMatchListActivity(Context context, String game_id) {
        if (context == null || game_id == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("game_id", game_id);
        intent.setClass(context, GroupMatchListActivity.class);
        context.startActivity(intent);
    }

    /**
     * 游戏赛事详情
     */
    public synchronized static void startGameMatchDetailActivity(Context context, String event_id) {
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
    public synchronized static void startGameMatchDetailActivityNewTask(Context context, String event_id) {
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
    public synchronized static void startSignUpActivity(Context context, String event_id,
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
//        intent.putExtra("format_type",format_type);
        intent.setClass(context, SignUpActivity.class);
        context.startActivity(intent);
    }

    /**
     * 我的赛程
     */
    public synchronized static void startMyMatchProcessActivity(Context context, Match match,
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
     * 我的赛程对战详情
     */
    public synchronized static void startMyMatchBettleActivity(Activity context,
                                                               String event_id,
                                                               String schedule_id,
                                                               View transitionView1,
                                                               String transitionName1,
                                                               View transitionView2,
                                                               String transitionName2) {
        Intent intent = new Intent();
        intent.putExtra("event_id", event_id);
        intent.putExtra("schedule_id", schedule_id);
        intent.setClass(context, MyMatchBettleActivity.class);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(context,
                Pair.create(transitionView1, transitionName1),
                Pair.create(transitionView2, transitionName2));

        ActivityCompat.startActivity(context, intent,
                options.toBundle());

    }

    /**
     * 我的赛程
     */
    public synchronized static void startMyMatchProcessActivityNewTask(Context context, Match match,
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
    public synchronized static void startUploadMatchResultImageActivity(Context context,
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
     * 选择视频
     */
    public synchronized static void startVideoChooseActivity(Context context, Match match, int to) {
      /*  if (!PreferencesHepler.getInstance().isLogin()) {
            ToastHelper.s("请先登录");
            return;
        }*/
        Intent intent = new Intent();
        intent.putExtra("match", match);
        intent.putExtra("to", to);
        intent.setClass(context, VideoChooseActivity.class);
        context.startActivity(intent);
    }

    /**
     * 选择视频
     * to 不传默认是跳视频管理
     * 带有游戏类型的，默认让它跳转视频管理，故不传to
     */
    public synchronized static void startVideoChooseActivity(Context context, Game game) {
        Intent intent = new Intent();
        intent.putExtra("game", game);
        intent.setClass(context, VideoChooseActivity.class);
        context.startActivity(intent);
    }

    /**
     * 玩家动态
     */
    public synchronized static void startPlayerDynamicActivity(Context context, Member record) {
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
    public synchronized static void startPrivacyActivity(Context context) {
        Intent intent = new Intent(context, PrivacyActivity.class);
        context.startActivity(intent);
    }

    /**
     * 搜索
     */
    public synchronized static void startSearchActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SearchActivity.class);
        context.startActivity(intent);
    }

    /**
     * 设置
     */
    public synchronized static void startSettingActivity(Context context) {

        Intent intent = new Intent();
        intent.setClass(context, SettingActivity.class);
        context.startActivity(intent);
    }

    /**
     * 广场
     */
    public synchronized static void startSquareActivity(Context context, String gameId) {
        Intent intent = new Intent();
        intent.setClass(context, SquareActivity.class);
        if (gameId != null) {
            intent.putExtra("game_id", gameId);
        }
        context.startActivity(intent);
    }

    /**
     * 我的动态
     */
    public synchronized static void startMyDynamicActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MyDynamicActivity.class);
        context.startActivity(intent);
    }

    /**
     * 帮助与教程
     */
    public synchronized static void startHelpActivity(Context context) {

        Intent intent = new Intent();
        intent.setClass(context, HelpActivity.class);
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

    /**
     * 视频编辑
     */
    public static void startVideoEditorActivity_2(Context context, VideoCaptureEntity entity) {
        Intent intnet = new Intent(context, VideoEditorActivity2.class);
        if (entity != null)
            intnet.putExtra("entity", entity);
        intnet.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intnet);
    }

    /**
     * 玩家广场游戏筛选
     */
    public static void startSquareGameChoiceActivity(Context context, SquareGameEntity entity) {

        Intent intent = new Intent();
        intent.setClass(context, SquareGameChoiceActivity.class);
        intent.putExtra("game", entity);
        context.startActivity(intent);
    }


    /**
     * 礼物流水
     */
    public static void startMyGiftBillActivity(Context context, String userId) {

        Intent intent = new Intent();
        intent.setClass(context, MyGiftActivity.class);
        intent.putExtra("user_id", userId);
        context.startActivity(intent);
    }


    /**
     * 教练详情
     */
    public static void startCoachDetailActivity(Context context,String memberId,String nickName,String avatar,String typeId) {

        Intent intent = new Intent();
        intent.setClass(context, CoachDetailActivity.class);
        intent.putExtra("member_id",memberId);
        intent.putExtra("nick_name",nickName);
        intent.putExtra("avatar",avatar);
        intent.putExtra("type_id",typeId);
        context.startActivity(intent);
    }

    /**
     * 生成陪玩订单
     */
    @Deprecated
    public static void startCreatePlayWithOrderActivity(Context context, String memberId, String nickName, String avatar, String QQ) {

        Intent intent = new Intent();
        intent.setClass(context, CreatePlayWithOrderActivity.class);
        intent.putExtra("coach_id", memberId);
        intent.putExtra("nick_name", nickName);
        intent.putExtra("avatar", avatar);
        intent.putExtra("qq", QQ);
        context.startActivity(intent);
    }


    /**
     * 生成陪玩订单(抢单模式)
     */
    public static void startCreatePlayWithOrderActivity(Context context,int mode,String memberId,String nickName,String avatar,String QQ,String typeId,String gameName) {
        Intent intent = new Intent();
        intent.setClass(context, CreatePlayWithOrderActivity.class);
        intent.putExtra("order_mode", mode);

        if (!StringUtil.isNull(memberId)) {
            intent.putExtra("coach_id", memberId);
        }

        if (!StringUtil.isNull(nickName)) {
            intent.putExtra("nick_name", nickName);
        }

        if (!StringUtil.isNull(avatar)) {
            intent.putExtra("avatar", avatar);
        }

        if (!StringUtil.isNull(QQ)) {
            intent.putExtra("qq", QQ);
        }

        if (!StringUtil.isNull(typeId)){
            intent.putExtra("type_id",typeId);
        }

        if (!StringUtil.isNull(gameName)){
            intent.putExtra("game_name",gameName);
        }

        context.startActivity(intent);
    }


    /**
     * 陪玩订单与赛事
     */
    public static void startPlayWithOrderAndMatchActivity(Context context, int position, int orderPosition) {
        Intent intent = new Intent();
        intent.setClass(context, PlayWithAndMatchActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("order_position", orderPosition);
        context.startActivity(intent);
    }

    /**
     * 陪玩订单详情
     */
    public static void startPlayWithOrderDetailActivity(Context context, String orderId, int role, boolean isShowCoach) {
        Intent intent = new Intent();
        intent.setClass(context, PlayWithOrderDetailActivity.class);
        intent.putExtra("order_id", orderId);
        intent.putExtra("role", role);
        intent.putExtra("is_show_coach", isShowCoach);
        context.startActivity(intent);
    }

    /**
     * 陪玩订单评价
     */
    public static void startPlayWithOrderCommentActivity(Context context, String coachId, String nickName, String avatar, String score, String orderId) {
        Intent intent = new Intent();
        intent.setClass(context, CommentPlayWithOrderActivity.class);
        intent.putExtra("coach_id", coachId);
        intent.putExtra("nick_name", nickName);
        intent.putExtra("avatar", avatar);
        intent.putExtra("score", score);
        intent.putExtra("order_id", orderId);
        context.startActivity(intent);
    }

    /**
     * 确认完成订单
     */


    public static void startConfirmOrderDoneActivity(Context context,String nickName,String avatar,String orderId,String count,String orderCount,String typeId){
        Intent intent = new Intent();
        intent.setClass(context, ConfirmOrderDoneActivity.class);
        intent.putExtra("order_id",orderId);
        intent.putExtra("nick_name",nickName);
        intent.putExtra("avatar",avatar);
        intent.putExtra("count",count);
        intent.putExtra("order_count",orderCount);
        intent.putExtra("type_id",typeId);
        context.startActivity(intent);
    }

    /**
     * 退款申请
     */

    public static void startRefundApplyActivity(Context context, PlayWithOrderDetailEntity entity, String orderId) {
        Intent intent = new Intent();
        intent.setClass(context, RefundApplyActivity.class);
        intent.putExtra("order_detail", entity);
        intent.putExtra("order_id", orderId);
        context.startActivity(intent);
    }


    /**
     * 阿里百川反馈页面
     */
    public static void startFeedbackActivity(Context context) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        context.startActivity(intent);
    }

    /**
     * 打赏榜
     */
    public static void startRewardRankActivity(Context context) {
        Intent intent = new Intent(context, RewardRankActivity.class);
        context.startActivity(intent);
    }

    /**
     * 编辑陪练信息
     */
    public static void startCoachInfoEditActivity(Context context) {
        Intent intent = new Intent(context, CoachInfoEditActivity.class);
        context.startActivity(intent);
    }

    /**
     * 陪练全部评价
     */
    public static void startCoachAllCommentActivity(Context context, String coachId) {
        Intent intent = new Intent(context, CoachCommentDetailsActivity.class);
        intent.putExtra("coach_id", coachId);
        context.startActivity(intent);
    }

}
