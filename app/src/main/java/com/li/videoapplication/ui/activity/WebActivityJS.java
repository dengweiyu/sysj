package com.li.videoapplication.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.webkit.DownloadListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ZoomButtonsController;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.database.FileDownloaderEntity;
import com.li.videoapplication.data.database.FileDownloaderManager;
import com.li.videoapplication.data.download.DownLoadListener;
import com.li.videoapplication.data.download.DownLoadManager;
import com.li.videoapplication.data.js.JSInterface;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.data.model.response.ShareInfoEntity;
import com.li.videoapplication.data.model.entity.Download;
import com.li.videoapplication.data.model.entity.FGame;
import com.li.videoapplication.data.model.response.GameDetailEntity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.tools.DownloadHelper;
import com.li.videoapplication.tools.IntentHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.utils.ApkUtil;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.URLUtil;
import com.li.videoapplication.utils.VersionUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import io.rong.eventbus.EventBus;

/**
 * 活动：网页浏览与js交互（去网页标题栏，js方法名）
 */
@SuppressLint({"SetJavaScriptEnabled", "CutPasteId"})
public class WebActivityJS extends TBaseAppCompatActivity {

    private static final String TAG = WebActivityJS.class.getSimpleName();

    private String url, title, js, id, strategyType;
    private boolean showToolbar;
    private WebView webView;
    private JSInterface mJsInterface;

    private ImageView ivShare;

    private FrameLayout mFlDownload;
    private ProgressBar mPbDownload;
    private TextView mTvDownload;


