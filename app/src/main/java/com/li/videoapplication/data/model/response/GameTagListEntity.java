package com.li.videoapplication.data.model.response;


import com.li.videoapplication.data.model.entity.Tag;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;


@SuppressWarnings("serial")
public class GameTagListEntity extends BaseResponseEntity {

    private List<Tag> data;

    public List<Tag> getData() {
        return data;
    }

    public void setData(List<Tag> data) {
        this.data = data;
    }
}
