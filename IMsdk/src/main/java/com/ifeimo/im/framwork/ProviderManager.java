package com.ifeimo.im.framwork;

import com.ifeimo.im.IEmployee;

/**
 * Created by lpds on 2017/1/24.
 */
final class ProviderManager implements IEmployee {

    private static ProviderManager providerManager;

    static{
        providerManager = new ProviderManager();
    }

    private ProviderManager(){
        ManagerList.getInstances().addManager(this);
    }


    @Override
    public boolean isInitialized() {
        return true;
    }
}
