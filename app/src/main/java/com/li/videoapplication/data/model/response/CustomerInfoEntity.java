package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

/**
 * 客服信息
 */

public class CustomerInfoEntity extends BaseResponseEntity {

    /**
     * code : 10000
     * data : {"member_id":"584221","member_name":"手游视界赛事客服","icon":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/5785f65f177f1.jpg"}
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
         * member_id : 584221
         * member_name : 手游视界赛事客服
         * icon : http://apps.ifeimo.com/Public/Uploads/Member/Avatar/5785f65f177f1.jpg
         */

        private String member_id;
        private String member_name;
        private String icon;

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public String getMember_name() {
            return member_name;
        }

        public void setMember_name(String member_name) {
            this.member_name = member_name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
