package com.li.videoapplication.data.image;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.support.v7.content.res.AppCompatResources;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.views.RoundedDrawable;

import java.io.File;
import java.lang.ref.WeakReference;

import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imageloader.core.display.CircleBitmapDisplayer;
import io.rong.imlib.filetransfer.RequestOption;

/**
 * 功能：Glider加载图片封装
 */
public class GlideHelper {
    protected final static String TAG = GlideHelper.class.getSimpleName();

    /**
     * ImageView的资源回收问题
     * 问题描述：默认情况下，Glide会根据 with()使用的Activity或Fragment的生命周期自动调整资源请求以及资源回收。
     * 但是如果有很占内存的Fragment或Activity不销毁而仅仅是隐藏视图，那么这些图片资源就没办法及时回收，即使是GC的时候。
     * 解决办法：使用WeakReference
     */
    public static void displayImage4Weak(Context context, String uri, ImageView view) {
        final WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(view);
        ImageView target = imageViewWeakReference.get();
        if (target != null) {
            Glide.with(context)
                    .load(uri)
                    .into(target);
        }
    }

    /**
     * 加载图片，沒有占位符
     */
    public static void displayImageEmpty(Context context, String uri, ImageView view) {
        Log.d(TAG, "imageUrl=" + uri);
        if (context == null){
            return;
        }
        if(context instanceof Activity){
            if (((Activity)context).isDestroyed()){
                return;
            }
        }
        Glide.with(context)
                .load(uri)
                .into(view);
    }

    /**
     * 加载图片，空白占位符带目标
     * Glide 中的回调：Targets
     */
    public static void displayImageWhiteTargets(Context context, String uri,
                                                SimpleTarget<Bitmap> target) {
        Log.d(TAG, "imageUrl=" + uri);
        if (context == null){
            return;
        }
        if(context instanceof Activity){
            if (((Activity)context).isDestroyed()){
                return;
            }
        }
        Glide.with(context)
                .load(uri)
                .asBitmap()//强制 Glide 去返回一个 Bitmap 对象
                .placeholder(Color.TRANSPARENT)
                .error(Color.TRANSPARENT)
                .into(target);
    }

    /**
     * 加载图片，空白占位符
     */
    public static void displayImageWhite(Context context, String uri, ImageView view) {
        Log.d(TAG, "imageUrl=" + uri);
        if (context == null){
            return;
        }
        if(context instanceof Activity){
            if (((Activity)context).isDestroyed()){
                return;
            }
        }
        Glide.with(context)
                .load(uri)
                .placeholder(Color.TRANSPARENT)
                .error(Color.TRANSPARENT)
                .into(view);
    }

    /**
     * 加载gif图片，空白占位符
     */
    public static void displayGIF(Context context, String gifUrl, ImageView view) {
        Log.d(TAG, "gifUrl =" + gifUrl);
        if (context == null){
            return;
        }
        if(context instanceof Activity){
            if (((Activity)context).isDestroyed()){
                return;
            }
        }
        Glide.with(context)
                .load(gifUrl)
                .asGif()//如果这个gifUrl不是一个Gif，当成失败处理
                .placeholder(Color.TRANSPARENT)
                .error(Color.TRANSPARENT)
                .into(view);
    }

    /**
     * 加载图片，空白占位符，模糊处理
     */
    public static void displayImageBlur(Context context, String url, ImageView view) {
        Log.d(TAG, "imageUrl =" + url);
        if (context == null){
            return;
        }
        if(context instanceof Activity){
            if (((Activity)context).isDestroyed()){
                return;
            }
        }
        Glide.with(context)
                .load(url)
                .transform(new BlurTransformation(context))
                .placeholder(Color.TRANSPARENT)
                .error(Color.TRANSPARENT)
                .into(view);
    }

    /**
     * 加载图片，空白占位符，模糊处理
     */
    public static void displayImageBlur(Context context, byte[] data, ImageView view,int radius) {
        if (context == null){
            return;
        }
        if(context instanceof Activity){
            if (((Activity)context).isDestroyed()){
                return;
            }
        }
        Glide.with(context)
                .load(data)
                .error(Color.TRANSPARENT)
                .bitmapTransform(new jp.wasabeef.glide.transformations.BlurTransformation(context,radius))
                .into(view);
    }


    /**
     * 加载图片，空白占位符，模糊处理
     */
    public static void displayImageBlur(Context context, String url, ImageView view,int radius) {
        if (context == null){
            return;
        }
        if(context instanceof Activity){
            if (((Activity)context).isDestroyed()){
                return;
            }
        }
        Glide.with(context)
                .load(url)
                .error(Color.TRANSPARENT)
                .bitmapTransform(new jp.wasabeef.glide.transformations.BlurTransformation(context,radius))
                .into(view);
    }


    /**
     * 加载图片，sysj默认占位符1s渐变消失
     */
    public static void displayImageFade(String uri, ImageView view) {
        Log.d(TAG, "imageUrl=" + uri);

        Glide.with(AppManager.getInstance().getContext())// FIXME: 全局上下文的GC问题
                .load(uri)
                .placeholder(R.drawable.default_video_211)
                .error(R.drawable.default_video_211)
                .crossFade(1000)
                .into(view);
    }

