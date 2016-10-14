package com.li.videoapplication.data.record;

import android.hardware.Camera;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Comparators {

    /**
     * 对相机参数根据像素进行排序
     */
    public static void sortCameraSize(List<Camera.Size> list) {
        Comparators.CameraSizeComparator comparator = new Comparators.CameraSizeComparator();
        Collections.sort(list, comparator);
    }

    public static class CameraSizeComparator implements Comparator<Camera.Size> {

        @Override
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            if (lhs.width > rhs.width) {
                return 1;
            } else if (lhs.width < rhs.width) {
                return -1;
            } else {
                if (lhs.height > rhs.height) {
                    return 1;
                } else if (lhs.height< rhs.height) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    }
}
