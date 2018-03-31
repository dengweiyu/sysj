package com.li.videoapplication.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.li.videoapplication.R;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.BaseDialog;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.utils.TextUtil;


/**
 * Created by y on 2017/5/24.
 */

public class ServiceDialog extends BaseDialog implements View.OnClickListener{
    private LinearLayout service_btn;
    private LinearLayout feedback_btn;
    private TextView service_text;

    private Context context;

    public ServiceDialog(Context context){
        super(context);

        this.context = context;

        service_btn = (LinearLayout) findViewById(R.id.service_btn_dialog);
        feedback_btn = (LinearLayout) findViewById(R.id.feedback_btn_dialog);
        service_text = (TextView) findViewById(R.id.service_text_dialog);

        service_text.setText(Html.fromHtml(TextUtil.toColor("添 加 好 友", "#40a7ff") + " 后 才 能 智 能 回 复 问 题"));

        service_btn.setOnClickListener(this);
        feedback_btn.setOnClickListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_service;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.service_btn_dialog:
                // 直接打开qq某个人（可以是陌生人）
        		String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=" +
                        PreferencesHepler.getInstance().getInitializationSetting().getQq_online_service() +
                        "&version=1";
        		try {
        			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
        		} catch (Exception e) {
        			e.printStackTrace();
        			ToastHelper.s("未安装手Q或安装的版本不支持");
        		}
                break;
            case R.id.feedback_btn_dialog:
                ActivityManager.startFeedbackActivity(context);
                break;
        }

        dismiss();
    }



}
