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
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.views.RoundedImageView;

import java.util.List;

import io.rong.eventbus.EventBus;

/**
 * 适配器：圈子消息
 */
@SuppressLint("InflateParams")
public class GameMessageAdapter extends BaseArrayAdapter<GroupMessage> {

    /**
     * 跳转：圈子详情
     */
    private void startGroupDetailActivity(Game item) {
        ActivityManager.startGroupDetailActivity(getContext(), item.getGroup_id());
    }

    private String mType;

    public GameMessageAdapter(Context context, List<GroupMessage> data,String type) {
        super(context, R.layout.adapter_gamemessage, data);
        mType = type;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final GroupMessage record = getItem(position);
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_gamemessage, null);
            holder.pic = (RoundedImageView) view.findViewById(R.id.gameMessage_pic);
            holder.title = (TextView) view.findViewById(R.id.gameMessage_title);
            holder.content = (TextView) view.findViewById(R.id.gameMessage_content);
            holder.count = (TextView) view.findViewById(R.id.gameMessage_count);
            holder.go = (ImageView) view.findViewById(R.id.gameMessage_go);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setTextViewText(holder.title, record.getGroup_name());
        setTextViewText(holder.content, record.getLate_data());
        setImageViewImageNet(holder.pic, record.getFlag());
        setCount(record, holder);

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startGroupDetailActivity(getGame(record));
                //
                ReadMessageEntity entity = new ReadMessageEntity();
                entity.setSymbol(mType);
                entity.setMsgId(record.getMsg_id());
                entity.setAll(0);
                EventBus.getDefault().post(entity);

                record.setNew_data_num("0");
                setCount(record, holder);
                UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.SLIDER, "圈子消息-有效");
            }
        });

        // 60
        setListViewLayoutParams(view, 58);

        return view;
    }

    /**
     * 消息是否已读
     */
    private void setCount(final GroupMessage record, final ViewHolder holder) {

        if (record.getNew_data_num().equals("0")) {// 已读
            holder.count.setVisibility(View.GONE);
            holder.go.setVisibility(View.VISIBLE);
        } else {
            holder.count.setVisibility(View.VISIBLE);
            holder.go.setVisibility(View.GONE);
        }
    }

    private Game getGame(GroupMessage record) {
        Game item = new Game();
        item.setGroup_id(record.getGroup_id());
        item.setGroup_name(record.getGroup_name());
        item.setFlag(record.getFlag());
        item.setGame_id(record.getGame_id());
        return item;
    }

    private static class ViewHolder {
        RoundedImageView pic;
        TextView title;
        TextView content;
        TextView count;
        ImageView go;
    }
}

