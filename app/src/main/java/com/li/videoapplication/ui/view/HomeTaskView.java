package com.li.videoapplication.ui.view;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.tools.AnimationHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.utils.TextUtil;

/**
 * 视图：主页任务提示条
 */
public class HomeTaskView extends LinearLayout implements
        View.OnClickListener {

    private Animation appearAnim, disappearAnim;
    private View container;

    public HomeTaskView(Context context) {
        super(context);
    }

    public HomeTaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initContentView();
    }

    private void initContentView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_hometask, this);

        container = view.findViewById(R.id.hometask_container);
        TextView textView = (TextView) view.findViewById(R.id.hometask_text2);
        String s = "完成" + toRed("任务") + "，参与" + toRed("赛事，活动") + "获得海量飞磨豆";
        textView.setText(Html.fromHtml(s));

        view.findViewById(R.id.hometask_go2task).setOnClickListener(this);
        view.findViewById(R.id.hometask_close).setOnClickListener(this);

        container.setVisibility(GONE);

        appearAnim = new AlphaAnimation(0, 1);
        appearAnim.setDuration(300);
        disappearAnim = new AlphaAnimation(1, 0);
        disappearAnim.setDuration(500);
    }

    private String toRed(String string) {
        return TextUtil.toColor(string, "#fe5e5e");
    }

    public void disappear() {
        if (container.getVisibility() == View.VISIBLE) {
            container.setVisibility(GONE);
            container.startAnimation(disappearAnim);
        }
    }

    public void appear() {
        if (container.getVisibility() != View.VISIBLE) {
            container.setVisibility(VISIBLE);
            container.startAnimation(appearAnim);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hometask_close:
                disappear();
                break;
            case R.id.hometask_go2task:
                if (!PreferencesHepler.getInstance().isLogin()) {
                    DialogManager.showLogInDialog(getContext());
                    return;
                }
                ActivityManeger.startMyWalletActivity(getContext());
                break;
        }
    }
}
