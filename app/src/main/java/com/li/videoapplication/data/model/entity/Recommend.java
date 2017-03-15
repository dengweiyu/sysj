package com.li.videoapplication.data.model.entity;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 实体类：推荐位
 */
public class Recommend extends BaseEntity {

    private String id;
    private String name;
    private String currency_num;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency_num() {
        return currency_num;
    }

    public void setCurrency_num(String currency_num) {
        this.currency_num = currency_num;
    }
}
