package com.li.videoapplication.ui.activity;

import android.os.Bundle;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.TBaseActivity;

/**
 * 活动：举报结果
 */
public class ReportResultActivity extends TBaseActivity {

	@Override
	public int getContentView() {
		return R.layout.activity_reportresult;
	}

	public int inflateActionBar() {
		return R.layout.actionbar_second;
	}

	@Override
	public void afterOnCreate() {
		super.afterOnCreate();

		setSystemBarBackgroundWhite();
		setAbTitle(R.string.reportresult_title);
	}
}
