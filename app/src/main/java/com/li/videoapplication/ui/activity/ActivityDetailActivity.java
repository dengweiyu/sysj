package com.li.videoapplication.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.ui.ActivityManeger;

import java.lang.reflect.Field;

/**
 * 活动：活动详情
 */
@SuppressLint({ "SetJavaScriptEnabled", "CutPasteId" })
public class ActivityDetailActivity extends TBaseActivity {

	private static final String TAG = ActivityDetailActivity.class.getSimpleName();

    private Match match;

	@Override
	public void refreshIntent() {
		super.refreshIntent();
		try {
			match = (Match) getIntent().getSerializableExtra("match");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (match == null) {
			finish();
		}
	}
	
	private WebView webView;

	@Override
	public void beforeOnCreate() {
		super.beforeOnCreate();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setSystemBar(false);
	}

	@Override
	public void afterOnCreate() {
		super.afterOnCreate();
		setContentView(R.layout.activity_activitydetail);

	}

	@Override
	public void initView() {
		super.initView();

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
		
		TextView join = (TextView) findViewById(R.id.swi_web_button);
		join.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
                ActivityManeger.startVideoMangerActivity(ActivityDetailActivity.this, match);
			}
		});
		
		join.setVisibility(View.GONE);
		
		if (match.getStatus().equals("已结束")) {// 活动已结束
			join.setVisibility(View.GONE);
		} else {
			join.setVisibility(View.VISIBLE);
		}
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

		webView.loadUrl(match.getUrl());
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

			;

			@Override
			public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
				return super.onJsConfirm(view, url, message, result);
			}

			;

			@Override
			public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
				return super.onJsPrompt(view, url, message, defaultValue, result);
			}

			;

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
}
