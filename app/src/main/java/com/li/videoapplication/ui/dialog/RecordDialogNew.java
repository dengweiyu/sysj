package com.li.videoapplication.ui.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.fmsysj.screeclibinvoke.ui.activity.ScreenRecordActivity;
import com.li.videoapplication.R;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.activity.PrivacyActivity;
import com.li.videoapplication.utils.AppUtil;
import com.li.videoapplication.utils.ScreenUtil;
import com.mob.MobSDK;

import java.lang.reflect.Field;


/**
 *首页底部录屏
 */

public class RecordDialogNew extends AlphaShadeDialog implements View.OnClickListener {

    private Activity mActivity;

    private int mShadow = 0x90000000;
    private ColorDrawable mColorDrawable;
    public RecordDialogNew(@NonNull Activity activity) {
       // super(activity,R.style.homeTranslucentDialog);
        super(activity);
        mActivity = activity;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup_record221);

        resetCallback();

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        params.width = ScreenUtil.getScreenWidth();
        params.alpha = 0.9f;

    //    mColorDrawable = new ColorDrawable(mShadow);
   //     window.setBackgroundDrawable(mColorDrawable);

        findViewById(R.id.record_close).setOnClickListener(this);
        findViewById(R.id.ll_popup_square_layout).setOnClickListener(this);
        findViewById(R.id.ll_popup_record_layout).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.ll_popup_record_layout:
                startScreenRecordActivity();
                break;
            case R.id.ll_popup_square_layout:
                startSquareActivity();
                break;
        }
        dismiss();
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
        UmengAnalyticsHelper.onEvent(mActivity, UmengAnalyticsHelper.MAIN, "首页-弹窗-录制视频");
    }

    public void startSquareActivity(){
        ActivityManager.startSquareActivity(mActivity,null);
        UmengAnalyticsHelper.onEvent(mActivity, UmengAnalyticsHelper.MAIN, "首页-弹窗-玩家广场");
    }
}
