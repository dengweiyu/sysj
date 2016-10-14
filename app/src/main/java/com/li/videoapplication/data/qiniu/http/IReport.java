package com.li.videoapplication.data.qiniu.http;

import org.apache.http.Header;

public interface IReport {
    Header[] appendStatHeaders(Header[] headers);

    void updateErrorInfo(ResponseInfo info);

    void updateSpeedInfo(ResponseInfo info);
}
