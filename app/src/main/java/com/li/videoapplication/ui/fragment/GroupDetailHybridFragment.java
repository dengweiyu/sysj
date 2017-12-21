package com.li.videoapplication.ui.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.js.JSInterface;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.activity.WebActivityJS;
import com.li.videoapplication.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *圈子详情页-加载HTML
 */

public class GroupDetailHybridFragment extends TBaseFragment {

    private WebView mWebView;

    private String mUrl;

    private boolean mIsNeedLoadData = false;



    public static GroupDetailHybridFragment newInstance(String url,boolean isNeedLoadData){
        GroupDetailHybridFragment fragment = new GroupDetailHybridFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url",url);
        bundle.putBoolean("is_need_load_data",isNeedLoadData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

     /*   if (!StringUtil.isNull(mUrl) && isVisibleToUser && mWebView != null && StringUtil.isNull(mWebView.getUrl())){
            mWebView.loadUrl(mUrl);
        }*/
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_group_detail_hybrid;
    }



    @Override
    protected void initContentView(View view) {

        Bundle bundle = getArguments();
        if (bundle != null){
            mUrl = bundle.getString("url");
            mIsNeedLoadData = bundle.getBoolean("is_need_load_data");
        }

        if (mWebView != null){
            return;
        }

        mWebView = (WebView)view.findViewById(R.id.wv_hybrid);

        //
        settingWebView();
    }


    private void settingWebView(){
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);


        mWebView.addJavascriptInterface(new JSInterface(getActivity()), "user");//app与js交互接口
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //禁用长按复制
                return true;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
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
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }
        });

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                WebActivityJS.startWebActivityJS(getActivity(),url,"","Interactive",false);
                return true;
            }

            @TargetApi(21)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                WebActivityJS.startWebActivityJS(getActivity(),request.getUrl().toString(),"","Interactive",false);
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
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

                view.loadUrl("blank");
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                view.loadUrl("blank");
            }
        });

        if (mIsNeedLoadData){
            mWebView.loadUrl(mUrl);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }


    public void refresh(){
        mWebView.reload();
    }
}
