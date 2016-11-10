package com.fmsysj.screeclibinvoke.logic.videoedit;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;


/**
 * 声效
 */
public class SoundHelper {

    /**
     * 播放系统声效
     */
    public static void playSystem() {
        SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        soundPool.play(1, 1, 1, 0, 0, 1);
    }

    /**
     * 播放截屏声效
     */
    public static void playCapture() {
        Context context = AppManager.getInstance().getContext();
        SoundPool soundPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 5);
        int id = soundPool.load(context, R.raw.kuaimen, 1);
        soundPool.play(id, 1, 1, 1, 0, 1);

    }
}
