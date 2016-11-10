package com.li.videoapplication.data.record.camera2.cameraView;
/*
 * AudioVideoRecordingSample
 * Sample project to cature audio and video from internal mic/camera and save as MPEG4 file.
 *
 * Copyright (c) 2014-2015 saki t_saki@serenegiant.com
 *
 * File name: CameraGLView.java
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * All files in the folder are under this Apache License, Version 2.0.
*/

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.EGL14;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import com.li.videoapplication.data.record.camera2.encoder.MediaVideoEncoder;
import com.li.videoapplication.data.record.camera2.glutils.GLDrawer2D;
import com.li.videoapplication.utils.ScreenUtil;

/**
 * Sub class of GLSurfaceView to display camera preview and write video frame to capturing surface
 */
public final class CameraGLView extends GLSurfaceView {

    private static final boolean DEBUG = true; // TODO set false on release
    private static final String TAG = "CameraGLView";

    private static final int CAMERA_ID = 0;

    private static final int SCALE_STRETCH_FIT = 0;
    private static final int SCALE_KEEP_ASPECT_VIEWPORT = 1;
    private static final int SCALE_KEEP_ASPECT = 2;
    private static final int SCALE_CROP_CENTER = 3;

    private CameraSurfaceRenderer mRenderer;
    private boolean mHasSurface;
    private CameraHandler mCameraHandler = null;
    private int mVideoWidth, mVideoHeight;
    private int mRotation;
    private int mScaleMode = SCALE_STRETCH_FIT;

    private static boolean isFrontCamera = false;

    /**
     * 闪光灯类型  默认为关闭
     */
    private static FlashMode flashMode = FlashMode.OFF;

    public CameraGLView(final Context context) {
        this(context, null, 0);
    }

    public CameraGLView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraGLView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs);
        if (DEBUG) Log.v(TAG, "CameraGLView:");
        mRenderer = new CameraSurfaceRenderer(this);
        setEGLContextClientVersion(2);    // GLES 2.0, API >= 8
        setRenderer(mRenderer);
/*		// the frequency of refreshing of camera preview is at most 15 fps
        // and RENDERMODE_WHEN_DIRTY is better to reduce power consumption
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY); */
    }

    @Override
    public void onResume() {
        if (DEBUG) Log.v(TAG, "onResume:");
        super.onResume();
        if (mHasSurface) {
            if (mCameraHandler == null) {
                if (DEBUG) Log.v(TAG, "surface already exist");
                startPreview(isFrontCamera);
            }
        }
    }

    @Override
    public void onPause() {
        if (DEBUG) Log.v(TAG, "onPause:");
        if (mCameraHandler != null) {
            // just request stop prviewing
            mCameraHandler.stopPreview(false);
        }
        super.onPause();
    }

    public void setVideoSize(final int width, final int height) {
        if (DEBUG) Log.d(TAG, "setVideoSize: width == " + width + " , height == " + height);
        if ((mRotation % 180) == 0) {
            mVideoWidth = width;
            mVideoHeight = height;
        } else {
            mVideoWidth = height;
            mVideoHeight = width;
        }
        queueEvent(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "setVideoSize: mRenderer == "+mRenderer);
                mRenderer.updateViewport();
            }
        });
    }

    public int getVideoWidth() {
        return mVideoWidth;
    }

    public int getVideoHeight() {
        return mVideoHeight;
    }

    public SurfaceTexture getSurfaceTexture() {
        if (DEBUG) Log.v(TAG, "getSurfaceTexture:");
        return mRenderer != null ? mRenderer.mSTexture : null;
    }

    @Override
    public void surfaceDestroyed(final SurfaceHolder holder) {
        if (DEBUG) Log.v(TAG, "------------ surfaceDestroyed: ------------");
        if (mCameraHandler != null) {
            // wait for finish previewing here
            // otherwise camera try to display on un-exist Surface and some error will occure
            mCameraHandler.stopPreview(true);
        }
        mCameraHandler = null;
        mHasSurface = false;
        mHasSurface = false;
        mRenderer.onSurfaceDestroyed();
        mRenderer = null;
        super.surfaceDestroyed(holder);
    }

    public void setVideoEncoder(final MediaVideoEncoder encoder) {
        if (DEBUG) Log.v(TAG, "setVideoEncoder:tex_id=" + mRenderer.hTex + ",encoder=" + encoder);
        queueEvent(new Runnable() {
            @Override
            public void run() {
                synchronized (mRenderer) {
                    if (encoder != null) {
                        encoder.setEglContext(EGL14.eglGetCurrentContext(), mRenderer.hTex);
                    }
                    mRenderer.mVideoEncoder = encoder;
                }
            }
        });
    }

    //********************************************************************************
