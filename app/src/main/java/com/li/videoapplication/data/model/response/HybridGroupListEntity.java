package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;
import com.li.videoapplication.utils.StringUtil;

import java.util.List;

/**
 *
 */

public class HybridGroupListEntity extends BaseResponseEntity {

    /**
     * AData : ["1876"]
     * OData : null
     */

    private Object OData;
    private List<String> AData;

    public Object getOData() {
        return OData;
    }

    public void setOData(Object OData) {
        this.OData = OData;
    }

    public List<String> getAData() {
        return AData;
    }

    public void setAData(List<String> AData) {
        this.AData = AData;
    }


    public boolean isHybridPager(String groupId){
        if (StringUtil.isNull(groupId)){
            return false;
        }
        if (AData == null){

            return  false;
        }

        for (String id: AData) {
            if (groupId.equals(id)){
                return true;
            }
        }

        return false;
    }
}
