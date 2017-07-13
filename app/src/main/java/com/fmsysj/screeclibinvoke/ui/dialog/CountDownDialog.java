package com.fmsysj.screeclibinvoke.ui.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.widget.TextView;

import com.fmsysj.screeclibinvoke.data.model.event.CountDownFinishEvent;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.BaseDialog;


import io.rong.eventbus.EventBus;

/**
 * Created by y on 2017/5/20.
 */

public class CountDownDialog extends BaseDialog {
    private TextView count;
    private TextView textView;

    private int number = 3;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 1:
                    if (number > 0) {
                        count.setText(number + "");
                        number--;

                        removeMessages(1);
                        sendEmptyMessageDelayed(1, 1000);
                    } else if (number == 0){
                        count.setText(number + "");
                        EventBus.getDefault().post(new CountDownFinishEvent());
                        dismiss();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public CountDownDialog(Context context){
        super(context);

        count = (TextView)findViewById(R.id.count_down_number);
        textView = (TextView) findViewById(R.id.count_down_text);

        textView.setText(Html.fromHtml("准备返回桌面并开始录屏"));

        handler.sendEmptyMessage(1);

        setCanceledOnTouchOutside(false);
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_down_count;
    }

}
