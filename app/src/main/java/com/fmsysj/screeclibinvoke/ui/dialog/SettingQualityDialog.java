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
package com.fmsysj.screeclibinvoke.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.fmsysj.screeclibinvoke.data.model.configuration.RecordingSetting;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.BaseDialog;

/**
 * 弹框：清晰度设置
 */
@SuppressLint("CutPasteId") 
public class SettingQualityDialog extends BaseDialog implements View.OnClickListener {

	private TextView low, standard, high, ultraHigh, cancel;
	private Qualityable listener;

	public SettingQualityDialog(Context context, Qualityable listener) {
		super(context);
		this.listener = listener;

		cancel = (TextView) findViewById(R.id.settingquality_cancel);
		low = (TextView) findViewById(R.id.settingquality_low);
		standard = (TextView) findViewById(R.id.settingquality_standard);
		high = (TextView) findViewById(R.id.settingquality_high);
		ultraHigh = (TextView) findViewById(R.id.settingquality_ultrahigh);

		low.setVisibility(View.GONE);

		cancel.setOnClickListener(this);
		low.setOnClickListener(this);
		standard.setOnClickListener(this);
		high.setOnClickListener(this);
		ultraHigh.setOnClickListener(this);
	}

	@Override
	protected int getContentView() {
		return R.layout.dialog_settingquality;
	}

	@Override
	public void onClick(View v) {

		if (v == low) {
			if (listener != null)
				listener.quality(RecordingSetting.QUALITY_LOW);
		} else if (v == standard) {
			if (listener != null)
				listener.quality(RecordingSetting.QUALITY_STANDARD);
		} else if (v == high) {
			if (listener != null)
				listener.quality(RecordingSetting.QUALITY_HIGH);
		} else if (v == ultraHigh) {
			if (listener != null)
				listener.quality(RecordingSetting.QUALITY_ULTRA_HIGH);
		}
		dismiss();
	}

	public interface Qualityable {
		void quality(String quality);
	}
}
