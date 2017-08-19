package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 *
 */

public class CoachEditSignEntity extends BaseResponseEntity {

    /**
     * AData : []
     * OData : {"pic":[""],"description":"","imgUrl":"http://img.17sysj.com/"}
     */

    private ODataBean OData;
    private List<?> AData;

    public ODataBean getOData() {
        return OData;
    }

    public void setOData(ODataBean OData) {
        this.OData = OData;
    }

    public List<?> getAData() {
        return AData;
    }

    public void setAData(List<?> AData) {
        this.AData = AData;
    }

    public static class ODataBean {
        /**
         * pic : [""]
         * description :
         * imgUrl : http://img.17sysj.com/
         */

        private String individuality_signature;
        private String imgUrl;
        private List<String> pic;

        public String getIndividuality_signature() {
            return individuality_signature;
        }

        public void setIndividuality_signature(String individuality_signature) {
            this.individuality_signature = individuality_signature;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public List<String> getPic() {
            return pic;
        }

        public void setPic(List<String> pic) {
            this.pic = pic;
        }
    }
}
