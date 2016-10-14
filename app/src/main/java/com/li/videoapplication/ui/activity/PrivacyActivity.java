package com.li.videoapplication.ui.activity;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.TBaseActivity;
/**
 * 活动：隐私及免责声明
 */
public class PrivacyActivity extends TBaseActivity {

	@Override
	public int getContentView() {
		return R.layout.activity_privacy;
	}

	public int inflateActionBar() {
		return R.layout.actionbar_second;
	}

	@Override
	public void afterOnCreate() {
		super.afterOnCreate();

		setSystemBarBackgroundWhite();
		setAbTitle(R.string.privacy_title);
	}
}
