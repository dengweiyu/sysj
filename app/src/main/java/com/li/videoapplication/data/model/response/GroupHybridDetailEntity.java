package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 *
 */

public class GroupHybridDetailEntity extends BaseResponseEntity {


    /**
     * AData : [{"name":"详情","game_id":"10171","flagName":"details","goUrl":"http://m.17sysj.com/GameGroup/gameGroupDetail"},{"name":"攻略","game_id":"10171","flagName":"strategy","goUrl":"http://m.17sysj.com/GameGroup/strategyDetail"},{"name":"评价","game_id":"10171","flagName":"appraise","goUrl":"http://m.17sysj.com/GameGroupComment"},{"name":"视频","game_id":"10171","flagName":"video","goUrl":""}]
     * OData : {"game_type_id":"23","group_id":"1876","game_id":"10171","group_name":"荒野行动","video_num":85265,"attention_num":"3091","flag":"http://apps.ifeimo.com/Public/Uploads/Game/Flag/5a090a2804e8c.jpg","group_banner":"","game_description":"《荒野行动》真正容纳100人开局！实时语音，组队开黑，战术配合花样迭出！道具装备种类丰富，自由搭配！针对手机端精心设计多种操作模式，提供极致竞技体验！","a_download":"http://downali.game.uc.cn/s/4/12/20171107095817a5b4c8_game_common_uc_platform_g83_21_20171106_192208.apk?x-oss-process=udf/uc-apk,AiDDjEwYLVhhAsOP51a20730a49f95da","i_download":"https://itunes.apple.com/cn/app/%E8%8D%92%E9%87%8E%E8%A1%8C%E5%8A%A8/id1279207754","chatroom_group_id":"3993","download_num":"1","type_name":"飞行射击","privateIM":true,"is_gift":0,"tick":0,"hasEvent":true}
     * edition : 2
     */

    private ODataBean OData;
    private String edition;
    private List<ADataBean> AData;

    public ODataBean getOData() {
        return OData;
    }

    public void setOData(ODataBean OData) {
        this.OData = OData;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public List<ADataBean> getAData() {
        return AData;
    }

    public void setAData(List<ADataBean> AData) {
        this.AData = AData;
    }

    public static class ODataBean {
        /**
         * game_type_id : 23
         * group_id : 1876
         * game_id : 10171
         * group_name : 荒野行动
         * video_num : 85265
         * attention_num : 3091
         * flag : http://apps.ifeimo.com/Public/Uploads/Game/Flag/5a090a2804e8c.jpg
         * group_banner :
         * game_description : 《荒野行动》真正容纳100人开局！实时语音，组队开黑，战术配合花样迭出！道具装备种类丰富，自由搭配！针对手机端精心设计多种操作模式，提供极致竞技体验！
         * a_download : http://downali.game.uc.cn/s/4/12/20171107095817a5b4c8_game_common_uc_platform_g83_21_20171106_192208.apk?x-oss-process=udf/uc-apk,AiDDjEwYLVhhAsOP51a20730a49f95da
         * i_download : https://itunes.apple.com/cn/app/%E8%8D%92%E9%87%8E%E8%A1%8C%E5%8A%A8/id1279207754
         * chatroom_group_id : 3993
         * download_num : 1
         * type_name : 飞行射击
         * privateIM : true
         * is_gift : 0
         * tick : 0
         * hasEvent : true
         */

        private String game_type_id;
        private String group_id;
        private String game_id;
        private String group_name;
        private int video_num;
        private String attention_num;
        private String flag;
        private String group_banner;
        private String game_description;
        private String a_download;
        private String i_download;
        private String chatroom_group_id;
        private String download_num;
        private String type_name;
        private boolean privateIM;
        private int is_gift;
        private int tick;
        private boolean hasEvent;

        public String getGame_type_id() {
            return game_type_id;
        }

        public void setGame_type_id(String game_type_id) {
            this.game_type_id = game_type_id;
        }

        public String getGroup_id() {
            return group_id;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
        }

        public String getGame_id() {
            return game_id;
        }

        public void setGame_id(String game_id) {
            this.game_id = game_id;
        }

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }

        public int getVideo_num() {
            return video_num;
        }

        public void setVideo_num(int video_num) {
            this.video_num = video_num;
        }

        public String getAttention_num() {
            return attention_num;
        }

        public void setAttention_num(String attention_num) {
            this.attention_num = attention_num;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getGroup_banner() {
            return group_banner;
        }

        public void setGroup_banner(String group_banner) {
            this.group_banner = group_banner;
        }

        public String getGame_description() {
            return game_description;
        }

        public void setGame_description(String game_description) {
            this.game_description = game_description;
        }

        public String getA_download() {
            return a_download;
        }

        public void setA_download(String a_download) {
            this.a_download = a_download;
        }

        public String getI_download() {
            return i_download;
        }

        public void setI_download(String i_download) {
            this.i_download = i_download;
        }

        public String getChatroom_group_id() {
            return chatroom_group_id;
        }

        public void setChatroom_group_id(String chatroom_group_id) {
            this.chatroom_group_id = chatroom_group_id;
        }

        public String getDownload_num() {
            return download_num;
        }

        public void setDownload_num(String download_num) {
            this.download_num = download_num;
        }

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }

        public boolean isPrivateIM() {
            return privateIM;
        }

        public void setPrivateIM(boolean privateIM) {
            this.privateIM = privateIM;
        }

        public int getIs_gift() {
            return is_gift;
        }

        public void setIs_gift(int is_gift) {
            this.is_gift = is_gift;
        }

        public int getTick() {
            return tick;
        }

        public void setTick(int tick) {
            this.tick = tick;
        }

        public boolean isHasEvent() {
            return hasEvent;
        }

        public void setHasEvent(boolean hasEvent) {
            this.hasEvent = hasEvent;
        }
    }

    public static class ADataBean {
        /**
         * name : 详情
         * game_id : 10171
         * flagName : details
         * goUrl : http://m.17sysj.com/GameGroup/gameGroupDetail
         */

        private String name;
        private String game_id;
        private String flagName;
        private String goUrl;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGame_id() {
            return game_id;
        }

        public void setGame_id(String game_id) {
            this.game_id = game_id;
        }

        public String getFlagName() {
            return flagName;
        }

        public void setFlagName(String flagName) {
            this.flagName = flagName;
        }

        public String getGoUrl() {
            return goUrl;
        }

        public void setGoUrl(String goUrl) {
            this.goUrl = goUrl;
        }
    }
}
