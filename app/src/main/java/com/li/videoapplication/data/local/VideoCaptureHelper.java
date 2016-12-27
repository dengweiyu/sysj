package com.li.videoapplication.data.local;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.os.EnvironmentCompat;
import android.util.Log;

import com.li.videoapplication.data.EventManager;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.database.VideoCaptureManager;
import com.li.videoapplication.data.database.VideoCaptureResponseObject;
import com.li.videoapplication.data.network.LightTask;
import com.li.videoapplication.data.preferences.VideoPreferences;
import com.li.videoapplication.framework.AppManager;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * 功能：加载本地视频
 */
public class VideoCaptureHelper {

    protected final String tag = this.getClass().getSimpleName();
    protected final String action = this.getClass().getName();

    public static final int RESULT_CODE_LOADING = VideoCaptureResponseObject.RESULT_CODE_LOADING;
    public static final int RESULT_CODE_CHECKING = VideoCaptureResponseObject.RESULT_CODE_CHECKING;
    public static final int RESULT_CODE_IMPORTING = VideoCaptureResponseObject.RESULT_CODE_IMPORTING;

    private Context context;

    private List<VideoCaptureEntity> data;
    private int resultCode;
    private boolean result;
    private String msg;

    /**
     * 回调
     */
    private void postEvent(boolean result, int resultCode, String msg, List<VideoCaptureEntity> data) {
        EventManager.postVideoCaptureResponseObject(result, resultCode, msg, data);
    }

