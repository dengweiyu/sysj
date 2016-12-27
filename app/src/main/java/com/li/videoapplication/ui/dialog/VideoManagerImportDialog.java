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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.framework.BaseDialog;
import com.li.videoapplication.ui.adapter.VideoManagerImportAdapter;
import com.li.videoapplication.ui.fragment.MyLocalVideoFragment;

import java.util.List;

/**
 * 弹框：导入外部视频
 */
@SuppressLint("CutPasteId") 
public class VideoManagerImportDialog extends BaseDialog implements View.OnClickListener {

	private TextView cancel, confirm;
	private ListView listView;
	private DialogInterface.OnClickListener listener;
	private List<VideoCaptureEntity> data;
	private VideoManagerImportAdapter adapter;
	private MyLocalVideoFragment fragment;

	public VideoManagerImportDialog(Context context,
									List<VideoCaptureEntity> data,
									MyLocalVideoFragment fragment,
									DialogInterface.OnClickListener listener) {
		super(context);
		this.data = data;
		this.fragment = fragment;
		this.listener = listener;

		cancel = (TextView) findViewById(R.id.videomanagerimport_cancel);
		confirm = (TextView) findViewById(R.id.videomanagerimport_confirm);

		cancel.setOnClickListener(this);
		confirm.setOnClickListener(this);

		listView = (ListView) findViewById(R.id.listview);

		if (data != null) {
			adapter = new VideoManagerImportAdapter(getContext(), listView, data, fragment);
			listView.setAdapter(adapter);
		}
	}

	@Override
	protected int getContentView() {
		return R.layout.dialog_videomanagerimport;
	}

	@Override
	public void onClick(View v) {

		if (v == confirm) {
			if (listener != null)
				listener.onClick(this, v.getId());
		}
		dismiss();
	}
}
