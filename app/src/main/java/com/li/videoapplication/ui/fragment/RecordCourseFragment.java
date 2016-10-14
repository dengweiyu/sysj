package com.li.videoapplication.ui.fragment;

import android.app.Activity;
import android.graphics.Paint;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.ui.activity.HelpActivity;

/**
 * 碎片：录屏教程
 */
public class RecordCourseFragment extends TBaseFragment implements View.OnClickListener {

    private HelpActivity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity = (HelpActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getCreateView() {
        return R.layout.pager_help_screen;
    }

    @Override
    protected void initContentView(View view) {
        TextView floatview = (TextView) view.findViewById(R.id.help_floatview);
        floatview.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
        floatview.getPaint().setAntiAlias(true);// 抗锯齿
        floatview.setOnClickListener(this);

        TextView attention = (TextView) view.findViewById(R.id.help_attention);
        attention.setText((Html.fromHtml("<font color=\"#ff0000\">注意:</font> 在使用录屏大师之前，请确保您的手机系统为Android 4.0以上，并且已经通过Root获得系统权限。")));

        TextView howToRoot = (TextView) view.findViewById(R.id.help_howtoroot);
        howToRoot.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
        howToRoot.getPaint().setAntiAlias(true);// 抗锯齿
        howToRoot.setOnClickListener(this);
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.help_howtoroot:
                if (activity != null) {
                    activity.viewPager.setCurrentItem(2);
                    // 展开第一个
                    activity.questionFragment.expandableListView.expandGroup(0);
                }
                break;
            case R.id.help_floatview:
                if (activity != null) {
                    activity.viewPager.setCurrentItem(2);
                    // 展开第3个
                    activity.questionFragment.expandableListView.expandGroup(2);
                }
                break;
        }
    }
}
