package com.li.videoapplication.data.model.entity;

import com.li.videoapplication.framework.BaseResponse2Entity;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;


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

    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data extends BaseResponse2Entity {

        private int ad_location_id;
        private int ad_id;
        private String server_pic_a;
        private String download_android;
        private String go_url;

        public String getGo_url() {
            return go_url;
        }

        public void setGo_url(String go_url) {
            this.go_url = go_url;
        }

        public int getAd_id() {
            return ad_id;
        }

        public void setAd_id(int ad_id) {
            this.ad_id = ad_id;
        }

        public int getAd_location_id() {
            return ad_location_id;
        }

        public void setAd_location_id(int ad_location_id) {
            this.ad_location_id = ad_location_id;
        }

        public String getDownload_android() {
            return download_android;
        }

        public void setDownload_android(String download_android) {
            this.download_android = download_android;
        }

        public String getServer_pic_a() {
            return server_pic_a;
        }

        public void setServer_pic_a(String server_pic_a) {
            this.server_pic_a = server_pic_a;
        }
    }
}
