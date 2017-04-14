package com.li.videoapplication.tools;

import android.util.Log;

import com.li.videoapplication.ui.srt.InvalidSRTException;
import com.li.videoapplication.ui.srt.SRT;
import com.li.videoapplication.ui.srt.SRTComparators;
import com.li.videoapplication.ui.srt.SRTEditor;
import com.li.videoapplication.ui.srt.SRTInfo;
import com.li.videoapplication.ui.srt.SRTReader;
import com.li.videoapplication.ui.srt.SRTReaderException;
import com.li.videoapplication.ui.srt.SRTTimeFormat;
import com.li.videoapplication.ui.srt.SRTWriter;
import com.li.videoapplication.ui.srt.SRTWriterException;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

public class SRTHelper {

    public static final String TAG = SRTHelper.class.getSimpleName();

    // ---------------------------------------------------------------------------------

    /**
     * 读取字幕
     */
    public static SRTInfo readSRT(String path) {
        Log.d(TAG, "----------------------------------[readSRT]----------------------------------");
        Log.d(TAG, "path=" + path);
        SRTInfo info = null;
        if (path != null) {
            try {
                info = SRTReader.read(new File(path));
            } catch (InvalidSRTException e) {
                e.printStackTrace();
            } catch (SRTReaderException e) {
                e.printStackTrace();
            }
            printSRT(info);
        }
        return info;
    }

