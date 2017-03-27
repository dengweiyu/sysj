package com.li.videoapplication.data.model.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.li.videoapplication.data.model.response.AdvertisementDto;
import com.li.videoapplication.framework.BaseResponse2Entity;

import java.util.List;

/**
 * 数据传输对象：首页
 */
public class HomeDto extends BaseResponse2Entity implements MultiItemEntity {

    public static final int TYPE_HOTGAME = 2;//热门游戏
    public static final int TYPE_AD = 3;//通栏广告
    public static final int TYPE_GUESSYOULIKE = 4;//猜你喜欢
    public static final int TYPE_SYSJVIDEO = 5;//视界原创
    public static final int TYPE_HOTNARRATE = 6;//热门主播
    public static final int TYPE_VIDEOGROUP = 7;//游戏视频

    private int itemType;

    public HomeDto(int itemType, GameGroup hotGame) {
        this.itemType = itemType;
        this.hotGame = hotGame;
    }

    public HomeDto(int itemType, VideoImageGroup videoGroup) {
        this.itemType = itemType;
        if (itemType == TYPE_GUESSYOULIKE)
            this.guessVideo = videoGroup;
        if (itemType == TYPE_SYSJVIDEO)
            this.sysjVideo = videoGroup;
        if (itemType == TYPE_VIDEOGROUP)
            this.videoGroupItem = videoGroup;
    }

    public HomeDto(int itemType, MemberGroup hotMemberVideo) {
        this.itemType = itemType;
        this.hotMemberVideo = hotMemberVideo;
    }

    public HomeDto(int itemType, AdvertisementDto advertisement) {
        this.itemType = itemType;
        this.advertisement = advertisement;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    //################## 首页 ######################

    //广告
    private List<Banner> banner;

    //热门游戏
    private GameGroup hotGame;

    //猜你喜欢
    private VideoImageGroup guessVideo;

    //视界原创的视频列表
    private VideoImageGroup sysjVideo;

    //热门解说
    private MemberGroup hotMemberVideo;

    //各游戏的视频列表 data.videoList[]
    private List<VideoImageGroup> videoList;

    //各游戏的视频列表 data.videoList[].get(i)
    private VideoImageGroup videoGroupItem;

    //################## 广告 ######################

    private AdvertisementDto advertisement;

    //################## 首页每日任务 ######################

    private int num;

    private int amount;


    public AdvertisementDto getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(AdvertisementDto advertisement) {
        this.advertisement = advertisement;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public VideoImageGroup getVideoGroupItem() {
        return videoGroupItem;
    }

    public void setVideoGroupItem(VideoImageGroup videoGroupItem) {
        this.videoGroupItem = videoGroupItem;
    }

    public List<Banner> getBanner() {
        return banner;
    }

    public void setBanner(List<Banner> banner) {
        this.banner = banner;
    }

    public List<VideoImageGroup> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<VideoImageGroup> videoList) {
        this.videoList = videoList;
    }

    public GameGroup getHotGame() {
        return hotGame;
    }

    public void setHotGame(GameGroup hotGame) {
        this.hotGame = hotGame;
    }

    public VideoImageGroup getGuessVideo() {
        return guessVideo;
    }

    public void setGuessVideo(VideoImageGroup guessVideo) {
        this.guessVideo = guessVideo;
    }

    public VideoImageGroup getSysjVideo() {
        return sysjVideo;
    }

    public void setSysjVideo(VideoImageGroup sysjVideo) {
        this.sysjVideo = sysjVideo;
    }

    public MemberGroup getHotMemberVideo() {
        return hotMemberVideo;
    }

    public void setHotMemberVideo(MemberGroup hotMemberVideo) {
        this.hotMemberVideo = hotMemberVideo;
    }
}
