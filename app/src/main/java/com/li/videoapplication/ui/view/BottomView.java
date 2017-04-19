package com.li.videoapplication.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.activity.VideoEditorActivity2;


/**
 * 视图：底部
 */
public class BottomView extends FrameLayout implements View.OnClickListener {

    public final String action = this.getClass().getName();
    public final String tag = this.getClass().getSimpleName();

    private LayoutInflater inflater;
    private View button;
    private TextView text;

    public EditModelable listener;
    private VideoEditorActivity2 activity;

    public BottomView(Context context) {
        this(context, null);
    }

    public BottomView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initContentView();
    }

    public void init(VideoEditorActivity2 activity, EditModelable listener) {
        this.activity = activity;
        this.listener = listener;
    }

    private void initContentView() {

        inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.view_bottom, this);
        button = findViewById(R.id.bottom_button);
        text = (TextView) findViewById(R.id.bottom_text);

        button.setOnClickListener(this);
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
        toogleText();
    }

    public void performClickButton() {
        if (button != null) {
            button.performClick();
        }
    }

    public void toogleText() {

        if (text.getText().equals(getContext().getResources().getString(R.string.videoeditor_advanced))) {
            changeText(false);
            if (listener != null)
                listener.editModel(false);
        } else {
            changeText(true);
            if (listener != null)
                listener.editModel(true);
        }
    }

    private void changeText(boolean isAdvanced) {
        if (isAdvanced) {// 高级模式
            text.setText(R.string.videoeditor_advanced);
        } else {// 普通模式
            text.setText(R.string.videoeditor_normal);
        }
    }

    public interface EditModelable {

        void editModel(boolean isAdvanced);
    }
}