//********************************************************************************
    private synchronized void startPreview(boolean isFrontCamera) {
        if (mCameraHandler == null) {
            final CameraThread thread = new CameraThread(this);
            thread.start();
            mCameraHandler = thread.getHandler();
        }
        mCameraHandler.startPreview(isFrontCamera);
    }

    /**
     * 设置闪光灯
     */
    public void setFlashMode(FlashMode flashMode) {
        mCameraHandler.setFlashMode(flashMode);
    }

    /**
     * 获取当前闪光灯类型
     */
    public FlashMode getFlashMode() {
        return flashMode;
    }

    /**
     * 闪光灯类型
     */
    public enum FlashMode {
        //拍照时打开闪光灯
        ON,
        //不打开闪光灯
        OFF,
        //系统决定是否打开闪光灯
        AUTO,
        //一直打开闪光灯
        TORCH
    }

    /**
     * 切换前、后摄像头
     *
     * @param isFrontCamera
     */
    public void switchCamera(boolean isFrontCamera) {
        CameraGLView.isFrontCamera = isFrontCamera;
        startPreview(isFrontCamera);
    }

    /**
     * GLSurfaceViewのRenderer
     */
    private static final class CameraSurfaceRenderer
            implements Renderer,
            SurfaceTexture.OnFrameAvailableListener {    // API >= 11

        private final WeakReference<CameraGLView> mWeakParent;
        private SurfaceTexture mSTexture;    // API >= 11
        private int hTex;
        private GLDrawer2D mDrawer;
        private final float[] mStMatrix = new float[16];
        private final float[] mMvpMatrix = new float[16];
        private MediaVideoEncoder mVideoEncoder;

        public CameraSurfaceRenderer(final CameraGLView parent) {
            if (DEBUG) Log.v(TAG, "CameraSurfaceRenderer:");
            mWeakParent = new WeakReference<>(parent);
            Matrix.setIdentityM(mMvpMatrix, 0);
        }

        @Override
        public void onSurfaceCreated(final GL10 unused, final EGLConfig config) {
            if (DEBUG) Log.v(TAG, "onSurfaceCreated:");
            // This renderer required OES_EGL_image_external extension
            final String extensions = GLES20.glGetString(GLES20.GL_EXTENSIONS);    // API >= 8
//			if (DEBUG) Log.i(TAG, "onSurfaceCreated:Gl extensions: " + extensions);
            if (!extensions.contains("OES_EGL_image_external"))
                throw new RuntimeException("This system does not support OES_EGL_image_external.");
            // create textur ID
            hTex = GLDrawer2D.initTex();
            // create SurfaceTexture with texture ID.
            mSTexture = new SurfaceTexture(hTex);
            mSTexture.setOnFrameAvailableListener(this);
            // clear screen with yellow color so that you can see rendering rectangle
            GLES20.glClearColor(1.0f, 1.0f, 0.0f, 1.0f);
            final CameraGLView parent = mWeakParent.get();
            if (parent != null) {
                parent.mHasSurface = true;
            }
            // create object for preview display
            mDrawer = new GLDrawer2D();
            mDrawer.setMatrix(mMvpMatrix, 0);
        }

        @Override
        public void onSurfaceChanged(final GL10 unused, final int width, final int height) {
            if (DEBUG) Log.v(TAG, String.format("onSurfaceChanged:(%d,%d)", width, height));
            // if at least with or height is zero, initialization of this view is still progress.
            if ((width == 0) || (height == 0)) return;
            updateViewport();
            final CameraGLView parent = mWeakParent.get();
            if (parent != null) {
                parent.startPreview(isFrontCamera);
            }
        }

        /**
         * when GLSurface context is soon destroyed
         */
        public void onSurfaceDestroyed() {
            if (DEBUG) Log.v(TAG, "------------ onSurfaceDestroyed: ------------");
            if (mDrawer != null) {
                mDrawer.release();
                mDrawer = null;
            }
            if (mSTexture != null) {
                mSTexture.release();
                mSTexture = null;
            }
            GLDrawer2D.deleteTex(hTex);
        }

        private final void updateViewport() {
            if (DEBUG) Log.d(TAG, "-------- updateViewport: --------");
            final CameraGLView parent = mWeakParent.get();
            if (parent != null) {
                final int view_width = parent.getWidth();
                final int view_height = parent.getHeight();
                GLES20.glViewport(0, 0, view_width, view_height);
                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
                final double video_width = parent.mVideoWidth;
                final double video_height = parent.mVideoHeight;
                if (video_width == 0 || video_height == 0) return;
                Matrix.setIdentityM(mMvpMatrix, 0);
                final double view_aspect = view_width / (double) view_height;
                Log.i(TAG, String.format("view(%d,%d)%f,video(%1.0f,%1.0f)", view_width, view_height, view_aspect, video_width, video_height));
                switch (parent.mScaleMode) {
                    case SCALE_STRETCH_FIT:
                        break;
                    case SCALE_KEEP_ASPECT_VIEWPORT: {
                        final double req = video_width / video_height;
                        int x, y;
                        int width, height;
                        if (view_aspect > req) {
                            // if view is wider than camera image, calc width of drawing area based on view height
                            y = 0;
                            height = view_height;
                            width = (int) (req * view_height);
                            x = (view_width - width) / 2;
                        } else {
                            // if view is higher than camera image, calc height of drawing area based on view width
                            x = 0;
                            width = view_width;
                            height = (int) (view_width / req);
                            y = (view_height - height) / 2;
                        }
                        // set viewport to draw keeping aspect ration of camera image
                        if (DEBUG)
                            Log.v(TAG, String.format("xy(%d,%d),size(%d,%d)", x, y, width, height));
                        GLES20.glViewport(x, y, width, height);
                        break;
                    }
                    case SCALE_KEEP_ASPECT:
                    case SCALE_CROP_CENTER: {
                        final double scale_x = view_width / video_width;
                        final double scale_y = view_height / video_height;
                        final double scale = (parent.mScaleMode == SCALE_CROP_CENTER
                                ? Math.max(scale_x, scale_y) : Math.min(scale_x, scale_y));
                        final double width = scale * video_width;
                        final double height = scale * video_height;
                        Log.v(TAG, String.format("size(%1.0f,%1.0f),scale(%f,%f),mat(%f,%f)",
                                width, height, scale_x, scale_y, width / view_width, height / view_height));
                        Matrix.scaleM(mMvpMatrix, 0, (float) (width / view_width), (float) (height / view_height), 1.0f);
                        break;
                    }
                }
                if (mDrawer != null)
                    mDrawer.setMatrix(mMvpMatrix, 0);
            }
        }

        private volatile boolean requesrUpdateTex = false;
        private boolean flip = true;

        /**
         * drawing to GLSurface
         * we set renderMode to GLSurfaceView.RENDERMODE_WHEN_DIRTY,
         * this method is only called when #requestRender is called(= when texture is required to update)
         * if you don't set RENDERMODE_WHEN_DIRTY, this method is called at maximum 60fps
         */
        @Override
        public void onDrawFrame(final GL10 unused) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

            if (requesrUpdateTex) {
                requesrUpdateTex = false;
                // update texture(came from camera)
                mSTexture.updateTexImage();
                // get texture matrix
                mSTexture.getTransformMatrix(mStMatrix);
            }
            // draw to preview screen
            mDrawer.draw(hTex, mStMatrix);
            flip = !flip;
            if (flip) {    // ~30fps
                synchronized (this) {
                    if (mVideoEncoder != null) {
                        // notify to capturing thread that the camera frame is available.
//						mVideoEncoder.frameAvailableSoon(mStMatrix);
                        mVideoEncoder.frameAvailableSoon(mStMatrix, mMvpMatrix);
                    }
                }
            }
        }

        @Override
        public void onFrameAvailable(final SurfaceTexture st) {
            requesrUpdateTex = true;
//			final CameraGLView parent = mWeakParent.get();
//			if (parent != null)
//				parent.requestRender();
        }
    }

    /**
     * Handler class for asynchronous camera operation
     */
    private static final class CameraHandler extends Handler {
        private static final int MSG_PREVIEW_START = 1;
        private static final int MSG_PREVIEW_STOP = 2;
        private static final int MSG_FLASH_MODE = 3;
        private CameraThread mThread;

        public CameraHandler(final CameraThread thread) {
            mThread = thread;
        }

        public void setFlashMode(FlashMode flashMode) {
            Message msg = new Message();
            msg.what = MSG_FLASH_MODE;
            msg.obj = flashMode;
            sendMessage(msg);
        }

        public void startPreview(boolean isFrontCamera) {
            Message msg = new Message();
            msg.what = MSG_PREVIEW_START;
            msg.obj = isFrontCamera;
            sendMessage(msg);
        }

        /**
         * request to stop camera preview
         *
         * @param needWait need to wait for stopping camera preview
         */
        public void stopPreview(final boolean needWait) {
            synchronized (this) {
                sendEmptyMessage(MSG_PREVIEW_STOP);
                if (needWait && mThread.mIsRunning) {
                    try {
                        if (DEBUG) Log.d(TAG, "wait for terminating of camera thread");
                        wait();
                    } catch (final InterruptedException e) {
                    }
                }
            }
        }

        /**
         * message handler for camera thread
         */
        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case MSG_PREVIEW_START:
                    mThread.startPreview((boolean) msg.obj);
                    break;
                case MSG_PREVIEW_STOP:
                    mThread.stopPreview();
                    synchronized (this) {
                        notifyAll();
                    }
                    Looper.myLooper().quit();
                    mThread = null;
                    break;
                case MSG_FLASH_MODE:
                    mThread.setFlashMode((FlashMode) msg.obj);
                    break;
                default:
                    throw new RuntimeException("unknown message:what=" + msg.what);
            }
        }
    }

    /**
     * Thread for asynchronous operation of camera preview
     */
    private static final class CameraThread extends Thread {
        private final Object mReadyFence = new Object();
        private final WeakReference<CameraGLView> mWeakParent;
        private CameraHandler mHandler;
        private volatile boolean mIsRunning = false;
        private Camera mCamera;
        private boolean mIsFrontFace;

        public CameraThread(final CameraGLView parent) {
            super("Camera thread");
            mWeakParent = new WeakReference<CameraGLView>(parent);
        }

        public CameraHandler getHandler() {
            synchronized (mReadyFence) {
                try {
                    mReadyFence.wait();
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return mHandler;
        }

        /**
         * message loop
         * prepare Looper and create Handler for this thread
         */
        @Override
        public void run() {
            if (DEBUG) Log.d(TAG, "Camera thread start");
            Looper.prepare();
            synchronized (mReadyFence) {
                mHandler = new CameraHandler(this);
                mIsRunning = true;
                mReadyFence.notify();
            }
            Looper.loop();
            if (DEBUG) Log.d(TAG, "Camera thread finish");
            synchronized (mReadyFence) {
                mHandler = null;
                mIsRunning = false;
            }
        }

        /**
         * start camera preview
         */
        private void startPreview(boolean isFrontCamera) {
            if (DEBUG) Log.v(TAG, "startPreview:");
            final CameraGLView parent = mWeakParent.get();
//			if ((parent != null) && (mCamera == null)) {
            if (parent != null) {
                // This is a sample project so just use 0 as camera ID.
                // it is better to selecting camera is available
                try {
                    //mCamera = Camera.open(CAMERA_ID);
                    openCamera(isFrontCamera);
                    final Camera.Parameters params = mCamera.getParameters();
                    final List<String> focusModes = params.getSupportedFocusModes();
                    if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                    } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                    } else {
                        if (DEBUG) Log.i(TAG, "Camera does not support autofocus");
                    }
                    // let's try fastest frame rate. You will get near 60fps, but your device become hot.
                    final List<int[]> supportedFpsRange = params.getSupportedPreviewFpsRange();
//					final int n = supportedFpsRange != null ? supportedFpsRange.size() : 0;
//					int[] range;
//					for (int i = 0; i < n; i++) {
//						range = supportedFpsRange.get(i);
//						Log.i(TAG, String.format("supportedFpsRange(%d)=(%d,%d)", i, range[0], range[1]));
//					}
                    final int[] max_fps = supportedFpsRange.get(supportedFpsRange.size() - 1);
                    Log.i(TAG, String.format("fps:%d-%d", max_fps[0], max_fps[1]));
                    params.setPreviewFpsRange(max_fps[0], max_fps[1]);
                    params.setRecordingHint(true);
                    // request preview size
                    // this is a sample project and just use fixed value
                    // if you want to use other size, you also need to change the recording size.
                    params.setPreviewSize(ScreenUtil.getScreenHeight(),ScreenUtil.getScreenWidth());
/*					final Size preferedSize = params.getPreferredPreviewSizeForVideo();
                    if (preferedSize != null) {
						params.setPreviewSize(preferedSize.width, preferedSize.height);
					} */
                    // rotate camera preview according to the device orientation
                    setRotation(params);
                    mCamera.setParameters(params);
                    // get the actual preview size
                    final Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
                    Log.i(TAG, String.format("previewSize(%d, %d)", previewSize.width, previewSize.height));
                    // adjust view size with keeping the aspect ration of camera preview.
                    // here is not a UI thread and we should request parent view to execute.
                    parent.post(new Runnable() {
                        @Override
                        public void run() {
                            parent.setVideoSize(previewSize.width, previewSize.height);
                        }
                    });
                    final SurfaceTexture st = parent.getSurfaceTexture();
                    if (st != null) {
                        st.setDefaultBufferSize(previewSize.width, previewSize.height);
                    }
                    mCamera.setPreviewTexture(st);
                } catch (final IOException e) {
                    Log.e(TAG, "startPreview:", e);
                    if (mCamera != null) {
                        mCamera.release();
                        mCamera = null;
                    }
                } catch (final RuntimeException e) {
                    Log.e(TAG, "startPreview:", e);
                    if (mCamera != null) {
                        mCamera.release();
                        mCamera = null;
                    }
                }
                if (mCamera != null) {
                    // start camera preview display
                    mCamera.startPreview();
                }
            }
        }

        /**
         * stop camera preview
         */
        private void stopPreview() {
            if (DEBUG) Log.v(TAG, "stopPreview:");
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
            final CameraGLView parent = mWeakParent.get();
            if (parent == null) return;
            parent.mCameraHandler = null;
        }

        /**
         * open front or default Camera
         */
        private void openCamera(boolean isFrontCamera) {
            //销毁Camera
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
            Log.i(TAG, "openCamera->isFrontCamera=" + isFrontCamera);
            if (!isFrontCamera) {//默认摄像头
                try {
                    mCamera = Camera.open(CAMERA_ID);
                } catch (Exception e) {
                    e.printStackTrace();
                    mCamera = null;
                }
            } else {//前置摄像头
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
                    Camera.getCameraInfo(i, cameraInfo);
                    if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                        try {
                            mCamera = Camera.open(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                            mCamera = null;
                        }
                    }
                }
            }
        }

        /**
         * rotate preview screen according to the device orientation
         *
         * @param params
         */
        private void setRotation(final Camera.Parameters params) {
            if (DEBUG) Log.v(TAG, "setRotation:");
            final CameraGLView parent = mWeakParent.get();
            if (parent == null) return;

            final Display display = ((WindowManager) parent.getContext()
                    .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            final int rotation = display.getRotation();
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
            // get whether the camera is front camera or back camera
            final Camera.CameraInfo info =
                    new Camera.CameraInfo();
            Camera.getCameraInfo(CAMERA_ID, info);
            mIsFrontFace = (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT);
            if (mIsFrontFace) {    // front camera
                degrees = (info.orientation + degrees) % 360;
                degrees = (360 - degrees) % 360;  // reverse
            } else {  // back camera
                degrees = (info.orientation - degrees + 360) % 360;
            }
            // apply rotation setting
            mCamera.setDisplayOrientation(degrees);
            parent.mRotation = degrees;
            // XXX This method fails to call and camera stops working on some devices.
//			params.setRotation(degrees);
        }

        private void setFlashMode(FlashMode flashMode) {
            if (mCamera == null)
                return;
            Camera.Parameters parameters = mCamera.getParameters();
            CameraGLView.flashMode = flashMode;
            switch (flashMode) {
                case ON:
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
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
                mCamera.setParameters(parameters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
