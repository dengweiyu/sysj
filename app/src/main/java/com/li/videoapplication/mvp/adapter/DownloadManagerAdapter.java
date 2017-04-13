package com.li.videoapplication.mvp.adapter;

import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.database.FileDownloaderEntity;
import com.li.videoapplication.data.download.DownLoadListener;
import com.li.videoapplication.data.download.DownLoadManager;
import com.li.videoapplication.data.local.FileUtil;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.data.network.LightTask;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.activity.DownloadManagerActivity;
import com.li.videoapplication.utils.ApkUtil;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.utils.VersionUtils;

import java.io.File;
import java.util.List;

/**
 * 适配器：下载管理
 */
public class DownloadManagerAdapter extends BaseQuickAdapter<FileDownloaderEntity, BaseViewHolder>
        implements DownLoadListener {
    private static final String TAG = DownloadManagerAdapter.class.getSimpleName();
    private TextImageHelper helper;
    private boolean isProgress = false;

    public DownloadManagerAdapter(List<FileDownloaderEntity> data) {
        super(R.layout.adapter_downloadmanager, data);
        helper = new TextImageHelper();
    }

    @Override
    protected void convert(BaseViewHolder holder, FileDownloaderEntity record) {
        if (!isProgress) {
            helper.setImageViewImageNet((ImageView) holder.getView(R.id.applicationdownload_icon), record.getFlag());
        } else {
            isProgress = false;
        }

        holder.setText(R.id.applicationdownload_title, record.getApp_name())
                .setText(R.id.applicationdownload_content, record.getApp_intro());

        setMark(record, holder);
        setRight(record, holder);
    }

    private void setMark(FileDownloaderEntity entity, BaseViewHolder holder) {
        if (entity != null) {
            String sb = entity.getPlay_num() + entity.getPlay_text() + "\t" +
                    entity.getSize_num() + entity.getSize_text();
            holder.setText(R.id.applicationdownload_mark, Html.fromHtml(sb));
        }
    }

    private void setRight(final FileDownloaderEntity entity, final BaseViewHolder holder) {
        Log.d(TAG, "setRight: isDownloading=" + entity.isDownloading());
        ProgressBar progress = holder.getView(R.id.applicationdownload_progress);
        progress.setMax(100);
        final TextView text = holder.getView(R.id.applicationdownload_text);
        ImageView delete = holder.getView(R.id.applicationdownload_delete);

        if (entity.isDownloading()) {// 下载中-->暂停
            Log.d(TAG, "setRight: isDownloading");
            progress.setProgress(entity.getProgress());
            text.setText(entity.getProgress() + "%");
            text.setBackgroundResource(0);
            text.setTextColor(Color.parseColor("#565656"));
            delete.setVisibility(View.GONE);
        } else if (entity.isPausing()) {// 暂停-->继续
            Log.d(TAG, "setRight: isPausing");
            progress.setProgress(entity.getProgress());
            text.setText(R.string.applicationdownload_resume);
            text.setBackgroundResource(0);
            text.setTextColor(Color.parseColor("#565656"));
            delete.setVisibility(View.GONE);
        } else if (entity.isInstalled()) {// 已安装-->打开
            Log.d(TAG, "setRight: isInstalled");
            progress.setProgress(0);
            text.setText(R.string.applicationdownload_open);
            text.setBackgroundResource(R.drawable.downloadmanager_install_bg);
            text.setTextColor(Color.parseColor("#fe5e5e"));
            delete.setVisibility(View.GONE);
        } else if (entity.isDownloaded()) {// 已下载-->安装
            Log.d(TAG, "setRight: isDownloaded");
            progress.setProgress(0);
            text.setText(R.string.applicationdownload_install);
            text.setBackgroundResource(R.drawable.downloadmanager_install_bg);
            text.setTextColor(Color.parseColor("#fe5e5e"));
            delete.setVisibility(View.GONE);
        } else {// -->下载
            Log.d(TAG, "setRight: download");
            progress.setProgress(0);
            if (entity.getApp_name() != null && entity.getApp_name().equals(mContext.getString(R.string.app_name))) {
                text.setText(R.string.applicationdownload_update);
            } else {
                text.setText(R.string.applicationdownload_download);
            }
            text.setBackgroundResource(R.drawable.downloadmanager_install_bg);
          //  text.setTextColor(Color.parseColor("#f9f9f9"));
            delete.setVisibility(View.GONE);
        }


        text.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                if (entity.isDownloading()) {// 下载中-->暂停
                    Log.d(TAG, "onClick: isDownloading");
                    // 暂停下载
                    DownLoadManager.getInstance().stopDownLoader(entity.getFileUrl());
                    text.setText(R.string.applicationdownload_resume);
                } else if (entity.isPausing()) {// 暂停-->继续
                    if (!NetUtil.isConnect()) {
                        ToastHelper.s(R.string.net_disable);
                        return;
                    }
                    Log.d(TAG, "onClick: isPausing");
                    // 继续下载
                    startTask();
                } else if (entity.isInstalled()) {// 已安装-->打开
                    Log.d(TAG, "onClick: isInstalled");
                    if (entity.getPackageName() != null) {
                        if (entity.getPackageName().equals(VersionUtils.getCurrentPackageName(mContext))) {// 手游视界
                            ToastHelper.s(R.string.applicationdownload_open_opened);
                        } else {
                            ApkUtil.launchApp(mContext, entity.getPackageName());
                            // 广告点击统计（16——打开游戏）
//                            DataManager.advertisementAdClick204_16(entity.getAd_id());
                        }
                    }
                } else if (entity.isDownloaded()) {// 已下载-->安装
                    Log.d(TAG, "onClick: isDownloaded");
                    ApkUtil.installApp(mContext, entity.getFilePath());
                    // 广告点击统计（15——启动安装）
//                    DataManager.advertisementAdClick204_15(entity.getAd_id());

                } else {// -->下载
                    if (!NetUtil.isConnect()) {
                        ToastHelper.s(R.string.net_disable);
                        return;
                    }
                    Log.d(TAG, "onClick: download");
                    startTask();
                }


                v.setEnabled(false);
                v.setClickable(false);
                UITask.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.setEnabled(true);
                        v.setClickable(true);
                    }
                }, 800);
            }

            private void startTask() {
                if (!NetUtil.isConnect()) {
                    ToastHelper.s(R.string.net_disable);
                } else if (NetUtil.isWIFI()) {
                    // 开始, 继续下载
                    DownLoadManager.getInstance().startDownLoader(entity.getFileUrl(), entity.getAd_id());
                } else {
                    // 文件下载
                    DialogManager.showFileDownloaderDialog(mContext,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // 开始, 继续下载
                                    DownLoadManager.getInstance().startDownLoader(entity.getFileUrl(), entity.getAd_id());
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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManager.showDeleteApkDialog(mContext,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 暂停下载
                                DownLoadManager.getInstance().stopDownLoader(entity.getFileUrl());
                                // 删除文件
                                LightTask.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        File apkFile = SYSJStorageUtil.createApkPath(entity.getFileUrl());
                                        File tmpFile = SYSJStorageUtil.createTmpApkPath(entity.getFileUrl());
                                        try {
                                            FileUtil.deleteFile(apkFile.getPath());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            FileUtil.deleteFile(tmpFile.getPath());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                // 更新所有任务（从数据库更新到下载器）
                                DownLoadManager.getInstance().updateFromDatabase();

                                notifyDataSetChanged();

                                try {
                                    ((DownloadManagerActivity) mContext).updateListView();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
            }
        });
    }

    // ----------------------------------------------------------------
    @Override
    public void preStart(FileDownloaderEntity entity) {

    }

    @Override
    public void onStart(FileDownloaderEntity entity) {
        for(FileDownloaderEntity e : getData()){
            if(e.getFileUrl().equals(entity.getFileUrl())){
                e.setDownloadSize(entity.getDownloadSize());
                e.setFileSize(entity.getFileSize());
                e.setDownloading(entity.isDownloading());
                Log.d(TAG, "onStart: e=" + e);
                notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public void onProgress(FileDownloaderEntity entity) {
        for(FileDownloaderEntity e : getData()){
            if(e.getFileUrl().equals(entity.getFileUrl())){
                e.setDownloadSize(entity.getDownloadSize());
                e.setFileSize(entity.getFileSize());
                e.setDownloading(entity.isDownloading());
                Log.d(TAG, "onProgress: progress=" + entity.getProgress());
                isProgress = true;
                notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public void onStop(FileDownloaderEntity entity) {
        for(FileDownloaderEntity e : getData()){
            if(e.getFileUrl().equals(entity.getFileUrl())){
                e.setDownloadSize(entity.getDownloadSize());
                e.setFileSize(entity.getFileSize());
                e.setDownloading(entity.isDownloading());
                Log.d(TAG, "onStop: e=" + e);
                notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public void onError(FileDownloaderEntity entity) {
        for(FileDownloaderEntity e : getData()){
            if(e.getFileUrl().equals(entity.getFileUrl())){
                e.setDownloading(entity.isDownloading());
                Log.d(TAG, "onError: e=" + e);
                notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public void onSuccess(FileDownloaderEntity entity) {
        for(FileDownloaderEntity e : getData()){
            if(e.getFileUrl().equals(entity.getFileUrl())){
                e.setDownloadSize(entity.getDownloadSize());
                e.setFileSize(entity.getFileSize());
                e.setDownloading(entity.isDownloading());
                Log.d(TAG, "onSuccess: e=" + e);
                notifyDataSetChanged();
                break;
            }
        }
    }
}
