package com.li.videoapplication.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.data.model.response.GoodsDetailEntity;
import com.li.videoapplication.data.model.response.OrderDetailEntity;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.toast.ToastHelper;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.RoundedImageView;

/**
 * 活动：订单详情
 */
public class OrderDetailActivity extends TBaseActivity implements View.OnClickListener {

    private RoundedImageView pic;
    private TextView name, beam, time, key, value, status, code;
    private String order_id;
    private View codeView;
    private Currency data;

    @Override
    public void refreshIntent() {
        super.refreshIntent();
        try {
            order_id = getIntent().getStringExtra("order_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StringUtil.isNull(order_id)) {
            finish();
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
        pic = (RoundedImageView) findViewById(R.id.orderdetail_pic);
        name = (TextView) findViewById(R.id.orderdetail_name);
        beam = (TextView) findViewById(R.id.orderdetail_beam);
        time = (TextView) findViewById(R.id.orderdetail_time);
        key = (TextView) findViewById(R.id.orderdetail_key);
        value = (TextView) findViewById(R.id.orderdetail_value);
        status = (TextView) findViewById(R.id.orderdetail_status);
        code = (TextView) findViewById(R.id.orderdetail_code);
        codeView = findViewById(R.id.orderdetail_codeView);

        findViewById(R.id.orderdetail_copy).setOnClickListener(this);
    }

    @Override
    public void loadData() {
        super.loadData();
        DataManager.orderDetail(getMember_id(), order_id);
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
        }
    }

    private void refreshView(Currency data) {
        setImageViewImageNet(pic, data.getCover());
        setTextViewText(name, data.getGoods_name());
        setTextViewText(beam, data.getCurrency_num() + "飞磨豆");
        try {
            String time = TimeHelper.getWholeTimeFormat(data.getAdd_time());
            setTextViewText(this.time, time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTextViewText(key, data.getPublicKey() + "：");
        setTextViewText(value, data.getPublicValue());

        setTextViewText(status, data.getStatusText());
        //订单状态：1=>审核不通，拒绝，2=>审核中，3=>待发放，4=>已发放，6=>已推荐
        switch (data.getStatus()) {
            case "1"://审核不通过
                setColor(R.color.currency_red);
                break;
            case "2"://审核中
                setColor(R.color.currency_blue);
                break;
            case "3"://待发放
                setColor(R.color.currency_modou_gold);
                break;
            case "4"://已发放
            case "6"://已推荐
                setColor(R.color.currency_green);
                break;
        }

        if (data.getStatus().equals("4") && !StringUtil.isNull(data.getCode())) {
            codeView.setVisibility(View.VISIBLE);
            setTextViewText(code, data.getCode());
        } else {
            codeView.setVisibility(View.GONE);
        }
    }

    private void setColor(int color) {
        status.setTextColor(resources.getColor(color));
    }

    /**
     * 回调:商品详情
     */
    public void onEventMainThread(OrderDetailEntity event) {

        if (event != null && event.isResult()) {
            if (event.getData() != null) {
                data = event.getData();
                refreshView(event.getData());
            }
        }
    }
}
