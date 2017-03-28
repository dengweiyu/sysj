package com.li.videoapplication.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.BaseOverShootDialog;

/**
 * Created by liuwei on 2017/3/28 0028.
 */

public class ConfirmDialog extends BaseOverShootDialog implements View.OnClickListener {

    private String mContent;
    public ConfirmDialog(Context context,String content) {
        super(context);
        mContent = content;
    }

    public ConfirmDialog(Context context, int theme,String content) {
        super(context, theme);
        mContent = content;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
        cancel();
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_confirm;
    }

    @Override
    protected void afterContentView(Context context) {
        super.afterContentView(context);

        findViewById(R.id.tv_confirm_dialog_no).setOnClickListener(this);
        findViewById(R.id.tv_confirm_dialog_yes).setOnClickListener(this);

        TextView content = (TextView) findViewById(R.id.tv_confirm_dialog_content);
        content.setText(mContent);
    }
}
