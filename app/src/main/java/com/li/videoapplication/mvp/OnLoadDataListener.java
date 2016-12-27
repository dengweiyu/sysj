package com.li.videoapplication.mvp;

/**
 * Created by pengzhipeng on 2016/11/23.
 */
public interface OnLoadDataListener<T> {
    void onSuccess(T data);
    void onFailure(Throwable e);
}
