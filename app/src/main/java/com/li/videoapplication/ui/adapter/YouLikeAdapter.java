package com.li.videoapplication.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.utils.ScreenUtil;

/**
 * 适配器：猜你喜欢
 */
@SuppressLint("InflateParams") 
public class YouLikeAdapter extends BaseArrayAdapter<VideoImage> {
	
	/**
	 * 跳转：视频播放
	 */
	private void startVideoPlayActivity(VideoImage videoImage) {
		ActivityManeger.startVideoPlayActivity(getContext(), videoImage);
	}

	public YouLikeAdapter(Context context, List<VideoImage> data) {
		super(context, R.layout.adapter_video, data);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		final VideoImage record = getItem(position);
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.adapter_video, null);
			holder.title = (TextView) view.findViewById(R.id.video_title);
			holder.playCount = (TextView) view.findViewById(R.id.video_playCount);
			holder.play = (ImageView) view.findViewById(R.id.video_play);
			holder.cover = (ImageView) view.findViewById(R.id.video_cover);
			holder.allTime = (TextView) view.findViewById(R.id.video_allTime);
			holder.deleteState = (CheckBox) view.findViewById(R.id.vedio_deleteState);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		setPicLayoutParams(holder.cover);

		setTextViewText(holder.title, record.getTitle());
		setTextViewText(holder.playCount, record.getClick_count());
		setTimeLength(holder.allTime, record);
		setImageViewImageNetAlpha(holder.cover, record.getFlagPath());
		holder.deleteState.setVisibility(View.GONE);
		
		view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				startVideoPlayActivity(record);
				UmengAnalyticsHelper.onEvent(getContext(),UmengAnalyticsHelper.MAIN,"猜你喜欢的视频");
			}
		});
		
		return view;
	}


	private void setPicLayoutParams(View view) {
		
		if (view != null) {
//			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
//			int w = params.width;
//			params.height = w*9/16;;
//			view.setLayoutParams(params);
			// 86/148
			int w = (srceenWidth - ScreenUtil.dp2px(10 * 2))/2;
			int h = w*9/16;
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, h);
			view.setLayoutParams(params);
		}
	}

	/**
	 * 视频时间长度
	 */
	private void setTimeLength(TextView view, VideoImage record) {
		// 17:00
		try {
			setTextViewText(view, TimeHelper.getVideoPlayTime(record.getTime_length()));
		} catch (Exception e) {
			e.printStackTrace();
			setTextViewText(view, "");
		}
	}

	private static class ViewHolder {
		TextView title;
		ImageView play;
		ImageView cover;
		TextView playCount;
		TextView allTime;
        CheckBox deleteState;
	}
}
