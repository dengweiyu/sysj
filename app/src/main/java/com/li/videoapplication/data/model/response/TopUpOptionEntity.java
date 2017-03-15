package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;



public class TopUpOptionEntity extends BaseResponseEntity {

    private String[] option;
    private String exchangeRate;

    public String[] getOption() {
        return option;
    }

    public void setOption(String[] option) {
        this.option = option;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
