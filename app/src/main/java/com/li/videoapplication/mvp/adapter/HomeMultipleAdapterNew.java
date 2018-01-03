package com.li.videoapplication.mvp.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.response.HomeModuleEntity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.mvp.home.view.HomeLazyColumnFragment3;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.activity.WebActivity;
import com.li.videoapplication.ui.adapter.VideoAdapter;
import com.li.videoapplication.ui.adapter.VideoQuickAdapter;
import com.li.videoapplication.ui.adapter.YouLikeAdapter;
import com.li.videoapplication.ui.view.GiftItemDecoration;
import com.li.videoapplication.ui.view.SimpleItemDecoration;
import com.li.videoapplication.ui.view.SpanItemDecoration;
import com.li.videoapplication.ui.view.VideoItemDecoration;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.GridViewY1;
import com.li.videoapplication.views.StaggeredGridView;

import java.util.ArrayList;
import java.util.List;

import io.rong.eventbus.EventBus;

/**
 * 首页多布局 适配器
 */

public class HomeMultipleAdapterNew extends BaseMultiItemQuickAdapter<HomeModuleEntity.ADataBean, BaseViewHolder> {
    private static final String TAG = HomeMultipleAdapter.class.getSimpleName();

    private TBaseFragment mFragment;

    private final TextImageHelper helper;
    private YouLikeAdapter youLikeAdapter;
    private List<VideoImage> gamerVideoList;

    private boolean isScrolling = false;
    private boolean isScrollingSingle = false;
    private boolean isYourlikeChange = false;

    private List<VideoAdapter> gameVideoAdapters;
    private List<VideoQuickAdapter> gamervideoQuickAdapters;

    public void setScrolling(boolean isScrolling) {
        this.isScrolling = isScrolling;
        for (int i = 0; i < gameVideoAdapters.size(); i++) {
            gameVideoAdapters.get(i).setScrolling(isScrolling);
            gamervideoQuickAdapters.get(i).setScrolling(isScrolling);
        }
    }

    public void setScrollingSingle(boolean isScrollingSingle) {
        this.isScrollingSingle = isScrollingSingle;
    }

    /**
     * 跳转：圈子详情
     */
    private void startGameDetailActivity(Game item) {
        ActivityManager.startGroupDetailActivity(mContext, item.getGroup_id());
    }


    /**
     * 跳转：玩家动态
     */
    private void startPlayerDynamicActivity(Member member) {
        if (StringUtil.isNull(member.getId())) {
            member.setId(member.getMember_id());
        }
        ActivityManager.startPlayerDynamicActivity(mContext, member);
        UmengAnalyticsHelper.onEvent(mContext, UmengAnalyticsHelper.MAIN, "热门主播");
    }

    public HomeMultipleAdapterNew(List<HomeModuleEntity.ADataBean> data) {
        super(data);
        helper = new TextImageHelper();
        init();
    }

    public HomeMultipleAdapterNew(TBaseFragment fragment, List<HomeModuleEntity.ADataBean> data) {
        super(data);
        mFragment = fragment;
        helper = new TextImageHelper();
        init();
    }

    private void init() {
        gameVideoAdapters = new ArrayList<>();
        gamervideoQuickAdapters = new ArrayList<>();
        addItemType(HomeModuleEntity.TYPE_HOT_GAME, R.layout.adapter_hometype_hotgame);
        addItemType(HomeModuleEntity.TYPE_AD, R.layout.view_banner);
        addItemType(HomeModuleEntity.TYPE_GUESS_YOU_LIKE, R.layout.adapter_hometype_video);
        addItemType(HomeModuleEntity.TYPE_SYSJ_VIDEO, R.layout.adapter_hometype_video);//视频推荐
        addItemType(HomeModuleEntity.TYPE_HOT_NARRATE, R.layout.adapter_hometype_hotnarrate);
        addItemType(HomeModuleEntity.TYPE_VIDEO_GROUP, R.layout.adapter_hometype_video);//游戏视频
        addItemType(HomeModuleEntity.TYPE_GAMER_VIDEO, R.layout.adapter_hometype_video);//玩家视频
    }

