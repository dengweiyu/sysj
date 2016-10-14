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

public class VideoTimeLoader {

	@SuppressLint("NewApi")
	private MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    private Map<TextView, String> map = Collections.synchronizedMap(new WeakHashMap<TextView, String>());
    private Context context;

	public VideoTimeLoader(Context context) {
		this.context = context;
	}

	public void displayText(String url, TextView textView) {
		map.put(textView, url);
		queuePhoto(url, textView);
	}

	private void queuePhoto(String url, TextView textView) {
		TimeToLoad p = new TimeToLoad(url, textView);
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

	private class TimeLoader implements Runnable {

		private TimeToLoad timeToLoad;

		public TimeLoader(TimeToLoad timeToLoad) {
			this.timeToLoad = timeToLoad;
		}

		@Override
		public void run() {
			if (reusedText(timeToLoad)) {
				return;
			}

			String str = null;
			try {
				str = getDuration(timeToLoad.url);
			} catch (Exception e) {
				e.printStackTrace();
			}
			TextDisplay bd = new TextDisplay(str, timeToLoad);
			Activity a = (Activity) timeToLoad.textview.getContext();
			a.runOnUiThread(bd);
		}
	}

	private boolean reusedText(TimeToLoad timeToLoad) {
		String tag = map.get(timeToLoad.textview);
		if (tag == null || !tag.equals(timeToLoad.url))
			return true;
		return false;
	}

	@SuppressLint("NewApi")
	public String getDuration(String dir) throws Exception {
		String duration = "0";
		retriever.setDataSource(dir);
		duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
		return duration;
	}

	private class TextDisplay implements Runnable {

		private String text;
		private TimeToLoad timeToLoad;

		public TextDisplay(String b, TimeToLoad p) {
			text = b;
			timeToLoad = p;
		}

		public void run() {
			if (reusedText(timeToLoad))
				return;
			if (text != null) {
				long time = 0;
				try {
					time = Long.valueOf(text);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				if (time <= 0)
					return;
				if (time < 1000) {
					time = 1000;
				}
				int min = (int) (time / 1000 / 60);
				int sec = (int) (time / 1000 % 60);
				if (min < 10) {
					if (sec < 10) {
						timeToLoad.textview.setText("0" + min + ":" + "0" + sec);
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
