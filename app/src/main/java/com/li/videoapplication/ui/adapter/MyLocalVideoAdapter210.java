package com.li.videoapplication.ui.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.LocalManager;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.database.VideoCaptureManager;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.image.VideoCoverHelper;
import com.li.videoapplication.data.image.VideoDuration;
import com.li.videoapplication.data.image.VideoDurationHelper;
import com.li.videoapplication.data.local.FileUtil;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.data.local.VideoCaptureHelper;
import com.li.videoapplication.data.network.RequestExecutor;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.data.upload.Contants;
import com.li.videoapplication.data.upload.VideoShareTask208;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.tools.IntentHelper;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.activity.VideoActivity;
import com.li.videoapplication.ui.activity.VideoMangerActivity;
import com.li.videoapplication.ui.activity.VideoShareActivity;
import com.li.videoapplication.ui.dialog.VideoManagerCopyDialog;
import com.li.videoapplication.ui.dialog.VideoManagerRenameDialog;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.utils.NetUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设配器:本地视频
 */
public class MyLocalVideoAdapter210 extends BaseAdapter implements
//        AbsListView.OnScrollListener,
        VideoShareTask208.Callback {

    private final VideoMangerActivity activity;
    private String tag = this.getClass().getSimpleName();

    private List<VideoCaptureEntity> data;

    private Context context;
    private LayoutInflater inflater;

    private VideoDurationHelper durationHelper;

    private boolean result;
    private int status;
    private String msg;
    private int pro;

    private Map<String, ViewHolder> holders = new HashMap<>();

    private void printHolders() {
        for (String key : holders.keySet()) {
            System.out.println("key= " + key + "/value= " + holders.get(key));
        }
    }

    /**
     * 跳转：视频分享
     */
    private void startVideoShareActivity210(VideoCaptureEntity record) {
        ActivityManeger.startVideoShareActivity210(context, record, null, VideoShareActivity.TO_VIDEOMANAGER);
    }

    /**
     * 回调：更新进度
     */
    @Override
    public void updateProgress(String filePath, boolean result, int status, String msg, double percent) {
        Log.d(tag, "filePath=" + filePath);
        Log.d(tag, "result=" + result);
        Log.d(tag, "status=" + status);
        Log.d(tag, "msg=" + msg);
        Log.d(tag, "progress=" + percent);
        this.result = result;
        this.status = status;
        this.msg = msg;
        this.pro = (int) (percent * 100);
        printHolders();
        final ViewHolder holder = holders.get(filePath);
        if (holder != null && holder.filePath != null && holder.filePath.equals(filePath)) {
            Log.d(tag, "holder=" + holder);
            Log.d(tag, "holder.filePath=" + holder.filePath);
            holder.cover.setClickable(false);
            holder.root.setClickable(true);
            holder.play.setVisibility(View.GONE);
//            holder.cancelUpload.setVisibility(View.VISIBLE);
            holder.progressContainer.setVisibility(View.VISIBLE);
            holder.pause.setVisibility(View.GONE);
            holder.progressText.setVisibility(View.VISIBLE);
            if (pro - holder.progress.getProgress() > 0) {
                holder.progressText.setText(pro + "%");
                holder.progress.setMax(100);
                holder.progress.setProgress(pro);
            }
        }
        if (status == Contants.STATUS_SUCCESS) {
            updataRecordAndView(filePath);
            ToastHelper.s("视频上传成功");
        } else if (status == Contants.STATUS_END ||
                status == Contants.STATUS_PAUSE ||
                status == Contants.STATUS_START) {
            updataRecordAndView(filePath);
        } else if (status == Contants.STATUS_FAILURE) {
            updataRecordAndView(filePath);
            ToastHelper.l("视频上传失败");
        } else {
            updataRecordAndView(filePath);
        }
    }

    public MyLocalVideoAdapter210(final Context context, List<VideoCaptureEntity> data, ListView listView, VideoMangerActivity activity) {
        this.data = data;
        this.context = context;
        this.activity = activity;

        inflater = LayoutInflater.from(context);

        durationHelper = new VideoDurationHelper(listView);

        VideoMangerActivity.myLocalVideoDeleteData.clear();
        for (int i = 0; i < this.data.size(); i++) {
            VideoMangerActivity.myLocalVideoDeleteData.add(false);
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public VideoCaptureEntity getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"NewApi", "ResourceAsColor"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final VideoCaptureEntity record = getItem(position);
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_mylocalvideo210, null);
            holder.root = convertView.findViewById(R.id.root);
            holder.cover = (ImageView) convertView.findViewById(R.id.mylocalvideo_cover);
            holder.title = (EditText) convertView.findViewById(R.id.mylocalvideo_title);
            holder.play = (ImageView) convertView.findViewById(R.id.mylocalvideo_play);
            holder.pause = (ImageView) convertView.findViewById(R.id.mylocalvideo_pause);
            holder.deleteState = (CheckBox) convertView.findViewById(R.id.mylocalvideo_deleteState);
            holder.createTime = (TextView) convertView.findViewById(R.id.mylocalvideo_createTime);
            holder.allTime = (TextView) convertView.findViewById(R.id.mylocalvideo_allTime);
            holder.rename = convertView.findViewById(R.id.mylocalvideo_rename);
            holder.deleteButton = (TextView) convertView.findViewById(R.id.mylocalvideo_deleteButton);
            holder.progressContainer = convertView.findViewById(R.id.mylocalvideo_progress_container);
            holder.progress = (ProgressBar) convertView.findViewById(R.id.mylocalvideo_progress);
            holder.progressText = (TextView) convertView.findViewById(R.id.mylocalvideo_progress_text);
            holder.share = (ImageView) convertView.findViewById(R.id.mylocalvideo_share);
            holder.cut = (ImageView) convertView.findViewById(R.id.mylocalvideo_cut);
            holder.copy = (ImageView) convertView.findViewById(R.id.mylocalvideo_copy);
//            holder.cancelUpload = (ImageView) convertView.findViewById(R.id.mylocalvideo_cancelupload);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final String filePath = record.getVideo_path();
        final String fileName = record.getVideo_name();

        holder.title.setText(fileName);

        holder.createTime.setText("保存于：" + VideoCaptureHelper.getLastModified(record.getVideo_path()));
        holder.filePath = filePath;

        holders.put(filePath, holder);

        GlideHelper.displayVideo(context, filePath, holder.cover);

        // 视频时长
        holder.allTime.setTag(filePath + filePath);
        durationHelper.displayDuration(filePath, holder.allTime);

        holder.position = position;

        // 刷新删除状态
        refreshDeleteState(holder, convertView, position, record);

        // 刷新上传进度
        refreshProgressState(record, holder);

        // 播放监听
        holder.cover.setOnClickListener(new OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if (record.getVideo_station().equals(VideoCaptureEntity.VIDEO_STATION_SHOW) ||
                        record.getVideo_station().equals(VideoCaptureEntity.VIDEO_STATION_HIDE) ||
                        record.getVideo_station().equals(VideoCaptureEntity.VIDEO_STATION_LOCAL)) {

                    startPlayerActivity(record);
                    UmengAnalyticsHelper.onEvent(context, UmengAnalyticsHelper.SLIDER, "本地视频-有效");
                }
            }
        });

        //取消上传
