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

import com.li.videoapplication.R;
import com.li.videoapplication.framework.BaseEmptyDialog;

/**
 * 弹框：进行中上传图片遮罩
 */
@SuppressLint("CutPasteId")
public class OnGoingUploadImageTipDialog extends BaseEmptyDialog implements View.OnClickListener {


    @Override
    protected int getContentView() {
        return R.layout.tip_ongoing_uploadiamge2;
    }

    public OnGoingUploadImageTipDialog(Context context) {
        super(context);
        findViewById(R.id.tip_text).setOnClickListener(this);
//        ImageView bottom = (ImageView) findViewById(R.id.tip_bottom);
//
//        int screenHeight = ScreenUtil.getScreenHeight();
//        int screenWidth = ScreenUtil.getScreenWidth();
//        int btnHeight = ScreenUtil.dp2px(38 + 32);
//
//        ViewGroup.LayoutParams params = bottom.getLayoutParams();
//        params.height = screenHeight - btnTop - btnHeight;
//        params.width = screenWidth;
//        bottom.setLayoutParams(params);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tip_text:
                dismiss();
                break;
        }
    }
}
