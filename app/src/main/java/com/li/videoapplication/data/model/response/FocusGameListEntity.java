package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

import java.io.Serializable;
import java.util.List;

/**
 *
 */

public class FocusGameListEntity extends BaseResponseEntity {

    /**
     * AData : [{"group_id":"109","game_id":"5057","group_name":"王者荣耀","flag":"http://apps.ifeimo.com/Public/Uploads/Game/Flag/591960b021a9f.png"},{"group_id":"112","game_id":"8128","group_name":"穿越火线","flag":"http://apps.ifeimo.com/Public/Uploads/Game/Flag/591962f9ddb95.png"},{"group_id":"30","game_id":"93","group_name":"我的世界","flag":"http://apps.ifeimo.com/Public/Uploads/Game/Flag/5919620dd04e5.png"},{"group_id":"713","game_id":"4804","group_name":"球球大作战","flag":"http://apps.ifeimo.com/Public/Uploads/Game/Flag/59196e76d02b2.png"},{"group_id":"113","game_id":"75","group_name":"部落冲突","flag":"http://apps.ifeimo.com/Public/Uploads/Game/Flag/5919635cc3662.png"},{"group_id":"1","game_id":"10","group_name":"天天酷跑","flag":"http://apps.ifeimo.com/Public/Uploads/Game/Flag/54cca2df8534c.png"},{"group_id":"108","game_id":"5062","group_name":"全民超神","flag":"http://apps.ifeimo.com/Public/Uploads/Game/Flag/59196ea3a5585.png"},{"group_id":"110","game_id":"88","group_name":"全民枪战","flag":"http://apps.ifeimo.com/Public/Uploads/Game/Flag/59196f0d5828a.png"},{"group_id":"804","game_id":"5028","group_name":"火影忍者","flag":"http://apps.ifeimo.com/Public/Uploads/Game/Flag/591963caaa620.png"}]
     * OData : null
     */

    private Object OData;
    private List<ADataBean> AData;

    public Object getOData() {
        return OData;
    }

    public void setOData(Object OData) {
        this.OData = OData;
    }

    public List<ADataBean> getAData() {
        return AData;
    }

    public void setAData(List<ADataBean> AData) {
        this.AData = AData;
    }

    public static class ADataBean  implements Serializable{
        /**
         * group_id : 109
         * game_id : 5057
         * group_name : 王者荣耀
         * flag : http://apps.ifeimo.com/Public/Uploads/Game/Flag/591960b021a9f.png
         */

        private String group_id;
        private String game_id;
        private String group_name;
        private String flag;
        private boolean isChoice;

        public boolean isChoice() {
            return isChoice;
        }

        public void setChoice(boolean choice) {
            isChoice = choice;
        }

        public String getGroup_id() {
            return group_id;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
        }

        public String getGame_id() {
            return game_id;
        }

        public void setGame_id(String game_id) {
            this.game_id = game_id;
        }

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }
    }
}
