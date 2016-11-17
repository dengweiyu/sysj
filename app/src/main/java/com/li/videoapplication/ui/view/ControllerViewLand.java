package com.li.videoapplication.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.ui.activity.VideoPlayActivity;
import com.li.videoapplication.ui.toast.ToastHelper;
import com.li.videoapplication.views.TouchSeekBar;

/**
 * 视图：播放控制
 */
public class ControllerViewLand extends RelativeLayout implements View.OnClickListener, IVideoPlay {

    public final String action = this.getClass().getName();
    public final String tag = this.getClass().getSimpleName();

    private LayoutInflater inflater;

    private View view;
    private LinearLayout root;
    private ImageView play, zoom;
    private ImageView danmuku, good, bad, star, comment;
    private TextView position, duration;
    private TouchSeekBar progress;// 0 - 100
    private VideoImage videoImage;

    public ControllerViewLand(Context context) {
        this(context, null);
    }

    public ControllerViewLand(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflater = LayoutInflater.from(getContext());
        activity = (VideoPlayActivity) AppManager.getInstance().getActivity(VideoPlayActivity.class);
    }

    private VideoPlayActivity activity;
    private VideoPlayView videoPlayView;
    private VideoPlayer videoPlayer;
    private DanmukuPlayer danmukuPlayer;


    public void init(VideoPlayView videoPlayView, VideoPlayer videoPlayer, DanmukuPlayer danmukuPlayer) {
        this.videoPlayer = videoPlayer;
        this.videoPlayView = videoPlayView;
        this.danmukuPlayer = danmukuPlayer;

        initContentView();
        minView();
        hideView();
    }

    public void setVideoImage(VideoImage videoImage) {
        this.videoImage = videoImage;
        //刷新点赞点踩收藏图标状态
        refreshIconView(videoImage);
    }

    private void initContentView() {

        view = inflater.inflate(R.layout.view_videoplay_controller_land, this);
        root = (LinearLayout) view.findViewById(R.id.root);

        play = (ImageView) view.findViewById(R.id.controller_play);
        zoom = (ImageView) view.findViewById(R.id.controller_zoom);
        danmuku = (ImageView) view.findViewById(R.id.controller_damuku);
        good = (ImageView) view.findViewById(R.id.controller_good);
        bad = (ImageView) view.findViewById(R.id.controller_bad);
        star = (ImageView) view.findViewById(R.id.controller_star);
        comment = (ImageView) view.findViewById(R.id.controller_comment);

        position = (TextView) view.findViewById(R.id.controller_position);
        duration = (TextView) view.findViewById(R.id.controller_duration);

        progress = (TouchSeekBar) view.findViewById(R.id.controller_progress);
        progress.setDrag(false);

        play.setOnClickListener(this);
        zoom.setOnClickListener(this);
        good.setOnClickListener(this);
        bad.setOnClickListener(this);
        star.setOnClickListener(this);
        comment.setOnClickListener(this);

        setTime();
        setPlay(false);
        setDanmuku(false);

        progress.setMax(100);
        progress.setProgress(0);
        progress.setSecondaryProgress(0);
        progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            private int progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                this.progress = progress;
                Log.i(tag, "changed/progress=" + progress);
                setTime(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setTime();
                if (videoPlayView != null)
                    videoPlayView.seekToVideo(this.progress);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.controller_play:
                videoPlayView.toogleVideo();
                break;
            case R.id.controller_zoom:
                if (activity != null && videoPlayer != null) {
                    activity.toogleScreen();
                    if (activity.isLandscape()) {
                        setZoom(true);
                    } else {
                        setZoom(false);
                    }
                }
                break;
            case R.id.controller_good:
                setGood();
                activity.comment.refreshGood();
                activity.comment.refreshBad();
                break;
            case R.id.controller_bad:
                setBad();
                activity.comment.refreshBad();
                activity.comment.refreshGood();
                break;
            case R.id.controller_star:
                if (PreferencesHepler.getInstance().isLogin()) {
                    setStar();
                    activity.comment.refreshStar();
                }else {
                    ToastHelper.s("请先登录");
                }
                break;
            case R.id.controller_comment:
                if (danmukuPlayer != null && activity != null && activity.addDanmukuView != null)
                    activity.addDanmukuView.showView();
                break;
        }
    }

