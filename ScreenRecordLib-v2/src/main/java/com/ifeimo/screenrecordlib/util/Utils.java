package com.ifeimo.screenrecordlib.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.ifeimo.screenrecordlib.RecordingManager;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Utils {

    public static final String TAG = Utils.class.getSimpleName();

    /**
     * 尝试获取Root权限
     *
     * @return  true:成功
     *          fakse:失败
     */
    public static boolean root() {
        Log.d(TAG, "root: ");
        DataInputStream dis = null;
        DataOutputStream dos = null;
        boolean flag = false;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            OutputStream os = process.getOutputStream();
            dos = new DataOutputStream(os);
            InputStream is = process.getInputStream();
            dis = new DataInputStream(is);
            String temp = "ls /data/" + "\n";
            dos.writeBytes(temp);
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            process.waitFor();
            if (dis.readLine() != null)
                flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dis != null)
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (dos != null)
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (process != null)
                try {
                    process.destroy();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        Log.d(TAG, "root: flag=" + flag);
        return flag;
    }

    /**
     * 将录屏核心复制到相应文件夹（4.4以上）
     */
    public static void copyScreenrecord() {
        Log.d(TAG, "copyScreenrecord: ");
        // if (StorageUtil.exitFmNewCore() == false) {
            copyFile(StorageUtil.getFmNewCore(), "screenrecord1111");
            chmod("chmod 777 " + StorageUtil.getFmNewCore());
        // }

        // if (StorageUtil.exitFmOldCore() == false) {
            copyFile(StorageUtil.getFmOldCore(), "screenrecord10272");
            chmod("chmod 777 " + StorageUtil.getFmOldCore());
        // }

        copyFile(StorageUtil.getFmTestCore(), "screenrecord_161109.161109");
        chmod("chmod 777 " + StorageUtil.getFmTestCore());
    }

    /**
     * 将核心复制到相应文件夹
     */
    public static void copyBusybox() {
        Log.d(TAG, "copyBusybox: ");
        if (StorageUtil.exitBusybox() == false) {
            copyFile(StorageUtil.getBusybox(), "busybox");
            chmod("chmod 777 " + StorageUtil.getBusybox());
        }
    }

    @SuppressLint("SdCardPath")
    private static void copyFile(String path, String assets) {
        Log.d(TAG, "copyFile: ");
        Log.d(TAG, "copyFile: path=" + path);
        Log.d(TAG, "copyFile: assets=" + assets);
        Context context = RecordingManager.getInstance().context();
        File file = null;
        try {
            file = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "copyFile: 1");
        if (file == null)
            return;
        if (!file.exists())
            try {
                file.createNewFile();
                Log.d(TAG, "copyFile: 2");
            } catch (IOException e) {
                e.printStackTrace();
            }
        Log.d(TAG, "copyFile: 3");
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is  = context.getAssets().open(assets);
            fos = new FileOutputStream(file);
            Log.d(TAG, "copyFile: 4");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buffer = new byte[1024];
        int count;
        try {
            while ((count = is.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            Log.d(TAG, "copyFile: 5");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null)
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        Log.d(TAG, "copyFile: 6");
        if (file != null && !file.setExecutable(true, false)) {
            file.delete();
            Log.d(TAG, "copyFile: 7");
        }
    }

    public static void chmod(String executable) {
        Log.d(TAG, "chmod: ");
        Process process = null;
        OutputStream os = null;
        InputStream is = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"su", "-c", executable});
            os = process.getOutputStream();
            is = process.getInputStream();
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            if (os != null)
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (process != null)
                try {
                    process.destroy();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     * 将录屏核心复制到相应文件夹（4.4以下）（复制）
     */
    public static void copyFfmpegV2sh_() {
        Log.d(TAG, "copyFfmpegV2sh_: ");
        copyFile(StorageUtil.getFfmpegV2sh(), "ffmpeg_v2sh");
        chmod("chmod 777 " + StorageUtil.getFfmpegV2sh());
    }

    /**
     * 将录屏核心复制到相应文件夹（4.4以下）（解压）
     */
    public static void copyFfmpegV2sh() {
        Log.d(TAG, "copyFfmpegV2sh: ");
        Context context = RecordingManager.getInstance().context();
        String path = StorageUtil.getFfmpegV2sh();

        Log.d(TAG, "copyFfmpegV2sh: 1");
        File file = null;
        try {
            file = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file != null && !file.exists())
            try {
                file.createNewFile();
                Log.d(TAG, "copyFile: 2");
            } catch (IOException e) {
                e.printStackTrace();
            }
        Log.d(TAG, "copyFfmpegV2sh: file=" + file);
        Log.d(TAG, "copyFfmpegV2sh: 2");
        InputStream is = null;
        InputStream bais = null;
        ZipInputStream zis = null;

        String target;
        switch (Build.VERSION.SDK_INT) {
            case 15: {
                target = "ffmpeg1";
                break;
            }
            case 16: {
                target = "ffmpeg2";
                break;
            }
            case 17: {
                target = "ffmpeg3";
                break;
            }
            default: {
                target = "ffmpeg4";
            }
        }
        Log.d(TAG, "copyFfmpegV2sh: target=" + target);
        try {
            is  = context.getAssets().open("ffmpeg_v2");
            Log.d(TAG, "copyFfmpegV2sh: is=" + is);
            Log.d(TAG, "copyFfmpegV2sh: 3");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] b = new byte[120];// 120
            int length;
            while ((length = is.read(b, 0, 100)) > 0) {
                baos.write(b, 0, length);
            }
            byte[] bytes = baos.toByteArray();
            baos.flush();
            baos.close();
            Log.d(TAG, "copyFfmpegV2sh: baos=" + baos);
            Log.d(TAG, "copyFfmpegV2sh: bytes=" + bytes);
            Log.d(TAG, "copyFfmpegV2sh: bytes/length=" + bytes.length);
            Log.d(TAG, "copyFfmpegV2sh: 3");

            int hx = 4, len = bytes.length, start = 0, bk = hx * hx;
            while (true) {
                if (len > (start + bk)) {
                    byte[] byteArray = new byte[6];
                    byte[] newByteArray = new byte[6];
                    synchronized (Utils.class) {
                        System.arraycopy(bytes, start, byteArray, 0, byteArray.length);
                        for (int i = 0; i < byteArray.length; i++) {
                            if (i % 2 == 0) {
                                newByteArray[i] = byteArray[i + 1];
                            } else {
                                newByteArray[i] = byteArray[i - 1];
                            }
                        }
                    }
                    synchronized (Utils.class) {
                        System.arraycopy(newByteArray, 0, bytes, start, newByteArray.length);
                    }
                    start += bk;
                } else {
                    break;
                }
            }

            bais = new ByteArrayInputStream(bytes);
            Log.d(TAG, "copyFfmpegV2sh: bais=" + bais);
            Log.d(TAG, "copyFfmpegV2sh: 4");
            // zis = new ZipInputStream(new BufferedInputStream(is));
            zis = new ZipInputStream(bais);
            // Log.d(TAG, "copyFfmpegV2sh: zis=" + zis);
            Log.d(TAG, "copyFfmpegV2sh: 4");
            ZipEntry entry;
            while (null != (entry = zis.getNextEntry())) {
                Log.d(TAG, "unzip: entry=" + entry);
                Log.d(TAG, "unzip: name=" + entry.getName());
                Log.d(TAG, "unzip: size=" + entry.getSize());
                Log.d(TAG, "unzip: comment=" + entry.getComment());
                Log.d(TAG, "copyFfmpegV2sh: 5");
                if (entry.getName().equals(target)) {
                    Log.d(TAG, "copyFfmpegV2sh: 5");
                    int count;
                    byte[] bt = new byte[512000];
                    if (file.exists()) {
                        FileOutputStream fos = new FileOutputStream(file);
                        BufferedOutputStream bos = new BufferedOutputStream(fos, 512000);
                        long l = 0;
                        while ((count = zis.read(bt, 0, 512000)) != -1) {
                            l += (int) (count * 0.91f);
                            bos.write(bt, 0, count);
                        }
                        bos.flush();
                        bos.close();
                        fos.close();
                        Log.d(TAG, "copyFfmpegV2sh: 6");
                    }
                    break;
                }
            }
            if (file.setExecutable(true, false) == false) {
                file.delete();
                Log.d(TAG, "copyFfmpegV2sh: 8");
            }
            Log.d(TAG, "copyFfmpegV2sh: 7");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (bais != null)
                try {
                    bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (zis != null)
                try {
                    zis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        chmod("chmod 777 " + StorageUtil.getFfmpegV2sh());
    }

    public static void uninstallCore() {
        synchronized (Utils.class) {
            Log.d(TAG, "uninstallCore: ");
            File file = new File(StorageUtil.getFfmpegV2sh());
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public static void command(String command) {
        Log.d(TAG, "command: ");
        Log.d(TAG, "command: command=" + command);
        Process process = null;
        DataOutputStream dos = null;
        DataInputStream dis = null;
        DataInputStream des = null;
        try {
            process = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(process.getOutputStream());
            dos.writeBytes(command + "\n");
            dos.writeBytes("exit\n");
            dos.flush();
            process.waitFor();
            dis = new DataInputStream(process.getInputStream());
            des = new DataInputStream(process.getErrorStream());
            while (dis.available() > 0)
                Log.d("SC", "stdout: " + dis.readLine() + "\n");
            while (des.available() > 0)
                Log.d("SC", "stderr: " + des.readLine() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null)
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (dis != null)
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (des != null)
                try {
                    des.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (process != null)
                try {
                    process.destroy();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }
}
