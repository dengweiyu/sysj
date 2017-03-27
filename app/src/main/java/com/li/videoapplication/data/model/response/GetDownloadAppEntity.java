package com.li.videoapplication.data.model.response;


import com.li.videoapplication.data.model.entity.Download;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

@SuppressWarnings("serial")
public class GetDownloadAppEntity extends BaseResponseEntity {

    private List<Download> data;

    public List<Download> getData() {
        return data;
    }

    public void setData(List<Download> data) {
        this.data = data;
    }
}
