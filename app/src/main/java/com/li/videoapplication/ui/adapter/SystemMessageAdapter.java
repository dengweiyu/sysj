package com.li.videoapplication.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.SysMessage;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.views.CircleImageView;

/**
 * 适配器：系统消息
 */
@SuppressLint("InflateParams")
public class SystemMessageAdapter extends BaseArrayAdapter<SysMessage> {

    public SystemMessageAdapter(Context context, List<SysMessage> data) {
//		super(context, R.layout.adapter_systemmessage, data);
        super(context, R.layout.adapter_videomessage, data);
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
                    ActivityManager.startOrderDetailActivity(getContext(), record.getRelation_id(), 1);
                }
            });
        }
        holder.count.setVisibility(View.GONE);
        holder.go.setVisibility(View.VISIBLE);
        // 60
        setListViewLayoutParams(view, 60);

        return view;
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

