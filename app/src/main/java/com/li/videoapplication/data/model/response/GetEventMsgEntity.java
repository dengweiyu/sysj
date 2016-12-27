package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponse2Entity;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;


public class GetEventMsgEntity extends BaseResponseEntity {

    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data extends BaseResponse2Entity {

        private String rewardTitle;
        private String rewardDes;
        private String rulesTitle;
        private String rulesDes;

        public String getRewardTitle() {
            return rewardTitle;
        }

        public void setRewardTitle(String rewardTitle) {
            this.rewardTitle = rewardTitle;
        }

        public String getRewardDes() {
            return rewardDes;
        }

        public void setRewardDes(String rewardDes) {
            this.rewardDes = rewardDes;
        }

        public String getRulesTitle() {
            return rulesTitle;
        }

        public void setRulesTitle(String rulesTitle) {
            this.rulesTitle = rulesTitle;
        }

        public String getRulesDes() {
            return rulesDes;
        }

        public void setRulesDes(String rulesDes) {
            this.rulesDes = rulesDes;
        }
    }
}
