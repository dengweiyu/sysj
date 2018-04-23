package com.li.videoapplication.data.network;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 功能：网络请求任务（在主线程执行）
 */
public class RequestTarget {

	protected final String name = this.getClass().getName();

	protected final String simpleName = this.getClass().getSimpleName();

    private ResponseHandler handler;

    /**
     * 功能：网络访问返回实体
     */
    public BaseEntity postEntity(RequestObject request) {
        HttpClientMethod http = new HttpClientMethod();
        OkHttpMethod ok = new OkHttpMethod();
        ResponseObject response = null;
        try {
            if (request.getType() == Contants.TYPE_GET) {
                response = ok.execute(request);
            } else if (request.getType() == Contants.TYPE_POST) {
                response = ok.execute(request);
            } else if (request.getType() == Contants.TYPE_UPLOAD) {
                response = http.execute(request);
            } else if (request.getType() == Contants.TYPE_POST_JSON) {
                response = ok.execute(request);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        handler = new ResponseHandler(response);
        handler.handle();
        BaseEntity entity = handler.getEntity();
        return entity;
    }
}
