package com.ifeimo.im.activity;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ifeimo.im.R;
import com.ifeimo.im.view.InformationView;

public class TestInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_information);
        ((InformationView)findViewById(R.id.id_info)).init();
    }
}
