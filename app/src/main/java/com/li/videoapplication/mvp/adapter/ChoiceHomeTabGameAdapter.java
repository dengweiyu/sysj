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

public class ChoiceHomeTabGameAdapter extends BaseItemDraggableAdapter<HomeGameSelectEntity.ADataBean.MyGameBean,BaseViewHolder> {

    private List<HomeGameSelectEntity.ADataBean.MyGameBean> myGameList;

    private boolean isEditState = false;

    private boolean isFirstLoad ;

    public ChoiceHomeTabGameAdapter(List<HomeGameSelectEntity.ADataBean.MyGameBean> myGameList) {
        super(R.layout.home_game_item_select, myGameList);
        this.myGameList=myGameList;
        this.isFirstLoad = true;
    }

    public void setEditState(boolean isEditState) {
        this.isEditState = isEditState;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final HomeGameSelectEntity.ADataBean.MyGameBean myGameBean) {
        holder.setText(R.id.tv_game_name,myGameBean.getName());
        if (!isEditState) {
            holder.getView(R.id.iv_is_choice).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.iv_is_choice).setVisibility(View.VISIBLE);
        }
        GlideHelper.displayImage(mContext.getApplicationContext(),myGameBean.getIco_pic(),(ImageView)holder.getView(R.id.iv_square_game_icon));
        if (isFirstLoad) {
            UITask.postDelayed(new Runnable() {
                @Override
                public void run() {
                    GlideHelper.displayImage(mContext.getApplicationContext(),myGameBean.getIco_pic(),(ImageView)holder.getView(R.id.iv_square_game_icon));
                }
            },1000); //延时加载才能正常
        }
        if (myGameBean.getName().equals(myGameList.get(myGameList.size()-1).getName())) {
            //加载到最后一个，firstload完成
            isFirstLoad = false;
        }
        holder.addOnClickListener(R.id.iv_is_choice);
    }

}
