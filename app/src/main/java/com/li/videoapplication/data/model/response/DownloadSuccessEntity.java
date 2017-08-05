package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseEntity;

/**
 *
 */

public class DownloadSuccessEntity extends BaseEntity {
    private String url;

    public DownloadSuccessEntity(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
