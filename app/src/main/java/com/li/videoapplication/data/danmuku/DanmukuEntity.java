package com.li.videoapplication.data.danmuku;

import android.graphics.Color;

import com.li.videoapplication.tools.ArrayHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DanmukuEntity extends BaseXMLEntity {

    /**
     * d-TAG
     */
    public static final String D = "d";

    /**
     * p-属性
     */
    public static final String P = "p";

    private String p;
    private String appearTime;
    private String appearType = "1";
    private String textSize = "25";
    private String textColor = "16777215";
    private String createTime = "";
    private String danmukuType = "0";
    private String a = "D6673695";
    private String b = "757075520";

    /**
     * d-的内容
     */
    private String text = new String();

    public String getP() {
        List<String> list = new ArrayList<>();
        list.add(appearTime);
        list.add(appearType);
        list.add(textSize);
        list.add(textColor);
        list.add(createTime);
        list.add(danmukuType);
        list.add(a);
        list.add(b);
        p = ArrayHelper.list2Array(list);
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getAppearTime() {
        return appearTime;
    }

    public void setAppearTime(String appearTime) {
        this.appearTime = appearTime;
    }

    public String getAppearType() {
        return appearType;
    }

    public void setAppearType(String appearType) {
        this.appearType = appearType;
    }

    public String getTextSize() {
        return textSize;
    }

    public void setTextSize(String textSize) {
        this.textSize = textSize;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDanmukuType() {
        return danmukuType;
    }

    public void setDanmukuType(String danmukuType) {
        this.danmukuType = danmukuType;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "DanmukuEntity{" +
                "p='" + p + '\'' +
                ", appearTime='" + appearTime + '\'' +
                ", appearType='" + appearType + '\'' +
                ", textSize='" + textSize + '\'' +
                ", textColor='" + textColor + '\'' +
                ", createTime='" + createTime + '\'' +
                ", danmukuType='" + danmukuType + '\'' +
                ", a='" + a + '\'' +
                ", b='" + b + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    public static int getTextColorRamdon() {
        int[] colors = new int[]{ Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.WHITE, Color.RED };
        Random r = new Random();
        int index = r.nextInt(colors.length);
        try {
            return colors[index];
        } catch (Exception e) {
            e.printStackTrace();
            return Color.WHITE;
        }
    }
}
