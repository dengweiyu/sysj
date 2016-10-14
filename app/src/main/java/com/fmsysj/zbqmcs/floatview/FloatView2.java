package com.fmsysj.zbqmcs.floatview;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fmsysj.zbqmcs.activity.ScreenRecord50;
import com.fmsysj.zbqmcs.activity.ScreenRecordActivity;
import com.fmsysj.zbqmcs.frontcamera.CameraView;
import com.fmsysj.zbqmcs.utils.ExApplication;
import com.fmsysj.zbqmcs.utils.RecordVideo;
import com.li.videoapplication.data.local.StorageUtil;
import com.li.videoapplication.data.preferences.SharedPreferencesUtils;
import com.li.videoapplication.R;
import com.li.videoapplication.ui.toast.ToastHelper;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 视图：浮窗2
 */
public class FloatView2 extends LinearLayout {

    public static int preXFirst;
    public static int preYFirst;
    public static int min = 0, sec = 0;
    public static boolean stopCnt = false;
    public static float alpha = 1;
    public static boolean doubleclick = false;
    private Timer timer = new Timer();
    private TimerTask task;
    public static float allSize;
    public static float residueSize;

    public static float rate;
    private static boolean percent10 = false;
    private static boolean percent5 = false;

    public AnimationDrawable anim;
    public LinearLayout tvInfo;
    public TextView timeText;
    private RelativeLayout floatViewRL2;
    private ImageView recordTV;

    public int mWidth;
    public int mHeight;
    private int preX;
    private int preY;
    private int x;
    private int y;
    private boolean isMove;

    private Context mContext;
    private LayoutInflater inflater;

    private SharedPreferences sharedPreferences;

