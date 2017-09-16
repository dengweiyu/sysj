package com.li.videoapplication.mvp.adapter;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.ui.activity.WebActivity;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.URLUtil;

import java.util.List;

/**
 * 适配器：活动列表，我的活动列表
 */
@SuppressLint("InflateParams")
public class ActivityListAdapter extends BaseQuickAdapter<Match, BaseViewHolder> {

    private TextImageHelper helper;

    /**
     * 跳转：获奖名单
     */
    @SuppressWarnings("unused")
    private void startWebActivity(String url) {
        WebActivity.startWebActivity(mContext, url);
    }

    public ActivityListAdapter(List<Match> data) {
        super(R.layout.adapter_matchlist, data);
        helper = new TextImageHelper();
    }

    @Override
    protected void convert(BaseViewHolder holder, Match match) {
        holder.setText(R.id.match_title, match.getTitle())
                .setText(R.id.match_type, match.getMatch_format())
                .setText(R.id.match_reward, match.getPc_rewards());

        ImageView pic = holder.getView(R.id.match_pic);
        helper.setImageViewImageNet(pic, match.getCover());

        setStatus(holder, match);
        setReward(holder, match);
        setTime(holder, match);

    }

    private void setTime(BaseViewHolder holder, Match record) {
        try {
            String string = TimeHelper.getMMddTimeFormat(record.getStarttime()) +
                    "~" + TimeHelper.getMMddTimeFormat(record.getEndtime());

            holder.setText(R.id.match_time, Html.fromHtml(string));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setReward(BaseViewHolder holder,final Match record ) {
        final boolean isReward = record.getStatus().equals("已结束")
                && !StringUtil.isNull(record.getReward_url())
                && URLUtil.isURL(record.getReward_url());

        holder.setVisible(R.id.activity_reward, isReward)
                .addOnClickListener(R.id.activity_reward);

        TextView reward = holder.getView(R.id.activity_reward);
        reward.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        reward.getPaint().setAntiAlias(true);//抗锯齿
    }

    private void setStatus(BaseViewHolder holder, Match match) {
        holder.setText(R.id.match_status, match.getStatus());
        switch (match.getStatus()) {
            case "进行中":
                setStatusBgColor(holder, R.color.activity_red);
                break;
            case "已结束":
                setStatusBgColor(holder, R.color.activity_gray);
                break;
            case "报名中":
                setStatusBgColor(holder, R.color.match_green);
                break;
            default:
                holder.setVisible(R.id.match_status, false);
                break;
        }
    }

    private void setStatusBgColor(BaseViewHolder holder, int color) {
        holder.setBackgroundColor(R.id.match_status,
                mContext.getResources().getColor(color));
    }
}
