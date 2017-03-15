package com.li.videoapplication.tools;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.li.videoapplication.data.database.VideoCaptureManager;
import com.li.videoapplication.data.image.VideoDuration;
import com.li.videoapplication.data.local.FileUtil;
import com.li.videoapplication.data.model.entity.VideoImage;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

@SuppressLint("SimpleDateFormat")
public class TimeHelper {

    public static final String TAG = TimeHelper.class.getSimpleName();

    public interface RunAfter {
        void runAfter();
    }

    /**
     * time毫秒后执行
     */
    public static Timer runAfter(final RunAfter callback, long time) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        callback.runAfter();
                    }
                });
            }
        };
        timer.schedule(task, time);
        return timer;
    }


    /**
     * 判断两个时间戳是否为同一天
     */
    public static boolean isSameDay(long time1, long time2) throws Exception {
        Calendar c1 = Calendar.getInstance();
        Date date = new Date(time1 * 1000L);
        c1.setTime(date);

        Calendar c2 = Calendar.getInstance();
        Date date2 = new Date(time2 * 1000L);
        c2.setTime(date2);

        if (c1.get(Calendar.YEAR) == (c2.get(Calendar.YEAR))) {
            if (c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 输入时间戳
     * 返回周
     */
    public static String getWeek(String time) throws Exception {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        Date date = null;
        int mydate = 0;
        String week = null;

        date = sdr.parse(times);
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        mydate = cd.get(Calendar.DAY_OF_WEEK);
        // 获取指定日期转换成星期几
        if (IsToday(Long.valueOf(time))) {
            return "今天";
        } else if (IsYesterday(Long.valueOf(time))) {
            return "昨天";
        } else if (mydate == 1) {
            week = "周日";
        } else if (mydate == 2) {
            week = "周一";
        } else if (mydate == 3) {
            week = "周二";
        } else if (mydate == 4) {
            week = "周三";
        } else if (mydate == 5) {
            week = "周四";
        } else if (mydate == 6) {
            week = "周五";
        } else if (mydate == 7) {
            week = "周六";
        }
        return week;
    }

    /**
     * 判断是否为今天
     */
    public static boolean IsToday(long time) throws Exception {
        Calendar currentCal = Calendar.getInstance();
        Date date = new Date(System.currentTimeMillis());
        currentCal.setTime(date);

        Calendar c = Calendar.getInstance();
        Date date2 = new Date(time * 1000L);
        c.setTime(date2);

        if (c.get(Calendar.YEAR) == (currentCal.get(Calendar.YEAR))) {
            int diffDay = currentCal.get(Calendar.DAY_OF_YEAR)
                    - c.get(Calendar.DAY_OF_YEAR);
            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为昨天
     */
    public static boolean IsYesterday(long time) throws Exception {

        Calendar currentCal = Calendar.getInstance();
        Date date = new Date(System.currentTimeMillis());
        currentCal.setTime(date);

        Calendar c = Calendar.getInstance();
        Date date2 = new Date(time * 1000L);
        c.setTime(date2);

        if (currentCal.get(Calendar.YEAR) == (c.get(Calendar.YEAR))) {
            int diffDay = currentCal.get(Calendar.DAY_OF_YEAR)
                    - c.get(Calendar.DAY_OF_YEAR);

            if (diffDay == -1) {
                return true;
            }
        }
        return false;
    }

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
     * @return MM月dd日
     */
    public static final String getMMddTimeFormat(String time) throws Exception {
        String format = "MM月dd日";
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
     * @return MM月dd日 HH:mm
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
     * @return yyyy年MM月dd日 HH:mm
     */
    public static final String getWholeTimeFormat_Str(String time) throws Exception {
        String format = "yyyy年MM月dd日 HH:mm";
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

    // 检查视频文件是否存在
    public static boolean checkVideoExists(String filePath) {
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
        return true;
    }

    // 检查视频文件时长，大小是否符合分享要求（要求：10s<分享时长<30min，大小<800M）
    public static boolean checkVideoDuration(String filePath) {
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
     * 分享时检查视频长度及安全性
     */
    public static boolean checkVideoInfo(String filePath) {

        // 检查文件是否存在
        boolean isExists = checkVideoExists(filePath);
        if (!isExists) return false;

        //检查是否符合分享要求，符合则全部检查完成返回true，不符合返回false
        return checkVideoDuration(filePath);
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
