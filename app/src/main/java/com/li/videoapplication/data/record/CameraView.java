package com.li.videoapplication.data.record;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.li.videoapplication.data.local.FileOperateUtil;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.utils.ScreenUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * 和Camera绑定的SurfaceView 封装了拍照方法
 */
public class CameraView extends SurfaceView implements CameraOperation {

    public final static String TAG = CameraView.class.getSimpleName();

    private Camera camera;
    private Camera.Parameters parameters;
    private SurfaceHolder holder;

    /**
     * 默认最大录制时间（10分）
     */
    private static final long MAXTIME_DEFAULT = 10 * 1000;

    /**
     * 默认录制视频旋转角度（0）
     */
    private static final int RECORDORIENTATION_DEFAULT = 90;

    /**
     * 最大录制时间（毫秒）
     */
    private long maxTime = MAXTIME_DEFAULT;

    /**
     * 录制视频旋转角度
     */
    private int recordOrientation = RECORDORIENTATION_DEFAULT;

    /**
     * 闪光灯类型  默认为关闭
     */
    private FlashMode flashMode = FlashMode.OFF;

    /**
     * 视频质量  默认为高质量
     */
    private RecorderQuality recorderQuality = RecorderQuality.HIGHT;

    /**
     * 缩放级别（0 ~ maxView）  默认为0
     */
    private int zoom = 0;

    /**
     * 屏幕旋转角度  默认为0
     */
    private int orientation = 0;

    /**
     * 是否打开前置相机  true为前置  false为后置
     */
    private boolean isOpenFrontCamera;

    /**
     * 录像类
     */
    private MediaRecorder mediaRecorder;

    /**
     * 录像存放路径 ，用以生成缩略图
     */
    private String recordPath;

