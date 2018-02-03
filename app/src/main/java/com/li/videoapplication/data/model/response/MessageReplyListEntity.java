package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 * Created by cx on 2018/1/29.
 */

public class MessageReplyListEntity extends BaseResponseEntity {

    private List<MessageReplyListEntity.DataBean> data;

    public List<MessageReplyListEntity.DataBean> getData() {
        return data;
    }

    public void setData(List<MessageReplyListEntity.DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

        /**
         "title": "视频回复",
         "content": "可米侠回复了你的视频评论，快去看看吧...",
         "interface_url": "http://apps.ifeimo.com/Sysj222/Message/videoReplyMessage",
         "symbol": "videoReply",
         "mark": "1",
         "time": "1517074442"
         */
        private String title;
        private String content;
        private String interface_url;
        private String symbol;
        private String mark;
        private String time;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }


        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getInterface_url() {
            return interface_url;
        }

        public void setInterface_url(String interface_url) {
            this.interface_url = interface_url;
        }
    }
}
