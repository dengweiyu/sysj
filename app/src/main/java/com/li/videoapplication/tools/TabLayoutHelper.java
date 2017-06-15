package com.li.videoapplication.tools;

import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.LinearLayout;
import java.lang.reflect.Field;

/**
 * 设置margin来设置下划线长度
 */

public class TabLayoutHelper {
   public static void  setIndicator(TabLayout tabLayout,int left,int right){
        Class<?> tabClass = tabLayout.getClass();
        Field tabStrip = null;
       try {
           tabStrip = tabClass.getDeclaredField("mTabStrip");
       } catch (NoSuchFieldException e) {
           e.printStackTrace();
       }
       if (tabStrip != null){
           tabStrip.setAccessible(true);
           LinearLayout layout = null;
           try {
               layout = (LinearLayout) tabStrip.get(tabLayout);
               for (int i = 0; i < layout.getChildCount(); i++) {
                   View child = layout.getChildAt(i);
                   child.setPadding(0,0,0,0);
                   LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)child.getLayoutParams();
                   params.leftMargin = left;
                   params.rightMargin = right;
                   child.setLayoutParams(params);
                   child.invalidate();
               }
           } catch (IllegalAccessException e) {
               e.printStackTrace();
           }
       }
   }
}
