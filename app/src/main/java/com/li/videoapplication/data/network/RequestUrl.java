package com.li.videoapplication.data.network;

import com.li.videoapplication.framework.AppConstant;

/**
 * 功能：网络请求地址
 */
public class RequestUrl {

    public RequestUrl() {
        super();
    }

    private static RequestUrl instance;

    public static RequestUrl getInstance() {
        if (instance == null) {
            synchronized (RequestUrl.class) {
                if (instance == null) {
                    instance = new RequestUrl();
                }
            }
        }
        return instance;
    }

	/* ############## 域名 ############## */

    public String BaseHome = String.format(AppConstant.FORMAT,AppConstant.IP,AppConstant.PORT,AppConstant.ROOT_DIR)+"/home";

    //http://apps.ifeimo.com
    //https://sapp.17sysj.com
    public String BaseSYSJ = String.format(AppConstant.FORMAT,AppConstant.IP,AppConstant.PORT,AppConstant.ROOT_DIR);

    public String SignSYSJ = "http://17sysj.com";

    public String BaseFMYX = "http://game.17sysj.com";

    public String BaseFMYX_V1 = BaseFMYX + "/api/public/index.php/v1";



    /**
     * 首页信息
     */
    public final String homeInfo() {
        return BaseSYSJ + "/Sysj217/Index/index";
    }

    /**
     * 分享页面广场信息
     */
    public final String sharePlayerSquare() {
        return BaseSYSJ + "/Sysj218/share/sharePlayerSquare";
    }


    /**
     * 分享成功后触发
     */
    public final String shareTriggerReward() {
        return BaseSYSJ + "/Sysj218/share/triggerReward";
    }


    /**
     * 官方推荐位
     */
    public final String getRecommendCate() {
        return BaseSYSJ + "/Sysj217/Recommend/recommendCate";
    }

    /* ############## 下载 ############## */

    /**
     * 更多精彩（内部）
     */
    public final String getDownloadApp() {
        return BaseSYSJ + "/sysj204/DownloadApp/getDownloadApp";
    }

    /**
     * 更多精彩（广告）
     */
    public final String getDownloadOther() {
        return BaseSYSJ + "/sysj204/DownloadApp/getDownloadOther";
    }

    /** ############## 飞磨游戏 ############## */

    /**
     * 游戏详情
     */
    public final String gameDetail() {
        return BaseFMYX_V1 + "/game/detail";
    }

    /** ############## 抽奖 ############## */
    /**
     * 抽奖状态获取接口
     */
    public final String getSweepstakeStatus() {
        return BaseSYSJ + "/Sysj215/Sweepstake/getSweepstake";
    }

    /**
     * 赛事奖金榜状态
     */
    public final String getRewardStatus() {
        return BaseSYSJ + "/sysj213/events/showRewardRankEntrance";
    }

    /**
     * 抽奖页
     */
    public final String getSweepstake() {
        return BaseSYSJ + "/Sysj215/Sweepstake";
    }

    /** ############## 标签 ############## */

    /**
     * 视频分享标签
     */
    public final String gameTagList() {
        return BaseSYSJ + "/Upload/video/gameTagList";
    }

	/* ############## 广告 ############## */

    /**
     * 启动图广告
     */
    public final String adverImage() {
        return "http://ad.17sysj.com" + "/Advertisement/adverSysjImage";
    }

    /**
     * 广告位置列表
     */
    public final String advertisementAdLocation204() {
        return BaseSYSJ + "/sysj204/advertisement/adLocation";
    }

    /**
     * 位置广告图集
     */
    public final String advertisementAdImage204() {
        return BaseSYSJ + "/sysj204/advertisement/adImage";
    }

    /**
     * 图片广告
     */
    public final String indexLaunchImage() {
        return BaseSYSJ + "/sysj201/index/launchImage";
    }

    /**
     * 首页广告（旧接口）
     */
    public final String homeHd() {
        return BaseHome + "/recommend/homeHd.html";
    }

    /**
     * 首页广告（旧接口）
     */
    public final String appBanner() {
        return BaseHome + "/AloneVersion/appBanner.html";
    }

    /**
     * 广告路径（旧接口）
     */
    public final String focuseList() {
        return BaseHome + "/focuse/data.html";
    }

    /**
     * 广告路径（玩家秀）（旧接口）
     */
    public final String playerShow() {
        return BaseHome + "/recommend/playerShow.html";
    }

    /**
     * 视频上传图片
     */
    public final String uplaodImage(String situation) {
        return BaseHome + "/video/uplaodImage.html";
    }

	/* ############## 礼包 ############## */

    /**
     * 礼包列表
     */
    public final String packageList() {
        return BaseHome + "/member/packageList.html";
    }

    /**
     * 礼包详情
     */
    public String packageInfo() {
        return BaseHome + "/member/PackageInfo.html";
    }

    /**
     * 领取礼包
     */
    public final String claimPackage() {
        return BaseHome + "/member/claimPackage.html";
    }

    /**
     * 圈子礼包203
     */
    public final String gamePackage203() {
        return BaseSYSJ + "/sysj203/Gifts/gamePackage";
    }

    /**
     * 礼包列表203
     */
    public final String packageList203() {
        return BaseSYSJ + "/sysj203/Gifts/packageList";
    }

    /**
     * 我的礼包203
     */
    public final String myPackage203() {
        return BaseSYSJ + "/sysj203/Gifts/myPackage";
    }

    /**
     * 礼包详情203
     */
    public final String packageInfo203() {
        return BaseSYSJ + "/sysj203/gifts/packageInfo";
    }

	/* ############## 收藏 ############## */

    /**
     * 视频收藏列表
     */
    public final String collectVideoList() {
        return BaseHome + "/member/collectVideoList.html";
    }

    /**
     * 收藏视频列表
     */
    public final String memberCollectVideoList() {
        return BaseSYSJ + "/sysj201/member/collectVideoList";
    }

    /**
     * 图文视频列表
     */
    public final String memberCollectPicList() {
        return BaseSYSJ + "/sysj201/member/collectPicList";
    }

	/* ############## ############## */

    /**
     * 上传视频信息
     */
    public final String postVideoInfo() {
        return BaseHome + "/video/publishVideo.html";
    }

    /**
     * 视频列表
     */
    public final String videoList() {
        return BaseHome + "/video/data.html";
    }

    /**
     * 收藏赛事
     */
    public final String matchLike() {
        return BaseHome + "/member/matchLike.html";
    }

    /**
     * 是否赛事收藏
     */
    public final String matchMark() {
        return BaseHome + "/member/matchMark.html";
    }

	/* ############## 评论 ############## */

    /**
     * 赛事评论
     */
    public final String matchReview() {
        return BaseHome + "/member/matchReview.html";
    }

    /**
     * 提交（二级）评论116
     */
    public final String doComment116() {
        return BaseHome + "/video/doComment116.html";
    }

    /**
     * 圈子评论
     */
    public final String groupReview() {
        return BaseHome + "/game/groupReview.html";
    }

    /**
     * 评论列表
     */
    public final String commentList() {
        return BaseHome + "/video/commentList.html";
    }

