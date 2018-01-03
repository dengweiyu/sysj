package com.li.videoapplication.data.model.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.li.videoapplication.data.model.response.HomeModuleEntity;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 新版本-首页分栏数据
 */

public class MyHomeModuleEntity extends BaseResponseEntity implements Cloneable {


    public static final int TYPE_HOT_GAME = 0;          //热门游戏
    public static final int TYPE_AD = 1;                //通栏广告
    public static final int TYPE_GUESS_YOU_LIKE = 2;    //猜你喜欢
    public static final int TYPE_SYSJ_VIDEO = 3;        //视界原创
    public static final int TYPE_HOT_NARRATE = 4;       //热门主播
    public static final int TYPE_VIDEO_GROUP = 5;       //游戏视频
    public static final int TYPE_GAMER_VIDEO = 6;       //玩家视频

    //区域标题栏
    public static final int TYPE_HOT_GAME_TITLE = 7;          //热门游戏
    public static final int TYPE_AD_TITLE = 8;                //通栏广告
    public static final int TYPE_GUESS_YOU_LIKE_TITLE = 9;    //猜你喜欢
    public static final int TYPE_SYSJ_VIDEO_TITLE = 10;        //视界原创
    public static final int TYPE_HOT_NARRATE_TITLE = 11;       //热门主播
    public static final int TYPE_VIDEO_GROUP_TITLE = 12;       //游戏视频
    public static final int TYPE_GAMER_VIDEO_TITLE = 13;       //玩家视频


