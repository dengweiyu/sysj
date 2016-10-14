package com.li.videoapplication.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * 功能：倒计时
 */
public class CountDownTimerUtils extends CountDownTimer {
    private TextView countDownTextView;
    private int countingRes, normalRes;

    /*
    * millisInFuture:总时间
    * countDownInterval:时间间隔
    */
    public CountDownTimerUtils(TextView countDownTextView, long millisInFuture, long countDownInterval,
                               int countingRes, int normalRes) {

        super(millisInFuture, countDownInterval);

        this.countDownTextView = countDownTextView;
        this.countingRes = countingRes;
        this.normalRes = normalRes;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        countDownTextView.setText("重新发送(" + millisUntilFinished / 1000 + ")");
        countDownTextView.setFocusable(false);
        countDownTextView.setClickable(false);
        countDownTextView.setBackgroundResource(countingRes);
    }

    @Override
    public void onFinish() {
        countDownTextView.setFocusable(true);
        countDownTextView.setClickable(true);
        countDownTextView.setText("获取验证码");
        countDownTextView.setBackgroundResource(normalRes);
    }

}
