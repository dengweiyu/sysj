package com.li.videoapplication.tools;

import android.content.Context;
import android.util.Log;

import com.li.videoapplication.framework.AppManager;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

/**
 * Created by linhui on 2017/12/25.
 */
public final class UmengAnalyticsHelper2 {

    /**
     * 录屏总数	只要点击了开始录屏（浮窗的录屏，无浮窗的横竖录屏，通知栏的录屏，摇晃录屏）就+1
     * 录屏成功数	录制生成视频时一次+1
     * 横屏录制	以横屏模式开始录屏一次+1
     * 竖屏录制	以竖屏状态开始录屏一次+1
     * 自主无声录屏	玩家在设置界面关闭了声音，然后开始开始录屏一次+1
     * 被动无声录屏	玩家设置中是打开声音录制的，但是由于权限没给到，这种情况下录屏一次+1
     * 悬浮窗录屏	悬浮窗的录屏按钮点击一次+1
     * 无浮窗录屏	无浮窗模式下，点击一次横屏录制或者竖屏录制+1
     */
    public static final String LU_PING_ID = "luping";//luping	录屏
    public static final String SCREEND_RECORD_COUNT_TYPE = "录屏总数";
    public static final String ADD_SCREEND_RECORD_VIDEO_TYPE = "录屏成功数";
    public static final String SCREEND_RECORD_LANDSCAPE_TYPE = "横屏录制";
    public static final String SCREEND_RECORD_PORTRAIT_TYPE = "竖屏录制";
    public static final String SCREEND_RECORD_NO_VOICE_BY_ME_TYPE = "自主无声录屏";
    public static final String SCREEND_RECORD_NO_VOICE_BY_SYSTEM_TYPE = "被动无声录屏";//在录屏sdk中反射调用了
    public static final String SCREEND_RECORD_FLOAT_WINDOW_TYPE = "悬浮窗录屏";
    public static final String SCREEND_RECORD_NO_FLOAT_WINDOW_TYPE = "无浮窗录屏";

    /**
     * 个人中心-入口	点击个人中心按钮一次+1
     * 客服-入口	在首页点击客服按钮一次+1
     * 客服-在线客服	客服界面，点击在线客服一次+1
     * 客服-留言反馈	客服界面，点击留言反馈一次+1
     * 设置-入口-点击	视频开始录制时，是竖屏状态+1
     * 悬浮窗模式-开	在首页，把悬浮窗按钮由关闭变换为开启一次+1
     * 悬浮窗模式-关	在首页，把悬浮窗按钮由开启变换为关闭一次+1
     * 横屏录制-按钮	在首页，点击横屏录制按钮一次+1
     * 竖屏录制-按钮	在首页，点击竖屏录制按钮一次+1
     * 横屏录制-全屏	在首页，从横竖对半分屏的状态或者竖屏录制的全屏状态变换为横屏录制的全屏状态一次+1
     * 竖屏录制-全屏	在首页，从横竖对半分屏的状态或者横屏录制的全屏状态变换为横屏录制的全屏状态一次+1
     */
    public static final String SHOU_YE_ID = "shouye";//shouye	首页
    public static final String MY_CENTER_ENTRANCE_TYPE = "个人中心-入口";
    public static final String SERVER_TYPE = "客服-入口";
    public static final String SERVER_ONLINE_TYPE = "客服-在线客服";
    public static final String SERVER_LEAVE_MESSAGE_TYPE = "客服-留言反馈";
    public static final String SETTING_MAIN_ENTRANCE_TYPE = "设置-入口";
    public static final String FLOAT_WINDOW_MODE_OPEN_TYPE = "悬浮窗模式-开";
    public static final String FLOAT_WINDOW_MODE_CLOSE_TYPE = "悬浮窗模式-关";
    public static final String SCREEND_RECORD_LANDSCAPE_BUTTON_TYPE = "横屏录制-按钮";
    public static final String SCREEND_RECORD__PORTRAIT_BUTTON_TYPE = "竖屏录制-按钮";
    public static final String SCREEND_RECORD_LANDSCAPE_CHANGE_TYPE = "横屏录制-全屏";//未做
    public static final String SCREEND_RECORD_PORTRAIT_ChANGE_TYPE = "竖屏录制-全屏";//未做


