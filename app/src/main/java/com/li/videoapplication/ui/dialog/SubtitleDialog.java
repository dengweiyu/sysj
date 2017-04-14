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
 * 弹框：覆盖字幕
 */
@SuppressLint("CutPasteId") 
public class SubtitleDialog extends BaseDialog implements View.OnClickListener {

	private TextView cancel, confirm;
	private DialogInterface.OnClickListener confirmListener, cancelListener;

	@Override
	protected void afterContentView(Context context) {
		super.afterContentView(context);

		setCanceledOnTouchOutside(false);
		setCancelable(false);

	}

	public SubtitleDialog(Context context, DialogInterface.OnClickListener confirmListener, DialogInterface.OnClickListener cancelListener) {
		super(context);
		this.confirmListener = confirmListener;
		this.cancelListener = cancelListener;

		cancel = (TextView) findViewById(R.id.subtitle_cancel);
		confirm = (TextView) findViewById(R.id.subtitle_confirm);

		cancel.setOnClickListener(this);
		confirm.setOnClickListener(this);
	}

	@Override
	protected int getContentView() {
		return R.layout.dialog_subtitle;
	}

	@Override
	public void onClick(View v) {

		if (v == confirm) {
			if (confirmListener != null)
				confirmListener.onClick(this, v.getId());
		} else if (v == cancel) {
			if (cancelListener != null)
				cancelListener.onClick(this, v.getId());
		}
		dismiss();
	}
}
