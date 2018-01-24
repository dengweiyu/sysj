package com.li.videoapplication.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.event.GoodAndStartEvent;
import com.li.videoapplication.data.model.response.GroupDataListEntity;
import com.li.videoapplication.data.model.response.GroupHotDataListEntity;
import com.li.videoapplication.data.model.response.GroupNewDataListEntity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.activity.GroupDetailActivity;
import com.li.videoapplication.mvp.adapter.GroupDetailVideoRecyclerAdapter;
import com.li.videoapplication.utils.GDTUtil;
import com.li.videoapplication.utils.StringUtil;
import com.qq.e.ads.nativ.NativeADDataRef;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 碎片：最新视频,最热视频
 */
public class GroupdetailVideoFragment extends TBaseFragment
        implements OnRefreshListener, RequestLoadMoreListener, AppBarLayout.OnOffsetChangedListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    public static final int GROUPDETAILVIDEO_NEW = 2;
    public static final int GROUPDETAILVIDEO_HOT = 3;
    public static final String NORMAL = "normal";
    public static final String HYBRID = "hybrid";

    private List<VideoImage> videoData;
    private GroupDetailVideoRecyclerAdapter adapter;
    private GroupDetailActivity activity;
    private int page = 1;
    private int page_count;

    public static NativeADDataRef adItem;

    private String from = NORMAL; //默認

    private String gameName;

    /**
     * 跳转：视频播放
     */
    public void startVideoPlayActivity(VideoImage videoImage) {
        ActivityManager.startVideoPlayActivity(getActivity(), videoImage);
        if (getTab() == GROUPDETAILVIDEO_NEW) {
            if (null != activity && activity.isSingleEvent) {
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.GAME, activity.game.getGroup_name() + "-" + "游戏圈-最新视频-有效");
            } else {
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.DISCOVER, "游戏圈-最新视频-有效");
            }
        } else if (getTab() == GROUPDETAILVIDEO_HOT) {
            if (null != activity && activity.isSingleEvent) {
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.GAME, activity.game.getGroup_name() + "-" + "游戏圈-最热视频-有效");
            } else {
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.DISCOVER, "游戏圈-最热视频-有效");
            }
        }
    }

    /**
     * 跳转：玩家动态
     */
    private void startPlayerDynamicActivity(Member member) {
        ActivityManager.startPlayerDynamicActivity(getActivity(), member);
    }

    public static GroupdetailVideoFragment newInstance(String from, int tab) {
        GroupdetailVideoFragment fragment = new GroupdetailVideoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("tab", tab);
        bundle.putString("from", from);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static GroupdetailVideoFragment newInstance(String from, String gameName, int tab, String groupId) {
        GroupdetailVideoFragment fragment = new GroupdetailVideoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("tab", tab);
        bundle.putString("group_id", groupId);
        bundle.putString("from", from);
        bundle.putString("game_name", gameName);

        fragment.setArguments(bundle);
        return fragment;
    }

    public int tab;

    private String mGroupId;

    public int getTab() {
        if (tab == 0) {
            try {
                tab = getArguments().getInt("tab");
                if (getArguments().containsKey("group_id")) {
                    mGroupId = getArguments().getString("group_id");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (tab == 0) {
                tab = GROUPDETAILVIDEO_NEW;
            }
        }
        return tab;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            if (activity instanceof GroupDetailActivity){
                this.activity = (GroupDetailActivity) activity;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //该fragment处于最前台交互状态
        if (isVisibleToUser) {
            if (from.equals(NORMAL)) {
                if (getTab() == GROUPDETAILVIDEO_NEW) {
                    if (null != activity && activity.isSingleEvent) {
                        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.GAME, activity.game.getGroup_name() + "-" + "游戏圈-最新视频");
                    } else {
                        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.GAME, "游戏圈-最新视频");
                    }
                } else if (getTab() == GROUPDETAILVIDEO_HOT) {
                    if (null != activity && activity.isSingleEvent) {
                        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.GAME, activity.game.getGroup_name() + "-" + "游戏圈-精彩视频");
                    } else {
                        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.GAME, "游戏圈-精彩视频");
                    }
                }
            } else if (from.equals(HYBRID)) {
                if (getTab() == GROUPDETAILVIDEO_NEW) {
                    if (gameName != null && gameName.length() > 0) {
                        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.GAME, gameName + "-" + "游戏圈2.0-视频-最新");
                    } else {
                        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.GAME, "游戏圈2.0-视频-最新");
                    }
                } else if (getTab() == GROUPDETAILVIDEO_HOT) {
                    if (gameName != null && gameName.length() > 0) {
                        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.GAME, gameName + "-" + "游戏圈2.0-视频-最熱");
                    } else {
                        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.GAME, "游戏圈2.0-视频-最熱");
                    }
                }
            }
        }
    }

    @Override
    protected int getCreateView() {
        return R.layout.refresh_recyclerview;
    }

    @Override
    protected void initContentView(View view) {

        getTab();
        from = getArguments().getString("from");
        gameName = getArguments().getString("game_name");


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
        videoData = new ArrayList<>();
        adapter = new GroupDetailVideoRecyclerAdapter(getActivity(), this, videoData);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.setOnLoadMoreListener(this);

        View emptyView = getActivity().getLayoutInflater().inflate(R.layout.emptyview,
                (ViewGroup) recyclerView.getParent(), false);
        TextView emptyText = (TextView) emptyView.findViewById(R.id.emptyview_text);
        emptyText.setText("暂无视频");
        adapter.setEmptyView(emptyView);

        recyclerView.setAdapter(adapter);
    }

    private void loadData() {
        if (activity != null && activity.group_id != null) {
            if (getTab() == GROUPDETAILVIDEO_NEW) {
                Log.d(tag, "~~~~~~~~~ loadHomeData: NEW ~~~~~~~~~");
                // 圈子视频列表（最新）
//                DataManager.groupDataList(activity.group_id, getMember_id(), page);
                DataManager.groupDataList226(activity.group_id, getMember_id(), page, "newest");
            } else if (getTab() == GROUPDETAILVIDEO_HOT) {
                Log.d(tag, "~~~~~~~~~ loadHomeData: HOT ~~~~~~~~~");
                // 圈子视频列表（最热）
//                DataManager.groupHotDataList(activity.group_id, getMember_id(), page);
                DataManager.groupDataList226(activity.group_id, getMember_id(), page, "hosttest");
            }
        } else {
            if (!StringUtil.isNull(mGroupId)) {
                if (getTab() == GROUPDETAILVIDEO_NEW) {
                    Log.d(tag, "~~~~~~~~~ loadHomeData: NEW ~~~~~~~~~");
                    // 圈子视频列表（最新）
                    DataManager.groupDataList(mGroupId, getMember_id(), page);
                } else if (getTab() == GROUPDETAILVIDEO_HOT) {
                    Log.d(tag, "~~~~~~~~~ loadHomeData: HOT ~~~~~~~~~");
                    // 圈子视频列表（最热）
                    DataManager.groupHotDataList(mGroupId, getMember_id(), page);
                }
            }
        }
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    @Override
    public void onRefresh() {
        Log.d(tag, "~~~~~~~~~ loadHomeData: ~~~~~~~~~");
        page = 1;
        loadData();
    }

    @Override
    public void onLoadMoreRequested() {
        Log.d(tag, "~~~~~~~~~ onLoadMoreRequested: ~~~~~~~~~");
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                Log.d(tag, "run: page == " + page + " , page_count == " + page_count);
                if (page <= page_count) {
                    loadData();
                } else {
                    // 数据全部加载完毕
                    adapter.loadMoreEnd();
                }
            }
        });
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

    private void addOnClickListener() {
        //recyclerview item点击事件处理
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                final VideoImage record = (VideoImage) adapter.getItem(position);
                if (record.isAD()) {
                    adItem.onExposured(recyclerView);
                    adItem.onClicked(view);
                }
            }
        });

        //Item内部子控件的点击事件
        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                final VideoImage record = (VideoImage) adapter.getItem(position);

                switch (view.getId()) {
                    case R.id.groupdetail_comment://评论
                        if (StringUtil.isNull(record.getPic_id())
                                && StringUtil.isNull(record.getVideo_id())
                                && !StringUtil.isNull(record.getId())) {
                            return;
                        }
                        startVideoPlayActivity(record);
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

    @Override
    public void onResume() {
        super.onResume();
        if (activity != null) {
            activity.appBarLayout.addOnOffsetChangedListener(this);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (activity != null) {
            activity.appBarLayout.removeOnOffsetChangedListener(this);
        }

    }

    /**
     * 回调：圈子视频列表（最新）
     */
    public void onEventMainThread(GroupNewDataListEntity event) {
        hideProgress();
        if (event != null && event.isResult() && getTab() == GROUPDETAILVIDEO_NEW) {
            refreshData(event, GDTUtil.POS_ID_GROUP_NEW);
            Log.d(tag, "~~~~~~~~~ 圈子视频列表（最新）: ~~~~~~~~~");
        }
    }

    /**
     * 回调：圈子视频列表（最热）
     */
    public void onEventMainThread(GroupHotDataListEntity event) {
        hideProgress();
        if (event != null && event.isResult() && getTab() == GROUPDETAILVIDEO_HOT) {
            refreshData(event, GDTUtil.POS_ID_GROUP_HOT);
            Log.d(tag, "~~~~~~~~~ 圈子视频列表（最热）: ~~~~~~~~~");
        }
    }

    private void refreshData(GroupDataListEntity event, String pos_id) {
        page_count = event.getData().getPage_count();
        if (page == 1) {
            videoData.clear();
            videoData = event.getData().getList();
            adapter.setNewData(videoData);
            // if (AppConstant.SHOW_DOWNLOAD_AD)
            //  initGDT(pos_id);//插入一行广点通广告
        } else {
            // 如果有下一页则调用addData，不需要把下一页数据add到list里面，直接新的数据给adapter即可。
            adapter.addData(event.getData().getList());
        }
        ++page;
    }

    private void initGDT(String pos_id) {
        GDTUtil.nativeAD(getActivity(), pos_id, new GDTUtil.GDTonLoaded() {

            @Override
            public void onADLoaded(NativeADDataRef adItem) {
                if (adItem != null) {
                    GroupdetailVideoFragment.adItem = adItem;
                    VideoImage ad = new VideoImage();
                    ad.setAD(true);//自己定一个广告标识
                    ad.setVideo_id("1");//自己定一个id给广告
                    ad.setAvatar(adItem.getIconUrl());
                    ad.setVideo_flag(adItem.getImgUrl());
                    ad.setNickname(adItem.getTitle());
                    ad.setTitle(adItem.getDesc());
                    // 放在第2个位置
                    if (adapter.getData().size() >= 2) {
                        adapter.addData(1, ad);
                    } else if (adapter.getData().size() == 0) {
                        videoData.add(ad);
                        adapter.setNewData(videoData);
                    } else {
                        adapter.addData(ad);
                    }
                }
            }

            @Override
            public void onADError() {

            }
        });
    }

    /**
     * 点赞 收藏事件
     */
    public void onEventMainThread(GoodAndStartEvent event) {
        if (StringUtil.isNull(event.getVideoId())) {
            return;
        }
        if (videoData == null) {
            return;
        }
        for (VideoImage v :
                videoData) {
            if (event.getVideoId().equals(v.getVideo_id())) {
                if (event.getType() == GoodAndStartEvent.TYPE_GOOD) {
                    if (event.isPositive()) {
                        v.flower_tick = 1;
                        v.flower_count = (Integer.parseInt(v.flower_count) + 1) + "";
                    } else {
                        int count = Integer.parseInt(v.flower_count);
                        if (count < 1) {
                            return;
                        }
                        v.flower_tick = 0;
                        v.flower_count = (count - 1) + "";
                    }
                    adapter.notifyDataSetChanged();
                } else if (event.getType() == GoodAndStartEvent.TYPE_START) {
                    if (event.isPositive()) {
                        v.collection_tick = 1;
                        v.collection_count = (Integer.parseInt(v.collection_count) + 1) + "";
                    } else {
                        v.collection_tick = 0;

                        int count = Integer.parseInt(v.collection_count);
                        if (count < 1) {
                            return;
                        }
                        v.collection_count = (count - 1) + "";
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
            }
        }
    }
}
