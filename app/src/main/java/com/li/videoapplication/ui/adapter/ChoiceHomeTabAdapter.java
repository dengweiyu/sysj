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
import com.li.videoapplication.mvp.adapter.ChoiceHomeTabGameAdapter;
import com.li.videoapplication.ui.activity.ChoiceHomeTabActivity;

import java.util.List;

/**
 *首页选择Tab
 */

public class ChoiceHomeTabAdapter extends BaseMultiItemQuickAdapter<ChoiceHomeTabActivity.MyMultiItemEntity,BaseViewHolder> {

    public ChoiceHomeTabAdapter(List<ChoiceHomeTabActivity.MyMultiItemEntity> data) {
        super(data);
        addItemType(ChoiceHomeTabActivity.MyMultiItemEntity.TYPE_TITLE,R.layout.adapter_choice_home_tab_header);
        addItemType(ChoiceHomeTabActivity.MyMultiItemEntity.TYPE_LIST,R.layout.adapter_choice_home_tab);
    }

    @Override
    protected void convert(BaseViewHolder holder, ChoiceHomeTabActivity.MyMultiItemEntity myMultiItemEntity) {
        switch (myMultiItemEntity.getItemType()){
            case ChoiceHomeTabActivity.MyMultiItemEntity.TYPE_TITLE:
                break;
            case ChoiceHomeTabActivity.MyMultiItemEntity.TYPE_LIST:
                RecyclerView gameList = holder.getView(R.id.rv_game_list);
                gameList.setLayoutManager(new GridLayoutManager(mContext,4));
                ChoiceHomeTabGameAdapter adapter= new ChoiceHomeTabGameAdapter(Lists.newArrayList("","","","","",""));

                ItemDragAndSwipeCallback callback =  new ItemDragAndSwipeCallback(adapter);
                ItemTouchHelper touchHelper =  new ItemTouchHelper(callback);
                touchHelper.attachToRecyclerView(gameList);
                adapter.enableDragItem(touchHelper);
                gameList.setAdapter(adapter);
                break;
        }
    }


}
