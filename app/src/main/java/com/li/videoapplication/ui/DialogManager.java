package com.li.videoapplication.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.preferences.Constants;
import com.li.videoapplication.data.preferences.NormalPreferences;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.activity.GameMatchDetailActivity;
import com.li.videoapplication.ui.activity.SignUpActivity;
import com.li.videoapplication.ui.activity.VideoMangerActivity;
import com.li.videoapplication.ui.dialog.DiscoverTipDialog;
import com.li.videoapplication.ui.dialog.EditNameDialog;
import com.li.videoapplication.ui.dialog.Exp2ShidouCenterDialog;
import com.li.videoapplication.ui.dialog.Exp2ShidouTaskDialog;
import com.li.videoapplication.ui.dialog.GameDetailDialog;
import com.li.videoapplication.ui.dialog.GameTipDialog;
import com.li.videoapplication.ui.dialog.MatchOpponentDialog;
import com.li.videoapplication.ui.dialog.MyTaskGrowupDialog;
import com.li.videoapplication.ui.dialog.MyTaskLevelDialog;
import com.li.videoapplication.ui.dialog.OnGoingContactTipDialog;
import com.li.videoapplication.ui.dialog.OnGoingUploadImageTipDialog;
import com.li.videoapplication.ui.dialog.PhotoDialog;
import com.li.videoapplication.ui.dialog.RecordDialog;
import com.li.videoapplication.ui.dialog.RegisterMobileDialog;
import com.li.videoapplication.ui.dialog.SettingDialog;
import com.li.videoapplication.ui.dialog.ShareDialog;
import com.li.videoapplication.ui.dialog.SignInSuccessDialog;
import com.li.videoapplication.ui.dialog.SignUpSuccessDialog;
import com.li.videoapplication.ui.dialog.UploadVideoDialog;
import com.li.videoapplication.ui.dialog.VictoryDialog;
import com.li.videoapplication.ui.dialog.VideoManagerCopyDialog;
import com.li.videoapplication.ui.dialog.VideoManagerRenameDialog;
import com.li.videoapplication.ui.dialog.VideoPlayDialog;
import com.li.videoapplication.ui.dialog.VideoTipDialog;

/**
 * 功能：弹框管理
 */
public class DialogManager {

    /**
     * 获胜
     */
    public static void showVictoryDialog(Context context, String next_time, boolean isLast, String schedule_id) {
        Dialog dialog = new VictoryDialog(context, next_time, isLast, schedule_id);
        dialog.show();
    }

    /**
     * 签到成功
     */
    public static void showSignInSuccessDialog(GameMatchDetailActivity activity,
                                               String schedule_starttime,
                                               String schedule_endtime) {
        Dialog dialog = new SignInSuccessDialog(activity, schedule_starttime, schedule_endtime);
        dialog.show();
    }

    /**
     * 报名成功
     */
    public static void showSignUpSuccessDialog(SignUpActivity activity,
                                               String sign_starttime,
                                               String sign_endtime,
                                               String schedule_starttime) {
        Dialog dialog = new SignUpSuccessDialog(activity, sign_starttime, sign_endtime, schedule_starttime);
        dialog.show();
    }

    /**
     * 开始匹配对手
     */
    public static void showMatchOpponentDialog(Context context, String schedule_id) {
        Dialog dialog = new MatchOpponentDialog(context, schedule_id);
        dialog.show();
    }

    /**
     * 编辑昵称
     */
    public static void showEditNameDialog(Context context, String oldName, EditNameDialog.NameCallback listener) {
        Dialog dialog = new EditNameDialog(context, oldName, listener);
        dialog.show();
    }

    /**
     * 编辑手机号码验证
     */
    public static void showRegisterMobileDialog(Context context, String oldMobile, RegisterMobileDialog.MobileCallback listener) {
        Dialog dialog = new RegisterMobileDialog(context, oldMobile, listener);
        dialog.show();
    }

    /**
     * 重命名本地视频，截图
     */
    public static void showVideoManagerRenameDialog(String oldFileName, VideoManagerRenameDialog.Callback listener) {
        Activity activity = AppManager.getInstance().getActivity(VideoMangerActivity.class);
        if (activity != null) {
            Dialog dialog = new VideoManagerRenameDialog(activity, oldFileName, listener);
            dialog.show();
        }
    }

