package com.li.videoapplication.data.image;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;

public class VideoCover {

    public static final String TAG  = VideoCover.class.getSimpleName();

    // ---------------------------------------------------------------------------------

    /**
     * 从视频中生成一张图片
     */
    public static Bitmap generateBitmap(String videoPath) {
        Log.d(TAG, "generateBitmap: // ---------------------------------------");
        Log.d(TAG, "generateBitmap: videoPath=" + videoPath);
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(videoPath);
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            int seconds = Integer.valueOf(time);
            bitmap = retriever.getFrameAtTime(seconds / 2 * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            Log.d(TAG, "generateBitmap: true");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 从视频中生成一张图片
     */
    public static Bitmap generateBitmap_(String videoPath) {
        Log.d(TAG, "generateBitmap_: // ---------------------------------------");
        Log.d(TAG, "generateBitmap: videoPath=" + videoPath);
        Bitmap bitmap = null;
        try {
            bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MINI_KIND);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 将位图进行局部截取
     */
    public static Bitmap transformBitmap(Bitmap srcBitmap) {
        Log.d(TAG, "transformBitmap: // ---------------------------------------");
        int scrWidth = srcBitmap.getWidth();
        int scrHeight = srcBitmap.getHeight();
        Bitmap dstBitmap;
        if (scrWidth < scrHeight) {
            int widthleft = 0;
            int heightleft = (scrHeight - scrWidth * 9 / 16) / 2;
            dstBitmap = Bitmap.createBitmap(srcBitmap, widthleft, heightleft, scrWidth, scrWidth * 9 / 16);
        } else {
            dstBitmap = srcBitmap;
        }
        Log.d(TAG, "transformBitmap: true");
        return dstBitmap;
    }
}
