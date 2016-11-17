package com.li.videoapplication.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Comment;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.ActivityDetailActivity208;
import com.li.videoapplication.ui.activity.GroupDetailActivity;
import com.li.videoapplication.ui.fragment.GroupdetailHotVideoFragment;
import com.li.videoapplication.ui.fragment.GroupdetailNewVideoFragment;
import com.li.videoapplication.ui.toast.ToastHelper;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.CircleImageView;
import com.li.videoapplication.views.GridViewY1;
import com.li.videoapplication.views.sparkbutton.SparkButton;

import java.lang.reflect.Field;
import java.util.List;

import cn.nekocode.emojix.Emojix;

/**
 * 适配器：圈子详情（最新/最热视频）,活动页参与活动
 */
@SuppressLint("InflateParams")
public class GroupDetailVideoRecyclerAdapter extends RecyclerView.Adapter<GroupDetailVideoRecyclerAdapter.ViewHolder> {

    private final Resources resources;

    private Context context;
    private List<VideoImage> data;
    private TextImageHelper helper;
    private Gson gson;
    private Activity activity;//引用此适配器页面
    private TBaseFragment fragment;
    private String[] expressionArray;
    private String[] expressionCnArray;

    /**
     * 跳转：视频播放
     */
    private void startVideoPlayActivity(VideoImage videoImage) {
        ActivityManeger.startVideoPlayActivity(context, videoImage);
    }

    /**
     * 跳转：玩家动态
     */
    private void startPlayerDynamicActivity(Member member) {
        ActivityManeger.startPlayerDynamicActivity(context, member);
    }

    public GroupDetailVideoRecyclerAdapter(Context context, List<VideoImage> data) {
        this.context = context;
        this.data = data;
        Emojix.wrap(context);
        try {
            activity = (Activity) context;
        } catch (Exception e) {
            e.printStackTrace();
        }

        helper = new TextImageHelper();
        gson = new Gson();

        resources = context.getResources();
        expressionArray = resources.getStringArray(R.array.expressionArray);
        expressionCnArray = resources.getStringArray(R.array.expressionCnArray);
    }

