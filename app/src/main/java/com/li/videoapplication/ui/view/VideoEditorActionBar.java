package com.li.videoapplication.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.li.videoapplication.R;


/**
 * 视图：视频剪辑标题栏
 */
public class VideoEditorActionBar extends FrameLayout {

    public final String action = this.getClass().getName();
    public final String tag = this.getClass().getSimpleName();

    private TextView save;
    private TextView cancel;

    private LayoutInflater inflater;
    private Resources resources;

    public VideoEditorActionBar(Context context) {
        this(context, null);
    }

    public VideoEditorActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.ab_videoeditor, this);
        resources = context.getResources();

        save = (TextView) findViewById(R.id.ab_videoeditor_save);
        cancel = (TextView) findViewById(R.id.ab_videoeditor_cancel);
    }

    public void setSaveListener(OnClickListener listener) {
        if (save != null)
            save.setOnClickListener(listener);
    }

    public void setCancelListener(OnClickListener listener) {
        if (cancel != null)
            cancel.setOnClickListener(listener);
    }
}