    public CameraView(Context context) {
        this(context, null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        isOpenFrontCamera = false;
        screenWidth = ScreenUtil.getScreenWidth();
        screenHeight = ScreenUtil.getScreenHeight();
        screenRatio = ((float) screenHeight) / ((float) screenHeight);
        Log.d(TAG, "setCameraParameters/screenWidth=" + screenWidth);
        Log.d(TAG, "setCameraParameters/screenHeight=" + screenHeight);
        Log.d(TAG, "setCameraParameters/screenRatio=" + screenRatio);
        holder = getHolder();
        holder.addCallback(callback);
        setOnTouchListener(onTouchListener);
    }

    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            CameraView.this.holder = holder;
            Log.i(TAG, "surfaceCreated");
            Log.i(TAG, "holder=" + holder);
            try {
                if (camera == null) {
                    openCamera(isOpenFrontCamera);
                }
                setCameraParameters();
                // updateSurfaceViewSize();
                printCameraParameters();
                holder = getHolder();
                camera.setPreviewDisplay(holder);

                camera.startPreview();
                autoFocus();
            } catch (Exception e) {
                e.printStackTrace();
                ToastHelper.s("打开相机失败");
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            CameraView.this.holder = holder;
            Log.i(TAG, "surfaceChanged");
            Log.i(TAG, "holder=" + holder);
            updateCameraOrientation();
            autoFocus();
            printCameraParameters();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            CameraView.this.holder = holder;
            Log.i(TAG, "surfaceDestroyed");
            Log.i(TAG, "holder=" + holder);
            //停止录像
            stopRecord();
            destroyCamera();
        }
    };

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    //设置聚焦
                    Point point = new Point((int) event.getX(), (int) event.getY());
                    focus(point);
                    break;
            }
            return true;
        }
    };

    private OrientationEventListener orientationEventListener = new OrientationEventListener(getContext()) {

        @Override
        public void onOrientationChanged(int rotation) {
            Log.i(TAG, "onOrientationChanged/rotation=" + rotation);
            if (((rotation >= 0) && (rotation <= 45)) || (rotation > 315)) {
                rotation = 0;
            } else if ((rotation > 45) && (rotation <= 135)) {
                rotation = 90;
            } else if ((rotation > 135) && (rotation <= 225)) {
                rotation = 180;
            } else if ((rotation > 225) && (rotation <= 315)) {
                rotation = 270;
            } else {
                rotation = 0;
            }
            if (rotation == orientation)
                return;
            orientation = rotation;
            updateCameraOrientation();
        }
    };

    /**
     * 打开相机
     *
     * @param flag 是否打开前置相机
     */
    private boolean openCamera(boolean flag) {
        destroyCamera();
        boolean isOpenCamera = false;
        if (flag) {// 打开前置摄像头
            CameraInfo cameraInfo = new CameraInfo();
            for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
                Camera.getCameraInfo(i, cameraInfo);
                if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
                    try {
                        camera = Camera.open(i);
                        isOpenFrontCamera = true;
                        isOpenCamera = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        camera = null;
                    }
                }
            }
        } else {// 打开默认摄像头
            try {
                camera = Camera.open();
                isOpenFrontCamera = false;
                isOpenCamera = true;
                return true;
            } catch (Exception e) {
                camera = null;
            }
        }
        Log.i(TAG, "camera=" + camera);
        Log.i(TAG, "isOpenFrontCamera=" + isOpenFrontCamera);
        Log.i(TAG, "isOpenCamera=" + isOpenCamera);
        return isOpenCamera;
    }

    /**
     * 销毁相机
     */
    private void destroyCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        if (orientationEventListener != null)
            orientationEventListener.disable();
    }

    /**
     * 重新连接相机
     */
    private void resetCamera() throws IOException {
        if (camera != null && parameters != null) {
            //重新连接相机
            camera.reconnect();
            //停止预览
            camera.stopPreview();
            //设置参数为录像前的参数，不然如果录像是低配，结束录制后预览效果还是低配画面
            try {
                // 注意这里必须先调用停止预览再设置参数才有效
                camera.setParameters(parameters);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //重新打开
            camera.startPreview();
            parameters = null;
        }
    }

    /**
     * 切换相机（前置，后置）
     */
    @Override
    public void switchCamera() {
        openCamera(!isOpenFrontCamera);
        if (camera != null) {
            setCameraParameters();
            // updateSurfaceViewSize();
            updateCameraOrientation();

            printCameraParameters();
            try {
                holder = getHolder();
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Camera.Size> supportPreviewSizes = new ArrayList<>();
    private List<Camera.Size> supportedVideoSizes = new ArrayList<>();
    private List<Camera.Size> supportedPictureSizes = new ArrayList<>();

    /**
     * 设置相机参数
     */
    private void setCameraParameters() {
        if (camera == null)
            return;
        parameters = camera.getParameters();
        if (parameters == null)
            return;
        int previewWidth = parameters.getPreviewSize().width;
        int previewHeight = parameters.getPreviewSize().height;
        Size size;
        // 预览尺寸
        supportPreviewSizes.clear();
        if (parameters.getSupportedPreviewSizes() != null)
            supportPreviewSizes.addAll(parameters.getSupportedPreviewSizes());
        Comparators.sortCameraSize(supportPreviewSizes);
        size = parameters.getPreviewSize();
        if (supportPreviewSizes.size() > 0) {
            Iterator<Size> iterator = supportPreviewSizes.iterator();
            // 查找最大的预览尺寸
            while (iterator.hasNext()) {
                Size s = iterator.next();
                if (s.width >= previewWidth
                        && s.height >= previewHeight) {
                    size = s;
                    break;
                }
            }
            // 查找宽高比相近的预览尺寸
            while (iterator.hasNext()) {
                Size s = iterator.next();
                int width = s.width;
                int height = s.height;
                float ratio = ((float) width) / ((float) height);
                if (ratio == screenRatio) {
                    size = s;
                    break;
                }
            }
            while (iterator.hasNext()) {
                Size s = iterator.next();
                int width = s.width;
                int height = s.height;
                float ratio = ((float) width) / ((float) height);
                if ((ratio - screenRatio) == 0 && width >= screenWidth) {
                    size = s;
                    break;
                }
            }
            parameters.setPreviewSize(size.width, size.height);
        }
        // 视频大小
        supportedVideoSizes.clear();
        if (parameters.getSupportedVideoSizes() != null)
            supportedVideoSizes.addAll(parameters.getSupportedVideoSizes());
        Comparators.sortCameraSize(supportedVideoSizes);
        //图片大小
        supportedPictureSizes.clear();
        if (parameters.getSupportedPictureSizes() != null)
            supportedPictureSizes.addAll(parameters.getSupportedPictureSizes());
        Comparators.sortCameraSize(supportedPictureSizes);
        size = parameters.getPictureSize();
        if (supportedPictureSizes.size() > 0) {
            int width = parameters.getPictureSize().width;
            int height = parameters.getPictureSize().height;
            for (Size s : supportedPictureSizes) {
                if (s.width >= width
                        && s.height >= height
                        && s.width >= size.width
                        && s.height >= size.height
                        && s.width * s.height < 100 * 10000) {
                    //小于100W像素
                    size = s;
                    break;
                }
            }
            parameters.setPictureSize(size.width, size.height);
        }
        //设置图片格式
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setJpegQuality(100);
        parameters.setJpegThumbnailQuality(100);
        //自动聚焦模式
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        try {
            camera.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setFlashMode(flashMode);
        setZoom(zoom);
        //开启屏幕朝向监听
        orientationEventListener.enable();
    }

    /**
     * 打印相机参数
     */
    private void printCameraParameters() {
        if (camera == null)
            return;
        if (camera.getParameters() == null)
            return;
        parameters = camera.getParameters();
        Log.d(TAG, "----------------parameters------------------");
        List<Size> sizes;
        Log.d(TAG, "previewFormat=" + parameters.getPreviewFormat());
        Log.d(TAG, "previewFrameRate=" + parameters.getPreviewFrameRate());
        Log.d(TAG, "pictureFormat=" + parameters.getPictureFormat());
        Log.d(TAG, "zoom=" + parameters.getZoom());
        Log.d(TAG, "maxZoom=" + parameters.getMaxZoom());
        Log.d(TAG, "zoomRatios=" + parameters.getZoomRatios());
        Log.d(TAG, "JpegQuality=" + parameters.getJpegQuality());
        Log.d(TAG, "jpegThumbnailQuality=" + parameters.getJpegThumbnailQuality());
        Log.d(TAG, "jpegThumbnailSize=" + parameters.getJpegThumbnailSize());
        Log.d(TAG, "FlashMode=" + parameters.getFlashMode());
        Log.d(TAG, "focusMode=" + parameters.getFocusMode());
        Log.d(TAG, "sceneMode=" + parameters.getSceneMode());
        Log.d(TAG, "horizontalViewAngle=" + parameters.getHorizontalViewAngle());
        Size s = parameters.getPreviewSize();
        if (s != null) {
            Log.d(TAG, "previewSize/width=" + s.width);
            Log.d(TAG, "previewSize/height=" + s.height);
        }
        s = parameters.getPictureSize();
        if (s != null) {
            Log.d(TAG, "pictureSize/width=" + s.width);
            Log.d(TAG, "pictureSize/height=" + s.height);
        }
        s = parameters.getPreferredPreviewSizeForVideo();
        if (s != null) {
            Log.d(TAG, "preferredPreviewSizeForVideo/width=" + s.width);
            Log.d(TAG, "preferredPreviewSizeForVideo/height=" + s.height);
        }
        Log.d(TAG, "----------------supportedPreviewSizes------------------");
        sizes = parameters.getSupportedPreviewSizes();
        if (sizes != null)
            for (Size size : sizes) {
                Log.d(TAG, "width=" + size.width);
                Log.d(TAG, "height=" + size.height);
            }
        Log.d(TAG, "----------------supportedPictureSizes------------------");
        sizes = parameters.getSupportedPictureSizes();
        if (sizes != null)
            for (Size size : sizes) {
                Log.d(TAG, "width=" + size.width);
                Log.d(TAG, "height=" + size.height);
            }
        Log.d(TAG, "----------------supportedVideoSizes------------------");
        sizes = parameters.getSupportedVideoSizes();
        if (sizes != null)
            for (Size size : sizes) {
                Log.d(TAG, "width=" + size.width);
                Log.d(TAG, "height=" + size.height);
            }
    }

    /**
     * 设置SurfaceView的大小
     */
    private void updateSurfaceViewSize() {
        Log.d(TAG, "----------------updateSurfaceViewSize------------------");
        if (camera == null)
            return;
        parameters = camera.getParameters();
        if (parameters == null)
            return;
        Size size = parameters.getPreviewSize();
        int w = size.width;
        int h = size.height;
        Log.i(TAG, "w=" + w);
        Log.i(TAG, "h=" + h);
        if (w > h) {
            int a = w;
            w = h;
            h = a;
        }

        if (h * 10000 / (w * 10000) >= screenHeight * 10000 / (screenWidth * 10000))
            return;
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params == null)
            params = new ViewGroup.LayoutParams(0, 0);
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = screenWidth * h / w;
        Log.i(TAG, "width=" + params.width);
        Log.i(TAG, "height=" + params.height);
        setLayoutParams(params);
    }

    /**
     * 是否正在视频拍摄
     */
    public boolean isRecording() {
        return mediaRecorder != null;
    }

    /**
     * 开始视频拍摄
     *
     * @return 开始录像是否成功
     */
    @Override
    public boolean startRecord() {
        boolean flag = false;
        if (camera == null)
            openCamera(isOpenFrontCamera);
        if (camera == null) {
            return false;
        }
        if (mediaRecorder == null)
            mediaRecorder = new MediaRecorder();
        else
            mediaRecorder.reset();
        parameters = camera.getParameters();
        findMediaParameters();
        // camera.setDisplayOrientation(recordOrientation);// 设置视频预览朝向
        holder = getHolder();
        mediaRecorder.setPreviewDisplay(holder.getSurface());
        camera.unlock();
        mediaRecorder.setCamera(camera);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        if (videoWidth <= 0 || videoHeight <= 0)
            setMediaProfile();
        else
            setMediaParameters();
        mediaRecorder.setOrientationHint(recordOrientation);// 设置输出视频朝向

        recordPath = FileOperateUtil.getVideoPath();
        Log.d(TAG, "startRecord/recordPath=" + recordPath);
        try {
            mediaRecorder.setOutputFile(recordPath);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
        try {
            mediaRecorder.start();
            flag = true;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        if (flag)
            startTimeTask();
        return flag;
    }

    /**
     * 设置视频拍摄参数
     */
    private void setMediaProfile() {
        if (mediaRecorder == null)
            return;
        Log.d(TAG, "----------------setMediaProfile------------------");
        int quality;
        if (recorderQuality == RecorderQuality.HIGHT)
            quality = CamcorderProfile.QUALITY_HIGH;
        else
            quality = CamcorderProfile.QUALITY_LOW;
        if (!CamcorderProfile.hasProfile(quality))
            quality = CamcorderProfile.QUALITY_HIGH;
        CamcorderProfile camcorderProfile = CamcorderProfile.get(quality);
        printCamcorderProfile(camcorderProfile);
        mediaRecorder.setProfile(camcorderProfile);
    }

    private int videoWidth;
    private int videoHeight;

    private int screenWidth;
    private int screenHeight;
    private float screenRatio;

    /**
     * 设置视频拍摄参数
     */
    private void findMediaParameters() {
        Log.d(TAG, "----------------findMediaParameters------------------");
        Camera.Size size = null;
        float ratio;
        // 视频大小
        if (supportedVideoSizes != null && supportedVideoSizes.size() > 0) {
            // 查找中间的录制尺寸
            int index = supportedVideoSizes.size() / 2;
            size = supportedVideoSizes.get(index);
            // 查找宽高比相近的预览尺寸
            for (Size s : supportedVideoSizes) {
                Log.d(TAG, "findMediaParameters/width=" + s.width);
                Log.d(TAG, "findMediaParameters/height=" + s.height);
                ratio = ((float) videoWidth) / ((float) videoHeight);
                if ((ratio - screenRatio) == 0) {
                    size = s;
                    break;
                }
            }
            if (supportedVideoSizes.size() == 1) {
                size = supportedVideoSizes.get(0);
            } else {
                if (size == supportedVideoSizes.get(0) ||
                        size == supportedVideoSizes.get(supportedVideoSizes.size() - 1)) {
                    int length = supportedVideoSizes.size() / 2;
                    if (length > 0)
                        size = supportedVideoSizes.get(length);
                }
            }
        }
        if (size != null) {
            videoWidth = size.width;
            videoHeight = size.height;
        }
        Log.d(TAG, "findMediaParameters/videoWidth=" + videoWidth);
        Log.d(TAG, "findMediaParameters/videoWidth=" + videoHeight);
    }

    /**
     * 设置视频拍摄参数
     */
    private void setMediaParameters() {
        if (mediaRecorder == null)
            return;
        Log.d(TAG, "----------------setMediaParameters------------------");
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        // mediaRecorder.setOrientationHint(0);
        mediaRecorder.setVideoSize(videoWidth, videoHeight);// 176,144
        // mediaRecorder.setVideoFrameRate(24);
        // mediaRecorder.setVideoEncodingBitRate(128);
        // mediaRecorder.setAudioChannels(1);
        // mediaRecorder.setAudioSamplingRate(48);
    }

    /**
     * 打印视频拍摄参数
     */
    private void printCamcorderProfile(CamcorderProfile camcorderProfile) {
        if (camcorderProfile == null)
            return;
        Log.d(TAG, "----------------camcorderProfile------------------");
        Log.d(TAG, "audioBitRate=" + camcorderProfile.audioBitRate);
        Log.d(TAG, "audioChannels=" + camcorderProfile.audioChannels);
        Log.d(TAG, "audioCodec=" + camcorderProfile.audioCodec);
        Log.d(TAG, "audioSampleRate=" + camcorderProfile.audioSampleRate);
        Log.d(TAG, "duration=" + camcorderProfile.duration);
        Log.d(TAG, "fileFormat=" + camcorderProfile.fileFormat);
        Log.d(TAG, "quality=" + camcorderProfile.quality);
        Log.d(TAG, "videoBitRate=" + camcorderProfile.videoBitRate);
        Log.d(TAG, "videoCodec=" + camcorderProfile.videoCodec);
        Log.d(TAG, "videoFrameHeight=" + camcorderProfile.videoFrameHeight);
        Log.d(TAG, "videoFrameWidth=" + camcorderProfile.videoFrameWidth);
        Log.d(TAG, "videoFrameRate=" + camcorderProfile.videoFrameRate);
    }

    /**
     * 停止视频拍摄
     *
     * @return 视频的缩略图
     */
    @Override
    public Bitmap stopRecord() {
        Bitmap bitmap = null;
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mediaRecorder.reset();
            mediaRecorder.release();
            //保存视频的缩略图
            DataHandler handler = new DataHandler(getContext(), cameraListener);
            bitmap = handler.saveVideoThumbnail(CameraView.this, recordPath);
        }
        try {
            resetCamera();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recordPath = null;
        mediaRecorder = null;
        if (!isRecording())
            stopTimeTask();
        if (cameraListener != null)
            cameraListener.onRecorderUpdate(false, 0);
        return bitmap;
    }

    private CameraListener cameraListener;

    public void setCameraListener(CameraListener cameraListener) {
        this.cameraListener = cameraListener;
    }

    private final Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            DataHandler handler = new DataHandler(getContext(), cameraListener);
            handler.setMaxSize(200);
            handler.saveImage(data, camera);
            camera.startPreview();
        }
    };

    private Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {

        @Override
        public void onAutoFocus(boolean success, Camera camera) {

        }
    };

    private long seconds = 0l;
    private android.os.Handler handler = new android.os.Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ++seconds;
            cameraListener.onRecorderUpdate(true, seconds);
            if (seconds > maxTime && isRecording()) {
                stopRecord();
            } else {
                handler.postDelayed(this, 1000);
            }

        }
    };

    /**
     * 开始计时
     */
    private void startTimeTask() {
        stopTimeTask();
        if (handler != null && runnable != null) {
            seconds = 0l;
            handler.postDelayed(runnable, 1000);
            cameraListener.onRecorderUpdate(true, seconds);
        }
    }

    /**
     * 停止计时
     */
    private void stopTimeTask() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
            seconds = 0l;
        }
    }


    /**
     * 拍照
     */
    @Override
    public void takePicture() {
        camera.takePicture(null, null, pictureCallback);
    }

    /**
     * 获取当前闪光灯类型
     */
    @Override
    public FlashMode getFlashMode() {
        return flashMode;
    }

    /**
     * 设置闪光灯类型
     */
    @Override
    public void setFlashMode(FlashMode flashMode) {
        if (camera == null)
            return;
        this.flashMode = flashMode;
        Camera.Parameters parameters = camera.getParameters();
        switch (flashMode) {
            case ON:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                break;
            case AUTO:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                break;
            case TORCH:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                break;
            default:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                break;
        }
        try {
            camera.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "========camera.setParameters catch========");
        }
    }

    /**
     * 获取相机最大缩放（40）
     */
    @Override
    public int getMaxZoom() {
        if (camera == null)
            return -1;
        Camera.Parameters parameters = camera.getParameters();
        if (!parameters.isZoomSupported()) return -1;
        return parameters.getMaxZoom() > 40 ? 40 : parameters.getMaxZoom();
    }

    @Override
    public int getZoom() {
        return zoom;
    }

    /**
     * 设置相机缩放
     */
    @Override
    public void setZoom(int zoom) {
        if (camera == null)
            return;
        parameters = camera.getParameters();
        if (!parameters.isZoomSupported())
            return;
        parameters.setZoom(zoom);
        try {
            camera.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.zoom = zoom;
    }

    public boolean setQuality(RecorderQuality recorderQuality) {
        this.recorderQuality = recorderQuality;
        return true;
    }

    public RecorderQuality getQuality() {
        return recorderQuality;
    }

    @Override
    public void autoFocus() {
        if (camera != null) {
            try {
                camera.autoFocus(autoFocusCallback);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void focus(Point point) {
        if (camera == null)
            return;
        Camera.Parameters parameters = camera.getParameters();
        //不支持设置自定义聚焦，则使用自动聚焦，返回
        if (parameters.getMaxNumFocusAreas() <= 0) {
            try {
                camera.autoFocus(autoFocusCallback);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        List<Camera.Area> areas = new ArrayList<>();
        int left = point.x - 300;
        int top = point.y - 300;
        int right = point.x + 300;
        int bottom = point.y + 300;
        left = left < -1000 ? -1000 : left;
        top = top < -1000 ? -1000 : top;
        right = right > 1000 ? 1000 : right;
        bottom = bottom > 1000 ? 1000 : bottom;
        areas.add(new Camera.Area(new Rect(left, top, right, bottom), 100));
        parameters.setFocusAreas(areas);
        try {
            // 小米手机会出异常
            camera.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            camera.autoFocus(autoFocusCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据当前朝向修改保存图片的旋转角度
     */
    private void updateCameraOrientation() {
        if (camera == null)
            return;
        parameters = camera.getParameters();
        if (parameters == null)
            return;
        //rotation参数为 0、90、180、270。水平方向为0。
        int rotation = 90 + orientation == 360 ? 0 : 90 + orientation;
        //前置摄像头需要对垂直方向做变换，否则照片是颠倒的
        if (isOpenFrontCamera) {
            if (rotation == 90)
                rotation = 270;
            else if (rotation == 270)
                rotation = 90;
        }
        parameters.setRotation(rotation);//生成的图片转90°
        //预览图片旋转90°
        camera.setDisplayOrientation(90);//预览转90°
        try {
            camera.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 闪光灯类型
     */
    public enum FlashMode {
        /**
         * 拍照时打开闪光灯
         */
        ON,
        /**
         * 不打开闪光灯
         */
        OFF,
        /**
         * 系统决定是否打开闪光灯
         */
        AUTO,
        /**
         * 一直打开闪光灯
         */
        TORCH
    }

    /**
     * 视频质量
     */
    public enum RecorderQuality {
        /**
         * 高质量
         */
        HIGHT,
        /**
         * 低质量
         */
        LOW
    }

    public static void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }
}