package com.li.videoapplication.mvp.adapter;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.mvp.billboard.BillboardContract.IBillboardPresenter;
import com.li.videoapplication.mvp.billboard.presenter.BillboardPresenter;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.adapter.GroupDetailImageAdapter;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;

import java.util.List;

/**
 * 适配器：视频榜
 */
public class VideoBillboardAdapter extends BaseQuickAdapter<VideoImage, BaseViewHolder> {

    private final String member_id;
    private final IBillboardPresenter presenter;
    private TextImageHelper helper;
    private int tab;

    /**
     * 跳转：视频播放
     */
    private void startVideoPlayActivity(VideoImage videoImage) {
        ActivityManeger.startVideoPlayActivity(mContext, videoImage);
        UmengAnalyticsHelper.onEvent(mContext, UmengAnalyticsHelper.DISCOVER, "视频榜-播放");
    }

    /**
     * 跳转：玩家动态
     */
    private void startPlayerDynamicActivity(Member member) {
        ActivityManeger.startPlayerDynamicActivity(mContext, member);
        UmengAnalyticsHelper.onEvent(mContext, UmengAnalyticsHelper.DISCOVER, "视频榜-动态");
    }

    public VideoBillboardAdapter(List<VideoImage> data) {
        super(R.layout.adapter_groupdetail_video, data);
        helper = new TextImageHelper();
        presenter = new BillboardPresenter();
        member_id = PreferencesHepler.getInstance().getMember_id();
    }

