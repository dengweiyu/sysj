package com.li.videoapplication.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.event.UserInfomationEvent;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.mvp.Constant;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.activity.WebActivityJS;
import com.li.videoapplication.utils.StringUtil;

/**
 * 魔豆/魔币
 */

public class MyBeansFragment extends TBaseFragment implements View.OnClickListener {
    public static int MY_BEANS = 1;             //魔豆
    public static int MY_CURRENCY = 2;          //魔币
    private int mMode = MY_BEANS;

    private TextView mOver;
    private TextView mOverTile;
    private TextView mExchange;
    public static MyBeansFragment newInsatnce(int mode){
        MyBeansFragment fragment = new MyBeansFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("mode",mode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_my_beans;
    }

    @Override
    protected void initContentView(View view) {
        view.findViewById(R.id.tv_my_wallet_recharge).setOnClickListener(this);
        view.findViewById(R.id.rl_my_wallet_bill).setOnClickListener(this);

        mOver = (TextView)view.findViewById(R.id.iv_my_beans_over);

        mOverTile = (TextView)view.findViewById(R.id.iv_my_beans_over_title);

        mExchange = (TextView)view.findViewById(R.id.tv_my_wallet_exchange);
        mExchange.setOnClickListener(this);
        Bundle bundle = getArguments();
        if (bundle != null){
            mMode = bundle.getInt("mode");
        }

       refreshInfo();
    }



    private void refreshInfo(){
        Member member = getUser();
        if (member != null){
            if (mMode == MY_BEANS){
                mOverTile.setText(R.string.my_wallet_beans_over);
                mExchange.setText("兑换");
                mOver.setText(StringUtil.formatMoney(Float.parseFloat(member.getCurrency())));
            }else if (mMode == MY_CURRENCY){
                mOverTile.setText(R.string.my_wallet_coin_over);
                mExchange.setText("提现");
                mOver.setText(StringUtil.formatMoneyOnePoint(Float.parseFloat(member.getCoin())));
            }
        }
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    @Override
    public void onClick(View v) {
        if (mMode == MY_BEANS){
            switch (v.getId()){
                case R.id.tv_my_wallet_recharge:
                    ActivityManager.startTopUpActivity(getActivity(), Constant.TOPUP_ENTRY_MYWALLEY,0);
                    break;
                case R.id.rl_my_wallet_bill:
                    ActivityManager.startMyWalletBillActivity(getActivity(),mMode);
                    break;
                case R.id.tv_my_wallet_exchange:
                    ActivityManager.startMallActivity(getActivity());
                    break;
            }
        }else if (mMode == MY_CURRENCY){
            switch (v.getId()){
                case R.id.tv_my_wallet_recharge:
                    ActivityManager.startTopUpActivity(getActivity(), Constant.TOPUP_ENTRY_MYWALLEY,1);
                    break;
                case R.id.rl_my_wallet_bill:
                    ActivityManager.startMyWalletBillActivity(getActivity(),mMode);
                    break;
                case R.id.tv_my_wallet_exchange:
                    WebActivityJS.startWebActivityJS(getActivity(),"http://apps.ifeimo.com/Sysj221/WalletInstructions/withdrawal","提现","",true);
                    break;
            }
        }
    }

    /**
     * 事件：更新个人资料
     */
    public void onEventMainThread(UserInfomationEvent event) {
        refreshInfo();
    }
}
