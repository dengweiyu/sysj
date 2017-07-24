package com.li.videoapplication.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.ui.activity.ImageViewPagerActivity;

/**
 * 适配器：圈子详情-图文
 */
@SuppressLint("InflateParams") 
public class ImageDetailAdapter extends BaseArrayAdapter<String> {

	/**
	 * 跳转：图片浏览
	 */
	public void startImageViewPagerActivity(View source,List<String> data, int position) {
		String[] urls = (String[]) data.toArray(new String[data.size()]);
		if (urls != null && urls.length > 0) {
			ImageViewPagerActivity.startImageViewPagerActivity(getContext(),source, position, urls);
		}
	}
	
	private List<String> data;

	private Context mContext ;
	public ImageDetailAdapter(Context context, List<String> data) {
		super(context, R.layout.adapter_groupdetail_image, data);
		this.data = data;
		mContext = context;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		final String record = getItem(position);
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.adapter_groupdetail_image, null);
			holder.pic = (ImageView) view.findViewById(R.id.groupdetail_image_pic);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		setLayoutParams(holder.pic);


		GlideHelper.displayImage(mContext,record,holder.pic);
		
		view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startImageViewPagerActivity(holder.pic,data, position);
			}
		});
		
		return view;
	}
	
	private void setLayoutParams(View view) {
		// 6*2 = 12
		int width = srceenWidth - dp2px(28) - dp2px(66);
		int w = (width - dp2px(12))/3;
		setLinearLayoutLayoutParams(view, w, w);
	}

	private static class ViewHolder {
		ImageView pic;
	}
}
