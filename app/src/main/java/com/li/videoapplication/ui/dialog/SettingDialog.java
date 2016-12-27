/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.li.videoapplication.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.SeekBar;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.BaseDialog;
import com.li.videoapplication.ui.activity.VideoPlayActivity;

/**
 * 弹框：音量，亮度设置
 */
@SuppressLint("CutPasteId") 
public class SettingDialog extends BaseDialog{

	private SeekBar volume, brightness;

	private VideoPlayActivity activity;

	public SettingDialog(Context context) {
		super(context);
	}

	@Override
	protected int getContentView() {
		return R.layout.dialog_setting;
	}

	private int maxVolume;
	private int currentVolume;
	private float currentBrightness;
	
	@Override
	protected void afterContentView(Context context) {
		super.afterContentView(context);

		activity = (VideoPlayActivity) AppManager.getInstance().getActivity(VideoPlayActivity.class);

		volume = (SeekBar) findViewById(R.id.setting_volume);
		brightness = (SeekBar) findViewById(R.id.setting_brightness);

		volume.setMax(100);
		volume.setProgress(0);
		if (activity != null) {
			maxVolume = activity.getMaxVolume();
			currentVolume = activity.getCurrentVolume();
			int volume = currentVolume * 100 / maxVolume;
			Log.i(tag, "maxVolume=" + maxVolume);
			Log.i(tag, "currentVolume=" + currentVolume);
			Log.i(tag, "volume=" + volume);
			this.volume.setProgress(volume);
		}
		volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				Log.i(tag, "progress=" + progress);
				if (activity != null) {
					maxVolume = activity.getMaxVolume();
					int volume = progress * maxVolume / 100;
					Log.i(tag, "progress=" + progress);
					Log.i(tag, "maxVolume=" + maxVolume);
					Log.i(tag, "volume=" + volume);
					activity.setCurrentVolume(volume);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});

		brightness.setMax(100);
		brightness.setProgress(0);
		if (activity != null) {
			currentBrightness = activity.getCurrentBrightness();
			int brightness = (int) (currentBrightness * 100);
			Log.i(tag, "currentBrightness=" + currentBrightness);
			Log.i(tag, "brightness=" + brightness);
			this.brightness.setProgress(brightness);
		}
		brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (activity != null) {
					float brightness = Float.valueOf(progress) / 100;
					Log.i(tag, "progress=" + progress);
					Log.i(tag, "brightness=" + brightness);
					activity.setCurrentBrightness(brightness);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
	}
}
