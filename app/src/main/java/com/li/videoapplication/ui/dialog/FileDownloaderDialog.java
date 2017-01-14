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
import com.li.videoapplication.framework.BaseDialog;

/**
 * 弹框：文件下载
 */
@SuppressLint("CutPasteId") 
public class FileDownloaderDialog extends BaseDialog implements View.OnClickListener {

	private TextView wifi, confirm;
	private View.OnClickListener continueListener, wifiListener;

	@Override
	protected int getContentView() {
		return R.layout.dialog_filedownloader;
	}

	public FileDownloaderDialog(Context context, View.OnClickListener continueListener, View.OnClickListener wifiListener) {
		super(context);
		this.continueListener = continueListener;
		this.wifiListener = wifiListener;

		wifi = (TextView) findViewById(R.id.filedownloader_wifi);
		confirm = (TextView) findViewById(R.id.filedownloader_continue);

		wifi.setOnClickListener(this);
		confirm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == confirm && continueListener != null) {
			continueListener.onClick(v);
		} else if (v == wifi && wifiListener != null) {
			wifiListener.onClick(v);
		}
		dismiss();
	}
}
