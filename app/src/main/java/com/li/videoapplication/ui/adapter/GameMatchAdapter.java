package com.li.videoapplication.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import com.li.videoapplication.ui.activity.WebActivity;
import com.li.videoapplication.utils.ScreenUtil;

import java.util.List;

/**
 * 适配器：游戏赛事
 */
@SuppressLint("InflateParams")
public class GameMatchAdapter extends BaseArrayAdapter<Match> {

    /**
     * 跳转：游戏赛事详情
     */
    private void startActivityDetailGameMatch(String event_id) {
        ActivityManeger.startGameMatchDetailActivity(getContext(), event_id);
        UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.MATCH, "进入赛事");
    }

    /**
     * 跳转：获奖名单
     */
    @SuppressWarnings("unused")
    private void startWebActivity(String url) {
        WebActivity.startWebActivity(getContext(), url);
    }

    public GameMatchAdapter(Context context, List<Match> data) {
        super(context, R.layout.adapter_gamematch, data);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final Match record = getItem(position);
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_gamematch, null);
            holder.bg = (ImageView) view.findViewById(R.id.activity_bg);
            holder.pic = (ImageView) view.findViewById(R.id.activity_pic);
            holder.title = (TextView) view.findViewById(R.id.activity_title);
            holder.time = (TextView) view.findViewById(R.id.activity_time);
            holder.match_type = (TextView) view.findViewById(R.id.activity_joined);
            holder.button = (RelativeLayout) view.findViewById(R.id.button);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setTextViewText(holder.title, record.getTitle());
        setTime(holder.time, record);
        setMatchType(record, holder.match_type);
        setImageViewImageNet(holder.pic, record.getCover());
        setBg(record, holder.bg);

        holder.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivityDetailGameMatch(record.getEvent_id());
            }
        });

        setPicLayoutParams(holder.pic);

        return view;
    }

    private void setMatchType(Match record, TextView view) {
        setTextViewText(view, record.getType_name());
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
        RelativeLayout button;
        TextView match_type;
    }

    /**
     * 活动时间
     */
    private void setTime(TextView view, Match record) {
        // ff641f
        try {
            String string = "起止时间：<font color=\"#ff641f\">" + TimeHelper.getTimeFormat(record.getStarttime()) +
                    "~" + TimeHelper.getTimeFormat(record.getEndtime()) + "</font>";

            view.setText(Html.fromHtml(string));
        } catch (Exception e) {
            e.printStackTrace();
            view.setText("");
        }
    }

    /**
     * 赛事状态
     */
    private void setBg(Match record, ImageView view) {

        switch (record.getStatus()) {
            case "火热":
                setImageViewImageRes(view, R.drawable.match_hot);
                break;
            case "进行中":
                setImageViewImageRes(view, R.drawable.activity_yellow);
                break;
            case "已结束":
                setImageViewImageRes(view, R.drawable.activity_gray);
                break;
            case "报名中":
                setImageViewImageRes(view, R.drawable.match_signingup);
                break;
            default:
                view.setVisibility(View.GONE);
                break;
        }
    }
}
