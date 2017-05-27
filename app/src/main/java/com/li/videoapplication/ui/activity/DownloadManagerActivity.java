package com.li.videoapplication.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.HttpManager;
import com.li.videoapplication.data.database.FileDownloaderEntity;
import com.li.videoapplication.data.database.FileDownloaderManager;
import com.li.videoapplication.data.download.DownLoadManager;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.data.model.entity.Download;
import com.li.videoapplication.data.model.entity.LaunchImage;
import com.li.videoapplication.data.model.event.FileDownloaderEvent;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.mvp.adapter.DownloadManagerAdapter;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.utils.ApkUtil;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observer;

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

    private String gameId;
    @Override
    public void refreshIntent() {
        super.refreshIntent();

        try {
            Intent intent = getIntent();
            entity = (LaunchImage)intent.getSerializableExtra("entity");
            gameId = intent.getStringExtra("game_id");

            getGameDownloadInfo();
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        gameId = getIntent().getStringExtra("game_id");
        getGameDownloadInfo();
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

        //从数据库还原游戏下载列表
        data1st.addAll(FileDownloaderManager.findByMark("1"));

        adapter1st = new DownloadManagerAdapter(data1st);
        recyclerView1st.setAdapter(adapter1st);

        adapter2nd = new DownloadManagerAdapter(data2nd);
        recyclerView2nd.setAdapter(adapter2nd);

    }

    @Override
    public void onBackPressed() {
        //回到后台 不销毁当前Activity
        moveTaskToBack(true);
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

    /**
     * 获取游戏下载详情
     */
    private void getGameDownloadInfo(){
        if (StringUtil.isNull(gameId)){
            return;
        }
        FileDownloaderEntity dbEntity = FileDownloaderManager.findByGameId(gameId);
        if (dbEntity != null){
            if (dbEntity.isDownloaded()){
                install(dbEntity);
                return;
            }else if (dbEntity.isInstalled()){
                open(dbEntity);
                return;
            }
        }
        if (!StringUtil.isNull(gameId)){
            HttpManager.getInstance().getGameDownloadInfo(gameId, new Observer<List<Download>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (e != null){
                        Log.e("getGameDownloadInfo",e.getMessage());
                    }
                    ToastHelper.s(R.string.net_disable);
                }

                @Override
                public void onNext(List<Download> downloads) {
                    if (downloads != null && downloads.size() > 0){
                        saveGame(downloads.get(0));
                    }
                }
            });
        }
    }

    /**
     * 保存游戏下载信息
     */
    private void saveGame(Download info){
        if (info == null){
            return;
        }
        FileDownloaderEntity entity = new FileDownloaderEntity();

        entity.setType_id(info.getType_id());
        entity.setGame_id(info.getGame_id());
        entity.setApp_name(info.getApp_name());
        entity.setFlag(info.getFlag());
        entity.setA_download_url(info.getA_download_url());
        entity.setI_download_url(info.getI_download_url());
        entity.setApp_intro(info.getApp_intro());
        entity.setSize_num(info.getSize_num());
        entity.setSize_text(info.getSize_text());
        entity.setPlay_num(info.getPlay_num());
        entity.setPlay_text(info.getPlay_text());
        entity.setFileUrl(info.getA_download_url());
        entity.setMark(info.getMark());
        entity.setDownloadSize(0L);
        if (data1st != null){
            //加入数据库 并开始下载
            try {
                FileDownloaderEntity dbEntity = FileDownloaderManager.findByGameId(entity.getGame_id());
                if (dbEntity == null){
                    if (FileDownloaderManager.save(entity) == 1){
                        //保存成功 加入列表
                        if (data1st.size() == 0){
                            adapter1st.addData(entity);
                            recyclerView1st.setVisibility(View.VISIBLE);
                        }else {
                            adapter1st.addData(0,entity);
                        }
                    }else {
                        Log.e("DownloadManager","下载信息保存失败");
                    }
                }else {
                    entity = dbEntity;
                }
                DownLoadManager.getInstance().addDownloader(entity,true,adapter1st);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
     *安装应用
     */
    private void install(FileDownloaderEntity entity){
        File tmpFile = SYSJStorageUtil.createTmpApkPath(entity.getFileUrl());
        if (tmpFile == null){
            DownLoadManager.getInstance().addDownloader(entity,true,adapter1st);
        }else {
            ApkUtil.installApp(this, entity.getFilePath());
        }
    }

    /**
     *打开应用
     */
    private void open(FileDownloaderEntity entity){
        ApkUtil.launchApp(this, entity.getPackageName());
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
