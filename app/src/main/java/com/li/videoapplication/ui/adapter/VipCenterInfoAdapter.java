package com.li.videoapplication.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.data.model.response.VipRechargeEntity;

import java.util.List;

/**
 * 会员中心 会员开通列表适配器
 */

public class VipCenterInfoAdapter extends BaseQuickAdapter<VipRechargeEntity.DataBean, BaseViewHolder> {

    public VipCenterInfoAdapter(int layoutResId, List<VipRechargeEntity.DataBean> data) {

        super(layoutResId, data);
    }

    public VipCenterInfoAdapter(List<VipRechargeEntity.DataBean> data) {
        super(data);
    }

    @Override
    protected void convert(BaseViewHolder holder, VipRechargeEntity.DataBean dataBean) {

    }

}
