package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.Gift;
import com.li.videoapplication.framework.BaseResponse2Entity;

import java.util.List;

/**
 * 接口实体类：礼包列表
 */
@SuppressWarnings("serial")
public class MyPackageEntity extends BaseResponse2Entity {

    private List<Gift> list;

    public List<Gift> getList() {
        return list;
    }

    public void setList(List<Gift> list) {
        this.list = list;
    }

}
