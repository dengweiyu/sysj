package com.li.videoapplication.ui.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 陪玩订单结果
 */

public class OrderResultListAdapter extends RecyclerView.Adapter {
    private int mCount;
    private Context mContext;
    private List<Integer> mSelectedList;
    private boolean isChoiceDone;
    public OrderResultListAdapter(Context context,int count) {
        super();
        mCount = count;
        mContext = context;
        mSelectedList = new ArrayList<>();
        for (int i = 0; i < mCount; i++) {
            mSelectedList.add(-1);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root  = null;
        root = LayoutInflater.from(mContext).inflate(R.layout.order_result_list_item,parent,false);

        OrderResultListViewHolder viewHolder = new OrderResultListViewHolder(root);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        TextView title = (TextView) holder.itemView.findViewById(R.id.tv_result_title);
        TextView result = (TextView) holder.itemView.findViewById(R.id.tv_result_content);

        if (position %4 == 0){              //title
            title.setVisibility(View.VISIBLE);
            result.setVisibility(View.GONE);

            title.setText("第"+StringUtil.convert2Chinese((position / 4)+1)+"局");
        }else {                             //option
            title.setVisibility(View.GONE);
            result.setVisibility(View.VISIBLE);

            switch (position % 4){
                case 1:
                    result.setText("胜");
                    break;
                case 2:
                    result.setText("负");
                    break;
                case 3:
                    result.setText("没打");
                    break;
            }

            int index = position /4;
            int select = mSelectedList.get(index);
            if (position == select){
                result.setBackgroundResource(R.drawable.red_round_fill);
            }else {
                result.setBackgroundResource(R.drawable.gray_round_fill);
            }

            holder.itemView.findViewById(R.id.rl_order_result_root).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position % 4 == 0){
                        return;
                    }else {
                        int index = position /4;
                        setSelectPosition(index,position);
                    }
                }
            });

            if (isChoiceDone){
                holder.itemView.findViewById(R.id.rl_order_result_root).setOnClickListener(null);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mCount*4;            //
    }

    private void setSelectPosition(int index,int position){
        if (mSelectedList != null){
            mSelectedList.set(index,position);
            notifyDataSetChanged();
        }
    }

    public boolean isChoiceDone() {
        return isChoiceDone;
    }

    public void setChoiceDone(boolean choiceDone) {
        isChoiceDone = choiceDone;
    }

    public List<Integer> getOrderResult(){
        return mSelectedList;
    }

    public static class OrderResultListViewHolder extends RecyclerView.ViewHolder{
        public OrderResultListViewHolder(View itemView) {
            super(itemView);
        }
    }
}
