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
import android.view.View;

import com.fmsysj.screeclibinvoke.utils.RootUtil;
import com.ifeimo.screenrecordlib.RecordingManager;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.BaseTopDialog;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.activity.GroupDetailActivity;
import com.li.videoapplication.ui.activity.VideoShareActivity;
import com.li.videoapplication.utils.AppUtil;

/**
 * 弹框：游戏详情
 */
@SuppressLint("CutPasteId")
public class GameDetailDialog extends BaseTopDialog implements View.OnClickListener {

    private Activity activity;
    private Game game;
    private boolean isLogin;

    /**
     * 跳转：登录
     */
    private void startLoginDialog() {
        DialogManager.showLogInDialog(activity);
    }

    /**
     * 跳转：外拍（5.0以上）
     */
    private void startCameraRecoed50Activity() {
        ActivityManeger.startCameraRecoed50Activity(getContext(), game);
    }

    /**
     * 跳转：外拍（5.0以下）
     */
    private void startCameraRecoedActivity() {
        ActivityManeger.startCameraRecoedActivity(getContext(), game);
    }

    /**
     * 跳转：图文分享
     */
    private void startHomeImageShareActivity() {
        ActivityManeger.startHomeImageShareActivity(getContext(), game, true);
    }

    /**
     * 跳转：视频管理
     */
    private void startVideoMangerActivity() {
        ActivityManeger.startVideoMangerActivity(getContext(), game);
    }

    /**
     * 跳转：选择上传视频
     */
    private void startVideoChooseActivity() {
        if (game != null)
            ActivityManeger.startVideoChooseActivity(getContext(), game);
        UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.GAME, "游戏圈-本地上传");
    }

    public GameDetailDialog(Context context, Game game) {
        super(context);
        this.game = game;
        try {
            activity = (Activity) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                if (isLogin) {
                    startVideoChooseActivity();
                } else {
                    startLoginDialog();
                }
                break;

            case R.id.gamedetail_video://录屏
                GroupDetailActivity activity = (GroupDetailActivity) AppManager.getInstance().getActivity(GroupDetailActivity.class);
                if (activity != null && game != null)
                    activity.startScreenRecordActivity();
                UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.GAME, "游戏圈-录制一段视频");
                break;

            case R.id.gamedetail_record://外拍
                if (RecordingManager.getInstance().isRecording()) {
                    ToastHelper.s("正在录屏中");
                    return;
                }
                if (isLogin) {
                    if (game != null) {
                        int SDKVesion = AppUtil.getAndroidSDKVersion();
                        // FIXME: CameraGLView第565行，setPreviewSize（预览宽高有几个固定比例等级），
                        // FIXME: 华为的垃圾机屏幕分辨率不是正常比例来的(因为有条垃圾返回键等底栏) 所以会抛异常。没空先不给华为进入先，看到的大神有空修一下
                        if (SDKVesion >= 21 && !RootUtil.getManufacturer().equals("HUAWEI")) {
                            startCameraRecoed50Activity();
                        } else {
                            startCameraRecoedActivity();
                        }
                    }
                    UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.GAME, "游戏圈-视频拍摄");
                } else {
                    startLoginDialog();
                }
                break;

            case R.id.gamedetail_image://图文
                if (isLogin) {
                    if (game != null)
                        startHomeImageShareActivity();
                    UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.GAME, "游戏圈-发表图文");
                } else {
                    startLoginDialog();
                }
                break;
        }
        cancel();
    }
}
