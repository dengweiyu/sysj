package com.li.videoapplication.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.BaseEmptyDialog;

/**
 * 打赏引导页
 */

public class PlayGiftTipDialog extends BaseEmptyDialog implements View.OnClickListener {
    private ImageView known;

    public PlayGiftTipDialog(Context context) {
        super(context);
        initView();
    }

    private void initView(){
        known = (ImageView) findViewById(R.id.tip_known);
        known.setOnClickListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_play_gift_tip;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tip_known:
                dismiss();
                break;
        }
    }
}
