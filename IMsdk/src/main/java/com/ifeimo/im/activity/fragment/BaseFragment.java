package com.ifeimo.im.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lpds on 2017/1/19.
 */
abstract class BaseFragment extends Fragment {

    protected ViewGroup contentView;
    protected static final String TGA = "XMPP_Fragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(contentView == null){
            contentView = (ViewGroup) inflater.inflate(getContentViewByID(),null);
        }
        init();

        return contentView;
    }

    protected abstract int getContentViewByID();

    protected abstract void init();

    protected void log(String msg){
        Log.i(TGA,msg);
    }


}