    /**
     * 视频评论删除接口
     */
    public final String commentDel() {
        return BaseSYSJ + "/Sysj211/Video/commentDel";
    }

    /**
     * 活动评论208
     */
    public final String sendComment208() {
        return BaseSYSJ + "/Sysj208/CompetitionComment/sendComment";
    }

	/* ############## 点赞 ############## */

    /**
     * 大神点赞接口
     */
    public final String like() {
        return BaseHome + "/member/like.html";
    }

    /**
     * 视频评论点赞
     */
    public String commentLike() {
        return BaseHome + "/video/commentLike.html";
    }

	/* ############## ICP 统计相关接口 ############## */

    /**
     * 用户注册、登录记录
     */
    public final String sign() {
        return SignSYSJ + "/Appweb/Signaction/sign";
    }

    /**
     * 用户内容及言论入口，IP等行为
     */
    public final String userdatabehavior() {
        return SignSYSJ + "/Appweb/Signaction/userdatabehavior";
    }

    /**
     * 个人资料过滤敏感词
     */
    public final String baseInfo() {
        return BaseSYSJ + "/Platform/Filter/baseInfo";
    }

    /**
     * 检测用户昵称重复接口
     */
    public final String isRepeat() {
        return BaseSYSJ + "/Sysj210/Member/isRepeat";
    }

	/* ############## 注册登录 ############## */

    /**
     * 注册
     */
    public final String registerNew() {
        return BaseHome + "/member/registerNew.html";
    }

    /**
     * 登录
     */
    public final String loginNew() {
        return BaseHome + "/member/loginNew.html";
    }

    /**
     * 获取验证码 登陆注册（216 加密）
     */
    public final String msgRequestCode() {
        return BaseSYSJ + "/Sysj211/Message/getMsgCode";
    }

    /**
     * 获取验证码211
     */
    public final String sendCode() {
        return BaseSYSJ + "/Sysj211/CurrencyMall/sendCode";
    }

    /**
     * 个人中心绑定手机号请求验证码
     */
    public final String phoneRequestMsg() {
        return BaseSYSJ + "/Sysj211/Message/bindPhone";
    }

    /**
     * 获取验证码 216 加密
     */
    public final String eventRequestMsg() {
        return BaseSYSJ + "/Sysj211/Message/getEventMsg";
    }

    /**
     * 提交手机和验证码
     * 参加赛事验证、个人中心验证
     */
    public final String verifyCodeNew() {
        return BaseSYSJ + "/Sysj211/message/verifyCode";
    }

    /**
     * 提交手机和验证码211
     */
    public final String verifyCode() {
        return BaseSYSJ + "/Sysj211/CurrencyMall/verifyCode";
    }

    /**
     * 手机登陆
     */
    public final String login() {
        return BaseHome + "/member/login.html";
    }

    /**
     * 飞磨登陆
     */
    public final String loginFm() {
        return BaseHome + "/member/loginFm.html";
    }

	/* ############## 忘记密码 ############## */

    /**
     * 直接修改密码
     */
    public final String pwdModify() {
        return BaseHome + "/member/pwdModify";
    }

    /**
     * 验证修改密码
     */
    public final String pwdForget() {
        return BaseHome + "/member/pwdForget";
    }

    /**
     * 邮箱找回密码
     */
    public final String findForEmail() {
        return BaseHome + "/member/findForEmail";
    }

	/* ############## 用户资料 ############## */

    /**
     * 用户资料（旧接口）
     */
    public final String detailNew() {
        return BaseHome + "/member/detailNew.html";
    }

    /**
     * 上传头像 300K以内 png,gif,jpg
     */
    public final String uploadAvatar() {
        return BaseHome + "/member/uploadAvatar.html";
    }

    /**
     * 修改用户资料
     */
    public final String finishMemberInfo() {
        return BaseHome + "/member/finishMemberInfo.html";
    }

    /**
     * 关注玩家
     */
    public final String memberAttention() {
        return BaseHome + "/member/attention.html";
    }

    /**
     * 关注玩家201
     */
    public final String memberAttention201() {
        return BaseSYSJ + "/sysj201/member/attention";
    }

    /**
     * 关注列表
     */
    public final String personalAttention() {
        return BaseHome + "/member/personalAttention.html";
    }

    /**
     * 粉丝列表
     */
    public final String personalFans() {
        return BaseHome + "/member/personalFans.html";
    }

    /**
     * 用户视频列表
     */
    public final String authorVideoList() {
        return BaseHome + "/Video/authorVideoList.html";
    }

    /**
     * 用户视频列表
     */
    public final String cloudList() {
        return BaseSYSJ + "/sysj208/video/cloudList";
    }

    /**
     * 个人空间评论
     */
    public final String memberReviewList() {
        return BaseHome + "/member/memberReviewList.html";
    }

    /**
     * 资料信息发表评论
     */
    public final String memberReview() {
        return BaseHome + "/member/memberReview.html";
    }

    /**
     * 评论内容的点赞
     */
    public String reviewLike() {
        return BaseHome + "/game/reviewLike.html";
    }

    /**
     * 个人留言评论点赞
     */
    public final String reviewLike2() {
        return BaseHome + "/member/reviewLike.html";
    }

    /**
     * 个人资料
     */
    public final String userProfilePersonalInformation() {
        return BaseSYSJ + "/Sysj201/UserProfile/personalInformation";
    }

    /**
     * 我的喜欢的游戏类型（已废弃）
     */
    public final String userProfileTypeList() {
        return BaseSYSJ + "/Sysj201/UserProfile/typeList";
    }

    /**
     * 编辑个人资料
     */
    public final String userProfileFinishMemberInfo() {
        return BaseSYSJ + "/Sysj201/UserProfile/finishMemberInfo";
    }

    /**
     * 上传头像
     */
    public final String userProfileUploadAvatar() {
        return BaseSYSJ + "/Sysj201/UserProfile/uploadAvatar";
    }

    /**
     * 上传封面
     */
    public final String userProfileUploadCover() {
        return BaseSYSJ + "/Sysj201/UserProfile/uploadCover";
    }

    /**
     * 个人视频（时间轴）
     */
    public final String userProfileTimelineLists() {
        return BaseSYSJ + "/Sysj201/UserProfile/timelineLists";
    }

    /**
     * 他的视频-分组208
     */
    public final String authorVideoGroup() {
        return BaseSYSJ + "/Sysj208/Video/authorVideoGroup";
    }

    /**
     * 个人视频（动态）
     */
    public final String memberDynamicList() {
        return BaseSYSJ + "/sysj201/member/dynamicList";
    }

    /**
     * 动态是否有更新
     */
    public final String dynamicDot() {
        return BaseSYSJ + "/Sysj210/Member/dynamicDot";
    }

    /**
     * 玩家广场是否有更新
     */
    public final String squareDot() {
        return BaseSYSJ + "/sysj218/PlayerSquare/getPlayerDynamicDot";
    }

    /**
     * 我的游戏
     */
    public final String myGroupList() {
        return BaseSYSJ + "/sysj201/group/myGroupList";
    }

    /**
     * 用户视频列表2
     */
    public final String authorVideoList2() {
        return BaseSYSJ + "/sysj201/video/authorVideoList";
    }

