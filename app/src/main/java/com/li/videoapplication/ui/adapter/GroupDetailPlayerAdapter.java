package com.li.videoapplication.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.toast.ToastHelper;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.CircleImageView;

/**
 * 适配器：圈子详情-玩家
 */
@SuppressLint("InflateParams")
public class GroupDetailPlayerAdapter extends RecyclerView.Adapter<GroupDetailPlayerAdapter.ViewHolder> {

    private Context context;
    private List<Member> data;
    private TextImageHelper helper;

    /**
     * 跳转：玩家动态
     */
    private void startPlayerDynamicActivity(Member member) {
        if (StringUtil.isNull(member.getId())) {
            member.setId(member.getMember_id());
        }
        ActivityManeger.startPlayerDynamicActivity(context, member);
    }

    public GroupDetailPlayerAdapter(Context context, List<Member> data) {
        this.context = context;
        this.data = data;
        helper = new TextImageHelper();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_searchmember, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Member record = data.get(position);

        helper.setImageViewImageNet(holder.head, record.getAvatar());
        helper.setTextViewText(holder.name, record.getNickname());

        if (record.isV()) {
            holder.isV.setVisibility(View.VISIBLE);
        } else {
            holder.isV.setVisibility(View.INVISIBLE);
        }

//        setLevel(holder.level, record);

        holder.content.setVisibility(View.VISIBLE);
        holder.mark.setVisibility(View.GONE);
//        holder.left.setVisibility(View.GONE);
        setFans(holder.middle, record);
        setVideo(holder.right, record);

        setFocus(record, holder.focus, holder.focusContainer, holder.go);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * 等级
     */
    private void setLevel(TextView view, final Member record) {
        helper.setTextViewText(view, "Lv." + record.getDegree());
        view.setVisibility(View.VISIBLE);
    }

    /**
     * 关注
     */
    public void setFocus(final Member record, TextView focus, LinearLayout focusContainer, ImageView go) {
        focus.setVisibility(View.VISIBLE);
        focusContainer.setVisibility(View.GONE);
        go.setVisibility(View.GONE);
        if (record != null) {
            if (record.getMember_tick() == 1) { // 已关注状态
                focus.setBackgroundResource(R.drawable.player_focus_gray);
                focus.setTextColor(context.getResources().getColorStateList(R.color.groupdetail_player_white));
            } else { // 未关注状态
                focus.setBackgroundResource(R.drawable.player_focus_red);
                focus.setTextColor(context.getResources().getColorStateList(R.color.groupdetail_player_red));
            }
        }

        focus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!PreferencesHepler.getInstance().isLogin()) {
                    ToastHelper.s("请先登录！");
                    return;
                }
                boolean flag;
                if (record.getMember_tick() == 1) {// 已关注状态
                    flag = false;
                    record.setFans(Integer.valueOf(record.getFans()) - 1 + "");
                    record.setMember_tick(0);
                } else {// 未关注状态
                    flag = true;
                    record.setFans(Integer.valueOf(record.getFans()) + 1 + "");
                    record.setMember_tick(1);
                }
                // 玩家关注
                DataManager.memberAttention201(record.getMember_id(), PreferencesHepler.getInstance().getMember_id());
                notifyDataSetChanged();
                UmengAnalyticsHelper.onEvent(context, UmengAnalyticsHelper.GAME, "游戏圈-玩家-关注");
            }
        });
    }

    /**
     * 等级
     */
    private String getLevel(final Member record) {
        return "Lv." + record.getDegree();
    }

    /**
     * 粉丝
     */
    private void setFans(TextView view, final Member record) {

        if (!StringUtil.isNull(record.getFans())) {
            helper.setTextViewText(view, "粉丝\t" + record.getFans());
        } else {
            helper.setTextViewText(view, "");
        }
    }

    /**
     * 视频
     */
    private void setVideo(TextView view, final Member record) {

        if (!StringUtil.isNull(record.getVideo_num())) {
            helper.setTextViewText(view, "视频\t" + record.getVideo_num());
        } else {
            helper.setTextViewText(view, "");
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView head;
        TextView mark;// 粉丝 56 视频 236
//        TextView level;// Lv.1
        LinearLayout content;
//        TextView left;
        TextView middle;// 视频 236
        TextView right;// 粉丝 56
        TextView name;
        LinearLayout focusContainer;
        TextView focus;
        ImageView go, isV;


        public ViewHolder(View itemView) {
            super(itemView);

            head = (CircleImageView) itemView.findViewById(R.id.searchmember_head);
            isV = (ImageView) itemView.findViewById(R.id.searchmember_v);
            name = (TextView) itemView.findViewById(R.id.playerbillboard_name);
            mark = (TextView) itemView.findViewById(R.id.searchmember_mark);
//            level = (TextView) itemView.findViewById(R.id.searchmember_level);
            content = (LinearLayout) itemView.findViewById(R.id.content);
//            left = (TextView) itemView.findViewById(R.id.searchmember_left);
            middle = (TextView) itemView.findViewById(R.id.searchmember_middle);
            right = (TextView) itemView.findViewById(R.id.searchmember_right);
            focusContainer = (LinearLayout) itemView.findViewById(R.id.searchmember_focus);
            focus = (TextView) itemView.findViewById(R.id.groupdetail_player_focus);
            go = (ImageView) itemView.findViewById(R.id.myplayer_go);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    startPlayerDynamicActivity(data.get(getAdapterPosition()));
                    UmengAnalyticsHelper.onEvent(context, UmengAnalyticsHelper.GAME, "游戏圈-玩家-头像");
                }
            });
        }
    }
}
