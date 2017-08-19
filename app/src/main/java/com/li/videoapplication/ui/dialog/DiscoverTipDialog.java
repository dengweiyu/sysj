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
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.BaseEmptyDialog;
import com.li.videoapplication.utils.ScreenUtil;

/**
 * 弹框：发现遮罩
 */
@SuppressLint("CutPasteId")
public class DiscoverTipDialog extends BaseEmptyDialog implements View.OnClickListener {

	private ImageView known;

	@Override
	protected int getContentView() {
		return R.layout.tip_discover;
	}
	
	public DiscoverTipDialog(Context context) {
		super(context);

		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();


		params.height = (ScreenUtil.getScreenHeight()); // 设置宽度

		window.setAttributes(params);

		findViewById(R.id.ll_discover_tip_root).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.ll_discover_tip_root:
				dismiss();
				break;
		}
	}
}
