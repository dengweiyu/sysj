package com.li.videoapplication.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.BaseOverShootDialog;
import com.li.videoapplication.mvp.billboard.view.BillboardActivity;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.MainActivity;

/**
 * 弹框：跳转（我的钱包跳转各页面提示框）
 */
public class Jump2Dialog extends BaseOverShootDialog implements View.OnClickListener {

    public static final int TO_HOME = 1;
    public static final int TO_MATCH = 2;
    public static final int TO_UPLOADVIDEO = 3;
    public static final int TO_BILLBOARD_VIDEO = 4;
    public static final int TO_BILLBOARD_PLAYER = 5;
    public static final int TO_MALL = 6;
    private int toWhere = TO_HOME;
    private TextView go, content;

    public Jump2Dialog(Context context, int toWhere) {
        super(context, R.style.MyDialog);

        this.toWhere = toWhere;
        switch (toWhere) {
            case TO_HOME:
                content.setText(R.string.jump_home_content);
                go.setText(R.string.jump_home_go);
                break;
            case TO_MATCH:
                content.setText(R.string.jump_match_content);
                go.setText(R.string.jump_match_go);
                break;
            case TO_UPLOADVIDEO:
                content.setText(R.string.jump_upload_content);
                go.setText(R.string.jump_upload_go);
                break;
            case TO_BILLBOARD_VIDEO:
                content.setText(R.string.jump_videobillboard_content);
                go.setText(R.string.jump_videobillboard_go);
                break;
            case TO_BILLBOARD_PLAYER:
                content.setText(R.string.jump_playerbillboard_content);
                go.setText(R.string.jump_playerbillboard_go);
                break;
            case TO_MALL:
                content.setText(R.string.jump_mall_content);
                go.setText(R.string.jump_mall_go);
                break;
        }
    }

    public Jump2Dialog(Context context, String name) {
        super(context, R.style.MyDialog);
        toWhere = TO_HOME;
        String s = context.getString(R.string.jump_home_content_L) + name
                + context.getString(R.string.jump_home_content_R);
        content.setText(s);
        go.setText(R.string.jump_home_go);
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_jumpto;
    }

    @Override
    protected void afterContentView(Context context) {
        super.afterContentView(context);

        content = (TextView) findViewById(R.id.jump_content);
        go = (TextView) findViewById(R.id.jump_go);
        go.setOnClickListener(this);

        findViewById(R.id.jump_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jump_cancel:
                dismiss();
                break;
            case R.id.jump_go:
                dismiss();
                switch (toWhere) {
                    case TO_HOME:
                    case TO_MATCH:
                        AppManager.getInstance().currentActivity().finish();
                        MainActivity mainActivity = AppManager.getInstance().getMainActivity();
                        mainActivity.slidingMenu.showContent();
                        if (toWhere == TO_MATCH)
                            mainActivity.viewPager.setCurrentItem(3);
                        break;
                    case TO_UPLOADVIDEO:
                        ActivityManeger.startVideoMangerActivity(getContext());
                        break;
                    case TO_BILLBOARD_VIDEO:
                        ActivityManeger.startBillboardActivity(getContext(), BillboardActivity.TYPE_VIDEO);
                        break;
                    case TO_BILLBOARD_PLAYER:
                        ActivityManeger.startBillboardActivity(getContext(), BillboardActivity.TYPE_PLAYER);
                        break;
                    case TO_MALL:
                        ActivityManeger.startMallActivity(getContext());
                        break;
                }
                break;
        }
    }
}
