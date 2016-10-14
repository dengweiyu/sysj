package com.li.videoapplication.ui.adapter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.local.ImageDirectoryEntity;
import com.li.videoapplication.data.image.ImageLoaderHelper;
import com.li.videoapplication.ui.activity.ImageViewActivity;
import com.li.videoapplication.ui.popupwindows.ImageDirectoryPopupWindow;

import java.util.List;

/**
 * 适配器：图片文件夹
 */
public class ImageDirectoryAdapter extends ArrayAdapter<ImageDirectoryEntity> {

    private LayoutInflater inflater;
    private String directory;
    private ImageDirectoryPopupWindow popupWindow;
    private int selectedPosition;
    private ImageViewActivity activity;

	public ImageDirectoryAdapter(ImageViewActivity activity, ImageDirectoryPopupWindow popupWindow, List<ImageDirectoryEntity> data, String directory) {
		super(activity, 0, data);
        this.directory = directory;
        this.activity = activity;
        this.popupWindow = popupWindow;
		inflater = LayoutInflater.from(activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        final ImageDirectoryEntity record = getItem(position);
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.adapter_imagedirectory, parent, false);
			holder.pic = (ImageView) convertView.findViewById(R.id.imagedirectory_pic);
			holder.name = (TextView) convertView.findViewById(R.id.imagedirectory_name);
            holder.count = (TextView) convertView.findViewById(R.id.imagedirectory_count);
            holder.check = (CheckBox) convertView.findViewById(R.id.imagedirectory_check);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

        // 重置
        holder.pic.setImageDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        final String filePath = record.getPath() + "/" + record.getFirstFileName();
        ImageLoaderHelper.displayImageWhite4Local(filePath, holder.pic);

        if (directory.equals(record.getPath())) {
            selectedPosition = position;
            holder.check.setChecked(true);
        } else {
            holder.check.setChecked(false);
        }

		holder.name.setText(record.getName());
        holder.count.setText(record.getFileCount() + "张");

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                holder.check.setChecked(true);
                if (activity != null)
                    activity.refreshContentView(record);
                if (popupWindow != null)
                    popupWindow.dismiss();
            }
        });
		
		return convertView;
	}

	private static class ViewHolder {
		ImageView pic;
		TextView name;
        TextView count;
        CheckBox check;
	}
}
