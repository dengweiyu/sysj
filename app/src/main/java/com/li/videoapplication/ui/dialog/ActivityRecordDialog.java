package com.li.videoapplication.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.fmsysj.screeclibinvoke.utils.RootUtil;
import com.ifeimo.screenrecordlib.RecordingManager;
import com.li.videoapplication.R;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.BaseDialog;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.activity.MainActivity;
import com.li.videoapplication.ui.activity.VideoShareActivity;
import com.li.videoapplication.utils.AppUtil;

/**
 * 活动页面 视频录制弹框
 */

public class ActivityRecordDialog extends BaseDialog implements View.OnClickListener {

    private RecordDialog.IOnClickListener onClickListener;
    private RecordDialog.IDialogVisableListener visableListener;
    private MainActivity activity;
    private boolean isLogin;
    public ActivityRecordDialog(Context context) {
        super(context);
    }

    public ActivityRecordDialog(Context context, int theme) {
        super(context, theme);
    }


    public ActivityRecordDialog(Context context, RecordDialog.IDialogVisableListener visableListener, RecordDialog.IOnClickListener onClickListener) {
        super(context, R.style.MyDialog);
        this.visableListener = visableListener;
        this.onClickListener = onClickListener;
        activity = AppManager.getInstance().getMainActivity();
    }

    /**
     * 跳转：登录
     */
    private void startLoginDialog() {
        DialogManager.showLogInDialog(activity);
    }

    /**
     * 跳转：外拍（5.0以上）
     */
    private void startCameraRecoed50Activity() {
        ActivityManeger.startCameraRecoed50Activity(activity);
        UmengAnalyticsHelper.onEvent(activity, UmengAnalyticsHelper.MAIN, "发布-拍摄-点击右上角发布按钮后点击拍摄视频按钮");
    }

    /**
     * 跳转：外拍（5.0以下）
     */
    private void startCameraRecoedActivity() {
        ActivityManeger.startCameraRecoedActivity(activity);
        UmengAnalyticsHelper.onEvent(activity, UmengAnalyticsHelper.MAIN, "发布-拍摄-点击右上角发布按钮后点击拍摄视频按钮");
    }

    /**
     * 跳转：图文分享
     */
    private void startHomeImageShareActivity() {
        ActivityManeger.startHomeImageShareActivity(activity, null, false);
        UmengAnalyticsHelper.onEvent(activity, UmengAnalyticsHelper.MAIN, "发布-图文-点击右上角发布按钮后点击图文按钮");
    }

    /**
     * 跳转：选择上传视频
     */
    private void startVideoChooseActivity() {
        ActivityManeger.startVideoChooseActivity(getContext(), null, VideoShareActivity.TO_VIDEOMANAGER);
        UmengAnalyticsHelper.onEvent(activity, UmengAnalyticsHelper.MAIN, "发布-视频-点击右上角发布按钮后点击视频按钮");
    }

    @Override
    protected int getContentView() {
        return R.layout.popup_record208;
    }

    @Override
    protected void afterContentView(Context context) {
        super.afterContentView(context);

        Window window = getWindow();
         window.setWindowAnimations(R.style.slideTopAnim); // 设置窗口弹出动画
        //    window.setBackgroundDrawableResource(android.R.color.transparent); // 设置对话框背景为透明
        WindowManager.LayoutParams params = window.getAttributes();
        // 根据x，y坐标设置窗口需要显示的位置
        params.x = 0; // x小于0左移，大于0右移
        params.y = 0; // y小于0上移，大于0下移
        params.gravity = Gravity.TOP; // 设置重力
        window.setAttributes(params);

        isLogin = PreferencesHepler.getInstance().isLogin();

        View record =  findViewById(R.id.record_video);
        record.setOnClickListener(this);

        final View recordRecord = findViewById(R.id.record_record);
        recordRecord.setOnClickListener(this);


        final  View local = findViewById(R.id.record_local);
        local.setOnClickListener(this);


        final  View image = findViewById(R.id.record_image);
        image.setOnClickListener(this);

        final View close = findViewById(R.id.record_close);
        close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record_video://录屏
                activity.startScreenRecordActivity();

                break;

            case R.id.record_record://外拍

                if (activity == null)
                    return;
                if (RecordingManager.getInstance().isRecording()) {
                    ToastHelper.s("正在录屏中");
                    return;
                }
                if (isLogin) {
                    int SDKVesion = AppUtil.getAndroidSDKVersion();
                    // FIXME: CameraGLView第565行，setPreviewSize（预览宽高有几个固定比例等级），
                    // FIXME: 华为的垃圾机屏幕分辨率不是正常比例来的(因为有条垃圾返回键等底栏) 所以会抛异常。没空先不给华为进入先，看到的大神有空修一下
                    if (SDKVesion >= 21 && !RootUtil.getManufacturer().equals("HUAWEI")) {
                        startCameraRecoed50Activity();
                    } else {
                        startCameraRecoedActivity();
                    }
                } else {
                    startLoginDialog();
                }
                break;

            case R.id.record_image://图文

                if (isLogin) {
                    if (onClickListener != null) {
                        onClickListener.onUploadImageClick();
                    } else {
                        if (activity != null)
                            startHomeImageShareActivity();
                    }
                } else {
                    startLoginDialog();
                }
                break;

            case R.id.record_local://视频

                if (isLogin) {
                    if (onClickListener !=null){
                        onClickListener.onUploadVideoClick();
                    }else {
                        if (activity != null)
                            startVideoChooseActivity();
                    }
                } else {
                    startLoginDialog();
                }
                break;
            case R.id.record_close:

                break;
        }
        cancel();
    }

    @Override
    public void cancel() {
        super.cancel();
        if (visableListener != null)
            visableListener.dialogCanceled();
    }

    @Override
    public void show() {
        super.show();
        if (visableListener != null)
            visableListener.dialogShowed();
    }
}
