package com.li.videoapplication.ui.activity;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.data.model.response.GoodsDetailEntity;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.toast.ToastHelper;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;
import com.li.videoapplication.views.RoundedImageView;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 活动：商品详情
 */
public class ProductsDetailActivity extends TBaseActivity implements View.OnClickListener {


    private RoundedImageView pic;
    private ImageView contentPic;
    private TextView name, beam, illustration, ok;
    private String goods_id;
    private Currency data;

    @Override
    public void refreshIntent() {
        super.refreshIntent();
        try {
            goods_id = getIntent().getStringExtra("goods_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StringUtil.isNull(goods_id)) {
            finish();
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_productsdetail;
    }

    @Override
    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundWhite();
        setAbTitle("商品详情");
    }

    @Override
    public void initView() {
        super.initView();
        pic = (RoundedImageView) findViewById(R.id.productsdetail_pic);
        contentPic = (ImageView) findViewById(R.id.productsdetail_contentpic);
        name = (TextView) findViewById(R.id.productsdetail_name);
        beam = (TextView) findViewById(R.id.productsdetail_beam);
        illustration = (TextView) findViewById(R.id.productsdetail_illustration);
        ok = (TextView) findViewById(R.id.productsdetail_ok);

        ok.setOnClickListener(this);

        int screenWidth = ScreenUtil.getScreenWidth();
        double r = 750.00 / 520;
        int picH = (int) (screenWidth / r);
        contentPic.setMinimumHeight(picH);
    }

    @Override
    public void loadData() {
        super.loadData();
        DataManager.goodsDetail(goods_id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.productsdetail_ok:
                //exchange_way：0=>周边商品，外链；1=>推荐位；2=>话费流量类；3=>Q币类；4=>京东卡类
                if (data != null && !StringUtil.isNull(data.getExchange_way())) {
                    switch (data.getExchange_way()) {
                        case "1"://推荐位
                            ActivityManeger.startVideoMangerActivity(this);
                            break;

                        case "2"://话费流量类
                        case "3"://Q币类
                        case "4"://京东卡类
                            if (Integer.valueOf(data.getInventory()) > 0) {
                                int myCurrency = Integer.valueOf(getUser().getCurrency());

                                if (myCurrency >= Integer.valueOf(data.getCurrency_num())) {
                                    DialogManager.showPaymentDialog(this, data);
                                } else {
                                    ToastHelper.s("飞磨豆不足");
                                }
                            } else {
                                ToastHelper.s("商品已售罄");
                            }
                            break;
                    }
                }
                break;
        }
    }

    private void refreshView(Currency data) {
        setImageViewImageNet(pic, data.getCover());
        setImageViewImageNet(contentPic, data.getContent_pic());
        setTextViewText(name, data.getName());
        setTextViewText(beam, data.getCurrency_num() + "飞磨豆");

        String s = TextUtil.toColor("使用说明：","#575757") + data.getContent();
        illustration.setText(Html.fromHtml(s));

        if (!StringUtil.isNull(data.getExchange_way()) && data.getExchange_way().equals("1")) {
            setTextViewText(ok, "上传视频");
        }
    }

    /**
     * 回调:商品详情
     */
    public void onEventMainThread(GoodsDetailEntity event) {

        if (event != null && event.isResult()) {
            if (event.getData() != null) {
                data = event.getData();
                refreshView(event.getData());
            }
        }
    }
}
