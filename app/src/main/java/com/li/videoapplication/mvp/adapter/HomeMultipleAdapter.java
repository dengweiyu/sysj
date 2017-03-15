package com.li.videoapplication.mvp.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.response.ChangeGuessEntity;
import com.li.videoapplication.data.model.entity.HomeDto;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.WebActivity;
import com.li.videoapplication.ui.adapter.VideoAdapter;
import com.li.videoapplication.ui.adapter.YouLikeAdapter;
import com.li.videoapplication.ui.fragment.GroupdetailVideoFragment;
import com.li.videoapplication.ui.view.BannerView;
import com.li.videoapplication.utils.GDTUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.GridViewY1;
import com.qq.e.ads.nativ.NativeADDataRef;

import java.util.List;

/**
 * 首页多重布局适配器
 */
public class HomeMultipleAdapter extends BaseMultiItemQuickAdapter<HomeDto, BaseViewHolder> {
    private static final String TAG = HomeMultipleAdapter.class.getSimpleName();

    private final TextImageHelper helper;
    private YouLikeAdapter youLikeAdapter;

    /**
     * 跳转：圈子详情
     */
    private void startGameDetailActivity(Game item) {
        ActivityManeger.startGroupDetailActivity(mContext, item.getGroup_id());
    }

    /**
     * 跳转：玩家动态
     */
    private void startPlayerDynamicActivity(Member member) {
        if (StringUtil.isNull(member.getId())) {
            member.setId(member.getMember_id());
        }
        ActivityManeger.startPlayerDynamicActivity(mContext, member);
        UmengAnalyticsHelper.onEvent(mContext, UmengAnalyticsHelper.MAIN, "热门主播");
    }

    public HomeMultipleAdapter(List<HomeDto> data) {
        super(data);
        addItemType(HomeDto.TYPE_HOTGAME, R.layout.adapter_hometype_hotgame);
        addItemType(HomeDto.TYPE_AD, R.layout.view_banner);
        addItemType(HomeDto.TYPE_GUESSYOULIKE, R.layout.adapter_hometype_video);
        addItemType(HomeDto.TYPE_SYSJVIDEO, R.layout.adapter_hometype_video);
        addItemType(HomeDto.TYPE_HOTNARRATE, R.layout.adapter_hometype_hotnarrate);
        addItemType(HomeDto.TYPE_VIDEOGROUP, R.layout.adapter_hometype_video);
        helper = new TextImageHelper();
    }

    @Override
    protected void convert(BaseViewHolder holder, HomeDto item) {
        switch (holder.getItemViewType()) {
            case HomeDto.TYPE_HOTGAME://热门游戏
                RecyclerView recyclerView = holder.getView(R.id.homehotgame_recyclerview);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                HomeHotGameAdapter hotGameAdapter = new HomeHotGameAdapter(item.getHotGame().getList());
                recyclerView.setAdapter(hotGameAdapter);
                recyclerView.addOnItemTouchListener(new OnItemClickListener() {
                    @Override
                    public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int i) {
                        Game record = (Game) adapter.getItem(i);
                        if (!StringUtil.isNull(record.getUrl())) { //H5游戏
                            WebActivity.startWebActivity(mContext, record.getUrl());
                        } else {
                            startGameDetailActivity(record);
                        }
                    }
                });
                holder.addOnClickListener(R.id.homehotgame_more);
                break;
            case HomeDto.TYPE_AD://通栏广告
                setAdLayoutParmas(holder);
                ImageView pic = holder.getView(R.id.banner_image);
                helper.setImageViewImageNet(pic, item.getAdvertisement().getData().get(0).getServer_pic_a());
                holder.addOnClickListener(R.id.banner_delete)
                        .addOnClickListener(R.id.banner_image);
                break;
            case HomeDto.TYPE_GUESSYOULIKE://猜你喜欢
                holder.setVisible(R.id.hometype_guess, true)
                        .addOnClickListener(R.id.hometype_youlike_change);

                GridViewY1 youLikeGridView = holder.getView(R.id.hometype_gridview);
                youLikeAdapter = new YouLikeAdapter(mContext, item.getGuessVideo().getList());
                youLikeGridView.setAdapter(youLikeAdapter);
                break;
            case HomeDto.TYPE_SYSJVIDEO://视界原创
                holder.setVisible(R.id.hometype_sysj, true)
                        .addOnClickListener(R.id.hometype_sysj);
                VideoAdapter sysjAdapter = new VideoAdapter(mContext, item.getSysjVideo().getList());
                GridViewY1 sysjGridView = holder.getView(R.id.hometype_gridview);
                sysjGridView.setAdapter(sysjAdapter);
                sysjAdapter.setVideoType(item.getSysjVideo().getMore_mark());
                break;
            case HomeDto.TYPE_VIDEOGROUP://游戏视频
                holder.setVisible(R.id.hometype_game, true)
                        .addOnClickListener(R.id.hometype_game);
                holder.setText(R.id.hoemtype_game_title, item.getVideoGroupItem().getTitle());
                VideoAdapter gameAdapter = new VideoAdapter(mContext, item.getVideoGroupItem().getList());
                GridViewY1 gameGridView = holder.getView(R.id.hometype_gridview);
                gameGridView.setAdapter(gameAdapter);
                gameAdapter.setVideoType(item.getVideoGroupItem().getMore_mark());
                break;
            case HomeDto.TYPE_HOTNARRATE://热门主播
                RecyclerView hotNarrateRecyclerView = holder.getView(R.id.homehotnarrate_recyclerview);
                hotNarrateRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                HomeHotNarrateAdapter hotNarrateAdapter = new HomeHotNarrateAdapter(item.getHotMemberVideo().getList());
                hotNarrateRecyclerView.setAdapter(hotNarrateAdapter);
                hotNarrateRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
                    @Override
                    public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int i) {
                        Member member = (Member) adapter.getItem(i);
                        startPlayerDynamicActivity(member);
                    }
                });
                holder.addOnClickListener(R.id.home_hotnarrate_more);
                break;
        }
    }

    private void setAdLayoutParmas(BaseViewHolder holder) {
        RelativeLayout container = holder.getView(R.id.container);
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        int srceenWidth = windowManager.getDefaultDisplay().getWidth();
        int height = (int) (((float) (srceenWidth)) / 7.5);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) container.getLayoutParams();
        if (params == null)
            params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height);
        params.width = srceenWidth;
        params.height = height;
        container.setLayoutParams(params);
    }

    public void changeGuessVideo(List<VideoImage> guessVideoList) {
        youLikeAdapter.clear();
        youLikeAdapter.addAll(guessVideoList);
        youLikeAdapter.notifyDataSetChanged();
    }
}
