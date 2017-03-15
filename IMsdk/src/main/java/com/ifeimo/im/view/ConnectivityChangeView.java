package com.ifeimo.im.view;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.ifeimo.im.R;
import com.ifeimo.im.common.bean.ConnectivityChangeBean;
import com.ifeimo.im.common.util.ConnectUtil;
import com.ifeimo.im.common.util.ScreenUtil;
import com.ypy.eventbus.EventBus;

/**
 * Created by lpds on 2017/2/10.
 */
public class ConnectivityChangeView extends LinearLayout implements
        View.OnClickListener{

    public final String action = this.getClass().getName();

    public final String tag = this.getClass().getSimpleName();

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    private LayoutInflater inflater;

    private LinearLayout view, root;

    private Animation appearAnim, disappearAnim;

    public ConnectivityChangeView(Context context) {
        this(context, null);
    }

    public ConnectivityChangeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initContentView();
    }

    private void initContentView() {

        inflater = LayoutInflater.from(getContext());
        view = (LinearLayout) inflater.inflate(R.layout.view_connectivitychange, this);
        root = (LinearLayout) view.findViewById(R.id.connectivitychange_root);
        root.setOnClickListener(this);

        if (!ConnectUtil.isConnect(getContext())) {
            root.setVisibility(View.VISIBLE);
        } else {
            root.setVisibility(View.GONE);
        }

        int height = ScreenUtil.dp2px(getContext(),40);
        appearAnim = new TranslateAnimation(0, 0, - height, 0);
        appearAnim.setDuration(300);
        Animation.AnimationListener appearListener = new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                root.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                root.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        };
        appearAnim.setAnimationListener(appearListener);

        disappearAnim = new TranslateAnimation(0, 0, 0, - height);
        disappearAnim.setDuration(300);
        Animation.AnimationListener disappearListener = new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                root.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                root.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        };
        disappearAnim.setAnimationListener(disappearListener);
    }

    private void disappear() {
        if (root.getVisibility() == View.VISIBLE) {
            root.startAnimation(disappearAnim);
        }
    }

    private void appear() {
        if (root.getVisibility() != View.VISIBLE) {
            root.setVisibility(VISIBLE);
            root.startAnimation(appearAnim);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == root) {
            showConnectivityDialog(getContext());
        }
    }

    /**
     * 打开设置网络界面
     */
    private void showConnectivityDialog(final Context context){
        //提示对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("网络设置提示");
        builder.setMessage("网络连接不可用,是否进行设置?");
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent;
                //判断手机系统的版本  即API大于10 就是3.0或以上版本
                if (android.os.Build.VERSION.SDK_INT > 10) {
                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                } else {
                    intent = new Intent();
                    ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 回调：网络变化事件
     */
    public void onEventMainThread(ConnectivityChangeBean event) {
        try {
            Log.i(tag, "connectivity=" + event);
            Log.i(tag, "connectivity=" + event.getNetworkInfo());
            Log.i(tag, "connectivity=" + event.getNetworkInfo().getTypeName());
            Log.i(tag, "connectivity=" + event.getNetworkInfo().getSubtypeName());
            Log.i(tag, "connectivity=" + event.getNetworkInfo().getExtraInfo());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (root != null) {
            Log.i(tag, "1");
            NetworkInfo info = event.getNetworkInfo();
            notifyConnectivityChange(root, info);
        }
    }

    /**
     * 显示和影藏控件
     */
    private void notifyConnectivityChange(View view, NetworkInfo info) {

        Log.i(tag, "2");
        if (info != null && info.isAvailable()) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                // WIFI网络，隐藏断网提示
                //view.setVisibility(View.GONE);
                disappear();
            } else if (info.getType() == ConnectivityManager.TYPE_ETHERNET) {
                // 有线网络，隐藏断网提示
                // view.setVisibility(View.GONE);
                disappear();
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                // 移动网络，隐藏断网提示
                //view.setVisibility(View.GONE);
                disappear();
            } else {
                // 隐藏断网提示
                //view.setVisibility(View.GONE);
                disappear();
            }
        } else {
            // 网络断开，显示断网提示
            //view.setVisibility(View.VISIBLE);
            appear();
        }
    }
}

