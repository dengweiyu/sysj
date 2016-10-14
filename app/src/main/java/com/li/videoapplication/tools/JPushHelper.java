package com.li.videoapplication.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.entity.VideoImageGroup;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.ActivityDetailActivity208;
import com.li.videoapplication.ui.activity.ActivityListActivity;
import com.li.videoapplication.ui.activity.GameMatchDetailActivity;
import com.li.videoapplication.ui.activity.GiftDetailActivity;
import com.li.videoapplication.ui.activity.GiftListActivity;
import com.li.videoapplication.ui.activity.GroupDetailActivity;
import com.li.videoapplication.ui.activity.HomeMoreActivity;
import com.li.videoapplication.ui.activity.MainActivity;
import com.li.videoapplication.ui.activity.RecommendActivity;
import com.li.videoapplication.ui.activity.VideoPlayActivity;
import com.li.videoapplication.ui.activity.WebActivity;
import com.li.videoapplication.utils.StringUtil;
import com.umeng.message.entity.UMessage;

import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


public class JPushHelper {

    public static final String TAG = JPushHelper.class.getSimpleName();

    public static void initUPush(Context context, boolean debug) {

        Log.d(TAG, "initUPush: ------------------------------------------------------");
        JPushInterface.setDebugMode(debug);
        JPushInterface.init(context);
    }

    public static void setAlias(String member_id) {

        if (member_id != null) {
            Log.d(TAG, "setAlias: ------------------------------------------------------");
            JPushInterface.setAlias(AppManager.getInstance().getApplication(),
                    member_id,
                    new TagAliasCallback() {
                        @Override
                        public void gotResult(int i, String s, Set<String> set) {
                            Log.d(TAG, "gotResult: i=" + i);
                            Log.d(TAG, "gotResult: s=" + s);
                        }
                    });
        }
    }

    public static void removeAlias() {
        Log.d(TAG, "removeAlias: ------------------------------------------------------");

        JPushInterface.setAlias(AppManager.getInstance().getApplication(),
                "",
                new TagAliasCallback() {
                    @Override
                    public void gotResult(int i, String s, Set<String> set) {
                        Log.d(TAG, "gotResult: i=" + i);
                        Log.d(TAG, "gotResult: s=" + s);
                    }
                });
    }

    /**
     * 自定义接收器
     * <p/>
     * 如果不定义这个 Receiver，则：
     * 1) 默认用户会打开主界面
     * 2) 接收不到自定义消息
     */
    public static class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: ------------------------------------------------------");
            Log.d(TAG, "onReceive: intent=" + intent);
            Bundle bundle = intent.getExtras();
            Log.d(TAG, "onReceive: bundle=" + bundle);
            String action = intent.getAction();
            Log.d(TAG, "onReceive: action=" + action);
            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                Log.d(TAG, "onReceive: key=" + key);
                Log.d(TAG, "onReceive: value=" + value);
            }
