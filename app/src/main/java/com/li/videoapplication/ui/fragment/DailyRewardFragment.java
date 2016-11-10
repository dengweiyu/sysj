package com.li.videoapplication.ui.fragment;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.ui.adapter.DailyAdapter;
import com.li.videoapplication.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.ScrollViewOverScrollDecorAdapter;

/**
 * 碎片：每日奖励
 */
public class DailyRewardFragment extends TBaseFragment implements View.OnClickListener {

    private DailyAdapter dailyAdapter;
    private DailyAdapter noviceAdapter;
    private List<Currency> dailyDatas, noviceDatas;

    @Override
    protected int getCreateView() {
        return R.layout.fragment_dailyreward;
    }

    @Override
    protected void initContentView(View view) {
        NestedScrollView root = (NestedScrollView) view.findViewById(R.id.dailyreward_root);

        RecyclerView dailyRecyclerView = (RecyclerView) view.findViewById(R.id.daily_daily);
        RecyclerView noviceRecyclerView = (RecyclerView) view.findViewById(R.id.daily_novice);

        dailyRecyclerView.setHasFixedSize(true);
        noviceRecyclerView.setHasFixedSize(true);

        GridLayoutManager manager1 = new GridLayoutManager(getActivity(), 2);
        GridLayoutManager manager2 = new GridLayoutManager(getActivity(), 2);

        dailyRecyclerView.setLayoutManager(manager1);
        noviceRecyclerView.setLayoutManager(manager2);

        dailyDatas = new ArrayList<>();
        dailyAdapter = new DailyAdapter(dailyDatas);
        dailyRecyclerView.setAdapter(dailyAdapter);

        noviceDatas = new ArrayList<>();
        noviceAdapter = new DailyAdapter(noviceDatas);
        noviceAdapter.coverShalow(true);
        noviceRecyclerView.setAdapter(noviceAdapter);
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    public void setDailyData(List<Currency> dailyData) {
        Log.d(tag, "setDailyData: ");
        dailyDatas.clear();
        dailyDatas.addAll(dailyData);
        dailyAdapter.notifyDataSetChanged();
    }

    public void setNoviceData(List<Currency> noviceData) {
        Log.d(tag, "setNoviceData: ");
        noviceDatas.clear();
        noviceDatas.addAll(noviceData);
        noviceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

    }
}
