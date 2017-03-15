package com.li.videoapplication.mvp.adapter;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Recommend;
import com.li.videoapplication.ui.dialog.OfficialPaymentDialog;
import com.li.videoapplication.utils.StringUtil;

import java.util.List;

/**
 * 适配器：推荐位
 */
public class OfficialPaymentAdapter extends BaseQuickAdapter<Recommend, BaseViewHolder> {
    private static final String TAG = OfficialPaymentAdapter.class.getSimpleName();
    private SparseBooleanArray mCheckStates;

    public OfficialPaymentAdapter(List<Recommend> data, SparseBooleanArray mCheckStates) {
        super(R.layout.adapter_officialpayment, data);
        this.mCheckStates = mCheckStates;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final Recommend item) {
        holder.setText(R.id.op_name, item.getName())
                .setText(R.id.op_price, StringUtil.formatNum(item.getCurrency_num()) + "飞磨豆");

        if (mCheckStates.get(holder.getAdapterPosition())) {
            holder.setImageResource(R.id.op_choose, R.drawable.radiobtn_blue_on);
        } else {
            holder.setImageResource(R.id.op_choose, R.drawable.radiobtn_blue_off);
        }

    }
}
