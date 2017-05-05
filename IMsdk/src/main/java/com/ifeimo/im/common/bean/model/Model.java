package com.ifeimo.im.common.bean.model;

import y.com.sqlitesdk.framework.interface_model.IModel;

/**
 * Created by lpds on 2017/4/20.
 */
public abstract class Model<T> implements IModel<T> {

    @Override
    public T clone() {
        try {
            return (T) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
