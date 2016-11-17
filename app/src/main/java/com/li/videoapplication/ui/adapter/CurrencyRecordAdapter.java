package com.li.videoapplication.ui.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.data.model.entity.CurrencySection;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.utils.StringUtil;

import java.util.List;

/**
 * 适配器：账单
 */
public class CurrencyRecordAdapter extends BaseSectionQuickAdapter<CurrencySection> {


    private TextImageHelper helper;

    public CurrencyRecordAdapter(int layoutResId, int sectionHeadResId, List data) {
        super(layoutResId, sectionHeadResId, data);
        helper = new TextImageHelper();
    }

    @Override
    protected void convertHead(BaseViewHolder holder, CurrencySection item) {
        holder.setText(R.id.currencyrecord_headertitle, item.header);
    }

    @Override
    protected void convert(BaseViewHolder holder, CurrencySection item) {
        Currency currency = item.t;

        if (currency.getOperation() == 0) {//支出
            holder.setText(R.id.currencyrecord_num, "-" + currency.getNum());
        } else {//收入
            holder.setText(R.id.currencyrecord_num, "+" + currency.getNum());
        }

        holder.setText(R.id.currencyrecord_description, currency.getTitle())
                .setText(R.id.currencyrecord_amount, "余额：" + StringUtil.formatNum(currency.getAmount()) + "豆");

        ImageView pic = holder.getView(R.id.currencyrecord_pic);
        helper.setImageViewImageNet(pic, currency.getIco());

        boolean isToday = false;
        boolean isYesterday = false;
        try {
            isToday = TimeHelper.IsToday(Long.valueOf(currency.getTime()));
            isYesterday = TimeHelper.IsYesterday(Long.valueOf(currency.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isToday || isYesterday) {
            try {
                // 12:30
                String time = TimeHelper.getTime2HmFormat(currency.getTime());
                holder.setText(R.id.currencyrecord_time, time);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // 04-11
            try {
                String time = TimeHelper.getTime2MdFormat(currency.getTime());
                holder.setText(R.id.currencyrecord_time, time);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        // 今天/昨天/周几
        try {
            String day = TimeHelper.getWeek(currency.getTime());
            holder.setText(R.id.currencyrecord_day, day);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
