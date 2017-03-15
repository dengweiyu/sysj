package com.li.videoapplication.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.fmsysj.screeclibinvoke.ui.dialog.ManufacturerDialog;
import com.fmsysj.screeclibinvoke.ui.dialog.SettingQualityDialog;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.entity.Update;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.response.GameCateEntity;
import com.li.videoapplication.data.model.response.RecommendedLocationEntity;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.mvp.match.view.GameMatchDetailActivity;
import com.li.videoapplication.ui.activity.SignUpActivity;
import com.li.videoapplication.ui.activity.VideoMangerActivity;
import com.li.videoapplication.ui.dialog.ClassifiedGameDialog;
import com.li.videoapplication.ui.dialog.DiscoverTipDialog;
import com.li.videoapplication.ui.dialog.EditNameDialog;
import com.li.videoapplication.ui.dialog.EditQQDialog;
import com.li.videoapplication.ui.dialog.EditSexDialog;
import com.li.videoapplication.ui.dialog.FileDownloaderDialog;
import com.li.videoapplication.ui.dialog.GameDetailDialog;
import com.li.videoapplication.ui.dialog.GameTipDialog;
import com.li.videoapplication.ui.dialog.Jump2Dialog;
import com.li.videoapplication.ui.dialog.LogInDialog;
import com.li.videoapplication.ui.dialog.LogInTaskDoneDialog;
import com.li.videoapplication.ui.dialog.MatchFliterDialog;
import com.li.videoapplication.ui.dialog.MatchOpponentDialog;
import com.li.videoapplication.ui.dialog.MyTaskGrowupDialog;
import com.li.videoapplication.ui.dialog.MyTaskLevelDialog;
import com.li.videoapplication.ui.dialog.OfficialPaymentDialog;
import com.li.videoapplication.ui.dialog.OnGoingContactTipDialog;
import com.li.videoapplication.ui.dialog.OnGoingUploadImageTipDialog;
import com.li.videoapplication.ui.dialog.PaymentDialog;
import com.li.videoapplication.ui.dialog.PhotoDialog;
import com.li.videoapplication.ui.dialog.RecordDialog;
import com.li.videoapplication.ui.dialog.RegisterMobileDialog;
import com.li.videoapplication.ui.dialog.SettingDialog;
import com.li.videoapplication.ui.dialog.ShareDialog;
import com.li.videoapplication.ui.dialog.SignInSuccessDialog;
import com.li.videoapplication.ui.dialog.SignUpQuestionDialog;
import com.li.videoapplication.ui.dialog.SignUpSuccessDialog;
import com.li.videoapplication.ui.dialog.UpdateDialog;
import com.li.videoapplication.ui.dialog.UploadPicDialog;
import com.li.videoapplication.ui.dialog.UploadVideoDialog;
import com.li.videoapplication.ui.dialog.VictoryDialog;
import com.li.videoapplication.ui.dialog.VideoManagerCopyDialog;
import com.li.videoapplication.ui.dialog.VideoManagerImportDialog;
import com.li.videoapplication.ui.dialog.VideoManagerRenameDialog;
import com.li.videoapplication.ui.dialog.VideoPlayDialog;
import com.li.videoapplication.ui.dialog.VideoTipDialog;
import com.li.videoapplication.ui.fragment.MyLocalVideoFragment;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 功能：弹框管理
 */
public class DialogManager {
    /**
     * 每日登陆（我的钱包跳转各页面提示框）
     */
    public static void showLogInTaskDoneDialog(Context context) {
        Dialog dialog = new LogInTaskDoneDialog(context);
        dialog.show();
    }

    /**
     * 跳转（我的钱包跳转各页面提示框）
     */
    public static void showJump2Dialog(Context context, int toWhere) {
        Dialog dialog = new Jump2Dialog(context, toWhere);
        dialog.show();
    }

    /**
     * 跳转主页（我的钱包跳转提示框）
     */
    public static void showJump2HomeDialog(Context context, String name) {
        Dialog dialog = new Jump2Dialog(context, name);
        dialog.show();
    }

