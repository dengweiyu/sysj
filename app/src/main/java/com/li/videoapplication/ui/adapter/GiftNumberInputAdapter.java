package com.li.videoapplication.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.collect.Iterators;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.response.PlayGiftTypeEntity;
import com.li.videoapplication.ui.srt.SRTInfo;
import com.li.videoapplication.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 礼物数量选择适配器
 */

public class GiftNumberInputAdapter extends BaseAdapter {
    private Context mContext;
    private int mHeight;
    private int mWidth;
    private SizeListener mListener;
    private List<PlayGiftTypeEntity.NumberSenseBean> mData;
    public GiftNumberInputAdapter(Context context, SizeListener listener,List<PlayGiftTypeEntity.NumberSenseBean> data){

        mContext = context;
        mListener = listener;
        mData = new ArrayList<>();
        if (data != null){
            for (int i = data.size() - 1; i >= 0 ; i--) {
                mData.add(data.get(i));
            }
        }
    }

    @Override
    public int getCount() {
        return mData == null?0:mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.gift_number_item,null);
        convertView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        if (mWidth == 0){
            mWidth = convertView.getMeasuredWidth();
        }

        if (mHeight == 0){
            mHeight =convertView.getMeasuredHeight();
            if (mListener != null){
                int size = mData.size();
                //加上分割线
                mListener.onListener(mHeight*size+size+ 1,mWidth+2);
            }
        }

        TextView  num = (TextView) convertView.findViewById(R.id.tv_gift_num);
        TextView  description = (TextView) convertView.findViewById(R.id.tv_gift_description);
        num.setText(mData.get(position).getNumber());
        description.setText(mData.get(position).getTitle());

        return convertView;
    }


    public interface SizeListener{
        void onListener(int h,int w);
    }
}
