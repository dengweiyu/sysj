package com.li.videoapplication.ui.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;

import java.util.List;

/**
 *
 */

public class ChoiceFocusGameAdapter extends BaseQuickAdapter<String,BaseViewHolder> {
    public ChoiceFocusGameAdapter(List<String> data) {
        super(R.layout.adapter_choice_focus_game,data);
    }

    @Override
    protected void convert(BaseViewHolder holder, String s) {
        ImageView icon = holder.getView(R.id.iv_square_game_icon);
        GlideHelper.displayImage(mContext,"",icon);
    }
}
