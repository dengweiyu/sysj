package com.li.videoapplication.ui.view;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.ui.activity.VideoEditorActivity2;
import com.li.videoapplication.ui.activity.VideoPlayActivity;


/**
 * 视图：字幕
 */
public class TimedTextView extends FrameLayout implements IVideoPlay, MediaPlayer.OnTimedTextListener{

    public final String action = this.getClass().getName();
    public final String tag = this.getClass().getSimpleName();

    private LayoutInflater inflater;
    private View view;
    public TextView content;
    private Activity activity;

    public void init(Activity activity) {
        this.activity = activity;
    }

    public TimedTextView(Context context) {
        this(context, null);
    }

    public TimedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initContentView();
        minView();
    }

    private void initContentView() {

        inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.view_timedtext, this);
        content = (TextView) view.findViewById(R.id.timedtext_content);
    }

    @Override
    public void onTimedText(MediaPlayer mp, final TimedText text) {
        try {
            Log.d(tag, "onTimedText/text=" + text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (activity != null) {
            if (text != null && content != null) {
                setContent(text.getText());
            } else {
                setContent("");
            }
        }
    }

    private void setContent(String text) {
        if (text == null)
            return;
        final String string = text.trim();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                content.setText(string);
            }
        });
    }

    public TextView getContent(){
        return content;
    }

    @Override
    public void showView() {
        setVisibility(VISIBLE);
    }

    @Override
    public void hideView() {
        setVisibility(GONE);
        if (content != null)
            content.setText("");
    }

    @Override
    public void minView() {
        if (content != null) {
            content.setTextSize(12);
        }
    }

    @Override
    public void maxView() {
        if (content != null) {
            content.setTextSize(16);
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