    private void setStar() {
        if (videoImage != null) {
            if (videoImage.getCollection_tick() == 0) {
                star.setImageResource(R.drawable.controller_star_red_208);
                videoImage.setCollection_tick(1);
                videoImage.setCollection_count(Integer.valueOf(videoImage.getCollection_count()) + 1 + "");
            } else {
                star.setImageResource(R.drawable.controller_star_white_208);
                videoImage.setCollection_tick(0);
                videoImage.setCollection_count(Integer.valueOf(videoImage.getCollection_count()) - 1 + "");
            }
            // 视频收藏
            DataManager.videoCollect2(activity.item.getId(), PreferencesHepler.getInstance().getMember_id());
        }
    }

    private void setBad() {
        if (videoImage != null) {
            if (videoImage.getFndown_tick() == 0) {
                bad.setImageResource(R.drawable.controller_bad_red_208);
                videoImage.setFndown_tick(1);
                videoImage.setFndown_count(Integer.valueOf(videoImage.getFndown_count()) + 1 + "");

                if (videoImage.getFlower_tick() == 1) {
                    //因为点踩，所以将赞去掉
                    good.setImageResource(R.drawable.controller_good_white_208);
                    videoImage.setFlower_tick(0);
                    videoImage.setFlower_count(Integer.valueOf(videoImage.getFlower_count()) - 1 + "");
                    // 视频点赞
                    DataManager.videoFlower2(videoImage.getId(), PreferencesHepler.getInstance().getMember_id());
                }

            } else {
                bad.setImageResource(R.drawable.controller_bad_white_208);
                videoImage.setFndown_tick(0);
                videoImage.setFndown_count(Integer.valueOf(videoImage.getFndown_count()) - 1 + "");
            }
            // 视频踩
            DataManager.fndownClick203(activity.item.getId(), PreferencesHepler.getInstance().getMember_id());
        }
    }

    private void setGood() {
        if (videoImage != null) {
            if (videoImage.getFlower_tick() == 0) {//未点赞
                good.setImageResource(R.drawable.videoplay_good_red_205);
                videoImage.setFlower_tick(1);
                videoImage.setFlower_count(Integer.valueOf(videoImage.getFlower_count()) + 1 + "");

                if (videoImage.getFndown_tick() == 1) {//如果已点踩
                    //将踩去掉
                    bad.setImageResource(R.drawable.controller_bad_white_208);
                    videoImage.setFndown_tick(0);
                    videoImage.setFndown_count(Integer.valueOf(videoImage.getFndown_count()) - 1 + "");
                    // 踩
                    DataManager.fndownClick203(videoImage.getId(), PreferencesHepler.getInstance().getMember_id());
                }
                // 提交点赞任务
                DataManager.TASK.doTask_21(PreferencesHepler.getInstance().getMember_id());
            } else {
                good.setImageResource(R.drawable.controller_good_white_208);
                videoImage.setFlower_tick(0);
                videoImage.setFlower_count(Integer.valueOf(videoImage.getFlower_count()) - 1 + "");
            }

            // 视频点赞
            DataManager.videoFlower2(activity.item.getId(), PreferencesHepler.getInstance().getMember_id());
        }
    }

    public void refreshIconView(VideoImage videoImage){
        if (videoImage != null) {
            if (videoImage.getFlower_tick() == 0) {//未点赞
                good.setImageResource(R.drawable.controller_good_white_208);
            } else {
                good.setImageResource(R.drawable.videoplay_good_red_205);
            }

            if (videoImage.getFndown_tick() == 0) {
                bad.setImageResource(R.drawable.controller_bad_white_208);
            } else {
                bad.setImageResource(R.drawable.controller_bad_red_208);
            }

            if (videoImage.getCollection_tick() == 0) {
                star.setImageResource(R.drawable.controller_star_white_208);
            } else {
                star.setImageResource(R.drawable.controller_star_red_208);
            }
        }
    }

    public void setDanmukListener(OnClickListener listener) {
        if (danmuku != null)
            danmuku.setOnClickListener(listener);
    }

    public void performDanmukuClick() {
        activity.post(new Runnable() {
            @Override
            public void run() {
                if (danmuku != null && !videoPlayView.isDanmukuShow)
                    danmuku.performClick();
            }
        });
    }

