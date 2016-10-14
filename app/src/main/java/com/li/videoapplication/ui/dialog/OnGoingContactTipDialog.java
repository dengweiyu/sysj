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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.BaseEmptyDialog;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.views.TipView2;

/**
 * 弹框：进行中约战遮罩
 */
@SuppressLint("CutPasteId")
public class OnGoingContactTipDialog extends BaseEmptyDialog implements View.OnClickListener {

    private static final String TAG = OnGoingContactTipDialog.class.getSimpleName();
//    private int btnLeft, btnTop;

    @Override
    protected int getContentView() {
        return R.layout.tip_ongoing_contact2;
    }

    public OnGoingContactTipDialog(Context context/*, int viewLeft, int viewTop, int btnLeft, int btnTop*/) {
        super(context);
//        this.btnLeft = btnLeft;
//        this.btnTop = btnTop;

        findViewById(R.id.tip_text).setOnClickListener(this);
//        ImageView tip_top = (ImageView) findViewById(R.id.tip_top);
//        ImageView tip_left = (ImageView) findViewById(R.id.tip_left);

//        int screenWidth = ScreenUtil.getScreenWidth();

//        ViewGroup.LayoutParams params = tip_top.getLayoutParams();
//        params.width = screenWidth;
//        params.height = viewTop;
//        tip_top.setLayoutParams(params);
//
//        ViewGroup.LayoutParams lp = tip_left.getLayoutParams();
//        lp.width = viewLeft;
//        lp.height = ScreenUtil.dp2px(30);
//        tip_left.setLayoutParams(lp);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tip_text:
                dismiss();
                showImageUploadTipDialog();
                break;
        }
    }

    /**
     * 遮罩提示页：上传截图
     */
    private void showImageUploadTipDialog() {

//        boolean tip = NormalPreferences.getInstance().getBoolean(Constants.TIP_ONGOING_UPLOADIMAGE, true);
//        if (tip) {
        DialogManager.showOnGoingUploadImageTipDialog(AppManager.getInstance().currentActivity()/*,btnLeft,btnTop*/);
//            NormalPreferences.getInstance().putBoolean(Constants.TIP_ONGOING_UPLOADIMAGE, false);
//        }
    }
}
