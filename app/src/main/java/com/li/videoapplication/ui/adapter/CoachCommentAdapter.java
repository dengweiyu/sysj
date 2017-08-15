package com.li.videoapplication.ui.adapter;

import android.widget.ImageView;
import android.widget.RatingBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.response.CoachCommentEntity;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.utils.StringUtil;

import java.util.List;

/**
 * 陪练评价~
 */

public class CoachCommentAdapter extends BaseQuickAdapter<CoachCommentEntity.ADataBean,BaseViewHolder> {
    public CoachCommentAdapter(List<CoachCommentEntity.ADataBean> data) {
        super(R.layout.adapter_coach_comment,data);
    }

    @Override
    protected void convert(BaseViewHolder holder, CoachCommentEntity.ADataBean aDataBean) {
        ImageView icon = holder.getView(R.id.civ_coach_detail_user_icon);
        GlideHelper.displayImageWhite(mContext,aDataBean.getAvatar(),icon);
        holder.setText(R.id.tv_user_nick_name,aDataBean.getNickname()).setText(R.id.tv_comment_content,aDataBean.getContent());
        try {
            holder.setText(R.id.tv_comment_time, TimeHelper.getWholeTimeFormat(aDataBean.getAddTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        RatingBar score = holder.getView(R.id.rb_comment_score);
        try {
            score.setRating(Float.parseFloat(aDataBean.getScore()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
