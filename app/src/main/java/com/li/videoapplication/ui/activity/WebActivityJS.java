package com.li.videoapplication.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.li.videoapplication.data.model.entity.Download;
import com.li.videoapplication.data.model.entity.FGame;
import com.li.videoapplication.data.model.response.GameDetailEntity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.tools.DownloadHelper;
import com.li.videoapplication.tools.IntentHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;

import io.rong.eventbus.EventBus;

/**
 * 活动：网页浏览与js交互（去网页标题栏，js方法名）
 */
@SuppressLint({"SetJavaScriptEnabled", "CutPasteId"})
public class WebActivityJS extends TBaseAppCompatActivity {

    private static final String TAG = WebActivityJS.class.getSimpleName();

    private String url, title, js;
    private boolean showToolbar;
    private WebView webView;
    private JSInterface mJsInterface;

    /**
     * 网页浏览
     */
    public static void startWebActivityJS(Context context, String url, String title, String js,boolean hideToolbar) {
        if (context == null) {
            return;
        }
        if (WebActivityJS.isNull(url)) {
            return;
        }
        if (!WebActivityJS.isURL(url)) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        intent.putExtra("js", js);
        intent.putExtra("show_toolbar",hideToolbar);
        intent.setClass(context, WebActivityJS.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refreshIntent() {
        super.refreshIntent();

        url = this.getIntent().getStringExtra("url");
        if (StringUtil.isNull(url)) {
            finish();
        }
        url = url.trim();

        try {
            title = getIntent().getStringExtra("title");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            js = getIntent().getStringExtra("js");
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            showToolbar = getIntent().getBooleanExtra("show_toolbar",true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_webjs;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundWhite();
    }

    @Override
    public void initView() {
        super.initView();

        initActionBar();
        initWebView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        url = this.getIntent().getStringExtra("url");
        if (StringUtil.isNull(url)) {
            finish();
        }
        url = url.trim();

        initActionBar();
        initWebView();
    }

    private void initActionBar() {
        ImageView goback = (ImageView) findViewById(R.id.goback);
        goback.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView tb_title = (TextView) findViewById(R.id.tb_title);
        setTextViewText(tb_title, title);

        if (showToolbar){
            findViewById(R.id.ab_toolbar).setVisibility(View.VISIBLE);
        }else {
            findViewById(R.id.ab_toolbar).setVisibility(View.GONE);
        }
    }

    @SuppressWarnings("deprecation")
    private void initWebView() {
        webView = (WebView) findViewById(R.id.swi_web_webview);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebSettings webSettings = webView.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        Log.d(TAG, "initWebView: js == "+js);

        if (!StringUtil.isNull(js)) {
            mJsInterface = new JSInterface(WebActivityJS.this);
            EventBus.getDefault().register(mJsInterface);
            webView.addJavascriptInterface(mJsInterface, js);
        }

        webView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

                try {
                    IntentHelper.startActivityActionView(WebActivityJS.this, url);
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
                webView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (showToolbar){
                    requestNoTitle();
                }else {
                    //
                    if (StringUtil.isNull(view.getTitle())){
                        findViewById(R.id.ab_toolbar).setVisibility(View.VISIBLE);
                    }
                }


                UITask.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        webView.setVisibility(View.VISIBLE);
                    }
                },300);
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

                try {
                    JSONObject object = new JSONObject(message);
                    if (object != null){
                        String msg = object.getString("msg");
                        String status = object.getString("status");
                        if ("110".equals(status)){
                            ToastHelper.l(msg);
                            result.confirm();
                            return true;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    e.printStackTrace();
                }

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

        webView.loadUrl(url);
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
     * 功能：判断字符串是否为空
     */
    public static boolean isNull(String str) {
        boolean isNull = false;
        if (str == null || str.length() == 0 || str.equals("null")) {
            isNull = true;
        }
        return isNull;
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

    /**
     * 隐藏标题栏
     */
    private void requestNoTitle() {
        if (webView != null) {
            Log.d(TAG, "----------------- requestNoTitle: -----------------");
            webView.loadUrl("javascript:requestNoTitle()");
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
        EventBus.getDefault().unregister(mJsInterface);
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
}
