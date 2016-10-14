package com.li.videoapplication.data.cache;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 功能：保存实体类
 */
public class BaseUtils {

    private static final String TAG = BaseUtils.class.getSimpleName();

    public static String UrlEncodeUnicode(final String s) {
        if (s == null) {
            return null;
        }
        final int length = s.length();
        final StringBuilder builder = new StringBuilder(length); // buffer
        for (int i = 0; i < length; i++) {
            final char ch = s.charAt(i);
            if ((ch & 0xff80) == 0) {
                if (BaseUtils.isSafe(ch)) {
                    builder.append(ch);
                } else if (ch == ' ') {
                    builder.append('+');
                } else {
                    builder.append('%');
                    builder.append(BaseUtils.toHex((ch >> 4) & 15));
                    builder.append(BaseUtils.toHex(ch & 15));
                }
            } else {
                builder.append("%u");
                builder.append(BaseUtils.toHex((ch >> 12) & 15));
                builder.append(BaseUtils.toHex((ch >> 8) & 15));
                builder.append(BaseUtils.toHex((ch >> 4) & 15));
                builder.append(BaseUtils.toHex(ch & 15));
            }
        }
        return builder.toString();
    }

    public static char toHex(final int n) {
        if (n <= 9) {
            return (char) (n + 0x30);
        }
        return (char) ((n - 10) + 0x61);
    }

    public static boolean isSafe(final char ch) {
        if ((((ch >= 'a') && (ch <= 'z')) || ((ch >= 'A') && (ch <= 'Z'))) || ((ch >= '0') && (ch <= '9'))) {
            return true;
        }
        switch (ch) {
            case '\'':
            case '(':
            case ')':
            case '*':
            case '-':
            case '.':
            case '_':
            case '!':
                return true;
        }
        return false;
    }

    // ----------------------------------------------------------------------------------------------------------------------------
    // MD5相关函数
    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * MD5运算
     *
     * @param s
     * @return String 返回密文
     */
    public static String getMd5(final String s) {
        try {
            // Create MD5 Hash
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.trim().getBytes());
            final byte messageDigest[] = digest.digest();
            return BaseUtils.toHexString(messageDigest);
        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 转换为十六进制字符串
     *
     * @param b byte数组
     * @return String byte数组处理后字符串
     */
    public static String toHexString(final byte[] b) {// String to byte
        final StringBuilder sb = new StringBuilder(b.length * 2);
        for (final byte element : b) {
            sb.append(BaseUtils.HEX_DIGITS[(element & 0xf0) >>> 4]);
            sb.append(BaseUtils.HEX_DIGITS[element & 0x0f]);
        }
        return sb.toString();
    }

    /**
     * 检查是否安装了sd卡
     *
     * @return false 未安装
     */
    public static boolean sdcardMounted() {
        final String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED) && !state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            return true;
        }
        return false;
    }

    /**
     * 获取SD卡剩余空间的大小
     *
     * @return long SD卡剩余空间的大小（单位：byte）
     */
    public static long getSdcardSize() {
        final String str = Environment.getExternalStorageDirectory().getPath();
        final StatFs localStatFs = new StatFs(str);
        final long blockSize = localStatFs.getBlockSize();
        return localStatFs.getAvailableBlocks() * blockSize;
    }

    /**
     * 保存对象到本地
     */
    public static final void saveObject(String path, Object o) {
        Log.d(TAG, "saveObject/path=" + path);
        Log.d(TAG, "saveObject/o=" + o);
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        File f = new File(path);
        try {
            fos = new FileOutputStream(f);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(o);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从本地取出对象
     */
    public static final Object restoreObject(String path) {
        Log.d(TAG, "restoreObject/path=" + path);
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        Object object = null;
        File f = new File(path);
        if (!f.exists()) {
            return null;
        }
        try {
            fis = new FileInputStream(f);
            ois = new ObjectInputStream(fis);
            object = ois.readObject();
            return object;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    private final static int BUFFER = 1024;

    /**
     * 保存输入流到本地
     */
    public static final void saveStream(String path, InputStream is) {
        Log.d(TAG, "saveStream/path=" + path);
        Log.d(TAG, "saveStream/is=" + is);
        if (is == null)
            return;
        File f = null;
        try {
            f = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (f == null)
            return;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            byte[] b = new byte[BUFFER];
            int len = -1;
            while ((len = is.read(b)) != -1) {
                fos.write(b, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 保存输入流到本地（流来自文本文件）
     */
    public static final void saveStringStream(String path, InputStream is) {
        InputStream inputStream = String2InputStream(InputStream2String(is));
        saveStream(path, inputStream);
    }

    /**
     * 从本地取出流
     */
    public static final InputStream restoreStream(String path) {
        Log.d(TAG, "restoreStream/path=" + path);
        File f = null;
        try {
            f = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (f == null && !f.exists()) {
            return null;
        }
        FileInputStream fis;
        try {
            fis = new FileInputStream(f);
            return fis;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream String2InputStream(String content) {
        if (content == null)
            return null;
        InputStream is = new ByteArrayInputStream(content.getBytes());
        return is;
    }

    public static String InputStream2String(InputStream is) {
        if (is == null)
            return null;
        InputStreamReader reader = new InputStreamReader(is, Charset.defaultCharset());
        BufferedReader bf = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer();
        String line;
        try {
            while ((line = bf.readLine()) != null) {
                buffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (is != null)
                is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
