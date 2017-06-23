package com.li.videoapplication.ui.fragment;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.adapter.MatchAdapter;
import com.li.videoapplication.ui.dialog.Jump2Dialog;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 碎片：赛事奖励
 */

public class MatchRewardFragment extends TBaseFragment {

    private List<Currency> matchDatas;
    private MatchAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected int getCreateView() {
        return R.layout.fragment_matchreward;
    }

    @Override
    protected void initContentView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.match_recycle);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(manager);

        matchDatas = new ArrayList<>();
        adapter = new MatchAdapter(matchDatas);
        recyclerView.setAdapter(adapter);

        addOnClickListener();
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    public void setMatchData(List<Currency> matchData){
        matchDatas.clear();
        matchDatas.addAll(matchData);
        adapter.notifyDataSetChanged();
    }

    private void addOnClickListener() {
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                Currency item = (Currency) adapter.getItem(position);
                switch (item.getJumpStatus()){
                    case 1://1=>直接跳转到视屏管理
                        ActivityManager.startVideoMangerActivity(getActivity());
                        break;
                    case 2://2=>跳回首页
                        DialogManager.showJump2HomeDialog(getActivity(), item.getName());
                        break;
                    case 3://3=>跳转到排行榜
                        DialogManager.showJump2Dialog(getActivity(), Jump2Dialog.TO_BILLBOARD_VIDEO);
                        break;
                    case 4://4=>跳转到赛事列表
                        DialogManager.showJump2Dialog(getActivity(), Jump2Dialog.TO_MATCH);
                        break;
                    case 5://5=>跳转到玩家推荐榜
                        DialogManager.showJump2Dialog(getActivity(), Jump2Dialog.TO_BILLBOARD_PLAYER);
                        break;
                    case 6://6=>跳转到商城
                        DialogManager.showJump2Dialog(getActivity(), Jump2Dialog.TO_MALL);
                        break;
                    case 7://7=>间接跳转到视屏管理
                        DialogManager.showJump2Dialog(getActivity(), Jump2Dialog.TO_UPLOADVIDEO);
                        break;
                    case 8://8=>每日登陆
                        DialogManager.showLogInTaskDoneDialog(getActivity());
                        break;
                }
            }
        });
    }

}
