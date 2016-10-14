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
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.BaseDialog;
import com.li.videoapplication.utils.TextUtil;


/**
 * 弹框：复制本地视频
 */
@SuppressLint("CutPasteId") 
public class VideoManagerCopyDialog extends BaseDialog implements View.OnClickListener {

	public interface Callback {

		void onCall(DialogInterface dialog, String videoName);
	}

	private TextView cancel, confirm;
	private EditText edit;
	private TextView title;
	private Callback listener;
	private String videoTitle;

	public String getEdit() {
		if (edit.getText() != null)
			return edit.getText().toString();
		return "";
	}

	public VideoManagerCopyDialog(Context context,
								  String videoTitle,
								  Callback listener) {
		super(context);
		this.videoTitle = videoTitle;
		this.listener = listener;

		cancel = (TextView) findViewById(R.id.videomanagercopy_cancel);
		confirm = (TextView) findViewById(R.id.videomanagercopy_confirm);

		title = (TextView) findViewById(R.id.videomanagercopy_title);
		edit = (EditText) findViewById(R.id.videomanagercopy_edit);

		cancel.setOnClickListener(this);
		confirm.setOnClickListener(this);

		if (videoTitle != null)
			title.setText(Html.fromHtml("复制视频" + TextUtil.toColor("'" + videoTitle + "'", "#fc3c2d")));

		edit.setText(videoTitle + "_tmp");
	}

	@Override
	protected int getContentView() {
		return R.layout.dialog_videomanagercopy;
	}

	@Override
	public void onClick(View v) {

		if (v == confirm) {
			if (listener != null)
				listener.onCall(this, getEdit());
		} else {
			dismiss();
		}
	}
}
