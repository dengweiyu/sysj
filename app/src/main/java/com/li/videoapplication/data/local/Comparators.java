package com.li.videoapplication.data.local;

import com.li.videoapplication.data.database.VideoCaptureEntity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Comparators {

    /**
     * 对图片根据修改时间进行排序
     */
    public static void sortScreenShot(List<ScreenShotEntity> list) {
        Comparators.ScreenShotLastModifiedComparator comparator = new Comparators.ScreenShotLastModifiedComparator();
        Collections.sort(list, comparator);
    }

    /**
     * 对视频根据修改时间进行排序
     */
    public static void sortVideoCapture(List<VideoCaptureEntity> list) {
        Comparators.VideoCaptureLastModifiedComparator comparator = new Comparators.VideoCaptureLastModifiedComparator();
        Collections.sort(list, comparator);
    }

    /**
     * 对图片根据修改时间进行排序
     */
    public static class ScreenShotLastModifiedComparator implements Comparator<ScreenShotEntity> {

        @Override
        public int compare(ScreenShotEntity lhs, ScreenShotEntity rhs) {
            if (lhs.getLastModified() > rhs.getLastModified()) {
                return -1;
            } else if (lhs.getLastModified() == rhs.getLastModified()) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    /**
     * 对图片根据修改时间进行排序
     */
    public static class VideoCaptureLastModifiedComparator implements Comparator<VideoCaptureEntity> {

        @Override
        public int compare(VideoCaptureEntity lhs, VideoCaptureEntity rhs) {
            if (lhs.getLastModified() > rhs.getLastModified()) {
                return -1;
            } else if (lhs.getLastModified() == rhs.getLastModified()) {
                return 0;
            } else {
                return 1;
            }
        }
    }
}
