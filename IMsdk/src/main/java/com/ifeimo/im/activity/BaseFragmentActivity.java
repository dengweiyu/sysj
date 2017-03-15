package com.ifeimo.im.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

/**
 * Created by lpds on 2017/1/17.
 */
abstract class BaseFragmentActivity extends FragmentActivity{




    protected Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
