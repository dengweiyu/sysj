package com.li.videoapplication.ui.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fmsysj.screeclibinvoke.utils.RootUtil;
import com.ifeimo.screenrecordlib.RecordingManager;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.BaseDialog;
import com.li.videoapplication.framework.BaseTopDialog;
import com.li.videoapplication.tools.AnimationHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.activity.MainActivity;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.activity.VideoShareActivity;
import com.li.videoapplication.ui.srt.SRTTimeFormat;
import com.li.videoapplication.utils.AppUtil;
import com.li.videoapplication.utils.BitmapUtil;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.ThreadUtil;

import java.io.ByteArrayOutputStream;

/**
 * 弹框：首页录制
 */
public class RecordDialog extends BaseDialog implements View.OnClickListener {
    private IOnClickListener onClickListener;
    private IDialogVisableListener visableListener;
    private MainActivity activity;
    private LinearLayout layout;
    private View closeLayout;

    private View shotView;
    private ImageView blurView;
    private boolean isLogin;

    private Bitmap blurBitmap;

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
        UmengAnalyticsHelper.onEvent(activity, UmengAnalyticsHelper.MAIN, "发布-拍摄-点击右上角发布按钮后点击拍摄视频按钮");
    }

    /**
     * 跳转：外拍（5.0以下）
     */
    private void startCameraRecoedActivity() {
        ActivityManeger.startCameraRecoedActivity(activity);
        UmengAnalyticsHelper.onEvent(activity, UmengAnalyticsHelper.MAIN, "发布-拍摄-点击右上角发布按钮后点击拍摄视频按钮");
    }

    /**
     * 跳转：图文分享
     */
    private void startHomeImageShareActivity() {
        ActivityManeger.startHomeImageShareActivity(activity, null, false);
        UmengAnalyticsHelper.onEvent(activity, UmengAnalyticsHelper.MAIN, "发布-图文-点击右上角发布按钮后点击图文按钮");
    }

    /**
     * 跳转：选择上传视频
     */
    private void startVideoChooseActivity() {
        ActivityManeger.startVideoChooseActivity(getContext(), null, VideoShareActivity.TO_VIDEOMANAGER);
        UmengAnalyticsHelper.onEvent(activity, UmengAnalyticsHelper.MAIN, "发布-视频-点击右上角发布按钮后点击视频按钮");
    }

    public RecordDialog(Context context,View shotView) {
        super(context);
        this.shotView = shotView;
        activity = AppManager.getInstance().getMainActivity();
        /*if (blurView != null){
            startBlurBackground();
        }*/
    }

    public RecordDialog(Context context, IDialogVisableListener visableListener) {
        super(context,R.style.MyDialog);
        this.visableListener = visableListener;
        activity = AppManager.getInstance().getMainActivity();
    }

    public RecordDialog(Context context, IDialogVisableListener visableListener, IOnClickListener onClickListener) {
        super(context,R.style.MyDialog);
        this.visableListener = visableListener;
        this.onClickListener = onClickListener;
        activity = AppManager.getInstance().getMainActivity();
    }

    @Override
    protected int getContentView() {

        return R.layout.popup_record209;
    }

    @Override
    protected void afterContentView(Context context) {
        super.afterContentView(context);

        //点击阴影无法取消
      //  setCancelable(false);

        Window window = getWindow();
       // window.setWindowAnimations(R.style.slideBottomAnim); // 设置窗口弹出动画
    //    window.setBackgroundDrawableResource(android.R.color.transparent); // 设置对话框背景为透明
        WindowManager.LayoutParams params = window.getAttributes();

        //无状态栏
      //  window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 根据x，y坐标设置窗口需要显示的位置
        params.x = 0; // x小于0左移，大于0右移
        params.y = 0; // y小于0上移，大于0下移
        params.alpha = 0.95f; //设置透明度
        params.gravity = Gravity.BOTTOM; // 设置重力

       // params.height = ScreenUtil.getScreenHeight();       //设置高度为全屏
        window.setAttributes(params);

        isLogin = PreferencesHepler.getInstance().isLogin();

        blurView = (ImageView)findViewById(R.id.iv_blur_back);


        View record =  findViewById(R.id.record_video);
        record.setOnClickListener(this);

        layout = (LinearLayout) findViewById(R.id.ll_popup_record_layout);

        startAnimation();

        closeLayout =  findViewById(R.id.rl_bottom_close);

        final View recordRecord = findViewById(R.id.record_record);
        recordRecord.setOnClickListener(this);


        final  View local = findViewById(R.id.record_local);
        local.setOnClickListener(this);


        final  View image = findViewById(R.id.record_image);
        image.setOnClickListener(this);

        final View close = findViewById(R.id.record_close);
        close.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record_video://录屏
                activity.startScreenRecordActivity();
                cancel();
                break;

            case R.id.record_record://外拍
                cancel();
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
                cancel();
                if (isLogin) {
                    if (onClickListener != null) {
                        onClickListener.onUploadImageClick();
                    } else {
                        if (activity != null)
                            startHomeImageShareActivity();
                    }
                } else {
                    startLoginDialog();
                }
                break;

            case R.id.record_local://视频
                cancel();
                if (isLogin) {
                    if (onClickListener !=null){
                        onClickListener.onUploadVideoClick();
                    }else {
                        if (activity != null)
                            startVideoChooseActivity();
                    }
                } else {
                    startLoginDialog();
                }
                break;
            case R.id.record_close:
                closeAnimation();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //执行结束动画
        closeAnimation();
    }

    @Override
    public void cancel() {
        super.cancel();
        if (visableListener != null)
            visableListener.dialogCanceled();

        if (blurBitmap != null){
            blurBitmap.recycle();
            blurBitmap = null;
        }
    }

    @Override
    public void show() {
        super.show();
        if (visableListener != null)
            visableListener.dialogShowed();
    }

    /**
     * 启动动画
     */
    private void startAnimation(){
        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.slide_bottom_in);
        LayoutAnimationController controller = new LayoutAnimationController(animation,0.05f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);

        layout.setLayoutAnimation(controller);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                closeLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //结束动画
    private void closeAnimation(){
        closeLayout.setVisibility(View.GONE);
        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.slide_bottom_out);
        LayoutAnimationController controller = layout.getLayoutAnimation();
        controller.setAnimation(animation);
        controller.setOrder(LayoutAnimationController.ORDER_REVERSE);
        layout.invalidate();                //源码中说明 必须要在layout再次绘制的时候才会触发动画 因此直接调用重绘制
        layout.scheduleLayoutAnimation();

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                    cancel();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 背景模糊
     */
    private void startBlurBackground(){
        blurBitmap = ScreenUtil.getViewShot(shotView);
        com.ifeimo.im.common.util.ThreadUtil.getInstances().createThreadStartToFixedThreadPool(new Runnable() {
            @Override
            public void run() {
                if (blurBitmap != null){
                    blurBitmap = BitmapUtil.scaleBitmap(blurBitmap,0.2f);   //缩小5倍
                    mHandler.sendEmptyMessage(1);
                }
            }
        });
    }

    /**
     * 更新UI
     */
    final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                if (blurBitmap != null){
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    blurBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    GlideHelper.displayImageBlur(getContext(),baos.toByteArray(),blurView,35);
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startAnimation();   //启动动画
                        }
                    },300);
                }
            }
        }
    };

    public interface IDialogVisableListener {
        void dialogShowed();

        void dialogCanceled();
    }

    public interface IOnClickListener {
        void onUploadImageClick();

        void onUploadVideoClick();
    }
}
