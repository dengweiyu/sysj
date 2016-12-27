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
import android.widget.LinearLayout;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.BaseDialog;
import com.li.videoapplication.tools.ToastHelper;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

/**
 * 弹框：分享
 */
@SuppressLint("CutPasteId") 
public class ShareDialog extends BaseDialog implements View.OnClickListener {

	private LinearLayout wx, wxfriends, qq, qzone, wb;

	private String videoUrl, imageUrl, VideoTitle, text;

	public ShareDialog(Context context, String videoUrl, String imageUrl, String VideoTitle, String text) {
		super(context);
		this.videoUrl = videoUrl;
		this.imageUrl = imageUrl;
		this.VideoTitle = VideoTitle;
		this.text = text;
	}

	@Override
	protected int getContentView() {
		return R.layout.dialog_share;
	}
	
	@Override
	protected void afterContentView(Context context) {
		super.afterContentView(context);

		wx = (LinearLayout) findViewById(R.id.share_wx);
		wxfriends = (LinearLayout) findViewById(R.id.share_wxfriends);
		qq = (LinearLayout) findViewById(R.id.share_qq);
		qzone = (LinearLayout) findViewById(R.id.share_qzone);
		wb = (LinearLayout) findViewById(R.id.share_wb);

		wx.setOnClickListener(this);
		wxfriends.setOnClickListener(this);
		qq.setOnClickListener(this);
		qzone.setOnClickListener(this);
		wb.setOnClickListener(this);


	}

	@Override
	public void onClick(View v) {

		if (v == wx) {
			share(VideoTitle, videoUrl, text, imageUrl, "Wechat");
		} else if (v == qq) {
			share(VideoTitle, videoUrl, text, imageUrl, "QQ");
		} else if (v == wxfriends) {
			share(VideoTitle, videoUrl, text, imageUrl, "WechatMoments");
		} else if (v == wb) {
			share(VideoTitle, videoUrl, text, imageUrl, "SinaWeibo");
		} else if (v == qzone) {
			share(VideoTitle, videoUrl, text, imageUrl, "QZone");
		}
		dismiss();
	}

	public void share(String title, String url, String text, String imageUrl, final String shareChannel) {
		Platform.ShareParams sp = new Platform.ShareParams();

		// 新浪微博文字内容无法携带超链接， 所以将超链接直接放在分享内容中
		if (shareChannel.equals("SinaWeibo")) {
			sp.setText(text + url);
		}
		sp.setImageUrl(imageUrl);
		sp.setTitle(title);
		sp.setText(text);
		sp.setUrl(url);
		sp.setTitleUrl(url);
		sp.setShareType(Platform.SHARE_WEBPAGE);

		if (!shareChannel.equals("SYSJ")) {
			Platform plat = null;
			// 根据sharchannel值进行所选平台的分享
			plat = ShareSDK.getPlatform(getContext(), shareChannel);
			if (listener != null) {
				plat.setPlatformActionListener(listener);
			}
			plat.share(sp);
		}
	}

	private PlatformActionListener listener = new PlatformActionListener() {

		@Override
		public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

		}

		@Override
		public void onError(Platform platform, int i, Throwable throwable) {
			ToastHelper.s("分享有误，请检查后重试");
		}

		@Override
		public void onCancel(Platform platform, int i) {
		}
	};
}
