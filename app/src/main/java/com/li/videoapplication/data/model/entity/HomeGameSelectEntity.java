package com.li.videoapplication.data.model.entity;

import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**选择首页分栏的游戏
 * Created by y on 2017/11/17.
 */

public class HomeGameSelectEntity extends BaseResponseEntity {

    /**
     * AData : {"my_game":[{"column_id":"20","name":"王者荣耀","ico_pic":"http://img.17sysj.com/photo_20171110173245743.jpg","type":"1"},{"column_id":"3","name":"穿越火线","ico_pic":"http://img.17sysj.com/photo_20171110174554550.jpg","type":"1"},{"column_id":"4","name":"我的世界","ico_pic":"http://img.17sysj.com/photo_20171110174629455.jpg","type":"1"},{"column_id":"15","name":"球球大作战","ico_pic":"http://img.17sysj.com/photo_20171110174701151.jpg","type":"1"}],"hot_game":[{"column_id":"9","name":"崩坏3","ico_pic":"http://img.17sysj.com/photo_20171110174738937.jpg","type":"2"}]}
     * OData : null
     */

    private ADataBean AData;

    public ADataBean getAData() {
        return AData;
    }

    public void setAData(ADataBean AData) {
        this.AData = AData;
    }

    public static class ADataBean {
        private List<MyGameBean> my_game;
        private List<HotGameBean> hot_game;

        public List<MyGameBean> getMy_game() {
            return my_game;
        }

        public void setMy_game(List<MyGameBean> my_game) {
            this.my_game = my_game;
        }

        public List<HotGameBean> getHot_game() {
            return hot_game;
        }

        public void setHot_game(List<HotGameBean> hot_game) {
            this.hot_game = hot_game;
        }

        public static class MyGameBean {
            /**
             * column_id : 20
             * name : 王者荣耀
             * ico_pic : http://img.17sysj.com/photo_20171110173245743.jpg
             * type : 1
             */

            private String column_id;
            private String name;
            private String ico_pic;
            private String type;

            public String getColumn_id() {
                return column_id;
            }

            public void setColumn_id(String column_id) {
                this.column_id = column_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getIco_pic() {
                return ico_pic;
            }

            public void setIco_pic(String ico_pic) {
                this.ico_pic = ico_pic;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }

        public static class HotGameBean {
            /**
             * column_id : 9
             * name : 崩坏3
             * ico_pic : http://img.17sysj.com/photo_20171110174738937.jpg
             * type : 2
             */

            private String column_id;
            private String name;
            private String ico_pic;
            private String type;

            public String getColumn_id() {
                return column_id;
            }

            public void setColumn_id(String column_id) {
                this.column_id = column_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getIco_pic() {
                return ico_pic;
            }

            public void setIco_pic(String ico_pic) {
                this.ico_pic = ico_pic;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
