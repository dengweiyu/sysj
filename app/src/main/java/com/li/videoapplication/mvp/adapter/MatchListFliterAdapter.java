package com.li.videoapplication.mvp.adapter;

import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.mvp.match.view.MatchListFliterFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 适配器：赛事列表筛选
 */
public class MatchListFliterAdapter extends BaseQuickAdapter<Game, BaseViewHolder> {

    private Map<Integer, Boolean> isSelected;

    public MatchListFliterAdapter(List<Game> data) {
        super(R.layout.adapter_matchlistfliter, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Game game) {
        holder.setText(R.id.fliter_game, game.getName())
                .setChecked(R.id.fliter_game, MatchListFliterFragment.selectedPos.get(holder.getAdapterPosition()));
    }
}
