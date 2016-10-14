package com.li.videoapplication.framework;

public interface IPullToRefreshActivity extends ITBaseActivity {

    void onRefresh();

    void onLoadMore();
}
