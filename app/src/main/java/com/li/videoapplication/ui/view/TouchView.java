package com.li.videoapplication.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.ui.activity.VideoPlayActivity;
import com.li.videoapplication.utils.ScreenUtil;

/**
 * 视图：播放触摸控制
 */
public class TouchView extends RelativeLayout implements IVideoPlay{

    public final String action = this.getClass().getName();
    public final String tag = this.getClass().getSimpleName();

    private LayoutInflater inflater;

    private View view;
    private RelativeLayout root, volume, brightness, progress;
    private ImageView volumeIcon, brightnessIcon, progressIcon;
    private TextView volumeText, brightnessText, progressText;// 80%,80%, 03:30/12:20

    public TouchView(Context context) {
        this(context, null);
    }

    public TouchView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflater = LayoutInflater.from(getContext());
        activity = (VideoPlayActivity) AppManager.getInstance().getActivity(VideoPlayActivity.class);
    }

    private VideoPlayer videoPlayer;
    private VideoPlayActivity activity;
    private VideoPlayView videoPlayView;
    private ControllerViewLand controllerViewLand;

//    public void init(VideoPlayer videoPlayer, VideoPlayView videoPlayView, ControllerView controllerView) {
//        this.videoPlayer = videoPlayer;
//        this.videoPlayView = videoPlayView;
//        this.controllerView = controllerView;
//
//        initContentView();
//        hideView();
//    }

    public void init(VideoPlayer videoPlayer, VideoPlayView videoPlayView, ControllerViewLand controllerViewLand) {
        this.videoPlayer = videoPlayer;
        this.videoPlayView = videoPlayView;
        this.controllerViewLand = controllerViewLand;

        initContentView();
        hideView();
    }

    private void initContentView() {

        view = inflater.inflate(R.layout.view_videoplay_touch, this);
        root = (RelativeLayout) view.findViewById(R.id.root);

        volume = (RelativeLayout) view.findViewById(R.id.touch_volume);
        brightness = (RelativeLayout) view.findViewById(R.id.touch_brightness);
        progress = (RelativeLayout) view.findViewById(R.id.touch_progress);

        volumeIcon = (ImageView) view.findViewById(R.id.touch_volume_icon);
        brightnessIcon = (ImageView) view.findViewById(R.id.touch_brightness_icon);
        progressIcon = (ImageView) view.findViewById(R.id.touch_progress_icon);

        volumeText = (TextView) view.findViewById(R.id.touch_volume_text);
        brightnessText = (TextView) view.findViewById(R.id.touch_brightness_text);
        progressText = (TextView) view.findViewById(R.id.touch_progress_text);

        volume.setVisibility(GONE);
        brightness.setVisibility(GONE);
        progress.setVisibility(GONE);

        // 手势监听
        gestureDetector = new GestureDetector(getContext(), simpleOnGestureListener);
        gestureDetector.setIsLongpressEnabled(true);

        root.setFocusable(true);
        root.setClickable(true);
        root.setLongClickable(true);
        root.setOnTouchListener(onTouchListener);
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent e) {
            Log.d(tag, "onTouch");
            if (MotionEvent.ACTION_DOWN == e.getAction()) {}

            if (e.getAction() == MotionEvent.ACTION_UP) {
                // 处理播放进度后设置播放位置
                if (gestureFlag == GESTURE_MODIFY_PROGRESS) {
                    long position = playingTime * 1000;
                    long duration = videoPlayer.getDuration();
                    int progress = (int) (position * 100 / duration);
                    if (videoPlayView != null)
                        videoPlayView.seekToVideo(progress);
                }
                gestureFlag = 0;// 手指离开屏幕后，重置调节音量或进度的标志
                volume.setVisibility(View.GONE);
                brightness.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
            }
            // 监听双击、滑动、长按等复杂的手势操作
            return gestureDetector.onTouchEvent(e);
        }
    };

    private GestureDetector gestureDetector;
    private static final float STEP_PROGRESS = 2f;// 设定进度滑动时的步长，避免每次滑动都改变，导致改变过快
    private static final float STEP_VOLUME = 2f;// 协调音量滑动时的步长，避免每次滑动都改变，导致改变过快
    private static final float STEP_BRIGHTNESS = 2f;// 协调音量滑动时的步长，避免每次滑动都改变，导致改变过快
    private boolean isFirstScroll = false;// 每次触摸屏幕后，第一次scroll的标志
    private int gestureFlag = 0;// 1,调节进度，2，调节音量,3.调节亮度
    private static final int GESTURE_MODIFY_PROGRESS = 1;
    private static final int GESTURE_MODIFY_VOLUME = 2;
    private static final int GESTURE_MODIFY_BRIGHT = 3;
    private long playingTime, totalTime;// 进度
    public int currentVolume, maxVolume;// 音量
    private float currentBrightness = -1f; // 亮度

    /**
     * 播放手势监听
     */
    private GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

        // 轻触屏幕
        @Override
        public boolean onDown(MotionEvent e) {
            Log.i(tag, "onDown");
            isFirstScroll = true;
            return false;
        }

        // 按下屏幕，并拖动
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (!activity.isLandscape()) {
                isFirstScroll = false;
                return false;
            }
            Log.i(tag, "onScroll");
            int width = getRight() - getLeft();
            int height = getBottom()- getTop();
            float mOldX = e1.getX(), mOldY = e1.getY();
            int y = (int) e2.getRawY();
            if (isFirstScroll) {// 以触摸屏幕后第一次滑动为标准，避免在屏幕上操作切换混乱
                // 横向的距离变化大则调整进度，纵向的变化大则调整音量
                if (Math.abs(distanceX) >= Math.abs(distanceY)) {// 进度
                    progress.setVisibility(View.VISIBLE);
                    volume.setVisibility(View.GONE);
                    brightness.setVisibility(View.GONE);
                    gestureFlag = GESTURE_MODIFY_PROGRESS;
                } else {
                    if (mOldX > width * 3.0 / 5) {// 音量
                        volume.setVisibility(View.VISIBLE);
                        brightness.setVisibility(View.GONE);
                        progress.setVisibility(View.GONE);
                        gestureFlag = GESTURE_MODIFY_VOLUME;
                    } else if (mOldX < width * 2.0 / 5) {// 亮度
                        brightness.setVisibility(View.VISIBLE);
                        volume.setVisibility(View.GONE);
                        progress.setVisibility(View.GONE);
                        gestureFlag = GESTURE_MODIFY_BRIGHT;
                    }
                }
            }

            /**
             * 进度
             */
            if (gestureFlag == GESTURE_MODIFY_PROGRESS) {
                totalTime = videoPlayer.getDuration() / 1000;
                if (Math.abs(distanceX) > Math.abs(distanceY)) {// 横向移动大于纵向移动
                    if (distanceX >= ScreenUtil.dp2px(STEP_PROGRESS)) {// 快退，用步长控制改变速度，可微调
                        progressIcon.setImageResource(R.drawable.souhu_player_backward);
                        if (playingTime > 3) {// 避免为负
                            playingTime -= 3;// scroll方法执行一次快退3秒
                        } else {
                            playingTime = 0;
                        }
                    } else if (distanceX <= - ScreenUtil.dp2px(STEP_PROGRESS)) {// 快进
                        progressIcon.setImageResource(R.drawable.souhu_player_forward);
                        if (playingTime < totalTime - 16) {// 避免超过总时长
                            playingTime += 3;// scroll执行一次快进3秒
                        } else {
                            playingTime = totalTime - 10;
                        }
                    }
                    if (playingTime < 0) {
                        playingTime = 0;
                    }
                    long position = playingTime * 1000;
                    long duration = videoPlayer.getDuration();
                    int progress = (int) (position * 100 / duration);
                    controllerViewLand.setTime(progress);
                    controllerViewLand.setProgress(progress);

                    String s = TimeHelper.getVideoPlayTime(playingTime) + "/" +
                            TimeHelper.getVideoPlayTime(totalTime);
                    // 设置播放位置
                    progressText.setText(s);
                }
            }

            /**
             * 声音
             */
            if (gestureFlag == GESTURE_MODIFY_VOLUME) {
                if (Math.abs(distanceY) > Math.abs(distanceX)) {// 纵向移动大于横向移动
                    currentVolume = activity.getCurrentVolume();
                    maxVolume = activity.getMaxVolume();
                    if (distanceY >= ScreenUtil.dp2px(STEP_VOLUME)) {// 音量调大,注意横屏时的坐标体系,尽管左上角是原点，但横向向上滑动时distanceY为正
                        if (currentVolume < maxVolume) {// 为避免调节过快，distanceY应大于一个设定值
                            currentVolume++;
                        }
                        volumeIcon.setImageResource(R.drawable.souhu_player_volume);
                    } else if (distanceY <= -ScreenUtil.dp2px(STEP_VOLUME)) {// 音量调小
                        if (currentVolume > 0) {
                            currentVolume--;
                            if (currentVolume == 0) {// 静音，设定静音独有的图片
                                volumeIcon.setImageResource(R.drawable.souhu_player_silence);
                            }
                        }
                    }
                    int percentage = (currentVolume * 100) / maxVolume;
                    volumeText.setText(percentage + "%");
                    activity.setCurrentVolume(currentVolume);
                }
            }

            /**
             * 亮度
             */
            if (gestureFlag == GESTURE_MODIFY_BRIGHT) {
                if (Math.abs(distanceY) > Math.abs(distanceX)) {// 纵向移动大于横向移动
                    currentBrightness = activity.getCurrentBrightness();
               /* if (distanceY >= ScreenUtil.dp2px(STEP_BRIGHTNESS)) {// 调大
                    if (currentBrightness <= 1.0f) {
                        currentBrightness = currentBrightness + 0.1f;
                    }
                } else if (distanceY <= -ScreenUtil.dp2px(STEP_BRIGHTNESS)) {// 调小
                    if (currentBrightness > 0.0f) {
                        currentBrightness = currentBrightness - 0.1f;
                    }
                }*/
                    if (currentBrightness >= 0.0f && currentBrightness <= 1.0f)
                        currentBrightness = currentBrightness + (mOldY - y) / height;
                    if (currentBrightness < 0.0f)
                        currentBrightness = 0.0f;
                    else if (currentBrightness > 1.0f)
                        currentBrightness = 1.0f;
                    brightnessText.setText((int) (currentBrightness * 100) + "%");
                    activity. setCurrentBrightness(currentBrightness);
                }
            }
            isFirstScroll = false;
            return false;
        }

        // 轻触屏幕，并松开
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.i(tag, "onSingleTapUp");
            return false;
        }

        // 按下屏幕、快速移动后松开
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.i(tag, "onFling");
            return false;
        }

        // 长按屏幕
        @Override
        public void onLongPress(MotionEvent e) {
            Log.i(tag, "onLongPress");
        }

        // 轻触屏幕，尚未松开或拖动
        @Override
        public void onShowPress(MotionEvent e) {
            Log.i(tag, "onShowPress");
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.i(tag, "onDoubleTap");
            if (videoPlayView != null) {
                videoPlayView.toogleVideo();
            }
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            Log.i(tag, "onDoubleTapEvent");
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.i(tag, "onSingleTapConfirmed");
            if (videoPlayView != null) {
                videoPlayView.toogleView();
            }
            return true;
        }
    };

    @Override
    public void showView() {
        Log.i(tag, "showView");
        setVisibility(VISIBLE);
    }

    @Override
    public void hideView() {
        Log.i(tag, "hideView");
        setVisibility(GONE);
    }

    @Override
    public void minView() {}

    @Override
    public void maxView() {}
}
