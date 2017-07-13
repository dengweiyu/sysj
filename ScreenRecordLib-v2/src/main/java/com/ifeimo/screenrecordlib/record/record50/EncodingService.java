package com.ifeimo.screenrecordlib.record.record50;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.util.Log;
import android.view.Surface;

import com.ifeimo.screenrecordlib.constant.Configuration;
import com.ifeimo.screenrecordlib.constant.Constant;
import com.ifeimo.screenrecordlib.RecordingManager;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by y on 2017/5/25.
 */

public class EncodingService extends Service {
    private static final String TAG = EncodingService.class.getSimpleName();

    private static final boolean DEBUG = false;

    public static final int CLIENT_MSG = 1;

    private Messenger mClientMessenger;
    private Messenger mServiceMessenger = new Messenger(new ServiceHandler());

    private MediaProjection projection;
    private static Handler handler = new Handler(Looper.getMainLooper());
    private static MediaMuxer mediaMuxer;
    private MediaCodec.BufferInfo videoInfo = new MediaCodec.BufferInfo();
    private MediaCodec.BufferInfo audioinfo = new MediaCodec.BufferInfo();
    protected MediaCodec videoCodec, audioCodec;
    private VirtualDisplay virtualDisplay;
    private Surface surface;
    private AudioRecord audioRecord;

    private int iframe = 2;

    private final int SAMPLE_RATE = 44100;
    private final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private final int SIZE_PER_FRAME = 1024;
    private final int FRAMES = 25;

    // 锁
    private static final Object sync = new Object();
    // 用于判断锁
    private static AtomicBoolean isStartRecord = new AtomicBoolean(false);

    private static AtomicBoolean isStopRecording = new AtomicBoolean(false);
    private static AtomicBoolean isPauseRecording = new AtomicBoolean(false);
    private AtomicBoolean isMuxerStarted = new AtomicBoolean(false);
    private AtomicBoolean isAudioStarted = new AtomicBoolean(false);
    private int MUXER_TRACK_NUMBER = 1;
    private int muxerTrackNumber = 0;
    private long mediaStartTime = 0;
    private static long pts = 0;           // 时间戳的缩减量
    private static long ptsPause = 0;      // 暂停时的时间戳
    private static long ptsRestart = 0;    // 重启时的时间戳
    protected static final int TIMEOUT_MSEC = 10000; // 10毫秒

    private int newVideoIndex = 0;
    private int newAudioIndex = 0;

