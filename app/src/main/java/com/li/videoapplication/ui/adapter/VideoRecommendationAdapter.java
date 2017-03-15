package com.li.videoapplication.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.framework.BaseBaseAdapter;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.VideoPlayActivity;
import com.li.videoapplication.utils.LogHelper;
import com.li.videoapplication.utils.StringUtil;

import java.util.List;

/**
 * 适配器：播放完后的视频推荐
 */
public class VideoRecommendationAdapter extends BaseBaseAdapter {

    private boolean maxValue;
    private List<List<VideoImage>> data;
    private VideoPlayActivity activity;

    public VideoRecommendationAdapter(VideoPlayActivity activity, List<List<VideoImage>> data) {
        LogHelper.d(tag, "VideoRecommendationAdapter constructor");
        this.activity = activity;
        this.data = data;
        inflater = LayoutInflater.from(activity);
    }

    /**
     * 跳转：视频播放
     */
    private void startVideoPlayActivity208(VideoImage item, boolean isLandscape) {
        ActivityManeger.startVideoPlayActivity208(activity, item, isLandscape);
        UmengAnalyticsHelper.onEvent(activity, UmengAnalyticsHelper.MACROSCOPIC_DATA, "视频播放完推荐视频点击数");
    }

    @Override
    protected Context getContext() {
        return activity;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public List<VideoImage> getItem(int position) {
        LogHelper.d(tag, "getItem position : " + position);
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        LogHelper.d(tag, "getItemId position : " + position);
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LogHelper.d(tag, "getView position: " + position);
        final List<VideoImage> record = getItem(position);
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_videorecommend, null);
            holder.cover1 = (ImageView) view.findViewById(R.id.complete_cover1);
            holder.cover2 = (ImageView) view.findViewById(R.id.complete_cover2);
            holder.text1 = (TextView) view.findViewById(R.id.complete_text1);
            holder.text2 = (TextView) view.findViewById(R.id.complete_text2);
            holder.video1 = view.findViewById(R.id.complete_video1);
            holder.video2 = view.findViewById(R.id.complete_video2);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (!StringUtil.isNull(record.get(0).getFlagPath())) {
            GlideHelper.displayImage(getContext(), record.get(0).getFlagPath(), holder.cover1);
        }
        if (!StringUtil.isNull(record.get(1).getFlagPath())) {
            GlideHelper.displayImage(getContext(), record.get(1).getFlagPath(), holder.cover2);
        }

        setTextViewText(holder.text1, record.get(0).getTitle());
        setTextViewText(holder.text2, record.get(1).getTitle());

        final boolean isLandscape = activity.isLandscape();

        holder.video1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startVideoPlayActivity208(record.get(0), isLandscape);
            }
        });

        holder.video2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVideoPlayActivity208(record.get(1), isLandscape);
            }
        });

        return view;
    }


    private class ViewHolder {
        ImageView cover1, cover2;
        TextView text1, text2;
        View video1, video2;
    }
}
