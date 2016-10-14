package com.li.videoapplication.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.response.RankingMemberRankingEntity;
import com.li.videoapplication.data.model.response.RankingMemberRankingFansEntity;
import com.li.videoapplication.data.model.response.RankingMemberRankingRankEntity;
import com.li.videoapplication.data.model.response.RankingMemberRankingVideoEntity;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.adapter.PlayerBillboardAdapter;
import com.li.videoapplication.utils.StringUtil;

/**
 * 碎片：风云榜-玩家榜
 */
public class FansBillboardFragment extends TBaseFragment implements OnRefreshListener2<ListView> {

    public static final int PLAYERBILLBOARD_FANS = 1;
    public static final int PLAYERBILLBOARD_RANK = 2;
    public static final int PLAYERBILLBOARD_VIDEO = 3;

    public static FansBillboardFragment newInstance(int tab) {
        FansBillboardFragment fragment = new FansBillboardFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("tab", tab);
        fragment.setArguments(bundle);
        return fragment;
    }

    public int tab;

    public int getTab() {
        if (tab == 0) {
            try {
                tab = getArguments().getInt("tab");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (tab == 0) {
                tab = PLAYERBILLBOARD_FANS;
            }
        }
        return tab;
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_searchmember;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected IPullToRefresh getPullToRefresh() {
        return pullToRefreshListView;
    }

    private View mHeaderView;
    private TextView rank;
    private RelativeLayout container;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (getTab() == PLAYERBILLBOARD_RANK) {
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.DISCOVER, "玩家榜-等级榜");
            } else if (getTab() == PLAYERBILLBOARD_VIDEO) {
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.DISCOVER, "玩家榜-视频榜");
            }
        }
    }

    public View getHeaderView() {
        if (mHeaderView == null) {
            mHeaderView = inflater.inflate(R.layout.header_playerbillboard, null);
            rank = (TextView) mHeaderView.findViewById(R.id.playerbillboard_rank);
            container = (RelativeLayout) mHeaderView.findViewById(R.id.container);
            setTextViewText(rank, "");
            container.setVisibility(View.GONE);
//			setListViewLayoutParams(mHeaderView, dp2px(24));
        }
        return mHeaderView;
    }

    private void refreshHeaderView(RankingMemberRankingEntity data) {

        if (isLogin() && data != null) {
            String myRanking = data.getData().getMyRanking();
            if (!StringUtil.isNull(myRanking)) {
                container.setVisibility(View.VISIBLE);
                setTextViewText(rank, "您当前的排名是：" + myRanking + " 快去发布视频提高排名吧~");
                return;
            }
        }
        setTextViewText(rank, "");
        container.setVisibility(View.GONE);
    }

    private PullToRefreshListView pullToRefreshListView;
    private ListView listView;
    private PlayerBillboardAdapter adapter;
    private List<Member> data;

    private int page = 1;
    private boolean isRefreshing;

    @Override
    protected void initContentView(View view) {

        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pulltorefresh);
        pullToRefreshListView.setMode(Mode.BOTH);
        listView = pullToRefreshListView.getRefreshableView();
        listView.addHeaderView(getHeaderView());

        data = new ArrayList<Member>();
        adapter = new PlayerBillboardAdapter(getActivity(), getTab(), data);
        listView.setAdapter(adapter);

        pullToRefreshListView.setOnRefreshListener(this);

        if (getTab() == PLAYERBILLBOARD_FANS) {// 粉丝榜

            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    onPullDownToRefresh(pullToRefreshListView);
                }
            }, 0);

        } else if (getTab() == PLAYERBILLBOARD_RANK) {// 等级榜

            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    onPullDownToRefresh(pullToRefreshListView);
                }
            }, 200);

        } else if (getTab() == PLAYERBILLBOARD_VIDEO) {// 视频榜

            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    onPullDownToRefresh(pullToRefreshListView);
                }
            }, 400);
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

        if (!isRefreshing) {
            page = 1;
            isRefreshing = true;
            pullToRefreshListView.setRefreshing();
            if (getTab() == PLAYERBILLBOARD_FANS) {
                // 玩家榜--粉丝榜
                DataManager.rankingMemberRankingFans(getMember_id(), page);
            } else if (getTab() == PLAYERBILLBOARD_RANK) {
                // 玩家榜--等级榜
                DataManager.rankingMemberRankingRank(getMember_id(), page);
            } else if (getTab() == PLAYERBILLBOARD_VIDEO) {
                // 玩家榜--视频榜
                DataManager.rankingMemberRankingVideo(getMember_id(), page);
            }
            onRefreshCompleteDelayed(PullToRefreshActivity.TIME_REFRESH_LONG);
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

        if (!isRefreshing) {
            isRefreshing = true;
            pullToRefreshListView.setRefreshing();
            if (getTab() == PLAYERBILLBOARD_FANS) {
                // 玩家榜--粉丝榜
                DataManager.rankingMemberRankingFans(getMember_id(), page);
            } else if (getTab() == PLAYERBILLBOARD_RANK) {
                // 玩家榜--等级榜
                DataManager.rankingMemberRankingRank(getMember_id(), page);
            } else if (getTab() == PLAYERBILLBOARD_VIDEO) {
                // 玩家榜--视频榜
                DataManager.rankingMemberRankingVideo(getMember_id(), page);
            }
            onRefreshCompleteDelayed(PullToRefreshActivity.TIME_REFRESH_LONG);
        }
    }

    /**
     * 回调：玩家榜--粉丝榜
     */
    public void onEventMainThread(RankingMemberRankingFansEntity event) {

        if (getTab() == PLAYERBILLBOARD_FANS && event != null) {
            refreshData(event);
        }
    }

    /**
     * 回调：玩家榜--等级榜
     */
    public void onEventMainThread(RankingMemberRankingRankEntity event) {

        if (getTab() == PLAYERBILLBOARD_RANK && event != null) {
            refreshData(event);
        }
    }

    /**
     * 回调：玩家榜--视频榜
     */
    public void onEventMainThread(RankingMemberRankingVideoEntity event) {

        if (getTab() == PLAYERBILLBOARD_VIDEO && event != null) {
            refreshData(event);
        }
    }

    /**
     * 回调：
     */
    private void refreshData(RankingMemberRankingEntity event) {
        if (event.getData().getList().size() > 0) {
            refreshHeaderView(event);
            if (page == 1) {
                data.clear();
            }
            data.addAll(event.getData().getList());
            adapter.notifyDataSetChanged();
            ++page;
        }
        isRefreshing = false;
        onRefreshComplete();
    }
}
