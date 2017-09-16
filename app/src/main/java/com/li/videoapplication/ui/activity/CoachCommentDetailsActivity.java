package com.li.videoapplication.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.response.CoachCommentEntity;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.ui.adapter.CoachCommentAdapter;
import com.li.videoapplication.ui.view.SimpleItemDecoration;
import com.li.videoapplication.ui.view.SpanItemDecoration;
import com.li.videoapplication.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 教练被评论列表
 */

public class CoachCommentDetailsActivity extends TBaseAppCompatActivity implements View.OnClickListener{
    private RecyclerView mAllComment;
    private CoachCommentAdapter mCommentAdapter;
    private String coachId;
    private int mPage = 1;
    private List<CoachCommentEntity.ADataBean> mData;
    @Override
    protected int getContentView() {
        return R.layout.activity_coach_comment_detail;
    }

    @Override
    public void refreshIntent() {
        super.refreshIntent();
        coachId = getIntent().getStringExtra("coach_id");
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        initToolbar();
        mAllComment = (RecyclerView) findViewById(R.id.rv_coach_all_comment);
        mAllComment.setLayoutManager(new LinearLayoutManager(this));
        mAllComment.addItemDecoration(new SimpleItemDecoration(this,false,false,false,true));

        mAllComment.addItemDecoration(new SpanItemDecoration(ScreenUtil.dp2px(15),true,true,true,false));

        mData = new ArrayList<>();
        mCommentAdapter = new CoachCommentAdapter(mData);
        mAllComment.setAdapter(mCommentAdapter);

        mCommentAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mCommentAdapter.setEnableLoadMore(false);
    }

    private void initToolbar(){
        findViewById(R.id.tb_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.tb_title)).setText("全部评价");
        setSupportActionBar(((Toolbar)findViewById(R.id.toolbar)));
    }

    @Override
    public void loadData() {
        super.loadData();
        DataManager.getCoachComment(coachId,"all",mPage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tb_back:
                finish();
                break;
        }
    }

    final BaseQuickAdapter.RequestLoadMoreListener mLoadMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
            mPage++;
            loadData();
        }
    };

    private synchronized void enableLoadMore(boolean isEnable){

        if (isEnable){
            mCommentAdapter.setOnLoadMoreListener(mLoadMoreListener);
            mCommentAdapter.setEnableLoadMore(true);
        }else {

            mCommentAdapter.setEnableLoadMore(false);
            //移除listener一定要在 EnableLoadMore后面。。具体可看源码
            mCommentAdapter.setOnLoadMoreListener(null);
        }
    }

    public void onEventMainThread(CoachCommentEntity entity){
        mCommentAdapter.loadMoreComplete();
        if (entity.isResult()){
            if(mPage == 1){
                mData.clear();
            }
            if (entity.getAData() != null && entity.getAData().size() > 0) {
                findViewById(R.id.cv_coach_detail_all_comment).setVisibility(View.VISIBLE);
                mData.addAll(entity.getAData());
                mCommentAdapter.notifyDataSetChanged();
            }
            enableLoadMore(entity.getPageCount() > mPage);
        }else{

        }


    }
}
