package com.li.videoapplication.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
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

import io.rong.eventbus.EventBus;

/**
 * 适配器：视频图文消息
 */
@SuppressLint("InflateParams")
public class VideoMessageAdapter extends BaseArrayAdapter<MyMessage> {

    /**
     * 跳转：玩家动态
     */
    private void startPlayerDynamicActivity(Member member) {
        ActivityManager.startPlayerDynamicActivity(getContext(), member);
    }

    /**
     * 跳转：视频播放
     */
    public void startVideoPlayActivity(VideoImage record) {
        ActivityManager.startVideoPlayActivity(getContext(), record);
    }

    /**
     * 跳转：图文详情
     */
    public void startImageDetailActivity(VideoImage record) {
        ActivityManager.startImageDetailActivity(getContext(), record);
    }


    private String mType;

    public VideoMessageAdapter(Context context, List<MyMessage> data,String type) {
        super(context, R.layout.adapter_videomessage, data);
        mType = type;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final MyMessage record = getItem(position);
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_videomessage, null);
            holder.pic = (CircleImageView) view.findViewById(R.id.vedioMessage_pic);
            holder.title = (TextView) view.findViewById(R.id.vedioMessage_title);
            holder.content = (TextView) view.findViewById(R.id.vedioMessage_content);
            holder.time = (TextView) view.findViewById(R.id.vedioMessage_time);
            holder.count = (TextView) view.findViewById(R.id.vedioMessage_count);
            holder.go = (ImageView) view.findViewById(R.id.vedioMessage_go);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setImageViewImageNet(holder.pic, record.getAvatar());
        setTextViewText(holder.title, record.getNickname());
        setTextViewText(holder.content, record.getContent());
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

                if (isVideo(record)) {// 视频
                    if (!StringUtil.isNull(record.getVideo_id()) &&
                            !record.getVideo_id().equals("0") &&
                            !record.isVideoIsDel()){
                        startVideoPlayActivity(getVideoImage(record));
                    } else {
                        ToastHelper.s("该视频已被删除");
                    }
                } else if (isImage(record)) { // 图文
                    startImageDetailActivity(getVideoImage(record));
                } else {// 个人中心
                    startPlayerDynamicActivity(getMember(record));
                }

                //
                ReadMessageEntity entity = new ReadMessageEntity();
                entity.setSymbol(mType);
                entity.setMsgId(record.getMsg_id());
                entity.setAll(0);
                EventBus.getDefault().post(entity);

                record.setMark("0");
                notifyDataSetChanged();

                UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.SLIDER, "视频消息-有效");
            }
        });

        // 60
        setListViewLayoutParams(view, 60);

        return view;
    }

    /**
     * 消息是否已读
     */
    private void setCount(final MyMessage record, final ViewHolder holder) {

        if (record.getMark().equals("1")) {// 未读
            holder.count.setVisibility(View.VISIBLE);
            holder.go.setVisibility(View.GONE);
        } else {
            holder.count.setVisibility(View.GONE);
            holder.go.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 是否是视频
     */
    private boolean isVideo(final MyMessage record) {
        // 视频
        if (!StringUtil.isNull(record.getVideo_id()) && !record.getVideo_id().equals("0")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否是图文
     */
    private boolean isImage(final MyMessage record) {
        // 图文
        if (!StringUtil.isNull(record.getPic_id()) && !record.getPic_id().equals("0")) {
            return true;
        } else {
            return false;
        }
    }

    private static class ViewHolder {
        CircleImageView pic;
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

