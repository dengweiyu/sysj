package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.LeBo;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

public class getLeboDownloadEntity extends BaseResponseEntity{

    private List<LeBo> list;

    public List<LeBo> getList() {
        return list;
    }

    public void setList(List<LeBo> list) {
        this.list = list;
    }
}
