package com.li.videoapplication.data.download;


import android.support.annotation.Nullable;
import android.util.Log;


import com.li.videoapplication.data.local.FileUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by liuwei on 2016/11/9.
 */

public class FileDownloadRequest{
    private DownloadListener mListener;
    private String mPath;
    private Call mCall;
    private long mFileSize;
    private long mDownloadSize;
    public void download(String url, String path,long fileSize,@Nullable DownloadListener listener){
        try {
            if (url == null){
                return;
            }
            if (listener != null){
                mListener = listener;
            }
            mPath = path;
            mFileSize = fileSize;
            if (mFileSize > 0){
                mDownloadSize = FileUtil.getFileSize(mPath);
                request(url,mDownloadSize,fileSize);
            }else {
                FileUtil.deleteFile(mPath);     //不支持断点 删除之前的
                FileUtil.createFile(mPath);
                request(url);
            }

        } catch (IOException e) {
            e.printStackTrace();
            if (mListener != null){
                mListener.finish();
            }
        }
    }

    final Callback mCallBack = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            if (mListener != null){
                mListener.finish();
            }
            Log.e("FileDownloadRequest","onFailure");
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (writeToFile(response)){

            }else {
                if (mListener != null){
                    mListener.finish();
                }
            }
        }
    };

    //支持断点下载
    private void request(String url,long startPoint,long fileSize){
        Log.e("fileDownloadUrl",url);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .header("Range","bytes="+startPoint+"-"+fileSize)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());
                        return originalResponse.newBuilder()
                                .body(new ProgressResponseBody(originalResponse.body(), mListener))
                                .build();
                    }
                })
                .build();
        mCall = client.newCall(request);
        mCall.enqueue(mCallBack);
    }

    //不支持断点下载
    private void request(String url){
        Log.e("fileDownloadUrl",url);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();
        mCall = new OkHttpClient().newCall(request);
        mCall.enqueue(mCallBack);
    }

    //save,默认断点为文件末尾
    private boolean writeToFile(Response response){
        boolean result  = false;
        ResponseBody body = response.body();
        String totalSizeStr =  response.header("Content-Length","0");
        InputStream is = body.byteStream();
        FileChannel channel = null;
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = FileUtil.getRandomFile(mPath);
            if (randomAccessFile == null){
                return false;
            }
            channel = randomAccessFile.getChannel();
            long startsPoint = FileUtil.getFileSize(mPath);
            randomAccessFile.seek(startsPoint);
            //下面这种做法 需要保存断点的位置 还是直接用随机写入把 不然还得要同步断点的情况，，比如文件被删除了
            // MappedByteBuffer mappedBuffer = channel.map(FileChannel.MapMode.READ_WRITE, startsPoint, body.contentLength());
            byte[] buffer = new byte[1024];
            int len;
            long downloadSize = 0;
            long totalSize = Long.parseLong(totalSizeStr);
            while ((len = is.read(buffer)) != -1) {
                //mappedBuffer.put(buffer, 0, len);
                randomAccessFile.write(buffer,0,len);
                if (mListener != null && mFileSize == 0){
                    downloadSize += len;
                    mListener.progress(downloadSize,totalSize,false);
                }
            }
            if (mListener != null && mFileSize == 0){
                mListener.progress(downloadSize,downloadSize,true);
            }
            result = true;
        }catch (IOException e1){
            e1.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
                if (channel != null){
                    channel.close();
                }
                if (randomAccessFile != null){
                    randomAccessFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    //官方demo
    private  class ProgressResponseBody extends ResponseBody {
        private final ResponseBody responseBody;
        DownloadListener progressListener;
        private BufferedSource bufferedSource;

        public ProgressResponseBody(ResponseBody responseBody,DownloadListener progressListener) {
            this.responseBody = responseBody;
            this.progressListener = progressListener;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = 0;
                    try {
                        bytesRead = super.read(sink, byteCount);
                        // read() returns the number of bytes read, or -1 if this source is exhausted.
                        totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                       // progressListener.progress(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                        progressListener.progress(totalBytesRead+mDownloadSize, mFileSize, bytesRead == -1);
                    } catch (Exception e) {
                        e.printStackTrace();
                      if (mListener != null){
                          mListener.finish();
                      }
                    }
                    return bytesRead;
                }
            };
        }
    }

    public interface DownloadListener {
        void progress(long totalBytesRead,long contentLength , boolean isDone);

        void finish();
    }
}
