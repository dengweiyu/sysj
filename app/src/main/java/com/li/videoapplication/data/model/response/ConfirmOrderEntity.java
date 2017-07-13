package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

/**
 * 订单确认结果
 */

public class ConfirmOrderEntity extends BaseResponseEntity {
    private int residue_coin;

    public int getResidue_coin() {
        return residue_coin;
    }

    public void setResidue_coin(int residue_coin) {
        this.residue_coin = residue_coin;
    }
}
