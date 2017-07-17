package com.li.videoapplication.ui.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.event.RefreshOrderDetailEvent;
import com.li.videoapplication.data.model.response.CommentTagEntity;
import com.li.videoapplication.data.model.response.CommitCommentEntity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.adapter.CommentTagAdapter;
import com.li.videoapplication.ui.dialog.LoadingDialog;
import com.li.videoapplication.ui.view.SpanItemDecoration;
import com.li.videoapplication.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import io.rong.eventbus.EventBus;

/**
 * 陪练订单评价
 */

public class CommentPlayWithOrderActivity extends TBaseAppCompatActivity implements View.OnClickListener {

    private String mCoachId;
    private String mOrderId;
    private String mNickName;
    private String mAvatar;
    private String mScore;

    private RecyclerView mTagList;

    private EditText mInput;

    private RatingBar mScoreStart;

    private CommentTagAdapter mTagAdapter;

    private List<CommentTagEntity.DataBean> mData;

    private LoadingDialog mLoadingDialog;


    @Override
    public void refreshIntent() {
        super.refreshIntent();
        mCoachId = getIntent().getStringExtra("coach_id");
        mNickName = getIntent().getStringExtra("nick_name");
        mAvatar = getIntent().getStringExtra("avatar");
        mScore = getIntent().getStringExtra("score");
        mOrderId = getIntent().getStringExtra("order_id");
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_comment_play_with_order;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        initToolbar();

        mInput = (EditText)findViewById(R.id.et_comment);
        mScoreStart = (RatingBar)findViewById(R.id.rb_score_start);

        mTagList = (RecyclerView)findViewById(R.id.rv_comment_tag);
        mTagList.setLayoutManager(new GridLayoutManager(this,2));
        mTagList.addItemDecoration(new SpanItemDecoration(20,false,false,true,false));
        mData = new ArrayList<>();
        mTagAdapter = new CommentTagAdapter(mData,0);
        mTagList.setAdapter(mTagAdapter);
        mTagList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if(mTagAdapter != null){
                    mTagAdapter.setSelected(i);
                }
            }
        });

        findViewById(R.id.iv_coach_info_go).setVisibility(View.GONE);
        findViewById(R.id.tv_commit_comment).setOnClickListener(this);
        final ImageView icon = (ImageView)findViewById(R.id.civ_coach_detail_icon);
        GlideHelper.displayImage(this,mAvatar,icon);
        UITask.postDelayed(new Runnable() {
            @Override
            public void run() {
                GlideHelper.displayImage(CommentPlayWithOrderActivity.this,mAvatar,icon);
            }
        },500);

        TextView nickName = (TextView)findViewById(R.id.tv_coach_detail_nick_name);
        nickName.setText(mNickName);

        TextView score = (TextView)findViewById(R.id.tv_coach_detail_score);
        score.setText(mScore+"分");

        RatingBar ratingBar = (RatingBar)findViewById(R.id.rb_coach_detail_score);
        try {
            ratingBar.setRating(Float.parseFloat(mScore));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mLoadingDialog = new LoadingDialog(this);
        mLoadingDialog.setProgressText("提交中..");
    }

    private void initToolbar(){
        findViewById(R.id.tb_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.tb_title)).setText("订单信息");
        setSupportActionBar(((Toolbar)findViewById(R.id.toolbar)));

    }

    @Override
    public void loadData() {
        super.loadData();

        //获取tag
        DataManager.getCommentTag();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tb_back:
                finish();
                break;
            case R.id.tv_commit_comment:
                String content = mInput.getEditableText().toString();
                if (StringUtil.isNull(content)){
                    ToastHelper.l("请输入评价内容哦~");
                    return;
                }
                mLoadingDialog.show();
                DataManager.commitComment(getMember_id(),mOrderId,content,mScoreStart.getRating(),mTagAdapter.getSelected());
                break;
        }
    }

    /**
     *
     */
    public void onEventMainThread(CommentTagEntity entity){
        if (entity != null && entity.isResult()){
            mData = entity.getData();
            mTagAdapter.setNewData(mData);
        }
    }

    /**
     *
     */
    public void onEventMainThread(CommitCommentEntity entity){
        if (entity != null && entity.isResult()){
            ToastHelper.l("感谢您的评价~");
            RefreshOrderDetailEvent event = new RefreshOrderDetailEvent(
                    mOrderId,
                    "",
                    "");
            EventBus.getDefault().post(event);
            finish();
        }else {

        }

        mLoadingDialog.dismiss();
    }
}
