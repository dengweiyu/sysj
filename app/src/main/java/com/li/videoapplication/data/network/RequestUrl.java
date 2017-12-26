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

	/* ############## 礼包 ############## */

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
     * 礼包详情203
     */
    public final String packageInfo203() {
        return BaseSYSJ + "/sysj203/gifts/packageInfo";
    }

	/* ############## 收藏 ############## */


    /**
     * 收藏视频列表
     */
    public final String memberCollectVideoList() {
        return BaseSYSJ + "/sysj201/member/collectVideoList";
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
     * 获取验证码 登陆注册（216 加密）
     */
    public final String msgRequestCode() {
        return BaseSYSJ + "/Sysj211/Message/getMsgCode";
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

	/* ############## 用户资料 ############## */

    /**
     * 用户资料（旧接口）
     */
    public final String detailNew() {
        return BaseHome + "/member/detailNew.html";
    }

    /**
     * 关注玩家201
     */
    public final String memberAttention201() {
        return BaseSYSJ + "/sysj201/member/attention";
    }

    /**
     * 个人资料
     */
    public final String userProfilePersonalInformation() {
        return BaseSYSJ + "/Sysj201/UserProfile/personalInformation";
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
     * 圈子消息总数
     */
    public final String messageMsgRed() {
        return BaseSYSJ + "/sysj201/message/msgRed";
    }

	/* ############## 圈子/游戏 ############## */


    /**
     * 最热，最新游戏列表
     */
    public final String gameList() {
        return BaseSYSJ + "/sysj201/game/gameList.html";
    }


    /**
     * 圈子类型
     */
    public final String groupType2() {
        return BaseSYSJ + "/sysj201/group/groupType";
    }

    /**
     * 圈子类型217
     */
    public final String gameCate() {
        return BaseSYSJ + "/sysj210/group/GameCate";
    }


    /**
     * 圈子列表
     */
    public final String groupList2() {
        return BaseSYSJ + "/sysj201/group/groupList";
    }


    /**
     * 圈子详情
     */
    public final String groupInfo() {
        return BaseSYSJ + "/sysj201/group/groupInfo";
    }

    /**
     * 圈子视频列表（最新）
     */
    public final String groupDataList() {
        return BaseSYSJ + "/Sysj226/Group/groupListData";
    }

    /**
     * 圈子视频列表（最热）
     */
    public final String groupHotDataList() {
        return BaseSYSJ + "/Sysj226/Group/groupListData";
    }


    /**
     * 圈子玩家列表
     */
    public final String groupGamerList() {
        return BaseSYSJ + "/sysj201/group/groupGamerList";
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
    /**
     * 视频详情201
     */
    public final String videoDetail201() {
        return BaseSYSJ + "/sysj226/video/getVideoDetail";
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

    /**
     * 版本升级
     */
    public final String updateVersion() {
        return BaseHome + "/index/updateVersion.html";
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



    /* ############## 版本审核 ############## */

    public final String checkAndroidStatus() {
        return BaseHome + "/sys/checkAndroidStatus";
    }

    /* ############## 货币商城 ############## */

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
     * 获取群名
     */
    public final String groupName208() {
        return BaseSYSJ + "/sysj204/YongYun/groupName";
    }

    /**
     * 赛事签到210
     */
    public final String signSchedule210() {
        return BaseSYSJ + "/sysj210/events/signSchedule";
    }

    /**
     * 赛事报名214
     */
    public final String joinEvents() {
        return BaseSYSJ + "/Sysj214/NewEvents/joinEvents";
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
     * 活动纯文本评论点赞
     */
    public final String flowerComment() {
        return BaseSYSJ + "/sysj201/Video/flowerComment";
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
     * 参加活动列表
     */
    public final String selectMatch() {
        return BaseSYSJ + "/upload/video/selectMatch";
    }

	/* ############## 榜单 ############## */


	/* ############## 首页 ############## */

    /**
     * 统计被邀请用户打开app的次数
     */
    public final String statisticalOpenApp() {
        return BaseSYSJ + "/Sysj214/OpenApp/statisticalOpenApp";
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
     * 视频上传凭证208
     */
    public final String doVideoMark() {
        return BaseSYSJ + "/Upload/Video/doVideoMark";
    }

    //app端收到七牛上传(fm-test)成功回调时，调用接口;视频状态3(上传成功),
    //临时视频状态1(成功),添加圈子视频信息(默认状态0);内调用图片上传函数
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

    /**
     * 关注游戏列表
     */
    public final String focusGameList(){
        return BaseSYSJ+"/Sysj225/Group/hotAttentionGame";
    }

    /**
     * 提交关注游戏列表
     */
    public final String commitFocusGameList(){
        return BaseSYSJ+"/Sysj225/Group/batchFollow";
    }


    /**
     * 混合页面圈子详情
     */
    public final String groupDetailHybrid(){
        return BaseSYSJ+"/sysj228/GameGroup/getGameGroupController";
    }

    /**
     * 需要混合页面的圈子
     */
    public final String groupHybridList(){
        return BaseSYSJ+"/sysj228/GameGroup/getGameGroupEdition";
    }
}
