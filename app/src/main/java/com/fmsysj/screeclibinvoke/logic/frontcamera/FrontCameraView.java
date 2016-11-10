package com.fmsysj.screeclibinvoke.logic.frontcamera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.fmsysj.screeclibinvoke.utils.ImageUtil;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.utils.ScreenUtil;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 视图：前置摄像头视图
 */
public class FrontCameraView extends LinearLayout implements
        SurfaceHolder.Callback,
        Camera.PreviewCallback {

    public static final String TAG = FrontCameraView.class.getSimpleName();

    public int mWidth;
    public int mHeight;
    private LayoutInflater inflater;
    private FrameLayout root;

    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private SurfaceTexture surfaceTexture;

    // 320 * 240
    // private static final int HEIGHT = 240;
    // private static final int WIDTH = 320;

    private int preX;
    public int preY;
    public int preXFirst;
    public int preYFirst;

    private int x;
    private int y;
    private boolean isMove;

    private int length;

    public Context context = AppManager.getInstance().getContext();
    private boolean isInitialized = false, isPreviewed = false;
    private WindowManager windowManager;

    /**
     * 图片线程
     */
    private String threadName = "LoopThread-SurfaceHolder";
    private HandlerThread thread;
    private Looper looper;
    private Handler handler;
    private Handler h;

    private void initLooperThread() {
        Log.d(TAG, "initLooperThread: // ---------------------------------------------");
        thread = new HandlerThread(threadName, android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        looper = thread.getLooper();
        handler = new Handler(looper);
        Log.d(TAG, "initLooperThread: true");
    }

    private void destroyLooperThread() {
        Log.d(TAG, "destroyLooperThread: // ---------------------------------------------");
        if (thread != null)
            try {
                thread.quit();
                Log.d(TAG, "destroyLooperThread: true");
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public FrontCameraView() {
        super(AppManager.getInstance().getContext(), null);

        h = new Handler(Looper.myLooper());

        initLooperThread();
        initExecutorService();

        inflater = LayoutInflater.from(context);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        inflater.inflate(R.layout.view_camera, this);
        root = (FrameLayout) findViewById(R.id.root);

        ViewGroup.LayoutParams params = root.getLayoutParams();
        mWidth = params.width;
        mHeight = params.height;

        surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        length = ScreenUtil.dp2px(42);

        surfaceTexture = new SurfaceTexture(10);
    }

    /**
     * 关闭
     */
    @SuppressWarnings("deprecation")
    public void close() {
        Log.d(TAG, "close: // ---------------------------------------------");
        closeCamera();
        destroyLooperThread();
        destroyExecutorService();
    }

    // ---------------------------------------------------------------------------------------

    @SuppressLint("NewApi")
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated: // ---------------------------------------------");
        initCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged: // ---------------------------------------------");
        if (camera != null && isInitialized == true) {
            try {
                camera.stopPreview();
                camera.setPreviewCallback(this);
                camera.startPreview();
                Log.d(TAG, "surfaceChanged: true");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed: // ---------------------------------------------");
        closeCamera();
        destroyLooperThread();
        destroyExecutorService();
    }

    // ---------------------------------------------------------------------------------------

    /**
     * 打开摄像头
     */
    private void initCamera() {
        Log.d(TAG, "initCamera: // ---------------------------------------------");
        if (camera == null) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            final int cameraCount = Camera.getNumberOfCameras();
            for (int index = 0; index < cameraCount; index++) {
                Camera.getCameraInfo(index, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    final int i = index;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            openCamera(i);
                            configCamera();
                            CameraUtil.setPreviewParametersSmallest(camera);
                            // configSurfaceView();
                            previewCamera();
                            CameraUtil.printParameters(camera);
                        }
                    });
                    break;
                }
            }
        }
    }

    private void openCamera(final int index) {
        Log.d(TAG, "openCamera: // ---------------------------------------------");
        try {
            camera = Camera.open(index);
            Log.d(TAG, "openCamera: true");
            Log.d(TAG, "openCamera: threadName=" + Thread.currentThread().getName());
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    private void configCamera() {
        Log.d(TAG, "configCamera: // ---------------------------------------------");
        if (camera != null) {
            /*try {
                camera.reconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            try {
                camera.setDisplayOrientation(90);
                camera.setPreviewTexture(surfaceTexture);
                camera.setPreviewCallback(this);
                Log.d(TAG, "configCamera: 2");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "configCamera: true");
        }
    }

    private void configSurfaceView() {
        Log.d(TAG, "configSurfaceView: // ---------------------------------------------");
        if (camera != null && camera.getParameters() != null) {
            int width = camera.getParameters().getPreviewSize().width;
            int height = camera.getParameters().getPreviewSize().height;
            Log.d(TAG, "configSurfaceView: length=" + length);
            Log.d(TAG, "configSurfaceView: width=" + width);
            Log.d(TAG, "configSurfaceView: height=" + height);
            if (width > 0 &&
                    height > 0) {
                if (width <= height) {
                    length = width;
                } else {
                    length = height;
                }
            }
            Log.d(TAG, "configSurfaceView: length=" + length);
            if (surfaceView != null) {
                final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT);
                params.width = length;
                params.height = length;
                h.post(new Runnable() {
                    @Override
                    public void run() {

                        surfaceView.setLayoutParams(params);
                        Log.d(TAG, "configSurfaceView: width=" + params.width);
                        Log.d(TAG, "configSurfaceView: height=" + params.height);
                        Log.d(TAG, "configSurfaceView: true");
                    }
                });
            }
        }
    }

    private void previewCamera() {
        Log.d(TAG, "previewCamera: // ---------------------------------------------");
        surfaceHolder = surfaceView.getHolder();
        h.post(new Runnable() {
            @Override
            public void run() {
                if (camera != null) {
                    try {
                        // camera.setPreviewDisplay(surfaceHolder);
                        camera.startPreview();
                        isPreviewed = true;
                        isInitialized = true;
                        Log.d(TAG, "previewCamera: true");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 关闭摄像头
     */
    public void closeCamera() {
        Log.d(TAG, "closeCamera: // ---------------------------------------------");
        if (camera != null) {
            try {
                camera.setPreviewCallback(null);
                camera.setPreviewCallbackWithBuffer(null);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            try {
                camera.stopPreview();
                camera.release();
                camera = null;
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        isPreviewed = false;
        surfaceHolder = null;
    }

    // ---------------------------------------------------------------------------------------

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                preX = (int) event.getRawX();
                preY = (int) event.getRawY();
                preXFirst = preX;
                preYFirst = preY;
                isMove = false;
                break;

            case MotionEvent.ACTION_UP:
                break;

            case MotionEvent.ACTION_MOVE:
                x = (int) event.getRawX();
                y = (int) event.getRawY();
                // 消除手指点击误差
                if (((x - preXFirst) > -15 && (x - preXFirst) < 15)
                        && ((y - preYFirst) > -15 && (y - preYFirst) < 15)) {
                    isMove = false;
                } else {
                    FrontCameraManager.getInstance().move(FrontCameraView.this, x - preX, y - preY);
                    isMove = true;
                }
                preX = x;
                preY = y;
                break;
        }
        return super.onTouchEvent(event);
    }

    // ---------------------------------------------------------------------------------------

    private Object syncObject = new Object();
    private int index = 0;

    @Override
    public void onPreviewFrame(final byte[] data, final Camera camera) {
        Log.d(TAG, "onPreviewFrame: // ---------------------------------------------");
        // Log.d(TAG, "onPreviewFrame: threadName=" + Thread.currentThread().getName());
        // Log.d(TAG, "onPreviewFrame: camera=" + camera);
        // Log.d(TAG, "onPreviewFrame: data=" + data);
        // Log.d(TAG, "onPreviewFrame: length=" + data.length);
        ++ index;
        // Log.d(TAG, "onPreviewFrame: index=" + index);
        if (camera == null) {
            return;
        }
        surfaceHolder = surfaceView.getHolder();
        // TODO: 2016/7/11 此处处理如果如果用线程池，线程切换容易导致阻塞
        if (camera.getParameters() != null) {
            Camera.Size size = camera.getParameters().getPreviewSize();
            if (data != null &&
                    size != null &&
                    surfaceHolder != null) {
                // index % 2 == 0 过滤后比较卡
                synchronized (syncObject) {
                    try {
                        drawCanvas(surfaceHolder, data, size.width, size.height);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // ---------------------------------------------------------------------------------------

    public void drawCanvas(SurfaceHolder surfaceHolder, byte[] data, int width, int height) {
        Log.d(TAG, "drawCanvas: // ---------------------------------------------");
        Canvas canvas = null;
        Bitmap srcBitmap = null, secBitmap = null, thirBitmap = null, fourBitmap = null, dstBitmap = null;
        try {
            canvas = surfaceHolder.lockCanvas();
            int newData[] = new int[width * height];
            ImageUtil.RGBtoYUV420SP(newData, data, width, height); // 解码
            srcBitmap = Bitmap.createBitmap(newData, width, height, Config.ARGB_8888);
            int rotation = 0;
            switch (screenRotation) {
                case 0:
                    rotation = -90;
                    break;
                case 1:
                    rotation = 0;
                    break;
                case 2:
                    rotation = 90;
                    break;
                case 3:
                    rotation = 180;
                    break;
            }

            int w = srcBitmap.getWidth();
            int h = srcBitmap.getHeight();
            int x, y, a;
            if (w < h) {
                x = 0;
                y = Math.abs(h - w) / 2;
                a = w;
            } else {
                x = Math.abs(h - w) / 2;
                y = 0;
                a = h;
            }

            //Log.d(TAG, "drawCanvas: w=" + w);
            //Log.d(TAG, "drawCanvas: h=" + h);
            //Log.d(TAG, "drawCanvas: x=" + x);
            //Log.d(TAG, "drawCanvas: y=" + y);
            //Log.d(TAG, "drawCanvas: a=" + a);
            //Log.d(TAG, "drawCanvas: length=" + length);
            secBitmap = Bitmap.createBitmap(srcBitmap, x, y, a, a);
            // Log.d(TAG, "drawCanvas: secWidth=" + secBitmap.getWidth());
            // Log.d(TAG, "drawCanvas: secHeight=" + secBitmap.getHeight());

            Matrix matrix = new Matrix();
            // Log.d(TAG, "drawCanvas: rotation=" + rotation);
            matrix.setRotate(rotation, a / 2, a / 2);
            // matrix.setRectToRect(new RectF(0, 0, a, a), new RectF(0, 0, length, length), Matrix.ScaleToFit.FILL);
            thirBitmap = Bitmap.createBitmap(secBitmap, 0, 0, a, a, matrix, false);
            // Log.d(TAG, "drawCanvas: thirWidth=" + thirBitmap.getWidth());
            // Log.d(TAG, "drawCanvas: thirHeight=" + thirBitmap.getHeight());

            fourBitmap = Bitmap.createScaledBitmap(thirBitmap, length, length, false);
            // Log.d(TAG, "drawCanvas: fourWidth=" + fourBitmap.getWidth());
            // Log.d(TAG, "drawCanvas: fourHeight=" + fourBitmap.getHeight());

            dstBitmap = ImageCache.get("SoftGlowFilter");
            dstBitmap = new SoftGlowFilter(fourBitmap, 1, 0.1f, 0.1f).imageProcess().getDstBitmap();
            ImageCache.put("SoftGlowFilter", dstBitmap);
            int left = length / 2 - dstBitmap.getWidth() / 2;
            int top = length / 2 - dstBitmap.getHeight() / 2;
            // Log.d(TAG, "drawCanvas: left=" + left);
            // Log.d(TAG, "drawCanvas: top=" + top);
            canvas.drawBitmap(dstBitmap, 0, 0, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (srcBitmap != null) {
                try {
                    srcBitmap.recycle();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (secBitmap != null) {
                try {
                    secBitmap.recycle();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (thirBitmap != null) {
                try {
                    thirBitmap.recycle();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (fourBitmap != null) {
                try {
                    fourBitmap.recycle();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (surfaceHolder != null) {
                try {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // ---------------------------------------------------------------------------------------

    private ScheduledExecutorService executorService;
    public int screenRotation = 0;

    private void initExecutorService() {
        Log.d(TAG, "initExecutorService: // ---------------------------------------------");
        if (executorService == null) {
            executorService = Executors.newScheduledThreadPool(1);
            executorService.scheduleAtFixedRate(command, 0, 1, TimeUnit.SECONDS);
        }
    }

    private void destroyExecutorService() {
        Log.d(TAG, "destroyExecutorService: // ---------------------------------------------");
        if (executorService != null) {
            try {
                executorService.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
            executorService = null;
        }
    }

    private Runnable command = new Runnable() {

        @Override
        public void run() {
            // 屏幕方向 0,1,2,3
            screenRotation = windowManager.getDefaultDisplay().getRotation();
            Log.d(TAG, "command: screenRotation=" + screenRotation);
        }
    };
}
