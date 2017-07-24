package com.li.videoapplication.mvp.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Associate;
import com.li.videoapplication.tools.TextImageHelper;

import java.util.List;

/**
 * 适配器：查找游戏
 */
public class ChooseGameAdapter extends BaseQuickAdapter<Associate, BaseViewHolder> {


    private final TextImageHelper helper;

    public ChooseGameAdapter(List<Associate> data) {
        super(R.layout.adapter_searchgame, data);
        helper = new TextImageHelper();
    }

    @Override
    protected void convert(BaseViewHolder holder, Associate associate) {
        holder.setText(R.id.searchgame_title, associate.getGame_name())
                .setVisible(R.id.searchgame_pic,false);

      //  ImageView pic = holder.getView(R.id.searchgame_pic);
      //  helper.setImageViewImageNet(pic, associate.getFlag());
    }
}
