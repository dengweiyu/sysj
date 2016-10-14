package com.li.videoapplication.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Task;
import com.li.videoapplication.data.model.entity.TaskGroup;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.ui.activity.MyTaskActivity;
import com.li.videoapplication.ui.toast.ToastHelper;
import com.li.videoapplication.utils.TextUtil;

/**
 * 适配器：我的任务
 */
@SuppressLint({ "InflateParams", "ViewHolder" })
public class MyTaskChildAdapter extends BaseArrayAdapter<Task> {

	private TaskGroup taskGroup;
	private MyTaskActivity activity;

	public MyTaskChildAdapter(Context context, List<Task> data, TaskGroup taskGroup) {
		super(context, R.layout.adapter_mytask_child, data);
		this.taskGroup = taskGroup;
		activity = (MyTaskActivity) AppManager.getInstance().getActivity(MyTaskActivity.class);
	}

	@SuppressWarnings("unused")
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final Task record = getItem(position);
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.adapter_mytask_child, null);
			holder.pic = (ImageView) view.findViewById(R.id.mytask_pic);
			holder.title = (TextView) view.findViewById(R.id.mytask_title);
			holder.content = (TextView) view.findViewById(R.id.mytask_content);
			holder.task = (TextView) view.findViewById(R.id.mytask_task);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		setImageViewImageNet(holder.pic, record.getFlag());
		setTextViewText(holder.title, record.getName());
		setContent(record, holder);

		setTask(holder, record);

//		setListViewLayoutParams(view, 48);

		return view;
	}

	private class ViewHolder {

		ImageView pic;
		TextView title;
		TextView content;
		TextView task;
	}

	/**
	 * 任务內容
	 */
	private void setContent(Task record, ViewHolder holder) {

		String sb = record.getContent() +
				"，经验值" + TextUtil.toColor(record.getAdd_exp(), "#fb3d2e") +
				"，进度" + TextUtil.toColor(record.getNum_ing() + "/" + record.getNum(), "#fb3d2e");
		holder.content.setText(Html.fromHtml(sb));
	}

	/**
	 * 任务按鍵
	 */
	private void setTask(final ViewHolder holder, final Task record) {
//		View.OnClickListener listener = new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (!isLogin()) {
//					showToastLogin();
//					return;
//				}
//				if (record.getStatus().equals(Task.STATUS_ACCEPT)) {// 任务进行中
//					if (activity != null) {
//						activity.startActivity(record);
//					}
//				} else if (record.getStatus().equals(Task.STATUS_FINISH)) {// 任务完成
//					showToastLong("任务已完成");
//				}
//			}
//		};
		if (record.getStatus().equals(Task.STATUS_ACCEPT)) {// 任务进行中
			holder.task.setVisibility(View.VISIBLE);
			holder.task.setBackgroundResource(R.drawable.task_yellow);
			holder.task.setText("未完成");

		} else if (record.getStatus().equals(Task.STATUS_FINISH)) {// 任务完成
			holder.task.setVisibility(View.VISIBLE);
			holder.task.setBackgroundResource(R.drawable.task_gray);
			holder.task.setText("已完成");

		} else {// 默认隐藏
			holder.task.setVisibility(View.GONE);
			holder.task.setBackgroundColor(Color.TRANSPARENT);
			holder.task.setText("");
			holder.task.setOnClickListener(null);
		}
	}
}
