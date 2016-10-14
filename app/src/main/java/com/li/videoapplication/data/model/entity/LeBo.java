package com.li.videoapplication.data.model.entity;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 实体类：乐播下载接口
 */
@SuppressWarnings("serial")
public class LeBo extends BaseEntity {

    private String id;
    private String package_name;
    private String version_code;
    private String url;
    private String type;
    private String package_icon;
    private String activity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getVersion_code() {
        return version_code;
    }

    public void setVersion_code(String version_code) {
        this.version_code = version_code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPackage_icon() {
        return package_icon;
    }

    public void setPackage_icon(String package_icon) {
        this.package_icon = package_icon;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}