    /**
     * AData : [{"more_mark":"","flag":"banner","title":"幻灯图","list":[{"type":"activity","type_id":"132","title":"手游视界陪练模块上线","flag":"59a386c30e61a.jpg","go_url":"http://www.17sysj.com/act/pljs/","match_id":"132","flagPath":"http://192.168.48.9:233/Public/Uploads/Focuse/Flag/59a386c30e61a.jpg"},{"type":"video","type_id":"4898767","title":"《快递小哥》帝王变身快递小哥去送快递啦！","flag":"video_20170824184146528.jpg","go_url":"","video_id":"4898767","flagPath":"http://cover.17sysj.com/video_20170824184146528.jpg?imageView2/2/h/200","yk_url":"","qn_key":"lpds_c5a22d2d0726b"},{"type":"video","type_id":"4899769","title":"《王者荣耀》荣耀大神秀第36期刺客操作要领全解析","flag":"video_20170824142810239.jpg","go_url":"","video_id":"4899769","flagPath":"http://cover.17sysj.com/video_20170824142810239.jpg?imageView2/2/h/200","yk_url":"","qn_key":"bj_rydsx36"},{"type":"event","type_id":"596","title":"《王者荣耀》实力对垒","flag":"598c1a0ba7c75.jpg","go_url":"","event_id":"596","flagPath":"http://192.168.48.9:233/Public/Uploads/Event/Flag/598c1a0ba7c75.jpg"},{"type":"video","type_id":"4879543","title":"《FACE DANCE》试玩 这游戏随手一截就是表情包","flag":"video_20170823171347584.jpg","go_url":"","video_id":"4879543","flagPath":"http://cover.17sysj.com/video_20170823171347584.jpg?imageView2/2/h/200","yk_url":"","qn_key":"lpds_00c85fd40722b"},{"type":"video","type_id":"4879765","title":"【王者快报】新版本\u201c齐聚长城\u201d速看 8月24更新内容一览","flag":"video_20170823154431456.jpg","go_url":"","video_id":"4879765","flagPath":"http://cover.17sysj.com/video_20170823154431456.jpg?imageView2/2/h/200","yk_url":"","qn_key":"sysj_dSGU5EeIzvH3a"}],"icon_pic":""},{"more_mark":"","flag":"hotGame","title":"热门游戏","list":[{"group_id":"109","game_id":"5057","group_name":"王者荣耀","flag":"http://192.168.48.9:233/Public/Uploads/Game/Flag/591960b021a9f.png","url":""},{"group_id":"30","game_id":"93","group_name":"我的世界","flag":"http://192.168.48.9:233/Public/Uploads/Game/Flag/5919620dd04e5.png","url":""},{"group_id":"112","game_id":"8128","group_name":"穿越火线","flag":"http://192.168.48.9:233/Public/Uploads/Game/Flag/591962f9ddb95.png","url":""},{"group_id":"713","game_id":"4804","group_name":"球球大作战","flag":"http://192.168.48.9:233/Public/Uploads/Game/Flag/59196e76d02b2.png","url":""},{"group_id":"804","game_id":"5028","group_name":"火影忍者","flag":"http://192.168.48.9:233/Public/Uploads/Game/Flag/591963caaa620.png","url":""},{"group_id":"172","game_id":"101","group_name":"节奏大师","flag":"http://192.168.48.9:233/Public/Uploads/Game/Flag/54a39739b9b7d.png","url":""},{"group_id":"1538","game_id":"9710","group_name":"滚动的天空","flag":"http://192.168.48.9:233/Public/Uploads/Game/Flag/57ff62a3deb8f.png","url":""},{"group_id":"1293","game_id":"9042","group_name":"皇室战争","flag":"http://192.168.48.9:233/Public/Uploads/Game/Flag/591963aca4b34.png","url":""},{"group_id":"1459","game_id":"9262","group_name":"迷你世界","flag":"http://192.168.48.9:233/Public/Uploads/Game/Flag/574e8f5149ebb.gif","url":""},{"group_id":"496","game_id":"2262","group_name":"全民突击","flag":"http://192.168.48.9:233/Public/Uploads/Game/Flag/59196f559fc13.png","url":""},{"group_id":"113","game_id":"75","group_name":"部落冲突","flag":"http://192.168.48.9:233/Public/Uploads/Game/Flag/5919635cc3662.png","url":""},{"group_id":"110","game_id":"88","group_name":"全民枪战","flag":"http://192.168.48.9:233/Public/Uploads/Game/Flag/59196f0d5828a.png","url":""},{"group_id":"164","game_id":"92","group_name":"天天炫斗","flag":"http://192.168.48.9:233/Public/Uploads/Game/Flag/59196ff053a88.png","url":""},{"group_id":"1524","game_id":"9625","group_name":"贪吃蛇大作战","flag":"http://192.168.48.9:233/Public/Uploads/Game/Flag/59196fc968f18.png","url":""},{"group_id":"1513","game_id":"9629","group_name":"全民斗战神","flag":"http://192.168.48.9:233/Public/Uploads/Game/Flag/59196ee829b48.png","url":""},{"group_id":"473","game_id":"2002","group_name":"泰拉瑞亚","flag":"http://192.168.48.9:233/Public/Uploads/Game/Flag/59196f822d039.png","url":""},{"group_id":"1103","game_id":"8799","group_name":"时空召唤","flag":"http://192.168.48.9:233/Public/Uploads/Game/Flag/591964878658a.png","url":""},{"group_id":"1532","game_id":"9372","group_name":"阴阳师","flag":"http://192.168.48.9:233/Public/Uploads/Game/Flag/59196465c2188.png","url":""},{"group_id":"29","game_id":"39","group_name":"海岛奇兵","flag":"http://192.168.48.9:233/Public/Uploads/Game/Flag/5919637b8f10e.png","url":""},{"group_id":"803","game_id":"5027","group_name":"炉石传说","flag":"http://192.168.48.9:233/Public/Uploads/Game/Flag/55c170350c110.png","url":""}],"icon_pic":""},{"video_ids":"4977020,4943214,4918381,4938486","more_mark":"","flag":"guessVideo","title":"猜你喜欢","list":[{"video_id":"4918381","title":"《吃豆大作战》趣萌休闲手游 再看就吃掉你！","flag":"pic_fb813e3e4754","pic_flsp":"","pic_mrgx":"","click_count":"49757","yk_url":"","qn_key":"lpds_6e02c74e7671b","time_length":"1504602370","cover_position":"1","flagPath":"http://cover.17sysj.com/pic_fb813e3e4754?imageView2/2/h/200"},{"video_id":"4938486","title":"《部落冲突》棍总天女蓝矿应变三星满十一防三","flag":"pic_e4af5b952542","pic_flsp":"","pic_mrgx":"","click_count":"37243","yk_url":"","qn_key":"lpds_c292628a3965b","time_length":"1503892693","cover_position":"1","flagPath":"http://cover.17sysj.com/pic_e4af5b952542?imageView2/2/h/200"},{"video_id":"4943214","title":"（星仔解说）王者荣耀新手秀第十期，用死亡骑士5连胜!","flag":"pic_6a2f7eb04799","pic_flsp":"","pic_mrgx":"","click_count":"35519","yk_url":"","qn_key":"lpds_494e0a596350b","time_length":"1503802021","cover_position":"1","flagPath":"http://cover.17sysj.com/pic_6a2f7eb04799?imageView2/2/h/200"},{"video_id":"4977020","title":"《火影忍者》落岩秘境磨血打法","flag":"pic_dd8bdba11811","pic_flsp":"","pic_mrgx":"","click_count":"32332","yk_url":"","qn_key":"lpds_f07c33556040b","time_length":"1503974925","cover_position":"1","flagPath":"http://cover.17sysj.com/pic_dd8bdba11811?imageView2/2/h/200"}],"icon_pic":""},{"isGame":"0","group_id":"","game_id":"","list":[{"video_id":"4937731","title":"【王者快报】诸葛亮貂蝉甄姬新皮肤 狄仁杰新模型","flag":"video_201708261436141070.jpg","pic_flsp":"","pic_mrgx":"","click_count":"44283","yk_url":"","qn_key":"sysj_G7wTp4WROhGNa","time_length":"1503729385","cover_position":"1","isRecommend":1,"flagPath":"http://cover.17sysj.com/video_201708261436141070.jpg?imageView2/2/h/200"},{"video_id":"4899769","title":"《王者荣耀》荣耀大神秀第36期刺客操作要领全解析","flag":"video_20170824142851730.jpg","pic_flsp":"","pic_mrgx":"video_20170824142810239.jpg","click_count":"13127","yk_url":"","qn_key":"bj_rydsx36","time_length":"1503556234","cover_position":"1","isRecommend":1,"flagPath":"http://cover.17sysj.com/video_20170824142851730.jpg?imageView2/2/h/200"},{"video_id":"4894734","title":"《方块大乱》小张解说 比球球好玩十倍的游戏！","flag":"pic_18db6b719163","pic_flsp":"","pic_mrgx":"video_20170824102752468.jpg","click_count":"43072","yk_url":"","qn_key":"lpds_4ba4f6db9029a","time_length":"1503541695","cover_position":"1","isRecommend":1,"flagPath":"http://cover.17sysj.com/pic_18db6b719163?imageView2/2/h/200"},{"video_id":"4879543","title":"《FACE DANCE》试玩 这游戏随手一截就是表情包","flag":"video_20170823170520243.jpg","pic_flsp":"","pic_mrgx":"video_20170823171347584.jpg","click_count":"27930","yk_url":"","qn_key":"lpds_00c85fd40722b","time_length":"1503479664","cover_position":"1","isRecommend":1,"flagPath":"http://cover.17sysj.com/video_20170823170520243.jpg?imageView2/2/h/200"}],"more_mark":"","flag":"sysj_original","title":"视界推荐","icon_pic":""},{"more_mark":"","flag":"hotMember","title":"热门解说","list":[{"member_id":"60","nickname":"小小舞","avatar":"http://192.168.48.9:233/Public/Uploads/Member/Avatar/56f8a34a68ff6.jpg","sex":"0","upload_time":"1444448965"},{"member_id":"9","nickname":"admin","avatar":"http://192.168.48.9:233/Public/Uploads/Member/Avatar/default.png","sex":"0","upload_time":"1505123759"},{"member_id":"269092","nickname":"孤独海怪69","avatar":"http://192.168.48.9:233/Public/Uploads/Member/Avatar/5857f6cef3677.jpg","sex":"1","upload_time":"1499308691"},{"member_id":"229027","nickname":"清雨","avatar":"http://192.168.48.9:233/Public/Uploads/Member/Avatar/5914422e53d61.jpg","sex":"1","upload_time":"1452968776"},{"member_id":"230653","nickname":"小玄","avatar":"http://192.168.48.9:233/Public/Uploads/Member/Avatar/586651a675c35.jpg","sex":"1","upload_time":"1453128057"},{"member_id":"82609","nickname":"天下吟唱","avatar":"http://192.168.48.9:233/Public/Uploads/Member/Avatar/563efa2cc907c.jpg","sex":"1","upload_time":"1450694837"},{"member_id":"93992","nickname":"小小诺","avatar":"http://192.168.48.9:233/Public/Uploads/Member/Avatar/5708b22cb9cda.jpg","sex":"1","upload_time":"1445148385"},{"member_id":"194212","nickname":"喵老师","avatar":"http://192.168.48.9:233/Public/Uploads/Member/Avatar/5866f76e169bc.jpg","sex":"1","upload_time":"1454687525"},{"member_id":"169442","nickname":"Boss天","avatar":"http://192.168.48.9:233/Public/Uploads/Member/Avatar/56ec4d7d622c9.jpg","sex":"1","upload_time":"1456653812"},{"member_id":"284263","nickname":"舒奇yokuwa","avatar":"http://192.168.48.9:233/Public/Uploads/Member/Avatar/5942bad7a41f3.jpg","sex":"0","upload_time":"1456552183"},{"member_id":"81458","nickname":"晨风","avatar":"http://192.168.48.9:233/Public/Uploads/Member/Avatar/5849113ad6ee1.jpg","sex":"1","upload_time":"1444440983"},{"member_id":"243564","nickname":"ぜ桩桩Roger's","avatar":"http://192.168.48.9:233/Public/Uploads/Member/Avatar/575a83480e976.jpg","sex":"1","upload_time":"1456576371"},{"member_id":"252145","nickname":"可爱的小公举","avatar":"http://192.168.48.9:233/Public/Uploads/Member/Avatar/57bbc29ed9986.jpg","sex":"1","upload_time":"1456477411"},{"member_id":"235638","nickname":"许生解说","avatar":"http://192.168.48.9:233/Public/Uploads/Member/Avatar/56a0d1b3eef0e.jpg","sex":"1","upload_time":"1456365659"},{"member_id":"57","nickname":"小小白","avatar":"http://192.168.48.9:233/Public/Uploads/Member/Avatar/562a5ad75b27c.jpg","sex":"1","upload_time":"1450323509"},{"member_id":"48","nickname":"舞大大","avatar":"http://192.168.48.9:233/Public/Uploads/Member/Avatar/56f8ef20d9496.jpg","sex":"0","upload_time":"1445219571"},{"member_id":"54","nickname":"胖哥","avatar":"http://192.168.48.9:233/Public/Uploads/Member/Avatar/568cc7b4cfe62.jpg","sex":"0","upload_time":"1444360425"},{"member_id":"183036","nickname":"骏Gg","avatar":"http://192.168.48.9:233/Public/Uploads/Member/Avatar/56a35c2bf11e4.jpg","sex":"1","upload_time":"1450181035"},{"member_id":"59","nickname":"SJ","avatar":"http://192.168.48.9:233/Public/Uploads/Member/Avatar/56455854f4212.jpg","sex":"1","upload_time":"1452679579"},{"member_id":"56","nickname":"视界小柿","avatar":"http://192.168.48.9:233/Public/Uploads/Member/Avatar/pc_1464764520678.jpg","sex":"0","upload_time":"1444464476"}],"icon_pic":""}]
     * OData : null
     * page_count : 4
     */


