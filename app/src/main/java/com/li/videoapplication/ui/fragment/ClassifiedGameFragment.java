package com.li.videoapplication.ui.fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.entity.GroupType;
import com.li.videoapplication.data.model.entity.HomeDto;
import com.li.videoapplication.data.model.event.LoginEvent;
import com.li.videoapplication.data.model.event.LogoutEvent;
import com.li.videoapplication.data.model.response.GameListEntity;
import com.li.videoapplication.data.model.response.GroupAttentionGroupEntity;
import com.li.videoapplication.data.model.response.GroupType2Entity;
import com.li.videoapplication.data.network.RequestConstant;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.PullToRefreshHepler;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.adapter.ClassifiedAdapter;
import com.li.videoapplication.ui.adapter.ClassifiedGameAdapter;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.views.GridViewY1;
import com.li.videoapplication.views.bubblelayout.BubbleLayout;
import com.li.videoapplication.views.bubblelayout.BubblePopupHelper;
import io.rong.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import y.com.sqlitesdk.framework.IfeimoSqliteSdk;

/**
 * 碎片：游戏分类
 */
public class ClassifiedGameFragment extends TBaseFragment implements OnRefreshListener2<ListView>, OnClickListener {

    private PullToRefreshListView mPullToRefreshListView;
    private ListView mListView;
    private ClassifiedGameAdapter adapter;
    private List<Game> data;

    private int page = 1;
    public String sort = RequestConstant.GAMELIST_SORT_HOT;

    private View mHeaderView;
    private GridViewY1 mGridView;
    private ClassifiedAdapter headerAdapter;
    private List<GroupType> headerData;
    private View mTitleView;
    private TextView show;

