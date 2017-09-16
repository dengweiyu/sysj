package com.li.videoapplication.data.upload;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.data.local.ScreenShotEntity;
import com.li.videoapplication.data.model.response.RetPhotoKeyAndToken208Entity;
import com.li.videoapplication.data.model.response.SavePhoto208Entity;
import com.li.videoapplication.data.qiniu.http.ResponseInfo;
import com.li.videoapplication.data.qiniu.storage.KeyGenerator;
import com.li.videoapplication.data.qiniu.storage.UpCancellationSignal;
import com.li.videoapplication.data.qiniu.storage.UpCompletionHandler;
import com.li.videoapplication.data.qiniu.storage.UpProgressHandler;
import com.li.videoapplication.data.qiniu.storage.UploadManager;
import com.li.videoapplication.data.qiniu.storage.UploadOptions;
import com.li.videoapplication.data.qiniu.storage.persistent.FileRecorder;
import com.li.videoapplication.data.qiniu.utils.UrlSafeBase64;
import com.li.videoapplication.utils.StringUtil;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.rong.eventbus.EventBus;


/**
 * 上传图片任务
 */
public class ImageShareTask208 {

    public static final int STATUS_START = Contants.STATUS_START;
    public static final int STATUS_PREPARING = Contants.STATUS_PREPARING;
    public static final int STATUS_UPLOADING = Contants.STATUS_UPLOADING;
    public static final int STATUS_COMPLETING = Contants.STATUS_COMPLETING;
    public static final int STATUS_END = Contants.STATUS_END;

    /**
     * 回调
     */
    interface Callback {

        void start(String msg);

        void preparing(String msg);

        void uploading(int several, double progress, String msg);

        void complete(String msg);

        void end(String msg);
    }

    private static final String TAG = ImageShareTask208.class.getSimpleName();
    private String name = this.getClass().getName();
    private String simpleName = this.getClass().getSimpleName();

    private ImageShareRequestObject208 request;

    private String match_id;
    private String member_id;
    private String title;
    private String description="";
    private List<ScreenShotEntity> files;

    private String token;
    private List<String> keys = new ArrayList<>();
    private List<String> newKeys = new ArrayList<>();

    private String threadName = "TASK-" + simpleName;
    private HandlerThread thread;
    private Looper looper;
    private Handler handler;

    private Callback callback;
    private String msg;
    private boolean result;
    private int several;
    private double progress;
    // 记录删除完成数量
    private int completeNumber;

    /**
     * 回调
     */
    public void publish(boolean result, int status, String msg) {
        ImageShareResponseObject object = new ImageShareResponseObject(result, status, msg);
        EventBus.getDefault().post(object);
    }

