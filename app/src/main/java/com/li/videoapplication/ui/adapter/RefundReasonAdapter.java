package com.li.videoapplication.ui.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.AppCompatRadioButton;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import java.util.List;

/**
 * 退款理由
 */

public class RefundReasonAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

    private int mSelectPosition = -1;

    private boolean mIsDone = false;

    private String mDefaultReason;
    public RefundReasonAdapter(List<String> data) {
        super(R.layout.refund_reason_item,data);
    }

    @Override
    protected void convert(final  BaseViewHolder holder, String s) {
        holder.setText(R.id.tv_refund_reason,s);

        AppCompatRadioButton radioButton = (AppCompatRadioButton)holder.getView(R.id.arb_select);
        if (mIsDone){
            radioButton.setOnCheckedChangeListener(null);
            radioButton.setSupportButtonTintList(ColorStateList.valueOf(Color.parseColor("#8c8c8c")));
            if (s.equals(mDefaultReason)){
                radioButton.setChecked(true);
            }
            radioButton.setEnabled(false);
        }else {

            radioButton.setSupportButtonTintList(ColorStateList.valueOf(Color.parseColor("#fc3c2e")));
            if (holder.getAdapterPosition() == mSelectPosition){
                if (!radioButton.isChecked()){
                    radioButton.setChecked(true);
                }
            }else {
                if (radioButton.isChecked()){
                    radioButton.setChecked(false);
                }
            }
            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked && mSelectPosition != holder.getAdapterPosition()){
                        mSelectPosition = holder.getAdapterPosition();
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

    public int getSelectPosition() {
        return mSelectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        mSelectPosition = selectPosition;

        notifyDataSetChanged();
    }


    public void setApplyDone(boolean isDone){
        mIsDone = isDone;
        notifyDataSetChanged();
    }


    public String getDefaultReason() {
        return mDefaultReason;
    }

    public void setDefaultReason(String defaultReason) {
        mDefaultReason = defaultReason;
    }
}
