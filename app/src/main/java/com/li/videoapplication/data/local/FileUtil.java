package com.li.videoapplication.data.local;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;

import com.li.videoapplication.framework.AppManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;

public class FileUtil {

    public final static String TAG = FileUtil.class.getSimpleName();

    /**
     * 重命名文件
     */
    public static boolean renameFile(String oldPath, String newPath) {
        Log.d(TAG, "renameFile/oldPath=" + oldPath);
        Log.d(TAG, "renameFile/newPath=" + newPath);
        File oldFile = new File(oldPath);
        File newFile = new File(newPath);
        if (newFile.exists())
            return false;
        if (!oldFile.exists())
            return false;
        boolean flag =  oldFile.renameTo(newFile);
        Log.d(TAG, "renameFile: flag=" + flag);
        return flag;
    }

    /**
     * 计算视频大小，返回字节
     */
	public static long getFileSize(File file) {
		try {
			FileInputStream inputStream = new FileInputStream(file);
			long size = inputStream.available();
			inputStream.close();
			return size;
		} catch (Exception e) {
			return 0;
		}
	}

    /**
     * 计算文件夹所占空间大小
     */
    public static long _getFileSize(File file) {
        if (file == null || !file.exists())
            return 0;
        long size = 0;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files)
                    size += _getFileSize(f);
                return size;
            } else {
                return size;
            }
        } else {
            size = file.length();
            return size;
        }
    }

    /**
     * 计算视频大小，返回字节
     */
    public static long getFileSize(String filePath) {
        File file = new File(filePath);
        long s = 0;
        if (file.exists()) {
            FileInputStream stream;
            try {
                stream = new FileInputStream(filePath);
                s = stream.available();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return s;
    }

    /**
     * 将文件大小（字节）转为M,K,B
     */
    public static String formatFileSize(long l) {
        DecimalFormat df = new DecimalFormat("#");
        String s;
        if (l < 1024) {
            s = df.format((double) l) + "B";
        } else if (l < 1048576) {
            s = df.format((double) l / 1024) + "K";
        } else if (l < 1073741824) {
            s = df.format((double) l / 1048576) + "M";
        } else {
            s = df.format((double) l / 1073741824) + "G";
        }
        return s;
    }

    /**
     * 复制文件
     */
    public static boolean copyFileToFile(String oldPath, String newPath) {
        Log.d(TAG, "copyFileToFile/oldPath=" + oldPath);
        Log.d(TAG, "copyFileToFile/newPath=" + newPath);
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            File newfile = new File(newPath);
            if (!newfile.exists()) {
                newfile.createNewFile();
            }
            if (oldfile.exists()) { // 文件存在时
                InputStream is = new FileInputStream(oldPath); // 读入原文件
                FileOutputStream fos = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = is.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    fos.write(buffer, 0, byteread);
                }
                is.close();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *  删除文件
     */
    public static boolean deleteFile(String path) {
        Log.d(TAG, "deleteFile/path=" + path);
        File file = new File(path);
        if (file != null && file.exists()) {
            try {
                return file.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static File createFile(String path) throws IOException {
        File file = new File(path);
        if (file.exists() && file.isFile()){
            return file;
        }else {
            file.createNewFile();
        }
        return file;
    }

    public static RandomAccessFile getRandomFile(String path) {
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(path,"rwd");
            return randomAccessFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 移动文件
     * @param srcPath 	源文件完整路径
     * @param destPath 	目的目录完整路径
     * @return 文件移动成功返回true，否则返回false
     */
    public static boolean moveFile(String srcPath, String destPath) {
        Log.d(TAG, "moveFile: srcPath=" + srcPath);
        Log.d(TAG, "moveFile: destPath=" + destPath);
        if (srcPath == null || destPath == null)
            return false;
        File srcFile = null;
        try {
            srcFile = new File(srcPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(srcFile == null ||
                !srcFile.exists() ||
                !srcFile.isFile())
            return false;
        File destFile = null;
        try {
            destFile = new File(destPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(destFile == null)
            return false;
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();
        boolean flag = srcFile.renameTo(new File(destPath));
        Log.d(TAG, "moveFile: flag=" + flag);
        return flag;
    }

    /**
     * 获得SD卡总大小
     */
    public static String getExternalStorageTotalSize() {
        Context context = AppManager.getInstance().getContext();
        File file = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(file.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(context, blockSize * totalBlocks);
    }

    /**
     * 获得SD卡剩余大小，即可用大小
     */
    public static String getExternalStorageAvailableSize() {
        Context context = AppManager.getInstance().getContext();
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(context, blockSize * availableBlocks);
    }

    /**
     * 获得机身内存总大小
     */
    public static String getDataTotalSize() {
        Context context = AppManager.getInstance().getContext();
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(context, blockSize * totalBlocks);
    }

    /**
     * 获得机身可用内存
     */
    public static String getDataAvailableSize() {
        Context context = AppManager.getInstance().getContext();
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(context, blockSize * availableBlocks);
    }

    /**
     * 获取文件后缀名
     *
     * @param name test.java/ TileTest.doc
     * @return java/ doc
     */
    public static String getExtName(String name) {
        Log.i(FileOperateUtil.TAG, "path=" + name);
        File f = null;
        try {
            f = new File(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (f == null)
            throw new IllegalArgumentException("file is null");
        if (f.isDirectory())
            throw new IllegalArgumentException("file is directoty");
        String fileName = f.getName();
        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
        Log.i(FileOperateUtil.TAG, "prefix=" + prefix);
        return prefix;
    }

    /**
     * 获取文件名
     *
     * @param name test.java/ TileTest.doc
     * @return test/ TileTest
     */
    public static String getFileName(String name) {
        String extName = getExtName(name);
        if (extName == null)
            return null;
        if (!extName.startsWith("."))
            extName = "." + extName;
        String fileName = name.replace(extName, "");
        return fileName;
    }

    /**
     * 是否是文件夹
     */
    public static boolean isFloder(String path) {
        Log.i(FileOperateUtil.TAG, "path=" + path);
        File file = null;
        try {
            file = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file != null && file.isDirectory())
            return true;
        return false;
    }

    /**
     * 是否是文件
     */
    public static boolean isFile(String path) {
        Log.i(FileOperateUtil.TAG, "path=" + path);
        File file = null;
        try {
            file = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file != null && file.isFile())
            return true;
        return false;
    }
}