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
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fmsysj.zbqmcs.utils.RecordVideo;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.BaseTopDialog;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.GroupDetailActivity;
import com.li.videoapplication.ui.toast.ToastHelper;

/**
 * 弹框：游戏详情
 */
@SuppressLint("CutPasteId")
public class GameDetailDialog extends BaseTopDialog implements View.OnClickListener {

    private Game game;
    private boolean isLogin;

    /**
     * 跳转：登录
     */
    public void startLoginActivity() {
        ActivityManeger.startLoginActivity(getContext());
    }

    public GameDetailDialog(Context context, Game game) {
        super(context);
        this.game = game;
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_gamedetail;
    }

    @Override
    protected void afterContentView(Context context) {
        super.afterContentView(context);
        isLogin = PreferencesHepler.getInstance().isLogin();

        findViewById(R.id.gamedetail_delete).setOnClickListener(this);
        findViewById(R.id.gamedetail_local).setOnClickListener(this);
        findViewById(R.id.gamedetail_video).setOnClickListener(this);
        findViewById(R.id.gamedetail_image).setOnClickListener(this);
        findViewById(R.id.gamedetail_record).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.gamedetail_local://视频
                if (game != null)
                    ActivityManeger.startVideoMangerActivity(getContext(), game);
                UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.GAME, "游戏圈-本地上传");
                break;

            case R.id.gamedetail_video://录屏
                if (isLogin) {
                    GroupDetailActivity activity = (GroupDetailActivity) AppManager.getInstance().getActivity(GroupDetailActivity.class);
                    if (activity != null && game != null)
                        activity.startScreenRecordActivity();
                    UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.GAME, "游戏圈-录制一段视频");
                } else {
                    startLoginActivity();
                }
                break;

            case R.id.gamedetail_record://外拍
                if (RecordVideo.isRecordering) {
                    ToastHelper.s("正在录屏中");
                    return;
                }
                if (isLogin) {
                    if (game != null)
                        ActivityManeger.startCameraRecoedActivity(getContext(), game);
                    UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.GAME, "游戏圈-视频拍摄");
                } else {
                    startLoginActivity();
                }
                break;

            case R.id.gamedetail_image://图文
                if (isLogin) {
                    if (game != null)
                        ActivityManeger.startHomeImageShareActivity(getContext(), game, true);
                    UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.GAME, "游戏圈-发表图文");
                } else {
                    startLoginActivity();
                }
                break;
        }
        cancel();
    }
}
