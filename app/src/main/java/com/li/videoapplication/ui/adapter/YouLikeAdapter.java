package com.li.videoapplication.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.mvp.home.view.HomeFragment;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.URLUtil;
import com.li.videoapplication.views.CircleImageView;

/**
 * 适配器：猜你喜欢
 */
@SuppressLint("InflateParams")
public class YouLikeAdapter extends BaseArrayAdapter<VideoImage> {

    /**
     * 跳转：视频播放
     */
    private void startVideoPlayActivity(VideoImage videoImage) {
        ActivityManager.startVideoPlayActivity(getContext(), videoImage);
    }

    public YouLikeAdapter(Context context, List<VideoImage> data) {
        super(context, R.layout.adapter_video, data);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final VideoImage record = getItem(position);
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_video, null);
            holder.title = (TextView) view.findViewById(R.id.video_title);
            holder.playCount = (TextView) view.findViewById(R.id.video_playCount);
            holder.play = (ImageView) view.findViewById(R.id.video_play);
            holder.cover = (ImageView) view.findViewById(R.id.video_cover);
            holder.allTime = (TextView) view.findViewById(R.id.video_allTime);
            holder.deleteState = (CheckBox) view.findViewById(R.id.vedio_deleteState);
            holder.adLogo = (ImageView)view.findViewById(R.id.iv_ad_logo);
            holder.userIamgeView= (CircleImageView) view.findViewById(R.id.civ_user);
            holder.nikename= (TextView) view.findViewById(R.id.tv_up_user_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setPicLayoutParams(holder.cover);

        setTextViewText(holder.title, record.getTitle());

        if (record.isAD() && record.getVideo_id().equals("1")) {//广告
            holder.playCount.setVisibility(View.GONE);
            holder.play.setVisibility(View.GONE);
            holder.allTime.setVisibility(View.GONE);
            //TODO 是广告就不显示上传主的名字和头像,需要与产品确定要求
            holder.userIamgeView.setVisibility(View.GONE);
            holder.nikename.setVisibility(View.GONE);
          //  setTextViewText(holder.allTime, "广告");
            holder.adLogo.setVisibility(View.VISIBLE);
        } else {
            holder.playCount.setVisibility(View.VISIBLE);
            holder.play.setVisibility(View.VISIBLE);
            holder.adLogo.setVisibility(View.GONE);
            holder.userIamgeView.setVisibility(View.VISIBLE);
            if (!StringUtil.isNull(record.getClick_count())) {
                //播放数格式成以万为单位
                String clickCount = StringUtil.toUnitW(record.getClick_count());
                setTextViewText(holder.playCount, clickCount);
            }
            setTimeLength(holder.allTime, record);
        }

        setImageViewImageNetAlpha(holder.cover, record.getFlagPath());
        //判断是否符合要求，然后组装上传主的是视图
        if (!StringUtil.isNull(record.getAvatar())){
            if (URLUtil.isURL(record.getAvatar())){
                setImageViewImageNetAlpha(holder.userIamgeView,record.getAvatar());
            }
        }
        if (!StringUtil.isNull(record.getNickname())){
            setTextViewText(holder.nikename,record.getNickname());
        }
        holder.deleteState.setVisibility(View.GONE);


        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (record.isAD()) {
                    HomeFragment.adItem.onExposured(v);
                    HomeFragment.adItem.onClicked(v);
                } else {
                    startVideoPlayActivity(record);
                    UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.MAIN, "猜你喜欢的视频");
                }
            }
        });
        return view;
    }


    private void setPicLayoutParams(View view) {

        if (view != null) {
            // 86/148
            int w = (srceenWidth - ScreenUtil.dp2px(10 * 2)) / 2;
            int h = w * 9 / 16;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, h);
            view.setLayoutParams(params);
        }
    }

    /**
     * 视频时间长度
     */
    private void setTimeLength(TextView view, VideoImage record) {
        // 17:00
        try {
            setTextViewText(view, TimeHelper.getVideoPlayTime(record.getTime_length()));
        } catch (Exception e) {
            e.printStackTrace();
            setTextViewText(view, "");
        }
    }

    private static class ViewHolder {
        TextView title;
        ImageView play;
        ImageView cover;
        TextView playCount;
        TextView allTime;
        CheckBox deleteState;
        ImageView adLogo;
        CircleImageView userIamgeView;
        TextView nikename;
    }
}
