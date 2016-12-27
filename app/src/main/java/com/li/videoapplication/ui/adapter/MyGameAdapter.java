package com.li.videoapplication.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.WebActivity;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;
import com.li.videoapplication.views.RoundedImageView;

/**
 * 适配器：我的游戏，搜索游戏
 */
@SuppressLint("InflateParams")
public class MyGameAdapter extends BaseArrayAdapter<Game> {

    public static final int PAGE_MYGAME = 1;
    public static final int PAGE_SEARCHMEMBER = 2;

    private int page;

    /**
     * 跳转：圈子详情
     */
    private void startGameDetailActivity(Game item) {
        ActivityManeger.startGroupDetailActivity(getContext(), item.getGroup_id());
        UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.GAME, "我的游戏-游戏");
    }

    public MyGameAdapter(Context context, int page, List<Game> data) {
        super(context, R.layout.adapter_mygame, data);
        this.page = page;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {

        final Game record = getItem(position);
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_mygame, null);
            holder.title = (TextView) view.findViewById(R.id.mygame_title);
            holder.content = (TextView) view.findViewById(R.id.mygame_content);
            holder.mark = (TextView) view.findViewById(R.id.mygame_mark_num);
            holder.topic = (TextView) view.findViewById(R.id.mygame_topic);
            holder.remark = (TextView) view.findViewById(R.id.mygame_remark);
            holder.pic = (RoundedImageView) view.findViewById(R.id.mygame_pic);
            holder.go = (ImageView) view.findViewById(R.id.mygame_go);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.go.setVisibility(View.VISIBLE);
        if (page == PAGE_MYGAME)
            holder.go.setImageResource(R.drawable.go_home);
        if (page == PAGE_SEARCHMEMBER)
            holder.go.setImageResource(R.drawable.go_mytask);

        setTextViewText(holder.title, record.getGroup_name());
        setContent(holder.content, record);
        setMark(holder.mark, record);
        setTopic(holder.topic, record);
        setRemark(holder.remark, record);
        setImageViewImageNet(holder.pic, record.getFlag());

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!StringUtil.isNull(record.getUrl())) { //H5游戏
                    WebActivity.startWebActivity(getContext(), record.getUrl());
                } else {
                    startGameDetailActivity(record);
                }
            }
        });

        setListViewLayoutParams(view, 58);

        return view;
    }

    /**
     * 内容
     */
    private void setContent(TextView view, final Game record) {

        StringBuffer sb = new StringBuffer();
        String attention_num = record.getAttention_num();
        String video_num = record.getVideo_num();
        if (!StringUtil.isNull(video_num)) {
            sb.append("视频\t" + video_num);
        }
        if (!StringUtil.isNull(attention_num)) {
            sb.append("关注\t " + attention_num);
        }
        view.setText(sb.toString());
    }

    /**
     * 备注
     */
    private void setMark(TextView view, final Game record) {
        String num = record.getDay_data_num();
        long number = Long.valueOf(num);
//		String title = "今日新增内容：";
        if (StringUtil.isNull(num) || Integer.valueOf(num) == 0) {
            num = "0";
        } else {
//			if (title.length() == 1) {
//				title = title + "\t\t";
//			}
//			if (title.length() == 2) {
//				title = title + "\t";
//			}
            if (number >= 100) {
                num = "99+";
            }
        }
//		view.setText(Html.fromHtml(title + TextUtil.toColor(num, "#ff5f5d")));
        view.setText(num);
        view.setVisibility(View.VISIBLE);
    }

    /**
     * 话题
     */
    private void setTopic(TextView view, final Game record) {
        view.setText("话题\t" + StringUtil.formatNum(record.getVideo_num()));
    }

    /**
     * 关注
     */
    private void setRemark(TextView view, final Game record) {
        view.setText("关注\t" + StringUtil.formatNum(record.getAttention_num()));
    }

    private static class ViewHolder {
        TextView title;
        TextView content;// 话题 20  关注 201
        TextView mark;// 今日新增内容：16
        TextView topic;// 话题  20
        TextView remark;// 关注 201
        RoundedImageView pic;
        ImageView go;
    }
}

