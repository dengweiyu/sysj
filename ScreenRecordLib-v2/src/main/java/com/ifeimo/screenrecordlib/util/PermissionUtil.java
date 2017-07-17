package com.ifeimo.screenrecordlib.util;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by y on 2017/5/31.
 */

public class PermissionUtil {
    private static final String TAG = PermissionUtil.class.getSimpleName();

    public static final int OP_NONE = -1;
    public static final int OP_COARSE_LOCATION = 0;
    public static final int OP_FINE_LOCATION = 1;
    public static final int OP_GPS = 2;
    public static final int OP_VIBRATE = 3;
    public static final int OP_READ_CONTACTS = 4;
    public static final int OP_WRITE_CONTACTS = 5;
    public static final int OP_READ_CALL_LOG = 6;
    public static final int OP_WRITE_CALL_LOG = 7;
    public static final int OP_READ_CALENDAR = 8;
    public static final int OP_WRITE_CALENDAR = 9;
    public static final int OP_WIFI_SCAN = 10;
    public static final int OP_POST_NOTIFICATION = 11;
    public static final int OP_NEIGHBORING_CELLS = 12;
    public static final int OP_CALL_PHONE = 13;
    public static final int OP_READ_SMS = 14;
    public static final int OP_WRITE_SMS = 15;
    public static final int OP_RECEIVE_SMS = 16;
    public static final int OP_RECEIVE_EMERGECY_SMS = 17;
    public static final int OP_RECEIVE_MMS = 18;
    public static final int OP_RECEIVE_WAP_PUSH = 19;
    public static final int OP_SEND_SMS = 20;
    public static final int OP_READ_ICC_SMS = 21;
    public static final int OP_WRITE_ICC_SMS = 22;
    public static final int OP_WRITE_SETTINGS = 23;
    // 悬浮窗权限
    public static final int OP_SYSTEM_ALERT_WINDOW = 24;
    public static final int OP_ACCESS_NOTIFICATIONS = 25;
    public static final int OP_CAMERA = 26;
    // 录音权限
    public static final int OP_RECORD_AUDIO = 27;
    public static final int OP_PLAY_AUDIO = 28;
    public static final int OP_READ_CLIPBOARD = 29;
    public static final int OP_WRITE_CLIPBOARD = 30;
    public static final int OP_TAKE_MEDIA_BUTTONS = 31;
    public static final int OP_TAKE_AUDIO_FOCUS = 32;
    public static final int OP_AUDIO_MASTER_VOLUME = 33;
    public static final int OP_AUDIO_VOICE_VOLUME = 34;
    public static final int OP_AUDIO_RING_VOLUME = 35;
    public static final int OP_AUDIO_MEDIA_VOLUME = 36;
    public static final int OP_AUDIO_ALARM_VOLUME = 37;
    public static final int OP_AUDIO_NOTIFICATION_VOLUME = 38;
    public static final int OP_AUDIO_BLUETOOTH_VOLUME = 39;
    public static final int OP_WAKE_LOCK = 40;
    public static final int OP_MONITOR_LOCATION = 41;
    public static final int OP_MONITOR_HIGH_POWER_LOCATION = 42;
    public static final int OP_GET_USAGE_STATS = 43;
    public static final int OP_MUTE_MICROPHONE = 44;
    public static final int OP_TOAST_WINDOW = 45;
    public static final int OP_PROJECT_MEDIA = 46;
    public static final int OP_ACTIVATE_VPN = 47;
    public static final int OP_WRITE_WALLPAPER = 48;
    public static final int OP_ASSIST_STRUCTURE = 49;
    public static final int OP_ASSIST_SCREENSHOT = 50;
    public static final int OP_READ_PHONE_STATE = 51;
    public static final int OP_ADD_VOICEMAIL = 52;
    public static final int OP_USE_SIP = 53;
    public static final int OP_PROCESS_OUTGOING_CALLS = 54;
    public static final int OP_USE_FINGERPRINT = 55;
    public static final int OP_BODY_SENSORS = 56;
    public static final int OP_READ_CELL_BROADCASTS = 57;
    public static final int OP_MOCK_LOCATION = 58;
    public static final int OP_READ_EXTERNAL_STORAGE = 59;
    public static final int OP_WRITE_EXTERNAL_STORAGE = 60;
    public static final int OP_TURN_SCREEN_ON = 61;
    public static final int _NUM_OP = 62;

    /**
     * 判断权限是否开启
     * @param context
     * @return
     */
    public static boolean isOpAllowed(Context context, int op) {
        final int version = Build.VERSION.SDK_INT;

        if (version >= 19) {
            return checkOp(context, op);
        }

        return false;
    }

    /**
     * 检查权限
     * @param context
     * @param op
     * @return
     */
    private static boolean checkOp(Context context, int op) {
        final int version = Build.VERSION.SDK_INT;

        if (version >= 19) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                if (AppOpsManager.MODE_ALLOWED == invokeMethod(manager, "checkOp", op,
                        Binder.getCallingUid(), context.getPackageName())) {  //这儿反射就自己写吧
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else {
            Log.e(TAG,"Below API 19 cannot invoke!");
        }
        return false;
    }

    /**
     * 调用函数
     * @param manager
     * @param method
     * @param op
     * @param callingUid
     * @param packageName
     * @return
     * @throws Exception
     */
    @TargetApi(19)
    private static int invokeMethod(AppOpsManager manager, String method, int op, int callingUid, String packageName) throws Exception{
        Class<AppOpsManager> clazz = AppOpsManager.class;
        Method dispatchMethod = clazz.getMethod(method, new Class[]{int.class, int.class, String.class});
        int mode = (Integer) dispatchMethod.invoke(manager, new Object[]{op, callingUid, packageName});
        return mode;
    }
}
