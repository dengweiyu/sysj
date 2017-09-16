package com.li.videoapplication.mvp.mall.view;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import com.li.videoapplication.ui.adapter.RechargeVIPAdapter;
import com.li.videoapplication.ui.adapter.VipCenterDurationAdapter;

import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.li.videoapplication.ui.view.SpanItemDecoration;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 充值会员
 */

public class RechargeVipFragment extends TBaseFragment implements View.OnClickListener {

    private RecyclerView mOptions;

    private RecyclerView mDuration;

    private List<VipRechargeEntity.DataBean> mData = new ArrayList<>();
    private List<VipRechargeEntity.PackageMemuBean> mDurationData = new ArrayList<>();

    private VipCenterDurationAdapter mDurationAdapter;

    private int mSelected = 0;

    private int mSelectedDuration = 0;

    private TextView mPrice;
    private TextView mOriginalPrice;

    private RechargeVIPAdapter mAdapter;

    public static RechargeVipFragment newInstance(){
        RechargeVipFragment fragment = new RechargeVipFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 跳转：支付方式选择
     */
    private void startPaymentWayActivity(float money,int entry,int level,int key) {
        ActivityManager.startPaymentWayActivity(getActivity(),money,0,entry, MallModel.USE_RECHARGE_VIP,level,key);
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_vip_center;
    }

    @Override
    protected void initContentView(View view) {
        mOptions  = (RecyclerView)view.findViewById(R.id.rv_vip_center_option);
        mDuration = (RecyclerView)view.findViewById(R.id.rv_vip_duration);
        mPrice = (TextView)view.findViewById(R.id.tv_vip_price);
        mOriginalPrice = (TextView)view.findViewById(R.id.tv_vip_original_price);
        view.findViewById(R.id.rl_vip_center_payment_now).setOnClickListener(this);


        mOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);

        mOptions.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new RechargeVIPAdapter(getActivity(),mData);
        mOptions.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new RechargeVIPAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                mData.get(mSelected).setChoice(false);
                mSelected = position;
                mData.get(mSelected).setChoice(true);
                setPrice(mData.get(mSelected).getPrice());
            }
        });

        DataManager.vipInfo();


        mDuration.setLayoutManager(new GridLayoutManager(getActivity(),4));

        mDurationAdapter = new VipCenterDurationAdapter(mDurationData);
        mDuration.setAdapter(mDurationAdapter);
        mDuration.addItemDecoration(new SpanItemDecoration(ScreenUtil.dp2px(getContext(),15),false,false,false,true));
        mDuration.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                mDurationData.get(mSelectedDuration).setChoice(false);
                mSelectedDuration = i;
                mDurationData.get(mSelectedDuration).setChoice(true);
                mDurationAdapter.setNewData(mDurationData);

                setPrice(mData.get(mSelected).getPrice());
            }
        });

    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    private float mCurrentPrice;
    public void setPrice(float price){
        int duration = 1;
        switch (mSelectedDuration){
            case 0:
                duration = 1;
                break;
            case 1:
                duration = 3;
                break;
            case 2:
                duration = 6;
                break;
            case 3:
                duration = 12;
                break;
        }

        try {
            mCurrentPrice = price*duration*mDurationData.get(mSelectedDuration).getDiscount();

            String priceStr =  "支付："+TextUtil.toColor(mCurrentPrice+"", "#ff3d2e") + " 元";
            mPrice.setText(Html.fromHtml(priceStr));

            if (mDurationData.get(mSelectedDuration).getDiscount() < 1f){
                String originalPriceStr =  " 原价"+price*duration+ " 元 ";
                mOriginalPrice.setVisibility(View.VISIBLE);
                mOriginalPrice.setText(originalPriceStr);
            }else {
                mOriginalPrice.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 回调
     */
    public void onEventMainThread(VipRechargeEntity entity){
        if (entity != null && entity.isResult()){
            if (mSelected >= entity.getData().size()){
                mSelected = 0;
            }
            if (entity.getData().size() > 0) {
                entity.getData().get(mSelected).setChoice(true);        //默认选择第一个
            }
            mData.clear();
            mData.addAll(entity.getData());

            mAdapter.notifyDataSetChanged();


            if (mSelectedDuration >= entity.getPackageMemu().size()){
                mSelectedDuration = 0;
            }
            if (entity.getPackageMemu().size() > 0) {
                entity.getPackageMemu().get(mSelectedDuration).setChoice(true);        //默认选择第一个
            }

            mDurationData.clear();
            mDurationData = entity.getPackageMemu();
            mDurationAdapter.addData(mDurationData);
            mDurationAdapter.notifyDataSetChanged();
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
                VipRechargeEntity.DataBean data = mData.get(mSelected);
                startPaymentWayActivity(mCurrentPrice,activity.entry,data.getLevel(),mDurationData.get(mSelectedDuration).getKey());
                break;
        }
    }
}
