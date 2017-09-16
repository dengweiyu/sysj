package com.li.videoapplication.ui.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Comment;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.ui.activity.VideoPlayActivity;
import com.li.videoapplication.ui.adapter.FaceAdapter;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.utils.StringUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 视图：评论
 */
public class CommentView extends FrameLayout implements
        View.OnClickListener,
        View.OnFocusChangeListener,
        IVideoPlay{

    public final String action = this.getClass().getName();
    public final String tag = this.getClass().getSimpleName();

    private LayoutInflater inflater;
    private Activity activity;
    private InputMethodManager manager;
    private Context context;

    private View view;
    private View container;
    private TextView face;
    private TextView submit;
    private EditText edit;
    private View playGift;

    private String getEdit() {
        if (edit.getText() == null)
            return "";
        return edit.getText().toString().trim();
    }

    private List<Integer> data = new ArrayList<>();// 表情的资源
    private FaceAdapter adapter;// 表情适配器
    private GridView gridView;
    private boolean hasFace = false;// 表情是否弹出

    public CommentView(Context context) {
        this(context, null);
    }

    public CommentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void init(Activity activity) {
        this.activity = activity;

        if (activity != null)
            manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inflater = LayoutInflater.from(context);

        initContentView();
    }

    private void initContentView() {

        view = inflater.inflate(R.layout.view_comment, this);

        container = findViewById(R.id.comment_conteiner);
        submit = (TextView) findViewById(R.id.comment_submit);
        face = (TextView) findViewById(R.id.comment_face);
        gridView = (GridView) findViewById(R.id.gridview);
        edit = (EditText) findViewById(R.id.comment_edit);
       // playGift = findViewById(R.id.tv_video_play_gift);
     //   playGift.setOnClickListener(this);

        face.setOnClickListener(this);
        submit.setOnClickListener(this);

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
            setHasFace(false);
            hideGiftFragment();
        }
    }

    private void hideGiftFragment(){
        if (activity instanceof VideoPlayActivity){
            ((VideoPlayActivity)activity).hideGiftFragment();
        }
    }

    private void setHasFace(boolean hasFace){
        this.hasFace = hasFace;
        showOrHideTimeLine(!hasFace);
    }

    private void showOrHideTimeLine(boolean isShow){
        if (activity instanceof VideoPlayActivity){
            ((VideoPlayActivity)activity).showOrHideTimeLine(isShow);
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
                    setHasFace(true);
                    hideGiftFragment();
                } else {// 再一次点击表情按钮
                    // toggle软键盘
                    toggleInput();
                    face.setBackgroundResource(R.drawable.face_normal_205);
                    gridView.setVisibility(View.GONE);
                    setHasFace(false);
                }
                break;

            case R.id.comment_edit:// 评论输入框
                hideGiftFragment();
                setHasFace(false);
                gridView.setVisibility(View.GONE);
                face.setBackgroundResource(R.drawable.face_normal_205);
                break;

            case R.id.comment_submit:// 发表评论
                resetCommentState();
                if (StringUtil.isNull(getEdit())) {
                    ToastHelper.s("评论不能为空");
                    return;
                }
                hideInput();
                boolean flag;
                flag = listener != null && listener.comment(isSecondComment, getEdit());

                if (flag) {
                    container.setFocusable(true);
                    container.setFocusableInTouchMode(true);
                    container.requestFocus();
                    gridView.setVisibility(View.GONE);
                    face.setBackgroundResource(R.drawable.face_normal_205);
                    edit.setText("");
                    setHasFace(false);
                    isSecondComment = false;
                }
                break;
           /* case R.id.tv_video_play_gift:
                if (activity instanceof VideoPlayActivity){
                    ((VideoPlayActivity)activity).setGiftFragmentState(true);
                  //  new GiftNumberInputDialog(context,playGift).show();
                }
                if (hasFace){
                    face.setBackgroundResource(R.drawable.face_normal_205);
                    gridView.setVisibility(View.GONE);
                    hasFace = false;
                }

                break;*/
        }
    }

    public void hideFaceView(){
        if (hasFace){
            face.setBackgroundResource(R.drawable.face_normal_205);
            gridView.setVisibility(View.GONE);
            setHasFace(false);
        }
    }

    private CommentListener listener;

    public void setCommentListener(CommentListener comment) {
        this.listener = comment;
    }

    public interface CommentListener {
        boolean comment(boolean isSecondComment, String text);
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
     * 是否是二级评论
     */
    private boolean isSecondComment = false;

    /**
     * 二级评论
     */
    public void replyComment(Comment comment) {
        if (comment == null)
            return;
        isSecondComment = true;
        String s = "//@" + comment.getNickname() + ":" + comment.getContent();
        edit.setText(s);
        // 取得焦点
        edit.requestFocus();
        showInput();
    }

    /**
     * toggle键盘
     */
    private void toggleInput() {
        if (activity != null && manager != null){
            manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
        hideGiftFragment();

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
        if (activity != null && manager != null){
            manager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            hideGiftFragment();

        }

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
    public void minView() {}

    @Override
    public void maxView() {}

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
