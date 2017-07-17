package com.li.videoapplication.data.model.response;

import com.google.gson.annotations.SerializedName;
import com.li.videoapplication.framework.BaseResponseEntity;

/**
 * 教练详情
 */

public class CoachDetailEntity extends BaseResponseEntity {


    /**
     * data : {"member_id":"60","game_level":"最强王者","win_rate":"88%","order_total":"48","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/56f8a34a68ff6.jpg","name":"小小舞","win_num":"0","lose_num":"0","score":"5.0","game_level_icon":"http://img.17sysj.com/app_coach_strongest_king.png","game_name":"王者荣耀","status":3}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * member_id : 60
         * game_level : 最强王者
         * win_rate : 88%
         * order_total : 48
         * avatar : http://apps.ifeimo.com/Public/Uploads/Member/Avatar/56f8a34a68ff6.jpg
         * name : 小小舞
         * win_num : 0
         * lose_num : 0
         * score : 5.0
         * game_level_icon : http://img.17sysj.com/app_coach_strongest_king.png
         * game_name : 王者荣耀
         * status : 3
         */

        private String member_id;
        private String game_level;
        private String win_rate;
        private String order_total;
        private String avatar;
        private String name;
        private String win_num;
        private String lose_num;
        private String score;
        private String game_level_icon;
        private String game_name;
        @SerializedName("status")
        private int statusX;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getWin_num() {
            return win_num;
        }

        public void setWin_num(String win_num) {
            this.win_num = win_num;
        }

        public String getLose_num() {
            return lose_num;
        }

        public void setLose_num(String lose_num) {
            this.lose_num = lose_num;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getGame_level_icon() {
            return game_level_icon;
        }

        public void setGame_level_icon(String game_level_icon) {
            this.game_level_icon = game_level_icon;
        }

        public String getGame_name() {
            return game_name;
        }

        public void setGame_name(String game_name) {
            this.game_name = game_name;
        }

        public int getStatusX() {
            return statusX;
        }

        public void setStatusX(int statusX) {
            this.statusX = statusX;
        }
    }
}
