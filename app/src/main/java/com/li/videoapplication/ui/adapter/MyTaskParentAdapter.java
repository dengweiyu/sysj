package com.li.videoapplication.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Task;
import com.li.videoapplication.data.model.entity.TaskGroup;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.views.ListViewY1;

/**
 * 适配器：我的任务
 */
@SuppressLint({ "InflateParams", "ViewHolder" })
public class MyTaskParentAdapter extends BaseArrayAdapter<TaskGroup> {

	public MyTaskParentAdapter(Context context, List<TaskGroup> data) {
		super(context, R.layout.adapter_mytask_parent, data);
	}

	@SuppressWarnings("unused")
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final TaskGroup record = getItem(position);
		final  ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.adapter_mytask_parent, null);
			holder.question = (ImageView) view.findViewById(R.id.mytask_question);
			holder.title = (TextView) view.findViewById(R.id.mytask_title);
			holder.listview = (ListViewY1) view.findViewById(R.id.mytask_listview);
			holder.questionBar = (LinearLayout) view.findViewById(R.id.mytask_first);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		setTextViewText(holder.title, record.getType_name());
		setIcon(holder, record);
		setListView(record, holder);

		return view;
	}

	private void setListView(final TaskGroup record, final  ViewHolder holder) {
		List<Task> data = new ArrayList<>();
		if (record.getList() != null) {
			data.addAll(record.getList());
		}
		MyTaskChildAdapter adapter = new MyTaskChildAdapter(getContext(), data, record);
		holder.listview.setAdapter(adapter);
	}

	private void setIcon(final ViewHolder holder, TaskGroup record) {

		if (record.getType_name() != null && record.getType_name().equals(TaskGroup.TYPENAME_EVERYDAY)) {
			setImageViewImageRes(holder.question, R.drawable.mytask_question);
			holder.question.setVisibility(View.VISIBLE);
			holder.questionBar.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					DialogManager.showMyPersonalInfoGrowupDialog(getContext());
				}
			});
		} else {
			holder.question.setVisibility(View.GONE);
			holder.questionBar.setOnClickListener(null);
		}
	}

	private static class ViewHolder {

		ImageView question;
		TextView title;
		ListViewY1 listview;
		LinearLayout questionBar;
	}
}
