package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 * 时间轴礼物
 */

public class TimeLineGiftEntity  extends BaseResponseEntity {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * avatar : http://apps.ifeimo.com/Public/Uploads/Member/Avatar/qq_1488781498450.jpg
         * name : ゛枫 ҉凝҉ゞ
         * video_name : 《阴阳师》得到sssr
         * currency_sum : 0
         * coin_sum : 24
         * member_id : 2373726
         * video_id : 2868093
         * num : 2
         * created : 2017-06-05 19:21:06
         * gift_icon : http://img.17sysj.com/photo_20170606180648887.jpg
         * left_gift_icon : http://img.17sysj.com/photo_20170607144103348.jpg
         * gift_name : 666
         * purchase_method : 1
         * video_node : 0.1
         */
        //是否在时间轴显示过了
        private boolean isShowed;
        private String avatar;
        private String name;
        private String video_name;
        private String currency_sum;
        private String coin_sum;
        private String member_id;
        private String video_id;
        private String num;
        private String created;
        private String gift_icon;
        private String left_gift_icon;
        private String gift_name;
        private String purchase_method;
        private String video_node;

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

        public String getVideo_name() {
            return video_name;
        }

        public void setVideo_name(String video_name) {
            this.video_name = video_name;
        }

        public String getCurrency_sum() {
            return currency_sum;
        }

        public void setCurrency_sum(String currency_sum) {
            this.currency_sum = currency_sum;
        }

        public String getCoin_sum() {
            return coin_sum;
        }

        public void setCoin_sum(String coin_sum) {
            this.coin_sum = coin_sum;
        }

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public String getVideo_id() {
            return video_id;
        }

        public void setVideo_id(String video_id) {
            this.video_id = video_id;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getGift_icon() {
            return gift_icon;
        }

        public void setGift_icon(String gift_icon) {
            this.gift_icon = gift_icon;
        }

        public String getLeft_gift_icon() {
            return left_gift_icon;
        }

        public void setLeft_gift_icon(String left_gift_icon) {
            this.left_gift_icon = left_gift_icon;
        }

        public String getGift_name() {
            return gift_name;
        }

        public void setGift_name(String gift_name) {
            this.gift_name = gift_name;
        }

        public String getPurchase_method() {
            return purchase_method;
        }

        public void setPurchase_method(String purchase_method) {
            this.purchase_method = purchase_method;
        }

        public String getVideo_node() {
            return video_node;
        }

        public void setVideo_node(String video_node) {
            this.video_node = video_node;
        }

        public boolean isShowed() {
            return isShowed;
        }

        public void setShowed(boolean showed) {
            isShowed = showed;
        }
    }
}
