package com.li.videoapplication.data.network;

import org.json.JSONException;
import org.json.JSONObject;

import com.li.videoapplication.utils.StringUtil;

/**
 * 功能：网络请求响应对象
 */
@SuppressWarnings("serial")
public class ResponseObject extends RequestObject {

	//HTTP返回码
	private int statusCode;
	
	//返回的String
	private String resultString = Contants.DEFAULT_STRING;
	
	//是否正确返回
	private boolean result;
	
	//返回的结果提示
	private String msg = Contants.DEFAULT_STRING;
	
	//返回的结果
	private String data = Contants.DEFAULT_STRING;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
		
		if (!StringUtil.isNull(resultString)) {
			try {
				JSONObject obj = new JSONObject(resultString);
				boolean result = obj.optBoolean("result", false);
				String msg = obj.optString("msg", null);
				String data = obj.optString("data", null);
				
				setResult(result);
				setMsg(msg);
				setData(data);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isResult() {
		return result;
	}

	public String getMsg() {
		return msg;
	}

	public String getData() {
		return data;
	}

	private void setResult(boolean result) {
		this.result = result;
	}

	private void setMsg(String msg) {
		this.msg = msg;
	}

	private void setData(String data) {
		this.data = data;
	}
}
