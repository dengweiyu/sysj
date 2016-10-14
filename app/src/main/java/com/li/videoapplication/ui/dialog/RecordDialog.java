package com.li.videoapplication.ui.dialog;

import android.content.Context;
import android.view.View;

import com.fmsysj.zbqmcs.utils.RecordVideo;
import com.li.videoapplication.R;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.BaseTopDialog;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.MainActivity;
import com.li.videoapplication.ui.toast.ToastHelper;

/**
 * 弹框：首页录制
 */
public class RecordDialog extends BaseTopDialog implements View.OnClickListener {

    private MainActivity activity;
    private boolean isLogin;

    /**
     * 跳转：登录
     */
    public void startLoginActivity() {
        ActivityManeger.startLoginActivity(getContext());
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
                if (RecordVideo.isRecordering) {
                    ToastHelper.s("正在录屏中");
                    return;
                }
                if (isLogin) {
                    ActivityManeger.startCameraRecoedActivity(activity);
                } else {
                    startLoginActivity();
                }
                break;

            case R.id.record_image://图文
                if (isLogin) {
                    if (activity != null)
                        ActivityManeger.startHomeImageShareActivity(activity, null, false);
//                        ActivityManeger.startImageViewActivity(getContext());
                } else {
                    startLoginActivity();
                }
                break;

            case R.id.record_local://视频
                if (activity != null)
                    ActivityManeger.startVideoMangerActivity(activity);
                break;
        }
        cancel();
    }
}
