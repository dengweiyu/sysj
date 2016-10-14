package com.li.videoapplication.data.network;

import com.li.videoapplication.framework.AsyncTask;

/**
 * 功能：网络请求任务
 */
public class RequestTask extends AsyncTask<RequestObject, Integer, ResponseObject> {

	protected final String action = this.getClass().getName();
	protected final String tag = this.getClass().getSimpleName();

	private RequestObject requestObject;
	private ResponseHandler responseHandler;
	private HttpClientMethod httpClientMethod;
	private OkHttpMethod okHttpMethod;
	private ResponseObject responseObject;

	@Override
	protected ResponseObject doInBackground(RequestObject... params) {
		this.requestObject = params[0];
		httpClientMethod = new HttpClientMethod();
		okHttpMethod = new OkHttpMethod();
		try {
			if (requestObject.getType() == Contants.TYPE_GET) {
				responseObject = okHttpMethod.execute(requestObject);
			} else if (requestObject.getType() == Contants.TYPE_POST) {
				responseObject = okHttpMethod.execute(requestObject);
			} else if (requestObject.getType() == Contants.TYPE_UPLOAD) {
				responseObject = httpClientMethod.execute(requestObject);
			}else {
				throw new NullPointerException("this Http Method is not support");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		responseHandler = new ResponseHandler(responseObject);
		responseHandler.handle();
		return responseObject;
	}

	public void cancel() {
		if (httpClientMethod != null) {
			try {
				httpClientMethod.cancel();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (okHttpMethod != null) {
			try {
				okHttpMethod.cancel();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
