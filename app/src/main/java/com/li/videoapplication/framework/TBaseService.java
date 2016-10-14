package com.li.videoapplication.framework;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * 基本服务
 */
public abstract class TBaseService extends BaseService {
	
    public class MyBinder extends Binder {
    	
        public TBaseService getService() {
            return TBaseService.this;
        }
    }

    protected MyBinder myBinder = new MyBinder();

	@Override
	public IBinder onBind(Intent intent) {
		Log.e(tag, "onBind/Binder=" + myBinder + "/intent=" + intent);
		return myBinder;
	}

    @Override
	public boolean onUnbind(Intent intent) {
		Log.e(tag, "onUnbind/intent=" + intent);
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) {
		Log.e(tag, "onUnbind/intent=" + intent);
		super.onRebind(intent);
	}
}
