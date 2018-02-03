package com.li.videoapplication.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.MessageReplyComment;
import com.li.videoapplication.data.model.response.MsgReplyCommentEntity;
import com.li.videoapplication.data.model.response.ReplyGameGroupCommentEntity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.ViewRelyKeyboardRequest;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.adapter.Face2Adapter;
import com.li.videoapplication.ui.adapter.FaceAdapter;
import com.li.videoapplication.ui.adapter.MessageReplyCommentAdapter;
import com.li.videoapplication.ui.view.SimpleItemDecoration;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.eventbus.EventBus;

import static com.li.videoapplication.R.id.root;
import static com.li.videoapplication.R.id.transition_area;

/**
 * Created by cx on 2018/1/29.
 */

public class MessageReplyActivity extends TBaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnFocusChangeListener {

    @BindView(R.id.civ_avatar)
    ImageView mCivAvatar;
    @BindView(R.id.tv_reply_name)
    TextView mTvReplyName;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.rv_reply)
    RecyclerView mRvReply;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;
    @BindView(R.id.v_comment_face)
    View mCommentFace;
    @BindView(R.id.et_comment)
    EditText mEtComment;
    @BindView(R.id.tv_reply_amount)
    TextView mTvAmount;
    @BindView(R.id.srl_reply)
    SwipeRefreshLayout mSrlReply;
    @BindView(R.id.rl_comment)
    View mRlComment;
    @BindView(R.id.ll_bottom)
    View mLlBottom;
    @BindView(R.id.ll_content)
    View mLlContent;

    private RecyclerView mRvFace;

    private String mRelid; //关联id

    private MessageReplyCommentAdapter mCommentAdapter;
    private Face2Adapter mFaceAdapter;

    private List<MessageReplyComment> mData = new ArrayList<>();
    private List<Integer> mFaceData = new ArrayList<>();
    private String[] faceCnArray;

    private MessageReplyComment replyCommentData; //回复的item的数据

    private MsgReplyCommentEntity.DataBean mAData;

    private TextImageHelper textImageHelper = new TextImageHelper();
    private InputMethodManager manager;

    private boolean isWillKeyboardShow;
    private boolean isFaceShow;

    private int mTotalPage = 0;
    private int mPage = 1;

    @Override
    public void refreshIntent() {
        super.refreshIntent();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        try {
            mRelid = getIntent().getStringExtra("relid");
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        initFaceData();
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public void initView() {
        super.initView();

        setAbTitle("游戏圈回复");
        mEtComment.setOnFocusChangeListener(this);
        mCommentAdapter = new MessageReplyCommentAdapter(this, mData);
        View emptyView = getLayoutInflater().inflate(R.layout.emptyview,
                (ViewGroup) mRvReply.getParent(), false);
        mCommentAdapter.setEmptyView(emptyView);
        TextView emptyText = (TextView) emptyView.findViewById(R.id.emptyview_text);
        emptyText.setText("暂时无其它评论");
        mRvReply.setLayoutManager(new LinearLayoutManager(this));
        mRvReply.addItemDecoration(new SimpleItemDecoration(this, true, true, true, true) {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int pos = parent.getChildAdapterPosition(view);
                if (pos == mData.size() - 1) {
                    outRect.bottom = ScreenUtil.dp2px(42);
                }
            }
        });
        mRvReply.setAdapter(mCommentAdapter);

        mSrlReply.setOnRefreshListener(this);
        setAdapterClickEvent();
        setAdapterListener();

        Log.d(tag, "内容布局的纵坐标:" + mLlContent.getTop());
        Log.d(tag, "内容布局的Y坐标:" + mLlContent.getY());
        initKeyboardListener();
    }

    private void setAdapterListener() {
        mCommentAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mRvReply.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mPage >= mTotalPage) {
                            mCommentAdapter.loadMoreEnd();
                        } else {
                            ++mPage;
                            loadData();
                        }
                    }
                }, 200);
            }
        }, mRvReply);
    }

    @Override
    public void loadData() {
        super.loadData();

        DataManager.getMsgReplyComment(getMember_id(), mRelid, mPage);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_message_reply;
    }

    @OnClick({R.id.tv_submit, R.id.v_comment_face, R.id.et_comment, R.id.tv_reply_name, R.id.tv_content})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                reply(getTextFromEdit());
                hideKeyboard();
                if (isFaceShow)
                    setFaceShow(false);
                break;
            case R.id.v_comment_face:
                if (mRvFace == null) {
                    initFaceView();
                }
                if (isFaceShow) {
//                    toggleKeyboard();
                    setFaceShow(false);
                } else {
                    hideKeyboard();
                    mFaceAdapter.notifyDataSetChanged();
                    setFaceShow(true);
                }
                break;
            case R.id.et_comment:
                if (mIsSoftKeyboardShowing)
                    isWillKeyboardShow = false;
                else isWillKeyboardShow = true;

                if (isFaceShow) {
                    setFaceShow(false);
                }
                break;
            case R.id.tv_reply_name:
            case R.id.tv_content:
                replyCommentData = mAData.getParentData();
                readyReply(replyCommentData);
                break;
        }

    }

    /**
     * 获取评论数据回调
     *
     * @param entity
     */
    public void onEventMainThread(MsgReplyCommentEntity entity) {
        if (entity != null && entity.isResult()) {
            mAData = entity.getAData();
            mTotalPage = mAData.getPage_count();
            replyCommentData = mAData.getParentData();
            setPresentData(mAData.getParentData());
            setAmount(String.valueOf(mAData.getCountNum()));
            mData.addAll(mAData.getChildList());
            if (mSrlReply.isRefreshing()) {
                mCommentAdapter.setNewData(mData);
                mSrlReply.setRefreshing(false);
            }
            mCommentAdapter.notifyDataSetChanged();
            if (commentFlag) {
                mRvReply.smoothScrollToPosition(0);
                commentFlag = false;
            }
        } else {
            mCommentAdapter.loadMoreFail();
        }
        if (mCommentAdapter.isLoading() && mPage > 1) {
            mCommentAdapter.loadMoreComplete();
        }
        if (entity!= null && entity.getAData() == null) {
            ToastHelper.s("该评论已删除..");
            finish();
        }
    }

    /**
     * 提交评论后回调
     *
     * @param entity
     */
    private boolean commentFlag = false;
    public void onEventMainThread(ReplyGameGroupCommentEntity entity) {
        if (entity != null && entity.isResult()) {
            ToastHelper.s("评论成功");
            commentFlag = true;
            onRefresh();
        } else {
            ToastHelper.s("评论失败");
        }
    }

    /**
     * 设置容器点击事件
     */
    public void setAdapterClickEvent() {
        mCommentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mIsSoftKeyboardShowing)
                    isWillKeyboardShow = false;
                else isWillKeyboardShow = true;
                replyCommentData = (MessageReplyComment) adapter.getData().get(position);
                readyReply(replyCommentData);
            }
        });

    }

    private void setFaceShow(boolean isShow) {
        if (isShow) {
            ObjectAnimator b2tAnim = ObjectAnimator.ofFloat(mLlBottom, "translationY", mRvFace.getHeight(), 0.f);
            ObjectAnimator aphaAnim = ObjectAnimator.ofFloat(mRvFace, "alpha", 0.4f, 1.f);
            AnimatorSet animSet = new AnimatorSet();
            animSet.play(b2tAnim).with(aphaAnim);
            animSet.setDuration(200);
            animSet.start();
            mCommentFace.setBackgroundResource(R.drawable.face_touch_205);
            isFaceShow = true;
        } else {
            ObjectAnimator t2bAnim = ObjectAnimator.ofFloat(mLlBottom, "translationY", 0.f, mRvFace.getHeight());
            ObjectAnimator aphaAnim = ObjectAnimator.ofFloat(mRvFace, "alpha", 1.f, 0.4f);
            AnimatorSet animSet = new AnimatorSet();
            if (isWillKeyboardShow) {
                animSet.setDuration(0);
            } else
                animSet.setDuration(200);
            animSet.play(t2bAnim).with(aphaAnim);
            animSet.start();
            mCommentFace.setBackgroundResource(R.drawable.face_normal_205);
            isFaceShow = false;
        }
    }

    /**
     * 父级评论数据
     *
     * @param data
     */
    private void setPresentData(MessageReplyComment data) {
        textImageHelper.setImageViewImageNet(this, mCivAvatar, data.getReplyMemberIcon());
        textImageHelper.setTextViewText(mTvReplyName, data.getReplyMemberName());
        textImageHelper.setTextViewText(mTvContent, data.getContent());
    }

    /**
     * 设置回复数量
     *
     * @param amount
     */
    private void setAmount(String amount) {
        SpannableStringBuilder ssb = new SpannableStringBuilder("回复 ");
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#ee414141"));
        ssb.setSpan(styleSpan, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(colorSpan, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append("(").append(amount).append(")");
        ssb.setSpan(new RelativeSizeSpan(1.1f), 3, 3 + amount.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvAmount.setText(ssb);
    }

    private void initFaceView() {
        ViewStub stub = (ViewStub) findViewById(R.id.vs_face);
        stub.inflate();
        mRvFace = (RecyclerView) findViewById(R.id.rv_face);
        initFaceData();
        mFaceAdapter = new Face2Adapter(mFaceData);
        mRvFace.setAdapter(mFaceAdapter);
        mRvFace.setLayoutManager(new GridLayoutManager(this, 7));
        mLlBottom.setTranslationY(mRvFace.getWidth());
        mRvFace.setAlpha(0.4f);
        mFaceAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (mFaceData.get(position) == R.drawable.face_del) {
                    // 删除表情
                    int selectionStart = mEtComment.getSelectionStart();// 获取光标的位置
                    if (selectionStart > 0) {
                        String body = mEtComment.getText().toString();
                        if (!TextUtils.isEmpty(body)) {
                            String tempStr = body.substring(0, selectionStart);
                            if (tempStr.lastIndexOf("]") == selectionStart - 1) {
                                int i = tempStr.lastIndexOf("[");// 获取最后一个表情的起始位置
                                int j = tempStr.lastIndexOf("]");// 获取最后一个表情的终止位置
                                if (i != -1 && j != -1) {
                                    mEtComment.getEditableText().delete(i, selectionStart);
                                    return;
                                }
                            } else {
                                mEtComment.getEditableText().delete(tempStr.length() - 1, selectionStart);
                            }
                        }
                    }
                } else {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mFaceData.get(position));
                    bitmap = FaceAdapter.zoomImage(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
                    ImageSpan imageSpan = new ImageSpan(MessageReplyActivity.this, bitmap);// 用ImageSpan指定图片替代文字

                    String faceCnStr = "[" + faceCnArray[position] + "]";
                    SpannableString spannableString = new SpannableString("[" + faceCnArray[position] + "]");
                    spannableString.setSpan(imageSpan, 0, faceCnStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    // 将表情追加到编辑框
                    int index = mEtComment.getSelectionStart();// 获取光标位置
                    // getEditableText：获取EditText的文字，可编辑
                    // getText：获取EditText的文字，不可编辑
                    Editable editable = mEtComment.getEditableText();
                    if (index < 0 || index >= editable.length()) {
                        editable.append(spannableString);
                    } else {
                        editable.insert(index, spannableString);// 光标所在位置插入文字
                    }
                }
            }
        });
    }

    private void initFaceData() {
        faceCnArray = getResources().getStringArray(R.array.expressionCnArray);

        int res = 0;
        Field field;
        String[] faceArray = getResources().getStringArray(R.array.expressionArray);
        for (int i = 0; i < 34; i++) {
            try {
                // 从R.drawable类中获得相应资源ID（静态变量）的Field对象
                field = R.drawable.class.getDeclaredField(faceArray[i]);
                res = Integer.parseInt(field.get(null).toString());
                mFaceData.add(res);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void readyReply(MessageReplyComment comment) {
        String s = new StringBuffer().append("//@")
                .append(comment.getReplyMemberName()).append(":")
                .append(comment.getContent()).toString();
        mEtComment.setHint(s);
        mEtComment.requestFocus();
        showKeyboard();
    }

    private void reply(String content) {
        if (StringUtil.isNull(content)) {
            ToastHelper.s("评论不能为空");
            return;
        }
        if (replyCommentData != null) {
            DataManager.replyGameGroupComment(content, getMember_id(), replyCommentData.getMember_id(),
                    replyCommentData.getId(), replyCommentData.getParent_id(),
                    replyCommentData.getGroup_id());

//            ReplyGameGroupCommentEntity entity = new ReplyGameGroupCommentEntity(); //TODO:记得换回来
//            entity.setResult(true);
//            EventBus.getDefault().post(entity);
        }
        getWindow().getDecorView().setFocusable(true);
        getWindow().getDecorView().setFocusableInTouchMode(true);
        getWindow().getDecorView().requestFocus();
        mEtComment.setText("");
        mEtComment.setHint("本宝宝有话要说");
    }

    private String getTextFromEdit() {
        if (mEtComment.getText() == null)
            return "";
        return mEtComment.getText().toString().trim();
    }

    private void showKeyboard() {
        if (isFaceShow) {
            setFaceShow(false);
        }
        if (manager != null) {
            manager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        }
    }

    private void hideKeyboard() {
        if (manager != null && getCurrentFocus() != null
                && getCurrentFocus().getApplicationWindowToken() != null) {
            manager.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void toggleKeyboard() {
        if (manager != null) {
            manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        mData.clear();
        loadData();

        UITask.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSrlReply.isRefreshing()) {
                    mSrlReply.setRefreshing(false);
                    ToastHelper.s(R.string.net_unstable);
                }
            }
        }, 5000);
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus && isFaceShow) {
            setFaceShow(false);
        }
    }

    private ViewTreeObserver.OnGlobalLayoutListener mLayoutChangeListener;
    private boolean mIsSoftKeyboardShowing;

    private int mContentTop;

    private void initKeyboardListener() {
        mIsSoftKeyboardShowing = false;

        mLayoutChangeListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //判断窗口可见区域大小
                Rect r = new Rect();
                View decorView = getWindow().getDecorView();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                final Point point = new Point();
                getWindowManager().getDefaultDisplay().getSize(point);
                //如果屏幕高度和Window可见区域高度差值大于整个屏幕高度的1/3，则表示软键盘显示中，否则软键盘为隐藏状态。
                final int heightDifference = point.y - (r.bottom - r.top);
                boolean isKeyboardShowing = heightDifference > point.y / 3;

                //如果之前软键盘状态为显示，现在为关闭，或者之前为关闭，现在为显示，则表示软键盘的状态发生了改变
                if ((mIsSoftKeyboardShowing && !isKeyboardShowing) || (!mIsSoftKeyboardShowing && isKeyboardShowing)) {
                    mIsSoftKeyboardShowing = isKeyboardShowing;
                    isWillKeyboardShow = false;
                    if (mIsSoftKeyboardShowing) {
                        Log.d(tag, "内容布局的Top坐标:" + mContentTop);
                        Log.d(tag, "内容布局的Y坐标:" + mLlContent.getY());
                        Log.d(tag, "内容布局的Y坐标:" + mLlContent.getY());
                        final int h = r.bottom;
                        mLlBottom.setY(h - mRlComment.getHeight());
                        mLlBottom.setAlpha(0);
                        UITask.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mLlBottom.setAlpha(1);
                            }
                        }, 100);
                        View view = (View) mLlBottom.getParent();
                        Log.d(tag, "decorView的纵Top坐标:" + decorView.getTop());
                        Log.d(tag, "decorView的纵Y坐标:" + decorView.getY());
                        Log.d(tag, "变化高度：" + heightDifference);
                        Log.d(tag, "底部布局的父布局的Top坐标:" + view.getTop());
                        Log.d(tag, "底部布局的父布局的Y坐标:" + view.getY());
                        Log.d(tag, "底部布局的Top坐标:" + mLlBottom.getTop());
                        Log.d(tag, "底部布局的Y坐标:" + mLlBottom.getY());
//                        UITask.postDelayed(new Runnable() { //延时会整个顶上去
//                            @Override
//                            public void run() {
////                                mLlBottom.setY(h - mRlComment.getHeight());
//                                mLlBottom.setTranslationY(-heightDifference);
//                            }
//                        }, 100);

                    } else {
                        mLlBottom.setY(point.y - mRlComment.getHeight());
//                        UITask.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                mLlBottom.setY(point.y - mRlComment.getHeight());
//                            }
//                        }, 100);
                    }
                }
            }
        };
        //注册布局变化监听
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(mLayoutChangeListener);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isFaceShow) {
                setFaceShow(false);
                return true;
            } else if (mIsSoftKeyboardShowing) {
                hideKeyboard();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        //移除布局变化监听
        getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(mLayoutChangeListener);
        super.onDestroy();
    }
}
