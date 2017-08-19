package com.li.videoapplication.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.response.VideoRewardRankEntity;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.utils.StringUtil;

import java.util.List;

/**
 * 视频榜
 */

public class VideoRewardAdapter extends BaseQuickAdapter<VideoRewardRankEntity.ADataBean.IncludeBean,BaseViewHolder> {
    public VideoRewardAdapter(List<VideoRewardRankEntity.ADataBean.IncludeBean> data) {
        super(R.layout.adapter_groupdetail_video,data);
    }

    @Override
    protected void convert(BaseViewHolder holder, final VideoRewardRankEntity.ADataBean.IncludeBean includeBean) {
        holder.getView(R.id.ll_currency_money).setVisibility(View.VISIBLE);
        holder.getView(R.id.groupdetail_like).setVisibility(View.GONE);
        holder.getView(R.id.groupdetail_likeCount).setVisibility(View.GONE);
        holder.getView(R.id.groupdetail_star).setVisibility(View.GONE);
        holder.getView(R.id.groupdetail_starCount).setVisibility(View.GONE);
        holder.getView(R.id.groupdetail_comment).setVisibility(View.GONE);
        holder.getView(R.id.groupdetail_commentCount).setVisibility(View.GONE);


        final ImageView icon = holder.getView(R.id.groupdetail_head);
        GlideHelper.displayImageWhite(mContext,includeBean.getAvatar(),icon);

        holder.setText(R.id.groupdetail_name,includeBean.getNickname())
                .setText(R.id.groupdetail_content,includeBean.getVideo_name())
                .setText(R.id.groupdetail_playCount,includeBean.getView_count());

        ImageView cover = (ImageView)holder.getView(R.id.groupdetail_cover);
         GlideHelper.displayImageWhite(mContext,includeBean.getFlag(),cover);
        try {
            holder.setText(R.id.groupdetail_allTime, TimeHelper.getVideoPlayTime(includeBean.getTime_length()));
            holder.setText(R.id.tv_currency_coin,StringUtil.formatMoney(Float.parseFloat(includeBean.getCoin())));
            holder.setText(R.id.tv_currency_beans,StringUtil.formatMoney(Float.parseFloat(includeBean.getCurrency())));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        try {
            holder.setText(R.id.groupdetail_time,TimeHelper.getVideoImageUpTime(includeBean.getUpload_time()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.getView(R.id.groupdetail_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtil.isNull(includeBean.getVideo_id())) {// 视频
                    VideoImage item = new VideoImage();
                    item.setVideo_id(includeBean.getVideo_id());
                    ActivityManager.startVideoPlayActivity(mContext, item);
                }
            }
        });

        holder.getView(R.id.root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtil.isNull(includeBean.getVideo_id())) {// 视频
                    VideoImage item = new VideoImage();
                    item.setVideo_id(includeBean.getVideo_id());
                    ActivityManager.startVideoPlayActivity(mContext, item);
                }
            }
        });

        holder.getView(R.id.groupdetail_head).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Member member = new Member();
                member.setId(includeBean.getMember_id());
                member.setMember_id(includeBean.getMember_id());
                startDynamicActivity(member);
            }
        });
    }
    private void startDynamicActivity(Member member) {
        ActivityManager.startPlayerDynamicActivity(mContext, member);
    }
}
