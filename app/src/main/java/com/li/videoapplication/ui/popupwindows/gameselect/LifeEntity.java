package com.li.videoapplication.ui.popupwindows.gameselect;


import com.li.videoapplication.data.model.response.Associate201Entity;

public class LifeEntity extends Associate201Entity {

    String classType;

    String keyWord;

    public LifeEntity(){

    }

    public LifeEntity(String classType, String keyWord) {
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
