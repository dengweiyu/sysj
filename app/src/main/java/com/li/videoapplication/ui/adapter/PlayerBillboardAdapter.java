package com.li.videoapplication.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.fragment.FansBillboardFragment;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;
import com.li.videoapplication.views.CircleImageView;

/**
 * 适配器：玩家榜
 */
@SuppressLint("InflateParams")
public class PlayerBillboardAdapter extends BaseArrayAdapter<Member> {

    /**
     * 跳转：玩家动态
     */
    private void startDynamicActivity(Member member) {
        ActivityManeger.startPlayerDynamicActivity(getContext(), member);
    }

    public int tab;

    public PlayerBillboardAdapter(Context context, int tab, List<Member> data) {
        super(context, R.layout.adapter_playerbillboard, data);
        this.tab = tab;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final Member record = getItem(position);
        final ViewHolder holder;
        final int number = position + 1;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_playerbillboard, null);
            holder.head = (CircleImageView) view.findViewById(R.id.playerbillboard_head);
            holder.head_v = (ImageView) view.findViewById(R.id.playerbillboard_v);
            holder.name = (TextView) view.findViewById(R.id.playerbillboard_name);
            holder.mark = (TextView) view.findViewById(R.id.playerbillboard_mark);
            holder.content = (LinearLayout) view.findViewById(R.id.content);
            holder.left = (TextView) view.findViewById(R.id.playerbillboard_left);
            holder.middle = (TextView) view.findViewById(R.id.playerbillboard_middle);
            holder.right = (TextView) view.findViewById(R.id.playerbillboard_right);
            holder.focus = (LinearLayout) view.findViewById(R.id.playerbillboard_focus);
            holder.playerFocus = (TextView) view.findViewById(R.id.groupdetail_player_focus);
            holder.ranking = (ImageView) view.findViewById(R.id.playerbillboard_ranking);
            holder.number = (TextView) view.findViewById(R.id.playerbillboard_number);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (number == 1) {
            holder.ranking.setVisibility(View.VISIBLE);
            holder.number.setVisibility(View.GONE);
            holder.ranking.setImageResource(R.drawable.playerbillboard_one);
        } else if (number == 2) {
            holder.ranking.setVisibility(View.VISIBLE);
            holder.number.setVisibility(View.GONE);
            holder.ranking.setImageResource(R.drawable.playerbillboard_two);
        } else if (number == 3) {
            holder.ranking.setVisibility(View.VISIBLE);
            holder.number.setVisibility(View.GONE);
            holder.ranking.setImageResource(R.drawable.playerbillboard_three);
        } else {
            holder.ranking.setVisibility(View.GONE);
            holder.number.setVisibility(View.VISIBLE);
            holder.number.setText(String.valueOf(number));
        }

        if (record.isV()) {
            holder.head_v.setVisibility(View.VISIBLE);
        } else {
            holder.head_v.setVisibility(View.INVISIBLE);
        }

        setImageViewImageNet(holder.head, record.getAvatar());
        setTextViewText(holder.name, record.getNickname());

        setMark(holder.mark, record);

        setDegree(holder.left, record);
        setVideo(holder.middle, record);
        setFans(holder.right, record);
        holder.content.setVisibility(View.VISIBLE);
        if (tab == FansBillboardFragment.PLAYERBILLBOARD_RANK) {
            holder.left.setVisibility(View.VISIBLE);
            holder.middle.setVisibility(View.GONE);
            holder.right.setVisibility(View.GONE);
        } else if (tab == FansBillboardFragment.PLAYERBILLBOARD_VIDEO) {
            holder.left.setVisibility(View.GONE);
            holder.middle.setVisibility(View.VISIBLE);
            holder.right.setVisibility(View.GONE);
        } else if (tab == FansBillboardFragment.PLAYERBILLBOARD_FANS) {
            holder.left.setVisibility(View.GONE);
            holder.middle.setVisibility(View.GONE);
            holder.right.setVisibility(View.VISIBLE);
        }

        setFocus(record, holder.focus, holder.playerFocus);

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Member info = gson.fromJson(record.toJSON(), Member.class);
                startDynamicActivity(info);
            }
        });

        setListViewLayoutParams(view, 58);

        return view;
    }

    /**
     * 关注
     */
    public void setFocus(final Member record, LinearLayout view, TextView v) {
        view.setVisibility(View.VISIBLE);
        v.setVisibility(View.GONE);
        if (record != null) {
            if (record.getMember_tick() == 1) {
                view.setBackgroundResource(R.drawable.focus_gray);
            } else {
                view.setBackgroundResource(R.drawable.focus_red);
            }
        }
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!isLogin()) {
                    showToastShort("请先登录！");
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
                notifyDataSetChanged();
                // 玩家关注
                DataManager.memberAttention201(record.getMember_id(), getMember_id());
            }
        });
    }

    /**
     * 等级
     */
    private void setDegree(TextView view, final Member record) {

        view.setText("Lv." + record.getDegree());
    }

    /**
     * 视频
     */
    private void setVideo(TextView view, final Member record) {

        if (!StringUtil.isNull(record.getVideo_num())) {
            setTextViewText(view, "视频\t" + record.getVideo_num());
        } else if (!StringUtil.isNull(record.getUploadVideoCount())) {
            setTextViewText(view, "视频\t" + record.getUploadVideoCount());
        } else {
            setTextViewText(view, "");
        }
    }

    /**
     * 粉丝
     */
    private void setFans(TextView view, final Member record) {

        if (!StringUtil.isNull(record.getFans())) {
            setTextViewText(view, "粉丝\t" + record.getFans());
        } else {
            setTextViewText(view, "");
        }
    }

    /**
     * 内容:等级 19 视频 236 粉丝 56
     */
    private void setMark(TextView view, final Member record) {

        //<!-- #ffb535 黄色 -->
        //<!-- #a9a9a9 灰色 -->
        //<!-- #ff5f5d 红色 -->
        StringBuffer buffer = new StringBuffer();
        if (!StringUtil.isNull(record.getRank())) {
            buffer.append(TextUtil.toColorItalic(String.valueOf("Lv." + record.getDegree()), "#ffb535"));
        }
        if (!StringUtil.isNull(record.getVideo_num())) {
            buffer.append("\t\t视频\t" + record.getVideo_num());
        } else if (!StringUtil.isNull(record.getUploadVideoCount())) {
            buffer.append("\t\t视频\t" + record.getUploadVideoCount());
        }
        if (!StringUtil.isNull(record.getFans())) {
            buffer.append("\t\t粉丝\t" + record.getFans());
        }
        view.setText(Html.fromHtml(buffer.toString()));
        view.setVisibility(View.GONE);
    }

    private static class ViewHolder {
        CircleImageView head;
        ImageView head_v;
        TextView mark;// 等级 19 视频 236 粉丝 56
        LinearLayout content;
        TextView left;//Lv.1
        TextView middle;// 视频 236
        TextView right;// 粉丝 56
        TextView name;
        LinearLayout focus;
        TextView playerFocus;
        ImageView ranking;// 排名 金牌 银牌 铜牌
        TextView number;// 排名 4
    }
}
