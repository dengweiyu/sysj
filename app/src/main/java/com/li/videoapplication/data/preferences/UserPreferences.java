package com.li.videoapplication.data.preferences;

/**
 * 功能：登录用户个人数据
 */
public class UserPreferences extends BasePreferences {

	private static final String NAME = "user";

    @Override
    protected String getName() {
        return NAME;
    }

    private static UserPreferences instance;

    public static UserPreferences getInstance() {
        if (instance == null) {
            synchronized (UserPreferences.class) {
                if (instance == null) {
                    instance = new UserPreferences();
                }
            }
        }
        return instance;
    }
}
