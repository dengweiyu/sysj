package com.li.videoapplication.data.model.response;

import com.google.gson.annotations.SerializedName;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 * 礼物类型列表
 */

public class PlayGiftTypeEntity extends BaseResponseEntity {


    /**
     * data : [{"gift_name":"666","gift_icon":"http://img.17sysj.com/photo_20170606180648887.jpg","left_gift_icon":"http://img.17sysj.com/photo_20170607144103348.jpg","purchase_method":"1","currency_num":"166","coin_num":"0","gift_id":"3"},{"gift_name":"棒棒糖","gift_icon":"http://img.17sysj.com/photo_20170607144201466.jpg","left_gift_icon":"http://img.17sysj.com/photo_20170607144205790.jpg","purchase_method":"2","currency_num":"0","coin_num":"5","gift_id":"4"},{"gift_name":"应援棒","gift_icon":"http://img.17sysj.com/photo_20170607144253248.jpg","left_gift_icon":"http://img.17sysj.com/photo_20170607144255246.jpg","purchase_method":"2","currency_num":"0","coin_num":"10","gift_id":"5"},{"gift_name":"辣条","gift_icon":"http://img.17sysj.com/photo_20170607144328657.jpg","left_gift_icon":"http://img.17sysj.com/photo_201706071443301057.jpg","purchase_method":"2","currency_num":"0","coin_num":"50","gift_id":"6"},{"gift_name":"猫头鹰","gift_icon":"http://img.17sysj.com/photo_20170607144402337.jpg","left_gift_icon":"http://img.17sysj.com/photo_20170607144405711.jpg","purchase_method":"2","currency_num":"0","coin_num":"100","gift_id":"7"},{"gift_name":"大宝剑","gift_icon":"http://img.17sysj.com/photo_20170607144428440.jpg","left_gift_icon":"http://img.17sysj.com/photo_20170607144432445.jpg","purchase_method":"2","currency_num":"0","coin_num":"190","gift_id":"8"},{"gift_name":"游艇","gift_icon":"http://img.17sysj.com/photo_20170607145304421.jpg","left_gift_icon":"http://img.17sysj.com/photo_20170607145307484.jpg","purchase_method":"2","currency_num":"0","coin_num":"680","gift_id":"9"},{"gift_name":"火箭","gift_icon":"http://img.17sysj.com/photo_20170607145350489.jpg","left_gift_icon":"http://img.17sysj.com/photo_20170607145353175.jpg","purchase_method":"2","currency_num":"0","coin_num":"3000","gift_id":"2"}]
     * code : 00000
     * numberSense : [{"number":"1","title":"一见钟情"},{"number":"3","title":"233"},{"number":"9","title":"就是爱你"},{"number":"18","title":"要抱~"},{"number":"66","title":"好66哦"}]
     */

    private List<DataBean> data;
    private List<NumberSenseBean> numberSense;


    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public List<NumberSenseBean> getNumberSense() {
        return numberSense;
    }

    public void setNumberSense(List<NumberSenseBean> numberSense) {
        this.numberSense = numberSense;
    }

    public static class DataBean {
        /**
         * gift_name : 666
         * gift_icon : http://img.17sysj.com/photo_20170606180648887.jpg
         * left_gift_icon : http://img.17sysj.com/photo_20170607144103348.jpg
         * purchase_method : 1
         * currency_num : 166
         * coin_num : 0
         * gift_id : 3
         */

        private String gift_name;
        private String gift_icon;
        private String left_gift_icon;
        private String purchase_method;
        private String currency_num;
        private String coin_num;
        private String gift_id;

        public String getGift_name() {
            return gift_name;
        }

        public void setGift_name(String gift_name) {
            this.gift_name = gift_name;
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

        public String getPurchase_method() {
            return purchase_method;
        }

        public void setPurchase_method(String purchase_method) {
            this.purchase_method = purchase_method;
        }

        public String getCurrency_num() {
            return currency_num;
        }

        public void setCurrency_num(String currency_num) {
            this.currency_num = currency_num;
        }

        public String getCoin_num() {
            return coin_num;
        }

        public void setCoin_num(String coin_num) {
            this.coin_num = coin_num;
        }

        public String getGift_id() {
            return gift_id;
        }

        public void setGift_id(String gift_id) {
            this.gift_id = gift_id;
        }
    }

    public static class NumberSenseBean {
        /**
         * number : 1
         * title : 一见钟情
         */

        private String number;
        private String title;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
