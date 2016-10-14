package com.li.videoapplication.framework;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * ViewHolder设配器
 */
public class ViewHolder {

	private SparseArray<View> mViews;
	public int mPosition;
	private View mConvertView;

	public ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {

		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);

		mConvertView.setTag(this);
	}

	public static ViewHolder get(Context context, View converView, ViewGroup parent, int layoutid, int position) {

		if (converView == null) {
			return new ViewHolder(context, parent, layoutid, position);
		} else {
			ViewHolder holder = (ViewHolder) converView.getTag();
			// 防止item复用时带来的控件错乱
			holder.mPosition = position;
			return holder;
		}

	}

	/**
	 * 通过viewid获取控件
	 */
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	public View getConvertView() {
		return mConvertView;
	}

	public ViewHolder setText(int viewId, String text) {
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
	}
}
