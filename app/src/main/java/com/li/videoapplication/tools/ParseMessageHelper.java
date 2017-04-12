package com.li.videoapplication.tools;

import com.li.videoapplication.framework.AppConstant;


/**
 * 解析IM点击事件
 */

public class ParseMessageHelper {
    public final static int CLICK_TYPE_MATCH = 0;          //赛事
    public final static int CLICK_TYPE_ACTIVITY = 1;       //活动

    private static ParseMessageHelper sInstance;

    private String mEventId;
    private String mActivityId;
    private ParseMessageHelper(){}

    public static ParseMessageHelper getInstance(){
        if (sInstance == null){
            sInstance = new ParseMessageHelper();
        }
        return sInstance;
    }

    /**
     *根据消息内容解析事件
     */
    public  int parseMessage(String msg){
        int type = -1;
        if (msg != null){
            if (msg.indexOf(AppConstant.getEventUrl()) > -1){
                String startIndex = "event_id/";
                String endIdIndex = "/from";
                int start = msg.indexOf(startIndex);
                int end = msg.indexOf(endIdIndex);
                if (start > -1 && end > -1){
                    try {
                        mEventId = msg.substring(start+startIndex.length(),end);
                        type = CLICK_TYPE_MATCH;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }else if (msg.indexOf(AppConstant.getActivityUrl()) > -1){
                String startIndex = "id/";
                String endIdIndex = ".";
                int start = msg.indexOf(startIndex);
                int end = msg.lastIndexOf(endIdIndex);
                if (start > -1 && end > -1){
                    try {
                        mActivityId = msg.substring(start+startIndex.length(),end);
                        type = CLICK_TYPE_ACTIVITY;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return type;
    }

    /**
     *
     */
    public String getEventId(){
        return mEventId;
    }

    public String getActivityId(){
        return mActivityId;
    }
}
