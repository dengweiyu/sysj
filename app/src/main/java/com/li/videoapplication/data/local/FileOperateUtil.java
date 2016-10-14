package com.li.videoapplication.data.local;

import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 文件操作工具类
 */
public class FileOperateUtil {

    public final static String TAG = FileOperateUtil.class.getSimpleName();

    public static final String FLODER_FILE = "file";
    public static final String FLODER_IMAGE = "image";
    public static final String FLODER_THUMBNAIL = "thumbnail";
    public static final String FLODER_VIDEO = "video";

    public final static int TYPE_IMAGE = 1;//图片
    public final static int TYPE_THUMBNAIL = 2;//缩略图
    public final static int TYPE_VIDEO = 3;//视频

    /**
     * 获取文件夹路径
     *
     * @param type     文件夹类别
     * @return /sysj/file/xxxx/
     */
    public static String getFolderPath(int type) {
        StringBuilder sb = new StringBuilder();
        File file = SYSJStorageUtil.getSysj();
        if (file == null || !file.exists())
            file = SYSJStorageUtil.getInnerSysj();
        if (file == null || !file.exists())
            file = SYSJStorageUtil.getDefaultSysj();
        sb.append(file.getAbsolutePath());
        sb.append(File.separator);
        sb.append(FLODER_FILE);
        sb.append(File.separator);
        switch (type) {
            case TYPE_IMAGE:
                sb.append(FLODER_IMAGE);
                break;
            case TYPE_VIDEO:
                sb.append(FLODER_VIDEO);
                break;
            case TYPE_THUMBNAIL:
                sb.append(FLODER_THUMBNAIL);
                break;
        }
        String path = sb.toString();
        File folder = null;
        try {
            folder = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (folder != null)
            if (!folder.exists())
                folder.mkdirs();
        Log.i(TAG, "path=" + path);
        return path;
    }

    /**
     * 获取图片文件夹
     *
     * @return /sysj/file/image/
     */
    public static String getImageFolder() {
        return FileOperateUtil.getFolderPath(FileOperateUtil.TYPE_IMAGE);
    }

    /**
     * 获取缩略图文件夹
     *
     * @return /sysj/file/thumbnail/
     */
    public static String getThumbnailFolder() {
        return FileOperateUtil.getFolderPath(FileOperateUtil.TYPE_THUMBNAIL);
    }

    /**
     * 获取拍摄视频文件夹
     *
     * @return /sysj/file/video/
     */
    public static String getVideoFolder() {
        return FileOperateUtil.getFolderPath(FileOperateUtil.TYPE_VIDEO);
    }

    /**
     * 获取拍摄视频
     *
     * @return /sysj/file/video/video_xxxxxxxxxxxxx.mp4
     */
    public static String getVideoPath() {
        String path = FileOperateUtil.getVideoFolder();
        String name = "video_" + FileOperateUtil.createFileName(".mp4");
        return path + File.separator + name;
    }

    /**
     * 获取图片
     *
     * @return /sysj/file/image/image_xxxxxxxxxxxxx.jpg
     */
    public static String getImagePath() {
        String path = FileOperateUtil.getImageFolder();
        String name = "image_" + FileOperateUtil.createFileName(".jpg");
        return path + File.separator + name;
    }

    /**
     * 获取图片缩略图
     *
     * @return /sysj/file/thumbnail/image_xxxxxxxxxxxxx.jpg
     */
    public static String getImageThumPath(String path) {
        String floder = FileOperateUtil.getThumbnailFolder();
        String name = new File(path).getName();
        String n = name.replaceAll("jpg", "jpg");
        return floder + File.separator + n;
    }

    /**
     * 获取拍摄视频缩略图
     *
     * @return /sysj/file/thumbnail/video_xxxxxxxxxxxxx.jpg
     */
    public static String getVideoThumPath(String path) {
        String floder = FileOperateUtil.getThumbnailFolder();
        String name = new File(path).getName();
        String n = name.replaceAll("mp4", "jpg");
        return floder + File.separator + n;
    }

    /**
     * 获取目标文件夹内指定后缀名的文件数组,按照修改日期排序
     *
     * @param file      目标文件夹路径
     * @param format    指定后缀名
     * @param content   包含的内容,用以查找视频缩略图
     */
    public static List<File> listFiles(String file, final String format, String content) {
        return listFiles(new File(file), format, content);
    }

    public static List<File> listFiles(String file, final String format) {
        return listFiles(new File(file), format, null);
    }

    /**
     * 获取目标文件夹内指定后缀名的文件数组,按照修改日期排序
     *
     * @param file      目标文件夹
     * @param extension 指定后缀名
     * @param content   包含的内容,用以查找视频缩略图
     */
    public static List<File> listFiles(File file, final String extension, final String content) {
        File[] files;
        if (file == null || !file.exists() || !file.isDirectory())
            return null;
        files = file.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File arg0, String arg1) {
                if (content == null || content.equals(""))
                    return arg1.endsWith(extension);
                else {
                    return arg1.contains(content) && arg1.endsWith(extension);
                }
            }
        });
        if (files != null) {
            List<File> list = new ArrayList<>(Arrays.asList(files));
            sortList(list, false);
            return list;
        }
        return null;
    }

    /**
     * 根据修改时间为文件列表排序
     *
     * @param list 排序的文件列表
     * @param asc  是否升序排序 true为升序 false为降序
     */
    public static void sortList(List<File> list, final boolean asc) {
        //按修改日期排序
        Collections.sort(list, new Comparator<File>() {
            public int compare(File file, File newFile) {
                if (file.lastModified() > newFile.lastModified()) {
                    if (asc) {
                        return 1;
                    } else {
                        return -1;
                    }
                } else if (file.lastModified() == newFile.lastModified()) {
                    return 0;
                } else {
                    if (asc) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            }
        });
    }

    /**
     * @param extension 后缀名 如".jpg",".mp4"
     */
    public static String createFileName(String extension) {
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        // 转换为字符串
        String fileName = format.format(new Date());
        Log.i(TAG, "fileName=" + fileName);
        //查看是否带"."
        if (!extension.startsWith("."))
            extension = "." + extension;
        fileName = fileName + extension;
        Log.i(TAG, "fileName=" + fileName);
        return fileName;
    }

    /**
     * 删除缩略图 同时删除源图或源视频
     *
     * @param thumbPath 缩略图路径
     */
    public static boolean deleteThumbFile(String thumbPath) {
        boolean flag = false;

        File file = new File(thumbPath);
        if (!file.exists()) { // 文件不存在直接返回
            return flag;
        }

        flag = file.delete();
        //源文件路径
        String sourcePath = thumbPath.replace(FLODER_THUMBNAIL, FLODER_IMAGE);
        file = new File(sourcePath);
        if (!file.exists()) { // 文件不存在直接返回
            return flag;
        }
        flag = file.delete();
        return flag;
    }

    /**
     * 删除源图或源视频 同时删除缩略图
     *
     * @param sourcePath 缩略图路径
     */
    public static boolean deleteSourceFile(String sourcePath) {
        boolean flag = false;

        File file = new File(sourcePath);
        if (!file.exists()) { // 文件不存在直接返回
            return flag;
        }

        flag = file.delete();
        //缩略图文件路径
        String thumbPath = sourcePath.replace(FLODER_IMAGE, FLODER_THUMBNAIL);
        file = new File(thumbPath);
        if (!file.exists()) { // 文件不存在直接返回
            return flag;
        }
        flag = file.delete();
        return flag;
    }


}