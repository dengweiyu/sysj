package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.framework.BaseResponseEntity;

public class GetMatchInfo208Entity extends BaseResponseEntity {

    private Match data;

    public Match getData() {
        return data;
    }

    public void setData(Match data) {
        this.data = data;
    }
}