    /**
     * 用户视频列表211
     */
    public final String videoCloudList() {
        return BaseSYSJ + "/sysj208/video/cloudList";
    }

    /**
     * 用户视频列表208
     */
    public final String authorVideoList208() {
        return BaseSYSJ + "/Sysj208/Video/authorVideoList";
    }

    /**
     * 用户玩家榜排名
     */
    public final String rankingMyRanking() {
        return BaseSYSJ + "/sysj201/ranking/myRanking";
    }

    /**
     * 隐藏云端视频
     */
    public final String videoDisplayVideo() {
        return BaseHome + "/video/displayVideo.html";
    }

	/* ############## 消息 ############## */

    /**
     * 消息列表
     */
    public String msgList() {
        return BaseHome + "/member/msgList.html";
    }



    /**
     * 清除视频图文消息
     */
    public final String allRead() {
        return BaseSYSJ + "/sysj211/message/allRead";
    }



	/* ############## 圈子/游戏 ############## */

    /**
     * 圈子消息
     */
    public final String messageGroupMessage() {
        return BaseSYSJ + "/sysj201/message/groupMessage";
    }

    /**
     * 圈子消息总数
     */
    public final String messageMsgRed() {
        return BaseSYSJ + "/sysj201/message/msgRed";
    }

    /**
     * 消息提示红点
     */
    public final String messageMsgGroupRed() {
        return BaseSYSJ + "/sysj201/message/msgGroupRed";
    }


    /**
     * 视频，图文消息已读
     */
    public final String messageClickMsg() {
        return BaseSYSJ + "/sysj201/message/clickMsg";
    }

	/* ############## 圈子/游戏 ############## */

    /**
     * 热门游戏列表（旧接口）
     */
    public final String hotGame() {
        return BaseHome + "/game/hotGame.html";
    }

    /**
     * 最热，最新游戏列表
     */
    public final String gameList() {
        return BaseSYSJ + "/sysj201/game/gameList.html";
    }

    /**
     * 圈子类型（旧接口）
     */
    public final String groupType() {
        return BaseHome + "/game/groupType.html";
    }

    /**
     * 圈子类型
     */
    public final String groupType2() {
        return BaseSYSJ + "/sysj201/group/groupType";
    }

    /**
     * 圈子类型
     */
    public final String groupType210() {
        return BaseSYSJ + "/sysj210/group/groupType";
    }

    /**
     * 圈子类型217
     */
    public final String gameCate() {
        return BaseSYSJ + "/sysj210/group/GameCate";
    }

    /**
     * 圈子列表（旧接口）
     */
    public final String groupList() {
        return BaseSYSJ + "/sysj201/game/groupList.html";
    }

    /**
     * 圈子列表
     */
    public final String groupList2() {
        return BaseSYSJ + "/sysj201/group/groupList";
    }

    /**
     * 圈子详情（旧接口）
     */
    public final String groupDetail() {
        return BaseHome + "/game/groupDetail.html";
    }

    /**
     * 圈子详情
     */
    public final String groupInfo() {
        return BaseSYSJ + "/sysj201/group/groupInfo";
    }

    /**
     * 圈子视频列表（旧接口）
     */
    public final String groupVideoList() {
        return BaseHome + "/game/groupVideoList.html";
    }

    /**
     * 圈子视频列表（最新）
     */
    public final String groupDataList() {
        return BaseSYSJ + "/sysj201/group/groupDataList";
    }

    /**
     * 圈子视频列表（最热）
     */
    public final String groupHotDataList() {
        return BaseSYSJ + "/sysj201/group/groupHotDataList";
    }

    /**
     * 圈子玩家列表（旧接口）
     */
    public final String groupMemberList() {
        return BaseHome + "/game/groupMemberList.html";
    }

    /**
     * 圈子玩家列表
     */
    public final String groupGamerList() {
        return BaseSYSJ + "/sysj201/group/groupGamerList";
    }

    /**
     * 圈子评论列表
     */
    public final String groupReviewList() {
        return BaseHome + "/game/reviewList.html?";
    }

    /**
     * 关注圈子
     */
    public final String groupAttention() {
        return BaseHome + "/game/attention.html";
    }

    /**
     * 关注圈子201
     */
    public final String groupAttentionGroup() {
        return BaseSYSJ + "/sysj201/group/attentionGroup";
    }

	/* ############## 广场 ############## */

    /**
     * 广场列表
     */
    public final String squareList() {
        return BaseSYSJ + "/sysj218/PlayerSquare/squareList";
    }

    /**
     * 广场游戏分类列表
     */
    public final String squareGameList() {
        return BaseSYSJ + "/sysj218/PlayerSquare/getPlayerSquareGame";
    }

    /**
     * 广场页面统计
     */
    public final String squareGameListStatistical() {
        return BaseSYSJ + "/sysj218/PlayerSquare/addPlayerSquareStatistical";
    }

	/* ############## 视频 ############## */

    /*
     * 视频点赞
     */
    public final String videoFlower() {
        return BaseHome + "/video/flower.html";
    }

    /*
     * 取消视频点赞
     */
    public final String videoCancelFlower() {
        return BaseHome + "/video/cancelFlower.html";
    }

    /*
     * 收藏视频
     */
    public final String videoCollect() {
        return BaseHome + "/video/collect.html";
    }

    /*
     * 取消收藏视频
     */
    public final String videoCancelCollect() {
        return BaseHome + "/video/cancelCollect.html";
    }

    /**
     * 视频详情201
     */
    public final String videoDetail201() {
        return BaseSYSJ + "/sysj201/video/detail";
    }

    /**
     * 下一个视频
     */
    public final String videoPlayNext() {
        return BaseHome + "/video/playNext.html";
    }

    /**
     * 视频评论列表
     */
    public final String videoCommentList() {
        return BaseSYSJ + "/sysj201/video/commentList";
    }

    /**
     * 视频评论列表211
     */
    public final String videoCommentList211() {
        return BaseSYSJ + "/Sysj211/Video/commentList";
    }

    /**
     * 视频发布评论（多级）
     */
    public final String videoDoComment() {
        return BaseSYSJ + "/sysj201/video/doComment";
    }

    /**
     * 视频收藏
     * <p/>
     * 视频收藏要登录,可以收藏/取消收藏
     */
    public final String videoCollect2() {
        return BaseSYSJ + "/sysj201/video/collect";
    }

    /**
     * 视频点赞（游客可点赞，游客点赞用前台记录，点赞数只加不减；有登录正常添加/取消）
     */
    public final String videoFlower2() {
        return BaseSYSJ + "/sysj201/video/flower";
    }

    /**
     * 视频踩203
     */
    public final String fndownClick203() {
        return BaseSYSJ + "/sysj203/fndown/click";
    }

    /**
     * 视频评论点赞（游客可点赞，游客点赞用前台记录，点赞数只加不减；有登录正常添加/取消）
     */
    public final String videoCommentLike2() {
        return BaseSYSJ + "/sysj201/video/commentLike";
    }

