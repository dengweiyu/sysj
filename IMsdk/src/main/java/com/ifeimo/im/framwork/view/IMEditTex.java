package com.ifeimo.im.framwork.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.ifeimo.im.common.util.StringUtil;
import com.ifeimo.im.framwork.Proxy;

/**
 * Created by lpds on 2017/2/17.
 */
public class IMEditTex extends EditText implements TextView.OnEditorActionListener{

    private boolean isListenSoft = false;


    public IMEditTex(Context context) {
        super(context);
        setOnEditorActionListener(this);
    }

    public IMEditTex(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnEditorActionListener(this);
    }

    public IMEditTex(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        switch (i){
            case EditorInfo.IME_ACTION_SEND:
                if(!StringUtil.isNull(getText().toString())) {
                    Proxy.getIMWindowManager().getLastWindow().send(getText().toString());
                    setText("");
                    return true;
                }
            break;
        }
        return false;
    }

}
