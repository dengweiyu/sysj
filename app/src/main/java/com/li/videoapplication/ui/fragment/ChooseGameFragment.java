package com.li.videoapplication.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.EventManager;
import com.li.videoapplication.data.model.entity.Associate;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.response.Associate201Entity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.mvp.adapter.PlayerBillboardAdapter;
import com.li.videoapplication.mvp.animation.RecyclerViewAnim;
import com.li.videoapplication.mvp.billboard.presenter.BillboardPresenter;
import com.li.videoapplication.mvp.billboard.view.PlayerBillboardFragment;
import com.li.videoapplication.ui.activity.SearchGameActivity;
import com.li.videoapplication.ui.adapter.ChooseGameAdapter;
import com.li.videoapplication.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 碎片：查找游戏--最近游戏，热门游戏
 */
public class ChooseGameFragment extends TBaseFragment {
    public static final int ChooseGame_HISTORY = 1;
    public static final int ChooseGame_HOT = 2;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public ChooseGameAdapter adapter;

    public static ChooseGameFragment newInstance(int tab) {
        ChooseGameFragment fragment = new ChooseGameFragment();
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
                tab = ChooseGame_HISTORY;
            }
        }
        return tab;
    }

    @Override
    protected int getCreateView() {
        return R.layout.refresh_recyclerview;
    }

    @Override
    protected void initContentView(View view) {

        initAdapter();

        loadData();

        addOnClickListener();
    }

    public void loadData() {
        if (getTab() == ChooseGame_HISTORY) {
            refreshHistoty();
        } else {
            // 搜索联想词201
            DataManager.associate210("game", "");
        }
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    private void initAdapter() {
        swipeRefreshLayout.setEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Associate> data = new ArrayList<>();

        adapter = new ChooseGameAdapter(data);
        adapter.openLoadAnimation();

        recyclerView.setAdapter(adapter);
    }

    public void refreshHistoty() {
        List<Associate> list = PreferencesHepler.getInstance().getAssociate201List();
        if (list != null && list.size() > 0) {
            adapter.setNewData(list);
        }
    }

    private void addOnClickListener() {
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                Associate record = (Associate) adapter.getItem(position);
                // 保存本地
                PreferencesHepler.getInstance().addAssociate201List(record);
                getActivity().finish();
                EventManager.postSearchGame2VideoShareEvent(record);
            }
        });
    }

    /**
     * 回调：搜索联想词201
     */
    public void onEventMainThread(Associate201Entity event) {
        if (event != null && event.isResult()) {

            if (event.getData().size() > 0) {
                if (getTab() == ChooseGame_HOT)
                    adapter.setNewData(event.getData());
            }
        }
    }
}
