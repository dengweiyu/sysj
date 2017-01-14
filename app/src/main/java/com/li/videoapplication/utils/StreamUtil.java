package com.li.videoapplication.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 *  流的转化
 */
public class StreamUtil {

    public static final String TAG = StreamUtil.class.getSimpleName();

    // ---------------------------------------------------------------------------

    public static void copyStream(InputStream is, OutputStream os) {
        final int bufferSize = 1024;
        try {
            byte[] bytes = new byte[bufferSize];
            for (;;) {
                int count = is.read(bytes, 0, bufferSize);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ---------------------------------------------------------------------------

    public static void closeInputStream(InputStream is) {
        if (is != null)
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static void closeOutputStream(OutputStream os) {
        if (os != null)
            try {
                os.flush();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static void flushOutputStream(OutputStream os) {
        if (os != null)
            try {
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    // ---------------------------------------------------------------------------

    /**
     * Bitmap
     */
    public static byte[] fromBitmap(Bitmap bitmap){
        if (bitmap == null)
            return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 字节数组
     */
    public static byte[] toByteArray(InputStream is) {
        if (is == null)
            return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int len;
        try {
            while (-1 != (len = is.read(buffer))) {
                baos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    /**
     * 字符窜（默认编码）
     */
    public static String toStringDefault(InputStream is) {
        if (is == null)
            return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len;
        try {
            while((len = is.read()) != -1){
                baos.write(len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "toStringDefault: " + baos.toString());
        return baos.toString();
    }

    /**
     * 字符窜（UTF-8）
     */
    public static String toStringUTF8(InputStream is) {
        return toString(is, "UTF-8");
    }

    /**
     * 字符窜
     *
     * @param charsetName "UTF-8"
     */
    public static String toString(InputStream is, String charsetName) {
        if (is == null)
            return null;
        if (charsetName == null)
            return null;
        InputStreamReader streamReader = null;
        try {
            streamReader = new InputStreamReader(is, charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (streamReader != null) {
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            StringBuffer buffer = new StringBuffer();
            String line;
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "toString: " + buffer.toString());
            return buffer.toString();
        }
        return null;
    }

    // ---------------------------------------------------------------------------

    /**
     * Bitmap
     */
    public static Bitmap toBitmap(InputStream is) {
        if (is == null)
            return null;
        return BitmapFactory.decodeStream(is);
    }

    /**
     * Bitmap
     */
    public static Bitmap toBitmap(byte[] bytes) {
        return toBitmap(bytes, null);
    }

    /**
     * Bitmap
     */
    public static Bitmap toBitmap(byte[] bytes, BitmapFactory.Options options) {
        if (bytes == null)
            return null;
        if (options != null)
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        else
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * 字节数组
     */
    public static InputStream fromByteArray(byte[] buffer) {
        try {
            return new ByteArrayInputStream(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文件
     */
    public static InputStream fromFile(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * assets
     */
    public static InputStream fromAssets(Context context, String assets) {
        try {
            return context.getAssets().open(assets);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * raw
     */
    public static InputStream fromRaw(Context context, int raw) {
        try {
            return context.getResources().openRawResource(raw);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字符窜（默认编码）
     */
    public static InputStream fromStringDefault(String string) {
        if (string == null)
            return null;
        try {
            return new ByteArrayInputStream(string.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字符窜（UTF-8）
     */
    public static InputStream fromStringUTF8(String string) {
        return fromString(string, "UTF-8");
    }

    /**
     * 字符窜
     *
     * @param charsetName "UTF-8"
     */
    public static InputStream fromString(String string, String charsetName) {
        if (string == null)
            return null;
        if (charsetName == null)
            return null;
        try {
            return new ByteArrayInputStream(string.getBytes(charsetName));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
