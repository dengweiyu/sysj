package com.li.videoapplication.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.SubtitleHelper;
import com.li.videoapplication.ui.view.AspectRatioLayout;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.URLUtil;

import java.io.File;


/**
 * 碎片：视频播放
 */
public class VideoActivity extends TBaseActivity {

	private static final String TAG = VideoActivity.class.getSimpleName();

	public static void startVideoActivity(Context context, String uri, String title) {
		if (context == null)
			return;
		if (uri == null)
			return;
		Intent intent = new Intent();
		intent.setClass(context, VideoActivity.class);
		intent.putExtra("uri", uri);
		if (title != null) {
			intent.putExtra("title", title);
		}
		context.startActivity(intent);
	}

	private String title;
	private String uri;

	@Override
	public void refreshIntent() {
		super.refreshIntent();
		try {
			uri = getIntent().getStringExtra("uri");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			title = getIntent().getStringExtra("title");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(tag, "uri=" + uri);
		if (uri == null) {
			finish();
		}
		File file = null;
		try {
			file = new File(uri);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!URLUtil.isURL(uri) &&
				(file == null || !file.exists())) {
			finish();
		}
	}

	@Override
	public int getContentView() {
		return R.layout.swi_video;
	}

	@Override
	public int inflateActionBar() {
		return 0;
	}

	public static final float RATIO_MAX = 16F / 9F;
	private long pos = 0;
	private VideoView videoPlayer;
	private MediaController mediaController;
	private SubtitleHelper subtitleHelper;
	private TextView subtitle, tl;
	private AspectRatioLayout aspectRatioLayout;

	// 用来判断屏幕是否锁屏
	private boolean isScreenOn;
	private PowerManager powerManager;
	private WakeLock wakeLock;


	@Override
	public void beforeOnCreate() {
		super.beforeOnCreate();

		setSystemBar(false);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 禁止屏幕休眠
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	public void afterOnCreate() {
		super.afterOnCreate();

		// 获取当前屏幕状态
		powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |
				PowerManager.ON_AFTER_RELEASE, TAG);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onStop() {
		super.onStop();
		isScreenOn = powerManager.isScreenOn();
		// 屏幕变暗
		if (!isScreenOn) {
			pauseVideo();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onStart() {
		super.onStart();
		isScreenOn = powerManager.isScreenOn();
		// 屏幕变亮
		if (isScreenOn) {
			if (null != wakeLock && (!wakeLock.isHeld())) {
				wakeLock.acquire();
			}
			resumeVideo();
		}
	}

	/**
	 * 复位
	 */
	@Override
	public void onResume() {
		super.onResume();
		resumeVideo();
		resumeSubtitle();
	}

	/**
	 * 暂停
	 */
	@Override
	public void onPause() {
		pauseVideo();
		pauseSubtitle();
		super.onPause();
	}

	/**
	 * 销毁
	 */
	@Override
	protected void onDestroy() {
		cancelProgressDialog();
		super.onDestroy();
		destroyVideo();
	}

	/**
	 * 保存播放进度
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong("pos", pos);
		Log.e(tag, "pos=" + pos);
	}

	/**
	 * 取出播放进度
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState != null && savedInstanceState.containsKey("pos")) {
			pos = savedInstanceState.getLong("pos");
			Log.e(tag, "pos=" + pos);
		}
	}

	/**
	 * 屏幕切换
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.e(tag, "newConfig=" + newConfig);
		int orientation = resources.getConfiguration().orientation;
		Log.e(tag, "onConfigurationChanged/orientation=" + orientation);
	}

	private int orientation;

	/**
	 * 横屏
	 */
	private void setMaxSize() {
		Log.d(TAG, "setMaxSize: // -------------------------------------------------");
		if (Build.VERSION.SDK_INT >= 9) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		orientation = Configuration.ORIENTATION_LANDSCAPE;
	}

	/**
	 * 竖屏
	 */
	private void setMinSize() {
		Log.d(TAG, "setMinSize: // -------------------------------------------------");
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		orientation = Configuration.ORIENTATION_PORTRAIT;
	}

	@Override
	public void initView() {
		super.initView();
	//	showLoadingDialog("提示","视频正在加载中",false);
		initPlayer();
	}

	@Override
	public void loadData() {
		super.loadData();

		try {
			startPlayer(uri, pos);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initPlayer() {

		videoPlayer = (VideoView) findViewById(R.id.videoPlayer);
		tl = (TextView) findViewById(R.id.title);
		subtitle = (TextView) findViewById(R.id.subtitle);
		setTextViewText(tl,title);
		aspectRatioLayout = (AspectRatioLayout) findViewById(R.id.aspectRatioLayout);
		aspectRatioLayout.setAspectRatio(1.0f / RATIO_MAX);

		mediaController = new MediaController(this);

		videoPlayer.setMediaController(mediaController);
		mediaController.setMediaPlayer(videoPlayer);
		mediaController.setAnchorView(videoPlayer);
		videoPlayer.setOnErrorListener(onErrorListener);
		if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
			videoPlayer.setOnInfoListener(onInfoListener);
		}
	}

	private void setRatio(MediaPlayer mediaPlayer) {
		Log.d(TAG, "setRatio: // -------------------------------------------------");
		if (mediaPlayer == null)
			return;
		float width = mediaPlayer.getVideoWidth();
		float height = mediaPlayer.getVideoHeight();
		float ratio = width / height;
		if (ratio > 1) {// 横屏
			setMaxSize();
		} else {
			setMinSize();
		}
		Log.d(TAG, "setRatio: ratio=" + ratio);
		if (aspectRatioLayout != null)
			aspectRatioLayout.setAspectRatio(ratio);
	}

	private MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {

		private boolean isError = false;

		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			Log.d(TAG, "onError: // -------------------------------------------------");
			Log.d(TAG, "onInfo: what=" + what);
			Log.d(TAG, "onInfo: extra=" + extra);
			if (mp != null)
				mp.stop();
			if (isError == false) {
				showToastLong("播放错误");

				postDelayed(new Runnable() {
					@Override
					public void run() {
						try {
							finish();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, 800);

				isError = true;
			}
			return true;
		}
	};
	private MediaPlayer.OnInfoListener onInfoListener = new MediaPlayer.OnInfoListener() {

		@Override
		public boolean onInfo(MediaPlayer mp, int what, int extra) {
			Log.d(TAG, "onInfo: // -------------------------------------------------");
			return false;
		}
	};

	private void startPlayer(final String url, final long pos) throws Exception{

		if (videoPlayer != null && url != null) {

			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

			this.pos = pos;
			videoPlayer.setVideoURI(Uri.parse(url));
			videoPlayer.setOnPreparedListener(onPreparedListener);
			videoPlayer.setOnCompletionListener(onCompletionListener);
		}
	}

	private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {

		@Override
		public void onPrepared(MediaPlayer mediaPlayer) {
			Log.d(TAG, "onPrepared: // -------------------------------------------------");
			setRatio(mediaPlayer);
			cancelProgressDialog();
			addSubtitle(mediaPlayer);
			mediaPlayer.start();
			mediaPlayer.seekTo((int) pos);
		}
	};

	private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer mp) {
			Log.d(TAG, "onCompletion: // -------------------------------------------------");
			finish();
		}
	};

	/**
	 * 加载字幕
	 */
	private void addSubtitle(MediaPlayer mediaPlayer) {
		if (mediaPlayer == null)
			return;
		Log.d(tag, "addSubtitle/uri=" + uri);
		if (uri == null)
			return;
		File file = SYSJStorageUtil.createSubtitlePath(uri);
		if (file == null || !file.exists())
			return;
		String path = file.getAbsolutePath();
		Log.d(tag, "addSubtitle/path=" + path);

		playSubtitle(true, path);

//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//			try {
//				mediaPlayer.addTimedTextSource(path, MediaPlayer.MEDIA_MIMETYPE_TEXT_SUBRIP);
//				MediaPlayer.TrackInfo[] trackInfos = mediaPlayer.getTrackInfo();
//				int index = -1;
//				for (int i = 0; i < trackInfos.length; i++) {
//					if (trackInfos[i].getTrackType() == MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT) {
//						index = i;
//					}
//				}
//				if (index >= 0) {
//					mediaPlayer.selectTrack(index);
//					Log.d(tag, "addSubtitle/index=" + index);
//				}
//				mediaPlayer.setOnTimedTextListener(onTimedTextListener);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
	}

	private void resumeSubtitle() {
		if (subtitleHelper != null) {
			subtitleHelper.setPlaying(true);
			subtitleHelper.playSubtitle();
		}
	}

	private void pauseSubtitle() {
		if (subtitleHelper != null) {
			subtitleHelper.setPlaying(false);
		}
	}

	private void playSubtitle(boolean playing, String path) {
		if (subtitleHelper == null) {
			subtitleHelper = new SubtitleHelper(path, videoPlayer, subtitle);
		}
		subtitleHelper.setPlaying(playing);
		subtitleHelper.playSubtitle();
	}


	// TODO: 2016/8/4 OnTimedTextListener 接口在字幕结束时并不会调用
	private MediaPlayer.OnTimedTextListener onTimedTextListener = new MediaPlayer.OnTimedTextListener() {


		@Override
		public void onTimedText(MediaPlayer mp, TimedText text) {

			try {
				Log.d(tag, "onTimedText/text=" + text);
				Log.d(tag, "onTimedText/text=" + text.getText());
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (text != null &&
					!StringUtil.isNull(text.getText())) {
				setContent(text.getText());
			} else {
				// setContent("");
			}
		}
	};

	private void setContent(final String text) {
		post(new Runnable() {
			@Override
			public void run() {
				if (text == null) {
					subtitle.setText("" );
				} else {
					try {
						String string = text.trim();
						subtitle.setText(string);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	private void resumeVideo() {
		if ((pos != 0) && !StringUtil.isNull(uri)) {
			try {
				startPlayer(uri, pos);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void pauseVideo() {
		if (videoPlayer != null) {
			pos = videoPlayer.getCurrentPosition();
			videoPlayer.pause();
		}
	}
	
	private void destroyVideo() {
		if (videoPlayer != null) {
			videoPlayer.pause();
			videoPlayer.stopPlayback();
		}
	}
}