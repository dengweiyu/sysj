package com.li.videoapplication.data.network;

import android.annotation.SuppressLint;

import com.li.videoapplication.data.model.entity.NetworkError;
import com.li.videoapplication.utils.NetUtil;
import com.squareup.okhttp.apache.OkApacheClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.rong.eventbus.EventBus;

/**
 * 功能：HttpClient网络请求方法
 */
@SuppressLint("UseValueOf")
public class HttpClientMethod implements AbsRequestMethod {

	protected final String action = this.getClass().getName();
	protected final String tag = this.getClass().getSimpleName();

	// private HttpClient client = RequestClient.getHttpClient43();
	private OkApacheClient client = RequestClient.getOkApacheClient();
	private HttpRequestBase httpRequest;
	private HttpResponse httpResponse;
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

	/**
	 * 功能：HttpClient执行任务
	 */
	@Override
	public ResponseObject execute(RequestObject requestObject) throws Exception {
		if (requestObject == null) {
			throw new NullPointerException("ResponseObject is null");
		}
		this.requestObject = requestObject;
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
		files = requestObject.getFiles();
		if (files == null)
			files = new HashMap<>();
		newUrl = Utils.getNewUrl(url, params);
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
		} else {
			throw new NullPointerException("request type is unsuppoeted");
		}
		System.out.println(tag + "/requestObject=" + requestObject);
		System.out.println(tag + "/statusCode=" + statusCode);
		System.out.println(tag + "/resultString=" + resultString);
		return responseObject;
	}

	@Override
	public boolean cancel() {
		if (httpRequest != null && !httpRequest.isAborted()) {
			try {
				httpRequest.abort();
				System.out.println(tag + "/cancel/this=" + this);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

    /**
     * 功能：GET请求
     */
	@Override
	public void doGet() {
		httpRequest = new HttpGet(newUrl);
		execute();
		parseResult();
	}

	/**
	 * 功能：POST请求
	 */
	@Override
	public void doPost() {
		httpRequest = new HttpPost(url);
		try {
			((HttpPost) httpRequest).setEntity(new UrlEncodedFormEntity(Utils.getPairs(params), HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		execute();
		parseResult();
	}

	/**
	 * 功能：POST请求发送文件
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void uploadFile() {

		httpRequest = new HttpPost(url);
		MultipartEntity multipartEntity = new MultipartEntity();
		Set<String> paramsKey = params.keySet();
		for (String k : paramsKey) {
			if (params.get(k) != null) {
				StringBody body = null;
				try {
					body = new StringBody((String) params.get(k), Charset.defaultCharset());
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				if (body != null)
					multipartEntity.addPart(k, body);
			}
		}
		Set<String> filesKey = files.keySet();
		for (String k : filesKey) {
			if (files.get(k) != null) {
				FileBody body = new FileBody(files.get(k));
				multipartEntity.addPart(k, body);
			}
		}
		((HttpPost) httpRequest).setEntity(multipartEntity);
		execute();
		parseResult();
	}

	@Override
	public void downloadFile() {

	}


	/**
	 * 功能：执行请求
	 */
	private void execute() {
		for (int i = 0; i < 3; i++) {
			if (!NetUtil.isConnect()) {
				continue;
			}
			try {
				httpResponse = client.execute(httpRequest);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				break;
			}
		}
	}


	/**
	 * 功能：解析结果
	 */
	private void parseResult() {
		if (httpResponse != null) {
			statusCode = httpResponse.getStatusLine().getStatusCode();
			responseObject.setStatusCode(statusCode);
			if (statusCode == 200) {
				HttpEntity entity = httpResponse.getEntity();
				/*try {
					resultString = EntityUtils.toString(entity, HTTP.UTF_8);
				} catch (IOException e) {
					e.printStackTrace();
				}*/
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				if (inputStream != null) {
					InputStreamReader reader = null;
					try {
						reader = new InputStreamReader(inputStream, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					if (reader != null) {
						BufferedReader bf = new BufferedReader(reader);
						// 最好在将字节流转换为字符流的时候 进行转码
						StringBuffer buffer = new StringBuffer();
						String line;
						try {
							while ((line = bf.readLine()) != null) {
								buffer.append(line);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						resultString = buffer.toString();
					}
				}
				responseObject.setResultString(resultString);
			}
		}
	}

	/**
     * 功能：POST请求发送流
     */
	@SuppressWarnings("unused")
	public void doSocket() throws Exception {
		FormFile[] formFiles = Utils.getFormFiles(files);
		String fromJSESSIONID = null;
		final String BOUNDARY = "---------------------------7da2137580612"; // 数据分隔线
		final String endline = "--" + BOUNDARY + "--\r\n";// 数据结束标志
		int fileDataLength = 0;
		for (FormFile uploadFile : formFiles) {// 得到文件类型数据的总长度
			StringBuilder fileExplain = new StringBuilder();
			fileExplain.append("--");
			fileExplain.append(BOUNDARY);
			fileExplain.append("\r\n");
			fileExplain.append("Content-Disposition: form-data;className=\"" + uploadFile.getParameterName() + "\";filename=\"" + uploadFile.getFileName() + "\"\r\n");
			fileExplain.append("Content-Type: " + uploadFile.getContentType() + "\r\n\r\n");
			fileExplain.append("\r\n");
			fileDataLength += fileExplain.length();
			if (uploadFile.getInputStream() != null) {
				fileDataLength += uploadFile.getFile().length();
			} else {
				fileDataLength += uploadFile.getData().length;
			}
		}
		StringBuilder textEntity = new StringBuilder();
		for (Map.Entry<String, Object> entry : params.entrySet()) {// 构造文本类型参数的实体数据
			textEntity.append("--");
			textEntity.append(BOUNDARY);
			textEntity.append("\r\n");
			textEntity.append("Content-Disposition: form-data; className=\"" + entry.getKey() + "\"\r\n\r\n");
			textEntity.append(entry.getValue());
			textEntity.append("\r\n");
		}
		// 计算传输给服务器的实体数据总长度
		int dataLength = textEntity.toString().getBytes().length + fileDataLength + endline.getBytes().length;

		URL mURL = new URL(url);
		int port = mURL.getPort() == -1 ? 80 : mURL.getPort();
		Socket socket = new Socket(InetAddress.getByName(mURL.getHost()), port);
		OutputStream outputStream = socket.getOutputStream();
		// 下面完成HTTP请求头的发送
		String requestmethod = "POST" + mURL.getPath() + " HTTP/1.1\r\n";
		outputStream.write(requestmethod.getBytes());
		String accept = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*\r\n";
		outputStream.write(accept.getBytes());
		String language = "Accept-Language: zh-CN\r\n";
		outputStream.write(language.getBytes());
		String contenttype = "Content-Type: multipart/form-data; boundary=" + BOUNDARY + "\r\n";
		outputStream.write(contenttype.getBytes());
		String contentlength = "Content-Length: " + dataLength + "\r\n";
		outputStream.write(contentlength.getBytes());
		String host = "Host: " + mURL.getHost() + ":" + port + "\r\n";
		outputStream.write(host.getBytes());

		// 写完HTTP请求头后根据HTTP协议再写一个回车换行
		if (null != fromJSESSIONID) {
			String Connection = "Connection: Keep-Alive\r\n";
			outputStream.write(Connection.getBytes());
			String Cookie = "Cookie:" + "JSESSIONID=" + fromJSESSIONID + "\r\n";
			outputStream.write(Cookie.getBytes());
		}
		outputStream.write("\r\n".getBytes());
		// 把所有文本类型的实体数据发送出来
		outputStream.write(textEntity.toString().getBytes());

		// 把所有文件类型的实体数据发送出来
		for (FormFile uploadFile : formFiles) {
			StringBuilder fileEntity = new StringBuilder();
			fileEntity.append("--");
			fileEntity.append(BOUNDARY);
			fileEntity.append("\r\n");
			fileEntity.append("Content-Disposition: form-data;className=\"" + uploadFile.getParameterName() + "\";filename=\"" + uploadFile.getFileName() + "\"\r\n");
			fileEntity.append("Content-Type: " + uploadFile.getContentType() + "\r\n\r\n");

			outputStream.write(fileEntity.toString().getBytes());
			if (uploadFile.getInputStream() != null) {
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = uploadFile.getInputStream().read(buffer, 0, 1024)) != -1) {
					outputStream.write(buffer, 0, len);
				}
				uploadFile.getInputStream().close();
			} else {
				outputStream.write(uploadFile.getData(), 0, uploadFile.getData().length);
			}
			outputStream.write("\r\n".getBytes());
		}
		// 下面发送数据结束标志，表示数据已经结束
		outputStream.write(endline.getBytes());

		// 取得Response内容
		InputStream is = socket.getInputStream();
		int ch;
		StringBuffer b = new StringBuffer();
		while ((ch = is.read()) != -1) {
			b.append((char) ch);
		}

		// 循环历遍字符串,提取返回值
		String response = "";
		String tem = b.toString().trim();// 中文乱码
		tem = new String(tem.getBytes("iso-8859-1"), "utf-8");// 不会乱码
		while (tem.contains("{")) {
			tem = tem.substring(tem.indexOf("{") + 1);
			System.out.println("{" + tem.substring(0, tem.indexOf("}") + 1));
			response = "{" + tem.substring(0, tem.indexOf("}") + 1);
		}

		outputStream.flush();
		outputStream.close();
		socket.close();
		responseObject.setResultString(response);
		responseObject.setStatusCode(200);
	}

	/**
     * 功能：POST请求发送流
     */
	private void doStream() throws Exception {
		httpRequest = new HttpPost(url);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		Set<String> paramsKey = params.keySet();
		for (String k : paramsKey) {
			if (params.get(k) != null) {
				builder.addTextBody(k, (String) params.get(k));
			}
		}
		Set<String> filesKey = files.keySet();
		for (String k : filesKey) {
			if (files.get(k) != null) {
				builder.addBinaryBody(k, files.get(k));
			}
		}
		((HttpPost) httpRequest).setEntity(builder.build());
		execute();
		parseResult();
	}
}
