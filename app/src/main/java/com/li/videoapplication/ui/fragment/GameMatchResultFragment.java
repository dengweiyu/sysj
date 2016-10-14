package com.li.videoapplication.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.li.videoapplication.R;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.activity.GameMatchDetailActivity;

/**
 * 碎片：比赛结果
 */
public class GameMatchResultFragment extends Fragment {

    private WebView webView;
    private View rootView;
    private View noData;
    private GameMatchDetailActivity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity = (GameMatchDetailActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //fragment处于当前交互状态
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {//该fragment处于最前台交互状态
            if (noData != null && webView != null)
                // 刷新数据
                refreshWebView();
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.MATCH, "结果");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == rootView) {
            rootView = inflater.inflate(R.layout.fragment_gamematch_result, container, false);
            initWebView();
        }
        return rootView;
    }

    private void initWebView() {
        webView = (WebView) rootView.findViewById(R.id.gamematch_result_webview);
        noData = rootView.findViewById(R.id.gamematch_result_nodata);

        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                return true;
            }
        });

        WebSettings ws = webView.getSettings();
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);// 排版适应屏幕
        ws.setLoadWithOverviewMode(true);//页面自适应屏幕
        ws.setJavaScriptEnabled(true);//支持js
        ws.setAllowFileAccess(true);//允许访问文件数据
        ws.setSupportZoom(true);//支持变焦
        ws.setBuiltInZoomControls(true);//可缩放
        ws.setDisplayZoomControls(false);//不显示缩放按钮
        ws.setDefaultFontSize(40);//默认字体大小
        ws.setUseWideViewPort(true);//双击变大，再双击恢复
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        refreshWebView();
    }

    private void refreshWebView() {
        if (activity != null && activity.match != null) {
            if (activity.match.getResult_status().equals("1") && activity.match.getResult_url() != null
                    && !activity.match.getResult_url().equals("")) {
                noData.setVisibility(View.GONE);
                webView.loadUrl(activity.match.getResult_url());
            } else {
                noData.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != rootView)
            ((ViewGroup) rootView.getParent()).removeView(rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webView != null)
            webView.destroy();
    }

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

}
