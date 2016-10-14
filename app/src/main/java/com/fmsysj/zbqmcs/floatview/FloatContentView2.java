package com.fmsysj.zbqmcs.floatview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fmsysj.zbqmcs.activity.ScreenCapture50;
import com.fmsysj.zbqmcs.activity.ScreenRecord50;
import com.fmsysj.zbqmcs.activity.ScreenRecordActivity;
import com.fmsysj.zbqmcs.animation.FloatContentViewAnimation;
import com.fmsysj.zbqmcs.frontcamera.CameraView;
import com.fmsysj.zbqmcs.frontcamera.FrontCameraService;
import com.fmsysj.zbqmcs.utils.ExApplication;
import com.fmsysj.zbqmcs.utils.RecordVideo;
import com.fmsysj.zbqmcs.utils.RootUtils;
import com.li.videoapplication.R;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.toast.ToastHelper;

import java.io.File;

/**
 * 视图：浮窗展开2
 */
public class FloatContentView2 extends RelativeLayout implements OnClickListener {

    /**
     * 处理消息
     */
    public static Handler h;

    /**
     * 发送广播
     * @param what
     */
    public static void sendEmptyMessage(int what) {
        if (FloatContentView2.h != null)
            FloatContentView2.h.sendEmptyMessage(what);
    }

