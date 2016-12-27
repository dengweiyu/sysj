package com.li.videoapplication.data.Retrofit;

/**
 * Created by pengzhipeng on 2016/11/23.
 */
public interface SubscriberOnNextListener<T> {
    void onNext(T t);
    void onError(Throwable e);
    void onCompleted();
}
