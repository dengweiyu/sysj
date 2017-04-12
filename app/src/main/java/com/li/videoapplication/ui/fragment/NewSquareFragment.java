package com.li.videoapplication.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.entity.VideoImageGroup;
import com.li.videoapplication.data.model.response.IndexIndexMore204HotEntity;
import com.li.videoapplication.data.model.response.IndexIndexMore204NewEntity;
import com.li.videoapplication.data.model.response.SquareListHotEntity;
import com.li.videoapplication.data.model.response.SquareListNewEntity;
import com.li.videoapplication.data.model.response.VideoCollect2Entity;
import com.li.videoapplication.data.model.response.VideoFlower2Entity;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.adapter.GroupDetailVideoAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 碎片：广场（最新，热门）；首页更多（最新，最热）
 */
public class NewSquareFragment extends TBaseFragment implements OnRefreshListener2<ListView> {

    public static final int SQUARE_NEW = 1;
    public static final int SQUARE_HOT = 2;
    public static final int HOMEMORE_NEW = 3;
    public static final int HOMEMORE_HOT = 4;

    public synchronized static NewSquareFragment newInstance(int square) {
        return newInstance(square, null);
    }

    public synchronized static NewSquareFragment newInstance(int square, VideoImageGroup group) {
        NewSquareFragment fragment = new NewSquareFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("square", square);
        if (group != null) {
            bundle.putSerializable("group", group);
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (getSquare() == SQUARE_HOT) {
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.DISCOVER, "广场-热门");
            } else if (getSquare() == HOMEMORE_HOT) {
                UmengAnalyticsHelper.onMainMoreHotEvent(getActivity(), getGroup().getMore_mark());
            }
        }
    }

    private int square;

    private VideoImageGroup group;

    public int getSquare() {
        if (square == 0) {
            try {
                square = getArguments().getInt("square");
                group = (VideoImageGroup) getArguments().getSerializable("group");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (square == SQUARE_NEW ||
                    square == SQUARE_HOT ||
                    square == HOMEMORE_HOT ||
                    square == HOMEMORE_NEW) {

            } else {
                square = SQUARE_NEW;
            }
        }
        return square;
    }

    public VideoImageGroup getGroup() {
        if (group == null) {
            getSquare();
        }
        return group;
    }

    private PullToRefreshListView pullToRefreshListView;
    private ListView listView;
    private GroupDetailVideoAdapter adapter;
    private List<VideoImage> data;

    private int page = 1;

    @Override
    protected int getCreateView() {
        return R.layout.fragment_newsquare;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected IPullToRefresh getPullToRefresh() {
        return pullToRefreshListView;
    }

    @Override
    protected void initContentView(View view) {

        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pulltorefresh);
        pullToRefreshListView.setMode(Mode.BOTH);
        listView = pullToRefreshListView.getRefreshableView();

        data = new ArrayList<>();
        adapter = new GroupDetailVideoAdapter(getActivity(), data);
        listView.setAdapter(adapter);
        pullToRefreshListView.setOnRefreshListener(this);

        if (getSquare() == SQUARE_NEW) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onPullDownToRefresh(pullToRefreshListView);
                }
            }, AppConstant.TIME.SQUARE_NEW);
        } else if (getSquare() == SQUARE_HOT) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onPullDownToRefresh(pullToRefreshListView);
                }
            }, AppConstant.TIME.SQUARE_HOT);
        }

        if (getSquare() == HOMEMORE_NEW) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onPullDownToRefresh(pullToRefreshListView);
                }
            }, AppConstant.TIME.SQUARE_NEW);
        } else if (getSquare() == HOMEMORE_HOT) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onPullDownToRefresh(pullToRefreshListView);
                }
            }, AppConstant.TIME.SQUARE_HOT);
        }


    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

        page = 1;
        onPullUpToRefresh(refreshView);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

        if (getSquare() == SQUARE_NEW) {
            // 广场列表（最新）
            DataManager.squareListNew(getMember_id(), page);
        } else if (getSquare() == SQUARE_HOT) {
            // 广场列表（最热）
            DataManager.squareListHot(getMember_id(), page);
        } else if (getSquare() == HOMEMORE_NEW) {
            // 首页更多（最新）
            DataManager.indexIndexMore217New(getGroup().getMore_mark(), getMember_id(), page);
        } else if (getSquare() == HOMEMORE_HOT) {
            // 首页更多（最热）
            DataManager.indexIndexMore217Hot(getGroup().getMore_mark(), getMember_id(), page);
        }
        onRefreshCompleteDelayed(PullToRefreshActivity.TIME_REFRESH_SHORT);

        if (getSquare() == HOMEMORE_HOT || getSquare() == HOMEMORE_NEW)
            adapter.setHomeMoreLocation(getGroup().getMore_mark(), getSquare() == HOMEMORE_NEW);
    }

    /**
     * 回调:广场列表（最新）
     */
    public void onEventMainThread(SquareListNewEntity event) {

        if (getSquare() == SQUARE_NEW && event != null) {
            if (event.isResult()) {
                if (event.getData().getList().size() > 0) {
                    refreshDta(event.getData().getList());
                }
            }
            onRefreshComplete();
        }
    }

    /**
     * 回调:广场列表（最热）
     */
    public void onEventMainThread(SquareListHotEntity event) {

        if (getSquare() == SQUARE_HOT && event != null) {
            if (event.isResult()) {
                if (event.getData().getList().size() > 0) {
                    refreshDta(event.getData().getList());
                }
            }
            onRefreshComplete();
        }
    }

    /**
     * 回调:首页更多（最新）
     */
    public void onEventMainThread(IndexIndexMore204NewEntity event) {

        if (getSquare() == HOMEMORE_NEW && event != null) {
            if (event.isResult()) {
                if (event.getData().getList().size() > 0) {
                    refreshDta(event.getData().getList());
                }
            }
            onRefreshComplete();
        }
    }

    /**
     * 回调:首页更多（最热）
     */
    public void onEventMainThread(IndexIndexMore204HotEntity event) {

        if (getSquare() == HOMEMORE_HOT && event != null) {
            if (event.isResult()) {
                if (event.getData().getList().size() > 0) {
                    refreshDta(event.getData().getList());
                }
            }
            onRefreshComplete();
        }
    }

    private void refreshDta(List<VideoImage> list) {
        if (page == 1) {
            data.clear();
        }
        data.addAll(list);
        adapter.notifyDataSetChanged();
        ++page;
    }

    /**
     * 回调：视频点赞
     */
    public void onEventMainThread(VideoFlower2Entity event) {

        if (getSquare() == HOMEMORE_HOT && event != null) {
            showToastShort(event.getMsg());
        } else if (getSquare() == HOMEMORE_NEW && event != null) {
            showToastShort(event.getMsg());
        }
    }

    /**
     * 回调：视频收藏
     */
    public void onEventMainThread(VideoCollect2Entity event) {

        if (getSquare() == HOMEMORE_HOT && event != null) {
            showToastShort(event.getMsg());
        } else if (getSquare() == HOMEMORE_NEW && event != null) {
            showToastShort(event.getMsg());
        }
    }
}
