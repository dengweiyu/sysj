package com.li.videoapplication.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.BaseDialog;

/**
 * 确定
 */

public class ConfirmDoneDialog extends BaseDialog implements View.OnClickListener {
    public ConfirmDoneDialog(Context context) {
        super(context);
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_confirm_done;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.ll_confirm).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_confirm:
                dismiss();
                break;
        }
    }
}