    /**
     * 保存字幕
     */
    public static boolean saveSRT(String path, SRTInfo info) {
        Log.d(TAG, "----------------------------------[saveSRT]----------------------------------");
        Log.d(TAG, "path=" + path);
        File file = null;
        try {
            file = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (info != null && file != null) {
            try {
                SRTWriter.write(file, info);
            } catch (SRTWriterException e) {
                e.printStackTrace();
            }
            printSRT(info);
            return true;
        }
        return false;
    }

    // ---------------------------------------------------------------------------------

    /**
     * 在末尾插入字幕
     */
    public static boolean appendSRT(SRTInfo info,
                                    long startTime,
                                    long endTime,
                                    List<String> texts) {
        Log.d(TAG, "----------------------------------[appendSRT]----------------------------------");
        Log.d(TAG, "info=" + info);
        if (info != null) {
            Date startDate = new Date(startTime);
            startDate = getGMTTime(startDate, "GMT+8", "GMT");
            Date endDate = new Date(endTime);
            endDate = getGMTTime(endDate, "GMT+8", "GMT");

            try {
                SRTEditor.appendSubtitle(info,
                        SRTTimeFormat.format(startDate),
                        SRTTimeFormat.format(endDate),
                        texts);
            } catch (Exception e) {
                e.printStackTrace();
            }
            printSRT(info);
            return true;
        }
        return false;
    }

    /**
     * 插入字幕
     */
    public static boolean insertSRT(SRTInfo info,
                                    int subtitleNumber,
                                    long startTime,
                                    long endTime,
                                    List<String> texts) {
        Log.d(TAG, "----------------------------------[insertSRT]----------------------------------");
        Log.d(TAG, "info=" + info);
        if (info != null) {
            Date startDate = new Date(startTime);
            startDate = getGMTTime(startDate, "GMT+8", "GMT");
            Date endDate = new Date(endTime);
            endDate = getGMTTime(endDate, "GMT+8", "GMT");

            SRT newSRT = new SRT(subtitleNumber,
                    startDate,
                    endDate,
                    texts);
            try {
                SRTEditor.insertSubtitle(info,
                        newSRT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            printSRT(info);
            return true;
        }
        return false;
    }

    /**
     * 插入字幕（删除重复）
     */
    public static boolean insertSRT(SRTInfo info, SRT newSRT) {
        Log.d(TAG, "----------------------------------[insertSRT]----------------------------------");
        return false;
    }

    /**
     * 插入字幕
     */
    public static boolean insertSRT_2(SRTInfo info,
                                      int subtitleNumber,
                                      long startTime,
                                      long endTime,
                                      List<String> texts) {
        Log.d(TAG, "----------------------------------[insertSRT_2]----------------------------------");
        Log.d(TAG, "info=" + info);
        if (info != null) {
            Date startDate = new Date(startTime);
            startDate = getGMTTime(startDate, "GMT+8", "GMT");
            Date endDate = new Date(endTime);
            endDate = getGMTTime(endDate, "GMT+8", "GMT");

            try {
                SRTEditor.insertSubtitle(info,
                        subtitleNumber,
                        SRTTimeFormat.format(startDate),
                        SRTTimeFormat.format(endDate),
                        texts);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    /**
     * 删除字幕
     */
    public static boolean removeSRT(SRTInfo info,
                                    int subtitleNumber) throws Exception {
        Log.d(TAG, "----------------------------------[removeSRT]----------------------------------");
        Log.d(TAG, "info=" + info);
        Log.d(TAG, "removeSRT: subtitleNumber=" + subtitleNumber);
        printSRT(info);
        if (info != null) {
            SRTEditor.removeSubtitle(info,
                    subtitleNumber);
            return true;
        }
        return false;
    }

    /**
     * 更新字幕
     */
    public static boolean updateSRT(SRTInfo info,
                                    int subtitleNumber,
                                    long startTime,
                                    long endTime,
                                    List<String> texts) {
        Log.d(TAG, "----------------------------------[updateSRT]----------------------------------");
        Log.d(TAG, "info=" + info);
        SRT srt = info.get(subtitleNumber);
        if (srt != null) {
            Date startDate = new Date(startTime);
            startDate = getGMTTime(startDate, "GMT+8", "GMT");
            Date endDate = new Date(endTime);
            endDate = getGMTTime(endDate, "GMT+8", "GMT");

            SRT s = new SRT(subtitleNumber, startDate, endDate, texts);

            try {
                SRTEditor.updateSubtitle(info,
                        s);
            } catch (Exception e) {
                e.printStackTrace();
            }
            printSRT(info);
            return true;
        }
        return false;
    }

    /**
     * 更新Text
     */
    public static SRTInfo updateText(SRTInfo info,
                                     int subtitleNumber,
                                     int width) {
        Log.d(TAG, "----------------------------------[updateText]----------------------------------");
        Log.d(TAG, "info=" + info);
        if (info != null) {
            SRTEditor.updateText(info,
                    subtitleNumber,
                    width);
        }
        return info;
    }

    /**
     * 更新所有Text
     */
    public static SRTInfo updateTexts(SRTInfo info,
                                      int width) {
        Log.d(TAG, "----------------------------------[updateTexts]----------------------------------");
        Log.d(TAG, "info=" + info);
        if (info != null) {
            SRTEditor.updateTexts(info,
                    width);
        }
        return info;
    }

    /**
     * 更新Srt
     */
    public static SRTInfo updateSubtitle(SRTInfo info,
                                         SRT srt) {
        Log.d(TAG, "----------------------------------[updateSubtitle]----------------------------------");
        Log.d(TAG, "info=" + info);
        if (info != null) {
            SRTEditor.updateSubtitle(info,
                    srt);
        }
        return info;
    }

    /**
     * 更新开始时间和结束时间
     */
    public static SRTInfo updateTime(SRTInfo info,
                                     int subtitleNumber,
                                     SRTTimeFormat.Type type,
                                     int value) {
        Log.d(TAG, "----------------------------------[updateSubtitle]----------------------------------");
        Log.d(TAG, "info=" + info);
        if (info != null) {
            SRTEditor.updateTime(info,
                    subtitleNumber,
                    type,
                    value);
        }
        return info;
    }

    /**
     * 更新所有开始时间和结束时间
     */
    public static SRTInfo updateTimes(SRTInfo info,
                                      SRTTimeFormat.Type type,
                                      int value) {
        Log.d(TAG, "----------------------------------[updateSubtitle]----------------------------------");
        Log.d(TAG, "info=" + info);
        if (info != null) {
            SRTEditor.updateTimes(info,
                    type,
                    value);
        }
        return info;
    }

    // ---------------------------------------------------------------------------------

    /**
     * 打印字幕
     */
    public static void printSRT(SRTInfo info) {
        Log.d(TAG, "----------------------------------[printSRT]----------------------------------");
        Log.d(TAG, "info=" + info);
        if (info != null) {
            Iterator<SRT> iterator = info.iterator();
            while (iterator.hasNext()) {
                SRT srt = iterator.next();
                Log.d(TAG, "printSRT: srt=" + srt);
            }
        }
    }

    /**
     * 打印字幕
     */
    public static void printSRT(List<SRT> srts) {
        Log.d(TAG, "----------------------------------[printSRT]----------------------------------");
        if (srts != null && srts.size() > 0) {
            Iterator<SRT> iterator = srts.iterator();
            while (iterator.hasNext()) {
                SRT srt = iterator.next();
                Log.d(TAG, "printSRT: srt=" + srt);
            }
        }
    }

    /**
     * 时区转换
     *
     * @param fromTime
     *            原始时区
     * @param toTime
     *            需要转换到的时区
     */
    public static Date getGMTTime(Date orgDate, String fromTime, String toTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone(toTime));
        cal.setTime(orgDate);
        Calendar date = Calendar.getInstance();
        date.setTimeZone(TimeZone.getTimeZone(fromTime));
        date.set(Calendar.MILLISECOND, cal.get(Calendar.MILLISECOND));
        date.set(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                cal.get(Calendar.SECOND));
        return date.getTime();
    }

    /**
     * 转换毫秒
     */
    public static long toMILLISECOND(Date orgDate) {
        Log.d(TAG, "toMILLISECOND: orgDate=" + orgDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(orgDate);
        long millisecond = cal.get(Calendar.HOUR_OF_DAY) * 3600 * 1000 +
                cal.get(Calendar.MINUTE) * 60 * 1000 +
                cal.get(Calendar.SECOND) * 1000 +
                cal.get(Calendar.MILLISECOND);
        Log.d(TAG, "toMILLISECOND: millisecond=" + millisecond);
        return millisecond;
    }

    // ---------------------------------------------------------------------------------

    /**
     * 取出有交集的字幕
     */
    public static List<SRT> getRepeattingSRT(SRTInfo info,
                                             long startTime,
                                             long endTime) {
        Log.d(TAG, "----------------------------------[getRepeattingSRT]----------------------------------");
        Log.d(TAG, "getRepeattingSRT/startTime="+ startTime);
        Log.d(TAG, "getRepeattingSRT/endTime=" + endTime);
        List<SRT> list = new ArrayList<>();
        Iterator<SRT> iterator = info.iterator();
        while (iterator.hasNext()) {
            SRT s = iterator.next();
            long start = SRTHelper.toMILLISECOND(s.startTime);
            long end = SRTHelper.toMILLISECOND(s.endTime);
            Log.d(TAG, "getRepeattingSRT/s/number="+ s.number);
            Log.d(TAG, "getRepeattingSRT/s/startTime="+ start);
            Log.d(TAG, "getRepeattingSRT/s/endTime="+ end);
            if (startTime >= start &&
                    startTime <= end) {
                list.add(s);
            } else if (endTime >= start &&
                    endTime <= end) {
                list.add(s);
            } else if (startTime < start &&
                    endTime > end) {
                list.add(s);
            }
        }
        printSRT(list);
        return list;
    }

    /**
     * 对字幕根据开始时间进行排序（从小到大）
     */
    public static SRTInfo sortSRT(SRTInfo info) {
        if (info != null && info.size() > 0) {
            final List<SRT> list = new ArrayList<>();

            Iterator<SRT> iterator = info.iterator();
            while (iterator.hasNext()) {
                SRT srt = iterator.next();
                list.add(srt);
            }

            SRTComparators.sortSRT(list);
            info.clear();
            for (int i = 0; i < list.size(); ++i) {
                SRT srt = list.get(i);
                SRT newSRT = new SRT(i + 1, srt.startTime, srt.endTime, srt.text);
                info.add(newSRT);
            }
        }
        return info;
    }

    /**
     * 插入字幕，并排序
     */
    public static SRTInfo newSRTInfo(SRTInfo info,
                                     long startTime,
                                     long endTime,
                                     List<String> texts) {
        Log.d(TAG, "----------------------------------[newSRTInfo]----------------------------------");
        // 在末尾插入字幕
        SRTHelper.appendSRT(info, startTime, endTime, texts);
        // 对字幕根据开始时间进行排序（从小到大）
        SRTInfo newInfo = SRTHelper.sortSRT(info);
        return newInfo;
    }

    /**
     * 插入字幕（覆盖），并排序
     */
    public static SRTInfo newSRTInfo_2(SRTInfo info,
                                       long startTime,
                                       long endTime,
                                       List<String> texts) throws Exception{
        Log.d(TAG, "----------------------------------[newSRTInfo_2]----------------------------------");
        SRTInfo newInfo = (SRTInfo) info.clone();
        Log.d(TAG, "--------------[newInfo]--------------");
        printSRT(newInfo);

        final List<SRT> list = getRepeattingSRT(newInfo, startTime, endTime);
        Log.d(TAG, "--------------[list]--------------");
        printSRT(list);

        for (int i = 0; i < list.size(); ++i) {
            newInfo.remove(list.get(i));
        }

        // 在末尾插入字幕
        SRTHelper.appendSRT(newInfo, startTime, endTime, texts);

        Log.d(TAG, "--------------[newInfo]--------------");
        printSRT(newInfo);

        // 对字幕根据开始时间进行排序（从小到大）
        newInfo = SRTHelper.sortSRT(newInfo);

        return newInfo;
    }
}
