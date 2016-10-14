package com.li.videoapplication.ui.fragment;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.IntentHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.ActivityDetailActivity208;

/**
 * 碎片：活动规则
 */
public class ActivityRulesFragment extends TBaseFragment {

    private ActivityDetailActivity208 activity;
    public WebView webView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity = (ActivityDetailActivity208) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_activityrules;
    }

    @Override
    protected void initContentView(View view) {
        initWebView(view);
    }

    @SuppressWarnings("deprecation")
    private void initWebView(View view) {
        webView = (WebView) view.findViewById(R.id.swi_web_webview);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        WebSettings webSettings = webView.getSettings();

        webSettings.setUseWideViewPort(true);//双击变大，再双击后变小，当手动放大后，双击可以恢复到原始大小
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);//可缩放
        webSettings.setDisplayZoomControls(false);//不显示缩放按钮
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        webView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                activity.setTitle(title);
                super.onReceivedTitle(view, title);
            }
        });

        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) { //按返回键时的操作
                        webView.goBack();   //后退
                        return true;    //已处理
                    }
                }
                return false;
            }
        });

        webView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

                try {
                    IntentHelper.startActivityActionView(getActivity(), url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }
}
