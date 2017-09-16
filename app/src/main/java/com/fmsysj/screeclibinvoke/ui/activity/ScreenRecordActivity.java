package com.fmsysj.screeclibinvoke.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fmsysj.screeclibinvoke.data.ConstantsPreferences;
import com.fmsysj.screeclibinvoke.data.model.event.CountDownFinishEvent;
import com.fmsysj.screeclibinvoke.data.model.event.SetFloatWindowEvent;
import com.fmsysj.screeclibinvoke.data.observe.ObserveManager;
import com.fmsysj.screeclibinvoke.data.observe.listener.Recording2Observable;
import com.fmsysj.screeclibinvoke.data.observe.listener.RecordingObservable;
import com.fmsysj.screeclibinvoke.logic.floatview.FloatViewManager;
import com.fmsysj.screeclibinvoke.logic.screenrecord.RecordingService;
import com.fmsysj.screeclibinvoke.ui.dialog.ManufacturerDialog;
import com.fmsysj.screeclibinvoke.ui.view.FlickerView2;
import com.fmsysj.screeclibinvoke.utils.PermissionUtil;
import com.fmsysj.screeclibinvoke.utils.RootUtil;
import com.ifeimo.screenrecordlib.RecordingManager;

import com.ifeimo.screenrecordlib.constant.Constant;
import com.ifeimo.screenrecordlib.listener.ScreenRecordPermissionListener;
import com.ifeimo.screenrecordlib.util.Utils;
import com.li.videoapplication.R;
import com.li.videoapplication.data.local.LPDSStorageUtil;
import com.li.videoapplication.data.model.event.ScreenRecordPermission2MainEvent;
import com.li.videoapplication.data.network.RequestExecutor;
import com.li.videoapplication.data.preferences.NormalPreferences;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.PermissionManager;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.utils.AppUtil;
import com.li.videoapplication.utils.ScreenUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 活动：录屏
 */
