package com.li.videoapplication.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Gift;
import com.li.videoapplication.data.model.event.LoginEvent;
import com.li.videoapplication.data.model.event.LogoutEvent;
import com.li.videoapplication.data.model.response.MyPackage203Entity;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.ui.adapter.MyGiftAdapter;

/**
 * 碎片：我的礼包
 */
public class MyGiftFragment extends TBaseFragment implements OnRefreshListener2<ListView> {

    private PullToRefreshListView mPullToRefreshListView;
    private ListView mListView;
    private MyGiftAdapter adapter;
    private List<Gift> data;

    private int page = 1;
    private View nodata;
    private int page_count;

    @Override
    protected int getCreateView() {
        return R.layout.fragment_mygift;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected IPullToRefresh getPullToRefresh() {
        return mPullToRefreshListView;
    }

    @Override
    protected void initContentView(View view) {
        nodata = view.findViewById(R.id.nodata_root);
        TextView nodata_text = (TextView) view.findViewById(R.id.nodata_text);
        nodata_text.setText("您还没有领取过礼包喔~");
        initListView(view);

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                onPullDownToRefresh(mPullToRefreshListView);
            }
        }, AppConstant.TIME.MYGIFT_FRAGMENT);
    }

    private void initListView(View view) {

        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pulltorefresh);
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

        mListView = mPullToRefreshListView.getRefreshableView();
        mListView.addHeaderView(newEmptyView(2));
        mListView.addFooterView(newEmptyView(2));

        data = new ArrayList<Gift>();
        adapter = new MyGiftAdapter(getActivity(), data);
        mListView.setAdapter(adapter);

        mPullToRefreshListView.setOnRefreshListener(this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

        page = 1;
        // 我的礼包203
        DataManager.myPackage203(getMember_id(), page);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        onRefreshCompleteDelayed(PullToRefreshActivity.TIME_REFRESH_SHORT);

        if (page <= page_count)
            // 我的礼包203
            DataManager.myPackage203(getMember_id(), page);
    }

    /**
     * 回调:我的礼包203
     */
    public void onEventMainThread(MyPackage203Entity event) {

        if (event.isResult()) {
            page_count = event.getData().getPage_count();
            if (event.getData().getList().size() > 0) {
                nodata.setVisibility(View.GONE);
                if (page == 1) {
                    data.clear();
                }
                data.addAll(event.getData().getList());
                adapter.notifyDataSetChanged();
                ++page;
            } else {
                nodata.setVisibility(View.VISIBLE);
            }
        } else {
            nodata.setVisibility(View.VISIBLE);
        }
        onRefreshComplete();
    }

    /**
     * 事件：登录
     */
    public void onEventMainThread(LoginEvent event) {

        onPullDownToRefresh(mPullToRefreshListView);
    }

    /**
     * 事件：注销
     */
    public void onEventMainThread(LogoutEvent event) {

        onPullDownToRefresh(mPullToRefreshListView);
    }
}
