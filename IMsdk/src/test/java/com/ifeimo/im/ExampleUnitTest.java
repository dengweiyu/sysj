package com.ifeimo.im;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.ifeimo.im.common.bean.chat.GroupChatBean;
import com.ifeimo.im.common.bean.model.ChatMsgModel;
import com.ifeimo.im.common.bean.model.GroupChatModel;
import com.ifeimo.im.framwork.connect.IConnectSupport;
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
import y.com.sqlitesdk.framework.interface_model.IModel;

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

//        Response response = OkHttpUtils.get().url("http://im.17sysj.com:8080/IM/GetMemberInfo").addParams("memberId","1722092").tag("ad").build().execute();
//        System.out.print(response.body().string());

//        ChatMsgModel iModel = ChatMsgModel.class.newInstance();
//
//        System.err.println(iModel.hashCode());
//
//        System.err.println(iModel.clone().hashCode());
//
//        ChatMsgModel iModela = iModel;
//        iModela.setCreateTime("asd");
//        System.err.println(iModela);
//        System.err.println(iModel);
        IConnectSupport iConnectSupport = (IConnectSupport) Class.forName("com.ifeimo.im.framwork.connect.ConnectSupport",true,ClassLoader.getSystemClassLoader()).newInstance();

        System.err.print("iConnectSupport.TAG = "+IConnectSupport.class.getField("TAG").get(iConnectSupport));
    }
}