    /**
     * 视频图文取消收藏
     */
    public final String memberCancelCollect() {
        return BaseSYSJ + "/sysj201/member/cancelCollect";
    }

	/* ############## 意见反馈/关于我们 ############## */

    /**
     * 关于我们
     */
    public final String aboutUs() {
        return BaseHome + "/index/aboutUs.html";
    }

    /**
     * 意见反馈
     */
    public final String feedback() {
        return BaseHome + "/index/feedback.html";
    }

    /**
     * 版本升级
     */
    public final String updateVersion() {
        return BaseHome + "/index/updateVersion.html";
    }


	/* ############## ############## */

    /**
     * 小编荐
     */
    public final String editorRecommend() {
        return BaseHome + "/recommend/editorRecommend.html";
    }

    /**
     * 返回视界专栏视频二级列表
     */
    public final String topicList() {
        return BaseHome + "/recommend/topicList.html";
    }

    /**
     * 游戏下载数+1
     */
    public final String videoDownLoad203() {
        return BaseSYSJ + "/sysj203/download/click";
    }

    /**
     * 更多热门视频、精彩推荐
     */
    public final String homeDaily() {
        return BaseHome + "/video/homeDaily.html";
    }

    public final String likeVideoList() {
        return BaseHome + "/video/likeVideoList.html";
    }

    /**
     * 游戏类型(分类)列表
     */
    public final String typeList() {
        return BaseHome + "/game/typeList.html";
    }

    /**
     * 视频下载次数+1
     */
    public final String downLoad() {
        return BaseHome + "/video/downLoad.html";
    }

	/* ############## 搜索 ############## */

    /**
     * 搜索关键字(v1.1.4接口)
     */
    public final String keyWordList() {
        return BaseHome + "/search/keyWordList.html";
    }

    /**
     * 搜索关键字(v1.1.5接口)
     */
    public final String keyWordListNew() {
        return BaseHome + "/search/keyWordListNew.html";
    }

    /**
     * 搜索联想词
     */
    public final String associate() {
        return BaseHome + "/search/associate.html";
    }

    /**
     * 视频搜索
     */
    public final String searchVideo() {
        return BaseHome + "/search/searchVideo.html";
    }

    /**
     * 搜索礼包
     */
    public final String searchPackage() {
        return BaseHome + "/search/searchPackage.html";
    }

    /**
     * 搜索玩家
     */
    public final String searchMember() {
        return BaseHome + "/search/searchMember.html";
    }

    /**
     * 搜索任务
     */
    public final String searchTask() {
        return BaseHome + "/search/searchTask.html";
    }

    /**
     * 圈子搜索
     */
    public final String groupSearch() {
        return BaseHome + "/search/groupSearch.html";
    }

    /**
     * 搜索联想词201
     */
    public final String associate201() {
        return BaseHome + "/search/associate201.html";
    }

    /**
     * 搜索联想词201
     */
    public final String associate210() {
        return BaseSYSJ + "/Upload/Video/associate";
    }

    /**
     * 搜索视频203
     */
    public final String searchVideo203() {
        return BaseSYSJ + "/sysj203/Search/searchVideo";
    }

    /**
     * 搜索游戏203
     */
    public final String searchGame203() {
        return BaseSYSJ + "/sysj203/Search/searchGame";
    }

    /**
     * 搜索玩家203
     */
    public final String searchMember203() {
        return BaseSYSJ + "/sysj203/Search/searchMember";
    }

    /**
     * 搜索礼包203
     */
    public final String searchPackage203() {
        return BaseSYSJ + "/sysj203/Search/searchPackage";
    }

	/* ############## ############## */

    /**
     * 商务合作的路径
     */
    public final String business() {
        return BaseHome + "/index/business.html";
    }

	/* ############## 任务 ############## */

    /**
     * 任务列表
     */
    @Deprecated
    public final String taskListNew() {
        return BaseHome + "/member/taskListNew.html";
    }

    /**
     * 任务列表115
     */
    @Deprecated
    public final String taskList115() {
        return BaseHome + "/member/taskList115.html";
    }

    /**
     * 任务列表201
     */
    public final String taskList201() {
        return BaseSYSJ + "/sysj201/member/taskList";
    }

    /**
     * 任务列表203
     */
    public final String taskList203() {
        return BaseSYSJ + "/sysj203/task/list";
    }

    /**
     * 接受任务
     */
    public final String acceptTask() {
        return BaseHome + "/member/acceptTask.html";
    }

    /**
     * 接受任务201
     */
    public final String acceptTask201() {
        return BaseSYSJ + "/sysj201/member/acceptTask";
    }

    /**
     * 做任务
     */
    public final String doTask() {
        return BaseHome + "/member/execute.html";
    }

    /**
     * 做任务115
     */
    @Deprecated
    public final String doTask115() {
        return BaseHome + "/member/doTask115.html";
    }

    /**
     * 做任务117
     */
    @Deprecated
    public final String doTask117() {
        return BaseHome + "/member/doTask117.html";
    }

    /**
     * 做任务201
     */
    public final String doTask201() {
        return BaseSYSJ + "/sysj201/member/doTask";
    }

    /**
     * 做任务203
     */
    public final String doTask203() {
        return BaseSYSJ + "/sysj203/task/do";
    }

    /**
     * 是否完成任务
     */
    public final String finishTaskNew() {
        return BaseHome + "/member/finishTaskNew.html";
    }

    /**
     * 领取任务奖励
     */
    public final String getExp() {
        return BaseHome + "/member/getExp.html";
    }

    /**
     * 未完成任务个数
     */
    public final String taskUnDoNum() {
        return BaseHome + "/member/taskUnDoNum";
    }

	/* ############## 活动 ############## */

    /**
     * 返回活动页面三个数据列表（活动、赛事、任务）
     */
    public final String taskList() {
        return BaseHome + "/member/taskList.html";
    }

    /**
     * 返回活动中心路径
     */
    public final String getFocuseList() {
        return BaseHome + "/focuse/getFocuseList.html";
    }

    /**
     * 活动详情
     */
    public final String activityDetail() {
        return BaseHome + "/recommend/activityDetail.html";
    }

    /* ############## 版本审核 ############## */

    public final String checkAndroidStatus() {
        return BaseHome + "/sys/checkAndroidStatus";
    }

    /* ############## 货币商城 ############## */

    /**
     * 首页任务
     */
    public final String unfinishedTask() {
        return BaseSYSJ + "/Sysj211/Currency/unfinishedTask";
    }

    /**
     * 兑换
     */
    public final String recommendedLocation() {
        return BaseSYSJ + "/Sysj217/Recommend/recommendedLocation";
    }

    /**
     * 兑换
     */
    public final String getMemberTask() {
        return BaseSYSJ + "/Sysj211/Currency/getMemberTask";
    }

    /**
     * 兑换
     */
    public final String payment() {
        return BaseSYSJ + "/Sysj211/CurrencyMall/payment";
    }

    /**
     * 商品详情
     */
    public final String goodsDetail() {
        return BaseSYSJ + "/Sysj217/CurrencyMall/goodsDetail";
    }

    /**
     * 订单详情
     */
    public final String orderDetail() {
        return BaseSYSJ + "/Sysj211/CurrencyMall/orderDetail";
    }

