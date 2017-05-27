package com.li.videoapplication.ui.activity;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.data.model.response.GoodsDetailEntity;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.mvp.Constant;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;
import com.li.videoapplication.views.RoundedImageView;

import android.text.Html;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
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
    private WebView webView;
    private int showPage;

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
        try {
            showPage = getIntent().getIntExtra("showPage", Constant.PRODUCTSDETAIL_DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
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
        webView = (WebView) findViewById(R.id.productsdetail_webview);
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

//        showPage:0=>默认，1=>富文本页面底部无按钮，2=>富文本页面底部有按钮
        if (showPage != Constant.PRODUCTSDETAIL_DEFAULT) {
            webView.setVisibility(View.VISIBLE);
            if (showPage == Constant.PRODUCTSDETAIL_RICHTEXT_NOBTN)
                ok.setVisibility(View.GONE);
            initWebView();
        }
    }

    private void initWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(false);//关闭缩放
        webSettings.setDisplayZoomControls(false);//不显示缩放按钮
        webSettings.setDomStorageEnabled(true);
        webSettings.setTextSize(WebSettings.TextSize.LARGER);
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
                            ActivityManager.startVideoMangerActivity(this);
                            break;
                        case "5"://抽奖
                            if (isLogin()) {
                                WebActivity.startWebActivityWithJS(this,
                                        Constant.API_SWEEPSTAKE + "?mid=" + getMember_id(),
                                        Constant.JS_SWEEPSTAKE);
                            } else {
                                WebActivity.startWebActivityWithJS(this, Constant.API_SWEEPSTAKE,
                                        Constant.JS_SWEEPSTAKE);
                            }
                            break;
                        case "2"://话费流量类
                        case "3"://Q币类
                        case "4"://京东卡类
                        case "6"://战网兑换类
                            if (Integer.valueOf(data.getInventory()) > 0) {
                                if (isLogin()) {
                                    int myCurrency = Integer.valueOf(getUser().getCurrency());

                                    if (myCurrency >= Integer.valueOf(data.getCurrency_num())) {
                                        DialogManager.showPaymentDialog(this, data);
                                    } else {
                                        ToastHelper.s("飞磨豆不足");
                                    }
                                } else {
                                    DialogManager.showLogInDialog(this);
                                }
                            } else {
                                ToastHelper.s("商品已售罄");
                            }
                            UmengAnalyticsHelper.onEvent(this,UmengAnalyticsHelper.SLIDER,"视界商城-商品兑换");
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
        setTextViewText(beam, StringUtil.formatNum(data.getCurrency_num()) + "飞磨豆");

        String s = TextUtil.toColor("使用说明：", "#575757") + data.getContent();
        illustration.setText(Html.fromHtml(s));

        if (!StringUtil.isNull(data.getExchange_way()) && data.getExchange_way().equals("1")) {
            ok.setText("上传视频");
        }
        if (showPage != Constant.PRODUCTSDETAIL_DEFAULT) {
            webView.loadData(data.getPage_html(), "text/html", "utf-8");
            //加载、并显示HTML代码
            webView.loadDataWithBaseURL(null, data.getPage_html(), "text/html", "utf-8", null);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
        webView.pauseTimers();
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
        webView.resumeTimers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webView.destroy();
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
