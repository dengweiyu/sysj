package com.li.videoapplication.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.URLUtil;

import java.util.List;

/**
 * 适配器：活动，我的活动
 * 
 */
@SuppressLint("InflateParams")
public class MyMatchListAdapter extends BaseArrayAdapter<Match> {

	/**
	 * 跳转：赛事详情
	 */
	private void startGameMatchDetailActivity(String event_id) {
		ActivityManeger.startGameMatchDetailActivity(getContext(), event_id);
		UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.SLIDER, "我的赛事有效");
	}

	public MyMatchListAdapter(Context context, List<Match> data) {
		super(context, R.layout.adapter_activity, data);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final Match record = getItem(position);
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.adapter_activity, null);
			holder.bg = (ImageView) view.findViewById(R.id.activity_bg);
			holder.pic = (ImageView) view.findViewById(R.id.activity_pic);
			holder.title = (TextView) view.findViewById(R.id.activity_title);
			holder.time = (TextView) view.findViewById(R.id.activity_time);
			holder.joined = (TextView) view.findViewById(R.id.activity_joined);
			holder.button = (RelativeLayout) view.findViewById(R.id.button);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		setTextViewText(holder.title, record.getTitle());
		setTime(holder.time, record);
		holder.joined.setText("");
		setImageViewImageNet(holder.pic, record.getCover());
		setBg(record, holder.bg);

		holder.button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startGameMatchDetailActivity(record.getEvent_id());
			}
		});

		setPicLayoutParams(holder.pic);

		return view;
	}

	private void setPicLayoutParams(ImageView view) {
		// 9 + 9 + 9 + 9 = 36
		int w = srceenWidth - ScreenUtil.dp2px(36);
		int h = w * 94 / 292;
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
		params.width = w;
		params.height = h;
		view.setLayoutParams(params);
	}

	private class ViewHolder {
		ImageView bg;
		ImageView pic;
		TextView title;
		TextView time;
		TextView joined;// 参加人数
		RelativeLayout button;
	}

	/**
	 * 活动时间
	 */
	private void setTime(TextView view, Match record) {
		// fc9b28
		// 00b4ff
		try {
			String string = "起止时间：<font color=\"#fc9b28\">"
					+ TimeHelper.getTimeFormat(record.getStarttime())
					+ "~" + TimeHelper.getTimeFormat(record.getEndtime()) + "</font>";

			view.setText(Html.fromHtml(string));
		} catch (Exception e) {
			e.printStackTrace();
			view.setText("");
		}
	}

	/**
	 * 赛事状态
	 */
	private void setBg(Match record, ImageView view) {

		switch (record.getStatus()) {
			case "火热":
				setImageViewImageRes(view, R.drawable.activity_red);
				break;
			case "进行中":
				setImageViewImageRes(view, R.drawable.activity_yellow);
				break;
			case "已结束":
				setImageViewImageRes(view, R.drawable.activity_gray);
				break;
			case "报名中":
				setImageViewImageRes(view, R.drawable.match_signingup);
				break;
			default:
				view.setVisibility(View.GONE);
				break;
		}
	}
}
