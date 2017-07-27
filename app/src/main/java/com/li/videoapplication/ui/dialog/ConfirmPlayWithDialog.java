package com.li.videoapplication.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.event.PayNowEvent;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.mvp.Constant;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.activity.CreatePlayWithOrderActivity;

import io.rong.eventbus.EventBus;


/**
 * 确认陪玩订单
 */

public class ConfirmPlayWithDialog extends AlphaShadeDialog implements View.OnClickListener{

    private float mPriceTotal;
    private float mUserCurrency;

    private boolean isEnough = false;

    private CreatePlayWithOrderActivity mActivity;


    private int mPage;
    public ConfirmPlayWithDialog(@NonNull Context context,float priceTotal,int currency,int page) {
        super(context);

        mPage = page;
        mPriceTotal = priceTotal;
        mUserCurrency = currency;
        if (mUserCurrency >= mPriceTotal){
            isEnough = true;
        }else {
            isEnough = false;
        }

        if (context instanceof CreatePlayWithOrderActivity){
            mActivity = (CreatePlayWithOrderActivity)context;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm_play_with_order);

        //启用阴影渐变
        resetCallback();

        findViewById(R.id.iv_close).setOnClickListener(this);

        TextView confirm = (TextView) findViewById(R.id.tv_confirm_order);
        TextView currency = (TextView) findViewById(R.id.tv_user_currency);
        TextView detail = (TextView)findViewById(R.id.tv_order_detail);
        detail.setText("支付："+mPriceTotal+"魔币");
        detail.invalidate();

        confirm.setOnClickListener(this);
        if (isEnough){
            confirm.setText("立即下单");
            currency.setText("余额："+mUserCurrency+"");
            currency.setTextColor(getContext().getResources().getColor(R.color.textcolor_french_gray));
            confirm.invalidate();
            currency.invalidate();
        }else {
            confirm.setText("立即充值");
            currency.setText("余额不足："+mUserCurrency+"");
            currency.setTextColor(getContext().getResources().getColor(R.color.ab_backdround_red));
            confirm.invalidate();
            currency.invalidate();
        }
    }


    public float getPriceTotal() {
        return mPriceTotal;
    }

    public void setPriceTotal(float priceTotal) {
        mPriceTotal = priceTotal;
        if (mUserCurrency >= mPriceTotal){
            isEnough = true;
        }
    }

    public float getUserCurrency() {
        return mUserCurrency;
    }

    public void setUserCurrency(int userCurrency) {
        mUserCurrency = userCurrency;
        if (mUserCurrency >= mPriceTotal){
            isEnough = true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.tv_confirm_order:
                if (isEnough){
                    //立即支付
                    EventBus.getDefault().post(new PayNowEvent(mPage));

                }else {
                    if (!PreferencesHepler.getInstance().isLogin()) {
                        DialogManager.showLogInDialog(mActivity);
                        return;
                    }
                    //立即充值
                    ActivityManager.startTopUpActivity(getContext(), Constant.TOPUP_ENTRY_PLAY_WITH,1);
                }
                break;
        }
    }
}
