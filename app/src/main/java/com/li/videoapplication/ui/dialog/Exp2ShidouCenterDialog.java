package com.li.videoapplication.ui.dialog;

import android.content.Context;
import android.view.View;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.BaseDialog;

/**
 * 弹框：经验转视豆对话框
 */
public class Exp2ShidouCenterDialog extends BaseDialog implements View.OnClickListener  {

    public Exp2ShidouCenterDialog(Context context) {
        super(context);

        findViewById(R.id.dialog_exp2shidou_ok).setOnClickListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_exp2shidou_center;
    }

    @Override
    public void onClick(View v) {
        cancel();
    }
}
