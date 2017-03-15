package com.ifeimo.im.common.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ifeimo.im.R;
import com.ifeimo.im.common.adapter.holder.InformationHolder;
import com.ifeimo.im.common.adapter.base.RecyclerViewCursorAdapter;
import com.ifeimo.im.common.bean.InformationBean;
import com.ifeimo.im.common.util.ThreadUtil;
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.database.Fields;
import com.ifeimo.im.framwork.database.business.Business;
import com.ifeimo.im.provider.InformationProvide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lpds on 2017/2/24.
 */
public class InformationAdapter extends RecyclerViewCursorAdapter<InformationHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private OnAdapterItemOnClickListener onAdapterItemOnClickListener;


    public void setOnAdapterItemOnClickListener(OnAdapterItemOnClickListener onAdapterItemOnClickListener) {
        this.onAdapterItemOnClickListener = onAdapterItemOnClickListener;
    }

    public InformationAdapter(Context context) {
        super(context, null, 1);
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    /**
     * Recommended constructor.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     * @param flags   Flags used to determine the behavior of the adapter;
     *                Currently it accept {@link #FLAG_REGISTER_CONTENT_OBSERVER}.
     */
    private InformationAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public void onBindViewHolder(InformationHolder holder, Cursor cursor) {

        final InformationBean infomationBean = InformationBean.buildInfomationMsgItemByCursor(cursor);
        holder.muc_left_username.setText(infomationBean.getTitle());
        holder.muc_left_msg.setText(infomationBean.getLastContent());
        holder.id_time_tv.setText(getConvertTime(infomationBean.getLastCreateTime()));

        if (infomationBean.getUnread() <= 0) {
            holder.id_unreandcount_tv.setVisibility(View.GONE);
        } else {
            holder.id_unreandcount_tv.setVisibility(View.VISIBLE);
            holder.id_unreandcount_tv.setText( infomationBean.getUnread() + "");
        }
        Glide.with(context)
                .load(infomationBean.getPicUrl())
                .dontAnimate()
                .placeholder(R.drawable.logo_round)
                .into(holder.muc_left_face);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final InformationBean clonBean = InformationBean.clon(infomationBean);
                if(onAdapterItemOnClickListener == null || onAdapterItemOnClickListener.onItemOnClick(clonBean)) {
                    try {
                        switch (clonBean.getType()) {
                            case InformationBean.ROOM:
                                IMSdk.createMuccRoom(context, clonBean.getOppositeId(), clonBean.getTitle(),clonBean.getPicUrl());
                                break;
                            case InformationBean.CHAT:
                                IMSdk.createChat(context, clonBean.getOppositeId(), clonBean.getTitle(), clonBean.getPicUrl());
                                break;
                            case InformationBean.Advertisement:
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    return;
                }
            }
        });

        if(infomationBean.getSendType() == Fields.MsgFields.SEND_FINISH){
            holder.id_reSendIV.setVisibility(View.GONE);
        }else{
            holder.id_reSendIV.setVisibility(View.VISIBLE);
        }

    }

    @Deprecated
    private void clearPoint(final int id) {

//        ThreadUtil.getInstances().createThreadStartToCachedThreadPool(new Runnable() {
//            @Override
//            public void run() {
//                Business.getInstances().cancelInfomationById(id);
//            }
//        });


    }

    @Override
    protected void onContentChanged() {

    }

    @Override
    public InformationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.item_contract, null);
        InformationHolder holder = new InformationHolder(v);
        return holder;
    }

    public String getConvertTime(String lasttime) {
        if (lasttime == null || lasttime.equals("")) {
            return "";
        }
        long lTime = Long.parseLong(lasttime);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = null;
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        if (calendar.getTimeInMillis() > lTime) {
            if (lTime > calendar.getTimeInMillis() - 1000 * 60 * 60 * 24 &&
                    lTime > calendar.getTimeInMillis() - 1000 * 60 * 60 * 24 * 2) {
                sdf = new SimpleDateFormat("昨天 HH:mm");
            } else {
                sdf = new SimpleDateFormat("yyyy/MM/dd");
            }
        } else {
            sdf = new SimpleDateFormat("HH:mm");
        }
//        sdf.setNumberFormat(NU);
        return sdf.format(new Date(lTime));
    }
}
