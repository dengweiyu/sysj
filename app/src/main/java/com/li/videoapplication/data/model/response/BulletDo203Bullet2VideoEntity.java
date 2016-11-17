package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponse2Entity;
import com.li.videoapplication.framework.BaseResponseEntity;


@SuppressWarnings("serial")
public class BulletDo203Bullet2VideoEntity extends BaseResponseEntity {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data extends BaseResponse2Entity {

        private String video_comment_id;

        public String getVideo_comment_id() {
            return video_comment_id;
        }

        public void setVideo_comment_id(String video_comment_id) {
            this.video_comment_id = video_comment_id;
        }
    }}
