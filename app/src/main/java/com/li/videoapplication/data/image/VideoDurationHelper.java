package com.li.videoapplication.data.image;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.li.videoapplication.framework.AppManager;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 获取视频时长
 */
public class VideoDurationHelper {

	public static final String TAG = VideoDurationHelper.class.getSimpleName();

	private Map<String, String> urls = Collections.synchronizedMap(new WeakHashMap<String, String>());
    private Context context;
	private Handler handler = new Handler(Looper.getMainLooper());
	private ViewGroup viewGroup;

	public VideoDurationHelper(ViewGroup viewGroup) {
		super();
		this.viewGroup = viewGroup;

		context = AppManager.getInstance().getContext();
	}

	public void displayDuration(String path, TextView textView) {
		if (urls.containsKey(path) &&
				urls.get(path) != null &&
				!urls.get(path).equals("00:00")) {
			textView.setText(urls.get(path));
		} else {
			DurationObject object = new DurationObject(path, textView);
			ImageExecutor.execute(new DurationTask(object));
		}
	}

	private class DurationObject {

		public String path;
		public TextView textView;

		public DurationObject(String u, TextView i) {
			path = u;
			textView = i;
		}
	}

	private class DurationTask implements Runnable {

		private DurationObject durationObject;
		private String text;

		public DurationTask(DurationObject durationObject) {
			this.durationObject = durationObject;
		}

		@Override
		public void run() {
			text = VideoDuration.getDuration(durationObject.path);
			Log.d(TAG, "DurationTask: text=" + text);
			final String time = formatTime(text);
			Log.d(TAG, "DurationTask: time=" + time);
			urls.put(durationObject.path, time);
			handler.post(new Runnable() {
				@Override
				public void run() {
					TextView textView = (TextView) viewGroup.findViewWithTag(durationObject.path + durationObject.path);
					if (textView != null) {
						textView.setText(time);
					}
				}
			});
		}
	}

	private String formatTime(String text) {
		if (text != null) {
			long time = 0;
			try {
				time = Long.valueOf(text);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			if (time <= 0)
				return "00:00";
			if (time < 1000) {
				time = 1000;
			}
			int min = (int) (time / 1000 / 60);
			int sec = (int) (time / 1000 % 60);
			if (min < 10) {
				if (sec < 10) {
					return "0" + min + ":" + "0" + sec;
				} else {
					return "0" + min + ":" + sec;
				}
			} else {
				if (sec < 10) {
					return min + ":" + "0" + sec;
				} else {
					return min + ":" + sec;
				}
			}
		} else {
			return "00:00";
		}
	}
}
