package com.li.videoapplication.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.image.ImageLoaderHelper;
import com.li.videoapplication.utils.URLUtil;

/**
 * 视图：无wifi下播放视频提示
 */
public class GPRSTipView extends RelativeLayout implements IVideoPlay {

    private TextView gprs_play;
    private LayoutInflater inflater;
    private View view;

    public GPRSTipView(Context context) {
        this(context, null);
    }

    public GPRSTipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initContentView();
        hideView();
    }

    private void initContentView() {
        inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.view_videoplay_gprstip, this);
        gprs_play = (TextView) view.findViewById(R.id.gprs_play);
    }

    public void setPlayListener(View.OnClickListener listener) {
        gprs_play.setOnClickListener(listener);
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

    }

    @Override
    public void maxView() {

    }
}
