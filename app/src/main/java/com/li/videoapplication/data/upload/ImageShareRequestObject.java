package com.li.videoapplication.data.upload;

import com.li.videoapplication.data.local.ScreenShotEntity;
import com.li.videoapplication.framework.BaseEntity;

import java.util.List;

/**
 * 图片上传请求参数
 */
public class ImageShareRequestObject extends BaseEntity{

    private String game_id;

    private String member_id;

    private String title;

    private String description;

    private List<ScreenShotEntity> data;

    public ImageShareRequestObject(String game_id, String member_id, String title, List<ScreenShotEntity> data) {
        this.game_id = game_id;
        this.member_id = member_id;
        this.title = title;
        this.data = data;
    }

    public ImageShareRequestObject(String game_id, String member_id, String title, String description, List<ScreenShotEntity> data) {
        this.game_id = game_id;
        this.member_id = member_id;
        this.title = title;
        this.description = description;
        this.data = data;
    }

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ScreenShotEntity> getData() {
        return data;
    }

    public void setData(List<ScreenShotEntity> data) {
        this.data = data;
    }
}
