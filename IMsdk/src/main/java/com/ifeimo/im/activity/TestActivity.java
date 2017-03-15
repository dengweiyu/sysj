package com.ifeimo.im.activity;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.R;
import com.ifeimo.im.common.bean.ConnectBean;
import com.ifeimo.im.common.util.PManager;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.interface_im.IMWindow;

@Deprecated
public class TestActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private EditText servierEt;
    private EditText portEt;
    private EditText roomEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        init();
    }

    private void init() {
        roomEt = (EditText) findViewById(R.id.roomEt);
        portEt = (EditText) findViewById(R.id.portEt);
        servierEt = (EditText) findViewById(R.id.servierEt);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("修改参数");
        toolbar.setLogo(R.drawable.logo_round);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setNavigationIcon(R.drawable.ab_goback_gray);
    }


    public void saveOnClick(View v){

        boolean flag = false;

        if(!roomEt.getText().toString().equals("")){
            flag = !flag;
            PManager.saveDefaultRoom(this,roomEt.getText().toString());

        }

        if(!servierEt.getText().toString().equals("")
                && !portEt.getText().toString().equals("")){


            ConnectBean connect = PManager.getConnectConfig(this);
            connect.setPort(Integer.parseInt(portEt.getText().toString()));
            connect.setHost(servierEt.getText().toString());
            connect.setServiceName(servierEt.getText().toString());
            PManager.saveConnectConfig(this,connect);
            if(flag){
                Toast.makeText(this,"修改默认房间和服务器参数成功！",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"服务器参数成功！",Toast.LENGTH_SHORT).show();
            }

            IMSdk.logout(this,false);
            Proxy.getConnectManager().disconnect();
            //startActivity(new Intent(ChatWindowsManage.getInstence().getFirstWindow().getContext(), LoginService.class));

//            for(IMWindows w : ChatWindowsManage.getInstence().getAllIMWindows()){
//                ((Activity)w.getContext()).finish();
//            }
//            startActivity(new Intent(getApplicationContext(), LoginService.class));

        }else{
            if(flag){
                Toast.makeText(this,"修改参数！",Toast.LENGTH_SHORT).show();
                for(IMWindow w : Proxy.getIMWindowManager().getAllIMWindows()){
                    ((Activity)w.getContext()).finish();
                }
            }else{
                Toast.makeText(this,"参数无变化！",Toast.LENGTH_SHORT).show();
            }
        }

        finish();

    }
}
