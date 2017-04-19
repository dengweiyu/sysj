package com.li.videoapplication.tools;

import android.util.Log;
import android.widget.TextView;
import android.widget.VideoView;


import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.ui.srt.SRT;
import com.li.videoapplication.ui.srt.SRTInfo;
import com.li.videoapplication.ui.srt.SRTReader;
import com.li.videoapplication.ui.srt.SRTTimeFormat;
import com.pili.pldroid.player.widget.PLVideoView;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 播放字幕
 * SubtitleHelper和SubtitleHelper2的区别，仅仅只有videoview的包不同。
 * pldroidplayer为新更换的播放器，解决播放到中间出错
 */
public class SubtitleHelper {

    public static final String TAG = SubtitleHelper.class.getSimpleName();

    // ---------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------

    private AtomicBoolean isPlaying = new AtomicBoolean(false);
    private SRTInfo info;
    private String path;
    private VideoView videoView;
    private TextView text;

    public boolean isPlaying() {
        return isPlaying.get();
    }

    public void setPlaying(boolean playing) {
        isPlaying.set(playing);
    }

    public SubtitleHelper(String path, VideoView videoView, TextView text) {
        super();
        this.path = path;
        this.videoView = videoView;
        this.text = text;

        readSubtitle(path);
    }

    public void playSubtitle() {

        if (info != null && isPlaying.get()) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    while (isPlaying.get()) {
                        Log.d(TAG, "playSubtitle: playing...");
                        long position = 0;
                        try {
                            position = videoView.getCurrentPosition();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        showSubtitle(position);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, "newThread-SubtitleHelper").start();
        }
    }

    /**
     * 显示字幕
     */
    public void showSubtitle(long position) {
        if (info != null) {
            boolean hasSubtitle = false;
            for (final SRT srt : info) {
                String startDate = SRTTimeFormat.format(srt.startTime);
                String endDate = SRTTimeFormat.format(srt.endTime);

                long starTime = string2Msec(startDate);
                long endTime = string2Msec(endDate);

                if (position > starTime && position < endTime) {
                    hasSubtitle = true;
                    UITask.post(new Runnable() {
                        @Override
                        public void run() {
                            if (srt.text != null && srt.text.size() > 0)
                                text.setText(srt.getText());
                        }
                    });
                }
                if (hasSubtitle)
                    break;
            }
            if (!hasSubtitle)
                UITask.post(new Runnable() {
                    @Override
                    public void run() {
                        text.setText("");
                    }
                });
        }
    }

    /**
     * 读取本地字幕文件
     */
    private void readSubtitle(String path) {
        if (path != null) {
            File file = null;
            try {
                file = new File(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (file != null && file.exists()) {
                try {
                    info = SRTReader.read(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 00:04:11,297转换为255000
     */
    public long string2Msec(String time) {
        String STime = time.substring(0, time.lastIndexOf(","));

        String[] my = STime.split(":");
        int hour = Integer.parseInt(my[0]);
        int min = Integer.parseInt(my[1]);
        int sec = Integer.parseInt(my[2]);
        long totalSec = (hour * 3600 + min * 60 + sec) * 1000;
        return totalSec;
    }
}