    @Override
    protected void convert(BaseViewHolder holder, HomeModuleEntity.ADataBean dataBean) {
        switch (dataBean.getItemType()) {
            case HomeModuleEntity.TYPE_HOT_GAME:        //热门游戏
                RecyclerView recyclerView = holder.getView(R.id.homehotgame_recyclerview);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                HomeHotGameAdapter hotGameAdapter = new HomeHotGameAdapter(changeGameType(dataBean.getList()));
                recyclerView.setAdapter(hotGameAdapter);
                hotGameAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        Game record = (Game) adapter.getItem(position);
                        if (!StringUtil.isNull(record.getUrl())) { //H5游戏
                            WebActivity.startWebActivity(mContext, record.getUrl());
                        } else {
                            startGameDetailActivity(record);
                        }
                    }
                });

                holder.addOnClickListener(R.id.homehotgame_more);
                break;
            case HomeModuleEntity.TYPE_AD:              //通栏广告
                setAdLayoutParmas(holder);
                ImageView pic = holder.getView(R.id.banner_image);
                // helper.setImageViewImageNet(pic, item.getData().getAdvertisement().getData().get(0).getServer_pic_a());
                holder.addOnClickListener(R.id.banner_delete)
                        .addOnClickListener(R.id.banner_image);
                break;
            case HomeModuleEntity.TYPE_GUESS_YOU_LIKE:  //猜你喜欢（视频类型)
                holder.setVisible(R.id.hometype_guess, true)
                        .addOnClickListener(R.id.hometype_youlike_change);

                GridViewY1 youLikeGridView = holder.getView(R.id.hometype_gridview);
                if (youLikeAdapter != null) {
                    youLikeAdapter.notifyDataSetChanged();
                    if (youLikeGridView.getAdapter() == null) { // TODO: 2017/12/21 为什么有时候adapter和gridView会断开绑定
                        youLikeGridView.setAdapter(youLikeAdapter);
                    }
                } else {
                    youLikeAdapter = new YouLikeAdapter(mContext, changeVideoImageType(dataBean.getList()));
                    youLikeGridView.setAdapter(youLikeAdapter);
                }
                break;
            case HomeModuleEntity.TYPE_SYSJ_VIDEO:      //视界原创（视频类型）
                holder.setVisible(R.id.hometype_sysj, true)
                        .addOnClickListener(R.id.hometype_sysj);
                holder.setVisible(R.id.hometype_gridview, false);
//                VideoAdapter sysjAdapter = new VideoAdapter(mContext, changeVideoImageType(dataBean.getList()));
//                GridViewY1 sysjGridView = holder.getView(R.id.hometype_gridview);
//                sysjGridView.setAdapter(sysjAdapter);
//                sysjAdapter.setVideoType(dataBean.getMore_mark());
                RecyclerView rvSysj = holder.getView(R.id.rv_hometype);
                VideoQuickAdapter sysjQuickAdapter;
                if (rvSysj.getAdapter() != null) {
                    if (!isScrollingSingle) {
                        sysjQuickAdapter = (VideoQuickAdapter) rvSysj.getAdapter();
                        sysjQuickAdapter.setAllData(changeVideoImageType(dataBean.getList()));
                        sysjQuickAdapter.notifyDataSetChanged();
                    } else isScrollingSingle = false;
                } else {
                    Log.i(TAG, "新建VideoQuickAdapter示例");
                    rvSysj.setVisibility(View.VISIBLE);
                    sysjQuickAdapter =
                            new VideoQuickAdapter(mFragment, R.layout.adapter_video, changeVideoImageType(dataBean.getList()));
                    sysjQuickAdapter.setVideoType(dataBean.getMore_mark());
                    sysjQuickAdapter.setOnItemClickListener(mOnItemClickListener);
                    rvSysj.setLayoutManager(new GridLayoutManager(mContext, 2));
                    rvSysj.addItemDecoration(new SpanItemDecoration(
                            ScreenUtil.dp2px(mFragment.getContext(), 3), true, true, true, false));
                    rvSysj.setAdapter(sysjQuickAdapter);
                }
                break;
            case HomeModuleEntity.TYPE_HOT_NARRATE:     //热门解说
                RecyclerView hotNarrateRecyclerView = holder.getView(R.id.homehotnarrate_recyclerview);
                hotNarrateRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                HomeHotNarrateAdapter hotNarrateAdapter = new HomeHotNarrateAdapter(changeMemberType(dataBean.getList()));
                hotNarrateRecyclerView.setAdapter(hotNarrateAdapter);
                hotNarrateAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        Member member = (Member) adapter.getItem(position);
                        startPlayerDynamicActivity(member);
                    }
                });
                holder.addOnClickListener(R.id.home_hotnarrate_more);
                break;
            case HomeModuleEntity.TYPE_VIDEO_GROUP:     //游戏视频（视频类型）
                holder.setVisible(R.id.hometype_game, true)
                        .addOnClickListener(R.id.hometype_game);
                holder.setText(R.id.hoemtype_game_title, dataBean.getTitle());
                holder.setVisible(R.id.hometype_gridview, false);
                RecyclerView rvGameVideo = holder.getView(R.id.rv_hometype);
                VideoQuickAdapter gameVideoQuickAdapter;
                if (rvGameVideo.getAdapter() != null) {
                    if (!isScrollingSingle) {
                        gameVideoQuickAdapter = (VideoQuickAdapter) rvGameVideo.getAdapter();
                        gameVideoQuickAdapter.setAllData(changeVideoImageType(dataBean.getList()));
                        gameVideoQuickAdapter.notifyDataSetChanged();
                    } else isScrollingSingle = false;
                } else {
                    Log.i(TAG, "新建VideoQuickAdapter示例");
                    rvGameVideo.setVisibility(View.VISIBLE);
                    gameVideoQuickAdapter =
                            new VideoQuickAdapter(mFragment, R.layout.adapter_video, changeVideoImageType(dataBean.getList()));
                    gameVideoQuickAdapter.setVideoType(dataBean.getMore_mark());
                    gameVideoQuickAdapter.setOnItemClickListener(mOnItemClickListener);
                    rvGameVideo.addItemDecoration(new SpanItemDecoration(
                            ScreenUtil.dp2px(mFragment.getContext(), 3), true, true, true, false));
                    rvGameVideo.setLayoutManager(new GridLayoutManager(mContext, 2));
                    rvGameVideo.setAdapter(gameVideoQuickAdapter);
                }
