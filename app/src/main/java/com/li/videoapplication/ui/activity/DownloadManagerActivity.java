package com.li.videoapplication.ui.activity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.li.videoapplication.R;
import com.li.videoapplication.component.service.DownloadService;
import com.li.videoapplication.data.model.entity.Download;
import com.li.videoapplication.data.model.entity.TopUp;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.tools.DownloadHelper;
import com.li.videoapplication.tools.ToastHelper;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;

/**
 * 活动：下载管理
 */
public class DownloadManagerActivity extends TBaseAppCompatActivity implements View.OnClickListener,
        DownloadService.OnProgressListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.progresstext)
    TextView progresstext;
    private Download download;

    @Override
    protected int getContentView() {
        return R.layout.activity_downloadmamager;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundWhite();
    }

    @Override
    public void initView() {
        super.initView();
        initToolbar();
        initAdapter();
        addOnClickListener();
        DownloadService.addListeners(this);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView tb_title = (TextView) findViewById(R.id.tb_title);
        tb_title.setText("下载管理");

        findViewById(R.id.tb_back).setOnClickListener(this);
    }

    private void initAdapter() {


        download = new Download();
        download.setDownload_url("http://www.zhaoshangdai.com/file/android.apk");//测试，随便找的链接
        download.setTitle("招商贷");
    }

    @Override
    public void loadData() {
        super.loadData();
        DownloadHelper.downloadFile(this, download);
    }

    private void addOnClickListener() {
        //recyclerview item点击事件处理
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int pos) {

            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDownloadProgress(float fraction) {
        Log.d(tag, "下载进度：" + fraction);
        int p = (int)(fraction * 100);
        progressBar.setProgress(p);
        progresstext.setText(p+" %");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DownloadService.removeListeners(this);
    }
}
