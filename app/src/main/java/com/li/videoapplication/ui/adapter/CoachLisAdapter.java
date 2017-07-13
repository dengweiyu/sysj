package com.li.videoapplication.ui.adapter;



import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.response.CoachListEntity;
import com.li.videoapplication.data.network.UITask;

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
                .setText(R.id.tv_coach_game_win_rate,"胜率"+includeBean.getWin_rate())
                .setText(R.id.tv_coach_order_number,"订单数"+includeBean.getOrder_total())
                .setText(R.id.tv_coach_score,includeBean.getScore()+"分");
        final  ImageView icon = (ImageView) holder.getView(R.id.civ_coach_icon);
        GlideHelper.displayImage(mContext,includeBean.getAvatar(),icon);

        final ImageView rankIcon = holder.getView(R.id.iv_coach_rank_icon);
        GlideHelper.displayImage(mContext,includeBean.getGame_level_icon(),rankIcon);
        UITask.postDelayed(new Runnable() {
            @Override
            public void run() {
                GlideHelper.displayImage(mContext,includeBean.getAvatar(),icon);

                GlideHelper.displayImage(mContext,includeBean.getGame_level_icon(),rankIcon);
            }
        },500);



        int resId = 0;
        switch (includeBean.getStatusX()){
            case 1:                             //在线
                resId = R.drawable.coach_on_line;
                break;
            case 2:                             //游戏中
                resId = R.drawable.coach_on_busy;
                break;
            case 3:                             //离线
                resId = R.drawable.coach_off_line;
                break;
        }

        holder.setImageResource(R.id.iv_coach_status,resId);

    }
}
