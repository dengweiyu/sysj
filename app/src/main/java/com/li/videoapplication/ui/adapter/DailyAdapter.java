package com.li.videoapplication.ui.adapter;

import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;

import java.util.List;

/**
 * 适配器：日常奖励，荣誉奖励
 */
public class DailyAdapter extends BaseQuickAdapter<Currency> {
    private static final String TAG = DailyAdapter.class.getSimpleName();

    private TextImageHelper helper;
    private boolean shouldCoverShalow;//是否要有已完成任务的遮罩

    public DailyAdapter(List<Currency> data) {
        super(R.layout.adapter_daily, data);
        helper = new TextImageHelper();
    }

    public void coverShalow(boolean shouldCoverShalow){
        this.shouldCoverShalow = shouldCoverShalow;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Currency currency) {

        ImageView pic = baseViewHolder.getView(R.id.daily_pic);
        helper.setImageViewImageNet(pic, currency.getIcon());

        baseViewHolder.setText(R.id.daily_name, currency.getName());

        //##飞磨豆（每日限奖1次） --> 200飞磨豆（每日限奖1次）
        String reward = TextUtil.toColor(currency.getReward(), "#f7b500");
        String description = currency.getDescription().replace("##", reward);
        baseViewHolder.setText(R.id.daily_description, Html.fromHtml(description));

        if (shouldCoverShalow){
            long currentTime;
            try {
                currentTime = TimeHelper.getCurrentTime();
                Long startTime = Long.valueOf(currency.getStart_time());
                Long endTime = Long.valueOf(currency.getEnd_time());
                if (startTime <= currentTime && currentTime < endTime) {
                    //任务状态：1=>进行中，2=>完成，0=>放弃，失败
                    if (!StringUtil.isNull(currency.getStatus()) && currency.getStatus().equals("2")) {
                        baseViewHolder.setVisible(R.id.daily_shadow, true);

                        TextView shadow = baseViewHolder.getView(R.id.daily_shadow);
                        ViewGroup.LayoutParams params = shadow.getLayoutParams();

                        View root = baseViewHolder.getView(R.id.daily_root);
                        root.measure(0,0);
                        params.height = root.getMeasuredHeightAndState();

                        shadow.setLayoutParams(params);

                    } else {
                        baseViewHolder.setVisible(R.id.daily_shadow, false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
