package com.li.videoapplication.data.model.response;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.google.gson.annotations.SerializedName;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 * 陪玩 下单列表
 */

public class PlayWithPlaceOrderEntity extends BaseResponseEntity {


    /**
     * code : 10000
     * page_count : 1
     * data : [{"title":"2017-06-22","list":[{"order_id":"1","status":"3","add_time":"1498103959","score":"5.0","avatar":"https://sapp.17sysj.com/Public/Uploads/Member/Avatar/568cc7b4cfe62.jpg","nickname":"胖哥","gameMode":"匹配","statusText":"陪练开始"},{"order_id":"2","status":"3","add_time":"1498103959","score":"5.0","avatar":"https://sapp.17sysj.com/Public/Uploads/Member/Avatar/568cc7b4cfe62.jpg","nickname":"胖哥","gameMode":"匹配","statusText":"陪练开始"}]},{"title":"2017-06-21","list":[{"order_id":"3","status":"4","add_time":"1498037219","score":"5.0","avatar":"https://sapp.17sysj.com/Public/Uploads/Member/Avatar/568cc7b4cfe62.jpg","nickname":"胖哥","gameMode":"匹配","statusText":"完成陪练"}]}]
     */

    private int page_count;
    private List<DataBean> data;

    public int getPage_count() {
        return page_count;
    }

    public void setPage_count(int page_count) {
        this.page_count = page_count;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    //封装分组数据实体
    public static class SectionData extends SectionEntity<DataBean.ListBean>{
        public SectionData(boolean isHeader, String header) {
            super(isHeader, header);
        }

        public SectionData(DataBean.ListBean listBean) {
            super(listBean);
        }
    }


    public static class DataBean {
        /**
         * title : 2017-06-22
         * list : [{"order_id":"1","status":"3","add_time":"1498103959","score":"5.0","avatar":"https://sapp.17sysj.com/Public/Uploads/Member/Avatar/568cc7b4cfe62.jpg","nickname":"胖哥","gameMode":"匹配","statusText":"陪练开始"},{"order_id":"2","status":"3","add_time":"1498103959","score":"5.0","avatar":"https://sapp.17sysj.com/Public/Uploads/Member/Avatar/568cc7b4cfe62.jpg","nickname":"胖哥","gameMode":"匹配","statusText":"陪练开始"}]
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
             * order_id : 1
             * status : 3
             * add_time : 1498103959
             * score : 5.0
             * avatar : https://sapp.17sysj.com/Public/Uploads/Member/Avatar/568cc7b4cfe62.jpg
             * nickname : 胖哥
             * gameMode : 匹配
             * statusText : 陪练开始
             */

            private String order_id;
            @SerializedName("status")
            private String statusX;
            private String add_time;
            private String score;
            private String avatar;
            private String nickname;
            private String gameMode;
            private String statusText;

            public String getOrder_id() {
                return order_id;
            }

            public void setOrder_id(String order_id) {
                this.order_id = order_id;
            }

            public String getStatusX() {
                return statusX;
            }

            public void setStatusX(String statusX) {
                this.statusX = statusX;
            }

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }

            public String getScore() {
                return score;
            }

            public void setScore(String score) {
                this.score = score;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getGameMode() {
                return gameMode;
            }

            public void setGameMode(String gameMode) {
                this.gameMode = gameMode;
            }

            public String getStatusText() {
                return statusText;
            }

            public void setStatusText(String statusText) {
                this.statusText = statusText;
            }
        }
    }
}
