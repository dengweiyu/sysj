package com.li.videoapplication.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.response.VipRechargeEntity;
import com.li.videoapplication.framework.BaseBaseAdapter;
import com.li.videoapplication.mvp.mall.view.RechargeVipFragment;
import com.li.videoapplication.utils.TextUtil;

import java.util.List;

/**
 * 会员中心 会员开通列表适配器
 */

public class VipCenterInfoAdapter extends BaseQuickAdapter<VipRechargeEntity.DataBean, BaseViewHolder> {

    private RechargeVipFragment fragment;
    public VipCenterInfoAdapter(int layoutResId, List<VipRechargeEntity.DataBean> data,RechargeVipFragment fragment) {
        super(layoutResId, data);
        this.fragment = fragment;
    }

    public VipCenterInfoAdapter(List<VipRechargeEntity.DataBean> data) {
        super(data);
    }

    @Override
    protected void convert(BaseViewHolder holder, VipRechargeEntity.DataBean dataBean) {
        ImageView vip = (ImageView)holder.getView(R.id.iv_vip_center_vip_icon);
        ImageView level = (ImageView)holder.getView(R.id.iv_vip_center_level_icon);
        ImageView select = (ImageView)holder.getView(R.id.iv_vip_center_select);
        TextView price = (TextView)holder.getView(R.id.iv_vip_center_money);
        View border = holder.getView(R.id.rl_vip_center_item_root);
        int resVip = 0;
        int resLevel = 0;
        switch (dataBean.getLevel()){
            case  1:
                resVip = R.drawable.vip_1;
                resLevel = R.drawable.vip_level_1;
                break;
            case  2:
                resVip = R.drawable.vip_2;
                resLevel = R.drawable.vip_level_2;
                break;
            case  3:
                resVip = R.drawable.vip_3;
                resLevel = R.drawable.vip_level_3;
                break;
        }

        vip.setImageResource(resVip);
        level.setImageResource(resLevel);

        if (dataBean.isChoice()){
            border.setBackgroundResource(R.drawable.vip_center_selected);
            select.setVisibility(View.VISIBLE);
            fragment.setPrice(dataBean.getPrice());
        }else {
            select.setVisibility(View.GONE);
            border.setBackgroundResource(R.drawable.vip_center_unselected);
        }

        String priceStr =  TextUtil.toColor(dataBean.getPrice()+"", "#ff3d2e") + " 元/月";
        price.setText(Html.fromHtml(priceStr));

        ListView privileges = holder.getView(R.id.lv_vip_privileges);
        privileges.setAdapter(new VipPrivilegeAdapter(this.mContext,dataBean.getDescription()));
    }


    static class VipPrivilegeAdapter extends BaseBaseAdapter{

        private Context mContext;

        private List<String> mPrivileges;
        public VipPrivilegeAdapter(Context context,List<String> privileges){
            mContext = context;
            mPrivileges = privileges;
        }

        @Override
        protected Context getContext() {
            return mContext;
        }

        @Override
        public int getCount() {
            return mPrivileges == null?0:mPrivileges.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.vip_center_privileges_item,null);

            TextView description = (TextView)convertView.findViewById(R.id.tv_vip_privileges_description);
            description.setText(mPrivileges.get(position));
            return convertView;
        }
    }
}
