package com.li.videoapplication.data.model.response;

import com.google.gson.annotations.SerializedName;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 接单列表
 */

public class PlayWithTakeOrderEntity extends BaseResponseEntity {

    /**
     * code : 10000
     * page_count : 1
     * data : [{"order_id":"6","status":"3","inning":"2","price_total":"100","add_time":"1498802521","start_time":"1498802531","avatar":"https://sapp.17sysj.com/Public/Uploads/Member/Avatar/qq_1473229998368.jpg","nickname":"隔壁小王38","orderTotal":"1","orderNum":"201706306","gameArea":"qq","gameMode":"匹配","trainingLevel":"倔强青铜","statusText":"陪练开始"}]
     */

    private int page_count;
    private List<DataBean> data;
    private int coachStatus;

    public int getCoachStatus() {
        return coachStatus;
    }

    public void setCoachStatus(int coachStatus) {
        this.coachStatus = coachStatus;
    }

    public int getPage_count() {
        return page_count;
    }

    public void setPage_count(int page_count) {
        this.page_count = page_count;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * order_id : 6
         * status : 3
         * inning : 2
         * price_total : 100
         * add_time : 1498802521
         * start_time : 1498802531
         * avatar : https://sapp.17sysj.com/Public/Uploads/Member/Avatar/qq_1473229998368.jpg
         * nickname : 隔壁小王38
         * orderTotal : 1
         * orderNum : 201706306
         * gameArea : qq
         * gameMode : 匹配
         * trainingLevel : 倔强青铜
         * statusText : 陪练开始
         */

        private String order_id;
        @SerializedName("status")
        private String statusX;
        private String inning;
        private String price_total;
        private String add_time;
        private String start_time;
        private String avatar;
        private String nickname;
        private String orderTotal;
        private String orderNum;
        private String gameArea;
        private String gameMode;
        private String trainingLevel;
        private String statusText;
        private String gameName;
        private String user_id;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getGameName() {

            return gameName;
        }

        public void setGameName(String gameName) {
            this.gameName = gameName;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getStatusX() {
            return statusX;
        }

        public void setStatusX(String statusX) {
            this.statusX = statusX;
        }

        public String getInning() {
            return inning;
        }

        public void setInning(String inning) {
            this.inning = inning;
        }

        public String getPrice_total() {
            return price_total;
        }

        public void setPrice_total(String price_total) {
            this.price_total = price_total;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
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

        public String getOrderTotal() {
            return orderTotal;
        }

        public void setOrderTotal(String orderTotal) {
            this.orderTotal = orderTotal;
        }

        public String getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(String orderNum) {
            this.orderNum = orderNum;
        }

        public String getGameArea() {
            return gameArea;
        }

        public void setGameArea(String gameArea) {
            this.gameArea = gameArea;
        }

        public String getGameMode() {
            return gameMode;
        }

        public void setGameMode(String gameMode) {
            this.gameMode = gameMode;
        }

        public String getTrainingLevel() {
            return trainingLevel;
        }

        public void setTrainingLevel(String trainingLevel) {
            this.trainingLevel = trainingLevel;
        }

        public String getStatusText() {
            return statusText;
        }

        public void setStatusText(String statusText) {
            this.statusText = statusText;
        }
    }
}
