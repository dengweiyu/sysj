package com.li.videoapplication.mvp.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.tools.LayoutParamsHelper;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.utils.ScreenUtil;

import java.util.List;

/**
 * 适配器：主页-热门主播， 精彩推荐-金牌主播
 */
public class HomeHotNarrateAdapter extends BaseQuickAdapter<Member, BaseViewHolder> {

    private TextImageHelper helper;

    public HomeHotNarrateAdapter(List<Member> data) {
        super(R.layout.adapter_home_hotnarrate, data);
        helper = new TextImageHelper();
    }

    @Override
    protected void convert(BaseViewHolder holder, Member item) {
        setLayoutParams(holder.getView(R.id.root)); //72*72

        ImageView pic = holder.getView(R.id.hotnarrate_pic);
        helper.setImageViewImageNet(pic, item.getAvatar());

        holder.setText(R.id.hotnarrate_name ,item.getNickname());
    }

    private void setLayoutParams(View view) {
        LayoutParamsHelper layoutParamsHelper = new LayoutParamsHelper();
        int w = ScreenUtil.dp2px(72);
        int h = ScreenUtil.dp2px(72);
        layoutParamsHelper.setLinearLayout(view, w, h);
    }
}
