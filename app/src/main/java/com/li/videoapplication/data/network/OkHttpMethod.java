package com.li.videoapplication.data.network;

import android.util.Log;

import com.li.videoapplication.data.cache.FileManager;
import com.li.videoapplication.data.model.response.DownloadSuccessEntity;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.rong.eventbus.EventBus;

/**
 * 功能：OKHttp网络请求方法
 */
public class OkHttpMethod implements AbsRequestMethod {

	protected final String action = this.getClass().getName();
	protected final String tag = this.getClass().getSimpleName();

	private OkHttpClient client = RequestClient.getOkHttpClient();
	private Request request;
	private Response response;
	private RequestObject requestObject;
	private int type;
	private String url;
	private Map<String, Object> params;
	private String newUrl;
	private Map<String, File> files;
	private String path;
	private ResponseObject responseObject;
	private int statusCode;
	private String resultString = "";

	private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
	private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
	private static final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain; charset=utf-8");
	private static final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream; charset=utf-8");

    /**
     * 功能：OkHttp执行任务
     */
	@Override
	public ResponseObject execute(RequestObject requestObject) throws Exception {
		if (requestObject == null) {
			throw new NullPointerException("ResponseObject is null");
		}
		this.requestObject = requestObject;
		System.out.println(tag + "/requestObject=" + requestObject);
		type = requestObject.getType();
		url = requestObject.getUrl();
		path = requestObject.getPath();
		if (url == null || url.length() == 0 || url.equals("null")) {
			throw new NullPointerException("url is null");
		}
		params = requestObject.getParams();
		if (params == null) {
			params = new HashMap<>();
		}
		newUrl = Utils.getNewUrl(url, params);
		files = requestObject.getFiles();
		if (files == null)
			files =  new HashMap<>();
		System.out.println(tag + "/url=" + url);
		System.out.println(tag + "/params=" + params);
		System.out.println(tag + "/newUrl=" + newUrl);
		System.out.println(tag + "/files=" + files);
		responseObject = Utils.newResponseObject(requestObject);

		if (type == Contants.TYPE_GET) {
			doGet();
		} else if (type == Contants.TYPE_POST) {
			doPost();
		} else if (type == Contants.TYPE_UPLOAD) {
			uploadFile();
		}  else if (type == Contants.TYPE_DOWNLOAD) {
			downloadFile();
		} else {
			throw new NullPointerException("request type is unsuppoeted");
		}
		System.out.println(tag + "/requestObject=" + requestObject);
		System.out.println(tag + "/statusCode=" + statusCode);
		System.out.println(tag + "/resultString=" + resultString);
		return responseObject;
	}

	@Override
	public void doGet() {
		get();
	}

	@Override
	public void doPost() {
		form();
	}

	@Override
	public void uploadFile() {
		multipart();
	}

	@Override
	public void downloadFile() {
		download();
	}

	/**
	 * Get方式访问网络
	 */
	private void get() {
		Log.d(tag, "get");
		request = new Request.Builder()
				.tag(newUrl)
				.url(newUrl)
				.build();
		execute();
		parseResult();

	}

	/**
	 * Get方式下載文件
	 *
	 * FIXME 此文件下载存在问题 请使用  FileDownloadRequest
	 */
	private void download() {
		Log.d(tag, "download");
		request = new Request.Builder()
				.url(url)
				.build();
		execute();
		downloadResult();
	}

	/**
	 * Post方式提交表单
	 */
	private void form() {
		Log.d(tag, "form");
		RequestBody body = null;
		if (params != null) {
			FormEncodingBuilder builder = new FormEncodingBuilder();
			Set<String> keys = params.keySet();
			for (String key : keys) {
				if (params.get(key) != null) {
					String value = params.get(key).toString();
					builder.add(key, value);
				}
			}
			body = builder.build();
		}


		request = new Request.Builder()
				.url(url)
				.post(body)
				.build();
		execute();
		parseResult();
	}

	/**
	 * Post方式提交文件
	 */
	private void file() {
		Log.d(tag, "file");
		MediaType mediaType = MediaType.parse("application/octet-stream; charset=utf-8");
		File file = files.get("");
		RequestBody body = RequestBody.create(mediaType, file);
		request = new Request.Builder()
				.url(newUrl)
				.post(body)
				.build();
		execute();
		parseResult();
	}

	/**
	 * Post方式提交JSON
	 */
	public void json() {
		Log.d(tag, "json");
		RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, requestObject.getJson());
		request = new Request.Builder()
				.url(url)
				.post(body)
				.build();
		execute();
		parseResult();
	}

	/**
	 * Post方式提交分块请求
	 */
	public void multipart() {
		Log.d(tag, "multipart");
		MultipartBuilder builder = new MultipartBuilder()
				.type(MultipartBuilder.FORM);
		if (params != null) {
			Set<String> keys = params.keySet();
			for (String key : keys) {
				if (params.get(key) != null) {
					String value = params.get(key).toString();
					builder.addFormDataPart(key, value);
					//builder.addPart(
							//Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
							//RequestBody.create(MEDIA_TYPE_TEXT, value));
				}
			}
		}
		Set<String> set = files.keySet();
		for (String key : set) {
			if (files.get(key) != null) {
				File value = files.get(key);
				if (value != null)
					builder.addPart(
							Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
							RequestBody.create(MEDIA_TYPE_STREAM, value));

			}
		}

		RequestBody body = builder.build();
		request = new Request.Builder()
				.url(url)
				.post(body)
				.build();
		execute();
		parseResult();
	}

	/**
	 * 解析结果
	 */
	private void parseResult() {
		if (response != null) {
			try {
				resultString = response.body().string();
				Log.i(tag, "response(结果)字段：" + resultString);
			} catch (IOException e) {
				e.printStackTrace();
			}
			statusCode = response.code();
			responseObject.setStatusCode(statusCode);
			responseObject.setResultString(resultString);
		}
	}

	/**
	 * 保存文件
	 */
	private void downloadResult() {
		if (response != null) {
			InputStream is = null;
			try {
				is = response.body().byteStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (is != null) {
				FileManager.saveStream(url, is);
				//
				EventBus.getDefault().post(new DownloadSuccessEntity(url));
			}
		}
	}

	/**
	 * 执行网络访问
	 */
	private void execute() {
		try {
			Log.w(tag, "接收返回response..");
			response = client.newCall(request).execute();
			Log.w(tag, "返回response后..");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean cancel() {
		if (client != null && request != null) {
			try {
				client.cancel(request.tag());
				System.out.println(tag + "/cancel/this=" + this);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
