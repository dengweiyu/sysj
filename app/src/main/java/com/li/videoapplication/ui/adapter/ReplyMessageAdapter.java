package com.li.videoapplication.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.entity.GroupMessage;
import com.li.videoapplication.data.model.event.ReadMessageEntity;
import com.li.videoapplication.data.model.response.MessageListEntity;
import com.li.videoapplication.data.model.response.MessageReplyListEntity;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.activity.MessageListActivity;
import com.li.videoapplication.views.RoundedImageView;

import java.util.List;

import io.rong.eventbus.EventBus;

/**
 * 适配器：回复消息
 */
@SuppressLint("InflateParams")
public class ReplyMessageAdapter extends BaseArrayAdapter<MessageReplyListEntity.DataBean> {

    /**
     * 跳转：回复消息列表(再跳转一次获得回复消息数据)
     */
    private void startMessageListActivity(MessageReplyListEntity.DataBean item) {
        MessageListActivity.showActivity(getContext(),item.getTitle(),item.getInterface_url(),item.getSymbol());
    }

    private String mType;

    public ReplyMessageAdapter(Context context, List<MessageReplyListEntity.DataBean> data, String type) {
        super(context, R.layout.adapter_message_reply, data);
        mType = type;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final MessageReplyListEntity.DataBean record = getItem(position);
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_message_reply, null);
            holder.title = (TextView) view.findViewById(R.id.reply_message_title);
            holder.content = (TextView) view.findViewById(R.id.reply_message_content);
            holder.count = (TextView) view.findViewById(R.id.reply_message_count);
            holder.go = (ImageView) view.findViewById(R.id.reply_message_go);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setTextViewText(holder.title, record.getTitle());
        setTextViewText(holder.content, record.getContent());
        setCount(record, holder);

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startMessageListActivity(record);
                UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.SLIDER, "回复消息列表-" + record.getTitle());
            }
        });

        // 60
        setListViewLayoutParams(view, 58);

        return view;
    }

    /**
     * 消息是否已读
     */
    private void setCount(final MessageReplyListEntity.DataBean record, final ViewHolder holder) {

        if (record.getMark().equals("0")) {// 已读
            holder.count.setVisibility(View.GONE);
            holder.go.setVisibility(View.VISIBLE);
        } else {
            holder.count.setVisibility(View.VISIBLE);
            holder.go.setVisibility(View.GONE);
            holder.count.setText(record.getMark());
        }
    }

    private static class ViewHolder {
        RoundedImageView pic;
        TextView title;
        TextView content;
        TextView count;
        ImageView go;
    }
}

