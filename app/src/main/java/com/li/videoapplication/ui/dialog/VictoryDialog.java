package com.li.videoapplication.ui.dialog;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.BaseDialog;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.ui.activity.SignUpActivity;
import com.li.videoapplication.utils.TextUtil;


/**
 * 弹框：您已获胜
 */
public class VictoryDialog extends BaseDialog implements View.OnClickListener {

    private String schedule_id;

    public VictoryDialog(Context context, String next_time, boolean isLast, String schedule_id) {
        super(context);
        this.schedule_id = schedule_id;

        findViewById(R.id.dialog_victory_yes).setOnClickListener(this);
        TextView text = (TextView) findViewById(R.id.dialog_victory_text);

        try {
            String nextMatchStartTime = TimeHelper.getMMddHHmmTimeFormat(next_time);

            String s1 = TextUtil.toColor(nextMatchStartTime, "#ff3d2e");
            String s2 = TextUtil.toColor("我的赛程", "#ff3d2e");

            text.setText(Html.fromHtml("下轮比赛将于" + s1 + "开始，请准时到" + s2 + "页面联系对手进行比赛。"));

        } catch (Exception e) {
            e.printStackTrace();
            text.setVisibility(View.GONE);
        }

        if (isLast) {
            text.setVisibility(View.GONE);
        }

        DataManager.setAlert(PreferencesHepler.getInstance().getMember_id(), schedule_id);
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_victory;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_victory_yes:
                DataManager.eventsSetAlert210(schedule_id, PreferencesHepler.getInstance().getMember_id());
                dismiss();
                break;
        }
    }
}
