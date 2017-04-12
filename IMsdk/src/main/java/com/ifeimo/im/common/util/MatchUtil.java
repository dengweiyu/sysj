package com.ifeimo.im.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lpds on 2017/4/11.
 */
public class MatchUtil {

    public static final String MATCH_HEML = "(http://|ftp://|https://|www){0,1}[^一-龥\\s]*?\\.(com|net|cn|me|tw|fr)[^一-龥\\s]*";

    public static boolean isHtml(String htmlStr){
        // 编译正则表达式
        Pattern pattern = Pattern.compile(MATCH_HEML);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(htmlStr);
        // 查找字符串中是否有匹配正则表达式的字符/字符串
        return matcher.find();
    }

    public static String[] returnHtmlStr(String htmlStr){
        if(!isHtml(htmlStr)){
            return null;
        }else{
            try {
                Pattern pattern = Pattern.compile(MATCH_HEML);
                Matcher matcher = pattern.matcher(htmlStr);

                String[] htmls = new String[matcher.groupCount()];

                int i = 0;
                while(matcher.find()){
                    htmls[i] = matcher.group(0);
                    i++;
                }

                return htmls;
            }catch (Exception ex){
                ex.printStackTrace();
                return null;
            }
        }
    }

}
