package com.li.videoapplication.ui.adapter;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.VideoPlayActivity;
import com.li.videoapplication.utils.StringUtil;

import java.util.List;

/**
 * 适配器：玩家视频
 */
@SuppressLint("InflateParams")
public class VideoPlayVideoAdapter2 extends BaseArrayAdapter<VideoImage> {

    private VideoPlayActivity activity;

    public VideoPlayVideoAdapter2(VideoPlayActivity context, List<VideoImage> data) {
        super(context, R.layout.adapter_videoplay_video2, data);
        this.activity = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final VideoImage record = getItem(position);
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_videoplay_video2, null);
            holder.cover = (ImageView) view.findViewById(R.id.videoplay_vedio_cover);
            holder.videocount = (TextView) view.findViewById(R.id.videoplay_vedio_videocount);
            holder.title = (TextView) view.findViewById(R.id.videoplay_vedio_title);
            holder.description = (TextView) view.findViewById(R.id.videoplay_vedio_description);
            holder.playCount = (TextView) view.findViewById(R.id.videoplay_vedio_playCount);
            holder.time = (TextView) view.findViewById(R.id.videoplay_vedio_time);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setImageViewImageNetAlpha(holder.cover, record.getFlag());

        setTextViewText(holder.videocount, record.getVideocount());

        setTextViewText(holder.title, record.getName());

        setTextViewText(holder.description, record.getDescription());

        setTextViewText(holder.playCount, StringUtil.formatNum(record.getTotalClick()));

        try {
            String time = TimeHelper.getTime2MdFormat(record.getTime()) + " 更新";
            setTextViewText(holder.time, time);
        } catch (Exception e) {
            e.printStackTrace();
        }

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                activity.startAuthorVideoListFragment(activity.item.getMember_id(), record.getGame_id(), record.getName());
                UmengAnalyticsHelper.onEvent(getContext(),UmengAnalyticsHelper.VIDEOPLAY, "TA的视频-视频合集");
            }
        });

        setListViewLayoutParams(view, 76);

        return view;
    }


    private static class ViewHolder {

        ImageView cover;
        TextView videocount;
        TextView title;
        TextView description;
        TextView playCount;
        TextView time;
    }
}
