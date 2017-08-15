package com.li.videoapplication.mvp.adapter;

import android.graphics.Color;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;

import java.util.List;

/**
 *
 */
public class ChoiceOptionAdapter extends BaseQuickAdapter<String ,BaseViewHolder> {
    private int isSelected = 0;

    public ChoiceOptionAdapter(List<String> data) {
        super(R.layout.adapter_choice_option,data);
    }

    @Override
    protected void convert(BaseViewHolder holder, String s) {
        TextView choice = holder.getView(R.id.tv_choice_name);
        choice.setText(s);

        if (isSelected == holder.getAdapterPosition()){
            choice.setTextColor(Color.parseColor("#fc3c2e"));
            choice.setBackgroundResource(R.drawable.choice_option_selected);
        }else {
            choice.setTextColor(Color.parseColor("#575757"));
            choice.setBackgroundResource(R.drawable.choice_option_unselected);
        }


    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }
}
