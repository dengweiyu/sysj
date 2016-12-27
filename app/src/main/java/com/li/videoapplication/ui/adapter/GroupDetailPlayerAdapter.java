package com.li.videoapplication.ui.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.CircleImageView;

import java.util.List;

/**
 * 适配器：圈子详情-玩家
 */
@SuppressLint("InflateParams")
public class GroupDetailPlayerAdapter extends BaseQuickAdapter<Member, BaseViewHolder> {

    private TextImageHelper helper;

    public GroupDetailPlayerAdapter(List<Member> data) {
        super(R.layout.adapter_searchmember, data);
        helper = new TextImageHelper();
    }

    @Override
    protected void convert(BaseViewHolder holder, Member record) {
        CircleImageView head = holder.getView(R.id.searchmember_head);
        helper.setImageViewImageNet(head, record.getAvatar());

        holder.setText(R.id.playerbillboard_name, record.getNickname())
                .setVisible(R.id.searchmember_v, record.isV())
                .setVisible(R.id.content, true)
                .setVisible(R.id.searchmember_mark, false);

        setFans((TextView) holder.getView(R.id.searchmember_middle), record);
        setVideo((TextView) holder.getView(R.id.searchmember_right), record);

        TextView focus = holder.getView(R.id.searchmember_focus);
        ImageView go = holder.getView(R.id.myplayer_go);
        setFocus(record, focus, go);
        holder.addOnClickListener(R.id.searchmember_focus);
    }

    /**
     * 关注
     */
    private void setFocus(final Member record, TextView focus, ImageView go) {
        go.setVisibility(View.GONE);
        if (record != null) {
            if (record.getMember_tick() == 1) { // 已关注状态
                focus.setBackgroundResource(R.drawable.player_focus_gray);
                focus.setTextColor(mContext.getResources().getColorStateList(R.color.groupdetail_player_white));
            } else { // 未关注状态
                focus.setBackgroundResource(R.drawable.player_focus_red);
                focus.setTextColor(mContext.getResources().getColorStateList(R.color.groupdetail_player_red));
            }
        }
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
}
