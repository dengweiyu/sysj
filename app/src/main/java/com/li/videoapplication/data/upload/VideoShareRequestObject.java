package com.li.videoapplication.data.upload;

import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.framework.BaseEntity;

import java.util.List;

/**
 * 视频上传请求参数
 */
public class VideoShareRequestObject extends BaseEntity {

    public static final int STATUS_START = 1;
    public static final int STATUS_PAUSE = 2;
    public static final int STATUS_RESUME = 3;

    private int status;

    private String shareChannel;

    private String member_id;

    private String title;

    private String game_id;

    private String match_id;

    private String description;

    private int isofficial;

    private VideoCaptureEntity data;

    private List<String> game_tags;

    public VideoShareRequestObject(int status) {
        this.status = status;
    }

    public VideoShareRequestObject(int status, VideoCaptureEntity data) {
        this.status = status;
        this.data = data;
    }

    public VideoShareRequestObject(int status,
                                   String shareChannel,
                                   String member_id,
                                   String title,
                                   String game_id,
                                   String match_id,
                                   String description,
                                   int isofficial,
                                   VideoCaptureEntity data) {
        this.status = status;
        this.shareChannel = shareChannel;
        this.member_id = member_id;
        this.title = title;
        this.game_id = game_id;
        this.match_id = match_id;
        this.description = description;
        this.isofficial = isofficial;
        this.data = data;
    }


    //带标签
    public VideoShareRequestObject(int status,
                                   String shareChannel,
                                   String member_id,
                                   String title,
                                   String game_id,
                                   String match_id,
                                   String description,
                                   int isofficial,
                                   List<String> game_tags,
                                   VideoCaptureEntity data) {
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
    }


    public List<String> getGame_tags() {
        return game_tags;
    }

    public void setGame_tags(List<String> game_tags) {
        this.game_tags = game_tags;
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
