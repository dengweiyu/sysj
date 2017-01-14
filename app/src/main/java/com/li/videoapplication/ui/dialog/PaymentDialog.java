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
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.response.PaymentEntity;
import com.li.videoapplication.data.model.response.PhoneRequestMsgEntity;
import com.li.videoapplication.data.model.response.UserProfileFinishMemberInfoEntity;
import com.li.videoapplication.data.model.response.VerifyCodeNewEntity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.BaseDialog;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.utils.CountDownTimerUtils;
import com.li.videoapplication.utils.InputUtil;
import com.li.videoapplication.utils.PatternUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;

import io.rong.eventbus.EventBus;

/**
 * 弹框：兑换
 */
@SuppressLint("CutPasteId")
public class PaymentDialog extends BaseDialog implements View.OnClickListener {

    private static final int VERIFY_PHONE = 1;
    private static final int EXCHANGE = 2;

    private int viewType;
    private Activity activity;
    private Currency childList;
    private EditText phone, code, qq;
    private TextView title, getCode, confirm;
    private Member member;
    private View codeView;

    public PaymentDialog(Context context, Currency childList) {
        super(context);
        //先执行super，所以 afterContentView()方法中的 childList仍=null，因此赋值操作放在下面的initData()中执行
        this.childList = childList;

        try {
            activity = (Activity) context;
        } catch (Exception e) {
            e.printStackTrace();
        }

        initData();
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_payment;
    }

    @Override
    protected void afterContentView(Context context) {
        super.afterContentView(context);
        title = (TextView) findViewById(R.id.payment_text);
        getCode = (TextView) findViewById(R.id.payment_getcode);
        qq = (EditText) findViewById(R.id.payment_qq);
        phone = (EditText) findViewById(R.id.payment_phone);
        code = (EditText) findViewById(R.id.payment_code);
        codeView = findViewById(R.id.payment_codeview);
        confirm = (TextView) findViewById(R.id.payment_confirm);

        getCode.setOnClickListener(this);
        confirm.setOnClickListener(this);
        findViewById(R.id.payment_delete).setOnClickListener(this);

        phone.requestFocus();
        setCanceledOnTouchOutside(false);

    }

    private void initData() {
        member = PreferencesHepler.getInstance().getUserProfilePersonalInformation();
        if (StringUtil.isNull(member.getMobile())) {
            switchView(VERIFY_PHONE);
        } else {
            switchView(EXCHANGE);
        }
    }

    private void switchView(int viewType) {
        this.viewType = viewType;
        if (viewType == VERIFY_PHONE) {
            phone.setVisibility(View.VISIBLE);
            codeView.setVisibility(View.VISIBLE);

            String s = "领取礼物之前为了您的账号安全，需要先进行手机验证";
            setTextViewText(title, s);
            setTextViewText(confirm, "立即验证");
        } else if (viewType == EXCHANGE) {
            phone.setVisibility(View.GONE);
            codeView.setVisibility(View.GONE);

            String s = "确认兑换 [ " + childList.getName() + " ] 兑换成功将扣除 " + childList.getCurrency_num()
                    + " 飞磨豆。(" + TextUtil.toColor("兑换成功可在兑换记录查看", "#fe5e5e") + ")";
            title.setText(Html.fromHtml(s));
            setTextViewText(confirm, "确认兑换");

            if (childList.getExchange_way().equals("3") ||  //3=>Q币类
                    childList.getExchange_way().equals("6")) { //6=>战网兑换类
                qq.setVisibility(View.VISIBLE);
                if (!StringUtil.isNull(childList.getPlaceholder()))
                    qq.setHint(childList.getPlaceholder());
                if (childList.getExchange_way().equals("6"))
                    qq.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            } else {
                qq.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.payment_getcode:
                if (checkPhoneNumber()) break;
                // 获取验证码
                DataManager.phoneRequestMsg(getPhoneText());
                break;

            case R.id.payment_confirm:
                if (viewType != 0) {
                    if (viewType == VERIFY_PHONE) {
                        verifyCodeNew();
                    } else if (viewType == EXCHANGE) {
                        if (childList.getExchange_way().equals("3") ||  //3=>Q币类
                                childList.getExchange_way().equals("6")) { //6=>战网兑换类
                            DataManager.payment(PreferencesHepler.getInstance().getMember_id(),
                                    childList.getId(), getPhoneText(), getQQText());
                        } else {// 2=>话费流量类, 4=>京东卡类
                            DataManager.payment(PreferencesHepler.getInstance().getMember_id(),
                                    childList.getId(), getPhoneText());
                        }
                    }
                }
                break;

            case R.id.payment_delete:
                dismiss();
                break;
        }
    }

    /**
     * 获取验证码60s计时
     */
    private void startCountDown() {
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

    private String getQQText() {
        if (qq.getText() != null)
            return qq.getText().toString();
        return "";
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
            showToastShort(event.getMsg());
            if (event.isResult()) {// 成功
                // 获取验证码计时
                startCountDown();
                code.requestFocus();
            }
        }
    }

    /**
     * 回调:提交手机和验证码
     */
    public void onEventMainThread(VerifyCodeNewEntity event) {

        if (event != null) {
            if (event.isResult()) {// 验证成功
                Member newMember = (Member) member.clone();
                newMember.setMobile(getPhoneText());
                // 编辑个人资料
                DataManager.userProfileFinishMemberInfo(newMember);
            } else {
                ToastHelper.s("验证码错误");
            }
        }
    }

    /**
     * 回调：编辑个人资料
     */
    public void onEventMainThread(UserProfileFinishMemberInfoEntity event) {

        if (event != null) {
            if (event.isResult()) {
                switchView(EXCHANGE);
            }
        }
    }

    /**
     * 回调:兑换
     */
    public void onEventMainThread(PaymentEntity event) {

        if (event != null) {
            if (event.isResult()) {// 兑换成功
                ToastHelper.s("兑换申请提交成功，请到兑换记录查询进度");
                String member_id = PreferencesHepler.getInstance().getMember_id();
                DataManager.userProfilePersonalInformation(member_id, member_id);
                dismiss();
            } else {
                ToastHelper.s(event.getMsg());
            }
        }
    }
}
