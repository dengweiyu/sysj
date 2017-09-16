package com.li.videoapplication.ui.adapter;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.response.FocusGameListEntity;
import com.li.videoapplication.data.network.UITask;

import java.util.List;

/**
 *
 */

public class ChoiceFocusGameAdapter extends BaseQuickAdapter<FocusGameListEntity.ADataBean,BaseViewHolder> {

    public ChoiceFocusGameAdapter(List<FocusGameListEntity.ADataBean> data) {
        super(R.layout.adapter_choice_focus_game,data);
    }

    @Override
    protected void convert(BaseViewHolder holder,final FocusGameListEntity.ADataBean aDataBean) {
        final ImageView icon = holder.getView(R.id.iv_square_game_icon);
        GlideHelper.displayImageWhite(mContext,aDataBean.getFlag(),icon);

        TextView gameName = holder.getView(R.id.tv_game_name);
        gameName.setText(aDataBean.getGroup_name());


        ImageView hook = holder.getView(R.id.iv_is_choice);
        if (aDataBean.isChoice()){
            hook.setImageResource(R.drawable.choice_red);
        }else {
            hook.setImageResource(R.drawable.choice_gray);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChoice = !aDataBean.isChoice();
                aDataBean.setChoice(isChoice);
                notifyDataSetChanged();
            }
        });
    }


}
