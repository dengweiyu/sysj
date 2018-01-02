package com.li.videoapplication.data.model.response;

import com.google.gson.annotations.SerializedName;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 * 教练列表
 */

public class CoachListEntity extends BaseResponseEntity {


    /**
     * data : {"page_count":1,"link":{"order_num_icon":"http://img.17sysj.com/apa_coach_order.png","win_rate_icon":"http://img.17sysj.com/app_coach_winRate.png"},"training_type":[{"id":"1","title":"王者荣耀陪练"},{"id":"2","title":"吃鸡陪练"}],"include":[{"member_id":"584218","game_level":"最强王者","win_rate":"100%","order_total":"212","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/59798706b1e7e.jpg","nickname":"官方陪练客服【9:00-18:00】","score":"5.0","picture_I":"coach_201708171736201b11","game_level_icon":"http://img.17sysj.com/app_coach_glory_king.png","status":2,"flag":"http://img.17sysj.com/coach_201708171736201b11"},{"member_id":"582767","game_level":"最强王者","win_rate":"100%","order_total":"29","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/5842ee821f468.jpg","nickname":"王者荣耀双系统接单","score":"5.0","picture_I":"coach_20170821145616dde","game_level_icon":"http://img.17sysj.com/app_coach_glory_king.png","status":2,"flag":"http://img.17sysj.com/coach_20170821145616dde"},{"member_id":"4775653","game_level":"最强王者","win_rate":"100%","order_total":"40","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/default1.JPG","nickname":"测试用陪练号","score":"5.0","picture_I":"coach_20170818104803d52","game_level_icon":"http://img.17sysj.com/app_coach_glory_king.png","status":2,"flag":"http://img.17sysj.com/coach_20170818104803d52"},{"member_id":"4761019","game_level":"最强王者","win_rate":"1%","order_total":"20","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/default26.JPG","nickname":"测试派","score":"5.0","picture_I":"","game_level_icon":"http://img.17sysj.com/app_coach_glory_king.png","status":2,"flag":""},{"member_id":"4741865","game_level":"至尊星曜","win_rate":"100%","order_total":"13","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/default3.JPG","nickname":"yangruiqi1","score":"5.0","picture_I":"","game_level_icon":"http://img.17sysj.com/app_coach_strongest_king.png","status":2,"flag":""},{"member_id":"4310023","game_level":"最强王者","win_rate":"100%","order_total":"44","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/597987503b461.jpg","nickname":"下单前看个性签名加Q","score":"5.0","picture_I":"coach_20170817143115336.jpg","game_level_icon":"http://img.17sysj.com/app_coach_glory_king.png","status":2,"flag":"http://img.17sysj.com/coach_20170817143115336.jpg"},{"member_id":"4266444","game_level":"最强王者","win_rate":"100%","order_total":"39","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/59939508aee4e.jpg","nickname":"很帅的昵称","score":"5.0","picture_I":"coach_20170816171544931.jpg","game_level_icon":"http://img.17sysj.com/app_coach_glory_king.png","status":2,"flag":"http://img.17sysj.com/coach_20170816171544931.jpg"},{"member_id":"4030524","game_level":"最强王者","win_rate":"100%","order_total":"30","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/59719660dd3f9.jpg","nickname":"luwei","score":"5.0","picture_I":"","game_level_icon":"http://img.17sysj.com/app_coach_glory_king.png","status":2,"flag":""},{"member_id":"3861185","game_level":"最强王者","win_rate":"100%","order_total":"46","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/qq_1499049416950.jpg","nickname":"luwei1","score":"5.0","picture_I":"","game_level_icon":"http://img.17sysj.com/app_coach_glory_king.png","status":2,"flag":""},{"member_id":"3634924","game_level":"最强王者","win_rate":"100%","order_total":"75","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/598f489de7285.jpg","nickname":"闪烁带你飞","score":"5.0","picture_I":"coach_201708280210331521","game_level_icon":"http://img.17sysj.com/app_coach_glory_king.png","status":2,"flag":"http://img.17sysj.com/coach_201708280210331521"},{"member_id":"3523215","game_level":"最强王者","win_rate":"1%","order_total":"20","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/598da2d617695.jpg","nickname":"测试陪练","score":"5.0","picture_I":"","game_level_icon":"http://img.17sysj.com/app_coach_glory_king.png","status":2,"flag":""},{"member_id":"3016068","game_level":"最强王者","win_rate":"100%","order_total":"71","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/597df02ca5628.jpg","nickname":"看我个性签名by千羽","score":"5.0","picture_I":"coach_20170817143330948.jpg","game_level_icon":"http://img.17sysj.com/app_coach_glory_king.png","status":2,"flag":"http://img.17sysj.com/coach_20170817143330948.jpg"},{"member_id":"2527289","game_level":"至尊星曜","win_rate":"33%","order_total":"29","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/58ef31b4bb2d6.jpg","nickname":"LiuW","score":"5.0","picture_I":"","game_level_icon":"http://img.17sysj.com/app_coach_strongest_king.png","status":2,"flag":""},{"member_id":"2394366","game_level":"最强王者","win_rate":"100%","order_total":"43","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/59996bd9920e3.jpg","nickname":"冷言｀下单加QQ","score":"5.0","picture_I":"coach_20170816171417108.jpg","game_level_icon":"http://img.17sysj.com/app_coach_glory_king.png","status":2,"flag":"http://img.17sysj.com/coach_20170816171417108.jpg"},{"member_id":"1722092","game_level":"至尊星曜","win_rate":"100%","order_total":"19","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/598a9ff8c75d5.jpg","nickname":"LinHui_1","score":"5.0","picture_I":"coach_201708101410541cd6","game_level_icon":"http://img.17sysj.com/app_coach_strongest_king.png","status":2,"flag":"http://img.17sysj.com/coach_201708101410541cd6"},{"member_id":"1201867","game_level":"英勇青铜","win_rate":"100%","order_total":"0","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/59730c0624566.jpg","nickname":"Zmjie","score":"5.0","picture_I":"","game_level_icon":"http://img.17sysj.com/app_coach_qingtong.png","status":2,"flag":""}]}
     * notice : 冲段大优惠：买五局减一局
     */

