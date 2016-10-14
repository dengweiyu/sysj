package com.li.videoapplication.ui.dialog;

import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.BaseDialog;
import com.li.videoapplication.ui.activity.SignUpActivity;
import com.li.videoapplication.utils.TextUtil;


/**
 * 弹框：开始匹配对手
 */
public class SignUpSuccessDialog extends BaseDialog implements View.OnClickListener {

    private SignUpActivity activity;

    public SignUpSuccessDialog(SignUpActivity activity,
                               String sign_starttime,
                               String sign_endtime,
                               String schedule_starttime) {
        super(activity);

        this.activity = activity;

        findViewById(R.id.dialog_signip_yes).setOnClickListener(this);
        TextView tip1 = (TextView) findViewById(R.id.dialog_signup_tip1);
        TextView tip2 = (TextView) findViewById(R.id.dialog_signup_tip2);

        String s1 = TextUtil.toColor(sign_starttime + "~" + sign_endtime, "#ff3d2e");
        tip1.setText(Html.fromHtml("请在 " + s1 + " 到赛规页面签到。"));

        String s2 = TextUtil.toColor(schedule_starttime, "#ff3d2e");
        tip2.setText(Html.fromHtml("比赛将于 " + s2 + " 开始，请准时参加。"));
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_signupsuccess;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_signip_yes:
                if (activity != null)
                    activity.finish();
                break;
        }
    }
}
