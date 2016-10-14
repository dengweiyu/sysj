package com.li.videoapplication.data;

import com.li.videoapplication.data.local.ImageDirectoryHelper;
import com.li.videoapplication.data.local.ScreenShotHelper;
import com.li.videoapplication.data.local.VideoCaptureHelper;

/**
 * 记载本地文件
 */
public final class LocalManager {

    /**
     * 加载截图
     */
    public static final void loadScreenShots() {
        ScreenShotHelper helper = new ScreenShotHelper();
        helper.loadScreenShots();
    }

    /**
     * 加载本地视频
     */
    public static final void loadVideoCaptures() {
        VideoCaptureHelper helper = new VideoCaptureHelper();
        helper.loadVideoCaptures();
    }

    /**
     * 验证并加载本地视频
     */
    public static final void checkVideoCaptures() {
        VideoCaptureHelper helper = new VideoCaptureHelper();
        helper.checkVideoCaptures();
    }

    /**
     * 导入外部视频
     */
    public static final void importVideoCaptures() {
        VideoCaptureHelper helper = new VideoCaptureHelper();
        helper.importVideoCaptures();
    }

    /**
     * 加载图片文件夹
     */
    public static final void loadImageDirectorys() {
        ImageDirectoryHelper helper = new ImageDirectoryHelper();
        helper.loadImageDirectorys();
    }
}
