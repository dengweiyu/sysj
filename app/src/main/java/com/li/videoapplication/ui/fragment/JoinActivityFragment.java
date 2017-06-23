package com.li.videoapplication.ui.fragment;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Comment;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.response.GetCompVideoLists208Entity;
import com.li.videoapplication.data.model.response.SendComment208Entity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.activity.ActivityDetailActivity;
import com.li.videoapplication.mvp.adapter.GroupDetailVideoRecyclerAdapter;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.view.ActivityCommentView;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.LoadMoreViewWhite;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 碎片：参与活动
 */
public class JoinActivityFragment extends TBaseFragment implements OnRefreshListener,
        RequestLoadMoreListener, ActivityCommentView.CommentListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.activity_commentview)
    ActivityCommentView commentView;

    private GroupDetailVideoRecyclerAdapter adapter;
    private int page = 1;
    private List<VideoImage> data;
    private ActivityDetailActivity activity;
    private int page_count;

    /**
     * 跳转：视频播放
     */
    private void startVideoPlayActivity(VideoImage videoImage) {
        ActivityManager.startVideoPlayActivity(getActivity(), videoImage);
        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.DISCOVER, "活动-参加活动-视频播放");
    }

    /**
     * 跳转：玩家动态
     */
    private void startPlayerDynamicActivity(Member member) {
        ActivityManager.startPlayerDynamicActivity(getActivity(), member);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity = (ActivityDetailActivity) activity;
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
        initCommentView();

        initRecyclerView();

        initAdapter();

        loadData();

        addOnClickListener();
    }

    private void initCommentView() {
        commentView.init((ActivityDetailActivity) getActivity());
        commentView.setCommentListener(this);
        commentView.showView();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    private void initAdapter() {
        data = new ArrayList<>();
        adapter = new GroupDetailVideoRecyclerAdapter(getActivity(),null, data);
        adapter.setLoadMoreView(new LoadMoreViewWhite());
        adapter.setOnLoadMoreListener(this);

        recyclerView.setAdapter(adapter);
    }

    private void loadData() {
        //活动视频
        if (activity != null && activity.match_id != null)
            DataManager.getCompVideoLists208(getMember_id(), activity.match_id, page);
    }

    private void addOnClickListener() {
        //recyclerview item点击事件处理
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int pos) {

            }
        });

        //Item内部子控件的点击事件
        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                final VideoImage record = (VideoImage) adapter.getItem(position);

                switch (view.getId()) {
                    case R.id.joinactivity_comment://评论
                        if (isComment(record)){
                            Comment comment = new Comment();
                            comment.setNickname(record.getNickname());
                            comment.setContent(record.getContent());
                            commentView.replyComment(comment);
                        }else {
                            startVideoPlayActivity(record);
                        }

                        break;
                    case R.id.groupdetail_head://头像
                        Member member = gson.fromJson(record.toJSON(), Member.class);
                        startPlayerDynamicActivity(member);
                        break;
                    case R.id.groupdetail_video://视频
                        if (record.getVideo_id() != null && !record.getVideo_id().equals("0")) {
                            startVideoPlayActivity(record);
                        }
                        break;
                }
            }
        });
    }
    /**
     * 是否是文字评论
     */
    private boolean isComment(final VideoImage record) {
        // 评论
        return !StringUtil.isNull(record.getComment_id()) && !record.getComment_id().equals("0");
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    @Override
    public void onRefresh() {
        page = 1;
        loadData();
    }

    @Override
    public void onLoadMoreRequested() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (page <= page_count) {
                    loadData();
                } else {
                    // 数据全部加载完毕
                    adapter.loadMoreEnd();
                }
            }
        });
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
        if (event != null && event.isResult()) {
            page_count = event.getData().getTotalPage();

            if (event.getData().getData().size() > 0) {
                String recordOld = event.toJSON();
                String recordNew = recordOld.replace("\\\\", "\\");// \\ud83d\\ude24 --> \ud83d\ude24
                GetCompVideoLists208Entity entity = gson.fromJson(recordNew, GetCompVideoLists208Entity.class);
                if (page == 1) {
                    adapter.setNewData(entity.getData().getData());
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    // 如果有下一页则调用addData，不需要把下一页数据add到list里面，直接新的数据给adapter即可。
                    adapter.addData(entity.getData().getData());
                }
                ++page;
            }
        }
        adapter.loadMoreComplete();//加载完成
    }

    /**
     * 回调：参加活动--发送评论
     */
    public void onEventMainThread(SendComment208Entity event) {

        if (event != null) {
            if (event.isResult()) {
                ToastHelper.s(event.getMsg());
                onRefresh();
            }
        }
    }
}
