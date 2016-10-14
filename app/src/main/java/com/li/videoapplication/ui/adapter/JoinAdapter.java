package com.li.videoapplication.ui.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.li.videoapplication.framework.CommonAdapter;
import com.li.videoapplication.framework.ViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Match;

/**
 * 适配器：手游视界视频上传页-活动列表设配器
 */
@SuppressLint("UseSparseArrays")
public class JoinAdapter extends CommonAdapter<Match> {

	public static String match_id;

	/** 单选框数据集 */
	private Map<Integer, Boolean> isSelected;

	public JoinAdapter(Context context, List<Match> data, int layoutId) {
		super(context, data, layoutId);
		
		initSelected();
	}
	
	private void initSelected() {
		isSelected = new HashMap<>();

		for (int i = 0; i < data.size(); i++) {
			isSelected.put(i, false);
		}
	}

	@Override
	public void convert(final ViewHolder holder, final Match record, final int position) {

		holder.setText(R.id.join_title, record.getName());
		RelativeLayout root = holder.getView(R.id.root);
		root.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				boolean b = !isSelected.get(position);
				for (Integer p : isSelected.keySet()) {
					isSelected.put(p, false);
				}
				isSelected.put(position, b);
				notifyDataSetChanged();
				if (b) {
					match_id = record.getMatch_id();
				} else {
					match_id = null;
				}
			}
		});

		CheckBox check = holder.getView(R.id.join_check);
		if (isSelected.get(position) != null) {
			check.setChecked(isSelected.get(position));
		}
	}
}
