package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponse2Entity;
import com.li.videoapplication.framework.BaseResponseEntity;


@SuppressWarnings("serial")
public class SrtUpload203Entity extends BaseResponseEntity {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data extends BaseResponse2Entity {

        private String video_id;
        private String time;
        private String file;
        private String srt_id;

        public String getVideo_id() {
            return video_id;
        }

        public void setVideo_id(String video_id) {
            this.video_id = video_id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getSrt_id() {
            return srt_id;
        }

        public void setSrt_id(String srt_id) {
            this.srt_id = srt_id;
        }
    }
}
