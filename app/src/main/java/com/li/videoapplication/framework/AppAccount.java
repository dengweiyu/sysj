package com.li.videoapplication.framework;

import com.li.videoapplication.data.EventManager;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.tools.JPushHelper;
import com.li.videoapplication.tools.ShareSDKLoginHelper;
/**
 * 功能：登录和注销
 *
 */
public class AppAccount {

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
}
