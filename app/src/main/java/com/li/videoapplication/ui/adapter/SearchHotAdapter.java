package com.li.videoapplication.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Keyword;
import com.li.videoapplication.framework.BaseArrayAdapter;

import java.util.List;
import java.util.Random;

/**
 * 适配器：热门搜索
 */
@SuppressLint("InflateParams")
public class SearchHotAdapter extends BaseArrayAdapter<Keyword> {

	private Random random = new Random();

	private int[] color = { Color.BLACK, Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED };

	public SearchHotAdapter(Context context, List<Keyword> data) {
		super(context, R.layout.adapter_searchhot, data);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		final Keyword record = getItem(position);
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.adapter_searchhot, null);
			holder.text = (TextView) view.findViewById(R.id.searchhot_text);
			/*
            int a = random.nextInt(10);
            int b = random.nextInt(5);
            if (a == position) {
                holder.text.setTextColor(color[b]);
            } else {
                holder.text.setTextColor(color[0]);
            }*/
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		} 

		setTextViewText(holder.text, record.getName());
		/*
		view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

			}
		});*/

		return view;
	}

	private static class ViewHolder {
		TextView text;
	}
}
