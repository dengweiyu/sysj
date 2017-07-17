package com.li.videoapplication.mvp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.event.ReadMessageEntity;
import com.li.videoapplication.data.model.response.RewardAndPlayWithMsgEntity;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.activity.MessageListActivity;
import com.li.videoapplication.ui.activity.PlayWithOrderDetailActivity;
import com.li.videoapplication.views.CircleImageView;

import java.util.List;

import io.rong.eventbus.EventBus;

/**
 * 打赏或陪练消息
 */

public class RewardAndPlayWithAdapter extends BaseArrayAdapter<RewardAndPlayWithMsgEntity.DataBean.ListBean> {
    public String mType;
    private MessageListActivity mActivity;
    public RewardAndPlayWithAdapter(Context context, List<RewardAndPlayWithMsgEntity.DataBean.ListBean> objects,String type) {
        super(context, R.layout.adapter_videomessage, objects);
        mType = type;
        mActivity = (MessageListActivity)context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        final RewardAndPlayWithMsgEntity.DataBean.ListBean data = getItem(position);
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

        setImageViewImageNet(holder.pic, data.getIcon());
        setTextViewText(holder.title, data.getMember_name());
        setTextViewText(holder.content, data.getContent());
        setCount(data, holder);

        try {
            setTextViewText(holder.time, TimeHelper.getMyMessageTime(data.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打赏消息
                if ("reward".equals(mType)){
                    //跳转视频播放页
                    VideoImage videoImage = new VideoImage();
                    videoImage.setId(data.getRelation_id());
                    videoImage.setVideo_id(data.getRelation_id());
                    ActivityManager.startVideoPlayActivity(mActivity,videoImage);
                }else{                                      //陪练消息
                    int role = PlayWithOrderDetailActivity.getRole(mActivity.getMemberId(),data.getUser_id(),data.getCoach_id());
                    if (role == PlayWithOrderDetailActivity.ROLE_COACH){
                        switch (data.getStatusX()){
                            case "10":                      //教练确认
                                //跳转退款确认页面
                                ActivityManager.startRefundApplyActivity(mActivity,null,data.getRelation_id());
                                break;
                            default:
                                //跳转订单详情
                                ActivityManager.startPlayWithOrderDetailActivity(mActivity,data.getRelation_id(),role,false);
                                break;

                        }
                    }else {
                        switch (data.getStatusX()){
                            case "2":
                            case "3":
                            case "4":
                                //跳转订单详情
                                ActivityManager.startPlayWithOrderDetailActivity(mActivity,data.getRelation_id(),role,true);
                                break;
                            case "5":
                                //跳转评价页面
                                ActivityManager.startPlayWithOrderCommentActivity(mActivity,data.getCoach_id(),data.getMember_name(),data.getIcon(),"0",data.getRelation_id());
                                break;
                            case "10":
                            case "11":
                                //跳转订单详情
                                ActivityManager.startPlayWithOrderDetailActivity(mActivity,data.getRelation_id(),role,true);
                                break;
                        }
                    }
                }

                data.setMark("0");
                notifyDataSetChanged();

                ReadMessageEntity entity = new ReadMessageEntity();

                entity.setAll(0);
                entity.setMsgId(data.getMsg_id());
                entity.setMsgType(data.getMsg_type());
                entity.setSymbol(data.getSymbol());
                EventBus.getDefault().post(entity);
            }
        });

        setListViewLayoutParams(view, 60);
        return view;
    }

    /**
     * 消息是否已读
     */
    private void setCount(final RewardAndPlayWithMsgEntity.DataBean.ListBean data, final ViewHolder holder) {
        holder.go.setVisibility(View.GONE);
        if (data.getMark().equals("1")) {// 未读
            holder.count.setVisibility(View.VISIBLE);
        } else {
            holder.count.setVisibility(View.GONE);
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

}
