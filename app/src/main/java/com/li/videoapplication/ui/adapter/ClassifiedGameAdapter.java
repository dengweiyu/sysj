package com.li.videoapplication.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.response.GroupAttentionGroupEntity;
import com.li.videoapplication.data.network.RequestConstant;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.activity.WebActivity;
import com.li.videoapplication.ui.fragment.ClassifiedGameFragment;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.RoundedImageView;
import com.ypy.eventbus.EventBus;

/**
 * 适配器：游戏分类
 */
@SuppressLint("InflateParams")
public class ClassifiedGameAdapter extends BaseArrayAdapter<Game> {

    private ClassifiedGameFragment fragment;

    /**
     * 页面跳转：圈子详情
     */
    private void startGameDetailActivity(Game item) {
        ActivityManager.startGroupDetailActivity(getContext(), item.getGroup_id());
    }

    public ClassifiedGameAdapter(Context context, List<Game> data, ClassifiedGameFragment fragment) {
        super(context, R.layout.adapter_classifiedgame, data);
        this.fragment = fragment;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {

        final Game record = getItem(position);
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_classifiedgame, null);
            holder.title = (TextView) view.findViewById(R.id.classifiedgame_title);
            holder.left = (TextView) view.findViewById(R.id.classifiedgame_left);
            holder.right = (TextView) view.findViewById(R.id.classifiedgame_right);
            holder.pic = (RoundedImageView) view.findViewById(R.id.classifiedgame_pic);
            holder.focus = (TextView) view.findViewById(R.id.classifiedgame_focus);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setTextViewText(holder.title, record.getGroup_name());
        setTopic(holder.left, record);
        setRemark(holder.right, record);
        setImageViewImageNet(holder.pic, record.getFlag());

        setFocus(record, holder.focus);

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!StringUtil.isNull(record.getUrl())) { //H5游戏
                    WebActivity.startWebActivity(getContext(), record.getUrl());
                } else {
                    startGameDetailActivity(record);
                }

                if (fragment != null) {
                    if (fragment.sort.equals(RequestConstant.GAMELIST_SORT_TIME)) {
                        UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.GAME, "找游戏-最新游戏-有效");
                    } else if (fragment.sort.equals(RequestConstant.GAMELIST_SORT_HOT)) {
                        UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.GAME, "找游戏-最热游戏-有效");
                    }
                }
            }
        });

        setListViewLayoutParams(view, 58);

        return view;
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
                view.setTextColor(getContext().getResources().getColorStateList(R.color.darkgray));
                view.setText(R.string.dynamic_focused);
                view.setEnabled(false);
            } else {
                view.setBackgroundResource(R.drawable.player_focus_red);
                view.setTextColor(resources.getColorStateList(R.color.groupdetail_player_red));
                setTextViewText(view, R.string.dynamic_focus);
                view.setEnabled(true);
            }


        }

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isLogin()) {
                    DialogManager.showLogInDialog(getContext());
                    return;
                }
                if (record.getTick() == 1) {
                    record.setAttention_num(Integer.valueOf(record.getAttention_num()) - 1 + "");
                    record.setTick(0);
                } else {
                    record.setAttention_num(Integer.valueOf(record.getAttention_num()) + 1 + "");
                    record.setTick(1);
                }
                // 关注圈子201
                DataManager.groupAttentionGroup(record.getGroup_id(), getMember_id());
                notifyDataSetChanged();
                UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.GAME, "找游戏-关注");

                //
                EventBus.getDefault().post(new GroupAttentionGroupEntity());
            }
        });
    }

    private static class ViewHolder {
        TextView title;
        TextView left;
        TextView right;
        RoundedImageView pic;
        TextView focus;
    }
}
