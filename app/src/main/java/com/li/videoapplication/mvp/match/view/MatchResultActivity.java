package com.li.videoapplication.mvp.match.view;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomButtonsController;

import com.li.videoapplication.R;
import com.li.videoapplication.data.js.JSInterface;
import com.li.videoapplication.data.model.response.MatchRewardBillboardEntity;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.mvp.match.MatchContract.IMatchResultView;
import com.li.videoapplication.mvp.match.presenter.MatchPresenter;
import com.li.videoapplication.tools.IntentHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.utils.StringUtil;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 活动：赛事结果
 */
public class MatchResultActivity extends TBaseAppCompatActivity implements View.OnClickListener,
        IMatchResultView {

    private WebView webView;
    private MatchRewardBillboardEntity data;
    private TextView tb_title;
    private String event_id;

    /**
     * 分享
     */
    public void startShareActivity() {
        if (data != null) {
            String url = data.getShare_url();
            String title = data.getShare_title();
            String imageUrl = data.getShare_flag();
            String content = data.getShare_description();

            ActivityManager.startActivityShareActivity4VideoPlay(this, url, title, imageUrl, content);
        }
    }

    @Override
    public void refreshIntent() {
        super.refreshIntent();
        try {
            event_id = getIntent().getStringExtra("event_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StringUtil.isNull(event_id)) {
            finish();
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_matchrewardbillboard;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundColor(Color.BLACK);
    }

    @Override
    public void initView() {
        super.initView();

        initToolBar();

        initWebView();
    }

    @Override
    public void loadData() {
        super.loadData();
        MatchPresenter presenter = MatchPresenter.getInstance();
        presenter.setMatchResultView(this);
        presenter.getEventResult(event_id);
    }

    private void initToolBar() {
        tb_title = (TextView) findViewById(R.id.tb_title);
        ImageView goback = (ImageView) findViewById(R.id.tb_back);
        ImageView share = (ImageView) findViewById(R.id.tb_share);
        share.setVisibility(View.VISIBLE);
        goback.setOnClickListener(this);
        share.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tb_back:
                finish();
                break;
            case R.id.tb_share:
                startShareActivity();
                break;
        }
    }

    @SuppressWarnings("deprecation")
    private void initWebView() {
        webView = (WebView) findViewById(R.id.swi_web_webview);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        //添加与js的交互接口,起的名称与js代码中的接口名称要一致
        webView.addJavascriptInterface(new JSInterface(this), "ap");
        webView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

                try {
                    IntentHelper.startActivityActionView(MatchResultActivity.this, url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                setTitle(title);
                super.onReceivedTitle(view, title);
            }
        });
    }

    @SuppressWarnings("unused")
    private void setZoomControlGone(View view) {
        Class classType;
        Field field;
        try {
            classType = WebView.class;
            field = classType.getDeclaredField("mZoomButtonsController");
            field.setAccessible(true);
            ZoomButtonsController mZoomButtonsController = new ZoomButtonsController(view);
            mZoomButtonsController.getZoomControls().setVisibility(View.GONE);
            try {
                field.set(view, mZoomButtonsController);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void back() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    /**
     * 功能：URL是否合法
     */
    public static boolean isURL(String url) {

        try {
            URL mURL = new URL(url);
            System.out.println("url 正确");
            return true;
        } catch (MalformedURLException e) {
            System.out.println("url 不可用");
            return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
            webView.resumeTimers();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
            webView.pauseTimers();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (webView != null) {
            webView.clearHistory();
            webView.removeAllViewsInLayout();
            webView.clearDisappearingChildren();
            webView.clearFocus();
            webView.clearView();
            webView.loadUrl("");
            webView.destroy();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_hold, R.anim.dialog_out);
    }

    //回调：赛事结果
    @Override
    public void refreshData(MatchRewardBillboardEntity data) {
        this.data = data;
        setTextViewText(tb_title, data.getShare_title());
        if (isURL(data.getUrl()))
            webView.loadUrl(data.getUrl());
    }
}
