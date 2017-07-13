package com.li.videoapplication.mvp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.entity.SquareGameEntity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.ui.activity.SquareGameChoiceActivity;

import java.util.List;

/**
 * 玩家广场 游戏筛选列表适配器
 */

public class SquareGameChoiceAdapter extends BaseQuickAdapter<SquareGameEntity.DataBean, BaseViewHolder> {

    private List<SquareGameEntity.DataBean> data;

    private  int lastChoice = 0;
    private Context mContext;

    public SquareGameChoiceAdapter(Context context,List<SquareGameEntity.DataBean> dataBeen){
        super(R.layout.square_game_list_item,dataBeen);
        this.data = dataBeen;
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final SquareGameEntity.DataBean dataBean) {
        holder.setText(R.id.tv_game_name,dataBean.getName());
        GlideHelper.displayImage(mContext.getApplicationContext(),dataBean.getFlag(),(ImageView)holder.getView(R.id.iv_square_game_icon));
        UITask.postDelayed(new Runnable() {
            @Override
            public void run() {

                GlideHelper.displayImage(mContext.getApplicationContext(),dataBean.getFlag(),(ImageView)holder.getView(R.id.iv_square_game_icon));
            }
        },1000);

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
                    //直接返回
                    if (mContext instanceof Activity){
                        ((Activity)mContext).finish();
                    }
                    //notifyDataSetChanged();
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
