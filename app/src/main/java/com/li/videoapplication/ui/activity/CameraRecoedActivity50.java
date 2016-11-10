package com.li.videoapplication.ui.activity;

import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.database.VideoCaptureManager;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.data.record.camera2.cameraView.CameraGLView;
import com.li.videoapplication.data.record.camera2.cameraView.CameraGLView.FlashMode;
import com.li.videoapplication.data.record.camera2.encoder.MediaAudioEncoder;
import com.li.videoapplication.data.record.camera2.encoder.MediaEncoder;
import com.li.videoapplication.data.record.camera2.encoder.MediaMuxerWrapper;
import com.li.videoapplication.data.record.camera2.encoder.MediaVideoEncoder;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.BitmapHelper;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 活动：视频拍摄5.0以上
 */
public class CameraRecoedActivity50 extends TBaseActivity implements OnClickListener {

    private static final String TAG = CameraRecoedActivity50.class.getSimpleName();

    public Game game;

    @Override
    public void refreshIntent() {
        try {
            game = (Game) getIntent().getSerializableExtra("game");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startVideoMangerActivity() {
        ActivityManeger.startVideoMangerActivity(this, game);
    }

    private void startVideoShareActivity() {
        if (entity != null) {
            ActivityManeger.startVideoShareActivity(this, entity, game);
            finish();
        }
    }

    private ImageView light, change, start, complete, look;
    private TextView time;
    private CameraGLView cameraView;

    private SimpleDateFormat dateFormat;
    private String path;
    private VideoCaptureEntity entity;
    private MediaMuxerWrapper mMuxer;
    private boolean isFrontCamera;
    private MediaVideoEncoder videoEncoder;

    @Override
    public int getContentView() {
        return R.layout.activity_camerarecord50;
    }

    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public void beforeOnCreate() {
        super.beforeOnCreate();
        setSystemBar(false);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        actionBar.hide();
    }

    @Override
    public void initView() {
        super.initView();

        initContentView();
        resetRecord();
    }

    private void initContentView() {

        cameraView = (CameraGLView) findViewById(R.id.cameraview);
        cameraView.setVideoSize(srceenWidth, srceenHeight);

        time = (TextView) findViewById(R.id.camerarecord_time);
        light = (ImageView) findViewById(R.id.camerarecord_light);
        change = (ImageView) findViewById(R.id.camerarecord_change);
        start = (ImageView) findViewById(R.id.camerarecord_start);
        complete = (ImageView) findViewById(R.id.camerarecord_complete);
        look = (ImageView) findViewById(R.id.camerarecord_look);

        light.setOnClickListener(this);
        change.setOnClickListener(this);
        start.setOnClickListener(this);
        complete.setOnClickListener(this);
        look.setOnClickListener(this);

        dateFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.camerarecord_light:
                toogleLight();
                break;

            case R.id.camerarecord_change:
                switchCamera(v);
                break;

            case R.id.camerarecord_start:
                startRecord(v);
                break;

            case R.id.camerarecord_complete:
                stopRecord(v);
                break;

            case R.id.camerarecord_look:
                startVideoMangerActivity();
                // fixme 启动新页面后返回会报错，先不给他返回，直接把外拍页杀死
                AppManager.getInstance().removeActivity(CameraRecoedActivity50.class);
                break;
        }
    }

    /**
     * 重置录制
     */
    private void resetRecord() {
        time.setText("00:00");
        start.setVisibility(View.VISIBLE);
        complete.setVisibility(View.GONE);
        path = null;
        entity = null;
    }

    /**
     * 开始录制
     */
    private void startRecord(final View v) {
        if (cameraView != null) {

            setViewEnabledDelayed(v);
            start.setVisibility(View.GONE);
            complete.setVisibility(View.VISIBLE);
            startTimeTask();
            try {
                mMuxer = new MediaMuxerWrapper();
                // for video capturing
                videoEncoder = new MediaVideoEncoder(mMuxer, mMediaEncoderListener,
                        cameraView.getVideoWidth(), cameraView.getVideoHeight());
                // for audio capturing
                new MediaAudioEncoder(mMuxer, mMediaEncoderListener);
                mMuxer.prepare();
                mMuxer.startRecording();
            } catch (final IOException e) {
                e.printStackTrace();
            }
            UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MAIN, "首页-录制按钮-拍摄视频次数");
        }
    }

