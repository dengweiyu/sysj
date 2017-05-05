package com.ifeimo.im.common.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ifeimo.im.R;

/**
 * Created by lpds on 2017/2/24.
 */
public class InformationHolder extends RecyclerView.ViewHolder {

    public ImageView muc_left_face;
    public TextView muc_left_username;
    public TextView id_unreandcount_tv;
    public TextView id_time_tv;
    public TextView muc_left_msg;
    public View id_reSendIV;
    public View muc_left_layout;
    public View delet_view;


    public InformationHolder(View itemView) {
        super(itemView);
        id_reSendIV = itemView.findViewById(R.id.id_reSendIV);
        id_unreandcount_tv = (TextView) itemView.findViewById(R.id.id_unreandcount_tv);
        id_time_tv = (TextView) itemView.findViewById(R.id.id_time_tv);
        muc_left_face = (ImageView) itemView.findViewById(R.id.muc_left_face);
        muc_left_username = (TextView) itemView.findViewById(R.id.muc_left_username);
        muc_left_msg = (TextView) itemView.findViewById(R.id.muc_left_msg);
        muc_left_layout=  itemView.findViewById(R.id.muc_left_layout);
        delet_view = itemView.findViewById(R.id.delet_view);
    }

}
