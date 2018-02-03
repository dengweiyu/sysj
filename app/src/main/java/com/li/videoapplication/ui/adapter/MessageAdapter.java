package com.li.videoapplication.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.response.MessageListEntity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.tools.GameGroupMsgCountHelper;
import com.li.videoapplication.tools.TimeHelper;

/**
 * 适配器：消息
 */
@SuppressLint({"InflateParams", "ViewHolder"})
public class MessageAdapter extends BaseArrayAdapter<MessageListEntity.DataBean> {

    private Context mContext;

    public MessageAdapter(Context context, List<MessageListEntity.DataBean> data) {
        super(context, R.layout.adapter_message, data);
        mContext = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final MessageListEntity.DataBean data = getItem(position);
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_message, null);
            holder.title = (TextView) view.findViewById(R.id.message_name);
            holder.pic = (ImageView) view.findViewById(R.id.message_pic);
            holder.go = (ImageView) view.findViewById(R.id.message_go);
            holder.count = (TextView) view.findViewById(R.id.tv_unread_message_count);
            holder.content = (TextView) view.findViewById(R.id.tv_message_content);
            holder.time = (TextView) view.findViewById(R.id.tv_message_time);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        setTextViewText(holder.content, data.getContent());
        setTextViewText(holder.title, data.getTitle());

        GlideHelper.displayImage(mContext, data.getCover(), holder.pic);
        UITask.postDelayed(new Runnable() {
            @Override
            public void run() {
                GlideHelper.displayImage(mContext, data.getCover(), holder.pic);
            }
        }, 200);
        setCount(data, holder.count);

        setListViewLayoutParams(view, 58);

        try {
            setTextViewText(holder.time, TimeHelper.getMyMessageTime(data.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 设置红点
     */
    private void setCount(MessageListEntity.DataBean data, TextView view) {

        view.setText("");
        int mark = 0;

        try {
            mark = Integer.parseInt(data.getMark_num());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (data.getSymbol().equals("group"))
            mark = mark - GameGroupMsgCountHelper.getInstance().isReadAmount();

        view.setText(mark + "");
        if (mark > 0) {
//			setTextViewTextVisibility(view, String.valueOf(record.getFileCount()));
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    private static class ViewHolder {

        TextView title;
        ImageView go;
        TextView count;
        ImageView pic;
        TextView content;
        TextView time;
    }

    public static class Message {

        private String content;

        private int pic;

        private int count;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getPic() {
            return pic;
        }

        public void setPic(int pic) {
            this.pic = pic;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
