package com.li.videoapplication.ui.adapter;



import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.response.CoachListEntity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.utils.ScreenUtil;

import java.util.List;

/**
 * 教练列表适配器
 */

public class CoachLisAdapter extends BaseQuickAdapter<CoachListEntity.DataBean.IncludeBean,BaseViewHolder>{

    private Context mContext;
    public CoachLisAdapter(Context context,List<CoachListEntity.DataBean.IncludeBean> data) {
        super(R.layout.coach_list_item,data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, final CoachListEntity.DataBean.IncludeBean includeBean) {
        holder.setText(R.id.tv_coach_nick_name,includeBean.getNickname())
                .setText(R.id.tv_coach_order_title,includeBean.getGame_level())
               // .setText(R.id.tv_coach_game_win_rate,"胜率"+includeBean.getWin_rate())
                .setText(R.id.tv_coach_order_number,"订单数"+includeBean.getOrder_total());
              //  .setText(R.id.tv_coach_score,includeBean.getScore()+"分");

        View view = holder.getView(R.id.rl_coach_icon);

        setPicLayoutParams(view);


        final  ImageView icon = (ImageView) holder.getView(R.id.iv_coach_icon);
        GlideHelper.displayImage(mContext,includeBean.getFlag(),icon);

        final ImageView rankIcon = holder.getView(R.id.iv_coach_rank_icon);
        GlideHelper.displayImageWhite(mContext,includeBean.getGame_level_icon(),rankIcon);


        TextView status = holder.getView(R.id.tv_coach_status);
        View statusLayout = holder.getView(R.id.ll_coach_layout);
        ImageView statusIcon = holder.getView(R.id.iv_coach_status_icon);
        switch (includeBean.getStatusX()){
            case 1:                             //在线
                status.setText("在线");
                status.setTextColor(Color.WHITE);
                statusLayout.setBackgroundColor(Color.parseColor("#c803f94a"));
                statusIcon.setImageResource(R.drawable.coach_online);
                break;
            case 2:                             //游戏中
                status.setText("陪练中");
                status.setTextColor(Color.WHITE);
                statusLayout.setBackgroundColor(Color.parseColor("#c803f94a"));
                statusIcon.setImageResource(R.drawable.coach_online);
                break;
            case 3:                             //离线
                status.setText("离线");
                status.setTextColor(Color.WHITE);
                statusLayout.setBackgroundColor(Color.parseColor("#C8808080"));
                statusIcon.setImageResource(R.drawable.coach_offline);
                break;
        }
    }

    private void setPicLayoutParams(View view) {

        if (view != null) {
            // 86/148
            int w = (ScreenUtil.getScreenWidth()) / 2;
            int h = w * 9 / 16;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h);
            view.setLayoutParams(params);
        }
    }
}
