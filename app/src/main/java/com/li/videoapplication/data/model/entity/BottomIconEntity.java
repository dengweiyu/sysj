package com.li.videoapplication.data.model.entity;

/**
 *
 */

public class BottomIconEntity {

    /**
     * updateTime : 1504854573
     * MenuIco : {"index":"http://img.17sysj.com/menuIco_1504768487.png","indexChecked":"http://img.17sysj.com/menuIco_1504768488.png","group":"http://img.17sysj.com/menuIco_1504768489.png","groupChecked":"http://img.17sysj.com/menuIco_1504768490.png","screen":"http://img.17sysj.com/menuIco_1504768397.png","screenChecked":"http://img.17sysj.com/menuIco_1504768482.png","discovery":"http://img.17sysj.com/menuIco_1504768483.png","discoveryChecked":"http://img.17sysj.com/menuIco_1504768484.png","training":"http://img.17sysj.com/menuIco_1504768485.png","trainingChecked":"http://img.17sysj.com/menuIco_1504768486.png"}
     */

    private int updateTime;
    private MenuIcoBean MenuIco;

    public int getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
    }

    public MenuIcoBean getMenuIco() {
        return MenuIco;
    }

    public void setMenuIco(MenuIcoBean MenuIco) {
        this.MenuIco = MenuIco;
    }

    public static class MenuIcoBean {
        /**
         * index : http://img.17sysj.com/menuIco_1504768487.png
         * indexChecked : http://img.17sysj.com/menuIco_1504768488.png
         * group : http://img.17sysj.com/menuIco_1504768489.png
         * groupChecked : http://img.17sysj.com/menuIco_1504768490.png
         * screen : http://img.17sysj.com/menuIco_1504768397.png
         * screenChecked : http://img.17sysj.com/menuIco_1504768482.png
         * discovery : http://img.17sysj.com/menuIco_1504768483.png
         * discoveryChecked : http://img.17sysj.com/menuIco_1504768484.png
         * training : http://img.17sysj.com/menuIco_1504768485.png
         * trainingChecked : http://img.17sysj.com/menuIco_1504768486.png
         */

        private String index;
        private String indexChecked;
        private String group;
        private String groupChecked;
        private String screen;
        private String screenChecked;
        private String discovery;
        private String discoveryChecked;
        private String training;
        private String trainingChecked;

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public String getIndexChecked() {
            return indexChecked;
        }

        public void setIndexChecked(String indexChecked) {
            this.indexChecked = indexChecked;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getGroupChecked() {
            return groupChecked;
        }

        public void setGroupChecked(String groupChecked) {
            this.groupChecked = groupChecked;
        }

        public String getScreen() {
            return screen;
        }

        public void setScreen(String screen) {
            this.screen = screen;
        }

        public String getScreenChecked() {
            return screenChecked;
        }

        public void setScreenChecked(String screenChecked) {
            this.screenChecked = screenChecked;
        }

        public String getDiscovery() {
            return discovery;
        }

        public void setDiscovery(String discovery) {
            this.discovery = discovery;
        }

        public String getDiscoveryChecked() {
            return discoveryChecked;
        }

        public void setDiscoveryChecked(String discoveryChecked) {
            this.discoveryChecked = discoveryChecked;
        }

        public String getTraining() {
            return training;
        }

        public void setTraining(String training) {
            this.training = training;
        }

        public String getTrainingChecked() {
            return trainingChecked;
        }

        public void setTrainingChecked(String trainingChecked) {
            this.trainingChecked = trainingChecked;
        }
    }
}
