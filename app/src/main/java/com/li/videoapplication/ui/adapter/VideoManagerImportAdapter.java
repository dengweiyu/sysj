package com.li.videoapplication.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.preferences.VideoPreferences;
import com.li.videoapplication.ui.fragment.MyLocalVideoFragment;
import com.li.videoapplication.views.SmoothCheckBox;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 适配器：导入外部视频
 */
public class VideoManagerImportAdapter extends BaseAdapter implements AbsListView.OnScrollListener {

    private static final String TAG = VideoManagerImportAdapter.class.getSimpleName();
    // 是否打钩
    private List<Boolean> checkData;
    // 是否已经导入
    private List<Boolean> extData;
    private List<VideoCaptureEntity> data;
    private Context context;
    private LayoutInflater inflater;
    private ViewHolder holder;
    private MyLocalVideoFragment fragment;


    public VideoManagerImportAdapter(Context context, ListView listView, List<VideoCaptureEntity> list, MyLocalVideoFragment fragment) {
        this.data = list;
        this.context = context;
        this.fragment = fragment;

        checkData = new ArrayList<>();
        inflater = LayoutInflater.from(context);
        extData = new ArrayList<>();

        String[] filePaths = new String[data.size()];
        for (int i = 0; i < data.size(); i++) {
            filePaths[i] = data.get(i).getVideo_path();
        }
        listView.setOnScrollListener(this);

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
            convertView = inflater.inflate(R.layout.adapter_videomanagerimport, null);
            holder.pic = (ImageView) convertView.findViewById(R.id.videomanagerimport_pic);
            holder.title = (TextView) convertView.findViewById(R.id.videomanagerimport_title);
            holder.state = (TextView) convertView.findViewById(R.id.videomanagerimport_state);
            holder.check = (SmoothCheckBox) convertView.findViewById(R.id.videomanagerimport_check);
            holder.root = convertView.findViewById(R.id.videomanagerimport_root);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        File f = new File(record.getVideo_path());
        holder.title.setText(f.getName());

    //    holder.pic.setTag(record.getVideo_path());

        GlideHelper.displayImage(context,record.getVideo_path(),holder.pic);

        // 对某个外部视频是否导入进行判断
        if (extData.get(postion)) {
            holder.check.setVisibility(View.GONE);
            holder.state.setVisibility(View.VISIBLE);
        } else {
            holder.check.setVisibility(View.VISIBLE);
            holder.state.setVisibility(View.GONE);
        }
        holder.check.setChecked(checkData.get(postion));

        holder.check.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {

                if (isChecked) {
                    checkData.set(postion, true);
                    // 将选择结果放入列表，用于刷新页面
                    fragment.myImportData.add(record);
                } else {
                    checkData.set(postion, false);
                    fragment.myImportData.remove(record);
                }
            }
        });
        return convertView;
    }

    // -----------------------------------------------------------------------



    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    // -----------------------------------------------------------------------

    private static class ViewHolder {
        ImageView pic;
        TextView title;
        TextView state;
        SmoothCheckBox check;
        View root;
    }
}