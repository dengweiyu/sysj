package com.ifeimo.im.common;

import android.content.Context;
import android.content.pm.PackageManager;

import com.ifeimo.im.framwork.interface_im.IMWindow;

/**
 * Created by lpds on 2017/2/6.
 */
@Deprecated
public class Version {

    public static final String PACKAGE_NAME = "com.ifeimo.im";

    public static String getPackageName(IMWindow imWindow) {
        String packageName = imWindow.getContext().getPackageName();
        if (!packageName.equals(PACKAGE_NAME)) {
            return null;
        }
        return packageName;
    }

    public static int getCode(IMWindow imWindow) {
        try {
            int versionCode = imWindow.getContext().getPackageManager()
                    .getPackageInfo(imWindow.getContext().getPackageName(), 0).versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
