package com.li.videoapplication.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.ui.activity.VideoPlayActivity;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;

/**
 * 视图：乐播投屏时的显示view
 */
public class LeBoView extends RelativeLayout implements IVideoPlay ,View.OnClickListener{

    private View bottomBg;
    private View stopLink;
    public ImageView play, tvImage;
    private TextView title,playText;
    private VideoPlayActivity activity;
    private ViewGroup.LayoutParams params;


    public LeBoView(Context context) {
        super(context);
    }

    public LeBoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initContentView();
        hideView();
    }

    private void initContentView() {
        activity = (VideoPlayActivity) AppManager.getInstance().getActivity(VideoPlayActivity.class);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.view_videoplay_leboview, this);
        stopLink = view.findViewById(R.id.lebo_stoplink);
        play =(ImageView) view.findViewById(R.id.lebo_play);
        tvImage =(ImageView) view.findViewById(R.id.lebo_tv_image);
        title =(TextView) view.findViewById(R.id.lebo_title);
        playText =(TextView) view.findViewById(R.id.lebo_playtext);
        bottomBg = findViewById(R.id.lebo_bottombg);

        view.findViewById(R.id.lebo_goback).setOnClickListener(this);
        params = tvImage.getLayoutParams();
    }

    public void setTitleText(String text) {
        if (!StringUtil.isNull(text))
            title.setText(text);
    }

    /**
     * 更新播放按键
     */
    public void setPlay(boolean isPlaying) {
        if (play != null)
            if (isPlaying)
                play.setImageResource(R.drawable.videoplay_play_208);
            else
                play.setImageResource(R.drawable.videoplay_pause_208);
    }

    public void setStopLinkListener(View.OnClickListener listener) {
        stopLink.setOnClickListener(listener);
    }

    public void setPlayButtonListener(View.OnClickListener listener){
        play.setOnClickListener(listener);
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
        bottomBg.setVisibility(GONE);
        params.height = ScreenUtil.dp2px((float) 68.5);
        params.width = ScreenUtil.dp2px((float) 125.5);
        tvImage.setLayoutParams(params);
        playText.setTextSize(12);
    }

    @Override
    public void maxView() {
        bottomBg.setVisibility(VISIBLE);
        params.height = ScreenUtil.dp2px((float) 123.3);
        params.width = ScreenUtil.dp2px((float) 225.9);
        tvImage.setLayoutParams(params);
        playText.setTextSize(18);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lebo_goback:
                activity.finish();
        }
    }

    @Override
    public void showCover() {

    }

    @Override
    public void hideCover() {

    }

    @Override
    public void showPlay() {

    }

    @Override
    public void hidePlay() {

    }
}
