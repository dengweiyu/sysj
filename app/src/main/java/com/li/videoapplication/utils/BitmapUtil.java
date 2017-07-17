package com.li.videoapplication.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.li.videoapplication.framework.AppManager;

/**
 * 功能：图片工具
 */
public class BitmapUtil {

    public static Bitmap readBitmap(int resId) {
        Context context = AppManager.getInstance().getContext();
        return readBitmap(context, resId);
    }

    public static Bitmap readBitmap(File file) {
        Context context = AppManager.getInstance().getContext();
        return readBitmap(context, file);
    }

    /**
     * 功能：以最省内存的方式读取本地资源的图片
     */
    public static Bitmap readBitmap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);// 获取资源图片
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * 功能：以最省内存的方式读取本地资源的图片
     */
    public static Bitmap readBitmap(Context context, File file) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 10; // width，hight设为原来的1/10
        InputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (is == null)
            return null;
        return BitmapFactory.decodeStream(is, null, options);
    }

    /**
     * 功能：回收图片占用的内存
     */
    public static void recycleBitmap(ImageView imageView) {
        if (imageView != null && imageView.getDrawable() != null) {
            Bitmap oldBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            imageView.setImageDrawable(null);
            if (oldBitmap != null) {
                oldBitmap.recycle();// 回收图片所占的内存
                oldBitmap = null;
                System.gc();// 提醒系统及时回收
            }
        }
    }

    //-----------------------------------------------------------------

    /**
     * 保存Bitmap到本地（不压缩）
     */
    public static boolean saveBitmap(Bitmap bitmap, String path) {
        Context context = AppManager.getInstance().getContext();
        return saveBitmap(context, bitmap, path);
    }

    //-----------------------------------------------------------------

    /**
     * 保存Bitmap到本地（不压缩）
     */
    private static boolean saveBitmap(Context context, Bitmap bitmap, String path) {
        if (bitmap == null || path == null)
            return false;
        File file = null;
        try {
            file = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file != null && file.exists())
            try {
                boolean isDelete = file.getAbsoluteFile().delete();
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (fos == null)
            return false;
        boolean isSave = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        try {
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    //-----------------------------------------------------------------

    /**
     * 读取Res资源图片
     */
    public static Bitmap readResBitmap(int resId) {
        Context context = AppManager.getInstance().getContext();
        return readResBitmap(context, resId);
    }

    /**
     * 读取本地的图片（原图）
     */
    public static Bitmap readLocalBitmap(String filePath) {
        return readLocalBitmapInSampleSize( filePath, 1);
    }

    /**
     * 读取本地的图片（1/2）
     */
    public static Bitmap readLocalBitmapHalf(String filePath) {
        return readLocalBitmapInSampleSize( filePath, 2);
    }

    /**
     * 读取本地的图片（1/4）
     */
    public static Bitmap readLocalBitmapQuarter(String filePath) {
        return readLocalBitmapInSampleSize(filePath, 4);
    }

    //-----------------------------------------------------------------

    /**
     * 读取Res资源图片
     */
    private static Bitmap readResBitmap(Context context, int resId) {
        if (context == null)
            return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, options);
    }

    /**
     * 读取本地图片
     *
     * @param inSampleSize 1/inSampleSize 尺寸缩放 [1, ++)
     *
     */
    private static Bitmap readLocalBitmapInSampleSize(String filePath, int inSampleSize) {
        if (filePath == null)
            return null;
        File file = null;
        try {
            file = new File(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file == null || !file.exists())
            return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize; // 宽高设为原来的1/inSampleSize
        InputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (is == null)
            return null;
        return BitmapFactory.decodeStream(is, null, options);
    }

    /**
     * 回收图片占用的内存
     */
    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            System.gc();
        }
    }

    /**
     * 压缩图片
     * @param bitmap
     * @return
     */
    public static Bitmap imageCompressor(Bitmap bitmap) {
        double targetwidth = Math.sqrt(64.00 * 1000);
        if (bitmap.getWidth() > targetwidth || bitmap.getHeight() > targetwidth) {
            // 创建操作图片用的matrix对象
            Matrix matrix = new Matrix();
            // 计算宽高缩放率
            double x = Math.max(targetwidth / bitmap.getWidth(), targetwidth
                    / bitmap.getHeight());
            // 缩放图片动作
            matrix.postScale((float) x, (float) x);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
        }
        return bitmap;
    }

    /**
     * 缩小图片
     * @param bitmap 需要缩小的图片
     * @return 缩小的图片
     */
    public static Bitmap scaleBitmap(Bitmap bitmap,float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }


}
