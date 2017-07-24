package com.ifeimo.im.common.adapter;

import android.database.Cursor;
import android.text.Spanned;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ifeimo.im.R;
import com.ifeimo.im.common.adapter.holder.Holder;
import com.ifeimo.im.common.bean.AccountBean;
import com.ifeimo.im.common.bean.model.ChatMsgModel;
import com.ifeimo.im.common.util.StringUtil;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.database.Fields;
import com.ifeimo.im.framwork.commander.IMWindow;

import y.com.sqlitesdk.framework.business.BusinessUtil;

/**
 * Created by lpds on 2017/2/18.
 */
public class ChatReAdapter extends BaseChatReCursorAdapter<Holder,ChatMsgModel> {


    private AccountBean receiverBean;

    /**
     * Recommended constructor.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     * @param flags   Flags used to determine the behavior of the adapter;
     *                Currently it accept {@link #FLAG_REGISTER_CONTENT_OBSERVER}.
     */
    public ChatReAdapter(IMWindow context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public ChatMsgModel bindHolder(Holder holder, Cursor cursor) {
        final ChatMsgModel msgBean = BusinessUtil.getLineModelByCursor(ChatMsgModel.class,cursor);
        holder.id_msgTime_layout.setVisibility(View.GONE);

        Spanned spanned = getSpanna(msgBean.getContent());

        if (!Proxy.getAccountManger().getUserMemberId().equals(msgBean.getMemberId())) {
            /// 如果是收到的消息，则显示左边的消息布局，将右边的消息布局隐藏
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(spanned);
            holder.leftName.setText(receiverBean.getNickName());
            String url = receiverBean.getAvatarUrl();
            holder.leftFace.setTag(R.id.image_url, url);
            Glide.with(activity.getContext())
                    .load(url)
                    .dontAnimate()
                    .placeholder(R.drawable.actor)
                    .into(holder.leftFace);

            holder.leftMsg.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(view.getContext(),"复制成功",Toast.LENGTH_SHORT).show();
                    StringUtil.copy(msgBean.getContent());
                    return true;
                }
            });

        } else {
            // 如果是收到发出的消息，则显示右边的消息布局，将左边的消息布局隐藏
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightMsg.setText(spanned);
            String url = Proxy.getAccountManger().getAccount(false).getAvatarUrl();
            holder.leftFace.setTag(R.id.image_url, url);
            Glide.with(activity.getContext())
                    .load(url)
                    .dontAnimate()
                    .placeholder(R.drawable.actor)
                    .into(holder.rightFace);
            holder.id_process.setVisibility(View.GONE);
            holder.reConnectIv.setVisibility(View.GONE);
            if (msgBean.getSendType() == Fields.ChatFields.SEND_UNCONNECT) {
                holder.reConnectIv.setVisibility(View.VISIBLE);
                holder.reConnectIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Proxy.getMessageManager().reSendChatMsg(activity.getKey(), msgBean);
                    }
                });
            } else if (msgBean.getSendType() == Fields.ChatFields.SEND_WAITING) {
                holder.id_process.setVisibility(View.VISIBLE);
            }

            holder.rightMsg.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(view.getContext(),"复制成功",Toast.LENGTH_SHORT).show();
                    StringUtil.copy(msgBean.getContent());
                    return true;
                }
            });
        }

        return msgBean;

    }

    public AccountBean getReceiverBean() {
        return receiverBean;
    }

    public void setReceiverBean(AccountBean receiverBean) {
        this.receiverBean = receiverBean;
    }
}


