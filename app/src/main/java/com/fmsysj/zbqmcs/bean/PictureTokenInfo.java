package com.fmsysj.zbqmcs.bean;

import java.util.ArrayList;

/** 图文信息token实体类 */
public class PictureTokenInfo {
	/** 接口返回结果 */
	String Result;
	/** key值 */
	ArrayList<String> keyList;
	/**token*/
	String token;
	/** 错误信息 */
	String MSG;
	public ArrayList<String> getKeyList() {
		return keyList;
	}

	public void setKeyList(ArrayList<String> keyList) {
		this.keyList = keyList;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}


	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}

	public String getResult() {
		return Result;
	}

	public void setResult(String result) {
		Result = result;
	}


}
