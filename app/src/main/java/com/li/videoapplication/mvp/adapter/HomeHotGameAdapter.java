package com.li.videoapplication.mvp.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.tools.LayoutParamsHelper;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.utils.ScreenUtil;

import java.util.List;

/**
 * 适配器：主页-热门游戏
 */
public class HomeHotGameAdapter extends BaseQuickAdapter<Game, BaseViewHolder> {

    private TextImageHelper helper;

    public HomeHotGameAdapter(List<Game> data) {
        super(R.layout.adapter_home_hotgame, data);
        helper = new TextImageHelper();
    }

    @Override
    protected void convert(BaseViewHolder holder, Game game) {
        setLayoutParams(holder.getView(R.id.root)); //72*72

        ImageView pic = holder.getView(R.id.hotgame_pic);
        helper.setImageViewImageNet(pic, game.getFlag());

        holder.setText(R.id.hotgame_name, game.getGroup_name());
    }

    private void setLayoutParams(View view) {
        LayoutParamsHelper layoutParamsHelper = new LayoutParamsHelper();
        int w = ScreenUtil.dp2px(72);
        int h = ScreenUtil.dp2px(72);
        layoutParamsHelper.setRelativeLayout(view, w, h);
    }
}
