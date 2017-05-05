package com.ifeimo.im.common.util;

import com.ifeimo.im.IEmployee;
import com.ifeimo.im.framwork.Proxy;

/**
 * Created by lpds on 2017/1/16.
 */
public class SettingsManager implements IEmployee{

    private static SettingsManager settingsManager;

    static{
        settingsManager = new SettingsManager();
    }

    private SettingsManager(){
        Proxy.getManagerList().addManager(this);
    }

    public static SettingsManager getInstances(){
        return settingsManager;
    }


    @Override
    public boolean isInitialized() {
        return true;
    }
}
