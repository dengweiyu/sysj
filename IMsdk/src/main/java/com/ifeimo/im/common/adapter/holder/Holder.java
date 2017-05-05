package com.ifeimo.im.common.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ifeimo.im.framwork.view.RoundedImageView;

/**
 * Created by lpds on 2017/2/8.
 */
public class Holder extends RecyclerView.ViewHolder{
    public String memberID;
    public View leftLayout;
    public View rightLayout;
    public RoundedImageView leftFace;
    public RoundedImageView rightFace;
    public ImageView reConnectIv;
    public TextView leftName;
    public TextView leftMsg;
    public TextView rightMsg;
    public TextView timeTv;
    public View id_process;
    public View id_msgTime_layout;
    public View id_process_more;

    public Holder(View itemView) {
        super(itemView);
    }
}
