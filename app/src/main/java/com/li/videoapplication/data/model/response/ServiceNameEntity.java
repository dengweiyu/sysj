package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponse2Entity;


@SuppressWarnings("serial")
public class ServiceNameEntity extends BaseResponse2Entity {

    private String name;
    private String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
