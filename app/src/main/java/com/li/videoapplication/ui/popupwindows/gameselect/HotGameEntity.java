package com.li.videoapplication.ui.popupwindows.gameselect;


import com.li.videoapplication.data.model.response.Associate201Entity;

/**
 * Created by linhui on 2017/9/18.
 */
public class HotGameEntity extends Associate201Entity {
    String classType;

    String keyWord;

    public HotGameEntity(){}


    public HotGameEntity(String classType, String keyWord) {
        this.classType = classType;
        this.keyWord = keyWord;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}



