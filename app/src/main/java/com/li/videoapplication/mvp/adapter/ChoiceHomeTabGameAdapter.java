package com.li.videoapplication.mvp.adapter;

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

    public ChoiceHomeTabGameAdapter(List<HomeGameSelectEntity.ADataBean.MyGameBean> myGameList) {
        super(R.layout.home_game_item_select, myGameList);
        this.myGameList=myGameList;

    }


    @Override
    protected void convert(final BaseViewHolder holder, final HomeGameSelectEntity.ADataBean.MyGameBean myGameBean) {
        holder.setText(R.id.tv_game_name,myGameBean.getName());
        GlideHelper.displayImage(mContext.getApplicationContext(),myGameBean.getIco_pic(),(ImageView)holder.getView(R.id.iv_square_game_icon));
        UITask.postDelayed(new Runnable() {
            @Override
            public void run() {

                GlideHelper.displayImage(mContext.getApplicationContext(),myGameBean.getIco_pic(),(ImageView)holder.getView(R.id.iv_square_game_icon));
            }
        },1000);

    }


}
