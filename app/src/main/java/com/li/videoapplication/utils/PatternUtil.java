package com.li.videoapplication.utils;

import java.util.regex.Pattern;

/**
 * 功能：匹配工具
 */
public class PatternUtil {

    /**
     * 功能：判断用户名 不能为中文,并且是是手机号码或者是邮箱
     *
     * @param s
     * @return
     */
    public static boolean matchUsername(String s) {
        if (Pattern.matches("[0-9a-zA-Z]{6,18}", s)) {// 6-18位
            if (isMatchMobile(s) || isMatchEmail(s)) {// 必须是手机
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 功能：判断密码，6-18位
     */
    public static boolean matchPassword(String s) {
        return Pattern.matches("[0-9a-zA-Z]{6,18}", s);
    }

    /**
     * 功能：校验手机号码
     */
//	public static boolean isMatchMobile(String s) {
//		return Pattern.matches("^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}", s);
//	}
    public static boolean isMatchMobile(String s) {
//        String telRegex = "13\\d{9}|14[57]\\d{8}|15[012356789]\\d{8}|18[012356789]\\d{8}|17[0678]\\d{8}";
        String telRegex = "1\\d{10}";

        return Pattern.matches(telRegex, s);
    }

    /**
     * 功能：检验是否是数字
     *
     * @param s
     * @return
     */
    public static boolean isMatchNumber(String s) {
        return Pattern.matches("\\d*", s);
    }

    /**
     * 功能：匹配邮箱
     *
     * @param s
     * @return
     */
    public static boolean isMatchEmail(String s) {
        return Pattern.matches("/^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$/;", s);
    }
}
