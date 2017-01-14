package com.li.videoapplication.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.network.RequestExecutor;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.adapter.VideoAdapter;

/**
 * 活动：收藏-观看记录
 */
public class MyHistoryFragment extends TBaseFragment {

    private PullToRefreshGridView pullToRefreshGridView;
    private GridView mGridView;
    public VideoAdapter adapter;
    public List<VideoImage> data = new ArrayList<VideoImage>();

    /**
     * 删除
     */
    public void deleteData() {
        // 本地删除
        PreferencesHepler.getInstance().removeHistoryVideoList();
        data.removeAll(adapter.deleteData);
        if (data.size() > 0) {
            PreferencesHepler.getInstance().saveHistoryVideoList(data);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_mycollection;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "我的收藏-观看记录");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshHistory();
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected IPullToRefresh getPullToRefresh() {
        return pullToRefreshGridView;
    }

    @Override
    protected void initContentView(View view) {
        initGridView(view);
        refreshHistory();
    }

    private void initGridView(View view) {
        pullToRefreshGridView = (PullToRefreshGridView) view.findViewById(R.id.pulltorefresh);
        pullToRefreshGridView.setMode(Mode.DISABLED);
        mGridView = pullToRefreshGridView.getRefreshableView();

        View emptyView = getActivity().getLayoutInflater().inflate(R.layout.emptyview,
                (ViewGroup) mGridView.getParent(), false);
        TextView emptyText = (TextView) emptyView.findViewById(R.id.emptyview_text);
        emptyText.setText("您还没观看过视频喔~");
        mGridView.setEmptyView(emptyView);

        adapter = new VideoAdapter(getActivity(), data, this);
        adapter.setDeleteMode(false);
        mGridView.setAdapter(adapter);
    }

    private void refreshHistory() {
        RequestExecutor.start(new Runnable() {
            @Override
            public void run() {
                if (PreferencesHepler.getInstance().getHistoryVideoList() != null) {
                    data.clear();
                    data.addAll(PreferencesHepler.getInstance().getHistoryVideoList());

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }
}
