package com.fmsysj.zbqmcs.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Helpers {

    protected static final String name = Helpers.class.getName();
    protected static final String simpleName = Helpers.class.getSimpleName();

	/**
	 * 获取后台ROOT提示权限是否开启
	 *
	 * @param channelName
	 *            渠道名称
	 * @return
	 */

	public static boolean getRootNotify(String channelName, Context mContext) {

		/**
		 * 增加弹出框数据
		 *
		 * http://apps.ifeimo.com/home/sys/addSysAlert.html?alert_name=
		 * yingyongbao
		 *
		 * 弹出框列表数据 http://apps.ifeimo.com/home/sys/sysAlert.html?alert_name=
		 * yingyongbao
		 *
		 * 修改弹出框数据 http://apps.ifeimo.com/home/sys/upSysAlert.html?alert_name=
		 * yingyongbao
		 *
		 */
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
		// 判断本地的root开关是否已经开启
		boolean isRootNotify = sp.getBoolean("getRootNotifyFor" + channelName, false);
		if (isRootNotify) {
			return true;
		} else {
			// 获取后台root开关
			String url = "http://apps.ifeimo.com/home/sys/sysAlert.html?alert_name=";
			String json = httpGet(url + channelName);
			try {
				JSONObject object = new JSONObject(json);
				String result = object.getString("result");
				if (result.equals("true")) {
					JSONArray arrayList = object.getJSONArray("data");
					JSONObject temp = (JSONObject) arrayList.get(0);
					String display = (String) temp.get("displayText");
					if (display.equals("1")) {// 1为开启，0为不开启
						// 修改本地root提示开关
						sp.edit().putBoolean("getRootNotifyFor" + channelName, true).commit();
						return true;
					} else {
						return false;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return true;
			}
			return true;
		}
	}

    /**
     * get请求
     */
    public static String httpGet(String url) {
        String str = "";
        HttpClient httpClient = null;
        HttpGet request;
        try {
            request = new HttpGet(url);
            httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                str = EntityUtils.toString(response.getEntity());
            }
            System.out.println(simpleName + "/url=" + url);
            System.out.println(simpleName + "/result=" + str);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null)
                httpClient.getConnectionManager().shutdown();
        }
        return str;
    }
}
