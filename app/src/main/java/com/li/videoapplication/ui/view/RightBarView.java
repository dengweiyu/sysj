package com.li.videoapplication.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.li.videoapplication.R;
import com.li.videoapplication.views.CircleImageView;

/**
 * 视图：播放右侧：举报，发弹慕
 */
public class RightBarView extends RelativeLayout implements IVideoPlay {

    public final String action = this.getClass().getName();
    public final String tag = this.getClass().getSimpleName();

    private CircleImageView send,tv,report;

    public RightBarView(Context context) {
        this(context, null);
    }

    public RightBarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initContentView();
        hideView();
    }

    private void initContentView() {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.view_videoplay_rightbar, this);

        send = (CircleImageView) view.findViewById(R.id.rightbar_send);
        report = (CircleImageView) view.findViewById(R.id.rightbar_report);
        tv = (CircleImageView) view.findViewById(R.id.rightbar_tv);
    }

    public void setReportListener(OnClickListener listener) {
        report.setOnClickListener(listener);
    }

    public void setSendListener(OnClickListener listener) {
        send.setOnClickListener(listener);
    }

    public void setTVListener(OnClickListener listener) {
        tv.setOnClickListener(listener);
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
        send.setVisibility(VISIBLE);
    }

    @Override
    public void maxView() {
        send.setVisibility(VISIBLE);
    }
}