    public void setTab(int tab) {
        this.tab = tab;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final VideoImage videoImage) {
        holder.setBackgroundColor(R.id.root, Color.WHITE)
                .setBackgroundColor(R.id.divider, ContextCompat.getColor(mContext, R.color.divider_bg))
                .setVisible(R.id.groupdetail_v, videoImage.isV())
                .setText(R.id.groupdetail_name, videoImage.getNickname())
                .setText(R.id.groupdetail_content, videoImage.getTitle())
                .setText(R.id.groupdetail_starCount, videoImage.getCollection_count());

        if (tab != 0 && tab == 1) {
            holder.setText(R.id.groupdetail_likeCount, Str2Red(videoImage.getFlower_count()))
                    .setText(R.id.groupdetail_commentCount, videoImage.getComment_count());
        } else if (tab != 0 && tab == 2) {
            holder.setText(R.id.groupdetail_likeCount, videoImage.getFlower_count())
                    .setText(R.id.groupdetail_commentCount, Str2Red(videoImage.getComment_count()));
        } else { //tab=0 || tab=3
            holder.setText(R.id.groupdetail_likeCount, videoImage.getFlower_count())
                    .setText(R.id.groupdetail_commentCount, videoImage.getComment_count());
        }

        ImageView head = holder.getView(R.id.groupdetail_head);
        helper.setImageViewImageNet(head, videoImage.getAvatar());
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                Member member = gson.fromJson(videoImage.toJSON(), Member.class);
                startPlayerDynamicActivity(member);
            }
        });

        setTime(holder, videoImage);

        if (isVideo(videoImage)) {// 视频
            holder.setVisible(R.id.groupdetail_video, true)
                    .setVisible(R.id.groupdetail_image, false);

            setTimeLength(holder, videoImage);

            if (tab != 0 && tab == 3) {
                holder.setText(R.id.groupdetail_playCount, Str2Red(StringUtil.toUnitW(videoImage.getClick_count())));
            } else {
                holder.setText(R.id.groupdetail_playCount, StringUtil.toUnitW(videoImage.getClick_count()));
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

        if (StringUtil.isNull(videoImage.getPic_id()) && StringUtil.isNull(videoImage.getVideo_id())
                && !StringUtil.isNull(videoImage.getId())) { // 搜索视频
            holder.setVisible(R.id.groupdetail_video, true)
                    .setVisible(R.id.groupdetail_image, false);

            setTimeLength(holder, videoImage);
            ImageView cover = holder.getView(R.id.groupdetail_cover);
            helper.setImageViewImageNet(cover, videoImage.getFlagPath());
            holder.setText(R.id.groupdetail_name, videoImage.getUserName())
                    .setText(R.id.groupdetail_content, videoImage.getName());
        }

        // 点赞设置
        setLike(holder, videoImage);

        // 收藏设置
        setStar(holder, videoImage);

        // 评论
        setComment(holder, videoImage);

        RelativeLayout video = holder.getView(R.id.groupdetail_video);
        video.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!StringUtil.isNull(videoImage.getVideo_id())) {// 视频
                    startVideoPlayActivity(videoImage);
                }
            }
        });
    }

    /**
     * 点赞
     */
    private void setLike(final BaseViewHolder holder, final VideoImage record) {

        if (StringUtil.isNull(record.getPic_id()) &&
                StringUtil.isNull(record.getVideo_id()) &&
                !StringUtil.isNull(record.getId())) { // 搜索视频
            return;
        }

        if (record.getFlower_tick() == 1) {// 已点赞状态
            holder.setImageResource(R.id.groupdetail_like, R.drawable.videoplay_good_red_205);
        } else {// 未点赞状态
            holder.setImageResource(R.id.groupdetail_like, R.drawable.videoplay_good_gray_205);
        }

        ImageView like = holder.getView(R.id.groupdetail_like);
        like.setVisibility(View.VISIBLE);
        like.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!PreferencesHepler.getInstance().isLogin()) {
                    DialogManager.showLogInDialog(mContext);
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
                    presenter.videoFlower(record.getVideo_id(), member_id);
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
                    presenter.photoFlower(record.getPic_id(), member_id, flag);
                }
                notifyItemChanged(holder.getLayoutPosition());
            }
        });
    }

    /**
     * 收藏
     */
    private void setStar(final BaseViewHolder holder, final VideoImage record) {

        if (StringUtil.isNull(record.getPic_id()) &&
                StringUtil.isNull(record.getVideo_id()) &&
                !StringUtil.isNull(record.getId())) { // 搜索视频
            return;
        }
        if (record.getCollection_tick() == 1) {// 已收藏状态
            holder.setImageResource(R.id.groupdetail_star, R.drawable.videoplay_star_red_205);
        } else {// 未收藏状态
            holder.setImageResource(R.id.groupdetail_star, R.drawable.videoplay_star_gray_205);
        }
        ImageView star = holder.getView(R.id.groupdetail_star);
        star.setVisibility(View.VISIBLE);
        star.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!PreferencesHepler.getInstance().isLogin()) {
                    DialogManager.showLogInDialog(mContext);
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
                    presenter.videoCollect(record.getVideo_id(), member_id);
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
                    presenter.photoCollection(record.getPic_id(), member_id, flag);
                }
                notifyItemChanged(holder.getLayoutPosition());
            }
        });
    }

    /**
     * 评论
     */
    private void setComment(final BaseViewHolder holder, final VideoImage record) {

        if (StringUtil.isNull(record.getPic_id()) &&
                StringUtil.isNull(record.getVideo_id()) &&
                !StringUtil.isNull(record.getId())) { // 搜索视频
            return;
        }
        final ImageView comment = holder.getView(R.id.groupdetail_comment);
        comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startVideoPlayActivity(record);
            }
        });
        holder.getView(R.id.groupdetail_commentCount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.performClick();
            }
        });
    }

    /**
     * 视频时间长度
     */
    private void setTimeLength(BaseViewHolder holder, VideoImage record) {
        try {
            holder.setText(R.id.groupdetail_allTime, TimeHelper.getVideoPlayTime(record.getTime_length()));
        } catch (Exception e) {
            e.printStackTrace();
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

    private void setTime(BaseViewHolder holder, VideoImage videoImage) {
        // 1小时前
        try {
            holder.setText(R.id.groupdetail_time, TimeHelper.getVideoImageUpTime(videoImage.getUptime()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private CharSequence Str2Red(String string) {
        return Html.fromHtml(TextUtil.toColor(string, "#ff3d2e"));
    }
}
