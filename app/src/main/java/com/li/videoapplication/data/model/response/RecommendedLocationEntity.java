package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseEntity;
import com.li.videoapplication.framework.BaseResponseEntity;


public class RecommendedLocationEntity extends BaseResponseEntity {

    private Goods goods;
    private String member_currency;

    public String getMember_currency() {
        return member_currency;
    }

    public void setMember_currency(String member_currency) {
        this.member_currency = member_currency;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public class Goods extends BaseEntity {

        private String id;
        private String currency_num;
        private String remark;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCurrency_num() {
            return currency_num;
        }

        public void setCurrency_num(String currency_num) {
            this.currency_num = currency_num;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
