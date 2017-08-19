package com.li.videoapplication.utils;

import android.content.Context;
import android.nfc.Tag;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能：字符串工具
 */
public class StringUtil {

    /**
     * 字符串数字格式（上万x.xx万，其他千分位 x,xxx）
     */
    public static String toUnitW(String num) {
        try {
            double number = Double.valueOf(num);
            if (number < 10000) {
                return formatNum(num);

            } else if (number >= 10000 && number < 11000) {
                return "1万";

            } else {
                double n = number / 10000;
                DecimalFormat df = new DecimalFormat("#.00");
                return df.format(n) + "万";
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * 字符串转数字格式（三位一个逗号）
     */
    public static String formatNum(String num) {
        try {
            DecimalFormat df = new DecimalFormat("###,###");
            return df.format(Double.parseDouble(num));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * 格式化金额
     * 不带小数点
     */
    public static String formatMoney(float money){
        String  format ="";
        DecimalFormat decimalFormat=new DecimalFormat("##0");
        int m = (int)money;
        format = StringUtil.formatNum(m+"");
        String value = decimalFormat.format(money - (float) m)+"";
        format += value.substring(1,value.length());
        return format;
    }
    /**
     * 格式化金额
     * 一位小数点
     */
    public static String formatMoneyOnePoint(float money){
        String  format ="";
        DecimalFormat decimalFormat=new DecimalFormat("##0.0");
        int m = (int)money;
        format = StringUtil.formatNum(m+"");
        String value = decimalFormat.format(money - (float) m)+"";
        format += value.substring(1,value.length());
        return format;
    }

    /**
     * 字符串转整数
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 对象转整数
     *
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 字符串转布尔值
     *
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 判断字符串是否为空
     */
    public static boolean isNull(String str) {
        boolean isNull = false;
        if (str == null || str.length() == 0 || str.equals("null")) {
            isNull = true;
        }
        return isNull;
    }

    /**
     * 转换空字符串
     */
    public static String convert(String str) {
        if (str == null || str.length() == 0 || str.equals("null")) {
            str = "";
        }
        return str;
    }

    /**
     * 字符串替换
     */
    public static String replace(String strSource, String strFrom, String strTo) {

        String strDest = "";
        int intFromLen = strFrom.length();
        int intPos;

        if (strFrom.equals("")) {
            return strSource;
        }
        while ((intPos = strSource.indexOf(strFrom)) != -1) {
            strDest = strDest + strSource.substring(0, intPos);
            strDest = strDest + strTo;
            strSource = strSource.substring(intPos + intFromLen);
        }
        strDest = strDest + strSource;
        return strDest;
    }

    /**
     * 校验邮箱格式
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 校验手机格式  fixme 缺少177
     */
    public static boolean isMobileNumber(String mobiles) {

        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 方法1， 将数组转换为list， 然后使用list的contains方法来判断： Arrays.asList().contains()
     * 方法2，遍历数组判断：
     */
    public static <T> boolean contains(final T[] array, final T v) {
        for (final T e : array)
            if (e == v || v != null && v.equals(e))
                return true;

        return false;
    }

    /**
     * 判断字符串是否为合法的时间戳
     */
    public static boolean isValidDate(String str) {
        // yyyy-MM-dd_HH_mm_ss
        String regEx = "^\\d{4}-\\d{2}-\\d{2}_\\d{2}_\\d{2}_\\d{2}$";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);
        // 字符串是否与正则表达式相匹配
        return matcher.matches();
    }

    /**
     * Java文件操作 获取文件扩展名
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * Java文件操作 获取不带扩展名的文件名
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * 获取路径中的文件名 aaa/bbb/xxx.eee --> xxx
     */
    public static String getFileName(String path) {

        int start = path.lastIndexOf("/");
        int end = path.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return path.substring(start + 1, end);
        } else {
            return path;
        }
    }

    /**
     * 获取路径中的带拓展名的文件名 aaa/bbb/xxx.eee --> xxx.eee
     */
    public static String getFileNameWithExt(String path) {

        int start = path.lastIndexOf("/");
        if (start != -1) {
            return path.substring(start + 1, path.length());
        } else {
            return path;
        }
    }

    /**
     * @param cs
     * @param mContext
     * @return
     * @desc <pre>表情解析,转成unicode字符</pre>
     * @author Weiliang Hu
     * @date 2013-12-17
     */
    public static String convertToMsg(CharSequence cs, Context mContext) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(cs);
        ImageSpan[] spans = ssb.getSpans(0, cs.length(), ImageSpan.class);
        for (ImageSpan span : spans) {
            String c = span.getSource();
            int a = ssb.getSpanStart(span);
            int b = ssb.getSpanEnd(span);
            if (c.contains("[")) {
                ssb.replace(a, b, convertUnicode(c));
            }
        }
        ssb.clearSpans();
        return ssb.toString();
    }

    private static String convertUnicode(String emo) {
        emo = emo.substring(1, emo.length() - 1);
        if (emo.length() < 6) {
            return new String(Character.toChars(Integer.parseInt(emo, 16)));
        }
        String[] emos = emo.split("_");
        char[] char0 = Character.toChars(Integer.parseInt(emos[0], 16));
        char[] char1 = Character.toChars(Integer.parseInt(emos[1], 16));
        char[] emoji = new char[char0.length + char1.length];
        System.arraycopy(char0, 0, emoji, 0, char0.length);
        System.arraycopy(char1, 0, emoji, char0.length, emoji.length - char0.length);
        return new String(emoji);
    }

    public static String convert2Chinese(int count){
        StringBuffer buffer = new StringBuffer();
        if (count < 0){
            return "";
        }else {
            if (count <= 999){
                if (count <= 10){
                    buffer.append(mMapChinese.get(count+""));
                }else if (count <= 100){
                    buffer.append(count/10);
                    buffer.append(mMapChinese.get("10"));
                    buffer.append(count%10 == 0 ?"":mMapChinese.get(count%10));
                }else if (count <= 999){
                    buffer.append(count/100);
                    buffer.append(mMapChinese.get("100"));
                    buffer.append((count-100*(count/100))/10);
                    buffer.append(mMapChinese.get("10"));
                    buffer.append(count%10 == 0 ?"":mMapChinese.get(count%10));
                }
                return buffer.toString();
            }
        }
        return "";
    }

    private final static Map<String,String> mMapChinese;
    static {
        mMapChinese = Maps.newHashMap();
        mMapChinese.put("0","零");
        mMapChinese.put("1","一");
        mMapChinese.put("2","二");
        mMapChinese.put("3","三");
        mMapChinese.put("4","四");
        mMapChinese.put("5","五");
        mMapChinese.put("6","六");
        mMapChinese.put("7","七");
        mMapChinese.put("8","八");
        mMapChinese.put("9","九");
        mMapChinese.put("10","十");
        mMapChinese.put("100","百");
    }

    public static boolean isMatchMobile(String s) {
//        String telRegex = "13\\d{9}|14[57]\\d{8}|15[012356789]\\d{8}|18[012356789]\\d{8}|17[0678]\\d{8}";
        String telRegex = "1\\d{10}";

        return Pattern.matches(telRegex, s);
    }
}
