package com.li.videoapplication.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.MyDynamicActivity;
import com.li.videoapplication.ui.activity.SquareActivity;
import com.li.videoapplication.ui.fragment.LikeBillboardFragment;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;
import com.li.videoapplication.views.CircleImageView;
import com.li.videoapplication.views.GridViewY1;

import java.util.List;

/**
 * 适配器：圈子详情（最新/最热视频）；广场（最新，最热）; 风云榜视频 ; 动态视频;
 */
@SuppressLint("InflateParams")
public class GroupDetailVideoAdapter extends BaseArrayAdapter<VideoImage> {

    private Activity activity;
    private int tab = 0;

    /**
     * 跳转：视频播放
     */
    private void startVideoPlayActivity(VideoImage videoImage) {
        ActivityManeger.startVideoPlayActivity(getContext(), videoImage);
    }

    /**
     * 跳转：玩家动态
     */
    private void startPlayerDynamicActivity(Member member) {
        ActivityManeger.startPlayerDynamicActivity(getContext(), member);
    }

    public GroupDetailVideoAdapter(Context context, List<VideoImage> data) {
        super(context, R.layout.adapter_groupdetail_video, data);
        try {
            activity = (Activity) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTab(int tab) {
        this.tab = tab;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final VideoImage record = getItem(position);
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_groupdetail_video, null);
            holder.head = (CircleImageView) view.findViewById(R.id.groupdetail_head);
            holder.head_v = (ImageView) view.findViewById(R.id.groupdetail_v);
            holder.name = (TextView) view.findViewById(R.id.groupdetail_name);
            holder.time = (TextView) view.findViewById(R.id.groupdetail_time);
            holder.content = (TextView) view.findViewById(R.id.groupdetail_content);
            holder.cover = (ImageView) view.findViewById(R.id.groupdetail_cover);
            holder.centerPlay = (ImageView) view.findViewById(R.id.groupdetail_centerPlay);
            holder.play = (ImageView) view.findViewById(R.id.groupdetail_play);
            holder.allTime = (TextView) view.findViewById(R.id.groupdetail_allTime);
            holder.playCount = (TextView) view.findViewById(R.id.groupdetail_playCount);
            holder.like = (ImageView) view.findViewById(R.id.groupdetail_like);
            holder.likeCount = (TextView) view.findViewById(R.id.groupdetail_likeCount);
            holder.star = (ImageView) view.findViewById(R.id.groupdetail_star);
            holder.starCount = (TextView) view.findViewById(R.id.groupdetail_starCount);
            holder.comment = (ImageView) view.findViewById(R.id.groupdetail_comment);
            holder.commentCount = (TextView) view.findViewById(R.id.groupdetail_commentCount);
            holder.image = (RelativeLayout) view.findViewById(R.id.groupdetail_image);
            holder.video = (RelativeLayout) view.findViewById(R.id.groupdetail_video);
            holder.grid = (GridViewY1) view.findViewById(R.id.gridview);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (record.isV()) {
            holder.head_v.setVisibility(View.VISIBLE);
        } else {
            holder.head_v.setVisibility(View.INVISIBLE);
        }

        setImageViewImageNet(holder.head, record.getAvatar());
        setTextViewText(holder.name, record.getNickname());
        setTextViewText(holder.content, record.getTitle());

        if (tab != 0) {
            if (tab == 1) {
                holder.likeCount.setText(Html.fromHtml(TextUtil.toColor(record.getFlower_count(), "#ff3d2e")));
                setTextViewText(holder.commentCount, record.getComment_count());
            } else if (tab == 2) {
                setTextViewText(holder.likeCount, record.getFlower_count());
                holder.commentCount.setText(Html.fromHtml(TextUtil.toColor(record.getComment_count(), "#ff3d2e")));
            } else {
                setTextViewText(holder.likeCount, record.getFlower_count());
                setTextViewText(holder.commentCount, record.getComment_count());
            }
        } else {
            setTextViewText(holder.likeCount, record.getFlower_count());
            setTextViewText(holder.commentCount, record.getComment_count());
        }

        setTextViewText(holder.starCount, record.getCollection_count());
        setTime(record, holder.time);

        if (isVideo(record)) {// 视频
            holder.video.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);

            setTimeLength(holder.allTime, record);

            if (tab != 0 && tab == 3) {
                holder.playCount.setText(Html.fromHtml(TextUtil.toColor(record.getClick_count(), "#ff3d2e")));
            } else {
                setTextViewText(holder.playCount, record.getClick_count());// 785
            }
            if (!StringUtil.isNull(record.getVideo_flag())) {
                setImageViewImageNet(holder.cover, record.getVideo_flag());
            } else if (!StringUtil.isNull(record.getFlag())) {
                setImageViewImageNet(holder.cover, record.getFlag());
            }
        }
        if (isImage(record)) { // 图文
            holder.video.setVisibility(View.GONE);
            holder.image.setVisibility(View.VISIBLE);
            List<String> data = record.getPic_urls();
            GroupDetailImageAdapter dapter = new GroupDetailImageAdapter(getContext(), data, record);
            holder.grid.setAdapter(dapter);
            dapter.notifyDataSetChanged();
        }
        if (StringUtil.isNull(record.getPic_id()) && StringUtil.isNull(record.getVideo_id()) && !StringUtil.isNull(record.getId())) { // 搜索视频
            holder.video.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);

            setTimeLength(holder.allTime, record);
            setImageViewImageNet(holder.cover, record.getFlagPath());
            setTextViewText(holder.name, record.getUserName());
            setTextViewText(holder.content, record.getName());
        }

