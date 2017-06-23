package com.li.videoapplication.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.image.MyLocalVideoImageLoader;
import com.li.videoapplication.data.image.VideoDurationHelper;
import com.li.videoapplication.data.local.FileUtil;
import com.li.videoapplication.data.local.VideoCaptureHelper;
import com.li.videoapplication.tools.IntentHelper;
import com.li.videoapplication.views.SmoothCheckBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 适配器：上传比赛结果视频
 */
public class ChooseLocalVideoAdapter extends RecyclerView.Adapter<ChooseLocalVideoAdapter.VideoViewHolder> {
    private final VideoDurationHelper durationHelper;
  //  public MyLocalVideoImageLoader imageLoader;

    private List<VideoCaptureEntity> data;
    private Context context;
    private VideoCaptureEntity record;
    //存储视频地址集合类
    public String[] URLS;
    private Map<Integer, Boolean> isSelected;

    public ChooseLocalVideoAdapter(final Context context, List<VideoCaptureEntity> data, RecyclerView recyclerView) {
        this.data = data;
        this.context = context;
        URLS = new String[this.data.size()];
        for (int i = 0; i < this.data.size(); i++) {
            URLS[i] = data.get(i).getVideo_path();
        }
       // imageLoader = new MyLocalVideoImageLoader(recyclerView, URLS);
        durationHelper = new VideoDurationHelper(recyclerView);

        initCheckBox();
    }

    public Map<Integer, Boolean> getSelecteMap(){
        return isSelected;
    }

    private void initCheckBox() {
        if (isSelected != null) {
            isSelected = null;
        }
        isSelected = new HashMap<>();
        for (int i = 0; i < data.size(); i++) {
            isSelected.put(i, false);
        }
    }

    public VideoCaptureEntity getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_uploadlocalvideo, parent, false));
    }

    @Override
    public void onBindViewHolder(final VideoViewHolder holder, final int position) {
        record = getItem(position);

        final String filePath = record.getVideo_path();
        final String fileName = record.getVideo_name();

        // 视频缩略图
        GlideHelper.displayImage(context,filePath,holder.cover);

        holder.title.setText(fileName);
        holder.createTime.setText(VideoCaptureHelper.getLastModified(record.getVideo_path()));

        // 视频时长
        holder.allTime.setTag(filePath + filePath);
        durationHelper.displayDuration(filePath, holder.allTime);

        // 视频大小
        holder.size.setText(FileUtil.formatFileSize(FileUtil.getFileSize((filePath))));

        holder.checkState.setChecked(isSelected.get(position));

    }

    public void notifyDataChanged(){
        URLS = new String[data.size()];
        for (int i = 0; i < data.size(); i++) {
            URLS[i] = data.get(i).getVideo_path();
        }
        notifyDataSetChanged();
        initCheckBox();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // 视频封面,播放图标
        ImageView cover, play;
        // 视频名称,保存时间,视频总长,视频大小
        TextView title, allTime, createTime, size;
        // 选中状态
        SmoothCheckBox checkState;
        View root;

        public VideoViewHolder(View view) {
            super(view);
            cover = (ImageView) view.findViewById(R.id.uploadvideo_cover);
            play = (ImageView) view.findViewById(R.id.uploadvideo_play);
            title = (TextView) view.findViewById(R.id.uploadvideo_title);
            allTime = (TextView) view.findViewById(R.id.uploadvideo_allTime);
            createTime = (TextView) view.findViewById(R.id.uploadvideo_createTime);
            size = (TextView) view.findViewById(R.id.uploadvideo_size);
            checkState = (SmoothCheckBox) view.findViewById(R.id.uploadvideo_checkState);
            root = view.findViewById(R.id.uploadvideo_root);
            cover.setOnClickListener(this);
            checkState.setOnClickListener(this);
            root.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.uploadvideo_cover:
                    try {
                        VideoCaptureEntity item = getItem(getAdapterPosition());
                        IntentHelper.startActivityActionViewMp4(context, item.getVideo_path());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.uploadvideo_checkState:
                    Boolean selected = !isSelected.get(getAdapterPosition());
                    // 先将所有的置为FALSE
                    for (Integer p : isSelected.keySet()) {
                        isSelected.put(p, false);
                    }
                    // 再将当前选择CheckBox的实际状态
                    isSelected.put(getAdapterPosition(), selected);
                    notifyDataSetChanged();
                    break;
                case R.id.uploadvideo_root:
                    checkState.performClick();
                    break;
            }
        }
    }
}
