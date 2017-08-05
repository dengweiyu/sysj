package com.li.videoapplication.framework;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.Base64;

import com.li.videoapplication.data.EventManager;
import com.li.videoapplication.data.cache.BaseUtils;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.response.Token;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.tools.JPushHelper;
import com.li.videoapplication.tools.ShareSDKLoginHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.utils.StringUtil;

/**
 * 功能：登录和注销
 *
 */
public class AppAccount {

	//保存在内存中的AccessToken 只在网络拦截器中使用 其他地方均从sharedPre中获取
	private static String sAccessToken = "";

	/**
	 * 功能：应用全局登录
	 */
	public static void login() {
		
		EventManager.postLoginEvent();
	}
	
	/**
	 * 功能：应用全局注销
	 */
	public static void logout() {
		
		PreferencesHepler.getInstance().clearAll();
		ShareSDKLoginHelper.removeAllAccount();
		EventManager.postLogoutEvent();

		// 设置友盟推送别名
		JPushHelper.removeAlias();
	}

	/**
	 *根据算法  加密
	 * @param params 需要加密的参数
	 * @param time 当前时间单位是秒
	 */
	public static String sign(String params,String time){
		String sign = "";
		try {
			sign = new String(Base64.encode(params.getBytes("UTF-8"),Base64.NO_WRAP));
			sign = BaseUtils.getMd5(sign+time);
			int index = Integer.parseInt(time.substring(time.length()-1,time.length()));
			sign = sign.substring(index,22);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sign;
	}

	public static String getAccessToken(){
		Member member = PreferencesHepler.getInstance().getUserProfilePersonalInformation();
		if (StringUtil.isNull(sAccessToken)){
			if (member != null && member.getSysj_token() != null){
				sAccessToken = member.getSysj_token().getAccess_token();
			}
		}
		return sAccessToken;
	}

	public static String getRefreshToken(){
		String refreshToken = "";
		Member member = PreferencesHepler.getInstance().getUserProfilePersonalInformation();
		if (member == null || member.getSysj_token() == null){
		}else {
			refreshToken = member.getSysj_token().getRefresh_token();
		}
		return refreshToken;
	}

	public static void saveAccessToken(Token data) {
		sAccessToken = data.getData().getAccess_token();
		Member member = PreferencesHepler.getInstance().getUserProfilePersonalInformation();
		if (member != null) {
			if (member.getSysj_token() == null) {
				member.setSysj_token(new Member.Token());
			}
			member.getSysj_token().setAccessTokenTime(System.currentTimeMillis());
			member.getSysj_token().setRefreshTokenTime(System.currentTimeMillis());
			member.getSysj_token().setAccess_token(data.getData().getAccess_token());
			member.getSysj_token().setRefresh_token(data.getData().getRefresh_token());
			member.getSysj_token().setExpires_in(data.getData().getExpires_in());
			member.getSysj_token().setToken_type(data.getData().getToken_type());
			//更新时间
			PreferencesHepler.getInstance().saveUserProfilePersonalInformation(member);
		}
	}
}
