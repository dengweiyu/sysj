package com.li.videoapplication.data.model.response;

import com.google.gson.annotations.SerializedName;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 * 教练列表
 */

public class CoachListEntity extends BaseResponseEntity {


    /**
     * data : {"page_count":2,"link":{"order_num_icon":"http://img.17sysj.com/testOrder.jpg","win_rate_icon":"http://img.17sysj.com/testWinRate.jpg"},"include":[{"member_id":"54","game_level":"永恒钻石","win_rate":"4%","order_total":"48","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/568cc7b4cfe62.jpg","nickname":"胖哥","score":"5.0","status":1,"game_level_icon":"http://img.17sysj.com/testzuiqiang.jpg"},{"member_id":"1201867","game_level":"永恒钻石","win_rate":"56%","order_total":"89","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/default21.JPG","nickname":"Zmjie","score":"5.0","status":3,"game_level_icon":"http://img.17sysj.com/testzuiqiang.jpg"},{"member_id":"60","game_level":"最强王者","win_rate":"88%","order_total":"48","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/56f8a34a68ff6.jpg","nickname":"小小舞","score":"5.0","status":3,"game_level_icon":"http://img.17sysj.com/testzuiqiang.jpg"},{"member_id":"10026","game_level":"荣耀王者","win_rate":"23%","order_total":"123","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/default.png","nickname":"玩家10026","score":"3.0","status":3,"game_level_icon":"http://img.17sysj.com/testzuiqiang.jpg"}]}
     */

    private String notice;

    private DataBean data;

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * page_count : 2
         * link : {"order_num_icon":"http://img.17sysj.com/testOrder.jpg","win_rate_icon":"http://img.17sysj.com/testWinRate.jpg"}
         * include : [{"member_id":"54","game_level":"永恒钻石","win_rate":"4%","order_total":"48","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/568cc7b4cfe62.jpg","nickname":"胖哥","score":"5.0","status":1,"game_level_icon":"http://img.17sysj.com/testzuiqiang.jpg"},{"member_id":"1201867","game_level":"永恒钻石","win_rate":"56%","order_total":"89","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/default21.JPG","nickname":"Zmjie","score":"5.0","status":3,"game_level_icon":"http://img.17sysj.com/testzuiqiang.jpg"},{"member_id":"60","game_level":"最强王者","win_rate":"88%","order_total":"48","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/56f8a34a68ff6.jpg","nickname":"小小舞","score":"5.0","status":3,"game_level_icon":"http://img.17sysj.com/testzuiqiang.jpg"},{"member_id":"10026","game_level":"荣耀王者","win_rate":"23%","order_total":"123","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/default.png","nickname":"玩家10026","score":"3.0","status":3,"game_level_icon":"http://img.17sysj.com/testzuiqiang.jpg"}]
         */

        private int page_count;
        private LinkBean link;
        private List<IncludeBean> include;

        public int getPage_count() {
            return page_count;
        }

        public void setPage_count(int page_count) {
            this.page_count = page_count;
        }

        public LinkBean getLink() {
            return link;
        }

        public void setLink(LinkBean link) {
            this.link = link;
        }

        public List<IncludeBean> getInclude() {
            return include;
        }

        public void setInclude(List<IncludeBean> include) {
            this.include = include;
        }

        public static class LinkBean {
            /**
             * order_num_icon : http://img.17sysj.com/testOrder.jpg
             * win_rate_icon : http://img.17sysj.com/testWinRate.jpg
             */

            private String order_num_icon;
            private String win_rate_icon;

            public String getOrder_num_icon() {
                return order_num_icon;
            }

            public void setOrder_num_icon(String order_num_icon) {
                this.order_num_icon = order_num_icon;
            }

            public String getWin_rate_icon() {
                return win_rate_icon;
            }

            public void setWin_rate_icon(String win_rate_icon) {
                this.win_rate_icon = win_rate_icon;
            }
        }

        public static class IncludeBean {
            /**
             * member_id : 54
             * game_level : 永恒钻石
             * win_rate : 4%
             * order_total : 48
             * avatar : http://apps.ifeimo.com/Public/Uploads/Member/Avatar/568cc7b4cfe62.jpg
             * nickname : 胖哥
             * score : 5.0
             * status : 1
             * game_level_icon : http://img.17sysj.com/testzuiqiang.jpg
             */

            private String member_id;
            private String game_level;
            private String win_rate;
            private String order_total;
            private String avatar;
            private String nickname;
            private String score;
            @SerializedName("status")
            private int statusX;
            private String game_level_icon;

            public String getMember_id() {
                return member_id;
            }

            public void setMember_id(String member_id) {
                this.member_id = member_id;
            }

            public String getGame_level() {
                return game_level;
            }

            public void setGame_level(String game_level) {
                this.game_level = game_level;
            }

            public String getWin_rate() {
                return win_rate;
            }

            public void setWin_rate(String win_rate) {
                this.win_rate = win_rate;
            }

            public String getOrder_total() {
                return order_total;
            }

            public void setOrder_total(String order_total) {
                this.order_total = order_total;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getScore() {
                return score;
            }

            public void setScore(String score) {
                this.score = score;
            }

            public int getStatusX() {
                return statusX;
            }

            public void setStatusX(int statusX) {
                this.statusX = statusX;
            }

            public String getGame_level_icon() {
                return game_level_icon;
            }

            public void setGame_level_icon(String game_level_icon) {
                this.game_level_icon = game_level_icon;
            }
        }
    }
}
