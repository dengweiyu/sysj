package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 * 魔币充值
 */

public class RechargeCoinEntity extends BaseResponseEntity{

    /**
     * option : [20,50,100,200,500,1000,5000,10000]
     * exchangeRate : 10
     */

    private int exchangeRate;
    private List<Integer> option;

    public int getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(int exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public List<Integer> getOption() {
        return option;
    }

    public void setOption(List<Integer> option) {
        this.option = option;
    }
}