    /**
     * 视频-入口	当进入到视频页面一次+1（包括从首页、发现、赛事TAB点击进来，还有录屏后跳转打开之后也算）
     * VIP-视频-按钮-入口	在视频页面（包括本地，云端，截图），点击VIP按钮一次+1
     * VIP-视频-文字-入口	在视频页面（包括本地，云端，截图），点击“成为会员...”文字一次+1
     * 导入-按钮	在视频页面，点击导入按钮一次+1
     * 导入-确定导入	在导入选单界面，点击导入一次+1
     * 导入-取消导入	在导入选单界面，点击取消一次+1
     * 本地视频-分享	在本地视频页面，点击分享按钮一次+1
     * 本地视频-复制	在本地视频页面，点击复制按钮一次+1
     * 本地视频-删除	在本地视频页面，点击删除按钮一次+1
     * 本地视频-重命名	在本地视频页面，点击重命名按钮一次+1
     * 本地视频-播放	在本地视频页面，点击视频播放一次+1
     * 视频-请点这里	在本地视频页面，点击“请点这里”跳转一次+1
     * 云端视频-入口	在本地视频和截图页面，点击云端视频TAB一次+1
     * 云端视频分享	在云端视频页面，点击分享按钮一次+1
     * 云端视频-播放	在云端视频页面，点击播放一个云端视频一次+1
     * 云端视频-粉丝互动	在云端视频页面，点击“可前往...粉丝互动”一次+1
     * 云端视频-查看详情	在云端视频页面，点击查看详情一次+1
     * 云端视频-申请推荐位	在云端视频页面，点击申请手游视界推荐位一次+1
     * 截图-入口	在本地视频和云端视频页面，点击截图TAB一次+1
     * 截图-分享	在截图页面，点击分享按钮一次+1
     */
    public static final String SHI_PIN_ID = "shipin";//shipin	视频
    public static final String VIDEO_ENTRANCE_TYPE = "视频-入口";
    public static final String VIP_VIDEO_ENTRANCE_BUTTON_TYPE = "VIP-视频-按钮-入口";
    public static final String VIP_VIDEO_ENTRANCE_TEXT_TYPE = "VIP-视频-文字-入口";
    public static final String IMPORT_BUTTON_TYPE = "导入-按钮";
    public static final String IMPORT_BUTTON_AGREE_TYPE = "导入-确定导入";
    public static final String IMPORT_BUTTON_CANCEL_TYPE = "导入-取消导入";
    public static final String NATIVE_VIDEO_SHARE_BUTTON_TYPE = "本地视频-分享";
    public static final String NATIVE_VIDEO_COPY_BUTTON_TYPE = "本地视频-复制";
    public static final String NATIVE_VIDEO_DELETE_BUTTON_TYPE = "本地视频-删除";
    public static final String NATIVE_VIDEO_RENAME_BUTTON_TYPE = "本地视频-重命名";
    public static final String NATIVE_VIDEO_PLAY_BUTTON_TYPE = "本地视频-播放";
    public static final String VIDEO_PLEASE_CLICK_TYPE = "视频-请点这里";//未做
    public static final String CLOUD_VIDEO_BUTTON_TYPE = "视频-云端视频-入口";
    public static final String CLOUD_VIDEO_SHARE_BUTTON_TYPE = "云端视频分享";
    public static final String CLOUD_VIDEO_PLAY_BUTTON_TYPE = "云端视频-播放";
    public static final String CLOUD_VIDEO_FANDS_TYPE = "云端视频-粉丝互动";
    public static final String CLOUD_VIDEO_DETAILS_TYPE = "云端视频-查看详情";
    public static final String CLOUD_VIDEO_APPLY_RECOMMEND_TYPE = "云端视频-申请推荐位";
    public static final String SCREEND_SHOT_ENTRANCE_TYPE = "截图-入口";
    public static final String SCREEND_SHOT_SHARE_TYPE = "截图-分享";

