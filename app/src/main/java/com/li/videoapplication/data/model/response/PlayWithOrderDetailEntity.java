package com.li.videoapplication.data.model.response;

import com.google.gson.annotations.SerializedName;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 订单详情
 */

public class PlayWithOrderDetailEntity extends BaseResponseEntity {


    /**
     * code : 10000
     * defaultReason : ["到约定时间找不到玩家","信息选错了","不想玩了"]
     * coach : {"member_id":"1201867","nickname":"Zmjie","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/default21.JPG","score":"5.0"}
     * user : {"member_id":"1722092","nickname":"潜影随形","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/594781f965475.jpg","orderCount":"0"}
     * data : {"order_id":"8","coach_id":"1201867","user_id":"1722092","grade":"荣耀黄金","inning":"1","status":"3","add_time":"1498103958","start_time":"1498103958","refund_reason":"","default_refund_reason":"","price_total":"100.00","has_result":"0","evaluate_counter":"0","gameArea":"微信大区","gameMode":"匹配","statusText":"陪练开始","orderNum":"201706228","gameName":"王者荣耀"}
     */

    private CoachBean coach;
    private UserBean user;
    private DataBean data;
    private List<String> defaultReason;

    public CoachBean getCoach() {
        return coach;
    }

    public void setCoach(CoachBean coach) {
        this.coach = coach;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public List<String> getDefaultReason() {
        return defaultReason;
    }

    public void setDefaultReason(List<String> defaultReason) {
        this.defaultReason = defaultReason;
    }

    public static class CoachBean implements Serializable {
        /**
         * member_id : 1201867
         * nickname : Zmjie
         * avatar : http://apps.ifeimo.com/Public/Uploads/Member/Avatar/default21.JPG
         * score : 5.0
         */

        private String member_id;
        private String nickname;
        private String avatar;
        private String score;

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }
    }

    public static class UserBean implements Serializable {
        /**
         * member_id : 1722092
         * nickname : 潜影随形
         * avatar : http://apps.ifeimo.com/Public/Uploads/Member/Avatar/594781f965475.jpg
         * orderCount : 0
         */

        private String member_id;
        private String nickname;
        private String avatar;
        private String orderCount;

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getOrderCount() {
            return orderCount;
        }

        public void setOrderCount(String orderCount) {
            this.orderCount = orderCount;
        }
    }

    public static class DataBean implements Serializable {
        /**
         * order_id : 8
         * coach_id : 1201867
         * user_id : 1722092
         * grade : 荣耀黄金
         * inning : 1
         * status : 3
         * add_time : 1498103958
         * start_time : 1498103958
         * refund_reason :
         * default_refund_reason :
         * price_total : 100.00
         * has_result : 0
         * evaluate_counter : 0
         * gameArea : 微信大区
         * gameMode : 匹配
         * statusText : 陪练开始
         * orderNum : 201706228
         * gameName : 王者荣耀
         */

        private String order_id;
        private String coach_id;
        private String user_id;
        private String grade;
        private String inning;
        @SerializedName("status")
        private String statusX;
        private String add_time;
        private String start_time;
        private String refund_reason;
        private String default_refund_reason;
        private String price_total;
        private String has_result;
        private String evaluate_counter;
        private String gameArea;
        private String gameMode;
        private String statusText;
        private String orderNum;
        private String gameName;

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getCoach_id() {
            return coach_id;
        }

        public void setCoach_id(String coach_id) {
            this.coach_id = coach_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getInning() {
            return inning;
        }

        public void setInning(String inning) {
            this.inning = inning;
        }

        public String getStatusX() {
            return statusX;
        }

        public void setStatusX(String statusX) {
            this.statusX = statusX;
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

        public String getRefund_reason() {
            return refund_reason;
        }

        public void setRefund_reason(String refund_reason) {
            this.refund_reason = refund_reason;
        }

        public String getDefault_refund_reason() {
            return default_refund_reason;
        }

        public void setDefault_refund_reason(String default_refund_reason) {
            this.default_refund_reason = default_refund_reason;
        }

        public String getPrice_total() {
            return price_total;
        }

        public void setPrice_total(String price_total) {
            this.price_total = price_total;
        }

        public String getHas_result() {
            return has_result;
        }

        public void setHas_result(String has_result) {
            this.has_result = has_result;
        }

        public String getEvaluate_counter() {
            return evaluate_counter;
        }

        public void setEvaluate_counter(String evaluate_counter) {
            this.evaluate_counter = evaluate_counter;
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

        public String getStatusText() {
            return statusText;
        }

        public void setStatusText(String statusText) {
            this.statusText = statusText;
        }

        public String getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(String orderNum) {
            this.orderNum = orderNum;
        }

        public String getGameName() {
            return gameName;
        }

        public void setGameName(String gameName) {
            this.gameName = gameName;
        }
    }
}
