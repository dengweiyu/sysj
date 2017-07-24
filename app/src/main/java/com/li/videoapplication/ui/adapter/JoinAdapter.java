package com.li.videoapplication.ui.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.ui.activity.VideoShareActivity;
import com.li.videoapplication.ui.activity.WebActivity;
import com.li.videoapplication.utils.URLUtil;

import java.util.List;

/**
 * 适配器：手游视界视频上传页-活动列表设配器
 */
@SuppressLint("UseSparseArrays")
public class JoinAdapter extends BaseArrayAdapter<Match> {

	private VideoShareActivity activity;

	public JoinAdapter(VideoShareActivity activity, List<Match> data) {
		super(activity, R.layout.adapter_join, data);
		this.activity = activity;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final Match record = getItem(position);
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.adapter_join, null);
			holder.root = convertView.findViewById(R.id.root);
			holder.title = (TextView) convertView.findViewById(R.id.join_title);
			holder.detail = (TextView) convertView.findViewById(R.id.join_detail);
			holder.check = (ImageView) convertView.findViewById(R.id.join_check);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.title.setText("#" + record.getName_tag() + "#");

		if (VideoShareActivity.getMatch_id() != null &&
				VideoShareActivity.getMatch_id().equals(record.getMatch_id())) {
			holder.check.setImageResource(R.drawable.tag_tick);
		} else {
			holder.check.setImageResource(R.drawable.videoshare_apply_gray);
		}

		holder.detail.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (URLUtil.isURL(record.getUrl())) {
					WebActivity.startWebActivity(activity, record.getUrl());
				}
			}
		});

		holder.title.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (activity != null)
					activity.updateMatchSpan(record);
				notifyDataSetChanged();
			}
		});

		holder.check.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (activity != null)
					activity.updateMatchSpan(record);
				notifyDataSetChanged();
			}
		});

		return convertView;
	}

	private static class ViewHolder {
		View root;
		TextView title;
		TextView detail;
		ImageView check;
	}
}
