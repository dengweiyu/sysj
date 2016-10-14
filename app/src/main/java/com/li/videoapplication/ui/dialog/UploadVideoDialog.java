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
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.BaseDialog;

/**
 * 弹框：上传视频
 */
@SuppressLint("CutPasteId") 
public class UploadVideoDialog extends BaseDialog implements View.OnClickListener {

	private TextView cancel, confirm;
	private DialogInterface.OnClickListener listener;

	public UploadVideoDialog(Context context, DialogInterface.OnClickListener listener) {
		super(context);
		this.listener = listener;

		cancel = (TextView) findViewById(R.id.uploadvideo_cancel);
		confirm = (TextView) findViewById(R.id.uploadvideo_confirm);

		cancel.setOnClickListener(this);
		confirm.setOnClickListener(this);
	}

	@Override
	protected int getContentView() {
		return R.layout.dialog_uploadvideo;
	}

	@Override
	public void onClick(View v) {

		if (v == confirm) {
			if (listener != null)
				listener.onClick(this, v.getId());
		}
		dismiss();
	}
}
