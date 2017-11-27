package com.li.videoapplication.ui.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.entity.SectionEntity;
import com.google.common.collect.Lists;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.entity.HomeGameSelectEntity;
import com.li.videoapplication.mvp.adapter.ChoiceHomeTabGameAdapter;
import com.li.videoapplication.ui.activity.ChoiceHomeTabActivity;

import java.util.List;

/**
 *首页选择Tab
 */

public class ChoiceHomeTabAdapter extends BaseMultiItemQuickAdapter<ChoiceHomeTabActivity.MyMultiItemEntity,BaseViewHolder> {
    private List<HomeGameSelectEntity.ADataBean.MyGameBean> myGameList;
    private List<HomeGameSelectEntity.ADataBean.HotGameBean> hotGameList;

    public ChoiceHomeTabAdapter(List<ChoiceHomeTabActivity.MyMultiItemEntity> data,List<HomeGameSelectEntity.ADataBean.MyGameBean> myGameList,List<HomeGameSelectEntity.ADataBean.HotGameBean> hotGameList) {
        super(data);
        addItemType(ChoiceHomeTabActivity.MyMultiItemEntity.TYPE_TITLE_MYGAME,R.layout.adapter_choice_home_tab_header);
        addItemType(ChoiceHomeTabActivity.MyMultiItemEntity.TYPE_LIST_MYGAME,R.layout.adapter_choice_home_tab);
        addItemType(ChoiceHomeTabActivity.MyMultiItemEntity.TYPE_VIEW,R.layout.adapter_choice_view);
        addItemType(ChoiceHomeTabActivity.MyMultiItemEntity.TYPE_TITLE_HOTGAME,R.layout.adapter_choice_title_hot_game);
        addItemType(ChoiceHomeTabActivity.MyMultiItemEntity.TYPE_LIST_HOTGAME,R.layout.adapter_choice_home_tab);
        this.myGameList=myGameList;
        this.hotGameList=hotGameList;
    }

    @Override
    protected void convert(BaseViewHolder holder, ChoiceHomeTabActivity.MyMultiItemEntity myMultiItemEntity) {
        switch (myMultiItemEntity.getItemType()){
            case ChoiceHomeTabActivity.MyMultiItemEntity.TYPE_TITLE_MYGAME:
                break;
            case ChoiceHomeTabActivity.MyMultiItemEntity.TYPE_TITLE_HOTGAME:
                break;
            case ChoiceHomeTabActivity.MyMultiItemEntity.TYPE_VIEW:
                break;
            case ChoiceHomeTabActivity.MyMultiItemEntity.TYPE_LIST_MYGAME://我的游戏
                //我的游戏
                RecyclerView gameList = holder.getView(R.id.rv_game_list);
                gameList.setLayoutManager(new GridLayoutManager(mContext,myGameList.size()));
                //这里传入newArrayList就能显示了
                ChoiceHomeTabGameAdapter adapter= new ChoiceHomeTabGameAdapter(myGameList);

                ItemDragAndSwipeCallback callback =  new ItemDragAndSwipeCallback(adapter);
                ItemTouchHelper touchHelper =  new ItemTouchHelper(callback);
                touchHelper.attachToRecyclerView(gameList);
                adapter.enableDragItem(touchHelper);
                gameList.setAdapter(adapter);
                break;
            case ChoiceHomeTabActivity.MyMultiItemEntity.TYPE_LIST_HOTGAME://热门游戏
                RecyclerView homeGameList = holder.getView(R.id.rv_game_list);
                homeGameList.setLayoutManager(new GridLayoutManager(mContext,hotGameList.size()));
                ChoiceHomeTabHotGameAdapter homeGameAdapter= new ChoiceHomeTabHotGameAdapter(hotGameList);
                ItemDragAndSwipeCallback hotCallback =  new ItemDragAndSwipeCallback(homeGameAdapter);
                ItemTouchHelper homeGameTouchHelper =  new ItemTouchHelper(hotCallback);
                homeGameTouchHelper.attachToRecyclerView(homeGameList);
                homeGameAdapter.enableDragItem(homeGameTouchHelper);
                homeGameList.setAdapter(homeGameAdapter);
                break;
        }
    }


}
