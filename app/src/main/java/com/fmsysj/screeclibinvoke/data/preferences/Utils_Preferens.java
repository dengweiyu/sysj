package com.fmsysj.screeclibinvoke.data.preferences;

import android.util.Log;

import com.fmsysj.screeclibinvoke.data.model.configuration.RecordingSetting;
import com.li.videoapplication.data.preferences.Constants;
import com.li.videoapplication.data.preferences.DefaultPreferences;
import com.li.videoapplication.data.preferences.NormalPreferences;
import com.li.videoapplication.data.preferences.PreferencesHepler;


public class Utils_Preferens {

    public static final String TAG = Utils_Preferens.class.getSimpleName();

    /**s
     * 迁移并删除2.1.0以前录屏设置
     */
    public static void copyRecordingSetting() {
        if (!NormalPreferences.getInstance().getBoolean(Constants.COPY_RECORDING_SETTING, false)) {
            try {
                copyPreferences();
            } catch (Exception e) {
                e.printStackTrace();
            }
            NormalPreferences.getInstance().putBoolean(Constants.COPY_RECORDING_SETTING, true);
        }
    }

    private static void copyPreferences() {
        Log.d(TAG, "copyPreferences: ");
        RecordingSetting recordingSetting = new RecordingSetting();
        boolean record_sound = DefaultPreferences.getInstance().getBoolean("record_sound", true);
        boolean yahangjieping = DefaultPreferences.getInstance().getBoolean("yahangjieping", false);
        boolean no_float_view_record = DefaultPreferences.getInstance().getBoolean("no_float_view_record", false);
        boolean isGotoVideoManage = DefaultPreferences.getInstance().getBoolean("isGotoVideoManage", true);
        boolean show_front_camera = DefaultPreferences.getInstance().getBoolean("show_front_camera", false);
        boolean show_touch_view = DefaultPreferences.getInstance().getBoolean("show_touch_view", false);
        boolean PackageInfoGridviewNotify = DefaultPreferences.getInstance().getBoolean("PackageInfoGridviewNotify", true);

        recordingSetting.setSoundRecording(record_sound);
        recordingSetting.setShakeRecording(yahangjieping);
        recordingSetting.setFloatingWindiws(!no_float_view_record);
        recordingSetting.setRecordedJump(isGotoVideoManage);
        recordingSetting.setAnchorModel(show_front_camera);
        recordingSetting.setGameScan(PackageInfoGridviewNotify);
        recordingSetting.setTouchPosition(show_touch_view);

        String quality_of_video = DefaultPreferences.getInstance().getString("quality_of_video", "0");
        // 0 超清
        // 1 标清
        // 2 超高清 （4.4以下没有）
        switch (quality_of_video) {
            case "0":
                recordingSetting.setQuality(RecordingSetting.QUALITY_HIGH);
                break;
            case "1":
                recordingSetting.setQuality(RecordingSetting.QUALITY_STANDARD);
                break;
            case "2":
                recordingSetting.setQuality(RecordingSetting.QUALITY_ULTRA_HIGH);
                break;
        }

        DefaultPreferences.getInstance().remove("record_sound");
        DefaultPreferences.getInstance().remove("yahangjieping");
        DefaultPreferences.getInstance().remove("no_float_view_record");
        DefaultPreferences.getInstance().remove("isGotoVideoManage");
        DefaultPreferences.getInstance().remove("show_front_camera");
        DefaultPreferences.getInstance().remove("show_touch_view");
        DefaultPreferences.getInstance().remove("PackageInfoGridviewNotify");
        DefaultPreferences.getInstance().remove("quality_of_video");

        // 保存录屏设置
        PreferencesHepler.getInstance().saveRecordingSetting(recordingSetting);
    }
}
