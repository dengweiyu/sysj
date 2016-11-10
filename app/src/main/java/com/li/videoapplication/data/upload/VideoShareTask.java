package com.li.videoapplication.data.upload;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.database.VideoCaptureManager;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.data.local.StorageUtil;
import com.li.videoapplication.data.model.response.QiniuTokenPassEntity;
import com.li.videoapplication.data.model.response.VideoDoVideoMark203Entity;
import com.li.videoapplication.data.model.response.VideoQiniuTokenPass203Entity;
import com.li.videoapplication.data.model.response.VideoUploadPicQiniuEntity;
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
import com.li.videoapplication.framework.BaseResponseEntity;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.activity.MainActivity;
import com.li.videoapplication.utils.LogHelper;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.utils.StringUtil;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import io.rong.eventbus.EventBus;

/**
 * 上传视频任务
 */
public class VideoShareTask{

    public static final int STATUS_START = Contants.STATUS_START;
    public static final int STATUS_PREPARING = Contants.STATUS_PREPARING;
    public static final int STATUS_UPLOADING = Contants.STATUS_UPLOADING;
    public static final int STATUS_COMPLETING = Contants.STATUS_COMPLETING;
    public static final int STATUS_SUCCESS = Contants.STATUS_SUCCESS;
    public static final int STATUS_PAUSE = Contants.STATUS_PAUSE;
    public static final int STATUS_END = Contants.STATUS_END;

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
        synchronized (VideoShareTask.class) {
            if (callback != null && !callbacks.contains(callback))
                callbacks.add(callback);
        }
    }

    public static void removeCallbacks(Callback callback) {
        synchronized (VideoShareTask.class) {
            if (callback != null)
                callbacks.remove(callback);
        }
    }

    public static void clearCallback() {
        synchronized (VideoShareTask.class) {
            callbacks.clear();
        }
    }

    // private static final String tag = VideoShareTask.class.getSimpleName();
    private String action = this.getClass().getName();
    private String tag = this.getClass().getSimpleName();

    private VideoShareRequestObject request;

    private int s;
    private String shareChannel;
    private String member_id = new String();
    private String title = new String();
    private String game_id = new String();
    private String match_id = new String();
    private String description = new String();
    private int isofficial;
    private String channel = "1";
    private VideoCaptureEntity entity;

    private String path = new String();
    private String imagePath = new String();

    private String pk_id;
    private String video_id;
    private String qn_key;
    private String is_success = "1";
    private String key = new String();
    private String token = new String();
    private String join_id = new String();
    private long tokenTime;

    private String threadName = "TASK-" + tag;
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
        if (status == STATUS_UPLOADING) {
            cancel = true;
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
            Log.i(tag, "h/result=" + result);
            Log.i(tag, "h/status=" + status);
            Log.i(tag, "h/msg=" + msg);
            Log.i(tag, "h/per=" + per);
            publish(result, status, msg);
            for (VideoShareTask.Callback callback : callbacks) {
                callback.updateProgress(path, result, status, msg, per);
            }
            if (callback != null) {
                callback.updateProgress(path, result, status, msg, per);
            }
            switch (status) {
                case STATUS_START:
                    // 加载本地视频
                    DataManager.LOCAL.loadVideoCaptures();
                    break;

                case STATUS_PREPARING:
                    break;

                case STATUS_UPLOADING:
                    break;

                case STATUS_COMPLETING:
                    break;

                case STATUS_SUCCESS:
                    destroy();
                    break;

                case STATUS_PAUSE:
                    destroy();
                    break;

                case STATUS_END:
                    if (!result) {
                        // 保存上传状态
                        VideoCaptureManager.updateStationByPath(path,
                                VideoCaptureEntity.VIDEO_STATION_LOCAL);
                    }
                    destroy();
                    break;
            }
            super.handleMessage(message);
        }
    };

    private UploadManager uploadManager;

    public VideoShareTask(VideoShareRequestObject request) {
        this(request, null);
    }

    public VideoShareTask(VideoShareRequestObject request, Callback callback) {
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

        int netType = NetUtil.getNetworkType(context);
        if (netType == 0) {
            msg = "当前网络不可用，请检查后再上传";
            status = STATUS_END;
            h.sendEmptyMessage(0);
            return;
        }

        this.s = request.getStatus();

        this.entity = request.getData();
        if (s == VideoShareRequestObject.STATUS_START) {
            this.shareChannel = request.getShareChannel();
            this.member_id = request.getMember_id();
            this.title = request.getTitle();
            this.game_id = request.getGame_id();
            this.match_id = request.getMatch_id();
            this.description = request.getDescription();
            this.isofficial = request.getIsofficial();
        } else {
            this.shareChannel = entity.getShare_channel();
            this.member_id = entity.getMember_id();
            this.title = entity.getUpvideo_title();
            this.game_id = entity.getGame_id();
            this.match_id = entity.getMatch_id();
            this.description = entity.getUpvideo_description();
            this.isofficial = entity.getUpvideo_isofficial();
        }

        this.path = this.entity.getVideo_path();
        this.video_id = this.entity.getUpvideo_id();
        this.qn_key = this.entity.getUpvideo_qnkey();
        this.key = this.entity.getUpvideo_key();
        this.token = this.entity.getUpvideo_token();
        this.join_id = this.entity.getJoin_id();
        this.tokenTime = this.entity.getUpvideo_tokentime();

        cancel = false;
        per = 0d;

        if (!checkVideoFile()) {
            status = STATUS_END;
            h.sendEmptyMessage(0);
            return;
        }
        if (StringUtil.isNull(member_id)) {
            msg = "请先登录";
            status = STATUS_END;
            h.sendEmptyMessage(0);
            return;
        }
        if (StringUtil.isNull(game_id)) {
            msg = "请选择类型";
            status = STATUS_END;
            h.sendEmptyMessage(0);
            return;
        }
        if (StringUtil.isNull(title)) {
            msg = "请填写标题";
            status = STATUS_END;
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
     * 上传
     */
    public void upload() {
        Log.d(tag, "upload: // ------------------------------------------------------");
        uploadingPath = path;

        // 保存上传状态
        VideoCaptureManager.updateStationByPath(path, VideoCaptureEntity.VIDEO_STATION_UPLOADING);

        this.imagePath = this.entity.getImage_path();
        if (!checkCoverFile()) {
            this.imagePath = generateVideoCover(path);
            VideoCaptureManager.updateCoverByPath(path, this.imagePath);
        }

        thread = new HandlerThread(threadName, android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        looper = thread.getLooper();
        handler = new Handler(looper);

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

        msg = "开始上传视频";
        status = STATUS_START;
        h.sendEmptyMessage(0);

        msg = "准备中";
        status = STATUS_PREPARING;
        h.sendEmptyMessage(0);

        handler.post(new Runnable() {
            @Override
            public void run() {
                // 保存上传状态
                VideoCaptureManager.updateStationByPath(path, shareChannel, member_id, game_id, match_id, title, description, isofficial);

                // 如果token不过期，采用之前上传时获得的token，不再向后台申请新的token
                // 计算获得token的时间是否已经超过24小时,并且token和videokeyza不为空
                if (!StringUtil.isNull(key) &&
                        !StringUtil.isNull(token) &&
                        !StringUtil.isNull(qn_key) &&
                        !StringUtil.isNull(video_id) &&
                        verifyToken()) {

                    msg = "上传中";
                    status = STATUS_UPLOADING;
                    h.sendEmptyMessage(0);

                    qiniu(token, key, path);
                } else {
                    preparing();
                }
            }
        });
    }

    /**
     * 视频上传凭证
     */
    private void preparing() {
        Log.d(tag, "preparing: // ------------------------------------------------------");
        Bitmap b = generateVideoBitmap(entity.getVideo_path());
        int width = b.getWidth();
        int height = b.getHeight();
        final String time_length = generateVideoLength(entity.getVideo_path());

        // 视频上传凭证
        VideoDoVideoMark203Entity entity =
                DataManager.UPLOAD.videoDoVideoMark203(member_id, title,
                        game_id,
                        String.valueOf(width),
                        String.valueOf(height),
                        time_length,
                        channel,
                        match_id,
                        description,
                        isofficial);
        Log.i(tag, "entity=" + entity);
        if (entity != null && entity.isResult() &&
                entity.getData() != null) {
            key = entity.getData().getVideokey();
            token = entity.getData().getVideouploadtoken();
            join_id = entity.getData().getJoin_id();
            video_id = entity.getData().getVideo_id();
            qn_key = entity.getData().getQn_key();
            Log.d(tag, "preparing: 1");

            if (!StringUtil.isNull(key) &&
                    !StringUtil.isNull(token) &&
                    !StringUtil.isNull(qn_key) &&
                    !StringUtil.isNull(video_id)) {
                msg = "获取视频上传凭证成功";
                status = STATUS_PREPARING;
                h.sendEmptyMessage(0);
                Log.d(tag, "preparing: 2");

                // 保存上传状态
                VideoCaptureManager.updateStationByPath(path, join_id, video_id, qn_key, key, token);
                msg = "上传中";
                status = STATUS_UPLOADING;
                h.sendEmptyMessage(0);

                qiniu(token, key, path);

                File file = null;
                try {
                    file = new File(imagePath);
                    Log.d(tag, "preparing: 3");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (file == null) {
                    msg = "本地视频封面不存在";
                    status = STATUS_END;
                    h.sendEmptyMessage(0);
                    Log.d(tag, "preparing: 4");
                }
                // 上传视频封面
                VideoUploadPicQiniuEntity e =
                        DataManager.UPLOAD.videoUploadPicQiniu(video_id, file);
                Log.d(tag, "preparing: 5");
                Log.i(tag, "entity=" + e);

                if (e != null && e.isResult()) {
                    msg = "视频封面上传成功";
                    status = STATUS_UPLOADING;
                    h.sendEmptyMessage(0);
                    Log.d(tag, "preparing: 6");

                    if (e.getData() != null && e.getData().getFlag() != null) {
                        imagePath = e.getData().getFlag();
                        Log.d(tag, "imagePath: "+imagePath);
                        Log.d(tag, "preparing: 7");
                    }

                } else {
                    if (!StringUtil.isNull(entity.getMsg()))
                        msg = entity.getMsg();
                    else
                        msg = "视频封面上传失败";
                    result = true;
                    status = STATUS_UPLOADING;
                    h.sendEmptyMessage(0);
                    Log.d(tag, "preparing: 8");
                }

                if (!this.entity.isShare_flag()) {
                    // 分享
                    share(title, description, AppConstant.getShareUrl(qn_key), imagePath);
                    // 保存分享状态
                    VideoCaptureManager.updateShareByPath(path, true);
                    msg = "分享成功";
                    status = STATUS_UPLOADING;
                    h.sendEmptyMessage(0);
                    Log.d(tag, "preparing: 9");
                }
            } else {
                msg = "获取视频上传凭证失败";
                status = STATUS_END;
                h.sendEmptyMessage(0);
                Log.d(tag, "preparing: 10");
            }
        } else {
            if (entity != null && !StringUtil.isNull(entity.getMsg()))
                msg = entity.getMsg();
            else
                msg = "获取视频上传凭证失败";
            status = STATUS_END;
            h.sendEmptyMessage(0);
            Log.d(tag, "preparing: 11");
        }
    }

    /**
     * 七牛上传
     */
    private void qiniu(final String token, final String key, final String path) {

        final File file = new File(path);
        UploadOptions options = new UploadOptions(null, null, false,
                new UpProgressHandler() {

                    @Override
                    public void progress(String key, double percent) {
                        // 上传进度
                        Log.i(tag, "percent=" + percent);
                        per = percent;
                        status = STATUS_UPLOADING;
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

        uploadManager.put(file, key, token, new UpCompletionHandler() {

            @Override
            public void complete(String key, ResponseInfo respInfo, JSONObject jsonData) {
                Log.i(tag, "respInfo=" + respInfo);
                if (respInfo.isOK()) {
                    // 上传完成
                    msg = "七牛视频上传成功";
                    per = 1.0d;
                    status = STATUS_UPLOADING;
                    h.sendEmptyMessage(0);
                    // 保存上传状态
                    VideoCaptureManager.updateStationByPath(path, per);

                    msg = "完成中";
                    status = STATUS_COMPLETING;
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
                        status = STATUS_PAUSE;
                        h.sendEmptyMessage(0);
                    } else {
                        msg = getErroeCode(respInfo.error);
                        status = STATUS_UPLOADING;
                        h.sendEmptyMessage(0);
                    }
                }
            }

            private String getErroeCode(String code) {
                if (code.equals("400")) {
                    return "请求报文格式错误";
                } else if (code.equals("401")) {
                    return "认证授权失败";
                } else if (code.equals("401")) {
                    return "认证授权失败";
                } else if (code.equals("404")) {
                    return "资源不存在";
                } else if (code.equals("405")) {
                    return "请求方式错误";
                } else if (code.equals("406")) {
                    return "上传的数据 CRC32 校验错误";
                } else if (code.equals("419")) {
                    return "用户账号被冻结";
                } else if (code.equals("478")) {
                    return "镜像回源失败";
                } else if (code.equals("503")) {
                    return "服务端不可用";
                } else if (code.equals("504")) {
                    return "服务端操作超时";
                } else if (code.equals("579")) {
                    return "上传成功但是回调失败";
                } else if (code.equals("599")) {
                    return "服务端操作失败";
                } else if (code.equals("608")) {
                    return "指定资源不存在或已被删除";
                } else if (code.equals("612")) {
                    return "上传的数据 CRC32 校验错误";
                } else if (code.equals("614")) {
                    return "目标资源已存在";
                } else if (code.equals("630")) {
                    return "已创建的空间数量达到上限，无法创建新空间";
                } else if (code.equals("631")) {
                    return "指定空间不存在";
                } else if (code.equals("640")) {
                    return "指定非法的marker参数";
                } else if (code.equals("701")) {
                    return "上传接收地址不正确或ctx信息已过期";
                }
                return "";
            }
        }, options);
    }

    /**
     * 视频上传回调
     */
    private void completing() {
        // 图片上传回调
        QiniuTokenPassEntity entity =
                DataManager.UPLOAD.qiniuTokenPassSync(video_id, game_id, is_success,member_id, join_id);


        Log.i(tag, "entity=" + entity);
        if (entity != null && entity.isResult()) {
            // 保存上传状态
            VideoCaptureManager.updateStationByPath(path, VideoCaptureEntity.VIDEO_STATION_SHOW);
            msg = "保存视频信息成功";
            status = STATUS_SUCCESS;
            h.sendEmptyMessage(0);

            DataManager.TASK.videoShareVideo211(video_id, member_id);
            UmengAnalyticsHelper.onEvent(context, UmengAnalyticsHelper.MACROSCOPIC_DATA, "玩家上传视频数");
        } else {
            if (entity != null && !StringUtil.isNull(entity.getMsg()))
                msg = entity.getMsg();
            else
                msg = "保存视频信息失败";
            status = STATUS_END;
            h.sendEmptyMessage(0);
        }
    }

    /**
     * 分享
     */
    public void share(String title, String text, String url, String imageUrl) {
        if (shareChannel == null)
            return;
        // 新浪微博文字内容无法携带超链接
        // 所以将超链接直接放在分享内容中
        if (!shareChannel.equals("SYSJ")) {
            Platform.ShareParams sp = new Platform.ShareParams();
            if (shareChannel.equals("SinaWeibo")) {
                sp.setText(text + url);
            }
            sp.setImageUrl(imageUrl);
            sp.setTitle(title);
            sp.setText(text);
            sp.setUrl(url);
            sp.setTitleUrl(url);
            sp.setShareType(Platform.SHARE_WEBPAGE);

            LogHelper.d(tag, "imageUrl: " + imageUrl);
            LogHelper.d(tag, "title: " + title);
            LogHelper.d(tag, "text: " + text);
            LogHelper.d(tag, "url: " + url);

            Platform platform = ShareSDK.getPlatform(AppManager.getInstance().getActivity(MainActivity.class), shareChannel);
            platform.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    LogHelper.d(tag, "onComplete ---- ");
                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                    LogHelper.d(tag, "onError ---- Throwable : " + throwable);
                }

                @Override
                public void onCancel(Platform platform, int i) {
                }
            });
            platform.share(sp);
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
    private boolean checkCoverFile() {
        if (imagePath == null)
            return false;
        File file = null;
        try {
            file = new File(imagePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file != null && file.exists() && file.isFile() && file.getName().endsWith(".png")) {
            return true;
        } else {
            msg = "上传的文件无效";
            return false;
        }
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

    /**
     * 根据视频地址生成一张图片
     */
    private static Bitmap generateVideoBitmap(String filePath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(filePath);
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        int seconds = Integer.valueOf(time);
        Bitmap b = retriever.getFrameAtTime(seconds / 2 * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        return b;
    }

    /**
     * 根据视频地址获取视频长度
     */
    @SuppressLint("NewApi")
    public String generateVideoLength(String filePath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        String duration = "0";
        retriever.setDataSource(filePath);
        duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        return duration;
    }

    /**
     * 根据视频地址生成视频封面
     */
    public static String generateVideoCover(String filePath) {
        // 从视频中获取一张位图
        Bitmap srcBitmap = generateVideoBitmap(filePath);
        // 封面大小处理
        int newWidth = 450;
        int newHeight = 254;
        Bitmap finallBitmap;
        int srcW, srcH;
        srcW = srcBitmap.getWidth();
        srcH = srcBitmap.getHeight();
        // 直接按比压缩
        if ((srcW < 720 && srcH < 720) || srcW > srcH) {
            Bitmap turnBmp;
            if (srcW < srcH) {// 竖屏
                Matrix mt = new Matrix();
                mt.setRotate(90);
                turnBmp = Bitmap.createBitmap(srcBitmap, 0, 0, srcW, srcH, mt, true);
            } else {
                turnBmp = srcBitmap;
            }
            int trunW = turnBmp.getWidth();
            int trunH = turnBmp.getHeight();
            Matrix matrix = new Matrix();
            // 计算缩放率，新尺寸除原始尺寸
            float scaleWidth = ((float) newWidth) / trunW;
            float scaleHeight = ((float) newHeight) / trunH;
            matrix.postScale(scaleWidth, scaleHeight);
            finallBitmap = Bitmap.createBitmap(turnBmp, 0, 0, trunW, trunH,
                    matrix, true);
        } else {// 截剪
            // "不直接压缩");
            int widleft = (srcW - newWidth) / 2;
            int Heighteft = (srcH - newHeight) / 2;
            finallBitmap = Bitmap.createBitmap(srcBitmap, widleft, Heighteft, newWidth, newHeight);
        }
        File file = new File(StorageUtil.getInner() + "/LuPingDaShi/tmp");
        File file2 = new File(filePath);
        // 截取视频文件后缀名前的字符串
        final String[] fileName = file2.getName().split("\\.mp4");
        if (file.exists() == false) {
            file.mkdirs();
            file = null;
        }

        File f = new File(StorageUtil.getInner() + "/LuPingDaShi/tmp/" + fileName[0] + ".png");
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finallBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f.getPath();
    }

}
