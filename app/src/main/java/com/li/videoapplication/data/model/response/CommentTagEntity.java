package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 * 评论标签
 */

public class CommentTagEntity extends BaseResponseEntity {

    /**
     * code : 10000
     * data : [{"id":"1","title":"大神水平高"},{"id":"2","title":"态度极好"},{"id":"3","title":"耐心指导"},{"id":"4","title":"水平一般"}]
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * title : 大神水平高
         */

        private String id;
        private String title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
