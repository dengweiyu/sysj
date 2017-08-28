package com.li.videoapplication.mvp.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.mvp.billboard.BillboardContract.IBillboardPresenter;
import com.li.videoapplication.mvp.billboard.presenter.BillboardPresenter;
import com.li.videoapplication.mvp.billboard.view.PlayerBillboardFragment;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.utils.StringUtil;

/**
 * 适配器：主播榜
 */
@SuppressLint("InflateParams")
public class PlayerBillboardAdapter extends BaseQuickAdapter<Member, BaseViewHolder> {

    private TextImageHelper helper;
    private int tab;

    public PlayerBillboardAdapter(List<Member> data, int tab) {
        super(R.layout.adapter_playerbillboard, data);
        this.tab = tab;
        helper = new TextImageHelper();
    }

    @Override
    protected void convert(BaseViewHolder holder, Member member) {
        switch (holder.getLayoutPosition()) {
            case 1:
                holder.setVisible(R.id.playerbillboard_ranking, true)
                        .setVisible(R.id.playerbillboard_number, false)
                        .setImageResource(R.id.playerbillboard_ranking, R.drawable.playerbillboard_one);
                break;
            case 2:
                holder.setVisible(R.id.playerbillboard_ranking, true)
                        .setVisible(R.id.playerbillboard_number, false)
                        .setImageResource(R.id.playerbillboard_ranking, R.drawable.playerbillboard_two);
                break;
            case 3:
                holder.setVisible(R.id.playerbillboard_ranking, true)
                        .setVisible(R.id.playerbillboard_number, false)
                        .setImageResource(R.id.playerbillboard_ranking, R.drawable.playerbillboard_three);
                break;
            default:
                holder.setVisible(R.id.playerbillboard_ranking, false)
                        .setVisible(R.id.playerbillboard_number, true)
                        .setText(R.id.playerbillboard_number, String.valueOf(holder.getLayoutPosition()));
                break;
        }

        holder.setVisible(R.id.playerbillboard_v, member.isV())
                .setText(R.id.playerbillboard_name, member.getNickname());

        ImageView head = holder.getView(R.id.playerbillboard_head);
        helper.setImageViewImageNet(head, member.getAvatar());

        setCurrency(holder, member);
        setVideo(holder, member);
        setFans(holder, member);

        if (tab == PlayerBillboardFragment.PLAYERBILLBOARD_CURRENCY) {
            holder.setVisible(R.id.playerbillboard_left, true)
                    .setVisible(R.id.playerbillboard_middle, false)
                    .setVisible(R.id.playerbillboard_right, false);
            holder.getView(R.id.iv_beans).setVisibility(View.VISIBLE);
        } else if (tab == PlayerBillboardFragment.PLAYERBILLBOARD_VIDEO) {
            holder.setVisible(R.id.playerbillboard_left, false)
                    .setVisible(R.id.playerbillboard_middle, true)
                    .setVisible(R.id.playerbillboard_right, false);
        } else if (tab == PlayerBillboardFragment.PLAYERBILLBOARD_FANS) {
            holder.setVisible(R.id.playerbillboard_left, false)
                    .setVisible(R.id.playerbillboard_middle, false)
                    .setVisible(R.id.playerbillboard_right, true);
        }

        setFocus(member, holder);
    }

    /**
     * 关注
     */
    private void setFocus(final Member record, final BaseViewHolder holder) {
        holder.setVisible(R.id.playerbillboard_focus, true)
                .addOnClickListener(R.id.playerbillboard_focus);

        LinearLayout focusView = holder.getView(R.id.playerbillboard_focus);
        if (record != null) {
            if (record.getMember_tick() == 1) {
                focusView.setBackground(null);
                focusView.setEnabled(false);
                holder.setText(R.id.playerbillboard_focus_text, "已关注")
                        .setTextColor(R.id.playerbillboard_focus_text, mContext.getResources().getColor(R.color.darkgray));
            } else {
                focusView.setBackgroundResource(R.drawable.player_focus);
                focusView.setEnabled(true);
                holder.setText(R.id.playerbillboard_focus_text, "关注")
                        .setTextColor(R.id.playerbillboard_focus_text,
                                mContext.getResources().getColor(R.color.groupdetail_player_red));
            }

            focusView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (!PreferencesHepler.getInstance().isLogin()) {
                        DialogManager.showLogInDialog(mContext);
                        return;
                    }
                    if (record.getMember_tick() == 1) {// 已关注状态
                        record.setFans(Integer.valueOf(record.getFans()) - 1 + "");
                        record.setMember_tick(0);
                    } else {// 未关注状态
                        record.setFans(Integer.valueOf(record.getFans()) + 1 + "");
                        record.setMember_tick(1);
                    }
                    notifyItemChanged(holder.getLayoutPosition());

                    // 玩家关注
                    IBillboardPresenter p = new BillboardPresenter();
                    p.memberAttention(record.getMember_id(), PreferencesHepler.getInstance().getMember_id());
                    UmengAnalyticsHelper.onEvent(mContext, UmengAnalyticsHelper.DISCOVER, "主播榜-关注");
                }
            });
        }
    }

    /**
     * 磨豆
     */
    private void setCurrency(BaseViewHolder holder, final Member record) {

        if (!StringUtil.isNull(record.getCurrency())) {
            holder.setText(R.id.playerbillboard_left,StringUtil.formatMoney(Float.parseFloat(record.getCurrency())));
        }
    }

    /**
     * 视频
     */
    private void setVideo(BaseViewHolder holder, final Member record) {

        if (!StringUtil.isNull(record.getVideo_num())) {
            holder.setText(R.id.playerbillboard_middle, "视频\t" + StringUtil.formatNum(record.getVideo_num()));
        } else if (!StringUtil.isNull(record.getUploadVideoCount())) {
            holder.setText(R.id.playerbillboard_middle, "视频\t" + StringUtil.formatNum(record.getUploadVideoCount()));
        }
    }

    /**
     * 粉丝
     */
    private void setFans(BaseViewHolder holder, final Member record) {

        if (!StringUtil.isNull(record.getFans())) {
            holder.setText(R.id.playerbillboard_right, "粉丝\t" + StringUtil.formatNum(record.getFans()));
        }
    }
}
