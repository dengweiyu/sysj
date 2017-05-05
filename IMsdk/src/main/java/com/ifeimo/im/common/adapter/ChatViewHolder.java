package com.ifeimo.im.common.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ifeimo.im.framwork.view.RoundedImageView;

/**
 * Created by lpds on 2017/2/8.
 */
public class ChatViewHolder {
    String memberID;
    View leftLayout;
    View rightLayout;
    RoundedImageView leftFace;
    RoundedImageView rightFace;
    ImageView reConnectIv;
    TextView leftName;
    TextView leftMsg;
    TextView rightMsg;
    TextView timeTv;
    View id_process;
    View id_msgTime_layout;
}