    /**
     * 消息处理
     */
    private Handler h = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message message) {
            int what = message.what;
            publish(result, what, msg);
            switch (what) {
                case STATUS_START:
                    Log.i(TAG, "h/what=" + what);
                    Log.i(TAG, "h/msg=" + msg);
                    if (callback != null) {
                        callback.start(msg);
                    }
                    break;

                case STATUS_PREPARING:
                    Log.i(TAG, "h/what=" + what);
                    Log.i(TAG, "h/msg=" + msg);
                    if (callback != null) {
                        callback.preparing(msg);
                    }
                    break;

                case STATUS_UPLOADING:
                    Log.i(TAG, "h/what=" + what);
                    Log.i(TAG, "h/msg=" + msg);
                    if (callback != null) {
                        callback.uploading(several, progress, msg);
                    }
                    break;

                case STATUS_COMPLETING:
                    Log.i(TAG, "h/what=" + what);
                    Log.i(TAG, "h/msg=" + msg);
                    if (callback != null) {
                        callback.complete(msg);
                    }
                    break;

                case STATUS_END:
                    Log.i(TAG, "h/what=" + what);
                    Log.i(TAG, "h/msg=" + msg);
                    if (callback != null) {
                        callback.end(msg);
                    }
                    destroy();
                    break;
            }
            super.handleMessage(message);
        }
    };

    private UploadManager uploadManager;


    public ImageShareTask208(ImageShareRequestObject208 request) {
        this(request, null);
    }

    public ImageShareTask208(ImageShareRequestObject208 request, Callback callback) {
        this.callback = callback;
        this.request = request;
        create();
    }

    /**
     * 初始化
     */
    private void create() {

        this.match_id = request.getMatch_id();
        this.member_id = request.getMember_id();
        this.title = request.getTitle();
        this.files = request.getData();

        if (!checkImages()) {
            h.obtainMessage(STATUS_END).sendToTarget();
            return;
        }
        if (StringUtil.isNull(member_id)) {
            msg = "请先登录";
            h.obtainMessage(STATUS_END).sendToTarget();
            return;
        }
        if (StringUtil.isNull(match_id)) {
            msg = "请选择类型";
            h.obtainMessage(STATUS_END).sendToTarget();
            return;
        }
        if (StringUtil.isNull(title)) {
            msg = "请填写标题";
            h.obtainMessage(STATUS_END).sendToTarget();
            return;
        }

        thread = new HandlerThread(threadName, android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        looper = thread.getLooper();
        handler = new Handler(looper);

        try {
            uploadManager = new UploadManager(new FileRecorder(SYSJStorageUtil.getSysjUploadimage().getPath()),
                    new KeyGenerator() {
                        @Override
                        public String gen(String key, File file) {
                            return UrlSafeBase64.encodeToString(file.getAbsolutePath());
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 销毁资源
     */
    private void destroy() {
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
    }

    /**
     * 上传
     */
    public void upload() {
        msg = "开始上传图片";
        h.obtainMessage(STATUS_START).sendToTarget();
        msg = "准备中";
        h.obtainMessage(STATUS_PREPARING).sendToTarget();

        handler.post(new Runnable() {
            @Override
            public void run() {

                preparing();
            }
        });
    }

    /**
     * 图片上传凭证
     */
    private void preparing() {

        // 图片上传凭证
        RetPhotoKeyAndToken208Entity entity =
                DataManager.UPLOAD.retPhotoKeyAndToken208(member_id, title, String.valueOf(files.size()));
        Log.i(TAG, "entity=" + entity);
        if (entity != null && entity.isResult()) {
            token = entity.getData().getToken();
            if (entity.getData() != null && entity.getData().getToken() != null) {
                keys.clear();
                keys.addAll(entity.getData().getKey());
            }
            if (token != null &&
                    keys != null &&
                    keys.size() > 0 &&
                    keys.size() == files.size()) {
                msg = "获取图片上传凭证成功";
                h.obtainMessage(STATUS_PREPARING).sendToTarget();

                msg = "上传中";
                h.obtainMessage(STATUS_UPLOADING).sendToTarget();
                for (int i = 0; i < keys.size(); ++i) {
                    Log.i(TAG, "i=" + i);
                    String k = keys.get(i);
                    String path = files.get(i).getPath();

                    qiniu(i + 1, token, k, path);
                }
            } else {
                msg = "获取图片上传凭证失败";
                h.obtainMessage(STATUS_END).sendToTarget();
            }
        } else {
            if (entity != null && !StringUtil.isNull(entity.getMsg()))
                msg = entity.getMsg();
            else msg = "获取图片上传凭证失败";
            h.obtainMessage(STATUS_END).sendToTarget();
        }
    }

    /**
     * 七牛上传
     */
    private void qiniu(final int number, final String token, final String key, final String path) {

        final File file = new File(path);
        UploadOptions options = new UploadOptions(null, null, false, new UpProgressHandler() {
            @Override
            public void progress(String key, double percent) {
                // 上传进度
                Log.i(TAG, "number=" + number + "/percent=" + percent);
                several = number;
                progress = percent;
            }
        }, new UpCancellationSignal() {

            @Override
            public boolean isCancelled() {
                return false;
            }
        });
        uploadManager.put(file, key, token, new UpCompletionHandler() {

            @Override
            public void complete(final String key, ResponseInfo respInfo, JSONObject jsonData) {
                ++completeNumber;
                Log.i(TAG, "respInfo=" + respInfo);
                if (respInfo.isOK()) {
                    // 上传完成
                    newKeys.add(key);
                    msg = "第" + number + "张图片上传完成";
                    h.obtainMessage(STATUS_UPLOADING).sendToTarget();
                } else {
                    msg = getErroeCode(respInfo.error);
                    h.obtainMessage(STATUS_UPLOADING).sendToTarget();
                }

                if (completeNumber == files.size() && newKeys.size() > 0) {
                    msg = "完成中";
                    h.obtainMessage(STATUS_COMPLETING).sendToTarget();

                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            completing();
                        }
                    });
                }
            }
        }, options);
    }

    private String getErroeCode(String code) {
        if (code.equals("cancelled by user")) {
            return "用户取消上传";
        } else if (code.equals("400")) {
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
    /**
     * 图片上传回调
     */
    private void completing() {
        // 图片上传回调
        SavePhoto208Entity entity = DataManager.UPLOAD.savePhoto208(member_id, match_id, newKeys, title, description);

        Log.i(TAG, "entity=" + entity);
        if (entity != null && entity.isResult()) {
            msg = "上传完成";
            result = true;
            h.obtainMessage(STATUS_END).sendToTarget();
        } else {
            if (entity != null && !StringUtil.isNull(entity.getMsg()))
                msg = entity.getMsg();
            else
                msg = "保存图片信息失败";
            h.obtainMessage(STATUS_END).sendToTarget();
        }
    }

    /**
     * 验证文件是否存在
     */
    private boolean checkImages() {
        for (Iterator<ScreenShotEntity> it = files.iterator(); it.hasNext(); ) {
            ScreenShotEntity value = it.next();

            String path = value.getPath();
            File file = null;
            try {
                file = new File(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!(file != null && file.exists() && file.isFile()))
                it.remove();
        }
        if (files.size() > 0) {
            return true;
        } else {
            msg = "上传的文件无效";
            return false;
        }
    }
}
