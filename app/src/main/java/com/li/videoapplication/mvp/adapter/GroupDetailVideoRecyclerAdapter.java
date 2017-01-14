package com.li.videoapplication.mvp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.ui.activity.ActivityDetailActivity;
import com.li.videoapplication.ui.activity.GroupDetailActivity;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.adapter.GroupDetailImageAdapter;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.sparkbutton.SparkButton;

import java.lang.reflect.Field;
import java.util.List;

import cn.nekocode.emojix.Emojix;

/**
 * 适配器：圈子详情（最新/最热视频）, 活动页参与活动
 */
@SuppressLint("InflateParams")
public class GroupDetailVideoRecyclerAdapter extends BaseQuickAdapter<VideoImage, BaseViewHolder> {

    private final Resources resources;
    private final String member_id;
    private TextImageHelper helper;
    private Activity activity;//引用此适配器页面
    private String[] expressionArray;
    private String[] expressionCnArray;

    public GroupDetailVideoRecyclerAdapter(Context context, List<VideoImage> data) {
        super(R.layout.adapter_groupdetail_video, data);
        Emojix.wrap(context);
        helper = new TextImageHelper();
        member_id = PreferencesHepler.getInstance().getMember_id();
        try {
            activity = (Activity) context;
        } catch (Exception e) {
            e.printStackTrace();
        }

        resources = context.getResources();
        expressionArray = resources.getStringArray(R.array.expressionArray);
        expressionCnArray = resources.getStringArray(R.array.expressionCnArray);
    }


    @Override
    protected void convert(BaseViewHolder holder, VideoImage videoImage) {

        if (activity instanceof ActivityDetailActivity) {
            holder.setBackgroundRes(R.id.root, R.drawable.button_blueblack)
                    .setVisible(R.id.goodstarcomment, false)
                    .setVisible(R.id.joinactivity_goodfloor, true)
                    .setTextColor(R.id.groupdetail_name, resources.getColor(R.color.white))
                    .setTextColor(R.id.groupdetail_content, resources.getColor(R.color.white))
                    .setTextColor(R.id.groupdetail_time, resources.getColor(R.color.white))
                    .setBackgroundColor(R.id.divider, resources.getColor(R.color.joinfragment_video_divider))
                    .setBackgroundColor(R.id.gridview, resources.getColor(R.color.activity_activity_bg))
                    .setText(R.id.joinactivity_floor, videoImage.getFloor() + " 楼")
                    .setText(R.id.joinactivity_likeCount, videoImage.getFlower_count())
                    .setText(R.id.joinactivity_commentCount, videoImage.getComment_count())
                    .addOnClickListener(R.id.joinactivity_comment);
            //活动点赞
            setGood(holder, videoImage);

            TextView content = holder.getView(R.id.groupdetail_content);
            if (isComment(videoImage)) {
                content.setSingleLine(false);
                setContent(videoImage, content);
            } else if (isImage(videoImage)) {
                content.setSingleLine(true);
                holder.setText(R.id.groupdetail_content, videoImage.getTitle());
            } else {
                content.setSingleLine(true);
                holder.setText(R.id.groupdetail_content, videoImage.getName());
            }
        } else if (activity instanceof GroupDetailActivity) {
            holder.setBackgroundColor(R.id.root, resources.getColor(R.color.white))
                    .setVisible(R.id.goodstarcomment, true)
                    .setVisible(R.id.joinactivity_goodfloor, false)
                    .setBackgroundColor(R.id.gridview, resources.getColor(R.color.white))
                    .setTextColor(R.id.groupdetail_name, resources.getColor(R.color.video_name))
                    .setTextColor(R.id.groupdetail_content, resources.getColor(R.color.video_content))
                    .setTextColor(R.id.groupdetail_time, resources.getColor(R.color.video_time))
                    .setBackgroundColor(R.id.divider, resources.getColor(R.color.divider_bg))
                    .setText(R.id.groupdetail_content, videoImage.getTitle())
                    .setText(R.id.groupdetail_likeCount, videoImage.getFlower_count())
                    .setText(R.id.groupdetail_starCount, videoImage.getCollection_count())
                    .setText(R.id.groupdetail_commentCount, videoImage.getComment_count())
                    .setVisible(R.id.bottom, !videoImage.isAD())
                    .addOnClickListener(R.id.groupdetail_comment);

            if (!videoImage.isAD()) {//不是广告
                // 点赞设置
                setLike(videoImage, holder);
                // 收藏设置
                setStar(videoImage, holder);
            }else{

            }
        }

        helper.setImageViewImageNet((ImageView) holder.getView(R.id.groupdetail_head), videoImage.getAvatar());

        holder.setVisible(R.id.groupdetail_v, videoImage.isV())
                .setText(R.id.groupdetail_name, videoImage.getNickname());

        if (videoImage.isAD()) {//广告
            holder.setVisible(R.id.advertisement, true)
                    .setVisible(R.id.groupdetail_time, false)
                    .setVisible(R.id.groupdetail_centerPlay, false);
        } else {//不是广告
            holder.setVisible(R.id.groupdetail_time, true)
                    .setVisible(R.id.advertisement, false)
                    .setVisible(R.id.groupdetail_centerPlay, true)
                    .addOnClickListener(R.id.groupdetail_head)
                    .addOnClickListener(R.id.groupdetail_video);

            setTime(videoImage, (TextView) holder.getView(R.id.groupdetail_time));
        }

        if (isVideo(videoImage)) {// 视频
            holder.setVisible(R.id.groupdetail_video, true)
                    .setVisible(R.id.groupdetail_image, false);

            if (videoImage.isAD()) {//广告
                holder.setVisible(R.id.goodstarcomment, false);
            } else {
                holder.setText(R.id.groupdetail_playCount, StringUtil.toUnitW(videoImage.getClick_count()));
                setTimeLength((TextView) holder.getView(R.id.groupdetail_allTime), videoImage);
            }

            ImageView cover = holder.getView(R.id.groupdetail_cover);
            if (!StringUtil.isNull(videoImage.getVideo_flag())) {
                helper.setImageViewImageNet(cover, videoImage.getVideo_flag());
            } else if (!StringUtil.isNull(videoImage.getFlag())) {
                helper.setImageViewImageNet(cover, videoImage.getFlag());
            }
        }
        if (isImage(videoImage)) { // 图文
            holder.setVisible(R.id.groupdetail_video, false)
                    .setVisible(R.id.groupdetail_image, true);

            List<String> data = videoImage.getPic_urls();
            GroupDetailImageAdapter dapter = new GroupDetailImageAdapter(mContext, data, videoImage);
            GridView grid = holder.getView(R.id.gridview);
            grid.setAdapter(dapter);
            dapter.notifyDataSetChanged();
        }
        if (isComment(videoImage)) { //文字评论
            holder.setVisible(R.id.groupdetail_video, false)
                    .setVisible(R.id.groupdetail_image, false);
        }
    }