    /**
     * 商品列表
     */
    public final String getOrderList() {
        return BaseSYSJ + "/Sysj211/CurrencyMall/orderList";
    }

    /**
     * 商品列表
     */
    public final String goodsList() {
        return BaseSYSJ + "/Sysj217/CurrencyMall/goodsList";
    }

    /**
     * 个人魔豆数量
     */
    public final String getMemberCurrency() {
        return BaseSYSJ + "/Sysj211/Currency/getMemberCurrency";
    }

    /**
     * 账单
     */
    public final String getCurrencyRecord() {
        return BaseSYSJ + "/Sysj211/Currency/getCurrencyRecord";
    }

	/* ############## 赛事 ############## */

    /**
     * 功能：赛事上传视频成功后调用接口
     */
    public final String saveEventVideo() {
        return BaseSYSJ + "/Sysj214/NewEvents/saveEventVideo";
    }

    /**
     * 获取赛事报名问号内容
     */
    public final String getEventMsg() {
        return BaseSYSJ + "/Sysj214/NewEvents/getEventMsg";
    }

    /**
     * 获取赛事参赛队员人数接口
     */
    public final String getTeamMemberNumber() {
        return BaseSYSJ + "/Sysj213/Events/getTeamMemberNumber";
    }

    /**
     * 赛事胜利者弹出框判断第一次接口
     */
    public final String setAlert() {
        return BaseSYSJ + "/sysj210/events/setAlert";
    }

    /**
     * 赛事列表
     */
    public final String getMatchList1() {
        return BaseHome + "/member/getMatchList.html";
    }

    /**
     * 赛事列表
     */
    public final String getMatchList2() {
        return BaseHome + "/Match/getMatchList.html";
    }

    /**
     * 赛事列表
     */
    public final String getMatchList3() {
        return BaseHome + "/AloneVersion/getMatchList.html";
    }

    /**
     * 赛事列表201
     */
    public final String getMatchList201() {
        return BaseSYSJ + "/sysj201/member/getMatchList";
    }

    /**
     * 获取融云token204
     */
    public final String getRongCloudToken204() {
        return BaseSYSJ + "/sysj204/YongYun/getRongCloudToken";
    }

    /**
     * 获取客服id204
     */
    public final String getServiceName204() {
        return BaseSYSJ + "/sysj204/events/serviceName";
    }

    /**
     * 获取客服id204
     */
    public final String groupJoin208() {
        return BaseSYSJ + "/sysj204/YongYun/groupJoin";
    }

    /**
     * 获取乐播产品下载地址
     */
    public final String getLeboDownloadLink() {
        return "http://api.hpplay.com.cn/?a=lebo_apk";
    }

    /**
     * 获取群名
     */
    public final String groupName208() {
        return BaseSYSJ + "/sysj204/YongYun/groupName";
    }

    /**
     * 圈子赛事列表211
     */
    public final String getGroupEventsList211() {
        return BaseSYSJ + "/sysj211/events/getGroupEventsList";
    }

    /**
     * 赛事列表211
     */
    public final String getEventsList211() {
        return BaseSYSJ + "/sysj211/events/getEventsList";
    }

    /**
     * 赛事列表211 -- 测试用
     */
    public final String getEventsList211Test() {
        return BaseSYSJ + "/sysj211/events/getEventsTestList";
    }

    /**
     * 赛事详情204
     */
    public final String getEventsInfo204() {
        return BaseSYSJ + "/sysj204/events/getEventsInfo";
    }

    /**
     * 赛事详情210
     */
    public final String getEventsInfo210() {
        return BaseSYSJ + "/sysj210/events/getEventsInfo";
    }

    /**
     * 赛事签到204
     */
    public final String signSchedule204() {
        return BaseSYSJ + "/sysj204/events/signSchedule";
    }

    /**
     * 赛事签到210
     */
    public final String signSchedule210() {
        return BaseSYSJ + "/sysj210/events/signSchedule";
    }

    /**
     * 赛事报名204
     */
    public final String joinEvents204() {
        return BaseSYSJ + "/sysj204/events/joinEvents";
    }

    /**
     * 赛事报名214
     */
    public final String joinEvents() {
        return BaseSYSJ + "/Sysj214/NewEvents/joinEvents";
    }

    /**
     * 赛程时间表204
     */
    public final String eventsSchedule204() {
        return BaseSYSJ + "/sysj204/events/eventsSchedule";
    }

    /**
     * 我的赛程（进行中）208 fixme 测试专用接口
     */
    public final String getMemberMatchPKTest() {
        return BaseSYSJ + "/sysj201/test/getMemberMatchPK";
    }

    /**
     * 我的赛程（进行中）204
     */
    public final String getMemberMatchPK204() {
        return BaseSYSJ + "/sysj204/events/getMemberMatchPK";
    }

    /**
     * 我的赛程（进行中）210
     */
    public final String getMemberMatchPK210() {
        return BaseSYSJ + "/sysj210/events/getMemberMatchPK";
    }

    /**
     * 我的赛程（已结束）204
     */
    public final String getMemberPKList204() {
        return BaseSYSJ + "/sysj204/events/getMemberPKList";
    }

    /**
     * 我的赛事某个已结束的赛事
     */
    public final String getMemberEndPKWindow() {
        return BaseSYSJ + "/Sysj214/NewEvents/getMemberEndPKWindow";
    }

    /**
     * 客服名称
     */
    public final String getServiceName() {
        return BaseSYSJ + "/sysj204/events/serviceName";
    }

    /**
     * 赛事胜利者弹出框判断第一次接口210
     */
    public final String eventsSetAlert210() {
        return BaseSYSJ + "/sysj210/events/setAlert";
    }

    /**
     * 赛程 PK列表接口204
     */
    public final String eventsPKList204() {
        return BaseSYSJ + "/sysj204/events/eventsPKList";
    }

    /**
     * 赛程 PK列表接口210
     */
    public final String eventsPKList210() {
        return BaseSYSJ + "/sysj210/events/eventsPKList";
    }

    /**
     * 赛事详情
     */
    public final String matchInfo() {
        return BaseHome + "/Match/matchInfo.html";
    }

    /**
     * 赛事视频列表
     */
    public final String matchVideoList() {
        return BaseHome + "/Match/matchVideoList.html";
    }

    /**
     * 我的赛事列表
     */
    public final String myMatchList() {
        return BaseHome + "/Match/myMatchList.html";
    }

    /**
     * 我的赛事列表201
     */
    public final String myMatchList201() {
        return BaseSYSJ + "/sysj201/member/myMatchList";
    }

    /**
     * 我的赛事列表204
     */
    public final String myEventsList204() {
        return BaseSYSJ + "/sysj204/events/myEventsList";
    }

    /**
     * 参加赛事
     */
    public final String joinMatch() {
        return BaseHome + "/match/joinMatch.html";
    }

    /**
     * 赛事视频列表
     */
    public final String matchVideoList201() {
        return BaseSYSJ + "/sysj201/member/matchVideoList";
    }

    /**
     * 参加活动列表203
     */
    public final String selectMatch203() {
        return BaseSYSJ + "/sysj203/activity/selectMatch";
    }

