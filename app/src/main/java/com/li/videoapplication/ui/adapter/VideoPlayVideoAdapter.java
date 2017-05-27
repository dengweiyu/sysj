package com.li.videoapplication.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.activity.VideoPlayActivity;
import com.li.videoapplication.utils.StringUtil;

import java.util.List;

/**
 * 适配器：玩家视频
 */
@SuppressLint("InflateParams")
public class VideoPlayVideoAdapter extends BaseArrayAdapter<VideoImage> {

    private Activity activity;

    /**
     * 跳转：视频播放
     */
    private void startVideoPlayActivity(VideoImage videoImage) {
        ActivityManager.startVideoPlayActivity(getContext(), videoImage);
        if (activity != null && activity instanceof VideoPlayActivity) {
            UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.VIDEOPLAY, "TA的视频-有效");
        }
    }

    public VideoPlayVideoAdapter(Context context, List<VideoImage> data) {
        super(context, R.layout.adapter_videoplay_video, data);
        try {
            activity = (Activity) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final VideoImage record = getItem(position);
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_videoplay_video, null);
            holder.cover = (ImageView) view.findViewById(R.id.videoplay_vedio_cover);
            holder.allTime = (TextView) view.findViewById(R.id.videoplay_vedio_allTime);
            holder.title = (TextView) view.findViewById(R.id.videoplay_vedio_title);
            holder.playCount = (TextView) view.findViewById(R.id.videoplay_vedio_playCount);
            holder.good = (ImageView) view.findViewById(R.id.videoplay_vedio_good);
            holder.likeCount = (TextView) view.findViewById(R.id.videoplay_vedio_likeCount);
            holder.comment = (ImageView) view.findViewById(R.id.videoplay_vedio_comment);
            holder.commentCount = (TextView) view.findViewById(R.id.videoplay_vedio_commentCount);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setImageViewImageNetAlpha(holder.cover, record.getFlag());
        setTimeLength(holder.allTime, record);
        setTextViewText(holder.title, record.getTitle());
        setTextViewText(holder.playCount, StringUtil.formatNum(record.getClick_count()));
        setTextViewText(holder.likeCount, record.getFlower_count());
        setTextViewText(holder.commentCount, record.getComment_count());

        // 评论
        setComment(record, holder.comment);

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                record.setId(record.getVideo_id());
                startVideoPlayActivity(record);
            }
        });

        setListViewLayoutParams(view, 76);

        return view;
    }

    /**
     * 视频时间长度
     */
    private void setTimeLength(TextView view, VideoImage record) {

        try {
            setTextViewText(view, TimeHelper.getVideoPlayTime(record.getTime_length()));
        } catch (Exception e) {
            e.printStackTrace();
            setTextViewText(view, "");
        }
    }

    /**
     * 点赞
     */
    public void setGood(final VideoImage record, ImageView view) {
        if (record != null) {
            if (record.getFlower_tick() == 1) {// 已点赞状态
                view.setImageResource(R.drawable.videoplay_good_red_205);
            } else {// 未点赞状态
                view.setImageResource(R.drawable.videoplay_good_gray_205);
            }
        }

        view.setVisibility(View.VISIBLE);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!isLogin()) {
                    showToastShort("请先登录！");
                    return;
                }
                if (StringUtil.isNull(record.getFlower_count())) {
                    record.setFlower_count("0");
                }
                if (record.getFlower_tick() == 1) {// 已点赞状态
                    record.setFlower_count(Integer.valueOf(record.getFlower_count()) - 1 + "");
                    record.setFlower_tick(0);
                } else {// 未点赞状态
                    record.setFlower_count(Integer.valueOf(record.getFlower_count()) + 1 + "");
                    record.setFlower_tick(1);
                }
                // 视频点赞
                DataManager.videoFlower2(record.getVideo_id(), getMember_id());
                notifyDataSetChanged();
            }
        });
    }

    /**
     * 评论
     */
    private void setComment(final VideoImage record, ImageView view) {
        view.setEnabled(false);
        view.setClickable(false);
    }

    private static class ViewHolder {

        ImageView cover;
        TextView allTime;
        TextView title;
        TextView playCount;
        ImageView good;
        TextView likeCount;
        ImageView comment;
        TextView commentCount;
    }
}
