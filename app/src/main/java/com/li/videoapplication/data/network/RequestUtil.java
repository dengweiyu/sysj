package com.li.videoapplication.data.network;

import android.graphics.Bitmap;
import android.util.Log;

import com.li.videoapplication.utils.StreamUtil;
import com.li.videoapplication.utils.URLUtil;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class RequestUtil {

    private static final String TAG = RequestUtil.class.getSimpleName();

    // ----------------------------------------------------------------------------

    public static String getString(String url, Map<String, Object> params) {
        InputStream inputStream = getStream(url, params);
        return StreamUtil.toStringUTF8(inputStream);
    }

    public static String getString(String url) {
        return getString(url, null);
    }

    public static Bitmap getBitmap(String url, Map<String, Object> params) {
        InputStream inputStream = getStream(url, params);
        return StreamUtil.toBitmap(inputStream);
    }

    public static Bitmap getBitmap(String url) {
        return getBitmap(url, null);
    }

    public static InputStream getStream(String url) {
        return getStream(url, null);
    }

    public static InputStream getStream(String url, Map<String, Object> params) {
        Log.d(TAG, "getStream: // ------------------------------------");
        Log.d(TAG, "getStream: url=" + url);
        Log.d(TAG, "getStream: params=" + params);
        if (url == null)
            return null;
        if (!URLUtil.isURL(url))
            return null;
        if (params != null && params.size() > 0) {
            url = Utils_Network.getNewUrl(url, params);
        }
        Log.d(TAG, "getStream: newUrl=" + url);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = null;
        try {
            response = RequestClient.getOkHttpClient().newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response != null) {
            InputStream is = null;
            try {
                is = response.body().byteStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (is != null) {
                return is;
            }
        }
        return null;
    }
}
