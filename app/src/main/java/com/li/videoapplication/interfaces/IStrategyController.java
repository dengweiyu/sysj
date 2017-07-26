package com.li.videoapplication.interfaces;

import com.li.videoapplication.data.model.response.PlayWithOrderDetailEntity;


/**
 * 策略生成器
 */

public interface IStrategyController {
   IOrderStrategy getStrategy(PlayWithOrderDetailEntity orderEntity, int role);
}