        // 点赞设置
        setLike(record, holder.like);

        // 收藏设置
        setStar(record, holder.star);

        // 评论
        setComment(record, holder.comment);

        holder.head.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Member member = gson.fromJson(record.toJSON(), Member.class);
                startPlayerDynamicActivity(member);
                if (activity instanceof MyDynamicActivity){
                    UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.DISCOVER, "动态-他人");
                }
            }
        });

        holder.video.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (record.getVideo_id() != null && !record.getVideo_id().equals("0")) {// 视频
                    startVideoPlayActivity(record);
                    if (activity instanceof SquareActivity) {
                        UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.DISCOVER, "广场-视频");
                    }else if (activity instanceof MyDynamicActivity){
                        UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.DISCOVER, "动态-视频");
                    }
                }
            }
        });

        return view;
    }

    /**
     * 发布时间
     */
    private void setTime(final VideoImage record, TextView view) {
        // 1小时前
        if (view != null && record != null) {
            if (!StringUtil.isNull(record.getUpload_time())) {
                try {
                    setTextViewText(view, TimeHelper.getVideoImageUpTime(record.getUpload_time()));
                } catch (Exception e) {
                    e.printStackTrace();
                    setTextViewText(view, "");
                }
            } else {
                setTextViewText(view, "");
            }
        }
    }

    /**
     * 点赞
     */
    public void setLike(final VideoImage record, ImageView view) {

        if (StringUtil.isNull(record.getPic_id()) && StringUtil.isNull(record.getVideo_id()) && !StringUtil.isNull(record.getId())) { // 搜索视频
            return;
        }
        if (record.getFlower_tick() == 1) {// 已点赞状态
            view.setImageResource(R.drawable.videoplay_good_red_205);
        } else {// 未点赞状态
            view.setImageResource(R.drawable.videoplay_good_gray_205);
        }

        view.setVisibility(View.VISIBLE);
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!isLogin()) {
                    showToastShort("请先登录！");
                    return;
                }
                if (StringUtil.isNull(record.getFlower_count())) {
                    record.setFlower_count("0");
                }
                if (isVideo(record)) {// 视频
                    if (record.getFlower_tick() == 1) {// 已点赞状态
                        record.setFlower_count(Integer.valueOf(record.getFlower_count()) - 1 + "");
                        record.setFlower_tick(0);
                    } else {// 未点赞状态
                        record.setFlower_count(Integer.valueOf(record.getFlower_count()) + 1 + "");
                        record.setFlower_tick(1);
                    }
                    // 视频点赞
                    DataManager.videoFlower2(record.getVideo_id(), getMember_id());
                }
                if (isImage(record)) { // 图文
                    int flag = record.getFlower_tick();
                    if (flag == 1) {// 已点赞状态
                        record.setFlower_count(Integer.valueOf(record.getFlower_count()) - 1 + "");
                        record.setFlower_tick(0);
                    } else {// 未点赞状态
                        record.setFlower_count(Integer.valueOf(record.getFlower_count()) + 1 + "");
                        record.setFlower_tick(1);
                    }
                    // 献花/取消献花用户
                    DataManager.photoFlower(record.getPic_id(), getMember_id(), flag);

                }
                notifyDataSetChanged();
            }
        });
    }

    /**
     * 收藏
     */
    public void setStar(final VideoImage record, ImageView view) {

        if (StringUtil.isNull(record.getPic_id()) && StringUtil.isNull(record.getVideo_id()) && !StringUtil.isNull(record.getId())) { // 搜索视频

            return;
        }
        if (record.getCollection_tick() == 1) {// 已收藏状态
            view.setImageResource(R.drawable.videoplay_star_red_205);
        } else {// 未收藏状态
            view.setImageResource(R.drawable.videoplay_star_gray_205);
        }

        view.setVisibility(View.VISIBLE);
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!isLogin()) {
                    showToastShort("请先登录！");
                    return;
                }
                if (StringUtil.isNull(record.getCollection_count())) {
                    record.setCollection_count("0");
                }
                if (isVideo(record)) {// 视频
                    if (record.getCollection_tick() == 1) {// 已收藏状态
                        record.setCollection_count(Integer.valueOf(record.getCollection_count()) - 1 + "");
                        record.setCollection_tick(0);
                    } else {// 未收藏状态
                        record.setCollection_count(Integer.valueOf(record.getCollection_count()) + 1 + "");
                        record.setCollection_tick(1);
                    }
                    // 视频收藏
                    DataManager.videoCollect2(record.getVideo_id(), getMember_id());
                }
                if (isImage(record)) { // 图文
                    int flag = record.getCollection_tick();
                    ;
                    if (flag == 1) {// 已收藏状态
                        record.setCollection_count(Integer.valueOf(record.getCollection_count()) - 1 + "");
                        record.setCollection_tick(0);
                    } else {// 未收藏状态
                        record.setCollection_count(Integer.valueOf(record.getCollection_count()) + 1 + "");
                        record.setCollection_tick(1);
                    }
                    // 收藏/取收藏花用户
                    DataManager.photoCollection(record.getPic_id(), getMember_id(), flag);
                }
                notifyDataSetChanged();
            }
        });
    }

    /**
     * 评论
     */
    private void setComment(final VideoImage record, ImageView view) {

        if (StringUtil.isNull(record.getPic_id()) && StringUtil.isNull(record.getVideo_id()) && !StringUtil.isNull(record.getId())) { // 搜索视频
            return;
        }

        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startVideoPlayActivity(record);
            }
        });
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

    /**
     * 是否是视频
     */
    private boolean isVideo(final VideoImage record) {
        // 视频
        return !StringUtil.isNull(record.getVideo_id()) && !record.getVideo_id().equals("0");
    }

    /**
     * 是否是图文
     */
    private boolean isImage(final VideoImage record) {
        // 图文
        return !StringUtil.isNull(record.getPic_id()) && !record.getPic_id().equals("0");
    }

    private static class ViewHolder {

        CircleImageView head;
        ImageView head_v;
        TextView name;
        TextView time;

        TextView content;
        ImageView cover;
        TextView allTime;
        TextView playCount;
        ImageView centerPlay;
        ImageView play;

        ImageView like;
        TextView likeCount;
        ImageView star;
        TextView starCount;
        ImageView comment;
        TextView commentCount;

        RelativeLayout video;
        RelativeLayout image;
        GridViewY1 grid;
    }
}
