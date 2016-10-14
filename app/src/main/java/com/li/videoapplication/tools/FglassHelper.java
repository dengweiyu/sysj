package com.li.videoapplication.tools;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.li.videoapplication.data.network.LightTask;
import com.li.videoapplication.framework.AppManager;

/**
 * 功能：图片高斯模糊
 */
public class FglassHelper {

    public static final String TAG = FglassHelper.class.getSimpleName();

    /**
     * 设置高斯模糊 设置高斯模糊是依靠scaleFactor和radius配合使用的，比如这里默认设置是：scaleFactor = 8;radius= 2; 模糊效果和scaleFactor = 1;radius = 20;是一样的，而且效率高
     *
     * @param fromView    从某个View获取截图
     * @param toView      高斯模糊设置到某个View上
     * @param radius      模糊度
     * @param scaleFactor 缩放比例
     */
    @SuppressWarnings("deprecation")
    public static void blur(ImageView fromView, ImageView toView, float radius, float scaleFactor) {

        // 获取View的截图
        fromView.buildDrawingCache();
        Bitmap bkg = fromView.getDrawingCache();

        if (radius < 1 || radius > 26) {
            scaleFactor = 8;
            radius = 2;
        }

        Bitmap overlay = Bitmap.createBitmap((int) (toView.getMeasuredWidth() / scaleFactor), (int) (toView.getMeasuredHeight() / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-50, -50);
        canvas.scale(scaleFactor, scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = FglassHelper.doBlur(overlay, (int) radius, true);
        toView.setBackgroundDrawable(new BitmapDrawable(overlay));
        toView.setScaleType(ImageView.ScaleType.CENTER_CROP);

    }

    /**
     * 新开线程设置高斯模糊
     */
    @SuppressWarnings("deprecation")
    public static void blur(final ImageView fromView, final ImageView toView) {
        if (fromView == null || toView == null) {
            throw new NullPointerException();
        }
        // 获取fromView的截图
        fromView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        fromView.buildDrawingCache();
        final Bitmap bitmap = fromView.getDrawingCache();

        LightTask.postAtFrontOfQueue(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                if (bitmap != null) {
                    // 按toView比例截取Bitmap
                    int w = bitmap.getWidth();
                    int h = bitmap.getHeight();
                    int viewWidth = toView.getWidth();
                    int viewHeight = toView.getHeight();
                    int height = (viewHeight * w) / viewWidth;
                    int startY = (h - height) / 2;
                    int endY = startY + height;
                    Log.d(TAG, "run: w=" + w);
                    Log.d(TAG, "run: h=" + h);
                    Log.d(TAG, "run: height=" + height);
                    Log.d(TAG, "run: startY=" + startY);
                    Log.d(TAG, "run: endY=" + endY);
                    final Bitmap bmp = Bitmap.createBitmap(bitmap, 0, startY, w, height);
                    final Bitmap b;
                    // Android原生API高斯模糊
                    if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        b = blur(bmp);
                    } else {
                        b = doBlur(bmp, 14, true);
                    }
                    toView.post(new Runnable() {

                        @Override
                        public void run() {
                            Drawable drawable = new BitmapDrawable(b);
                            toView.setBackgroundDrawable(drawable);
                            toView.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                    });
                }
            }
        });
    }

    /**
     * 高斯模糊操作
     */
    public static Bitmap doBlur(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {

        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

    /**
     * Android原生API高斯模糊
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static final Bitmap blur(Bitmap bitmap) {
        // 用需要创建高斯模糊bitmap创建一个空的bitmap
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        // 初始化Renderscript，这个类提供了RenderScript
        // context，在创建其他RS类之前必须要先创建这个类，他控制RenderScript的初始化，资源管理，释放
        RenderScript rs = RenderScript.create(AppManager.getInstance().getApplication());
        // 创建高斯模糊对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        // 创建Allocations，此类是将数据传递给RenderScript内核的主要方法，并制定一个后备类型存储给定类型
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
        // 设定模糊度
        blurScript.setRadius(25.f);
        // Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);
        // Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);
        // recycle the original bitmap
        bitmap.recycle();
        // After finishing everything, we destroy the Renderscript.
        rs.destroy();
        return outBitmap;
    }
}
