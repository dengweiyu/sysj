package com.ifeimo.im.common.bean;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lpds on 2017/4/25.
 */
public class OpenHelpBean {
    public static final String SP_NAME = "ifeimo_sqlite_sdk";
    private static OpenHelpBean openHelpBean = new OpenHelpBean();

    private OpenHelpBean(){

    }

    public static OpenHelpBean getInstances(Context context){
        get(context);
        return openHelpBean;
    }

    private static void get(Context context) {
        synchronized (openHelpBean){
            if (openHelpBean.getNewver() == 0
                    || openHelpBean.getOldver() == 0) {

                SharedPreferences s = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
                openHelpBean.setOldver(s.getInt("old", 0));
                openHelpBean.setNewver(s.getInt("new", 0));
            }
        }

    }

    int oldver;

    int newver;

    public int getOldver() {
        return openHelpBean.oldver;
    }

    public void setOldver(int oldver) {
        openHelpBean.oldver = oldver;
    }

    public int getNewver() {
        return openHelpBean.newver;
    }

    public void setNewver(int newver) {
        openHelpBean.newver = newver;
    }


    public static void save(Context context,int old,int news){
        synchronized (openHelpBean) {
            SharedPreferences s = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
            s.edit().putInt("old", old).putInt("new", news).commit();
        }
    }

    public static boolean isFirster(Context context){
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getBoolean("f",false);
    }

    public static boolean changeFirster(Context context,boolean flag){
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).
                edit().putBoolean("f",flag).commit();
    }

}
