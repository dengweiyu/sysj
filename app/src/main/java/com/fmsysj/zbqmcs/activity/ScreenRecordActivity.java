package com.fmsysj.zbqmcs.activity;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fmsysj.zbqmcs.floatview.FloatContentView;
import com.fmsysj.zbqmcs.floatview.FloatViewManager;
import com.fmsysj.zbqmcs.frontcamera.CameraView;
import com.fmsysj.zbqmcs.record.Recorder44;
import com.fmsysj.zbqmcs.record.Settings;
import com.fmsysj.zbqmcs.floatview.FloatViewService;
import com.fmsysj.zbqmcs.service.PanelTimeService;
import com.fmsysj.zbqmcs.service.PanelTimeService.TimeBinder;
import com.fmsysj.zbqmcs.service.ScreenRECService;
import com.fmsysj.zbqmcs.utils.ExApplication;
import com.fmsysj.zbqmcs.utils.LaunchCheckThread;
import com.fmsysj.zbqmcs.utils.RecordVideo;
import com.li.videoapplication.R;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.BaseActivity;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.VideoMangerActivity;
import com.li.videoapplication.ui.toast.ToastHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 碎片：录屏
 */
@SuppressLint("SdCardPath")
public class ScreenRecordActivity extends BaseActivity implements
        OnClickListener,
        OnTouchListener {
    /**
     * 录屏
     */
    public synchronized final static void startScreenRecordActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, ScreenRecordActivity.class);
        context.startActivity(intent);
    }

    /**
     * 跳转
     */
    public synchronized static void showNewTask() {
        Context context = AppManager.getInstance().getContext();
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, ScreenRecordActivity.class);
        context.startActivity(intent);
    }

    /**
     * 跳转：
     */
    private void startPackageInfoGridview() {
        PackageInfoGridview.show(this);
    }

    /**
     * 跳转：
     */
    private void startActivityActionMain() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    public static boolean isInMain = true;

    /**
     * 横竖屏录制按钮
     */
    private ImageView btStart_horizontal, btStart_vertical;
    /**
     * 录屏时暂停继续linearlayout
     */
    private LinearLayout recording_vertical_ll, recording_horizontal_ll;
    /**
     * 暂停按钮
     */
    private ImageView pause_bt_verticall, pause_bt_horizontal;
    /**
     * 停止按钮
     */
    private ImageView stop_bt_verticall, stop_bt_horizontal;
    /**
     * 录屏提示
     */
    private TextView vertical_hint, horizontal_hint;
    /**
     * 计时文本框
     */
    private TextView vertical_timing, horizontal_timing;
    private LinearLayout buttonLinearlayout;
    /**
     * 视频按钮
     */
    private LinearLayout videoButton;
    /**
     * 设置按钮
     */
    private LinearLayout settingButton;
    /**
     * 分割线
     */
    private View halvingLine;
    /**
     * 关闭按钮
     */
    private ImageButton close;
    /**
     * ROOT检查类
     */
    private LaunchCheckThread launchCheckThread;
    /**
     * 是否进行无悬浮窗录制
     */
    boolean floatview = false;

    public static boolean isFirstTimeUse = true; // 判断是否启动第一次调用这界面

    private String HMSTimes;
    public static String path_dir;
    /**
     * 录制视频名称
     */
    public static String videofilename;
    public static int videolong = 0;
    SharedPreferences sp;
    public static boolean startRecord = false;
    public static boolean endRecord = false;
    public static boolean startFloatService = false;

    public static int SDKVesion = 1;
    public Context mContext;
    // 手机分辨率_高，默认值为720
    public static int ScreenH = 720;
    // 手机分辨率—_宽，默认值为1086
    public static int ScreenW = 1086;
    public static float density;
    private FrameLayout container;

    private String storeDir;

    /**
     * 上、下箭头
     */
    private ImageView down_img, up_img;
    /**
     * 上、下箭头点击区域
     */
    private LinearLayout up, down;
    /**
     * 横竖屏区域
     */
    private FrameLayout frameLayout1, frameLayout2;
    private FrameLayout mainFramelayout;
    /**
     * 录屏时阴影遮罩
     */
    private ImageView record_horizontal_shade, record_vertical_shade;
    /**
     * 录屏提示页
     */
    private LinearLayout hintLinearlayout;
    /**
     * 录屏提示按钮
     */
    private ImageView hintButton;

    private LayoutParams para, para2, mainPara;
    private int preY;
    public int preYFirst;
    private RecordVideo recordVideo;
    /**
     * ontouch事件是否进行过移动
     */
    boolean isOnTouchMove = false;
    /**
     * 如果当前横竖屏模式正进行动画中，暂时屏蔽其他的动画点击事件
     */
    boolean isMoving = false;
    private Intent panelTimeService;
    /**
     * 滑动提示文本
     */
    private TextView hintTextView;

    /**
     * 发送广播
     */
    public static void sendEmptyMessage(int what) {
        ScreenRecordActivity activity = (ScreenRecordActivity) AppManager.getInstance().getActivity(ScreenRecordActivity.class);
        if (activity != null && activity.handler != null)
            activity.handler.sendEmptyMessage(what);
    }

    /**
     * 发送广播
     */
    public static void sendMessage(Message msg) {
        ScreenRecordActivity activity = (ScreenRecordActivity) AppManager.getInstance().getActivity(ScreenRecordActivity.class);
        if (activity != null && activity.handler != null)
            activity.handler.sendMessage(msg);
    }

    /**
     * 消息处理
     */
    public Handler handler = new Handler() {

        @SuppressLint("Recycle")
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {

                case 1:
                    launchCheckThread.createCustomDialog(ScreenRecordActivity.this);
                    break;

                case 2:
                    launchCheckThread.dialog_Root(ScreenRecordActivity.this);
                    break;

                case 3:
                    frameLayout1.setLayoutParams(para);
                    para2.height = ExApplication.height - para.height;
                    if (para2.height < 0) {
                        para2.height = 0;
                    }
                    frameLayout2.setLayoutParams(para2);
                    break;

                case 4:
                    frameLayout2.setLayoutParams(para2);
                    para.height = ExApplication.height - para2.height;
                    if (para.height <= 0) {
                        para.height = 1;
                    }
                    frameLayout1.setLayoutParams(para);
                    break;

                case 5:// 横屏计时
                    HMSTimes = (String) msg.obj;
                    horizontal_timing.setText(HMSTimes);
                    break;

                case 6:// 竖屏计时
                    HMSTimes = (String) msg.obj;
                    vertical_timing.setText(HMSTimes);
                    break;

                case 7:// 横屏初始化录屏状态，开始录屏

                    // 判断android系统版本
                    if (ScreenRecordActivity.SDKVesion >= 19) {
                        btStart_horizontal.setVisibility(View.GONE);
                        recording_horizontal_ll.setVisibility(View.VISIBLE);
                        // 录屏文字提示
                        horizontal_hint.setVisibility(View.VISIBLE);

                    } else {
                        btStart_horizontal.setImageResource(R.drawable.btn_selector_record_stop);
                    }
                    // 开始录屏
                    recordVideo.stardRecordVideo();
                    // 设置阴影遮罩
                    record_vertical_shade.setVisibility(View.VISIBLE);

                    // 开始计时
                    panelTimeService.putExtra("which", 0);
                    startService(panelTimeService);
                    mContext.bindService(panelTimeService, timeConn, Service.BIND_AUTO_CREATE);

                    startFloatService = true;
                    isInMain = false;
                    FloatContentView.isFirstTimeUse = true;
                    // 如果是5.0以下版本，直接回到桌面
                    if (ScreenRecordActivity.SDKVesion < 21) {
                        startActivityActionMain();
                    }
                    ExApplication.stopTimeService = false;
                    break;

                case 8:// 竖屏初始化录屏状态，开始录屏
                    // 判断android系统版本
                    if (ScreenRecordActivity.SDKVesion >= 19) {
                        // 将带有暂停模式录屏状态恢复为待录制状态
                        btStart_vertical.setVisibility(View.GONE);
                        recording_vertical_ll.setVisibility(View.VISIBLE);
                    } else {

                        btStart_vertical
                                .setImageResource(R.drawable.btn_selector_record_stop);
                    }
                    // 开始录屏
                    recordVideo.stardRecordVideo();
                    // 设置阴影遮罩
                    record_horizontal_shade.setVisibility(View.VISIBLE);
                    // 开始计时
                    panelTimeService.putExtra("which", 1);
                    startService(panelTimeService);
                    mContext.bindService(panelTimeService, timeConn,
                            Service.BIND_AUTO_CREATE);
                    startFloatService = true;
                    isInMain = false;
                    FloatContentView.isFirstTimeUse = true;

                    // 如果是5.0以下版本，直接回到桌面
                    if (ScreenRecordActivity.SDKVesion < 21) {
                        startActivityActionMain();
                    }
                    ExApplication.stopTimeService = false;
                    break;

                case 9:
                    initRecordState();
                    break;

                case 10:
                    finish();
                    break;

                case 11:// 退出录屏大师
                    ExitApp();
                    break;
            }
        }
    };

    private PanelTimeService.TimeBinder timeBinder;

    // 计时服务绑定
    private ServiceConnection timeConn = new ServiceConnection() {

        public void onServiceDisconnected(ComponentName name) {
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            timeBinder = (TimeBinder) service;
            timeBinder.startTime();
        }
    };

    /**
     * 开启录屏服务
     */
    private void startScreenRECService() {
        Intent intent = new Intent();
        intent.setClass(ScreenRecordActivity.this, ScreenRECService.class);
        startService(intent);
    }

    /**
     * 停止录屏服务
     */
    private void stopScreenRECService() {
        Context context = AppManager.getInstance().getApplication();
        Intent intent = new Intent(context, ScreenRECService.class);
        context.stopService(intent);
    }

    /**
     * 开启悬浮窗服务
     */
    private void startFloatViewService() {
        Intent intent = new Intent();
        intent.setClass(ScreenRecordActivity.this, FloatViewService.class);
        startService(intent);
    }

    /**
     * 停止悬浮窗服务
     */
    private void stopFloatViewService() {
        Context context = AppManager.getInstance().getApplication();
        Intent intent = new Intent(context, FloatViewService.class);
        context.stopService(intent);
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
    public void afterOnCreate() {
        super.afterOnCreate();

        isInMain = true;
        setContentView(R.layout.activity_screenrecord);

        mContext = getApplicationContext();

        initContentView();

        /**#########################################*/

        // 初始化数据
        initData();
        if (SYSJStorageUtil.getSysj() != null) {
            storeDir = SYSJStorageUtil.getSysj().getAbsolutePath();
        }

        // 开启录屏服务
        startScreenRECService();

        getDisplayMetrics();

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // 获得当前设备系统版本
        SDKVesion = getAndroidSDKVersion();
        // 对文件储存路径是否存在进行判断
        if (SYSJStorageUtil.getSysjRec() != null) {
            path_dir = SYSJStorageUtil.getSysjRec().getPath();
        } else {
            launchCheckThread.AlertDialoshow();
        }

        // 获得当前设备的分辨率
        Display mDisplay = getWindowManager().getDefaultDisplay();
        ExApplication.DEVW = mDisplay.getWidth();
        ExApplication.DEVH = mDisplay.getHeight();

        Settings.width = (int) (ScreenRecordActivity.ScreenW / 1.7);
        Settings.height = (int) (ScreenRecordActivity.ScreenH / 1.7);

        // 检测关闭前置摄像头
        if (!ExApplication.floatCameraClose) {
            CameraView.closeFloatView();
        }

        // 初始化录屏方法类
        recordVideo = new RecordVideo(AppManager.getInstance().getContext());

        iniViewState();
    }

    @Override
    protected void onDestroy() {
        try {
            handler.removeCallbacksAndMessages(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        isInMain = false;
        super.onDestroy();
    }

    /**
     * 初始化数据
     */
    private void initData() {

        // 子线程进行root、存储空间、版本等检查
        launchCheckThread = new LaunchCheckThread(ScreenRecordActivity.this);
        launchCheckThread.start();
        // 初始化后台计时服务
        panelTimeService = new Intent(this, PanelTimeService.class);
    }

    /**
     * 初始化控件状态
     */
    private void iniViewState() {

        // 如果是横屏录屏并且处于无悬浮窗录屏中
        if (sp.getBoolean("horizontalRecord", false)
                && RecordVideo.isRecordering
                && sp.getBoolean("no_float_view_record", false)) {

            if (SDKVesion >= 19) {
                btStart_horizontal.setVisibility(View.GONE);
                recording_horizontal_ll.setVisibility(View.VISIBLE);
                horizontal_hint.setVisibility(View.VISIBLE);
            } else {

            }

            record_vertical_shade.setVisibility(View.VISIBLE);
        } else if (!sp.getBoolean("horizontalRecord", false)
                && RecordVideo.isRecordering
                && sp.getBoolean("no_float_view_record", false)) {

            if (SDKVesion >= 19) {
                btStart_vertical.setVisibility(View.GONE);
                recording_vertical_ll.setVisibility(View.VISIBLE);
                vertical_hint.setVisibility(View.VISIBLE);
            } else {

            }

            record_horizontal_shade.setVisibility(View.VISIBLE);
        }

        // 初始化横竖屏framlayout控件高度
        int para1_height = sp.getInt("framelayout1Height", ExApplication.height / 2);
        int para2_height = sp.getInt("framelayout2Height", ExApplication.height / 2);

        if (para1_height != 0 || para2_height != 0) {
            if (para == null) {
                para = frameLayout1.getLayoutParams();
            }
            if (para2 == null) {
                para2 = frameLayout2.getLayoutParams();
            }

            para.height = para1_height;
            para2.height = para2_height;
            frameLayout1.setLayoutParams(para);
            frameLayout2.setLayoutParams(para2);
        }
        int mHeight = para1_height + para2_height;
        // 初始化上下按钮浮标
        if (para1_height > mHeight * 0.7) {
            down_img.setImageResource(R.drawable.main_up_0);
        } else if (para2_height > mHeight * 0.7) {

            up_img.setImageResource(R.drawable.main_down);
        }

    }

    /**
     * 将控件高度存到sp
     */
    private void saveFramelayoutHeight(int height1, int height2) {
        sp.edit().putInt("framelayout1Height", height1).commit();
        sp.edit().putInt("framelayout2Height", height2).commit();
    }

    // 首次启动应用显示遮罩层
    private void showMasked() {
        // 如果没有显示过遮罩层
        if (sp.getBoolean("showMaskedInMain", true)) {

            hintLinearlayout.setVisibility(View.VISIBLE);
            hintTextView.setVisibility(View.VISIBLE);
            sp.edit().putBoolean("showMaskedInMain", false).commit();

        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (!ExApplication.ISCOUNT) {
            // 底部按钮的高度
            int buttonHeight = buttonLinearlayout.getHeight();
            mainPara = mainFramelayout.getLayoutParams();
            // 设置中间两个录屏按钮所在layout高度
            mainPara.height = mainFramelayout.getHeight() - buttonHeight;
            mainFramelayout.setLayoutParams(mainPara);

            para = frameLayout1.getLayoutParams();
            para.height = frameLayout1.getHeight();
            para2 = frameLayout2.getLayoutParams();
            para2.height = frameLayout2.getHeight();

            // 获取应用区域高度
            ExApplication.height = para.height + para2.height - buttonHeight;
            ExApplication.moveHeight = para.height + para2.height - buttonHeight;
            ExApplication.ISCOUNT = true;
        }
        // 提示遮罩层
        showMasked();
    }

    /**
     * 查找控件
     */
    @SuppressLint("ResourceAsColor")
    private void initContentView() {

        container = (FrameLayout) findViewById(R.id.container);
        btStart_horizontal = (ImageView) findViewById(R.id.main_record_server_bt_horizontal);
        btStart_vertical = (ImageView) findViewById(R.id.main_record_server_bt_vertical);

        // 箭头图片
        down_img = (ImageView) findViewById(R.id.down_img);
        up_img = (ImageView) findViewById(R.id.up_img);
        // 箭头按钮
        up = (LinearLayout) findViewById(R.id.up_button);
        down = (LinearLayout) findViewById(R.id.down_button);
        hintTextView = (TextView) findViewById(R.id.fm_main_hint_text);

        frameLayout1 = (FrameLayout) findViewById(R.id.frameLayout1);
        frameLayout2 = (FrameLayout) findViewById(R.id.frameLayout2);
        mainFramelayout = (FrameLayout) findViewById(R.id.fm_main_record_server_framelayout);
        hintLinearlayout = (LinearLayout) findViewById(R.id.fm_main_hint_linearlayout);
        recording_horizontal_ll = (LinearLayout) findViewById(R.id.main_recording_bt_horizontal);
        recording_vertical_ll = (LinearLayout) findViewById(R.id.main_recording_bt_vertical);
        hintButton = (ImageView) findViewById(R.id.fm_main_hint_button);
        // 暂停按钮
        pause_bt_horizontal = (ImageView) findViewById(R.id.main_record_server_bt_horizontal_pause);
        pause_bt_verticall = (ImageView) findViewById(R.id.main_record_server_bt_vertical_pause);
        // 停止按钮
        stop_bt_horizontal = (ImageView) findViewById(R.id.main_record_server_bt_horizontal_stop);
        stop_bt_verticall = (ImageView) findViewById(R.id.main_record_server_bt_vertical_stop);
        // 录屏阴影遮罩
        record_vertical_shade = (ImageView) findViewById(R.id.fm_main_record_vertical_shade);
        record_horizontal_shade = (ImageView) findViewById(R.id.fm_main_record_horizontal_shade);
        // 提示文本框
        vertical_hint = (TextView) findViewById(R.id.fm_main_record_vertical_hint);
        horizontal_hint = (TextView) findViewById(R.id.fm_main_record_horizontal_hint);
        // 计时文本框
        horizontal_timing = (TextView) findViewById(R.id.fm_main_record_horizontal_timing);
        vertical_timing = (TextView) findViewById(R.id.fm_main_record_vertical_timing);
        videoButton = (LinearLayout) findViewById(R.id.main_button_video);
        settingButton = (LinearLayout) findViewById(R.id.main_button_setting);
        buttonLinearlayout = (LinearLayout) findViewById(R.id.main_button_linearlayout);
        halvingLine = (View) findViewById(R.id.halvingLine);
        close = (ImageButton) findViewById(R.id.main_close);

        hintButton.setOnClickListener(this);
        up.setOnClickListener(this);
        down.setOnClickListener(this);
        up.setOnTouchListener(this);
        down.setOnTouchListener(this);

        btStart_horizontal.setOnClickListener(this);
        btStart_vertical.setOnClickListener(this);

        hintButton.setOnClickListener(this);
        // 暂停，停止按钮
        pause_bt_horizontal.setOnClickListener(this);
        stop_bt_verticall.setOnClickListener(this);
        pause_bt_verticall.setOnClickListener(this);
        stop_bt_horizontal.setOnClickListener(this);
        videoButton.setOnClickListener(this);
        settingButton.setOnClickListener(this);
        close.setOnClickListener(this);
        OnTouchListener onTouchListener = new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 将点击事件进行吞没，不传递到下一层
                return true;
            }
        };
        hintLinearlayout.setOnTouchListener(onTouchListener);
        record_vertical_shade.setOnTouchListener(onTouchListener);
        record_horizontal_shade.setOnTouchListener(onTouchListener);
    }

    /**
     * 获取手机分辨率
     **/
    public static void getDisplayMetrics() {
        Context context = AppManager.getInstance().getContext();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        ScreenW = dm.widthPixels;
        ScreenH = dm.heightPixels;
        density = dm.density;

    }

    /**
     * 判断手机屏幕方向 横屏返回true,竖屏返回false
     **/
    public static boolean getConfiguration() {
        Context context = AppManager.getInstance().getContext();
        Configuration config = context.getResources().getConfiguration();
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isMoving && !RecordVideo.isRecordering) {
            myOnTouchEvent(event);
        }
        return true;
    }

    /**
     * 触摸事件动画
     *
     * @param event
     */
    private void myOnTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                preYFirst = (int) event.getRawY();
                para.height = frameLayout1.getHeight();
                para2.height = frameLayout2.getHeight();
                ExApplication.height = para.height + para2.height;

                break;
            case MotionEvent.ACTION_MOVE:

                preY = (int) event.getRawY();

                if (preYFirst < preY) {// 向下滑

                    // 如果linearlayout1d的高度不超过屏幕高度
                    if (para.height < ExApplication.height) {

                        para.height = para.height + (preY - preYFirst);
                        frameLayout1.setLayoutParams(para);
                        para2.height = para2.height - (preY - preYFirst);
                        if (para2.height < 0) {
                            para2.height = 0;
                        }
                        if (para.height > ExApplication.moveHeight) {
                            para.height = ExApplication.moveHeight;
                        }
                        frameLayout2.setLayoutParams(para2);

                    }

                } else if (preYFirst > preY) {

                    if (para2.height < ExApplication.height) {// 向上滑

                        para2.height = para2.height + (preYFirst - preY);
                        frameLayout2.setLayoutParams(para2);
                        para.height = para.height - (preYFirst - preY);
                        if (para.height < 0) {
                            para.height = 0;
                        }
                        frameLayout1.setLayoutParams(para);

                    }

                }

                preYFirst = preY;

                break;
            case MotionEvent.ACTION_UP:

                // 如果para的高度大于屏幕高度的70%，则向下递增高度
                if (para.height > (ExApplication.height * 0.7)) {// 横屏录屏按钮全屏
                    // 如果para.height超过了应用高度
                    if (para.height >= ExApplication.height) {
                        // 重新设定高度
                        para.height = frameLayout1.getHeight();

                    }
                    new Thread() {
                        public void run() {

                            // 当前linearyout与屏幕的高度差，作为循环的次数
                            int mHeight = (ExApplication.height - para.height) / 2;

                            for (int i = 0; i < mHeight; i++) {
                                try {
                                    // 循环遍历，模拟移动下滑动画
                                    para.height = para.height + 2;
                                    handler.sendEmptyMessage(3);
                                    Thread.sleep(1);
                                } catch (InterruptedException e) {

                                    e.printStackTrace();
                                }

                            }

                            saveFramelayoutHeight(ExApplication.height, 1);
                        }
                    }.start();
                    down_img.setImageResource(R.drawable.main_up_0);
                    halvingLine.setVisibility(View.GONE);
                } else if (para2.height > (ExApplication.height * 0.7)) {// 竖屏录屏按钮全屏

                    // 如果para.height超过了应用高度
                    if (para2.height >= ExApplication.height) {
                        // 重新设定高度
                        para2.height = frameLayout2.getHeight();

                    }
                    new Thread() {
                        public void run() {
                            int mHeight = (ExApplication.height - para2.height) / 2;
                            for (int i = 0; i < mHeight; i++) {
                                try {

                                    para2.height = para2.height + 2;
                                    handler.sendEmptyMessage(4);
                                    Thread.sleep(1);

                                } catch (InterruptedException e) {

                                    e.printStackTrace();
                                }

                            }
                            saveFramelayoutHeight(1, ExApplication.height);
                        }
                    }.start();
                    up_img.setImageResource(R.drawable.main_down);
                    halvingLine.setVisibility(View.GONE);
                } else {// 横屏，录屏占据二分之一
                    new Thread() {
                        public void run() {
                            // 计算para与屏幕二分之一高度的高度差
                            int mHeight = para.height - (ExApplication.height / 2);
                            // 将高度差绝对值化
                            int abs = Math.abs(mHeight);
                            if (mHeight > 0) {
                                for (int i = 0; i < abs; i++) {
                                    try {
                                        Thread.sleep(1);
                                        para.height = para.height - 1;
                                        handler.sendEmptyMessage(3);

                                    } catch (InterruptedException e) {

                                        e.printStackTrace();
                                    }

                                }
                            } else {
                                for (int i = 0; i < abs; i++) {
                                    try {
                                        Thread.sleep(1);
                                        para.height = para.height + 1;
                                        handler.sendEmptyMessage(3);

                                    } catch (InterruptedException e) {

                                        e.printStackTrace();
                                    }

                                }

                            }
                            saveFramelayoutHeight(frameLayout1.getHeight(),
                                    frameLayout2.getHeight());
                        }
                    }.start();
                    up_img.setImageResource(R.drawable.main_up_0);
                    down_img.setImageResource(R.drawable.main_down);
                    halvingLine.setVisibility(View.VISIBLE);
                }

                break;
        }

    }

    @Override
    public void onClick(View v) {

        if (v == btStart_horizontal) {
            // 如果正在进行悬浮窗录屏，按钮点击无效
            if (!floatview && RecordVideo.isRecordering) {
                ToastHelper.s("正在录屏中，请结束录屏后再试试");
            } else {
                record_main(0);
            }
        } else if (v == btStart_vertical) {
            // 如果正在进行悬浮窗录屏，按钮点击无效
            if (!floatview && RecordVideo.isRecordering) {
                ToastHelper.s("正在录屏中，请结束录屏后再试试");
            } else {
                record_main(1);
            }
        } else if (v == up) {
            if (!isOnTouchMove && !isMoving && !RecordVideo.isRecordering) {
                if (frameLayout2.getHeight() / 2 > frameLayout1.getHeight()) {
                    down_move();
                    halvingLine.setVisibility(View.VISIBLE);
                } else {
                    up_move();
                    halvingLine.setVisibility(View.GONE);
                }
            } else {
                isOnTouchMove = false;
            }
        } else if (v == down) {

            if (!isOnTouchMove && !isMoving && !RecordVideo.isRecordering) {
                if (!isOnTouchMove && !isMoving && !RecordVideo.isRecordering) {
                    if (frameLayout1.getHeight() / 2 > frameLayout2.getHeight()) {
                        up_move();
                        halvingLine.setVisibility(View.VISIBLE);
                    } else {

                        down_move();
                        halvingLine.setVisibility(View.GONE);

                    }
                } else {
                    isOnTouchMove = false;
                }
            }
        } else if (v == hintButton) {// 隐藏遮罩层
            // 防止双击
            hintButton.setClickable(false);
            final Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.zoom_exit);
            // 隐藏遮罩层
            hintLinearlayout.startAnimation(animation);
            hintLinearlayout.setVisibility(View.GONE);

            setSimulateClick();
            TimerTask task = new TimerTask() {

                public void run() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            hintTextView.startAnimation(animation);
                            hintTextView.setVisibility(View.GONE);
                        }
                    });

                }
            };
            // 五秒后隐藏文字提示框
            Timer timer = new Timer();
            timer.schedule(task, 5000);
        } else if (v == pause_bt_horizontal) {
            pauseRecord(pause_bt_horizontal, horizontal_timing);
        } else if (v == stop_bt_verticall) {
            stopRecord();
        } else if (v == pause_bt_verticall) {
            pauseRecord(pause_bt_verticall, vertical_timing);
        } else if (v == stop_bt_horizontal) {
            stopRecord();
        } else if (v == videoButton) {
            Intent intent = new Intent(this, VideoMangerActivity.class);
            startActivity(intent);
        } else if (v == settingButton) {
            ActivityManeger.startSettingActivity(this);
        } else if (v == close) {
            finish();
        }
    }

    /**
     * 向上移动动画
     */
    private void up_move() {
        isMoving = true;

        final int mHeight = ExApplication.moveHeight / 4;

        new Thread() {
            public void run() {

                for (int i = 0; i < mHeight; i++) {
                    try {
                        Thread.sleep(1);
                        para2.height = para2.height + 2;
                        handler.sendEmptyMessage(4);

                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }

                }
                isMoving = false;
                saveFramelayoutHeight(1, ExApplication.height);
                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        if (para2.height / 2 > para.height) {

                            up_img.setImageResource(R.drawable.main_down);

                        } else {
                            up_img.setImageResource(R.drawable.main_up_0);

                        }
                        down_img.setImageResource(R.drawable.main_down);

                    }
                });
            }
        }.start();

    }

    /**
     * 向下移动动画
     */
    private void down_move() {
        isMoving = true;

        // 当前linearyout与屏幕的高度差，作为循环的次数
        final int mHeight = ExApplication.moveHeight / 4;

        new Thread() {
            public void run() {
                for (int i = 0; i < mHeight; i++) {
                    try {
                        Thread.sleep(1);
                        // 循环遍历，模拟移动下滑动画
                        para.height = para.height + 2;
                        handler.sendEmptyMessage(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                saveFramelayoutHeight(ExApplication.height, 1);
                isMoving = false;
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        if (para.height / 2 > para2.height) {
                            down_img.setImageResource(R.drawable.main_up_0);
                        } else {
                            down_img.setImageResource(R.drawable.main_down);
                        }
                        up_img.setImageResource(R.drawable.main_up_0);
                    }
                });
            }
        }.start();
    }

    /**
     * 首次启动时进行动画移动提示
     */
    @SuppressLint("Recycle")
    private void setSimulateClick() {
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(1000);
                    for (int i = 0; i < 150; i++) {
                        // 循环遍历，模拟移动下滑动画
                        para.height = para.height + 1;
                        handler.sendEmptyMessage(3);
                        Thread.sleep(1);
                    }
                    for (int i = 0; i < 150; i++) {
                        // 循环遍历，模拟移动下滑动画
                        para2.height = para2.height + 1;
                        handler.sendEmptyMessage(4);
                        Thread.sleep(1);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private void stopRecord() {
        ExApplication.pauseRecVideo = false;
        if (ScreenRecordActivity.SDKVesion >= 21) {
            // 停止录屏
            ScreenRecord50.stopRecord();
            ScreenRecord50.startRecoing = false;
        } else {
            // 根据安卓系统版本不同分别进行视频合成
            if (ScreenRecordActivity.SDKVesion < 19) {// 4.4以下
                recordVideo.StopRecordForVesionOther();
            } else if (ScreenRecordActivity.SDKVesion >= 19
                    && ScreenRecordActivity.SDKVesion < 21) {// 4.4
                recordVideo.StopRecordForVesion19();
            }
        }
        // 初始化录屏状态
        initRecordState();
    }

    /**
     * 初始化录屏状态
     */
    private void initRecordState() {
        // 关闭触摸显示
        android.provider.Settings.System.putInt(mContext.getContentResolver(), "show_touches", 0);
        // 关闭画中画
        if (ExApplication.floatCameraClose == false) {
            CameraView.closeFloatView();

        }
        // 关闭阴影
        record_vertical_shade.setVisibility(View.GONE);
        record_horizontal_shade.setVisibility(View.GONE);
        // 关闭提示
        vertical_hint.setVisibility(View.GONE);
        horizontal_hint.setVisibility(View.GONE);

        if (timeBinder != null) {
            timeBinder.stopTime();
        } else {

        }
        if (panelTimeService != null) {
            try {

                mContext.unbindService(timeConn);

            } catch (Exception e) {

                e.printStackTrace();
            }
            try {

                stopService(panelTimeService);
            } catch (Exception e) {

                e.printStackTrace();
            }
            ExApplication.stopTimeService = true;
        }

        vertical_timing.setText("竖屏录制");
        horizontal_timing.setText("横屏录制");
        btStart_vertical
                .setImageResource(R.drawable.btn_selector_startservice_vertical);
        btStart_horizontal
                .setImageResource(R.drawable.btn_selector_startservice_horizontal);
        // TODO
        btStart_horizontal.setVisibility(View.VISIBLE);
        recording_horizontal_ll.setVisibility(View.GONE);
        pause_bt_horizontal
                .setImageResource(R.drawable.btn_selector_starting_pause);

        btStart_vertical.setVisibility(View.VISIBLE);
        recording_vertical_ll.setVisibility(View.GONE);
        pause_bt_verticall
                .setImageResource(R.drawable.btn_selector_starting_pause);

    }

    /**
     * 暂停录屏
     */
    private void pauseRecord(ImageView v, TextView tv) {

        if (ExApplication.pauseRecVideo) {
            timeBinder.goonTime();
            ExApplication.pauseRecVideo = false;
            if (ScreenRecordActivity.SDKVesion >= 21) {
                if (ScreenRecord50.mMuxer != null) {
                    ScreenRecord50.mMuxer.restartRecording();
                }
            } else {
                Recorder44.RestartRecordVideo();
            }
            v.setImageResource(R.drawable.btn_selector_starting_pause);
        } else {
            timeBinder.pauseTime();
            ExApplication.pauseRecVideo = true;
            ToastHelper.s("录制暂停");
            if (ScreenRecordActivity.SDKVesion >= 21) {
                if (ScreenRecord50.mMuxer != null) {
                    ScreenRecord50.mMuxer.pauseRecording();
                }
            } else {
                Recorder44.PauseRecordVideo();
            }
            v.setImageResource(R.drawable.btn_selector_starting_goon);
        }

    }

    /**
     * 录屏方法
     *
     * @param orientation 0 横屏 1竖屏
     */
    private void record_main(int orientation) {

        boolean str = false;
        str = sp.getBoolean("FirstTimeUse", true);

        floatview = sp.getBoolean("no_float_view_record", false);
        // HUAWEI,Xiaomi在首次启动时，开浮窗提醒
        if (str && (launchCheckThread.getManufacturer().equals("HUAWEI") ||
                launchCheckThread.getManufacturer().equals("MIUI"))) {
            Editor editor = sp.edit();
            editor.putBoolean("FirstTimeUse", false);
            editor.commit();
            launchCheckThread.tipManufacturer();
        } else if (!RecordVideo.isRecordering) {
            boolean notify = sp.getBoolean("PackageInfoGridviewNotify", false);
            // 是否已经勾选不再弹出应用列表
            if (!notify) {
                PackageInfoGridview.isInPackageInfo = false;
                // 是否进行无悬浮窗录制
                if (floatview) {
                    if (orientation == 0) {
                        sp.edit().putBoolean("horizontalRecord", true).commit();
                        handler.sendEmptyMessage(7);
                    } else {
                        sp.edit().putBoolean("horizontalRecord", false)
                                .commit();
                        handler.sendEmptyMessage(8);
                    }

                } else {
                    // 写入横屏录屏配置
                    if (orientation == 0) {
                        sp.edit().putBoolean("horizontalRecord", true).commit();
                    } else {
                        sp.edit().putBoolean("horizontalRecord", false)
                                .commit();
                    }
                    // 打开浮窗
                    if (isFirstTimeUse) {
                        isFirstTimeUse = false;

                        // 开启悬浮窗服务
                        startFloatViewService();
                    }

                    startFloatService = true;
                    finish();
                    isInMain = false;
                    FloatContentView.isFirstTimeUse = true;
                }

            } else {// 打开第三方应用列表
                // 写入横屏录屏配置
                if (orientation == 0) {
                    sp.edit().putBoolean("horizontalRecord", true).commit();
                } else {
                    sp.edit().putBoolean("horizontalRecord", false).commit();
                }
                ToastHelper.s("正在加载应用列表，请稍候...");
                startPackageInfoGridview();
                startFloatService = true;
                isInMain = false;
                FloatContentView.isFirstTimeUse = true;
            }
        } else {
            // 4.4以下版本专用
            stopRecord();

        }
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MAIN, "首页-录制按钮-录制屏幕次数");
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MACROSCOPIC_DATA, "录制视频次数");
    }

    // 输入命令
    public static void runCommand(String command) {
        try {
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream dos = new DataOutputStream(p.getOutputStream());
            dos.writeBytes(command + "\n");
            dos.writeBytes("exit\n");
            dos.flush();
            p.waitFor();
            DataInputStream dis = new DataInputStream(p.getInputStream());
            DataInputStream des = new DataInputStream(p.getErrorStream());
            while (dis.available() > 0)
                Log.d("SC", "stdout: " + dis.readLine() + "\n");
            while (des.available() > 0)
                Log.d("SC", "stderr: " + des.readLine() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getAndroidSDKVersion() {
        int version = 1;
        try {
            version = android.os.Build.VERSION.SDK_INT;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 退出录屏大师
     */
    public void exitProgrames() {
        // 取消所有的通知
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) AppManager.getInstance()
                .getApplication()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();

        // 停止录屏服务
        stopScreenRECService();

        if (ExApplication.isCompositionVideo == false) {
            // 停止悬浮窗服务
            stopFloatViewService();
        } else {
            // 待合成后再关闭录屏大师
            ExApplication.islaterCloaseApp = true;
        }
        // 退出到桌面
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);

        if (ExApplication.isCompositionVideo == false) {
            android.os.Process.killProcess(android.os.Process.myPid());
        }

    }

    public boolean runInBack() {
        PackageManager pm = getPackageManager();
        ResolveInfo homeInfo = pm.resolveActivity(
                new Intent(Intent.ACTION_MAIN)
                        .addCategory(Intent.CATEGORY_HOME), 0);
        ActivityInfo ai = homeInfo.activityInfo;
        Intent startIntent = new Intent(Intent.ACTION_MAIN);
        startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        startIntent.setComponent(new ComponentName(ai.packageName, ai.name));
        startActivitySafely(startIntent);
        return true;
    }

    private void startActivitySafely(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_HOME) {
            if (!RecordVideo.isRecordering) {
                isInMain = false;
                finish();
            }
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ExitApp();

            return true;
        }

        return super.onKeyDown(keyCode, event);

    }

    private long exitTime = 0;

    /**
     * 双击退出函数
     */
    public void ExitApp() {

        finish();
//        if ((System.currentTimeMillis() - exitTime) > 2000) {
//            Toast.makeText(getApplicationContext(), "再按一次退出录屏大师", Toast.LENGTH_SHORT).startVideoMangerActivity();
//            exitTime = System.currentTimeMillis();
//        } else {
//            // 退出程序并开启视屏上传服务
//            serviceIntent = new Intent(getApplicationContext(), UploadVideoService.class);
//            getApplicationContext().bindService(serviceIntent, conn, Service.BIND_AUTO_CREATE);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();

        isInMain = true;

        iniViewState();
        para = frameLayout1.getLayoutParams();
        para2 = frameLayout2.getLayoutParams();
        if (ExApplication.resultCode50 == 0) {

            btStart_horizontal.setVisibility(View.VISIBLE);
            recording_horizontal_ll.setVisibility(View.GONE);
            pause_bt_horizontal
                    .setImageResource(R.drawable.btn_selector_starting_pause);

            btStart_vertical.setVisibility(View.VISIBLE);
            recording_vertical_ll.setVisibility(View.GONE);
            pause_bt_verticall
                    .setImageResource(R.drawable.btn_selector_starting_pause);
            initRecordState();
            ExApplication.resultCode50 = -2;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!isMoving && !RecordVideo.isRecordering) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    preYFirst = (int) event.getRawY();
                    para.height = frameLayout1.getHeight();
                    para2.height = frameLayout2.getHeight();
                    break;
                case MotionEvent.ACTION_MOVE:

                    preY = (int) event.getRawY();
                    // preYFirst、preY之间的差值必须小于或者大于阀值，不然，onclik的上移下移按钮会失效
                    if ((preYFirst - preY) < 0) {// 向下滑

                        isOnTouchMove = true;
                        // 如果linearlayout1d的高度不超过屏幕高度
                        if (para.height < ExApplication.height) {
                            para.height = para.height + (preY - preYFirst) * 2;
                            frameLayout1.setLayoutParams(para);
                            para2.height = para2.height - (preY - preYFirst) * 2;
                            frameLayout2.setLayoutParams(para2);

                        }
                    } else if ((preYFirst - preY) > 2) {

                        isOnTouchMove = true;
                        if (para2.height < ExApplication.height) {// 向上滑

                            para2.height = para2.height + (preYFirst - preY) * 2;
                            frameLayout2.setLayoutParams(para2);
                            para.height = para.height - (preYFirst - preY) * 2;
                            frameLayout1.setLayoutParams(para);
                        }

                    }// TODO
                    preYFirst = preY;
                    break;
                case MotionEvent.ACTION_UP:
                    para.height = frameLayout1.getHeight();
                    para2.height = frameLayout2.getHeight();
                    // 如果para的高度大于屏幕高度的70%，则向下递增高度
                    if (para.height > (ExApplication.height * 0.7)) {// 横屏录屏按钮全屏
                        // 如果para.height超过了应用高度
                        if (para.height >= ExApplication.height) {
                            // 重新设定高度
                            para.height = frameLayout1.getHeight();
                        }
                        new Thread() {
                            public void run() {

                                // 当前linearyout与屏幕的高度差，作为循环的次数
                                int mHeight = (ExApplication.height - para.height) / 2;

                                for (int i = 0; i < mHeight; i++) {
                                    try {

                                        // 循环遍历，模拟移动下滑动画
                                        para.height = para.height + 2;
                                        handler.sendEmptyMessage(3);
                                        Thread.sleep(1);
                                    } catch (InterruptedException e) {

                                        e.printStackTrace();
                                    }

                                }
                                saveFramelayoutHeight(ExApplication.height, 1);
                            }
                        }.start();
                        down_img.setImageResource(R.drawable.main_up_0);
                        halvingLine.setVisibility(View.GONE);

                    }
                    // 如果para2的高度大于屏幕高度的70%，则向上递增高度
                    else if (para2.height > (ExApplication.height * 0.7)) {// 竖屏录屏按钮全屏

                        // 如果para.height超过了应用高度
                        if (para2.height >= ExApplication.height) {
                            // 重新设定高度
                            para2.height = frameLayout2.getHeight();
                        }
                        new Thread() {
                            public void run() {
                                int mHeight = (ExApplication.height - para2.height) / 2;
                                for (int i = 0; i < mHeight; i++) {
                                    try {

                                        para2.height = para2.height + 2;
                                        handler.sendEmptyMessage(4);
                                        Thread.sleep(1);

                                    } catch (InterruptedException e) {

                                        e.printStackTrace();
                                    }

                                }
                                saveFramelayoutHeight(1, ExApplication.height);
                            }
                        }.start();
                        up_img.setImageResource(R.drawable.main_down);
                        halvingLine.setVisibility(View.GONE);
                    }
                    // 如果有触发ACTION_MOVE则进行以下操作
                    else if (isOnTouchMove) {// 横屏，录屏占据二分之一

                        new Thread() {
                            public void run() {
                                // 计算para与屏幕二分之一高度的高度差
                                int mHeight = para.height - (ExApplication.height / 2);
                                // 将高度差绝对值化
                                int abs = Math.abs(mHeight);
                                if (mHeight > 0) {
                                    for (int i = 0; i < abs; i++) {
                                        try {
                                            Thread.sleep(1);
                                            para.height = para.height - 1;
                                            handler.sendEmptyMessage(3);

                                        } catch (InterruptedException e) {

                                            e.printStackTrace();
                                        }

                                    }
                                } else {
                                    for (int i = 0; i < abs; i++) {
                                        try {

                                            Thread.sleep(1);
                                            para.height = para.height + 1;
                                            handler.sendEmptyMessage(3);

                                        } catch (InterruptedException e) {

                                            e.printStackTrace();
                                        }

                                    }

                                }
                                saveFramelayoutHeight(frameLayout1.getHeight(),
                                        frameLayout2.getHeight());
                            }
                        }.start();
                        up_img.setImageResource(R.drawable.main_up_0);
                        down_img.setImageResource(R.drawable.main_down);
                        halvingLine.setVisibility(View.VISIBLE);

                    }
                    saveFramelayoutHeight(frameLayout1.getHeight(),
                            frameLayout2.getHeight());
                    isOnTouchMove = false;
                    break;
            }

        }
        return false;
    }
}
