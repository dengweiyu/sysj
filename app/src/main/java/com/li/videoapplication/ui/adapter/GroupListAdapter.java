package com.li.videoapplication.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.response.GroupAttentionGroupEntity;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.activity.GroupListActivity;
import com.li.videoapplication.ui.activity.WebActivity;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.RoundedImageView;

import java.util.List;

import io.rong.eventbus.EventBus;

/**
 * 适配器：圈子
 */
@SuppressLint("InflateParams")
public class GroupListAdapter extends BaseArrayAdapter<Game> {


    /**
     * 跳转：圈子详情
     */
    private void startGroupDetailActivity(Game item) {
        ActivityManager.startGroupDetailActivity(getContext(), item.getGroup_id());
    }

    public GroupListAdapter(Context context, List<Game> data) {
        super(context, R.layout.adapter_grouplist, data);
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        View mView = null;
        final Game record = getItem(position);
        int column = position + 1;
        ViewHolder holder = null;
        if (mView == null) {
            holder = new ViewHolder();
            mView = inflater.inflate(R.layout.adapter_grouplist, null);
            holder.medal_img = (ImageView) mView.findViewById(R.id.grouplist_medal_iv);
            holder.medal_num = (TextView) mView.findViewById(R.id.grouplist_medal_tv);
            holder.title = (TextView) mView.findViewById(R.id.grouplist_title);
            holder.left = (TextView) mView.findViewById(R.id.grouplist_left);
            holder.right = (TextView) mView.findViewById(R.id.grouplist_right);
            holder.pic = (RoundedImageView) mView.findViewById(R.id.grouplist_pic);
            holder.focus = (TextView) mView.findViewById(R.id.grouplist_focus);
            holder.game_medal = (RelativeLayout) mView.findViewById(R.id.game_medal);
            holder.content_layout = (LinearLayout) mView.findViewById(R.id.content_layout);
            holder.large_content = (TextView) mView.findViewById(R.id.large_content);
            holder.large_img = (ImageView) mView.findViewById(R.id.large_icon);
            holder.content_rlayout = (RelativeLayout) mView.findViewById(R.id.grouplist_two);
            holder.grouplist_one = (LinearLayout) mView.findViewById(R.id.grouplist_one);
            holder.large_dark = (RelativeLayout) mView.findViewById(R.id.large_drak);
            mView.setTag(holder);
        } else {
            holder = (ViewHolder) mView.getTag();
        }
        if (StringUtil.isNull(record.getContent())) {
            setListViewLayoutParams(mView, 58);
            setTextViewText(holder.title, record.getGroup_name());
            setImageViewImageNet(holder.pic, record.getFlag());
            setTopic(holder.left, record);
            setRemark(holder.right, record);
            setFocus(record, holder.focus);
            setMedal(column, holder.medal_img, holder.medal_num);
            holder.content_rlayout.setVisibility(View.GONE);
            holder.large_img.setVisibility(View.GONE);
        } else {
            holder.game_medal.setVisibility(View.GONE);
            holder.left.setVisibility(View.GONE);
            holder.right.setVisibility(View.GONE);
            holder.content_rlayout.setVisibility(View.VISIBLE);
            setTextViewText(holder.title, record.getGroup_name());
            setImageViewImageNet(holder.pic, record.getFlag());
            setFocus(record, holder.focus);
            setTextViewText(holder.large_content, record.getContent());
            if (holder.large_img.getTag() == null) {
                setImageViewImageNet(holder.large_img, record.getLarge_icon());
            } else {

            }
            holder.large_dark.setVisibility(View.VISIBLE);
            holder.large_img.setPadding(0, 0, 0, dp2px(8));
            holder.grouplist_one.setPadding(0, dp2px(4), 0, 0);

        }
        mView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!StringUtil.isNull(record.getUrl())) { //H5游戏
                    WebActivity.startWebActivity(getContext(), record.getUrl());
                } else {
                    startGroupDetailActivity(record);
                }
                try {
                    GroupListActivity activity = (GroupListActivity) getContext();
                    UmengAnalyticsHelper.onGameMoreEvent(getContext(), activity.groupType.getGroup_type_id());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return mView;
    }

    private void setMedal(int column, ImageView view1, TextView view2) {

        if (column <= 3) {
            switch (column) {
                case 1:
                    view2.setVisibility(View.GONE);
                    setImageViewImageRes(view1, R.drawable.medal_first);
                    break;
                case 2:
                    view2.setVisibility(View.GONE);
                    setImageViewImageRes(view1, R.drawable.medal_second);
                    break;
                case 3:
                    view2.setVisibility(View.GONE);
                    setImageViewImageRes(view1, R.drawable.medal_third);
                    break;
                default:

                    break;
            }
        } else {
            view1.setVisibility(View.GONE);
            setTextViewText(view2, Integer.toString(column));

        }
    }



    /**
     * 话题
     */
    private void setTopic(TextView view, final Game record) {
        view.setText("话题\t" + StringUtil.toUnitW(record.getVideo_num()));
    }

    /**
     * 关注
     */
    private void setRemark(TextView view, final Game record) {
        view.setText("关注\t" + StringUtil.toUnitW(record.getAttention_num()));
    }

    /**
     * 关注
     */
    private void setFocus(final Game record, TextView view) {

        view.setVisibility(View.VISIBLE);
        if (record != null) {
            if (record.getTick() == 1) {
                view.setBackground(null);
                view.setTextColor(resources.getColorStateList(R.color.textcolor_french_gray));
                setTextViewText(view, R.string.dynamic_focused);
                view.setEnabled(true);
            } else {
                view.setBackgroundResource(R.drawable.player_focus_red);
                view.setTextColor(resources.getColorStateList(R.color.groupdetail_player_red));
                setTextViewText(view, R.string.dynamic_focus);
            }
        }

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                if (!isLogin()) {
//                    showToastLogin();
//                    return;
//                }
                if (StringUtil.isNull(getMember_id())) {
                    DialogManager.showLogInDialog(getContext());
                    return;
                }
                if (record.getTick() == 1) {

                    DialogManager.showConfirmDialog(getContext(), "确认取消关注该游戏圈?", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (v.getId()){
                                case R.id.tv_confirm_dialog_yes:
                                    record.setAttention_num(Integer.valueOf(record.getAttention_num()) - 1 + "");
                                    record.setTick(0);
                                    // 关注圈子201
                                    DataManager.groupAttentionGroup(record.getGroup_id(), getMember_id());
                                    notifyDataSetChanged();
                                    UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.GAME, "找游戏-关注");

                                    //
                                    EventBus.getDefault().post(new GroupAttentionGroupEntity());
                                    break;
                            }
                        }
                    });

                } else {
                    record.setAttention_num(Integer.valueOf(record.getAttention_num()) + 1 + "");
                    record.setTick(1);
                    DataManager.groupAttentionGroup(record.getGroup_id(), getMember_id());
                    notifyDataSetChanged();
                    UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.GAME, "找游戏-关注");

                    //
                    EventBus.getDefault().post(new GroupAttentionGroupEntity());
                }
//
            }
        });
    }


    private static class ViewHolder {
        TextView title;
        TextView content;// 视频 2020 关注 201
        TextView left;
        TextView right;
        TextView focus;
        ImageView medal_img;
        TextView medal_num;
        RoundedImageView pic;
        RelativeLayout game_medal;
        LinearLayout content_layout;
        TextView large_content;
        ImageView large_img;
        LinearLayout grouplist_one;
        RelativeLayout large_dark;
        RelativeLayout content_rlayout;
    }
}
