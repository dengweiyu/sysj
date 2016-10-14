package com.li.videoapplication.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.li.videoapplication.R;
import com.li.videoapplication.data.image.ImageLoaderHelper;
import com.li.videoapplication.utils.URLUtil;

/**
 * 视图：开始播放：封面，中心播放按键
 */
public class StartView extends RelativeLayout implements IVideoPlay{

    public final String action = this.getClass().getName();
    public final String tag = this.getClass().getSimpleName();

    private LayoutInflater inflater;

    private View view;
    private RelativeLayout root;
    private ImageView cover, play;

    public StartView(Context context) {
        this(context, null);
    }

    public StartView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initContentView();
        hideView();
    }

    private void initContentView() {

        inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.view_videoplay_start, this);
        root = (RelativeLayout) view.findViewById(R.id.root);
        cover = (ImageView) view.findViewById(R.id.start_cover);
        play = (ImageView) view.findViewById(R.id.start_play);
    }

    public void setPlayListener(View.OnClickListener listener) {
        play.setOnClickListener(listener);
    }

    public void displayCoverImage(String url) {
        if (url != null && URLUtil.isURL(url))
            ImageLoaderHelper.displayImageWhite(url, cover);
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
}
