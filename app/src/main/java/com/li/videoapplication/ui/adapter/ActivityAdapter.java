package com.li.videoapplication.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.ActivityListActivity;
import com.li.videoapplication.ui.activity.MyWelfareActivity;
import com.li.videoapplication.ui.activity.WebActivity;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.URLUtil;

/**
 * 适配器：活动，我的活动
 */
@SuppressLint("InflateParams")
public class ActivityAdapter extends BaseArrayAdapter<Match> {

    private Activity activity;

    /**
     * 跳转：活动详情
     */
    private void startActivityDetailActivity(Match record) {
        ActivityManeger.startActivityDetailActivityNewTask(getContext(), record.getMatch_id());
    }

    /**
     * 跳转：获奖名单
     */
    @SuppressWarnings("unused")
    private void startWebActivity(String url) {
        WebActivity.startWebActivity(getContext(), url);
    }

    /**
     * 跳转：参赛视频
     */
    private void startActivityVideoActivity(Match item) {
        ActivityManeger.startActivityVideoActivity(getContext(), item);
    }

    public ActivityAdapter(Context context, List<Match> data) {
        super(context, R.layout.adapter_activity, data);
        try {
            activity = (Activity) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final Match record = getItem(position);
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_activity, null);
            holder.bg = (ImageView) view.findViewById(R.id.activity_bg);
            holder.pic = (ImageView) view.findViewById(R.id.activity_pic);
            holder.title = (TextView) view.findViewById(R.id.activity_title);
            holder.time = (TextView) view.findViewById(R.id.activity_time);
            holder.joined = (TextView) view.findViewById(R.id.activity_joined);
            holder.button = (RelativeLayout) view.findViewById(R.id.button);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setTextViewText(holder.title, record.getTitle());
        setTime(holder.time, record);
        setJoined(record, holder.joined);
        setImageViewImageNet(holder.pic, record.getFlag());
        setBg(record, holder.bg);

        holder.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (StringUtil.isNull(record.getUrl()) || !URLUtil.isURL(record.getUrl())) {
                    showToastShort("此活动已经束");
                    return;
                }

                startActivityDetailActivity(record);

                if (activity != null && activity instanceof ActivityListActivity) {
                    if (record.getStatus().equals("进行中")) {
                        UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.DISCOVER, "热门活动-进行中");
                    } else if (record.getStatus().equals("已结束")) {
                        UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.DISCOVER, "热门活动-已结束");
                    }
                } else if (activity != null && activity instanceof MyWelfareActivity) {
                    UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.SLIDER, "我的福利-活动");
                }

            }
        });

        setPicLayoutParams(holder.pic);

        return view;
    }

    private void setPicLayoutParams(ImageView view) {
        // 9 + 9 + 9 + 9 = 36
        int w = srceenWidth - ScreenUtil.dp2px(36);
        int h = w * 94 / 292;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.width = w;
        params.height = h;
        view.setLayoutParams(params);
    }

    private class ViewHolder {

        ImageView bg;
        ImageView pic;
        TextView title;
        TextView time;
        TextView joined;// 参加人数
        RelativeLayout button;
    }

    /**
     * 活动时间
     */
    private void setTime(TextView view, Match record) {
        // fc9b28
        // 00b4ff
        try {

            String string = "起止时间：<font color=\"#fc9b28\">"
                    + TimeHelper.getTimeFormat(record.getStarttime())
                    + "~" + TimeHelper.getTimeFormat(record.getEndtime()) + "</font>";

            view.setText(Html.fromHtml(string));
        } catch (Exception e) {
            e.printStackTrace();
            view.setText("");
        }

    }

    /**
     * 参加人数
     */
    private void setJoined(final Match record, final TextView view) {

        if (record.getStatus().equals("已结束") &&
                !StringUtil.isNull(record.getReward_url()) &&
                URLUtil.isURL(record.getReward_url())) {
            view.setVisibility(View.VISIBLE);
            view.setText("获奖名单");
            view.setTextColor(Color.parseColor("#fe3626"));//red
        } else {
            view.setVisibility(View.INVISIBLE);
        }

        view.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        view.getPaint().setAntiAlias(true);//抗锯齿

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (record.getStatus().equals("已结束") &&
                        !StringUtil.isNull(record.getReward_url()) &&
                        URLUtil.isURL(record.getReward_url())) {// 获奖名单

                    startWebActivity(record.getReward_url());
                } else {// 参赛视频
                    startActivityVideoActivity(record);
                }
            }
        });
    }

    /**
     * 赛事状态
     */
    private void setBg(Match record, ImageView view) {

        if (record.getStatus().equals("推荐")) {
            setImageViewImageRes(view, R.drawable.activity_red);
        } else if (record.getStatus().equals("进行中")) {
            setImageViewImageRes(view, R.drawable.activity_yellow);
        } else if (record.getStatus().equals("已结束")) {
            setImageViewImageRes(view, R.drawable.activity_gray);
        } else {
            view.setVisibility(View.GONE);
        }
    }
}
