package com.li.videoapplication.ui.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Associate;
import com.li.videoapplication.data.EventManager;
import com.li.videoapplication.framework.BaseBaseAdapter;

/**
 * 适配器:图文分享-搜索礼包
 */
public class SearchLiftAdapter extends BaseBaseAdapter {

	private Activity activity;
	private ViewHodler hodler;
	private ArrayList<Associate> data;

	@Override
	protected Context getContext() {
		return activity;
	}

	public SearchLiftAdapter(Context context, ArrayList<Associate> data) {
		try {
			this.activity = (Activity) context;
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.data = data;

		inflater = LayoutInflater.from(activity);
	}

	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		final Associate record = (Associate) getItem(position);
		if (view == null) {
			hodler = new ViewHodler();
			view = inflater.inflate(R.layout.adapter_searchgame, null);
			hodler.title = (TextView) view.findViewById(R.id.searchgame_title);

			view.setTag(hodler);
		} else {
			hodler = (ViewHodler) view.getTag();
		}

		hodler.title.setText(record.getGame_name());
		
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

                activity.finish();
                EventManager.postSearchGame2VideoShareEvent(record);
			}
		});
		return view;
	}

	public static class ViewHodler {
		TextView title;
//		ImageView pic;
	}
}