    /**
     * 消息处理
     */
    private Handler handler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message message) {
            postEvent(result, resultCode, msg, data);
            super.handleMessage(message);
        }
    };

    public VideoCaptureHelper() {
        super();

        data = new ArrayList<>();
        context = AppManager.getInstance().getContext();
        result = false;
    }

    /**
     * 新线程加载本地视频
     */
    public void loadVideoCaptures() {

        LightTask.postAtFrontOfQueue(new Runnable() {
            @Override
            public void run() {
                loadVideoCaptures(null);
                Log.d(tag, "run: data" + data);
                result = true;
                resultCode = RESULT_CODE_LOADING;
                msg = "加载本地视频完成";
                handler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * 新线程验证并加载本地视频
     */
    public void checkVideoCaptures() {

        LightTask.postAtFrontOfQueue(new Runnable() {
            @Override
            public void run() {
                checkVideoCaptures(null);
                loadVideoCaptures(null);
                Log.d(tag, "run: data" + data);
                result = true;
                resultCode = RESULT_CODE_CHECKING;
                msg = "验证并加载本地视频完成";
                handler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * 新线程导入外部视频
     */
    public void importVideoCaptures() {

        LightTask.postAtFrontOfQueue(new Runnable() {
            @Override
            public void run() {
                try {
                    importVideoCaptures(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d(tag, "run: data" + data);
                result = true;
                resultCode = RESULT_CODE_IMPORTING;
                msg = "导入外部视频完成";
                handler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * 新线程验证并加载本地视频(自定义条数)
     */
    public void checkAndLoadVideoCaptures(final int pageSize, final int pageIndex) {

        LightTask.postAtFrontOfQueue(new Runnable() {
            @Override
            public void run() {
                checkVideoCapturesLimit(pageSize, pageIndex);
                loadVideoCapturesLimit(pageSize, pageIndex);
                Log.d(tag, "run: data" + data);
                result = true;
                resultCode = RESULT_CODE_CHECKING;
                msg = "验证并加载本地视频完成";
                handler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * 验证本地视频(自定义条数)
     */
    private void checkVideoCapturesLimit(int pageSize, int pageIndex) {
        List<VideoCaptureEntity> list = VideoCaptureManager.find(pageSize, pageIndex);
        if (list != null) {
            Iterator<VideoCaptureEntity> t = list.iterator();
            while (t.hasNext()) {
                VideoCaptureEntity e = t.next();
                File file = new File(e.getVideo_path());
                if (!file.exists()) {// 如果本地文件不存在
                    // 移出列表
                    t.remove();
                    // 删除数据库记录
                    VideoCaptureManager.deleteByPath(e.getVideo_path());
                }
            }
        }
    }

    /**
     * 加载本地视频(自定义条数)
     */
    private void loadVideoCapturesLimit(int pageSize, int pageIndex) {
        List<VideoCaptureEntity> list = VideoCaptureManager.find(pageSize, pageIndex);
        if (list != null)
            data.addAll(list);
        // 对视频根据修改时间进行排序
        Comparators.sortVideoCapture(data);
    }

    /**
     * 加载本地视频
     */
    private void loadVideoCaptures(Object o) {
        List<VideoCaptureEntity> list = VideoCaptureManager.findAll();
        if (list != null)
            data.addAll(list);
        // 对视频根据修改时间进行排序
        Comparators.sortVideoCapture(data);
    }

    /**
     * 验证并加载本地视频
     */
    private void checkVideoCaptures(Object o) {
        List<VideoCaptureEntity> list = VideoCaptureManager.findAll();
        if (list != null) {
            Iterator<VideoCaptureEntity> t = list.iterator();
            while (t.hasNext()) {
                VideoCaptureEntity e = t.next();
                File file = new File(e.getVideo_path());
                if (!file.exists()) {// 如果本地文件不存在
                    // 移出列表
                    t.remove();
                    // 删除数据库记录
                    VideoCaptureManager.deleteByPath(e.getVideo_path());
                }
            }
        }
    }

    /**
     * 导入外部视频
     */
    private void importVideoCaptures(Object o) {
        // 获取视频文件
        List<VideoCaptureEntity> list = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        String[] projection = new String[]{MediaStore.Video.Media.DATA, MediaStore.Video.Media.DISPLAY_NAME};
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, projection, null, null, null);
        cursor.moveToFirst();
        int fileCount = cursor.getCount();
        String str;
        VideoCaptureEntity entity;
        for (int i = 0; i < fileCount; i++) {
            str = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
            if (!str.contains(Contants.SYSJ)) {
                String video_name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                video_name = video_name.split("\\.mp4")[0];
                String video_path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                // 文件是否存在
                File file = new File(video_path);
                if (!file.exists()) {
                    Log.d(tag, "importVideoCaptures: " + "本地文件不存在/path=" + video_path);
                } else {
                    entity = new VideoCaptureEntity();
                    entity.setVideo_name(video_name);
                    entity.setVideo_path(video_path);
                    list.add(entity);
                    Log.d(tag, "importVideoCaptures: " + "本地文件存在/path=" + entity.getVideo_path());
                }
            }
            cursor.moveToNext();
        }
        // 检查Preferences是否记录正确已导入的视频
        for (VideoCaptureEntity record : list) {
            boolean b = VideoPreferences.getInstance().getBoolean(record.getVideo_path(), false);
            VideoCaptureEntity e = VideoCaptureManager.findByPath(record.getVideo_path());
            if (b && e == null) {
                VideoPreferences.getInstance().putBoolean(record.getVideo_path(), false);
            }
        }
        data.addAll(list);
        cursor.close();
    }

    public static  List<VideoCaptureEntity> allVideoList = null;

    // FIXME: 2016/11/23 寻求一个扫描大量媒体文件的方法。
    public void importVideo211(){
        // 视频信息集合
        allVideoList = new ArrayList<>();
        getVideoFile(allVideoList,Environment.getExternalStorageDirectory());
    }

    // 获得视频文件
    private void getVideoFile(final List<VideoCaptureEntity> list, File file) {
        // 获得视频文件
        file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                // sdCard找到视频名称
                String name = file.getName();
                int i = name.indexOf('.');
                if (i != -1) {
                    name = name.substring(i);
                    if (name.equalsIgnoreCase(".mp4")) {
                        VideoCaptureEntity entity = new VideoCaptureEntity();
                        entity.setVideo_name(file.getName());
                        entity.setVideo_path(file.getAbsolutePath());
                        list.add(entity);
                        return true;
                    }
                } else if (file.isDirectory()) {
                    getVideoFile(list, file);
                }
                return false;
            }
        });
    }


    /**
     * 文件创建时间
     */
    public static String getLastModified(String filePath) {
        String path = filePath.toString();
        File f = new File(path);
        Calendar cal = Calendar.getInstance();
        long time = f.lastModified();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        cal.setTimeInMillis(time);
        return formatter.format(cal.getTime());
    }
}
