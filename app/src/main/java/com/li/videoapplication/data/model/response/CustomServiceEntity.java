package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

/**
 * Created by y on 2018/3/7.
 */

public class CustomServiceEntity extends BaseResponseEntity {


    public static class DataBean extends BaseResponseEntity {
        private String qq;

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }
    }
}
