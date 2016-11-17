package com.li.videoapplication.data.network;

import java.util.Map;
import java.util.HashMap;

import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.utils.StringUtil;

/**
 * 功能：网络请求参数
 */
public class RequestParams {

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

    /**
     * ############## 货币商城 ##############
     */
    public Map<String, Object> payment(String member_id, String goods_id, String mobile, String account) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("goods_id", goods_id);
        map.put("mobile", mobile);
        map.put("account", account);
        return map;
    }

    public Map<String, Object> orderDetail(String member_id, String order_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("order_id", order_id);
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

    public Map<String, Object> allRead(String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("type", "video");
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

    public Map<String, Object> messageMsgGroupRed(String memberId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("memberId", memberId);
        return map;
    }

    public Map<String, Object> messageMsgRed(String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> messageClickMsg(String msg_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("msg_id", msg_id);
        return map;
    }

	/* ############## 版本升级 ############## */

    public Map<String, Object> updateVersion(int build, String target) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("build", build);
        map.put("target", target);//i_lpds,a_sysj,i_sysj,a_sysj_wm,a_sysj_LD
        return map;
    }

	/* ############## 用户资料 ############## */

    public Map<String, Object> detailNew(String id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> uploadAvatar(String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        return map;
    }

    public Map<String, Object> memberAttention(String member_id, String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("id", id);
        return map;
    }

    public Map<String, Object> memberAttention201(String member_id, String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("id", id);// 登录用户id
        return map;
    }

    public Map<String, Object> personalAttention(String member_id, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> personalFans(String member_id, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> cloudList(String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> authorVideoList(int page, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> memberReviewList(String member_id, int page, String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("page", page);
        map.put("id", id);
        return map;
    }

    public Map<String, Object> reviewLike(String review_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("review_id", review_id);
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> reviewLike2(String id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("member_id", member_id);
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

    public Map<String, Object> rankingMyRanking(String memberId, String order) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("memberId", memberId);
        map.put("order", order);
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

    public Map<String, Object> squareList(String member_id, String sort, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("sort", sort);// 最新：time 最热：click
        map.put("page", page);
        return map;
    }

	/* ############## ############## */

    public Map<String, Object> subscribe(String id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("id", id);
        return map;
    }

	/* ############## 收藏 ############## */

    public Map<String, Object> collectVideoList(String member_id, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> matchLike(String member_id, String match_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("match_id", match_id);
        return map;
    }

    public Map<String, Object> matchMark(String member_id, String match_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("match_id", match_id);
        return map;
    }

    public Map<String, Object> matchReview(String member_id, String match_id, String content) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("match_id", match_id);
        map.put("content", content);
        return map;
    }

    public Map<String, Object> selectMatch203(String game_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("game_id", game_id);
        return map;
    }

    public Map<String, Object> getMatchInfo208(String match_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("match_id", match_id);
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

    public Map<String, Object> memberCollectPicList(String member_id, int page) {
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

    public Map<String, Object> groupList(int page, String type_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("type_id", type_id);
        map.put("page", page);
        return map;
    }

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

    public Map<String, Object> groupDetail(String group_id, String member_id, String type_name) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("group_id", group_id);
        map.put("type_name", type_name);
        return map;
    }

    public Map<String, Object> groupInfo(String group_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("group_id", group_id);
        return map;
    }

    public Map<String, Object> groupVideoList(String group_id, String member_id, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("group_id", group_id);
        map.put("page", page);
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

    public Map<String, Object> groupAttention(String group_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("group_id", group_id);
        return map;
    }

    public Map<String, Object> groupAttentionGroup(String group_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("group_id", group_id);
        return map;
    }

    public Map<String, Object> groupMemberList(String group_id, String member_id, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("group_id", group_id);
        map.put("member_id", member_id);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> groupReviewList(String group_id, String member_id, String page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("group_id", group_id);
        map.put("member_id", member_id);
        map.put("page", page);
        return map;
    }

	/* ############## 视频 ############## */

    public Map<String, Object> videoFlower(String id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> videoCancelFlower(String id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> videoCollect(String id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("id", id);
        return map;
    }

    public Map<String, Object> videoCancelCollect(String id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("id", id);
        return map;
    }

    public Map<String, Object> videoDetail201(String video_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("video_id", video_id);
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> videoPlayNext(String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
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

    public Map<String, Object> editorRecommend(String page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        return map;
    }

    public Map<String, Object> topicList(String page, String mark) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        map.put("mark", mark);
        return map;
    }

    public Map<String, Object> topicList2(String page, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> list(String page, String type_id, String sort) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        map.put("sort", sort);
        map.put("type_id", type_id);
        return map;
    }

    public Map<String, Object> homeDaily(String page, String sort) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        map.put("sort", sort);
        return map;
    }

    public Map<String, Object> likeVideoList(String member_id, String page, String sort) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("page", page);
        map.put("sort", sort);
        return map;
    }

    public Map<String, Object> downLoad(String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        return map;
    }

    public Map<String, Object> like(String id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> commentLike(String comment_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("comment_id", comment_id);
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> keyWordList(String page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
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

    public Map<String, Object> searchTask(String name, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("className", name);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> searchMember(String name, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> searchVideo(String name, String sort, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("sort", sort);// flower/time
        map.put("page", page);
        return map;
    }

    public Map<String, Object> searchPackage(String name, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> groupSearch(String keyWord) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("keyWord", keyWord);
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

    public Map<String, Object> registerNew(String key, String password, String origin) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", key);
        map.put("password", password);
        map.put("origin", origin);
        return map;
    }

    public Map<String, Object> loginNew(String key, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", key);
        map.put("password", password);
        return map;
    }

    public Map<String, Object> eventRequestMsg(String key, String target, String title) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", key);
        map.put("target", target);// target = "sysj"
        map.put("event", title);
        return map;
    }

    public Map<String, Object> sendCode(String mobile, String target) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("mobile", mobile);
        map.put("target", target);// target = "sysj"
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

    public Map<String, Object> checkAndroidStatus(int version) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("target", AppConstant.SYSJ_ANDROID);
        map.put("version", version);
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

    public Map<String, Object> pwdModify(String uid, String oldpwd, String newpwd) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uid", uid);
        map.put("oldpwd", oldpwd);
        map.put("newpwd", newpwd);
        return map;
    }

    public Map<String, Object> pwdForget(String uid, String newpwd) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uid", uid);
        map.put("newpwd", newpwd);
        return map;
    }

    public Map<String, Object> findForEmail(String email) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("email", email);
        return map;
    }

	/* ############## 礼包 ############## */

    public Map<String, Object> claimPackage(String member_id, String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("id", id);
        return map;
    }

    public Map<String, Object> packageInfo(String member_id, String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> getMyPackageList(String member_id, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> packageList(String member_id, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        if (!StringUtil.isNull(member_id)) {
            map.put("member_id", member_id);
        }
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

    public Map<String, Object> myPackage203(String member_id, int page) {
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

    public Map<String, Object> taskListNew(String member_id, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> taskList115(int page, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> taskList201(int page, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> taskList203(String member_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> acceptTask(String task_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("task_id", task_id);
        return map;
    }

    public Map<String, Object> acceptTask201(String task_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("task_id", task_id);
        return map;
    }

    public Map<String, Object> doTask(String task_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("task_id", task_id);
        return map;
    }

    public Map<String, Object> doTask115(String task_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("task_id", task_id);
        return map;
    }

    public Map<String, Object> doTask201(String task_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("task_id", task_id);
        return map;
    }

    public Map<String, Object> doTask203(String task_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("task_id", task_id);
        return map;
    }

    public Map<String, Object> doTask117(String task_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("task_id", task_id);
        return map;
    }

    public Map<String, Object> finishTaskNew(String task_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("task_id", task_id);
        return map;
    }

    public Map<String, Object> getExp(String task_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("task_id", task_id);
        return map;
    }

    public Map<String, Object> taskUnDoNum(String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        return map;
    }

	/* ############## ############## */

    public Map<String, Object> taskUnDoNum(String page, String member_id, String flag) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        map.put("flag", flag);
        if (!StringUtil.isNull(member_id)) {
            map.put("member_id", member_id);
        }
        return map;
    }

    public Map<String, Object> getFocuseList(String page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        return map;
    }

    public Map<String, Object> activityDetail(String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        return map;
    }

	/* ############## 赛事 ############## */

    public Map<String, Object> signSchedule204(String member_id, String event_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("event_id", event_id);
        return map;
    }

    public Map<String, Object> signSchedule210(String member_id, String schedule_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("schedule_id", schedule_id);
        return map;
    }

    public Map<String, Object> getEventsInfo204(String event_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("event_id", event_id);
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> joinEvents204(String member_id, String event_id, String type_id,
                                             String team_name, String game_role, String qq, String phone) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("event_id", event_id);
        map.put("type_id", type_id);
        map.put("team_name", team_name);
        map.put("game_role", game_role);
        map.put("qq", qq);
        map.put("phone", phone);
        return map;
    }

    public Map<String, Object> joinEvents210(String member_id, String event_id, String type_id,
                                             String team_name, String game_role, String qq, String phone) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("event_id", event_id);
        map.put("target", "a_sysj");
        map.put("type_id", type_id);
        map.put("team_name", team_name);
        map.put("game_role", game_role);
        map.put("qq", qq);
        map.put("phone", phone);
        return map;
    }

    public Map<String, Object> joinEvents210(String member_id, String event_id, String type_id,
                                             String team_name, String game_role, String qq, String phone, String team_member_qq) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("event_id", event_id);
        map.put("target", "a_sysj");
        map.put("type_id", type_id);
        map.put("team_name", team_name);
        map.put("game_role", game_role);
        map.put("qq", qq);
        map.put("phone", phone);
        map.put("team_member_qq", team_member_qq);
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

    public Map<String, Object> eventsPKList204(String schedule_id, int page, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("schedule_id", schedule_id);
        map.put("page", page);
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> getGroupEventsList211(String game_id,int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("game_id", game_id);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> getEventsList204(int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        return map;
    }

    public Map<String, Object> getMatchList1_2(int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        return map;
    }

    public Map<String, Object> getMatchList3(String alone_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("alone_id", alone_id);
        return map;
    }

    public Map<String, Object> getMatchList201(int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        return map;
    }

    public Map<String, Object> getServiceName204(String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> groupJoin208(String member_id, String chatroom_group_id) {
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

    public Map<String, Object> matchInfo(String match_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("match_id", match_id);
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> matchVideoList(String match_id, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("match_id", match_id);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> myMatchList(String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> myMatchList201(String member_id, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> myEventsList204(String member_id, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> joinMatch(String match_id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("match_id", match_id);
        return map;
    }

    public Map<String, Object> matchVideoList201(String match_id, String member_id, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("match_id", match_id);
        map.put("page", page);
        return map;
    }

	/* ############## 榜单 ############## */

    public Map<String, Object> daRenListNew(String member_id, String flag, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("flag", flag);// video fan rank
        map.put("page", page);
        return map;
    }

    public Map<String, Object> myDaRenList(String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> memberRankingCurrency(String member_id, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> rankingMemberRanking(String member_id, String sort, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("sort", sort);// fans/rank/video
        map.put("page", page);
        return map;
    }

    public Map<String, Object> rankingVideoRanking(String member_id, String sort, int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("sort", sort);// like/comment/click
        map.put("page", page);
        return map;
    }

    public Map<String, Object> likeVideoList115(String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> specialTopic(String id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("id", id);
        return map;
    }

    public Map<String, Object> homeDaily(String page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        return map;
    }

	/* ############## 首页 ############## */

    public Map<String, Object> appIndexList(int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        return map;
    }

    public Map<String, Object> appIndexListAloneVersion(String alone_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("alone_id", alone_id);
        return map;
    }

    public Map<String, Object> appIndexMore(String more_mark, String sort, String page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("more_mark", more_mark);
        map.put("sort", sort);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> appIndexMoreAloneVersion(String type_id, String sort, String page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type_id", type_id);
        map.put("sort", sort);
        map.put("page", page);
        return map;
    }

    public Map<String, Object> relaxedMoment(int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        return map;
    }

    public Map<String, Object> recommendList(int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        return map;
    }

    public Map<String, Object> indexIndex(int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        return map;
    }

    public Map<String, Object> indexIndex204(int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
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

    public Map<String, Object> indexBanner(String alone_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("alone_id", alone_id);
        return map;
    }

    public Map<String, Object> indexVideoList(int page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        return map;
    }

    public Map<String, Object> indexDoSurvey(String member_id, String group_ids) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("group_ids", group_ids);
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

    public Map<String, Object> launchImage(String target, int time) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("target", target);
        map.put("time", time);
        return map;
    }

    public Map<String, Object> indexLaunchImage(String target, int time) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("target", target);
        map.put("time", time);
        return map;
    }

    public Map<String, Object> appBanner(String alone_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("alone_id", alone_id);
        return map;
    }

    public Map<String, Object> uplaodImage(String uplaodImage) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uplaodImage", uplaodImage);
        return map;
    }

    public Map<String, Object> detail(String id, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("id", id);
        return map;
    }

    public Map<String, Object> topicVideoList(String id, String page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        map.put("id", id);
        return map;
    }

    public Map<String, Object> playNext(String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        return map;
    }

    public Map<String, Object> commentList(String id, String page, String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("page", page);
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> getVidById(String member_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
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

    public Map<String, Object> photoCommentLike(String piccommentid, String member_id, boolean isLike) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("piccommentid", piccommentid);
        map.put("member_id", member_id);
        map.put("isLike", isLike);// 点赞状态,false表示点击,true表示取消点赞
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

    public Map<String, Object> photoAttention(String fan_id, String member_id, boolean isAttent) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("fan_id", fan_id);// 登录用户的id
        map.put("member_id", member_id);// 发表图文的用户id(不是登录用户!)
        map.put("isAttent", isAttent);// 登录用户是否已经关注,false为还没有关注,true表示已经关注
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

    public Map<String, Object> videoDoVideoMark(String member_id,
                                                String video_title,
                                                String game_id,
                                                String width,
                                                String height,
                                                String time_length,
                                                String channel,
                                                String match_id,
                                                String description) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("video_title", video_title);
        map.put("game_id", game_id);
        map.put("width", width);
        map.put("height", height);
        map.put("time_length", time_length);
        map.put("channel", channel);// (通道，默认1)
        map.put("match_id", match_id);// (活动用，可空)
        map.put("description", description);// (可空)
        return map;
    }

    public Map<String, Object> videoDoVideoMark203(String member_id,
                                                   String video_title,
                                                   String game_id,
                                                   String width,
                                                   String height,
                                                   String time_length,
                                                   String channel,

                                                   String match_id,
                                                   String description,
                                                   int isofficial) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("member_id", member_id);
        map.put("video_title", video_title);
        map.put("game_id", game_id);
        map.put("width", width);
        map.put("height", height);
        map.put("time_length", time_length);
        map.put("channel", channel);// (通道，默认1)
        map.put("match_id", match_id);// (活动用，可空)
        map.put("description", description);// (可空)
        map.put("isofficial", isofficial);// 0/1(可空)
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
        return map;
    }

    public Map<String, Object> videoQiniuTokenPass(String video_id,
                                                   String game_id,
                                                   String is_success,
                                                   String join_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("video_id", video_id);
        map.put("game_id", game_id);
        map.put("is_success", is_success);
        map.put("join_id", join_id);// （活动用，可空）
        return map;
    }

    public Map<String, Object> saveEventVideo204(String pk_id, String video_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pk_id", pk_id);
        map.put("video_id", video_id);
        return map;
    }

    public Map<String, Object> videoQiniuTokenPass203(String video_id,
                                                      String game_id,
                                                      String is_success,
                                                      String join_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("video_id", video_id);
        map.put("game_id", game_id);
        map.put("is_success", is_success);
        map.put("join_id", join_id);// （活动用，可空）
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

    public Map<String, Object> videoUploadPicQiniu(String video_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("video_id", video_id);
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

    public Map<String, Object> gameDownloadNum(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        return map;
    }

    public Map<String, Object> downloadClick203(String game_id, String target) {
        Map<String, Object> map = new HashMap<>();
        map.put("game_id", game_id);
        map.put("target", target);// target分不同的平台传入（i_lpds,a_lpds,i_sysj,a_sysj,pc_sysj）
        return map;
    }

    public Map<String, Object> videoClickVideo201(String video_id, String member_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("video_id", video_id);
        map.put("member_id", member_id);
        return map;
    }

    public Map<String, Object> videoDownLoad201(String video_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("video_id", video_id);
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
}
