package com.li.videoapplication.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.li.videoapplication.R;

/**
 * 视图：出错了
 */
public class ErrorView extends RelativeLayout implements IVideoPlay,View.OnClickListener{

    public final String action = this.getClass().getName();
    public final String tag = this.getClass().getSimpleName();

    private LayoutInflater inflater;

    private View view;
    private RelativeLayout root;

    private  VideoPlayView mPlayView;

    public ErrorView(Context context) {
        this(context, null);
    }

    public ErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initContentView();
        hideView();
    }

    private void initContentView() {

        inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.view_videoplay_error, this);
        root = (RelativeLayout) view.findViewById(R.id.root);

        view.findViewById(R.id.iv_reload).setOnClickListener(this);

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
    public void minView() {}

    @Override
    public void maxView() {}

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_reload:
                if (mPlayView != null){
                    mPlayView.switchPlay(VideoPlayView.STATE_VIDEOPLAY);
                }
                break;
        }
    }

    public void setPlayView(VideoPlayView playView) {
        mPlayView = playView;
    }
}
