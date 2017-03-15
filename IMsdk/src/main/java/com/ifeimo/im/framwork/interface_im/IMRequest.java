package com.ifeimo.im.framwork.interface_im;

import com.ifeimo.im.IEmployee;
import com.ifeimo.im.common.bean.RequestTag;
import com.zhy.http.okhttp.builder.HasParamsable;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.List;
import java.util.Map;

/**
 * Created by lpds on 2017/2/13.
 */
public interface IMRequest extends IEmployee{

    void init();
//    void saveQueue(String key);
    void convertParameter(Map<String, String> para, HasParamsable hasParamsable);
    Map<String, RequestTag> getRequestQueue();
}
