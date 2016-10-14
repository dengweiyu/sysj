package com.li.videoapplication.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.GroupListActivity;

/**
 * 适配器：圈子
 */
@SuppressLint("InflateParams")
public class GroupListAdapter extends BaseArrayAdapter<Game> {

    /**
     * 跳转：圈子详情
     */
    private void startGroupDetailActivity(Game item) {
        ActivityManeger.startGroupDetailActivity(getContext(), item.getGroup_id());
    }

    public GroupListAdapter(Context context, List<Game> data) {
        super(context, R.layout.adapter_grouplist, data);
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {

        final Game record = getItem(position);
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_grouplist, null);
            holder.title = (TextView) view.findViewById(R.id.grouplist_title);
            holder.left = (TextView) view.findViewById(R.id.grouplist_left);
            holder.right = (TextView) view.findViewById(R.id.grouplist_right);
            holder.pic = (ImageView) view.findViewById(R.id.grouplist_pic);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setListViewLayoutParams(view, 58);

        setTextViewText(holder.title, record.getGroup_name());
        setImageViewImageNet(holder.pic, record.getFlag());
        setTopic(holder.left, record);
        setRemark(holder.right, record);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startGroupDetailActivity(record);
                try {
                    GroupListActivity activity = (GroupListActivity) getContext();
                    UmengAnalyticsHelper.onGameMoreEvent(getContext(), activity.groupType.getGroup_type_id());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    /**
     * 话题
     */
    private void setTopic(TextView view, final Game record) {
        view.setText("话题\t" + record.getVideo_num());
    }

    /**
     * 关注
     */
    private void setRemark(TextView view, final Game record) {
        view.setText("关注\t" + record.getAttention_num());
    }

    private static class ViewHolder {
        TextView title;
        TextView content;// 视频 2020 关注 201
        TextView left;
        TextView right;
        ImageView pic;
    }
}
