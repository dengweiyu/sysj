package com.li.videoapplication.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.activity.VideoEditorActivity2;

import java.util.ArrayList;
import java.util.List;

/**
 * 视图：视频剪辑菜单
 */
public class VideoEditorMenu extends FrameLayout {

    public final String action = this.getClass().getName();
    public final String tag = this.getClass().getSimpleName();

    private LayoutInflater inflater;
    private Resources resources;

    private List<LinearLayout> layouts = new ArrayList<>();
    private List<ImageView> imageViews = new ArrayList<>();
    private List<TextView> textViews = new ArrayList<>();

    private VideoEditorActivity2 activity;

    public VideoEditorMenu(Context context) {
        this(context, null);
    }

    public VideoEditorMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.menu_videoeditor, this);
        resources = context.getResources();

        initContentView();
        toogleMenu(false);
    }

    public void init(VideoEditorActivity2 activity) {
        this.activity = activity;
    }

    private void initContentView() {

        layouts.add((LinearLayout) findViewById(R.id.videoeditor_first));
        layouts.add((LinearLayout) findViewById(R.id.videoeditor_second));
        layouts.add((LinearLayout) findViewById(R.id.videoeditor_third));
        layouts.add((LinearLayout) findViewById(R.id.videoeditor_four));

        imageViews.add((ImageView) findViewById(R.id.videoeditor_first_icon));
        imageViews.add((ImageView) findViewById(R.id.videoeditor_second_icon));
        imageViews.add((ImageView) findViewById(R.id.videoeditor_third_icon));
        imageViews.add((ImageView) findViewById(R.id.videoeditor_four_icon));

        textViews.add((TextView) findViewById(R.id.videoeditor_first_text));
        textViews.add((TextView) findViewById(R.id.videoeditor_second_text));
        textViews.add((TextView) findViewById(R.id.videoeditor_third_text));
        textViews.add((TextView) findViewById(R.id.videoeditor_four_text));

        for (int i = 0; i < layouts.size(); i++) {
            layouts.get(i).setOnClickListener(new Clickable(i));
        }
    }

    public class Clickable implements OnClickListener {

        private int index;

        public Clickable(int i) {
            this.index = i;
        }

        @Override
        public void onClick(View v) {
            if (activity != null && activity.isAudioRecording == true) {
                ToastHelper.s(R.string.videoeditor_tip_audiorecording);
                return;
            }
            if (activity != null && activity.isAddingSubtitle == true) {
                return;
            }
            switchTab(index);
            if (callback != null)
                callback.onClick(index);
        }
    }

    public void performClick(int index) {
        for (int i = 0; i < textViews.size(); i++) {
            if (index == i) {
                layouts.get(index).performClick();
            }
        }
    }

    public void switchTab(int index) {
        for (int i = 0; i < textViews.size(); i++) {
            if (index == i) {
                textViews.get(i).setTextColor(resources.getColorStateList(R.color.menu_videoeditor_blue));
                layouts.get(i).setBackgroundResource(R.color.menu_videoeditor_gray);
            } else {
                textViews.get(i).setTextColor(resources.getColorStateList(R.color.menu_videoeditor_white));
                layouts.get(i).setBackgroundResource(R.color.menu_videoeditor_black);
            }
        }
        switch (index) {
            case 0:// 剪辑
                imageViews.get(0).setImageResource(R.drawable.videoeditor_editor_blue);
                imageViews.get(1).setImageResource(R.drawable.videoeditor_cover_white);
                imageViews.get(2).setImageResource(R.drawable.videoeditor_subtitle_white);
                imageViews.get(3).setImageResource(R.drawable.videoeditor_audio_white);
                break;
            case 1: // 字幕
                imageViews.get(0).setImageResource(R.drawable.videoeditor_editor_white);
                imageViews.get(1).setImageResource(R.drawable.videoeditor_cover_blue);
                imageViews.get(2).setImageResource(R.drawable.videoeditor_subtitle_white);
                imageViews.get(3).setImageResource(R.drawable.videoeditor_audio_white);
                break;
            case 2: // 配音
                imageViews.get(0).setImageResource(R.drawable.videoeditor_editor_white);
                imageViews.get(1).setImageResource(R.drawable.videoeditor_cover_white);
                imageViews.get(2).setImageResource(R.drawable.videoeditor_subtitle_blue);
                imageViews.get(3).setImageResource(R.drawable.videoeditor_audio_white);
                break;
            case 3:// 封面
                imageViews.get(0).setImageResource(R.drawable.videoeditor_editor_white);
                imageViews.get(1).setImageResource(R.drawable.videoeditor_cover_white);
                imageViews.get(2).setImageResource(R.drawable.videoeditor_subtitle_white);
                imageViews.get(3).setImageResource(R.drawable.videoeditor_audio_blue);
                break;
        }
    }

    public void toogleMenu(boolean isAdvanced) {
        if (isAdvanced) {
            for (int i = 0; i < layouts.size(); ++i) {
                layouts.get(i).setVisibility(VISIBLE);
            }
        } else {
            layouts.get(2).setVisibility(GONE);
            layouts.get(3).setVisibility(GONE);
        }
    }

    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        void onClick(int index);
    }
}
