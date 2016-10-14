package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponse2Entity;
import com.li.videoapplication.framework.BaseResponseEntity;


@SuppressWarnings("serial")
public class VideoDoVideoMark203Entity extends BaseResponseEntity {

    public Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data extends BaseResponse2Entity {

        private String video_id;

        private String videokey;

        private String qn_key;

        private String join_id;

        private String videouploadtoken;

        public String getVideo_id() {
            return video_id;
        }

        public void setVideo_id(String video_id) {
            this.video_id = video_id;
        }

        public String getVideokey() {
            return videokey;
        }

        public void setVideokey(String videokey) {
            this.videokey = videokey;
        }

        public String getQn_key() {
            return qn_key;
        }

        public void setQn_key(String qn_key) {
            this.qn_key = qn_key;
        }

        public String getJoin_id() {
            return join_id;
        }

        public void setJoin_id(String join_id) {
            this.join_id = join_id;
        }

        public String getVideouploadtoken() {
            return videouploadtoken;
        }

        public void setVideouploadtoken(String videouploadtoken) {
            this.videouploadtoken = videouploadtoken;
        }
    }
}
