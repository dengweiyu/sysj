package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

public class UnfinishedTaskEntity extends BaseResponseEntity {

    private int num;

    private int amount;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
