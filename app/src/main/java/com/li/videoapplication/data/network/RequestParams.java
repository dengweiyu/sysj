package com.li.videoapplication.data.network;

import java.util.Map;
import java.util.HashMap;

import com.li.videoapplication.framework.AppAccount;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.utils.StringUtil;

/**
 * 功能：网络请求参数
 */
public class RequestParams {
    private static final int A_SYSJ = 2;
    private static final String SYSJ = "a_sysj";


    public RequestParams() {
        super();
    }

    private static Object syncObject = new Object();

    private static RequestParams instance;

    public static RequestParams getInstance() {
        if (instance == null) {
            synchronized (syncObject) {
                if (instance == null) {
                    instance = new RequestParams();
                }
            }
        }
        return instance;
    }


    public Map<String, Object> homeInfo(int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        return map;
    }

    public Map<String, Object> homeInfoById(String columnId,int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        map.put("column_id",columnId);
        map.put("target",SYSJ);
        return map;
    }



    //分享成功后触发
    public Map<String, Object> shareTriggerReward(String memberId,String hook,String taskId,String flag) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", memberId);
        map.put("hook", hook);
        map.put("task_id", taskId);
        map.put("target", SYSJ);
        map.put("flag", flag);
        return map;
    }


    //分享页面信息
    public Map<String, Object> sharePlayerSquare(String memberId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", memberId);
        return map;
    }

    /**
     * ############## 飞磨游戏 ##############
     */
    // appid=100&from=3&clientid=125&agent=
    public Map<String, Object> gameDetail(String gameid) {
        Map<String, Object> map = new HashMap<>();
        map.put("gameid", gameid);
        map.put("appid", 100);
        map.put("from", 3);
        map.put("clientid", 125);
        map.put("agent", "");
        return map;
    }

    /**
     * ############## 货币商城 ##############
     */
    public Map<String, Object> payment(String member_id, String currency_num, int pay_type, int ingress) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("currency_num", currency_num);
        map.put("pay_type", pay_type);
        map.put("ingress", ingress);
        map.put("target", SYSJ);
        return map;
    }

    public Map<String, Object> paymentCoin(String member_id, String coin_num, int pay_type, int ingress) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("coin_num", coin_num);
        map.put("pay_type", pay_type);
        map.put("ingress", ingress);
        map.put("target", SYSJ);
        return map;
    }

    public Map<String, Object> payment(String member_id, String goods_id, String mobile, String account) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("goods_id", goods_id);
        map.put("mobile", mobile);
        map.put("account", account);
        map.put("target", SYSJ);
        return map;
    }

    public Map<String, Object> payment(String member_id, int level,int packageKey,int pay_type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("level", level);
        map.put("pay_type", pay_type);
        map.put("package_key",packageKey);
        map.put("target", SYSJ);
        return map;
    }

    public Map<String, Object> orderDetail(String member_id, String order_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("order_id", order_id);
        return map;
    }

    public Map<String, Object> getMemberAwardDetail(String member_id, String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("id", id);
        return map;
    }

    public Map<String, Object> goodsDetail(String goods_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("goods_id", goods_id);
        return map;
    }

    public Map<String, Object> getOrderList(String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> getBillList(String member_id,int type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("type", type);
        return map;
    }

    public Map<String, Object> getCurrencyRecord(String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        return map;
    }

    /**
     * ############## 标签 ##############
     */

    public Map<String, Object> gameTagList(String game_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("game_id", game_id);// 89
        return map;
    }

	/* ############## 消息 ############## */

    public Map<String, Object> msgList(int type_id, String member_id, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("type_id", type_id);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> messageMyMessage(String member_id, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("page", page);
        return map;
    }


    public Map<String, Object> messageSysMessage(String member_id, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> messageGroupMessage(String member_id, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("page", page);
        return map;
    }


    public Map<String, Object> messageMsgRed(String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        return map;
    }


	/* ############## 版本升级 ############## */

    public Map<String, Object> updateVersion(int build, String target) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("build", build);
        map.put("target", target);//i_lpds,a_sysj,i_sysj,a_sysj_wm,a_sysj_LD
        return map;
    }



    public Map<String, Object> memberAttention201(String member_id, String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("id", id);// 登录用户id
        return map;
    }


    public Map<String, Object> UserProfilePersonalInformation(String user_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user_id", user_id);
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> UserProfileUploadAvatar(String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        return map;
    }

    public Map<String, Object> UserProfileUploadCover(String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        return map;
    }

    public Map<String, Object> UserProfileFinishMemberInfo(String id,
                                                           String nickname,
                                                           String signature,
                                                           int display,
                                                           String like_grouptype,
                                                           int sex,
                                                           String qq,
                                                           String mobile) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        if (!StringUtil.isNull(nickname)) {
            map.put("nickname", nickname);
        }
        if (display == 0 || display == 1) {// 秘密资料是否公开,默认为1,即公开
            map.put("display", display);
        }
        if (!StringUtil.isNull(signature)) {// 喜欢的游戏类型,传id过来,可选
            map.put("signature", signature);
        }
        if (!StringUtil.isNull(like_grouptype)) {
            map.put("like_grouptype", like_grouptype);// like_grouptype,like_gametype
        }
        if (sex == 0 || sex == 1) {
            map.put("sex", sex);
        }
        if (!StringUtil.isNull(qq)) {
            map.put("qq", qq);
        }
        if (!StringUtil.isNull(mobile)) {
            map.put("mobile", mobile);
        }
        return map;
    }

    public Map<String, Object> myGroupList(String member_id, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> authorVideoGroup(String member_id, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> userProfileTimelineLists(String member_id, String user_id, int currentpage, int pagelength) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("user_id", user_id);
        map.put("currentpage", currentpage);
        map.put("pagelength", pagelength);
        return map;
    }

    public Map<String, Object> memberDynamicList(String member_id, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> dynamicDot(String member_id, long currentTime) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("time", currentTime);
        return map;
    }

    public Map<String, Object> authorVideoList2(String member_id, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> authorVideoList208(String member_id, String game_id, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("game_id", game_id);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> videoDisplayVideoQnKey(String qn_key) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("qn_key", qn_key);
        return map;
    }

    public Map<String, Object> videoDisplayVideoUrl(String url) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("url", url);
        return map;
    }

	/* ############## 广场 ############## */

    public Map<String, Object> squareList(String member_id, String sort, int page,String gameId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("sort", sort);// 最新：time 最热：click
        map.put("page", page);
        map.put("target",SYSJ);
        map.put("game_id",gameId);
        return map;
    }

    /* ############## 玩家广场游戏列表 ############## */
    public Map<String, Object> squareGameList() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("target", SYSJ);
        return map;
    }


    /* ############## 玩家广场页面统计 ############## */
    public Map<String, Object> squareGameListStatistical(String gameId,String sort) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("target", SYSJ);
        map.put("game_id", gameId);
        map.put("sort", sort);
        return map;
    }


	/* ############## 收藏 ############## */


    public Map<String, Object> getMatchInfo(String match_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("match_id", match_id);
        return map;
    }

    public Map<String, Object> getDetailMode(String match_id, String member_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("match_id", match_id);
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> selectMatch(String game_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("game_id", game_id);
        return map;
    }

    public Map<String, Object> getCompVideoLists208(String member_id, String match_id, int page) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", member_id);
        map.put("match_id", match_id);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> memberCollectVideoList(String member_id, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("page", page);
        return map;
    }


    public Map<String, Object> memberCancelCollect(String member_id, String video_ids, String pic_ids) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("video_ids", video_ids);
        map.put("pic_ids", pic_ids);
        return map;
    }

	/* ############## 游戏圈子 ############## */


    public Map<String, Object> groupList2(int page, String group_type_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("group_type_id", group_type_id);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> gameList(int page, String member_id, String sort) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("page", page);
        map.put("sort", sort);// sort(hot,time)
        return map;
    }


    public Map<String, Object> groupInfo(String group_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("group_id", group_id);
        return map;
    }


    public Map<String, Object> groupDataList(String group_id, String member_id, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("group_id", group_id);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> groupHotDataList(String group_id, String member_id, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("group_id", group_id);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> groupGamerList(String group_id, String member_id, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("group_id", group_id);
        map.put("page", page);
        return map;
    }


    public Map<String, Object> groupAttentionGroup(String group_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("group_id", group_id);
        return map;
    }


	/* ############## 视频 ############## */


    public Map<String, Object> videoDetail201(String video_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("video_id", video_id);
        map.put("member_id", member_id);
        map.put("target",SYSJ);
        return map;
    }


    public Map<String, Object> videoCommentList(String video_id, String member_id, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("video_id", video_id);
        map.put("member_id", member_id);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> videoDoComment(String video_id, String member_id, String content, String mark, String comment_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("video_id", video_id);
        map.put("member_id", member_id);
        map.put("content", content);
        map.put("mark", mark); //
        map.put("comment_id", comment_id); // mark=0/1判断是否为多级评论，当mark=1时comment_id不能为空
        return map;
    }

    public Map<String, Object> sendComment208(String match_id, String member_id, String content) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("match_id", match_id);
        map.put("member_id", member_id);
        map.put("content", content);
        map.put("target", A_SYSJ);
        return map;
    }

    public Map<String, Object> commentDel(String member_id, String comment_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("comment_id", comment_id);
        return map;
    }

    public Map<String, Object> videoCollect2(String video_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("video_id", video_id);
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> videoFlower2(String video_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("video_id", video_id);
        map.put("member_id", member_id);// (可空)
        return map;
    }

    public Map<String, Object> fndownClick203(String video_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("video_id", video_id);
        map.put("member_id", member_id);// (可空)
        return map;
    }

    public Map<String, Object> videoCommentLike2(String comment_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("comment_id", comment_id);
        map.put("member_id", member_id);// (可空)
        return map;
    }

	/* ############## ############## */

    public Map<String, Object> list(String page, String type_id, String sort) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        map.put("sort", sort);
        map.put("type_id", type_id);
        return map;
    }


    public Map<String, Object> like(String id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("member_id", member_id);
        return map;
    }


    public Map<String, Object> keyWordListNew(int num) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("num", num);// num = 9
        return map;
    }

    public Map<String, Object> associate(String keyWord) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("keyWord", keyWord);
        return map;
    }

    public Map<String, Object> associate201(String classType, String keyWord) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("classType", classType);
        map.put("keyWord", keyWord);
        return map;
    }

    public Map<String, Object> searchVideo(String name, String sort, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("sort", sort);// flower/time
        map.put("page", page);
        return map;
    }


    public Map<String, Object> searchVideo203(String keyword, String member_id, int page) {
        Map<String, Object> map = new HashMap<>();
        map.put("keyword", keyword);
        map.put("member_id", member_id);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> searchGame203(String keyword, String member_id, int page) {
        Map<String, Object> map = new HashMap<>();
        map.put("keyword", keyword);
        map.put("member_id", member_id);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> searchMember203(String keyword, String member_id, int page) {
        Map<String, Object> map = new HashMap<>();
        map.put("keyword", keyword);
        map.put("member_id", member_id);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> searchPackage203(String keyword, String member_id, int page) {
        Map<String, Object> map = new HashMap<>();
        map.put("keyword", keyword);
        map.put("member_id", member_id);
        map.put("page", page);
        return map;
    }

	/* ############## 注册登录 ############## */

    public Map<String, Object> eventRequestMsg(String key, String target, String title) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", key);
        map.put("target", target);// target = "sysj"
        map.put("event", title);
        return map;
    }

    public Map<String, Object> msgRequestNew(String key, String target) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", key);
        map.put("target", target);// target = "sysj"
        return map;
    }

    public Map<String, Object> verifyCode(String mobile, String code, String target) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("mobile", mobile);
        map.put("code", code);
        map.put("target", target);// target = "sysj"
        return map;
    }

    public Map<String, Object> verifyCodeNew(String key, String code) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", key);
        map.put("code", code);
        return map;
    }

    public Map<String, Object> setAlert(String member_id, String schedule_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("schedule_id", schedule_id);
        return map;
    }

    public Map<String, Object> checkAndroidStatus(int version, String channel) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("target", AppConstant.SYSJ_ANDROID);
        map.put("version", version);
        map.put("channel", channel);
        return map;
    }


    public Map<String, Object> login(String key) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", key);

        return map;
    }

    public Map<String, Object> sign(String member_id, String member_nickname, String sign_type, String sign_entrance,
                                    String sign_ip, String sign_hardwarecode, String imei, String time) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("member_nickname", member_nickname);
        map.put("sign_type", sign_type);
        map.put("sign_entrance", sign_entrance);
        map.put("sign_ip", sign_ip);
        map.put("sign_hardwarecode", sign_hardwarecode);
        map.put("imei", imei);
        map.put("time", time);
        return map;
    }

    public Map<String, Object> userdatabehavior(String member_id, String video_id, String pic_id,
                                                String video_comment_id, String entrance, String ip,
                                                String hardwarecode, String imei, String time) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("video_id", video_id);
        map.put("pic_id", pic_id);
        map.put("video_comment_id", video_comment_id);
        map.put("entrance", entrance);
        map.put("ip", ip);
        map.put("hardwarecode", hardwarecode);
        map.put("imei", imei);
        map.put("time", time);
        return map;
    }

    public Map<String, Object> baseInfo(String word) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("word", word);
        return map;
    }

    public Map<String, Object> isRepeat(String nickname) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("nickname", nickname);
        return map;
    }

    public Map<String, Object> login(String key, String isOpenId, String nickname, String name, String sex, String location, String avatar, String regOrigin) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", key);
        map.put("isOpenId", isOpenId);// "1"
        map.put("nickname", nickname);
        map.put("name", name);
        map.put("sex", sex);
        map.put("location", location);
        map.put("avatar", avatar);
        map.put("regOrigin", regOrigin);// "a_sysj"
        return map;
    }

    public Map<String,Object> addLoginParams(Map<String, Object> map,int version,String time,String sign,String clientId,String clientSecret,String grantType){
        map.put("target", SYSJ);
        map.put("timestamp", time);
        map.put("sign", sign);
        map.put("client_id", clientId);
        map.put("client_secret", clientSecret);
        map.put("grant_type", grantType);
        map.put("version",version);
        return map;
    }


	/* ############## 礼包 ############## */

    public Map<String, Object> claimPackage(String member_id, String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("id", id);
        return map;
    }


    public Map<String, Object> gamePackage203(String game_id, String member_id, int page) {
        Map<String, Object> map = new HashMap<>();
        map.put("game_id", game_id);
        map.put("member_id", member_id);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> packageList203(String member_id, int page) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", member_id);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> packageInfo203(String member_id, String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("member_id", member_id);
        return map;
    }


	/* ############## 任务 ############## */


    public Map<String, Object> taskList203(String member_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", member_id);
        return map;
    }



    /* ############## 赛事 ############## */
    public Map<String, Object> signSchedule214(String member_id, String schedule_id, String event_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("schedule_id", schedule_id);
        map.put("event_id", event_id);
        map.put("source", A_SYSJ);//2=>a_sysj,3=>i_sysj
        return map;
    }

    public Map<String, Object> signSchedule210(String member_id, String schedule_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("schedule_id", schedule_id);
        return map;
    }

    public Map<String, Object> joinEvents(String member_id, String event_id, String type_id,
                                          String team_name, String game_role, String qq,
                                          String phone, String invite_code, String team_member_tel) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("event_id", event_id);
        map.put("target", SYSJ);
        map.put("type_id", type_id);
        map.put("team_name", team_name);
        map.put("game_role", game_role);
        map.put("qq", qq);
        map.put("phone", phone);
        map.put("invite_code", invite_code);
        map.put("team_member_tel", team_member_tel);
        map.put("ip", NetUtil.getLocalIpAddress());
        return map;
    }

    public Map<String, Object> saveEventVideo(String team_id, String pk_id, String video_id,
                                              String event_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("team_id", team_id);
        map.put("pk_id", pk_id);
        map.put("video_id", video_id);
        map.put("event_id", event_id);
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> eventsSchedule204(String event_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("event_id", event_id);
        return map;
    }

    public Map<String, Object> getMemberMatchPK204(String member_id, String event_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("event_id", event_id);
        return map;
    }

    public Map<String, Object> getMemberEndPKWindow(String member_id, String event_id, String schedule_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("event_id", event_id);
        map.put("schedule_id", schedule_id);
        return map;
    }

    public Map<String, Object> getMemberPKList204(String member_id, String event_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("event_id", event_id);
        return map;
    }

    public Map<String, Object> eventsSetAlert210(String schedule_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("schedule_id", schedule_id);
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> eventsPKList(String schedule_id, int page, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("schedule_id", schedule_id);
        map.put("page", page);
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> getHistoricalRecord(String memberId, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("memberId", memberId);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> eventsRecordClick(String event_id, int e_record_status) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("event_id", event_id);
        map.put("e_record_status", e_record_status);
        map.put("source", A_SYSJ);
        return map;
    }


    public Map<String, Object> getMatchList201(int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        return map;
    }

    public Map<String, Object> getServiceName(String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> groupJoin(String member_id, String chatroom_group_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("chatroom_group_id", chatroom_group_id);
        return map;
    }

    public Map<String, Object> groupName208(String chatroom_group_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("chatroom_group_id", chatroom_group_id);
        return map;
    }

    public Map<String, Object> getRongCloudToken204(String member_id, String nickname) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("nickname", nickname);
        return map;
    }


    public Map<String, Object> myMatchList(String member_id, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> competitionRecordClick(String competition_id, int c_record_status) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("competition_id", competition_id);
        map.put("c_record_status", c_record_status);
        map.put("source", A_SYSJ);
        return map;
    }


    public Map<String, Object> statisticalOpenApp(String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("target", A_SYSJ);//2=>andor_sysj,3=>ios_sysj
        return map;
    }


    public Map<String, Object> indexIndexMore(String more_mark, String member_id, String sort, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("more_mark", more_mark);
        map.put("member_id", member_id);
        map.put("sort", sort);// time/click
        map.put("page", page);
        return map;
    }

    public Map<String, Object> indexIndexMore204(String more_mark, String member_id, String sort, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("more_mark", more_mark);
        map.put("member_id", member_id);
        map.put("sort", sort);// time/click
        map.put("page", page);
        return map;
    }



    public Map<String, Object> indexChangeGuess(String group_ids) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("group_ids", group_ids);
        return map;
    }

    public Map<String, Object> indexChangeGuessSecond(String video_ids) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("video_ids", video_ids);
        return map;
    }

	/* ############## ############## */

    public Map<String, Object> advertisementAdLocation204(String target) {
        Map<String, Object> map = new HashMap<>();
        map.put("target", target);// lpds，sysj，sysj_pc，sysj_m
        return map;
    }

    public Map<String, Object> advertisementAdImage204(long location_id, int time) {
        Map<String, Object> map = new HashMap<>();
        map.put("location_id", location_id);
        map.put("time", time);
        return map;
    }

    public Map<String, Object> adverImage(int location_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("location_id", location_id);
        map.put("platform", "android");
        return map;
    }

    public Map<String, Object> adImage208(int location_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("location_id", location_id);
        map.put("platform", "android");
        return map;
    }
	/* ############## 图文 ############## */

    public Map<String, Object> photoPhotoDetail(String pic_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pic_id", pic_id);
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> photoPhotoCommentList(String pic_id, int page_count, int current_page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pic_id", pic_id);
        map.put("page_count", page_count);
        map.put("current_page", current_page);
        return map;
    }


    public Map<String, Object> photoSendComment(String pic_id, String member_id, String content) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pic_id", pic_id);
        map.put("member_id", member_id);
        map.put("content", content);
        return map;
    }

    public Map<String, Object> photoFlower(String pic_id, String member_id, int isflower) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pic_id", pic_id);
        map.put("member_id", member_id);
        map.put("isflower", isflower);// 是否已经献花,0表示未献花,1表示已经献花
        return map;
    }

    public Map<String, Object> photoCollection(String pic_id, String member_id, int iscoll) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pic_id", pic_id);
        map.put("member_id", member_id);
        map.put("iscoll", iscoll);// 是否已经收藏,0表示未收藏,1表示已经收藏
        return map;
    }



    public Map<String, Object> report(String video_id, String member_id, String description, String type_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("video_id", video_id);
        map.put("member_id", member_id);
        map.put("description", description);
        map.put("type_id", type_id);
        return map;
    }


    public Map<String, Object> doVideoMark(String member_id,
                                           String video_title,

                                           String game_id,
                                           String width,
                                           String height,
                                           String time_length,
                                           String channel,

                                           String match_id,
                                           String description,
                                           int isofficial,
                                           String game_tags) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", member_id);
        map.put("video_title", video_title);
        map.put("game_id", game_id);
        map.put("width", width);
        map.put("height", height);
        map.put("time_length", time_length);
        map.put("channel", channel);// (通道，默认1)
        map.put("description", description);
        map.put("match_id", match_id);
        map.put("isofficial", isofficial);
        map.put("game_tags", game_tags);
        map.put("target", SYSJ);
        return map;
    }




    public Map<String, Object> qiniuTokenPass(String video_id,
                                              String game_id,
                                              String is_success,
                                              String member_id,
                                              String join_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("video_id", video_id);
        map.put("game_id", game_id);
        map.put("is_success", is_success);
        map.put("member_id", member_id);
        map.put("join_id", join_id);// （活动用，可空）
        return map;
    }



    public Map<String, Object> retPhotoKeyAndToken204(String member_id, String length) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("length", length);
        return map;
    }

    public Map<String, Object> photoRetPhotoNameAndToken(String member_id,
                                                         String title,
                                                         String length) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("title", title);
        map.put("length", length);
        return map;
    }

    public Map<String, Object> saveEventResult204(String pk_id,
                                                  String pic_keys,
                                                  String team_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pk_id", pk_id);
        map.put("pic_keys", pic_keys);
        map.put("team_id", team_id);
        map.put("target", A_SYSJ);//2=>an_sysj,3=>ios=>sysj
        return map;
    }

    public Map<String, Object> savePhoto208(String member_id,
                                            String match_id,
                                            String key,
                                            String title,
                                            String description) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("match_id", match_id);
        map.put("key", key);
        map.put("title", title);
        map.put("description", description);
        map.put("target", A_SYSJ);//3=>i_sysj，2=>a_sysj
        return map;
    }

    public Map<String, Object> photoSavePhoto(String member_id,
                                              String game_id,
                                              String key,
                                              String title,
                                              String description) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("game_id", game_id);
        map.put("key", key);
        map.put("title", title);
        map.put("description", description);
        return map;
    }

    public Map<String, Object> downloadClick217(String game_id, String target,
                                                String member_id, int location, String involve_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("game_id", game_id);
        map.put("target", target);// target分不同的平台传入（i_lpds,a_lpds,i_sysj,a_sysj,pc_sysj）
        map.put("member_id", member_id);
        map.put("location", location);
        map.put("involve_id", involve_id);
        return map;
    }

    public Map<String, Object> videoClickVideo201(String video_id, String member_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("video_id", video_id);
        map.put("member_id", member_id);
        map.put("target",SYSJ);
        return map;
    }

    public Map<String, Object> videoClickVideo221(String video_id, String member_id,int mark) {
        Map<String, Object> map = new HashMap<>();
        map.put("video_id", video_id);
        map.put("member_id", member_id);
        map.put("videoMark",mark);
        return map;
    }



    public Map<String, Object> fansList203(String member_id, String type, int page) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", member_id);
        map.put("type", type);// type=fans/attention
        map.put("page", page);
        return map;
    }

    public Map<String, Object> bulletList203(String video_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("video_id", video_id);
        return map;
    }

    public Map<String, Object> bulletDo203(String video_id,
                                           String video_node,
                                           String member_id,
                                           String content,
                                           String bullet,
                                           String mark,
                                           String comment_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("video_id", video_id);
        map.put("video_node", video_node);
        map.put("member_id", member_id);
        map.put("content", content);
        map.put("bullet", bullet);
        map.put("mark", mark);
        map.put("target",SYSJ);
        map.put("comment_id", comment_id);
        return map;
    }

    public Map<String, Object> editList203(String member_id, int page) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", member_id);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> srtUpload203(String video_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("video_id", video_id);
        return map;
    }

    public Map<String, Object> srtList203(String video_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("video_id", video_id);
        return map;
    }

    public Map<String, Object> parseMessage(String url) {
        Map<String, Object> map = new HashMap<>();
        map.put("urlInfo", url);
        return map;
    }

    public Map<String, Object> vipInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("target", SYSJ);
        return map;
    }

    public Map<String, Object> giftType() {
        Map<String, Object> map = new HashMap<>();
        map.put("target", SYSJ);
        return map;
    }


    public Map<String, Object> giftBill(String memberId,String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);
        map.put("user_id", userId);
        return map;
    }

    public Map<String, Object> getPlayGiftList(String memberId,String videoId) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);
        map.put("video_id", videoId);
        return map;
    }
    public Map<String, Object> getGiftTimeLineList(String videoId) {
        Map<String, Object> map = new HashMap<>();
        map.put("video_id", videoId);
        return map;
    }

    public Map<String, Object> playGift(String sign,String memberId,String videoId,String giftId,String videoNode,int number,long time) {
        Map<String, Object> map = new HashMap<>();
        map.put("sign", sign);
        map.put("member_id", memberId);
        map.put("video_id", videoId);
        map.put("gift_id", giftId);
        map.put("video_node", videoNode);
        map.put("target", SYSJ);
        map.put("num", number);
        map.put("timestamp", time);
        return map;
    }

    public Map<String, Object> sharedSuccess(String videoId) {
        Map<String, Object> map = new HashMap<>();
        map.put("video_id", videoId);
        return map;
    }

    public Map<String, Object> getCoachList(int page) {
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        return map;
    }

    public Map<String, Object> getCoachDetail(String memberId) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);
        return map;
    }


    public Map<String, Object>   getPlayWithOrderOptions(String coachId) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", coachId);
        return map;
    }

    public Map<String, Object> getPreviewOrderPrice(String memberId,int  rank,int mode,int gameCount) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);
        map.put("training_level",rank);
        map.put("game_mode",mode);
        map.put("version","233");
        map.put("inning",gameCount);
        return map;
    }

    public Map<String, Object> createPlayWithOrder(String memberId,String coachId,int server,int rank,int mode,String time,int count,int orderMode) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);
        map.put("coach_id",coachId);
        map.put("game_area",server);
        map.put("training_level", rank);
        map.put("game_mode",mode);
        map.put("start_time",time);
        map.put("inning",count);
        map.put("target",SYSJ);
        map.put("order_mode",orderMode);
        map.put("access_token", AppAccount.getAccessToken());
        return map;
    }

    public Map<String, Object> getPlayWithPlaceOrder(String memberId,int page) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);
        map.put("page",page);
        map.put("access_token", AppAccount.getAccessToken());
        return map;
    }

    public Map<String, Object>  getPlayWithTakeOrder(String memberId,int page) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);
        map.put("page",page);
        map.put("access_token", AppAccount.getAccessToken());
        return map;
    }


    public Map<String, Object>  getPlayWithOrderDetail(String memberId,String orderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("order_id", orderId);
        map.put("member_id", memberId);
        map.put("access_token", AppAccount.getAccessToken());
        return map;
    }

    public Map<String, Object>  refreshToken(String secret,String grantType,String refreshToken,String memberId) {
        Map<String, Object> map = new HashMap<>();
        map.put("client_id", "app_sysj");
        map.put("client_secret", secret);
        map.put("grant_type", grantType);
        map.put("refresh_token", refreshToken);
        map.put("member_id",memberId);
        return map;
    }

    public Map<String, Object>  confirmOrder(String memberId,String orderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);
        map.put("order_id", orderId);
        map.put("access_token", AppAccount.getAccessToken());
        return map;
    }

    public Map<String, Object>  commitComment(String memberId,String orderId,String content,float score,int tag) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);
        map.put("order_id", orderId);
        map.put("content", content);
        map.put("score", score);
        map.put("tag", tag);
        map.put("access_token", AppAccount.getAccessToken());
        return map;
    }

    public Map<String, Object>  commitOrderResult(String memberId, String orderId, String data,String picKey) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);
        map.put("order_id", orderId);
        map.put("data",data);
        map.put("access_token", AppAccount.getAccessToken());
        map.put("pic_keys",picKey);
        return map;
    }

    public Map<String, Object>  coachSign(String memberId, int status) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);
        map.put("status", status);
        map.put("access_token", AppAccount.getAccessToken());
        return map;
    }

    public Map<String, Object>  refundApply(String memberId,String orderId,String defaultReason,String inputReason) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);
        map.put("order_id",orderId);
        map.put("default_refund_reason", defaultReason);
        map.put("refund_reason",inputReason);
        map.put("access_token", AppAccount.getAccessToken());
        return map;
    }

    public Map<String, Object>  getMessageList(String memberId) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);
        map.put("version","v23");
        return map;
    }

    public Map<String, Object>  confirmOrderDone(String memberId,String orderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);
        map.put("order_id",orderId);
        map.put("access_token", AppAccount.getAccessToken());
        return map;
    }

    public Map<String, Object>  confirmTakeOrder(String memberId,String orderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);
        map.put("order_id",orderId);
        map.put("access_token", AppAccount.getAccessToken());
        return map;
    }


    public Map<String, Object>  readMessage(String memberId,String msgId,String symbol,String msgType,int isAll) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);
        map.put("msg_id",msgId);
        map.put("symbol", symbol);
        map.put("msg_type", msgType);
        map.put("all", isAll);
        return map;
    }

    public Map<String, Object>  coachConfirmRefund(String memberId,String orderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);
        map.put("order_id",orderId);
        map.put("access_token", AppAccount.getAccessToken());
        return map;
    }


    public Map<String, Object>  fetchPatch(String channelId,String version) {
        Map<String, Object> map = new HashMap<>();
        map.put("channel_id", channelId);
        map.put("app_version",version);
        return map;
    }

    public Map<String, Object>  getUserVipInfo(String memberId) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);
        return map;
    }

    public Map<String, Object>  getUploadKey(String memberId,int size) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);
        map.put("size",size);
        return map;
    }


    public Map<String, Object>  commitSign(String memberId,String sign,String key) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);
        map.put("individuality_signature", sign);
        map.put("pic_keys",key);
        return map;
    }

    public Map<String, Object>  getSign(String memberId) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);
        return map;
    }

    public Map<String, Object>  unBindBaiduPush(String memberId) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);
        return map;
    }

    public Map<String, Object>  rewardRank(String memberId,int page,String type) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);
        map.put("page", page);
        map.put("type", type);
        return map;
    }

    public Map<String, Object>  getCoachComment(String coachId,String mark,int page) {
        Map<String, Object> map = new HashMap<>();
        map.put("coach_id", coachId);
        map.put("page", page);
        map.put("mark", mark);
        return map;
    }

    public Map<String, Object>  commitStayDuration(String memberId,String videoId,int duration) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);
        map.put("video_id", videoId);
        map.put("play_time", duration);
        map.put("target",SYSJ);
        return map;
    }


    public Map<String, Object>  clearToken(String accessToken,String refreshToken) {
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", accessToken);
        map.put("refresh_token", refreshToken);
        return map;
    }


    public Map<String, Object>  grabPlayWithOrder(String memberId,String orderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);
        map.put("order_id",orderId);
        map.put("access_token", AppAccount.getAccessToken());

        return map;
    }

    public Map<String, Object>  downloadSuccess(String memberId,String gameId,String location,String involveId) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);
        map.put("game_id",gameId);
        map.put("location", location);
        map.put("involve_id", involveId);
        map.put("target",SYSJ);
        return map;
    }


    public Map<String, Object>  commitFocusGameList(String memberId,String gameIds) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);
        map.put("group_ids",gameIds);
        return map;
    }
}
