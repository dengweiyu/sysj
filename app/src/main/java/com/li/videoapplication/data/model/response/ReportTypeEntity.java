package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.ReportType;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;


@SuppressWarnings("serial")
public class ReportTypeEntity extends BaseResponseEntity {

    private List<ReportType> data;

    public List<ReportType> getData() {
        return data;
    }

    public void setData(List<ReportType> data) {
        this.data = data;
    }
}