    public GroupDetailVideoRecyclerAdapter(Context context, List<VideoImage> data, TBaseFragment fragment) {
        this.context = context;
        this.data = data;
        try {
            activity = (Activity) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.fragment = fragment;

        helper = new TextImageHelper();
        gson = new Gson();

        resources = context.getResources();
        expressionArray = resources.getStringArray(R.array.expressionArray);
        expressionCnArray = resources.getStringArray(R.array.expressionCnArray);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_groupdetail_video, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final VideoImage record = data.get(position);

        if (activity instanceof ActivityDetailActivity208) {
            holder.root.setBackgroundResource(R.drawable.button_blueblack);
            holder.goodstarcomment.setVisibility(View.GONE);
            holder.goodFloorView.setVisibility(View.VISIBLE);

            holder.name.setTextColor(Color.WHITE);
            holder.content.setTextColor(Color.WHITE);
            holder.time.setTextColor(Color.WHITE);
            holder.divider.setBackgroundColor(Color.parseColor("#b0595959"));
            holder.grid.setBackgroundColor(Color.parseColor("#0f0f20"));

            helper.setTextViewText(holder.floor, record.getFloor() + " 楼");
            helper.setTextViewText(holder.joinLikeCount, record.getFlower_count());
            helper.setTextViewText(holder.joinCommentCount, record.getComment_count());
            //活动点赞
            setGood(holder, record);

            if (isComment(record)) {
                holder.content.setSingleLine(false);
                setContent(record, holder.content);
            } else if (isImage(record)) {
                holder.content.setSingleLine(true);
                helper.setTextViewText(holder.content, record.getTitle());
            } else {
                holder.content.setSingleLine(true);
                helper.setTextViewText(holder.content, record.getName());
            }

        } else if (activity instanceof GroupDetailActivity) {
            holder.root.setBackgroundResource(R.drawable.button_white);
            holder.goodstarcomment.setVisibility(View.VISIBLE);
            holder.goodFloorView.setVisibility(View.GONE);

            holder.grid.setBackgroundColor(Color.WHITE);
            holder.name.setTextColor(Color.parseColor("#454545"));
            holder.content.setTextColor(Color.parseColor("#aa454545"));
            holder.time.setTextColor(Color.parseColor("#8c8c8c"));
            holder.divider.setBackgroundColor(Color.parseColor("#e2e2e2"));

            helper.setTextViewText(holder.content, record.getTitle());
            // 点赞设置
            setLike(record, holder);
            // 收藏设置
            setStar(record, holder);
            // 评论
            setComment(record, holder.comment);
            helper.setTextViewText(holder.likeCount, record.getFlower_count());
            helper.setTextViewText(holder.starCount, record.getCollection_count());
            helper.setTextViewText(holder.commentCount, record.getComment_count());
        }

        if (record.isV()) {
            holder.isV.setVisibility(View.VISIBLE);
        } else {
            holder.isV.setVisibility(View.INVISIBLE);
        }

        helper.setImageViewImageNet(holder.head, record.getAvatar());
        helper.setTextViewText(holder.name, record.getNickname());
        setTime(record, holder.time);

        if (isVideo(record)) {// 视频
            holder.video.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);

            setTimeLength(holder.allTime, record);
            helper.setTextViewText(holder.playCount, record.getClick_count());// 785
            if (!StringUtil.isNull(record.getVideo_flag())) {
                helper.setImageViewImageNet(holder.cover, record.getVideo_flag());
            } else if (!StringUtil.isNull(record.getFlag())) {
                helper.setImageViewImageNet(holder.cover, record.getFlag());
            }
        }
        if (isImage(record)) { // 图文
            holder.video.setVisibility(View.GONE);
            holder.image.setVisibility(View.VISIBLE);
            List<String> data = record.getPic_urls();
            GroupDetailImageAdapter dapter = new GroupDetailImageAdapter(context, data, record);
            holder.grid.setAdapter(dapter);
            dapter.notifyDataSetChanged();
        }
        if (isComment(record)) { //文字评论
            holder.video.setVisibility(View.GONE);
            holder.image.setVisibility(View.GONE);
        }

        holder.head.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Member member = gson.fromJson(record.toJSON(), Member.class);
                startPlayerDynamicActivity(member);
            }
        });

        holder.video.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (record.getVideo_id() != null && !record.getVideo_id().equals("0")) {// 视频

                    startVideoPlayActivity(record);

                    if (activity instanceof ActivityDetailActivity208) {
                        UmengAnalyticsHelper.onEvent(context, UmengAnalyticsHelper.DISCOVER, "活动-参加活动-视频播放");
                    } else if (activity instanceof GroupDetailActivity) {
                        if (fragment instanceof GroupdetailNewVideoFragment)
                            UmengAnalyticsHelper.onEvent(context, UmengAnalyticsHelper.DISCOVER, "游戏圈-最新视频-有效");
                        if (fragment instanceof GroupdetailHotVideoFragment)
                            UmengAnalyticsHelper.onEvent(context, UmengAnalyticsHelper.DISCOVER, "游戏圈-最热视频-有效");
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * 活动页点赞
     */
    private void setGood(final ViewHolder holder, final VideoImage item) {
        if (item != null) {
            if (item.getFlower_tick() == 1) {// 已点赞状态
                holder.joinLike.setChecked(true);
            } else {// 未点赞状态
                holder.joinLike.setChecked(false);
            }
        }

        holder.joinLike.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item != null) {
                    if (StringUtil.isNull(item.getFlower_count())) {
                        item.setFlower_count("0");
                    }
                    if (isVideo(item)) {
                        setFlower_tick(item);
                        // 视频点赞
                        DataManager.videoFlower2(item.getVideo_id(), PreferencesHepler.getInstance().getMember_id());
                    } else if (isImage(item)) {
                        setFlower_tick(item);
                        // 献花/取消献花用户
                        DataManager.photoFlower(item.getPic_id(), PreferencesHepler.getInstance().getMember_id(), item.getFlower_tick());
                    }
                    notifyItemChanged(holder.getAdapterPosition());
                }
            }
        });
    }

    private void setFlower_tick(VideoImage item){
        if (item.getFlower_tick() == 0) { //未点赞
            item.setFlower_tick(1);
            item.setFlower_count(Integer.valueOf(item.getFlower_count()) + 1 + "");
        } else { //已点赞
            item.setFlower_tick(0);
            item.setFlower_count(Integer.valueOf(item.getFlower_count()) - 1 + "");
        }
    }

    /**
     * 发布时间
     */
    private void setTime(final VideoImage record, TextView view) {
        // 1小时前
        if (view != null && record != null) {

            if (!StringUtil.isNull(record.getUptime())) {
                try {
                    helper.setTextViewText(view, TimeHelper.getVideoImageUpTime(record.getUptime()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                helper.setTextViewText(view, "");
            }
        }
    }

    /**
     * 点赞
     */
    private void setLike(final VideoImage record, final ViewHolder holder) {

        if (StringUtil.isNull(record.getPic_id()) && StringUtil.isNull(record.getVideo_id()) && !StringUtil.isNull(record.getId())) { // 搜索视频
            return;
        }
        if (record.getFlower_tick() == 1) {// 已点赞状态
            holder.like.setImageResource(R.drawable.videoplay_good_red_205);
        } else {// 未点赞状态
            holder.like.setImageResource(R.drawable.videoplay_good_gray_205);
        }

        holder.like.setVisibility(View.VISIBLE);
        holder.like.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!PreferencesHepler.getInstance().isLogin()) {
                    ToastHelper.s("请先登录！");
                    return;
                }
                if (StringUtil.isNull(record.getFlower_count())) {
                    record.setFlower_count("0");
                }
                if (isVideo(record)) {// 视频
                    setFlower_tick(record);
                    // 视频点赞
                    DataManager.videoFlower2(record.getVideo_id(), PreferencesHepler.getInstance().getMember_id());
                }
                if (isImage(record)) { // 图文
                    int flag = record.getFlower_tick();
                    setFlower_tick(record);
                    // 献花/取消献花用户
                    DataManager.photoFlower(record.getPic_id(), PreferencesHepler.getInstance().getMember_id(), flag);

                }
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
    }

    /**
     * 收藏
     */
    private void setStar(final VideoImage record, final ViewHolder holder) {

        if (StringUtil.isNull(record.getPic_id()) && StringUtil.isNull(record.getVideo_id()) && !StringUtil.isNull(record.getId())) { // 搜索视频

            return;
        }
        if (record.getCollection_tick() == 1) {// 已收藏状态
            holder.star.setImageResource(R.drawable.videoplay_star_red_205);
        } else {// 未收藏状态
            holder.star.setImageResource(R.drawable.videoplay_star_gray_205);
        }

        holder.star.setVisibility(View.VISIBLE);
        holder.star.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!PreferencesHepler.getInstance().isLogin()) {
                    ToastHelper.s("请先登录！");
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
                    DataManager.videoCollect2(record.getVideo_id(), PreferencesHepler.getInstance().getMember_id());
                }
                if (isImage(record)) { // 图文
                    int flag = record.getCollection_tick();
                    if (flag == 1) {// 已收藏状态
                        record.setCollection_count(Integer.valueOf(record.getCollection_count()) - 1 + "");
                        record.setCollection_tick(0);
                    } else {// 未收藏状态
                        record.setCollection_count(Integer.valueOf(record.getCollection_count()) + 1 + "");
                        record.setCollection_tick(1);
                    }
                    // 收藏/取收藏花用户
                    DataManager.photoCollection(record.getPic_id(), PreferencesHepler.getInstance().getMember_id(), flag);
                }
                notifyItemChanged(holder.getAdapterPosition());
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
            helper.setTextViewText(view, TimeHelper.getVideoPlayTime(record.getTime_length()));
        } catch (Exception e) {
            e.printStackTrace();
            helper.setTextViewText(view, "");
        }
    }


    /**
     * 评论内容，如果是表情串码则替换显示相应表情
     */
    private void setContent(final VideoImage record, TextView view) {
        if (record == null || view == null) {
            helper.setTextViewText(view, "");
            return;
        }
        String content = record.getContent();
        if (StringUtil.isNull(content)) {
            helper.setTextViewText(view, "");
            return;
        }
        //处理显示表情
        int len = 0;
        int starts = 0;
        int end = 0;
        SpannableString spannableString = new SpannableString(content);
        while (len < content.length()) {
            if (content.indexOf("[", starts) != -1 && content.indexOf("]", end) != -1) {
                starts = content.indexOf("[", starts);
                end = content.indexOf("]", end);
                String face = content.substring(starts + 1, end);

                for (int i = 0; i < expressionCnArray.length; i++) {
                    if (face.equals(expressionCnArray[i])) {
                        face = expressionArray[i];
                        break;
                    }
                }
                try {
                    Field f = R.drawable.class.getDeclaredField(face);
                    int i = f.getInt(R.drawable.class);
                    Drawable drawable = resources.getDrawable(i);
                    if (drawable != null) {
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth() / 2, drawable.getIntrinsicHeight() / 2);
                        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
                        spannableString.setSpan(span, starts, end + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {

                }
                starts = end;
                len = end;
                end++;
            } else {
                starts++;
                end++;
                len = end;
            }
        }
        view.setText(spannableString);
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

    /**
     * 是否是文字评论
     */
    private boolean isComment(final VideoImage record) {
        // 评论
        return !StringUtil.isNull(record.getComment_id()) && !record.getComment_id().equals("0");
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView head;
        ImageView isV;
        TextView name;
        TextView time;

        TextView content;
        ImageView cover;
        TextView allTime;
        TextView playCount;
        ImageView centerPlay;
        ImageView play;

        ImageView like;
        TextView likeCount, joinLikeCount;
        ImageView star;
        TextView starCount;
        ImageView comment;
        TextView commentCount,joinCommentCount;

        RelativeLayout video;
        RelativeLayout image;
        GridViewY1 grid;
        View goodstarcomment, goodFloorView, divider;
        TextView floor;
        SparkButton joinLike;
        RelativeLayout root;

        public ViewHolder(View itemView) {
            super(itemView);

            head = (CircleImageView) itemView.findViewById(R.id.groupdetail_head);
            isV = (ImageView) itemView.findViewById(R.id.groupdetail_v);
            name = (TextView) itemView.findViewById(R.id.groupdetail_name);
            time = (TextView) itemView.findViewById(R.id.groupdetail_time);
            content = (TextView) itemView.findViewById(R.id.groupdetail_content);
            cover = (ImageView) itemView.findViewById(R.id.groupdetail_cover);
            centerPlay = (ImageView) itemView.findViewById(R.id.groupdetail_centerPlay);
            play = (ImageView) itemView.findViewById(R.id.groupdetail_play);
            allTime = (TextView) itemView.findViewById(R.id.groupdetail_allTime);
            playCount = (TextView) itemView.findViewById(R.id.groupdetail_playCount);
            like = (ImageView) itemView.findViewById(R.id.groupdetail_like);
            likeCount = (TextView) itemView.findViewById(R.id.groupdetail_likeCount);
            star = (ImageView) itemView.findViewById(R.id.groupdetail_star);
            starCount = (TextView) itemView.findViewById(R.id.groupdetail_starCount);
            comment = (ImageView) itemView.findViewById(R.id.groupdetail_comment);
            commentCount = (TextView) itemView.findViewById(R.id.groupdetail_commentCount);
            image = (RelativeLayout) itemView.findViewById(R.id.groupdetail_image);
            video = (RelativeLayout) itemView.findViewById(R.id.groupdetail_video);
            grid = (GridViewY1) itemView.findViewById(R.id.gridview);
            goodstarcomment = itemView.findViewById(R.id.goodstarcomment);
            divider = itemView.findViewById(R.id.divider);
            root = (RelativeLayout) itemView.findViewById(R.id.root);

            //活动页
            goodFloorView = itemView.findViewById(R.id.joinactivity_goodfloor);
            floor = (TextView) itemView.findViewById(R.id.joinactivity_floor);
            joinLike = (SparkButton) itemView.findViewById(R.id.joinactivity_like);
            joinLikeCount = (TextView) itemView.findViewById(R.id.joinactivity_likeCount);
            joinCommentCount = (TextView) itemView.findViewById(R.id.joinactivity_commentCount);
        }
    }
}
