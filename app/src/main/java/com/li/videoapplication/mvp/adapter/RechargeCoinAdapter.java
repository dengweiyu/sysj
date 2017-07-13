package com.li.videoapplication.mvp.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.mvp.mall.view.RechargeCoinFragment;
import com.li.videoapplication.tools.ToastHelper;

import java.util.List;

/**
 *
 */

public class RechargeCoinAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {
    private int mSelectPrice;
    private RechargeCoinFragment mFragment;

    private int mMinNum = 1;
    private int mInputNum;
    public RechargeCoinAdapter(RechargeCoinFragment fragment,List<Integer> data) {
        super(R.layout.adapter_topup, data);
        if (data != null && data.size() > 2){
            mSelectPrice = 2;
        }
        this.mFragment = fragment;

    }

    @Override
    protected void convert(BaseViewHolder holder,Integer price) {
        EditText input = holder.getView(R.id.topup_custom);
        holder.setImageResource(R.id.topup_bean,R.drawable.currency);
        if (price != -1 ){
            input.clearFocus();
            holder.setVisible(R.id.topup_option,true)
                    .setText(R.id.topup_option,price+" 魔币")
                    .setVisible(R.id.topup_custom,false);
        }else {
            holder.setVisible(R.id.topup_option,false)
                    .setVisible(R.id.topup_custom,true);
            input.addTextChangedListener(mListener);
            input.requestFocus();
        }

        if (holder.getAdapterPosition() == mSelectPrice){
            holder.setBackgroundRes(R.id.topup_check, R.drawable.stroke_red_4px)
                    .setVisible(R.id.topup_check_img,true);
        }else {
            holder.setBackgroundRes(R.id.topup_check, R.drawable.stroke_gray_2px)
                    .setVisible(R.id.topup_check_img,false);
        }
    }


    final TextWatcher mListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s == null || s.length() == 0){
                return;
            }
            try {
                mInputNum = Integer.valueOf(s.toString());

            } catch (NumberFormatException e) {
                ToastHelper.s("充值魔币数量不在允许范围");
            }
            //update
            mFragment.setPrice(getData().size() - 1);
            if (mInputNum < mMinNum){
                ToastHelper.s("充值魔币数量最低为1");
            }
        }
    };

    public void refreshSelectItem(int select){
        mSelectPrice = select;
        notifyDataSetChanged();
    }

    public int getSelectPrice(){
        return mSelectPrice;
    }

    public int getInputNumber(){
        return mInputNum;
    }
}
