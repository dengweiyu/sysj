package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 * 土豪榜/人气榜
 */

public class SendRewardRankEntity extends BaseResponseEntity {

    /**
     * AData : {"position":9,"page_count":2,"include":[{"member_id":"4065449","nickname":"大笨熊","avatar":"qq_1500019596806.jpg","currency":"0","coin":"100.00","member_tick":0},{"member_id":"3794103","nickname":"四三之灵","avatar":"default13.JPG","currency":"0","coin":"100.00","member_tick":0},{"member_id":"3782259","nickname":"咦？","avatar":"qq_1498642726769.jpg","currency":"0","coin":"100.00","member_tick":0},{"member_id":"3782221","nickname":"跳刀敌法","avatar":"5953797492369.jpg","currency":"0","coin":"100.00","member_tick":0},{"member_id":"1083272","nickname":"小G哈","avatar":"default4.JPG","currency":"332","coin":"99.50","member_tick":0},{"member_id":"3782360","nickname":"keke?","avatar":"5978359e2d90d.jpg","currency":"166","coin":"99.50","member_tick":0},{"member_id":"3753917","nickname":"没有丨昵称","avatar":"qq_1498461865398.jpg","currency":"0","coin":"99.50","member_tick":0},{"member_id":"3751576","nickname":"玩家5819","avatar":"5959ae9c87890.jpg","currency":"0","coin":"99.50","member_tick":0},{"member_id":"969106","nickname":"隔壁小王38","avatar":"qq_1473229998368.jpg","currency":"19422","coin":"77.00","member_tick":1},{"member_id":"1665906","nickname":"纳尼哈哈","avatar":"default2.JPG","currency":"498","coin":"94.50","member_tick":0},{"member_id":"4065363","nickname":"蚊子","avatar":"qq_1500019294967.jpg","currency":"0","coin":"93.80","member_tick":0},{"member_id":"47","nickname":"啊沫","avatar":"pc_1459134919144.jpg","currency":"5810","coin":"62.00","member_tick":0},{"member_id":"54","nickname":"胖哥","avatar":"568cc7b4cfe62.jpg","currency":"5478","coin":"80.50","member_tick":0},{"member_id":"3188694","nickname":"楠木菌","avatar":"596468bf9d61c.jpg","currency":"166","coin":"45.50","member_tick":0},{"member_id":"3794516","nickname":"玩家3794516","avatar":"default19.JPG","currency":"24402","coin":"11.50","member_tick":0},{"member_id":"3861185","nickname":"luwei1","avatar":"qq_1499049416950.jpg","currency":"332","coin":"33.00","member_tick":0},{"member_id":"2265320","nickname":"林安浅","avatar":"58b6656f4a526.jpg","currency":"25398","coin":"0.00","member_tick":0},{"member_id":"2229297","nickname":"NicoNico","avatar":"pc_1487576996405.jpg","currency":"166","coin":"23.50","member_tick":0},{"member_id":"1286334","nickname":"斗比熊GeGe","avatar":"5954f9cf6071d.jpg","currency":"2324","coin":"20.50","member_tick":0},{"member_id":"1011560","nickname":"小白^O^","avatar":"598ace7c625c0.jpg","currency":"0","coin":"21.50","member_tick":0}]}
     * OData : null
     */

    private ADataBean AData;
    private Object OData;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ADataBean getAData() {
        return AData;
    }

    public void setAData(ADataBean AData) {
        this.AData = AData;
    }

    public Object getOData() {
        return OData;
    }

    public void setOData(Object OData) {
        this.OData = OData;
    }