    public Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (mContext != null) {
                if (msg.what == 3) {
                    msg.what = 0;
                    HideFloatView();
                } else if (msg.what == 4) {
                    msg.what = 0;
                    showFloatView();
                }
            }
        }
    };

    public Handler h = new Handler() {
        @SuppressWarnings("deprecation")
        public void handleMessage(android.os.Message msg) {

            if (RecordVideo.isStart) {
                ScreenRecordActivity.videolong++;
                if (msg.what == 1) {
                    if (ExApplication.recStartTime == 0) {
                        ExApplication.recStartTime = System.currentTimeMillis();
                        String videoMode = ExApplication.videoQuality;
                        if (videoMode.equals("-1")) {
                            if (ScreenRecordActivity.SDKVesion >= 19) {
                                videoMode = sharedPreferences.getString("quality_of_video", ExApplication.HQuality);
                            } else {
                                videoMode = sharedPreferences.getString("quality_of_video", ExApplication.SQuality);
                            }
                        }
                    }

                    msg.what = 0;
                    if (FloatViewService.times >= 2) {
                        sec = (FloatViewService.times - 2) % 60;
                        min = (FloatViewService.times - 2) / 60;
                        if (min < 10) {
                            if (sec < 10) {
                                timeText.setText("  " + min + " :" + "0" + (sec));
                                if (sec > 3) {
                                    if (alpha >= 0.5) {
                                        alpha = (float) (alpha - 0.1);
                                        tvInfo.setAlpha(alpha);
                                    }
                                }

                            } else {
                                timeText.setText("  " + min + " :" + (sec));
                            }
                        } else {
                            timeText.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.float_recording_bg));
                            if (sec < 10) {
                                timeText.setText(" " + min + " : " + "0" + (sec));
                            } else {
                                timeText.setText(" " + min + " : " + (sec));
                            }
                        }
                        if (sec % 10 == 0) {
                            allSize = StorageUtil.getLpdsTotalSize_long();
                            residueSize = StorageUtil.getLpdsAvailableSize_long();
                            rate = residueSize / allSize;

                            if (rate < 0.10 && rate > 0.05 && !percent10) {
                                ToastHelper.s("您的手机内存剩余空间低于10%，建议停止录屏");
                                percent10 = true;
                            }
                            if (rate < 0.05 && !percent5) {
                                ToastHelper.s("您的手机内存剩余空间低于5%，建议停止录屏");
                                percent5 = true;
                            }
                        }
                    }
                }
            } else {
                sec = 0;
                min = 0;
                stopCnt = false;
                timeText.setText("  0 :00");
                percent10 = false;
                percent5 = false;
                try {
                    android.provider.Settings.System.putInt(mContext.getContentResolver(), "show_touches", 0);
                } catch (Exception e) {
                    e.fillInStackTrace();
                }
            }
        }
    };

    public FloatView2(Context context) {
        super(context);
        mContext = context;
        inflater = LayoutInflater.from(context);

        inflater.inflate(R.layout.fm_float_view_2, this);
        tvInfo = (LinearLayout) findViewById(R.id.layout_view_2);

        floatViewRL2 = (RelativeLayout) findViewById(R.id.float_view_RL2);
        timeText = (TextView) findViewById(R.id.time_txt);
        recordTV = (ImageView) findViewById(R.id.record_anmin_tv);
        timeText.setText("  0 :00");

        mWidth = tvInfo.getLayoutParams().width;
        mHeight = tvInfo.getLayoutParams().height;

        anim = (AnimationDrawable) recordTV.getBackground();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        task = new TimerTask() {
            @Override
            public void run() {
                h.sendEmptyMessage(1);
            }
        };
        timer.schedule(task, 1000, 1000);
        boolean showtouch = SharedPreferencesUtils.getPreferenceboolean(context, "show_float_touch");
        if (showtouch == true) {
            android.provider.Settings.System.putInt(context.getContentResolver(), "show_touches", 1);
        } else {
            android.provider.Settings.System.putInt(context.getContentResolver(), "show_touches", 0);
        }
        if (ExApplication.pauseRecVideo) {
            recordTV.setImageDrawable(getResources().getDrawable(R.drawable.fm_pause_menu));
        } else {
            recordTV.setImageDrawable(getResources().getDrawable(R.drawable.recordanmin));
        }
        tvInfo.setAlpha(alpha);
    }

    public boolean HideFloatView() {
        floatViewRL2.setVisibility(RelativeLayout.INVISIBLE);
        return true;
    }

    public boolean showFloatView() {
        floatViewRL2.setVisibility(RelativeLayout.VISIBLE);
        return true;
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                preX = (int) event.getRawX();
                preY = (int) event.getRawY();
                preXFirst = preX;
                preYFirst = preY;
                isMove = false;
                break;

            case MotionEvent.ACTION_UP:
                int[] location = new int[2];
                tvInfo.getLocationOnScreen(location);
                ExApplication.tvWidth = tvInfo.getWidth();
                ExApplication.moveX = location[0];
                ExApplication.moveY = location[1];
                if (!isMove && doubleclick == false) {
                    if (ScreenRecordActivity.SDKVesion >= 19) {
                        FloatViewManager.getInstance().showContentForView2(FloatView2.this);
                        //FloatViewService.h.sendEmptyMessage(3);
                        FloatViewService.sendEmptyMessage(3);
                        ExApplication.floatViewTime = timeText.getText().toString();
                    } else {
                        doubleclick = true;
                        alpha = 1;
                        tvInfo.setAlpha(alpha);
                        anim.stop();
                        if (ScreenRecordActivity.SDKVesion >= 21) {
                            ScreenRecord50.stopRecord();
                            ScreenRecord50.startRecoing = false;
                        } else {
                            //FloatViewService.h.sendEmptyMessage(4);
                            FloatViewService.sendEmptyMessage(4);
                        }
                        android.provider.Settings.System.putInt(mContext.getContentResolver(), "show_touches", 0);
                        if (ExApplication.floatCameraClose == false) {
                            CameraView.closeFloatView();
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:
                x = (int) event.getRawX();
                y = (int) event.getRawY();
                if (((x - preXFirst) > -15 && (x - preXFirst) < 15) && ((y - preYFirst) > -15 && (y - preYFirst) < 15)) {
                    isMove = false;
                } else {
                    FloatViewManager.getInstance().move(this, x - preX, y - preY);
                    isMove = true;
                }
                preX = x;
                preY = y;
                break;
        }
        return super.onTouchEvent(event);
    }
}