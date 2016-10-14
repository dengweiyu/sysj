package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponse2Entity;
import com.li.videoapplication.framework.BaseResponseEntity;


@SuppressWarnings("serial")
public class SaveEventVideo204Entity extends BaseResponseEntity {

    public Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data extends BaseResponse2Entity {

        private String pk_id;
        private String video_id;

        public String getPk_id() {
            return pk_id;
        }

        public void setPk_id(String pk_id) {
            this.pk_id = pk_id;
        }

        public String getVideo_id() {
            return video_id;
        }

        public void setVideo_id(String video_id) {
            this.video_id = video_id;
        }
    }
}
