package com.li.videoapplication.ui.view;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Comment;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.ActivityDetailActivity208;
import com.li.videoapplication.ui.adapter.FaceAdapter;
import com.li.videoapplication.ui.toast.ToastHelper;
import com.li.videoapplication.utils.StringUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 视图：活动评论
 */
public class ActivityCommentView extends FrameLayout implements
        View.OnClickListener,
        View.OnFocusChangeListener,
        IVideoPlay,
        TextWatcher {

    /*
    * 跳转：上传图片
    */
    private void startActivityImageUploadActivity() {
        ActivityManeger.startActivityImageUploadActivity(context, activity.match.getMatch_id());
    }

    /*
    * 跳转：上传视频
    */
    private void  startVideoChooseActivity() {
        ActivityManeger.startVideoChooseActivity(context, activity.match);
    }


    public final String action = this.getClass().getName();
    public final String tag = this.getClass().getSimpleName();

    private LayoutInflater inflater;
    private ActivityDetailActivity208 activity;
    private InputMethodManager manager;
    private Context context;

    private View view;
    private View container;
    private TextView face;
    private ImageView plus;
    private EditText edit;
    private View uploadView, uploadPic, uploadVideo;
    private boolean isShowUploadView;
    private Button send;

    private String getEdit() {
        if (edit.getText() == null)
            return "";
        return edit.getText().toString().trim();
    }

    private List<Integer> data = new ArrayList<>();// 表情的资源
    private FaceAdapter adapter;// 表情适配器
    private GridView gridView;
    private boolean hasFace = false;// 表情是否弹出

    public ActivityCommentView(Context context) {
        this(context, null);
    }

    public ActivityCommentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void init(ActivityDetailActivity208 activity) {
        this.activity = activity;

        if (activity != null)
            manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inflater = LayoutInflater.from(context);

        initContentView();
    }

    private void initContentView() {

        view = inflater.inflate(R.layout.view_activitycomment, this);

        container = findViewById(R.id.comment_conteiner);
        plus = (ImageView) findViewById(R.id.comment_plus);
        send = (Button) findViewById(R.id.comment_send);
        face = (TextView) findViewById(R.id.comment_face);
        gridView = (GridView) findViewById(R.id.gridview);
        edit = (EditText) findViewById(R.id.comment_edit);
        uploadView = findViewById(R.id.activitycomment_uploadview);
        uploadPic = findViewById(R.id.activitycomment_uploadpic);
        uploadVideo = findViewById(R.id.activitycomment_uploadvideo);

        face.setOnClickListener(this);
        plus.setOnClickListener(this);
        uploadPic.setOnClickListener(this);
        uploadVideo.setOnClickListener(this);
        send.setOnClickListener(this);
        edit.setOnClickListener(this);
        edit.setOnFocusChangeListener(this);
        edit.addTextChangedListener(this);

        int res = 0;
        Field field;
        String[] faceArray = getResources().getStringArray(R.array.expressionArray);
        for (int i = 0; i < 34; i++) {
            try {
                // 从R.drawable类中获得相应资源ID（静态变量）的Field对象
                field = R.drawable.class.getDeclaredField(faceArray[i]);
                res = Integer.parseInt(field.get(null).toString());
                data.add(res);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        adapter = new FaceAdapter(context, data, edit);
        gridView.setAdapter(adapter);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            gridView.setVisibility(View.GONE);
            face.setBackgroundResource(R.drawable.face_normal_205);
            hasFace = false;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.comment_face:// 评论表情
                if (!hasFace) {// 点击表情按钮
                    hideInput();
                    adapter.notifyDataSetChanged();
                    face.setBackgroundResource(R.drawable.face_touch_205);
                    UITask.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            gridView.setVisibility(View.VISIBLE);
                        }
                    }, 300);
                    hasFace = true;
                } else {// 再一次点击表情按钮
                    // toggle软键盘
                    toggleInput();
                    face.setBackgroundResource(R.drawable.face_normal_205);
                    gridView.setVisibility(View.GONE);
                    hasFace = false;
                }
                break;

            case R.id.comment_edit:// 评论输入框
                uploadView.setVisibility(GONE);
                isShowUploadView = false;
                hasFace = false;
                gridView.setVisibility(View.GONE);
                face.setBackgroundResource(R.drawable.face_normal_205);
                break;

            case R.id.comment_plus:// +号
                if (isShowUploadView) {
                    uploadView.setVisibility(GONE);
                    isShowUploadView = false;
                } else {
                    uploadView.setVisibility(VISIBLE);
                    isShowUploadView = true;
                }
                break;

            case R.id.comment_send: // 发表评论
                resetCommentState();
                if (StringUtil.isNull(getEdit())) {
                    ToastHelper.s("评论不能为空");
                    return;
                }
                hideInput();
                boolean flag;
                if (listener != null) {
                    flag = listener.comment(getEdit());
                } else {
                    flag = false;
                }

                if (flag) {
                    container.setFocusable(true);
                    container.setFocusableInTouchMode(true);
                    container.requestFocus();
                    gridView.setVisibility(View.GONE);
                    face.setBackgroundResource(R.drawable.face_normal_205);
                    edit.setText("");
                    hasFace = false;
                }
                break;

            case R.id.activitycomment_uploadpic:
                if (activity != null && activity.match != null)
                    startActivityImageUploadActivity();
                break;

            case R.id.activitycomment_uploadvideo:
                if (activity != null && activity.match != null)
                    startVideoChooseActivity();
                break;
        }
    }

    private CommentListener listener;

    public void setCommentListener(CommentListener comment) {
        this.listener = comment;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (StringUtil.isNull(getEdit())) {
            send.setVisibility(View.GONE);
        } else {
            send.setVisibility(View.VISIBLE);
        }
    }

    public interface CommentListener {
        boolean comment(String text);
    }

    /**
     * 是否能够评论（60秒之内只能评论一次）
     */
    private boolean isComment = true;

    public void resetCommentState() {
        isComment = false;
        UITask.postDelayed(new Runnable() {

            @Override
            public void run() {
                isComment = true;
            }
        }, 60 * 1000);
    }

    /**
     * toggle键盘
     */
    private void toggleInput() {
        if (activity != null && manager != null)
            manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
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

    @Override
    public void showView() {
        setVisibility(VISIBLE);
        view.setVisibility(VISIBLE);
    }

    @Override
    public void hideView() {
        setVisibility(GONE);
        view.setVisibility(GONE);
    }

    @Override
    public void minView() {
    }

    @Override
    public void maxView() {
    }
}
