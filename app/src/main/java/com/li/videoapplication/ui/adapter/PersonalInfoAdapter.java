package com.li.videoapplication.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.BaseArrayAdapter;

import java.util.List;

/**
 * 适配器：玩家资料
 */
@SuppressLint("InflateParams") 
public class PersonalInfoAdapter extends BaseArrayAdapter<Object> {

	public PersonalInfoAdapter(Context context, List<Object> data) {
		super(context, R.layout.adapter_mypersonainfo, data);
		
		inflater = LayoutInflater.from(context);
		resources = context.getResources();
	}

	@SuppressWarnings({ "null", "unused" })
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		
		final Object recored = getItem(position);
		ViewHolder viewHolder = null;
		if (view == null) {
			view = inflater.inflate(R.layout.adapter_mypersonainfo, null);
			viewHolder.pic = (ImageView) view.findViewById(R.id.mypersonalinfo_pic);
			viewHolder.name = (TextView) view.findViewById(R.id.mypersonalinfo_name);
			viewHolder.selected = (ImageView) view.findViewById(R.id.mypersonalinfo_selected);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		
		viewHolder.selected.setVisibility(View.GONE);
		
		return view;
	}
	
	private static class ViewHolder {
		
		ImageView pic;
		TextView name;
		ImageView selected;
	}
	
}
