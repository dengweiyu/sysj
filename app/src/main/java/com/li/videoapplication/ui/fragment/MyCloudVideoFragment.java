package com.li.videoapplication.ui.fragment;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.event.ConnectivityChangeEvent;
import com.li.videoapplication.data.model.response.AuthorVideoList2Entity;
import com.li.videoapplication.data.model.response.CloudListEntity;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.PullToRefreshHepler;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.adapter.MyCloudVideoAdapter;

import java.util.ArrayList;

/**
 * 碎片：云端视频
 */
public class MyCloudVideoFragment extends TBaseFragment implements PullToRefreshBase.OnRefreshListener2<ListView> {

    public PullToRefreshListView pullToRefreshListView;
    public ListView listView;
	public MyCloudVideoAdapter adapter;
	public ArrayList<VideoImage> data = new ArrayList<VideoImage>();

	public TextView tipUnlogin;
	public TextView tipEmpty;

    public int page = 1;

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return pullToRefreshListView;
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_mycloudvideo;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "我的视频-云端视频");
        }
    }

    @Override
    protected void initContentView(View view) {

        initListView(view);

        onPullDownToRefresh(pullToRefreshListView);
    }

    private void initListView(View view) {
        tipUnlogin = (TextView) view.findViewById(R.id.mycloudvideo_unlogin);
        tipEmpty = (TextView) view.findViewById(R.id.mycloudvideo_empty);

        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pulltorefresh);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        listView = pullToRefreshListView.getRefreshableView();
        pullToRefreshListView.setOnRefreshListener(this);

        adapter = new MyCloudVideoAdapter(data, getActivity());
        listView.setAdapter(adapter);

        tipUnlogin.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        tipEmpty.setVisibility(View.GONE);
	}

    private void refreshContentView() {
        if (isLogin()) {// 已经登陆
            tipUnlogin.setVisibility(View.GONE);
            if (data.size() > 0) {
                listView.setVisibility(View.VISIBLE);
                tipEmpty.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            } else {
                listView.setVisibility(View.GONE);
                tipEmpty.setVisibility(View.VISIBLE);
            }
        } else {// 未登录
            tipUnlogin.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            tipEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

        page = 1;
        onPullUpToRefresh(refreshView);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        PullToRefreshHepler.setLastUpdatedLabel(refreshView);
        onRefreshCompleteDelayed(PullToRefreshActivity.TIME_REFRESH_SHORT);

        // 用户视频列表2
//        DataManager.authorVideoList2(getMember_id(), page);

        // 用户视频列表
        DataManager.videoCloudList(getMember_id(), page);
    }

    /**
     * 回调:云端视频列表
     */
    public void onEventMainThread(AuthorVideoList2Entity event) {
        onRefreshComplete();
        if (event.isResult()) {
            if (event.getData() != null
                    && event.getData().getList() != null
                    && event.getData().getList().size() > 0) {
                if (page == 1) {
                    data.clear();
                }
                data.addAll(event.getData().getList());
                adapter.notifyDataSetChanged();
                ++ page;
            } else if (event.getData() != null
                    && event.getData().getList() != null
                    && event.getData().getList().size() == 0){
                showToastShort("没有更多了");
            }
        }
        refreshContentView();
    }

    /**
     * 回调：网络变化事件
     */
    public void onEventMainThread(ConnectivityChangeEvent event) {
        refreshContentView();
    }
}
