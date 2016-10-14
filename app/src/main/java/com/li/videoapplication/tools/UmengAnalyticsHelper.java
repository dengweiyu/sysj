package com.li.videoapplication.tools;

import android.content.Context;
import android.util.Log;

import com.li.videoapplication.utils.StringUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能：友盟统计分析
 */
public class UmengAnalyticsHelper {

    private static final String TAG = UmengAnalyticsHelper.class.getSimpleName();

    // ##########  宏观数据  ##########

    public static final String MACROSCOPIC_DATA = "210_macroscopic_data";

    // ##########  首页  ##########

    public static final String MAIN = "210_main";

    // ##########  发现  ##########

    public static final String DISCOVER = "210_discover";

    // ##########  找游戏  ##########

    public static final String GAME = "210_game";

    // ##########  赛事  ##########

    public static final String MATCH = "210_match";

    // ##########  左侧栏  ##########

    public static final String SLIDER = "210_slider";

    // ##########  视频播放页面  ##########

    public static final String VIDEOPLAY = "210_videoplay";

    // ##########  分享  ##########

    public static final String SHARE = "210_share";


    // -----------------------------------------------------------------------------

    public static void onPause(Context context) {
        MobclickAgent.onPause(context);
    }

    public static void onResume(Context context) {
        MobclickAgent.onResume(context);
    }

    // -----------------------------------------------------------------------------

    public static void onEvent(Context context, String eventId, String eventType) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("type", eventType);
            MobclickAgent.onEvent(context, eventId, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void onMainGameEvent(Context context, String more_mark) {
        switch (more_mark) {
            case "sysj_video":
                onEvent(context, MAIN, "视界原创");
                break;
            case "new_game_flag":
                onEvent(context, MAIN, "新游推荐");
                break;
            case "gameid_5057":
                onEvent(context, MAIN, "王者荣耀");
                break;
            case "gameid_5062":
                onEvent(context, MAIN, "全民超神");
                break;
            case "gameid_93":
                onEvent(context, MAIN, "我的世界");
                break;
            case "gameid_8128":
                onEvent(context, MAIN, "穿越火线");
                break;
            case "gameid_9042":
                onEvent(context, MAIN, "皇室战争");
                break;
            case "gameid_39":
                onEvent(context, MAIN, "海岛奇兵");
                break;
            case "typeid_66":
                onEvent(context, MAIN, "赛车跑酷");
                break;
            case "typeid_64":
                onEvent(context, MAIN, "策略塔防");
                break;
            case "typeid_62":
                onEvent(context, MAIN, "卡牌游戏");
                break;
            case "typeid_57":
                onEvent(context, MAIN, "飞行射击");
                break;
            case "typeid_51":
                onEvent(context, MAIN, "角色扮演");
                break;
        }
    }

    public static void onMainGameMoreEvent(Context context, String more_mark) {
        switch (more_mark) {
            case "sysj_video":
                onEvent(context, MAIN, "视界原创更多");
                break;
            case "new_game_flag":
                onEvent(context, MAIN, "新游推荐更多");
                break;
            case "gameid_5057":
                onEvent(context, MAIN, "王者荣耀更多");
                break;
            case "gameid_5062":
                onEvent(context, MAIN, "全民超神更多");
                break;
            case "gameid_93":
                onEvent(context, MAIN, "我的世界更多");
                break;
            case "gameid_8128":
                onEvent(context, MAIN, "穿越火线更多");
                break;
            case "gameid_9042":
                onEvent(context, MAIN, "皇室战争更多");
                break;
            case "gameid_39":
                onEvent(context, MAIN, "海岛奇兵更多");
                break;
            case "typeid_66":
                onEvent(context, MAIN, "赛车跑酷更多");
                break;
            case "typeid_64":
                onEvent(context, MAIN, "策略塔防更多");
                break;
            case "typeid_62":
                onEvent(context, MAIN, "卡牌游戏更多");
                break;
            case "typeid_57":
                onEvent(context, MAIN, "飞行射击更多");
                break;
            case "typeid_51":
                onEvent(context, MAIN, "角色扮演更多");
                break;
        }
    }

    public static void onGameEvent(Context context, String group_type_id) {
        switch (group_type_id) {
            case "1":
                onEvent(context, GAME, "角色扮演-有效");
                break;
            case "2":
                onEvent(context, GAME, "竞技Moba-有效");
                break;
            case "3":
                onEvent(context, GAME, "动作街机-有效");
                break;
            case "4":
                onEvent(context, GAME, "休闲益智-有效");
                break;
            case "5":
                onEvent(context, GAME, "策略塔防-有效");
                break;
            case "6":
                onEvent(context, GAME, "赛车跑酷-有效");
                break;
            case "7":
                onEvent(context, GAME, "枪战射击-有效");
                break;
            case "8":
                onEvent(context, GAME, "音乐游戏-有效");
                break;
        }
    }

    public static void onGameMoreEvent(Context context, String group_type_id) {
        switch (group_type_id) {
            case "1":
                onEvent(context, GAME, "角色扮演");
                break;
            case "2":
                onEvent(context, GAME, "竞技Moba");
                break;
            case "3":
                onEvent(context, GAME, "动作街机");
                break;
            case "4":
                onEvent(context, GAME, "休闲益智");
                break;
            case "5":
                onEvent(context, GAME, "策略塔防");
                break;
            case "6":
                onEvent(context, GAME, "赛车跑酷");
                break;
            case "7":
                onEvent(context, GAME, "枪战射击");
                break;
            case "8":
                onEvent(context, GAME, "音乐游戏");
                break;
        }
    }

    public static void onShareEvent(Context context, String game_id) {
        switch (game_id) {
            case "5057":
                onEvent(context, SHARE, "分享类别-王者荣耀");
                break;
            case "5062":
                onEvent(context, SHARE, "分享类别-全民超神");
                break;
            case "10":
                onEvent(context, SHARE, "分享类别-天天酷跑");
                break;
            case "8128":
                onEvent(context, SHARE, "分享类别-穿越火线");
                break;
            case "89":
                onEvent(context, SHARE, "分享类别-乱斗西游2");
                break;
            case "7050":
                onEvent(context, SHARE, "分享类别-全民枪战");
                break;
            case "93":
                onEvent(context, SHARE, "分享类别-我的世界");
                break;
            case "9042":
                onEvent(context, SHARE, "分享类别-皇室战争");
                break;
            case "5027":
                onEvent(context, SHARE, "分享类别-炉石传说");
                break;
            case "75":
                onEvent(context, SHARE, "分享类别-部落冲突");
                break;
            case "8554":
                onEvent(context, SHARE, "分享类别-梦三国");
                break;
            case "444":
                onEvent(context, SHARE, "分享类别-自由之战");
                break;
            case "7063":
                onEvent(context, SHARE, "分享类别-刀塔传奇");
                break;
            case "3121":
                onEvent(context, SHARE, "分享类别-奇迹暖暖");
                break;
            case "4480":
                onEvent(context, SHARE, "分享类别-其他游戏");
                onEvent(context, MACROSCOPIC_DATA, "手机游戏-其他-上传视频数");
                break;
            case "5224":
                onEvent(context, MACROSCOPIC_DATA, "精彩生活-其他-上传视频数");
                break;
        }
    }
}
