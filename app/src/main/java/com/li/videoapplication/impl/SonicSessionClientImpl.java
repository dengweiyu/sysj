package com.li.videoapplication.impl;

import android.os.Bundle;
import android.webkit.WebView;

import com.tencent.sonic.sdk.SonicSessionClient;

import java.util.HashMap;

/**
 * SonicSessionClient  is a thin API class that delegates its public API to a backend WebView class instance, such as loadUrl and loadDataWithBaseUrl.
 */

public class SonicSessionClientImpl extends SonicSessionClient {

    private WebView mWebView;

    public void bindWebView(WebView webView) {
        mWebView = webView;
    }

    public WebView getWebView() {
        return mWebView;
    }

    @Override
    public void loadUrl(String url, Bundle extraData) {
        mWebView.loadUrl(url);
    }

    @Override
    public void loadDataWithBaseUrl(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        mWebView.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    @Override
    public void loadDataWithBaseUrlAndHeader(String baseUrl, String data, String mimeType, String encoding, String historyUrl, HashMap<String, String> headers) {

    }
}
