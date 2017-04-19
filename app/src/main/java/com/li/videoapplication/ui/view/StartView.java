package com.li.videoapplication.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.utils.URLUtil;

import static android.R.attr.bitmap;

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
            GlideHelper.displayImageWhite(getContext(),url, cover);
    }

    private Bitmap bitmap;

    public void loadCover(Bitmap b) {
        if (b != null) {
            Log.d(tag, "loadCover: ");
            recycleBitmap(bitmap);
            bitmap = b;
            cover.setImageBitmap(bitmap);
            Log.d(tag, "loadCover: true");
        }
    }

    private void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled())
            try {
                bitmap.recycle();
                Log.d(tag, "recycleBitmap: true");
            } catch (Exception e) {
                e.printStackTrace();
            }
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
    public void showCover() {
        cover.setVisibility(VISIBLE);
    }

    @Override
    public void hideCover() {
        cover.setVisibility(GONE);
    }

    @Override
    public void showPlay() {
        play.setVisibility(VISIBLE);
    }

    @Override
    public void minView() {}

    @Override
    public void maxView() {}

    @Override
    public void hidePlay() {

    }
}
