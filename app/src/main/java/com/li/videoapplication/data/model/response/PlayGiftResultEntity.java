package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

/**
 * 礼物打赏结果
 */

public class PlayGiftResultEntity extends BaseResponseEntity {
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean{
        private String coin;
        private String currency;
        private String rewardedPrice;

        public String getRewardedPrice() {
            return rewardedPrice;
        }

        public void setRewardedPrice(String rewardedPrice) {
            this.rewardedPrice = rewardedPrice;
        }

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }
}
