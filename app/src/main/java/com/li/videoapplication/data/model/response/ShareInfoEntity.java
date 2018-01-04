package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

/**
 * Created by cx on 2018/1/4.
 */

public class ShareInfoEntity extends BaseResponseEntity {

    private ADataBean AData;

    public ADataBean getaData() {

        return AData;
    }

    public void setaData(ADataBean aData) {
        this.AData = aData;
    }

    public static class ADataBean{
        private String share_title;
        private String share_desc;
        private String share_icon;

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        private String share_url;

        public String getShare_title() {
            return share_title;
        }

        public void setShare_title(String share_title) {
            this.share_title = share_title;
        }

        public String getShare_desc() {
            return share_desc;
        }

        public void setShare_desc(String share_desc) {
            this.share_desc = share_desc;
        }

        public String getShare_icon() {
            return share_icon;
        }

        public void setShare_icon(String share_icon) {
            this.share_icon = share_icon;
        }
    }
}
