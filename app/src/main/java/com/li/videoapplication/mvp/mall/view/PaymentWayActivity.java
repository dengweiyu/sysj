package com.li.videoapplication.mvp.mall.view;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.PaymentList;
import com.li.videoapplication.data.model.response.PaymentEntity;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.mvp.Constant;
import com.li.videoapplication.mvp.adapter.PaymentLisAdapter;
import com.li.videoapplication.mvp.mall.MallContract;
import com.li.videoapplication.mvp.mall.model.MallModel;
import com.li.videoapplication.mvp.mall.presenter.MallPresenter;
import com.li.videoapplication.payment.IPayment;
import com.li.videoapplication.payment.PaymentFactory;
import com.li.videoapplication.tools.ToastHelper;

/**
 * Created by liuwei on 2017/4/1.
 * 支付方式选择
 */

public class PaymentWayActivity extends TBaseAppCompatActivity implements MallContract.IPaymentListView ,View.OnClickListener{

    public final static String MONEY = "money";
    public final static String ENTRY = "entryt";       //支付页面入口
    public final static String NUMBER = "number";      //魔豆数量
    public final static String USE = "use";            //支付用途
    public final static String LEVEL = "level";        //VIP 等级
    public final static String KEY = "key";             //套餐包
    private MallContract.IMallPresenter presenter;

    private ListView mPaymentList;

    private PaymentLisAdapter mAdapter;

    private TextView mPaymentNow;

    private View mPayNow;
    private float mMoney = 0;
    private int entry;
    private int mNumber = 0;
    private int mUse;
    private int mLevel;
    private int mKey;

    private String mTitle ;
    private IPayment mPayment;


    @Override
    protected int getContentView() {
        return R.layout.activity_payment_way;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();

        if (mPayment != null){
            mPayment.handleIntent(getIntent());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (mPayment != null){
            mPayment.handleIntent(intent);
        }
    }

    @Override
    public void initView() {
        super.initView();
        initData();
        initToolbar();
        initAdapter();
        presenter.getPaymentList(AppConstant.SYSJ_ANDROID);

    }

    @Override
    public void loadData() {
        super.loadData();
    }

    private void initData(){
        Intent intent = getIntent();
        mMoney = intent.getFloatExtra(MONEY,0f);
        entry =intent.getIntExtra("entry", Constant.TOPUP_ENTRY_MYWALLEY);
        mNumber = intent.getIntExtra(NUMBER,0);
        mUse = intent.getIntExtra(USE,-1);
        mLevel = intent.getIntExtra(LEVEL,-1);
        mKey = intent.getIntExtra(KEY,-1);
        switch (mUse){
            case  MallModel.USE_RECHARGE_VIP:
                mTitle = "充值会员";
                break;
            case MallModel.USE_RECHARGE_COIN:
                mTitle = "充值魔币";
                break;
            default:
                mTitle = "支付魔豆";
                break;
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView tb_title = (TextView) findViewById(R.id.tb_title);
        tb_title.setText(mTitle);
        findViewById(R.id.tb_back).setOnClickListener(this);
    }

    private void initAdapter(){
        presenter = new MallPresenter();
        mAdapter = new PaymentLisAdapter(this,null);
        mPaymentNow = (TextView)findViewById(R.id.tv_payment_now);
        mPaymentList = (ListView)findViewById(R.id.lv_payment_list);
        mPaymentList.setAdapter(mAdapter);
        presenter.setPaymentLisView(this);

        mPayNow = findViewById(R.id.rl_payment_now);
        mPayNow.setOnClickListener(this);
        mPaymentNow.setText("立即支付 ¥"+mMoney);

    }

    @Override
    public void refreshPaymentList(PaymentList list) {
        mAdapter.onRefresh(list.getData());
        mPayNow.setVisibility(View.VISIBLE);
    }

    /**
     * 回调：获取支付订单信息
     */
    @Override
    public void refreshOrderInfoData(PaymentEntity entity) {
        Log.d(tag, "PaymentEntity " + entity);
        paymentNow(mAdapter.getSelectedPayId(),entity);
    }

    /**
     * 回调：获取支付订单信息失败
     */
    @Override
    public void refreshFault() {
        ToastHelper.l("订单生成失败");
    }

    /**
     * 回调：支付结果
     */
    final IPayment.Callback mCallback = new IPayment.Callback() {
        @Override
        public void success() {
            ToastHelper.l("支付成功");

            try {
                DataManager.userProfilePersonalInformation(getMember_id(),getMember_id());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            finish();
        }

        @Override
        public void fail(String message) {
            ToastHelper.l(message);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tb_back:
                finish();
                break;
            case R.id.rl_payment_now:
                if (mUse == MallModel.USE_RECHARGE_MONEY){
                    if (mNumber >= 100){
                        presenter.payment(mUse,getMember_id(),mLevel,mKey,mNumber+"",Integer.valueOf(mAdapter.getSelectedPayId()),entry);
                    }else {
                        ToastHelper.l("值数量最低为100");
                    }
                }else if(mUse == MallModel.USE_RECHARGE_VIP){
                    presenter.payment(mUse,getMember_id(),mLevel,mKey,mNumber+"",Integer.valueOf(mAdapter.getSelectedPayId()),entry);
                }else if(mUse == MallModel.USE_RECHARGE_COIN){
                    presenter.payment(mUse,getMember_id(),mLevel,mKey,mNumber+"",Integer.valueOf(mAdapter.getSelectedPayId()),entry);
                }
                break;
        }
    }

    private void paymentNow(String type_id,PaymentEntity entity){
        mPayment = PaymentFactory.createPayment(this,Integer.valueOf(type_id));
        mPayment.executePayment(entity,mCallback);
    }

}
