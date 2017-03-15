package com.ifeimo.im.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.IllegalFormatException;

/**
 * Created by lpds on 2017/2/7.
 */
public class DateFormatUtil {

    /**
     * 时间间隔
     */
    private static final long $t = 60 * 5 * 1000;

    /**
     *
     * @param fristStr 现在的时间
     * @param date 之前的一条时间
     * @return
     */
    public static String getBorderDate(String fristStr, String date) {
        try {
            long fristDate = Long.parseLong(fristStr);
            long sDate = Long.parseLong(date);
            long difference = 60 * 60 * 24 * 1000;
            Calendar c = Calendar.getInstance();
            c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
            if (fristDate > c.getTimeInMillis()) {//今天
                if (fristDate - sDate > $t || sDate == 0) {
                    return new SimpleDateFormat("HH:mm").format(new Date(fristDate));
                } else {
                    return null;
                }
            } else if (fristDate > c.getTimeInMillis() - difference) {//昨天
                if (fristDate - sDate > $t || sDate == 0) {
                    return new SimpleDateFormat("昨天 HH:mm").format(new Date(fristDate));
                } else {
                    return null;
                }
            }else{
                if (fristDate - sDate > $t || sDate == 0) {//之前
                    return new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date(fristDate));
                } else {
                    return null;
                }


            }
        } catch (IllegalFormatException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     *
     * @param date 时间
     * @param style 例如 yyyy:MM:dd
     * @return
     */
    public static String getstyleByDateStr(String date,String style){
        try{

            return new SimpleDateFormat(style).format(Long.parseLong(date));
        }catch (Exception e){
            e.printStackTrace();
            return "0";
        }
    }

}
