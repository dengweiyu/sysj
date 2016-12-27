package com.li.videoapplication.ui.activity;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.util.Log;
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
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.data.record.CameraListener;
import com.li.videoapplication.data.record.CameraView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 活动：视频拍摄5.0以下
 */
public class CameraRecoedActivity extends TBaseActivity implements OnClickListener {

    private static final String TAG = CameraRecoedActivity.class.getSimpleName();

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
        /*if (path != null) {
			ActivityManeger.startVideoMangerActivity(this, game);
			finish();
		}*/
    }

    private void startVideoShareActivity() {
        if (entity != null) {
            ActivityManeger.startVideoShareActivity210(this, entity, game);
            finish();
        }
    }

    private ImageView light, change, start, complete, look;
    private TextView time;
    private CameraView cameraView;

    private SimpleDateFormat dateFormat;
    private String path;
    private VideoCaptureEntity entity;

    @Override
    public int getContentView() {
        return R.layout.activity_camerarecord;
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

        cameraView = (CameraView) findViewById(R.id.cameraview);
        cameraView.setFlashMode(CameraView.FlashMode.OFF);
        cameraView.setQuality(CameraView.RecorderQuality.HIGHT);
        cameraView.setZoom(0);
        cameraView.setCameraListener(listener);

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
                // 检查视频长度及安全性
                if (!TimeHelper.checkVideoInfo(path)) {
                    resetRecord();
                    return;
                }
                startVideoShareActivity();
                if (entity == null)
                    resetRecord();
                break;

            case R.id.camerarecord_look:
                startVideoMangerActivity();
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
        if (cameraView != null && !cameraView.isRecording()) {
            cameraView.autoFocus();
            boolean flag = cameraView.startRecord();
            if (!flag) {
                cameraView.stopRecord();
                showToastShort("录制失败");
                return;
            }
            setViewEnabledDelayed(v);
            start.setVisibility(View.GONE);
            complete.setVisibility(View.VISIBLE);
            UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MAIN, "首页-录制按钮-拍摄视频次数");
        }
    }

    /**
     * 停止录制
     */
    private void stopRecord(final View v) {
        if (cameraView != null && cameraView.isRecording()) {
            cameraView.stopRecord();
            setViewEnabledDelayed(v);
        }
    }

    /**
     * 选择相机
     */
    private void switchCamera(final View v) {
        if (cameraView != null) {
            if (cameraView.isRecording()) {
                showToastShort("正在录像中");
            } else {
                cameraView.switchCamera();
                cameraView.autoFocus();
                setViewEnabledDelayed(v);
            }
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
            }, 800);
        }
    }

    /**
     * 开关闪光灯
     */
    private void toogleLight() {
        if (cameraView != null) {
            if (cameraView.getFlashMode() == CameraView.FlashMode.ON) {
                cameraView.setFlashMode(CameraView.FlashMode.OFF);
                setImageViewImageRes(light, R.drawable.camerarecord_light_off);
            } else {
                cameraView.setFlashMode(CameraView.FlashMode.ON);
                setImageViewImageRes(light, R.drawable.camerarecord_light_on);
            }
        }
    }

    private CameraListener listener = new CameraListener() {

        @Override
        public void onMinPictureTaken(Bitmap bitmap) {

        }

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

        }

        @Override
        public void onPictureTaken(Bitmap bitmap) {

        }

        @Override
        public void onRecorderUpdate(boolean isRecording, long seconds) {
            if (isRecording) {
                if (seconds > 0) {
                    Date date = new Date(seconds * 1000);
                    time.setText(dateFormat.format(date));
                }
            }
        }

        @Override
        public void onRecorderFinish(Bitmap bitmap, String path) {
            Log.i(TAG, "onRecorderFinish: path == "+path);
            File file = null;
            try {
                file = new File(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (file != null && file.isFile()) {
                Log.i(TAG, "onRecorderFinish2: path == "+path);
                CameraRecoedActivity.this.path = path;
                showToastShort("录制成功");
                saveDatabase(path);
            } else {
                showToastShort("录制出错");
            }
            if (bitmap != null)
                look.setImageBitmap(bitmap);
        }
    };

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
}
