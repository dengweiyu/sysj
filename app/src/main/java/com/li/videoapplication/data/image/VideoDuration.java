package com.li.videoapplication.data.image;

import android.media.MediaMetadataRetriever;
import android.util.Log;

public class VideoDuration {

    public static final String TAG  = VideoDuration.class.getSimpleName();

    // ---------------------------------------------------------------------------------

    public static String getDuration(String videoPath) {
        Log.d(TAG, "getDuration: // ---------------------------------------");
        Log.d(TAG, "getDuration: videoPath=" + videoPath);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        String duration = null;
        try {
            retriever.setDataSource(videoPath);
            duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Log.d(TAG, "getDuration: duration=" + duration);
        return duration;
    }
}
