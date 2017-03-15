package com.ifeimo.im.common.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.ifeimo.im.R;

/**
 * Created by lpds on 2017/1/19.
 */
public class WindowUtil {

    public static int ID = 0x9314;

    public static void addMark(Activity activity){
        final ViewGroup viewGroup = (ViewGroup) activity.getWindow().getDecorView();
        View v = viewGroup.findViewById(ID);
        if(v == null){
            v = new View(activity);
            v.setBackgroundColor(Color.parseColor("#cc000000"));
            v.setId(ID);
            viewGroup.addView(v);
        }
    }

    public static void removeMark(Activity activity){
        final ViewGroup viewGroup = (ViewGroup) activity.getWindow().getDecorView();
        View v = viewGroup.findViewById(ID);
        if(v != null){
            viewGroup.removeView(v);
        }
    }

    public static void setHideSoft(Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && activity.getCurrentFocus() != null) {
            if (activity.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


}
