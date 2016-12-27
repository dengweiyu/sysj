package com.li.videoapplication.ui.dialog;

import android.content.Context;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.response.GetEventMsgEntity;
import com.li.videoapplication.framework.BaseOverShootDialog;

import io.rong.eventbus.EventBus;

/**
 * 弹框：登陆
 */
public class SignUpQuestionDialog extends BaseOverShootDialog {

    private TextView text_1, text_2, text_3, text_4;

    public SignUpQuestionDialog(Context context) {
        super(context, R.style.MyDialog);
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_signup_question;
    }

    @Override
    protected void afterContentView(Context context) {
        super.afterContentView(context);

        text_1 = (TextView) findViewById(R.id.signup_question_1);
        text_2 = (TextView) findViewById(R.id.signup_question_2);
        text_3 = (TextView) findViewById(R.id.signup_question_3);
        text_4 = (TextView) findViewById(R.id.signup_question_4);
        DataManager.getEventMsg();
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
     * 回调:问号内容
     */
    public void onEventMainThread(GetEventMsgEntity event) {
        if (event != null && event.isResult()) {
            setTextViewText(text_1, event.getData().get(0).getRewardTitle());
            setTextViewText(text_2, event.getData().get(0).getRewardDes());
            setTextViewText(text_3, event.getData().get(0).getRulesTitle());
            setTextViewText(text_4, event.getData().get(0).getRulesDes());
        }
    }
}
