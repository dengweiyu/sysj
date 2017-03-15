package com.ifeimo.im.framwork.database.iduq;

import java.util.List;

/**
 * Created by lpds on 2017/1/11.
 */
public interface OnQureListener<T> {

    void quer(T t);

    void nullQuer();

}
