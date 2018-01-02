package com.li.videoapplication.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.response.HomeModuleEntity;
import com.li.videoapplication.mvp.adapter.HomeHotNarrateAdapter;
import com.li.videoapplication.mvp.adapter.HomeMultipleAdapter;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.GridViewY1;

import java.util.ArrayList;
import java.util.List;

/**首页分栏下面的王者荣耀的分栏的适配器
 * Created by y on 2017/11/16.
 */

public class HomeColumnWZRYAdapter extends BaseMultiItemQuickAdapter<HomeModuleEntity.ADataBean,BaseViewHolder> {
    private static final String TAG = HomeMultipleAdapter.class.getSimpleName();

    private final TextImageHelper helper;


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

    //构造方法
    public HomeColumnWZRYAdapter(List<HomeModuleEntity.ADataBean> data){
        super(data);
        addItemType(HomeModuleEntity.TYPE_HOT_NARRATE, R.layout.adapter_hometype_hotnarrate);//热门解说
        addItemType(HomeModuleEntity.TYPE_AD, R.layout.view_banner);//广告
        addItemType(HomeModuleEntity.TYPE_SYSJ_VIDEO, R.layout.adapter_hometype_video);//手游推荐
        addItemType(HomeModuleEntity.TYPE_VIDEO_GROUP, R.layout.adapter_hometype_video);//玩家视频
        helper = new TextImageHelper();
    }

    @Override
    protected void convert(BaseViewHolder holder, HomeModuleEntity.ADataBean dataBean) {
        switch (dataBean.getItemType()){
            case HomeModuleEntity.TYPE_HOT_NARRATE:     //热门解说
                RecyclerView hotNarrateRecyclerView = holder.getView(R.id.homehotnarrate_recyclerview);
                hotNarrateRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                HomeHotNarrateAdapter hotNarrateAdapter = new HomeHotNarrateAdapter(changeMemberType(dataBean.getList()));
                hotNarrateAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        Member member = (Member) adapter.getItem(position);
                        startPlayerDynamicActivity(member);
                    }
                });
                hotNarrateRecyclerView.setAdapter(hotNarrateAdapter);
                holder.addOnClickListener(R.id.home_hotnarrate_more);
                break;
            case HomeModuleEntity.TYPE_AD:              //通栏广告
                setAdLayoutParmas(holder);
                ImageView pic = holder.getView(R.id.banner_image);
                //helper.setImageViewImageNet(pic, item.getData().getAdvertisement().getData().get(0).getServer_pic_a());
                holder.addOnClickListener(R.id.banner_delete)
                        .addOnClickListener(R.id.banner_image);
                break;
            case HomeModuleEntity.TYPE_SYSJ_VIDEO:      //视界原创
                holder.setVisible(R.id.hometype_sysj, true)
                        .addOnClickListener(R.id.hometype_sysj);
                VideoAdapter sysjAdapter = new VideoAdapter(mContext,changeVideoImageType(dataBean.getList()));
                GridViewY1 sysjGridView = holder.getView(R.id.hometype_gridview);
                sysjGridView.setAdapter(sysjAdapter);
                sysjAdapter.setVideoType(dataBean.getMore_mark());
                break;
            case HomeModuleEntity.TYPE_VIDEO_GROUP:     //游戏视频
                holder.setVisible(R.id.hometype_game, true)
                        .addOnClickListener(R.id.hometype_game);
                holder.setText(R.id.hoemtype_game_title, dataBean.getTitle());
                VideoAdapter gameAdapter = new VideoAdapter(mContext, changeVideoImageType(dataBean.getList()));
                GridViewY1 gameGridView = holder.getView(R.id.hometype_gridview);
                gameGridView.setAdapter(gameAdapter);
                gameAdapter.setVideoType(dataBean.getMore_mark());
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
    public List<VideoImage> changeVideoImageType(List<HomeModuleEntity.ADataBean.ListBean> listBean){
        List<VideoImage> videoImages =new ArrayList<>();
        for (int i = 0; i <listBean.size() ; i++) {
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
            videoImages.add(videoImage);
        }
        return videoImages;
    }
    public List<Member> changeMemberType(List<HomeModuleEntity.ADataBean.ListBean> listBeen){
        List<Member> members = new ArrayList<>();
        for (int i = 0; i <listBeen.size() ; i++) {
            Member member = new Member();
            HomeModuleEntity.ADataBean.ListBean dataBean =listBeen.get(i);
            member.setMember_id(dataBean.getMember_id());
            member.setNickname(dataBean.getNickname());
            member.setAvatar(dataBean.getAvatar());
            member.setSex(dataBean.getSex());
            //还有一个上传时间没有匹配,因为前版本没有用上传时间这个字段
            members.add(member);
        }
        return members;
    }
    public List<Game> changeGameType(List<HomeModuleEntity.ADataBean.ListBean> listBeen){
        List<Game> games = new ArrayList<>();
        for (int i = 0; i <listBeen.size() ; i++) {
            Game game =new Game();
            HomeModuleEntity.ADataBean.ListBean dataBean =listBeen.get(i);
            game.setGroup_id(dataBean.getGroup_id());
            game.setGame_id(dataBean.getGame_id());
            game.setGroup_name(dataBean.getGroup_name());
            game.setFlag(dataBean.getFlag());
            game.setUrl(dataBean.getUrl());
            games.add(game);
        }
        return games;
    }

}
