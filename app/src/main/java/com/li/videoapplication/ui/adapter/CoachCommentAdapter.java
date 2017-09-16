package com.li.videoapplication.ui.adapter;

import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.response.CoachCommentEntity;
import com.li.videoapplication.tools.TimeHelper;

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


        final TextView open = holder.getView(R.id.tv_open_all_comment);
        final TextView content = holder.getView(R.id.tv_comment_content);

        open.setVisibility(View.GONE);
        content.setSingleLine(true);
        content.setEllipsize(TextUtils.TruncateAt.END);

        if (content.getLayout() != null){
            if (content.getLayout().getLineCount() > 0){
                open.setVisibility(View.VISIBLE);
                if (content.getLayout().getLineCount() > 1){
                    open.setText("收起");
                }else {
                    if (content.getLayout().getEllipsisCount(content.getLayout().getLineCount()-1) > 0){
                        open.setText("查看全部");
                    }else {
                        open.setVisibility(View.GONE);
                    }
                }
            }else {
                open.setVisibility(View.GONE);
            }
        }

        content.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                final Layout layout = content.getLayout();
                final int line = layout.getLineCount();
                if (layout == null){
                    return ;
                }
                if (line > 0){
                    open.setVisibility(View.VISIBLE);
                    if (line > 1){
                        open.setText("收起");
                    }else {
                        if (layout.getEllipsisCount(line-1) > 0){
                            open.setText("查看全部");
                        }else {
                            open.setVisibility(View.GONE);
                        }
                    }
                }else {
                    open.setVisibility(View.GONE);
                }
                content.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                return ;
            }
        });

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Layout l = content.getLayout();
                if (l == null){
                    return;
                }
                if (l.getLineCount() == 1){
                    content.setSingleLine(false);
                    content.setEllipsize(null);
                    open.setText("收起");
                } else if (l.getLineCount() > 1){
                    content.setSingleLine(true);
                    content.setEllipsize(TextUtils.TruncateAt.END);
                    open.setText("查看全部");
                }

                content.invalidate();
            }
        });

    }
}
