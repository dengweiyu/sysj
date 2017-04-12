package com.li.videoapplication.ui.fragment;

import android.app.Activity;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.response.GroupGamerListEntity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.animation.RecyclerViewAnim;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.GroupDetailActivity;
import com.li.videoapplication.ui.adapter.GroupDetailPlayerAdapter;
import com.li.videoapplication.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 碎片：玩家
 */
public class GroupdetailPlayerFragment extends TBaseFragment implements OnRefreshListener,
        RequestLoadMoreListener,
        AppBarLayout.OnOffsetChangedListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private GroupDetailPlayerAdapter adapter;
    private GroupDetailActivity activity;
    private int page = 1;
    private int page_count;

    /**
     * 跳转：玩家动态
     */
    private void startPlayerDynamicActivity(Member member) {
        if (StringUtil.isNull(member.getId())) {
            member.setId(member.getMember_id());
        }
        ActivityManeger.startPlayerDynamicActivity(getActivity(), member);

        if (null != activity && activity.isSingleEvent){
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.GAME, activity.game.getGroup_name()+"-"+"游戏圈-玩家-头像");
        }else {
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.GAME, "游戏圈-玩家-头像");
        }

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity = (GroupDetailActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //该fragment处于最前台交互状态
        if (isVisibleToUser) {
            if (null != activity && activity.isSingleEvent){
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.GAME, activity.game.getGroup_name()+"-"+"游戏圈-玩家");
            }else {
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.GAME, "游戏圈-玩家");
            }

        }
    }

    @Override
    protected int getCreateView() {
        return R.layout.refresh_recyclerview;
    }

    @Override
    protected void initContentView(View view) {
        initRecyclerView();

        initAdapter();

        onRefresh();

        addOnClickListener();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    private void initAdapter() {
        List<Member> playerData = new ArrayList<>();
        adapter = new GroupDetailPlayerAdapter(playerData);
        adapter.openLoadAnimation(new RecyclerViewAnim());
        adapter.setOnLoadMoreListener(this);

        View emptyView = getActivity().getLayoutInflater().inflate(R.layout.emptyview,
                (ViewGroup) recyclerView.getParent(), false);
        TextView emptyText = (TextView) emptyView.findViewById(R.id.emptyview_text);
        emptyText.setText("暂无玩家关注");
        adapter.setEmptyView(emptyView);

        recyclerView.setAdapter(adapter);
    }

    private void loadData() {
        // 圈子玩家列表
        if (activity != null && activity.group_id != null)
            DataManager.groupGamerList(activity.group_id, getMember_id(), page);
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
                    // 数据全部加载完毕就调用 loadComplete
                    adapter.loadMoreEnd();
                }
            }
        });
    }

    private void addOnClickListener() {
        //recyclerview item点击事件处理
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int pos) {
                Member item = (Member) adapter.getItem(pos);
                startPlayerDynamicActivity(item);
                if (null != activity && activity.isSingleEvent){
                    UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.GAME,activity.game.getGroup_name()+"-"+"游戏圈-玩家-头像");
                }else {
                    UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.GAME, "游戏圈-玩家-头像");
                }

            }
        });
        //recyclerview item上子控件点击事件处理
        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter adapter, View view, int pos) {
                Member record = (Member) adapter.getItem(pos);
                if (!isLogin()) {
                    showLoginDialog();
                    return;
                }
                if (record.getMember_tick() == 1) {// 已关注状态
                    record.setFans(Integer.valueOf(record.getFans()) - 1 + "");
                    record.setMember_tick(0);
                } else {// 未关注状态
                    record.setFans(Integer.valueOf(record.getFans()) + 1 + "");
                    record.setMember_tick(1);
                }
                // 玩家关注
                DataManager.memberAttention201(record.getMember_id(), getMember_id());
                adapter.notifyItemChanged(pos);
                if (null != activity && activity.isSingleEvent){
                    UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.GAME, activity.game.getGroup_name()+"-"+"游戏圈-玩家-关注");
                }else {
                    UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.GAME, "游戏圈-玩家-关注");
                }

            }
        });
    }

    /**
     * 回调：圈子玩家列表
     */
    public void onEventMainThread(GroupGamerListEntity event) {

        if (event != null && event.isResult()) {
            page_count = event.getData().getPage_count();
            if (event.getData().getList().size() > 0) {
                if (page == 1) {
                    adapter.setNewData(event.getData().getList());
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    // 如果有下一页则调用addData，不需要把下一页数据add到list里面，直接新的数据给adapter即可。
                    adapter.addData(event.getData().getList());
                }
                ++page;
            }
        }
       hideProgress();
    }

    public void hideProgress() {
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);

        adapter.loadMoreComplete();//加载完成
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        swipeRefreshLayout.setEnabled(i == 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.appBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        activity.appBarLayout.removeOnOffsetChangedListener(this);
    }

}
