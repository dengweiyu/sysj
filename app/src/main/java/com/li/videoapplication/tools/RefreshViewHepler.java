package com.li.videoapplication.tools;

import android.text.format.DateUtils;

import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.views.RefreshListView;

public class RefreshViewHepler {

	/**
	 * 功能：设置最后更新时间
	 */
	@SuppressWarnings("rawtypes")
	public static final void setRefreshTime(RefreshListView refreshView) {
		
		String label = DateUtils.formatDateTime(AppManager.getInstance().getApplication(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
		refreshView.setRefreshTime(label);
	}
}
