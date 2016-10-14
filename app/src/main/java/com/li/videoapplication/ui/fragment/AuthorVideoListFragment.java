package com.li.videoapplication.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.response.AuthorVideoList208Entity;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.ui.activity.VideoPlayActivity;
import com.li.videoapplication.ui.adapter.VideoPlayVideoAdapter;
import com.li.videoapplication.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.AbsListViewOverScrollDecorAdapter;

/**
 * 碎片：视频播放/图文详情--玩家视频
 */
public class AuthorVideoListFragment extends TBaseFragment implements OnRefreshListener2<ListView>, View.OnClickListener {

    private String game_id;
    private String member_id;//player's member id

    private PullToRefreshListView pullToRefreshListView;
    private ListView listView;
    private VideoPlayVideoAdapter adapter;
    private List<VideoImage> data;

    private int page = 1;
    private VideoPlayActivity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity = (VideoPlayActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_authorvideolist;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected IPullToRefresh getPullToRefresh() {
        return pullToRefreshListView;
    }

    @Override
    protected void initContentView(View view) {
        Bundle arguments = getArguments();
        member_id = arguments.getString("member_id");
        game_id = arguments.getString("game_id");
        String titleText = arguments.getString("title");

        TextView title = (TextView) view.findViewById(R.id.videoplay_videoList_title);
        setTextViewText(title, titleText);

        view.findViewById(R.id.videoplay_videoList_close).setOnClickListener(this);
        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pulltorefresh);
        pullToRefreshListView.setMode(Mode.PULL_FROM_END);
        listView = pullToRefreshListView.getRefreshableView();
        new VerticalOverScrollBounceEffectDecorator(new AbsListViewOverScrollDecorAdapter(listView));

        data = new ArrayList<>();
        adapter = new VideoPlayVideoAdapter(getActivity(), data);
        listView.setAdapter(adapter);

        onPullDownToRefresh(pullToRefreshListView);
        pullToRefreshListView.setOnRefreshListener(this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        page = 1;
        onPullUpToRefresh(refreshView);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        if (!StringUtil.isNull(member_id)) {
            DataManager.authorVideoList208(member_id, game_id, page);
            onRefreshCompleteDelayed(PullToRefreshActivity.TIME_REFRESH_SHORT);
        }
    }

    /**
     * 回调:用户视频列表2
     */
    public void onEventMainThread(AuthorVideoList208Entity event) {

        if (event.isResult()) {
            if (event.getData().getData().size() > 0) {
                if (page == 1) {
                    data.clear();
                }
                data.addAll(event.getData().getData());
                adapter.notifyDataSetChanged();
                ++page;
            }
        }
        onRefreshComplete();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.videoplay_videoList_close:
                if (activity != null)
                    activity.removeAuthorVideoListFragment();
                break;
        }
    }
}
