package com.li.videoapplication.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Comment;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.activity.ActivityDetailActivity;
import com.li.videoapplication.ui.activity.VideoShareActivity;
import com.li.videoapplication.ui.adapter.FaceAdapter;
import com.li.videoapplication.tools.ToastHelper;
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
        IVideoPlay{

    /*
    * 跳转：上传图片
    */
    private void startActivityImageUploadActivity() {
        ActivityManager.startActivityImageUploadActivity(context, activity.match.getMatch_id());
    }

    /*
    * 跳转：上传视频
    */
    private void  startVideoChooseActivity() {
        ActivityManager.startVideoChooseActivity(context, activity.match, VideoShareActivity.TO_FINISH);
    }


    public final String action = this.getClass().getName();
    public final String tag = this.getClass().getSimpleName();

    private LayoutInflater inflater;
    private ActivityDetailActivity activity;
    private InputMethodManager manager;
    private Context context;

    private View view;
    private View container;
    private TextView face;
    private EditText edit;
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

    public void init(ActivityDetailActivity activity) {
        this.activity = activity;

        if (activity != null)
            manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inflater = LayoutInflater.from(context);

        initContentView();
    }

    private void initContentView() {

        view = inflater.inflate(R.layout.view_activitycomment, this);

        container = findViewById(R.id.comment_conteiner);
        send = (Button) findViewById(R.id.comment_send);
        face = (TextView) findViewById(R.id.comment_face);
        gridView = (GridView) findViewById(R.id.gridview);
        edit = (EditText) findViewById(R.id.comment_edit);

        face.setOnClickListener(this);
        send.setOnClickListener(this);
        edit.setOnClickListener(this);
        edit.setOnFocusChangeListener(this);

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
                            activity.setFABVisable(false);
                        }
                    }, 300);
                    hasFace = true;
                } else {// 再一次点击表情按钮
                    // toggle软键盘
                    toggleInput();
                    face.setBackgroundResource(R.drawable.face_normal_205);
                    gridView.setVisibility(View.GONE);
                    activity.setFABVisable(true);
                    hasFace = false;
                }
                break;

            case R.id.comment_edit:// 评论输入框
                hasFace = false;
                gridView.setVisibility(View.GONE);
                face.setBackgroundResource(R.drawable.face_normal_205);
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
        }
    }

    private CommentListener listener;

    public void setCommentListener(CommentListener comment) {
        this.listener = comment;
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
     * 二级评论
     */
    public void replyComment(Comment comment) {
        if (comment == null)
            return;
        String s = "//@" + comment.getNickname() + ":" + comment.getContent();
        edit.setText(s);
        // 取得焦点
        edit.requestFocus();
//        edit.setSelection(s.length());
        showInput();
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
