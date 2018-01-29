package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 * Created by cx on 2018/1/25.
 */

public class NewCurrencyMallRecommendedLocationEntity extends BaseResponseEntity {
    private List<Goods> goods;
    private String detailId;
    private String member_currency;

    public void setGoods(List<Goods> goods) { this.goods = goods; }
    public List<Goods> getGoods() { return goods; }

    public void setDetailId(String detailId) { this.detailId = detailId; }
    public String getDetailId() { return detailId; }

    public void setMember_currency(String member_currency) { this.member_currency = member_currency; }
    public String getMember_currency() { return member_currency; }

    public class Goods{
        private String id;
        private String cover;
        private String currency_num;
        private String content;
        private String name;

        public void setId(String id) { this.id = id; }
        public String getId() { return id; }

        public void setCover(String cover) { this.cover = cover; }
        public String getCover() { return cover; }

        public void setCurrency_num(String currency_num) { this.currency_num = currency_num; }
        public String getCurrency_num() { return currency_num; }

        public void setContent(String content) { this.content = content; }
        public String getContent() { return content; }

        public void setName(String name) { this.name = name; }
        public String getName() { return name; }
    }
}