    @Override
    protected int getCreateView() {
        return R.layout.fragment_classifiedgame;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected IPullToRefresh getPullToRefresh() {
        return mPullToRefreshListView;
    }

    @Override
    protected void initContentView(View view) {

        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pulltorefresh);
        mPullToRefreshListView.setMode(Mode.BOTH);
        mListView = mPullToRefreshListView.getRefreshableView();

        data = new ArrayList<>();
        adapter = new ClassifiedGameAdapter(getActivity(), data, this);

        mListView.addHeaderView(getHeaderView());
        mListView.addHeaderView(getTitleView());
        showHeaderView(true);
        mListView.setAdapter(adapter);

        mPullToRefreshListView.setOnRefreshListener(this);

        if (sort.equals(RequestConstant.GAMELIST_SORT_TIME)) {
            setTextViewText(show, R.string.classifiedgame_new);
        } else if (sort.equals(RequestConstant.GAMELIST_SORT_HOT)) {
            setTextViewText(show, R.string.classifiedgame_hot);
        }

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                onPullDownToRefresh(mPullToRefreshListView);
            }
        }, AppConstant.TIME.GAME_CLASSIFIED);
    }

    private View getHeaderView() {

        if (mHeaderView == null) {
            mHeaderView = inflater.inflate(R.layout.header_classifiedgame_classified, null);
            ListView.LayoutParams params = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.WRAP_CONTENT);
            mHeaderView.setLayoutParams(params);
            mGridView = (GridViewY1) mHeaderView.findViewById(R.id.gridview);
            headerData = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                headerData.add(new GroupType());
            }
            headerAdapter = new ClassifiedAdapter(getActivity(), headerData);
            mGridView.setAdapter(headerAdapter);
        }
        return mHeaderView;
    }

    private View getTitleView() {

        if (mTitleView == null) {
            mTitleView = View.inflate(getActivity(), R.layout.header_classifiedgame_title, null);
            show = (TextView) mTitleView.findViewById(R.id.classifiedgame_title_text);
            mTitleView.findViewById(R.id.classifiedgame_title_pop).setOnClickListener(this);
            setLayoutParams(mTitleView, 22);
        }
        return mTitleView;
    }

    private void setLayoutParams(View view, int height) {
        if (view != null) {
            int pxHeight = ScreenUtil.dp2px(height);
            ListView.LayoutParams params = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, pxHeight);
            view.setLayoutParams(params);
        }
    }

    private void showHeaderView(boolean isShow) {
        if (isShow) {
            mGridView.setVisibility(View.VISIBLE);
        } else {
            mGridView.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

        page = 1;
        onPullUpToRefresh(refreshView);

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // 游戏圈子类型
                DataManager.groupType2();
            }
        }, 200);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        PullToRefreshHepler.setLastUpdatedLabel(refreshView);
        onRefreshCompleteDelayed(PullToRefreshActivity.TIME_REFRESH_SHORT);

        // 最热，最新游戏列表
        DataManager.gameList(page, getMember_id(), sort);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.classifiedgame_title_pop:
                DialogManager.showClassifiedGameDialog(getActivity(), sort, this, this);
                break;

            case R.id.classifiedgame_new:
                setTextViewText(show, R.string.classifiedgame_new);
                sort = RequestConstant.GAMELIST_SORT_TIME;
                onPullDownToRefresh(mPullToRefreshListView);
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.GAME, "找游戏-最新游戏");
                break;

            case R.id.classifiedgame_hot:
                setTextViewText(show, R.string.classifiedgame_hot);
                sort = RequestConstant.GAMELIST_SORT_HOT;
                onPullDownToRefresh(mPullToRefreshListView);
                break;
        }
    }

    /**
     * 圈子类型回调
     */
    public void onEventMainThread(GroupType2Entity event) {

        if (event != null) {
            boolean result = event.isResult();
            if (result) {
                headerData.clear();
                headerData.addAll(event.getData());
                headerAdapter.notifyDataSetChanged();
                showHeaderView(true);
                onRefreshComplete();
            }
        }
    }

    /**
     * 事件：登录
     */
    public void onEventMainThread(LoginEvent event) {
        if (event != null) {
            page = 1;
            // 最热，最新游戏列表,请求只是为了刷新关注按钮状态
            DataManager.gameList(page, getMember_id(), sort);
        }
    }

    /**
     * 事件：注销
     */
    public void onEventMainThread(LogoutEvent event) {
        if (event != null) {
            page = 1;
            // 最热，最新游戏列表,请求只是为了刷新关注按钮状态
            DataManager.gameList(page, getMember_id(), sort);
        }
    }

    /**
     * 最热，最新游戏列表回调
     */
    public void onEventMainThread(GameListEntity event) {

        if (event != null) {
            final boolean result = event.isResult();
            if (result) {
                if (page == 1) {
                    data.clear();
                }
               /* LinkedHashMap<String ,Game> map = new LinkedHashMap();
                for (Game game:
                        event.getData().getList()) {
                    map.put(game.getGame_id(),game);
                }
                Iterator<Map.Entry<String,Game>> iterator = map.entrySet().iterator();
                while(iterator.hasNext()){
                    Map.Entry<String,Game> entity = iterator.next();

                    data.add(entity.getValue());
                }*/
               //去除重复
                data.addAll(Lists.newArrayList(Iterables.filter(event.getData().getList(), new Predicate<Game>() {
                    @Override
                    public boolean apply(Game input) {
                        for (Game g:
                             data) {
                            if (input.getGame_id().equals(g.getGame_id())){
                                return false;
                            }
                        }
                        return true;
                    }
                })));

                adapter.notifyDataSetChanged();
                if (event.getData().getList().size() > 0) {
                    ++page;
                }
                onRefreshComplete();
            }
        }
    }

    /**
     * 回调：关注圈子201
     */
    public void onEventMainThread(GroupAttentionGroupEntity event) {

        if (event != null && event.isResult()) {
            Log.d(tag, event.getMsg());
//                showToastShort(event.getMsg());
        }
        //关注发生改变 刷新列表
        page = 1;
        DataManager.gameList(page, getMember_id(), sort);

    }
}
