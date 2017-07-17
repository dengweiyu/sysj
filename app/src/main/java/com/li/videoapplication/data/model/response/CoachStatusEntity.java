package com.li.videoapplication.data.model.response;

import com.google.gson.annotations.SerializedName;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 * 教练状态
 */

public class CoachStatusEntity extends BaseResponseEntity {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * member_id : 3633792
         * status : 1
         */

        private String member_id;
        @SerializedName("status")
        private String statusX;

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public String getStatusX() {
            return statusX;
        }

        public void setStatusX(String statusX) {
            this.statusX = statusX;
        }
    }
}
