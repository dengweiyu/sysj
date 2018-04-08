package com.li.videoapplication.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.event.LoginEvent;
import com.li.videoapplication.data.model.event.LogoutEvent;
import com.li.videoapplication.data.model.response.GroupList2Entity;
import com.li.videoapplication.data.network.RequestConstant;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.PullToRefreshHepler;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.activity.MainActivity;
import com.li.videoapplication.ui.adapter.GroupListAdapter;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.rong.eventbus.EventBus;

/**
 * Created by y on 2018/3/20.
 */

public class GameLazyColumnFragment extends TBaseFragment implements PullToRefreshBase.OnRefreshListener2<ListView> {
    private MainActivity mActivity;

    private boolean isInit = false;

    private boolean isGetData = false;

    public String sort = RequestConstant.GAMELIST_SORT_HOT;

    public List<Game> tData;
    private PullToRefreshListView mPullToRefreshListView;
    public static final String INTENT_BOOLEAN_LAZY_LOAD = "intent_boolean_lazy_load";
    private boolean mIsNeedLoadData = false;
    private boolean isLazyLoad = false;
    private String mHeaderId;
    private int mPage = 1;
    private GroupListAdapter mAdapter;

    private ListView mListView;

    public static GameLazyColumnFragment newIstance(String headerId, boolean isNeedLoadData, int delayTime, boolean isLazyLoad) {
        final GameLazyColumnFragment fragment = new GameLazyColumnFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_need_load_data", isNeedLoadData);
        bundle.putString("header_id", headerId);
        Log.i("GameLazyColumnFragment", headerId);
        bundle.putBoolean(GameLazyColumnFragment.INTENT_BOOLEAN_LAZY_LOAD, isLazyLoad);
        fragment.setArguments(bundle);

        return fragment;
    }

    private void loadData() {

        if (NetUtil.isConnect()) {
            DataManager.groupList2(mPage, mHeaderId, getMember_id());
            Log.i(tag, "page" + mPage + "header_id" + mHeaderId + "member_id" + getMember_id());

        } else {
            Log.i("when no connect", mHeaderId);
            if (mHeaderId.equals("9")) {
                EventBus.getDefault().post(PreferencesHepler.getInstance().getGameEntity());
            } else {
                ToastHelper.s(R.string.net_disable);
            }
        }

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (mActivity == null) {
            if (activity instanceof MainActivity) {
                mActivity = (MainActivity) activity;
            }
        }
    }


    @Override
    protected int getCreateView() {
        return R.layout.fragment_game_type;
    }

    @Override
    protected void initContentView(View view) {
        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.game_pulltorefresh);
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

        mListView = mPullToRefreshListView.getRefreshableView();
        mListView.setPadding(0, 0, 0, dp2px(46));

        if (!isInit) {
            layzLoadView();
        }
    }

    private void layzLoadView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mIsNeedLoadData = bundle.getBoolean("is_need_load_data", false);
            mHeaderId = bundle.getString("header_id");
            isLazyLoad = bundle.getBoolean(INTENT_BOOLEAN_LAZY_LOAD, isLazyLoad);
        } else {
            Log.e("bundle", "为空");
        }
        Log.i(tag, "GameLazy加载view" + mHeaderId + mIsNeedLoadData + isLazyLoad);

        loadView();
        isInit = true;

    }


    private void loadView() {

        if (tData == null) {
            tData = new ArrayList<>();
        }

        if (mAdapter == null) {
            mAdapter = new GroupListAdapter(getContext(), tData);
            mListView.setAdapter(mAdapter);
        }


        mPullToRefreshListView.setOnRefreshListener(this);

        if (!mIsNeedLoadData) {

            loadData();
        }
    }


    @Override
    protected IPullToRefresh getPullToRefresh() {
        return mPullToRefreshListView;
    }

    /**
     * 事件：登录
     */
    public void onEventMainThread(LoginEvent event) {
        if (event != null) {
            mPage = 1;
            // 最热，最新游戏列表,请求只是为了刷新关注按钮状态
            DataManager.gameList(mPage, getMember_id(), sort);

        }
    }

    /**
     * 事件：注销
     */
    public void onEventMainThread(LogoutEvent event) {
        if (event != null) {
            mPage = 1;
            // 最热，最新游戏列表,请求只是为了刷新关注按钮状态
            DataManager.gameList(mPage, getMember_id(), sort);
        }
    }

    /**
     * 回调:游戏圈子列表
     */
    public void onEventMainThread(GroupList2Entity event) {
        Map<String, Object> extra = event.getExtra();
        if (extra == null) {
            return;
        }
        if (!mHeaderId.equals(extra.get("group_type_id"))) {
            return;
        }

        Log.i(tag, "Result" + event.getMsg() + "Code" + event.getCode());
        if (event != null) {
            if (event.isResult()) {
                if (null != event.getAData().getList() && event.getAData().getList().size() > 0) {
                    if (mPage == 1) {
                        tData.clear();
                    }
                    tData.addAll(event.getAData().getList());
                    Log.i("event", "" + event.getAData().getList());

                    mAdapter.notifyDataSetChanged();

                }
                onRefreshComplete();
            }
            if (mHeaderId.equals("1")) {
                PreferencesHepler.getInstance().saveGameEntity(event);
            }
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        mPage = 1;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DataManager.groupList2(mPage, mHeaderId, getMember_id());
                onRefreshComplete();
            }
        }, 200);

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        ++mPage;
        PullToRefreshHepler.setLastUpdatedLabel(refreshView);
        onRefreshCompleteDelayed(PullToRefreshActivity.TIME_REFRESH_SHORT);
        DataManager.groupList2(mPage, mHeaderId, getMember_id());
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {

        return super.onCreateAnimation(transit, enter, nextAnim);


    }


}
