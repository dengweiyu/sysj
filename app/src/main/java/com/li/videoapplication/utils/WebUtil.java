package com.li.videoapplication.utils;

import com.li.videoapplication.ui.activity.ImageActivity;
import com.li.videoapplication.ui.activity.WebActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
/**
 * 功能：网页工具
 */
@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
public class WebUtil {

	public void initWebView(Activity mActivity, WebView mWebView, String content) {
		
		WebSettings mWebSettings = mWebView.getSettings();
		
		mWebSettings.setSupportZoom(false);
		mWebSettings.setBuiltInZoomControls(false);
		mWebSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		mWebSettings.setDefaultFontSize(18);
		
		mWebSettings.setJavaScriptEnabled(true);
		mWebView.addJavascriptInterface(new JavascriptInterface(mActivity), "imagelistner");

		mWebView.setWebViewClient(new Client(mActivity));

		mWebView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
	}

	public class JavascriptInterface {
		private Context context;

		public JavascriptInterface(Context context) {
			this.context = context;
		}

		public void openImage(String img) {
			ImageActivity.startImageActivityWeb(context, img);
		}
	}

	@SuppressLint("SetJavaScriptEnabled")
	public class Client extends WebViewClient {

		public Activity mActivity = null;

		public Client(Activity mActivity) {
			super();
			this.mActivity = mActivity;
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			WebActivity.startWebActivity(mActivity, url);
			return true;

			// view.loadUrl(url);
			// return true;

			// return super.shouldOverrideUrlLoading(view, url);
		}

		@SuppressLint("SetJavaScriptEnabled")
		@Override
		public void onPageFinished(WebView view, String url) {
			view.getSettings().setJavaScriptEnabled(true);
			super.onPageFinished(view, url);
			view.loadUrl("javascript:(function(){" + "var objs = document.getElementsByTagName(\"img\"); " + "for(var i=0;i<objs.length;i++)  " + "{" + "    objs[i].onclick=function()  " + "    {  " + "        window.imagelistner.openImage(this.src);  " + "    }  " + "}" + "})()");
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			view.getSettings().setJavaScriptEnabled(true);

			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
		}
	}

	public static void synCookies(Context context, String url, String cookie) {
		if (TextUtils.isEmpty(url)) {
			return;
		}
		if (TextUtils.isEmpty(cookie)) {
			return;
		}
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		cookieManager.removeSessionCookie();
		cookieManager.setCookie(url, cookie);
		CookieSyncManager.getInstance().sync();
	}
}
