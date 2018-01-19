package com.li.videoapplication.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.MyMessage;
import com.li.videoapplication.data.model.entity.SysMessage;
import com.li.videoapplication.data.model.event.ReadMessageEntity;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.views.CircleImageView;

import io.rong.eventbus.EventBus;

/**
 * 适配器：系统消息
 */
@SuppressLint("InflateParams")
public class SystemMessageAdapter extends BaseArrayAdapter<SysMessage> {

    private String mType;

    public SystemMessageAdapter(Context context, List<SysMessage> data, String type) {
//		super(context, R.layout.adapter_systemmessage, data);
        super(context, R.layout.adapter_videomessage, data);
        mType = type;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final SysMessage record = getItem(position);
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_videomessage, null);
            holder.title = (TextView) view.findViewById(R.id.vedioMessage_title);
            holder.time = (TextView) view.findViewById(R.id.vedioMessage_time);
            holder.content = (TextView) view.findViewById(R.id.vedioMessage_content);
            holder.cover = (CircleImageView) view.findViewById(R.id.vedioMessage_pic);
            holder.count = (TextView) view.findViewById(R.id.vedioMessage_count);
            holder.go = (ImageView) view.findViewById(R.id.vedioMessage_go);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (record != null) {
            if (record.getExplain() == 1) {//推荐位
                setTextViewText(holder.title, "推荐位");
                setTextViewText(holder.content, record.getContent());
            }
            setImageViewImageNet(holder.cover, record.getCover());
            try {
                setTextViewText(holder.time, TimeHelper.getTime2MdFormat(record.getTime()));
            } catch (Exception e) {
                e.printStackTrace();
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
                    setCount(record, holder);
                    ActivityManager.startOrderDetailActivity(getContext(), record.getRelation_id(), 1);
                }
            });
        }
        setCount(record, holder);

        // 60
        setListViewLayoutParams(view, 60);

        return view;
    }

    /**
     * 消息是否已读
     */
    private void setCount(final SysMessage record, final SystemMessageAdapter.ViewHolder holder) {

        if (record.getMark().equals("1")) {// 未读
            holder.count.setVisibility(View.VISIBLE);
            holder.go.setVisibility(View.GONE);
        } else {
            holder.count.setVisibility(View.GONE);
            holder.go.setVisibility(View.VISIBLE);
        }
    }


    private static class ViewHolder {
        TextView title;
        TextView time;
        TextView content;
        CircleImageView cover;
        TextView count;
        ImageView go;
    }
}

