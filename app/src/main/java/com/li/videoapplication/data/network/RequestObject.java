package com.li.videoapplication.data.network;

import com.li.videoapplication.framework.BaseEntity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.li.videoapplication.framework.BaseResponseEntity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * 功能：网络请求对象
 */
@SuppressWarnings("serial")
public class RequestObject extends BaseEntity {
	
	//请求方法
	private int type;// POST/GET
	
	//请求地址
	private String url = Contants.DEFAULT_STRING;
	
	//发送的文本
	private String json = Contants.DEFAULT_STRING;
	
	//请求参数
	private Map<String, Object> params = new HashMap<>();
	
	//请求文件
	private Map<String, File> files = new HashMap<>();

	private String path = Contants.DEFAULT_STRING;

	//请求数据结构
	private BaseResponseEntity entity;

	// 是否退出当前页面取消请求
	private int a;

	public RequestObject() {
		super();
	}

	public RequestObject(int type, String url, Map<String, Object> params, Map<String, File> files) {
		super();
		setType(type);
		setUrl(url);
		setParams(params);
		setFiles(files);
		/*
		this.type = type;
		this.url = url;
		this.params = params;
		this.files = files;*/
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		if (url != null) {
			url = url.trim();
		}
		this.url = url;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		Map<String, Object> map = new HashMap<>();
		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				String key = Contants.DEFAULT_STRING;
				String value = Contants.DEFAULT_STRING;
				try {
					key = entry.getKey().trim();
					value = entry.getValue().toString().trim();
				} catch (Exception e) {
					e.printStackTrace();
				}
				map.put(key, value);
			}
		}
		this.params = map;
	}

	public Map<String, File> getFiles() {
		return files;
	}

	public void setFiles(Map<String, File> files) {
		this.files = files;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public BaseResponseEntity getEntity() {
		return entity;
	}

	public void setEntity(BaseResponseEntity entity) {
		this.entity = entity;
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}
}
