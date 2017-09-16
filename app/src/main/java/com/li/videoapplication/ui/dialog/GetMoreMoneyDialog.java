package com.li.videoapplication.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.BaseOverShootDialog;

/**
 * 弹框：是否要获取更多魔豆
 */

public class GetMoreMoneyDialog extends BaseOverShootDialog implements View.OnClickListener {

    public static int MODE_BEANS = 1;      //魔豆
    public static int MODE_CURRENCY = 2;    //魔币
    private TextView mContent;
    private TextView yes,no;
    private View.OnClickListener mListener;
    private int mMode = 0;

    public GetMoreMoneyDialog(Context context,String content,int mode,@NonNull View.OnClickListener listener) {
        super(context);
        init(content,mode,listener);
    }

    public GetMoreMoneyDialog(Context context, int theme,String content,int mode,@NonNull View.OnClickListener listener) {
        super(context, theme);
        init(content,mode,listener);
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
        no = (TextView) findViewById(R.id.tv_confirm_dialog_no);
        yes = (TextView) findViewById(R.id.tv_confirm_dialog_yes);
        no.setOnClickListener(this);
        yes.setOnClickListener(this);


        no.setTextColor(Color.parseColor("#fe5e5e"));
        mContent = (TextView) findViewById(R.id.tv_confirm_dialog_content);
        mContent.setGravity(Gravity.LEFT);
    }

    private void init(String content, int mode,View.OnClickListener listener){
        if (mContext != null){
            mContent.setText(content);
        }
        mListener = listener;
        yes.setTag(mode);
        no.setTag(mode);
        if (mode == MODE_BEANS){
            yes.setText("马上做任务");
            no.setText("立即充值");
        }else if (mode == MODE_CURRENCY){
            yes.setText("再看看");
            no.setText("立即充值");
        }

    }
}
