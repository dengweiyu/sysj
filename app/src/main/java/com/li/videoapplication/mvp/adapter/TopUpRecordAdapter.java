package com.li.videoapplication.mvp.adapter;

import android.text.Html;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.TopUp;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;

import java.util.List;

/**
 * 适配器：充值记录
 */
public class TopUpRecordAdapter extends BaseQuickAdapter<TopUp, BaseViewHolder> {
    private static final String TAG = TopUpRecordAdapter.class.getSimpleName();
    private boolean DEBUG = false;

    public TopUpRecordAdapter(List<TopUp> data) {
        super(R.layout.adapter_topuprecord, data);
    }

    @Override
    protected void convert(final BaseViewHolder holder, TopUp item) {

        try {
            holder.setText(R.id.topuprecord_time, TimeHelper.getWholeTimeFormat_Str(item.getAdd_time()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String cost = "充值" + TextUtil.toColor(StringUtil.formatNum(item.getCurrency_num()), "#fe5e5e")
                + "魔豆 (" + item.getPrice() + "元)";
        holder.setText(R.id.topuprecord_cost, Html.fromHtml(cost));
    }


}
