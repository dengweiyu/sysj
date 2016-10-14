package com.li.videoapplication.framework;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.tools.LayoutParamsHelper;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.ui.activity.LoginActivity;
import com.li.videoapplication.ui.toast.ToastHelper;
import com.li.videoapplication.utils.ScreenUtil;

/**
 * 基本碎片
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2) 
public abstract class TBaseFragment extends BaseFragment {
	
	protected String getMember_id() {
		return PreferencesHepler.getInstance().getMember_id();
	}
	
	protected boolean isLogin() {
		return PreferencesHepler.getInstance().isLogin();
	}
	
	protected Member getUser() {
		return PreferencesHepler.getInstance().getUserProfilePersonalInformation();
	}

	protected LayoutInflater inflater;
	
	protected Resources resources;
	
	protected TextImageHelper textImageHelper = new TextImageHelper();
	
	protected LayoutParamsHelper layoutParamsHelper = new LayoutParamsHelper();
	
	protected int srceenWidth, srceenHeight;
	
	protected WindowManager windowManager;
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		inflater = LayoutInflater.from(getActivity());
		resources = activity.getResources();
		windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		
	    srceenWidth = windowManager.getDefaultDisplay().getWidth();
	    srceenHeight = windowManager.getDefaultDisplay().getHeight();
	}

    protected abstract int getCreateView();
	
	@Override
	public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(getCreateView(), null, false);
		initContentView(view);
		return view;
	}
	
	protected abstract void initContentView(View view);
	
	@SuppressWarnings("rawtypes")
	protected abstract IPullToRefresh getPullToRefresh();
	
	protected void onRefreshCompleteDelayed(int delayMillis) {
		onRefreshCompleteDelayed(getPullToRefresh(), delayMillis);
	}
	
	protected void onRefreshCompleteDelayed(final IPullToRefresh mIPullToRefresh, int delayMillis) {
		
		if (mIPullToRefresh != null) {
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					
					mIPullToRefresh.onRefreshComplete();
				}
			}, delayMillis);
		}
	}
	
	protected void onRefreshComplete() {
		onRefreshComplete(getPullToRefresh());
	}
	
	protected void onRefreshComplete(final IPullToRefresh mIPullToRefresh) {
		onRefreshCompleteDelayed(mIPullToRefresh, 0);
	}
	
	/*##############  加载文本和图像   ##############*/
	
	protected void setTextViewText(TextView mTextView, String text) {
		textImageHelper.setTextViewText(mTextView, text);
	}
	
	protected void setTextViewText(TextView mTextView, int res) {
		textImageHelper.setTextViewText(getActivity(), mTextView, res);
	}
	
	protected void setTextViewTextVisibility(TextView mTextView, String text) {
		textImageHelper.setTextViewTextVisibility(mTextView, text);
	}
	
	protected void setTextViewTextVisibility(TextView mTextView, int res) {
		textImageHelper.setTextViewTextVisibility(getActivity(), mTextView, res);
	}
	
	protected void setImageViewImageRes(ImageView mImageView, int res) {
		textImageHelper.setImageViewImageRes(mImageView, res);
	}
	
	protected void setImageViewImageLocal(ImageView mImageView, String path) {
		textImageHelper.setImageViewImageLocal(mImageView, path);
	}
	
	protected void setImageViewImageNet(ImageView mImageView, String url) {
		textImageHelper.setImageViewImageNet(mImageView, url);
	}
	
	/*##############  提示   ##############*/
	
	protected void showToastShort(final String text) {
		ToastHelper.s(text);
	}
	
	protected void showToastLong(final String text) {
		ToastHelper.l(text);
	}
	
	protected void showToastLogin() {
		this.showToastShort("请先登录!");
	}
	
	/*##############  页脚和页头   ##############*/
	
	protected View newEmptyView(int dpHeight) {
		
		View view = new View(getActivity());
		view.setBackgroundResource(android.R.color.transparent);
		int pxHeight = ScreenUtil.dp2px(dpHeight);
		ListView.LayoutParams params = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, pxHeight);
		view.setLayoutParams(params);
		return view;
	}
	
	/*##############  屏尺寸转换    ##############*/

	protected int dp2px(int dpValue) {
		return ScreenUtil.dp2px(dpValue);
	}

	protected int px2dp(int pxValue) {
		return ScreenUtil.px2dp(pxValue);
	}
	
	/*##############  布局参数设置    ##############*/
	
	protected void setAdapterViewLayoutParams(View view, int dpHeight) {
		layoutParamsHelper.setAdapterView(view, dpHeight);
	}
	
	protected void setListViewLayoutParams(View view, int dpHeight) {
		layoutParamsHelper.setListView(view, dpHeight);
	}
	
	protected void setGridViewLayoutParams(View view, int dpHeight) {
		layoutParamsHelper.setGridView(view, dpHeight);
	}
	
	protected void setListViewLayoutParams(View view, int w, int h) {
		layoutParamsHelper.setListView(view, w, h);
	}
	
	protected void setLinearLayoutLayoutParams(View view, int w, int h) {
		layoutParamsHelper.setLinearLayout(view, w, h);
	}
	
	protected void setRelativeLayoutLayoutParams(View view, int w, int h) {
		layoutParamsHelper.setRelativeLayout(view, w, h);
	}
	
	protected void setGridViewLayoutParams(View view, int w, int h) {
		layoutParamsHelper.setGridView(view, w, h);
	}
	
	protected void setAdapterViewLayoutParams(View view, int w, int h) {
		layoutParamsHelper.setAdapterView(view, w, h);
	}
	
	/*##############   页面跳转   ##############*/
	
	protected void startLoginActivity() {
		
		Intent intent = new Intent();
		intent.setClass(getActivity(), LoginActivity.class);
		startActivity(intent);
	}

	/*##############   网络变化   ##############*/
/*
    private View connectivityView;

    public void onEventMainThread(ConnectivityChangeEvent event) {
        try {
            LogHelper.i(tag, "connectivity=" + event);
            LogHelper.i(tag, "connectivity=" + event.getNetworkInfo());
            LogHelper.i(tag, "connectivity=" + event.getNetworkInfo().getTypeName());
            LogHelper.i(tag, "connectivity=" + event.getNetworkInfo().getSubtypeName());
            LogHelper.i(tag, "connectivity=" + event.getNetworkInfo().getExtraInfo());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (connectivityView == null) {
            try {
                connectivityView = getView().findViewById(R.id.connectivitychange_root);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (connectivityView != null) {
            LogHelper.i(tag, "1");
            NetworkInfo info = event.getNetworkInfo();
            notifyConnectivityChange(connectivityView, info);
        }
    }

    private void notifyConnectivityChange(View view, NetworkInfo info) {

        LogHelper.i(tag, "2");
        if (info != null && info.isAvailable()) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                // WIFI网络，隐藏断网提示
                view.setVisibility(View.GONE);
            } else if (info.getType() == ConnectivityManager.TYPE_ETHERNET) {
                // 有线网络，隐藏断网提示
                view.setVisibility(View.GONE);
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                // 移动网络，隐藏断网提示
                view.setVisibility(View.GONE);
            } else {
                // 隐藏断网提示
                view.setVisibility(View.GONE);
            }
        } else {
            // 网络断开，显示断网提示
            view.setVisibility(View.VISIBLE);
        }
    }*/
}