    private int page_count;
    private List<ADataBean> AData;

    public int getPage_count() {
        return page_count;
    }

    public void setPage_count(int page_count) {
        this.page_count = page_count;
    }

    public List<ADataBean> getAData() {
        return AData;
    }

    public void setAData(List<ADataBean> AData) {
        this.AData = AData;
    }

    public static class ADataBean implements MultiItemEntity, Cloneable {
        public static final int TITLE_SPAN_SIZE = 1;
        public static final int VIDEO_SPAN_SIZE = 2;

        private int mItemType;
        private int spanSize;


        public void setItemType(int itemType) {
            mItemType = itemType;
        }

        @Override
        public int getItemType() {
            return mItemType;
        }

        public int getSpanSize() {
            return spanSize;
        }

        public void setSpanSize(int spanSize) {
            this.spanSize = spanSize;
        }

        public ADataBean(){

        }

        public ADataBean(HomeModuleEntity.ADataBean.ListBean listBean, int spanSize, int type) {
            this.listBean = listBean;
            this.spanSize = spanSize;
            this.mItemType = type;
        }

        /**
         * more_mark :
         * flag : banner
         * title : 幻灯图
         * list : [{"type":"activity","type_id":"132","title":"手游视界陪练模块上线","flag":"59a386c30e61a.jpg","go_url":"http://www.17sysj.com/act/pljs/","match_id":"132","flagPath":"http://192.168.48.9:233/Public/Uploads/Focuse/Flag/59a386c30e61a.jpg"},{"type":"video","type_id":"4898767","title":"《快递小哥》帝王变身快递小哥去送快递啦！","flag":"video_20170824184146528.jpg","go_url":"","video_id":"4898767","flagPath":"http://cover.17sysj.com/video_20170824184146528.jpg?imageView2/2/h/200","yk_url":"","qn_key":"lpds_c5a22d2d0726b"},{"type":"video","type_id":"4899769","title":"《王者荣耀》荣耀大神秀第36期刺客操作要领全解析","flag":"video_20170824142810239.jpg","go_url":"","video_id":"4899769","flagPath":"http://cover.17sysj.com/video_20170824142810239.jpg?imageView2/2/h/200","yk_url":"","qn_key":"bj_rydsx36"},{"type":"event","type_id":"596","title":"《王者荣耀》实力对垒","flag":"598c1a0ba7c75.jpg","go_url":"","event_id":"596","flagPath":"http://192.168.48.9:233/Public/Uploads/Event/Flag/598c1a0ba7c75.jpg"},{"type":"video","type_id":"4879543","title":"《FACE DANCE》试玩 这游戏随手一截就是表情包","flag":"video_20170823171347584.jpg","go_url":"","video_id":"4879543","flagPath":"http://cover.17sysj.com/video_20170823171347584.jpg?imageView2/2/h/200","yk_url":"","qn_key":"lpds_00c85fd40722b"},{"type":"video","type_id":"4879765","title":"【王者快报】新版本\u201c齐聚长城\u201d速看 8月24更新内容一览","flag":"video_20170823154431456.jpg","go_url":"","video_id":"4879765","flagPath":"http://cover.17sysj.com/video_20170823154431456.jpg?imageView2/2/h/200","yk_url":"","qn_key":"sysj_dSGU5EeIzvH3a"}]
         * icon_pic :
         * video_ids : 4977020,4943214,4918381,4938486
         * isGame : 0
         * group_id :
         * game_id :
         */

