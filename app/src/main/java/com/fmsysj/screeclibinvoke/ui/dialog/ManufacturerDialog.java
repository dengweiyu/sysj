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
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.fmsysj.screeclibinvoke.logic.floatview.FloatViewManager;
import com.fmsysj.screeclibinvoke.logic.screenrecord.RecordingService;
import com.fmsysj.screeclibinvoke.utils.RootUtil;
import com.li.videoapplication.R;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.BaseDialog;
import com.li.videoapplication.tools.IntentHelper;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.utils.TextUtil;


/**
 * 弹框：悬浮窗设置
 */
@SuppressLint("CutPasteId") 
public class ManufacturerDialog extends BaseDialog implements View.OnClickListener {
	public static final int TYPE_SETTING_BUTTON = 1;
	public static final int TYPE_RECORD_BUTTON = 2;


	private int type;

	private TextView cancel, confirm, content;
	private String manufacturer;

	private Context context;

	public ManufacturerDialog(Context context, int type) {
		super(context);

		this.context = context;
		this.type = type;

		cancel = (TextView) findViewById(R.id.manufacturer_cancel);
		confirm = (TextView) findViewById(R.id.manufacturer_confirm);
		content = (TextView) findViewById(R.id.manufacturer_content);

		cancel.setOnClickListener(this);
		confirm.setOnClickListener(this);

		manufacturer = RootUtil.getManufacturer();

		if (type == TYPE_RECORD_BUTTON) {
			content.setText(Html.fromHtml("悬浮窗录屏模式需要获得" +
					TextUtil.toColor("悬浮窗权限", "#ff0000") +
					"！"));
			cancel.setText("使用无浮窗模式");
			cancel.setTextColor(0xff2588ff);
		} else if (type == TYPE_SETTING_BUTTON){
			content.setText(Html.fromHtml(manufacturer + "机型下" +
					TextUtil.toColor("回桌面", "#ff0000") +
					"悬浮窗会" +
					TextUtil.toColor("被系统隐藏", "#ff0000") +
					"，需要给与悬浮窗权限才可以正常在桌面显示"));
			cancel.setText("取消");
			cancel.setTextColor(0x88787878);
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
			} else if (manufacturer.equals("OPPO")){
				IntentHelper.openFloatWindiws_Oppo();
			} else if (manufacturer.equals("VIVO")) {
				IntentHelper.openFloatWindiws_Vivo();
			} else {
				IntentHelper.openNormalPermission();
			}
		} else if (v == cancel){
			// 自动设置为无悬浮窗模式
			if (type == TYPE_RECORD_BUTTON) {
				PreferencesHepler.getInstance().saveRecordingSettingFloatingWindiws(false);
				RecordingService.toogleFloatView(false);
				FloatViewManager.getInstance().isFloatingWindiws = false;

				DialogManager.showCountDownDialog(context);
			}
		}
		dismiss();
	}
}
