package com.li.videoapplication.mvp.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.response.HomeModuleEntity;
import com.li.videoapplication.tools.LayoutParamsHelper;
import com.li.videoapplication.utils.ScreenUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/11/14.
 */

public class HomeHotGameAdapterNew extends BaseQuickAdapter<HomeModuleEntity.ADataBean.ListBean, BaseViewHolder> {



    public HomeHotGameAdapterNew(List<HomeModuleEntity.ADataBean.ListBean> data) {
        super(R.layout.adapter_home_hotgame,data);
    }

    @Override
    protected void convert(BaseViewHolder holder, HomeModuleEntity.ADataBean.ListBean listBean) {
        setLayoutParams(holder.getView(R.id.root)); //72*72

        ImageView pic = holder.getView(R.id.hotgame_pic);
        GlideHelper.displayImageWhite(mContext, listBean.getFlag(), pic);

        holder.setText(R.id.hotgame_name, listBean.getGroup_name());
    }


    private void setLayoutParams(View view) {
        LayoutParamsHelper layoutParamsHelper = new LayoutParamsHelper();
        int w = ScreenUtil.dp2px(72);
        int h = ScreenUtil.dp2px(72);
        layoutParamsHelper.setRelativeLayout(view, w, h);
    }
}