    /**
     * 加载图片，sysj默认占位符1s渐变消失
     */
    public static void displayImageFade(Context context, String uri, ImageView view) {
        Log.d(TAG, "imageUrl=" + uri);

        Glide.with(context)
                .load(uri)
                .placeholder(R.drawable.default_video_211)
                .error(R.drawable.default_video_211)
                .crossFade(1000)
                .into(view);
    }

    /**
     * 加载图片，sysj默认占位符1s渐变消失
     */
    public static void displayCircleImageFade(Context context, String uri, ImageView view) {
        Log.d(TAG, "imageUrl=" + uri);

        Glide.with(context)
                .load(uri)
                .skipMemoryCache(false)
                .placeholder(new RoundedDrawable(R.drawable.default_video_211)) //R.drawable.default_video_211
                .transform(new GlideCircleTransform(AppManager.getInstance().getContext(), 2, AppManager.getInstance().getContext().getResources().getColor(R.color.white)))
                .error(R.drawable.default_video_211)
                .crossFade(1000)
                .into(view);
    }

    /**
     * 加载图片，sysj默认占位符
     */
    public static void displayImage(Context context, String uri, ImageView view) {
        Log.d(TAG, "imageUrl=" + uri);
        if (context == null){
            return;
        }
        if(context instanceof Activity){
            if (((Activity)context).isDestroyed()){
                return;
            }
        }

        Glide.with(context)
                .load(uri)
                .placeholder(R.drawable.default_video_211)
                .error(R.drawable.default_video_211)
                .centerCrop()
                .into(view);
    }




    /**
     * 显示本地视频封面，sysj默认占位符
     */
    public static void displayVideo(Context context, String filePath, ImageView view) {
        Log.d(TAG, "filePath =" + filePath);
        if (context == null){
            return;
        }

        if(context instanceof Activity){
            if (((Activity)context).isDestroyed()){
                return;
            }
        }

        Glide.with(context)
                .load(Uri.fromFile(new File(filePath)))
                .placeholder(R.drawable.default_video_211)
                .error(R.drawable.default_video_211)
                .into(view);
    }

    /**
     *显示drawable目录下图，无渐变效果
     */
    public static void displayImage(Context context,int resId,ImageView view){
        if (context == null){
            return;
        }

        if(context instanceof Activity){
            if (((Activity)context).isDestroyed()){
                return;
            }
        }
        Glide.with(context)
                .load(resId)
                .dontAnimate()
                .into(view);
    }


    /**
     */
    public static void displayImageNoFade(Context context,String url,ImageView view){
        if (context == null){
            return;
        }

        if(context instanceof Activity){
            if (((Activity)context).isDestroyed()){
                return;
            }
        }
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.default_video_211)
                .dontAnimate()
                .into(view);
    }


    /**
     * 加载图片，自定义占位符
     */
    public static void displayImage(Context context,int placeHolder,String uri, ImageView view) {

        if (context == null){
            return;
        }
        if(context instanceof Activity){
            if (((Activity)context).isDestroyed()){
                return;
            }
        }

        Glide.with(context)
                .load(uri)
                .placeholder(placeHolder)
                .dontAnimate()
                .fitCenter()
                .into(view);
    }


    /**
     * 加载图片，自定义占位符
     */
    public static void displayImageByDrawable(final Context context, int placeHolder, String uri, final ImageView view,SimpleTarget<GlideDrawable> target) {

        if (context == null){
            return;
        }
        if(context instanceof Activity){
            if (((Activity)context).isDestroyed()){
                return;
            }
        }

        Glide.with(context)
                .load(uri)
                .placeholder(placeHolder)
                .dontAnimate()
                .into(target /*new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        if (context == null){
                            return;
                        }
                    //    Drawable drawable = AppCompatResources.getDrawable(context,R.drawable.home_bottom_record_bg);
                        Drawable drawable = view.getDrawable();
                        StateListDrawable stateDrawable = null;
                        if (drawable instanceof StateListDrawable){
                            stateDrawable = (StateListDrawable)drawable;
                        }else {
                            stateDrawable = new StateListDrawable();
                        }
                        if (isPress){
                            stateDrawable.addState(new int[]{android.R.attr.state_pressed},resource);
                        }else {
                            stateDrawable.addState(new int[]{-android.R.attr.state_pressed},resource);
                        }
                        view.setImageDrawable(stateDrawable);
                    }
                }*/);
    }

    public static class GlideCircleTransform extends BitmapTransformation {

        private Paint mBorderPaint;
        private float mBorderWidth;

        public GlideCircleTransform(Context context) {
            super(context);
        }

        public GlideCircleTransform(Context context, int borderWidth, int borderColor) {
            super(context);
            mBorderWidth = Resources.getSystem().getDisplayMetrics().density * borderWidth;

            mBorderPaint = new Paint();
            mBorderPaint.setDither(true);
            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setColor(borderColor);
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setStrokeWidth(mBorderWidth);
        }


        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = (int) (Math.min(source.getWidth(), source.getHeight()) - (mBorderWidth / 2));
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            if (mBorderPaint != null) {
                float borderRadius = r - mBorderWidth / 2;
                canvas.drawCircle(r, r, borderRadius, mBorderPaint);
            }
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }
}

