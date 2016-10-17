package com.li.videoapplication.ui.srt;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * 字幕实体
 */
public class SRTInfo implements Iterable<SRT>, Cloneable {

    private final TreeSet<SRT> srts;

    public SRTInfo() {
        srts = new TreeSet<>();
    }
    
    public SRTInfo(SRTInfo srtInfo) {
        srts = new TreeSet<>(srtInfo.srts);
    }

    public void add(SRT srt) {
        remove(srt);
        srts.add(srt);
    }

    public int size() {
        return srts.size();
    }
    
    public void remove(SRT srt) {
        srts.remove(srt);
    }

    public void remove(int number) {
        srts.remove(get(number));
    }

    public void clear() {
        srts.clear();
    }

    //获取SRT实例
    public SRT get(int number) {
        return srts.tailSet(new SRT(number, null, null, new String[]{})).first();
    }
    
    public SRT get(SRT srt) {
        return srts.tailSet(srt).first();
    }
    
    //检查字幕序号是SRTInfo对象
    public boolean contains(int number) {
        return srts.contains(new SRT(number, null, null, new String[]{}));
    }

    //SRT是否在SRTInfo
    public boolean contains(SRT srt) {
        return srts.contains(srt);
    }

    @Override
    public Object clone() {
        return new SRTInfo(this);
    }

    public Iterator<SRT> iterator() {
        return srts.iterator();
    }

    public interface Iterable {

        void iterator(SRT srt);
    }
}