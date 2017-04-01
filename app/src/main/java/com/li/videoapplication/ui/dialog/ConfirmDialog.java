package com.li.videoapplication.ui.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.BaseOverShootDialog;

/**
 * Created by liuwei on 2017/3/28 0028.
 */

public class ConfirmDialog extends BaseOverShootDialog implements View.OnClickListener {

    private TextView mContent;
    private View.OnClickListener mListener;
    public ConfirmDialog(Context context,String content,@NonNull View.OnClickListener listener) {
        super(context);
        init(content,listener);
    }

    public ConfirmDialog(Context context, int theme,String content,@NonNull View.OnClickListener listener) {
        super(context, theme);
        init(content,listener);
    }

    @Override
    public void onClick(View view) {
        cancel();
        if (mListener != null){
            mListener.onClick(view);
        }
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

        mContent = (TextView) findViewById(R.id.tv_confirm_dialog_content);

    }

    private void init(String content,View.OnClickListener listener){
        if (mContext != null){
            mContent.setText(content);
        }
        mListener = listener;
    }
}
