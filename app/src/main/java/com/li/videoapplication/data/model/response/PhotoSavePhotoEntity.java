package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;


@SuppressWarnings("serial")
public class PhotoSavePhotoEntity extends BaseResponseEntity {

    public String pic_id;

    public String getPic_id() {
        return pic_id;
    }

    public void setPic_id(String pic_id) {
        this.pic_id = pic_id;
    }
}
