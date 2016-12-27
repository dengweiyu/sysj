package com.li.videoapplication.data.upload;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.LocalManager;
import com.li.videoapplication.data.SubTitleManager;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.database.VideoCaptureManager;
import com.li.videoapplication.data.image.VideoCover;
import com.li.videoapplication.data.image.VideoDuration;
import com.li.videoapplication.data.local.FileUtil;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.data.model.response.DoVideoMarkEntity;
import com.li.videoapplication.data.model.response.QiniuTokenPassEntity;
import com.li.videoapplication.data.model.response.SrtUpload203Entity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.data.qiniu.http.ResponseInfo;
import com.li.videoapplication.data.qiniu.storage.KeyGenerator;
import com.li.videoapplication.data.qiniu.storage.UpCancellationSignal;
import com.li.videoapplication.data.qiniu.storage.UpCompletionHandler;
import com.li.videoapplication.data.qiniu.storage.UpProgressHandler;
import com.li.videoapplication.data.qiniu.storage.UploadManager;
import com.li.videoapplication.data.qiniu.storage.UploadOptions;
import com.li.videoapplication.data.qiniu.storage.persistent.FileRecorder;
import com.li.videoapplication.data.qiniu.utils.UrlSafeBase64;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.tools.BitmapHelper;
import com.li.videoapplication.tools.ShareSDKShareHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.activity.VideoMangerActivity;
import com.li.videoapplication.utils.BitmapUtil;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.utils.StringUtil;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import io.rong.eventbus.EventBus;

/**
 * 上传视频任务
 */
public class VideoShareTask208 {

    private static String uploadingPath;

    public static String getUploadingPath() {
        return uploadingPath;
    }

    public static boolean isUploading() {
        if (uploadingPath == null)
            return false;
        else
            return true;
    }

    /**
     * 回调
     */
    public interface Callback {

        void updateProgress(String filePath, boolean result, int status, String msg, double percent);
    }

    /**
     * 回调
     */
    private static List<Callback> callbacks = new ArrayList<>();

    public static void addCallbacks(Callback callback) {
        synchronized (VideoShareTask208.class) {
            if (callback != null && !callbacks.contains(callback))
                callbacks.add(callback);
        }
    }

    public static void removeCallbacks(Callback callback) {
        synchronized (VideoShareTask208.class) {
            if (callback != null)
                callbacks.remove(callback);
        }
    }

    public static void clearCallback() {
        synchronized (VideoShareTask208.class) {
            callbacks.clear();
        }
    }

    private String tag = this.getClass().getSimpleName();
    private static final String TAG = VideoShareTask208.class.getSimpleName();

    private VideoUploadRequestObject request;
    private int s;
    private String shareChannel = "";
    private String member_id = "";
    private String title = "";
    private String game_id = "";
    private String match_id = "";
    private String description = "";
    private int isofficial;
    private VideoCaptureEntity entity;

    private String path = "";
    private String imageUrl = "";
    private String flag = "";

    private String video_id = "";
    private String qn_key = "";
    private String is_success = "1";
    private String key = "";
    private String token = "";
    private String covertoken = "";
    private String join_id = "";
    private long tokenTime;
    private List<String> game_tags;
    private String goods_id;

    private String threadName = "LoopThread-" + tag;
    private HandlerThread thread;
    private Looper looper;
    private Handler handler;

    private Context context;

    private Callback callback;
    private boolean result;
    private int status;
    private String msg;
    private double per;

    /**
     * 取消上传
     */
    private boolean cancel;

    /**
     * 取消上传
     */
    public boolean cancel() {
        Log.d(tag, "cancel: // ------------------------------------------------------");
        if (status == Contants.STATUS_UPLOADING) {
            cancel = true;
            Log.d(tag, "cancel: true");
            return cancel;
        }
        return false;
    }

    /**
     * 回调
     */
    public void publish(boolean result, int status, String msg) {
        VideoShareResponseObject object = new VideoShareResponseObject(result, status, msg);
        EventBus.getDefault().post(object);
    }

