
package com.li.videoapplication.ui.srt;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 编辑字幕
 */
public class SRTEditor {
    
    //更新SRTInfo开始时间和结束时间
    public static void updateTime(SRTInfo info, int subtitleNumber,
        SRTTimeFormat.Type type, int value) {
        if (!info.contains(subtitleNumber)) {
            throw new SRTEditorException(subtitleNumber + " could not be found");
        }
        info.add(setTime(info.get(subtitleNumber), type, value));
    }
    
    public static void updateTimes(SRTInfo info, SRTTimeFormat.Type type, int value) {
        for (int i = 1; i <= info.size(); i++) {
            updateTime(info, info.get(i).number, type, value);
        }
    }
    
    //new SRT
    static SRT setTime(SRT srt, SRTTimeFormat.Type type, int value) {
        return new SRT(srt.number, newDate(srt.startTime, type, value),
            newDate(srt.endTime, type, value), srt.text);
    }
    
    private static Date newDate(Date oldDate, SRTTimeFormat.Type type, int value) {
        Calendar newCal = Calendar.getInstance();
        newCal.setTime(oldDate);
        switch (type) {
        case HOUR:
            newCal.add(Calendar.HOUR_OF_DAY, value);
            break;
        case MINUTE:
            newCal.add(Calendar.MINUTE, value);
            break;
        case SECOND:
            newCal.add(Calendar.SECOND, value);
            break;
        case MILLISECOND:
            newCal.add(Calendar.MILLISECOND, value);
            break;
        }
        return newCal.getTime();
    }
    
    //更新text
    public static void updateText(SRTInfo info, int subtitleNumber, int width) {
        if (!info.contains(subtitleNumber)) {
            throw new SRTEditorException(subtitleNumber + " could not be found");
        }
        info.add(breakText(info.get(subtitleNumber), width));
    }
    
    //所有text
    public static void updateTexts(SRTInfo info, int width) {
        for (int i = 1; i <= info.size(); i++) {
            updateText(info, info.get(i).number, width);
        }
    }
    
    public static SRT breakText(SRT srt, int width) {
        List<String> newTexts = new ArrayList<>();
        String subtitle = StringUtils.join(srt.text, " ");
        if (subtitle.length() <= width) {
            newTexts.addAll(srt.text);
        } else {
            int begin = 0;
            int end = width;
            while (end < subtitle.length()) {
                while (subtitle.charAt(end) != ' ' && end != 0) {
                    end--;
                }
                if (end == 0) {
                    break;
                }
                newTexts.add(subtitle.substring(begin, end));
                begin = end + 1;
                end = begin + width;
            }
            newTexts.add(subtitle.substring(begin, subtitle.length()));
        }
        return new SRT(srt.number, srt.startTime, srt.endTime, newTexts);
    }

    /**
     * 在末尾插入SRT
     */
    public static void appendSubtitle(SRTInfo info, String startTime,
        String endTime, List<String> text) {
        try {
            SRT newSRT = new SRT(
                info.size() + 1,
                SRTTimeFormat.parse(startTime),
                SRTTimeFormat.parse(endTime),
                text);
            info.add(newSRT);
        } catch (ParseException e) {
            throw new SRTEditorException(e);
        }
    }

    /**
     * 在第一个位置插入SRT
     */
    public static void prependSubtitle(SRTInfo info, String startTime,
        String endTime, List<String> text) {
        insertSubtitle(info, 1, startTime, endTime, text);
    }

    /**
     * 在位置number插入SRT
     */
    public static void insertSubtitle(SRTInfo info, int subtitleNumber,
        String startTime, String endTime, List<String> text) {
        if (!info.contains(subtitleNumber)) {
            throw new SRTEditorException(subtitleNumber + " could not be found");
        }
        
        for (int i = info.size(); i >= subtitleNumber; i--) {
            SRT tmp = info.get(i);
            info.add(new SRT(tmp.number+1, tmp.startTime, tmp.endTime, tmp.text));
        }
        
        try {
            info.add(new SRT(subtitleNumber, SRTTimeFormat.parse(startTime),
                SRTTimeFormat.parse(endTime), text));
        } catch (ParseException e) {
            throw new SRTEditorException(e);
        }
    }

    /**
     * 在位置number插入SRT
     */
    public static void insertSubtitle(SRTInfo info, SRT newSRT) {
        for (int i = info.size(); i >= newSRT.number; i--) {
            SRT tmp = info.get(i);
            info.add(new SRT(tmp.number+1, tmp.startTime, tmp.endTime, tmp.text));
        }
        info.add(newSRT);
    }

    /**
     * 移除SRT
     */
    public static void removeSubtitle(SRTInfo info, int subtitleNumber) {
        if (!info.contains(subtitleNumber)) {
            throw new SRTEditorException(subtitleNumber + " could not be found");
        }
        
        int originalSize = info.size();
        for (int i = subtitleNumber+1; i  <= originalSize; i++) {
            SRT tmp = info.get(i);
            info.add(new SRT(tmp.number-1, tmp.startTime, tmp.endTime, tmp.text));
        }
        info.remove(info.size());
    }

    /**
     * 更新SRT
     */
    public static void updateSubtitle(SRTInfo info, SRT srt) {
        info.add(srt);
    }
}
