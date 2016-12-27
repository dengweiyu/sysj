package com.li.videoapplication.views;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.li.videoapplication.R;

/**
 * 自定义加载更多view-字体黑色
 */
public class LoadMoreViewAccent extends LoadMoreView {

    @Override
    public int getLayoutId() {
        return R.layout.view_loadmore_accent;
    }

    @Override
    public boolean isLoadEndGone() {
        return true;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return 0;
    }
}
