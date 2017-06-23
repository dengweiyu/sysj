package com.li.videoapplication.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.response.VideoPlayGiftEntity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.utils.StringUtil;

import java.util.List;

/**
 * 打赏排名适配器
 */

public class GiftRankAdapter extends BaseQuickAdapter<VideoPlayGiftEntity.DataBean.IncludesBean,BaseViewHolder> {

    private String mMemberId;

    public GiftRankAdapter(List<VideoPlayGiftEntity.DataBean.IncludesBean> data,String memberId) {
        super(R.layout.dialog_gift_rank_item,data);
        mMemberId = memberId;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final VideoPlayGiftEntity.DataBean.IncludesBean includesBean) {
        int position = holder.getAdapterPosition();

        if (position < 3){
            int id = 0;
            switch (position){
                case 0:
                    id = R.drawable.playerbillboard_one;
                    break;
                case 1:
                    id = R.drawable.playerbillboard_two;
                    break;
                case 2:
                    id = R.drawable.playerbillboard_three;
                    break;
            }
            holder.getView(R.id.tv_rank).setVisibility(View.GONE);
            ImageView icon = (ImageView)holder.getView(R.id.iv_rank);
            icon.setVisibility(View.VISIBLE);
            icon.setImageResource(id);
        }else {
            holder.getView(R.id.tv_rank).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_rank,position+1+"");
        }

        GlideHelper.displayImage(mContext,includesBean.getAvatar(),(ImageView)holder.getView(R.id.civ_player_icon));

        UITask.postDelayed(new Runnable() {
            @Override
            public void run() {
                GlideHelper.displayImage(mContext,includesBean.getAvatar(),(ImageView)holder.getView(R.id.civ_player_icon));
            }
        },1000);

        holder.setText(R.id.tv_player_nick_name, includesBean.getName())
                .setText(R.id.tv_play_currency,StringUtil.formatNum(includesBean.getCoin_sum()))
                .setText(R.id.tv_play_beans,StringUtil.formatNum(includesBean.getCurrency_sum()));

        if (!StringUtil.isNull(mMemberId)){
            View root  = holder.getView(R.id.ll_rank_root);
            if (mMemberId.equals(includesBean.getMember_id())){
                root.setBackgroundResource(R.drawable.gift_rank_own);
            }else {
                root.setBackgroundResource(R.drawable.gift_rank_other);
            }
        }
    }
}
