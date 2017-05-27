package com.li.videoapplication.data.database;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.li.videoapplication.data.model.entity.Tag;
import com.li.videoapplication.data.upload.Contants;
import com.li.videoapplication.framework.BaseEntity;
import com.li.videoapplication.tools.JSONHelper;
import com.li.videoapplication.utils.StringUtil;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 实体：视频（录屏，本地导入，编辑）
 */
@Table(name = VideoCaptureEntity.TABLE_NAME)
public class VideoCaptureEntity extends BaseEntity {

    // 文件源，录制的视频（rec）/外部导入的视频(ext)/ 拍摄cam
    public final static String VIDEO_SOURCE_REC = "rec";
    public final static String VIDEO_SOURCE_EXT = "ext";
    public final static String VIDEO_SOURCE_CUT = "cut";
    public final static String VIDEO_SOURCE_CAM = "cam";

    // 视频状态，在本地（local）/上传中(uploading)/在云端（startActivityVideoActivity）/暂停上传(pause)/云端隐藏(hide)
    public final static String VIDEO_STATION_LOCAL = "local";
    public final static String VIDEO_STATION_SHOW = "show";
    public final static String VIDEO_STATION_HIDE = "hide";
    public final static String VIDEO_STATION_UPLOADING = "uploading";
    public final static String VIDEO_STATION_PAUSE = "pause";

    public final static String TABLE_NAME = "video_capture";
    public final static String ID = "id";
    public final static String VIDEO_NAME = "video_name";
    public final static String VIDEO_PATH = "video_path";
    public final static String IMAGE_PATH = "image_path";
    public final static String VIDEO_SOURCE = "video_source";
    public final static String VIDEO_STATION = "video_station";

    public final static String UPVIDEO_PRECENT = "upvideo_precent";
    public final static String SHARE_CHANNEL = "share_channel";
    public final static String SHARE_FLAG = "share_flag";
    public final static String MEMBER_ID = "member_id";
    public final static String GAME_ID = "game_id";
    public final static String MATCH_ID = "match_id";
    public final static String JOIN_ID = "join_id";
    public final static String UPVIDEO_TITLE = "upvideo_title";
    public final static String UPVIDEO_DESCRIPTION = "upvideo_description";
    public final static String UPVIDEO_ISOFFICIAL = "upvideo_isofficial";
    public final static String UPVIDEO_ID = "upvideo_id";
    public final static String UPVIDEO_QNKEY = "upvideo_qnkey";
    public final static String UPVIDEO_KEY = "upvideo_key";
    public final static String UPVIDEO_TOKEN = "upvideo_token";
    public final static String UPVIDEO_TOKENTIME = "upvideo_tokentime";

    public final static String PK_ID = "pk_id";

    public final static String GAME_NAME = "game_name";
    public final static String MATCH_NAME = "match_name";

    public final static String UPVIDEO_GAMETAGS = "upvideo_gametags";
    public final static String UPVIDEO_TAGS = "upvideo_tags";
    public final static String UPVIDEO_FLAG = "upvideo_flag";
    public final static String UPVIDEO_COVERTOKEN = "upvideo_covertoken";

    @Column(name = ID, isId = true, autoGen = true)
    private int id;

    @Column(name = VIDEO_NAME)
    private String video_name = "";

    @Column(name = VIDEO_PATH)
    private String video_path = "";

    @Column(name = IMAGE_PATH)
    private String image_path = "";

    @Column(name = VIDEO_SOURCE)
    private String video_source = "";

    @Column(name = VIDEO_STATION)
    private String video_station = "";

    @Column(name = UPVIDEO_PRECENT)
    private double upvideo_precent;

    @Column(name = SHARE_CHANNEL)
    private String share_channel = "";

    @Column(name = SHARE_FLAG)
    private boolean share_flag;

    @Column(name = MEMBER_ID)
    private String member_id = "";

    @Column(name = GAME_ID)
    private String game_id = "";

    @Column(name = MATCH_ID)
    private String match_id = "";

    @Column(name = JOIN_ID)
    private String join_id = "";

    @Column(name = UPVIDEO_TITLE)
    private String upvideo_title = "";

    @Column(name = UPVIDEO_DESCRIPTION)
    private String upvideo_description = "";

    @Column(name = UPVIDEO_ISOFFICIAL)
    private int upvideo_isofficial;

    @Column(name = UPVIDEO_ID)
    private String upvideo_id = "";

    @Column(name = UPVIDEO_QNKEY)
    private String upvideo_qnkey = "";

    @Column(name = UPVIDEO_KEY)
    private String upvideo_key = "";

    @Column(name = UPVIDEO_TOKEN)
    private String upvideo_token = "";

    @Column(name = UPVIDEO_TOKENTIME)
    private long upvideo_tokentime;

    @Column(name = PK_ID)
    private String pk_id = "";

    @Column(name = GAME_NAME)
    private String game_name = "";

    @Column(name = MATCH_NAME)
    private String match_name = "";

    @Column(name = UPVIDEO_GAMETAGS)
    private String upvideo_gametags = "";

