package com.li.videoapplication.ui.dialog;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.BaseDialog;
import com.li.videoapplication.utils.TextUtil;


/**
 * 弹框：开始匹配对手
 */
public class MatchOpponentDialog extends BaseDialog implements View.OnClickListener {


    private String schedule_id;

    public MatchOpponentDialog(Context context, String schedule_id) {
        super(context);
        this.schedule_id = schedule_id;

        findViewById(R.id.matchopponent_content_no).setOnClickListener(this);
        findViewById(R.id.matchopponent_content_yes).setOnClickListener(this);
        TextView content = (TextView) findViewById(R.id.matchopponent_content);

        String s = TextUtil.toColor("现在开始匹配", "#ff3d2e");
        content.setText(Html.fromHtml("是否" + s + "您的对手进行比赛？"));
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_matchopponent;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.matchopponent_content_yes:
                DataManager.signSchedule210(PreferencesHepler.getInstance().getMember_id(), schedule_id);
                break;
        }
        cancel();
    }
}
