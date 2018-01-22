package com.li.videoapplication.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import com.li.videoapplication.data.model.event.SearchResultEvent;
import com.li.videoapplication.data.model.response.SearchVideo203Entity;
import com.li.videoapplication.data.model.response.VideoCollect2Entity;
import com.li.videoapplication.data.model.response.VideoFlower2Entity;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.framework.TBaseChildFragment;
import com.li.videoapplication.tools.PullToRefreshHepler;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.adapter.GroupDetailVideoAdapter;

import io.rong.eventbus.EventBus;


/**
 * 碎片：搜索视频
 */
public class SearchVideoFragment extends TBaseChildFragment implements OnRefreshListener2<ListView> {

    private int page_count;

    public static SearchVideoFragment newInstance(String content) {
        SearchVideoFragment fragment = new SearchVideoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("content", content);
        fragment.setArguments(bundle);
        return fragment;
    }

    private String content;

    public void setContent(String content) {
        if (this.content != null && !this.content.equals(content)){
            this.content = content;
            if (data != null){
                data.clear();
            }
            if (adapter != null){
                adapter.updateKey(content);
            }
        }
        refreshListView();
    }

    private String getContent() {
        return content;
    }

    private PullToRefreshListView pullToRefreshListView;
    private ListView listView;
    private GroupDetailVideoAdapter adapter;
    private List<VideoImage> data;

    private int page = 1;

    @Override
    protected int getCreateView() {
        return R.layout.fragment_searchvideo;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected IPullToRefresh getPullToRefresh() {
        return pullToRefreshListView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {//该fragment处于最前台交互状态
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.MAIN, "搜索-相关视频-点击搜索进入相关视频次数");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (data != null){
            data.clear();
        }
    }

    @Override
    protected void initContentView(View view) {

        try {
            content = getArguments().getString("content");
        } catch (Exception e) {
            e.printStackTrace();
        }

        initListView(view);
        refreshListView();
    }

    private void initListView(View view) {

        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pulltorefresh);
        pullToRefreshListView.setMode(Mode.BOTH);
        pullToRefreshListView.setOnRefreshListener(this);
        listView = pullToRefreshListView.getRefreshableView();
        View emptyView = getActivity().getLayoutInflater().inflate(R.layout.emptyview,
                (ViewGroup) listView.getParent(), false);
        TextView emptyText = (TextView) emptyView.findViewById(R.id.emptyview_text);
        emptyText.setText("没有相关视频");
        listView.setEmptyView(emptyView);
        data = new ArrayList<>();
        adapter = new GroupDetailVideoAdapter(getActivity(), data, content);
        listView.setAdapter(adapter);
    }

    public void refreshListView() {
        onPullDownToRefresh(pullToRefreshListView);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {

        page = 1;
        // 视频搜索203
        DataManager.searchVideo203(getContent(), getMember_id(), page);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        PullToRefreshHepler.setLastUpdatedLabel(refreshView);
        onRefreshCompleteDelayed(PullToRefreshActivity.TIME_REFRESH_SHORT);
        Log.d(tag, "onPullUpToRefresh: page == " + page);
        Log.d(tag, "page_count " + page_count);
        if (page <= page_count)
            // 视频搜索203
            DataManager.searchVideo203(getContent(), getMember_id(), page);
    }

    /**
     * 回调：视频搜索203
     */
    public void onEventMainThread(SearchVideo203Entity event) {
        if (event.isResult()) {
            page_count = event.getData().getPage_count();
            if (event.getData().getList().size() > 0) {
                EventBus.getDefault().post(new SearchResultEvent(1,true));
                if (page == 1) {
                    data.clear();
                }
                data.addAll(event.getData().getList());
                ++page;
            }else {
                EventBus.getDefault().post(new SearchResultEvent(1,false));

            }
            adapter.notifyDataSetChanged();
        }
        onRefreshComplete();
    }

    /**
     * 回调：视频点赞
     */
    public void onEventMainThread(VideoFlower2Entity event) {

        if (event != null) {
            if (event.isResult()) {
                showToastShort(event.getMsg());
            } else {
                showToastShort(event.getMsg());
            }
        }
    }

    /**
     * 回调：视频收藏
     */
    public void onEventMainThread(VideoCollect2Entity event) {

        if (event != null) {
            if (event.isResult()) {
                showToastShort(event.getMsg());
            } else {
                showToastShort(event.getMsg());
            }
        }
    }
}
