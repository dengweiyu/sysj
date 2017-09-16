package com.li.videoapplication.data.model.entity;

import java.io.Serializable;

/**
 *
 */

public class IMPushCustomEntity {
    private String module;

    private Parameter parameter;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Parameter getParameter() {
        return parameter;
    }

    private String  action;

    public String getAction() {
        return action;
    }

    public void setAction(String actionX) {
         action = actionX;
    }
    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public static class Parameter implements Serializable{
        private String order_id;
        private String pkg_content;
        private int role;
        private String nickname;
        private String avatar;
        private String score;
        private String coach_id;



        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nick_name) {
            this.nickname = nick_name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getCoach_id() {
            return coach_id;
        }

        public void setCoach_id(String coach_id) {
            this.coach_id = coach_id;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getPkg_content() {
            return pkg_content;
        }

        public void setPkg_content(String pkg_content) {
            this.pkg_content = pkg_content;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }
    }
}