    /**
     * 发现-入口	当进入到发现页面一次+1（包括从首页、视频、赛事陪练页面点击进来，等等其他页面进来也算，打开就算一次）
     * 玩家广场	在发现页面，点击玩家广场一次+1
     * 让视频更好玩	在发现页面，点击让视频更好玩一次+1
     * 游戏	在发现页面，点击游戏一次+1
     * 即点即玩	在发现页面，点击即点即玩一次+1
     * 外设商城	在发现页面，点击外设商城一次+1
     * 手游视界	在发现页面，点击手游视界一次+1
     * 活动	在发现页面，点击活动一次+1
     * 积分商城	在发现页面，点击积分商城一次+1
     * 抽奖	在发现页面，点击抽奖一次+1
     * 联系我们	在发现页面，点击联系我们一次+1
     */
    public static final String FA_XIAN_ID = "faxian";//faxian	发现
    public static final String FA_XIAN_ENTRANCE_TYPE = "发现-入口";
    public static final String PLAYER_SQUARE_TYPE = "玩家广场";
    public static final String DOWNLOAD_NATIVE_TYPE = "让视频更好玩";
    public static final String GAME_TYPE = "游戏";
    public static final String NOW_PALY_TYPE = "即点即玩";
    public static final String PERIPHERAL_STORE_TYPE = "外设商城";
    public static final String SYSJ_TYPE = "手游视界";
    public static final String ACTIVITY_TYPE = "活动";
    public static final String INTEGRAL_TYPE = "积分商城";
    public static final String LOTTERY_TYPE = "抽奖";
    public static final String CALL_US_TYPE = "联系我们";


    /**
     * 赛事陪练入口	当进入到赛事陪练页面一次+1（包括从首页、发现、视频页面点击进来，等等其他页面进来也算，打开就算一次）
     * 赛事陪练-手游视界下载	在赛事陪练页面，点击右上方下载按钮一次+1
     * 赛事-筛选	在赛事页面，点击筛选按钮一次+1
     * 赛事-赛事曝光	在赛事页面，点击赛事一次+1
     * 陪练-入口	当进入到陪练页面一次+1（包括从赛事点进去，或者广告引导进去，打开一次陪练页面就算一次）
     */
    public static final String SAI_SHI_PEI_LIAN_ID = "saishipeilian";//saishipeilian	赛事陪练
    public static final String COMPETITIONP_PAARTNER_ENTRANCE_TYPE = "赛事陪练入口";
    public static final String COMPETITIONP_PAARTNER_SYSJ_DOWNLOAD_TYPE = "赛事陪练-手游视界下载";
    public static final String COMPETITIONP_FILTER_TYPE = "赛事-筛选在赛事页面";//web，未做
    public static final String COMPETITIONP_TYPE = "赛事-赛事曝光";//web，未做
    public static final String PAARTNER_ENTRANCE_TYPE = "陪练-入口";//web，未做

    /**
     * 登录-手机	以手机号方式登录成功一次+1
     * 登录-QQ	以QQ登录的方式登录成功一次+1
     * 登录-微信	以微信登录的方式登录成功一次+1
     * 登录-微博	以微博的登录方式登录成功一次+1
     * 个人中心-消息	在个人页面，点击消息按钮一次+1
     * 个人中心-资料	在个人页面，点击更多按钮（右箭头）一次+1
     * 个人中心-VIP	在个人页面，点击VIP按钮一次+1
     * 个人中心-设置	在个人页面，点击设置按钮一次+1
     * 个人中心-播放	在个人中心页面，点击视频播放一次+1
     */
    public static final String GE_REN_ZHONG_XIN_ID = "gerenzhongxin";//gerenzhongxin	个人中心
    public static final String LOGIN_PHONE_TYPE = "登录-手机";
    public static final String LOGIN_QQ_TYPE = "登录-QQ";
    public static final String LOGIN_WECHAT_TYPE = "登录-微信";
    public static final String LOGIN_WEIBO_TYPE = "登录-微博";
    public static final String MY_CENTER_MESSAGE_TYPE = "个人中心-消息";
    public static final String MY_CENTER_INFO_TYPE = "个人中心-资料";
    public static final String MY_CENTER_VIP_TYPE = "个人中心-VIP";
    public static final String MY_CENTER_SETTING_TYPE = "个人中心-设置";
    public static final String MY_CENTER_PLAY_VIDEO_TYPE = "个人中心-播放";

