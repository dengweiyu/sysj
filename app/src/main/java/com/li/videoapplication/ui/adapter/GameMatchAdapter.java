package com.li.videoapplication.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Paint;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.ui.activity.ActivityListActivity;
import com.li.videoapplication.ui.activity.WebActivity;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.URLUtil;

import java.util.List;

/**
 * 适配器：圈子-赛事列表, 活动-列表
 */
@SuppressLint("InflateParams")
public class GameMatchAdapter extends BaseQuickAdapter<Match, BaseViewHolder> {

    private TextImageHelper helper;
    private Activity activity;

    /**
     * 跳转：获奖名单
     */
    @SuppressWarnings("unused")
    private void startWebActivity(String url) {
        WebActivity.startWebActivity(mContext, url);
    }

    public GameMatchAdapter(Activity activity, List<Match> data) {
        super(R.layout.adapter_gamematch, data);
        this.activity = activity;
        helper = new TextImageHelper();
    }

    @Override
    protected void convert(BaseViewHolder holder, Match match) {
        holder.setText(R.id.activity_title, match.getTitle());

        TextView time = holder.getView(R.id.activity_time);
        setTime(time, match);

        ImageView pic = holder.getView(R.id.match_pic);
        setPicLayoutParams(pic);

        ImageView bg = holder.getView(R.id.activity_bg);
        setBg(match, bg);

        if (activity instanceof ActivityListActivity) {
            setReward(match, holder);
            helper.setImageViewImageNet(pic, match.getFlag());
        } else {
            helper.setImageViewImageNet(pic, match.getCover());
            holder.setVisible(R.id.activity_joined, true)
                    .setText(R.id.activity_joined, match.getType_name());
        }
    }

    private void setReward(final Match record, BaseViewHolder holder) {

        final boolean isReward = record.getStatus().equals("已结束")
                && !StringUtil.isNull(record.getReward_url())
                && URLUtil.isURL(record.getReward_url());

        holder.setVisible(R.id.activity_reward, isReward);

        TextView reward = holder.getView(R.id.activity_reward);
        reward.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        reward.getPaint().setAntiAlias(true);//抗锯齿
        reward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isReward) {// 获奖名单
                    startWebActivity(record.getReward_url());
                }
            }
        });
    }

    private void setPicLayoutParams(ImageView view) {
        // 9 + 9 + 9 + 9 = 36
        int w = ScreenUtil.getScreenWidth() - ScreenUtil.dp2px(36);
        int h = w * 94 / 292;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.width = w;
        params.height = h;
        view.setLayoutParams(params);
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
                helper.setImageViewImageRes(view, R.drawable.match_hot);
                break;
            case "进行中":
                helper.setImageViewImageRes(view, R.drawable.activity_yellow);
                break;
            case "已结束":
                helper.setImageViewImageRes(view, R.drawable.activity_gray);
                break;
            case "报名中":
                helper.setImageViewImageRes(view, R.drawable.match_signingup);
                break;
            default:
                view.setVisibility(View.GONE);
                break;
        }
    }
}
