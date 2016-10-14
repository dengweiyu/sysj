package com.li.videoapplication.data.image;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.widget.TextView;

import com.li.videoapplication.data.network.RequestExecutor;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public class VideoTimeThumbnailLoader {

	private Map<TextView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<TextView, String>());
	Context mContext;
	@SuppressLint("NewApi")
	MediaMetadataRetriever mmr = new MediaMetadataRetriever();

	public VideoTimeThumbnailLoader(Context context) {
		mContext = context;
	}

	public void DisplayThumbnailForLocalTime(String dir, TextView textview) {
		imageViews.put(textview, dir);
		queuePhoto(dir, textview);

	}

	private void queuePhoto(String url, TextView textview) {
		TimeToLoad p = new TimeToLoad(url, textview);
		RequestExecutor.execute(new TimeLoader(p));
	}

	private class TimeToLoad {
		public String url;
		public TextView textview;

		public TimeToLoad(String u, TextView i) {
			url = u;
			textview = i;
		}
	}

	class TimeLoader implements Runnable {
		TimeToLoad timeToLoad;

		TimeLoader(TimeToLoad timeToLoad) {
			this.timeToLoad = timeToLoad;
		}

		@Override
		public void run() {
			if (textViewReused(timeToLoad)) {
				return;
			}

			String str = null;
			try {
				str = GetThumbnailForLocalVideo(timeToLoad.url);
			} catch (Exception e) {
				e.printStackTrace();
			}
			TextViewDisplayer bd = new TextViewDisplayer(str, timeToLoad);
			Activity a = (Activity) timeToLoad.textview.getContext();
			a.runOnUiThread(bd);
		}
	}

	boolean textViewReused(TimeToLoad timeToLoad) {
		String tag = imageViews.get(timeToLoad.textview);

		if (tag == null || !tag.equals(timeToLoad.url))
			return true;
		return false;
	}

	@SuppressLint("NewApi")
	public String GetThumbnailForLocalVideo(String dir) throws Exception {
		String duration = "0";
		mmr.setDataSource(dir);
		duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
		return duration;
	}

	class TextViewDisplayer implements Runnable {
		String text;
		TimeToLoad timeToLoad;

		public TextViewDisplayer(String b, TimeToLoad p) {
			text = b;
			timeToLoad = p;
		}

		public void run() {
			if (textViewReused(timeToLoad))
				return;
			if (text != null) {

				long time = Long.valueOf(text).longValue();
				if (time < 1000) {
					time = 1000;
				}
				int min = (int) (time / 1000 / 60);
				int sec = (int) (time / 1000 % 60);
				if (min < 10) {
					if (sec < 10) {
						timeToLoad.textview
								.setText("0" + min + ":" + "0" + sec);
					} else {
						timeToLoad.textview.setText("0" + min + ":" + sec);
					}
				} else {
					if (sec < 10) {
						timeToLoad.textview.setText(min + ":" + "0" + sec);
					} else {
						timeToLoad.textview.setText(min + ":" + sec);
					}
				}

			} else
				timeToLoad.textview.setText("00:00");
		}
	}
}
