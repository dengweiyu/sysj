package com.li.videoapplication.ui.adapter;

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
 *
 */

public class PlayGiftListAdapter extends BaseQuickAdapter<VideoPlayGiftEntity.DataBean.IncludesBean,BaseViewHolder> {
    public PlayGiftListAdapter(List<VideoPlayGiftEntity.DataBean.IncludesBean> data) {
        super(R.layout.play_gift_item,data);
    }

    @Override
    protected void convert(final  BaseViewHolder holder,final VideoPlayGiftEntity.DataBean.IncludesBean includesBean) {
        int position = holder.getAdapterPosition();
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

        holder.setImageResource(R.id.iv_trophy,id);
        holder.setText(R.id.tv_player_nick_name,includesBean.getName());
        if (includesBean.getCoin_sum() != 0f){
            holder.setText(R.id.tv_play_gift_number,includesBean.getCoin_sum()+"");
            holder.setImageResource(R.id.tv_play_gift_type,R.drawable.currency);
        }else {

            holder.setText(R.id.tv_play_gift_number,includesBean.getCurrency_sum());
            holder.setImageResource(R.id.tv_play_gift_type,R.drawable.slider_bean);
        }
        GlideHelper.displayImage(mContext,includesBean.getAvatar(),(ImageView)holder.getView(R.id.rv_play_gift_icon));
        UITask.postDelayed(new Runnable() {
            @Override
            public void run() {
                GlideHelper.displayImage(mContext,includesBean.getAvatar(),(ImageView)holder.getView(R.id.rv_play_gift_icon));
            }
        },1000);
    }
}
