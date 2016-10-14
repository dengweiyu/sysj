package com.li.videoapplication.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.ReportType;
import com.li.videoapplication.framework.BaseArrayAdapter;

import java.util.List;

/**
 * 适配器：举报
 */
@SuppressLint("InflateParams")
public class ReportAdapter extends BaseArrayAdapter<ReportType> {

    /**选中*/
    public Integer checkPosition = -1;

    public List<ReportType> data;

	public ReportAdapter(Context context, List<ReportType> data) {
		super(context, R.layout.adapter_report, data);
        this.data = data;

        checkPosition = 0;
    }

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		final ReportType record = getItem(position);
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.adapter_report, null);
			holder.reason = (TextView) view.findViewById(R.id.report_reason);
            holder.checkState = (CheckBox) view.findViewById(R.id.report_checkState);
            holder.checkButton = (TextView) view.findViewById(R.id.report_checkButton);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

        holder.reason.setText(record.getName());

        if (checkPosition == position) {
            holder.checkState.setChecked(true);
        } else {
            holder.checkState.setChecked(false);
        }

        holder.checkButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkPosition = position;
                notifyDataSetChanged();
            }
        });

        return view;
	}

	private static class ViewHolder {

        TextView reason;
        CheckBox checkState;
        TextView checkButton;
	}
}