//        holder.cancelUpload.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (record.getVideo_station().equals(VideoCaptureEntity.VIDEO_STATION_UPLOADING) ||
//                                record.getVideo_station().equals(VideoCaptureEntity.VIDEO_STATION_PAUSE)) {
//
//                    // 暂停上传视频服务
//                    DataManager.UPLOAD.pauseVideo210();
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//                    builder.setTitle("取消上传")
//                            .setMessage("是否取消正在上传的视频？")
//                            .setNegativeButton("取消", null)
//                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    // 移除上传信息
//                                    VideoCaptureManager.removeStationByPath(record.getVideo_path());
//                                    //更新一项数据
//                                    updataRecordAndView(record.getVideo_path());
//                                }
//                            }).show();
//                }
//            }
//        });

        // 分享监听
        holder.share.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (VideoShareTask208.isUploading()) {
                    ToastHelper.s("当前有视频在上传");
                    return;
                }

                if (record.getVideo_station().equals(VideoCaptureEntity.VIDEO_STATION_UPLOADING) ||
                        record.getVideo_station().equals(VideoCaptureEntity.VIDEO_STATION_PAUSE)) {
                    ToastHelper.s("该视频正在上传");
                    return;
                }

                if (record.getVideo_station().equals(VideoCaptureEntity.VIDEO_STATION_HIDE)) {

                }

                if (!PreferencesHepler.getInstance().isLogin()) {// 检查用户是否已登陆
                    DialogManager.showLogInDialog(context);
                    return;
                }
                // 10秒以上，30分钟以内，800M以内
                if (!share(record.getVideo_path())) {
                    return;
                }

                if (record.getVideo_station().equals(VideoCaptureEntity.VIDEO_STATION_SHOW)) {
                    final String url = AppConstant.getMUrl(record.getUpvideo_qnkey());
                    final String imageUrl = AppConstant.getCoverUrl(record.getUpvideo_flag());
                    ActivityManeger.startActivityShareActivity4MyCloudVideo(activity, url, imageUrl, fileName);
                    UmengAnalyticsHelper.onEvent(context, UmengAnalyticsHelper.SLIDER, "本地视频-分享");
                    return;
                }

                // 获取当前网络环境
                int netType = NetUtil.getNetworkType(context);
                if (netType == 0) {
                    ToastHelper.s(R.string.net_disable);
                } else if (netType == 1) {// wifi
                    startVideoShareActivity210(record);
                } else {
                    // 上传视频
                    DialogManager.showUploadVideoDialog(context, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startVideoShareActivity210(record);
                        }
                    });
                }
                UmengAnalyticsHelper.onEvent(context, UmengAnalyticsHelper.SLIDER, "本地视频-分享");
            }
        });

        // 编辑监听
        holder.cut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!PreferencesHepler.getInstance().isLogin()) {// 检查用户是否已登陆
                    ToastHelper.s("请先登录");
                }

                if (record.getVideo_station().equals(VideoCaptureEntity.VIDEO_STATION_UPLOADING) ||
                        record.getVideo_station().equals(VideoCaptureEntity.VIDEO_STATION_PAUSE)) {
                    ToastHelper.s("该视频正在上传");
                    return;
                }

                if (!edit(record.getVideo_path())) {
                    return;
                }

                ActivityManeger.startVideoEditorActivity(context, record);
                UmengAnalyticsHelper.onEvent(context, UmengAnalyticsHelper.SLIDER, "本地视频-编辑");
            }
        });

        // 复制监听
        holder.copy.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 复制文件对话框
                showCopyDialog(record);
                UmengAnalyticsHelper.onEvent(context, UmengAnalyticsHelper.SLIDER, "本地视频-复制");
            }
        });

        // 重命名监听
        holder.rename.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (record.getVideo_station().equals(VideoCaptureEntity.VIDEO_STATION_UPLOADING) ||
                        record.getVideo_station().equals(VideoCaptureEntity.VIDEO_STATION_PAUSE)) {
                    ToastHelper.s("该视频正在上传");
                    return;
                }
                showRenameDialog(v, position, record, holder);
                UmengAnalyticsHelper.onEvent(context, UmengAnalyticsHelper.SLIDER, "本地视频-重命名");
            }
        });

        holder.deleteState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 该视频正在上传
                VideoCaptureEntity entity = VideoCaptureManager.findByPath(record.getVideo_path());
                if (entity != null &&
                        entity.getVideo_station().equals(VideoCaptureEntity.VIDEO_STATION_UPLOADING) &&
                        entity.getVideo_station().equals(VideoCaptureEntity.VIDEO_STATION_PAUSE)) {
                    ToastHelper.s("该视频正在上传中，不能删除");
                    buttonView.setChecked(false);
                    return;
                }
                VideoMangerActivity.myLocalVideoDeleteData.set(position, isChecked);
                if (isChecked){
                    if (activity != null) {
                        activity.myLocalData.add(record);
                    }
                }else {
                    if (activity != null) {
                        activity.myLocalData.remove(record);
                    }
                }
                VideoMangerActivity.refreshAbTitle2();
            }
        });

        holder.deleteButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 该视频正在上传
                VideoCaptureEntity entity = VideoCaptureManager.findByPath(record.getVideo_path());
                if (entity != null &&
                        entity.getVideo_station().equals(VideoCaptureEntity.VIDEO_STATION_UPLOADING) &&
                        entity.getVideo_station().equals(VideoCaptureEntity.VIDEO_STATION_PAUSE)) {
                    ToastHelper.s("该视频正在上传中，不能删除");
                    return;
                }
                if (VideoMangerActivity.myLocalVideoDeleteData.get(position)) {
                    VideoMangerActivity.myLocalVideoDeleteData.set(position, false);
                } else {
                    VideoMangerActivity.myLocalVideoDeleteData.set(position, true);
                }
                notifyDataSetChanged();
            }
        });

        // 长按监听
        holder.root.setId(-1);
        holder.root.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VideoMangerActivity.isDeleteMode) {
                    holder.deleteButton.performClick();
                } else {
                    holder.cover.performClick();
                }
            }
        });
        holder.root.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                v.setId(position);
                VideoMangerActivity.performClick2();

                return true;
            }
        });

        return convertView;
    }

    /**
     * 刷新删除状态
     */
    private void refreshDeleteState(final ViewHolder holder, final View view, final int position, final VideoCaptureEntity record) {

        // 处于批量删除状态
        if (VideoMangerActivity.isDeleteMode) {
            holder.play.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteState.setVisibility(View.VISIBLE);
            // 如果是通过长按召唤出的，将长按的控件设置为选中
            if (holder.root.getId() != -1) {
                VideoMangerActivity.myLocalVideoDeleteData.set(position, true);
                if (activity != null) {
                    activity.myLocalData.add(record);
                }
                VideoMangerActivity.refreshAbTitle2();
                holder.root.setId(-1);
            }
            holder.deleteState.setChecked(VideoMangerActivity.myLocalVideoDeleteData.get(position));
        } else {// 于正常状态
            VideoMangerActivity.myLocalVideoDeleteData.clear();
            for (int i = 0; i < data.size(); i++) {
                VideoMangerActivity.myLocalVideoDeleteData.add(false);
            }
            holder.play.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.GONE);
            holder.deleteState.setVisibility(View.GONE);
        }
    }

    /**
     * 复制文件对话框
     */
    protected void showCopyDialog(final VideoCaptureEntity record) {

        if (record == null)
            return;

        if (activity != null) {
            // 复制本地视频
            DialogManager.showVideoManagerCopyDialog(activity,
                    record.getVideo_name(),
                    new VideoManagerCopyDialog.Callback() {
                        @Override
                        public void onCall(DialogInterface dialog, final String videoName) {

                            if (videoName == null) {
                                ToastHelper.s(R.string.videomanagercopy_tip_empty);
                                return;
                            }

                            VideoCaptureEntity entity = VideoCaptureManager.findByName(videoName);
                            if (entity != null) {
                                ToastHelper.s(R.string.videomanagercopy_tip_remane);
                                return;
                            }

                            dialog.dismiss();
                            try {
                                activity.showLoadingDialog("复制中", "正在复制文件，请稍候...", false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            RequestExecutor.start(new Runnable() {
                                @Override
                                public void run() {

                                    String filePath = record.getVideo_path();
                                    File newFile = SYSJStorageUtil.createRecPath(videoName);
                                    if (newFile != null) {
                                        // 复制文件
                                        boolean flag = FileUtil.copyFileToFile(filePath, newFile.getPath());
                                        if (flag) {

                                            try {
                                                // 复制文件
                                                FileUtil.copyFileToFile(SYSJStorageUtil.createCoverPath(filePath).getPath(),
                                                        SYSJStorageUtil.createCoverPath(newFile.getPath()).getPath());
                                                FileUtil.copyFileToFile(SYSJStorageUtil.createSubtitlePath(filePath).getPath()
                                                        , SYSJStorageUtil.createSubtitlePath(newFile.getPath()).getPath());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            // 保存
                                            VideoCaptureManager.save(newFile.getPath(),
                                                    record.getVideo_source(),
                                                    VideoCaptureEntity.VIDEO_STATION_LOCAL);

                                            // 加载本地视频
                                            LocalManager.loadVideoCaptures();

                                            UITask.post(new Runnable() {
                                                @Override
                                                public void run() {

                                                    try {
                                                        activity.changeType2SuccessDialog("成功", "视频复制已完成!", "确认");
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            });
                        }
                    });
        }
    }

    /**
     * 刷新上传进度
     */
    private void refreshProgressState(final VideoCaptureEntity record, final ViewHolder holder) {
        int progress = (int) (record.getUpvideo_precent() * 100);
        // 有视频在上传
        if (VideoShareTask208.isUploading() && VideoShareTask208.getUploadingPath().equals(record.getVideo_path())) {
            Log.d(tag, "02");
            holder.cover.setClickable(false);
            holder.root.setClickable(true);
            holder.play.setVisibility(View.GONE);
//            holder.cancelUpload.setVisibility(View.VISIBLE);
            holder.progressContainer.setVisibility(View.VISIBLE);
            holder.pause.setVisibility(View.GONE);
            holder.progressText.setVisibility(View.VISIBLE);
            holder.progressText.setText(pro + "%");
            holder.progress.setMax(100);
            holder.progress.setProgress(pro);
        } else {// 没有视频在上传
            switch (record.getVideo_station()) {
                case VideoCaptureEntity.VIDEO_STATION_UPLOADING:
                case VideoCaptureEntity.VIDEO_STATION_PAUSE: // 继续
                    Log.d(tag, "03");
                    holder.cover.setClickable(false);
                    holder.root.setClickable(true);
                    holder.play.setVisibility(View.GONE);
//                    holder.cancelUpload.setVisibility(View.VISIBLE);
                    holder.progressContainer.setVisibility(View.VISIBLE);
                    holder.pause.setVisibility(View.VISIBLE);
                    holder.progressText.setVisibility(View.GONE);
                    holder.progressText.setText(pro + "%");
                    holder.progress.setMax(100);
                    holder.progress.setProgress(progress);
                    break;
                case VideoCaptureEntity.VIDEO_STATION_HIDE:
                case VideoCaptureEntity.VIDEO_STATION_SHOW: // 已完成
                    Log.d(tag, "04");
                    holder.cover.setClickable(true);
                    holder.root.setClickable(false);
                    holder.play.setVisibility(View.VISIBLE);
//                    holder.cancelUpload.setVisibility(View.GONE);
                    holder.progressContainer.setVisibility(View.GONE);
                    holder.pause.setVisibility(View.GONE);
                    holder.progressText.setVisibility(View.GONE);
                    holder.progressText.setText(pro + "%");
                    holder.progress.setMax(100);
                    holder.progress.setProgress(0);
                    break;
                default: // 没上传
                    Log.d(tag, "05");
                    holder.cover.setClickable(true);
                    holder.root.setClickable(false);
                    holder.play.setVisibility(View.VISIBLE);
                    holder.progressContainer.setVisibility(View.GONE);
                    holder.pause.setVisibility(View.GONE);
//                    holder.cancelUpload.setVisibility(View.GONE);
                    holder.progressText.setVisibility(View.GONE);
                    holder.progressText.setText(pro + "%");
                    holder.progress.setMax(100);
                    holder.progress.setProgress(0);
                    break;
            }
        }

        // 继续，暂停
        holder.progressContainer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (VideoShareTask208.isUploading()) {
                    if (VideoShareTask208.getUploadingPath().equals(record.getVideo_path())) {
                        Log.d(tag, "refreshProgressState: 11");
                        // 暂停上传视频服务
                        DataManager.UPLOAD.pauseVideo210();
                    } else {
                        Log.d(tag, "refreshProgressState: 12");
                    }
                } else {
                    switch (record.getVideo_station()) {
                        case VideoCaptureEntity.VIDEO_STATION_LOCAL:
                            Log.d(tag, "refreshProgressState: 13");
                            break;
                        case VideoCaptureEntity.VIDEO_STATION_PAUSE:
                        case VideoCaptureEntity.VIDEO_STATION_UPLOADING:
                            Log.d(tag, "refreshProgressState: 14");
                            // 恢复上传视频服务
                            DataManager.UPLOAD.resumeVideo210(record);
                            break;
                        case VideoCaptureEntity.VIDEO_STATION_SHOW:
                            Log.d(tag, "refreshProgressState: 15");
                            break;
                        case VideoCaptureEntity.VIDEO_STATION_HIDE:
                            Log.d(tag, "refreshProgressState: 16");
                            break;
                    }
                }
            }
        });
    }

    /**
     * 更新某几项项数据
     */
    public void updataRecordsAndViews(String[] filePaths) {
        for (String filePath : filePaths) {
            updataRecordAndView(filePath);
        }
    }

    /**
     * 更新一项数据
     */
    public void updataRecordAndView(String filePath) {
        Log.d(tag, "updataRecordAndView: filePath=" + filePath);
        if (data != null && data.size() > 0 && filePath != null) {
            VideoCaptureEntity record = null;
            for (VideoCaptureEntity e : data) {
                if (e.getVideo_path().equals(filePath)) {
                    record = e;
                    break;
                }
            }
            if (record != null) {
                VideoCaptureEntity entity = VideoCaptureManager.findByPath(record.getVideo_path());
                if (entity != null) {
                    tramsformEntity(record, entity);
                    notifyDataSetChanged();
                }
            }
        }
    }

    private void tramsformEntity(VideoCaptureEntity record, VideoCaptureEntity entity) {
        Log.d(tag, "tramsformEntity: record=" + record);
        Log.d(tag, "tramsformEntity: entity=" + entity);

        record.setUpvideo_title(entity.getUpvideo_title());
        record.setUpvideo_description(entity.getUpvideo_description());
        record.setUpvideo_id(entity.getUpvideo_id());
        record.setUpvideo_key(entity.getUpvideo_key());
        record.setUpvideo_precent(entity.getUpvideo_precent());
        record.setUpvideo_qnkey(entity.getUpvideo_qnkey());
        record.setUpvideo_title(entity.getUpvideo_title());
        record.setUpvideo_token(entity.getUpvideo_token());
        record.setUpvideo_tokentime(entity.getUpvideo_tokentime());
        record.setUpvideo_gametags(entity.getUpvideo_gametags());
        record.setUpvideo_covertoken(entity.getUpvideo_covertoken());
        record.setUpvideo_flag(entity.getUpvideo_flag());
        record.setUpvideo_isofficial(entity.getUpvideo_isofficial());

        record.setVideo_name(entity.getVideo_name());
        record.setVideo_source(entity.getVideo_source());
        record.setVideo_station(entity.getVideo_station());

        record.setGame_id(entity.getGame_id());
        record.setGame_name(entity.getGame_name());
        record.setImage_path(entity.getImage_path());
        record.setJoin_id(entity.getJoin_id());
        record.setMatch_id(entity.getMatch_id());
        record.setMatch_name(entity.getMatch_name());
        record.setMember_id(entity.getMember_id());
    }

    /**
     * 重命名弹框
     */
    private void showRenameDialog(final View v, final int position, final VideoCaptureEntity record, final ViewHolder viewHolder) {

        final File oldFile = new File(record.getVideo_path());
        final String oldFileName = record.getVideo_name();

        // 重命名本地视频，截图
        DialogManager.showVideoManagerRenameDialog(
                oldFileName,
                new VideoManagerRenameDialog.Callback() {

                    @Override
                    public void onCall(DialogInterface dialog, String newFileName) {

                        if (newFileName.isEmpty()) {
                            ToastHelper.s("不能为空");
                            return;
                        }
                        File newFile = new File(oldFile.getParentFile().getPath() + "/" + newFileName + "." + FileUtil.getExtName(oldFile.getName()));
                        // 保存文件路径，文件名
                        int i = VideoCaptureManager.updateNamePathById(record.getId(), newFileName, newFile.getPath());
                        if (i == 0) {
                            ToastHelper.s("文件名已存在，请重新命名");
                        } else if (i == 1) {
                            dialog.dismiss();
                            // 重命名
                            oldFile.renameTo(newFile);
                            viewHolder.title.setText(newFileName);
                            try {
                                FileUtil.renameFile(SYSJStorageUtil.createCoverPath(oldFile.getPath()).getPath(),
                                        SYSJStorageUtil.createCoverPath(newFile.getPath()).getPath());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                FileUtil.renameFile(SYSJStorageUtil.createSubtitlePath(oldFile.getPath()).getPath(),
                                        SYSJStorageUtil.createSubtitlePath(newFile.getPath()).getPath());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            ToastHelper.s("修改成功");
                            // 加载本地视频
                            LocalManager.loadVideoCaptures();
                        } else {
                            ToastHelper.s("修改失败，请重试");
                        }
                    }
                });
    }

    /**
     * 检查视频信息
     */
    private boolean edit(String filePath) {
        // 检查文件是否存在
        File file = new File(filePath);
        if (!file.exists()) {
            ToastHelper.s("视频文件不存在");
            // 删除该视频
            VideoCaptureManager.deleteByPath(filePath);
            return false;
        }
        String duration;
        try {
            duration = VideoDuration.getDuration(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            ToastHelper.s("视频存在错误，不能编辑");
            return false;
        }
        long secs = 0;
        try {
            secs = Integer.valueOf(duration);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (secs < 10 * 1000) {
            ToastHelper.s("视频长度不能少于10秒");
            return false;
        }
        return true;
    }

    /**
     * 检查视频信息
     */
    private boolean share(String filePath) {
        // 检查文件是否存在
        File file = new File(filePath);
        if (!file.exists()) {
            ToastHelper.s("视频文件不存在");
            // 删除该视频
            VideoCaptureManager.deleteByPath(filePath);
            return false;
        }
        String duration;
        try {
            duration = VideoDuration.getDuration(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            ToastHelper.s("视频存在错误，不能分享");
            return false;
        }
        // 获取视频长度（毫秒）
        long secs = 0;
        try {
            secs = Integer.valueOf(duration);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (secs < 10 * 1000) {// 10 000
            ToastHelper.s("视频长度不能少于10秒");
            return false;
        } else if (secs > 30 * 60 * 1000) {// 1800 000
            ToastHelper.s("视频长度不能大于30分钟");
            return false;
        }
        // 计算视频文件的大小
        long fileLong = FileUtil.getFileSize(filePath);
        if ((fileLong / 1048576) > 800) {
            ToastHelper.s("视频长度不能大于800M");
            return false;
        }
        return true;
    }

    /**
     * 跳转：视频播放
     */
    private void startPlayerActivity(final VideoCaptureEntity record) {
        if (record == null)
            return;
        // 本地视频
        try {
            VideoActivity.startVideoActivity(context, record.getVideo_path(), record.getVideo_name());
            Log.d(tag, "startPlayerActivity: 1");
        } catch (Exception exc) {
            exc.printStackTrace();
            try {
                IntentHelper.startActivityActionViewMp4(context, record.getVideo_path());
                Log.d(tag, "startPlayerActivity: 2");
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.d(tag, "startPlayerActivity: 3");
            }
        }
    }

    private static class ViewHolder {
        View root;
        // 视频封面
        ImageView cover;
        // 视频名称
        EditText title;
        // 保存时间
        TextView createTime;
        // 视频总长
        TextView allTime;
        // 视频大小
        TextView size;
        int position;
        // 播放图标
        ImageView play, pause;
        // 勾选按键
        TextView deleteButton;
        // 删除状态
        CheckBox deleteState;
        View progressContainer;
        // 上传进度
        ProgressBar progress;
        TextView progressText;
        String filePath;
        // 重命名
        View rename;
        // 剪辑
        ImageView cut;
        // 分享
        ImageView share;
        // 复制
        ImageView copy;
        // 取消上传
//        ImageView cancelUpload;
    }
}
