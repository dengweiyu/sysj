package com.li.videoapplication.data.model.entity;

import com.li.videoapplication.framework.BaseEntity;

import java.io.Serializable;

/**
 *
 */

public class IMPushCustomEntity extends BaseEntity {
    private String module;


    public static class Parameter implements Serializable{
        private String order_id;
        private String pkg_content;
        private int role;

    }
}
