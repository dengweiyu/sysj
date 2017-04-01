package com.li.videoapplication.data.image;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;

import java.io.File;
import java.lang.ref.WeakReference;

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
        Glide.with(context)
                .load(url)
                .transform(new BlurTransformation(context))
                .placeholder(Color.TRANSPARENT)
                .error(Color.TRANSPARENT)
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
     * 加载图片，sysj默认占位符
     */
    public static void displayImage(Context context, String uri, ImageView view) {
        Log.d(TAG, "imageUrl=" + uri);
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
        Glide.with(context)
                .load(resId)
                .dontAnimate()
                .into(view);
    }
}
