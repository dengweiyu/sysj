package com.li.videoapplication.ui.adapter;


import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.response.TimeLineGiftEntity;
import com.li.videoapplication.ui.dialog.PlayGiftDialog;

import java.util.List;

/**
 *礼物时间轴适配器
 */

public class GiftTimeLineAdapter extends BaseQuickAdapter<TimeLineGiftEntity.DataBean,BaseViewHolder> {


    public GiftTimeLineAdapter(List<TimeLineGiftEntity.DataBean> data) {
        super(R.layout.gift_time_line_item,data);
    }

    @Override
    protected void convert(final BaseViewHolder holder,final TimeLineGiftEntity.DataBean dataBean) {
        holder.setText(R.id.tv_player_name,dataBean.getName())
                .setText(R.id.tv_play_gift_name,"送"+dataBean.getGift_name());
        GlideHelper.displayImageWhite(mContext,dataBean.getAvatar(),(ImageView)holder.getView(R.id.rv_play_gift_icon));
        GlideHelper.displayImageWhite(mContext,dataBean.getGift_icon(),(ImageView)holder.getView(R.id.iv_play_gift_icon));


        TextView number = holder.getView(R.id.tv_play_gift_num);
        PlayGiftDialog.replaceIcon("X"+dataBean.getNum(),mContext,number);

        //初始化透明度
        View root  = holder.getView(R.id.ll_gift_time_line_root);
        int position = holder.getLayoutPosition();
        float scale = 1f;
        switch (position){
            case 0:
                if (getData().size() >= 3){
                    scale = 0.6f;
                }else if (getData().size() == 2){
                    scale = 0.8f;
                }else if (getData().size() == 1){
                    scale = 1f;
                }
                break;
            case 1:
                if (getData().size() >= 3){
                    scale = 0.8f;
                }else if (getData().size() == 2){
                    scale = 1f;
                }
                break;
        }
        if (scale != 1){
            ObjectAnimator.ofFloat(root,"alpha",1,scale).setDuration(0).start();
        }
    }
}
