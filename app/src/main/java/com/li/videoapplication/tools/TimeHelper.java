package com.li.videoapplication.tools;


import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.li.videoapplication.data.database.VideoCaptureManager;
import com.li.videoapplication.data.image.VideoDuration;
import com.li.videoapplication.data.image.VideoTimeLoader;
import com.li.videoapplication.data.local.FileUtil;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.ui.toast.ToastHelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

@SuppressLint("SimpleDateFormat")
public class TimeHelper {

    public static final String TAG = TimeHelper.class.getSimpleName();

    /**
     * 返回当前时间
     */
    public static long getCurrentTime() throws Exception {
        //获取系统时间的10位的时间戳
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 返回延后20分钟时间
     *
     * @param startTime 开始时间戳：秒数
     * @return HH:mm
     */
    public static String after20minFormat(String startTime) throws Exception {
        String format = "HH:mm";
        Long l = Long.valueOf(startTime);
        Date date = new Date(l * 1000L);

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, 20);

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String s = sdf.format(c.getTimeInMillis());
        System.out.println(s);
        return s;
    }

    /**
     * 返回提前半小时时间
     *
     * @param startTime 开始时间戳：秒数
     * @return HH:mm
     */
    public static String aheadHalfHourFormat(String startTime) throws Exception {
        String format = "HH:mm";
        Long l = Long.valueOf(startTime);
        Date date = new Date(l * 1000L);

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, -30);

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String s = sdf.format(c.getTimeInMillis());
        System.out.println(s);
        return s;
    }

    /**
     * 返回时间
     *
     * @param time 时间戳：秒数
     * @return MM-dd HH:mm
     */
    public static final String getMMddHHmmTimeFormat(String time) throws Exception {
        String format = "MM月dd日 HH:mm";
        Long l = Long.valueOf(time);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date(l * 1000L);
        String s = sdf.format(date);
        System.out.println(s);
        return s;
    }

    /**
     * 返回时间
     *
     * @param time 时间戳：秒数
     * @return MM-dd HH:mm
     */
    public static final String getMMddHHmmTimeFormat2(String time) throws Exception {
        String format = "MM-dd HH:mm";
        Long l = Long.valueOf(time);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date(l * 1000L);
        String s = sdf.format(date);
        System.out.println(s);
        return s;
    }


    /**
     * 返回时间
     *
     * @param time 时间戳：秒数
     * @return yyyy-MM-dd HH:mm
     */
    public static final String getWholeTimeFormat(String time) throws Exception {
        String format = "yyyy-MM-dd HH:mm";
        Long l = new Long(time);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date(l * 1000L);
        String s = sdf.format(date);
        System.out.println(s);
        return s;
    }


    /**
     * 返回时间
     *
     * @param time 时间戳：秒数
     * @return yyyy-MM-dd
     */
    public static final String getTimeFormat(String time) throws Exception {
        String format = "yyyy-MM-dd";
        Long l = new Long(time);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date(l * 1000L);
        String s = sdf.format(date);
        System.out.println(s);
        return s;
    }

    /**
     * 返回时间
     *
     * @param time 时间戳：秒数
     * @return MM-dd
     */
    public static final String getTime2MdFormat(String time) throws Exception {
        String format = "MM-dd";
        Long l = new Long(time);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date(l * 1000L);
        String s = sdf.format(date);
        System.out.println(s);
        return s;
    }

    /**
     * 返回时间
     *
     * @param time 时间戳：秒数
     * @return HH:mm
     */
    public static final String getTime2HmFormat(String time) throws Exception {
        String format = "HH:mm";
        Long l = new Long(time);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date(l * 1000L);
        String s = sdf.format(date);
        System.out.println(s);
        return s;
    }

    /**
     * 返回系统消息时间
     *
     * @param time 时间戳：秒数
     * @return yyyy-MM-dd
     */
    public static final String getSysMessageTime(String time) throws Exception {
        String format = "yyyy-MM-dd";
        Long l = new Long(time);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date(l * 1000L);
        String s = sdf.format(date);
        System.out.println(s);
        return s;
    }

    /**
     * 返回视频消息时间
     *
     * @param time 时间戳：秒数
     */
    @SuppressWarnings({"deprecation", "unused"})
    public static final String getMyMessageTime(String time) throws Exception {
        Long l = new Long(time);
        Date originalDate = new Date(l * 1000L);
        int originalYear = originalDate.getYear();
        int originalMonth = originalDate.getMonth();
        int originalDay = originalDate.getDay();
        int originalHour = originalDate.getHours();
        int originalMinute = originalDate.getMinutes();
        int originalSecond = originalDate.getSeconds();
        try {
            Log.d(TAG, "getMyMessageTime: originalDate == " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(originalDate));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Date nowDate = new Date(System.currentTimeMillis());
        int nowYear = nowDate.getYear();
        int nowMonth = nowDate.getMonth();
        int nowDay = nowDate.getDay();
        int nowHour = nowDate.getHours();
        int nowMinute = nowDate.getMinutes();
        int nowSecond = nowDate.getSeconds();
        try {
            Log.d(TAG, "getMyMessageTime: currentTime == " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(nowDate));
        } catch (Exception e) {
            e.printStackTrace();
        }

        int differYear = nowYear - originalYear;
        int differMonth = nowMonth - originalMonth;
        int differDay = nowDay - originalDay;
        int differHour = nowHour - originalHour;
        int differMinute = nowMinute - originalMinute;
        int differSecond = nowSecond - originalSecond;

        long lo = nowDate.getTime() - originalDate.getTime();
        long day = lo / (24 * 60 * 60 * 1000);
        long hour = (lo / (60 * 60 * 1000) - day * 24);
        long minute = ((lo / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long second = (lo / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60);
        System.out.println("相差时间：" + day + "天" + hour + "小时" + minute + "分" + second + "秒");

        // 同一天
        if (differYear == 0 && differMonth == 0 && differDay == 0) {
            Log.d(TAG, "getMyMessageTime: sameDay == " + new SimpleDateFormat("HH:mm").format(originalDate));
            return new SimpleDateFormat("HH:mm").format(originalDate);
        }

        // 同一年
        if (differYear == 0) {
            Log.d(TAG, "getMyMessageTime: sameYear == " + new SimpleDateFormat("MM-dd").format(originalDate));
            return new SimpleDateFormat("MM-dd").format(originalDate);
        }

        return new SimpleDateFormat("yyyy-MM").format(originalDate);
    }

    /**
     * 返回视频图文更新時間
     *
     * @param time
     * @return
     */
    @SuppressWarnings("deprecation")
    public static final String getVideoImageUpTime(String time) throws Exception {

        Long l = Long.valueOf(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date originalDate = new Date(l * 1000L);
        String originalTime = format.format(originalDate);
        System.out.println(originalTime);
        int originalYear = originalDate.getYear();
        int originalMonth = originalDate.getMonth();
        int originalDay = originalDate.getDay();
        int originalHour = originalDate.getHours();
        int originalMinute = originalDate.getMinutes();
        int originalSecond = originalDate.getSeconds();

        Date nowDate = new Date(System.currentTimeMillis());
        String nowTime = format.format(nowDate);
        System.out.println(nowTime);
        int nowYear = nowDate.getYear();
        int nowMonth = nowDate.getMonth();
        int nowDay = nowDate.getDay();
        int nowHour = nowDate.getHours();
        int nowMinute = nowDate.getMinutes();
        int nowSecond = nowDate.getSeconds();

        int differYear = nowYear - originalYear;
        int differMonth = nowMonth - originalMonth;
        int differDay = nowDay - originalDay;
        int differHour = nowHour - originalHour;
        int differMinute = nowMinute - originalMinute;
        int differSecond = nowSecond - originalSecond;

        long lo = nowDate.getTime() - originalDate.getTime();
        long day = lo / (24 * 60 * 60 * 1000);
        long hour = (lo / (60 * 60 * 1000) - day * 24);
        long minute = ((lo / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long second = (lo / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60);
        System.out.println("相差时间：" + day + "天" + hour + "小时" + minute + "分" + second + "秒");

        if (day == 0) {
            if (hour == 0) {// 多少分钟以內
                if (minute == 0) {// 刚刚
                    return "刚刚";
                } else {
                    return minute + "分钟前";
                }
            } else {// 多少小时以内
                return hour + "小时前";
            }
        } else {// 多少天前
            if (day < 7) {
                return day + "天前";
            } else {
                return originalTime;
            }
        }
    }

    /**
     * 返回播放时间
     *
     * @param time
     * @return ##:##
     */
    public static String getVideoPlayTime(int time) {
        int mi = 1 * 60;
        int hh = mi * 60;

        long hour = (time) / hh;
        long minute = (time - hour * hh) / mi;
        long second = time - hour * hh - minute * mi;

        String strHour = hour < 10 ? "0" + hour : "" + hour;
        String strMinute = minute < 10 ? "0" + minute : "" + minute;
        String strSecond = second < 10 ? "0" + second : "" + second;
        if (hour > 0) {
            return strHour + ":" + strMinute + ":" + strSecond;
        } else {
            return strMinute + ":" + strSecond;
        }
    }

    /**
     * 返回播放时间
     *
     * @param time
     * @return ##:##
     */
    public static String getVideoPlayTime(long time) {
        int mi = 1 * 60;
        int hh = mi * 60;

        long hour = (time) / hh;
        long minute = (time - hour * hh) / mi;
        long second = time - hour * hh - minute * mi;

        String strHour = hour < 10 ? "0" + hour : "" + hour;
        String strMinute = minute < 10 ? "0" + minute : "" + minute;
        String strSecond = second < 10 ? "0" + second : "" + second;
        if (hour > 0) {
            return strHour + ":" + strMinute + ":" + strSecond;
        } else {
            return strMinute + ":" + strSecond;
        }
    }

    /**
     * 返回播放时间
     *
     * @param
     * @return ##:##
     */
    public static String getVideoPlayTime(String timeString) {
        return getVideoPlayTime(Integer.valueOf(timeString));
    }

    /**
     * 返回视频时间长度
     *
     * @param record
     * @return mm:ss
     */
    public static String getVideoImageTimeLength(final VideoImage record) throws Exception {

        int a = Integer.valueOf(record.getTime_length());
        int minute = a / 60;
        int second = a - minute * 60;
        String minuteString = String.valueOf(minute);
        String secondString = String.valueOf(second);
        int length = 2; //指定位数，可以是任意长度
        while (minuteString.length() < length) {
            minuteString = "0" + minuteString;
        }
        while (secondString.length() < length) {
            secondString = "0" + minuteString;
        }
        return minuteString + ":" + secondString;
    }

    /**
     * 分享时检查视频长度及安全性
     */
    public static boolean checkVideoInfo(String filePath) {
        // 检查文件是否存在
        File file = null;
        try {
            file = new File(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file == null || !file.exists()) {
            ToastHelper.s("视频文件不存在");
            // 删除该视频
            VideoCaptureManager.deleteByPath(filePath);
            return false;
        }
        String duration;
        try {
            duration = VideoDuration.getDuration(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            ToastHelper.s("视频存在错误，不能分享");
            return false;
        }
        // 获取视频长度（毫秒）
        long secs = 0;
        try {
            secs = Integer.valueOf(duration);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (secs < 10 * 1000) {// 10 000
            ToastHelper.s("分享视频长度不能少于10秒");
            return false;
        } else if (secs > 30 * 60 * 1000) {// 1800 000
            ToastHelper.s("分享视频长度不能大于30分钟");
            return false;
        }
        // 计算视频文件的大小
        long fileLong = FileUtil.getFileSize(filePath);
        if ((fileLong / 1048576) > 800) {
            ToastHelper.s("分享视频长度不能大于800M");
            return false;
        }
        return true;
    }

    /**
     * 判断广告时间是否有效
     *
     * @param startTime 时间戳，秒 1462584600
     * @param endTime   时间戳，秒 1463104800
     */
    public static boolean isBannerValid(long startTime, long endTime) {
        Log.d(TAG, "--------------------------------------------------------");
        Log.d(TAG, "isBannerValid: startTime=" + startTime);
        Log.d(TAG, "isBannerValid: endTime=" + endTime);
        long nowTime = System.currentTimeMillis() / 1000;
        Log.d(TAG, "isBannerValid: nowTime=" + nowTime);
        if (nowTime >= startTime && nowTime <= endTime) {
            Log.d(TAG, "isBannerValid: true");
            return true;
        }
        Log.d(TAG, "isBannerValid: false");
        return false;
    }
}
