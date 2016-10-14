package com.li.videoapplication.data.network;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    /**
     * 功能：转化ResponseObject
     */
    public static ResponseObject newResponseObject(RequestObject requestObject) {
        ResponseObject responseObject = new ResponseObject();
        responseObject.setUrl(requestObject.getUrl());
        responseObject.setParams(requestObject.getParams());
        responseObject.setFiles(requestObject.getFiles());
        responseObject.setType(requestObject.getType());
        responseObject.setEntity(requestObject.getEntity());
        return responseObject;
    }

    /**
     * 功能：拼接Url和参数
     */
    public static String getNewUrl(final String url, final Map<String, Object> params) {
        if (url == null || url.length() == 0 || url.equals("null")) {
            throw new NullPointerException();
        }
        if (params != null && params.size() > 0) {
            List<NameValuePair> pairs = getPairs(params);
            String parameters = URLEncodedUtils.format(pairs, "UTF-8");
            if (parameters != null && parameters.length() > 0 && !parameters.equals("null")) {
                return url + "?" + parameters;
            }
        }
        return url;
    }

    /**
     * 功能：转化参数
     */
    public static Map<String, String> getMap(Map<String, Object> params) {
        Map<String, String> map = new HashMap<>();
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String key = "";
                String value = "";
                try {
                    key = entry.getKey().trim();
                    value = entry.getValue().toString().trim();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                NameValuePair pair = new BasicNameValuePair(key, value);
                map.put(key, value);
            }
        }
        return map;
    }

    /**
     * 功能：转化参数
     */
    public static List<NameValuePair> getPairs(Map<String, Object> map) {
        List<NameValuePair> params = new ArrayList<>();
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = "";
                String value = "";
                try {
                    key = entry.getKey().trim();
                    value = entry.getValue().toString().trim();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                NameValuePair pair = new BasicNameValuePair(key, value);
                params.add(pair);
            }
        }
        sortPairs(params);
        return params;
    }

    /**
     * 排序参数
     */
    private static void sortPairs(List<NameValuePair> pairs) {
        for (int i = 1; i < pairs.size(); i++) {
            for (int j = i; j > 0; j--) {
                NameValuePair a = pairs.get(j - 1);
                NameValuePair b = pairs.get(j);
                if (compare(a.getName(), b.getName())) {
                    // 互换
                    /*String name = a.getName();
                    String value = a.getValue();

                    a.setName(b.getName());
                    a.setValue(b.getValue());

                    b.setName(name);
                    b.setValue(value);*/
                    NameValuePair e = new BasicNameValuePair(a.getName(), a.getValue());
                    NameValuePair f = new BasicNameValuePair(b.getName(), b.getValue());
                    pairs.set(j - 1, f);
                    pairs.set(j, e);
                }
            }
        }
    }

    /**
     * 返回true说明str1大，返回false说明str2大
     */
    private static boolean compare(String a, String b) {
        String a1 = a.toUpperCase();
        String b1 = b.toUpperCase();
        boolean flag = true;
        int minLen = 0;
        if (b.length() < b.length()) {
            minLen = b.length();
            flag = false;
        } else {
            minLen = b.length();
            flag = true;
        }
        for (int index = 0; index < minLen; index++) {
            char a2 = a1.charAt(index);
            char b2 = b1.charAt(index);
            if (a2 != b2) {
                if (a2 > b2) {
                    return true; // a大
                } else {
                    return false; // b大
                }
            }
        }
        return flag;
    }

    /**
     * 功能：转化文件
     */
    public static FormFile[] getFormFiles(Map<String, File> files) {
        if (files == null || files.size() == 0) {
            return new FormFile[] { null };
        } else {
            FormFile[] formFiles = new FormFile[files.size()];
            int i = 0;
            for (Map.Entry<String, File> entry : files.entrySet()) {
                String key = entry.getKey();
                File value = entry.getValue();
                String filname = "key" + "_" + i;
                String contentType = "application/octet-stream";
                formFiles[i] = new FormFile(filname, value, key, contentType);
            }
            return formFiles;
        }
    }

    /**
     * 功能：取出一项
     */
    public static FormFile getFormFile(Map<String, File> files) {
        FormFile formFile = null;
        if (files != null && files.size() > 0) {
            int i = 0;
            for (Map.Entry<String, File> entry : files.entrySet()) {
                String key = entry.getKey();
                File value = entry.getValue();
                String filname = "key" + "_" + i;
                String contentType = "application/octet-stream";
                formFile = new FormFile(filname, value, key, contentType);
                break;
            }
        }
        return formFile;
    }
}
