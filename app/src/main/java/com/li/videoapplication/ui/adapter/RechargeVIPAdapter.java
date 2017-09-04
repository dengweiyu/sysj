package com.li.videoapplication.ui.adapter;

import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.response.VipRechargeEntity;
import com.li.videoapplication.framework.CommonAdapter;
import com.li.videoapplication.framework.ViewHolder;


import java.util.List;

/**
 *
 */
public class RechargeVIPAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<VipRechargeEntity.DataBean> data;
    // 默认选中第一个
    private int currentPos = 0;

    private int color_999999;
    private int color_333333;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    public RechargeVIPAdapter(Context context, List<VipRechargeEntity.DataBean> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        color_999999 = ActivityCompat.getColor(context, R.color.color_999999);
        color_333333 = ActivityCompat.getColor(context,R.color.color_333333);
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_recharge_vip, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            setIsSelected(holder, position);
            setIcon(holder, position);
            setListView(holder, position);
            ((ItemViewHolder) holder).root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClick(v, holder.getAdapterPosition());
                        currentPos = holder.getAdapterPosition();

                        notifyDataSetChanged();
                    }
                }
            });
            final String price = data.get(position).getPrice() + "元/月";
            ((ItemViewHolder) holder).id_month_price.setText(price);
            ((ItemViewHolder) holder).vip_listview.setClickable(false);
            ((ItemViewHolder) holder).vip_listview.setEnabled(false);
            ((ItemViewHolder) holder).vip_listview.setFocusable(false);
            ((ItemViewHolder) holder).vip_listview.setFocusableInTouchMode(false);
        }
    }

    /**
     * 根据vip设置icon
     */
    private void setIcon(RecyclerView.ViewHolder holder, int position){
        int vip_level = data.get(position).getLevel();
        if(position == currentPos){
            ((ItemViewHolder) holder).id_desc_vip.setTextColor(color_333333);
            ((ItemViewHolder) holder).id_month_price.setTextColor(color_333333);
        }else{
            ((ItemViewHolder) holder).id_desc_vip.setTextColor(color_999999);
            ((ItemViewHolder) holder).id_month_price.setTextColor(color_999999);
        }
        if (vip_level == 1){ // vip1
            ((ItemViewHolder) holder).id_desc_vip.setText(R.string.vip1);
        } else if (vip_level == 2){ // vip2
            ((ItemViewHolder) holder).id_desc_vip.setText(R.string.vip2);
        } else if (vip_level == 3){ // vip3
            ((ItemViewHolder) holder).id_desc_vip.setText(R.string.vip3);
        }

    }


    /**
     * 设置是否选中
     */
    private void setIsSelected(RecyclerView.ViewHolder holder, int position) {
        if (currentPos == position) {
            ((ItemViewHolder) holder).selected_icon.setImageResource(R.drawable.pay_checkbox_select);
        } else {
            ((ItemViewHolder) holder).selected_icon.setImageResource(R.drawable.pay_checkbox_unselect);
        }
    }

    /**
     * 设置ListView
     */
    private void setListView(RecyclerView.ViewHolder holder, final int p) {
        ((ItemViewHolder) holder).vip_listview.setAdapter(new CommonAdapter<String>(context,
                data.get(p).getDescription(),R.layout.adapter_vip_desc_1) {
            @Override
            public void convert(ViewHolder holder, String s, int position) {
                if(p > currentPos){
                    ((TextView)holder.getView(R.id.vip_desc_text)).
                            setTextColor(ActivityCompat.getColor(context,R.color.color_999999));
                }else{
                    ((TextView)holder.getView(R.id.vip_desc_text)).
                            setTextColor(ActivityCompat.getColor(context,R.color.color_333333));
                }
                holder.setText(R.id.vip_desc_text,s);
            }
        });
    }

    /**
     * 设置点击监听器
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private View root;
        private ImageView selected_icon;
        private TextView id_month_price;
        private TextView id_desc_vip;
        private ListView vip_listview;

        public ItemViewHolder(View view) {
            super(view);

            root = view.findViewById(R.id.vip_root);
            vip_listview = (ListView) view.findViewById(R.id.vip_listview);
            selected_icon = (ImageView) view.findViewById(R.id.vip_selected_icon);
            id_desc_vip = (TextView) view.findViewById(R.id.id_desc_vip);
            id_month_price = (TextView) view.findViewById(R.id.id_month_price);
        }
    }
}
