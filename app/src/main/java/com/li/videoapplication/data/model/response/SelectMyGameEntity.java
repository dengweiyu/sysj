package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 * Created by cx on 2018/1/9.
 */

public class SelectMyGameEntity extends BaseResponseEntity {

    private List<Bean> AData;

    public List<Bean> getAData() {
        return AData;
    }

    public void setAData(List<Bean> AData) {
        this.AData = AData;
    }

    public static class Bean{
        String group_id;
        String group_name;
        String flag;
        String url;
        int is_attention;
        private boolean selected;

        public String getGroup_id() {
            return group_id;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getIs_attention() {
            return is_attention;
        }

        public void setIs_attention(int is_attention) {
            this.is_attention = is_attention;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
}
