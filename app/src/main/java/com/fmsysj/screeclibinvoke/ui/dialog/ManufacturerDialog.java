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

import com.fmsysj.screeclibinvoke.utils.RootUtil;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.BaseDialog;
import com.li.videoapplication.tools.IntentHelper;

/**
 * 弹框：悬浮窗设置
 */
@SuppressLint("CutPasteId") 
public class ManufacturerDialog extends BaseDialog implements View.OnClickListener {

	private TextView cancel, confirm, content;
	private String manufacturer;

	public ManufacturerDialog(Context context) {
		super(context);

		cancel = (TextView) findViewById(R.id.manufacturer_cancel);
		confirm = (TextView) findViewById(R.id.manufacturer_confirm);
		content = (TextView) findViewById(R.id.manufacturer_content);

		cancel.setOnClickListener(this);
		confirm.setOnClickListener(this);

		manufacturer = RootUtil.getManufacturer();
		if (manufacturer.equals("HUAWEI")) {
			content.setText(R.string.manufacturer_content_huawei);
		} else if (manufacturer.equals("MIUI")) {
			content.setText(R.string.manufacturer_content_miui);
		} else if (manufacturer.equals("oppo")) {
			content.setText(R.string.manufacturer_content_oppo);
		} else if (manufacturer.equals("MEIZU")) {
			content.setText(R.string.manufacturer_content_meizu);
		} else {
			dismiss();
		}
	}

	@Override
	protected int getContentView() {
		return R.layout.dialog_manufacturer;
	}

	@Override
	public void onClick(View v) {

		if (v == confirm) {
			if (manufacturer.equals("MIUI")) {//小米
				IntentHelper.openFloatWindiws_XiaoMi();
			} else if (manufacturer.equals("HUAWEI")) {// 华为
				IntentHelper.openFloatWindiws_HuaWei();
			} else if (manufacturer.equals("MEIZU")) {// 魅族
				IntentHelper.openFloatWindiws_Meizu();
			}
		}
		dismiss();
	}
}
