package com.li.videoapplication.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.BaseArrayAdapter;

import java.util.List;

/**
 *
 */

public class ChoiceCoachGameListAdapter  extends BaseAdapter {

    private int mItemWidth;

    private Context mContext;

    private List<String> mData;


    public ChoiceCoachGameListAdapter(Context context,List<String> data,int width) {
        mItemWidth = width;
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData == null? 0:mData.size();
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

        convertView = LayoutInflater.from(mContext).inflate(R.layout.choice_coach_game_item,null);

        TextView gameName = (TextView) convertView.findViewById(R.id.tv_choice_game_type);
        gameName.setText(mData.get(position));


        if (position == 0){
            convertView.findViewById(R.id.iv_arrow).setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}
