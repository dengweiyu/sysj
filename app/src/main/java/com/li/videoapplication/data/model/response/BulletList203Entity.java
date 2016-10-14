package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.Bullet;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;


@SuppressWarnings("serial")
public class BulletList203Entity extends BaseResponseEntity {

    private List<Bullet> data;

    public List<Bullet> getData() {
        return data;
    }

    public void setData(List<Bullet> data) {
        this.data = data;
    }
}
