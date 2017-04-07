package com.li.videoapplication.data.model.response;

import com.google.gson.annotations.SerializedName;
import com.li.videoapplication.framework.BaseResponseEntity;

/**
 * Created by pengzhipeng on 2016/10/28.
 */

public class PaymentEntity extends BaseResponseEntity {


    /**
     * data : {"appId":"1103189341","bargainorId":"1452072601","tokenId":"6V97c92d72e5ad035bc29b0dc2f2f241","nonce":"228d063b071a19a97e0ef2f01e6cad6a","pubAcc":"","sign":"scNEl7zijoKFBRVh3wWpbhSFFFI=","timestamp":1491481223,"alipay":"","package":""}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * appId : 1103189341
         * bargainorId : 1452072601
         * tokenId : 6V97c92d72e5ad035bc29b0dc2f2f241
         * nonce : 228d063b071a19a97e0ef2f01e6cad6a
         * pubAcc :
         * sign : scNEl7zijoKFBRVh3wWpbhSFFFI=
         * timestamp : 1491481223
         * alipay :
         * package :
         */

        private String appId;
        private String bargainorId;
        private String tokenId;
        private String nonce;
        private String pubAcc;
        private String sign;
        private long timestamp;
        private String alipay;
        @SerializedName("package")
        private String packageX;

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getBargainorId() {
            return bargainorId;
        }

        public void setBargainorId(String bargainorId) {
            this.bargainorId = bargainorId;
        }

        public String getTokenId() {
            return tokenId;
        }

        public void setTokenId(String tokenId) {
            this.tokenId = tokenId;
        }

        public String getNonce() {
            return nonce;
        }

        public void setNonce(String nonce) {
            this.nonce = nonce;
        }

        public String getPubAcc() {
            return pubAcc;
        }

        public void setPubAcc(String pubAcc) {
            this.pubAcc = pubAcc;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public String getAlipay() {
            return alipay;
        }

        public void setAlipay(String alipay) {
            this.alipay = alipay;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }
    }
}
