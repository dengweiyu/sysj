package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

/**
 * 陪玩订单结果
 */

public class PlayWithOrderEntity extends BaseResponseEntity {


    /**
     * code : 10000
     * order : {"user_id":1201867,"coach_id":1228683,"game_area":1,"game_mode":1,"start_time":12357489,"inning":2,"price_total":120,"id":6,"grade":5}
     * residue_coin : 640
     */

    private OrderBean order;
    private String residue_coin;

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }

    public String getResidue_coin() {
        return residue_coin;
    }

    public void setResidue_coin(String residue_coin) {
        this.residue_coin = residue_coin;
    }

    public static class OrderBean {
        /**
         * user_id : 1201867
         * coach_id : 1228683
         * game_area : 1
         * game_mode : 1
         * start_time : 12357489
         * inning : 2
         * price_total : 120
         * id : 6
         * grade : 5
         */

        private long user_id;
        private long coach_id;
        private int game_area;
        private int game_mode;
        private long start_time;
        private int inning;
        private int price_total;
        private long id;
        private int grade;

        public long getUser_id() {
            return user_id;
        }

        public void setUser_id(long user_id) {
            this.user_id = user_id;
        }

        public long getCoach_id() {
            return coach_id;
        }

        public void setCoach_id(long coach_id) {
            this.coach_id = coach_id;
        }

        public int getGame_area() {
            return game_area;
        }

        public void setGame_area(int game_area) {
            this.game_area = game_area;
        }

        public int getGame_mode() {
            return game_mode;
        }

        public void setGame_mode(int game_mode) {
            this.game_mode = game_mode;
        }

        public long getStart_time() {
            return start_time;
        }

        public void setStart_time(long start_time) {
            this.start_time = start_time;
        }

        public int getInning() {
            return inning;
        }

        public void setInning(int inning) {
            this.inning = inning;
        }

        public int getPrice_total() {
            return price_total;
        }

        public void setPrice_total(int price_total) {
            this.price_total = price_total;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }
    }
}
