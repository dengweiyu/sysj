package com.li.videoapplication.ui.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.adapter.DailyAdapter;
import com.li.videoapplication.ui.dialog.Jump2Dialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 碎片：每日奖励
 */
public class DailyRewardFragment extends TBaseFragment implements View.OnClickListener {

    private RecyclerView dailyRecyclerView,noviceRecyclerView;
    private DailyAdapter dailyAdapter;
    private DailyAdapter noviceAdapter;
    private List<Currency> dailyDatas, noviceDatas;

    @Override
    protected int getCreateView() {
        return R.layout.fragment_dailyreward;
    }

    @Override
    protected void initContentView(View view) {

         dailyRecyclerView = (RecyclerView) view.findViewById(R.id.daily_daily);
         noviceRecyclerView = (RecyclerView) view.findViewById(R.id.daily_novice);

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

        addOnClickListener();
    }

    private void addOnClickListener() {
        dailyRecyclerView.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                Currency item = (Currency) adapter.getItem(position);
                setJump(item);
            }
        });

        noviceRecyclerView.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                Currency item = (Currency) adapter.getItem(position);
                setJump(item);
            }
        });
    }

    private void setJump(Currency item) {
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
            case 9://9=>分享视频到玩家广场任务
                DialogManager.showSharedToSquareDialog(getActivity(), "分享至手游视界即有机会获得飞磨豆奖励，是否立即分享视频？", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()){
                            case R.id.tv_confirm_dialog_no:
                                ActivityManager.startVideoMangerActivity(getActivity());
                                break;
                        }
                    }
                });
                break;
        }
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
