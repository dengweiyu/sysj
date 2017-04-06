package com.li.videoapplication.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.database.FileDownloaderEntity;
import com.li.videoapplication.data.download.DownLoadManager;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.data.model.entity.LaunchImage;
import com.li.videoapplication.data.model.event.FileDownloaderEvent;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.mvp.adapter.DownloadManagerAdapter;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.utils.NetUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 活动：下载管理
 */
public class DownloadManagerActivity extends TBaseAppCompatActivity implements View.OnClickListener {

    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.firstRecyclerView)
    RecyclerView recyclerView1st;
    @BindView(R.id.secondRecyclerView)
    RecyclerView recyclerView2nd;
    @BindView(R.id.secondContainer)
    LinearLayout layout2nd;

    private List<FileDownloaderEntity> data1st = new ArrayList<>(), data2nd = new ArrayList<>();
    private DownloadManagerAdapter adapter1st, adapter2nd;
    private LaunchImage entity;

    @Override
    public void refreshIntent() {
        super.refreshIntent();

        try {
            entity = (LaunchImage) getIntent().getSerializableExtra("entity");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView tb_title = (TextView) findViewById(R.id.tb_title);
        tb_title.setText("下载管理");

        findViewById(R.id.tb_back).setOnClickListener(this);
    }

    private void initAdapter() {
        recyclerView1st.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2nd.setLayoutManager(new LinearLayoutManager(this));

        adapter1st = new DownloadManagerAdapter(data1st);
        recyclerView1st.setAdapter(adapter1st);

        adapter2nd = new DownloadManagerAdapter(data2nd);
        recyclerView2nd.setAdapter(adapter2nd);

    }

    @Override
    public void loadData() {
        super.loadData();
        FileDownloaderEvent event = null;
        onEventMainThread(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tb_back:
                finish();
                break;
        }
    }

    public void updateListView() {
        List<FileDownloaderEntity> newData = DownLoadManager.getInstance().getAllAdvertisement();
        DownLoadManager.getInstance().updateToView(data1st, newData);
        newData = DownLoadManager.getInstance().getAllFeimo();
        DownLoadManager.getInstance().updateToView(data2nd, newData);

        adapter1st.notifyDataSetChanged();
        adapter2nd.notifyDataSetChanged();


    }

    public void updateListViewVisablity() {
        recyclerView1st.setVisibility(data1st.size() > 0 ? View.VISIBLE : View.GONE);
        layout2nd.setVisibility(data2nd.size() > 0 ? View.VISIBLE : View.GONE);
    }

    /**
     * 事件回调: 更新下载应用信息
     */
    public void onEventMainThread(FileDownloaderEvent event) {
        Log.d(tag, "onMessage: ");

        DownLoadManager.getInstance().addAllDownLoadListener(adapter1st);
        DownLoadManager.getInstance().addAllDownLoadListener(adapter2nd);

        updateListView();
        updateListViewVisablity();
        updateInstall(entity, this);
    }

    public static void updateInstall(final LaunchImage entity, final Context context) {
        if (entity != null && entity.getDownload_android() != null) {
            final File apkFile = SYSJStorageUtil.createApkPath(entity.getDownload_android());
            if (apkFile == null || !apkFile.exists()) {// 文件不存在
                if (!DownLoadManager.getInstance().isDownLoading(entity.getDownload_android())) {// 不在下载

                    if (!NetUtil.isConnect()) {
                        ToastHelper.s(R.string.net_disable);
                    } else if (NetUtil.isWIFI()) {
                        // 开始, 继续下载
                        DownLoadManager.getInstance().startDownLoader(entity.getDownload_android(),
                                entity.getAd_id() + "");
                    } else {
                        // 文件下载
                        DialogManager.showFileDownloaderDialog(context,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // 开始, 继续下载
                                        DownLoadManager.getInstance().startDownLoader(entity.getDownload_android(), entity.getAd_id() + "");
                                    }
                                }, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // WIFI下载
//                                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                                    }
                                });
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        DownLoadManager.getInstance().removeAllDownLoadListener(adapter1st);
        DownLoadManager.getInstance().removeAllDownLoadListener(adapter2nd);
    }
}