    public void setDanmuku(boolean isShow) {
        if (danmuku != null) {
            if (isShow) {
                danmuku.setImageResource(R.drawable.videoplay_large_danmuku_yellow);
            } else {
                danmuku.setImageResource(R.drawable.videoplay_large_danmuku_gray);
            }
        }
    }

    public void setDanmukuEnabledDelayed() {
        if (danmuku != null && activity != null) {
            danmuku.setEnabled(false);
            danmuku.setClickable(false);
            activity.postDelayed(new Runnable() {
                @Override
                public void run() {

                    danmuku.setEnabled(true);
                    danmuku.setClickable(true);
                }
            }, 800);
        }
    }

    /**
     * 更新播放时间（根据播放器）
     */
    public void setTime() {
        if (videoPlayer != null && this.position != null && this.duration != null) {
            long duration = videoPlayer.getDuration();
            long position = videoPlayer.getCurrentPosition();
            String playTime = TimeHelper.getVideoPlayTime(position / 1000);
            String totleTime = TimeHelper.getVideoPlayTime(duration / 1000);
            this.position.setText(playTime);
            this.duration.setText(totleTime);
            Log.i(tag, "playTime=" + playTime);
            Log.i(tag, "totleTime=" + totleTime);
        }
    }

    /**
     * 更新播放时间
     */
    public void setTime(int progress) {
        if (progress < 0)
            progress = 0;
        if (progress > 100)
            progress = 100;
        if (videoPlayer != null && this.position != null && this.duration != null) {
            long duration = videoPlayer.getDuration();
            long position = progress * duration / 100;
            String playTime = TimeHelper.getVideoPlayTime(position / 1000);
            String totleTime = TimeHelper.getVideoPlayTime(duration / 1000);
            this.position.setText(playTime);
            this.duration.setText(totleTime);
            Log.i(tag, "playTime=" + playTime);
            Log.i(tag, "totleTime=" + totleTime);
        }
    }

    /**
     * 更新播放进度（根据播放器）
     */
    public void setProgress() {
        if (videoPlayer != null) {
            long position = videoPlayer.getCurrentPosition();
            long duration = videoPlayer.getDuration();
            // 缓冲进度
            int buffer = videoPlayer.getBufferPercentage();
            int progress = 0;
            if (duration != 0 && position !=0)
                progress = (int) (position * 100 / duration);

            Log.i(tag, "position=" + position);
            Log.i(tag, "duration=" + duration);
            Log.i(tag, "buffer=" + buffer);
            Log.i(tag, "progress=" + progress);
            if (this.progress != null) {
                this.progress.setMax(100);
                this.progress.setProgress(progress);
                this.progress.setSecondaryProgress(buffer);
            }
        }
    }

    /**
     * 更新进度条
     */
    public void setProgress(int progress) {
        if (progress < 0)
            progress = 0;
        if (progress > 100)
            progress = 100;
        if (this.progress != null)
            this.progress.setProgress(progress);
    }

    /**
     * 拖动条是否能拖动
     */
    public void setDrag(boolean drag) {
        if (this.progress != null) {
            this.progress.setDrag(drag);
            this.progress.setEnabled(drag);
            this.progress.setClickable(drag);
        }
    }

    /**
     * 更新播放按键
     */
    public void setPlay(boolean isPlaying) {
        if (play != null)
            if (isPlaying)
                play.setImageResource(R.drawable.videoplay_pause_208);
            else
                play.setImageResource(R.drawable.videoplay_play_208);
    }

    /**
     * 更新全屏按键
     */
    public void setZoom(boolean isFull) {
        if (zoom != null)
            if (isFull)
                zoom.setImageResource(R.drawable.videoplay_zoom_small);
            else
                zoom.setImageResource(R.drawable.videoplay_zoom_small);
//                zoom.setImageResource(R.drawable.videoplay_zoom_large);
    }

    @Override
    public void showView() {
        setVisibility(VISIBLE);
    }

    @Override
    public void hideView() {
        setVisibility(GONE);
    }

    @Override
    public void minView() {
        danmuku.setVisibility(View.VISIBLE);
    }

    @Override
    public void maxView() {
        danmuku.setVisibility(View.GONE);
    }
}
