package com.li.videoapplication.data.preferences;

/**
 * 功能：设置
 */
public class SettingPreferences extends BasePreferences {

	private static final String NAME = "setting";

    @Override
    protected String getName() {
        return NAME;
    }

    private static SettingPreferences instance;

    public static SettingPreferences getInstance() {
        if (instance == null) {
            synchronized (SettingPreferences.class) {
                if (instance == null) {
                    instance = new SettingPreferences();
                }
            }
        }
        return instance;
    }
}
