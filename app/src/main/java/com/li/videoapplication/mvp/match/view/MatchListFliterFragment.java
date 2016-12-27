package com.li.videoapplication.mvp.match.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.EventManager;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.mvp.adapter.MatchListFliterAdapter;
import com.li.videoapplication.mvp.match.MatchContract;
import com.li.videoapplication.mvp.match.presenter.MatchPresenter;
import com.li.videoapplication.tools.AnimationHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 碎片：赛事列表 游戏类型筛选
 */
public class MatchListFliterFragment extends TBaseFragment implements MatchContract.IMatchListFliterView,
        View.OnClickListener, View.OnTouchListener, RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.fliter_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.fliter_type_official)
    RadioButton official;
    @BindView(R.id.fliter_type_invite)
    RadioButton invite;
    @BindView(R.id.fliter_type_anchor)
    RadioButton anchor;
    @BindView(R.id.fliter_topbar_left)
    TextView tv_topbar_left;
    @BindView(R.id.fliter_topbar_middle)
    TextView tv_topbar_mid;

    private MatchContract.IMatchPresenter presenter;
    private MatchListFliterAdapter adapter;
    private List<Game> data;
    //选中游戏类型
    public static SparseBooleanArray selectedPos = new SparseBooleanArray();

    /**
     * 隐藏：筛选
     */
    private void hideMatchListFliterFragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.activity_slide_in_right, R.anim.activity_slide_out_right);
        ft.hide(this).commit();
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_matchlist_fliter;
    }

    @Override
    protected void initContentView(View view) {

        initView(view);

        initAdapter();

        loadData();

        addOnClickListener(view);
    }

    private void initView(View view) {
        view.setOnTouchListener(this);//防止点击上一层fragment
    }

    private void initAdapter() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        presenter = MatchPresenter.getInstance();
        presenter.setMatchListFliterView(this);

        data = new ArrayList<>();
        adapter = new MatchListFliterAdapter(data);

        recyclerView.setAdapter(adapter);
    }

    private void loadData() {
        //游戏类型
        presenter.getGameCate();
    }

    private void addOnClickListener(View view) {
        view.findViewById(R.id.fliter_default).setOnClickListener(this);
        view.findViewById(R.id.fliter_confirm).setOnClickListener(this);
        view.findViewById(R.id.fliter_topbar_right).setOnClickListener(this);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.fliter_type_radiogroup);
        radioGroup.setOnCheckedChangeListener(this);

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int pos) {
                Game game = (Game) adapter.getItem(pos);

                if (isSelected(pos)) {
                    selectedPos.put(pos, false);
                } else {
                    selectedPos.put(pos, true);
                }
                adapter.notifyItemChanged(pos);//选中或取消点击的游戏

                if (game.getId().equals("0")) { //点击的是[全部游戏]
                    resetGameType();//重置所有选中，即单选[全部游戏]
                } else { //点击的是其他游戏
                    if (isSelected(0)) { //将[全部游戏]置为未选中
                        selectedPos.put(0, false);
                        adapter.notifyItemChanged(0);
                    }
                }

                refreshTopBar(false);
            }
        });
    }

    private void refreshTopBar(boolean isDefault) {
        if (isDefault) {
            tv_topbar_left.setText("默认：");
            tv_topbar_mid.setText("全部游戏 | 官方淘汰赛");
        } else {
            tv_topbar_left.setText("已选择：");
            String s = getGameSelectedNames() + getMatchTypeSelectedNames();
            tv_topbar_mid.setText(s);
        }
    }

    private boolean isSelected(int pos) {
        return selectedPos.get(pos);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        refreshTopBar(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fliter_default:
                resetGameType();
                resetMatchType();
                refreshTopBar(true);
                break;
            case R.id.fliter_confirm:
                EventManager.postMatchListFliterEvent(getGameSelectedId(), getGameSelectedNames(),
                        getMatchTypeSelectedId(), getMatchTypeSelectedNames());
                break;
            case R.id.fliter_topbar_right:
                hideMatchListFliterFragment();
                break;
        }
    }

    //获得游戏选中id
    private String getGameSelectedId() {
        StringBuilder gameIds = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            if (isSelected(i)) {
                gameIds.append(data.get(i).getId());
                gameIds.append(",");
            }
        }
        gameIds.deleteCharAt(gameIds.length()-1);//删除最后多余的逗号
        return gameIds.toString();
    }

    //获得游戏选中name
    private String getGameSelectedNames() {
        StringBuilder selectedGamesName = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            if (isSelected(i)) {
                selectedGamesName.append(data.get(i).getName());
                selectedGamesName.append(" | ");
            }
        }
        return selectedGamesName.toString();
    }

    //获得赛事类型选中条目的结果id
    private int getMatchTypeSelectedId() {
        if (official.isChecked())
            return GameMatchFragment.MATCH_ALL;
        else if (invite.isChecked())
            return GameMatchFragment.MATCH_INVITATION;
        else if (anchor.isChecked())
            return GameMatchFragment.MATCH_ANCHOR;
        else
            return GameMatchFragment.MATCH_ALL;
    }

    //获得赛事类型选中条目的结果name
    private String getMatchTypeSelectedNames() {
        switch (getMatchTypeSelectedId()) {
            case GameMatchFragment.MATCH_ALL:
                return "官方淘汰赛";
            case GameMatchFragment.MATCH_INVITATION:
                return "邀请淘汰赛";
            case GameMatchFragment.MATCH_ANCHOR:
                return "主播淘汰赛";
            default:
                return "官方淘汰赛";
        }
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    /**
     * 回调：赛事筛选
     */
    @Override
    public void refreshGameCateData(List<Game> data) {
        this.data = data;
        resetGameType();
    }

    //默认选中【0:全部游戏】
    private void resetGameType() {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId().equals("0")) {
                selectedPos.put(i, true);
            } else {
                selectedPos.put(i, false);
            }
        }
        adapter.setNewData(data);
    }

    //默认选中【官方淘汰赛】
    private void resetMatchType() {
        official.setChecked(true);
        invite.setChecked(false);
        anchor.setChecked(false);
    }

}
