package com.li.videoapplication.data.local;

import com.li.videoapplication.data.database.VideoCaptureManager;
import com.li.videoapplication.data.preferences.Constants;
import com.li.videoapplication.data.preferences.NormalPreferences;

import java.io.File;

public class Utils {

    /**
     * 把/LuPingDaShi目录下的录屏视频/截屏复制到/sysj
     */
    public static void copyLuPingDaShiToSysj() {
        boolean flag = NormalPreferences.getInstance().getBoolean(Constants.COPY_LUPINGDSHI_TO_SYSJ, false);
        if (flag == false) {
            copyRec();
            copyPicture();
            VideoCaptureManager.dropTable();
            NormalPreferences.getInstance().putBoolean(Constants.COPY_LUPINGDSHI_TO_SYSJ, true);
        }
    }

    /**
     * 把/LuPingDaShi目录下的录屏视频复制到/sysj
     */
    private static void copyRec() {
        // 外置SD卡
        File outer = LPDSStorageUtil.getOuterLpdsRec();
        // 内置SD卡
        File inner = LPDSStorageUtil.getInnerLpdsRec();
        if (outer != null) {
            scanFloder(new File(inner.getPath() + File.separator), new File(outer.getPath() + File.separator));
        } else {
            scanFloder(new File(inner.getPath() + File.separator), null);
        }
    }

    /**
     * 把/LuPingDaShi目录下的截屏复制到/sysj
     */
    private static void copyPicture() {
        // 外置SD卡
        File outer = LPDSStorageUtil.getOuterLpdsPicture();
        // 内置SD卡
        File inner = LPDSStorageUtil.getInnerLpdsPicture();
        if (outer != null) {
            scanFloder(new File(inner.getPath() + File.separator), new File(outer.getPath() + File.separator));
        } else {
            scanFloder(new File(inner.getPath() + File.separator), null);
        }
    }

    /**
     * 扫描文件夹
     */
    private static void scanFloder(final File inner, final File outer) {
        int lengths = 0;
        int innerLength = 0;
        int outerLength = 0;
        File[] innerFiles = null;
        if (inner != null) {
            innerFiles = inner.listFiles();
            if (innerFiles != null) {
                innerLength = innerFiles.length;
            }
        }
        File[] outerFiles = null;
        if (outer != null) {
            outerFiles = outer.listFiles();
            outerLength = outerFiles.length;
        }
        lengths = innerLength + outerLength;
        if (lengths <= 0) {
            return;
        }
        if (innerFiles != null && innerFiles.length > 0) {
            // 内置SD卡
            for (int i = 0; i < innerLength; i++) {
                copyFile(innerFiles[i]);
            }
        }
        if (outerFiles != null && outerFiles.length > 0) {
            // 外置SD卡
            for (int i = 0; i < outerLength; i++) {
                copyFile(outerFiles[i]);
            }
        }
    }

    /**
     * 复制文件
     */
    private static void copyFile(File oldFile) {
        if (oldFile.isFile()) {
            // 文件是MP4文件且大于512
            if ((oldFile.getName().endsWith("mp4") && !oldFile.getName().equals(".mp4") && FileUtil.getFileSize(oldFile) > 512) ||
                    (oldFile.getName().endsWith("png") && !oldFile.getName().equals(".png"))) {
                String newFile = null;
                if (oldFile.getName().endsWith("mp4")) {
                    newFile = SYSJStorageUtil.getSysjRec() + File.separator +  oldFile.getName();
                } else if (oldFile.getName().endsWith("png")){
                    newFile = SYSJStorageUtil.getSysjPicture() + File.separator +  oldFile.getName();
                }
                if (newFile != null) {
                    FileUtil.copyFileToFile(oldFile.getAbsolutePath(), newFile);
                }
            }
        }
    }
}
