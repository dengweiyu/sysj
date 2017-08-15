package com.ifeimo.im.framwork.notification;

import android.content.Intent;

import com.ifeimo.im.common.bean.model.HeadLineModel;

/**
 * Created by linhui on 2017/8/8.
 */
public interface NotifyHeadLineObservable {

    Intent subscribe(HeadLineModel headLineModel);

}
