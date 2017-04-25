package com.fmsysj.screeclibinvoke.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.li.videoapplication.data.model.event.ScreenRecordPermission2MainEvent;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.AppManager;
import com.ypy.eventbus.EventBus;

/**
 * 活动：5.0录屏（获取权限）
 */
public class ScreenRecordPermissionActivity extends Activity {

	private static final String TAG = ScreenRecordPermissionActivity.class.getSimpleName();

	public static void startPermission(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, ScreenRecordPermissionActivity.class);
		// intent.setAction(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		try {
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------

	private static final int REQUEST_CODE = 1;
	private MediaProjectionManager manager;
	private MediaProjection projection;
	private Handler handler = new Handler(Looper.getMainLooper());

	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------

	@TargetApi(21)
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
/*
		int a = checkPermission(Manifest.permission.RECORD_AUDIO, Process.myPid(), Process.myUid());
		int b = checkCallingPermission(Manifest.permission.RECORD_AUDIO);
		int c = checkCallingOrSelfPermission(Manifest.permission.RECORD_AUDIO);
		Log.d(TAG, "onCreate: checkPermission=" + a);
		Log.d(TAG, "onCreate: checkCallingPermission=" + b);
		Log.d(TAG, "onCreate: checkCallingOrSelfPermission=" + c);*/

		manager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
		Intent intent = manager.createScreenCaptureIntent();
		try {
			startActivityForResult(intent, REQUEST_CODE);
			Log.d(TAG, "onCreate: startActivityForResult");
		} catch (Exception e) {
			e.printStackTrace();
			finishTask();
		}
	}

	@Override
	protected void onDestroy() {/*
		if (handler != null)
			handler.removeCallbacksAndMessages(null);*/
//        EventBus.getDefault().post(new ScreenRecordPermission2MainEvent());
		ScreenRecordActivity activity = (ScreenRecordActivity)AppManager.getInstance().
				getActivity(ScreenRecordActivity.class);
		if (activity != null){
			activity.handler.sendEmptyMessage(1);
		}
		Log.i(TAG, "onDestroy");
		super.onDestroy();

	}

	@TargetApi(21)
	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		Log.d(TAG, "onActivityResult: ----------------------------------------------------------");

		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
			moveTaskToBack(true);
			projection = manager.getMediaProjection(resultCode, data);
			if (projection != null) {
				try {
					projection.stop();
					Log.d(TAG, "projection: stop");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		finishTask();
	}

	private void finishTask() {
		finish();
		Log.d(TAG, "TaskUtil: finish");
	}
}
