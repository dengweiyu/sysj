package com.li.videoapplication.ui.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;

import java.util.List;

/**
 *礼物列表
 */

public class PlayGiftChoiceAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {
    public PlayGiftChoiceAdapter(List<Integer> data) {
        super(R.layout.square_game_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Integer integer) {

    }
}
