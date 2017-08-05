package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 *
 */

public class UserVipInfoEntity extends BaseResponseEntity {

    /**
     * data : {"member_id":"1722092","level":"3","end_time":"1514860770","valid":"1","details":[{"level":"3","end_time":"1503041771"},{"level":"2","end_time":"1514860770"},{"level":"1","end_time":"1507084770"}]}
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
         * member_id : 1722092
         * level : 3
         * end_time : 1514860770
         * valid : 1
         * details : [{"level":"3","end_time":"1503041771"},{"level":"2","end_time":"1514860770"},{"level":"1","end_time":"1507084770"}]
         */

        private String member_id;
        private String level;
        private String end_time;
        private String valid;
        private List<DetailsBean> details;

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getValid() {
            return valid;
        }

        public void setValid(String valid) {
            this.valid = valid;
        }

        public List<DetailsBean> getDetails() {
            return details;
        }

        public void setDetails(List<DetailsBean> details) {
            this.details = details;
        }

        public static class DetailsBean {
            /**
             * level : 3
             * end_time : 1503041771
             */

            private String level;
            private String end_time;

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getEnd_time() {
                return end_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }
        }
    }
}
