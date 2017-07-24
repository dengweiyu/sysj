package com.li.videoapplication.mvp.mall.view;

import android.media.midi.MidiOutputPort;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.common.collect.Lists;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.event.UserInfomationEvent;
import com.li.videoapplication.data.model.response.RechargeCoinEntity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.mvp.Constant;
import com.li.videoapplication.mvp.adapter.RechargeCoinAdapter;
import com.li.videoapplication.mvp.mall.MallContract;
import com.li.videoapplication.mvp.mall.model.MallModel;
import com.li.videoapplication.mvp.mall.presenter.MallPresenter;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;

import java.text.DecimalFormat;
import java.util.List;


/**
 *充值魔币
 */

public class RechargeCoinFragment  extends TBaseFragment implements MallContract.IRechargeCoinListView,View.OnClickListener {
    private MallContract.IMallPresenter presenter;
    private RechargeCoinAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private TextView mPrice;
    private TextView mRate;
    private TextView mCoin;
    private TextView mBeans;
    private List<Integer> mOptions;
    private float mRateFloat;
    public static RechargeCoinFragment newInstance() {
        RechargeCoinFragment fragment = new RechargeCoinFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    /**
     * 跳转：支付方式选择
     */
    private void startPaymentWayActivity() {
        ActivityManager.startPaymentWayActivity(getActivity(),Float.parseFloat(getPrice(mAdapter.getSelectPrice())),getNumber(), Constant.TOPUP_ENTRY_MYWALLEY,MallModel.USE_RECHARGE_COIN,-1);
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_recharge_coin;
    }

    @Override
    protected void initContentView(View view) {
        mRecyclerView = (RecyclerView)view.findViewById(R.id.rv_my_recharge_coin);
        mRecyclerView.addOnItemTouchListener(mListener);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        mPrice = (TextView)view.findViewById(R.id.tv_coin_price);
        mRate = (TextView)view.findViewById(R.id.tv_coin_rate);
        mCoin = (TextView)view.findViewById(R.id.tv_my_currency_coin);
        mBeans = (TextView) view.findViewById(R.id.tv_my_currency_beans);
        view.findViewById(R.id.rl_payment_now).setOnClickListener(this);

        presenter = new MallPresenter();
        presenter.setCoinListView(this);
        presenter.getCoinList();

        Member member = getUser();
        setTextViewText(mCoin, StringUtil.formatNum(member.getCoin()));
        setTextViewText(mBeans, StringUtil.formatNum(member.getCurrency()));
    }

    @Override
    public void refreshCoinList(RechargeCoinEntity entity) {
        if (entity != null){
            mOptions = entity.getOption();
            mOptions.add(-1);           //最后加一条自定义数量
            if (mAdapter == null){
                mAdapter = new RechargeCoinAdapter(this,mOptions);
            }else {
                mAdapter.setNewData(mOptions);
            }
            mRecyclerView.setAdapter(mAdapter);
            mRateFloat = entity.getExchangeRate();

            setPrice(2);
            setTextViewText(mRate, "(1人民币=" + mRateFloat + "魔币)");
        }
    }

    @Override
    public void refreshFault() {

    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    final OnItemClickListener mListener = new OnItemClickListener() {
        @Override
        public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            mAdapter.refreshSelectItem(i);
            setPrice(i);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_payment_now:
                    if (mAdapter != null){
                        if (getNumber() >= 1){
                            startPaymentWayActivity();
                        }else {
                            ToastHelper.s("充值魔币数量不低于1哦~");
                        }
                    }

                break;
        }
    }

    public void setPrice(int pos) {
        Log.d(tag, "setPrice: pos = " + pos);
        String priceStr = "售价：" + TextUtil.toColor(getPrice(pos), "#fe5e5e") + " 元";
        mPrice.setText(Html.fromHtml(priceStr));
    }

    private String getPrice(int pos){
        Double priceDouble = 0D;
        try {
            if (pos != mOptions.size() - 1) {
                priceDouble = Double.valueOf(mOptions.get(pos)) / Double.valueOf(mRateFloat);
            } else {
                priceDouble = mAdapter.getInputNumber() / Double.valueOf(mRateFloat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DecimalFormat("#0.00").format(priceDouble);
    }

    private int getNumber(){
        int number = 0;
        if (mAdapter == null){
            return number;
        }
        int position = mAdapter.getSelectPrice();

        if (position != mOptions.size() - 1){
            number = mOptions.get(position);
        }else {
            number = mAdapter.getInputNumber();
        }
        return number;
    }

    /**
     * 事件：更新个人资料
     */
    public void onEventMainThread(UserInfomationEvent event) {

        if (event != null) {
            if (mCoin != null){
                mCoin.setText(StringUtil.formatNum(getUser().getCoin()));
            }
            if (mBeans != null){
                mBeans.setText(StringUtil.formatNum(getUser().getCurrency()));
            }
        }
    }
}
