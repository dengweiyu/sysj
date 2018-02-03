package com.li.videoapplication.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.GroupVideoReplyMessage;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.MyMessage;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.event.ReadMessageEntity;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.CircleImageView;

import java.util.List;

import io.rong.eventbus.EventBus;

/**
 * 适配器：游戏圈回复消息
 */
@SuppressLint("InflateParams")
public class ReplyMessageGameGroupAdapter extends BaseArrayAdapter<GroupVideoReplyMessage> {

    private String mType;

    public ReplyMessageGameGroupAdapter(Context context, List<GroupVideoReplyMessage> data, String type) {
        super(context, R.layout.adapter_msg_reply_video_group, data);
        mType = type;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final GroupVideoReplyMessage record = getItem(position);
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_msg_reply_video_group, null);
            holder.avatar = (CircleImageView) view.findViewById(R.id.civ_avatar);
            holder.title = (TextView) view.findViewById(R.id.tv_title);
            holder.content = (TextView) view.findViewById(R.id.tv_content);
            holder.time = (TextView) view.findViewById(R.id.tv_time);
            holder.count = (TextView) view.findViewById(R.id.tv_count);
            holder.go = (ImageView) view.findViewById(R.id.iv_go);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setImageViewImageNet(holder.avatar, record.getIcon());
        setTextViewText(holder.title, record.getMember_name());
        setTextViewText(holder.content, record.getOriginal_content());
        setCount(record, holder);
        try {
            setTextViewText(holder.time, TimeHelper.getWholeTimeFormat(record.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
            setTextViewText(holder.time, "");
        }

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ReadMessageEntity entity = new ReadMessageEntity();
                entity.setSymbol(mType);
                entity.setMsgId(record.getMsg_id());
                entity.setAll(0);
                EventBus.getDefault().post(entity);

                record.setMark("0");
                notifyDataSetChanged();

                ActivityManager.startMessageReplyActivity(getContext(), record.getRelid());
                UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.SLIDER, "游戏圈回复消息列表");
            }
        });

        // 60
        setListViewLayoutParams(view, 60);

        return view;
    }

    /**
     * 消息是否已读
     */
    private void setCount(final GroupVideoReplyMessage record, final ViewHolder holder) {

        if (!record.getMark().equals("0")) {// 未读
            holder.count.setVisibility(View.VISIBLE);
            holder.go.setVisibility(View.GONE);
        } else {
            holder.count.setVisibility(View.GONE);
            holder.go.setVisibility(View.VISIBLE);
        }
    }


    private static class ViewHolder {
        CircleImageView avatar;
        TextView title;
        TextView content;
        TextView time;
        TextView count;
        ImageView go;
    }

    private VideoImage getVideoImage(MyMessage record) {
        VideoImage item = new VideoImage();
        item.setMember_id(record.getMember_id());
        item.setAvatar(record.getAvatar());
        item.setNickname(record.getNickname());
        item.setVideo_id(record.getVideo_id());
        item.setId(record.getVideo_id());
        item.setPic_id(record.getPic_id());
        return item;
    }

    private Member getMember(MyMessage record) {
        Member item = new Member();
        item.setId(record.getMember_id());
        item.setMember_id(record.getMember_id());
        item.setName(record.getNickname());
        item.setNickname(record.getNickname());
        item.setAvatar(record.getAvatar());
        return item;
    }
}

