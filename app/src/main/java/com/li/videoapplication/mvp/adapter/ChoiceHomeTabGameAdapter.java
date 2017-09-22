package com.li.videoapplication.mvp.adapter;

import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.SectionEntity;
import com.li.videoapplication.R;

import java.util.List;

/**
 * 首页Tab选择页 游戏列表
 */

public class ChoiceHomeTabGameAdapter extends BaseItemDraggableAdapter<String,BaseViewHolder> {

    public ChoiceHomeTabGameAdapter(List<String> data) {
        super(R.layout.square_game_list_item, data);

    }


    @Override
    protected void convert(BaseViewHolder holder, String s) {

    }

}
