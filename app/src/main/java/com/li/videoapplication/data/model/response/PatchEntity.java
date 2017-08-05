package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

/**
 *
 */

public class PatchEntity extends BaseResponseEntity {

    /**
     * code : 10000
     * data : {"channel_id":"FM","app_version":"","download_url":""}
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
         * channel_id : FM
         * app_version :
         * download_url :
         */

        private String channel_id;
        private String app_version;
        private String download_url;
        private String patch_version;

        public String getPatch_version() {
            return patch_version;
        }

        public void setPatch_version(String patch_version) {
            this.patch_version = patch_version;
        }

        public String getChannel_id() {
            return channel_id;
        }

        public void setChannel_id(String channel_id) {
            this.channel_id = channel_id;
        }

        public String getApp_version() {
            return app_version;
        }

        public void setApp_version(String app_version) {
            this.app_version = app_version;
        }

        public String getDownload_url() {
            return download_url;
        }

        public void setDownload_url(String download_url) {
            this.download_url = download_url;
        }
    }
}
