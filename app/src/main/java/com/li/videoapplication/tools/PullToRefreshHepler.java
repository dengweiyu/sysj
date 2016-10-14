package com.li.videoapplication.tools;

import android.os.Handler;
import android.text.format.DateUtils;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.li.videoapplication.framework.AppManager;

public class PullToRefreshHepler {

	/**
	 * 设置最后更新时间
	 */
	@SuppressWarnings("rawtypes")
	public static final void setLastUpdatedLabel(PullToRefreshBase refreshView) {
		if (refreshView != null) {

			String label = DateUtils.formatDateTime(AppManager.getInstance().getApplication(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
			ILoadingLayout mILoadingLayout = refreshView.getLoadingLayoutProxy();
			mILoadingLayout.setLastUpdatedLabel(label);
		}
	}

	@SuppressWarnings("rawtypes")
	public static void onRefreshCompleteDelayed(final Handler handler, final IPullToRefresh iPullToRefresh, int delayMillis) {

		if (iPullToRefresh != null) {
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {

					iPullToRefresh.onRefreshComplete();
				}
			}, delayMillis);
		}
	}
}
