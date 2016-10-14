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
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.BaseDialog;
import com.li.videoapplication.utils.InputUtil;


/**
 * 弹框：重命名本地视频，截图
 */
@SuppressLint("CutPasteId") 
public class VideoManagerRenameDialog extends BaseDialog implements View.OnClickListener {

	private Activity activity;

	private TextView cancel, confirm;
	private EditText edit;
	private Callback listener;
	private String name;
	private boolean isFirstDel = false;

	public String getEdit() {
		if (edit.getText() != null)
			return edit.getText().toString();
		return "";
	}

	public VideoManagerRenameDialog(Context context,
									String oldFileName,
									Callback listener) {
		super(context);
		this.name = oldFileName;
		this.listener = listener;

		try {
			activity = (Activity) context;
		} catch (Exception e) {
			e.printStackTrace();
		}

		cancel = (TextView) findViewById(R.id.videomanagerrename_cancel);
		confirm = (TextView) findViewById(R.id.videomanagerrename_confirm);

		edit = (EditText) findViewById(R.id.videomanagerrename_edit);
		edit.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if(keyCode == KeyEvent.KEYCODE_DEL &&
						event.getAction() == KeyEvent.ACTION_DOWN){
					if (!isFirstDel) {
						if (edit.getText() != null && edit.getText().toString().length() > 0) {
							while (edit.getText().toString().length() > 0) {
								int length = edit.getText().toString().length();
								try {
									edit.getText().delete(length - 1, length);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						isFirstDel = true;
						return true;
					}
				}
				return false;
			}
		});

		cancel.setOnClickListener(this);
		confirm.setOnClickListener(this);

		if (name != null)
			edit.setText(name);

		if (edit.getText() != null && edit.getText().toString().length() > 0) {
			edit.setSelection(edit.getText().toString().length());
		}

		try {
			InputUtil.showKeyboard(edit);
			InputUtil.restartKeyboard(edit);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected int getContentView() {
		return R.layout.dialog_videomanagerrename;
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

	@Override
	public void dismiss() {
		try {
			InputUtil.closeKeyboard(activity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.dismiss();
	}

	public interface Callback {
		void onCall(DialogInterface dialog, String newFileName);
	}
}
