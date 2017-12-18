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
import android.os.Build;
import android.renderscript.RSRuntimeException;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.content.res.AppCompatResources;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ifeimo.im.framwork.GlideApp;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.views.RoundedDrawable;

import java.io.File;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imageloader.core.display.CircleBitmapDisplayer;
import io.rong.imlib.filetransfer.RequestOption;
import jp.wasabeef.glide.transformations.internal.FastBlur;
import jp.wasabeef.glide.transformations.internal.RSBlur;

import static com.bumptech.glide.load.engine.executor.GlideExecutor.newDiskCacheExecutor;
import static com.bumptech.glide.load.engine.executor.GlideExecutor.newSourceExecutor;

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
        if (context == null) {
            return;
        }
        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed()) {
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
        if (context == null) {
            return;
        }
        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed()) {
                return;
            }
        }
        Glide.with(context)
                .asBitmap()//强制 Glide 去返回一个 Bitmap 对象
                .load(uri)
                .apply(new RequestOptions().placeholder(Color.TRANSPARENT))
                .into(target);
    }

    /**
     * 加载图片，空白占位符
     */
    public static void displayImageWhite(Context context, String uri, ImageView view) {
        Log.d(TAG, "imageUrl=" + uri);
        if (context == null) {
            return;
        }
        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed()) {
                return;
            }
        }
        Glide.with(context)
                .load(uri)
                .apply(new RequestOptions().placeholder(Color.TRANSPARENT))
                .into(view);
    }

    /**
     * 加载gif图片，空白占位符
     */
    public static void displayGIF(Context context, String gifUrl, ImageView view) {
        Log.d(TAG, "gifUrl =" + gifUrl);
        if (context == null) {
            return;
        }
        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed()) {
                return;
            }
        }
        Glide.with(context)
                .asGif()//如果这个gifUrl不是一个Gif，当成失败处理
                .load(gifUrl)
                .apply(new RequestOptions().placeholder(Color.TRANSPARENT))
                .into(view);
    }

    /**
     * 加载图片，空白占位符，模糊处理
     */
    public static void displayImageBlur(Context context, String url, ImageView view) {
        Log.d(TAG, "imageUrl =" + url);
        if (context == null) {
            return;
        }
        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed()) {
                return;
            }
        }
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions().placeholder(Color.TRANSPARENT).transform(new MyBlurTransformation(context)))
                .into(view);
    }

    /**
     * 加载图片，空白占位符，模糊处理
     */
    public static void displayImageBlur(Context context, byte[] data, ImageView view, int radius) {
        if (context == null) {
            return;
        }
        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed()) {
                return;
            }
        }
        Glide.with(context)
                .load(data)
                .apply(new RequestOptions().error(Color.TRANSPARENT).transform(new MyBlurTransformation(context, radius)))
                .into(view);
    }


    /**
     * 加载图片，空白占位符，模糊处理
     */
    public static void displayImageBlur(Context context, String url, ImageView view, int radius) {
        if (context == null) {
            return;
        }
        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed()) {
                return;
            }
        }
        Glide.with(context)
                .load(url)
                .apply(RequestOptions.errorOf(Color.TRANSPARENT).transform(new MyBlurTransformation(context, radius)))
                .into(view);
    }


    /**
     * 加载图片，sysj默认占位符1s渐变消失
     */
    public static void displayImageFade(String uri, ImageView view) {
        Log.d(TAG, "imageUrl=" + uri);

        Glide.with(AppManager.getInstance().getContext())// FIXME: 全局上下文的GC问题
                .asBitmap()
                .load(uri)
                .apply(new RequestOptions().skipMemoryCache(true).placeholder(R.drawable.default_video_211).dontAnimate())
//                .transition(DrawableTransitionOptions.withCrossFade(1000))
                .into(view);
    }

    /**
     * 加载图片，sysj默认占位符1s渐变消失
     */
    public static void displayImageFade(Context context, String uri, ImageView view) {
        Log.d(TAG, "imageUrl=" + uri);

        Glide.with(context)
                .asBitmap()
                .load(uri)
                .apply(new RequestOptions().skipMemoryCache(true).placeholder(R.drawable.default_video_211).dontAnimate())
//                .transition(BitmapTransitionOptions.withCrossFade(1000))
                .into(view);
    }

    /**
     * 加载图片，sysj默认占位符1s渐变消失
     */
    public static void displayImageFade(Fragment fragment, String uri, ImageView view) {
        Log.d(TAG, "imageUrl=" + uri);

        Glide.with(fragment)
                .asBitmap()
                .load(uri)
                .apply(new RequestOptions().skipMemoryCache(false).placeholder(R.drawable.default_video_211).dontAnimate())
//                .transition(BitmapTransitionOptions.withCrossFade(1000))
                .into(view);
    }

    /**
     * 加载图片，sysj默认占位符1s渐变消失
     */
    public static void displayCircleImageFade(Context context, String uri, ImageView view) {
        Log.d(TAG, "imageUrl=" + uri);

        RequestOptions requestOptions = new RequestOptions()
                .skipMemoryCache(false)
//                .circleCrop()
                .override(ScreenUtil.dp2px(45), ScreenUtil.dp2px(45))
                .placeholder(R.drawable.avatar_default)
                .transform(new GlideCircleTransform(2, ContextCompat.getColor(context, R.color.white)))
                .dontAnimate();

        Glide.with(context)
                .asBitmap()
                .load(uri)
                .apply(requestOptions)
//                .transition(BitmapTransitionOptions.withCrossFade(1000))
                .into(view);
    }

    /**
     * 加载图片，sysj默认占位符1s渐变消失
     */
    public static void displayCircleImageFade(Fragment fragment, String uri, ImageView view) {
        Log.d(TAG, "imageUrl=" + uri);

        RequestOptions requestOptions = new RequestOptions()
                .skipMemoryCache(false)
//                .circleCrop()
                .override(ScreenUtil.dp2px(45), ScreenUtil.dp2px(45))
                .dontAnimate()
                .transform(new GlideCircleTransform(2, ContextCompat.getColor(fragment.getContext(), R.color.white)))
                .placeholder(R.drawable.avatar_default);

        Glide.with(fragment)
//                .asBitmap()
                .load(uri)
                .apply(requestOptions)
//                .transition(BitmapTransitionOptions.withCrossFade(1000))
                .into(view);
    }


    /**
     * 加载图片，sysj默认占位符
     */
    public static void displayImage(Context context, String uri, ImageView view) {
        Log.d(TAG, "imageUrl=" + uri);
        if (context == null) {
            return;
        }
        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed()) {
                return;
            }
        }

        GlideApp.with(context)
                .load(uri)
                .apply(new RequestOptions().placeholder(R.drawable.default_video_211).centerCrop())
                .into(view);
    }


    /**
     * 显示本地视频封面，sysj默认占位符
     */
    public static void displayVideo(Context context, String filePath, ImageView view) {
        Log.d(TAG, "filePath =" + filePath);
        if (context == null) {
            return;
        }

        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed()) {
                return;
            }
        }

        Glide.with(context)
                .load(Uri.fromFile(new File(filePath)))
                .apply(new RequestOptions().placeholder(R.drawable.default_video_211))
                .into(view);
    }

    /**
     * 显示drawable目录下图，无渐变效果
     */
    public static void displayImage(Context context, int resId, ImageView view) {
        if (context == null) {
            return;
        }

        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed()) {
                return;
            }
        }
        Glide.with(context)
                .load(resId)
                .apply(RequestOptions.noAnimation())
                .into(view);
    }


    /**
     */
    public static void displayImageNoFade(Context context, String url, ImageView view) {
        if (context == null) {
            return;
        }

        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed()) {
                return;
            }
        }
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions().placeholder(R.drawable.default_video_211).dontAnimate())
                .into(view);
    }


    /**
     * 加载图片，自定义占位符
     */
    public static void displayImage(Context context, int placeHolder, String uri, ImageView view) {

        if (context == null) {
            return;
        }
        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed()) {
                return;
            }
        }

        Glide.with(context)
                .load(uri)
                .apply(RequestOptions.fitCenterTransform())
                .apply(RequestOptions.noAnimation())
                .apply(new RequestOptions().placeholder(placeHolder))
                .into(view);
    }


    /**
     * 加载图片，自定义占位符
     */
    public static void displayImageByDrawable(final Context context, int placeHolder, String uri, final ImageView view, SimpleTarget<Drawable> target) {

        if (context == null) {
            return;
        }
        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed()) {
                return;
            }
        }

        Glide.with(context)
                .load(uri)
                .apply(new RequestOptions().placeholder(placeHolder).dontAnimate())
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
        private static final String ID = "com.li.videoapplication.glide.glidecircletransform";
        private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

        private Paint mBorderPaint;
        private float mBorderWidth;

        public GlideCircleTransform(int borderWidth, int borderColor) {
            mBorderWidth = Resources.getSystem().getDisplayMetrics().density * borderWidth;

            mBorderPaint = new Paint();
            mBorderPaint.setDither(true);
            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setColor(borderColor);
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setStrokeWidth(mBorderWidth);
        }

        @Override
        protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof GlideCircleTransform;
        }

        @Override
        public int hashCode() {
            return ID_BYTES.hashCode();
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {

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

    }


    public static class MyBlurTransformation extends BitmapTransformation {

        private static final String ID = "gift.witch.glide.MyBlurTransformation";
        private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

        private static int MAX_RADIUS = 25;
        private static int DEFAULT_DOWN_SAMPLING = 1;

        private int mRadius;
        private int mSampling;
        private Context mContext;

        public MyBlurTransformation(Context context) {
            init(context, MAX_RADIUS, DEFAULT_DOWN_SAMPLING);
        }

        public MyBlurTransformation(Context context, BitmapPool pool) {
            init(context, MAX_RADIUS, DEFAULT_DOWN_SAMPLING);
        }

        public MyBlurTransformation(Context context, BitmapPool pool, int radius) {
            init(context, radius, DEFAULT_DOWN_SAMPLING);
        }

        public MyBlurTransformation(Context context, int radius) {
            init(context, radius, DEFAULT_DOWN_SAMPLING);
        }

        public MyBlurTransformation(Context context, int radius, int sampling) {
            init(context, radius, sampling);
        }

        private void init(Context context, int radius, int sampling) {
            mContext = context.getApplicationContext();
            mRadius = radius;
            mSampling = sampling;
        }

        @Override
        protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {

            Bitmap source = getAlphaSafeBitmap(pool, toTransform);

            int width = source.getWidth();
            int height = source.getHeight();
            int scaledWidth = width / mSampling;
            int scaledHeight = height / mSampling;

            Bitmap bitmap = pool.get(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(bitmap);
            canvas.scale(1 / (float) mSampling, 1 / (float) mSampling);
            Paint paint = new Paint();
            paint.setFlags(Paint.FILTER_BITMAP_FLAG);
            canvas.drawBitmap(source, 0, 0, paint);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                try {
                    bitmap = RSBlur.blur(mContext, bitmap, mRadius);
                } catch (RSRuntimeException e) {
                    bitmap = FastBlur.blur(bitmap, mRadius, true);
                }
            } else {
                bitmap = FastBlur.blur(bitmap, mRadius, true);
            }

            if (!source.equals(toTransform)) {
                pool.put(source);
            }

            return bitmap;
        }

        private Bitmap getAlphaSafeBitmap(@NonNull BitmapPool pool,
                                          @NonNull Bitmap maybeAlphaSafe) {
            if (Bitmap.Config.ARGB_8888.equals(maybeAlphaSafe.getConfig())) {
                return maybeAlphaSafe;
            }

            Bitmap argbBitmap = pool.get(maybeAlphaSafe.getWidth(), maybeAlphaSafe.getHeight(),
                    Bitmap.Config.ARGB_8888);
            new Canvas(argbBitmap).drawBitmap(maybeAlphaSafe, 0 /*left*/, 0 /*top*/, null /*pain*/);

            // We now own this Bitmap. It's our responsibility to replace it in the pool outside this method
            // when we're finished with it.
            return argbBitmap;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof MyBlurTransformation;
        }

        @Override
        public int hashCode() {
            return ID.hashCode();
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {
            messageDigest.update(ID_BYTES);
            byte[] radiusData = ByteBuffer.allocate(4).putInt(mRadius).array();
            messageDigest.update(radiusData);
        }
    }

}

