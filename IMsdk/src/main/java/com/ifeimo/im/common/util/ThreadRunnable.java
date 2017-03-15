package com.ifeimo.im.common.util;

/**
 * Created by lpds on 2017/2/23.
 */
@Deprecated
public abstract class ThreadRunnable implements Runnable {


    public int count = 0;

    public ThreadRunnable(int count) {
        this.count = count;
    }

    public ThreadRunnable() {
    }

    public ThreadRunnable setCount(int count) {
        this.count = count;
        return this;
    }
}
