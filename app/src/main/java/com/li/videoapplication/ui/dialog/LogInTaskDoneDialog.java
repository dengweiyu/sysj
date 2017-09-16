package com.li.videoapplication.ui.dialog;

import android.content.Context;
import android.view.View;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.BaseOverShootDialog;


/**
 * 弹框：每日登陆任务完成
 */
public class LogInTaskDoneDialog extends BaseOverShootDialog implements View.OnClickListener {


    public LogInTaskDoneDialog(Context context) {
        super(context, R.style.MyDialog);
        findViewById(R.id.logintask_confirm).setOnClickListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_logintaskdone;
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
