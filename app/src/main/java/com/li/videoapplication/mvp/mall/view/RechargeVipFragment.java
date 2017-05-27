package com.li.videoapplication.mvp.mall.view;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.response.VipRechargeEntity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.mvp.mall.model.MallModel;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.adapter.VipCenterInfoAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.li.videoapplication.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 充值会员
 */

public class RechargeVipFragment extends TBaseFragment implements View.OnClickListener {

    private RecyclerView mOptions;

    private List<VipRechargeEntity.DataBean> mData = new ArrayList<>();

    private VipCenterInfoAdapter mAdapter;

    private int selected = 0;

    private TextView mPrice;

    public static RechargeVipFragment newInstance(){
        RechargeVipFragment fragment = new RechargeVipFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 跳转：支付方式选择
     */
    private void startPaymentWayActivity(float money,int entry,int level) {
        ActivityManager.startPaymentWayActivity(getActivity(),money,0,entry, MallModel.USE_RECHARGE_VIP,level);
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_vip_center;
    }

    @Override
    protected void initContentView(View view) {
        mOptions  = (RecyclerView)view.findViewById(R.id.rv_vip_center_option);
        mPrice = (TextView)view.findViewById(R.id.tv_vip_price);
        view.findViewById(R.id.rl_vip_center_payment_now).setOnClickListener(this);

        mOptions.setLayoutManager(new GridLayoutManager(getActivity(),3));
        mAdapter = new VipCenterInfoAdapter(R.layout.vip_center_list_item,mData,this);
        mOptions.setAdapter(mAdapter);

        mOptions.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                mData.get(selected).setChoice(false);
                selected = i;
                mData.get(selected).setChoice(true);
                mAdapter.setNewData(mData);
            }
        });
        DataManager.vipInfo();
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }


    public void setPrice(float price){
        String priceStr =  "支付"+TextUtil.toColor(price+"", "#ff3d2e") + " 元/月";
        mPrice.setText(Html.fromHtml(priceStr));
    }

    /**
     * 回调
     */
    public void onEventMainThread(VipRechargeEntity entity){
        if (entity != null && entity.isResult()){
            if (selected >= entity.getData().size()){
                selected = 0;
            }
            if (entity.getData().size() > 0) {
                entity.getData().get(selected).setChoice(true);        //默认选择第一个
            }
            mData.clear();
            mData = entity.getData();
            mAdapter.addData(mData);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_vip_center_payment_now:
                TopUpActivity activity = (TopUpActivity) getActivity();
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "会员充值-立即支付");
                if (mData == null || mData.size() == 0){
                    return;
                }
                VipRechargeEntity.DataBean data = mData.get(selected);
                startPaymentWayActivity(data.getPrice(),activity.entry,data.getLevel());
                break;
        }
    }
}