    @Column(name = UPVIDEO_TAGS)
    private String upvideo_tags = "";

    @Column(name = UPVIDEO_FLAG)
    private String upvideo_flag = "";

    @Column(name = UPVIDEO_COVERTOKEN)
    private String upvideo_covertoken = "";


    public String getUpvideo_covertoken() {
        return upvideo_covertoken;
    }

    public void setUpvideo_covertoken(String upvideo_covertoken) {
        this.upvideo_covertoken = upvideo_covertoken;
    }

    public String getUpvideo_flag() {
        return upvideo_flag;
    }

    public void setUpvideo_flag(String upvideo_flag) {
        this.upvideo_flag = upvideo_flag;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public String getMatch_name() {
        return match_name;
    }

    public void setMatch_name(String match_name) {
        this.match_name = match_name;
    }

    public long getUpvideo_tokentime() {
        return upvideo_tokentime;
    }

    public void setUpvideo_tokentime(long upvideo_tokentime) {
        this.upvideo_tokentime = upvideo_tokentime;
    }

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideo_name() {
        return video_name;
    }

    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }

    public String getVideo_path() {
        return video_path;
    }

    public void setVideo_path(String video_path) {
        this.video_path = video_path;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getVideo_source() {
        return video_source;
    }

    public void setVideo_source(String video_source) {
        this.video_source = video_source;
    }

    public String getVideo_station() {
        return video_station;
    }

    public void setVideo_station(String video_station) {
        this.video_station = video_station;
    }

    public double getUpvideo_precent() {
        return upvideo_precent;
    }

    public void setUpvideo_precent(double upvideo_precent) {
        this.upvideo_precent = upvideo_precent;
    }

    public String getShare_channel() {
        return share_channel;
    }

    public void setShare_channel(String share_channel) {
        this.share_channel = share_channel;
    }

    public boolean isShare_flag() {
        return share_flag;
    }

    public void setShare_flag(boolean share_flag) {
        this.share_flag = share_flag;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
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

    public String getJoin_id() {
        return join_id;
    }

    public void setJoin_id(String join_id) {
        this.join_id = join_id;
    }

    public String getUpvideo_title() {
        return upvideo_title;
    }

    public void setUpvideo_title(String upvideo_title) {
        this.upvideo_title = upvideo_title;
    }

    public String getUpvideo_description() {
        return upvideo_description;
    }

    public void setUpvideo_description(String upvideo_description) {
        this.upvideo_description = upvideo_description;
    }

    public String getUpvideo_id() {
        return upvideo_id;
    }

    public void setUpvideo_id(String upvideo_id) {
        this.upvideo_id = upvideo_id;
    }

    public String getUpvideo_qnkey() {
        return upvideo_qnkey;
    }

    public void setUpvideo_qnkey(String upvideo_qnkey) {
        this.upvideo_qnkey = upvideo_qnkey;
    }

    public String getUpvideo_key() {
        return upvideo_key;
    }

    public void setUpvideo_key(String upvideo_key) {
        this.upvideo_key = upvideo_key;
    }

    public String getUpvideo_token() {
        return upvideo_token;
    }

    public void setUpvideo_token(String upvideo_token) {
        this.upvideo_token = upvideo_token;
    }

    public int getUpvideo_isofficial() {
        return upvideo_isofficial;
    }

    public void setUpvideo_isofficial(int upvideo_isofficial) {
        this.upvideo_isofficial = upvideo_isofficial;
    }


    static class DownloadEntity{
        private int state = Contants.STATUS_END;

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }

    /**
     * 文件最后修改时间
     */
    private long lastModified;

    public long getLastModified() {
        try {
            File file = new File(video_path);
            lastModified = file.lastModified();
        } catch (Exception e) {
            e.printStackTrace();
            lastModified = 0l;
        }
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public List<String> getUpvideo_gametags_2() {
        List<String> list = new ArrayList<>();
        if (!StringUtil.isNull(upvideo_gametags)) {
            String[] arr = upvideo_gametags.split(",");
            list = Arrays.asList(arr);
        }
        return list;
    }

    public String getUpvideo_gametags() {
        return upvideo_gametags;
    }

    public void setUpvideo_gametags(String upvideo_gametags) {
        this.upvideo_gametags = upvideo_gametags;
    }

    public void setUpvideo_gametags_2(List<String> list) {

        StringBuffer sb = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < (list.size() - 1)) {
                    sb.append(list.get(i));
                    sb.append(",");
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        upvideo_gametags = sb.toString();
    }


    public String getUpvideo_tags() {
        return upvideo_tags;
    }

    public void setUpvideo_tags(String upvideo_tags) {
        this.upvideo_tags = upvideo_tags;
    }

    public List<Tag> getUpvideo_tags_2() {
        List<Tag> list = null;
        if (!StringUtil.isNull(upvideo_tags)) {
            try {
                list = JSONHelper.GSON.fromJson(upvideo_tags, new TypeToken<List<Tag>>(){}.getType());
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public void setUpvideo_tags_2(List<Tag> list) {
        try {
            upvideo_tags = JSONHelper.to(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
