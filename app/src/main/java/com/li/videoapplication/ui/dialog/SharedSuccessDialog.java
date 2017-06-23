package com.li.videoapplication.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.li.videoapplication.R;
import com.li.videoapplication.framework.BaseDialog;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.utils.ScreenUtil;


/**
 * 分享到玩家广场
 */

public class SharedSuccessDialog extends BaseDialog implements View.OnClickListener {


    private String gameId;
    public SharedSuccessDialog(Context context,String title,String gameId) {
        super(context);
        TextView tvTitle = (TextView)findViewById(R.id.iv_shared_success_title);
        setTextViewText(tvTitle,title);
        this.gameId = gameId;
    }

    public SharedSuccessDialog(Context context, int theme,String title) {
        super(context, theme);
        TextView tvTitle = (TextView)findViewById(R.id.iv_shared_success_title);
        setTextViewText(tvTitle,title);
    }

    @Override
    protected int getContentView() {
        return R.layout.dialo_shared_success;
    }

    @Override
    protected void afterContentView(Context context) {
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        // 根据x，y坐标设置窗口需要显示的位置
        params.x = 0; // x小于0左移，大于0右移
        params.y = 0; // y小于0上移，大于0下移
        params.gravity = Gravity.CENTER; // 设置重力
        params.width = (int) (ScreenUtil.getScreenWidth()*0.85);
        window.setAttributes(params);
        findViewById(R.id.iv_shared_success_confirm).setOnClickListener(this);
        findViewById(R.id.rl_shared_to_square).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_shared_success_confirm:
                dismiss();break;
            case R.id.rl_shared_to_square:
                ActivityManager.startSquareActivity(getContext(),gameId);
                dismiss();
                break;
        }
    }
}
