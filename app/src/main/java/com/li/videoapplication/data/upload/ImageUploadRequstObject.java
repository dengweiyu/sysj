package com.li.videoapplication.data.upload;

import com.li.videoapplication.data.local.ScreenShotEntity;
import com.li.videoapplication.framework.BaseEntity;

import java.util.List;

/**
 * 图片上传请求参数
 */
public class ImageUploadRequstObject extends BaseEntity {

    private String pk_id;

    private String member_id;

    private List<ScreenShotEntity> data;

    private String team_id;

    public ImageUploadRequstObject(String pk_id, String member_id, List<ScreenShotEntity> data,String team_id) {
        this.pk_id = pk_id;
        this.member_id = member_id;
        this.data = data;
        this.team_id = team_id;
    }

    public String getPk_id() {
        return pk_id;
    }

    public String getTeam_id() {
        return team_id;
    }

    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public List<ScreenShotEntity> getData() {
        return data;
    }

    public void setData(List<ScreenShotEntity> data) {
        this.data = data;
    }
}
