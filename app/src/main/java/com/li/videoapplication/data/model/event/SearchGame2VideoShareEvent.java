package com.li.videoapplication.data.model.event;

import com.li.videoapplication.data.model.entity.Associate;
import com.li.videoapplication.framework.BaseEntity;

/**
 * 组件间的通讯事件：查找游戏 查找精彩生活
 */
@SuppressWarnings("serial")
public class SearchGame2VideoShareEvent extends BaseEntity {

    private Associate associate;

    private String gamaName;

    private String gamaId;

    private String groupId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGamaId() {
        return gamaId;
    }

    public void setGamaId(String gamaId) {
        this.gamaId = gamaId;
    }

    public String getGamaName() {
        return gamaName;
    }

    public void setGamaName(String gamaName) {
        this.gamaName = gamaName;
    }

    public Associate getAssociate() {
        return associate;
    }

    public void setAssociate(Associate associate) {
        this.associate = associate;
    }
}
