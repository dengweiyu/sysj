package com.li.videoapplication.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.response.SendRewardRankEntity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.utils.StringUtil;
import java.text.DecimalFormat;
import java.util.List;


/**
 * 土豪榜/人气榜
 */

public class SendRewardRankAdapter extends BaseQuickAdapter<SendRewardRankEntity.ADataBean.IncludeBean,BaseViewHolder> {
    public SendRewardRankAdapter(List<SendRewardRankEntity.ADataBean.IncludeBean> data) {
        super(R.layout.adapter_send_reward_rank,data);
    }

    @Override
    protected void convert(BaseViewHolder holder, SendRewardRankEntity.ADataBean.IncludeBean includeBean) {
        int position = holder.getAdapterPosition();
        ImageView rankIcon = (ImageView) holder.getView(R.id.iv_rank);
        TextView rankText = (TextView) holder.getView(R.id.tv_rank);
        if (position < 4){
            int redId = 0;
            switch (position){
                case 1:
                    redId = R.drawable.playerbillboard_one;
                    break;
                case 2:
                    redId = R.drawable.playerbillboard_two;
                    break;
                case 3:
                    redId = R.drawable.playerbillboard_three;
                    break;
            }
            rankIcon.setVisibility(View.VISIBLE);
            rankText.setVisibility(View.GONE);
            rankIcon.setImageResource(redId);
        }else {
            rankText.setVisibility(View.VISIBLE);
            rankIcon.setVisibility(View.GONE);
            rankText.setText(position+"");
        }

        GlideHelper.displayImageWhite(mContext,includeBean.getAvatar(),(ImageView) holder.getView(R.id.civ_player_icon));

        holder.setText(R.id.tv_user_nick_name,includeBean.getNickname());

        try {
            holder.setText(R.id.tv_currency_coin,format(Float.parseFloat(includeBean.getCoin())));
            holder.setText(R.id.tv_currency_beans,format(Float.parseFloat(includeBean.getCurrency())));
        } catch (Exception e) {
            e.printStackTrace();
        }


        setFocus((TextView) holder.getView(R.id.tv_send_reward_focus),includeBean);
    }

    /**
     * 关注
     */
    private void setFocus(TextView focus, final SendRewardRankEntity.ADataBean.IncludeBean includeBean) {

        if (includeBean.getMember_tick() == 1) { // 已关注状态
            focus.setBackground(null);
            focus.setTextColor(mContext.getResources().getColorStateList(R.color.darkgray));
            focus.setText(R.string.dynamic_focused);
            focus.setEnabled(false);
        } else { // 未关注状态
            focus.setBackgroundResource(R.drawable.player_focus_red);
            focus.setTextColor(mContext.getResources().getColorStateList(R.color.groupdetail_player_red));
            focus.setText(R.string.dynamic_focus);
            focus.setEnabled(true);
        }

        focus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!PreferencesHepler.getInstance().isLogin()) {
                    DialogManager.showLogInDialog(mContext);
                    return;
                }

                // 关注圈子201
                if (includeBean.getMember_tick() == 1) { // 已关注状态

                    DialogManager.showConfirmDialog(mContext, "确认取消关注该玩家?", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (v.getId()){
                                case R.id.tv_confirm_dialog_yes:
                                    includeBean.setMember_tick(0);
                                    DataManager.memberAttention201(includeBean.getMember_id(),PreferencesHepler.getInstance().getMember_id());

                                    notifyDataSetChanged();
                                    UmengAnalyticsHelper.onEvent(mContext, UmengAnalyticsHelper.GAME, "打赏榜-关注");
                                    break;
                            }
                        }
                    });

                } else { // 未关注状态
                    includeBean.setMember_tick(1);
                }
                DataManager.memberAttention201(includeBean.getMember_id(),PreferencesHepler.getInstance().getMember_id());
                notifyDataSetChanged();
                UmengAnalyticsHelper.onEvent(mContext, UmengAnalyticsHelper.GAME, "打赏榜-关注");
                //

            }
        });
    }

    /**
     * 格式化金额
     */
    public static String format(float money){
        String  format ="";
        DecimalFormat decimalFormat=new DecimalFormat("##0.00");
        int m = (int)money;
        format = StringUtil.formatNum(m+"");
        String value = decimalFormat.format(money - (float) m)+"";
        format += value.substring(1,value.length());
        return format;
    }
}
