package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 * 打赏榜
 */

public class VideoPlayGiftEntity extends BaseResponseEntity {

    /**
     * data : {"coin":12024,"currency":166,"includes":[{"avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/default21.JPG","name":"13710318457","currency_sum":"166","coin_sum":"6000","member_id":"2713296"},{"avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/qq_1473229998368.jpg","name":"隔壁老王","currency_sum":"0","coin_sum":"6000","member_id":"969106"},{"avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/qq_1488781498450.jpg","name":"゛枫 ҉凝҉ゞ","currency_sum":"0","coin_sum":"24","member_id":"2373726"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * coin : 12024
         * currency : 166
         * includes : [{"avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/default21.JPG","name":"13710318457","currency_sum":"166","coin_sum":"6000","member_id":"2713296"},{"avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/qq_1473229998368.jpg","name":"隔壁老王","currency_sum":"0","coin_sum":"6000","member_id":"969106"},{"avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/qq_1488781498450.jpg","name":"゛枫 ҉凝҉ゞ","currency_sum":"0","coin_sum":"24","member_id":"2373726"}]
         */

        private int coin;
        private int currency;
        private List<IncludesBean> includes;

        public int getCoin() {
            return coin;
        }

        public void setCoin(int coin) {
            this.coin = coin;
        }

        public int getCurrency() {
            return currency;
        }

        public void setCurrency(int currency) {
            this.currency = currency;
        }

        public List<IncludesBean> getIncludes() {
            return includes;
        }

        public void setIncludes(List<IncludesBean> includes) {
            this.includes = includes;
        }

        public static class IncludesBean {
            /**
             * avatar : http://apps.ifeimo.com/Public/Uploads/Member/Avatar/default21.JPG
             * name : 13710318457
             * currency_sum : 166
             * coin_sum : 6000
             * member_id : 2713296
             */

            private String avatar;
            private String name;
            private String currency_sum;
            private String coin_sum;
            private String member_id;

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCurrency_sum() {
                return currency_sum;
            }

            public void setCurrency_sum(String currency_sum) {
                this.currency_sum = currency_sum;
            }

            public String getCoin_sum() {
                return coin_sum;
            }

            public void setCoin_sum(String coin_sum) {
                this.coin_sum = coin_sum;
            }

            public String getMember_id() {
                return member_id;
            }

            public void setMember_id(String member_id) {
                this.member_id = member_id;
            }
        }
    }
}
