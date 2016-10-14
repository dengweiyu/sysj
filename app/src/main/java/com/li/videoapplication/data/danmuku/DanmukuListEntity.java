package com.li.videoapplication.data.danmuku;

import com.li.videoapplication.data.model.entity.Bullet;
import com.li.videoapplication.data.model.response.BulletList203Entity;

import java.util.ArrayList;
import java.util.List;

public class DanmukuListEntity extends BaseXMLEntity {

    /**
     * document-TAG
     */
    public static final String I = "i";

    /**
     * chatserver-TAG
     */
    public static final String CHATSERVER = "chatserver";

    /**
     * chatid-TAG
     */
    public static final String CHATID = "chatid";

    /**
     * mission-TAG
     */
    public static final String MISSION = "mission";

    /**
     * maxlimit-TAG
     */
    public static final String MAXLIMIT = "maxlimit";

    /**
     * maxlimit-TAG
     */
    public static final String SOURCE = "source";

//    <danmuserver>danmu.ifeimo.com</danmuserver>
//    <danmuid>000001</danmuid>
//    <mission>0</mission>
//    <!-- 弹幕池上限，常见的1500、3000、8000 -->
//    <maxlimit>1500</maxlimit>
//    <source>k-v</source>

    public String chatserver = "danmu.ifeimo.com";
    public String chatid = "000001";
    public String mission = "0";
    public String maxlimit = "1500";
    public String source = "k-v";

    private List<DanmukuEntity> data = new ArrayList<>();

    public List<DanmukuEntity> getData() {
        return data;
    }

    public void setData(List<DanmukuEntity> data) {
        this.data = data;
    }

    public static DanmukuListEntity tranform(BulletList203Entity entity) {

        DanmukuListEntity danmukuListEntity = new DanmukuListEntity();
        if (entity == null || entity.getData() == null || entity.getData().size() == 0)
            return danmukuListEntity;
        for (Bullet bullet : entity.getData()) {
            DanmukuEntity danmukuEntity = new DanmukuEntity();
            danmukuEntity.setAppearTime(bullet.getVideo_node());
            danmukuEntity.getP();
            danmukuEntity.setText(bullet.getContent());
            danmukuListEntity.getData().add(danmukuEntity);
        }
        return danmukuListEntity;
    }
}
