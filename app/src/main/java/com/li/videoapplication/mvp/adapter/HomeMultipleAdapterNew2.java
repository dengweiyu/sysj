package com.li.videoapplication.mvp.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.MyHomeModuleEntity;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.response.HomeModuleEntity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.activity.WebActivity;
import com.li.videoapplication.ui.adapter.VideoAdapter;
import com.li.videoapplication.ui.adapter.VideoQuickAdapter;
import com.li.videoapplication.ui.adapter.YouLikeAdapter;
import com.li.videoapplication.ui.view.SpanItemDecoration;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.URLUtil;
import com.li.videoapplication.views.GridViewY1;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页多布局 适配器
 */

public class HomeMultipleAdapterNew2 extends BaseMultiItemQuickAdapter<MyHomeModuleEntity.ADataBean, BaseViewHolder> {
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

    protected TextImageHelper textImageHelper = new TextImageHelper();

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

    public HomeMultipleAdapterNew2(List<MyHomeModuleEntity.ADataBean> data) {
        super(data);
        helper = new TextImageHelper();
        init();
    }

    public HomeMultipleAdapterNew2(TBaseFragment fragment, List<MyHomeModuleEntity.ADataBean> data) {
        super(data);
        mFragment = fragment;
        helper = new TextImageHelper();
        init();
    }

    private void init() {
        gameVideoAdapters = new ArrayList<>();
        gamervideoQuickAdapters = new ArrayList<>();
        //热门游戏
        addItemType(MyHomeModuleEntity.TYPE_HOT_GAME_TITLE, R.layout.adapter_hometype_hotgame);
        //广告
        addItemType(MyHomeModuleEntity.TYPE_AD, R.layout.view_banner);
        //猜你喜欢
        addItemType(MyHomeModuleEntity.TYPE_GUESS_YOU_LIKE_TITLE, R.layout.adapter_hometype_video);
        addItemType(MyHomeModuleEntity.TYPE_GUESS_YOU_LIKE, R.layout.adapter_video);
        //视频推荐
        addItemType(MyHomeModuleEntity.TYPE_SYSJ_VIDEO_TITLE, R.layout.adapter_hometype_video);
        addItemType(MyHomeModuleEntity.TYPE_SYSJ_VIDEO, R.layout.adapter_video);
        //热门解说
        addItemType(MyHomeModuleEntity.TYPE_HOT_NARRATE, R.layout.adapter_hometype_hotnarrate);
        //游戏视频
        addItemType(MyHomeModuleEntity.TYPE_VIDEO_GROUP_TITLE, R.layout.adapter_hometype_video);
        addItemType(MyHomeModuleEntity.TYPE_VIDEO_GROUP, R.layout.adapter_video);
        //玩家视频
        addItemType(MyHomeModuleEntity.TYPE_GAMER_VIDEO_TITLE, R.layout.adapter_hometype_video);
        addItemType(MyHomeModuleEntity.TYPE_GAMER_VIDEO, R.layout.adapter_video);
    }

