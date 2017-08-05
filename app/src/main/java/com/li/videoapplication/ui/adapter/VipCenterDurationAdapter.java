package com.li.videoapplication.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.response.VipRechargeEntity;

import java.util.List;

/**
 * vip开通时长
 */

public class VipCenterDurationAdapter extends BaseQuickAdapter<VipRechargeEntity.PackageMemuBean,BaseViewHolder> {
    public VipCenterDurationAdapter(List<VipRechargeEntity.PackageMemuBean> data) {
        super(R.layout.adapter_vip_duration,data);
    }

    @Override
    protected void convert(BaseViewHolder holder, VipRechargeEntity.PackageMemuBean packageMemuBean) {
        holder.setText(R.id.tv_duration,packageMemuBean.getText());
        if (packageMemuBean.getDiscount() < 1f){
            holder.getView(R.id.iv_duration_discount).setVisibility(View.VISIBLE);
            if (packageMemuBean.isChoice()){
                holder.setImageResource(R.id.iv_duration_discount,R.drawable.discount);
            }else {
                holder.setImageResource(R.id.iv_duration_discount,R.drawable.no_discount);
            }
        }else {
            holder.getView(R.id.iv_duration_discount).setVisibility(View.GONE);
        }

        if (packageMemuBean.isChoice()){
            holder.getView(R.id.tv_duration).setBackgroundResource(R.drawable.vip_center_selected);
        }else {
            holder.getView(R.id.tv_duration).setBackgroundResource(R.drawable.shape_gray);
        }
    }
}