    /**
     * 设置进入次数	设置页面打开一次就+1
     * 设置-运行权限	在设置界面点击运行权限一次+1
     * 设置-标清	在设置界面将清晰度设置为标清一次+1
     * 设置-高清	在设置界面将清晰度设置为高清一次+1
     * 设置-超高清	在设置界面将清晰度设置为超高清一次+1
     * 设置-主播模式	在设置界面打开主播模式一次+1
     * 设置-声音-开启	在设置界面将声音打开一次+1
     * 设置-声音-关闭	在设置界面将声音关闭一次+1
     * 设置-摇晃录屏-开启	在设置界面将摇晃录屏开启一次+1
     * 设置-摇晃录屏-关闭	在设置界面将摇晃录屏关闭一次+1
     * 设置-录屏后跳转-开启	在设置界面将录屏后跳转至编辑页面开启一次+1
     * 设置-录屏后跳转-关闭	在设置界面将录屏后跳转至编辑页面关闭一次+1
     * 设置-教程与反馈	在设置界面点击教程与反馈一次+1
     * 设置-版本更新	在设置界面点击版本更新一次+1
     * 设置-邀请好友	在设置界面点击邀请好友一次+1
     * 设置-关于我们	在设置界面点击关于我们一次+1
     */
    public static final String SHE_ZHI_ID = "shezhi";//shezhi	设置
    public static final String SETTING_ENTRANCE_TYPE = "设置进入次数";
    public static final String SETTING_AUTHORITY_TYPE = "设置-运行权限";
    public static final String SETTING_RECORD_STANDARD_TYPE = "设置-标清";
    public static final String SETTING_RECORD_HIGHT_TYPE = "设置-高清";
    public static final String SETTING_RECORD_ULTRAHIGH_TYPE = "设置-超高清";
    public static final String SETTING_SCREED_RECORD_CAMERA_OPEN_TYPE = "设置-主播模式";
    public static final String SETTING_VOICE_OPEN_TYPE = "设置-声音-开启";
    public static final String SETTING_VOICE_CLOSE_TYPE = "设置-声音-关闭";
    public static final String SETTING_SCREND_RECORD_ROCK_TYPE = "设置-摇晃录屏-开启";
    public static final String SETTING_SCREND_RECORD_NO_ROCK_TYPE = "设置-摇晃录屏-关闭";
    public static final String SETTING_SCREND_RECORD_NO_TYPE = "设置-录屏后跳转-开启";
    public static final String SETTING_SCREND_RECORD_NO_GO_TYPE = "设置-录屏后跳转-关闭";
    public static final String SETTING_COURSE_TYPE = "设置-教程与反馈";
    public static final String SETTING_VERSION_UPDATE_TYPE = "设置-版本更新";
    public static final String SETTING_INVITATION_FRIEND_TYPE = "设置-邀请好友";
    public static final String SETTING_ABOUT_ME_TYPE = "设置-关于我们";

    /**
     * 分享总次数	所有地方的分享点击次数都+1
     * 本地分享	本地视频点击分享按钮次数一次+1（视频模块也有这个，这里也记录一次）
     * 本地分享成功	生成链接，视频上传中就+1，
     * 视频上传成功	视频上传完成就+1
     * 云端分享	云端点击分享按钮次数
     * 分享渠道-QQ	（本地，云端，截图）分享时，点击QQ渠道一次+1
     * 分享渠道-QQ空间	（本地，云端，截图）分享时，点击QQ空间一次+1
     * 分享渠道-微信	（本地，云端，截图）分享时，点击微信一次+1
     * 分享渠道-朋友圈	（本地，云端，截图）分享时，点击朋友圈一次+1
     * 分享渠道-微博	（本地，云端，截图）分享时，点击微博一次+1
     * 类型选择-生活类	本地分享时，类型选择界面，点击一下生活类一次+1
     * 分享-带标签	分享游戏视频界面，一个视频如果选择了标签不论几个 +1
     * 分享-更多标签	分享游戏视频界面，点击标签的更多按钮一次+1
     * 分享-视频特权	分享本地视频界面，点击任何特权一次+1
     * 分享-三个月特权	分享时的特权购买页面，点击三个月一次+1
     * 分享-六个月特权	分享时的特权购买页面，点击六个月一次+1
     * 分享-十二个月特权	分享时的特权购买页面，点击十二个月一次+1
     * 分享-特权支付	分享时的特权购买页面，点击立即支付一次+1
     * 分享-官方推荐	分享游戏视频界面，点击申请官方推荐按钮一次+1
     */
    public static final String FEN_XIANG_ID = "fenxiang";//fenxiang	分享
    public static final String SHARE_ALL_TYPE = "分享总次数";
    public static final String SHARE_NATIVE_TYPE = "本地分享";
    public static final String SHARE_NATIVE_SUCCEED_TYPE = "本地分享成功";
    public static final String VIDEO_UPLOAD_SUCCEED_TYPE = "视频上传成功";
    public static final String CLOUD_SHARE_TYPE = "云端分享";
    public static final String SHARE_ChANNEL_QQ_TYPE = "分享渠道-QQ";
    public static final String SHARE_ChANNEL_QQZONE_TYPE = "分享渠道-QQ空间";
    public static final String SHARE_ChANNEL_SYSJ_TYPE = "分享渠道-sysj";
    public static final String SHARE_ChANNEL_WECHAT_TYPE = "分享渠道-微信";
    public static final String SHARE_ChANNEL_MEIPAI_TYPE = "分享渠道-美拍";
    public static final String SHARE_ChANNEL_WECHAT_FRIEND_TYPE = "分享渠道-朋友圈";
    public static final String SHARE_ChANNEL_WEIBO_TYPE = "分享渠道-微博";
    public static final String GENRE_CHOOSE_LIFE_TYPE = "类型选择-生活类";
    public static final String ShARE_TAG_TYPE = "分享-带标签";
    public static final String SHARE_MORE_TAG_TYPE = "分享-更多标签";
    public static final String SHARE_VIDEO_VIP_PRIVILEGE_TYPE = "分享-视频特权";
    public static final String SHARE_THREE_MONTH_PRIVILEGE_TYPE = "分享-三个月特权";
    public static final String SHARE_SIX_MONTH_PRIVILEGE_TYPE = "分享-六个月特权";
    public static final String SHARE_TWELVE_MONTH_PRIVILEGE_TYPE = "分享-十二个月特权";
    public static final String SHARE_PRIVILEGE_PAY_TYPE = "分享-特权支付";
    public static final String SHARE_OFFICIAL_RECOMMEND_TYPE = "分享-官方推荐";

