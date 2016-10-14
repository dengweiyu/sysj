package com.li.videoapplication.data.upload;

import com.li.videoapplication.data.local.ScreenShotEntity;
import com.li.videoapplication.framework.BaseEntity;

import java.util.List;

/**
 * 图片上传请求参数
 */
public class ImageShareRequestObject208 extends BaseEntity{

    private String match_id;

    private String member_id;

    private String title;

    private List<ScreenShotEntity> data;

    public ImageShareRequestObject208(String match_id, String member_id, String title, List<ScreenShotEntity> data) {
        this.match_id = match_id;
        this.member_id = member_id;
        this.title = title;
        this.data = data;
    }

    public String getMatch_id() {
        return match_id;
    }

    public void setMatch_id(String match_id) {
        this.match_id = match_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ScreenShotEntity> getData() {
        return data;
    }

    public void setData(List<ScreenShotEntity> data) {
        this.data = data;
    }
}
