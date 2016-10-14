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
import com.li.videoapplication.framework.BaseBottomDialog;
/**
 * 弹框：相册
 */
@SuppressLint("CutPasteId")
public class PhotoDialog extends BaseBottomDialog implements View.OnClickListener {
	
	private TextView pick, take, cancel;
	
	private View.OnClickListener pickListener, takeListener;

	public PhotoDialog(Context context, View.OnClickListener pickClickListener, View.OnClickListener takeClickListener) {
		super(context);
		this.pickListener = pickClickListener;
		this.takeListener = takeClickListener;
	}

	@Override
	protected int getContentView() {
		return R.layout.dialog_photo;
	}

	@Override
	protected void afterContentView(Context context) {
		super.afterContentView(context);
		
		pick = (TextView) findViewById(R.id. photo_pick);
		take = (TextView) findViewById(R.id.photo_take);
		cancel = (TextView) findViewById(R.id.photo_cancel);

		pick.setOnClickListener(this);
		take.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.photo_pick:
			if (pickListener != null) {
				pickListener.onClick(v);
			}
			cancel();
			break;

		case R.id.photo_take:
			if (takeListener != null) {
				takeListener.onClick(v);
			}
			cancel();
			break;

		case R.id.photo_cancel:
			cancel();
			break;

		default:
			break;
		}
	}
}
