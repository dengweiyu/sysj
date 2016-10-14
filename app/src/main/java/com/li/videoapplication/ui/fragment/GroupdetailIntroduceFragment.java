package com.li.videoapplication.ui.fragment;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.activity.GroupDetailActivity;

/**
 * 碎片：游戏介绍
 */
public class GroupdetailIntroduceFragment extends TBaseFragment{

    private GroupDetailActivity activity;
    private TextView introduce;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity = (GroupDetailActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getCreateView() {
        return R.layout.pager_groupdetail_introduce;
    }

    @Override
    protected void initContentView(View view) {
        introduce = (TextView) view.findViewById(R.id.groupdetail_introduce);
    }

    public void loadData() {

        setTextViewText(introduce, activity.game.getGame_description());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //该fragment处于最前台交互状态
        if (isVisibleToUser) {
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.GAME, "游戏圈-游戏介绍");
        }
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

}
