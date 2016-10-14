package com.li.videoapplication.framework;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;

public abstract class CommonAdapter<T> extends BaseAdapter {

	protected Context context;
	protected List<T> data;
	protected LayoutInflater inflater;
	protected int res;
	protected int srceenWidth, srceenHeight;
	protected WindowManager windowManager;

	@SuppressWarnings("deprecation")
	public CommonAdapter(Context context, List<T> data, int res) {
		this.context = context;
		this.data = data;
		this.res = res;

		inflater = LayoutInflater.from(context);
		windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	    srceenWidth = windowManager.getDefaultDisplay().getWidth();
	    srceenHeight = windowManager.getDefaultDisplay().getHeight();

	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public T getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 刷新数据
	 */
	public void refreshData(List<T> data) {
		this.data.clear();
        this.data.addAll(data);
		notifyDataSetChanged();

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder = ViewHolder.get(context, convertView, parent, res, position);
		convert(holder, getItem(position), position);
		return holder.getConvertView();
	}

	public abstract void convert(ViewHolder holder, T t, int position);

	public void onMeasure(int widthMeasureSpec, int expandSpec) {}

}