    public static class ADataBean {
        /**
         * position : 9
         * page_count : 2
         * include : [{"member_id":"4065449","nickname":"大笨熊","avatar":"qq_1500019596806.jpg","currency":"0","coin":"100.00","member_tick":0},{"member_id":"3794103","nickname":"四三之灵","avatar":"default13.JPG","currency":"0","coin":"100.00","member_tick":0},{"member_id":"3782259","nickname":"咦？","avatar":"qq_1498642726769.jpg","currency":"0","coin":"100.00","member_tick":0},{"member_id":"3782221","nickname":"跳刀敌法","avatar":"5953797492369.jpg","currency":"0","coin":"100.00","member_tick":0},{"member_id":"1083272","nickname":"小G哈","avatar":"default4.JPG","currency":"332","coin":"99.50","member_tick":0},{"member_id":"3782360","nickname":"keke?","avatar":"5978359e2d90d.jpg","currency":"166","coin":"99.50","member_tick":0},{"member_id":"3753917","nickname":"没有丨昵称","avatar":"qq_1498461865398.jpg","currency":"0","coin":"99.50","member_tick":0},{"member_id":"3751576","nickname":"玩家5819","avatar":"5959ae9c87890.jpg","currency":"0","coin":"99.50","member_tick":0},{"member_id":"969106","nickname":"隔壁小王38","avatar":"qq_1473229998368.jpg","currency":"19422","coin":"77.00","member_tick":1},{"member_id":"1665906","nickname":"纳尼哈哈","avatar":"default2.JPG","currency":"498","coin":"94.50","member_tick":0},{"member_id":"4065363","nickname":"蚊子","avatar":"qq_1500019294967.jpg","currency":"0","coin":"93.80","member_tick":0},{"member_id":"47","nickname":"啊沫","avatar":"pc_1459134919144.jpg","currency":"5810","coin":"62.00","member_tick":0},{"member_id":"54","nickname":"胖哥","avatar":"568cc7b4cfe62.jpg","currency":"5478","coin":"80.50","member_tick":0},{"member_id":"3188694","nickname":"楠木菌","avatar":"596468bf9d61c.jpg","currency":"166","coin":"45.50","member_tick":0},{"member_id":"3794516","nickname":"玩家3794516","avatar":"default19.JPG","currency":"24402","coin":"11.50","member_tick":0},{"member_id":"3861185","nickname":"luwei1","avatar":"qq_1499049416950.jpg","currency":"332","coin":"33.00","member_tick":0},{"member_id":"2265320","nickname":"林安浅","avatar":"58b6656f4a526.jpg","currency":"25398","coin":"0.00","member_tick":0},{"member_id":"2229297","nickname":"NicoNico","avatar":"pc_1487576996405.jpg","currency":"166","coin":"23.50","member_tick":0},{"member_id":"1286334","nickname":"斗比熊GeGe","avatar":"5954f9cf6071d.jpg","currency":"2324","coin":"20.50","member_tick":0},{"member_id":"1011560","nickname":"小白^O^","avatar":"598ace7c625c0.jpg","currency":"0","coin":"21.50","member_tick":0}]
         */

        private int position;
        private int page_count;
        private List<IncludeBean> include;

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public int getPage_count() {
            return page_count;
        }

        public void setPage_count(int page_count) {
            this.page_count = page_count;
        }

        public List<IncludeBean> getInclude() {
            return include;
        }

        public void setInclude(List<IncludeBean> include) {
            this.include = include;
        }

        public static class IncludeBean {
            /**
             * member_id : 4065449
             * nickname : 大笨熊
             * avatar : qq_1500019596806.jpg
             * currency : 0
             * coin : 100.00
             * member_tick : 0
             */

            private String member_id;
            private String nickname;
            private String avatar;
            private String currency;
            private String coin;
            private int member_tick;

            public String getMember_id() {
                return member_id;
            }

            public void setMember_id(String member_id) {
                this.member_id = member_id;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public String getCoin() {
                return coin;
            }

            public void setCoin(String coin) {
                this.coin = coin;
            }

            public int getMember_tick() {
                return member_tick;
            }

            public void setMember_tick(int member_tick) {
                this.member_tick = member_tick;
            }
        }
    }
}
