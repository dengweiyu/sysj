package com.li.videoapplication.data.model.response;

import java.util.List;

import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.framework.BaseResponseEntity;


@SuppressWarnings("serial")
public class VideoRankingEntity extends BaseResponseEntity {

    private List<VideoImage> list;

    public List<VideoImage> getList() {
        return list;
    }

    public void setList(List<VideoImage> list) {
        this.list = list;
    }
}
