package com.li.videoapplication.ui.adapter;


import android.graphics.Color;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.response.PlayGiftTypeEntity;

import java.util.List;

/**
 *礼物列表
 */

public class PlayGiftChoiceAdapter extends BaseQuickAdapter<PlayGiftTypeEntity.DataBean, BaseViewHolder> {

    private int mSelected = 0;
    public PlayGiftChoiceAdapter(int layoutId,List<PlayGiftTypeEntity.DataBean> data) {
        super(layoutId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, PlayGiftTypeEntity.DataBean dataBean) {
        GlideHelper.displayImage(mContext,dataBean.getGift_icon(),(ImageView) holder.getView(R.id.iv_video_play_gift_icon));
        holder.setText(R.id.tv_video_play_gift_name, dataBean.getGift_name());
        if ("1".equals(dataBean.getPurchase_method())){
            holder.setText(R.id.tv_video_play_gift_price,subZeroAndDot(dataBean.getCurrency_num())+"魔豆");
        }else {
            holder.setText(R.id.tv_video_play_gift_price,subZeroAndDot(dataBean.getCoin_num())+"魔币");
        }

        if (holder.getAdapterPosition() == mSelected){
            holder.setVisible(R.id.iv_gift_selected,true);
            holder.setBackgroundRes(R.id.rl_gift_item,R.drawable.stroke_red);
        }else {
            holder.setVisible(R.id.iv_gift_selected,false);
            holder.setBackgroundColor(R.id.rl_gift_item, Color.TRANSPARENT);
        }
    }

    public int getSelected() {
        return mSelected;
    }

    public void setSelected(int selected) {
        mSelected = selected;
        notifyDataSetChanged();
    }

    public static String subZeroAndDot(String s){
        if (s == null){
            s = "0";
        }

        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }
}
