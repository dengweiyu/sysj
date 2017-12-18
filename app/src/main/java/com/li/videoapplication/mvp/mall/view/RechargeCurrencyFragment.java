package com.li.videoapplication.mvp.mall.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.TopUp;
import com.li.videoapplication.data.model.event.UserInfomationEvent;
import com.li.videoapplication.data.model.response.TopUpOptionEntity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.mvp.adapter.TopUpAdapter;
import com.li.videoapplication.mvp.mall.MallContract;
import com.li.videoapplication.mvp.mall.model.MallModel;
import com.li.videoapplication.mvp.mall.presenter.MallPresenter;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.view.AdapterGridLayoutManager;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;
import com.li.videoapplication.views.CircleImageView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;


/**
 * 充值魔豆
 */

public class RechargeCurrencyFragment extends TBaseFragment implements MallContract.ITopUpView,
        View.OnClickListener {
    private static final int DEFAULT_SELECTED_POS = 1;//初始选中位置(从0开始)
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.topup_price)
    TextView price;
    @BindView(R.id.topup_rate)
    TextView rate;
    @BindView(R.id.topup_head)
    CircleImageView topupHead;
    @BindView(R.id.topup_name)
    TextView topupName;
    @BindView(R.id.topup_currency)
    TextView currency;
    @BindView(R.id.tv_my_currency_coin)
    TextView coin;
    @BindView(R.id.tv_my_currency_beans)
    TextView beans;
    @BindView(R.id.topup_pay)
    RelativeLayout paymentNow;

    private List<TopUp> data;
    private TopUpAdapter adapter;
    private String[] option;
    private TopUpActivity mActivity;
    private MallContract.IMallPresenter presenter;
    private String rateStr;
    private int selectedPos = DEFAULT_SELECTED_POS;

    public static RechargeCurrencyFragment newInstance() {
        RechargeCurrencyFragment fragment = new RechargeCurrencyFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 跳转：支付方式选择
     */
    private void startPaymentWayActivity(int number,int entry) {
        ActivityManager.startPaymentWayActivity(mActivity,Float.parseFloat(getPrice(selectedPos)),number,entry, MallModel.USE_RECHARGE_MONEY,-1,-1);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mActivity = (TopUpActivity) context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_recharge_currency;
    }

    @Override
    protected void initContentView(View view) {
        initHeader(view);
        initAdapter(view);
        addOnClickListener();
    }

    private void initHeader(View view) {
        Member member = getUser();
        CircleImageView head = (CircleImageView) view.findViewById(R.id.topup_head);
        TextView name = (TextView) view.findViewById(R.id.topup_name);
        currency = (TextView) view.findViewById(R.id.topup_currency);

        setImageViewImageNet(head, member.getAvatar());
        setTextViewText(name, member.getNickname());
        setTextViewText(currency, StringUtil.formatMoney(Float.parseFloat(member.getCurrency())));

        setTextViewText(coin, StringUtil.formatMoneyOnePoint(Float.parseFloat(member.getCoin())));
        setTextViewText(beans, StringUtil.formatMoney(Float.parseFloat(member.getCurrency())));
        paymentNow.setOnClickListener(this);
    }

    private void initAdapter(View view) {
        GridLayoutManager layoutManager = new AdapterGridLayoutManager(mActivity, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(null);
        presenter = new MallPresenter();
        presenter.setTopUpView(this);
        data = new ArrayList<>();
        adapter = new TopUpAdapter(data,this);
        recyclerView.setAdapter(adapter);
        presenter.getRechargeRule();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topup_pay:
                Log.d(tag, "onClick: adapter.getSelectedPos() = " + selectedPos);
                Log.d(tag, "onClick: adapter.getData().size() = " + adapter.getData().size());
                Log.d(tag, "onClick: adapter.getCustomInt() = " + adapter.getCustomInt());
                if (selectedPos == adapter.getData().size() - 1) {
                    if (adapter.getCustomInt() < 100) {
                        ToastHelper.s("充值数量最低为100");
                        return;
                    }
                    startPaymentWayActivity(adapter.getCustomInt(),mActivity.entry);
                } else {
                    if(option != null){
                        Log.d(tag, "onClick: final seleccted = " + option[selectedPos]);
                        startPaymentWayActivity(Integer.valueOf(option[selectedPos]),mActivity.entry);
                    }

                }
                break;
        }
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

      private void addOnClickListener() {
        //recyclerview item点击事件处理
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                TopUp record = (TopUp) adapter.getItem(position);
                if (selectedPos != position) {
                    //先取消上个item的勾选状态
                    data.get(selectedPos).setSelected(false);
                    adapter.notifyItemChanged(selectedPos);
                    //设置新Item的勾选状态
                    selectedPos = position;
                    data.get(selectedPos).setSelected(true);
                    adapter.notifyItemChanged(selectedPos);

                    if (!record.getOption().equals("-1")) {//正常充值选项
                        setPrice(position);
                    }
                }
            }

        });
    }

    public void setPrice(int pos) {
        Log.d(tag, "setPrice: pos = " + pos);
        String priceStr = "售价：" + TextUtil.toColor(getPrice(pos), "#fe5e5e") + " 元";
        price.setText(Html.fromHtml(priceStr));
    }

    private String getPrice(int pos){
        Double priceDouble;
        if (pos != adapter.getData().size() - 1) {
            priceDouble = Double.valueOf(option[pos]) / Double.valueOf(rateStr);
        } else {
            priceDouble = adapter.getCustomInt() / Double.valueOf(rateStr);
        }
        return new DecimalFormat("#0.00").format(priceDouble);
    }


    //重置转换数据，默认选择第2个
    private void transformData(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            TopUp item = new TopUp();
            if (i == DEFAULT_SELECTED_POS) {
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
     * 回调：充值魔豆数量选项
     */
    @Override
    public void refreshTopUpOptionData(TopUpOptionEntity data) {
        option = data.getOption();
        rateStr = data.getExchangeRate();
        if (option != null && option.length != 0) {
            transformData(Arrays.asList(option));

            adapter.setNewData(this.data);
            paymentNow.setVisibility(View.VISIBLE);
        }
        setPrice(DEFAULT_SELECTED_POS);
        setTextViewText(rate, "(1人民币=" + StringUtil.formatNum(rateStr) + "魔豆)");
    }

    /**
     * 事件：更新个人资料
     */
    public void onEventMainThread(UserInfomationEvent event) {

        if (event != null) {
            if (coin != null){
                try {
                    coin.setText(StringUtil.formatMoneyOnePoint(Float.parseFloat(getUser().getCoin())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (beans != null){
                try {
                    beans.setText(StringUtil.formatMoney(Float.parseFloat(getUser().getCurrency())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