    /**
     * 游戏分类
     */
    public static void showClassifiedGameDialog(Context context, String sort,
                                                View.OnClickListener hotClickListener,
                                                View.OnClickListener newClickListener) {
        Dialog dialog = new ClassifiedGameDialog(context, sort, hotClickListener, newClickListener);
        dialog.show();
    }

    /**
     * 文件下载
     */
    public static void showFileDownloaderDialog(Context context, View.OnClickListener continueListener,
                                                View.OnClickListener wifiListener) {
        Dialog dialog = new FileDownloaderDialog(context, continueListener, wifiListener);
        dialog.show();
    }

    /**
     * 版本更新
     */
    public static void showUpdateDialog(Context context, Update update) {
        Dialog dialog = new UpdateDialog(context, update);
        dialog.show();
    }

    /**
     * 赛事筛选
     */
    public static MatchFliterDialog showMatchFliterDialog(Context context, GameCateEntity data) {
        MatchFliterDialog dialog = new MatchFliterDialog(context, data);
        dialog.show();
        return dialog;
    }

    /**
     * 赛事截图上传
     */
    public static void showUploadPicDialog(Context context) {
        Dialog dialog = new UploadPicDialog(context);
        dialog.show();
    }

    /**
     * 赛事报名问号
     */
    public static void showSignUpQuestionDialog(Context context) {
        Dialog dialog = new SignUpQuestionDialog(context);
        dialog.show();
    }

    /**
     * 登陆
     */
    public static void showLogInDialog(Context context) {
        Dialog dialog = new LogInDialog(context);
        dialog.show();
    }

    /**
     * 申请官方推荐
     */
    public static void showOfficialPaymentDialog(Context context, RecommendedLocationEntity event) {
        Dialog dialog = new OfficialPaymentDialog(context, event);
        dialog.show();
    }

    /**
     * 申请官方推荐
     */
    public static void showOfficialPaymentDialog(Context context, RecommendedLocationEntity event,String video_id) {
        Dialog dialog = new OfficialPaymentDialog(context, event, video_id);
        dialog.show();
    }

    /**
     * 兑换商品
     */
    public static void showPaymentDialog(Context context, Currency childList) {
        Dialog dialog = new PaymentDialog(context, childList);
        dialog.show();
    }

    /**
     * 导入外部视频
     */
    public static void showVideoManagerImportDialog(Context context,
                                                    List<VideoCaptureEntity> data,
                                                    MyLocalVideoFragment fragment,
                                                    DialogInterface.OnClickListener listener) {
        Dialog dialog = new VideoManagerImportDialog(context, data, fragment, listener);
        dialog.show();
    }

    /**
     * 悬浮窗设置
     */
    public static void showManufacturerDialog(Context context) {
        Dialog dialog = new ManufacturerDialog(context);
        dialog.show();
    }

    /**
     * 清晰度设置
     */
    public static void showSettingQualityDialog(Context context, SettingQualityDialog.Qualityable listener) {
        Dialog dialog = new SettingQualityDialog(context, listener);
        dialog.show();
    }

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
    public static void showRegisterMobileDialog(Context context) {
        final RegisterMobileDialog dialog = new RegisterMobileDialog(context);
        dialog.show();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                dialog.showKeyboard();
            }
        }, 200);
    }

    /**
     * 编辑qq
     */
    public static void showEditQQDialog(Context context) {
        final EditQQDialog dialog = new EditQQDialog(context);
        dialog.show();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                dialog.showKeyboard();
            }
        }, 200);
    }

    /**
     * 编辑性别
     */
    public static void showEditSexDialog(Context context) {
        Dialog dialog = new EditSexDialog(context);
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
     * 首页录制框
     */
    public static void showRecordDialog(Context context) {
        Dialog dialog = new RecordDialog(context);
        dialog.show();
        UmengAnalyticsHelper.onEvent(context, UmengAnalyticsHelper.MAIN, "首页-录制框打开次数");
    }

    /**
     * 活动详情录制框
     */
    public static RecordDialog showActivityRecordDialog(Context context,
                                                        RecordDialog.IDialogVisableListener visableListener,
                                                        RecordDialog.IOnClickListener onClickListener) {
        RecordDialog dialog = new RecordDialog(context, visableListener,onClickListener);
        dialog.show();
        return dialog;
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
}
