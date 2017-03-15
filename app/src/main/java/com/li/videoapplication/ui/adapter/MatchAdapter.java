package com.li.videoapplication.ui.adapter;

import android.text.Html;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;

import java.util.List;

/**
 * 适配器：赛事奖励
 */
public class MatchAdapter extends BaseQuickAdapter<Currency, BaseViewHolder> {

    private TextImageHelper helper;

    public MatchAdapter(List<Currency> data) {
        super(R.layout.adapter_match, data);
        helper = new TextImageHelper();
    }

    @Override
    protected void convert(BaseViewHolder holder, Currency currency) {
        ViewGroup.LayoutParams params = holder.getView(R.id.match_root).getLayoutParams();

        helper.setImageViewImageNet((ImageView) holder.getView(R.id.match_pic), currency.getIcon());

        holder.setText(R.id.match_name, currency.getName());

        if (!StringUtil.isNull(currency.getCompleteRatio())) {
            String ratio = "今日已完成：" + TextUtil.toColor(currency.getCompleteRatio(), "#fe5e5e");//red
            holder.setVisible(R.id.match_ratio, true)
                    .setText(R.id.match_ratio, Html.fromHtml(ratio));
        } else if (!StringUtil.isNull(currency.getEventDesc())) {
            holder.setVisible(R.id.match_ratio, true)
                    .setText(R.id.match_ratio, currency.getEventDesc());
        } else {
            holder.setVisible(R.id.match_ratio, false);
        }

        //##飞磨豆（每日限奖1次） --> 200飞磨豆（每日限奖1次）
        String reward = TextUtil.toColor(currency.getReward(), "#f7b500");
        String description = currency.getDescription().replace("##", reward);
        holder.setText(R.id.match_description, Html.fromHtml(description));

        long currentTime;
        try {
            currentTime = TimeHelper.getCurrentTime();
            Long startTime = Long.valueOf(currency.getStart_time());
            Long endTime = Long.valueOf(currency.getEnd_time());
            if (startTime <= currentTime && currentTime < endTime){
                //任务状态：1=>进行中，2=>完成，0=>放弃，失败
                if (!StringUtil.isNull(currency.getStatus()) && currency.getStatus().equals("2")) {
                    holder.setVisible(R.id.match_shadow, true);
                    TextView shadow = holder.getView(R.id.match_shadow);
                    shadow.setLayoutParams(params);
                    shadow.setGravity(Gravity.CENTER);
                } else {
                    holder.setVisible(R.id.match_shadow, false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
