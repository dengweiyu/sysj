package com.li.videoapplication.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.MyImportVideoImageLoader;
import com.li.videoapplication.data.preferences.VideoPreferences;
import com.li.videoapplication.ui.activity.VideoChooseActivity;
import com.li.videoapplication.ui.fragment.MyLocalVideoFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 适配器：导入外部视频
 */
public class MyImportVideoAdapter extends BaseAdapter {

    // 是否打钩
    private List<Boolean> checkData;
    // 是否已经导入
    private List<Boolean> extData;
    private List<VideoCaptureEntity> data;
    private Context context;
    private LayoutInflater inflater;
    private ViewHolder holder;
    private MyLocalVideoFragment fragment;
	private VideoChooseActivity activity;
    private MyImportVideoImageLoader loader;

	public MyImportVideoAdapter(Context context, List<VideoCaptureEntity> list, MyLocalVideoFragment fragment) {
		this.data = list;
        this.context = context;
        this.fragment = fragment;

        checkData = new ArrayList<>();
		inflater = LayoutInflater.from(context);
		extData = new ArrayList<>();

        loader = new MyImportVideoImageLoader();

		for (int i = 0; i < list.size(); i++) {
            extData.add(VideoPreferences.getInstance().getBoolean(list.get(i).getVideo_path(), false));
		}
		for (int i = 0; i < list.size(); i++) {
			checkData.add(false);
		}
	}

	public MyImportVideoAdapter(Context context, List<VideoCaptureEntity> list, VideoChooseActivity activity) {
		this.data = list;
		this.context = context;
		this.activity = activity;

		checkData = new ArrayList<>();
		inflater = LayoutInflater.from(context);
		extData = new ArrayList<>();

		loader = new MyImportVideoImageLoader();

		for (int i = 0; i < list.size(); i++) {
			extData.add(VideoPreferences.getInstance().getBoolean(list.get(i).getVideo_path(), false));
		}
		for (int i = 0; i < list.size(); i++) {
			checkData.add(false);
		}
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public VideoCaptureEntity getItem(int postion) {
		return data.get(postion);
	}

	@Override
	public long getItemId(int postion) {
		return postion;
	}

	@Override
	public View getView(final int postion, View convertView, ViewGroup parent) {
        final VideoCaptureEntity record = getItem(postion);
		if (convertView == null) {
            holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.adapter_myimportvideo, null);
			holder.pic = (ImageView) convertView.findViewById(R.id.myimportvideo_pic);
			holder.title = (TextView) convertView.findViewById(R.id.myimportvideo_title);
			holder.state = (TextView) convertView.findViewById(R.id.myimportvideo_state);
			holder.check = (CheckBox) convertView.findViewById(R.id.myimportvideo_check);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		File f = new File(record.getVideo_path());
		holder.title.setText(f.getName());

        loader.displayImage(record.getVideo_path(), holder.pic);

            // 对某个外部视频是否导入进行判断
            if (extData.get(postion)) {
                holder.check.setVisibility(View.GONE);
                holder.state.setVisibility(View.VISIBLE);
            } else {
			holder.check.setVisibility(View.VISIBLE);
			holder.state.setVisibility(View.GONE);
		}
		holder.check.setChecked(checkData.get(postion));
		holder.check.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkData.get(postion)) {
                    checkData.set(postion, false);
					if (fragment != null) {
						fragment.myImportData.remove(record);
					}
					if (activity != null) {
						activity.getMyImportData().remove(record);
					}
                } else {
					checkData.set(postion, true);
					// 将选择结果放入列表，用于刷新页面
					if (fragment != null)
						fragment.myImportData.add(record);
					if (activity != null)
						activity.getMyImportData().add(record);
                }
            }
        });
		return convertView;
	}

	private static class ViewHolder {
		ImageView pic;
		TextView title;
		TextView state;
		CheckBox check;
	}
}