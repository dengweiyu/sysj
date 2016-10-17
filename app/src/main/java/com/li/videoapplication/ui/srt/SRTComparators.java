package com.li.videoapplication.ui.srt;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 字幕排序
 */
public class SRTComparators {

    /**
     * 对字幕根据开始时间进行排序（从小到大）
     */
    public static void sortSRT(List<SRT> list) {
        SRTComparator comparator = new SRTComparator();
        Collections.sort(list, comparator);
    }

    /**
     * 对字幕根据开始时间进行排序（从小到大）
     */
    public static class SRTComparator implements Comparator<SRT> {

        @Override
        public int compare(SRT lhs, SRT rhs) {
            if(lhs.startTime.after(rhs.startTime)){
                return 1;
            } else{
                return -1;
            }
        }
    }
}
