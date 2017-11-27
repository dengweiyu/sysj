package com.li.videoapplication.data.model.entity;

import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**保存首页分栏项
 * Created by y on 2017/11/16.
 */

public class HomeColumnEntity extends BaseResponseEntity {

    /**
     * result : true
     * code : 10000
     * msg :
     * AData : [{"id":"1","name":"推荐"},{"id":"20","name":"王者荣耀"},{"id":"3","name":"穿越火线"},{"id":"4","name":"我的世界"},{"id":"15","name":"球球大作战"},{"id":"6","name":"其他游戏"}]
     * OData : null
     */

    private List<ADataBean> AData;

    public List<ADataBean> getAData() {
        return AData;
    }

    public void setAData(List<ADataBean> AData) {
        this.AData = AData;
    }

    public static class ADataBean {
        /**
         * id : 1
         * name : 推荐
         */

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
