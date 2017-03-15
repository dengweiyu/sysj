package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.Recommend;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

public class RecommendCateEntity extends BaseResponseEntity {

    private List<Recommend> data;

    public List<Recommend> getData() {
        return data;
    }

    public void setData(List<Recommend> data) {
        this.data = data;
    }
}
