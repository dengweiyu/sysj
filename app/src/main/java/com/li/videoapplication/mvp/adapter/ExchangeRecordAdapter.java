package com.li.videoapplication.mvp.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.tools.TimeHelper;

import java.util.List;

/**
 * 适配器：兑换记录
 */
public class ExchangeRecordAdapter extends BaseQuickAdapter<Currency, BaseViewHolder> {

    private TextImageHelper helper;

    public ExchangeRecordAdapter(List<Currency> data) {
        super(R.layout.adapter_exchangerecord, data);
        helper = new TextImageHelper();
    }

    @Override
    protected void convert(BaseViewHolder holder, Currency item) {

        helper.setImageViewImageNet((ImageView) holder.getView(R.id.exchangerecord_pic), item.getCover());

        holder.setText(R.id.exchangerecord_name, item.getGoods_name())
                .setText(R.id.exchangerecord_status, item.getStatusText());

        try {
            String time = TimeHelper.getWholeTimeFormat(item.getAdd_time());
            holder.setText(R.id.exchangerecord_time, time);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //订单状态：1=>审核不通，拒绝，2=>审核中，3=>待发放，4=>已发放，6=>已推荐
        switch (item.getStatus()) {
            case "1"://审核不通过
                setColor(holder, R.color.currency_red);
                break;
            case "2"://审核中
                setColor(holder, R.color.currency_blue);
                break;
            case "3"://待发放
                setColor(holder, R.color.currency_modou_gold);
                break;
            case "4"://已发放
            case "6"://已推荐
                setColor(holder, R.color.currency_green);
                break;
        }
    }

    private void setColor(BaseViewHolder holder, int color) {
        holder.setTextColor(R.id.exchangerecord_status, mContext.getResources().getColor(color));
    }
}
