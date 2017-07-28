package com.li.videoapplication.data.model.entity;

/**
 *透传消息
 */

public class BaiduMessageEntity {

    /**
     * title : 订单消息通知
     * description : 哇哈哈er1qwe23邀请您进行陪练，赶紧来查看吧...
     * open_type : 0
     * pkg_content : null
     * custom_content : {"type":"training","status":5}
     */

    private String title;
    private String description;
    private int open_type;
    private Object pkg_content;
    private CustomContentBean custom_content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOpen_type() {
        return open_type;
    }

    public void setOpen_type(int open_type) {
        this.open_type = open_type;
    }

    public Object getPkg_content() {
        return pkg_content;
    }

    public void setPkg_content(Object pkg_content) {
        this.pkg_content = pkg_content;
    }

    public CustomContentBean getCustom_content() {
        return custom_content;
    }

    public void setCustom_content(CustomContentBean custom_content) {
        this.custom_content = custom_content;
    }

    public static class CustomContentBean {
        /**
         * type : training
         * status : 5
         */

        private String type;
        private int status;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