    /**
     * 复制本地视频
     */
    public static void showVideoManagerCopyDialog(Context context, String string, VideoManagerCopyDialog.Callback listener) {
        Dialog dialog = new VideoManagerCopyDialog(context, string, listener);
        dialog.show();
    }

    /**
     * 上传视频
     */
    public static void showUploadVideoDialog(Context context, DialogInterface.OnClickListener listener) {
        Dialog dialog = new UploadVideoDialog(context, listener);
        dialog.show();
    }

    /**
     * 任务
     */
    public static void showMyPersonalInfoGrowupDialog(Context context) {
        Dialog dialog = new MyTaskGrowupDialog(context);
        dialog.show();
    }

    /**
     * 我的任务
     */
    public static void showMyPersonalInfoLevelDialog(Context context) {
        Dialog dialog = new MyTaskLevelDialog(context);
        dialog.show();
    }

    /**
     * 游戏详情
     */
    public static void showGameDetailDialog(Context context, Game game) {
        Dialog dialog = new GameDetailDialog(context, game);
        dialog.show();
    }

    /**
     * 录屏
     */
    public static void showRecordDialog(Context context) {
        Dialog dialog = new RecordDialog(context);
        dialog.show();
        UmengAnalyticsHelper.onEvent(context, UmengAnalyticsHelper.MAIN, "首页-录制框打开次数");
    }

    /**
     * 相册
     */
    public static void showPhotoDialog(Context context, View.OnClickListener pickClickListener, View.OnClickListener takeClickListener) {
        Dialog dialog = new PhotoDialog(context, pickClickListener, takeClickListener);
        dialog.show();
    }

    /**
     * 举报
     */
    public static void showVideoPlayDialog(Context context, VideoImage videoImage) {
        Dialog dialog = new VideoPlayDialog(context, videoImage);
        dialog.show();
    }

    /**
     * 分享
     */
    public static void showShareDialog(Context context, String videoUrl, String imageUrl, String VideoTitle, String text) {
        Dialog dialog = new ShareDialog(context, videoUrl, imageUrl, VideoTitle, text);
        dialog.show();
    }

    /**
     * 音量，亮度设置
     */
    public static void showSettingDialog(Context context) {
        Dialog dialog = new SettingDialog(context);
        dialog.show();
    }

    /**
     * 视频遮罩
     */
    public static void showVideoTipDialog(Context context) {
        Dialog dialog = new VideoTipDialog(context);
        dialog.show();
    }

    /**
     * 游戏遮罩
     */
    public static void showGameTipDialogg(Context context) {
        Dialog dialog = new GameTipDialog(context);
        dialog.show();
    }

    /**
     * 发现遮罩
     */
    public static void showDiscoverTipDialog(Context context) {
        Dialog dialog = new DiscoverTipDialog(context);
        dialog.show();
    }

    /**
     * 进行中约战遮罩
     */
    public static void showOnGoingContactTipDialog(Context context) {
        Dialog dialog = new OnGoingContactTipDialog(context);
        dialog.show();
    }

    /**
     * 进行中上传截图遮罩
     */
    public static void showOnGoingUploadImageTipDialog(Context context/*,int btnLeft,int btnTop*/) {
        Dialog dialog = new OnGoingUploadImageTipDialog(context);
        dialog.show();
    }

    /**
     * 提示对话框：经验转视豆
     */
    public static void showChangeExp2ShidouDialog_Center(Context context) {

        boolean tip = NormalPreferences.getInstance().getBoolean(Constants.CHANGE_EXPTOSHIDOU_CENTER, true);
        if (tip) {
            Dialog dialog = new Exp2ShidouCenterDialog(context);
            dialog.show();

            NormalPreferences.getInstance().putBoolean(Constants.CHANGE_EXPTOSHIDOU_CENTER, false);
        }
    }

    /**
     * 提示对话框：经验转视豆
     */
    public static void showChangeExp2ShidouDialog_Task(Context context) {

        boolean tip = NormalPreferences.getInstance().getBoolean(Constants.CHANGE_EXPTOSHIDOU_TASK, true);
        if (tip) {
            Dialog dialog = new Exp2ShidouTaskDialog(context);
            dialog.show();

            NormalPreferences.getInstance().putBoolean(Constants.CHANGE_EXPTOSHIDOU_TASK, false);
        }
    }
}
