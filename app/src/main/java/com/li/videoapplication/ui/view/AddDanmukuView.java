package com.li.videoapplication.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.ui.activity.VideoPlayActivity;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.utils.StringUtil;

/**
 * 视图：发射弹幕
 */
public class AddDanmukuView extends FrameLayout implements
        View.OnFocusChangeListener,
        TextView.OnEditorActionListener,
        View.OnTouchListener,
        IVideoPlay {

    public final String action = this.getClass().getName();
    public final String tag = this.getClass().getSimpleName();

    private LayoutInflater inflater;
    private VideoPlayActivity activity;
    private InputMethodManager manager;
    private Context context;

    private View view;
    private ScrollView touch;
    private EditText text;

    private String getText() {
        if (text.getText() == null)
            return "";
        return text.getText().toString().trim();
    }


    public AddDanmukuView(Context context) {
        this(context, null);
    }

    public AddDanmukuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void init(VideoPlayActivity activity) {
        this.activity = activity;

        if (activity != null)
            manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inflater = LayoutInflater.from(context);

        initContentView();
        hideView();
    }

    private void initContentView() {

        view = inflater.inflate(R.layout.view_adddanmuku, this);
        text = (EditText) findViewById(R.id.adddanmuku_text);
        touch = (ScrollView)findViewById(R.id.adddanmuku_touch);

        text.setOnFocusChangeListener(this);
        text.setOnEditorActionListener(this);
        touch.setOnTouchListener(this);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (actionId == EditorInfo.IME_ACTION_SEND ||
                (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            if (StringUtil.isNull(getText())) {
                ToastHelper.s("弹幕不能为空");
                return true;
            }
            hideInput();
            boolean flag;
            flag = listener != null && listener.addDanmuku(getText());

            if (flag) {
                hideView();
            }
            return true;
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            showInput();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            hideView();
        }
        return true;
    }

    private AddDanmukuListener listener;

    public void setAddDanmukuListener(AddDanmukuListener comment) {
        this.listener = comment;
    }

    public interface AddDanmukuListener {
        boolean addDanmuku(String text);
    }

    /**
     * 隐藏键盘
     */
    private void hideInput() {
        if (activity != null && manager != null)
            if (activity.getCurrentFocus() != null)
                if (activity.getCurrentFocus().getApplicationWindowToken() != null)
                    manager.hideSoftInputFromWindow(activity.getCurrentFocus().getApplicationWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 弹出键盘
     */
    private void showInput() {
        if (activity != null && manager != null)
            manager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    public boolean isShowView() {
        boolean flag;
        if (getVisibility() == VISIBLE || getVisibility() == INVISIBLE)
            flag = true;
        else
            flag = false;
        Log.d(tag, "isShowView/flag=" + flag);
        return flag;

    }

    @Override
    public void showView() {
        Log.d(tag, "showView");
//        if (activity != null &&
//                activity.videoPlayView != null &&
//                activity.videoPlayView.videoPlayer != null &&
//                activity.videoPlayView.videoPlayer.isPlayingVideo()) {
//            activity.videoPlayView.videoPlayer.pauseVideo();
//        }
//        if (activity != null &&
//                activity.videoPlayView != null &&
//                activity.videoPlayView.danmukuPlayer != null)
//            activity.videoPlayView.danmukuPlayer.pauseDanmaku();
        if (text != null) {
            text.requestFocus();
            text.setText("");
        }
        setVisibility(VISIBLE);
        view.setVisibility(VISIBLE);
    }

    @Override
    public void hideView() {
        Log.d(tag, "hideView");
        hideInput();
        setVisibility(GONE);
        view.setVisibility(GONE);
//        if (activity != null &&
//                activity.videoPlayView != null &&
//                activity.videoPlayView.videoPlayer != null) {
//            activity.videoPlayView.videoPlayer.startVideo();
//        }
//        if (activity != null &&
//                activity.videoPlayView != null &&
//                activity.videoPlayView.danmukuPlayer != null)
//            activity.videoPlayView.danmukuPlayer.resumeDanmaku();
        if (text != null) {
            text.clearFocus();
            text.setText("");
        }
    }

    @Override
    public void minView() {
    }

    @Override
    public void maxView() {
    }
}