@SuppressLint("SdCardPath")
public class ScreenRecordActivity extends TBaseActivity implements
        View.OnTouchListener,
        View.OnClickListener,
        RecordingObservable,
        Recording2Observable {

    @BindView(R.id.screenrecord_landscape)
    FrameLayout landscape;
    @BindView(R.id.screenrecord_landscape_left)
    ImageView landscapeLeft;
    @BindView(R.id.screenrecord_landscape_right)
    ImageView landscapeRight;
    @BindView(R.id.screenrecord_portrait)
    FrameLayout portrait;
    @BindView(R.id.screenrecord_portrait_left)
    ImageView portraitLeft;
    @BindView(R.id.screenrecord_portrait_right)
    ImageView portraitRight;
    @BindView(R.id.screenrecord_landscape_text)
    TextView landscapeText;
    @BindView(R.id.screenrecord_portrait_text)
    TextView portraitText;
    @BindView(R.id.screenrecord_landscape_start)
    ImageView landscapeStart;
    @BindView(R.id.screenrecord_landscape_cover)
    View landscapeCover;
    @BindView(R.id.screenrecord_portrait_start)
    ImageView portraitStart;
    @BindView(R.id.screenrecord_portrait_cover)
    View portraitCover;
    @BindView(R.id.screenrecord_landscape_arrows_container)
    FrameLayout landscapeArrowsContainer;
    @BindView(R.id.screenrecord_portrait_arrows_container)
    FrameLayout portraitArrowsContainer;
    @BindView(R.id.screenrecord_landscape_arrows)
    ImageView landscapeArrows;
    @BindView(R.id.screenrecord_portrait_arrows)
    ImageView portraitArrows;
    @BindView(R.id.screenrecord_landscape_point)
    FlickerView2 landscapePoint;
    @BindView(R.id.screenrecord_portrait_point)
    FlickerView2 portraitPoint;
    @BindView(R.id.rootlayout)
    LinearLayout rootlayout;
    @BindView(R.id.screenrecord_video)
    LinearLayout bottom_video;
    @BindView(R.id.screenrecord_setting)
    LinearLayout bottom_setting;
    @BindView(R.id.screenrecord_close)
    ImageView close;
    @BindView(R.id.screenrecord_float_window_layout)
    LinearLayout float_window_layout;
    @BindView(R.id.screenrecord_float_window_text)
    TextView float_window_text;
    @BindView(R.id.screenrecord_float_window_toggle)
    ImageView float_window_toggle;

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 1:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        ScreenRecordActivity.this.checkPermission(new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.CAMERA});

                    }
                    PermissionManager manager = new PermissionManager(ScreenRecordActivity.this, new PermissionManager.Finishable() {
                        @Override
                        public void onFinish() {
                            Log.d(tag, "onFinish: ");
                        }
                    });
                    manager.checkPermission();
                    break;
            }
        }
    };
    /**
     * 录屏
     */
    public synchronized final static void startScreenRecordActivity(Context context) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, ScreenRecordActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_hold, R.anim.activity_slide_out_top);
    }

    @Override
    public void beforeOnCreate() {
        super.beforeOnCreate();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_screenrecord;
    }

    @Override
    public void initView() {
        super.initView();

        ObserveManager.getInstance().addRecordingObservable(this);
        ObserveManager.getInstance().addRecording2Observable(this);

        landscape.setOnTouchListener(this);
        portrait.setOnTouchListener(this);

        refreshArrows();

        try {
            refreshContentView();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        checkPermission();

        RecordingManager.getInstance().setPermissionListener(new ScreenRecordPermissionListener() {
            @Override
            public void getPermissionSuccess() {
                getAudioAndCameraPermission();
            }

            @Override
            public void getPermissionFail() {

            }
        });
        // 录屏权限开启
        showRecordPermission(800);
    }

    private void checkPermission(){
        if (Build.VERSION.SDK_INT >= 21) {
            ScreenRecordPermissionActivity.startPermission(ScreenRecordActivity.this);
        } else {/*
                    showMainTipDialog();
                    showOppoToast();*/
        }

    }

    private void getAudioAndCameraPermission(){
        PermissionManager manager = new PermissionManager(this, new PermissionManager.Finishable() {
            @Override
            public void onFinish() {

            }
        });
        manager.checkPermission();
    }

    private void showRecordPermission(int delay){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 开启权限，小米用户会重复弹出权限弹框
                if (Build.VERSION.SDK_INT >= 21) {
                    com.ifeimo.screenrecordlib.record.record50.ScreenRecordActivity.openPermission(ScreenRecordActivity.this, LPDSStorageUtil.createTmpRecPath().getPath(),
                            com.ifeimo.screenrecordlib.constant.Configuration.DEFAULT,
                            UmengAnalyticsHelper.getChannel(ScreenRecordActivity.this));
                } else { // 直接申请录音和照相权限
                    getAudioAndCameraPermission();
                }
            }
        }, delay);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        refreshLayoutParams();
    }

    /**
     * 布局参数是否初始化
     */
    private boolean firstLayoutParams = false;

    public void refreshLayoutParams() {
        if (rootlayout != null) {
            height = rootlayout.getHeight();
            Log.d(tag, "refreshLayoutParams: height=" + height);
            if (!firstLayoutParams) {

                height = rootlayout.getHeight();
                Log.d(tag, "refreshLayoutParams: layout/height=" + rootlayout.getHeight());

                landscapeParams = (LinearLayout.LayoutParams) landscape.getLayoutParams();
                portraitParams = (LinearLayout.LayoutParams) portrait.getLayoutParams();

                landscapeParams.height = landscape.getHeight();
                portraitParams.height = portrait.getHeight();
                Log.d(tag, "refreshLayoutParams: landscapeParams.height=" + landscapeParams.height);
                Log.d(tag, "refreshLayoutParams: portraitParams.height=" + portraitParams.height);

                landscapeParams.height = height / 2;
                portraitParams.height = height / 2;

                if (PreferencesHepler.getInstance().getRecordingSetting().getLandscape() == 1) {
                    landscapeParams.height = height;
                    portraitParams.height = 0;
                } else if (PreferencesHepler.getInstance().getRecordingSetting().getLandscape() == 2) {
                    height = height + +ScreenUtil.dp2px(38);
                    landscapeParams.height = 0;
                    portraitParams.height = height;
                }

                landscape.setLayoutParams(landscapeParams);
                portrait.setLayoutParams(portraitParams);
                refreshFloatWindow();
            }
            firstLayoutParams = true;
        }
    }

    /**
     * 更新箭头
     */
    private void refreshArrows() {
        if (landscapeArrows == null)
            return;
        if (portraitArrows == null)
            return;
        // 上半部分
        if (RecordingManager.getInstance().isRecording()) {// 录屏中
            landscapeArrows.setImageResource(R.drawable.screenrecord_up_white);
        } else {
            landscapeArrows.setImageResource(R.drawable.screenrecord_down_white);
        }
        // 下半部分
        if (RecordingManager.getInstance().isRecording()) {// 录屏中
            portraitArrows.setImageResource(R.drawable.screenrecord_down_blue);
        } else {
            portraitArrows.setImageResource(R.drawable.screenrecord_up_blue);
        }
    }

    public void refreshContentView() throws Exception {
        Log.d(tag, "refreshContentView: ");
        autoMoveCenter(4, null);
        refreshFloatWindow();
        if (RecordingManager.getInstance().isRecording()) {// 录屏中
            if (RecordingManager.getInstance().configuration() != null &&
                    RecordingManager.getInstance().configuration().isLandscape()) {// 横屏

                portraitCover.setVisibility(View.VISIBLE);
                landscapeCover.setVisibility(View.GONE);

                landscapeArrows.setVisibility(View.GONE);
                portraitArrows.setVisibility(View.GONE);

                if (!PreferencesHepler.getInstance().getRecordingSetting().isFloatingWindiws()) {// 无浮窗模式
                    landscapeLeft.setVisibility(View.VISIBLE);
                    landscapeRight.setVisibility(View.VISIBLE);
                } else {
                    landscapeLeft.setVisibility(View.GONE);
                    landscapeRight.setVisibility(View.GONE);
                }

                portraitPoint.hideView();
                landscapeStart.setImageResource(R.drawable.screenrecord_center_btn);
                if (RecordingManager.getInstance().isPausing()) {// 暂停中
                    landscapeLeft.setImageResource(R.drawable.screenrecord_start_btn);
                    landscapePoint.showView();
                } else {
                    landscapeLeft.setImageResource(R.drawable.screenrecord_pause_btn);
                    landscapePoint.flickView();
                }

                landscapeText.setText(RecordingManager.getInstance().time());
                portraitText.setText(R.string.screenrecord_portrait);
            } else {// 竖屏

                landscapeCover.setVisibility(View.VISIBLE);
                portraitCover.setVisibility(View.GONE);
                portraitLeft.setVisibility(View.VISIBLE);
                portraitRight.setVisibility(View.VISIBLE);

                if (!PreferencesHepler.getInstance().getRecordingSetting().isFloatingWindiws()) {// 无浮窗模式
                    portraitLeft.setVisibility(View.VISIBLE);
                    portraitRight.setVisibility(View.VISIBLE);
                } else {
                    portraitLeft.setVisibility(View.GONE);
                    portraitRight.setVisibility(View.GONE);
                }

                landscapePoint.hideView();
                portraitStart.setImageResource(R.drawable.screenrecord_center_btn);
                if (RecordingManager.getInstance().isPausing()) {// 暂停中
                    portraitLeft.setImageResource(R.drawable.screenrecord_start_btn);
                    portraitPoint.showView();
                } else {
                    portraitLeft.setImageResource(R.drawable.screenrecord_pause_btn);
                    portraitPoint.flickView();
                }

                landscapeText.setText(R.string.screenrecord_landscape);
                portraitText.setText(RecordingManager.getInstance().time());
            }
        } else {// 不在录屏
            portraitCover.setVisibility(View.GONE);
            landscapeCover.setVisibility(View.GONE);

            landscapeLeft.setVisibility(View.GONE);
            landscapeRight.setVisibility(View.GONE);

            portraitLeft.setVisibility(View.GONE);
            portraitRight.setVisibility(View.GONE);

            landscapeArrows.setVisibility(View.VISIBLE);
            portraitArrows.setVisibility(View.VISIBLE);

            landscapeStart.setImageResource(R.drawable.screenrecord_landscape_btn);
            portraitStart.setImageResource(R.drawable.screenrecord_portrait_btn);

            landscapeText.setText(R.string.screenrecord_landscape);
            portraitText.setText(R.string.screenrecord_portrait);

            landscapePoint.hideView();
            portraitPoint.hideView();
        }
    }


    @OnClick({R.id.screenrecord_landscape_start,
            R.id.screenrecord_landscape_left,
            R.id.screenrecord_landscape_right,

            R.id.screenrecord_portrait_start,
            R.id.screenrecord_portrait_left,
            R.id.screenrecord_portrait_right,

            R.id.screenrecord_landscape,
            R.id.screenrecord_portrait,

            R.id.screenrecord_landscape_arrows_container,
            R.id.screenrecord_portrait_arrows_container,

            R.id.screenrecord_setting,
            R.id.screenrecord_video,
            R.id.screenrecord_close,
            R.id.screenrecord_float_window_toggle})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.screenrecord_float_window_toggle:
                refreshFloatWindowToggleState();
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MAIN, "发布-录制视频-开启/关闭悬浮窗");

                break;

            case R.id.screenrecord_close:
                finish();
                break;

            case R.id.screenrecord_setting:
                ActivityManager.startSettingActivity(this);
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MAIN, "发布-录制视频-点击设置跳转设置页面");
                break;

            case R.id.screenrecord_video:
                ActivityManager.startVideoMangerActivity(this);
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MAIN, "发布-录制视频-点击视频跳转视频页面");
                break;

            case R.id.screenrecord_landscape_start:
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MAIN, "发布-录制视频-点击横屏游戏录制");
                if (RecordingManager.getInstance().isRecording()) { // 录屏中
                    stopRecording();
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                refreshContentView();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, 300);
                } else {
                    showManufacturerDialog(true,new Runnable() {
                        @Override
                        public void run() {
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && !Utils.root()) {
                                ToastHelper.s(R.string.root_content);
                                return;
                            }
                            if (PreferencesHepler.getInstance().getRecordingSetting().isFloatingWindiws()) {// 悬浮窗
                                showFloatView();

                                if (NormalPreferences.getInstance().
                                        getBoolean(ConstantsPreferences.FLOAT_WINDOW_TIPS_FIRST_TIME, true)) {

                                    Toast.makeText(ScreenRecordActivity.this, "如果没有浮窗出现，请返回主页设置无浮窗模式",
                                            Toast.LENGTH_LONG).show();
                                    NormalPreferences.getInstance().
                                            putBoolean(ConstantsPreferences.FLOAT_WINDOW_TIPS_FIRST_TIME, false);
                                } else {
                                    Toast.makeText(ScreenRecordActivity.this, ScreenRecordActivity.this.getString(R.string.screenrecord_portrait_tip),
                                            Toast.LENGTH_SHORT).show();
                                }
                                AppUtil.simulateHomeKey(ScreenRecordActivity.this);
                            } else {// 无悬浮窗
                                DialogManager.showCountDownDialog(ScreenRecordActivity.this);
                            }
                        }
                    });
                }
                break;

            case R.id.screenrecord_landscape_left:
                toogleRecording();
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            refreshContentView();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 300);
                break;

            case R.id.screenrecord_landscape_right:
                stopRecording();
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            refreshContentView();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 300);
                break;

            case R.id.screenrecord_portrait_start:
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MAIN, "发布-录制视频-点击竖屏游戏录制");
                if (RecordingManager.getInstance().isRecording()) { // 录屏中
                    stopRecording();
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                refreshContentView();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, 300);
                } else {
                    showManufacturerDialog(false,new Runnable() {
                        @Override
                        public void run() {
                            PreferencesHepler.getInstance().saveRecordingSettingLandscape(false);
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && !Utils.root()) {
                                ToastHelper.s(R.string.root_content);
                                return;
                            }
                            if (PreferencesHepler.getInstance().getRecordingSetting().isFloatingWindiws()) {// 悬浮窗
                                showFloatView();
                                showToastShort(R.string.screenrecord_portrait_tip);
                            } else {// 无悬浮窗
                                startRecording();
                            }
                            finishBottomOut();
                            //go to launcher
                            AppUtil.simulateHomeKey(ScreenRecordActivity.this);

                        }
                    });
                }
                break;

            case R.id.screenrecord_portrait_left:
                toogleRecording();
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            refreshContentView();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 300);
                break;

            case R.id.screenrecord_portrait_right:
                stopRecording();
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            refreshContentView();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 300);
                break;
        }

        if (view == landscapeArrowsContainer) {// 横屏箭头
            Log.d(tag, "onClick: landscapeArrows");
            if (!isTouchMoving && !isAutoMoving &&
                    !RecordingManager.getInstance().isRecording() && firstLayoutParams) {
                if (landscape.getHeight() > (int) (0.7f * (float) height)) {
                    autoMoveCenter(4, null);
                } else {
                    autoMoveDown();
                }
            }
        } else if (view == portraitArrowsContainer) {// 竖屏箭头
            Log.d(tag, "onClick: portraitArrows");
            if (!isTouchMoving &&
                    !isAutoMoving &&
                    !RecordingManager.getInstance().isRecording() && firstLayoutParams) {
                if (portrait.getHeight() > (int) (0.7f * (float) height)) {
                    autoMoveCenter(4, null);
                } else {
                    autoMoveUp();
                }
            }
        }
    }

    /**
     * 滑动动画
     */
    public void slideDownToCenter() throws Exception {
        Log.d(tag, "slideDownToCenter: ");
        postDelayed(new Runnable() {
            @Override
            public void run() {
                autoMoveDown(ScreenUtil.px2dp(srceenHeight / 15), new Runnable() {
                    @Override
                    public void run() {
                        autoMoveCenter(1, null);
                    }
                });
            }
        }, 300);
    }

    /**
     * 悬浮窗设置
     */
    public void showManufacturerDialog(boolean isLandscape,Runnable r) {

        PreferencesHepler.getInstance().saveRecordingSettingLandscape(isLandscape);
        String manufacturer = RootUtil.getManufacturer();
        if (Build.VERSION.SDK_INT >= 21 && PreferencesHepler.getInstance().getRecordingSetting().isFloatingWindiws()) {
            if (manufacturer.equals("MIUI") || manufacturer.equals("OPPO")){
                if (!PermissionUtil.isOpAllowed(this, PermissionUtil.OP_SYSTEM_ALERT_WINDOW)) {
                    // 其他手机如果获取不到SYSTEM_ALERT_WINDOW权限就一直弹窗
                    DialogManager.showManufacturerDialog(this, ManufacturerDialog.TYPE_RECORD_BUTTON);
                    return;
                }
            }
        }

        r.run();
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MAIN, "首页-录制按钮-录制屏幕次数");
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MACROSCOPIC_DATA, "录制视频次数");
    }

    private void refreshFloatWindowToggleState(){
        if (RecordingManager.getInstance().isRecording()){
            showToastShort(R.string.record_cannot_change_floatingwindiws);
            return;
        }

        String manufacturer = RootUtil.getManufacturer();
        if (manufacturer.equals("MIUI") || manufacturer.equals("OPPO") || manufacturer.equals("VIVO")){
            if (!PreferencesHepler.getInstance().getRecordingSetting().isFloatingWindiws()) {
                if (NormalPreferences.getInstance().getBoolean(Constant.MAIN_VIVO_FIRST_START, true)) {
                    // 在第一次显示弹窗
                    DialogManager.showManufacturerDialog(this, ManufacturerDialog.TYPE_SETTING_BUTTON);
                    NormalPreferences.getInstance().putBoolean(Constant.MAIN_VIVO_FIRST_START, false);
                }
            }
        }

        if (PreferencesHepler.getInstance().getRecordingSetting().isFloatingWindiws()){
            PreferencesHepler.getInstance().saveRecordingSettingFloatingWindiws(false);
            // 隐藏与显示悬浮窗
            RecordingService.toogleFloatView(false);
            FloatViewManager.getInstance().isFloatingWindiws = false;
        } else {
            PreferencesHepler.getInstance().saveRecordingSettingFloatingWindiws(true);
            // 隐藏与显示悬浮窗
            RecordingService.toogleFloatView(true);
            FloatViewManager.getInstance().isFloatingWindiws = true;
        }
    }


    /**
     * 是否进行触摸移动
     */
    private boolean isTouchMoving = false;

    /**
     * 两个按钮所在区域的高度
     */
    private int height;
    private LinearLayout.LayoutParams landscapeParams, portraitParams;
    private int moveY;
    private int downY;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(tag, "onTouch: ");
        if (!isAutoMoving && !RecordingManager.getInstance().isRecording() && firstLayoutParams) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isTouchMoving = true;
                    Log.d(tag, "onTouch: ACTION_DOWN");
                    downY = (int) event.getRawY();
                    Log.d(tag, "onTouch: downY=" + downY);
                    landscapeParams.height = landscape.getHeight();
                    portraitParams.height = portrait.getHeight();
                    break;

                case MotionEvent.ACTION_MOVE:
                    Log.d(tag, "onTouch: ACTION_MOVE");
                    moveY = (int) event.getRawY();
                    Log.d(tag, "onTouch: moveY=" + moveY);
                    int distanceY = moveY - downY;
                    Log.d(tag, "onTouch: distanceY=" + distanceY);
                    height = rootlayout.getHeight();
                    Log.d(tag, "onTouch: height=" + height);

                    if (distanceY > 2) {// 向下滑
                        if (landscapeParams.height < height) {
                            landscapeParams.height = landscapeParams.height + distanceY * 2;
                            portraitParams.height = portraitParams.height - distanceY * 2;
                        }
                    } else if (distanceY < -2) {
                        if (portraitParams.height < height) {// 向上滑
                            portraitParams.height = portraitParams.height - distanceY * 2;
                            landscapeParams.height = landscapeParams.height + distanceY * 2;
                        }
                    }

                    setLandscapeAndPortraitParams(true, false);

                    Log.d(tag, "onTouch: landscapeParams=" + landscapeParams);
                    Log.d(tag, "onTouch: landscapeParams.height=" + landscapeParams.height);
                    Log.d(tag, "onTouch: height=" + height);
                    Log.d(tag, "onTouch: portraitParams=" + portraitParams);
                    Log.d(tag, "onTouch: portraitParams.height=" + portraitParams.height);
                    Log.d(tag, "onTouch: height=" + height);

                    downY = moveY;
                    refreshArrows();

                    refreshFloatWindow();
                    break;

                case MotionEvent.ACTION_UP:
                    Log.d(tag, "onTouch: ACTION_UP");
                    landscapeParams.height = landscape.getHeight();
                    portraitParams.height = portrait.getHeight();
                    height = rootlayout.getHeight();
                    Log.d(tag, "onTouch: height=" + height);

                    if (landscapeParams.height > (height * 0.7)) {// 向下滑动
                        if (landscapeParams.height >= height) {
                            landscapeParams.height = landscape.getHeight();
                        }
                        RequestExecutor.start(new Runnable() {
                            @Override
                            public void run() {
                                int deltaHeight = (height - landscapeParams.height) / 2;
                                for (int i = 0; i < deltaHeight; i++) {
                                    try {
                                        landscapeParams.height = landscapeParams.height + 2;
                                        post(new Runnable() {
                                            @Override
                                            public void run() {
                                                setLandscapeParams(true, false);
                                                refreshArrows();
                                            }
                                        });
                                        Thread.sleep(1);
                                    } catch (InterruptedException e) {

                                        e.printStackTrace();
                                    }
                                }
                                // 字体颜色改变
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        refreshFloatWindow();
                                    }
                                });
                            }
                        });
                    } else if (portraitParams.height > (height * 0.7)) {// 向上滑动
                        if (portraitParams.height >= height) {
                            portraitParams.height = portrait.getHeight();
                        }
                        RequestExecutor.start(new Runnable() {
                            @Override
                            public void run() {
                                int deltaHeight = (height - portraitParams.height) / 2;
                                for (int i = 0; i < deltaHeight; i++) {
                                    try {
                                        portraitParams.height = portraitParams.height + 2;
                                        post(new Runnable() {
                                            @Override
                                            public void run() {
                                                setPortraitParams(true, false);
                                                refreshArrows();
                                            }
                                        });
                                        Thread.sleep(1);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                // 字体颜色改变
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        refreshFloatWindow();
                                    }
                                });
                            }
                        });
                    } else {// 向中间滑动
                        RequestExecutor.start(new Runnable() {

                            @Override
                            public void run() {
                                int deltaHeight = landscapeParams.height - height / 2;
                                Log.d(tag, "onTouch: ACTION_UP/deltaHeight=" + deltaHeight);
                                if (deltaHeight > 0) {// 横屏占大部分
                                    for (int i = 0; i < Math.abs(deltaHeight); i++) {
                                        try {
                                            landscapeParams.height = landscapeParams.height - 1;
                                            post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    setLandscapeParams(true, false);
                                                    refreshArrows();
                                                }
                                            });
                                            Thread.sleep(1);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {// 竖屏占大部分
                                    for (int i = 0; i < Math.abs(deltaHeight); i++) {
                                        try {
                                            portraitParams.height = portraitParams.height - 1;
                                            post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    setPortraitParams(true, false);
                                                }
                                            });
                                            Thread.sleep(1);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                isTouchMoving = false;
                                Log.d(tag, "onTouch: ACTION_UP/isTouchMoving=" + isTouchMoving);
                            }
                        });
                    }
                    break;
            }
        }
        return false;
    }

    private void refreshFloatWindow(){
        if (portraitParams != null) {
            if (portraitParams.height > height - ScreenUtil.dp2px(15)) {
                close.setImageResource(R.drawable.close_blue);
                float_window_text.setTextColor(0xff40a7ff);

                if (PreferencesHepler.getInstance().getRecordingSetting().isFloatingWindiws())
                    float_window_toggle.setImageResource(R.drawable.main_white_bg_open);
                else
                    float_window_toggle.setImageResource(R.drawable.main_white_bg_close);
            } else {
                close.setImageResource(R.drawable.close_white);
                float_window_text.setTextColor(0xffffffff);
                if (PreferencesHepler.getInstance().getRecordingSetting().isFloatingWindiws())
                    float_window_toggle.setImageResource(R.drawable.main_blue_bg_open);
                else
                    float_window_toggle.setImageResource(R.drawable.main_blue_bg_close);
            }
        }
    }

    /**
     * 设置布局
     */
    private void setLandscapeAndPortraitParams(boolean landscapeFlag, boolean portraitFlag) {
        Log.d(tag, "setLandscapeAndPortraitParams: ");
        if (portrait == null)
            return;
        if (landscape == null)
            return;
        if (portraitParams == null)
            return;
        if (landscapeParams == null)
            return;

        if (landscapeParams.height < 0)
            landscapeParams.height = 0;
        if (portraitParams.height < 0)
            portraitParams.height = 0;
        if (landscapeFlag) {
            if (landscapeParams.height > height)
                landscapeParams.height = height;
        }
        if (portraitFlag) {
            if (portraitParams.height > height)
                portraitParams.height = height;
        }
        landscape.setLayoutParams(landscapeParams);
        portrait.setLayoutParams(portraitParams);
    }

    /**
     * 根据横屏设置布局
     */
    private void setLandscapeParams(boolean landscapeFlag, boolean portraitFlag) {
        Log.d(tag, "setLandscapeParams: ");
        if (portrait == null)
            return;
        if (landscape == null)
            return;
        if (portraitParams == null)
            return;
        if (landscapeParams == null)
            return;

        if (landscapeParams.height < 0)
            landscapeParams.height = 0;
        if (landscapeFlag) {
            if (landscapeParams.height > height)
                landscapeParams.height = height;
        }
        portraitParams.height = height - landscapeParams.height;
        if (portraitParams.height < 0)
            portraitParams.height = 0;
        if (portraitFlag) {
            if (portraitParams.height > height)
                portraitParams.height = height;
        }
        landscape.setLayoutParams(landscapeParams);
        portrait.setLayoutParams(portraitParams);
    }

    /**
     * 根据竖屏设置布局
     */
    private void setPortraitParams(boolean landscapeFlag, boolean portraitFlag) {
        Log.d(tag, "setPortraitParams: ");
        if (portrait == null)
            return;
        if (landscape == null)
            return;
        if (portraitParams == null)
            return;
        if (landscapeParams == null)
            return;

        if (portraitParams.height < 0)
            portraitParams.height = 0;
        if (portraitFlag) {
            if (portraitParams.height > height)
                portraitParams.height = height;
        }
        landscapeParams.height = height - portraitParams.height;
        if (landscapeParams.height < 0)
            landscapeParams.height = 0;
        if (landscapeFlag) {
            if (landscapeParams.height > height)
                landscapeParams.height = height;
        }
        portrait.setLayoutParams(portraitParams);
        landscape.setLayoutParams(landscapeParams);
    }

    /**
     * 是否正在自行滑动
     */
    private boolean isAutoMoving = false;
    private static final int AUTO_MOVING_TIME = 1;// million seconds
    private static final int AUTO_MOVING_LENGTH = 25;// px
    private static final float AUTO_MOVING_STEP = 0.7f;// px

    /**
     * 自行向上滑动（滑到顶部）
     */
    private void autoMoveUp() {
        if (!isTouchMoving && !isAutoMoving && firstLayoutParams && portraitParams != null) {
            Log.d(tag, "autoMoveUp: ");
            isAutoMoving = true;
            RequestExecutor.start(new Runnable() {
                @Override
                public void run() {
                    while ((portraitParams.height = portrait.getHeight()) < height) {
                        portraitParams.height = portraitParams.height + AUTO_MOVING_LENGTH;
                        post(new Runnable() {
                            @Override
                            public void run() {
                                setPortraitParams(true, false);
                                refreshArrows();
                            }
                        });
                        try {
                            Thread.sleep(AUTO_MOVING_TIME);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    isAutoMoving = false;
                }
            });
        }
    }

    /**
     * 自行向下滑动（滑到底部）
     */
    private void autoMoveDown() {
        if (!isTouchMoving && !isAutoMoving && firstLayoutParams && landscapeParams != null) {
            Log.d(tag, "autoMoveDown: ");
            isAutoMoving = true;
            RequestExecutor.start(new Runnable() {
                @Override
                public void run() {
                    while ((landscapeParams.height = landscape.getHeight()) < height) {
                        landscapeParams.height = landscapeParams.height + AUTO_MOVING_LENGTH;
                        post(new Runnable() {
                            @Override
                            public void run() {
                                setLandscapeParams(true, false);
                                refreshArrows();
                            }
                        });
                        try {
                            Thread.sleep(AUTO_MOVING_TIME);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    isAutoMoving = false;
                }
            });
        }
    }

    /**
     * 自行向下滑动（滑动一定的距离）
     *
     * @param downDip 滑动的总长（dip）
     */
    private void autoMoveDown(final float downDip, final Runnable r) {
        if (!isTouchMoving && !isAutoMoving && firstLayoutParams && landscapeParams != null) {
            Log.d(tag, "autoMoveDown: ");
            isAutoMoving = true;
            RequestExecutor.start(new Runnable() {
                @Override
                public void run() {
                    int deltaHeight = dp2px(downDip);
                    Log.d(tag, "autoMoveDown: downDip=" + downDip);
                    Log.d(tag, "autoMoveDown: deltaHeight=" + deltaHeight);
                    for (int i = 0; i < deltaHeight; i++) {
                        try {
                            landscapeParams.height = landscapeParams.height + 1;
                            post(new Runnable() {
                                @Override
                                public void run() {
                                    setLandscapeParams(true, false);
                                    refreshArrows();
                                }
                            });
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    isAutoMoving = false;
                    if (r != null)
                        post(r);
                }
            });
        }
    }

    /**
     * 自行向中间滑动
     *
     * @param step 每步滑动的步进 [1, ++]
     */
    private void autoMoveCenter(final int step, final Runnable r) {
        if (!isTouchMoving && !isAutoMoving && firstLayoutParams &&
                landscapeParams != null && portraitParams != null) {
            Log.d(tag, "autoMoveCenter: ");
            isAutoMoving = true;
            RequestExecutor.start(new Runnable() {
                @Override
                public void run() {

                    int deltaHeight = landscapeParams.height - (height / 2);
                    Log.d(tag, "autoMoveCenter: deltaHeight=" + deltaHeight);
                    if (deltaHeight > 0) {// 横屏占大部分
                        for (int i = 0; i < Math.abs(deltaHeight) / step; i++) {
                            try {
                                landscapeParams.height = landscapeParams.height - step;
                                post(new Runnable() {
                                    @Override
                                    public void run() {

                                        setLandscapeParams(true, false);
                                        refreshArrows();
                                    }
                                });
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {// 竖屏占大部分
                        for (int i = 0; i < Math.abs(deltaHeight) / step; i++) {
                            try {
                                portraitParams.height = portraitParams.height - step;
                                post(new Runnable() {
                                    @Override
                                    public void run() {

                                        setPortraitParams(true, false);
                                        refreshArrows();
                                    }
                                });
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (r != null)
                        post(r);
                    isAutoMoving = false;
                }
            });
        }
    }

// ------------------------------------------------------------------

    /**
     * 暂停，继续录屏
     */
    private void toogleRecording() {
        if (RecordingManager.getInstance().isRecording()) {// 录屏中
            if (RecordingManager.getInstance().isPausing()) {// 暂停中
                // 继续录屏
                RecordingService.resumeScreenRecord();
            } else {// 录屏中
                // 暂停录屏
                RecordingService.pauseScreenRecord();
            }
        }
    }

    /**
     * 开始录屏
     */
    private void startRecording() {
        // 开始录屏
        RecordingService.startScreenRecord();
    }

    /**
     * 停止录屏
     */
    private void stopRecording() {
        // 停止录屏
        RecordingService.stopScreenRecord();
    }

    /**
     * 显示浮窗1
     */
    private void showFloatView() {
        // 显示浮窗1
        RecordingService.showFloatView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        ObserveManager.getInstance().removeRecordingObservable(this);
        ObserveManager.getInstance().removeRecording2Observable(this);
        super.onDestroy();
    }

    @Override
    public void onRecording2() {
        try {
            refreshContentView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRecording() {
        post(new Runnable() {
            @Override
            public void run() {

                try {
                    refreshContentView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 组件间的通讯事件：5.0录屏（获取权限）
     */

    public void onEventMainThread(ScreenRecordPermission2MainEvent event) {

    }

    /**
     *
     */
    public void onEventMainThread(CountDownFinishEvent event){
        startRecording();
        // 模拟home键
        AppUtil.simulateHomeKey(this);
    }

    /**
     *
     */
    public void onEventMainThread(SetFloatWindowEvent event){
        refreshFloatWindow();
    }
}