    /**
     * 活动页点赞
     */
    private void setGood(final BaseViewHolder holder, final VideoImage item) {

        SparkButton joinLike = holder.getView(R.id.joinactivity_like);

        if (item.getFlower_tick() == 1) {// 已点赞状态
            joinLike.setChecked(true);
        } else {// 未点赞状态
            joinLike.setChecked(false);
        }

        joinLike.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.isNull(item.getFlower_count())) {
                    item.setFlower_count("0");
                }

                setFlower_tick(item);//设置数据

                if (isVideo(item)) {// 视频点赞
                    DataManager.videoFlower2(item.getVideo_id(), member_id);
                } else if (isImage(item)) {// 献花/取消献花用户
                    DataManager.photoFlower(item.getPic_id(), member_id, item.getFlower_tick());
                } else if (isComment(item)) {// 评论点赞
                    DataManager.flowerComment(member_id, item.getComment_id());
                }
                notifyItemChanged(holder.getLayoutPosition());
            }
        });
    }

    private void setFlower_tick(VideoImage item) {
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
    private void setLike(final VideoImage record, final BaseViewHolder holder) {

        if (StringUtil.isNull(record.getPic_id()) && StringUtil.isNull(record.getVideo_id()) && !StringUtil.isNull(record.getId())) { // 搜索视频
            return;
        }
        if (record.getFlower_tick() == 1) {// 已点赞状态
            holder.setImageResource(R.id.groupdetail_like, R.drawable.videoplay_good_red_205);
        } else {// 未点赞状态
            holder.setImageResource(R.id.groupdetail_like, R.drawable.videoplay_good_gray_205);
        }
        holder.setVisible(R.id.groupdetail_like, true);

        holder.getView(R.id.groupdetail_like).setOnClickListener(new OnClickListener() {
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
    private void setStar(final VideoImage record, final BaseViewHolder holder) {

        if (StringUtil.isNull(record.getPic_id())
                && StringUtil.isNull(record.getVideo_id())
                && !StringUtil.isNull(record.getId())) { // 搜索视频
            return;
        }
        if (record.getCollection_tick() == 1) {// 已收藏状态
            holder.setImageResource(R.id.groupdetail_star, R.drawable.videoplay_star_red_205);
        } else {// 未收藏状态
            holder.setImageResource(R.id.groupdetail_star, R.drawable.videoplay_star_gray_205);
        }

        holder.setVisible(R.id.groupdetail_star, true);

        holder.getView(R.id.groupdetail_star).setOnClickListener(new OnClickListener() {
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
                } catch (SecurityException | NoSuchFieldException
                        | IllegalArgumentException
                        | IllegalAccessException e) {
                    e.printStackTrace();
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
}
