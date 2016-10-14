package com.li.videoapplication.framework;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.utils.ScreenUtil;
/**
 * 基本适配器
 */
public abstract class BaseBaseAdapter extends BaseAdapter {

    protected final String action = this.getClass().getName();
    protected final String tag = this.getClass().getSimpleName();
	
	protected LayoutInflater inflater;
	protected Resources resources;
	protected TextImageHelper textImageHelper = new TextImageHelper();
	
	protected abstract Context getContext();
	
	/*##############  加载文本和图像   ##############*/
	
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
	
	/*##############  布局参数设置    ##############*/
	
	protected void setListViewLayoutParams(View view, int dpHeight) {
		if (view != null) {
			int pxHeight = ScreenUtil.dp2px(dpHeight);
			setListViewLayoutParams(view, ListView.LayoutParams.MATCH_PARENT, pxHeight);
		}
	}
	
	protected void setListViewLayoutParams(View view, int w, int h) {
		if (view != null) {
			ListView.LayoutParams params = new ListView.LayoutParams(w, h);
			view.setLayoutParams(params);
		}
	}
	
	protected void setGridViewLayoutParams(View view, int dpHeight) {
		if (view != null) {
			int pxHeight = ScreenUtil.dp2px(dpHeight);
			setGridViewLayoutParams(view, ListView.LayoutParams.MATCH_PARENT, pxHeight);
		}
	}
	
	protected void setGridViewLayoutParams(View view, int w, int h) {
		if (view != null) {
			GridView.LayoutParams params = new GridView.LayoutParams(w, h);
			view.setLayoutParams(params);
		}
	}
	
	protected void setAdapterViewLayoutParams(View view, int dpHeight) {
		if (view != null) {
			int pxHeight = ScreenUtil.dp2px(dpHeight);
			setAdapterViewLayoutParams(view, ListView.LayoutParams.MATCH_PARENT, pxHeight);
		}
	}
	
	protected void setAdapterViewLayoutParams(View view, int w, int h) {
		if (view != null) {
			AdapterView.LayoutParams params = new AdapterView.LayoutParams(w, h);
			view.setLayoutParams(params);
		}
	}
}
