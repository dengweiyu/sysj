package com.ifeimo.im.common.adapter;

import android.database.Cursor;
import android.text.Spanned;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ifeimo.im.R;
import com.ifeimo.im.common.adapter.holder.Holder;
import com.ifeimo.im.common.bean.model.GroupChatModel;
import com.ifeimo.im.common.util.StringUtil;
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.database.Fields;
import com.ifeimo.im.framwork.commander.IMWindow;

import y.com.sqlitesdk.framework.business.BusinessUtil;

/**
 * Created by lpds on 2017/2/18.
 */
public class MuccChatReAdapter extends BaseChatReCursorAdapter<Holder,GroupChatModel> {

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
    protected GroupChatModel bindHolder(final Holder holder, Cursor cursor) {
        final GroupChatModel groupChatModel = BusinessUtil.getLineModelByCursor(GroupChatModel.class,cursor);
        holder.memberID = groupChatModel.getMemberId();
        holder.id_msgTime_layout.setVisibility(View.GONE);
        Spanned spanned = getSpanna(groupChatModel.getContent());
        if (!Proxy.getAccountManger().getUserMemberId().equals(groupChatModel.getMemberId())) {
            /// 如果是收到的消息，则显示左边的消息布局，将右边的消息布局隐藏
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(spanned);
            holder.leftName.setText(groupChatModel.getMemberNickName());
            String url = groupChatModel.getMemberAvatarUrl();
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
                    switch (IMSdk.versionCode) {
                        case 1:
                            if (Proxy.getMessageManager().getOnGroupItemOnClickListener() != null) {
                                Proxy.getMessageManager().getOnGroupItemOnClickListener().onGroupItemOnClick(groupChatModel.getMemberId(), groupChatModel.getMemberNickName());
                            }
                            break;
                        default:
                            IMSdk.createChat(activity.getContext(), groupChatModel.getMemberId(),
                                    groupChatModel.getMemberNickName(), groupChatModel.getMemberAvatarUrl());
                            break;
                    }
                }
            });

            holder.leftMsg.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(view.getContext(),"复制成功",Toast.LENGTH_SHORT).show();
                    StringUtil.copy(groupChatModel.getContent());
                    return true;
                }
            });

        } else {
            // 如果是收到发出的消息，则显示右边的消息布局，将左边的消息布局隐藏
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightMsg.setText(spanned);
            String url = groupChatModel.getMemberAvatarUrl();
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
            if (groupChatModel.getSendType() == Fields.GroupChatFields.SEND_UNCONNECT) {
                holder.reConnectIv.setVisibility(View.VISIBLE);
                holder.reConnectIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Proxy.getMessageManager().reSendMuccMsg(activity.getKey(), groupChatModel.clone());
                    }
                });
            } else if (groupChatModel.getSendType() == Fields.GroupChatFields.SEND_WAITING) {
                holder.id_process.setVisibility(View.VISIBLE);
            }

            holder.rightMsg.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(view.getContext(),"复制成功",Toast.LENGTH_SHORT).show();
                    StringUtil.copy(groupChatModel.getContent());
                    return true;
                }
            });

        }

        String formatTime = time.get(groupChatModel.getCreateTime());
        if (formatTime != null && !formatTime.equals("")) {
            holder.id_msgTime_layout.setVisibility(View.VISIBLE);
            holder.timeTv.setText(formatTime);
        }
        textViewCheck(holder, groupChatModel.getContent());
        return groupChatModel;
    }

}
