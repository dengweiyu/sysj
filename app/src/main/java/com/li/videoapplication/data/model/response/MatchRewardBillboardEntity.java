package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;


public class MatchRewardBillboardEntity extends BaseResponseEntity {

    private String url;
    private String share_url;
    private String share_title;
    private String share_flag;
    private String share_description;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getShare_title() {
        return share_title;
    }

    public void setShare_title(String share_title) {
        this.share_title = share_title;
    }

    public String getShare_flag() {
        return share_flag;
    }

    public void setShare_flag(String share_flag) {
        this.share_flag = share_flag;
    }

    public String getShare_description() {
        return share_description;
    }

    public void setShare_description(String share_description) {
        this.share_description = share_description;
    }
}
