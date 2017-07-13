package com.ifeimo.im.common.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import com.bumptech.glide.Glide;
import com.ifeimo.im.R;
import com.ifeimo.im.common.adapter.base.RecyclerViewCursorAdapter;
import com.ifeimo.im.common.adapter.holder.InformationHolder;
import com.ifeimo.im.common.bean.chat.GroupChatBean;
import com.ifeimo.im.common.bean.model.InformationModel;
import com.ifeimo.im.common.util.ScreenUtil;
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.database.Fields;
import com.ifeimo.im.framwork.view.SlideView;
import com.ifeimo.im.provider.business.ChatBusiness;
import com.ifeimo.im.provider.business.GroupChatBusiness;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import y.com.sqlitesdk.framework.business.BusinessUtil;
import y.com.sqlitesdk.framework.db.Access;
import y.com.sqlitesdk.framework.sqliteinterface.Execute;

/**
 * Created by lpds on 2017/2/24.
 */
public class InformationAdapter extends RecyclerViewCursorAdapter<InformationHolder> implements View.OnTouchListener {
    private static final String TAG = "XMPP_InformationAdapter";
    private LayoutInflater layoutInflater;
    private Context context;
    private OnAdapterItemOnClickListener onAdapterItemOnClickListener;
    private View currentView;

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
    public void onBindViewHolder(final InformationHolder holder, Cursor cursor) {

        final InformationModel informationModel = BusinessUtil.getLineModelByCursor(InformationModel.class, cursor);
        holder.muc_left_username.setText(informationModel.getTitle());
        holder.muc_left_msg.setText(informationModel.getLastContent());
        holder.id_time_tv.setText(getConvertTime(informationModel.getLastCreateTime()));
        holder.delet_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteDialog(informationModel);
            }
        });
        if (informationModel.getUnread() <= 0) {
            holder.id_unreandcount_tv.setVisibility(View.GONE);
        } else {
            holder.id_unreandcount_tv.setVisibility(View.VISIBLE);
            holder.id_unreandcount_tv.setText(informationModel.getUnread() > 99 ? 99 + "+" : informationModel.getUnread() + "");
        }
        holder.itemView.setOnTouchListener(this);
        Glide.with(context)
                .load(informationModel.getPicUrl())
                .dontAnimate()
                .placeholder(R.drawable.logo_round)
                .into(holder.muc_left_face);
        holder.muc_left_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: item");
                if (setInitializeDelete()) {
                    return;
                }
                if (onAdapterItemOnClickListener == null || onAdapterItemOnClickListener.onItemOnClick(informationModel)) {
                    try {
                        switch (informationModel.getType()) {
                            case InformationModel.ROOM:
                                IMSdk.createMuccRoom(context, informationModel.getOppositeId(), informationModel.getTitle(), informationModel.getPicUrl());
                                break;
                            case InformationModel.CHAT:
                                IMSdk.createChat(context, informationModel.getOppositeId(), informationModel.getTitle(), informationModel.getPicUrl());
                                break;
                            case InformationModel.Advertisement:
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        if (informationModel.getSendType() == Fields.MsgFields.SEND_FINISH) {
            holder.id_reSendIV.setVisibility(View.GONE);
        } else {
            holder.id_reSendIV.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onContentChanged() {

    }

    @Override
    public InformationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = new SlideView(context);
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (currentView != view) {
            setInitializeDelete();
        }
        currentView = view;
        return false;
    }

    /**
     * 设置回滚
     */
    public boolean setInitializeDelete() {
        boolean flag = false;
        if (currentView != null && currentView instanceof SlideView) {
            SlideView slideView = (SlideView) currentView;
            slideView.fullScroll(HorizontalScrollView.FOCUS_UP);
            currentView = null;
            flag = true;
        }
        return flag;
    }


    private void deleteInformation(final InformationModel informationModel) {
        switch (informationModel.getType()) {
            case InformationModel.ROOM:
                GroupChatBusiness.getInstances().deleteInformation(informationModel);
                break;
            case InformationModel.CHAT:
                ChatBusiness.getInstances().deleteInformation(informationModel);
                break;
        }

    }


    private void showDeleteDialog(final InformationModel informationModel) {
        new AlertDialog.Builder(context).setTitle("删除消息").setMessage("确定删除此消息？").
                setNeutralButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteInformation(informationModel);
                    }
                }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setCancelable(true).show();
    }


}
