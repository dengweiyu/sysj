package com.li.videoapplication.data.upload;

import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.framework.BaseEntity;

import java.util.List;

/**
 * 视频上传请求参数
 */
public class VideoUploadRequestObject extends BaseEntity {

    public static final int STATUS_START = 1;
    public static final int STATUS_PAUSE = 2;
    public static final int STATUS_RESUME = 3;

    private int status;

    private String pk_id;

    private String shareChannel;

    private String member_id;

    private String title;

    private String game_id;

    private String match_id;

    private String description;

    private int isofficial;

    private VideoCaptureEntity data;

    private List<String> game_tags;

    private String goods_id;


    public VideoUploadRequestObject(int status) {
        this.status = status;
    }

    public VideoUploadRequestObject(int status, VideoCaptureEntity data) {
        this.status = status;
        this.data = data;
    }

    //带标签
    public VideoUploadRequestObject(int status,
                                    String shareChannel,
                                    String member_id,
                                    String title,
                                    String game_id,
                                    String match_id,
                                    String description,
                                    int isofficial,
                                    List<String> game_tags,
                                    VideoCaptureEntity data,
                                    String goods_id) {
        this.status = status;
        this.shareChannel = shareChannel;
        this.member_id = member_id;
        this.title = title;
        this.game_id = game_id;
        this.match_id = match_id;
        this.description = description;
        this.isofficial = isofficial;
        this.game_tags = game_tags;
        this.data = data;
        this.goods_id = goods_id;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public List<String> getGame_tags() {
        return game_tags;
    }

    public void setGame_tags(List<String> game_tags) {
        this.game_tags = game_tags;
    }

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getShareChannel() {
        return shareChannel;
    }

    public void setShareChannel(String shareChannel) {
        this.shareChannel = shareChannel;
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

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public String getMatch_id() {
        return match_id;
    }

    public void setMatch_id(String match_id) {
        this.match_id = match_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public VideoCaptureEntity getData() {
        return data;
    }

    public void setData(VideoCaptureEntity data) {
        this.data = data;
    }

    public int getIsofficial() {
        return isofficial;
    }

    public void setIsofficial(int isofficial) {
        this.isofficial = isofficial;
    }
}
