package com.li.videoapplication.data.record;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.util.Log;
import android.view.View;

import com.li.videoapplication.data.local.FileOperateUtil;
import com.li.videoapplication.ui.toast.ToastHelper;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 拍照返回的byte数据处理类
 */
public final class DataHandler {

    public final static String TAG = CameraView.class.getSimpleName();

    /**
     * 压缩后的图片最大值 单位KB
     */
    private int maxSize = 200;

    private Context context;
    private CameraListener listener;

    public DataHandler(Context context, CameraListener listener) {
        this.listener = listener;
        this.context = context;
    }

    /**
     * 保存视频帧缩略图
     */
    public Bitmap saveVideoThumbnail(View view, String recordPath) {
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
                    if (listener != null)
                        listener.onRecorderFinish(bitmap, recordPath);
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
    private Bitmap getVideoThumbnail(View view, String recordPath) {
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
     * 保存图片
     *
     * @param data 相机返回的文件流
     * @return 解析流生成的缩略图
     */
    public Bitmap saveImage(byte[] data, Camera camera) {
        if (data != null) {
            if (listener != null)
                listener.onPictureTaken(data, camera);
            // 解析数据
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            // 添加水印
            // bitmap = getWaterMark(bitmap);
            //生成缩略图
            Bitmap thumBitmap = ThumbnailUtils.extractThumbnail(imageBitmap, 213, 213);
            if (listener != null) {
                listener.onPictureTaken(imageBitmap);
                listener.onMinPictureTaken(thumBitmap);
            }
            String imagePath = FileOperateUtil.getImagePath();
            String thumPath = FileOperateUtil.getImageThumPath(imagePath);
            File imageFile = new File(imagePath);
            File thumFile = new File(thumPath);
            try {
                //存图片大图
                FileOutputStream fos = new FileOutputStream(imageFile);
                ByteArrayOutputStream baos = compressImage(imageBitmap);
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
                //存图片小图
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(thumFile));
                thumBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
                bos.flush();
                bos.close();
                return imageBitmap;
            } catch (Exception e) {
                e.printStackTrace();
                ToastHelper.s("解析相片流失败");
            }
        } else {
            ToastHelper.s("拍照失败，请重试");
        }
        return null;
    }

    /**
     * 添加水印
     */
    private Bitmap getWaterMark(Bitmap bitmap) {
        Drawable mark = null;
        Bitmap wBitmap = drawableToBitmap(mark);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int ww = wBitmap.getWidth();
        int wh = wBitmap.getHeight();
        Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newb);
        //draw src into
        canvas.drawBitmap(bitmap, 0, 0, null);//在 0，0坐标开始画入src
        canvas.drawBitmap(wBitmap, w - ww + 5, h - wh + 5, null);//在src的右下角画入水印
        //save all clip
        canvas.save(Canvas.ALL_SAVE_FLAG);//保存
        //store
        canvas.restore();//存储
        bitmap.recycle();
        bitmap = null;
        wBitmap.recycle();
        wBitmap = null;
        return newb;

    }

    /**
     * 图片转换
     */
    public Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 图片压缩
     */
    public ByteArrayOutputStream compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 99;
        while (baos.toByteArray().length / 1024 > maxSize) { // 循环判断如果压缩后图片是否大于maxSize,大于继续压缩
            options -= 3;// 每次都减少10
            //压缩比小于0，不再压缩
            if (options < 0) {
                break;
            }
            Log.i(TAG, baos.toByteArray().length / 1024 + "");
            baos.reset();// 重置清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        }
        return baos;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
}
