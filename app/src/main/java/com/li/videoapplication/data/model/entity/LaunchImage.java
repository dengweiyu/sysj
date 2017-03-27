package com.li.videoapplication.data.model.entity;

import com.li.videoapplication.framework.BaseEntity;

import java.util.List;

/**
 * 实体类：启动图片广告
 */
@SuppressWarnings("serial")
public class LaunchImage extends BaseEntity {

    //2.1.4
    private int ad_location_id;
    private int ad_id;
    private String server_pic_a;
    private String download_android;
    private String go_url;
    private int ad_type;
    private String ad_title;
    private String game_id;
    private List<Download> download_desc;
    private String module;//跳转类型
    private String associated_id;//跳转相关id

    public String getAssociated_id() {
        return associated_id;
    }

    public void setAssociated_id(String associated_id) {
        this.associated_id = associated_id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public List<Download> getDownload_desc() {
        return download_desc;
    }

    public void setDownload_desc(List<Download> download_desc) {
        this.download_desc = download_desc;
    }

    public String getAd_title() {
        return ad_title;
    }

    public void setAd_title(String ad_title) {
        this.ad_title = ad_title;
    }

    public int getAd_type() {
        return ad_type;
    }

    public void setAd_type(int ad_type) {
        this.ad_type = ad_type;
    }

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
