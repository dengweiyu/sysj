package com.li.videoapplication.data.model.entity;

import com.li.videoapplication.framework.BaseResponse2Entity;

import java.util.List;

/**
 * 举报
 */
@SuppressWarnings("serial")
public class ReportType extends BaseResponse2Entity {

    private String type_id;
    private String name;

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
