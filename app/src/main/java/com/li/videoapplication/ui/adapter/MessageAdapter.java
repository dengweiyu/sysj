package com.li.videoapplication.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.BaseArrayAdapter;

/**
 * 适配器：消息
 */
@SuppressLint({ "InflateParams", "ViewHolder" }) 
public class MessageAdapter extends BaseArrayAdapter<MessageAdapter.Message> {

	public MessageAdapter(Context context, List<MessageAdapter.Message> data) {
		super(context, R.layout.adapter_message, data);
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		
		final Message record = getItem(position);
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.adapter_message, null);
			holder.title = (TextView) view.findViewById(R.id.message_name);
			holder.pic = (ImageView) view.findViewById(R.id.message_pic);
			holder.go = (ImageView) view.findViewById(R.id.message_go);
			holder.count = (TextView) view.findViewById(R.id.message_count);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		setTextViewText(holder.title, record.getContent());
		setImageViewImageRes(holder.pic, record.getPic());
		setCount(record, holder.count, holder.go);
		
		setListViewLayoutParams(view, 58);
		
		return view;
	}
	
	/**
	 * 设置红点
	 */
	private void setCount(Message record, TextView view, ImageView v) {

		view.setText("");
		if (record.getCount() > 0) {
//			setTextViewTextVisibility(view, String.valueOf(record.getFileCount()));
			view.setVisibility(View.VISIBLE);
			v.setVisibility(View.GONE);
		} else {
			view.setVisibility(View.GONE);
			v.setVisibility(View.VISIBLE);
		}
	}

	private static class ViewHolder {
		
		TextView title;
		ImageView go;
		TextView count;
		ImageView pic;
	}
	
	public static class Message {
		
		private String content;
		
		private int pic;
		
		private int count;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public int getPic() {
			return pic;
		}

		public void setPic(int pic) {
			this.pic = pic;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}
	}
}
