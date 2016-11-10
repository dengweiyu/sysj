package com.li.videoapplication.data.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.network.RequestExecutor;
import com.li.videoapplication.framework.AppManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 功能：ImageLoader加载图片封装
 */
public class ImageLoaderHelper {
	
	protected final static String TAG = ImageLoaderHelper.class.getSimpleName();

    /**
     * 加载图片配置
     */
    private static final ImageLoaderConfiguration CONFIGURATION = new ImageLoaderConfiguration.Builder(AppManager.getInstance().getContext())
            .taskExecutor(RequestExecutor.THREAD_POOL_EXECUTOR)
            .denyCacheImageMultipleSizesInMemory()
            .discCacheFileNameGenerator(new Md5FileNameGenerator())
            .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
            .memoryCacheSize(2 * 1024 * 1024)
            .memoryCacheSizePercentage(13)
            .diskCacheSize(50 * 1024 * 1024)
            .diskCacheFileCount(100)
            .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
            .writeDebugLogs()
            .build();

    /**
     * 显示图片的配置
     */
    private static final DisplayImageOptions OPTIONS_WHITE = new DisplayImageOptions.Builder()
            .showImageOnLoading(Constant.PICTRUE_DEFAULT_TRANSPARENT)
            .showImageOnFail(Constant.PICTRUE_DEFAULT_TRANSPARENT)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();

    /**
     * 显示图片的配置
     */
    private static final DisplayImageOptions OPTIONS_VIDEO = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.default_video_211)
            .showImageOnFail(R.drawable.default_video_211)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();

    /**
     * 显示图片的配置
     */
    private static final DisplayImageOptions OPTIONS_VIDEO_ALPHA = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.default_video_211)
            .showImageOnFail(R.drawable.default_video_211)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .displayer(new FadeInBitmapDisplayer(2000))//渐变
            .build();

    /**
     *
     */
    @SuppressWarnings("deprecation")
	public static void init(Context context) {
        ImageLoader.getInstance().init(CONFIGURATION);
    }
    
    /**
     * 加载图片，沒有占位符
     */
    public static void displayImageEmpty(String uri, ImageView view) {
    	Log.d(TAG, "imageUrl=" + uri);
    	ImageLoader.getInstance().displayImage(uri, view);
    }

    /**
     * 加载图片，空白占位符
     */
    public static void displayImageWhiteListener(String uri, ImageView view,ImageLoadingListener listener) {
        Log.d(TAG, "imageUrl=" + uri);
        ImageLoader.getInstance().displayImage(uri, view, OPTIONS_WHITE, listener);
    }

    /**
     * 加载图片，空白占位符
     */
    public static void displayImageWhite(String uri, ImageView view) {
        Log.d(TAG, "imageUrl=" + uri);
        ImageLoader.getInstance().displayImage(uri, view, OPTIONS_WHITE);
    }

    /**
     * 加载图片，空白占位符
     */
    public static void displayImageWithAlpha(String uri, ImageView view) {
        Log.d(TAG, "imageUrl=" + uri);
        ImageLoader.getInstance().displayImage(uri, view, OPTIONS_VIDEO_ALPHA);
    }

    /**
     * 加载SDCard图片，空白占位符
     */
    public static void displayImageWhite4Local(String uri, ImageView view) {
        Log.d(TAG, "imageUrl=" + uri);
        ImageLoader.getInstance().displayImage("file://" + uri, view, OPTIONS_WHITE);
    }

    /**
     * 加载Assets图片，空白占位符
     */
    public static void displayImageWhite4Assets(String uri, ImageView view) {
        Log.d(TAG, "imageUrl=" + uri);
        ImageLoader.getInstance().displayImage("assets://" + uri, view, OPTIONS_WHITE);
    }
    
    /**
     * 加载图片，占位符
     */
    public static void displayImageVideo(String uri, ImageView view) {
    	Log.d(TAG, "imageUrl=" + uri);
    	ImageLoader.getInstance().displayImage(uri, view, OPTIONS_VIDEO);
    }

    public static void clearDiskCache() {
        ImageLoader.getInstance().clearDiskCache();
    }

    public static void clearMemoryCache() {
        ImageLoader.getInstance().clearMemoryCache();
    }
    
	public static void destroy() {
        ImageLoader.getInstance().destroy();
    }

    public void loadImage(String uri, ImageLoadingListener listener) {
        Log.d(TAG, "imageUrl=" + uri);
        ImageLoader.getInstance().loadImage(uri, listener);
    }

    public void loadImage(String uri, ImageSize targetImageSize, ImageLoadingListener listener) {
        Log.d(TAG, "imageUrl=" + uri);
        ImageLoader.getInstance().loadImage(uri, targetImageSize, listener);
    }

    public void loadImage(String uri, DisplayImageOptions options, ImageLoadingListener listener) {
        Log.d(TAG, "imageUrl=" + uri);
        ImageLoader.getInstance().loadImage(uri, options, listener);
    }

    public void loadImage(String uri, ImageSize targetImageSize, DisplayImageOptions options, ImageLoadingListener listener) {
        Log.d(TAG, "imageUrl=" + uri);
        ImageLoader.getInstance().loadImage(uri, targetImageSize, options, listener, (ImageLoadingProgressListener) null);
    }

    public void loadImage(String uri, ImageSize targetImageSize, DisplayImageOptions options, ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {
        Log.d(TAG, "imageUrl=" + uri);
        ImageLoader.getInstance().loadImage(uri, targetImageSize, options, listener, progressListener);
    }

    public Bitmap loadImageSync(String uri) {
        Log.d(TAG, "imageUrl=" + uri);
        return ImageLoader.getInstance().loadImageSync(uri);
    }

    public Bitmap loadImageSync(String uri, DisplayImageOptions options) {
        Log.d(TAG, "imageUrl=" + uri);
        return ImageLoader.getInstance().loadImageSync(uri, options);
    }

    public Bitmap loadImageSync(String uri, ImageSize targetImageSize) {
        Log.d(TAG, "imageUrl=" + uri);
        return ImageLoader.getInstance().loadImageSync(uri, targetImageSize);
    }

    public Bitmap loadImageSync(String uri, ImageSize targetImageSize, DisplayImageOptions options) {
        Log.d(TAG, "imageUrl=" + uri);
        return ImageLoader.getInstance().loadImageSync(uri, targetImageSize, options);
    }
}
