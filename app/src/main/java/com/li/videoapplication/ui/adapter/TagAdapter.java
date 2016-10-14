package com.li.videoapplication.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Tag;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.ui.activity.VideoShareActivity210;
import com.li.videoapplication.ui.toast.ToastHelper;

import java.util.List;

/**
 * 适配器：标签
 */
@SuppressLint("UseSparseArrays")
public class TagAdapter extends BaseArrayAdapter<Tag> {

	public TagAdapter(Context context, List<Tag> data) {
		super(context, R.layout.adapter_tag, data);
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		final Tag record = getItem(position);
		final ViewHolder hodler;
		if (view == null) {
			hodler = new ViewHolder();
			view = inflater.inflate(R.layout.adapter_tag, null);
			hodler.root = view.findViewById(R.id.root);
			hodler.text = (TextView) view.findViewById(R.id.tag_text);
			hodler.tick = (ImageView) view.findViewById(R.id.tag_tick);
			view.setTag(hodler);
		} else {
			hodler = (ViewHolder) view.getTag();
		}

		hodler.text.setText("#" + record.getName() + "#");
		if (VideoShareActivity210.IDS.contains(record.getGame_tag_id())) {
			hodler.tick.setVisibility(View.VISIBLE);
			hodler.text.setTextColor(Color.parseColor("#40a8fe"));
		} else {
			hodler.tick.setVisibility(View.GONE);
			hodler.text.setTextColor(Color.parseColor("#787878"));
		}

		hodler.root.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String id = record.getGame_tag_id();
				if (VideoShareActivity210.IDS.contains(id)) {// 移除
					hodler.tick.setVisibility(View.GONE);
					VideoShareActivity210.IDS.remove(id);
				} else {
					if (VideoShareActivity210.IDS.size() > 1) {// 最多2个
						ToastHelper.s("最多只能选择2个标签");
					} else {// 增加
						hodler.tick.setVisibility(View.VISIBLE);
						VideoShareActivity210.IDS.add(id);
					}
				}
				notifyDataSetChanged();
			}
		});
		return view;
	}

	private static class ViewHolder {
		View root;
		TextView text;
		ImageView tick;
	}
}
