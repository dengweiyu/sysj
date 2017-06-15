package com.li.videoapplication.data.model.response;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 * 礼物
 */

public class MyGiftBillEntity extends BaseResponseEntity {


    /**
     * data : [{"title":"今天","list":[{"gift_icon":"http://img.17sysj.com/photo_20170606180648887.jpg","number":"1","gift_name":"666","nickname":"13710318457","video_name":"《阴阳师》得到sssr","member_id":"2713296","video_id":"2868093","reward_price":"166飞魔豆","time":"17:09","type":"currency","tab":"send"}]},{"title":"2017-06-07","list":[{"gift_icon":"http://img.17sysj.com/photo_20170607145350489.jpg","number":"2","gift_name":"火箭","nickname":"隔壁老王","video_name":"《阴阳师》得到sssr","member_id":"969106","video_id":"2868093","reward_price":"6000飞魔币","time":"17:11","type":"coin","tab":"received"},{"gift_icon":"http://img.17sysj.com/photo_20170607145350489.jpg","number":"2","gift_name":"火箭","nickname":"13710318457","video_name":"《阴阳师》得到sssr","member_id":"2713296","video_id":"2868093","reward_price":"6000飞魔币","time":"14:59","type":"coin","tab":"send"}]}]
     * title : TA的礼物
     */

    private String title;
    private List<DataBean> data;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    //封装分组数据的实体类
    public static class SectionBill extends SectionEntity<DataBean.ListBean>{
        public SectionBill(boolean isHeader, String header) {
            super(isHeader, header);
        }

        public SectionBill(DataBean.ListBean listBean) {
            super(listBean);
        }
    }


    public static class DataBean {
        /**
         * title : 今天
         * list : [{"gift_icon":"http://img.17sysj.com/photo_20170606180648887.jpg","number":"1","gift_name":"666","nickname":"13710318457","video_name":"《阴阳师》得到sssr","member_id":"2713296","video_id":"2868093","reward_price":"166飞魔豆","time":"17:09","type":"currency","tab":"send"}]
         */

        private String title;
        private List<ListBean> list;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * gift_icon : http://img.17sysj.com/photo_20170606180648887.jpg
             * number : 1
             * gift_name : 666
             * nickname : 13710318457
             * video_name : 《阴阳师》得到sssr
             * member_id : 2713296
             * video_id : 2868093
             * reward_price : 166飞魔豆
             * time : 17:09
             * type : currency
             * tab : send
             */

            private String gift_icon;
            private String number;
            private String gift_name;
            private String nickname;
            private String video_name;
            private String member_id;
            private String video_id;
            private String reward_price;
            private String time;
            private String type;
            private String tab;

            public String getGift_icon() {
                return gift_icon;
            }

            public void setGift_icon(String gift_icon) {
                this.gift_icon = gift_icon;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public String getGift_name() {
                return gift_name;
            }

            public void setGift_name(String gift_name) {
                this.gift_name = gift_name;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getVideo_name() {
                return video_name;
            }

            public void setVideo_name(String video_name) {
                this.video_name = video_name;
            }

            public String getMember_id() {
                return member_id;
            }

            public void setMember_id(String member_id) {
                this.member_id = member_id;
            }

            public String getVideo_id() {
                return video_id;
            }

            public void setVideo_id(String video_id) {
                this.video_id = video_id;
            }

            public String getReward_price() {
                return reward_price;
            }

            public void setReward_price(String reward_price) {
                this.reward_price = reward_price;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getTab() {
                return tab;
            }

            public void setTab(String tab) {
                this.tab = tab;
            }
        }
    }
}
