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
import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.component.service.UpdateService;
import com.li.videoapplication.data.model.entity.Update;
import com.li.videoapplication.framework.BaseDialog;
import com.li.videoapplication.tools.DownloadHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.utils.ScreenUtil;

import cn.jpush.android.service.DownloadService;


/**
 * 弹框：版本更新
 */
@SuppressLint("CutPasteId")
public class UpdateDialog extends BaseDialog implements View.OnClickListener {

    private TextView confirm;
    private Update update;

    @Override
    protected int getContentView() {
        return R.layout.dialog_update;
    }

    public UpdateDialog(Context context, Update update) {
        super(context);
        setCanceledOnTouchOutside(false);

        this.update = update;

        confirm = (TextView) findViewById(R.id.update_now);
        TextView cancel = (TextView) findViewById(R.id.update_cancel);
        TextView version = (TextView) findViewById(R.id.update_version);
        TextView content = (TextView) findViewById(R.id.update_message);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);

        ImageView header = (ImageView) findViewById(R.id.update_header);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) header.getLayoutParams();
        int screenWidth = ScreenUtil.getScreenWidth();
        int margin = ScreenUtil.dp2px(80);
        int width = screenWidth - margin;
        params.width = width;
        params.height = width / 3;
        header.setLayoutParams(params);

        setTextViewText(version, update.getVersion_str());

        String changelog = update.getChange_log();

        if ("U".equals(update.getUpdate_flag())) {// 可用升级
            String message = "新版特性：\n" + changelog;
            content.setText(message);
        } else if ("A".equals(update.getUpdate_flag())) {// 强制升级
            String message = "手游视界" + update.getVersion_str() + "\n更新日志：\n" + changelog;
            content.setText(message);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == confirm) {
            downloadAPK();
        }
        dismiss();
    }

    private void downloadAPK() {
        if (!NetUtil.isConnect()) {
            ToastHelper.s(R.string.net_disable);
        } else if (NetUtil.isWIFI()) {
            // 开始, 继续下载
            UpdateService.startUpdateService(update);
        } else {
            // 文件下载
            DialogManager.showFileDownloaderDialog(mContext,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 开始, 继续下载
                            UpdateService.startUpdateService(update);
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // WIFI下载
                            mContext.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    });
        }
    }
}
