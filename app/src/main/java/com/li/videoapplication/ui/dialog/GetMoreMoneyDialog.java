package com.li.videoapplication.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.BaseOverShootDialog;

/**
 * 弹框：是否要获取更多飞磨豆
 */

public class GetMoreMoneyDialog extends BaseOverShootDialog implements View.OnClickListener {

    private TextView mContent;
    private View.OnClickListener mListener;

    public GetMoreMoneyDialog(Context context,String content,@NonNull View.OnClickListener listener) {
        super(context);
        init(content,listener);
    }

    public GetMoreMoneyDialog(Context context, int theme,String content,@NonNull View.OnClickListener listener) {
        super(context, theme);
        init(content,listener);
    }

    @Override
    public void onClick(View v) {
        cancel();
        if (mListener != null){
            mListener.onClick(v);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_confirm;
    }

    @Override
    protected void afterContentView(Context context) {
        super.afterContentView(context);
        TextView no = (TextView) findViewById(R.id.tv_confirm_dialog_no);
        TextView yes = (TextView) findViewById(R.id.tv_confirm_dialog_yes);
        no.setOnClickListener(this);
        yes.setOnClickListener(this);

        yes.setText("马上做任务");
        no.setText("立即充值");
        no.setTextColor(Color.parseColor("#fe5e5e"));
        mContent = (TextView) findViewById(R.id.tv_confirm_dialog_content);
        mContent.setGravity(Gravity.LEFT);
    }

    private void init(String content, View.OnClickListener listener){
        if (mContext != null){
            mContent.setText(content);
        }
        mListener = listener;
    }
}
