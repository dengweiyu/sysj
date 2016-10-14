package com.li.videoapplication.data.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.provider.MediaStore.Images.Thumbnails;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.data.network.RequestExecutor;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.AppManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public class MyImportVideoImageLoader {

	private MemoryCache memoryCache = MemoryCache.getInstance();
	private FileCache fileCache = new FileCache(AppManager.getInstance().getContext());
	private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
	private MediaMetadataRetriever mmr = new MediaMetadataRetriever();

	public MyImportVideoImageLoader() {
		super();
	}

	public void displayImage(String url, ImageView imageView) {
		imageViews.put(imageView, url);
		// 先从内存缓存中查找
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else {
			// 若没有的话则开启新线程加载图片
			displayImageNewTask(url, imageView);
		}
	}

	public void displayVideoTime(String dir, TextView textView) {
		mmr.setDataSource(dir);
		String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 播放时长单位为毫秒
		int time = 0;
		time = Integer.parseInt(duration);

		long min = time / 1000 / 60;
		long sec = time / 1000 % 60;
		// textView.setText(minView+":"+sec);
		if (min < 10) {
			if (sec < 10) {
				textView.setText("0" + min + ":" + "0" + sec);
			} else {
				textView.setText("0" + min + ":" + sec);
			}
		} else {
			if (sec < 10) {
				textView.setText(min + ":" + "0" + sec);
			} else {
				textView.setText(min + ":" + sec);
			}
		}
	}

	private void displayImageNewTask(String url, ImageView imageView) {
		ImageObject object = new ImageObject(url, imageView);
		ImageLoader loader = new ImageLoader(object);
		RequestExecutor.execute(loader);
	}

	private static class ImageObject {

		public String url;
		public ImageView imageView;

		public ImageObject(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	/**
	 * 加载图片
	 */
	private class ImageLoader implements Runnable {

		private ImageObject object;

		private ImageLoader(ImageObject object) {
			this.object = object;
		}

		@Override
		public void run() {
			if (reusedImage(object))
				return;
			Bitmap bitmap = getBitmap(object.url);
			memoryCache.put(object.url, bitmap);
			if (reusedImage(object))
				return;
			ImageDisplayer displayer = new ImageDisplayer(bitmap, object);
			UITask.post(displayer);
		}
	}

	/**
	 * 更新界面
	 */
	private class ImageDisplayer implements Runnable {

		private Bitmap bitmap;
		private ImageObject imageObject;

		public ImageDisplayer(Bitmap b, ImageObject p) {
			bitmap = b;
			imageObject = p;
		}

		public void run() {
			if (reusedImage(imageObject))
				return;
			if (bitmap != null) {
				imageObject.imageView.setImageBitmap(bitmap);
			}
		}
	}

	private Bitmap getBitmap(String url) {
		File f = fileCache.getFile(url);
		// 先从文件缓存中查找是否有
		Bitmap b = decodeFile(f);
		if (b != null)
			return b;
		try {
			Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(url, Thumbnails.MINI_KIND);
			return bitmap;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private Bitmap decodeFile(File f) {
		try {
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean reusedImage(ImageObject object) {
		String tag = imageViews.get(object.imageView);
		if (tag == null || !tag.equals(object.url))
			return true;
		return false;
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

	public static void copyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}