    /**
     * 浮窗第一次启动标示 此变量为true时，启动浮窗时，浮窗状态会回到floatview1。
     */
    public boolean isFirstTimeUse = true;
    public Context mContext;
    private LayoutInflater inflater;
    // 图片路径
    private static String picFileUrl;
    public int mWidth;
    public int mHeight;
    public int contentViewWidth;
    public int contentViewHeight;
    public RelativeLayout relativeLayout;
    public ViewGroup.LayoutParams params;
    SoundPool soundPool;
    int lastX;
    int lastY;
    View view;
    // 视频模式（高清，标准）
    String videoMode;
    FloatContentViewAnimation floatContentViewAnimation;
    /** 停止录屏 */
    private ImageView btStop;
    /** 返回首页 */
    private ImageView btBackHome;
    /** 截屏 */
    private ImageView btLaySC;
    /** 暂停录屏 */
    private ImageView btPause;
    /** 主播按钮 */
    private ImageView btCamera;
    /** 暂停录屏文本框 */
    private TextView btPauseTV;
    /** 主播按钮文本框 */
    private TextView cameraTV;
    private int preX;
    private int x;
    private int y;
    private FrameLayout frameLayout;
    private SharedPreferences sp;
    @SuppressLint("HandlerLeak")

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 4) {
                if (isFirstTimeUse) {

                    setVisibility(View.GONE);
                    isFirstTimeUse = false;
                    FloatViewManager.getInstance().back();

                }
            } else if (msg.what == 5) {
                FloatViewManager.getInstance().back();
            }
        }
    };

    public FloatContentView2(Context context) {
        this(context, null);
    }

    public FloatContentView2(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        inflater = LayoutInflater.from(context);

        view = inflater.inflate(R.layout.fm_float_content_view2, this);
        // 查找页面控件
        findViews(context);

        View view = inflater.inflate(R.layout.fm_float_content_view2, this);
        floatContentViewAnimation = new FloatContentViewAnimation(AppManager.getInstance().getContext(), relativeLayout, view, FloatViewManager.getInstance());

        params = findViewById(R.id.content_layout).getLayoutParams();
        mWidth = params.width;
        mHeight = params.height;

        // 相机声音准备
        soundPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 5);
        soundPool.load(AppManager.getInstance().getContext(), R.raw.kuaimen, 1);

        ViewGroup.LayoutParams paramsContent = findViewById(R.id.content).getLayoutParams();
        contentViewWidth = paramsContent.width;
        contentViewHeight = paramsContent.height;

        // 重置暂停按钮状态
        if (ExApplication.pauseRecVideo) {

            btPause.setImageDrawable(getResources().getDrawable(R.drawable.bt_goon));
            btPauseTV.setText("继续");
        } else {
            btPause.setImageDrawable(getResources().getDrawable(R.drawable.bt_pause));
            btPauseTV.setText("暂停");
        }

        setCameraState();
        sp = PreferenceManager.getDefaultSharedPreferences(AppManager.getInstance().getApplication());

        h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    // 展开浮窗详细框
                    floatContentViewAnimation.extandAnmin();
                } else if (msg.what == 2) {

                } else if (msg.what == 6) {
                    FloatViewManager.getInstance().back2();
                    // root判断
                    if (RootUtils.appRoot1()) {
                        ToastHelper.s("截屏成功!");
                        // 更新手机图库
                        scanPhotos(picFileUrl, getContext());
                    } else {
                        ToastHelper.s("截屏失败，请确保手机ROOT成功后重试!");
                    }
                }
            }
        };

        // 控件监听事件
        setOnclickEvent();

    }

    // 刷新图库
    public static void scanPhotos(String filePath, Context context) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    /** 查找页面控件 */
    private void findViews(Context context) {
        btStop = (ImageView) findViewById(R.id.bt_stop);
        btPause = (ImageView) findViewById(R.id.bt_pause);
        btBackHome = (ImageView) findViewById(R.id.bt_backHome);
        frameLayout = (FrameLayout) findViewById(R.id.content_layout);
        relativeLayout = (RelativeLayout) findViewById(R.id.content);

        btLaySC = (ImageView) findViewById(R.id.float_layout_botton_SC);

        btPauseTV = (TextView) findViewById(R.id.bt_pause_text);
        btCamera = (ImageView) findViewById(R.id.bt_camera);
        cameraTV = (TextView) findViewById(R.id.bt_camera_tv);

    }

    /** 控件监听事件 */
    private void setOnclickEvent() {
        btBackHome.setOnClickListener(this);
        frameLayout.setOnClickListener(this);
        btStop.setOnClickListener(this);
        btLaySC.setOnClickListener(this);
        btPause.setOnClickListener(this);
        btCamera.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v == btStop) {
            // 重新设置浮窗透明度
            FloatView2.alpha = 1;
            FloatView2 view = (FloatView2) AppManager.getInstance().getView(FloatView2.class);
            if (view != null) {
                view.tvInfo.setAlpha(FloatView2.alpha);
                view.anim.stop();
            }
            ExApplication.pauseRecVideo = false;

            if (ScreenRecordActivity.SDKVesion >= 21) {
                // 停止录屏，返回浮窗
                ScreenRecord50.stopRecord();
                ScreenRecord50.startRecoing = false;
            } else {
                //FloatViewService.h.sendEmptyMessage(4);
                FloatViewService.sendEmptyMessage(4);
                FloatViewManager.getInstance().back2();
            }
            // 关闭触摸显示
            android.provider.Settings.System.putInt(AppManager.getInstance().getApplication().getContentResolver(), "show_touches", 0);
            // 关闭画中画
            if (!ExApplication.floatCameraClose) {
                CameraView.closeFloatView();
            }
        } else if (v == btLaySC) {// 截屏layout处理事件
            if (ScreenRecordActivity.SDKVesion < 21) {
                // 隐藏浮窗
                setVisibility(GONE);
                // 播放截屏音效
                soundPool.play(1, 1, 1, 0, 0, 1);
                // 开启线程进行截屏
                try {
                    MyThread myTHread = MyThread.getMyThread();
                    myTHread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (RecordVideo.isStart == false) {
                    // 本浮窗隐藏
                    setVisibility(GONE);
                    // 打开5.0截屏activity
                    ScreenCapture50.showNewTask();
                } else {
                    ToastHelper.s("正在录屏中，请录屏后再截屏");
                }
            }
        } else if (v == frameLayout) {
            floatContentViewAnimation.extandAnmin();
        } else if (v == btBackHome) { // 返回主页
            ActivityManeger.startMainActivityNewTask();
            FloatViewManager.getInstance().back2();
        } else if (v == btPause) {
            if (ExApplication.pauseRecVideo) {
                ExApplication.pauseRecVideo = false;
                if (ScreenRecordActivity.SDKVesion >= 21) {
                    if (ScreenRecord50.mMuxer != null) {
                        ScreenRecord50.mMuxer.restartRecording();
                    }
                }
            } else {
                ExApplication.pauseRecVideo = true;
                ToastHelper.s("录制暂停");
                if (ScreenRecordActivity.SDKVesion >= 21) {
                    if (ScreenRecord50.mMuxer != null) {
                        ScreenRecord50.mMuxer.pauseRecording();
                    }
                }
            }
            FloatViewManager.getInstance().back2();
        } else if (v == btCamera) {
            if (!ExApplication.floatCameraClose) {
                CameraView.closeFloatView();
                ExApplication.floatCameraClose = true;
            } else {
                // 开启画中画
                Intent intent = new Intent(AppManager.getInstance().getApplication(), FrontCameraService.class);
                AppManager.getInstance().getApplication().startService(intent);
                ExApplication.floatCameraClose = false;
            }
            setCameraState();
        }
    }

    /**
     * 设置主播按钮状态
     */
    private void setCameraState() {
        if (!ExApplication.floatCameraClose) {
            btCamera.setImageDrawable(getResources().getDrawable(R.drawable.closecamera));
            cameraTV.setText("关闭主播");
        } else {
            btCamera.setImageDrawable(getResources().getDrawable(R.drawable.opencamera));
            cameraTV.setText("打开主播");
        }
    }

    /**
     * 新开线程进行截屏
     *
     * @author WYX
     *
     */
    public static class MyThread extends Thread {
        private static MyThread myThread = null;

        private MyThread() {}

        public static MyThread getMyThread() {
            if (myThread != null) {
                myThread.interrupt();

            }
            myThread = new MyThread();
            return myThread;
        }

        public void run() {
            try {
                // 发出消息通知界面更新，显示浮窗
                FloatContentView2.sendEmptyMessage(6);
                // 进行截屏
                picFileUrl = SYSJStorageUtil.createPicturePath().getPath();
                String command = "screencap -p " + picFileUrl;
                ScreenRecordActivity.runCommand(command);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}