    /**
     * 消息处理
     */
    private Handler h = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message message) {
            Log.d(tag, "h/result=" + result);
            Log.d(tag, "h/status=" + status);
            Log.d(tag, "h/msg=" + msg);
            Log.d(tag, "h/per=" + per);
            switch (status) {
                case Contants.STATUS_START:
                    // 加载本地视频
                    LocalManager.loadVideoCaptures();
                    break;

                case Contants.STATUS_PREPARING:
                    break;

                case Contants.STATUS_UPLOADING:
                    break;

                case Contants.STATUS_COMPLETING:
                    break;

                case Contants.STATUS_SUCCESS:
                    destroy();
                    break;
                case Contants.STATUS_FAILURE:
                    failure();
                    destroy();
                    break;
                case Contants.STATUS_PAUSE:
                    destroy();
                    break;

                case Contants.STATUS_END:
                    if (!result) {
                        // 保存上传状态
                        VideoCaptureManager.updateStationByPath(path,
                                VideoCaptureEntity.VIDEO_STATION_LOCAL);
                    }
                    destroy();
                    break;
            }
            publish(result, status, msg);
            for (VideoShareTask208.Callback callback : callbacks) {
                callback.updateProgress(path, result, status, msg, per);
            }
            if (callback != null) {
                callback.updateProgress(path, result, status, msg, per);
            }
            super.handleMessage(message);
        }
    };

    public VideoShareTask208(VideoUploadRequestObject request) {
        this(request, null);
    }

    public VideoShareTask208(VideoUploadRequestObject request, Callback callback) {
        super();

        Log.d(tag, "########################################################################");

        this.callback = callback;
        this.request = request;
        create();
    }

    /**
     * 初始化
     */
    private void create() {
        Log.d(tag, "create: // ------------------------------------------------------");

        this.context = AppManager.getInstance().getContext();

        if (NetUtil.getNetworkType() == 0) {
            msg = "当前网络不可用，请检查后再上传";
            status = Contants.STATUS_END;
            h.sendEmptyMessage(0);
            return;
        }

        this.s = request.getStatus();

        this.entity = request.getData();
        this.path = this.entity.getVideo_path();

        if (s == VideoUploadRequestObject.STATUS_START) {
            this.shareChannel = request.getShareChannel();
            this.member_id = request.getMember_id();
            this.title = request.getTitle();
            this.game_id = request.getGame_id();
            this.match_id = request.getMatch_id();
            this.description = request.getDescription();
            this.isofficial = request.getIsofficial();
            this.game_tags = request.getGame_tags();
            this.goods_id = request.getGoods_id();
        } else {
            VideoCaptureEntity e = VideoCaptureManager.findByPath(path);
            if (e != null)
                entity = e;
            this.shareChannel = entity.getShare_channel();
            this.member_id = entity.getMember_id();
            this.title = entity.getUpvideo_title();
            this.game_id = entity.getGame_id();
            this.match_id = entity.getMatch_id();
            this.description = entity.getUpvideo_description();
            this.isofficial = entity.getUpvideo_isofficial();
            this.game_tags = entity.getUpvideo_gametags_2();
        }

        this.video_id = this.entity.getUpvideo_id();
        this.qn_key = this.entity.getUpvideo_qnkey();
        this.join_id = this.entity.getJoin_id();
        this.tokenTime = this.entity.getUpvideo_tokentime();
        this.key = this.entity.getUpvideo_key();
        this.token = this.entity.getUpvideo_token();

        this.flag = this.entity.getUpvideo_flag();
        this.covertoken = this.entity.getUpvideo_covertoken();

        Log.d(tag, "create: shareChannel=" + shareChannel);
        Log.d(tag, "create: match_id=" + match_id);
        Log.d(tag, "create: title=" + title);
        Log.d(tag, "create: game_id=" + game_id);
        Log.d(tag, "create: match_id=" + match_id);
        Log.d(tag, "create: description=" + description);
        Log.d(tag, "create: isofficial=" + isofficial);
        Log.d(tag, "create: path=" + path);
        Log.d(tag, "create: video_id=" + video_id);
        Log.d(tag, "create: qn_key=" + qn_key);
        Log.d(tag, "create: join_id=" + join_id);
        Log.d(tag, "create: tokenTime=" + tokenTime);
        Log.d(tag, "create: key=" + key);
        Log.d(tag, "create: token=" + token);

        Log.d(tag, "create: game_tags=" + game_tags);

        Log.d(tag, "create: flag=" + flag);
        Log.d(tag, "create: covertoken=" + covertoken);
        Log.d(tag, "create: goods_id=" + goods_id);

        cancel = false;
        per = 0d;

        if (!checkVideoFile()) {
            status = Contants.STATUS_END;
            h.sendEmptyMessage(0);
            return;
        }
        if (StringUtil.isNull(member_id)) {
            msg = "请先登录";
            status = Contants.STATUS_END;
            h.sendEmptyMessage(0);
            return;
        }
        if (StringUtil.isNull(game_id)) {
            msg = "请选择类型";
            status = Contants.STATUS_END;
            h.sendEmptyMessage(0);
            return;
        }
        if (StringUtil.isNull(title)) {
            msg = "请填写标题";
            status = Contants.STATUS_END;
            h.sendEmptyMessage(0);
            return;
        }
    }

    /**
     * 销毁资源
     */
    private void destroy() {
        Log.d(tag, "destroy: // ------------------------------------------------------");
        if (thread != null)
            if (thread.isAlive())
                try {
                    thread.quit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        try {
            h.removeCallbacksAndMessages(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        uploadingPath = null;
    }

    /**
     * 上传失败
     */
    private void failure() {
        Log.d(tag, "failure: // ------------------------------------------------------");

        // 移除上传信息
        VideoCaptureManager.removeStationByPath(path);
    }

    /**
     * 视频上传凭证
     */
    private void preparing() {
        Log.d(tag, "preparing: // ------------------------------------------------------");
        Bitmap b = VideoCover.generateBitmap(entity.getVideo_path());
        int width = 0;
        int height = 0;
        if (b != null) {
            width = b.getWidth();
            height = b.getHeight();
        }
        final String time_length = VideoDuration.getDuration(entity.getVideo_path());
        try {
            if (b != null) {
                b.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 视频上传凭证
        DoVideoMarkEntity entity =
                DataManager.UPLOAD.doVideoMarkSync208(member_id,
                        title,
                        game_id,
                        String.valueOf(width),
                        String.valueOf(height),
                        time_length,
                        match_id,
                        description,
                        isofficial,
                        game_tags);

        Log.d(tag, "entity=" + entity);
        if (entity != null && entity.isResult() && entity.getData() != null) {
            key = entity.getData().getVideokey();
            token = entity.getData().getVideouploadtoken();
            flag = entity.getData().getFlag();
            covertoken = entity.getData().getCovertoken();
            join_id = entity.getData().getJoin_id();
            video_id = entity.getData().getVideo_id();
            qn_key = entity.getData().getQn_key();
            Log.d(tag, "preparing: 1");

            if (isofficial == 1 && !StringUtil.isNull(video_id) &&
                    !StringUtil.isNull(goods_id) && !StringUtil.isNull(member_id)) {

                String mobile = "";
                try {
                    mobile = PreferencesHepler.getInstance().getUserProfilePersonalInformation().getMobile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 商品兑换
                DataManager.payment(member_id, goods_id, mobile, video_id);
            }

            if (!StringUtil.isNull(key) &&
                    !StringUtil.isNull(token) &&
                    !StringUtil.isNull(qn_key) &&
                    !StringUtil.isNull(video_id)) {
                msg = "获取视频上传凭证成功";
                status = Contants.STATUS_PREPARING;
                h.sendEmptyMessage(0);
                Log.d(tag, "preparing: 2");

                // 保存上传状态
                VideoCaptureManager.updateStationByPath(path,
                        join_id,
                        video_id,
                        qn_key,
                        key,
                        token,
                        flag,
                        covertoken);

                Log.d(tag, "preparing: key=" + key);
                Log.d(tag, "preparing: key=" + key);
                Log.d(tag, "preparing: token=" + token);
                Log.d(tag, "preparing: flag=" + flag);
                Log.d(tag, "preparing: covertoken=" + covertoken);
                Log.d(tag, "preparing: join_id=" + join_id);
                Log.d(tag, "preparing: video_id=" + video_id);
                Log.d(tag, "preparing: qn_key=" + qn_key);

                Log.d(tag, "preparing: path=" + path);
                Log.d(tag, "preparing: shareChannel=" + shareChannel);
                Log.d(tag, "preparing: member_id=" + member_id);
                Log.d(tag, "preparing: game_id=" + game_id);
                Log.d(tag, "preparing: match_id=" + match_id);
                Log.d(tag, "preparing: title=" + title);
                Log.d(tag, "preparing: match_id=" + match_id);
                Log.d(tag, "preparing: description=" + description);
                Log.d(tag, "preparing: isofficial=" + isofficial);

                msg = "上传中";
                status = Contants.STATUS_UPLOADING;
                h.sendEmptyMessage(0);

                // 七牛视频上传
                qiniuRec(token, key, path);

                // 七牛封面上传
                qiniuCover(covertoken, flag);

                // 分享
                share();

                // 字幕
                subtitle();
            } else {
                msg = "获取视频上传凭证失败";
                status = Contants.STATUS_FAILURE;
                h.sendEmptyMessage(0);
                Log.d(tag, "preparing: 18");
            }
        } else {
            if (entity != null &&
                    !StringUtil.isNull(entity.getMsg()))
                msg = entity.getMsg();
            else
                msg = "获取视频上传凭证失败";
            status = Contants.STATUS_FAILURE;
            h.sendEmptyMessage(0);
            Log.d(tag, "preparing: 19");
        }
    }

    /**
     * 上传
     */
    public void upload() {
        Log.d(tag, "upload: // ------------------------------------------------------");

        uploadingPath = path;

        // 保存上传状态
        VideoCaptureManager.updateStationByPath(path, VideoCaptureEntity.VIDEO_STATION_UPLOADING);

        thread = new HandlerThread(threadName, android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        looper = thread.getLooper();
        handler = new Handler(looper);

        msg = "开始上传视频";
        status = Contants.STATUS_START;
        h.sendEmptyMessage(0);

        msg = "准备中";
        status = Contants.STATUS_PREPARING;
        h.sendEmptyMessage(0);

        handler.post(new Runnable() {
            @Override
            public void run() {
                // 保存上传状态
                VideoCaptureManager.updateStationByPath(path,
                        shareChannel,
                        member_id,
                        game_id,
                        match_id,
                        title,
                        description,
                        isofficial,
                        game_tags);

                // 如果token不过期，采用之前上传时获得的token，不再向后台申请新的token
                // 计算获得token的时间是否已经超过24小时,并且token和videokeyza不为空
                if (!StringUtil.isNull(key) &&
                        !StringUtil.isNull(token) &&
                        !StringUtil.isNull(qn_key) &&
                        !StringUtil.isNull(video_id) &&
                        verifyToken()) {

                    msg = "上传中";
                    status = Contants.STATUS_UPLOADING;
                    h.sendEmptyMessage(0);

                    // 七牛视频上传
                    qiniuRec(token, key, path);
                } else {
                    preparing();
                }
            }
        });
    }

    /**
     * 字幕
     */
    private void subtitle() {
        File srtFile = SYSJStorageUtil.createSubtitlePath(path);
        if (srtFile != null && FileUtil.isFile(srtFile.getPath())) {
            // 上传字幕
            SrtUpload203Entity a =
                    SubTitleManager.srtUpload203Sync(video_id, srtFile);
            if (a != null && a.isResult()) {
                msg = "视频字幕上传成功";
                h.sendEmptyMessage(0);
                Log.d(tag, "preparing: 11");
            } else {
                msg = "视频字幕上传失败";
                h.sendEmptyMessage(0);
                Log.d(tag, "preparing: 12");
            }
        } else {
            msg = "本地沒有视频字幕";
            h.sendEmptyMessage(0);
            Log.d(tag, "preparing: 13");
        }
    }

    /**
     * 七牛封面上传
     */
    private void qiniuCover(final String token, final String key) {
        File coverFile = SYSJStorageUtil.createCoverPath(this.entity.getVideo_path());
        if (coverFile == null ||
                !coverFile.exists()) {
            try {
                Bitmap bitmap = VideoCover.generateBitmap(this.entity.getVideo_path());
                coverFile = SYSJStorageUtil.createCoverPath(this.entity.getVideo_path());
                BitmapUtil.saveBitmap(bitmap, coverFile.getPath());
                VideoCaptureManager.updateCoverByPath(path, coverFile.getPath());
                Log.d(tag, "qiniuCover: 1");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (coverFile == null) {
            msg = "本地视频封面不存在";
            h.sendEmptyMessage(0);
            Log.d(tag, "qiniuCover: 2");
        }

        File tmpFile = SYSJStorageUtil.createCoverPath(coverFile.getPath());
        Bitmap srcBitmap = null;
        Bitmap targetBitmap = null;
        try {
            srcBitmap = BitmapUtil.readLocalBitmap(coverFile.getPath());
            targetBitmap = BitmapHelper.getCover(srcBitmap);
            Log.d(tag, "qiniuCover: 3");
            if (targetBitmap != null && tmpFile != null) {
                BitmapUtil.saveBitmap(targetBitmap, tmpFile.getPath());
                Log.d(tag, "qiniuCover: 4");
            }
            Log.d(tag, "qiniuCover: width=" + srcBitmap.getWidth());
            Log.d(tag, "qiniuCover: height=" + srcBitmap.getHeight());
            Log.d(tag, "qiniuCover: byteCount=" + srcBitmap.getByteCount());
            Log.d(tag, "qiniuCover: width=" + targetBitmap.getWidth());
            Log.d(tag, "qiniuCover: height=" + targetBitmap.getHeight());
            Log.d(tag, "qiniuCover: byteCount=" + targetBitmap.getByteCount());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BitmapUtil.recycleBitmap(srcBitmap);
            BitmapUtil.recycleBitmap(targetBitmap);
        }

        tmpFile = SYSJStorageUtil.createCoverPath(coverFile.getPath());
        if (tmpFile == null || !tmpFile.exists()) {
            tmpFile = coverFile;
            Log.d(tag, "qiniuCover: 5");
        }

        Log.d(tag, "qiniuCover: // ------------------------------------------------------");
        Log.d(tag, "qiniuCover: token=" + token);
        Log.d(tag, "qiniuCover: key=" + key);
        UploadManager uploadManager = null;
        try {
            uploadManager = new UploadManager(new FileRecorder(SYSJStorageUtil.getSysjUploadimage().getPath()),
                    new KeyGenerator() {

                        @Override
                        public String gen(String key, File file) {
                            return UrlSafeBase64.encodeToString(file.getAbsolutePath());
                        }
                    });
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (uploadManager != null) {
            uploadManager.put(tmpFile, key, token, new UpCompletionHandler() {

                @Override
                public void complete(String key, ResponseInfo respInfo, JSONObject jsonData) {
                    Log.d(tag, "respInfo=" + respInfo);
                    if (respInfo.isOK()) {
                        msg = "视频封面上传成功";
                        h.sendEmptyMessage(0);
                        Log.d(tag, "qiniuCover: 6");
                    } else {
                        msg = "视频封面上传失败";
                        h.sendEmptyMessage(0);
                        Log.d(tag, "qiniuCover: 7");
                    }
                }
            }, null);
        } else {
            msg = "视频封面上传失败";
            h.sendEmptyMessage(0);
            Log.d(tag, "qiniuCover: 8");
        }
    }

    /**
     * 七牛视频上传
     */
    private void qiniuRec(final String token, final String key, final String path) {
        Log.d(tag, "qiniuRec: // ------------------------------------------------------");
        Log.d(tag, "qiniuRec: token=" + token);
        Log.d(tag, "qiniuRec: key=" + key);
        Log.d(tag, "qiniuRec: path=" + path);
        final File file = new File(path);
        UploadOptions options = new UploadOptions(null, null, false,
                new UpProgressHandler() {

                    @Override
                    public void progress(String key, double percent) {
                        // 上传进度
                        Log.d(tag, "percent=" + percent);
                        per = percent;
                        msg = "上传中";
                        status = Contants.STATUS_UPLOADING;
                        h.sendEmptyMessage(0);
                        // 保存上传状态
                        VideoCaptureManager.updateStationByPath(path, per);
                    }
                }, new UpCancellationSignal() {

            @Override
            public boolean isCancelled() {
                return cancel;
            }
        });

        UploadManager uploadManager = null;
        try {
            uploadManager = new UploadManager(new FileRecorder(SYSJStorageUtil.getSysjUploadvideo().getPath()),
                    new KeyGenerator() {

                        @Override
                        public String gen(String key, File file) {
                            return UrlSafeBase64.encodeToString(file.getAbsolutePath());
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (uploadManager != null) {
            uploadManager.put(file, key, token, new UpCompletionHandler() {

                @Override
                public void complete(String key, ResponseInfo respInfo, JSONObject jsonData) {
                    Log.d(tag, "respInfo=" + respInfo);
                    if (respInfo.isOK()) {
                        // 上传完成
                        msg = "七牛视频上传成功";
                        per = 1.0d;
                        status = Contants.STATUS_UPLOADING;
                        h.sendEmptyMessage(0);
                        // 保存上传状态
                        VideoCaptureManager.updateStationByPath(path, per);

                        msg = "完成中";
                        status = Contants.STATUS_COMPLETING;
                        h.sendEmptyMessage(0);

                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                completing();
                            }
                        });
                    } else {
                        if (respInfo.error.equals("cancelled by user")) {// 用户取消上传
                            // 保存上传状态
                            VideoCaptureManager.updateStationByPath(path, per);
                            // 保存上传状态
                            VideoCaptureManager.updateStationByPath(path, VideoCaptureEntity.VIDEO_STATION_PAUSE);
                            // 暂停上传
                            msg = "七牛视频上传暂停";
                            status = Contants.STATUS_PAUSE;
                            h.sendEmptyMessage(0);
                        } else {
                            String errorCode = getErroeCode(respInfo.error.toString());
                            msg = errorCode;
                            status = Contants.STATUS_PAUSE;
                            h.sendEmptyMessage(0);
                        }
                    }
                }

                private String getErroeCode(String code) {
                    switch (code) {
                        case "400":
                            return "请求报文格式错误";
                        case "401":
                            return "认证授权失败";
                        case "404":
                            return "资源不存在";
                        case "405":
                            return "请求方式错误";
                        case "406":
                            return "上传的数据 CRC32 校验错误";
                        case "419":
                            return "用户账号被冻结";
                        case "478":
                            return "镜像回源失败";
                        case "503":
                            return "服务端不可用";
                        case "504":
                            return "服务端操作超时";
                        case "579":
                            return "上传成功但是回调失败";
                        case "599":
                            return "服务端操作失败";
                        case "608":
                            return "指定资源不存在或已被删除";
                        case "612":
                            return "上传的数据 CRC32 校验错误";
                        case "614":
                            return "目标资源已存在";
                        case "630":
                            return "已创建的空间数量达到上限，无法创建新空间";
                        case "631":
                            return "指定空间不存在";
                        case "640":
                            return "指定非法的marker参数";
                        case "701":
                            return "上传接收地址不正确或ctx信息已过期";
                    }
                    return "";
                }
            }, options);
        }
    }

    /**
     * 视频上传回调
     */
    private void completing() {
        Log.d(tag, "completing: // ------------------------------------------------------");
        Log.d(tag, "completing: video_id == " + video_id);
        Log.d(tag, "completing: game_id == " + game_id);
        Log.d(tag, "completing: is_success == " + is_success);
        Log.d(tag, "completing: member_id == " + member_id);
        Log.d(tag, "completing: join_id == " + join_id);
        // 图片上传回调
        QiniuTokenPassEntity entity =
                DataManager.UPLOAD.qiniuTokenPassSync(video_id,
                        game_id,
                        is_success,
                        member_id,
                        join_id);
        Log.d(tag, "entity=" + entity);
        if (entity != null && entity.isResult()) {
            // 保存上传状态
            VideoCaptureManager.updateStationByPath(path, VideoCaptureEntity.VIDEO_STATION_SHOW);
            msg = "保存视频信息成功";
            status = Contants.STATUS_SUCCESS;
            result = true;
            h.sendEmptyMessage(0);

            DataManager.TASK.videoShareVideo211(video_id, member_id);
            UmengAnalyticsHelper.onEvent(context, UmengAnalyticsHelper.MACROSCOPIC_DATA, "玩家上传视频数");
        } else {
            if (entity != null && !StringUtil.isNull(entity.getMsg())) {
                msg = entity.getMsg();
            } else {
                msg = "保存视频信息失败";
            }
            status = Contants.STATUS_FAILURE;
            h.sendEmptyMessage(0);
        }
    }

    /**
     * 分享
     */
    private void share() {
        if (!this.entity.isShare_flag()) {
            if (!StringUtil.isNull(flag)) {
                // 分享
                share(title, description, AppConstant.getWebUrl(qn_key), AppConstant.getCoverUrl(flag), shareChannel);
                Log.d(tag, "share: 15");
            } else {
                // 分享
                share(title, description, AppConstant.getWebUrl(qn_key), AppConstant.getImageUrlDef(), shareChannel);
                Log.d(tag, "share: 16");
            }
            // 保存分享状态
            VideoCaptureManager.updateShareByPath(path, true);
            msg = "分享成功";
            status = Contants.STATUS_UPLOADING;
            h.sendEmptyMessage(0);
            Log.d(tag, "share: 17");
        }
    }

    /**
     * 分享
     */
    public static void share(String title, String text, String url, String imageUrl, String shareChannel) {
        Log.d(TAG, "share: // ------------------------------------------------------");
        if (shareChannel == null)
            return;
        Activity activity = AppManager.getInstance().getActivity(VideoMangerActivity.class);

        Log.d(TAG, "share: shareChannel=" + shareChannel);
        Log.d(TAG, "share: title=" + title);
        Log.d(TAG, "share: text=" + text);
        Log.d(TAG, "share: url=" + url);
        Log.d(TAG, "share: imageUrl=" + imageUrl);
        if (!shareChannel.equals("SYSJ")) {
            Platform.ShareParams params = new Platform.ShareParams();
            if (shareChannel.equals("SinaWeibo")) {
                params.setText(text + url);
            }
            params.setImageUrl(imageUrl);
            params.setTitle(title);
            params.setText(text);
            params.setUrl(url);
            params.setTitleUrl(url);
            params.setShareType(Platform.SHARE_WEBPAGE);

            Platform platform = ShareSDK.getPlatform(activity, shareChannel);
            platform.setPlatformActionListener(ShareSDKShareHelper.getListener());
            platform.share(params);
        }
    }

    /**
     * 验证Token是否过期(24小时)
     */
    private boolean verifyToken() {
        if (tokenTime != 0) {
            Long nowTime = System.currentTimeMillis();
            if (nowTime - tokenTime < 86400000) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证视频文件是否存在
     */
    private boolean checkVideoFile() {
        File file = null;
        try {
            file = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file != null && file.exists() && file.isFile()) {
            return true;
        } else {
            msg = "上传的文件无效";
            return false;
        }
    }
}
