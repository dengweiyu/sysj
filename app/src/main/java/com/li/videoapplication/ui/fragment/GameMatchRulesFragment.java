package com.li.videoapplication.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.mvp.match.view.GameMatchDetailActivity;

/**
 * 碎片：赛规
 */
public class GameMatchRulesFragment extends Fragment {

    private View rootView;
    private WebView webView;
    private TextView noData;
    private GameMatchDetailActivity activity;
    public boolean isFirstIn = true;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.activity = (GameMatchDetailActivity) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == rootView) {
            rootView = inflater.inflate(R.layout.fragment_gamematch_rules, container, false);
            initWebView();
        }
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!isFirstIn) {
                activity.loadData();
            }
            isFirstIn = false;
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.MATCH, "赛规");
        }
    }

    private void initWebView() {
        webView = (WebView) rootView.findViewById(R.id.gamematch_rules_webview);
        noData = (TextView) rootView.findViewById(R.id.gamematch_rules_nodata);
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
        ws.setLoadWithOverviewMode(true);
        ws.setJavaScriptEnabled(true);
        ws.setAllowFileAccess(true);//允许访问文件数据
        ws.setSupportZoom(true);//支持变焦
        ws.setBuiltInZoomControls(true);//可缩放
        ws.setDisplayZoomControls(false);//不显示缩放按钮
        ws.setDefaultFontSize(40);//默认字体大小
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        ws.setLoadWithOverviewMode(true);
        ws.setUseWideViewPort(true);//双击变大，再双击后变小，当手动放大后，双击可以恢复到原始大小
//        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                return true;
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
        webView.destroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != rootView)
            ((ViewGroup) rootView.getParent()).removeView(rootView);
    }

    public void setRuleUrl(String rule_url) {
        if (rule_url != null && !rule_url.equals("")) {
            noData.setVisibility(View.GONE);
            webView.loadUrl(rule_url);
        } else {
            webView.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
        }
    }
}
