package com.li.videoapplication.tools;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.entity.VideoImageGroup;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.WebActivity;
import com.li.videoapplication.utils.LogHelper;
import com.li.videoapplication.utils.StringUtil;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.util.Map;


public class UPushHelper {

    public static final String TAG = UPushHelper.class.getSimpleName();

    private static PushAgent mPushAgent;

    public static void initUPush(final Context context) {

        mPushAgent = PushAgent.getInstance(context);
        //开启推送并设置注册的回调处理
        connect();

        mPushAgent.setNotificationClickHandler(new UPushHandler());
    }

    public static void setAlias(String member_id) {
        LogHelper.d(TAG, "member_id: " + member_id);

        if (member_id != null) {
            mPushAgent.addAlias(member_id, "sysj_member_id", new UTrack.ICallBack() {
                @Override
                public void onMessage(boolean isSuccess, String message) {
                    LogHelper.d(TAG, "addAlias: isSuccess == " + isSuccess);
                }
            });
            mPushAgent.addExclusiveAlias(member_id, "sysj_member_id", new UTrack.ICallBack() {
                @Override
                public void onMessage(boolean isSuccess, String message) {
                    LogHelper.d(TAG, "addExclusiveAlias: isSuccess == " + isSuccess);
                }
            });
        }
    }

    private static void connect() {
        //开启推送并设置注册的回调处理
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.d(TAG, "======== Umeng Push register onSuccess ========");
                Log.d(TAG, "DeviceToken: " + deviceToken);

                if (PreferencesHepler.getInstance().isLogin())
                    setAlias(PreferencesHepler.getInstance().getMember_id());
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
    }

    /**
     * 该Handler是在BroadcastReceiver中被调用，故
     * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
     */
     public static class UPushHandler extends UmengNotificationClickHandler {

        private static final String EVENT = "event";
        private static final String VIDEO = "video";
        private static final String GAME_GROUP = "game_group";
        private static final String ACTIVITY = "activity";
        private static final String GIFT_PACKAGE = "gift_package";
        private static final String GO_URL = "go_url";
        private static final String SJYC = "sjyc";
        private static final String PLAYER = "player";
        private static final String NEWGAME = "newgame";

        private String push_msg_key, push_msg_value;
        private Handler handler = new Handler(Looper.getMainLooper());

        /*
        * 推送跳转：赛事详情
        */
        private void startGameMatchDetailActivity(final Context context) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ActivityManeger.startGameMatchDetailActivity(context, push_msg_value);
                }
            }, 250);
        }

        /*
         * 推送跳转：视频播放页
         */
        private void startVideoPlayActivity(final Context context) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    VideoImage item = new VideoImage();
                    item.setId(push_msg_value);
                    item.setVideo_id(push_msg_value);
                    Log.d(TAG, "startVideoPlayActivity: item == " + item);
                    ActivityManeger.startVideoPlayActivityNewTask(context, item);
                }
            }, 250);
        }

        /*
        * 推送跳转：游戏圈子详情
        */
        private void startGroupDetailActivity(final Context context) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ActivityManeger.startGroupDetailActivityNewTask(context, push_msg_value);
                }
            }, 250);
        }

        /*
        * 推送跳转：活动详情
        */
        private void startActivityDetailActivity208(final Context context) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ActivityManeger.startActivityDetailActivityNewTask(context, push_msg_value);
                }
            }, 250);
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
            }, 250);

        }

        /*
        * 推送跳转：礼包详情
        */
        private void startGiftDetailActivity(final Context context) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ActivityManeger.startGiftDetailActivityNewTask(context, push_msg_value);
                }
            }, 250);
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
                    ActivityManeger.startHomeMoreActivityNewTask(context, group);
                }
            }, 250);
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
                    ActivityManeger.startHomeMoreActivityNewTask(context, group);
                }
            }, 250);
        }

        /*
        * 推送跳转：精彩推荐
        */
        private void startRecommendActivity(final Context context) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ActivityManeger.startRecommendActivityNewTask(context);
                }
            }, 250);
        }

        @Override
        public void launchApp(Context context, UMessage uMessage) {
            super.launchApp(context, uMessage);

            for (Map.Entry<String, String> entry : uMessage.extra.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equals("push_msg_key")) {
                    push_msg_key = value;
                }
                if (key.equals("push_msg_value")) {
                    push_msg_value = value;
                }
            }

            Log.d(TAG, "launchApp: push_msg_key = " + push_msg_key + " , push_msg_value = " + push_msg_value);

            if (!StringUtil.isNull(push_msg_key) && !StringUtil.isNull(push_msg_value)) {

                switch (push_msg_key) {
                    case EVENT://赛事 event_id 109 ok
                        startGameMatchDetailActivity(context);
                        break;
                    case VIDEO://视频播放页 video_id 857309
                        startVideoPlayActivity(context);
                        break;
                    case GAME_GROUP://游戏圈 group_id 109 ok
                        startGroupDetailActivity(context);
                        break;
                    case ACTIVITY://活动页 match_id 69 ok
                        startActivityDetailActivity208(context);
                        break;
                    case GIFT_PACKAGE://礼包 id 389  ok
                        startGiftDetailActivity(context);
                        break;
                    case GO_URL://打开url网页
                        startWebActivity(context);
                        break;
                    case SJYC://视界原创
                        startSJYCActivity(context);
                        break;
                    case PLAYER://玩家秀(发现-精彩推荐)
                        startRecommendActivity(context);
                        break;
                    case NEWGAME://新游推荐
                        startNewGameActivity(context);
                        break;
                    default://nothing
                        break;
                }
            }
        }
    }
}

