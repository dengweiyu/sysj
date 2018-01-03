package com.li.videoapplication.mvp.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.SectionEntity;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.entity.HomeGameSelectEntity;
import com.li.videoapplication.data.network.UITask;

import java.util.List;

/**
 * 首页Tab选择页 游戏列表
 */

public class ChoiceHomeTabGameAdapter extends BaseItemDraggableAdapter<HomeGameSelectEntity.ADataBean.MyGameBean, BaseViewHolder> {

    private List<HomeGameSelectEntity.ADataBean.MyGameBean> myGameList;

    private boolean isEditState = false;

    public ChoiceHomeTabGameAdapter(List<HomeGameSelectEntity.ADataBean.MyGameBean> myGameList) {
        super(R.layout.home_game_item_select, myGameList);
        this.myGameList = myGameList;
    }

    public void setEditState(boolean isEditState) {
        this.isEditState = isEditState;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final HomeGameSelectEntity.ADataBean.MyGameBean myGameBean) {
        holder.setText(R.id.tv_game_name, myGameBean.getName());
        if (!isEditState) {
            holder.getView(R.id.iv_is_choice).setVisibility(View.GONE);
            holder.getView(R.id.ll_square_item_root).setClickable(isEditState);
        } else {
            holder.getView(R.id.iv_is_choice).setVisibility(View.VISIBLE);
            holder.getView(R.id.ll_square_item_root).setClickable(isEditState);
        }

        GlideHelper.displayRoundImage(mContext.getApplicationContext(), myGameBean.getIco_pic(), (ImageView) holder.getView(R.id.iv_square_game_icon));

        UITask.postDelayed(new Runnable() {
            @Override
            public void run() {
                GlideHelper.displayRoundImage(mContext.getApplicationContext(), myGameBean.getIco_pic(), (ImageView) holder.getView(R.id.iv_square_game_icon));
            }
        }, 100); //延时加载才能正常

    }

}