//                VideoAdapter gameAdapter;
//                GridViewY1 gameGridView = holder.getView(R.id.hometype_gridview);
//                if (gameGridView.getAdapter() != null) {
//                    gameAdapter = (VideoAdapter) gameGridView.getAdapter();
//                    gameGridView.setAdapter(null);
//                    gameGridView.setAdapter(gameAdapter);
////                  gameAdapter.notifyDataSetChanged();
//                } else {
//                    gameAdapter = new VideoAdapter(mContext, changeVideoImageType(dataBean.getList()), mFragment);
//                    gameGridView.setAdapter(gameAdapter);
//                }
//                gameAdapter.setScrolling(isScrolling);
//                gameAdapter.setVideoType(dataBean.getMore_mark());
                break;
            case HomeModuleEntity.TYPE_GAMER_VIDEO:
                holder.setVisible(R.id.hometype_game, true);
                holder.setVisible(R.id.tv_more, false);
                holder.setVisible(R.id.iv_more, false);
                holder.setVisible(R.id.hometype_gridview, false);
                holder.setText(R.id.hoemtype_game_title, dataBean.getTitle());
//                gameVideoAdapters.add(gameVideoAdapter);
//                gamervideoQuickAdapters.add(gamerVideoQuickAdapter);
//                gameVideoAdapter.setScrolling(isScrolling);
//                gamerVideoQuickAdapter.setScrolling(isScrolling);
//                GridViewY1 gameVideoGridView = holder.getView(R.id.hometype_gridview);
//                VideoAdapter gameVideoAdapter;
//                if (gameVideoGridView.getAdapter() != null) {
//                    gameVideoAdapter = (VideoAdapter) gameVideoGridView.getAdapter();
//                    gameVideoAdapter.setData(changeVideoImageType(dataBean.getList()));
//                    gameVideoGridView.setAdapter(null);
//                    gameVideoGridView.setAdapter(gameVideoAdapter);
//                } else {
//                    gameVideoAdapter = new VideoAdapter(mContext, changeVideoImageType(dataBean.getList()));
//                    gameVideoGridView.setAdapter(gameVideoAdapter);
//                }
                RecyclerView rvGamerVideo = holder.getView(R.id.rv_hometype);
                VideoQuickAdapter gamerVideoQuickAdapter;

                if (rvGamerVideo.getAdapter() != null) {
                    if (!isScrollingSingle) {
                        gamerVideoQuickAdapter = (VideoQuickAdapter) rvGamerVideo.getAdapter();
                        gamerVideoQuickAdapter.setAllData(changeVideoImageType(dataBean.getList()));
                        gamerVideoQuickAdapter.notifyDataSetChanged();
                    } else isScrollingSingle = false;
                } else {
                    Log.i(TAG, "新建VideoQuickAdapter示例");
                    rvGamerVideo.setVisibility(View.VISIBLE);
                    rvGamerVideo.addItemDecoration(new SpanItemDecoration(
                            ScreenUtil.dp2px(mFragment.getContext(), 3), true, true, true, false));
                    rvGamerVideo.setLayoutManager(new GridLayoutManager(mContext, 2));
                    gamerVideoQuickAdapter =
                            new VideoQuickAdapter(mFragment, R.layout.adapter_video, changeVideoImageType(dataBean.getList()));
                    gamerVideoQuickAdapter.setOnItemClickListener(mOnItemClickListener);
                    gamerVideoQuickAdapter.setVideoType(dataBean.getMore_mark());
                    rvGamerVideo.setAdapter(gamerVideoQuickAdapter);
                }

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
        if (youLikeAdapter != null) {
            youLikeAdapter.clear();
            youLikeAdapter.addAll(guessVideoList);
            youLikeAdapter.notifyDataSetChanged();
            isYourlikeChange = true;
        }
    }

    public YouLikeAdapter getGuessListAdapter() {
        return youLikeAdapter;
    }

    public static List<VideoImage> changeVideoImageType(List<HomeModuleEntity.ADataBean.ListBean> listBean) {
        List<VideoImage> videoImages = new ArrayList<>();
        for (int i = 0; i < listBean.size(); i++) {
            VideoImage videoImage = new VideoImage();
            HomeModuleEntity.ADataBean.ListBean dataBean = listBean.get(i);
            videoImage.setFlag(dataBean.getFlag());
            videoImage.setVideo_id(dataBean.getVideo_id());
            videoImage.setTitle(dataBean.getTitle());
            videoImage.setClick_count(dataBean.getClick_count());
            videoImage.setYk_url(dataBean.getYk_url());
            videoImage.setQn_key(dataBean.getQn_key());
            videoImage.setFlagPath(dataBean.getFlagPath());
            videoImage.setIsRecommend(dataBean.getIsRecommend());
            videoImage.setPic_flsp(dataBean.getPic_flsp());
            videoImage.setAvatar(dataBean.getAvatar());
            videoImage.setTime_length(dataBean.getTime_length());
            videoImage.setNickname(dataBean.getNickname());
            videoImage.setMember_id(dataBean.getMember_id());
            videoImages.add(videoImage);
        }
        return videoImages;
    }

    public List<Member> changeMemberType(List<HomeModuleEntity.ADataBean.ListBean> listBeen) {
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < listBeen.size(); i++) {
            Member member = new Member();
            HomeModuleEntity.ADataBean.ListBean dataBean = listBeen.get(i);
            member.setMember_id(dataBean.getMember_id());
            member.setNickname(dataBean.getNickname());
            member.setAvatar(dataBean.getAvatar());
            member.setSex(dataBean.getSex());
            //还有一个上传时间没有匹配,因为前版本没有用上传时间这个字段
            members.add(member);
        }
        return members;
    }

    public List<Game> changeGameType(List<HomeModuleEntity.ADataBean.ListBean> listBeen) {
        List<Game> games = new ArrayList<>();
        for (int i = 0; i < listBeen.size(); i++) {
            Game game = new Game();
            HomeModuleEntity.ADataBean.ListBean dataBean = listBeen.get(i);
            game.setGroup_id(dataBean.getGroup_id());
            game.setGame_id(dataBean.getGame_id());
            game.setGroup_name(dataBean.getGroup_name());
            game.setFlag(dataBean.getFlag());
            game.setUrl(dataBean.getUrl());
            games.add(game);
        }
        return games;
    }

    /**
     * 跳转：视频播放
     */
    private void startVideoPlayActivity(VideoImage item) {
        if (!StringUtil.isNull(item.getVideo_id())) {
            ActivityManager.startVideoPlayActivity(mContext, item);
        }
    }

    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            VideoImage record = (VideoImage) adapter.getItem(position);
            startVideoPlayActivity(record);
            Log.i(TAG, "video_id是：" + record.getVideo_id());
            if (!StringUtil.isNull(record.getMore_mark()))
                UmengAnalyticsHelper.onMainGameEvent(mContext, record.getMore_mark());
        }


    };

}