    /**
     * 停止录制
     */
    private void stopRecord(final View v) {

        if (mMuxer != null) {
            mMuxer.stopRecording();
            mMuxer = null;
            // you should not wait here
        }
        stopTimeTask();
        setViewEnabledDelayed(v);
    }

    /**
     * 选择相机
     */
    private void switchCamera(final View v) {
        if (cameraView != null) {
            isFrontCamera = !isFrontCamera;
            cameraView.switchCamera(isFrontCamera);
            setViewEnabledDelayed(v);
        }
    }

    private void setViewEnabledDelayed(final View v) {
        if (v != null) {
            v.setEnabled(false);
            v.setClickable(false);
            UITask.postDelayed(new Runnable() {
                @Override
                public void run() {
                    v.setEnabled(true);
                    v.setClickable(true);
                }
            }, 1000);
        }
    }

    /**
     * 开关闪光灯
     */
    private void toogleLight() {
        if (cameraView != null) {
            if (cameraView.getFlashMode() == FlashMode.TORCH) {
                cameraView.setFlashMode(FlashMode.OFF);
                setImageViewImageRes(light, R.drawable.camerarecord_light_off);
            } else {
                cameraView.setFlashMode(FlashMode.TORCH);
                setImageViewImageRes(light, R.drawable.camerarecord_light_on);
            }
        }
    }

    private long seconds = 0L;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seconds++;
            if (videoEncoder.isCapturing() && seconds > 0) {
                Date date = new Date(seconds * 1000);
                time.setText(dateFormat.format(date));
                handler.postDelayed(this, 1000);
            }
        }
    };

    /**
     * 开始计时
     */
    private void startTimeTask() {
        stopTimeTask();
        if (handler != null && runnable != null) {
            handler.postDelayed(runnable, 1000);
        }
    }

    /**
     * 停止计时
     */
    private void stopTimeTask() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    /**
     * 将视频路径和视频名称填入数据库
     */
    private void saveDatabase(String path) {
        File file = new File(path);
        VideoCaptureManager.save(file.getPath(),
                VideoCaptureEntity.VIDEO_SOURCE_CAM,
                VideoCaptureEntity.VIDEO_STATION_LOCAL);
        entity = VideoCaptureManager.findByPath(path);
    }

    @Override
    public void onResume() {
        super.onResume();
        cameraView.onResume();
    }

    @Override
    public void onPause() {
        cameraView.onPause();
        super.onPause();
    }

    private final MediaEncoder.MediaEncoderListener mMediaEncoderListener = new MediaEncoder.MediaEncoderListener() {
        @Override
        public void onPrepared(final MediaEncoder encoder) {
            if (encoder instanceof MediaVideoEncoder)
                cameraView.setVideoEncoder((MediaVideoEncoder) encoder);
        }

        @Override
        public void onStopped(final MediaEncoder encoder) {

            if (encoder instanceof MediaVideoEncoder) {
                cameraView.setVideoEncoder(null);
                CameraRecoedActivity50.this.path = encoder.getOutputPath();
                // 检查视频文件是否存在
                if (!TimeHelper.checkVideoExists(path)) {
                    resetRecord();
                    return;
                }
                File file = null;
                try {
                    file = new File(path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (file != null && file.isFile()) {
                    showToastShort("录制成功");
                    saveDatabase(path);

                } else {
                    showToastShort("录制出错");
                }

                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Bitmap bitmap = BitmapHelper.saveVideoThumbnail(cameraView, path);
                            if (bitmap != null)
                                look.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //是否符合分享要求
                        if (TimeHelper.checkVideoDuration(path)) {
                            startVideoShareActivity();
                        }
                        if (entity != null)
                            resetRecord();
                    }
                }, 1000);
            }
        }
    };
}
