package com.li.videoapplication.data.preferences;

import java.util.Map;

import android.util.Log;

import com.li.videoapplication.data.network.Utils;
import com.li.videoapplication.utils.StringUtil;
/**
 * 功能：缓存网络访问数据
 */
public class RequestPreferences extends BasePreferences {
	
	private static final String NAME = "request";

    @Override
    protected String getName() {
        return NAME;
    }

    private static RequestPreferences instance;

    public static RequestPreferences getInstance() {
        if (instance == null) {
            synchronized (RequestPreferences.class) {
                if (instance == null) {
                    instance = new RequestPreferences();
                }
            }
        }
        return instance;
    }
	
	public void save(String url, Map<String, Object> params, String resultString) {
		if (StringUtil.isNull(url)) {
			throw new NullPointerException();
		}
		url = Utils.getNewUrl(url, params);
		putString(url, resultString);
		Log.d(tag, "save/url=" + url);
		Log.d(tag, "save/resultString=" + resultString);
	}
	
	public String get(String url, Map<String, Object> params) {
		if (StringUtil.isNull(url)) {
			throw new NullPointerException();
		}
		url = Utils.getNewUrl(url, params);
		String resultString = getString(url, "");
		Log.d(tag, "get/url=" + url);
		Log.d(tag, "get/resultString=" + resultString);
		return resultString;
	}
	
	public void remove(String url, Map<String, Object> params) {
		if (StringUtil.isNull(url)) {
			throw new NullPointerException();
		}
		url = Utils.getNewUrl(url, params);
		Log.d(tag, "remove/url=" + url);
		remove(url);
	}
}