    /**
     * 活动纯文本评论点赞
     */
    public final String flowerComment() {
        return BaseSYSJ + "/sysj201/Video/flowerComment";
    }

    /**
     * 活动详情208
     */
    public final String getMatchInfo() {
        return BaseSYSJ + "/sysj204/events/getMatchInfo";
    }

    /**
     * 活动tab分页
     */
    public final String getDetailMode() {
        return BaseSYSJ + "/Sysj213/Match/getDetailMode";
    }

    /**
     * 活动视频、图文、评论混合列表接口208
     */
    public final String getCompVideoLists208() {
        return BaseSYSJ + "/Sysj208/Video/getCompVideoLists";
    }

    /**
     * 参加活动列表201
     */
    public final String selectMatch201() {
        return BaseSYSJ + "/sysj201/activity/selectMatch";
    }

    /**
     * 参加活动列表
     */
    public final String selectMatch() {
        return BaseSYSJ + "/upload/video/selectMatch";
    }


	/* ############## 大神 ############## */

    /**
     * 大神详情头部信息接口
     */
    public final String specialTopic() {
        return BaseHome + "/topic/specialTopic.html";
    }

    /**
     * 大神详情页大神更新接口
     */
    public final String topicVideoList() {
        return BaseHome + "/topic/topicVideoList.html";
    }

    /**
     * 大神关注
     */
    public final String subscribe() {
        return BaseHome + "/topic/subscribe.html";
    }

    /**
     * 获取大神列表
     */
    public final String topicList2() {
        return BaseHome + "/topic/topicList.html";
    }

	/* ############## 榜单 ############## */

    /**
     * 达人榜
     */
    public final String daRenListNew() {
        return BaseHome + "/member/daRenListNew.html";
    }

    /**
     * 我的达人榜
     */
    public final String myDaRenList() {
        return BaseHome + "/member/myDaRenList.html";
    }

    /**
     * 主播榜
     */
    public final String rankingMemberRanking() {
        return BaseSYSJ + "/sysj201/ranking/memberRanking";
    }

    /**
     * 磨豆榜
     */
    public final String memberRankingCurrency() {
        return BaseSYSJ + "/Sysj211/Currency/memberRanking";
    }

    /**
     * 视频榜
     */
    public final String rankingVideoRanking() {
        return BaseSYSJ + "/sysj201/ranking/videoRanking";
    }

	/* ############## 首页 ############## */

    /**
     * 统计被邀请用户打开app的次数
     */
    public final String statisticalOpenApp() {
        return BaseSYSJ + "/Sysj214/OpenApp/statisticalOpenApp";
    }

    /**
     * 首页专栏116
     */
    public final String appIndexList() {
        return BaseHome + "/index/appIndexList.html";
    }

    /**
     * 首页
     */
    public final String appIndexListAloneVersion() {
        return BaseHome + "/AloneVersion/appIndexList.html";
    }

    /**
     * 首页更多116
     */
    public final String appIndexMore() {
        return BaseHome + "/index/appIndexMore.html";
    }

    /**
     * 首页更多
     */
    public final String appIndexMoreAloneVersion() {
        return BaseHome + "/AloneVersion/appIndexMore.html";
    }

    /**
     * 精彩推荐115 1、有选择喜欢类型，获取数据成功 2、没有选择喜欢类型，获取数据成功
     */
    public final String likeVideoList115() {
        return BaseHome + "/video/likeVideoList115.html";
    }

    /**
     * 轻松一刻
     */
    public final String relaxedMoment() {
        return BaseHome + "/video/relaxedMoment.html";
    }

    /**
     * 推荐
     */
    public final String recommendList() {
        return BaseHome + "/recommend/recommendList.html";
    }

    /**
     * 首页
     */
    public final String indexIndex() {
        return BaseSYSJ + "/sysj201/index/index";
    }

    /**
     * 首页204
     */
    public final String indexIndex204() {
        return BaseSYSJ + "/sysj204/index/index";
    }

    /**
     * 首页更多
     */
    public final String indexIndexMore() {
        return BaseSYSJ + "/sysj201/index/indexMore";
    }

    /**
     * 首页更多
     */
    public final String indexIndexMore217() {
        return BaseSYSJ + "/sysj217/index/indexMore";
    }

    /**
     * 首页廣告
     */
    public final String indexBanner() {
        return BaseSYSJ + "/sysj201/index/banner";
    }

    /**
     * 首页熱門遊戲
     */
    public final String indexHotGame() {
        return BaseSYSJ + "/sysj201/index/hotGame";
    }

    /**
     * 首页猜你喜歡
     */
    public final String indexGuessVideo() {
        return BaseSYSJ + "/sysj201/index/guessVideo";
    }

    /**
     * 首页視頻分組
     */
    public final String indexCompanyMemberVideo() {
        return BaseSYSJ + "/sysj201/index/companyMemberVideo";
    }

    /**
     * 首页热门解说
     */
    public final String indexHotMemberVideo() {
        return BaseSYSJ + "/sysj201/index/hotMemberVideo";
    }

    /**
     * 首頁视频列表
     */
    public final String indexVideoList() {
        return BaseSYSJ + "/sysj201/index/videoList";
    }

    /**
     * 生成问卷
     */
    public final String indexSurveyForm() {
        return BaseSYSJ + "/sysj201/index/surveyForm";
    }

    /**
     * 问卷
     */
    public final String indexDoSurvey() {
        return BaseSYSJ + "/sysj201/index/doSurvey";
    }

    /**
     * 播放推荐208
     */
    public final String changeVideo208() {
        return BaseSYSJ + "/sysj208/video/changeVideo";
    }

    /**
     * 首页猜你喜歡
     */
    public final String indexChangeGuess() {
        return BaseSYSJ + "/sysj201/index/changeGuess";
    }

    /**
     * 首页猜你喜歡详情
     */
    public final String indexChangeGuessSecond() {
        return BaseSYSJ + "/sysj201/index/changeGuessSecond";
    }

	/* ############## ############## */

    /**
     * 视频详情的路径
     */
    public final String detail() {
        return BaseHome + "/video/detail.html";
    }

    /**
     * 玩家秀视频列表
     */
    public final String playerShowList() {
        return BaseHome + "/recommend/playerShowList.html";
    }

    /**
     * 发现视频列表接口
     */
    public final String everyoneLook() {
        return BaseHome + "/video/everyoneLook.html";
    }

    /**
     * 下一个视频接口
     */
    public final String playNext() {
        return BaseHome + "/video/playNext.html";
    }

    /**
     * 获取用户上传的视频ID
     */
    public final String getVidById() {
        return BaseHome + "/Video/getVidById.html";
    }

	/* ############## 图文 ############## */

    /**
     * 图文详情
     */
    public final String photoPhotoDetail() {
        return BaseSYSJ + "/Sysj201/Photo/photoDetail";
    }

    /**
     * 图文评论列表
     */
    public final String photoPhotoCommentList() {
        return BaseSYSJ + "/Sysj201/Photo/photoCommentList";
    }

