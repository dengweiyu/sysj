package com.li.videoapplication.ui.dialog;

import android.content.Context;
import android.view.View;

import com.ifeimo.screenrecordlib.RecordingManager;
import com.li.videoapplication.R;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.BaseTopDialog;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.MainActivity;
import com.li.videoapplication.ui.toast.ToastHelper;
import com.li.videoapplication.utils.AppUtil;

/**
 * 弹框：首页录制
 */
public class RecordDialog extends BaseTopDialog implements View.OnClickListener {

    private MainActivity activity;
    private boolean isLogin;

    /**
     * 跳转：登录
     */
    private void startLoginActivity() {
        ActivityManeger.startLoginActivity(getContext());
    }

    /**
     * 跳转：外拍（5.0以上）
     */
    private void startCameraRecoed50Activity() {
        ActivityManeger.startCameraRecoed50Activity(activity);
    }

    /**
     * 跳转：外拍（5.0以下）
     */
    private void startCameraRecoedActivity() {
        ActivityManeger.startCameraRecoedActivity(activity);
    }

    /**
     * 跳转：图文分享
     */
    private void startHomeImageShareActivity() {
        ActivityManeger.startHomeImageShareActivity(activity, null, false);
    }

    /**
     * 跳转：视频管理
     */
    private void startVideoMangerActivity() {
        ActivityManeger.startVideoMangerActivity(activity);
    }



    public RecordDialog(Context context) {
        super(context);
        activity = AppManager.getInstance().getMainActivity();
    }

    @Override
    protected int getContentView() {
        return R.layout.popup_record208;
    }

    @Override
    protected void afterContentView(Context context) {
        super.afterContentView(context);
        isLogin = PreferencesHepler.getInstance().isLogin();
        findViewById(R.id.record_video).setOnClickListener(this);
        findViewById(R.id.record_record).setOnClickListener(this);
        findViewById(R.id.record_local).setOnClickListener(this);
        findViewById(R.id.record_image).setOnClickListener(this);
        findViewById(R.id.record_close).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.record_video://录屏
                if (isLogin) {
                    if (activity != null)
                        activity.startScreenRecordActivity();
                } else {
                    startLoginActivity();
                }
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
                    if (SDKVesion >= 21) {
                        startCameraRecoed50Activity();
                    } else {
                        startCameraRecoedActivity();
                    }
                } else {
                    startLoginActivity();
                }
                break;

            case R.id.record_image://图文
                if (isLogin) {
                    if (activity != null)
                        startHomeImageShareActivity();
                } else {
                    startLoginActivity();
                }
                break;

            case R.id.record_local://视频
                if (activity != null)
                    startVideoMangerActivity();
                break;
        }
        cancel();
    }
}
