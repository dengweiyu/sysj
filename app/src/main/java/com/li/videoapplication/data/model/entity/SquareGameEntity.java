package com.li.videoapplication.data.model.entity;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 玩家广场游戏列表实体
 */

public class SquareGameEntity extends BaseResponseEntity {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * game_id : 5057
         * name : 王者荣耀
         * flag : http://apps.ifeimo.com/Public/Uploads/Game/Flag/5625b60cc6ff6.jpg
         */

        private String game_id;
        private String name;
        private String flag;
        private boolean isChoice;    //自定义字段  是否在列表中选中

        public String getGame_id() {
            return game_id;
        }

        public void setGame_id(String game_id) {
            this.game_id = game_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }


        public boolean isChoice() {
            return isChoice;
        }

        public void setChoice(boolean choice) {
            isChoice = choice;
        }
    }
}
