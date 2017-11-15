package com.li.videoapplication.mvp.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.response.HomeModuleEntity;
import com.li.videoapplication.ui.activity.WebActivity;
import com.li.videoapplication.ui.adapter.YouLikeAdapter;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.GridViewY1;

import java.util.List;

/**
 * 首页多布局 适配器
 */

public class HomeMultipleAdapterNew extends BaseMultiItemQuickAdapter<HomeModuleEntity.ADataBean, BaseViewHolder> {


    public HomeMultipleAdapterNew(List<HomeModuleEntity.ADataBean> data) {
        super(data);

        addItemType(HomeModuleEntity.TYPE_HOT_GAME, R.layout.adapter_hometype_hotgame);
        addItemType(HomeModuleEntity.TYPE_AD, R.layout.view_banner);
        addItemType(HomeModuleEntity.TYPE_GUESS_YOU_LIKE, R.layout.adapter_hometype_video);
        addItemType(HomeModuleEntity.TYPE_SYSJ_VIDEO, R.layout.adapter_hometype_video);
        addItemType(HomeModuleEntity.TYPE_HOT_NARRATE, R.layout.adapter_hometype_hotnarrate);
        addItemType(HomeModuleEntity.TYPE_VIDEO_GROUP, R.layout.adapter_hometype_video);
    }



    @Override
    protected void convert(BaseViewHolder holder, HomeModuleEntity.ADataBean dataBean) {
        switch (dataBean.getItemType()){
            case HomeModuleEntity.TYPE_HOT_GAME:        //热门游戏
                RecyclerView recyclerView = holder.getView(R.id.homehotgame_recyclerview);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                HomeHotGameAdapterNew hotGameAdapter = new HomeHotGameAdapterNew(dataBean.getList());
                recyclerView.setAdapter(hotGameAdapter);

                break;
            case HomeModuleEntity.TYPE_AD:              //通栏广告

                break;
            case HomeModuleEntity.TYPE_GUESS_YOU_LIKE:  //猜你喜欢

                break;
            case HomeModuleEntity.TYPE_SYSJ_VIDEO:      //视界原创

                break;
            case HomeModuleEntity.TYPE_HOT_NARRATE:     //热门解说

                break;
            case HomeModuleEntity.TYPE_VIDEO_GROUP:     //游戏视频

                break;

        }
    }
}
