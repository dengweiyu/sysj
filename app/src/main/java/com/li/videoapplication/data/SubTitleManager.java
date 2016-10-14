package com.li.videoapplication.data;


import com.li.videoapplication.data.model.response.SrtList203Entity;
import com.li.videoapplication.data.model.response.SrtUpload203Entity;
import com.li.videoapplication.data.network.Contants;
import com.li.videoapplication.data.network.RequestHelper;
import com.li.videoapplication.data.network.RequestObject;
import com.li.videoapplication.data.network.RequestParams;
import com.li.videoapplication.data.network.RequestUrl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 字幕
 */
public final class SubTitleManager {

    /**
     * 下載字幕
     */
    public static final void download(String url) {
        RequestHelper helper = new RequestHelper();
        RequestObject request = new RequestObject(Contants.TYPE_DOWNLOAD, url, null, null);
        request.setEntity(null);
        helper.doService(request);
    }

    /**
     * 上传字幕（同步）
     */
    public static final SrtUpload203Entity srtUpload203Sync(String video_id, File file) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().srtUpload203();
        Map<String, Object> params = RequestParams.getInstance().srtUpload203(video_id);
        Map<String, File> files = new HashMap<>();
        files.put("srt", file);
        RequestObject request = new RequestObject(Contants.TYPE_UPLOAD, url, params, files);
        request.setEntity(new SrtUpload203Entity());
        SrtUpload203Entity entity = (SrtUpload203Entity) helper.postEntity(request);
        return entity;
    }

    /**
     * 上传字幕
     */
    public static final void srtUpload203(String video_id, File file) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().srtUpload203();
        Map<String, Object> params = RequestParams.getInstance().srtUpload203(video_id);
        Map<String, File> files = new HashMap<>();
        files.put("srt", file);
        RequestObject request = new RequestObject(Contants.TYPE_UPLOAD, url, params, files);
        helper.doService(request);
    }

    /**
     * 获取字幕
     */
    public static final void srtList203(String video_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().srtList203();
        Map<String, Object> params = RequestParams.getInstance().srtList203(video_id);
        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new SrtList203Entity());
        helper.doService(request);
    }
}
