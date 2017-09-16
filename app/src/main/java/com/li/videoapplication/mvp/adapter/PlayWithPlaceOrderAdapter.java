package com.li.videoapplication.mvp.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.response.PlayWithPlaceOrderEntity;
import com.li.videoapplication.tools.TimeHelper;
import java.util.List;

/**
 * 陪玩下单列表适配器
 */

public class PlayWithPlaceOrderAdapter extends BaseSectionQuickAdapter<PlayWithPlaceOrderEntity.SectionData,BaseViewHolder> {
    public PlayWithPlaceOrderAdapter( List<PlayWithPlaceOrderEntity.SectionData> data) {
        super(R.layout.play_with_place_order_item, R.layout.bill_list_item_header, data);
    }

    @Override
    protected void convertHead(BaseViewHolder baseViewHolder, PlayWithPlaceOrderEntity.SectionData sectionData) {
        baseViewHolder.setText(R.id.iv_bill_list_title,sectionData.header);
    }

    @Override
    protected void convert(final BaseViewHolder holder, PlayWithPlaceOrderEntity.SectionData sectionData) {
       final PlayWithPlaceOrderEntity.DataBean.ListBean data = sectionData.t;
        holder.setText(R.id.tv_user_nick_name,data.getNickname())
                .setText(R.id.tv_user_score,data.getScore()+"分")
                .setText(R.id.tv_play_with_order_mode,"模式："+data.getGameMode())
                .setText(R.id.tv_play_with_order_status,data.getStatusText());
        try {
            holder.setText(R.id.tv_play_with_order_time, TimeHelper.getTime2HmFormat(data.getAdd_time()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        GlideHelper.displayImageWhite(mContext,data.getAvatar(),(ImageView) holder.getView(R.id.civ_user_icon));

    }
}
