package com.li.videoapplication.ui.fragment;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.framework.TBaseFragment;
/**
 * 碎片：启动-logo
 */
public class SplashFragment extends TBaseFragment {

	@Override
	protected int getCreateView() {
		return R.layout.fragment_splash;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected IPullToRefresh getPullToRefresh() {
		return null;
	}

	@Override
	protected void initContentView(View view) {
		ImageView bg = (ImageView)view.findViewById(R.id.iv_red_background);
		ImageView top = (ImageView)view.findViewById(R.id.top);
		ImageView second = (ImageView)view.findViewById(R.id.second);

		Context context = getActivity();
		GlideHelper.displayImage(context,R.drawable.appstart_bg,bg);
		GlideHelper.displayImage(context,R.drawable.logo_appstart,top);
		GlideHelper.displayImage(context,R.drawable.appstart_name,second);
	}
}