    /**
     * VIP页面曝光数	VIP购买页面展示一次就+1
     * VIP-V2	VIP购买页面点击一下V2或者V2特权一次+1
     * VIP-V3	VIP购买页面点击一下V3或者V3特权一次+1
     * VIP-购买	VIP购买页面点击确定一次+1
     * VIP-支付	选择支付渠道页面点击立即支付一次+1
     * V1-一月-发起	VIP购买页面选择V1一个月的情况下，点击了确定一次+1
     * V1-三月-发起	VIP购买页面选择V1三个月的情况下，点击了确定一次+1
     * V1-六月-发起	VIP购买页面选择V1六个月的情况下，点击了确定一次+1
     * V1-十二月-发起	VIP购买页面选择V1十二个月的情况下，点击了确定一次+1
     * V1-一月-成功购买	成功购买了V1一个月一次+1
     * V1-三月-成功购买	成功购买了V1三个月一次+1
     * V1-六月-成功购买	成功购买了V1六个月一次+1
     * V1-十二月-成功购买	成功购买了V1十二个月一次+1
     * V2-一月-发起	VIP购买页面选择V2一个月的情况下，点击了确定一次+1
     * V2-三月-发起	VIP购买页面选择V2三个月的情况下，点击了确定一次+1
     * V2-六月-发起	VIP购买页面选择V2六个月的情况下，点击了确定一次+1
     * V2-十二月-发起	VIP购买页面选择V2十二个月的情况下，点击了确定一次+1
     * V2-一月-成功购买	成功购买了V2一个月一次+1
     * V2-三月-成功购买	成功购买了V2三个月一次+1
     * V2-六月-成功购买	成功购买了V2六个月一次+1
     * V2-十二月-成功购买	成功购买了V2十二个月一次+1
     * V3-一月-发起	VIP购买页面选择V3一个月的情况下，点击了确定一次+1
     * V3-三月-发起	VIP购买页面选择V3三个月的情况下，点击了确定一次+1
     * V3-六月-发起	VIP购买页面选择V3六个月的情况下，点击了确定一次+1
     * V3-十二月-发起	VIP购买页面选择V3十二个月的情况下，点击了确定一次+1
     * V3-一月-成功购买	成功购买了V3一个月一次+1
     * V3-三月-成功购买	成功购买了V3三个月一次+1
     * V3-六月-成功购买	成功购买了V3六个月一次+1
     * V3-十二月-成功购买	成功购买了V3十二个月一次+1
     */
    public static final String VIP_ID = "vip";//vip	VIP
    public static final String VIP_PAGE_SHOW_COUNT_TYPE = "VIP页面曝光数";
    public static final String VIP_V2_CHOOSE_TYPE = "VIP-V2";
    public static final String VIP_V3_CHOOSE_TYPE = "VIP-V3";
    public static final String VIP_BUY_TYPE = "VIP-购买";
    public static final String VIP_PAY_TYPE = "VIP-支付";
    public static final String VIP_V1_CHOOSE_MONTH_1_TYPE = "V1-一月-发起";
    public static final String VIP_V1_CHOOSE_MONTH_3_TYPE = "V1-三月-发起";
    public static final String VIP_V1_CHOOSE_MONTH_6_TYPE = "V1-六月-发起";
    public static final String VIP_V1_CHOOSE_MONTH_12_TYPE = "V1-十二月-发起";
    public static final String VIP_V1_PAY_SUCCEED_MONTH_1_TYPE = "V1-一月-成功购买";
    public static final String VIP_V1_PAY_SUCCEED_MONTH_3_TYPE = "V1-三月-成功购买";
    public static final String VIP_V1_PAY_SUCCEED_MONTH_6_TYPE = "V1-六月-成功购买";
    public static final String VIP_V1_PAY_SUCCEED_MONTH_12_TYPE = "V1-十二月-成功购买";
    public static final String VIP_V2_CHOOSE_MONTH_1_TYPE = "V2-一月-发起";
    public static final String VIP_V2_CHOOSE_MONTH_3_TYPE = "V2-三月-发起";
    public static final String VIP_V2_CHOOSE_MONTH_6_TYPE = "V2-六月-发起";
    public static final String VIP_V2_CHOOSE_MONTH_12_TYPE = "V2-十二月-发起";
    public static final String VIP_V2_PAY_SUCCEED_MONTH_1_TYPE = "V2-一月-成功购买";
    public static final String VIP_V2_PAY_SUCCEED_MONTH_3_TYPE = "V2-三月-成功购买";
    public static final String VIP_V2_PAY_SUCCEED_MONTH_6_TYPE = "V2-六月-成功购买";
    public static final String VIP_V2_PAY_SUCCEED_MONTH_12_TYPE = "V2-十二月-成功购买";
    public static final String VIP_V3_CHOOSE_MONTH_1_TYPE = "V3-一月-发起";
    public static final String VIP_V3_CHOOSE_MONTH_3_TYPE = "V3-三月-发起";
    public static final String VIP_V3_CHOOSE_MONTH_6_TYPE = "V3-六月-发起";
    public static final String VIP_V3_CHOOSE_MONTH_12_TYPE = "V3-十二月-发起";
    public static final String VIP_V3_PAY_SUCCEED_MONTH_1_TYPE = "V3-一月-成功购买";
    public static final String VIP_V3_PAY_SUCCEED_MONTH_3_TYPE = "V3-三月-成功购买";
    public static final String VIP_V3_PAY_SUCCEED_MONTH_6_TYPE = "V3-六月-成功购买";
    public static final String VIP_V3_PAY_SUCCEED_MONTH_12_TYPE = "V3-十二月-成功购买";

