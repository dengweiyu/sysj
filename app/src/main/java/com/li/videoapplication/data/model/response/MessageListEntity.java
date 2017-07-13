package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 * 消息列表
 */

public class MessageListEntity extends BaseResponseEntity {

    /**
     * code : 10000
     * data : [{"content":"暂无消息","time":"1499406515","mark_num":"0","symbol":"training","cover":"http://apps.ifeimo.com/Public/Message/images/training.png","title":"陪练订单消息","interface_url":"https://sapp.17sysj.com/Sysj222/Message/trainingOrderMessage"},{"content":"暂无消息","time":"1499406515","mark_num":"0","symbol":"reward","cover":"http://apps.ifeimo.com/Public/Message/images/reward.png","title":"打赏消息","interface_url":"https://sapp.17sysj.com/Sysj222/Message/rewardMessage"},{"content":"收藏了您的视频《广告宣传》AFEDFABD13252F3ABB962FA7BBC7B764","time":"1497494945","mark_num":"0","symbol":"video","cover":"http://apps.ifeimo.com/Public/Message/images/video.png","title":"视频消息","interface_url":"https://sapp.17sysj.com/Sysj222/Message/videoMessage"},{"content":"抽中400飞磨豆","time":"1488764280","mark_num":"0","symbol":"sysm","cover":"http://apps.ifeimo.com/Public/Message/images/sys.png","title":"系统消息","interface_url":"https://sapp.17sysj.com/Sysj222/Message/sysMessage"},{"content":"123我操 等发布了新动态，快去看看吧~~","mark_num":"2","time":"1499406517","symbol":"group","interface_url":"https://sapp.17sysj.com/Sysj222/Message/groupMessage","cover":"http://apps.ifeimo.com/Public/Message/images/group.png","title":"圈子消息"}]
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * content : 暂无消息
         * time : 1499406515
         * mark_num : 0
         * symbol : training
         * cover : http://apps.ifeimo.com/Public/Message/images/training.png
         * title : 陪练订单消息
         * interface_url : https://sapp.17sysj.com/Sysj222/Message/trainingOrderMessage
         */

        private String content;
        private String time;
        private String mark_num;
        private String symbol;
        private String cover;
        private String title;
        private String interface_url;

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

        public String getMark_num() {
            return mark_num;
        }

        public void setMark_num(String mark_num) {
            this.mark_num = mark_num;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
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
