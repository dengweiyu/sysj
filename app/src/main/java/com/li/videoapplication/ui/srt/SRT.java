
package com.li.videoapplication.ui.srt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 字幕实体
 */
public class SRT implements Comparable<SRT> {

    public final int number;
    public final Date startTime;
    public final Date endTime;
    public final List<String> text;
    
    //SRT实例
    public SRT(int number, Date startTime, Date endTime, String... text) {
        this.number = number;
        this.startTime = startTime;
        this.endTime = endTime;
        this.text = new ArrayList<>(Arrays.asList(text));
    }
    
    public SRT(int number, Date startTime, Date endTime, List<String> text) {
        this.number = number;
        this.startTime = startTime;
        this.endTime = endTime;
        this.text = new ArrayList<>(text);
    }

    public String getText() {
        if (text != null) {
            StringBuffer buffer = new StringBuffer();
            Iterator<String> iterator = text.iterator();
            int index = 0;
            while (iterator.hasNext()) {
                String s = iterator.next();
                ++ index;
                buffer.append(s);
                if (index > text.size()) {
                    buffer.append("\n");
                }
            }
            return buffer.toString();
        }
        return "";
    }

    /**
     * hash
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (number ^ (number >>> 32));
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SRT other = (SRT) obj;
        if (number != other.number)
            return false;
        return true;
    }

    @Override
    public int compareTo(SRT o) {
        return new Integer(number).compareTo(o.number);
    }

    @Override
    public String toString() {
        return "SRT{" +
                "number=" + number +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", text=" + text +
                '}';
    }
}