    /**
     * 浮窗出现次数	只要浮窗出现一次+1
     * 浮窗-录屏	浮窗展开情况下，点击录屏一次+1
     * 浮窗-暂停	浮窗录屏情况下，点击暂停一次+1
     * 浮窗-停止	浮窗录屏情况下，点击停止一次+1
     * 浮窗-主播开	浮窗展开情况下，点击主播模式开启一次+1
     * 浮窗-主播关	浮窗展开情况下，点击主播模式关闭一次+1
     * 浮窗-主页	浮窗展开情况下，点击主页一次+1
     * 浮窗-截图	浮窗展开情况下，点击截图一次+1
     */
    public static final String FU_CHUANG_ID = "fuchuang";//fuchuang	浮窗
    public static final String FLOAT_SHOW_COUNT_TYPE = "浮窗出现次数";
    public static final String FLOAT_SCREEND_RECORD_TYPE = "浮窗-录屏";
    public static final String FLOAT_SCREEND_RECORD_PAUSE_TYPE = "浮窗-暂停";
    public static final String FLOAT_SCREEND_RECORD_STOP_TYPE = "浮窗-停止";
    public static final String FLOAT_SCREEND_RECORD_CAMERA_OPEN_TYPE = "浮窗-主播开";
    public static final String FLOAT_SCREEND_RECORD_CAMERA_CLOSETYPE = "浮窗-主播关";
    public static final String FLOAT_MAIN_TYPE = "浮窗-主页";
    public static final String FLOAT_SCREENDSHOT_TYPE = "浮窗-截图";

    private UmengAnalyticsHelper2() {
    }

    public static void onEvent(String eventId, String eventType) {
        onEvent(AppManager.getInstance().getContext(), eventId, eventType);
    }

    public static void onEvent(Context context, String eventId, String eventType) {
        Log.i("UmengAnalyticsHelper2", "onEvent: eventId = " + eventId + "  eventType = " + eventType);
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("type", eventType);
            MobclickAgent.onEvent(context, eventId, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
