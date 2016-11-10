package com.li.videoapplication.tools;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.View;

import com.li.videoapplication.data.local.FileOperateUtil;

public class BitmapHelper {

    private static String TAG;

    /**
     * 保存视频帧缩略图
     */
    public static Bitmap saveVideoThumbnail(View view, String recordPath) {
        Bitmap bitmap = null;
        if (recordPath != null) {
            //创建缩略图,该方法只能获取384X512的缩略图，舍弃，使用源码中的获取缩略图方法
            // Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(recordPath, Thumbnails.MINI_KIND);
            bitmap = getVideoThumbnail(view, recordPath);
            if (bitmap != null) {
                String thumPath = FileOperateUtil.getVideoThumPath(recordPath);
                File thumFile = new File(thumPath);
                //存图片小图
                try {
                    FileOutputStream fos = new FileOutputStream(thumFile);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    /**
     * 获取视频帧缩略图，根据容器的高宽进行缩放
     */
    private static Bitmap getVideoThumbnail(View view, String recordPath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(recordPath);
            bitmap = retriever.getFrameAtTime(-1);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        if (bitmap == null)
            return null;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int pWidth = view.getWidth();// 容器宽度
        int pHeight = view.getHeight();//容器高度
        if (pWidth <= 0 || pHeight <= 0) {
            pWidth = 640;
            pHeight = 640;
        }
        //获取宽高跟容器宽高相比较小的倍数，以此为标准进行缩放
        float scale = Math.min((float) width / pWidth, (float) height / pHeight);
        int w = Math.round(scale * pWidth);
        int h = Math.round(scale * pHeight);
        bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
        return bitmap;
    }

    /**
     * 质量压缩方法
     */
    public static final Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 图片按比例大小压缩方法（根据路径获取图片并压缩）
     */
    public static final Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return BitmapHelper.compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 图片按比例大小压缩方法（根据Bitmap图片压缩）
     */
    public static final Bitmap comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return BitmapHelper.compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    public static Bitmap getCover(Bitmap srcBitmap) {
        int w = srcBitmap.getWidth();
        int h = srcBitmap.getHeight();

        if (w > h) {// 横屏
            return zoomImage(srcBitmap, 70.0);
        } else {// 竖屏
            int height = (9 * w) / 16;
            int startY = (h - height) / 2;
            int endY = startY + height;
            Log.d(TAG, "run: w=" + w);
            Log.d(TAG, "run: h=" + h);
            Log.d(TAG, "run: height=" + height);
            Log.d(TAG, "run: startY=" + startY);
            Log.d(TAG, "run: endY=" + endY);
            Bitmap targetBitmap = Bitmap.createBitmap(srcBitmap, 0, startY, w, height);
            return zoomImage(targetBitmap, 70.0);
        }
    }

    /**
     * 压缩图片
     */
    public static Bitmap zoomImage(Bitmap srcBitmap, double maxSize) {
        //图片允许最大空间   单位：KB
        // double maxSize = 400.00;
        //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        //将字节换成KB
        double mid = b.length / 1024;
        //判断bitmap占用空间是否大于允许最大空间  如果大于则压缩 小于则不压缩
        if (mid > maxSize) {
            //获取bitmap大小 是允许最大大小的多少倍
            double i = mid / maxSize;
            //开始压缩  此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
            srcBitmap = cropImage(srcBitmap,
                    srcBitmap.getWidth() / Math.sqrt(i),
                    srcBitmap.getHeight() / Math.sqrt(i));
        }
        return srcBitmap;
    }

    /**
     * 缩放图片
     */
    public static Bitmap cropImage(Bitmap srcBitmap, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = srcBitmap.getWidth();
        float height = srcBitmap.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(srcBitmap, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }
}
