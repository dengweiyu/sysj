package com.li.videoapplication.mvp.adapter;

import android.util.SparseBooleanArray;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Game;

import java.util.List;

/**
 * 适配器：赛事列表筛选
 */
public class MatchListFliterAdapter extends BaseQuickAdapter<Game, BaseViewHolder> {

    private SparseBooleanArray selectedPos;

    public MatchListFliterAdapter(List<Game> data, SparseBooleanArray selectedPos) {
        super(R.layout.adapter_matchlistfliter, data);
        this.selectedPos = selectedPos;
    }

    @Override
    protected void convert(BaseViewHolder holder, Game game) {
        holder.setText(R.id.fliter_game, game.getName())
                .setChecked(R.id.fliter_game, selectedPos.get(holder.getAdapterPosition()));
    }
}