    /**
     * 图文评论点赞
     */
    public final String photoCommentLike() {
        return BaseSYSJ + "/Sysj201/Photo/commentLike";
    }

    /**
     * 图文发布评论
     */
    public final String photoSendComment() {
        return BaseSYSJ + "/Sysj201/Photo/sendComment";
    }

    /**
     * 图文献花
     */
    public final String photoFlower() {
        return BaseSYSJ + "/Sysj201/Photo/flower";
    }

    /**
     * 图文收藏
     */
    public final String photoCollection() {
        return BaseSYSJ + "/Sysj201/Photo/collection";
    }

    /**
     * 玩家关注
     */
    public final String photoAttention() {
        return BaseSYSJ + "/Sysj201/Photo/attention";
    }

	/* ############## 举报 ############## */

    /**
     * 举报类型列表
     */
    public final String reportType() {
        return BaseSYSJ + "/sysj201/group/reportType";
    }

    /**
     * 举报
     */
    public final String report() {
        return BaseSYSJ + "/sysj201/group/report";
    }

	/* ############## 上传视频 ############## */

    //传参判断封号，判断横，竖屏，判断加水印大小，判断传入的视频是高清还是标清，判断转码是转高清，还是转标清，
    // 生成七牛视频的名称，插入临时表，部分数据插入视频表，生成七牛凭证，添加参加活动数据

    /**
     * 视频上传凭证（POST）
     */
    public final String videoDoVideoMark() {
        return BaseSYSJ + "/sysj201/video/doVideoMark";
    }

    /**
     * 视频上传凭证203（POST）
     */
    public final String videoDoVideoMark203() {
        return BaseSYSJ + "/sysj203/video/doVideoMark";
    }

    /**
     * 视频上传凭证208
     */
    public final String doVideoMark() {
        return BaseSYSJ + "/Upload/Video/doVideoMark";
    }

    //app端收到七牛上传(fm-test)成功回调时，调用接口;视频状态3(上传成功),
    //临时视频状态1(成功),添加圈子视频信息(默认状态0);内调用图片上传函数

    /**
     * 视频上传回调（POST）
     */
    public final String videoQiniuTokenPass() {
        return BaseSYSJ + "/sysj201/video/qiniuTokenPass";
    }

    /**
     * 视频上传回调204（POST）
     */
    public final String saveEventVideo204() {
        return BaseSYSJ + "/sysj204/events/saveEventVideo";
    }

    /**
     * 视频上传回调203（POST）
     */
    public final String videoQiniuTokenPass203() {
        return BaseSYSJ + "/sysj203/video/qiniuTokenPass";
    }

    /**
     * 视频上传回调208
     */
    public final String qiniuTokenPass() {
        return BaseSYSJ + "/Upload/Video/qiniuTokenPass";
    }

    /**
     * 上传视频封面（POST）
     */
    public final String videoUploadPicQiniu() {
        return BaseSYSJ + "/sysj201/video/uploadPicQiniu";
    }

	/* ############## 上传图片 ############## */

    /**
     * 图片上传凭证208（POST）
     */
    public final String retPhotoKeyAndToken208() {
        return BaseSYSJ + "/Sysj208/Photo/retPhotoNameAndToken";
    }

    /**
     * 图片上传凭证204（POST）
     */
    public final String retPhotoKeyAndToken204() {
        return BaseSYSJ + "/sysj204/events/retPhotoKeyAndToken";
    }

    /**
     * 图片上传凭证（POST）
     */
    public final String photoRetPhotoNameAndToken() {
        return BaseSYSJ + "/Sysj201/Photo/retPhotoNameAndToken";
    }

    /**
     * 图片上传回调（POST）208
     */
    public final String savePhoto208() {
        return BaseSYSJ + "/Sysj208/Photo/savePhoto";
    }

    /**
     * 图片上传回调204（POST）
     */
    public final String saveEventResult204() {
        return BaseSYSJ + "/Sysj204/events/saveEventResult";
    }

    /**
     * 图片上传回调（POST）
     */
    public final String photoSavePhoto() {
        return BaseSYSJ + "/Sysj201/Photo/savePhoto";
    }

	/* ############## 统计 ############## */

    /**
     * 游戏下载数+1
     */
    public final String gameDownloadNum() {
        return BaseHome + "/game/downloadNum.html";
    }

    /**
     * 游戏下载数+1
     */
    public final String downloadClick217() {
        return BaseSYSJ + "/Sysj217/Download/click";
    }

    /**
     * 视频播放数+1
     */
    public final String videoClickVideo201() {
        return BaseSYSJ + "/sysj201/video/clickVideo";
    }

    /**
     * 视频分享数+1
     */
    public final String videoShare211() {
        return BaseSYSJ + "/Sysj211/Video/share";
    }

    /**
     * 视频下载数+1
     */
    public final String videoDownLoad201() {
        return BaseSYSJ + "/sysj201/video/downLoad";
    }

    /**
     * 关注/粉丝列表
     */
    public final String fansList203() {
        return BaseSYSJ + "/sysj203/member/fansList";
    }

	/* ############## 弹幕 ############## */

    /**
     * 弹幕列表
     */
    public final String bulletList203() {
        return BaseSYSJ + "/sysj203/bullet/list";
    }

//	mark=0/1 0为一级评论，1为多级评论
//	bullet=0/1 0不插入弹幕，1插入弹幕
//	node 为float型，小数点后一位，最小0.1
//	member_id 可空，即没有登录情况
//	comment_id 在mark=1的情况下不能为空
//	comment 在mark=1的情况下拼接之前一条评论内容发送

    /**
     * 弹幕发射
     */
    public final String bulletDo203() {
        return BaseSYSJ + "/sysj203/bullet/do";
    }

	/* ############## 精彩推荐 ############## */

    /**
     * 精彩推荐--视频列表
     */
    public final String editList203() {
        return BaseSYSJ + "/Sysj219/Edit/list";
    }

    /**
     * 精彩推荐--banner
     */
    public final String editBanner203() {
        return BaseSYSJ + "/sysj203/edit/banner";
    }

    /**
     * 精彩推荐--金牌主播
     */
    public final String editGoldMember203() {
        return BaseSYSJ + "/sysj203/edit/goldMember";
    }

	/* ############## 字幕  ############## */

    /**
     * 上传字幕
     */
    public final String srtUpload203() {
        return BaseSYSJ + "/sysj203/srt/upload";
    }

    /**
     * 获取字幕
     */
    public final String srtList203() {
        return BaseSYSJ + "/sysj203/srt/list";
    }


    /**
     * IM 消息（url） 内容解析
     */
    public final String parseMessage() {
        return BaseSYSJ + "/Sysj219/ResolveLink/getLinkInfo";
    }

    /**
     *使用融云还是自有IM
     */
    public final String switchChat(){
        return BaseSYSJ+"/Sysj220/SwitchSelfChat/switchChat";
    }

    /**
     *会员开通信息
     */
    public final String vipInfo(){
        return BaseSYSJ+"/Lpds227/VIP/getVIPRechargeInfo";
    }

    /**
     *礼物类型列表
     */
    public final String giftType(){
        return BaseSYSJ+"/Sysj221/Reward/getGift";
    }

