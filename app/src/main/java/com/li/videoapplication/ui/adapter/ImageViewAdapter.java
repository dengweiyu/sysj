package com.li.videoapplication.ui.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.ui.activity.ImageViewActivity;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.utils.ScreenUtil;

/**
 * 适配器：图片选择
 */
public class ImageViewAdapter extends BaseAdapter {

    private String directory;
	private List<String> data;
	private LayoutInflater inflater;
	private ImageViewActivity activity;
	protected int srceenWidth, srceenHeight;
	protected WindowManager windowManager;

	@SuppressWarnings("deprecation")
	public ImageViewAdapter(Context context, List<String> data, String directory) {
		this.directory = directory;
		this.data = data;

		try {
			this.activity = (ImageViewActivity) context;
		} catch (Exception e) {
			e.printStackTrace();
		}

		inflater = LayoutInflater.from(context);
		windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	    srceenWidth = windowManager.getDefaultDisplay().getWidth();
	    srceenHeight = windowManager.getDefaultDisplay().getHeight();
	}

	@Override
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
	public View getView(int position, View view, ViewGroup parent) {
        final String fileName = (String) getItem(position);
		final ViewHolder holder;
		if (view == null) {
            holder = new ViewHolder();
			view = inflater.inflate(R.layout.adapter_imageview, parent, false);
			holder.pic = (ImageView) view.findViewById(R.id.imageview_pic);
			holder.click = (ImageButton) view.findViewById(R.id.imageview_click);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		setPicLayoutParams(holder.pic);
		
		// 重置状态
		holder.pic.setImageBitmap(null);
		holder.pic.setColorFilter(null);
		holder.click.setImageResource(R.drawable.choose_gray);

        final String filePath = directory + "/" + fileName;
        GlideHelper.displayImageWhite(activity,filePath, holder.pic);

		holder.pic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ImageViewActivity.imageViewDeleteData.contains(filePath)) {// 将加进的图片移出set
					ImageViewActivity.imageViewDeleteData.remove(filePath);
					holder.pic.setColorFilter(null);
					holder.click.setImageResource(R.drawable.choose_gray);
					activity.refreshActionBar(ImageViewActivity.imageViewDeleteData.size());
				} else {// 将选择的图片加进set
					if (ImageViewActivity.imageViewDeleteData.size() < 9) {
						ImageViewActivity.imageViewDeleteData.add(filePath);
						holder.pic.setColorFilter(Color.parseColor("#77000000"));
						holder.click.setImageResource(R.drawable.choose_green);
						activity.refreshActionBar(ImageViewActivity.imageViewDeleteData.size());
					} else {
						ToastHelper.s("最多只能选择9张图片");
					}
				}
			}
		});

		if (ImageViewActivity.imageViewDeleteData.contains(filePath)) {
			holder.pic.setColorFilter(Color.parseColor("#77000000"));
			holder.click.setImageResource(R.drawable.choose_green);
		}
		
		return view;
	}
	
	private void setPicLayoutParams(View view) {
		// 4 + 4*3 + 4 = 20
		int w = (srceenWidth - ScreenUtil.dp2px(20))/4;
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, w);
		view.setLayoutParams(params);
	}

	private static class ViewHolder {
		ImageView pic;
		ImageButton click;
	}
}
