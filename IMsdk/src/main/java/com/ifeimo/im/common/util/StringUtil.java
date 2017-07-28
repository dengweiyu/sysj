package com.ifeimo.im.common.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ifeimo.im.framwork.IMSdk;

import java.util.IllegalFormatException;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by lpds on 2017/2/15.
 */
public class StringUtil {


    private static final String TAG = "Im.StringUtil";

    /**
     * str == null || str.equals("")
     * @param str
     * @return
     */
    public static boolean isNull(String str){
        return str == null || str.equals("");
    }

    /**
     * 复制到剪切板
     * @param str
     */
    public static void copy(String str){
        ClipboardManager manager = (ClipboardManager) IMSdk.CONTEXT.getSystemService(CLIPBOARD_SERVICE);
        manager.setPrimaryClip(ClipData.newPlainText(str, str));
//        Toast.makeText(context, "复制成功！", Toast.LENGTH_SHORT).show();
    }

    public static final boolean isNumber(String number){
        try{
            Long.parseLong(number);
            return true;
        }catch (NumberFormatException ex){
            ex.printStackTrace();
            return false;
        }
    }

}
