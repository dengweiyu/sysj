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
import android.view.View;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.BaseBottomDialog;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.VideoPlayActivity;

/**
 * 弹框：举报
 */
@SuppressLint("CutPasteId")
public class VideoPlayDialog extends BaseBottomDialog implements View.OnClickListener {

	private TextView share, report, cancel;

    private VideoImage videoImage;

	public VideoPlayDialog(Context context, VideoImage videoImage) {
		super(context);
        this.videoImage = videoImage;
	}

	@Override
	protected int getContentView() {
		return R.layout.dialog_videoplay;
	}

	@Override
	protected void afterContentView(Context context) {
		super.afterContentView(context);
		
		share = (TextView) findViewById(R.id.videoplay_share);
		report = (TextView) findViewById(R.id.videoplay_report);
		cancel = (TextView) findViewById(R.id.videoplay_cancel);

		share.setOnClickListener(this);
		report.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.videoplay_share:
            VideoPlayActivity activity = (VideoPlayActivity) AppManager.getInstance().getActivity(VideoPlayActivity.class);
            if (activity != null)
                activity.startShareActivity();
			cancel();
			break;

		case R.id.videoplay_report:

            if (PreferencesHepler.getInstance().isLogin()) {
                showToastShort("请登录");
                return;
            }

            if (videoImage == null) {
                return;
            }
            ActivityManeger.startReportActivity(getContext(), videoImage);
			cancel();
			break;

		case R.id.videoplay_cancel:
			cancel();
			break;
		}
	}
}
