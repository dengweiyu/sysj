package com.ifeimo.im.common.adapter;

import android.database.Cursor;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ifeimo.im.R;
import com.ifeimo.im.common.adapter.holder.Holder;
import com.ifeimo.im.common.bean.MsgBean;
import com.ifeimo.im.common.bean.MuccMsgBean;
import com.ifeimo.im.common.bean.UserBean;
import com.ifeimo.im.common.bean.chat.MuccBean;
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.database.Fields;
import com.ifeimo.im.framwork.interface_im.IMWindow;
import com.ifeimo.im.provider.BaseProvider;

/**
 * Created by lpds on 2017/2/18.
 */
public class MuccChatReAdapter extends BaseChatReCursorAdapter<Holder> {

    /**
     * Recommended constructor.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     * @param flags   Flags used to determine the behavior of the adapter;
     *                Currently it accept {@link #FLAG_REGISTER_CONTENT_OBSERVER}.
     */
    public MuccChatReAdapter(IMWindow context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    protected MsgBean bindHolder(final Holder holder, Cursor cursor) {
        final MuccMsgBean muccMsgBean = MuccMsgBean.createLineByCursor(cursor);
        holder.memberID = muccMsgBean.getMemberId();
        holder.id_msgTime_layout.setVisibility(View.GONE);
        if (!UserBean.getMemberID().equals(muccMsgBean.getMemberId())) {
            /// 如果是收到的消息，则显示左边的消息布局，将右边的消息布局隐藏
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(muccMsgBean.getContent());
            holder.leftName.setText(muccMsgBean.getMemberNickName());
            String url = muccMsgBean.getMemberAvatarUrl();
            holder.leftFace.setTag(R.id.image_url, url);
            Glide.with(activity.getContext())
                    .load(url)
                    .dontAnimate()
                    .placeholder(R.drawable.actor)
//                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            holder.leftFace.setImageDrawable(resource);
                        }
                    });
            holder.leftFace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (IMSdk.versionCode){
                        case 1:
                            if(Proxy.getMessageManager().getOnGroupItemOnClickListener()!=null) {
                                Proxy.getMessageManager().getOnGroupItemOnClickListener().onGroupItemOnClick(muccMsgBean.getMemberId(), muccMsgBean.getMemberNickName());
                            }
                            break;
                        default:
                            IMSdk.createChat(activity.getContext(), muccMsgBean.getMemberId(),
                                    muccMsgBean.getMemberNickName(), muccMsgBean.getMemberAvatarUrl());
                            break;
                    }
                }
            });
        } else {
            // 如果是收到发出的消息，则显示右边的消息布局，将左边的消息布局隐藏
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightMsg.setText(muccMsgBean.getContent());
            String url = muccMsgBean.getMemberAvatarUrl();
            holder.leftFace.setTag(R.id.image_url, url);
            Glide.with(activity.getContext())
                    .load(url)
                    .dontAnimate()
                    .placeholder(R.drawable.actor)
                    .into(holder.rightFace);
            holder.leftFace.setOnClickListener(null);
            holder.leftFace.setOnLongClickListener(null);
            holder.id_process.setVisibility(View.GONE);
            holder.reConnectIv.setVisibility(View.GONE);
            if (muccMsgBean.getSendType() == Fields.GroupChatFields.SEND_UNCONNECT) {
                holder.reConnectIv.setVisibility(View.VISIBLE);
                holder.reConnectIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Proxy.getMessageManager().reSendMuccMsg(activity.getKey(), (MuccBean) activity.getBean(),muccMsgBean);
                    }
                });
            } else if (muccMsgBean.getSendType() == Fields.GroupChatFields.SEND_WAITING) {
                holder.id_process.setVisibility(View.VISIBLE);
            }
        }

        String formatTime = time.get(muccMsgBean.getCreateTime());
        if (formatTime != null && !formatTime.equals("")) {
            holder.id_msgTime_layout.setVisibility(View.VISIBLE);
            holder.timeTv.setText(formatTime);
        }
        return muccMsgBean;
    }

}
