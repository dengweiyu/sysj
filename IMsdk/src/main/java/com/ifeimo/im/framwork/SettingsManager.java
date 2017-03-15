package com.ifeimo.im.framwork;

import com.ifeimo.im.framwork.interface_im.IMSetting;
import com.ifeimo.im.framwork.setting.Builder;

import java.nio.Buffer;

/**
 * Created by lpds on 2017/1/16.
 */
final class SettingsManager implements IMSetting{


    private Builder builder;
    private static SettingsManager settingsManager;

    static{
        settingsManager = new SettingsManager();
    }

    private SettingsManager(){
        ManagerList.getInstances().addManager(this);
        builder = new Builder(this);
    }

    public static IMSetting getInstances(){
        return settingsManager;
    }

    public Builder getBuilder() {
        return builder;
    }

    @Override
    public boolean isInitialized() {
        return true;
    }


}
