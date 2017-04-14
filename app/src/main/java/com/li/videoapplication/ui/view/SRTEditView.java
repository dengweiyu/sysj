package com.li.videoapplication.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.li.videoapplication.R;
import com.li.videoapplication.ui.activity.VideoEditorActivity2;
import com.li.videoapplication.utils.InputUtil;
import com.li.videoapplication.utils.StringUtil;


/**
 * 视图：底部
 */
public class SRTEditView extends FrameLayout implements View.OnClickListener {

    public final String action = this.getClass().getName();
    public final String tag = this.getClass().getSimpleName();

    private LayoutInflater inflater;
    private ImageView delete;
    private EditText edit;
    private VideoEditorActivity2 activity;

    public SRTEditView(Context context) {
        this(context, null);
    }

    public SRTEditView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initContentView();
        showView(false);
    }

    public void init(VideoEditorActivity2 activity) {
        this.activity = activity;
    }

    private void initContentView() {

        inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.view_srtedit, this);
        edit = (EditText) findViewById(R.id.srtedit_edit);
        delete = (ImageView) findViewById(R.id.srtedit_delete);

        edit.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == edit) {
            edit.requestFocus();
            try {
                InputUtil.showKeyboard(edit);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (v == delete) {
            if (activity != null
                    && activity.subtitleFragment != null) {
                activity.subtitleFragment.cancelSubtitle();
            }
        }
    }

    public String getEdit() {
        if (edit != null && edit.getText() != null)
            return edit.getText().toString();
        else
            return "";
    }

    public void setEdit(String text) {
        if (edit != null)
            edit.setText(text);
    }

    public boolean isEditEmpty() {
        if (StringUtil.isNull(getEdit()))
            return true;
        else
            return false;
    }

    public void showView(boolean flag) {
        if (flag) {
            setVisibility(VISIBLE);
            edit.setText("");
            edit.setHint(R.string.videoeditor_hint_edit);
        } else {
            setVisibility(GONE);
        }
    }
}
