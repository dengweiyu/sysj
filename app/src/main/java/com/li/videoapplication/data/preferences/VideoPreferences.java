package com.li.videoapplication.data.preferences;

/**
 * 功能：记录已导入的外部视频
 */
public class VideoPreferences extends BasePreferences {

	private static final String NAME = "ext_video";

    @Override
    protected String getName() {
        return NAME;
    }

    private static VideoPreferences instance;

    public static VideoPreferences getInstance() {
        if (instance == null) {
            synchronized (VideoPreferences.class) {
                if (instance == null) {
                    instance = new VideoPreferences();
                }
            }
        }
        return instance;
    }
}
