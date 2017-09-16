package com.li.videoapplication.mvp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.entity.PaymentList;
import com.li.videoapplication.framework.BaseBaseAdapter;

import java.util.List;

/**
 * Created by liuwei on 2017/4/5.
 */

public class PaymentLisAdapter extends BaseBaseAdapter {
   private Context mContext;
   private List<PaymentList.DataBean> mData;
   private String mSelectedPayId;
   private int mSelectPosition = 0;
   public PaymentLisAdapter(Context context,List<PaymentList.DataBean> data){
       mContext = context;
       mData = data;
   }

    @Override
    protected Context getContext() {
        return mContext;
    }

    @Override
    public int getCount() {
        return null == mData ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_payment_way,null);
        TextView type = (TextView)view.findViewById(R.id.tv_payment_name);
        TextView note = (TextView)view.findViewById(R.id.tv_payment_note);
        ImageView icon = (ImageView)view.findViewById(R.id.iv_payment_icon);
        ImageView click = (ImageView)view.findViewById(R.id.iv_click_choice);

        PaymentList.DataBean dataBean = mData.get(position);
        type.setText(dataBean.getPay_name());
        note.setText(dataBean.getNote());
        view.setTag(position);
        click.setTag(position);

        GlideHelper.displayImage(mContext,dataBean.getIcon(),icon);

        if (position == mSelectPosition){
            click.setImageResource(R.drawable.select);
            mSelectedPayId = mData.get(position).getPay_type();
        }else {
            click.setImageResource(R.drawable.un_select);
        }
        view.setOnClickListener(mListener);
        click.setOnClickListener(mListener);
        return view;
    }

    public void onRefresh(List<PaymentList.DataBean> data){
        mData = data;
        notifyDataSetChanged();
    }

    final View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int)v.getTag();
            mSelectedPayId = mData.get(position).getPay_type();
            onSelectedChange(position);
        }
    };

    public String getSelectedPayId(){
        return mSelectedPayId;
    }

    private void onSelectedChange(int position){
        mSelectPosition = position;
        notifyDataSetChanged();
    }
}
