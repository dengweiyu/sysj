package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 *
 */

public class UploadKeyEntity  extends BaseResponseEntity{

    /**
     * AData : ["coach_20170809165341135f","coach_201708091653411b1f","coach_201708091653417fd"]
     * OData : null
     * token : U2NOe1xrNtDMILvSWUzLNmBxhVhcQF2hSSTmaQmg:uZnQooGprbjhcYLRqX8VLW-2W0M=:eyJzY29wZSI6ImZtLXN5c2otcGljIiwiZGVhZGxpbmUiOjE1MDIzNTUyMjF9
     */

    private Object OData;
    private String token;
    private List<String> AData;

    public Object getOData() {
        return OData;
    }

    public void setOData(Object OData) {
        this.OData = OData;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<String> getAData() {
        return AData;
    }

    public void setAData(List<String> AData) {
        this.AData = AData;
    }
}
