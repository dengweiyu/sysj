package com.li.videoapplication.data.model.response;

import com.google.gson.annotations.SerializedName;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 * 打赏与陪练消息
 */

public class RewardAndPlayWithMsgEntity extends BaseResponseEntity {

    /**
     * code : 10000
     * data : {"page_count":1,"list":[{"msg_id":"6","content":"您的陪练订单已结束，快来确认评价吧","time":"1498808732","relation_id":"6","member_id":"969106","relevance_member_id":"1201867","mark":"1","member_name":"隔壁小王38","icon":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/qq_1473229998368.jpg"},{"msg_id":"4","content":"您的陪练订单已结束，快来确认评价吧","time":"1498643596","relation_id":"4","member_id":"969106","relevance_member_id":"60","mark":"1","member_name":"隔壁小王38","icon":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/qq_1473229998368.jpg"},{"msg_id":"2","content":"小小舞接受了您的陪练订单，快来一起玩耍","time":"1498643589","relation_id":"4","member_id":"969106","relevance_member_id":"60","mark":"1","member_name":"隔壁小王38","icon":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/qq_1473229998368.jpg"}]}
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
         * page_count : 1
         * list : [{"msg_id":"6","content":"您的陪练订单已结束，快来确认评价吧","time":"1498808732","relation_id":"6","member_id":"969106","relevance_member_id":"1201867","mark":"1","member_name":"隔壁小王38","icon":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/qq_1473229998368.jpg"},{"msg_id":"4","content":"您的陪练订单已结束，快来确认评价吧","time":"1498643596","relation_id":"4","member_id":"969106","relevance_member_id":"60","mark":"1","member_name":"隔壁小王38","icon":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/qq_1473229998368.jpg"},{"msg_id":"2","content":"小小舞接受了您的陪练订单，快来一起玩耍","time":"1498643589","relation_id":"4","member_id":"969106","relevance_member_id":"60","mark":"1","member_name":"隔壁小王38","icon":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/qq_1473229998368.jpg"}]
         */

        private int page_count;
        private List<ListBean> list;

        public int getPage_count() {
            return page_count;
        }

        public void setPage_count(int page_count) {
            this.page_count = page_count;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * msg_id : 6
             * content : 您的陪练订单已结束，快来确认评价吧
             * time : 1498808732
             * relation_id : 6
             * member_id : 969106
             * relevance_member_id : 1201867
             * mark : 1
             * member_name : 隔壁小王38
             * icon : http://apps.ifeimo.com/Public/Uploads/Member/Avatar/qq_1473229998368.jpg
             */

            private String msg_id;
            private String content;
            private String time;
            private String relation_id;
            private String member_id;
            private String relevance_member_id;
            private String mark;
            private String member_name;
            private String icon;
            private String user_id;
            private String coach_id;
            @SerializedName("status")
            private String statusX;
            private String symbol;
            private String msg_type;

            public String getSymbol() {
                return symbol;
            }

            public void setSymbol(String symbol) {
                this.symbol = symbol;
            }

            public String getMsg_type() {
                return msg_type;
            }

            public void setMsg_type(String msg_type) {
                this.msg_type = msg_type;
            }

            public String getStatusX() {
                return statusX;
            }

            public void setStatusX(String statusX) {
                this.statusX = statusX;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getCoach_id() {
                return coach_id;
            }

            public void setCoach_id(String coach_id) {
                this.coach_id = coach_id;
            }

            public String getMsg_id() {
                return msg_id;
            }

            public void setMsg_id(String msg_id) {
                this.msg_id = msg_id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getRelation_id() {
                return relation_id;
            }

            public void setRelation_id(String relation_id) {
                this.relation_id = relation_id;
            }

            public String getMember_id() {
                return member_id;
            }

            public void setMember_id(String member_id) {
                this.member_id = member_id;
            }

            public String getRelevance_member_id() {
                return relevance_member_id;
            }

            public void setRelevance_member_id(String relevance_member_id) {
                this.relevance_member_id = relevance_member_id;
            }

            public String getMark() {
                return mark;
            }

            public void setMark(String mark) {
                this.mark = mark;
            }

            public String getMember_name() {
                return member_name;
            }

            public void setMember_name(String member_name) {
                this.member_name = member_name;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }
        }
    }
}