        private String more_mark;
        private String flag;
        private String title;
        private String icon_pic;
        private String video_ids;
        private String isGame;
        private String group_id;
        private String game_id;
        private HomeModuleEntity.ADataBean.ListBean listBean;
        private List<HomeModuleEntity.ADataBean.ListBean> list;

        public String getMore_mark() {
            return more_mark;
        }

        public void setMore_mark(String more_mark) {
            this.more_mark = more_mark;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIcon_pic() {
            return icon_pic;
        }

        public void setIcon_pic(String icon_pic) {
            this.icon_pic = icon_pic;
        }

        public String getVideo_ids() {
            return video_ids;
        }

        public void setVideo_ids(String video_ids) {
            this.video_ids = video_ids;
        }

        public String getIsGame() {
            return isGame;
        }

        public void setIsGame(String isGame) {
            this.isGame = isGame;
        }

        public HomeModuleEntity.ADataBean.ListBean getListBean() {
            return listBean;
        }

        public void setListBean(HomeModuleEntity.ADataBean.ListBean listBean) {
            this.listBean = listBean;
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

        public List<HomeModuleEntity.ADataBean.ListBean> getList() {
            return list;
        }

        public void setList(List<HomeModuleEntity.ADataBean.ListBean> list) {
            this.list = list;
        }

        public void clearList() {
            this.list.clear();
        }


        @Override
        protected Object clone() throws CloneNotSupportedException {
            ADataBean aDataBean = (ADataBean) super.clone();
            List<HomeModuleEntity.ADataBean.ListBean> listBeens = new ArrayList<>();
            Iterator<HomeModuleEntity.ADataBean.ListBean> iterator = this.list.iterator();
            while (iterator.hasNext()) {
                listBeens.add((HomeModuleEntity.ADataBean.ListBean) iterator.next().clone());
            }
            aDataBean.list = listBeens;
            HomeModuleEntity.ADataBean.ListBean listBean = (HomeModuleEntity.ADataBean.ListBean) this.listBean.clone();
            aDataBean.listBean = listBean;
            return aDataBean;
        }
    }

    @Override
    public Object clone() {
        MyHomeModuleEntity entity = (MyHomeModuleEntity) super.clone();
        if (this.AData != null) {
            List<ADataBean> aDataBeanList = new ArrayList<>();
            for (ADataBean aDataBean : this.AData) {
                try {
                    aDataBeanList.add((ADataBean) aDataBean.clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
            entity.AData = aDataBeanList;
        }

        return entity;

    }
}
