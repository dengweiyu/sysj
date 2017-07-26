package com.li.videoapplication.interfaces;

import android.widget.TextView;

/**
 * 陪玩订单策略接口
 */

public interface IOrderStrategy {
    //左边按钮的显示
    void renderLeftButton(TextView left);
    //右边按钮的显示
    void renderRightButton(TextView right);
    //左边按钮被点击
    void onClickLeftButton();
    //右边按钮被点击
    void onClickRightButton();

}