    private DataBean data;
    private String notice;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public static class DataBean {
        /**
         * page_count : 1
         * link : {"order_num_icon":"http://img.17sysj.com/apa_coach_order.png","win_rate_icon":"http://img.17sysj.com/app_coach_winRate.png"}
         * training_type : [{"id":"1","title":"王者荣耀陪练"},{"id":"2","title":"吃鸡陪练"}]
         * include : [{"member_id":"584218","game_level":"最强王者","win_rate":"100%","order_total":"212","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/59798706b1e7e.jpg","nickname":"官方陪练客服【9:00-18:00】","score":"5.0","picture_I":"coach_201708171736201b11","game_level_icon":"http://img.17sysj.com/app_coach_glory_king.png","status":2,"flag":"http://img.17sysj.com/coach_201708171736201b11"},{"member_id":"582767","game_level":"最强王者","win_rate":"100%","order_total":"29","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/5842ee821f468.jpg","nickname":"王者荣耀双系统接单","score":"5.0","picture_I":"coach_20170821145616dde","game_level_icon":"http://img.17sysj.com/app_coach_glory_king.png","status":2,"flag":"http://img.17sysj.com/coach_20170821145616dde"},{"member_id":"4775653","game_level":"最强王者","win_rate":"100%","order_total":"40","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/default1.JPG","nickname":"测试用陪练号","score":"5.0","picture_I":"coach_20170818104803d52","game_level_icon":"http://img.17sysj.com/app_coach_glory_king.png","status":2,"flag":"http://img.17sysj.com/coach_20170818104803d52"},{"member_id":"4761019","game_level":"最强王者","win_rate":"1%","order_total":"20","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/default26.JPG","nickname":"测试派","score":"5.0","picture_I":"","game_level_icon":"http://img.17sysj.com/app_coach_glory_king.png","status":2,"flag":""},{"member_id":"4741865","game_level":"至尊星曜","win_rate":"100%","order_total":"13","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/default3.JPG","nickname":"yangruiqi1","score":"5.0","picture_I":"","game_level_icon":"http://img.17sysj.com/app_coach_strongest_king.png","status":2,"flag":""},{"member_id":"4310023","game_level":"最强王者","win_rate":"100%","order_total":"44","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/597987503b461.jpg","nickname":"下单前看个性签名加Q","score":"5.0","picture_I":"coach_20170817143115336.jpg","game_level_icon":"http://img.17sysj.com/app_coach_glory_king.png","status":2,"flag":"http://img.17sysj.com/coach_20170817143115336.jpg"},{"member_id":"4266444","game_level":"最强王者","win_rate":"100%","order_total":"39","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/59939508aee4e.jpg","nickname":"很帅的昵称","score":"5.0","picture_I":"coach_20170816171544931.jpg","game_level_icon":"http://img.17sysj.com/app_coach_glory_king.png","status":2,"flag":"http://img.17sysj.com/coach_20170816171544931.jpg"},{"member_id":"4030524","game_level":"最强王者","win_rate":"100%","order_total":"30","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/59719660dd3f9.jpg","nickname":"luwei","score":"5.0","picture_I":"","game_level_icon":"http://img.17sysj.com/app_coach_glory_king.png","status":2,"flag":""},{"member_id":"3861185","game_level":"最强王者","win_rate":"100%","order_total":"46","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/qq_1499049416950.jpg","nickname":"luwei1","score":"5.0","picture_I":"","game_level_icon":"http://img.17sysj.com/app_coach_glory_king.png","status":2,"flag":""},{"member_id":"3634924","game_level":"最强王者","win_rate":"100%","order_total":"75","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/598f489de7285.jpg","nickname":"闪烁带你飞","score":"5.0","picture_I":"coach_201708280210331521","game_level_icon":"http://img.17sysj.com/app_coach_glory_king.png","status":2,"flag":"http://img.17sysj.com/coach_201708280210331521"},{"member_id":"3523215","game_level":"最强王者","win_rate":"1%","order_total":"20","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/598da2d617695.jpg","nickname":"测试陪练","score":"5.0","picture_I":"","game_level_icon":"http://img.17sysj.com/app_coach_glory_king.png","status":2,"flag":""},{"member_id":"3016068","game_level":"最强王者","win_rate":"100%","order_total":"71","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/597df02ca5628.jpg","nickname":"看我个性签名by千羽","score":"5.0","picture_I":"coach_20170817143330948.jpg","game_level_icon":"http://img.17sysj.com/app_coach_glory_king.png","status":2,"flag":"http://img.17sysj.com/coach_20170817143330948.jpg"},{"member_id":"2527289","game_level":"至尊星曜","win_rate":"33%","order_total":"29","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/58ef31b4bb2d6.jpg","nickname":"LiuW","score":"5.0","picture_I":"","game_level_icon":"http://img.17sysj.com/app_coach_strongest_king.png","status":2,"flag":""},{"member_id":"2394366","game_level":"最强王者","win_rate":"100%","order_total":"43","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/59996bd9920e3.jpg","nickname":"冷言｀下单加QQ","score":"5.0","picture_I":"coach_20170816171417108.jpg","game_level_icon":"http://img.17sysj.com/app_coach_glory_king.png","status":2,"flag":"http://img.17sysj.com/coach_20170816171417108.jpg"},{"member_id":"1722092","game_level":"至尊星曜","win_rate":"100%","order_total":"19","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/598a9ff8c75d5.jpg","nickname":"LinHui_1","score":"5.0","picture_I":"coach_201708101410541cd6","game_level_icon":"http://img.17sysj.com/app_coach_strongest_king.png","status":2,"flag":"http://img.17sysj.com/coach_201708101410541cd6"},{"member_id":"1201867","game_level":"英勇青铜","win_rate":"100%","order_total":"0","avatar":"http://apps.ifeimo.com/Public/Uploads/Member/Avatar/59730c0624566.jpg","nickname":"Zmjie","score":"5.0","picture_I":"","game_level_icon":"http://img.17sysj.com/app_coach_qingtong.png","status":2,"flag":""}]
         */

        private int page_count;
        private LinkBean link;
        private List<TrainingTypeBean> training_type;
        private List<IncludeBean> include;

        public int getPage_count() {
            return page_count;
        }

        public void setPage_count(int page_count) {
            this.page_count = page_count;
        }

        public LinkBean getLink() {
            return link;
        }

        public void setLink(LinkBean link) {
            this.link = link;
        }

        public List<TrainingTypeBean> getTraining_type() {
            return training_type;
        }

        public void setTraining_type(List<TrainingTypeBean> training_type) {
            this.training_type = training_type;
        }

        public List<IncludeBean> getInclude() {
            return include;
        }

        public void setInclude(List<IncludeBean> include) {
            this.include = include;
        }

        public static class LinkBean {
            /**
             * order_num_icon : http://img.17sysj.com/apa_coach_order.png
             * win_rate_icon : http://img.17sysj.com/app_coach_winRate.png
             */

            private String order_num_icon;
            private String win_rate_icon;

            public String getOrder_num_icon() {
                return order_num_icon;
            }

            public void setOrder_num_icon(String order_num_icon) {
                this.order_num_icon = order_num_icon;
            }

            public String getWin_rate_icon() {
                return win_rate_icon;
            }

            public void setWin_rate_icon(String win_rate_icon) {
                this.win_rate_icon = win_rate_icon;
            }
        }

        public static class TrainingTypeBean {
            /**
             * id : 1
             * title : 王者荣耀陪练
             */

            private String id;
            private String title;
            private int isCheck;
            private String game_name;

            public String getGame_name() {
                return game_name;
            }

            public void setGame_name(String game_name) {
                this.game_name = game_name;
            }

            public int getIsCheck() {
                return isCheck;
            }

            public void setIsCheck(int isCheck) {
                this.isCheck = isCheck;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }

        public static class IncludeBean {
            /**
             * member_id : 584218
             * game_level : 最强王者
             * win_rate : 100%
             * order_total : 212
             * avatar : http://apps.ifeimo.com/Public/Uploads/Member/Avatar/59798706b1e7e.jpg
             * nickname : 官方陪练客服【9:00-18:00】
             * score : 5.0
             * picture_I : coach_201708171736201b11
             * game_level_icon : http://img.17sysj.com/app_coach_glory_king.png
             * status : 2
             * flag : http://img.17sysj.com/coach_201708171736201b11
             */

            private String member_id;
            private String game_level;
            private String win_rate;
            private String order_total;
            private String avatar;
            private String nickname;
            private String score;
            private String picture_I;
            private String game_level_icon;
            @SerializedName("status")
            private int statusX;
            private String flag;

            public String getMember_id() {
                return member_id;
            }

            public void setMember_id(String member_id) {
                this.member_id = member_id;
            }

            public String getGame_level() {
                return game_level;
            }

            public void setGame_level(String game_level) {
                this.game_level = game_level;
            }

            public String getWin_rate() {
                return win_rate;
            }

            public void setWin_rate(String win_rate) {
                this.win_rate = win_rate;
            }

            public String getOrder_total() {
                return order_total;
            }

            public void setOrder_total(String order_total) {
                this.order_total = order_total;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getScore() {
                return score;
            }

            public void setScore(String score) {
                this.score = score;
            }

            public String getPicture_I() {
                return picture_I;
            }

            public void setPicture_I(String picture_I) {
                this.picture_I = picture_I;
            }

            public String getGame_level_icon() {
                return game_level_icon;
            }

            public void setGame_level_icon(String game_level_icon) {
                this.game_level_icon = game_level_icon;
            }

            public int getStatusX() {
                return statusX;
            }

            public void setStatusX(int statusX) {
                this.statusX = statusX;
            }

            public String getFlag() {
                return flag;
            }

            public void setFlag(String flag) {
                this.flag = flag;
            }
        }
    }
}
