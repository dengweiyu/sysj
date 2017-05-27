package com.li.videoapplication.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.mvp.mall.MallContract.IMallPresenter;
import com.li.videoapplication.mvp.mall.MallContract.IExchangeRecordDetailView;
import com.li.videoapplication.mvp.mall.presenter.MallPresenter;
import com.li.videoapplication.mvp.mall.view.ExchangeRecordFragment;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;
import com.li.videoapplication.views.RoundedImageView;

/**
 * 活动：订单详情
 */
public class OrderDetailActivity extends TBaseActivity implements IExchangeRecordDetailView,
        View.OnClickListener {

    private RoundedImageView pic;
    private TextView name, beam, time, rewardTime, key, value, status, code, hint;
    private TextView rejectiveReason, recommendLocation, recommendNote, recommendStatus, recommendTime;
    private String id;
    private int tab;
    private View codeView, auditingStatusView, hintView, keyView;
    private View recommendLocationView, recommendGo, recommendView;
    private Currency data;
    private IMallPresenter presenter;

    @Override
    public void refreshIntent() {
        super.refreshIntent();
        try {
            id = getIntent().getStringExtra("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StringUtil.isNull(id)) {
            finish();
        }
        try {
            tab = getIntent().getIntExtra("tab", ExchangeRecordFragment.EXC_MALL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_orderdetail;
    }

    @Override
    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundWhite();
        setAbTitle("订单详情");
    }

    @Override
    public void initView() {
        super.initView();
        presenter = new MallPresenter();
        presenter.setExchangeRecordDetailView(this);

        pic = (RoundedImageView) findViewById(R.id.orderdetail_pic);
        name = (TextView) findViewById(R.id.orderdetail_name);
        beam = (TextView) findViewById(R.id.orderdetail_beam);
        time = (TextView) findViewById(R.id.orderdetail_time);
        key = (TextView) findViewById(R.id.orderdetail_key);
        value = (TextView) findViewById(R.id.orderdetail_value);
        auditingStatusView = findViewById(R.id.orderdetail_auditingstatusview);//审核中
        status = (TextView) findViewById(R.id.orderdetail_status);
        code = (TextView) findViewById(R.id.orderdetail_code);
        codeView = findViewById(R.id.orderdetail_codeView);
        View payView = findViewById(R.id.orderdetail_payview);
        View dealView = findViewById(R.id.orderdetail_dealtimeview);
        View rewardTimeView = findViewById(R.id.orderdetail_rewardtimeview);
        rewardTime = (TextView) findViewById(R.id.orderdetail_rewardtime);
        keyView = findViewById(R.id.orderdetail_keyview);
        hintView = findViewById(R.id.orderdetail_hintview);
        hint = (TextView) findViewById(R.id.orderdetail_hint);

        recommendView = findViewById(R.id.orderdetail_recommendview);
        recommendLocationView = findViewById(R.id.orderdetail_recommendlocationview);
        recommendGo = findViewById(R.id.orderdetail_recommendgo);//立即查看
        rejectiveReason = (TextView) findViewById(R.id.orderdetail_rejectivereason);
        recommendLocation = (TextView) findViewById(R.id.orderdetail_recommendLocation);
        recommendNote = (TextView) findViewById(R.id.orderdetail_recommendnote);
        recommendStatus = (TextView) findViewById(R.id.orderdetail_recommendstatus);//已推荐
        recommendTime = (TextView) findViewById(R.id.orderdetail_recommendtime);//推荐时间...

        findViewById(R.id.orderdetail_copy).setOnClickListener(this);
        recommendGo.setOnClickListener(this);

        if (tab == ExchangeRecordFragment.EXC_SWEEPSTAKE) {
            payView.setVisibility(View.GONE);
            dealView.setVisibility(View.GONE);
            rewardTimeView.setVisibility(View.VISIBLE);
            hintView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void loadData() {
        super.loadData();
        if (tab == ExchangeRecordFragment.EXC_MALL)
            presenter.orderDetail(getMember_id(), id);
        else if (tab == ExchangeRecordFragment.EXC_SWEEPSTAKE)
            presenter.getMemberAwardDetail(getMember_id(), id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.orderdetail_copy:
                if (!StringUtil.isNull(data.getCode())) {
                    ClipboardManager clipboardManager = (ClipboardManager) this.getSystemService(CLIPBOARD_SERVICE);
                    clipboardManager.setPrimaryClip(ClipData.newPlainText("code", data.getCode()));
                    ToastHelper.s("已复制到粘贴板");
                }
                break;
            case R.id.orderdetail_recommendgo:
                ActivityManager.startMainActivity(this);
                MainActivity mainActivity = AppManager.getInstance().getMainActivity();
                mainActivity.slidingMenu.showContent();
                break;
        }
    }

    private void refreshView(Currency data) {
        if (tab == ExchangeRecordFragment.EXC_MALL) {
            setImageViewImageNet(pic, data.getCover());
            setTextViewText(name, data.getGoods_name());
            setTextViewText(beam, StringUtil.formatNum(data.getCurrency_num()) + "飞磨豆");
            try {
                String time = TimeHelper.getWholeTimeFormat(data.getAdd_time());
                setTextViewText(this.time, time);
            } catch (Exception e) {
                e.printStackTrace();
            }
            setTextViewText(key, data.getPublicKey() + "：");
            setTextViewText(value, data.getPublicValue());
            setStatus(status, data);

            if (data.getStatus().equals("4") && !StringUtil.isNull(data.getCode())) {//兑换码
                codeView.setVisibility(View.VISIBLE);
                setTextViewText(code, data.getCode());
            } else {
                codeView.setVisibility(View.GONE);
            }

        } else if (tab == ExchangeRecordFragment.EXC_SWEEPSTAKE) {
            setImageViewImageNet(pic, data.getIcon());
            setTextViewText(name, data.getName());
            setTextViewText(rewardTime, data.getAddtime());
            if (StringUtil.isNull(data.getAccountType())) {
                keyView.setVisibility(View.GONE);
            } else {
                switch (data.getAccountType()) {
                    case "mobile":
                        if (StringUtil.isNull(data.getAccount())) {
                            value.setText(Html.fromHtml(TextUtil.toColor("未绑定", "#fe5e5e")));
                        } else {
                            setTextViewText(value, data.getAccount());
                        }
                        break;
                    case "qq":
                        setTextViewText(key, "QQ 号码：");
                        if (StringUtil.isNull(data.getAccount())) {
                            value.setText(Html.fromHtml(TextUtil.toColor("未填写", "#fe5e5e")));
                        } else {
                            setTextViewText(value, data.getAccount());
                        }
                        break;
                }
            }

            if (StringUtil.isNull(data.getReminder())) {
                hintView.setVisibility(View.GONE);
            } else { //温馨提示
                setTextViewText(hint, data.getReminder());
            }

            //奖品发放状态，1=>R信息不完整，待完善、2=>Y待发放、3=>G已发放
            switch (data.getStatus()) {
                case "1"://审核不通过
                    setColor(status, R.color.currency_red);
                    break;
                case "2"://待发放
                    setColor(status, R.color.currency_modou_gold);
                    break;
                case "3"://已发放
                    setColor(status, R.color.currency_green);
                    break;
            }
        }

        if (!StringUtil.isNull(data.getExchange_way()) && data.getExchange_way().equals("1")) {//推荐位
            recommendLocationView.setVisibility(View.VISIBLE);
            recommendView.setVisibility(View.VISIBLE);
            auditingStatusView.setVisibility(View.GONE);
            setTextViewText(recommendLocation, data.getShowLocation());
            setStatus(recommendStatus, data);
            setTextViewText(recommendStatus, data.getStatusText());
            setTextViewText(recommendNote, data.getNote());
            setTextViewText(recommendTime, data.getRecommendedTime());

            switch (data.getStatus()){
                case "1"://推荐位申请失败
                    rejectiveReason.setVisibility(View.VISIBLE);
                    String reason = TextUtil.toColor("拒绝理由：", "#575757") + data.getNote();
                    rejectiveReason.setText(Html.fromHtml(reason));
                    recommendNote.setVisibility(View.GONE);
                    recommendTime.setVisibility(View.GONE);
                    break;
                case "2"://审核中
                    setColor(recommendStatus,R.color.title_bg_color);
                    recommendNote.setVisibility(View.GONE);
                    recommendTime.setVisibility(View.GONE);
                    break;
                case "6"://已推荐
                    recommendGo.setVisibility(View.VISIBLE);
                    break;
            }

        } else {
            recommendView.setVisibility(View.GONE);
            auditingStatusView.setVisibility(View.VISIBLE);

            setTextViewText(status, data.getStatusText());
        }
    }

    //商城兑换的状态
    private void setStatus(TextView textView, Currency data) {
        //订单状态：1=>R审核不通，拒绝，2=>B审核中，3=>Y待发放，4=>G已发放，6=>G已推荐，7=>R推荐已过期
        switch (data.getStatus()) {
            case "1"://审核不通过
            case "7"://推荐已过期
                setColor(textView, R.color.currency_red);
                break;
            case "2"://审核中
                setColor(textView, R.color.currency_blue);
                break;
            case "3"://待发放
                setColor(textView, R.color.currency_modou_gold);
                break;
            case "4"://已发放
            case "6"://已推荐
                setColor(textView, R.color.currency_green);
                break;
        }
    }

    private void setColor(TextView textView, int color) {
        textView.setTextColor(resources.getColor(color));
    }

    /**
     * 回调:商品兑换详情
     */
    @Override
    public void refreshOrderDetailData(Currency data) {
        this.data = data;
        refreshView(data);
    }

    /**
     * 回调:抽奖详情
     */
    @Override
    public void refreshRewardDetailData(Currency data) {
        this.data = data;
        refreshView(data);
    }
}
