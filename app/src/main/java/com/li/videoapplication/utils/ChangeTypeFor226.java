package com.li.videoapplication.utils;

import com.li.videoapplication.data.model.entity.Banner;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.response.HomeModuleEntity;

import java.util.ArrayList;
import java.util.List;

/**类型转换类 2.2.6版本中修改了数据类型，为了兼容可以使用下面的方法
 * Created by y on 2017/11/22.
 */
 public  class  ChangeTypeFor226 {
    public static List<VideoImage> changeVideoImageType(List<HomeModuleEntity.ADataBean.ListBean> listBean){
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
            videoImage.setAvatar(dataBean.getAvatar());
            videoImage.setTime_length(dataBean.getTime_length());
            videoImage.setNickname(dataBean.getNickname());
            videoImage.setMember_id(dataBean.getMember_id());
            videoImages.add(videoImage);
        }
        return videoImages;
    }
    public static List<Member> changeMemberType(List<HomeModuleEntity.ADataBean.ListBean> listBeen){
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
    public static List<Game> changeGameType(List<HomeModuleEntity.ADataBean.ListBean> listBeen){
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
    public static List<Banner> changBannerType(List<HomeModuleEntity.ADataBean.ListBean> listBeen){
        List<Banner> bannerData = new ArrayList<>();

        for (int i = 0; i < listBeen.size(); i++) {
            Banner banner = new Banner();
            HomeModuleEntity.ADataBean.ListBean dataBean = listBeen.get(i);

            banner.setType(dataBean.getType());
            banner.setVideo_id(dataBean.getVideo_id());
            banner.setPackage_id(dataBean.getPackage_id());

            banner.setFlag(dataBean.getFlag());
            banner.setFlagPath(dataBean.getFlagPath());
            banner.setTitle(dataBean.getTitle());
            banner.setUrl(dataBean.getUrl());
            banner.setQn_key(dataBean.getQn_key());

            bannerData.add(banner);
        }
        return bannerData;
    }
}
