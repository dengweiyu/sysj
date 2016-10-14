package com.li.videoapplication.ui.activity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ZoomButtonsController;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.IntentHelper;
import com.li.videoapplication.utils.StringUtil;

/**
 * 活动：网页浏览
 */
@SuppressLint({ "SetJavaScriptEnabled", "CutPasteId" })
public class WebActivity extends TBaseActivity {

	private static final String TAG = WebActivity.class.getSimpleName();

	/**
	 * 网页浏览
	 */
	public static void startWebActivity(Context context, String url) {
		Log.e(TAG, "url=" + url);
		if (context == null) {
			return;
		}
		if (WebActivity.isNull(url)) {
			return;
		}
		if (!WebActivity.isURL(url)) {
			return;
		}
		Intent intent = new Intent();
		intent.putExtra("url", url);
		intent.setClass(context, WebActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 网页浏览
	 */
	public static void startWebActivityNewTask(Context context, String url) {
		Log.e(TAG, "url=" + url);
		if (context == null) {
			return;
		}
		if (WebActivity.isNull(url)) {
			return;
		}
		if (!WebActivity.isURL(url)) {
			return;
		}
		Intent intent = new Intent();
		intent.putExtra("url", url);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClass(context, WebActivity.class);
		context.startActivity(intent);
	}

	private String url;

	@Override
	public void refreshIntent() {
		super.refreshIntent();

		url = this.getIntent().getStringExtra("url");
		if (StringUtil.isNull(url)) {
			finish();
		}
		url = url.trim();
	}

	@Override
	public int getContentView() {
		return R.layout.swi_web;
	}

	@Override
	public int inflateActionBar() {
		return 0;
	}

	private WebView webView;

	@Override
	public void beforeOnCreate() {
		super.beforeOnCreate();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setSystemBar(false);
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

	private void initActionBar() {
		ImageView goback = (ImageView) findViewById(R.id.swi_goback);
		goback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
	}

	@SuppressWarnings("deprecation")
	private void initWebView() {
		webView = (WebView) findViewById(R.id.swi_web_webview);
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		WebSettings webSettings = webView.getSettings();

		webSettings.setJavaScriptEnabled(true);
		webSettings.setDomStorageEnabled(true);

		/*
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setSupportZoom(true);
		webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setDefaultFontSize(20);
		webSettings.setUseWideViewPort(true);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webSettings.setLoadWithOverviewMode(true);*/
		// setZoomControlGone(webView);

		webView.setDownloadListener(new DownloadListener() {

			@Override
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

				try {
					IntentHelper.startActivityActionView(WebActivity.this, url);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		webView.loadUrl(url);
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
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
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
		     System.out.println( "url 正确");
		     return true;
		} catch (MalformedURLException e) {
		     System.out.println( "url 不可用");
		     return false;
		}
	}
}
