package com.li.videoapplication.data.preferences;

/**
 * 功能：一般数据（不随登陆用户数据删除而删除）
 */
public class NormalPreferences extends BasePreferences {

	private static final String NAME = "normal";

    @Override
    protected String getName() {
        return NAME;
    }

    private static NormalPreferences instance;

    public static NormalPreferences getInstance() {
        if (instance == null) {
            synchronized (NormalPreferences.class) {
                if (instance == null) {
                    instance = new NormalPreferences();
                }
            }
        }
        return instance;
    }
}
