package com.li.videoapplication.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.fmsysj.screeclibinvoke.ui.activity.ScreenRecordActivity;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.BaseTopDialog;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.activity.VideoShareActivity;
import com.li.videoapplication.utils.AppUtil;

/**
 * 玩家广场录屏对话框
 */

public class SquareRecordDialog extends BaseTopDialog implements View.OnClickListener {
    private Activity mActivity;
    public SquareRecordDialog(Activity activity) {
        super(activity);
        mActivity = activity;
    }

    /**
     * 跳转：选择上传视频
     */
    private void startVideoChooseActivity() {
        ActivityManager.startVideoChooseActivity(getContext(), null, VideoShareActivity.TO_VIDEOMANAGER);
        UmengAnalyticsHelper.onEvent(mActivity, UmengAnalyticsHelper.MAIN, "玩家视频-弹窗-上传视频");
    }


    /**
     * 跳转：录屏
     */
    public void startScreenRecordActivity() {
        int SDKVesion = AppUtil.getAndroidSDKVersion();
        if (SDKVesion >= 21) {
            ScreenRecordActivity.startScreenRecordActivity(mActivity);
            mActivity.overridePendingTransition(R.anim.activity_slide_in_top, R.anim.activity_hold);
        } else {
            if (AppUtil.appRoot()) {
                ScreenRecordActivity.startScreenRecordActivity(mActivity);
                mActivity.overridePendingTransition(R.anim.activity_slide_in_top, R.anim.activity_hold);
            } else {// 提示用户获取root
                ToastHelper.s(R.string.main_rootnotify);
            }
        }
        UmengAnalyticsHelper.onEvent(mActivity, UmengAnalyticsHelper.MAIN, "玩家视频-弹窗-录制视频");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.ll_square_record_upload:
                startVideoChooseActivity();
                break;
            case R.id.ll_square_record_video:
                startScreenRecordActivity();
                break;
        }
        dismiss();
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_square_record;
    }

    @Override
    protected void afterContentView(Context context) {
        super.afterContentView(context);

        findViewById(R.id.iv_square_record_close).setOnClickListener(this);
        findViewById(R.id.ll_square_record_upload).setOnClickListener(this);
        findViewById(R.id.ll_square_record_video).setOnClickListener(this);
    }
}
