package com.li.videoapplication.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.utils.MResource;



/**
 * 视图：视频剪辑标题栏
 */
public class VideoEditorActionBar extends FrameLayout {

    public final String action = this.getClass().getName();
    public final String tag = this.getClass().getSimpleName();


    TextView cancel;

    TextView save;

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

        cancel = (TextView) findViewById(R.id.ab_videoeditor_cancel);
        save = (TextView) findViewById(R.id.ab_video_editor_save);
    }

    public void setSaveListener(View.OnClickListener listener) {
        if (save != null){
            save.setOnClickListener(listener);
        }

    }

    public void setCancelListener(View.OnClickListener listener) {
        if (cancel != null)
            cancel.setOnClickListener(listener);

    }
}
