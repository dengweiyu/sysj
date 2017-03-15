package com.li.videoapplication.ui.activity;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.fmsysj.screeclibinvoke.data.model.configuration.RecordingSetting;
import com.fmsysj.screeclibinvoke.data.observe.listener.FrontCameraObservable;
import com.fmsysj.screeclibinvoke.logic.floatview.FloatViewManager;
import com.fmsysj.screeclibinvoke.logic.frontcamera.FrontCameraManager;
import com.fmsysj.screeclibinvoke.logic.screenrecord.RecordingService;
import com.fmsysj.screeclibinvoke.ui.dialog.SettingQualityDialog;
import com.fmsysj.screeclibinvoke.utils.ViewUtil;
import com.ifeimo.screenrecordlib.RecordingManager;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Update;
import com.li.videoapplication.data.model.response.UpdateVersionEntity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.utils.LogHelper;

/**
 * 活动：设置
 */
public class SettingActivity extends TBaseActivity implements OnClickListener, OnCheckedChangeListener,
        FrontCameraObservable {

    /**
     * 跳转：关于
     */
    public void startAboutActivity() {
        ActivityManeger.startAboutActivity(this);
    }

    /**
     * 跳转：分享
     */
    public void startActivityShareActivity4SYSJ() {
        ActivityManeger.startActivityShareActivity4SYSJ(this);
    }

    private RelativeLayout help;
    private RelativeLayout soundRecording;
    private RelativeLayout shakeRecording;
    private RelativeLayout anchorModel;
    private RelativeLayout touchPosition;
//    private RelativeLayout gameScan;
    private RelativeLayout screenQuality;
    private RelativeLayout floatingWindiws;
    private RelativeLayout recordedJump;
    private RelativeLayout update;
    private ImageView point;
    private RelativeLayout inviteFriends;
    private RelativeLayout about;

    private ToggleButton soundRecordingToggle;
    private ToggleButton shakeRecordingToggle;
    private ToggleButton anchorModelToggle;
    private ToggleButton touchPositionToggle;
//    private ToggleButton gameScanToggle;
    private ToggleButton floatingWindiwsToggle;
    private ToggleButton recordedJumpToggle;

    private TextView screenQualityText;

    @Override
    public int getContentView() {
        return R.layout.activity_setting;
    }

    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundWhite();
        setAbTitle(R.string.setting_title);
    }

    @Override
    public void initView() {
        super.initView();

        initContentView();
    }

    @Override
    public void loadData() {
        super.loadData();

        initData();
    }

    private void initContentView() {

        help = (RelativeLayout) findViewById(R.id.setting_help);
        soundRecording = (RelativeLayout) findViewById(R.id.setting_soundRecording);
        shakeRecording = (RelativeLayout) findViewById(R.id.setting_shakeRecording);
        anchorModel = (RelativeLayout) findViewById(R.id.setting_anchorModel);
        touchPosition = (RelativeLayout) findViewById(R.id.setting_touchPosition);
//        gameScan = (RelativeLayout) findViewById(R.id.setting_gameScan);
        screenQuality = (RelativeLayout) findViewById(R.id.setting_screenQuality);
        floatingWindiws = (RelativeLayout) findViewById(R.id.setting_floatingWindiws);
        recordedJump = (RelativeLayout) findViewById(R.id.setting_recordedJump);
        update = (RelativeLayout) findViewById(R.id.setting_update);
        point = (ImageView) findViewById(R.id.setting_update_point);
        inviteFriends = (RelativeLayout) findViewById(R.id.setting_inviteFriends);
        about = (RelativeLayout) findViewById(R.id.setting_about);

        findViewById(R.id.setting_downloadmanager).setOnClickListener(this);
        help.setOnClickListener(this);
        soundRecording.setOnClickListener(this);
        shakeRecording.setOnClickListener(this);
        anchorModel.setOnClickListener(this);
        touchPosition.setOnClickListener(this);
//        gameScan.setOnClickListener(this);
        screenQuality.setOnClickListener(this);
        floatingWindiws.setOnClickListener(this);
        recordedJump.setOnClickListener(this);
        update.setOnClickListener(this);
        inviteFriends.setOnClickListener(this);
        about.setOnClickListener(this);

        soundRecordingToggle = (ToggleButton) findViewById(R.id.setting_soundRecording_toggle);
        shakeRecordingToggle = (ToggleButton) findViewById(R.id.setting_shakeRecording_toggle);
        anchorModelToggle = (ToggleButton) findViewById(R.id.setting_anchorModel_toggle);
        touchPositionToggle = (ToggleButton) findViewById(R.id.setting_touchPosition_toggle);
//        gameScanToggle = (ToggleButton) findViewById(R.id.setting_gameScan_toggle);
        recordedJumpToggle = (ToggleButton) findViewById(R.id.setting_recordedJump_toggle);
        floatingWindiwsToggle = (ToggleButton) findViewById(R.id.setting_floatingWindiws_toggle);

        screenQualityText = (TextView) findViewById(R.id.setting_screenQuality_text);

        point.setVisibility(View.GONE);
        refreshRedPoint();
    }


    private void initData() {
        RecordingSetting setting = PreferencesHepler.getInstance().getRecordingSetting();
        // 录制声音
        soundRecordingToggle.setChecked(setting.isSoundRecording());
        // 摇晃录屏
        shakeRecordingToggle.setChecked(setting.isShakeRecording());
        // 触摸位置
        touchPositionToggle.setChecked(setting.isTouchPosition());
        // 显示浮窗
        floatingWindiwsToggle.setChecked(!setting.isFloatingWindiws());

        screenQualityText.setText(setting.getQualityText());

        // 扫描游戏列表
//        gameScanToggle.setChecked(setting.isGameScan());
        // 跳转到视频管理
        recordedJumpToggle.setChecked(setting.isRecordedJump());

        soundRecordingToggle.setOnCheckedChangeListener(this);
        shakeRecordingToggle.setOnCheckedChangeListener(this);
        touchPositionToggle.setOnCheckedChangeListener(this);
        floatingWindiwsToggle.setOnCheckedChangeListener(this);
//        gameScanToggle.setOnCheckedChangeListener(this);
        recordedJumpToggle.setOnCheckedChangeListener(this);

        anchorModelToggle.setOnClickListener(this);
        refreshFrontCamera();
    }

    private void refreshFrontCamera() {
        Log.d(tag, "refreshFrontCamera: // ---------------------------------------------------");
        if (anchorModelToggle != null) {
            if (FrontCameraManager.getInstance().isOpen()) {
                anchorModelToggle.setChecked(true);
            } else {
                anchorModelToggle.setChecked(false);
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.setting_downloadmanager:
                ActivityManeger.startDownloadManagerActivity(this);
                break;
            case R.id.setting_anchorModel_toggle:// 主播
                ViewUtil.enabled(v, 1200);
                if (!FrontCameraManager.getInstance().isOpen()) {
                    // 打开前置摄像头
                    RecordingService.openFrontCamera();
                } else {
                    // 关闭前置摄像头
                    RecordingService.closeFrontCamera();
                }
                break;

            case R.id.setting_help:// 帮助与教程
                ActivityManeger.startHelpActivity(this);
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "帮助教程");
                break;

            case R.id.setting_screenQuality:// 清晰度
                dialogQualityOfVideo();
                break;

            case R.id.setting_floatingWindiws:
                break;

            case R.id.setting_inviteFriends:// 邀请好友
                inviteFriends();
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "邀请好友");
                break;

            case R.id.setting_update:// 版本更新
                // 版本更新
                DataManager.updateVersionSetting();
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "版本更新");
                break;

            case R.id.setting_about:// 关于我们
                this.startAboutActivity();
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "关于我们");
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == soundRecordingToggle) {// 声音选项，开启声音录制
            if (isChecked) {
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "开启声音录制-被开启");
            }else {
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "开启声音录制-被关闭");
            }
            PreferencesHepler.getInstance().saveRecordingSettingSoundRecording(isChecked);

        } else if (buttonView == shakeRecordingToggle) {// 开启摇晃录屏
            if (isChecked) {// 摇晃截屏
                RecordingService.openShake();
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "摇晃录制-被开启");
            } else {
                RecordingService.closeShake();
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "摇晃录制-被关闭");
            }
            PreferencesHepler.getInstance().saveRecordingSettingShakeRecording(isChecked);
        } else if (buttonView == floatingWindiwsToggle) { // 开启悬浮窗
            if (RecordingManager.getInstance().isRecording()) {// 录屏时不允许修改状态
                showToastShort(R.string.record_cannot_change_floatingwindiws);
                floatingWindiwsToggle.setChecked(!PreferencesHepler.getInstance().getRecordingSetting().isFloatingWindiws());
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "浮窗模式-被关闭");
            } else {// 显示悬浮框
                PreferencesHepler.getInstance().saveRecordingSettingFloatingWindiws(!isChecked);
                // 隐藏与显示悬浮窗
                RecordingService.toogleFloatView(!isChecked);
                FloatViewManager.getInstance().isFloatingWindiws = !isChecked;

                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "浮窗模式-被开启");
            }
        } else if (buttonView == recordedJumpToggle) { // 录屏后跳转
            if (isChecked) {
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "录屏后跳转-被开启");
            } else {
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "录屏后跳转-被关闭");
            }
            PreferencesHepler.getInstance().saveRecordingSettingRecordedJump(isChecked);
        } else if (buttonView == touchPositionToggle) {// 触摸选项， 显示触摸位置
            if (isChecked) {
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "显示触摸位置-被开启");
            } else {
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "显示触摸位置-被关闭");
            }
            PreferencesHepler.getInstance().saveRecordingSettingTouchPosition(isChecked);
        } /*else if (buttonView == anchorModelToggle) {// 前摄像头选项，开启主播模式
            if (isChecked) {
                sharedPreferences.edit().putBoolean("show_front_camera", true).apply();
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "主播模式-被开启");
            } else {
                sharedPreferences.edit().putBoolean("show_front_camera", false).apply();
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "主播模式-被关闭");
            }
        } else if (buttonView == gameScanToggle) {// 游戏列表框，启动游戏扫描
            if (isChecked) {
                sharedPreferences.edit().putBoolean("PackageInfoGridviewNotify", true).apply();
            } else {
                sharedPreferences.edit().putBoolean("PackageInfoGridviewNotify", false).apply();
            }
        }*/
    }

    private void refreshRedPoint() {

        Update update = PreferencesHepler.getInstance().getUpdate();
        if (update != null) {
            if ("U".equals(update.getUpdate_flag()) || "A".equals(update.getUpdate_flag())) {
                postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        point.setVisibility(View.VISIBLE);
                    }
                }, 400);
            }
        }
    }

    /**
     * 邀请好友
     */
    private void inviteFriends() {

        startActivityShareActivity4SYSJ();
    }

    /**
     * 清晰度选择对话框
     */
    public void dialogQualityOfVideo() {
        DialogManager.showSettingQualityDialog(this, new SettingQualityDialog.Qualityable() {

                    @Override
                    public void quality(String quality) {

                        PreferencesHepler.getInstance().saveRecordingSettingQuality(quality);
                        screenQualityText.setText(PreferencesHepler.getInstance().getRecordingSetting().getQualityText());
                    }
                });
    }

    /**
     * 回调：版本更新
     */
    public void onEventMainThread(UpdateVersionEntity event) {

        if (event != null && event.isResult()) {
            Update update = event.getData().get(0);
            if (update != null) {
                updateVersion(update);
            }
        }
    }

    private void updateVersion(final Update update) {
        LogHelper.i(tag, "updateVersion  ");
        if ("U".equals(update.getUpdate_flag()) || // 可用升级
                "A".equals(update.getUpdate_flag())){// 强制升级
            // 版本更新对话框
            DialogManager.showUpdateDialog(this, update);
        }else {// N:最新版本
            ToastHelper.s(R.string.update_new);
            Log.d(tag, "updateVersion: New");
        }
    }

    /**
     * 回调：发布前置摄像头事件
     */
    @Override
    public void onCamera() {
        post(new Runnable() {
            @Override
            public void run() {
                refreshFrontCamera();
            }
        });
    }
}
