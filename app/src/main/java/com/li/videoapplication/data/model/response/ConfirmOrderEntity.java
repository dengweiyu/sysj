package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

/**
 * 订单确认结果
 */

public class ConfirmOrderEntity extends BaseResponseEntity {

    private String order_id;

    private int residue_coin;

    public int getResidue_coin() {
        return residue_coin;
    }

    public void setResidue_coin(int residue_coin) {
        this.residue_coin = residue_coin;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
}
