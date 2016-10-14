package com.fmsysj.zbqmcs.floatview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
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
import com.fmsysj.zbqmcs.activity.ScreenRecordActivity;
import com.fmsysj.zbqmcs.animation.FloatContentAnimation;
import com.fmsysj.zbqmcs.frontcamera.CameraView;
import com.fmsysj.zbqmcs.frontcamera.FrontCameraService;
import com.fmsysj.zbqmcs.utils.ExApplication;
import com.fmsysj.zbqmcs.utils.RecordVideo;
import com.fmsysj.zbqmcs.utils.RootUtils;
import com.li.videoapplication.data.local.LPDSStorageUtil;
import com.li.videoapplication.R;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.data.local.StorageUtil;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.toast.ToastHelper;

import java.io.File;

/**
 * 视图：浮窗展开1
 */
public class FloatContentView extends RelativeLayout implements OnClickListener {

    /**
     * 浮窗第一次启动标示 此变量为true时，启动浮窗时，浮窗状态会回到floatview1。
     */
    public static boolean isFirstTimeUse = true;
    public static Context mContext;
    public static FloatContentAnimation floatContentAnimation;

    // 图片路径
     private static String picFileUrl;
     public int mWidth;
     public int mHeight;
     /**
     * 悬浮窗layout
     */
    public RelativeLayout relativeLayout;
    public ViewGroup.LayoutParams params;

    View view;
    RecordVideo recrodVideo;
    private LayoutInflater inflater;
    private ImageView btStart;
    private ImageView btBackHome;
    private ImageView btLaySC;
    private ImageView btFloatIco;
    private TextView floatTV;
    private SoundPool soundPool;
    private FrameLayout frameLayout;

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

    /**
     * 发送广播
     * @param what
     */
    public static void sendEmptyMessage(int what) {/*
        List<FloatContentView> views = (List<FloatContentView>) AppManager.getInstance().getView(FloatContentView.class);
        if (views != null && views.size() > 0)
            for (FloatContentView view: views) {
                view.h.sendEmptyMessage(what);
            }
*/
        if (FloatContentView.h != null)
            FloatContentView.h.sendEmptyMessage(what);
    }

    /**
     * 处理消息
     */
    public static Handler h;

    public FloatContentView(Context context) {
        this(context, null);
    }

    public FloatContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = LayoutInflater.from(context);

        mContext = context;

        view = inflater.inflate(R.layout.fm_float_content_view, this);
        // 查找页面控件
        findViews(context);
        floatContentAnimation = new FloatContentAnimation(mContext, relativeLayout, FloatViewManager.getInstance());
        inflater.inflate(R.layout.fm_float_content_view, this);
        params = findViewById(R.id.content_layout).getLayoutParams();
        mWidth = params.width;
        mHeight = params.height;

        // 相机声音准备
        soundPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 5);
        soundPool.load(mContext, R.raw.kuaimen, 1);

        handler.sendEmptyMessageDelayed(4, 1);
        ViewGroup.LayoutParams paramsContent = findViewById(R.id.content).getLayoutParams();

        // 重置之前的UI状态
        if (RecordVideo.isStart) {
            btStart.setImageDrawable(getResources().getDrawable(R.drawable.bt_stop_down));
        } else {
            btStart.setImageDrawable(getResources().getDrawable(R.drawable.bt_start));
        }
        // 设置主播按钮状态
        setCameraState();
        recrodVideo = new RecordVideo(AppManager.getInstance().getContext());
        // 控件监听事件
        setOnclickEvent();
        h = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                if (msg.what == 1) { // 展开浮窗详细框
                    floatContentAnimation.extandAnmin();
                } else if (msg.what == 2) { // 根据安卓系统版本不同分别进行视频合成
                    if (ScreenRecordActivity.SDKVesion < 19) {// 4.4以下
                        recrodVideo.StopRecordForVesionOther();
                    } else if (ScreenRecordActivity.SDKVesion >= 19 && ScreenRecordActivity.SDKVesion < 21) {// 4.4
                        recrodVideo.StopRecordForVesion19();
                    }
                    btStart.setImageDrawable(getResources().getDrawable(R.drawable.bt_start));
                    FloatViewManager.getInstance().BackToView1();
                } else if (msg.what == 6) {
                    FloatViewManager.getInstance().back();
                    // File file = new File(picFileUrl);
                    // root判断
                    if (RootUtils.appRoot1()) {
                        ToastHelper.s("截屏成功!");
                    } else {
                        ToastHelper.s("截屏失败，请确保手机ROOT成功后重试!");
                    }
                }
            }
        };;

        // 第一次启动不展开悬浮窗，避免错位问题
        if (!ExApplication.firstExtandAnmin) {
            // floatContentAnimation.extandAnmin();
        } else {
            ExApplication.firstExtandAnmin = false;
        }
        // floatContentAnimation.setFloatContentViewLocation(context);
    }

    // 刷新图库
    public static void scanPhotos(String filePath, Context context) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    private void setCameraState() {
        if (!ExApplication.floatCameraClose) {
            btFloatIco.setImageDrawable(getResources().getDrawable(R.drawable.closecamera));
            floatTV.setText("关闭主播");
        } else {
            btFloatIco.setImageDrawable(getResources().getDrawable(R.drawable.opencamera));
            floatTV.setText("打开主播");
        }

    }

    /** 查找页面控件 */
    private void findViews(Context context) {
        btStart = (ImageView) findViewById(R.id.bt_starts);
        btBackHome = (ImageView) findViewById(R.id.bt_backHome);
        frameLayout = (FrameLayout) findViewById(R.id.content_layout);
        relativeLayout = (RelativeLayout) findViewById(R.id.content);

        btLaySC = (ImageView) findViewById(R.id.float_layout_botton_SC);

        btFloatIco = (ImageView) findViewById(R.id.bt_float_ico);
        floatTV = (TextView) findViewById(R.id.bt_float_tv);

    }

    /** 控件监听事件 */
    private void setOnclickEvent() {
        btBackHome.setOnClickListener(this);
        frameLayout.setOnClickListener(this);
        btStart.setOnClickListener(this);
        btLaySC.setOnClickListener(this);
        btFloatIco.setOnClickListener(this);

    }

    public void onClick(View v) {
        if (v == btStart) {
            recrodVideo.stardRecordVideo();
            btStart.setImageDrawable(getResources().getDrawable(R.drawable.bt_start));
            FloatViewManager.getInstance().BackToView2();
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
                // 本浮窗隐藏
                setVisibility(GONE);
                // 打开5.0截屏activity
                ScreenCapture50.showNewTask();
            }

        } else if (v == frameLayout) {
            floatContentAnimation.extandAnmin();
            // windowManager.back();
            // floatContentAnimation.extandAnmin();
        } else if (v == btBackHome) { // 返回主页
            ActivityManeger.startMainActivityNewTask();
            FloatViewManager.getInstance().back();
        } else if (v == btFloatIco) {
            if (!ExApplication.floatCameraClose) {
                CameraView.closeFloatView();
                ExApplication.floatCameraClose = true;
            } else {
                // 开启画中画
                startFrontCameraService();
                ExApplication.floatCameraClose = false;
            }
            setCameraState();
        }
    }

    private void startFrontCameraService() {
        Context context = AppManager.getInstance().getContext();
        Intent intent = new Intent(context, FrontCameraService.class);
        context.startService(intent);
    }

    /**
     * 新开线程进行截屏
     *
     * @author WYX
     *
     */
    public static class MyThread extends Thread {

        private static MyThread myThread;

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
//                h.sendEmptyMessage(6);
                FloatContentView.sendEmptyMessage(6);
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