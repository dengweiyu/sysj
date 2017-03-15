package com.ifeimo.im;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.apache.http.HttpConnection;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Response;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
//        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("http://im.17sysj.com:8080/IM/GetMemberInfo?memberId=1722092").openConnection();
//
//        httpURLConnection.setRequestMethod("GET");
//        InputStream inputStream = httpURLConnection.getInputStream();
//        byte[] bytes = new byte[2];
//        inputStream.read(bytes);
//        System.out.println(" JSON =  "+new String(bytes)+" 。 ");

        Response response = OkHttpUtils.get().url("http://im.17sysj.com:8080/IM/GetMemberInfo").addParams("memberId","1722092").tag("ad").build().execute();
        System.out.print(response.body().string());
    }
}