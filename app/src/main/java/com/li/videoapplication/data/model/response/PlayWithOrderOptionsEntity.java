package com.li.videoapplication.data.model.response;

import com.google.gson.annotations.SerializedName;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 * 陪玩订单 选项
 */

public class PlayWithOrderOptionsEntity extends BaseResponseEntity {

    /**
     * code : 10000
     * gameLevelMap : [{"value":1,"text":"倔强青铜"},{"value":2,"text":"秩序白银"},{"value":3,"text":"荣耀黄金"},{"value":4,"text":"尊贵铂金"},{"value":5,"text":"永恒钻石"},{"value":7,"text":"最强王者"},{"value":8,"text":"荣耀王者"}]
     * gameAreaMap : [{"value":1,"text":"qq"},{"value":2,"text":"微信"}]
     * gameModeMap : [{"value":1,"text":"匹配"},{"value":2,"text":"排位"}]
     */

    private List<GameLevelMapBean> gameLevelMap;
    private List<GameAreaMapBean> gameAreaMap;
    private List<GameModeMapBean> gameModeMap;


    public List<GameLevelMapBean> getGameLevelMap() {
        return gameLevelMap;
    }

    public void setGameLevelMap(List<GameLevelMapBean> gameLevelMap) {
        this.gameLevelMap = gameLevelMap;
    }

    public List<GameAreaMapBean> getGameAreaMap() {
        return gameAreaMap;
    }

    public void setGameAreaMap(List<GameAreaMapBean> gameAreaMap) {
        this.gameAreaMap = gameAreaMap;
    }

    public List<GameModeMapBean> getGameModeMap() {
        return gameModeMap;
    }

    public void setGameModeMap(List<GameModeMapBean> gameModeMap) {
        this.gameModeMap = gameModeMap;
    }

    public static class GameLevelMapBean {
        /**
         * value : 1
         * text : 倔强青铜
         */

        private int value;
        private String text;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class GameAreaMapBean {
        /**
         * value : 1
         * text : qq
         */

        private int value;
        private String text;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class GameModeMapBean {
        /**
         * value : 1
         * text : 匹配
         */

        private int value;
        private String text;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
