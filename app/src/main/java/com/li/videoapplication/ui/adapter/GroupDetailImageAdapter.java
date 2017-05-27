package com.li.videoapplication.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.utils.ScreenUtil;
/**
 * 适配器：圈子详情-图文
 */
@SuppressLint("InflateParams") 
public class GroupDetailImageAdapter extends BaseArrayAdapter<String> {
	
	/**
	 * 跳转：图文详情
	 */
	private void startImageDetailActivity(VideoImage videoImage) {
		ActivityManager.startImageDetailActivity(getContext(), videoImage);
	}
	
	private VideoImage videoImage;

	public GroupDetailImageAdapter(Context context, List<String> data, VideoImage record) {
		super(context, R.layout.adapter_groupdetail_image, data);
		this.videoImage = record;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final String record = getItem(position);
		ViewHolder holder = null;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.adapter_groupdetail_image, null);
			holder.pic = (ImageView) view.findViewById(R.id.groupdetail_image_pic);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		setLayoutParams(holder.pic);
		
		setImageViewImageNet(holder.pic, record);
		
		view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (videoImage.getPic_id() != null && !videoImage.getPic_id().equals("0")) { // 图文
					startImageDetailActivity(videoImage);
				}
			}
		});
		
		return view;
	}
	
	private void setLayoutParams(View view) {
		// 10 + 38 + 10 + 36 = 94
		// 2*4 = 8
		// 102
		int w = (srceenWidth - ScreenUtil.dp2px(102))/3;
		setLinearLayoutLayoutParams(view, w, w);
	}

	private class ViewHolder {
		ImageView pic;
	}
}
