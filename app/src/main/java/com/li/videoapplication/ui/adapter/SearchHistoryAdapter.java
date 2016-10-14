package com.li.videoapplication.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.BaseArrayAdapter;

import java.util.List;

/**
 * 适配器：搜索历史
 */
@SuppressLint("InflateParams")
public class SearchHistoryAdapter extends BaseArrayAdapter<String> {

	public SearchHistoryAdapter(Context context, List<String> data) {
		super(context, R.layout.adapter_searchhot, data);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		final String record = getItem(position);
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.adapter_searchhot, null);
			holder.text = (TextView) view.findViewById(R.id.searchhot_text);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		} 

		setTextViewText(holder.text, record);

		return view;
	}

	private static class ViewHolder {
		TextView text;
	}
}
