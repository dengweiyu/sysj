package com.fmsysj.screeclibinvoke.logic.videoedit;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import com.ifeimo.screenrecordlib.RecordingManager;
import com.fmsysj.screeclibinvoke.logic.frontcamera.FrontCameraManager;
import com.li.videoapplication.R;
import com.li.videoapplication.ui.toast.ToastHelper;

/**
 * 摇晃监听
 */
public class ShakeHelper implements SensorEventListener {

	public static final String TAG = ShakeHelper.class.getSimpleName();

	public static int FORCE_THRESHOLD = 4000;// 4000
	private static final int TIME_THRESHOLD = 100;
	private static final int SHAKE_TIMEOUT = 500;
	private static final int SHAKE_DURATION = 3000;
	private static final int SHAKE_COUNT = 6;

	private SensorManager sensorManager;
	private Sensor sensor;
	private float mLastX = -1.0f, mLastY = -1.0f, mLastZ = -1.0f;
	private long mLastTime;
	private OnShakeListener onShakeListener;
	private int mShakeCount = 0;
	private long mLastShake;
	private long mLastForce;

	public interface OnShakeListener {

		void onShake();
	}

	public ShakeHelper(Context context) {
		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		if (sensorManager != null) {
			sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		}
	}

	public void setOnShakeListener(OnShakeListener listener) {
		onShakeListener = listener;
	}

	private boolean first = true;

	public void resume() {
		Log.d(TAG, "resume: // ---------------------------------");

		if (sensorManager != null && sensor != null) {
			Log.d(TAG, "resume: 1");
			boolean supported = sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
			Log.d(TAG, "resume: supported=" + supported);
			if (!supported) {
				Log.d(TAG, "resume: 2");
				sensorManager.unregisterListener(this);
				if (first) {
					first = false;
				} else {
					ToastHelper.s(R.string.shake_not_support);
				}
			} else {
				Log.d(TAG, "resume: 3");
			}
		}
	}

	public void pause() {
		Log.d(TAG, "pause: // ---------------------------------");
		if (sensorManager != null) {
			sensorManager.unregisterListener(this);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		Log.d(TAG, "onAccuracyChanged: // ---------------------------------");
		Log.d(TAG, "onAccuracyChanged: accuracy" + accuracy);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent event) {
		Log.d(TAG, "onSensorChanged: // ---------------------------------");
		if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
			return;
		}

		Log.d(TAG, "onSensorChanged: 1");

		long now = System.currentTimeMillis();

		if ((now - mLastForce) > SHAKE_TIMEOUT) {
			mShakeCount = 0;
		}

		Log.d(TAG, "onSensorChanged: 2");

		if ((now - mLastTime) > TIME_THRESHOLD) {
			Log.d(TAG, "onSensorChanged: 3");
			long diff = now - mLastTime;
			float speed = Math.abs(event.values[SensorManager.DATA_X] + event.values[SensorManager.DATA_Y]
					+ event.values[SensorManager.DATA_Z] - mLastX - mLastY - mLastZ)
					/ diff * 10000;
			if (RecordingManager.getInstance().isRecording() &&
					FrontCameraManager.getInstance().isOpen())
				FORCE_THRESHOLD = 350;
			Log.d(TAG, "onSensorChanged: diff=" + diff);
			Log.d(TAG, "onSensorChanged: speed=" + speed);
			Log.d(TAG, "onSensorChanged: FORCE_THRESHOLD=" + FORCE_THRESHOLD);
			if (speed > FORCE_THRESHOLD) {
				Log.d(TAG, "onSensorChanged: 4");
				Log.d(TAG, "onSensorChanged: now=" + now);
				Log.d(TAG, "onSensorChanged: mLastShake=" + mLastShake);
				Log.d(TAG, "onSensorChanged: SHAKE_DURATION=" + SHAKE_DURATION);
				if (now - mLastShake > SHAKE_DURATION) {
					Log.d(TAG, "onSensorChanged: 5");
					mLastShake = now;
					mShakeCount = 0;
					if (onShakeListener != null) {
						Log.d(TAG, "onSensorChanged: 6");
						onShakeListener.onShake();
					}
				}
				mLastForce = now;
			}
			mLastTime = now;
			mLastX = event.values[SensorManager.DATA_X];
			mLastY = event.values[SensorManager.DATA_Y];
			mLastZ = event.values[SensorManager.DATA_Z];
		}
	}
}