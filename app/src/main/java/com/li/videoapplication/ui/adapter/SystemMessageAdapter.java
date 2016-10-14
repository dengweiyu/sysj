package com.li.videoapplication.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.SysMessage;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.tools.TimeHelper;

/**
 * 适配器：系统消息
 */
@SuppressLint("InflateParams") 
public class SystemMessageAdapter extends BaseArrayAdapter<SysMessage> {

	public SystemMessageAdapter(Context context, List<SysMessage> data) {
		super(context, R.layout.adapter_systemmessage, data);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		final SysMessage record = getItem(position);
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.adapter_systemmessage, null);
			holder.title = (TextView) view.findViewById(R.id.systemMessage_title);
			holder.time = (TextView) view.findViewById(R.id.systemMessage_time);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		setTextViewText(holder.title, record.getContent());
		try {
			setTextViewText(holder.time, TimeHelper.getSysMessageTime(record.getTime()));
		} catch (Exception e) {
			e.printStackTrace();
			setTextViewText(holder.time, "");
		}
		
		return view;
	}

	private static class ViewHolder {
		TextView title;
		TextView time;
	}
}

