package com.li.videoapplication.ui.dialog;

import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.BaseDialog;
import com.li.videoapplication.ui.activity.GameMatchDetailActivity;
import com.li.videoapplication.ui.activity.SignUpActivity;
import com.li.videoapplication.utils.TextUtil;


/**
 * 弹框：签到
 */
public class SignInSuccessDialog extends BaseDialog implements View.OnClickListener {

    private GameMatchDetailActivity activity;

    public SignInSuccessDialog(GameMatchDetailActivity activity,
                               String schedule_starttime,
                               String schedule_endtime) {
        super(activity);

        this.activity = activity;

        findViewById(R.id.dialog_signip_yes).setOnClickListener(this);
        TextView tip1 = (TextView) findViewById(R.id.dialog_signup_tip1);
        TextView tip2 = (TextView) findViewById(R.id.dialog_signup_tip2);

        String s1 = TextUtil.toColor(schedule_starttime, "#ff3d2e");
        tip1.setText(Html.fromHtml("请在 " + s1 + " 去赛程页面约战对手。"));

        String s2 = TextUtil.toColor(schedule_endtime, "#ff3d2e");
        tip2.setText(Html.fromHtml("胜方请于 " + s2 + " 前上传截图。"));
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_signinsuccess;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_signip_yes:
                if (activity != null)
                    activity.loadData();


                dismiss();
                    break;
        }
    }
}