    /**
     *礼物流水
     */
    public final String giftBill(){
        return BaseSYSJ+"/Sysj221/UserProfile/personalGift";
    }

    /**
     *礼物榜单
     */
    public final String getPlayGiftList(){
        return BaseSYSJ+"/Sysj221/Reward/rewardRanking";
    }

    /**
     *时间轴礼物列表
     */
    public final String getGiftTimeLineList(){
        return BaseSYSJ+"/Sysj221/Reward/rewardHistory";
    }

    /**
     *获取服务器时间
     */
    public final String getServiceTime(){
        return BaseSYSJ+"/Sysj221/Reward/serviceTimestamp";
    }

    /**
     *打赏
     */
    public final String playGift(){
        return BaseSYSJ+"/Sysj221/Reward/rewardGift";
    }

    /**
     *视频播放页分享成功后触发
     */
    public final String sharedSuccess(){
        return BaseSYSJ+"/Sysj221/ShareStatical/share";
    }

    /**
     *教练列表
     */
    public final String getCoachList(){
        return BaseSYSJ+"/Sysj222/Coach/coaches";
    }

    /**
     *教练详情
     */
    public final String getCoachDetail(){
        return BaseSYSJ+"/Sysj222/Coach/coachDetail";
    }

    /**
     *陪玩订单选项
     */
    public final String getPlayWithOrderOptions(){
        return BaseSYSJ+"/Sysj222/TrainingOrder/menuMap";
    }

    /**
     *陪玩订单价格预览
     */
    public final String getPreviewOrderPrice(){
        return BaseSYSJ+"/Sysj222/TrainingOrder/estimatePrice";
    }

    /**
     *生成陪玩订单
     */
    public final String createPlayWithOrder(){
        return BaseSYSJ+"/Sysj222/TrainingOrder/submitOrder";
    }

    /**
     *查询下单列表
     */
    public final String getPlayWithPlaceOrder(){
        return BaseSYSJ+"/Sysj222/TrainingOrder/getOrders";
    }

    /**
     *查询接单列表
     */
    public final String getPlayWithTakeOrder(){
        return BaseSYSJ+"/Sysj225/TrainingOrder/receiveOrders";
    }


    /**
     *查询订单详情
     */
    public final String getPlayWithOrderDetail(){
        return BaseSYSJ+"/Sysj222/TrainingOrder/orderDetail";
    }

    /**
     *刷新access token
     */
    public final String refreshAccessToken(){
        return BaseSYSJ+"/Sysj222/Token/refresh";
    }

    /**
     *确认
     */
    public final String confirmOrder(){
        return BaseSYSJ+"/Sysj225/TrainingOrder/pay";
    }

    /**
     *获取评论标签
     */
    public final String getCommentTag(){
        return BaseSYSJ+"/Sysj222/TrainingOrder/evaluateTag";
    }

    /**
     *提交评价
     */
    public final String commitComment(){
        return BaseSYSJ+"/Sysj222/TrainingOrder/evaluate";
    }

    /**
     *提交订单结果
     */
    public final String commitOrderResult(){
        return BaseSYSJ+"/Sysj222/TrainingOrder/submitResult";
    }

    /**
     *教练签到
     */
    public final String coachSign(){
        return BaseSYSJ+"/Sysj222/Coach/status";
    }

    /**
     *申请退款
     */
    public final String refundApply(){
        return BaseSYSJ+"/Sysj222/TrainingOrder/applyRefund";
    }

    /**
     *消息列表
     */
    public final String getMessageList(){
        return BaseSYSJ+"/Sysj222/message/sysjMessageList";
    }

    /**
     * 用户确认完成订单
     */
    public final String confirmOrderDone(){
        return BaseSYSJ+"/Sysj222/TrainingOrder/confirm";
    }

    /**
     * 教练确认接单
     */
    public final String confirmTakeOrder(){
        return BaseSYSJ+"/Sysj222/TrainingOrder/receive";
    }


    /**
     * 提交channel id
     */
    public final String submitChannelId(){
        return BaseSYSJ+"/Sysj222/BindingChannel/memberBindingChannel";
    }

    /**
     * 取消消息红点
     */
    public final String readMessage(){
        return BaseSYSJ+"/Sysj222/message/clearRedMsg";
    }

    /**
     * 获取客服信息
     */
    public final String getCustomerInfo(){
        return BaseSYSJ+"/Sysj222/TrainingCommunication/trainingCustomerService";
    }

    /**
     * 确认退款
     */
    public final String coachConfirmRefund(){
        return BaseSYSJ+"/Sysj222/TrainingOrder/confirmRefund";
    }

    /**
     * 获取教练状态
     */
    public final String getCoachStatus(){
        return "http://op.17sysj.com:9090/plugins/coachPresence/status";
    }

    /**
     * 获取订单开始时间
     */
    public final String getOrderTime(){
        return BaseSYSJ+"/Sysj222/TrainingOrder/timeMenu";
    }

    /**
     * 拉取补丁文件
     */
    public final String fetchPatch(){
        return BaseSYSJ+"/Sysj224/SysjVersionUpdate/versionHotUpdate";
    }

    /**
     * 获取vip信息
     */
    public final String getUserVipInfo(){
        return BaseSYSJ+"/Lpds227/VIP/getMemberVIPInfo";
    }

    /**
     * 获取图片上传的七牛key token
     */
    public final String getUploadKey(){
        return BaseSYSJ+"/Sysj224/Coach/getPicUploadParam";
    }

    /**
     * 上传个性签名
     */
    public final String commitSign(){
        return BaseSYSJ+"/Sysj224/Coach/saveInfo";
    }

    /**
     * 获取个性签名
     */
    public final String getSign(){
        return BaseSYSJ+"/Sysj224/Coach/psersonalizedSign";
    }

    /**
     * 解绑定百度推送
     */
    public final String unBindBaiduPush(){
        return BaseSYSJ+"/Sysj222/BindingChannel/removeBindingChannel";
    }

    /**
     * 打赏榜
     */
    public final String rewardRank(){
        return BaseSYSJ+"/Sysj224/RewardVideo/ranking";
    }

    /**
     * 对教练的评价
     */
    public final String getCoachComment(){
        return BaseSYSJ+"/Sysj224/TrainingAppraise/getTrainingAppraise";
    }

    /**
     * 统计播放页停留时间
     */
    public final String commitStayDuration(){
        return BaseSYSJ+"/Sysj224/Statistics/stayTimeRecord";
    }

    /**
     *clear token
     */
    public final String clearToken(){
        return BaseSYSJ+"/Sysj224/member/logout";
    }

    /**
     * 抢单
     */
    public final String grabPlayWithOrder(){
        return BaseSYSJ+"/Sysj225/TrainingOrder/grab";
    }

    /**
     * 取消订单
     */
    public final String cancelPlayWithOrder(){
        return BaseSYSJ+"/Sysj225/TrainingOrder/cancelOrder";
    }


    /**
     * 下载成功统计
     */
    public final String downloadSuccess(){
        return BaseSYSJ+"/Sysj225/DownloadGameStatistics/StatisticsDownloadGameSuccess";
    }
}
