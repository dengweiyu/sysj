package com.li.videoapplication.ui.fragment;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.response.GetCompVideoLists208Entity;
import com.li.videoapplication.data.model.response.SendComment208Entity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.activity.ActivityDetailActivity208;
import com.li.videoapplication.ui.adapter.GroupDetailVideoRecyclerAdapter;
import com.li.videoapplication.ui.toast.ToastHelper;
import com.li.videoapplication.ui.view.ActivityCommentView;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 碎片：参与活动
 */
public class JoinActivityFragment extends TBaseFragment implements PullLoadMoreRecyclerView.PullLoadMoreListener,
        ActivityCommentView.CommentListener {
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private GroupDetailVideoRecyclerAdapter adapter;
    private int page = 1;
    private List<VideoImage> data;
    private ActivityDetailActivity208 activity;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity = (ActivityDetailActivity208) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.DISCOVER, "活动-参加活动页面");
        }
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_joinactivity;
    }

    @Override
    protected void initContentView(View view) {
        ActivityCommentView commentView = (ActivityCommentView) view.findViewById(R.id.activity_commentview);
        commentView.init((ActivityDetailActivity208) getActivity());
        commentView.setCommentListener(this);
        commentView.showView();

        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) view.findViewById(R.id.pullLoadMoreRecyclerView);
        //设置线性布局
        mPullLoadMoreRecyclerView.setLinearLayout();
        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(this);//滑动监听（刷新/加载更多）

        data = new ArrayList<>();
        adapter = new GroupDetailVideoRecyclerAdapter(getActivity(), data);
        mPullLoadMoreRecyclerView.setAdapter(adapter);
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    @Override
    public void onRefresh() {
        page = 1;
        onLoadMore();
    }

    @Override
    public void onLoadMore() {
        if (activity != null && activity.match_id != null)
            DataManager.getCompVideoLists208(getMember_id(), activity.match_id, page);
    }

    @Override
    public boolean comment(String text) {
        Log.d(tag, "comment/text=" + text);
        // 评论
        DataManager.sendComment208(activity.match.getMatch_id(),
                getMember_id(),
                text);
        return true;
    }


    /**
     * 回调：参加活动--图文视频评论混合列表
     */
    public void onEventMainThread(GetCompVideoLists208Entity event) {

        if (event != null) {
            if (event.isResult() && event.getData().getData() != null) {
                if (page == 1) {
                    data.clear();
                }
                data.addAll(event.getData().getData());
                adapter.notifyDataSetChanged();
                if (event.getData().getData().size() > 0) {
                    ++page;
                }
            }
            mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
        }
    }

    /**
     * 回调：参加活动--发送评论
     */
    public void onEventMainThread(SendComment208Entity event) {

        if (event != null) {
            if (event.isResult()) {
                ToastHelper.s(event.getMsg());
            }
        }
    }

}
