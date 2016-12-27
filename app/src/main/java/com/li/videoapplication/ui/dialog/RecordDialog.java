package com.li.videoapplication.ui.dialog;

import android.content.Context;
import android.view.View;

import com.fmsysj.screeclibinvoke.utils.RootUtil;
import com.ifeimo.screenrecordlib.RecordingManager;
import com.li.videoapplication.R;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.BaseTopDialog;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.activity.MainActivity;
import com.li.videoapplication.tools.ToastHelper;
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
    private void startLoginDialog() {
        DialogManager.showLogInDialog(activity);
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
     * 跳转：选择上传视频
     */
    private void startVideoChooseActivity() {
        ActivityManeger.startVideoChooseActivity(getContext(), null);
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
                    if (activity != null)
                        startHomeImageShareActivity();
                } else {
                    startLoginDialog();
                }
                break;

            case R.id.record_local://视频
                if (activity != null)
                    startVideoChooseActivity();
                break;
        }
        cancel();
    }
}
