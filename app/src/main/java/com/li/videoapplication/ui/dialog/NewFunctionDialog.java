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
import android.widget.ImageView;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.BaseDialog;
/**
 * 弹框：新功能
 */
@SuppressLint("CutPasteId") 
public class NewFunctionDialog extends BaseDialog implements android.view.View.OnClickListener {
	
	private ImageView imageView;
	
	public NewFunctionDialog(Context context) {
		super(context);
	}

	@Override
	protected int getContentView() {
		return R.layout.dialog_mytask_grouwup;
	}
	
	@Override
	protected void afterContentView(Context context) {
		super.afterContentView(context);
		
		imageView = (ImageView) findViewById(R.id.mytask_delete);
		imageView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		
		case R.id.mytask_delete:
			dismiss();
			break;

		default:
			break;
		}
	}
}
