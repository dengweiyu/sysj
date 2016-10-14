package com.li.videoapplication.data.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.utils.BitmapUtil;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 本地视频图片加载
 */
@SuppressLint("NewApi")
public class VideoCoverHelper {

	public static final String TAG = VideoCoverHelper.class.getSimpleName();

	/**
	 * 图片缓存
	 */
	private LruCache<String, Bitmap> lruCache;
	private ViewGroup viewGroup;
	private Context context;
	private Resources resources;
	private Set<ImageLoader> tasks = Collections.synchronizedSet(new HashSet<ImageLoader>());
	private Handler handler = new Handler(Looper.getMainLooper());

    private String[] filePaths;

    public void setFilePaths(String[] filePaths) {
        this.filePaths = filePaths;
    }

	public VideoCoverHelper(ViewGroup viewGroup, String[] filePaths) {
		super();
		this.viewGroup = viewGroup;
        this.filePaths = filePaths;

		context = AppManager.getInstance().getContext();
		resources = context.getResources();

		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		lruCache = new LruCache<String, Bitmap>(maxMemory / 4) {

			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getByteCount();
			}
		};
	}

	// ------------------------------------------------------------------

	/**
	 * 清除图片存缓存
	 */
	public void removeCache(String url) {
		if (url == null)
			return;
		if (lruCache != null) {
			lruCache.remove(url);
			Log.d(TAG, "removeCache: true");
		}
	}

	/**
	 * 清除图片存缓存
	 */
	public void removeCache(String[] urls) {
		if (urls == null || urls.length == 0)
			return;
		for (String url: urls) {
			removeCache(url);
		}
	}

	// ------------------------------------------------------------------

	/**
	 * 保存图片存缓存
	 */
	public void toCache(String url, Bitmap bitmap) {
		if (url == null)
			return;
		if (bitmap == null)
			return;
		if (fromCache(url) == null) {
			lruCache.put(url, bitmap);
			Log.d(TAG, "toCache: true");
		}
	}

	/**
	 * 从缓存上获取图片
	 */
	public Bitmap fromCache(String url) {
		if (url == null)
			return null;
		Log.d(TAG, "fromCache: true");
		return lruCache.get(url);
	}

	/**
	 * 保存图片存储卡上
	 */
	public void toStorage(String url, Bitmap bitmap) {
		File file = urlToFile(url);
		if (file != null) {
			BitmapUtil.saveBitmap(bitmap, file.getPath());
			Log.d(TAG, "toStorage: true");
		}
	}

	/**
	 * 从存储卡上获取图片
	 */
	public Bitmap fromStorage(String url) {
		File file = urlToFile(url);
		if (file == null)
			return null;
		Bitmap bitmap = BitmapUtil.readLocalBitmapQuarter(file.getPath());
		if (bitmap != null && bitmap.toString().length() > 3) {
			Log.d(TAG, "fromStorage: true");
			return bitmap;
		}
		return null;
	}

	private File urlToFile(String url) {
		if (url == null)
			return null;
		File file = SYSJStorageUtil.createCoverPath(url);
		if (file != null) {
			Log.d(TAG, "urlToFile: file=" + file);
			return file;
		}
		return null;
	}

	// ------------------------------------------------------------------

	/**
	 * 显示默认图片或者缓存中的图片
	 */
	public void displayImage(String url, ImageView imageView) {
		Log.d(TAG, "displayImage: // ---------------------------------------");
		Bitmap bitmap = fromCache(url);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			Log.d(TAG, "displayImage: 1");
		} else {
			imageView.setImageDrawable(Constant.PICTRUE_DEFAULT_VIDEO);
			Log.d(TAG, "displayImage: 2");
		}
	}

	/**
	 * 加载图片
	 *
	 * @param start 开始加载位置
	 * @param end 结束加载位置
	 */
	public void loadImage(int start, int end) {
		Log.d(TAG, "loadImage: // ---------------------------------------");
		Log.d(TAG, "loadImage: start=" + start);
		Log.d(TAG, "loadImage: end=" + end);
		for (int i = start; i < end; i++) {
			String url = null;
			try {
				url = filePaths[i];
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (url != null) {
				Bitmap bitmap = fromCache(url);
				if (bitmap != null) {
                    setImageBitmap(url, bitmap);
                    Log.d(TAG, "loadImage: 1");
                } else {
                    // setImageDrawable(url, Constant.PICTRUE_DEFAULT_VIDEO);// 有残留的图片
                    bitmap = fromStorage(url);
                    if (bitmap != null) {
                        animImageBitmap(url, bitmap);
                        toCache(url, bitmap);
                        Log.d(TAG, "loadImage: 2");
                    } else {
                        ImageLoader task = new ImageLoader(url);
                        ImageExecutor.execute(task);
                        tasks.add(task);
                        Log.d(TAG, "loadImage: 3");
                    }
                }
			}
		}
	}

	/**
	 * 显示默认图片或者加载内存的图片或缓存中的图片
	 */
	public void displayImage208(String url, ImageView imageView) {
		Log.d(TAG, "displayImage208: // ---------------------------------------");

		if (url != null) {
			Bitmap bitmap = fromCache(url);
			if (bitmap != null) {
				imageView.setImageBitmap(bitmap);
				Log.d(TAG, "loadImage208: 1");
			} else {
				bitmap = fromStorage(url);
				if (bitmap != null) {
					animImageBitmap(url, bitmap);
					toCache(url, bitmap);
					imageView.setImageBitmap(bitmap);
					Log.d(TAG, "loadImage208: 2");
				} else {
					ImageLoader task = new ImageLoader(url);
					ImageExecutor.execute(task);
					tasks.add(task);
					Log.d(TAG, "loadImage208: 3");
				}
			}
		}
	}

	private class ImageLoader extends Thread {

		private Bitmap bitmap;
		private String url;

		public ImageLoader(String url) {
			this.url = url;
		}

		@Override
		public void run() {
			Log.d(TAG, "ImageLoader: // ---------------------------------------");
			bitmap = VideoCover.generateBitmap(url);
			/*
			if (bitmap != null) {
				bitmap = VideoCover.transformBitmap(bitmap);
			}*/
			handler.post(new Runnable() {
				@Override
				public void run() {
					ImageView imageView = (ImageView) viewGroup.findViewWithTag(url);
					if (imageView != null) {
						Log.d(TAG, "ImageLoader: 1");
						if (bitmap != null) {
							imageView.startAnimation(getDismissAnimation(imageView, bitmap));
							Log.d(TAG, "ImageLoader: 2");
						} else {
							imageView.setImageDrawable(Constant.PICTRUE_DEFAULT_VIDEO);
							Log.d(TAG, "ImageLoader: 3");
						}
					}
				}
			});
			if (bitmap != null) {
				toCache(url, bitmap);
				toStorage(url, bitmap);
			}
			tasks.remove(this);
		}
	}

	private void setImageBitmap(String url, Bitmap bitmap) {
		ImageView imageView = (ImageView) viewGroup.findViewWithTag(url);
		if (imageView != null) {
			imageView.setImageBitmap(bitmap);
		}
	}

	private void setImageDrawable(String url, Drawable drawable) {
		ImageView imageView = (ImageView) viewGroup.findViewWithTag(url);
		if (imageView != null) {
			imageView.setImageDrawable(drawable);
		}
	}

	private void animImageBitmap(String url, Bitmap bitmap) {
		ImageView imageView = (ImageView) viewGroup.findViewWithTag(url);
		if (imageView != null) {
			imageView.startAnimation(getDismissAnimation(imageView, bitmap));
		}
	}

	/**
	 * 消失动画
	 */
	private Animation getDismissAnimation(final ImageView imageView, final Bitmap bitmap) {
		Animation animation = new AlphaAnimation(1.0f, 0.1f);
		animation.setDuration(200);
		AnimationListener listener = new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {}

			@Override
			public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationEnd(Animation animation) {
				imageView.startAnimation(getFloatAnimation());
				imageView.setImageBitmap(bitmap);
			}
		};
		animation.setAnimationListener(listener);
		return animation;
	}

	/**
	 * 浮现动画
	 */
	private Animation getFloatAnimation() {
		Animation animation = new AlphaAnimation(0.1f, 1.0f);
		animation.setDuration(100);
		return animation;
	}

	/**
	 * 取消加载
	 */
	public void cancelTask(ImageLoader task) {
		if (tasks != null) {
			ImageExecutor.removeTask(task);
		}
	}

	/**
	 * 取消加载
	 */
	public void cancelAllTask() {
		if (tasks != null) {
			Iterator<ImageLoader> iterator = tasks.iterator();
			while (iterator.hasNext()) {
				ImageLoader task = iterator.next();
				ImageExecutor.removeTask(task);
			}
		}
	}
}