    /**
     * 网页浏览
     */
    public static void startWebActivityJS(Context context, String url, String title, String js, boolean hideToolbar) {
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
        intent.putExtra("show_toolbar", hideToolbar);
        intent.setClass(context, WebActivityJS.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startWebActivityJS(Context context, String url, String title, String js, String id, String strategyType, boolean hideToolbar) {
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
        intent.putExtra("id", id);
        intent.putExtra("strategyType", strategyType);
        intent.putExtra("show_toolbar", hideToolbar);
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
            id = getIntent().getStringExtra("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            strategyType = getIntent().getStringExtra("strategyType");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            showToolbar = getIntent().getBooleanExtra("show_toolbar", true);
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

        if (showToolbar) {
            findViewById(R.id.ab_toolbar).setVisibility(View.VISIBLE);
            if (strategyType != null && id != null && strategyType.length() != 0 && id.length() != 0) {
                ViewStub stub = (ViewStub) findViewById(R.id.vs_share);
                stub.inflate();
                ivShare = (ImageView) findViewById(R.id.iv_share);
                ivShare.setVisibility(View.VISIBLE);
                ivShare.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        share();
                    }
                });
            }
        } else {
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

        Log.d(TAG, "initWebView: js == " + js);

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
                if (showToolbar) {
                    requestNoTitle();
                    if (ivShare != null) {
                        if (view.canGoBack()) {
                            ivShare.setVisibility(View.GONE);
                        } else {
                            ivShare.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    //
                    if (StringUtil.isNull(view.getTitle())) {
                        findViewById(R.id.ab_toolbar).setVisibility(View.VISIBLE);
                    }
                }


                UITask.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        webView.setVisibility(View.VISIBLE);
                    }
                }, 300);
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
                    if (object != null) {
                        String msg = object.getString("msg");
                        String status = object.getString("status");
                        if ("110".equals(status)) {
                            ToastHelper.l(msg);
                            result.confirm();
                            return true;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
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

    public void share() {
//        DialogManager.showShareDialog(this, url, "http://apps.ifeimo.com/Public/Uploads/Member/Avatar/5a3cb354ab101.jpg", title, "此处为图文攻略内容");
        if (strategyType != null && id != null && strategyType.length() != 0 && id.length() != 0) {
            ShareInfoEntity entity = new ShareInfoEntity();
            Map<String, Object> map = new HashMap<>();
            map.put("tag", tag);
            entity.setExtra(map);
            DataManager.getHybridGroupShareInfo(id, strategyType, entity);
        }
//        ActivityManager.startActivityShareActivity4VideoPlay(this, url, title, "http://apps.ifeimo.com/Public/Uploads/Member/Avatar/5a3cb354ab101.jpg", "此处为图文攻略内容");
    }

    @Subscribe
    public void onEventMainThread(ShareInfoEntity entity) {
        if (entity.getaData() != null && entity.getExtra().get("tag").equals(tag)) {
            ActivityManager.startActivityShareActivity4VideoPlay(this,
                    entity.getaData().getShare_url(),
                    entity.getaData().getShare_title(),
                    entity.getaData().getShare_icon(),
                    entity.getaData().getShare_desc());
        } else {
            ToastHelper.s("分享失败");
        }
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

    public void onEventMainThread(JSInterface.FDEntity entity) {
        fileEntity = entity.getEntity();
        initDownloadView();
    }

    private void initDownloadView() {
        ViewStub stub = (ViewStub) findViewById(R.id.vs_download);
        if (stub != null) {
            stub.inflate();
            mFlDownload = (FrameLayout) findViewById(R.id.fl_download);
            mPbDownload = (ProgressBar) findViewById(R.id.pb_download);
            mTvDownload = (TextView) findViewById(R.id.tv_download);
        } else if (!isDownloadViewVisible()) {
            setDownloadViewVisible(true);
        }
        hideWebDownloadBtn();
        if (fileEntity != null) {
            FileDownloaderEntity localEntity = FileDownloaderManager.findByFileUrl(fileEntity.getFileUrl());
            if (localEntity != null) {
                fileEntity = localEntity;
                isDownloading = true;
                if (localEntity.isDownloaded()) { // 已下载-->安装
                    mPbDownload.setProgress(100);
                    mTvDownload.setText(R.string.applicationdownload_install);
                } else if (localEntity.isDownloading()) { // 下载中-->暂停
                    mPbDownload.setProgress(localEntity.getProgress());
                    mTvDownload.setText(localEntity.getProgress() + "%");
                } else if (localEntity.isInstalled()) { // 已安装-->打开
                    mPbDownload.setProgress(100);
                    mTvDownload.setText(R.string.applicationdownload_open);

                } else if (localEntity.isPausing()) { // 暂停-->继续
                    mPbDownload.setProgress(localEntity.getProgress());
                    mTvDownload.setText(R.string.applicationdownload_resume);
                }
            } else {
                mPbDownload.setProgress(0);
                mTvDownload.setText(R.string.applicationdownload_download);
            }
            // 增加所有任务一个监听
            DownLoadManager.getInstance().addDownLoadListener(fileEntity.getFileUrl(), downloadListener);
            mTvDownload.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fileEntity.isDownloading()) { // 下载中-->暂停
                        DownLoadManager.getInstance().stopDownLoader(fileEntity.getFileUrl()); // 暂停下载
                    } else if (fileEntity.isPausing()) { // 暂停-->继续
                        startTask();
                    } else if (fileEntity.isDownloaded()) { // 已下载-->安装
                        ApkUtil.installApp(WebActivityJS.this, fileEntity.getFilePath());
                    } else if (fileEntity.isInstalled()) { // 已安装-->打开
                        if (fileEntity.getPackageName() != null) {
                            if (fileEntity.getPackageName().equals
                                    (VersionUtils.getCurrentPackageName(WebActivityJS.this))) {// 手游视界
                                ToastHelper.s(R.string.applicationdownload_open_opened);
                            } else {
                                ApkUtil.launchApp(WebActivityJS.this, fileEntity.getPackageName());
                            }
                        }
                    } else { // -->下载
                        startTask();
                    }
                }

                private void startTask() {
                    if (!NetUtil.isConnect()) {
                        ToastHelper.s(R.string.net_disable);
                    } else if (NetUtil.isWIFI()) {
                        // 开始, 继续下载
                        DownLoadManager.getInstance().startDownLoader(fileEntity.getFileUrl(), fileEntity.getAd_id());
                    } else {
                        // 文件下载
                        DialogManager.showFileDownloaderDialog(WebActivityJS.this,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // 开始, 继续下载
                                        DownLoadManager.getInstance().startDownLoader(fileEntity.getFileUrl(), fileEntity.getAd_id());
                                    }
                                }, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // WIFI下载
                                    }
                                });
                    }
                }
            });
        }
    }

    private FileDownloaderEntity fileEntity;

    private boolean isDownloading = false;
    private DownLoadListener downloadListener = new DownLoadListener() {
        @Override
        public void preStart(FileDownloaderEntity entity) {
            Log.d(TAG, "preStart: entity=" + entity);
        }

        @Override
        public void onStart(FileDownloaderEntity entity) {
            Log.d(TAG, "onStart: entity=" + entity);
            if (fileEntity != null && fileEntity.getFileUrl().equals(entity.getFileUrl())) {
                mPbDownload.setProgress(entity.getProgress());
                mTvDownload.setText(entity.getProgress() + "%");
                mTvDownload.setBackgroundResource(R.drawable.swi_web_text_download);
                fileEntity = entity;
            }
        }

        @Override
        public void onProgress(FileDownloaderEntity entity) {
            Log.d(TAG, "onProgress: entity=" + entity);
            Log.d(TAG, "onProgress: progress=" + entity.getProgress());
            if (fileEntity != null && fileEntity.getFileUrl().equals(entity.getFileUrl())) {
                mPbDownload.setProgress(entity.getProgress());
                mTvDownload.setText(entity.getProgress() + "%");
                isDownloading = true;
                fileEntity.setDownloading(entity.isDownloading());
            }
        }

        @Override
        public void onStop(FileDownloaderEntity entity) {
            Log.d(TAG, "onStop: entity=" + entity);
            if (fileEntity != null && fileEntity.getFileUrl().equals(entity.getFileUrl())) {
                mTvDownload.setText(R.string.applicationdownload_resume);
                fileEntity = entity;
            }
        }

        @Override
        public void onError(FileDownloaderEntity entity) {
            Log.d(TAG, "onError: entity=" + entity);
            if (fileEntity != null && fileEntity.getFileUrl().equals(entity.getFileUrl())) {
                ToastHelper.s(R.string.applicationdownload_error);
                fileEntity = entity;
            }

        }

        @Override
        public void onSuccess(FileDownloaderEntity entity) {
            Log.d(TAG, "onSuccess: entity=" + entity);
            if (fileEntity != null && fileEntity.getFileUrl().equals(entity.getFileUrl())) {
                mPbDownload.setProgress(100);
                mTvDownload.setText(R.string.applicationdownload_install);
                mTvDownload.setBackgroundResource(R.drawable.swi_web_text);
                fileEntity = entity;
            }

        }

        @Override
        public void addQueue(FileDownloaderEntity entity) {
            Log.d(TAG, "addQueue: entity=" + entity);

        }
    };

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
        if (isDownloading && isDownloadViewVisible())
            ToastHelper.s(R.string.applicationdownload_go_to_download_manager);
        if (fileEntity != null)
            DownLoadManager.getInstance().removeDownLoadListener(fileEntity.getFileUrl(), downloadListener);
        if (isDownloadViewVisible())
            setDownloadViewVisible(false);
    }

    private boolean isDownloadViewVisible() {
        return mFlDownload != null && mFlDownload.getVisibility() == View.VISIBLE;
    }

    private void setDownloadViewVisible(boolean viewVisible) {
        if (mFlDownload != null) {
            if (viewVisible) {
                mFlDownload.setVisibility(View.VISIBLE);
            } else {
                mFlDownload.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 隐藏下载按钮
     */
    private void hideWebDownloadBtn(){
        if (webView != null){
            webView.loadUrl("javascript:hideGameBtn()");
            Log.d(tag, "hideGameBtn-->true");
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
