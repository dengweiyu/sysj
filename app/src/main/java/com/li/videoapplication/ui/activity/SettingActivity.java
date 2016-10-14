package com.li.videoapplication.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.fmsysj.zbqmcs.record.RecordAction;
import com.fmsysj.zbqmcs.service.ScreenRECService;
import com.fmsysj.zbqmcs.utils.ExApplication;
import com.fmsysj.zbqmcs.utils.RecordVideo;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Update;
import com.li.videoapplication.data.model.response.UpdateVersionSettingEntity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.toast.ToastHelper;
import com.li.videoapplication.utils.LogHelper;
import com.li.videoapplication.utils.ScreenUtil;

/**
 * 活动：设置
 */
public class SettingActivity extends TBaseActivity implements OnClickListener, OnCheckedChangeListener {

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
//    private ToggleButton recordedJumpToggle;

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
        mContext = this;
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
//        recordedJump = (RelativeLayout) findViewById(R.id.setting_recordedJump);
        update = (RelativeLayout) findViewById(R.id.setting_update);
        point = (ImageView) findViewById(R.id.setting_update_point);
        inviteFriends = (RelativeLayout) findViewById(R.id.setting_inviteFriends);
        about = (RelativeLayout) findViewById(R.id.setting_about);

        help.setOnClickListener(this);
        soundRecording.setOnClickListener(this);
        shakeRecording.setOnClickListener(this);
        anchorModel.setOnClickListener(this);
        touchPosition.setOnClickListener(this);
//        gameScan.setOnClickListener(this);
        screenQuality.setOnClickListener(this);
        floatingWindiws.setOnClickListener(this);
//        recordedJump.setOnClickListener(this);
        update.setOnClickListener(this);
        inviteFriends.setOnClickListener(this);
        about.setOnClickListener(this);

        soundRecordingToggle = (ToggleButton) findViewById(R.id.setting_soundRecording_toggle);
        shakeRecordingToggle = (ToggleButton) findViewById(R.id.setting_shakeRecording_toggle);
        anchorModelToggle = (ToggleButton) findViewById(R.id.setting_anchorModel_toggle);
        touchPositionToggle = (ToggleButton) findViewById(R.id.setting_touchPosition_toggle);
//        gameScanToggle = (ToggleButton) findViewById(R.id.setting_gameScan_toggle);
//        recordedJumpToggle = (ToggleButton) findViewById(R.id.setting_recordedJump_toggle);
        floatingWindiwsToggle = (ToggleButton) findViewById(R.id.setting_floatingWindiws_toggle);

        soundRecordingToggle.setOnCheckedChangeListener(this);
        shakeRecordingToggle.setOnCheckedChangeListener(this);
        anchorModelToggle.setOnCheckedChangeListener(this);
        touchPositionToggle.setOnCheckedChangeListener(this);
//        gameScanToggle.setOnCheckedChangeListener(this);
//        recordedJumpToggle.setOnCheckedChangeListener(this);
        floatingWindiwsToggle.setOnCheckedChangeListener(this);

        screenQualityText = (TextView) findViewById(R.id.setting_screenQuality_text);

