package com.li.videoapplication.ui.fragment;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.ui.adapter.DailyAdapter;
import com.li.videoapplication.utils.TextUtil;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * 碎片：荣誉奖励
 */

public class HonorRewardFragment extends TBaseFragment implements View.OnClickListener {

    private List<Currency> honorDatas;
    private DailyAdapter adapter;

    @Override
    protected int getCreateView() {
        return R.layout.fragment_honorreward;
    }

    @Override
    protected void initContentView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.honor_recycle);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(manager);
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        honorDatas = new ArrayList<>();
        adapter = new DailyAdapter(honorDatas);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    public void setHonorData(List<Currency> honorData){
        honorDatas.clear();
        honorDatas.addAll(honorData);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

    }
}