    private static String path;
    private static Configuration configuration;

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate--------->");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand--------->");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mServiceMessenger.getBinder();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy--------->");
        super.onDestroy();
    }

    public static void startRecording2(String path, Configuration configuration){
        EncodingService.path = path;
        EncodingService.configuration = configuration;
        synchronized (sync) {
            sync.notifyAll();
        }
    }

    public static void stopRecording() {
        restartRecording();
        if (mediaMuxer != null) {
            isStopRecording.set(true);
            Log.d(TAG, "stopRecording: true");
            RecordingManager.getInstance().onRecordingStoped();
        }
    }

    public static void pauseRecording() {
        if (mediaMuxer != null) {
            if (!isPauseRecording.get()) {
                isPauseRecording.set(true);
                ptsPause = System.nanoTime();
                Log.d(TAG, "pauseRecording: true");
                RecordingManager.getInstance().onRecordingPaused();
            }
        }
    }

    public static void restartRecording() {
        if (mediaMuxer != null) {
            if (isPauseRecording.get()) {
                isPauseRecording.set(false);
                ptsRestart = System.nanoTime() - 1000000000L;
                pts = pts + (ptsRestart - ptsPause);
                Log.d(TAG, "restartRecording: true");
                RecordingManager.getInstance().onRecordingResumed();
            }
        }
    }

    @TargetApi(21)
    private class ServiceHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case CLIENT_MSG:
                    Object[] tmp = (Object[])msg.obj;

                    final int resultCode = (int) tmp[0];
                    final Intent data = (Intent) tmp[1];
                    final MediaProjectionManager manager = (MediaProjectionManager)tmp[2];
                    path = (String) tmp[3];
                    configuration = (Configuration) tmp[4];

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_DISPLAY);

                            while (true) {
                                if (!isStartRecord.get() && !ScreenRecordActivity.isApplyForPermission) {
                                    isStartRecord.set(true);
                                    // 录屏参数
                                    int width;
                                    int height;
                                    int bitrate = configuration.getBitrate();
                                    int fps = configuration.getFps();
                                    int dpi = configuration.getDpi();
                                    boolean isAudio = configuration.isAudio();
                                    if (configuration.isLandscape()) {// 横屏
                                        width = configuration.getHeight();
                                        height = configuration.getWidth();
                                    } else {// 竖屏
                                        width = configuration.getWidth();
                                        height = configuration.getHeight();
                                    }

                                    projection = manager.getMediaProjection(resultCode, data);

                                    // 音频编码器
                                    prepareAudioEncoder(isAudio);
                                    // 视频编码器
                                    prepareVideoEncoder(width, height, bitrate, fps);

                                    isAudio = getAudioPermission(isAudio);
                                    Log.i(TAG, "isAudio======" + isAudio);
                                    if (DEBUG)
                                        Log.d(TAG, "muxer: MUXER_TRACK_NUMBER=" + MUXER_TRACK_NUMBER);
                                    // 初始化混合器
                                    prepareMuxer(path);
                                    // 初始化VirtualDisplay
                                    createVirtualDisplay(width, height);

                                    RecordingManager.getInstance().onRecordingStarted();
                                    RecordingManager.getInstance().sendMessage(Constant.Msg.STATE_RECORDING);
                                    // 开始计时
                                    RecordingManager.getInstance().startTiming();

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            RecordingManager.getInstance().onRecordingStarted2();
                                        }
                                    });

                                    if (DEBUG) Log.d(TAG, "codec: start");

                                    isStopRecording.set(false);
                                    isPauseRecording.set(false);

                                    pts = 0;
                                    ptsPause = 0;
                                    ptsRestart = 0;

                                    LOOP:
                                    while (!isStopRecording.get()) {
                                        if (!isPauseRecording.get()) {
                                            if (mediaMuxer == null) {
                                                break LOOP;
                                            }
                                            if (videoCodec == null) {
                                                break LOOP;
                                            }
                                            long nanoTime = System.nanoTime();
                                            // 取出录音器的数据，写入音频编码器
                                            if (isAudio && isAudioStarted.get() && isMuxerStarted.get()) {
                                                // 获取声音数据
                                                isAudio = getAudioBuffer(nanoTime);
                                            }

                                            nanoTime = System.nanoTime();
                                            // 视频编码
                                            drainVideoBuffer(nanoTime);

                                            nanoTime = System.nanoTime();
                                            // 音频编码
                                            drainAudioBuffer(isAudio, nanoTime);

                                            // 混合器开始工作
                                            if (!isMuxerStarted.get() && muxerTrackNumber == MUXER_TRACK_NUMBER) {
                                                mediaMuxer.start();
                                                isMuxerStarted.set(true);
                                                mediaStartTime = System.nanoTime();
                                                if (DEBUG) Log.d(TAG, "muxer: start");
                                            }
                                        }
                                    }

                                    Log.d(TAG, "codec: end");
                                    // 释放资源
                                    release();

                                    RecordingManager.getInstance().onRecordingCompleted();
                                    Log.i(TAG, "finish------------------>");
                                } else {
                                    Log.i(TAG, "lock------------------>");
                                    isStartRecord.set(false);
                                    if (ScreenRecordActivity.isApplyForPermission){
                                        ScreenRecordActivity.isApplyForPermission = false;
                                    }
                                    synchronized (sync){
                                        try {
                                            sync.wait();
                                        } catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    }, "newThread-EncodingService").start();

                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 配置视频编码器
     */
    @TargetApi(21)
    private void prepareVideoEncoder(int width, int height, int bitrate, int fps){
        MediaFormat format = MediaFormat.createVideoFormat("video/avc", width, height);
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        format.setInteger(MediaFormat.KEY_BIT_RATE, bitrate);
        format.setInteger(MediaFormat.KEY_FRAME_RATE, fps);
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, iframe);
        try {
            videoCodec = MediaCodec.createEncoderByType("video/avc");
            videoCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            surface = videoCodec.createInputSurface();
            videoCodec.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 配置音频编码器
     */
    private void prepareAudioEncoder(boolean isAudio){
        if (isAudio) {
            MediaFormat audioFormat = MediaFormat.createAudioFormat("audio/mp4a-latm", 44100, 1);
            audioFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
            audioFormat.setInteger(MediaFormat.KEY_CHANNEL_MASK, AudioFormat.CHANNEL_IN_MONO);
            audioFormat.setInteger(MediaFormat.KEY_BIT_RATE, 64000);
            audioFormat.setInteger(MediaFormat.KEY_CHANNEL_COUNT, 1);
            try {
                audioCodec = MediaCodec.createEncoderByType("audio/mp4a-latm");
                audioCodec.configure(audioFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
                audioCodec.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (audioCodec != null) {
                // 录音器
                int minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
                int bufferSize = SIZE_PER_FRAME * FRAMES;
                if (bufferSize < minBufferSize) {
                    bufferSize = (minBufferSize / SIZE_PER_FRAME + 1) * SIZE_PER_FRAME * 2;
                }

                audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE,
                        CHANNEL_CONFIG, AUDIO_FORMAT, bufferSize);
                if (audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
                    audioRecord.startRecording();
                    isAudioStarted.set(true);
                    MUXER_TRACK_NUMBER = 2;
                    if (DEBUG) Log.d(TAG, "audioRecord: start");
                }
            }
        }
    }

    private boolean getAudioPermission(boolean isAudio){
        if (isAudio) {
            if (audioRecord != null) {
                int capacity = SIZE_PER_FRAME * 3 / 2;
                ByteBuffer byteBuffer = ByteBuffer.allocateDirect(capacity);
                byteBuffer.clear();
                int readBytes = audioRecord.read(byteBuffer, capacity);
                if (readBytes > 0) {
                    Log.i(TAG, "getAudioPermission------>AudioRecord.SUCCESS");
                    return true;
                } else if (readBytes == AudioRecord.ERROR_INVALID_OPERATION) {
                    Log.i(TAG, "getAudioPermission------>AudioRecord.ERROR_INVALID_OPERATION");
                }
            }

            MUXER_TRACK_NUMBER = 1;
            return false;
        } else {
            return false;
        }

    }

    /**
     * 初始化混合器
     */
    @TargetApi(21)
    private void prepareMuxer(String path){
        try {
            mediaMuxer = new MediaMuxer(path, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化VirtualDisplay
     */
    @TargetApi(21)
    private void createVirtualDisplay(int width, int height){
        virtualDisplay = projection.createVirtualDisplay("display",
                width,
                height,
                1,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
                surface,
                null,
                null);
    }

    /**
     * 获取声音数据，写入编码器
     */
    @TargetApi(21)
    private boolean getAudioBuffer(long nanoTime){
        // 方法二：录制出来的视频有类似快进的效果
        int capacity = SIZE_PER_FRAME * 3 / 2;
        if (DEBUG) Log.d(TAG, "record: capacity=" + capacity);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(capacity);
        byteBuffer.clear();
        int readBytes = audioRecord.read(byteBuffer, capacity);
        if (DEBUG) Log.d(TAG, "record: readBytes=" + readBytes);
        if (readBytes > 0) {
            int inputIndex = audioCodec.dequeueInputBuffer(TIMEOUT_MSEC);
            if (DEBUG)
                Log.d(TAG, "record: inputIndex=" + inputIndex);
            if (inputIndex >= 0) {
                byteBuffer.position(readBytes);
                byteBuffer.flip();
                ByteBuffer inputBuffer = audioCodec.getInputBuffer(inputIndex);
                inputBuffer.clear();
                inputBuffer.put(byteBuffer);
                audioCodec.queueInputBuffer(inputIndex, 0, readBytes,
                        (nanoTime - mediaStartTime - pts) / 1000, 0);
            }
        } else if (readBytes == AudioRecord.ERROR_INVALID_OPERATION){
            Log.i(TAG, "AudioRecord.ERROR_INVALID_OPERATION");
            MUXER_TRACK_NUMBER = 1;
            return false;
        }

        return true;
    }

    /**
     * 视频编码
     */
    @TargetApi(21)
    private void drainVideoBuffer(long nanoTime){
        int videoIndex = videoCodec.dequeueOutputBuffer(videoInfo, TIMEOUT_MSEC);
        if (DEBUG) Log.d(TAG, "video: videoIndex=" + videoIndex);
        if (videoIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {// -1

        } else if (videoIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {// -2
            if (!isMuxerStarted.get()) {
                newVideoIndex = mediaMuxer.addTrack(videoCodec.getOutputFormat());
                muxerTrackNumber++;
                if (DEBUG)
                    Log.d(TAG, "video: muxerTrackNumber=" + muxerTrackNumber);
            }
        } else if (videoIndex > 0 && isMuxerStarted.get()) {// 写入混合器
            ByteBuffer outputBuffer = videoCodec.getOutputBuffer(videoIndex);
            if (outputBuffer != null) {
                if ((videoInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
                    videoInfo.size = 0;
                }
                if (videoInfo.size != 0) {
                    outputBuffer.position(videoInfo.offset);
                    outputBuffer.limit(videoInfo.offset + videoInfo.size);
                    videoInfo.presentationTimeUs = (nanoTime - mediaStartTime - pts) / 1000;
                    mediaMuxer.writeSampleData(newVideoIndex, outputBuffer, videoInfo);
                    Log.i("ScreenRecordActivity", "video outputBuffer=" + outputBuffer);
                }
                videoCodec.releaseOutputBuffer(videoIndex, false);
            }
        }
    }

    @TargetApi(21)
    private void drainAudioBuffer(boolean isAudio, long nanoTime){
        if (isAudio && isAudioStarted.get()) {
            int audioIndex = audioCodec.dequeueOutputBuffer(audioinfo, TIMEOUT_MSEC);
            if (DEBUG) Log.d(TAG, "audio: audioIndex=" + audioIndex);
            //check audio index
            if (audioIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {// -1

            } else if (audioIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {// -2
                if (!isMuxerStarted.get()) {
                    newAudioIndex = mediaMuxer.addTrack(audioCodec.getOutputFormat());
                    Log.i(TAG, "newAudioIndex=" + newAudioIndex);
                    muxerTrackNumber++;
                    if (DEBUG)
                        Log.d(TAG, "audio: muxerTrackNumber=" + muxerTrackNumber);
                }
            } else if (audioIndex > 0 && isMuxerStarted.get()) {// 写入混合器
                ByteBuffer outputBuffer = audioCodec.getOutputBuffer(audioIndex);
                if (outputBuffer != null) {
                    if ((audioinfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
                        audioinfo.size = 0;
                    }
                    if (audioinfo.size != 0) {
                        outputBuffer.position(audioinfo.offset);
                        outputBuffer.limit(audioinfo.offset + audioinfo.size);
                        audioinfo.presentationTimeUs = (nanoTime - mediaStartTime - pts) / 1000;
                        mediaMuxer.writeSampleData(newAudioIndex, outputBuffer, audioinfo);
                    }
                    audioCodec.releaseOutputBuffer(audioIndex, false);
                }
            }
        }
    }

    /**
     * 释放资源
     */
    @TargetApi(21)
    private void release(){
        if (mediaMuxer != null) {
            // IllegalStateException: Failed to stop the muxer
            // There are no sync frames for video track
            // 5.0 权限问题
            try {
                mediaMuxer.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                mediaMuxer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mediaMuxer = null;

            isMuxerStarted.set(false);
            muxerTrackNumber = 0;

            if (DEBUG) Log.d(TAG, "muxer: stop");
        }
        if (videoCodec != null) {
            videoCodec.stop();
            videoCodec.release();
            videoCodec = null;
            if (DEBUG) Log.d(TAG, "video: stop");
        }
        if (audioCodec != null) {
            audioCodec.stop();
            audioCodec.release();
            audioCodec = null;
            if (DEBUG) Log.d(TAG, "audio: stop");
        }
        if (projection != null) {
            projection.stop();
            if (DEBUG) Log.d(TAG, "projection: stop");
        }
        if (surface != null) {
            surface.release();
            if (DEBUG) Log.d(TAG, "surface: release");
        }
        if (virtualDisplay != null) {
            virtualDisplay.release();
            if (DEBUG) Log.d(TAG, "virtualDisplay: release");
        }
        if (audioRecord != null) {
            audioRecord.setRecordPositionUpdateListener(null);
            if (audioRecord.getRecordingState() == AudioRecord.STATE_INITIALIZED &&
                    audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_STOPPED) {
                try {
                    audioRecord.stop();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
            audioRecord.release();
            audioRecord = null;
            if (DEBUG) Log.d(TAG, "audioRecord: stop");
        }
    }
}
