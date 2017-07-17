package com.li.videoapplication.ui.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.li.videoapplication.ui.view.WheelRecyclerView;
import com.li.videoapplication.R;
import java.util.List;

/**
 * 滚轮选择器 适配器
 */

public class SimpleChoiceAdapter extends WheelRecyclerView.WheelRecyclerViewAdapter{
    private List<String> mData;
    private Context mContext;
    public SimpleChoiceAdapter(Context context,WheelRecyclerView list,List<String> data) {
        super(list);
        mData = data;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.wheet_recycler_view_item,parent,false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        if (position < mList.getItemHolder() || position >= mData.size() + mList.getItemHolder()){
            holder.itemView.setVisibility(View.INVISIBLE);
            return;
        }

        int dataIndex = position-mList.getItemHolder();
        if (holder.itemView.getVisibility() == View.VISIBLE ){
            TextView content  = ((TextView)holder.itemView.findViewById(R.id.tv_item_content));
            content.setText(mData.get(dataIndex));
            content.setTextColor(mContext.getResources().getColor(R.color.textcolor_french_gray));
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0:mData.size()+super.getItemCount();
    }

    @Override
    public int getRootViewId() {
        return R.id.ll_item_root;
    }

    class SimpleViewHolder extends RecyclerView.ViewHolder{
        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }
}
