package com.li.videoapplication.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.entity.HomeGameSelectEntity;
import com.li.videoapplication.data.network.UITask;

import java.util.List;

/**
 * 首页Tab选择页 游戏列表
 */

public class ChoiceHomeTabHotGameAdapter extends BaseItemDraggableAdapter<HomeGameSelectEntity.ADataBean.HotGameBean, BaseViewHolder> {

    private List<HomeGameSelectEntity.ADataBean.HotGameBean> hotGameList;

    private boolean isEditState = false;

    public ChoiceHomeTabHotGameAdapter(List<HomeGameSelectEntity.ADataBean.HotGameBean> hotGameList) {
        super(R.layout.home_hot_game_item_select, hotGameList);
        this.hotGameList = hotGameList;
    }

    public void setEditState(boolean isEditState) {
        this.isEditState = isEditState;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final HomeGameSelectEntity.ADataBean.HotGameBean hotGameBean) {
        holder.setText(R.id.tv_game_name, hotGameBean.getName());
        if (!isEditState) {
            holder.getView(R.id.iv_is_choice).setVisibility(View.GONE);
            holder.getView(R.id.ll_square_item_root).setClickable(isEditState);
        } else {
            holder.getView(R.id.iv_is_choice).setVisibility(View.VISIBLE);
            holder.getView(R.id.ll_square_item_root).setClickable(isEditState);
        }
        GlideHelper.displayRoundImage(mContext.getApplicationContext(), hotGameBean.getIco_pic(), (ImageView) holder.getView(R.id.iv_square_game_icon));
        UITask.postDelayed(new Runnable() {
            @Override
            public void run() {
                GlideHelper.displayRoundImage(mContext.getApplicationContext(), hotGameBean.getIco_pic(), (ImageView) holder.getView(R.id.iv_square_game_icon));
            }
        }, 100);
    }

}