/*
            action=cn.jpush.android.intent.NOTIFICATION_RECEIVED
            key=cn.jpush.android.ALERT
            value=test_andorid_msg
            key=cn.jpush.android.EXTRA
            value={"push_msg_key":"event","push_msg_content":null,"push_msg_value":"104","push_msg_title":"test_android"}
            key=cn.jpush.android.NOTIFICATION_ID
            value=1885946526
            key=cn.jpush.android.NOTIFICATION_CONTENT_TITLE
            value=test_android
            key=cn.jpush.android.MSG_ID
            value=1885946526*/

            String json = bundle.getString(JPushInterface.EXTRA_EXTRA);
            // json = "{\"push_msg_key\":\"event\",\"push_msg_content\":null,\"push_msg_value\":\"104\",\"push_msg_title\":\"test_android\"}";
            if (json != null) {
                JPushModel model = null;
                try {
                    model = JSONHelper.parse(json, JPushModel.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (model != null) {
                    push_msg_key = model.getPush_msg_key();
                    push_msg_value = model.getPush_msg_value();
                }
            }

            // 收到通知
            if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "onReceive: notify");
                if (!StringUtil.isNull(push_msg_key) && !StringUtil.isNull(push_msg_value)) {
                    if (push_msg_key.equals(PK_SUCCESS)) {
                        FragmentActivity currentActivity = AppManager.getInstance().currentActivity();
                        if (currentActivity instanceof GameMatchDetailActivity) {
                            GameMatchDetailActivity activity = (GameMatchDetailActivity) currentActivity;
                            activity.loadData();
                        }
                    }
                }
                // 点击通知
            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Log.d(TAG, "onReceive: click");
                launchApp(context);
            }
        }

        private static final String EVENT = "event";
        private static final String VIDEO = "video";
        private static final String GAME_GROUP = "game_group";
        private static final String ACTIVITY = "activity";
        private static final String GIFT_PACKAGE = "gift_package";
        private static final String GO_URL = "go_url";
        private static final String SJYC = "sjyc";
        private static final String PLAYER = "player";
        private static final String NEWGAME = "newgame";
        private static final String PK_SUCCESS = "pk_success";

        private String push_msg_key, push_msg_value;
        private Handler handler = new Handler(Looper.getMainLooper());

        private boolean isNothing() {

            if (StringUtil.isNull(push_msg_value))
                return true;
            if (push_msg_value.equals("nothing"))
                return true;
            return false;
        }

        /*
        * 推送跳转：赛事详情
        */
        private void startGameMatchDetailActivity(final Context context) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent intent = new Intent();
                    if (isNothing()) {
                        intent.putExtra("tab", 4);
                        intent.setClass(context, MainActivity.class);
                    } else {
                        intent.putExtra("event_id", push_msg_value);
                        intent.setClass(context, GameMatchDetailActivity.class);
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 1200);
        }

        /*
         * 推送跳转：视频播放页
         */
        private void startVideoPlayActivity(final Context context) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    VideoImage item = null;
                    if (!isNothing()) {
                        item = new VideoImage();
                        item.setId(push_msg_value);
                        item.setVideo_id(push_msg_value);
                        intent.setClass(context, VideoPlayActivity.class);
                    } else {
                        intent.setClass(context, MainActivity.class);
                    }
                    intent.putExtra("item", item);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 1200);
        }

        /*
        * 推送跳转：游戏圈子详情
        */
        private void startGroupDetailActivity(final Context context) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    if (!isNothing()) {
                        intent.putExtra("group_id", push_msg_value);
                        intent.setClass(context, GroupDetailActivity.class);
                    } else {
                        intent.putExtra("tab", 3);
                        intent.setClass(context, MainActivity.class);
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 1200);
        }

        /*
        * 推送跳转：活动详情
        */
        private void startActivityDetailActivity208(final Context context) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    if (!isNothing()) {
                        intent.putExtra("match_id", push_msg_value);
                        intent.setClass(context, ActivityDetailActivity208.class);
                    } else {
                        intent.setClass(context, ActivityListActivity.class);
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 1200);
        }

        /*
        * 推送跳转：打开网页
        */
        private void startWebActivity(final Context context) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    WebActivity.startWebActivityNewTask(context, push_msg_value);
                }
            }, 1200);

        }

        /*
        * 推送跳转：礼包详情
        */
        private void startGiftDetailActivity(final Context context) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    if (!isNothing()) {
                        intent.putExtra("id", push_msg_value);
                        intent.setClass(context, GiftDetailActivity.class);
                    } else {
                        intent.setClass(context, GiftListActivity.class);
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 1200);
        }

        /*
        * 推送跳转：视界原创
        */
        private void startSJYCActivity(final Context context) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    VideoImageGroup group = new VideoImageGroup();
                    group.setTitle("视界原创");
                    group.setMore_mark("sysj_video");

                    Intent intent = new Intent();
                    intent.putExtra("group", group);
                    intent.setClass(context, HomeMoreActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 1200);
        }

        /*
        * 推送跳转：新游推荐
        */
        private void startNewGameActivity(final Context context) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    VideoImageGroup group = new VideoImageGroup();
                    group.setTitle("新游推荐");
                    group.setMore_mark("new_game_flag");

                    Intent intent = new Intent();
                    intent.putExtra("group", group);
                    intent.setClass(context, HomeMoreActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 1200);
        }

        /*
        * 推送跳转：精彩推荐
        */
        private void startRecommendActivity(final Context context) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    intent.setClass(context, RecommendActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 1200);
        }

        /*
        * 推送跳转：主页
        */
        private void startMainActivity(final Context context) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    intent.setClass(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 1200);
        }

        public void launchApp(Context context) {

            Log.d(TAG, "launchApp: push_msg_key = " + push_msg_key + " , push_msg_value = " + push_msg_value);

            if (!StringUtil.isNull(push_msg_key) &&
                    !StringUtil.isNull(push_msg_value)) {

                switch (push_msg_key) {
                    case PK_SUCCESS://匹配成功
                    case EVENT:// 赛事
                        startGameMatchDetailActivity(context);
                        break;
                    case VIDEO://视频播放页
                        startVideoPlayActivity(context);
                        break;
                    case GAME_GROUP:// 游戏圈
                        startGroupDetailActivity(context);
                        break;
                    case ACTIVITY:// 活动页
                        startActivityDetailActivity208(context);
                        break;
                    case GIFT_PACKAGE://礼包
                        startGiftDetailActivity(context);
                        break;
                    case GO_URL://打开url网页
                        startWebActivity(context);
                        break;
                    case SJYC:// 视界原创
                        startSJYCActivity(context);
                        break;
                    case PLAYER://玩家秀(发现-精彩推荐)
                        startRecommendActivity(context);
                        break;
                    case NEWGAME://新游推荐
                        startNewGameActivity(context);
                        break;
                    default:// nothing
                        startMainActivity(context);
                        break;
                }
            }
        }
    }

    public static class JPushModel {

        private String push_msg_key;
        private String push_msg_content;
        private String push_msg_value;
        private String push_msg_title;

        public String getPush_msg_key() {
            return push_msg_key;
        }

        public void setPush_msg_key(String push_msg_key) {
            this.push_msg_key = push_msg_key;
        }

        public String getPush_msg_content() {
            return push_msg_content;
        }

        public void setPush_msg_content(String push_msg_content) {
            this.push_msg_content = push_msg_content;
        }

        public String getPush_msg_value() {
            return push_msg_value;
        }

        public void setPush_msg_value(String push_msg_value) {
            this.push_msg_value = push_msg_value;
        }

        public String getPush_msg_title() {
            return push_msg_title;
        }

        public void setPush_msg_title(String push_msg_title) {
            this.push_msg_title = push_msg_title;
        }
    }
}

