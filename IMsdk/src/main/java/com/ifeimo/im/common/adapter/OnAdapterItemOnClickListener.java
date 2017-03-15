package com.ifeimo.im.common.adapter;

import com.ifeimo.im.common.bean.InformationBean;

/**
 * Created by lpds on 2017/3/4.
 */
public interface OnAdapterItemOnClickListener {


    /**
     * 此行的消息
     * @param informationBean
     *返回 TRUE 将会执行点击之后sdk的代码，返回FALSE则不会执行之后sdk的代码
     * @return
     */
    boolean onItemOnClick(InformationBean informationBean);



}
