package com.li.videoapplication.framework;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.tools.LayoutParamsHelper;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.ui.toast.ToastHelper;
import com.li.videoapplication.utils.ScreenUtil;
/**
 * 基本适配器
 */
public abstract class BaseArrayAdapter<T> extends ArrayAdapter<T> {

    protected final String action = this.getClass().getName();

    protected final String tag = this.getClass().getSimpleName();
	
	public interface Callback {
		public void onCall(BaseArrayAdapter adapter, final int position, View view, final ViewGroup parent);
	}
	
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
	protected WindowManager windowManager;
	protected int srceenWidth, srceenHeight;

	protected TextImageHelper textImageHelper = new TextImageHelper();
	protected LayoutParamsHelper layoutParamsHelper = new LayoutParamsHelper();
	
	protected Gson gson = new Gson();

	public BaseArrayAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
		super(context, resource, textViewResourceId, objects);
		
		init();
	}

	public BaseArrayAdapter(Context context, int resource, int textViewResourceId, T[] objects) {
		super(context, resource, textViewResourceId, objects);
		
		init();
	}

	public BaseArrayAdapter(Context context, int resource, int textViewResourceId) {
		super(context, resource, textViewResourceId);

		init();
	}

	public BaseArrayAdapter(Context context, int resource, List<T> objects) {
		super(context, resource, objects);
		
		init();
	}

	public BaseArrayAdapter(Context context, int resource, T[] objects) {
		super(context, resource, objects);
		
		init();
	}

	public BaseArrayAdapter(Context context, int resource) {
		super(context, resource);
		
		init();
	}
	
	@SuppressWarnings("deprecation")
	private void init() {
		
		inflater = LayoutInflater.from(getContext());
		resources = getContext().getResources();
		windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		
	    srceenWidth = windowManager.getDefaultDisplay().getWidth();
	    srceenHeight = windowManager.getDefaultDisplay().getHeight();
	}
	
	/*##############  加载文本和图像   ##############*/
	
	protected void setView(final View view, int delayMillis) {
		textImageHelper.setView(view, delayMillis);
	}
	
	protected void setTextViewText(TextView mTextView, String text) {
		textImageHelper.setTextViewText(mTextView, text);
	}
	
	protected void setTextViewText(TextView mTextView, int res) {
		textImageHelper.setTextViewText(getContext(), mTextView, res);
	}
	
	protected void setTextViewTextVisibility(TextView mTextView, String text) {
		textImageHelper.setTextViewTextVisibility(mTextView, text);
	}
	
	protected void setTextViewTextVisibility(TextView mTextView, int res) {
		textImageHelper.setTextViewTextVisibility(getContext(), mTextView, res);
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

	protected void setImageViewImageNetAlpha(ImageView mImageView, String url) {
		textImageHelper.setImageViewImageNetAlpha(mImageView, url);
	}
	
	/*##############  提示   ##############*/
	
	protected void showToastShort(final String text) {
		ToastHelper.s(text);
	}
	
	protected void showToastShort(final int res) {
		ToastHelper.s(res);
	}
	
	protected void showToastLong(final String text) {
		ToastHelper.l(text);
	}
	
	protected void showToastLong(final int res) {
		ToastHelper.l(res);
	}
	
	protected void showToastLogin() {
		this.showToastShort("请先登录!");
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
}
