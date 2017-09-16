package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.Recommend;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;


public class RecommendedLocationEntity extends BaseResponseEntity {

    private List<Recommend> goods;
    private String member_currency;
    private String detailId;

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public List<Recommend> getGoods() {
        return goods;
    }

    public void setGoods(List<Recommend> goods) {
        this.goods = goods;
    }

    public String getMember_currency() {
        return member_currency;
    }

    public void setMember_currency(String member_currency) {
        this.member_currency = member_currency;
    }
}
