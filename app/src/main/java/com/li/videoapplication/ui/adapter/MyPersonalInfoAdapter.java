package com.li.videoapplication.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.GroupType;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.views.RoundedImageView;

/**
 * 适配器：我的个人资料喜欢的游戏类型
 */
@SuppressLint({ "InflateParams", "ViewHolder" })
public class MyPersonalInfoAdapter extends BaseArrayAdapter<GroupType> {

	public static final int MODE_EDIT = 1;
	public static final int MODE_NORMAL = 0;

	private int mode = MODE_NORMAL;
	
	private List<GroupType> data;
	
	private List<String> group_type_id = new ArrayList<String>();

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public MyPersonalInfoAdapter(Context context, List<GroupType> data) {
		super(context, R.layout.adapter_mypersonainfo, data);
		this.data = data;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		final GroupType recored = getItem(position);
		view = inflater.inflate(R.layout.adapter_mypersonainfo, null);
		ViewHolder holder = new ViewHolder();
		holder.pic = (RoundedImageView) view.findViewById(R.id.mypersonalinfo_pic);
		holder.name = (TextView) view.findViewById(R.id.mypersonalinfo_name);
		holder.selected = (ImageView) view.findViewById(R.id.mypersonalinfo_selected);

		setTextViewText(holder.name, recored.getGroup_type_name());
		setImageViewImageNet(holder.pic, recored.getFlag());
		setSelected(recored, holder.selected);

		final ImageView s = holder.selected;
		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mode == MODE_EDIT) {
					if (canSeleceed()) {
						if (recored.isSelected()) {// 選中狀態
							recored.setSelected(false);
						} else { // 未選中狀態
							recored.setSelected(true);
						}
					} else {
						if (recored.isSelected()) {// 選中狀態
							recored.setSelected(false);
						} else {
							showToastShort("只能选择3个喜欢的游戏类型");
						}
					}
				}
				notifyDataSetChanged();
			}
		});

		setAdapterViewLayoutParams(view, dp2px(48), dp2px(64));

		return view;
	}

	private void setSelected(GroupType recored, ImageView view) {

		if (mode == MODE_NORMAL) {
			view.setVisibility(View.GONE);
		} else if (mode == MODE_EDIT) {
			view.setVisibility(View.VISIBLE);
		}
		if (recored.isSelected()) {// 選中狀態
			view.setImageResource(R.drawable.personalinfo_like_yellow);
		} else { // 未選中狀態
			view.setImageResource(R.drawable.personalinfo_like_gray);
		}
	}
	
	private boolean canSeleceed() {
		
		group_type_id.clear();
		for (GroupType record : data) {
			if (record.isSelected()) {
				group_type_id.add(record.getGroup_type_id());
			}
		}
		return group_type_id.size() < 3;
	}

	private static class ViewHolder {

		RoundedImageView pic;
		TextView name;
		ImageView selected;
	}
}
