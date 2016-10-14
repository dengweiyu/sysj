package com.li.videoapplication.data.network;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * 功能：网络请求线程执行体
 */
public class RequestRunnable implements Runnable {

	protected final String action = this.getClass().getName();
	protected final String tag = this.getClass().getSimpleName();

	public static Handler handler = new Handler(Looper.getMainLooper());
	
	private RequestObject requestObject;
	private ResponseHandler responseHandler;
	private HttpClientMethod http;
	private OkHttpMethod ok;
	private ResponseObject responseObject;

	public RequestRunnable(RequestObject requestObject) {
		super();
		this.requestObject = requestObject;
		http = new HttpClientMethod();
		ok = new OkHttpMethod();

		try {
			Log.d(tag, "RequestRunnable/url=" + requestObject.getUrl());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			if (requestObject.getType() == Contants.TYPE_GET) {
				responseObject = ok.execute(requestObject);
			} else if (requestObject.getType() == Contants.TYPE_POST) {
				responseObject = ok.execute(requestObject);
			} else if (requestObject.getType() == Contants.TYPE_UPLOAD) {
				responseObject = http.execute(requestObject);
			} else if (requestObject.getType() == Contants.TYPE_DOWNLOAD) {
				ok.execute(requestObject);
			} else {
				throw new NullPointerException("this Http Method is not support");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		responseHandler = new ResponseHandler(responseObject);
		responseHandler.handle();
	}

	public void cancel() {
		if (http != null) {
			try {
				http.cancel();
				System.out.println(tag + "/cancel");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (ok != null) {
			try {
				ok.cancel();
				System.out.println(tag + "/cancel");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

