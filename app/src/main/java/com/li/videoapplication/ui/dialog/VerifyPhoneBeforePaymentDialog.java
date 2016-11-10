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
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.data.model.response.PaymentEntity;
import com.li.videoapplication.data.model.response.PhoneRequestMsgEntity;
import com.li.videoapplication.data.model.response.RecommendedLocationEntity;
import com.li.videoapplication.data.model.response.VerifyCodeNewEntity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.BaseDialog;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.toast.ToastHelper;
import com.li.videoapplication.utils.CountDownTimerUtils;
import com.li.videoapplication.utils.InputUtil;
import com.li.videoapplication.utils.PatternUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;

import io.rong.eventbus.EventBus;

/**
 * 弹框：支付前验证手机号
 */
@SuppressLint("CutPasteId")
public class VerifyPhoneBeforePaymentDialog extends BaseDialog implements View.OnClickListener {

    private Activity activity;
    private VideoCaptureEntity entity;
    private RecommendedLocationEntity event;
    private EditText phone, code;
    private TextView getCode;

    public VerifyPhoneBeforePaymentDialog(Context context, VideoCaptureEntity entity, RecommendedLocationEntity event) {
        super(context);
        this.entity = entity;
        this.event = event;
        try {
            activity = (Activity) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_verifyphone_payment;
    }

    @Override
    protected void afterContentView(Context context) {
        super.afterContentView(context);
        getCode = (TextView) findViewById(R.id.payment_getcode);
        phone = (EditText) findViewById(R.id.payment_phone);
        code = (EditText) findViewById(R.id.payment_code);

        getCode.setOnClickListener(this);
        findViewById(R.id.payment_delete).setOnClickListener(this);
        findViewById(R.id.payment_confirm).setOnClickListener(this);

        phone.requestFocus();
        setCanceledOnTouchOutside(false);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.payment_getcode:
                // 获取验证码
                msgRequestNew();
                code.requestFocus();
                break;

            case R.id.payment_confirm:
                verifyCodeNew();
                break;

            case R.id.payment_delete:
                dismiss();
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void msgRequestNew() {
        if (checkPhoneNumber()) return;
        // 获取验证码
        DataManager.sendCode(getPhoneText());

        CountDownTimerUtils countDownTimer = new CountDownTimerUtils(getCode, 60000, 1000,
                R.drawable.dialog_registermobile_gray, R.drawable.dialog_registermobile_red);
        countDownTimer.start();
    }

    /**
     * 提交手机和验证码
     */
    private void verifyCodeNew() {
        if (checkPhoneNumber()) return;
        if (StringUtil.isNull(getCodeText())) {
            showToastShort("验证码不能为空");
            return;
        }
        // 提交手机和验证码
        DataManager.verifyCode(getPhoneText(), getCodeText());
    }

    private boolean checkPhoneNumber() {
        if (StringUtil.isNull(getPhoneText())) {
            showToastShort("手机号码不能为空");
            return true;
        }
        if (!PatternUtil.isMatchMobile(getPhoneText())) {
            showToastShort("请输入正确的手机号");
            return true;
        }
        return false;
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

    private String getPhoneText() {
        if (phone.getText() != null)
            return phone.getText().toString();
        return "";
    }

    private String getCodeText() {
        if (code.getText() != null)
            return code.getText().toString();
        return "";
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 回调:取验证码
     */
    public void onEventMainThread(PhoneRequestMsgEntity event) {

        if (event != null) {
            boolean result = event.isResult();
            String msg = event.getMsg();
            if (result) {// 成功
                showToastShort(msg);
            }
        }
    }

    /**
     * 回调:提交手机和验证码
     */
    public void onEventMainThread(VerifyCodeNewEntity event) {

        if (event != null) {
            if (event.isResult()) {// 验证成功
                dismiss();
                DialogManager.showOfficialPaymentDialog(getContext(), entity, this.event);
            } else {
                ToastHelper.s("验证码错误");
            }
        }
    }
}
