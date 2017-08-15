package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 * 对教练的评价
 */

public class CoachCommentEntity  extends BaseResponseEntity{

    /**
     * AData : [{"user_id":"4607176","score":"5.0","order_id":"845","nickname":"Believe","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/default21.JPG","addTime":"1502363177"},{"user_id":"4520407","score":"5.0","order_id":"834","nickname":"很贵","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/default22.JPG","addTime":"1502358937"},{"user_id":"4553885","score":"5.0","order_id":"818","nickname":"(=^_^=)辣小子狠拽凸^-^凸","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/default2.JPG","addTime":"1502336982"},{"user_id":"3909025","score":"5.0","order_id":"787","nickname":"叶知秋","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/default1.JPG","addTime":"1502330140"},{"user_id":"3790883","score":"5.0","order_id":"768","nickname":"圊舂哪么骚","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/default28.JPG","addTime":"1502259288"},{"user_id":"4411478","score":"5.0","order_id":"751","nickname":"陈嘉骐","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/598269e22ea40.jpg","addTime":"1502341202"},{"user_id":"4287507","score":"5.0","order_id":"734","nickname":"凉介-","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/5987c7d976b92.jpg","addTime":"1502326801"},{"user_id":"4287507","score":"5.0","order_id":"733","nickname":"凉介-","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/5987c7d976b92.jpg","addTime":"1502262001"},{"user_id":"4277627","score":"5.0","order_id":"728","nickname":"千丘符霖","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/5987eeca22b2c.jpg","addTime":"1502280001"},{"user_id":"3170959","score":"5.0","order_id":"724","nickname":"小墨解说游戏","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/591324efa7a03.jpg","addTime":"1502272801"}]
     * OData : null
     * pageCount : 2
     */

    private Object OData;
    private int pageCount;
    private List<ADataBean> AData;

    public Object getOData() {
        return OData;
    }

    public void setOData(Object OData) {
        this.OData = OData;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List<ADataBean> getAData() {
        return AData;
    }

    public void setAData(List<ADataBean> AData) {
        this.AData = AData;
    }

    public static class ADataBean {
        /**
         * user_id : 4607176
         * score : 5.0
         * order_id : 845
         * nickname : Believe
         * avatar : http://apps.ifeimo.com/Public/Uploads/Member/Avatar/default21.JPG
         * addTime : 1502363177
         */

        private String user_id;
        private String score;
        private String order_id;
        private String nickname;
        private String avatar;
        private String addTime;
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
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

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }
    }
}