    @Override
    protected void convert(BaseViewHolder holder, MyHomeModuleEntity.ADataBean dataBean) {
        switch (dataBean.getItemType()) {
            case MyHomeModuleEntity.TYPE_HOT_GAME_TITLE:        //热门游戏
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
            case MyHomeModuleEntity.TYPE_AD:              //通栏广告
                setAdLayoutParmas(holder);
                ImageView pic = holder.getView(R.id.banner_image);
                // helper.setImageViewImageNet(pic, item.getData().getAdvertisement().getData().get(0).getServer_pic_a());
                holder.addOnClickListener(R.id.banner_delete)
                        .addOnClickListener(R.id.banner_image);
                break;
            case MyHomeModuleEntity.TYPE_GUESS_YOU_LIKE_TITLE: //猜你喜欢（视频类型)
                holder.setVisible(R.id.hometype_guess, true)
                        .addOnClickListener(R.id.hometype_youlike_change);
                holder.setVisible(R.id.hometype_gridview, false);
                holder.setVisible(R.id.rv_hometype, false);
                break;
            case MyHomeModuleEntity.TYPE_GUESS_YOU_LIKE:
                VideoImage ykVideoImage = changeVideoImageType(dataBean.getListBean());
                initVideoParams(holder, ykVideoImage);
                break;
            case MyHomeModuleEntity.TYPE_SYSJ_VIDEO_TITLE: //视界原创（视频类型）
                holder.setVisible(R.id.hometype_sysj, true)
                        .addOnClickListener(R.id.hometype_sysj);
                holder.setVisible(R.id.hometype_gridview, false);
                holder.setVisible(R.id.rv_hometype, false);
                break;
            case MyHomeModuleEntity.TYPE_SYSJ_VIDEO:
                VideoImage sysjVideoImage = changeVideoImageType(dataBean.getListBean());
                initVideoParams(holder, sysjVideoImage);
                break;
            case MyHomeModuleEntity.TYPE_HOT_NARRATE:     //热门解说
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
            case MyHomeModuleEntity.TYPE_VIDEO_GROUP_TITLE: //游戏视频（视频类型）
                holder.setVisible(R.id.hometype_gridview, false);
                holder.setVisible(R.id.rv_hometype, false);
                holder.setVisible(R.id.hometype_game, true)
                        .addOnClickListener(R.id.hometype_game);
                holder.setText(R.id.hoemtype_game_title, dataBean.getTitle());
                break;
            case MyHomeModuleEntity.TYPE_VIDEO_GROUP:
                VideoImage groupVideoImage = changeVideoImageType(dataBean.getListBean());
                initVideoParams(holder, groupVideoImage);
                break;
            case MyHomeModuleEntity.TYPE_GAMER_VIDEO_TITLE:
                holder.setVisible(R.id.hometype_gridview, false);
                holder.setVisible(R.id.rv_hometype, false);
                holder.setVisible(R.id.hometype_game, true);
                holder.setVisible(R.id.tv_more, false);
                holder.setVisible(R.id.iv_more, false);
                holder.setText(R.id.hoemtype_game_title, dataBean.getTitle());
                break;
            case MyHomeModuleEntity.TYPE_GAMER_VIDEO:
                VideoImage gamerVideoImage = changeVideoImageType(dataBean.getListBean());
                initVideoParams(holder, gamerVideoImage);
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

    private void setRootLayoutParams(View view) {
        if (view != null) {
            WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            int srceenWidth = windowManager.getDefaultDisplay().getWidth();
            // 86/148
            int w = (srceenWidth - ScreenUtil.dp2px(10 * 2)) / 2;
            int h = w * 9 / 16;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, h);
            view.setLayoutParams(params);
        }
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

    public static VideoImage changeVideoImageType(HomeModuleEntity.ADataBean.ListBean listBean) {
        VideoImage videoImage = new VideoImage();
        HomeModuleEntity.ADataBean.ListBean dataBean = listBean;
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
        return videoImage;
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

    private void initVideoParams(BaseViewHolder holder, VideoImage videoImage) {
        setRootLayoutParams(holder.getView(R.id.root));
        TextView title = holder.getView(R.id.video_title);
        ImageView cover = holder.getView(R.id.video_cover);
        TextView allTime = holder.getView(R.id.video_allTime);
        ImageView avatar = holder.getView(R.id.civ_user);//FIXME 这里采用CircleImageView 可能会出错
        TextView nickname= holder.getView(R.id.tv_up_user_name);

        // 标题
        if (!StringUtil.isNull(videoImage.getVideo_name())) {
            textImageHelper.setTextViewText(title, videoImage.getVideo_name());
        } else if (!StringUtil.isNull(videoImage.getTitle())) {
            textImageHelper.setTextViewText(title, videoImage.getTitle());
        }
        //作者名字
        if (!StringUtil.isNull((videoImage.getNickname()))){
            textImageHelper.setTextViewText(nickname, videoImage.getNickname());
        }
        // 播放时长
        setTimeLength(allTime, videoImage);
        // 封面
        if (!StringUtil.isNull(videoImage.getFlagPath())) {
            if (URLUtil.isURL(videoImage.getFlagPath())) {
                textImageHelper.setImageViewNetAlpha(mFragment, cover, videoImage.getFlagPath());
            }
        }
        //上传主的头像
        if (!StringUtil.isNull(videoImage.getAvatar())){
            if (URLUtil.isURL(videoImage.getAvatar())){
                textImageHelper.setCircleImageNetAlpha(mFragment, avatar,videoImage.getAvatar());
            }
        }
    }

    /**
     * 视频时间长度
     */
    private void setTimeLength(TextView view, VideoImage record) {
        // 17:00
        try {
            textImageHelper.setTextViewText(view, TimeHelper.getVideoPlayTime(record.getTime_length()));
        } catch (Exception e) {
            e.printStackTrace();
            textImageHelper.setTextViewText(view, "");
        }
    }

}
