package com.ifeimo.screenrecordlib.record.record50;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.util.Log;

import com.ifeimo.screenrecordlib.RecordingManager;
import com.ifeimo.screenrecordlib.constant.Configuration;
import com.ifeimo.screenrecordlib.util.TaskUtil;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 活动：5.0录屏（再简化线程）b
 */
@TargetApi(21)
public class ScreenRecordActivity extends Activity {

	private static final String TAG = ScreenRecordActivity.class.getSimpleName();

	public static final boolean DEBUG = true;

    private static String channel = "";

    private static AtomicBoolean isFirstStart = new AtomicBoolean(true);
    private MediaProjectionManager manager;
    private int resultCode;
    private Intent data;

    private Messenger mServiceMessenger;
    private Messenger mClientMessenger = new Messenger(new ClientHandler());
    private boolean mCon = false;

    private EncodingServiceConnection connection;

    private static class ClientHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    private class EncodingServiceConnection implements ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected------->");
            mServiceMessenger = new Messenger(service);

            mCon = true;

            sendMessageToService(path, configuration);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected------->");
            mCon = false;
        }
    }

    private void startEncodingService(){
        connection = new EncodingServiceConnection();
        Intent intent = new Intent(this, EncodingService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private void sendMessageToService(String path, Configuration configuration){
        if (!mCon)
            return;

        Object[] objects = new Object[5];
        objects[0] = resultCode;
        objects[1] = data;
        objects[2] = manager;
        objects[3] = path;
        objects[4] = configuration;
        Message msg = Message.obtain(null, EncodingService.CLIENT_MSG, objects);
        msg.replyTo = mClientMessenger;
        try{
            mServiceMessenger.send(msg);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

	public static void startRecording(Context context, String path, Configuration configuration) {
        if (isFirstStart.get()) {
            isFirstStart.set(false);

            Intent intent = new Intent();
            intent.setClass(context, ScreenRecordActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("path", path);
            intent.putExtra("configuration", configuration);
            try {
                context.startActivity(intent);
                Log.d(TAG, "startRecording: true");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            startRecording2(path, configuration);
        }
	}

    /**
     * 开启权限，用于第一次启动时获取权限
     */
	public static void openPermission(Context context, String path, Configuration configuration, String channel){
        ScreenRecordActivity.channel = channel;

        if (isFirstStart.get()) {
            isFirstStart.set(false);

            Intent intent = new Intent();
            intent.setClass(context, ScreenRecordActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("path", path);
            intent.putExtra("configuration", configuration);
            intent.putExtra("permission", true);
            try {
                context.startActivity(intent);
                Log.d(TAG, "startRecording: true");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------

	private static final int REQUEST_CODE = 1;
	private PowerManager powerManager;
	private PowerManager.WakeLock wakeLock;
	private String path;
	private Configuration configuration;
    public static boolean isApplyForPermission;

	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			path = getIntent().getStringExtra("path");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			configuration = (Configuration) getIntent().getSerializableExtra("configuration");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
            isApplyForPermission = getIntent().getBooleanExtra("permission", false);
        } catch (Exception e){
            e.printStackTrace();
        }
		if (path == null) {
			TaskUtil.clearTaskAndAffinity(this);
		}

		powerManager = (PowerManager) getSystemService(POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, TAG);

		manager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

		startRecording();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (wakeLock != null)
			wakeLock.acquire();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (wakeLock != null)
			wakeLock.release();
	}

	@Override
	protected void onDestroy() {
		if (handler != null)
			handler.removeCallbacksAndMessages(null);

        if (mCon && connection != null){
            unbindService(connection);
        }

        super.onDestroy();
	}

	@Override
	public void onConfigurationChanged(android.content.res.Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.d(TAG, "onConfigurationChanged: newConfig=" + newConfig);
	}

	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------

	private void startRecording() {
        Intent intent = manager.createScreenCaptureIntent();
        try {
            startActivityForResult(intent, REQUEST_CODE);
            Log.d(TAG, "startRecording2: true");
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	private static void startRecording2(String path, Configuration configuration) {
        EncodingService.startRecording2(path, configuration);
    }

	public static void stopRecording() {
		EncodingService.stopRecording();
	}

	public static void pauseRecording() {
		EncodingService.pauseRecording();
	}

	public static void restartRecording() {
		EncodingService.restartRecording();
	}

	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------

	private Handler handler = new Handler(Looper.getMainLooper());

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		Log.d(TAG, "onActivityResult: ----------------------------------------------------------");
		Log.d(TAG, "onActivityResult: requestCode=" + requestCode);
		Log.d(TAG, "onActivityResult: resultCode=" + resultCode);
		Log.d(TAG, "onActivityResult: data=" + data);

		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            RecordingManager.getInstance().getPermissionSuccess();

            this.resultCode = resultCode;
            this.data = data;

			moveTaskToBack(true);

			new Thread(new Runnable() {
				@Override
				public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            startEncodingService();
                        }
                    });
                }
			}, "newThread-ScreenRecordActivity").start();

            TaskUtil.clearTaskAndAffinity(this);
		} else { // 拒绝录屏权限
            RecordingManager.getInstance().getPermissionFail();
            // 重新弹出录屏权限直到点击允许为止
            if (!channel.equals("vivo")){
                startRecording();
            } else {
                isFirstStart.set(true);
                TaskUtil.clearTaskAndAffinity(this);
            }

//            if (Build.MANUFACTURER.equals("Xiaomi")) {
//                startRecording();
//            } else {
//                isFirstStart.set(true);
//                TaskUtil.clearTaskAndAffinity(this);
//            }
		}
	}

}
