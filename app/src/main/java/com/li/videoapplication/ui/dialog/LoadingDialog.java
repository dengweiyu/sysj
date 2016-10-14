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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.BaseDialog;
/**
 * 弹框：加载
 */
@SuppressLint("CutPasteId") 
public class LoadingDialog extends BaseDialog {

	public static String VERIFYING = "验证中...";
	public static String LOADING = "加载中...";
	public static String UPLOADING = "上传中...";
	public static String LOHIN = "登录中...";

	private TextView textView;
	private ImageView imageView;
	
	public LoadingDialog(Context context) {
		super(context);
	}
	
	@Override
	protected void afterContentView(Context context) {
		super.afterContentView(context);
		
		textView = (TextView) findViewById(R.id.msg);
		imageView = (ImageView) findViewById(R.id.progress);
		
		setTextViewTextVisibility(textView, "");
	}

	@Override
	protected int getContentView() {
		return R.layout.dialog_loading;
	}

	public final void setProgressText(String text) {
		setTextViewTextVisibility(textView, text);
	}

	public final void dismiss() {
		super.dismiss();
		imageView.clearAnimation();
	}
	
	@Override
	public void cancel() {
		super.cancel();
		imageView.clearAnimation();
	}

	public final void show() {
		super.show();
		Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_loading);
		imageView.startAnimation(loadAnimation);
	}
}
