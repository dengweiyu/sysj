package com.li.videoapplication.mvp.mall.view;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.PayResult;
import com.li.videoapplication.data.model.entity.TopUp;
import com.li.videoapplication.data.model.response.MemberCurrencyEntity;
import com.li.videoapplication.data.model.response.TopUpOptionEntity;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.mvp.Constant;
import com.li.videoapplication.mvp.adapter.TopUpAdapter;
import com.li.videoapplication.mvp.mall.MallContract;
import com.li.videoapplication.mvp.mall.presenter.MallPresenter;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.utils.InputUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;
import com.li.videoapplication.views.CircleImageView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit2.http.POST;

/**
 * 活动：充值
 */
public class TopUpActivity extends TBaseAppCompatActivity implements MallContract.ITopUpView,
        View.OnClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.topup_price)
    TextView price;
    @BindView(R.id.topup_rate)
    TextView rate;
    private TextView currency;
    private MallContract.IMallPresenter presenter;
    private String[] option;
    private List<TopUp> data;
    private TopUpAdapter adapter;
    private String rateStr;
    private static String orderInfo;
    private int selectedPos;
    private int entry;//充值页面入口

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.ALIPAY: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    //对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    Log.d(tag, "resultInfo: " + resultInfo);
                    String resultStatus = payResult.getResultStatus();
                    Log.d(tag, "resultStatus: " + resultStatus);
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ToastHelper.s("支付成功");
                        //  刷新个人飞磨豆数量
//                        DataManager.getMemberCurrency(getMember_id());
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastHelper.s("支付失败");
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 跳转：充值记录
     */
    private void startTopUpRecordActivity() {
        ActivityManeger.startTopUpRecordActivity(this);
    }

    @Override
    public void refreshIntent() {
        super.refreshIntent();
        entry = getIntent().getIntExtra("entry", Constant.TOPUP_ENTRY_MYWALLEY);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_topup;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundWhite();
    }

    @Override
    public void initView() {
        super.initView();
        initToolbar();
        initHeader();
        initAdapter();
        addOnClickListener();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView tb_title = (TextView) findViewById(R.id.tb_title);
        tb_title.setText("充值");
        findViewById(R.id.tb_topup_record).setVisibility(View.VISIBLE);
        findViewById(R.id.tb_topup_record).setOnClickListener(this);
        findViewById(R.id.tb_back).setOnClickListener(this);

        findViewById(R.id.topup_pay).setOnClickListener(this);
    }

    private void initHeader() {
        Member member = getUser();
        CircleImageView head = (CircleImageView) findViewById(R.id.topup_head);
        TextView name = (TextView) findViewById(R.id.topup_name);
        currency = (TextView) findViewById(R.id.topup_currency);

        setImageViewImageNet(head, member.getAvatar());
        setTextViewText(name, member.getNickname());
        setTextViewText(currency, StringUtil.formatNum(member.getCurrency()));
    }

    private void initAdapter() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setItemAnimator(null);
        presenter = new MallPresenter();
        presenter.setTopUpView(this);
        data = new ArrayList<>();
        adapter = new TopUpAdapter(data, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void loadData() {
        super.loadData();
        presenter.getRechargeRule();
    }

    public void setPrice(int pos) {
        Log.d(tag, "setPrice: pos = " + pos);
        Double priceDouble;
        if (pos != adapter.getData().size() - 1) {
            priceDouble = Double.valueOf(option[pos]) / Double.valueOf(rateStr);
        } else {
            priceDouble = adapter.getCustomInt() / Double.valueOf(rateStr);
        }
        String priceDouble2 = new DecimalFormat("#0.00").format(priceDouble);
        String priceStr = "售价：" + TextUtil.toColor(priceDouble2, "#fe5e5e") + " 元";
        price.setText(Html.fromHtml(priceStr));
    }

    private void addOnClickListener() {
        //recyclerview item点击事件处理
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int pos) {
                TopUp record = (TopUp) adapter.getItem(pos);
                if (selectedPos != pos) {
                    //先取消上个item的勾选状态
                    data.get(selectedPos).setSelected(false);
                    adapter.notifyItemChanged(selectedPos);
                    //设置新Item的勾选状态
                    selectedPos = pos;
                    data.get(selectedPos).setSelected(true);
                    adapter.notifyItemChanged(selectedPos);

                    if (!record.getOption().equals("-1")) {//正常充值选项
                        setPrice(pos);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tb_back:
                finish();
                break;
            case R.id.tb_topup_record:
                startTopUpRecordActivity();
                break;
            case R.id.topup_pay:
                Log.d(tag, "onClick: adapter.getSelectedPos() = " + selectedPos);
                Log.d(tag, "onClick: adapter.getData().size() = " + adapter.getData().size());
                Log.d(tag, "onClick: adapter.getCustomInt() = " + adapter.getCustomInt());
                if (selectedPos == adapter.getData().size() - 1) {
                    if (adapter.getCustomInt() < 100){
                        ToastHelper.s("充值数量最低为100");
                        return;
                    }
                    presenter.payment(getMember_id(), adapter.getCustomInt() + "",
                            Constant.ALIPAY, entry);
                } else {
                    Log.d(tag, "onClick: final seleccted = " + option[selectedPos]);
                    presenter.payment(getMember_id(), option[selectedPos],
                            Constant.ALIPAY, entry);
                }
                break;
        }
    }

    //重置转换数据，默认选择第一个
    private void transformData(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            TopUp item = new TopUp();
            if (i == 0) {
                item.setSelected(true);
            } else {
                item.setSelected(false);
            }
            item.setOption(list.get(i));
            data.add(i, item);
        }
        //最后插一条 自定义数量，标记为-1
        TopUp lastItem = new TopUp();
        lastItem.setSelected(false);
        lastItem.setOption("-1");
        data.add(lastItem);
    }

    /**
     * 回调：充值飞磨豆数量选项
     */
    @Override
    public void refreshTopUpOptionData(TopUpOptionEntity data) {
        option = data.getOption();
        rateStr = data.getExchangeRate();
        if (option != null && option.length != 0) {
            transformData(Arrays.asList(option));
            adapter.setNewData(this.data);
        }
        setPrice(0);
        setTextViewText(rate, "(1人民币=" + StringUtil.formatNum(rateStr) + "飞磨豆)");
    }

    /**
     * 回调：获取支付订单信息
     */
    @Override
    public void refreshOrderInfoData(String orderInfo) {
        TopUpActivity.orderInfo = orderInfo;
        Log.d(tag, "orderInfo: " + TopUpActivity.orderInfo);
        /**
         * 切换沙箱环境与生产环境；
         * 如果不使用此方法，默认使用生产环境；
         * 在钱包不存在的情况下，会唤起h5支付；
         * // FIXME: 在生产环境，必须将此代码注释！
         */
//        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                //支付宝支付
                PayTask alipay = new PayTask(TopUpActivity.this);
                Map<String, String> result = alipay.payV2(TopUpActivity.orderInfo, true);
                Message msg = new Message();
                msg.what = Constant.ALIPAY;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 回调：个人飞磨豆数量
     */
    public void onEventMainThread(MemberCurrencyEntity event) {
        setTextViewText(currency, StringUtil.formatNum(event.getCurrency()));
    }
}
