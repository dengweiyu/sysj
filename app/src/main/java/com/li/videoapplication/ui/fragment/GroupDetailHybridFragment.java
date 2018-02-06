package com.li.videoapplication.ui.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.js.JSInterface;
import com.li.videoapplication.data.model.event.LoginEvent;
import com.li.videoapplication.data.model.response.LoginEntity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.activity.GroupDetailHybridActivity;
import com.li.videoapplication.ui.activity.WebActivityJS;
import com.li.videoapplication.ui.dialog.LogInDialog;
import com.li.videoapplication.ui.view.SimpleSwipeRefreshLayout;
import com.li.videoapplication.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import io.rong.eventbus.EventBus;

/**
 *圈子详情页-加载HTML
 */

public class GroupDetailHybridFragment extends TBaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private WebView mWebView;

    private String mUrl;

    private boolean mIsNeedLoadData = false;

    private GroupDetailHybridActivity mActivity;

    private SimpleSwipeRefreshLayout mRefreshLayout;

    private View mErrorView;

    private String gameName;
    private String title;

    private boolean isExecuteVisible = false;

    public static GroupDetailHybridFragment newInstance(String gameName, String title, String url,boolean isNeedLoadData){
        GroupDetailHybridFragment fragment = new GroupDetailHybridFragment();
        Bundle bundle = new Bundle();
        bundle.putString("game_name", gameName);
        bundle.putString("title", title);
        bundle.putString("url",url);
        bundle.putBoolean("is_need_load_data",isNeedLoadData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof GroupDetailHybridActivity ){
            mActivity = (GroupDetailHybridActivity)activity;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            isExecuteVisible = true;
            umengStatistics();
        }
    }

    private void umengStatistics(){
        if (gameName != null && gameName.length() > 0 && title != null && title.length() > 0) {
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.GAME, gameName+"-"+ "游戏圈2.0-" + title);
        }
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_group_detail_hybrid;
    }



    @Override
    protected void initContentView(View view) {

        Bundle bundle = getArguments();
        if (bundle != null){
            gameName = bundle.getString("game_name");
            title = bundle.getString("title");
            mUrl = bundle.getString("url");
            mIsNeedLoadData = bundle.getBoolean("is_need_load_data");
        }

        if (!isExecuteVisible) {
            umengStatistics();
        }

        if (mWebView != null){
            return;
        }
        mRefreshLayout = (SimpleSwipeRefreshLayout)view.findViewById(R.id.srl_pull_to_refresh);
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mRefreshLayout.setOnRefreshListener(this);

        mRefreshLayout.setRefreshing(true);

        mWebView = (WebView)view.findViewById(R.id.wv_hybrid);

        mRefreshLayout.setWebView(mWebView);

        mErrorView = view.findViewById(R.id.ll_webview_error);

        //
        settingWebView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void settingWebView(){
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
       //webSettings.setDomStorageEnabled(true);
        webSettings.setAllowContentAccess(true);
       //webSettings.setDatabaseEnabled(true);
       //webSettings.setAppCacheEnabled; (false);
        webSettings.setSavePassword(false);
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
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

                        //转换为Toast
                        if ("110".equals(status)){
                            ToastHelper.l(msg);
                            result.confirm();
                            return true;
                        }

                        //login
                        if ("120".equals(status)){
                            DialogManager.showLogInDialog(getActivity());
                            result.confirm();
                            return true;
                        }

                        //切换fragment
                        if ("122".equals(status)){
                            if (mActivity != null){
                                mActivity.setFragmentByFlagName("appraise");
                            }
                            result.confirm();
                            return true;
                        }
                        //页面跳转  两种跳转方式 取决于页面了
                        if ("222".equals(status)){
                            String title = object.optString("title");
                            String toUrl = object.optString("url");
                            String id = object.optString("id");
                            String strategyType = object.optString("strategyType");
                            WebActivityJS.startWebActivityJS(getActivity(),toUrl,title,"user",
                                    id, strategyType, !StringUtil.isNull(title));
                            result.confirm();
                            return true;
                        }

                        //登录完成 刷新页面
                        if ("119".equals(status)){
                            mWebView.reload();
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
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress >= 80){
                    mRefreshLayout.setRefreshing(false);
                }

                if (newProgress >= 60){
                    mErrorView.setVisibility(View.GONE);
                }

                if ("about:blank".equals(view.getUrl())){
                    mErrorView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                return super.onConsoleMessage(consoleMessage);
            }

            @Override
            public void onConsoleMessage(String message, int lineNumber, String sourceID) {
                super.onConsoleMessage(message, lineNumber, sourceID);
            }
        });

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                WebActivityJS.startWebActivityJS(getActivity(),url,"","user",false);
                return true;
            }

            @TargetApi(21)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                WebActivityJS.startWebActivityJS(getActivity(),request.getUrl().toString(),"","user",false);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);

                if(!StringUtil.isNull(getMember_id())){
                    login();
                }else {
                    logout();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

                mWebView.loadUrl("blank");
                mErrorView.setVisibility(View.VISIBLE);
                mWebView.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                mWebView.loadUrl("blank");
                mErrorView.setVisibility(View.VISIBLE);
                mWebView.setVisibility(View.GONE);
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

    @Override
    public void onRefresh() {
        if (mWebView != null){
            mWebView.setVisibility(View.VISIBLE);

            if (mWebView.getUrl() == null ||"about:blank".equals(mWebView.getUrl()) || mUrl.equals(mWebView.getUrl())){
                mWebView.loadUrl(mUrl);
            }else {
                mWebView.reload();
            }

        }
    }

    public boolean logout(){
        if (mWebView == null){
            return false;
        }
        mWebView.loadUrl("javascript:removewapMember_id()");
        return true;
    }

    private void login(){
        if(!StringUtil.isNull(getMember_id()) && !mIsLoad){
            mWebView.loadUrl("javascript:setwapMember_id("+getMember_id()+")");
            mIsLoad = true;
        }
    }

    public void refresh(){
        mWebView.reload();
    }

    private boolean mIsLoad = false;

    public void onEventMainThread(LoginEvent event){
            login();
    }
}
