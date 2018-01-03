package com.li.videoapplication.utils;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能：文本工具
 */
public class TextUtil {
    private static final String START = "start", END = "end", RESULT = "result";

    /**
     * 功能：TextView显示颜色HTML
     *
     * @param text
     * @param color "#00b4ff"
     * @return
     */
    public static String toColor(String text, String color) {
        return "<font color=\"" + color + "\">" + text + "</font>";
    }

    /**
     * 功能：TextView显示颜色斜体HTML
     *
     * @param text
     * @param color "#00b4ff"
     * @return
     */
    public static String toColorItalic(String text, String color) {
        return "<font color=\"" + color + "\" style=\"font-weight:bold;font-style:italic;\">" + text + "</font>";
    }

    /**
     * 功能：指定位置的字符串显示红色
     */
    public static SpannableString stringAtRed(String string, int start, int end) {
        if (StringUtil.isNull(string) || start < 0 || end >= string.length() || start >= end) {
            return null;
        }
        SpannableString spannableString = new SpannableString(string);

        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#ff3d2e"));
        spannableString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        return spannableString;
    }

    /**
     * 功能：非中文及标点显示红色
     *
     * @param string
     * @return
     */
    public static SpannableString dateAtRed(String string) {
        //非中文 非 。 ；  ， ： “ ”（ ） 、 ？ 《 》 这些标点符号
        final String pattern = "[^\\u4e00-\\u9fa5\\u3002\\uff1b\\uff0c\\uff1a\\u201c\\u201d\\uff08\\uff09\\u3001\\uff1f\\u300a\\u300b]";

        SpannableString spannableString = new SpannableString(string);

        ArrayList<Map<String, String>> lists = getStartEndResult(string, Pattern.compile(pattern));

        for (Map<String, String> str : lists) {
            ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#ff3d2e"));
            spannableString.setSpan(span, Integer.parseInt(str.get(START)),
                    Integer.parseInt(str.get(END)), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }

        return spannableString;
    }

    /**
     * 功能：数字显示颜色
     *
     * @param string
     * @param color  "#ff3d2e"--red
     * @return
     */
    public static SpannableString numberAtRed(String string, String color) {
        //数字
        final String pattern = "\\d*";

        SpannableString spannableString = new SpannableString(string);

        ArrayList<Map<String, String>> lists = getStartEndResult(string, Pattern.compile(pattern));

        for (Map<String, String> str : lists) {
            ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor(color));
            spannableString.setSpan(span, Integer.parseInt(str.get(START)),
                    Integer.parseInt(str.get(END)), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }

        return spannableString;
    }

    private static ArrayList<Map<String, String>> getStartEndResult(String text, Pattern pattern) {

        ArrayList<Map<String, String>> lists = new ArrayList<Map<String, String>>(0);
        //正则对象.匹配（数据源）
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            Map<String, String> map = new HashMap<String, String>(0);
            map.put(START, matcher.start() + "");
            map.put(END, matcher.end() + "");
            map.put(RESULT, matcher.group());
            lists.add(map);
        }
        return lists;
    }

    /**
     * 设置文本背景
     * @param txt
     * @param startP
     * @param endP
     * @param color
     */
    @Deprecated
    public static Spannable setBackgroundColorText(String txt,int startP,int endP,int color){
        SpannableString spanned = new SpannableString(txt);
        spanned.setSpan(new BackgroundColorSpan(color),startP,endP,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanned;
    }

    /**
     * 设置文本某段字体颜色
     * @param txt
     * @param startP
     * @param endP
     * @param color
     */
    @Deprecated
    public static Spannable setForegroundColorText(String txt,int startP,int endP,int color){
        return setForegroundColorText(new SpannableString(txt),startP,endP,color);
    }

    /**
     * 设置文本某段字体颜色
     * @param startP
     * @param endP
     * @param color
     */
    public static Spannable setForegroundColorText(Spannable spanned,int startP,int endP,int color){
        spanned.setSpan(new ForegroundColorSpan(color),startP,endP,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanned;
    }

    /**
     * 中划线，删除线
     * @return
     */
    public static Spannable setStrikethroughSpan(Spannable spanned,int startP,int endP){
        spanned.setSpan(new StrikethroughSpan(),startP,endP,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanned;
    }

    /**
     * 设置文本某个字体的大小
     * @param txt
     * @param startP
     * @param endP
     * @param dp
     */
    public static Spannable setSizeText(Spannable spanned,int startP,int endP,int dp){
        spanned.setSpan(new AbsoluteSizeSpan(ScreenUtil.dp2px(dp)), startP, endP, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanned;
    }
}
