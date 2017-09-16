package com.li.videoapplication.tools;

import android.content.Context;

import com.li.videoapplication.utils.StringUtil;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

    // ##########  视频播放停留时长  ##########

    public static final String VIDEO_PLAY_DURATION = "210_play_duration";


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

    /**
     * 获取渠道
     */
    public static String getChannel(Context context){
        return AnalyticsConfig.getChannel(context);
    }

    public static void onMainMoreHotEvent(Context context, String more_mark) {
        switch (more_mark) {
            case "sysj_video":
                onEvent(context, MAIN, "视界原创-最热-点击视界原创最热次数");
                break;
            case "new_game_flag":
                onEvent(context, MAIN, "新游推荐-最热-点击新游推荐最热次数");
                break;
            case "gameid_5057":
                onEvent(context, MAIN, "王者荣耀-最热-点击王者荣耀最热次数");
                break;
            case "gameid_5062":
                onEvent(context, MAIN, "全民超神-最热-点击全民超神最热次数");
                break;
            case "gameid_93":
                onEvent(context, MAIN, "我的世界-最热-点击我的世界最热次数");
                break;
            case "gameid_8128":
                onEvent(context, MAIN, "穿越火线-最热-点击穿越火线最热次数");
                break;
            case "gameid_9042":
                onEvent(context, MAIN, "皇室战争-最热-点击皇室战争最热次数");
                break;
            case "gameid_39":
                onEvent(context, MAIN, "海岛奇兵-最热-点击海岛奇兵最热次数");
                break;
            case "typeid_66":
                onEvent(context, MAIN, "赛车跑酷-最热-点击赛车跑酷最热次数");
                break;
            case "typeid_64":
                onEvent(context, MAIN, "策略塔防-最热-点击策略塔防最热次数");
                break;
            case "typeid_62":
                onEvent(context, MAIN, "卡牌游戏-最热-点击卡牌游戏最热次数");
                break;
            case "typeid_57":
                onEvent(context, MAIN, "飞行射击-最热-点击飞行射击最热次数");
                break;
            case "typeid_51":
                onEvent(context, MAIN, "角色扮演-最热-点击角色扮演最热次数");
                break;
        }
    }

    public static void onMainMoreEvent(Context context, String more_mark, boolean isHomeMoreNew) {
        switch (more_mark) {
            case "sysj_video":
                mainMoreAnalytics(context, isHomeMoreNew, "视界原创");
                break;
            case "new_game_flag":
                mainMoreAnalytics(context, isHomeMoreNew, "新游推荐");
                break;
            case "gameid_5057":
                mainMoreAnalytics(context, isHomeMoreNew, "王者荣耀");
                break;
            case "gameid_5062":
                mainMoreAnalytics(context, isHomeMoreNew, "全民超神");
                break;
            case "gameid_93":
                mainMoreAnalytics(context, isHomeMoreNew, "我的世界");
                break;
            case "gameid_8128":
                mainMoreAnalytics(context, isHomeMoreNew, "穿越火线");
                break;
            case "gameid_9042":
                mainMoreAnalytics(context, isHomeMoreNew, "皇室战争");
                break;
            case "gameid_39":
                mainMoreAnalytics(context, isHomeMoreNew, "海岛奇兵");
                break;
            case "typeid_66":
                mainMoreAnalytics(context, isHomeMoreNew, "赛车跑酷");
                break;
            case "typeid_64":
                mainMoreAnalytics(context, isHomeMoreNew, "策略塔防");
                break;
            case "typeid_62":
                mainMoreAnalytics(context, isHomeMoreNew, "卡牌游戏");
                break;
            case "typeid_57":
                mainMoreAnalytics(context, isHomeMoreNew, "飞行射击");
                break;
            case "typeid_51":
                mainMoreAnalytics(context, isHomeMoreNew, "角色扮演");
                break;
        }
    }

    private static void mainMoreAnalytics(Context context, boolean isHomeMoreNew, String name) {
        if (isHomeMoreNew) {
            onEvent(context, MAIN, name + "-最新-点击" + name + "内任意视频次数");
        } else {
            onEvent(context, MAIN,  name + "-最热-点击" + name + "最热任意视频次数");
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

    public static void onGameListFocusEvent(Context context, String group_type_id) {
        switch (group_type_id) {
            case "1":
                onEvent(context, GAME, "角色扮演-关注-角色扮演页面，点击任何一个游戏关注");
                break;
            case "2":
                onEvent(context, GAME, "竞技Moba-关注-竞技Moba页面，点击任何一个游戏关注");
                break;
            case "3":
                onEvent(context, GAME, "动作街机-关注-动作街机页面，点击任何一个游戏关注");
                break;
            case "4":
                onEvent(context, GAME, "休闲益智-关注-休闲益智页面，点击任何一个游戏关注");
                break;
            case "5":
                onEvent(context, GAME, "策略塔防-关注-策略塔防页面，点击任何一个游戏关注");
                break;
            case "6":
                onEvent(context, GAME, "赛车跑酷-关注-赛车跑酷页面，点击任何一个游戏关注");
                break;
            case "7":
                onEvent(context, GAME, "枪战射击-关注-枪战射击页面，点击任何一个游戏关注");
                break;
            case "8":
                onEvent(context, GAME, "音乐游戏-关注-音乐游戏页面，点击任何一个游戏关注");
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

    /**
     * 需要将游戏圈的埋点独立出来的游戏
     * @param gameName
     *                  游戏名
     * @return
     */
    public static boolean isSingleEvent(String gameName){
        boolean isSingle = false;
        if (!StringUtil.isNull(gameName)){
            List<String> gameList = Arrays.asList(
                    "王者荣耀",
                    "我的世界",
                    "穿越火线",
                    "球球大作战",
                    "龙之谷",
                    "皇室战争",
                    "火影忍者",
                    "炉石传说",
                    "阴阳师",
                    "海岛奇兵"
            );
            for (String name:
                    gameList) {
                if (name.equals(gameName)){
                    isSingle = true;
                }
            }
        }
        return isSingle;
    }


    /**
     *
     * @param context
     * @param id
     * @param map
     * @param duration
     */
    public static void onEventValue(Context context,String id,Map<String,String> map,int duration){
        if (map == null){
            map = new HashMap<>();
        }

        MobclickAgent.onEventValue(context, id , map, duration);
    }
}
