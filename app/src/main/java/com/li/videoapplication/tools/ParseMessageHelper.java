package com.li.videoapplication.tools;

import com.li.videoapplication.framework.AppConstant;


/**
 * 解析IM点击事件
 */

public class ParseMessageHelper {
    public final static int CLICK_TYPE_MATCH = 0;          //赛事
    public final static int CLICK_TYPE_ACTIVITY = 1;       //活动
    public final static int CLICK_TYPE_VIDEO = 2;           //视频

    private static ParseMessageHelper sInstance;

    private String mEventId;
    private String mActivityId;
    private String mVideoId;
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
        if (msg != null) {
            if (msg.indexOf(AppConstant.getEventUrl()) > -1) {
                String startIndex = "id/";
                String endIdIndex = "/from";
                int start = msg.indexOf(startIndex);
                int end = msg.indexOf(endIdIndex);
                if (start > -1) {        //m站复制的地址:http://m.17sysj.com/event/detail/event_id/528/from/sysj_m.html
                    if (end > -1) {
                        try {
                            mEventId = msg.substring(start + startIndex.length(), end);
                            type = CLICK_TYPE_MATCH;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {                         //手游视界分享出去的地址: http://m.17sysj.com/event/detail/event_id/528
                        end = msg.length();
                        if (end > -1) {
                            try {
                                mEventId = msg.substring(start + startIndex.length(), end);
                                type = CLICK_TYPE_MATCH;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }else if (msg.indexOf(AppConstant.getActivityUrl()) > -1) {
                String startIndex = "id/";
                String endIdIndex = ".html";
                int start = msg.indexOf(startIndex);
                int end = msg.lastIndexOf(endIdIndex);
                if (start > -1 && end > -1) {
                    try {
                        mActivityId = msg.substring(start + startIndex.length(), end);
                        type = CLICK_TYPE_ACTIVITY;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }else if(msg.indexOf("/video/") > -1){
                String startIndex = "/video/";
                String endIdIndex = ".html";
                int start = msg.indexOf(startIndex);
                int end = msg.lastIndexOf(endIdIndex);
                if (start > -1) {
                    if (end > -1){                  //http://www.17sysj.com/video/lpds_07624fd48731a.html
                        try {
                            mVideoId = msg.substring(start + startIndex.length(), end);
                            type = CLICK_TYPE_VIDEO;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {                         //http://www.17sysj.com/video/lpds_07624fd48731a
                        try {
                            mVideoId = msg.substring(start + startIndex.length(),msg.length());
                            type = CLICK_TYPE_VIDEO;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

    public String getVideoId() {
        return mVideoId;
    }
}
