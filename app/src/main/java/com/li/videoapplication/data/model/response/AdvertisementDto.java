package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.LaunchImage;
import com.li.videoapplication.framework.BaseResponse2Entity;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

@SuppressWarnings("serial")
public class AdvertisementDto extends BaseResponseEntity {


    //启动海报（手游世界）
    public static final int ADVERTISEMENT_6 = 6;

    //首页-第四副轮播
    public static final int ADVERTISEMENT_7 = 7;

    //首页-热门游戏下方通栏
    public static final int ADVERTISEMENT_8 = 8;

    //福利-活动第二位置
    public static final int ADVERTISEMENT_9 = 9;

    //下载类：
    public static final int AD_CLICK_STATUS_11 = 11; //点击广告位，展示页面或直接下载
    public static final int AD_CLICK_STATUS_12 = 12; //点击展示页面下载按键并进行下载
    //落地类：
    public static final int AD_CLICK_STATUS_23 = 23; //点击广告位，展示页面

    private List<LaunchImage> data;

    public List<LaunchImage> getData() {
        return data;
    }

    public void setData(List<LaunchImage> data) {
        this.data = data;
    }
}