        point.setVisibility(View.GONE);
        refreshRedPoint();
    }

    private SharedPreferences sharedPreferences;

    private void initData() {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        // 录制声音
        boolean recoudsound = sharedPreferences.getBoolean("record_sound", true);
        soundRecordingToggle.setChecked(recoudsound);
        // 摇晃录屏
        boolean yahuan = sharedPreferences.getBoolean("yahangjieping", false);
        shakeRecordingToggle.setChecked(yahuan);
        // 显示浮窗
        boolean showfloat = sharedPreferences.getBoolean("no_float_view_record", false);
        floatingWindiwsToggle.setChecked(!showfloat);
        // 触摸位置
        boolean showtouch = sharedPreferences.getBoolean("show_touch_view", false);
        touchPositionToggle.setChecked(showtouch);
        // 前摄像头
        boolean showcamera = sharedPreferences.getBoolean("show_front_camera", false);
        anchorModelToggle.setChecked(showcamera);
        // 跳转到视频管理
//        boolean showvideomanage = sharedPreferences.getBoolean("isGotoVideoManage", true);
//        recordedJumpToggle.setChecked(showvideomanage);
        // 扫描游戏列表
//        boolean showgamelist = sharedPreferences.getBoolean("PackageInfoGridviewNotify", false);
//        gameScanToggle.setChecked(showgamelist);

        // 清晰度
        String quality = "";
        if (ExApplication.getAndroidSDKVersion() >= 19) {
            quality = sharedPreferences.getString("quality_of_video", "0");
        } else {

            quality = sharedPreferences.getString("quality_of_video", "1");
        }
        // 将2.0.1.1及之前版本的清晰度修改成最新版本
        if (quality.equals("高清")) {
            quality = "0";
            sharedPreferences.edit().putString("quality_of_video", ExApplication.HQuality).apply();
        } else if (quality.equals("标准")) {
            quality = "1";
            sharedPreferences.edit().putString("quality_of_video", ExApplication.HQuality).apply();
        } else if (quality.equals("流畅")) {
            sharedPreferences.edit().putString("quality_of_video", ExApplication.HQuality).apply();
            quality = "0";
        }

        if (quality.equals("0")) {
            screenQualityText.setText(ExApplication.HQualityVaule);
        } else {
            screenQualityText.setText(ExApplication.SQualityVaule);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

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
                sharedPreferences.edit().putBoolean("record_sound", true).apply();
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "开启声音录制-被开启");
            } else {
                sharedPreferences.edit().putBoolean("record_sound", false).apply();
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "开启声音录制-被关闭");
            }
        } else if (buttonView == shakeRecordingToggle) {// 开启摇晃录屏
            if (isChecked) {// 摇晃截屏
                startYaohuangjiepingService();
                sharedPreferences.edit().putBoolean("yahangjieping", true).apply();
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "摇晃录制-被开启");
            } else {
                endYaohuangjiepingService();
                sharedPreferences.edit().putBoolean("yahangjieping", false).apply();
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "摇晃录制-被关闭");
            }
        } else if (buttonView == floatingWindiwsToggle) { // 开启悬浮窗
            if (RecordVideo.isRecordering) {// 录屏时不允许修改状态
                ToastHelper.s("正在录屏中，无法修改");
                floatingWindiwsToggle.setChecked(!isChecked);
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "浮窗模式-被关闭");
            } else {// 显示悬浮框
                sharedPreferences.edit().putBoolean("no_float_view_record", !isChecked).apply();
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "浮窗模式-被开启");
            }
        } /*else if (buttonView == recordedJumpToggle) { // 录屏后跳转
            if (isChecked) {
                sharedPreferences.edit().putBoolean("isGotoVideoManage", true).apply();
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "录屏后跳转-被开启");
            } else {
                sharedPreferences.edit().putBoolean("isGotoVideoManage", false).apply();
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "录屏后跳转-被关闭");
            }

        }*/ else if (buttonView == touchPositionToggle) {// 触摸选项， 显示触摸位置
            if (isChecked) {
                sharedPreferences.edit().putBoolean("show_touch_view", true).apply();
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "显示触摸位置-被开启");
            } else {
                sharedPreferences.edit().putBoolean("show_touch_view", false).apply();
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "显示触摸位置-被关闭");
            }
        } else if (buttonView == anchorModelToggle) {// 前摄像头选项，开启主播模式
            if (isChecked) {
                sharedPreferences.edit().putBoolean("show_front_camera", true).apply();
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "主播模式-被开启");
            } else {
                sharedPreferences.edit().putBoolean("show_front_camera", false).apply();
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "主播模式-被关闭");
            }
        } /*else if (buttonView == gameScanToggle) {// 游戏列表框，启动游戏扫描
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

    private void updateVersion(final Update update) {
        if ("U".equals(update.getUpdate_flag())) {// 可用升级
            updateNow(update);
        } else if ("A".equals(update.getUpdate_flag())) {// 强制升级
            updateNow(update);
            /*String changelog = update.getChange_log();
            String[] changeArray = changelog.split(";");
            changelog = "";
            for (int i = 0; i < changeArray.length; i++) {
                if (i != changeArray.length) {
                    changelog += changeArray[i] + "\n";
                } else {
                    changelog += changeArray[i];
                }
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
            builder.setTitle("更新提示");
            builder.setMessage("手游视界" + update.getVersion_str() + "\n\n\t\t更新日志：\n" + changelog);
            builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            	
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Uri uri = Uri.parse(update.getUpdate_url());
                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                    SettingActivity.this.startActivity(it);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            	
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create().show();*/
        } else if ("N".equals(update.getUpdate_flag())) {// 最新版本
            showToastLong("当前已经是最新版本");
        }

    }

    private void updateNow(final Update update) {
        LogHelper.i(tag, "updateNow  ");
        String changelog = update.getChange_log();
        String[] changeArray = changelog.split(";");
        changelog = "";
        for (String aChangeArray : changeArray) {
            changelog += aChangeArray + "\n\t\n";
        }

        final AlertDialog dialog = new AlertDialog.Builder(SettingActivity.this).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.dialog_update);

        TextView changeText = (TextView) dialog.getWindow().findViewById(R.id.dialog_update_text);
        changeText.setText(changelog);

        TextView version = (TextView) dialog.getWindow().findViewById(R.id.buildcode);
        version.setText(update.getVersion_str());

        ImageView logo = (ImageView) dialog.getWindow().findViewById(R.id.logo);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) logo.getLayoutParams();

        int screenWidth = ScreenUtil.getScreenWidth();
        int margin = dp2px(40);
        int width = screenWidth - margin;
        params.width = width;
        params.height = width / 3;
        logo.setLayoutParams(params);

        dialog.getWindow().findViewById(R.id.dialog_update_ok).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(update.getUpdate_url());
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                SettingActivity.this.startActivity(it);
                UmengAnalyticsHelper.onEvent(SettingActivity.this, UmengAnalyticsHelper.SLIDER, "版本更新-有效");
            }
        });
        dialog.getWindow().findViewById(R.id.dialog_update_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


		/*String changelog = update.getChange_log();
        String[] changeArray = changelog.split(";");
		changelog = "";
		for (int i = 0; i < changeArray.length; i++) {
            if (i != changeArray.length) {
                changelog += changeArray[i] + "\n";
            } else {
                changelog += changeArray[i];
            }
        }
		AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
		builder.setTitle("发现新版本");
		String versionName = VersionUtils.getCurrentVersionName(AppManager.getInstance().getContext());
		builder.setMessage("你当前安装的版本是" + versionName + "手游视界已经发布最新" + update.getVersion_str() + "版本，是否现在升级？" + "\n\n\t\t更新日志：\n" + changelog);
		builder.setPositiveButton("立即升级", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri uri = Uri.parse(update.getUpdate_url());
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                SettingActivity.this.startActivity(it);
            }
        });
		builder.setNegativeButton("暂不升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
		builder.create().show();*/
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
        final String[] array = new String[]{ExApplication.HQualityVaule, ExApplication.SQualityVaule};
        Dialog alertDialog = new AlertDialog.Builder(this).setTitle("画质选择").setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        sharedPreferences.edit().putString("quality_of_video", ExApplication.HQuality).apply();
                        screenQualityText.setText(array[which]);
                        UmengAnalyticsHelper.onEvent(SettingActivity.this, UmengAnalyticsHelper.SLIDER, "画质切换-超清");
                        // 写入XML配置文件
                        // xmlFileWriter.creat(context);
                        break;
                    case 1:
                        sharedPreferences.edit().putString("quality_of_video", ExApplication.SQuality).apply();
                        screenQualityText.setText(array[which]);
                        UmengAnalyticsHelper.onEvent(SettingActivity.this, UmengAnalyticsHelper.SLIDER, "画质切换-标清");
                        // 写入XML配置文件
                        // xmlFileWriter.creat(context);
                        break;
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create();
        alertDialog.show();
    }

    private Context mContext;

    private void startService() {
        // UtilService.startService(SettingActivity.this);
        mContext.startService(new Intent(mContext, ScreenRECService.class));
    }

    private void endYaohuangjiepingService() {
        startService();
        Intent intent = new Intent(RecordAction.ACTION);
        intent.putExtra("action", "end_yaohuangjieping_service");
        sendBroadcast(intent);
    }

    private void startYaohuangjiepingService() {
        startService();
        Intent intent = new Intent(RecordAction.ACTION);
        intent.putExtra("action", "start_yaohuangjieping_service");
        sendBroadcast(intent);
    }

    /**
     * 回调：版本更新
     */
    public void onEventMainThread(UpdateVersionSettingEntity event) {

        if (event != null) {
            if (event.isResult()) {
                Update update = event.getData().get(0);
                if (update != null) {
                    updateVersion(update);
                }
            }
        }
    }
}
