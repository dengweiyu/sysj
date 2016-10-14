package com.li.videoapplication.utils;

import java.net.MalformedURLException;
import java.net.URL;
/**
 * 功能：URL工具
 */
public class URLUtil {
	
	/**
	 * 功能：URL是否合法
	 */
	public static boolean isURL(String url) {
		
		try {
		     URL mURL = new URL(url);
		     System.out.println( "url 正确");
		     return true;
		} catch (MalformedURLException e) {
		     System.out.println( "url 不可用");
		     return false;
		}
	}
}
