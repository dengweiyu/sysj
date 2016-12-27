package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

public class GetDetailModeEntity extends BaseResponseEntity {

    private String match_id;
    private String title;
    private String flag;
    private String share_description;
    private int display_mode;

    private List<Tab> tab;


    public String getShare_description() {
        return share_description;
    }

    public void setShare_description(String share_description) {
        this.share_description = share_description;
    }

    public String getMatch_id() {
        return match_id;
    }

    public void setMatch_id(String match_id) {
        this.match_id = match_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public int getDisplay_mode() {
        return display_mode;
    }

    public void setDisplay_mode(int display_mode) {
        this.display_mode = display_mode;
    }

    public List<Tab> getTab() {
        return tab;
    }

    public void setTab(List<Tab> tab) {
        this.tab = tab;
    }

    public class Tab{

        private String name;
        private String url;
        private String share_url;
        private int type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

}
