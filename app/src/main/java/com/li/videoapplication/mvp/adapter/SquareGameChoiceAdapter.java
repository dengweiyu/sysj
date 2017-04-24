package com.li.videoapplication.mvp.adapter;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.entity.SquareGameEntity;

import java.util.List;

/**
 * 玩家广场 游戏筛选列表适配器
 */

public class SquareGameChoiceAdapter extends BaseQuickAdapter<SquareGameEntity.DataBean, BaseViewHolder> {

    private List<SquareGameEntity.DataBean> data;

    private  int lastChoice = 0;

    public SquareGameChoiceAdapter(List<SquareGameEntity.DataBean> dataBeen){
        super(R.layout.square_game_list_item,dataBeen);
        this.data = dataBeen;

    }

    @Override
    protected void convert(BaseViewHolder holder, SquareGameEntity.DataBean dataBean) {
        holder.setText(R.id.tv_game_name,dataBean.getName());
        GlideHelper.displayImage(mContext,dataBean.getFlag(),(ImageView)holder.getView(R.id.iv_square_game_icon));
        View root  = holder.getView(R.id.ll_square_item_root);
        if (dataBean.isChoice()){
            root.setBackgroundResource(R.drawable.square_game_selected);
            lastChoice = holder.getLayoutPosition();
        }else {
            root.setBackgroundResource(R.drawable.square_game_unselected);
        }
        root.setTag(holder.getLayoutPosition());
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = 0;
                try {
                    position = (Integer) v.getTag();
                    data.get(lastChoice).setChoice(false);
                    lastChoice = position;
                    data.get(lastChoice).setChoice(true);
                    notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public int getLastChoice() {
        return lastChoice;
    }
}
