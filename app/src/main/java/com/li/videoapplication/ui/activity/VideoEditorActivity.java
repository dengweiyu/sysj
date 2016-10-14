package com.li.videoapplication.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.local.FileUtil;
import com.li.videoapplication.data.local.LPDSStorageUtil;
import com.fmsysj.zbqmcs.utils.VideoEditor;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.view.VideoPlayer;
import com.li.videoapplication.views.VideoSliceSeekBar;
import com.li.videoapplication.R;
import com.li.videoapplication.data.database.VideoCaptureManager;
import com.li.videoapplication.data.network.LightTask;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.TBaseActivity;

import java.io.File;

/**
 * 碎片：视频编辑
 */
public class VideoEditorActivity extends TBaseActivity implements OnClickListener {

	private VideoCaptureEntity entity;
	private String extName;

	@Override
    public void refreshIntent() {
		super.refreshIntent();
        try {
            entity = (VideoCaptureEntity) getIntent().getSerializableExtra("entity");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (entity == null)
            finish();
		File file = new File(entity.getVideo_path());
		if (file != null)
			extName = FileUtil.getExtName(file.getName());
    }

	private VideoSliceSeekBar seekBar;
	private VideoPlayer videoView;
	private ImageView play;
    private Button save, cancel;
    private ImageView cover;

	private ImageView a, b, c, d;

	private StateObserver stateObserver;
	// 视频简介的结束时间
	public TextView textTimeMax;
	// 视频剪辑的开始时间
	public TextView textTimeMin;
	// 时间剪辑的总时长
	private TextView textTimeSel;

	private long[] textTime;
	// 秒数
	private long seconds;
	private long startMs = 0;
	private long endMs = 1000;

	public Handler handler = new Handler() {
		@SuppressLint("NewApi")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case 1:
				// 从VideoSliceSeekBar传过来的时间数值
				textTime = (long[]) msg.obj;
				String textright;
				// 获得右边游标得到的时间
				if (textTime[1] <= 1000) {// 小于1000毫秒按1000毫秒
					textright = getTimeForTrackFormat(1000, true);
				} else {
					textright = getTimeForTrackFormat(textTime[1], true);
				}
				// 获得左边游标得到的时间
				String textleft = getTimeForTrackFormat(textTime[0], true);
				textTimeMax.setText(textright);
				textTimeMin.setText(textleft);
				break;

			case 4:
				String s = (String) msg.obj;
				showToastLong(s);
				break;
			}
		}
	};

	@Override
	public int getContentView() {
		return R.layout.activity_videoeditor;
	}

	public int inflateActionBar() {
		return 0;
	}

	@Override
	public void beforeOnCreate() {
		super.beforeOnCreate();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	public void afterOnCreate() {
		super.afterOnCreate();
		stateObserver = new StateObserver();
	}

	@Override
	public void initView() {
		super.initView();
		initContentView();
		initVideoView();
	}

	private void initContentView() {

		videoView = (VideoPlayer) findViewById(R.id.videoview);
        seekBar = (VideoSliceSeekBar) findViewById(R.id.seekbar);

        textTimeMin = (TextView) findViewById(R.id.videoeditor_timeMin);
		textTimeMax = (TextView) findViewById(R.id.videoeditor_timeMax);
		textTimeSel = (TextView) findViewById(R.id.videoeditor_timeSel);

        save = (Button) findViewById(R.id.videoeditor_save);
		cancel = (Button) findViewById(R.id.videoeditor_cancel);
		cover = (ImageView) findViewById(R.id.videoeditor_cover);
		play = (ImageView) findViewById(R.id.videoeditor_play);

		a = (ImageView) findViewById(R.id.videoeditor_image1);
        b = (ImageView) findViewById(R.id.videoeditor_image2);
        c = (ImageView) findViewById(R.id.videoeditor_image3);
        d = (ImageView) findViewById(R.id.videoeditor_image4);

		cancel.setOnClickListener(this);
		save.setOnClickListener(this);
	}

	@SuppressLint("NewApi")
	private void initVideoView() {

		videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBar.setSeekBarChangeListener(new VideoSliceSeekBar.SeekBarChangeListener() {

                    public void SeekBarValueChanged(long leftThumb, long rightThumb) {
                        if (startMs != leftThumb) {
                            videoView.seekTo((int) leftThumb);
                            startMs = leftThumb;
                        }
                        if (endMs != rightThumb) {
                            videoView.seekTo((int) rightThumb);
                            endMs = rightThumb;
                        }
                        textTimeSel.setText(getTimeForTrackFormat(endMs - startMs, true));
                    }
                });

                textTimeMax.setText(getTimeForTrackFormat(mp.getDuration(), true));
                if (mp.getDuration() >= 10000) {
                    textTimeMin.setText(getTimeForTrackFormat(10000, true));
                } else {
                    textTimeMin.setText(getTimeForTrackFormat(mp.getDuration(), true));
                }

                startMs = 0;
                endMs = mp.getDuration();

                seekBar.setMaxValue(mp.getDuration());
                seekBar.setLeftProgress(0);
                seekBar.setRightProgress(mp.getDuration());
                seekBar.setProgressMinDiff(10000);

                play.setOnClickListener(VideoEditorActivity.this);
                cover.setOnClickListener(VideoEditorActivity.this);
            }
        });
		videoView.setVideoURI(Uri.parse(entity.getVideo_path()));
		videoView.seekTo(1);
		videoView.start();
		videoView.pause();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(entity.getVideo_path());
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                int seconds = Integer.valueOf(time);
                final Bitmap aa = retriever.getFrameAtTime(seconds / 8 * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                final Bitmap bb = retriever.getFrameAtTime(seconds / 6 * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                final Bitmap cc = retriever.getFrameAtTime(seconds / 4 * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                final Bitmap dd = retriever.getFrameAtTime(seconds / 2 * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        a.setBackground(new BitmapDrawable(getResources(), aa));
                        b.setBackground(new BitmapDrawable(getResources(), bb));
                        c.setBackground(new BitmapDrawable(getResources(), cc));
                        d.setBackground(new BitmapDrawable(getResources(), dd));
                    }
                });
            }
        };
        LightTask.postAtFrontOfQueue(r);
	}

    @Override
    public void onClick(View v) {
        if (v == cancel) {// 取消
            finish();
        } else if (v == save) {// 保存
            showCustomDialog(entity.getVideo_name());
			UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "视频编辑-有效");
        } else if (v == cover) {// 暂停
            playVideo();
        } else if (v == play) {// 播放
            playVideo();
        }
    }

	/**
	 * 播放暂停视频
	 */
	@SuppressWarnings("deprecation")
	private void playVideo() {
		if (videoView.isPlaying()) {
			videoView.pause();
			seekBar.setSliceBlocked(false);
			seekBar.removeVideoStatusThumb();
			play.setBackgroundDrawable(resources.getDrawable(R.drawable.icon_play_normal));
		} else {
			videoView.seekTo((int) seekBar.getLeftProgress());
			videoView.start();
			seekBar.setSliceBlocked(true);
			seekBar.videoPlayingProgress(seekBar.getLeftProgress());
			stateObserver.startVideoProgressObserving();
			play.setBackgroundDrawable(null);
		}
	}

	/**
	 * 将时间毫秒值转为hh:mm:ss
	 */

	private String getTimeForTrackFormat(long timeInMills, boolean display2DigitsInMinsSection) {
		long hours = (timeInMills / (60 * 60 * 1000));
		long minutes = timeInMills / 1000 / 60 % 60;
		seconds = timeInMills / 1000;
		long mseconds = timeInMills / 1000 % 60 % 60;

		String resultsec = (display2DigitsInMinsSection && mseconds < 10 ? "0" : "") + mseconds;
		String resultmin = (display2DigitsInMinsSection && minutes < 10 ? "0" : "") + minutes;
		String resulthours = (display2DigitsInMinsSection && hours < 10 ? "0" : "") + hours;
		String result = resulthours + ":" + resultmin + ":" + resultsec;
		return result;
	}

	@SuppressLint("HandlerLeak")
	private class StateObserver extends Handler {

		private boolean alreadyStarted = false;

		private void startVideoProgressObserving() {
			if (!alreadyStarted) {
				alreadyStarted = true;
				sendEmptyMessage(0);
			}
		}

		private Runnable observerWork = new Runnable() {

			@Override
			public void run() {
				startVideoProgressObserving();
			}
		};

		@SuppressWarnings("deprecation")
		@Override
		public void handleMessage(Message msg) {
			alreadyStarted = false;
			seekBar.videoPlayingProgress(videoView.getCurrentPosition());
			if (videoView.isPlaying() && videoView.getCurrentPosition() < seekBar.getRightProgress()) {
				postDelayed(observerWork, 50);
			} else {
				if (videoView.isPlaying())
					videoView.pause();
				seekBar.setSliceBlocked(false);
				seekBar.removeVideoStatusThumb();
				// 重新设置播放图片
				play.setBackgroundDrawable(resources.getDrawable(R.drawable.icon_play_normal));
			}
		}
	}

	private void showCustomDialog(final String oldName) {

		final EditText editText = new EditText(this);
		editText.setPadding(dp2px(6), dp2px(12), dp2px(6), dp2px(12));
		editText.setText(oldName + "_tmp");

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("请输入视频名称");
		builder.setView(editText);
		builder.setPositiveButton("确定", null);
		builder.setNegativeButton("取消", null);
		final AlertDialog dialog = builder.create();
        dialog.show();
		View.OnClickListener positive = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final String newName = editText.getText().toString();
				if (newName.isEmpty()) {
					showToastShort("视频名不能为空");
					return;
				}
				if (newName.equals(oldName)) {
					showToastShort("不能与源视频同名");
					return;
				}
				cutVideo(newName, dialog);
			}
		};
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(positive);
	}

    /**
     * 保存剪辑的视频
     */
	private void cutVideo(final String newName, final AlertDialog dialog) {

		LightTask.postAtFrontOfQueue(new Runnable() {

			@Override
			public void run() {
				String newPath = SYSJStorageUtil.getSysjRec() + File.separator + newName + "." + extName;
				// 尝试插入数据库
                int result = VideoCaptureManager.saveSafely(newName,
                        newPath,
                        VideoCaptureEntity.VIDEO_SOURCE_CUT,
                        VideoCaptureEntity.VIDEO_STATION_LOCAL);
				Message msg = new Message();
				msg.what = 4;
				if (result == 1) {// 数据库插入成功
					UITask.post(new Runnable() {
						@Override
						public void run() {
							dialog.dismiss();
						}
					});
					// 开始视频剪辑
					boolean flag = VideoEditor.startTrim(entity.getVideo_path(), newPath, textTime[0], textTime[1]);
					if (flag) {
						msg.obj = "视频剪辑成功";
                        // 加载本地视频
                        DataManager.LOCAL.loadVideoCaptures();
						finish();
					} else {
						msg.obj = "视频剪辑失败了";
					}
				} else if (result == 0) {// 数据库存在重名
					msg.obj = "视频名称已存在";
				} else {
					UITask.post(new Runnable() {
						@Override
						public void run() {
							dialog.dismiss();
						}
					});
					msg.obj = "视频剪辑失败了";
				}
				handler.sendMessage(msg);
			}
		});
	}
}
