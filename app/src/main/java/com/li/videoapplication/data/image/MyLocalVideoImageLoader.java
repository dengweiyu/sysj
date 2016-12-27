package com.li.videoapplication.data.image;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.AsyncTask;
import com.li.videoapplication.utils.BitmapUtil;

/**
 * 功能：本地视频图片加载
 */
@SuppressLint("NewApi")
public class MyLocalVideoImageLoader {

	/**
	 * 图片缓存
	 */
	private LruCache<String, Bitmap> lruCache;

	private ViewGroup viewGroup;
	private Set<ImageLoaderTask> task = new HashSet<>();
	private Context context;

	/** 图片SD卡缓存地址 */
	private String path;
    private String[] URLS;

    public void setURLS(String[] URLS) {
        this.URLS = URLS;
    }

	public MyLocalVideoImageLoader(ViewGroup viewGroup, String[] URLS) {
		this.viewGroup = viewGroup;
        this.URLS = URLS;
		context = AppManager.getInstance().getContext();

		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 4;
		lruCache = new LruCache<String, Bitmap>(cacheSize) {

			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getByteCount();
			}
		};

		// 默认缓存图片存储到应用缓存文件夹
		path = context.getExternalCacheDir().getPath() + "/";
	}

	/**
	 * 将图片添加到内存中
	 */
	public void addBitmapToCaChe(String url, Bitmap bitmap) {
		if (getBitmapFromCache(url) == null) {
			lruCache.put(url, bitmap);
		}
	}

	/**
	 * 从内存中获取图片
	 */
	public Bitmap getBitmapFromCache(String url) {
		return lruCache.get(url);
	}

	/**
	 * 该方法只负责显示默认图片或者缓存中的图片
	 */
	public void loadImagesFromCaches(ImageView imageView, String url) {
		// 从缓存中获取图片
		Bitmap bitmap = getBitmapFromCache(url);
		if (bitmap == null) {
			// 如果缓存中没有图片,直接设置为默认图片
			imageView.setImageDrawable(Constant.PICTRUE_DEFAULT_TRANSPARENT);
		} else {
			imageView.setImageBitmap(bitmap);
		}

	}

	/**
	 * 加载图片
	 * 
	 * @param start 开始加载的listview的item位置
	 * @param end 结束加载的listview的item图片位置
	 */
	public void loadImages(int start, int end) {
		for (int i = start; i < end; i++) {
			String url = URLS[i];
			// 从内存中读取图片
			Bitmap bitmap = getBitmapFromCache(url);
			if (bitmap != null) {
				setImageBitmap(url, bitmap);
			} else {
				// 从SD卡中读取缓存图片
				bitmap = getBitmapFromSDCrad(url);
				if (bitmap != null) {
					// 将图片加入缓存
					addBitmapToCaChe(url, bitmap);
					setImageBitmapAndAnimation(url, bitmap);
				} else {
					ImageLoaderTask task = new ImageLoaderTask(url);
					task.execute(url);
					this.task.add(task);
				}
			}
		}
	}

	private void setImageBitmap(String url, Bitmap bitmap) {
		ImageView imageView = (ImageView) viewGroup.findViewWithTag(url);
		if (imageView != null) {
			imageView.setImageBitmap(bitmap);
		}
	}

	private void setImageBitmapAndAnimation(String url, Bitmap bitmap) {
		ImageView imageView = (ImageView) viewGroup.findViewWithTag(url);
		if (imageView != null) {
			imageView.startAnimation(getDismissAnimation(imageView, bitmap));
		}
	}

	/**
	 * 消失动画
	 */
	private Animation getDismissAnimation(final ImageView imageView, final Bitmap bitmap) {

		Animation anim = new AlphaAnimation(1.0f, 0.1f);
		// 渐变时间
		anim.setDuration(200);
		anim.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.startAnimation(getFloatAnimation());
                imageView.setImageBitmap(bitmap);
            }
        });
		return anim;
	}

	/**
	 * 浮现动画
	 */
	private Animation getFloatAnimation() {
		Animation anim = new AlphaAnimation(0.1f, 1.0f);
		// 渐变时间
		anim.setDuration(100);
		return anim;
	}

	/**
	 * 取消加载
	 */
	public void cancelAllTasks() {
		if (task != null) {
			for (ImageLoaderTask task : this.task) {
				task.cancel(false);
			}
		}
	}

	/**
	 * 加载图片异步类
	 */
	public class ImageLoaderTask extends AsyncTask<String, Void, Bitmap> {

		private Bitmap bitmap;
        private String url;

		public ImageLoaderTask(String url) {
			this.url = url;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			bitmap = getBitmapFromVideo2(params[0]);
			if (bitmap != null) {
				// 存入内存和SD卡
				addBitmapToCaChe(params[0], bitmap);
				addBitmapToSDCard(params[0], bitmap);
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			super.onPostExecute(bitmap);
			ImageView imageView = (ImageView) viewGroup.findViewWithTag(url);
			if (imageView != null && bitmap != null) {
				imageView.startAnimation(getDismissAnimation(imageView, bitmap));
			}
			task.remove(this);
		}
	}

	/**
	 * 从视频文件中截取图片
	 */
	public static Bitmap getBitmapFromVideo2(String url) {
		Bitmap bitmap;
		bitmap = getBitmapFromVideo(url);
		if (bitmap != null) {
			return getBitmap(bitmap);
		}
		// 返回默认图片
		int bimId = R.drawable.play_bg;
		Resources res = AppManager.getInstance().getContext().getResources();
		bitmap = BitmapFactory.decodeResource(res, bimId);
		return bitmap;
	}

	/**
	 * 将位图进行局部截取
	 */
	private static Bitmap getBitmap(Bitmap srcBitmap) {
		int scrWidth = srcBitmap.getWidth();
		int scrHeight = srcBitmap.getHeight();
		Bitmap finalBitmap;
		if (scrWidth < scrHeight) {
			int widthleft = 0;
			int heightleft = (scrHeight - scrWidth * 9 / 16) / 2;
			finalBitmap = Bitmap.createBitmap(srcBitmap, widthleft, heightleft, scrWidth, scrWidth * 9 / 16);
		} else {
			finalBitmap = srcBitmap;
		}
		return finalBitmap;
	}

	/**
	 * 从视频中生成一张图片
	 */
	public static Bitmap getBitmapFromVideo(String videoPath) {
		Bitmap bitmap;
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		try {
			retriever.setDataSource(videoPath);
			String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
			int seconds = Integer.valueOf(time);
			bitmap = retriever.getFrameAtTime(seconds / 2 * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
		} catch (Exception e) {
			e.printStackTrace();
			int bimId = R.drawable.play_bg;
			Resources res = AppManager.getInstance().getContext().getResources();
			bitmap = BitmapFactory.decodeResource(res, bimId);
		}
		return bitmap;
	}

	/**
	 * 保存图片到SD卡上
	 */
	public void addBitmapToSDCard(String url, Bitmap bm) {
		try {
			String fileName = String.valueOf(url.hashCode());
			File file = new File(path + fileName);
			// 创建图片缓存文件夹
			boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
			if (sdCardExist) {
				File maiduo = new File(path);
				File ad = new File(path);
				// 如果文件夹不存在
				if (!maiduo.exists()) {
					// 按照指定的路径创建文件夹
					maiduo.mkdir();
					// 如果文件夹不存在
				} else if (!ad.exists()) {
					// 按照指定的路径创建文件夹
					ad.mkdir();
				}
				// 检查图片是否存在
				if (!file.exists()) {
					file.createNewFile();
				}
			}
            FileOutputStream fos = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
			bos.flush();
			bos.close();
		} catch (Exception e) {
            e.printStackTrace();
		}
	}

	/**
	 * 从SD卡上获取图片
	 */
	@SuppressWarnings("null")
	public Bitmap getBitmapFromSDCrad(String imageUrl) {
		String fileName = String.valueOf(imageUrl.hashCode());
		// 获得文件路径
		String imagePath = path + fileName;
		File file = new File(imagePath);
		if (!file.exists()) {
			return null;
		}
        Bitmap bitmap = BitmapUtil.readBitmap(file);
		if (bitmap != null && bitmap.toString().length() > 3) {
			return bitmap;
		} else
			return null;
	}
}
