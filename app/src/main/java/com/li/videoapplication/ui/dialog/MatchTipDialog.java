package com.li.videoapplication.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.BaseEmptyDialog;

/**
 * 赛事遮罩
 */

public class MatchTipDialog extends BaseEmptyDialog implements View.OnClickListener {
    private ImageView known;
    public MatchTipDialog(Context context) {
        super(context);

        known = (ImageView) findViewById(R.id.tip_known);
        known.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tip_known:
                dismiss();
                break;
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.tip_match;
    }
}
