package com.li.videoapplication.data.preferences;

import android.util.Log;

import com.fmsysj.screeclibinvoke.data.ConstantsPreferences;
import com.fmsysj.screeclibinvoke.data.model.configuration.InitializationSetting;
import com.fmsysj.screeclibinvoke.data.model.configuration.RecordingSetting;
import com.fmsysj.screeclibinvoke.data.model.event.SetFloatWindowEvent;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Advertisement;
import com.li.videoapplication.data.model.response.AdvertisementDto;
import com.li.videoapplication.data.model.entity.Associate;
import com.li.videoapplication.data.model.entity.HomeDto;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.Update;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.response.AdvertisementAdImage204Entity;
import com.li.videoapplication.data.model.response.AdvertisementAdLocation204Entity;
import com.li.videoapplication.tools.ArrayHelper;
import com.li.videoapplication.tools.JSONHelper;
import com.li.videoapplication.utils.StringUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.rong.eventbus.EventBus;

/**
 * 功能：保存数据帮助类
 */
public class PreferencesHepler {

    protected final String action = this.getClass().getName();

    protected final String tag = this.getClass().getSimpleName();

    private PreferencesHepler() {
        super();
    }

    private static PreferencesHepler instance;

    public static PreferencesHepler getInstance() {
        if (instance == null) {
            synchronized (PreferencesHepler.class) {
                if (instance == null) {
                    instance = new PreferencesHepler();
                }
            }
        }
        return instance;
    }

    private Gson gson = new Gson();


     /* ##############  玩家广场选中Tab页  ############### */

     public int getSquareTabPosition(){
         return UserPreferences.getInstance().getInt(Constants.SQUARE_POSITION,0);
     }

    public void saveSquareTabPosition(int position){
        UserPreferences.getInstance().putInt(Constants.SQUARE_POSITION,position);
    }

     /* ##############  IM  ############### */
    /**
     * 获取IM是否掉线
     */
    public boolean getIsIMlogOut() {
        boolean isIMLogout = UserPreferences.getInstance().getBoolean(Constants.IM_LOGOUT, false);
        Log.d(tag, "get/getIsIMlogOut=" + isIMLogout);
        return isIMLogout;
    }

    /**
     * 保存IM是否掉线
     */
    public void saveIsIMlogOut(boolean isIMLogout) {
        Log.d(tag, "save/saveIsIMlogOut=true");
        UserPreferences.getInstance().putBoolean(Constants.IM_LOGOUT, isIMLogout);
    }


    /* ##############  任务  ############### */

    /**
     * 获取刷新任务时间
     */
    public long getTaskTime() {
        long taskTime = UserPreferences.getInstance().getLong(Constants.TASK_TIME);
        Log.d(tag, "get/getTaskTime=" + taskTime);
        return taskTime;
    }

    /**
     * 保存刷新任务时间
     */
    public void saveTaskTime(long currentTime) {
        UserPreferences.getInstance().putLong(Constants.TASK_TIME, currentTime);
        Log.d(tag, "save/saveTaskTime=" + currentTime);
    }

    /** ##############  录屏设置  ############### */

    /**
     * 获取录屏设置
     */
    public RecordingSetting getRecordingSetting() {
        String string = NormalPreferences.getInstance().getString(Constants.RECORDING_SETTING);
        if (StringUtil.isNull(string)){
            RecordingSetting.DEFAULT.setQuality(RecordingSetting.QUALITY_ULTRA_HIGH);
            return RecordingSetting.DEFAULT;
        }
        RecordingSetting entity = JSONHelper.parse(string, RecordingSetting.class);
        if (entity == null)
            return RecordingSetting.DEFAULT;
        Log.d(tag, "getRecordingSetting: entity=" + entity);
        return entity;
    }

    /**
     * 保存录屏设置
     */
    public void saveRecordingSetting(RecordingSetting entity) {
        Log.d(tag, "saveRecordingSetting: entity=" + entity);
        if (entity != null)
            NormalPreferences.getInstance().putString(Constants.RECORDING_SETTING, JSONHelper.to(entity));
    }

    /**
     * 保存录屏设置：声音
     */
    public void saveRecordingSettingSoundRecording(boolean flag) {
        RecordingSetting recordingSetting = getRecordingSetting();
        recordingSetting.setSoundRecording(flag);
        saveRecordingSetting(recordingSetting);
    }

    /**
     * 保存录屏设置：摇晃
     */
    public void saveRecordingSettingShakeRecording(boolean flag) {
        RecordingSetting recordingSetting = getRecordingSetting();
        recordingSetting.setShakeRecording(flag);
        saveRecordingSetting(recordingSetting);
    }

    /**
     * 保存录屏设置：悬浮窗
     */
    public void saveRecordingSettingFloatingWindiws(boolean flag) {
        RecordingSetting recordingSetting = getRecordingSetting();
        recordingSetting.setFloatingWindiws(flag);
        saveRecordingSetting(recordingSetting);
        //
        EventBus.getDefault().post(new SetFloatWindowEvent());
    }

    /**
     * 保存录屏设置：触摸位置
     */
    public void saveRecordingSettingTouchPosition(boolean flag) {
        RecordingSetting recordingSetting = getRecordingSetting();
        recordingSetting.setTouchPosition(flag);
        saveRecordingSetting(recordingSetting);
    }

    /**
     * 保存录屏设置：游戏扫描
     */
    public void saveRecordingSettingGameScan(boolean flag) {
        RecordingSetting recordingSetting = getRecordingSetting();
        recordingSetting.setGameScan(flag);
        saveRecordingSetting(recordingSetting);
    }

    /**
     * 保存录屏设置：主播模式
     */
    public void saveRecordingSettingAnchorModel(boolean flag) {
        RecordingSetting recordingSetting = getRecordingSetting();
        recordingSetting.setAnchorModel(flag);
        saveRecordingSetting(recordingSetting);
    }

    /**
     * 保存录屏设置：录屏后跳转
     */
    public void saveRecordingSettingRecordedJump(boolean flag) {
        RecordingSetting recordingSetting = getRecordingSetting();
        recordingSetting.setRecordedJump(flag);
        saveRecordingSetting(recordingSetting);
    }

    /**
     * 保存录屏设置：录屏清晰度
     */
    public void saveRecordingSettingQuality(String s) {
        RecordingSetting recordingSetting = getRecordingSetting();
        recordingSetting.setQuality(s);
        saveRecordingSetting(recordingSetting);
    }

    /**
     * 保存录屏设置：是否横屏
     */
    public void saveRecordingSettingLandscape(boolean flag) {
        RecordingSetting recordingSetting = getRecordingSetting();
        if (flag) {
            recordingSetting.setLandscape(1);
        } else {
            recordingSetting.setLandscape(2);
        }
        saveRecordingSetting(recordingSetting);
    }

    /* ##############  动态  ############### */

    /**
     * 保存动态刷新时间
     */
    public void saveDynamicTime(long currentTime) {
        Log.d(tag, "save/dynamicTime=" + currentTime);
        UserPreferences.getInstance().putLong(Constants.DYNAMICT_TIME, currentTime);
    }

    /**
     * 获取动态刷新时间
     */
    public long getDynamicTime() {
        long dynamicTime = UserPreferences.getInstance().getLong(Constants.DYNAMICT_TIME);
        Log.d(tag, "get/dynamicTime=" + dynamicTime);
        return dynamicTime;
    }

	/* ##############  用户  ############### */

	/* ##############  id  ############### */

    /**
     * 登录用户id
     */
    public String getMember_id() {
        String member_id = UserPreferences.getInstance().getString(Constants.MEMBER_ID);
        Log.d(tag, "get/member_id=" + member_id);
        if (member_id == null) {
            member_id = "";
        }
        return member_id;
    }

    /**
     * 登录用户id
     */
    public void saveMember_id(String json) {
        Log.d(tag, "save/member_id=" + json);
        UserPreferences.getInstance().putString(Constants.MEMBER_ID, json);
    }

	/* ##############  是否登录  ############### */

    /**
     * 判断是否登录
     */
    public boolean isLogin() {
        boolean isLogin = false;
        if (!StringUtil.isNull(getMember_id())) {
            isLogin = true;
        }
        Log.d(tag, "isLogin=" + isLogin);
        return isLogin;
    }

	/* ##############  登录资料  ############### */

    /**
     * 获取登录资料
     */
    public Member getLogin() {
        String json = UserPreferences.getInstance().getString(Constants.LOGIN);
        Member member = gson.fromJson(json, Member.class);
        if (member == null) {
            member = new Member();
        }
        Log.d(tag, "get/login=" + member.toJSON());
        return member;
    }

    /**
     * 保存登录资料
     */
    public void saveLogin(Member member) {
        UserPreferences.getInstance().putString(Constants.LOGIN, gson.toJson(member));
        saveMember_id(member.getId());
        try {
            Log.d(tag, "save/login=" + member.toJSON());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** ##############  广告  ############### */

    /**
     * 获取广告位置列表
     */
    public List<Advertisement> getAdvertisements() {
        String string = NormalPreferences.getInstance().getString(Constants.ADVERTISEMENT_AD_LOCATION_204);
        List<Advertisement> list = gson.fromJson(string, new TypeToken<List<Advertisement>>() {
        }.getType());
        return list;
    }

    /**
     * 保存广告位置列表
     */
    public void saveAdvertisements(AdvertisementAdLocation204Entity entity) {
        if (entity != null &&
                entity.getData() != null &&
                entity.getData().size() > 0) {
            String string = gson.toJson(entity.getData());
            NormalPreferences.getInstance().putString(Constants.ADVERTISEMENT_AD_LOCATION_204, string);
        }
    }

    /**
     * 广告1：启动海报（录屏大师）
     */
    public Advertisement getAdvertisement_1() {
        String string = NormalPreferences.getInstance().getString(Constants.ADVERTISEMENT_1);
        Advertisement entity = gson.fromJson(string, Advertisement.class);
        return entity;
    }

    /**
     * 广告2：视频管理-本地视频
     */
    public Advertisement getAdvertisement_2() {
        String string = NormalPreferences.getInstance().getString(Constants.ADVERTISEMENT_2);
        Advertisement entity = gson.fromJson(string, Advertisement.class);
        return entity;
    }

    /**
     * 广告3：视频管理-云端视频
     */
    public Advertisement getAdvertisement_3() {
        String string = NormalPreferences.getInstance().getString(Constants.ADVERTISEMENT_3);
        Advertisement entity = gson.fromJson(string, Advertisement.class);
        return entity;
    }

    /**
     * 广告4：视频管理-截图
     */
    public Advertisement getAdvertisement_4() {
        String string = NormalPreferences.getInstance().getString(Constants.ADVERTISEMENT_4);
        Advertisement entity = gson.fromJson(string, Advertisement.class);
        return entity;
    }

    /**
     * 广告5：设置-更多精彩
     */
    public Advertisement getAdvertisement_5() {
        String string = NormalPreferences.getInstance().getString(Constants.ADVERTISEMENT_5);
        Advertisement entity = gson.fromJson(string, Advertisement.class);
        return entity;
    }

    /**
     * 广告6：启动海报（手游世界）
     */
    public Advertisement getAdvertisement_6() {
        String string = NormalPreferences.getInstance().getString(Constants.ADVERTISEMENT_6);
        Advertisement entity = gson.fromJson(string, Advertisement.class);
        return entity;
    }

    /**
     * 广告7：首页-第四副轮播
     */
    public Advertisement getAdvertisement_7() {
        String string = NormalPreferences.getInstance().getString(Constants.ADVERTISEMENT_7);
        Advertisement entity = gson.fromJson(string, Advertisement.class);
        return entity;
    }

    /**
     * 广告8：首页-热门游戏下方通栏
     */
    public Advertisement getAdvertisement_8() {
        String string = NormalPreferences.getInstance().getString(Constants.ADVERTISEMENT_8);
        Advertisement entity = gson.fromJson(string, Advertisement.class);
        return entity;
    }

    /**
     * 广告9：福利-活动第二位置
     */
    public Advertisement getAdvertisement_9() {
        String string = NormalPreferences.getInstance().getString(Constants.ADVERTISEMENT_9);
        Advertisement entity = gson.fromJson(string, Advertisement.class);
        return entity;
    }

    /**
     * 广告10：首页-热门游戏下方通栏
     */
    public Advertisement getAdvertisement_10() {
        String string = NormalPreferences.getInstance().getString(Constants.ADVERTISEMENT_10);
        Advertisement entity = gson.fromJson(string, Advertisement.class);
        return entity;
    }

    /**
     * 广告1-10
     */
    public void saveAdvertisement(AdvertisementAdImage204Entity entity) {
        if (entity != null &&
                entity.isResult() &&
                entity.getData() != null &&
                entity.getData().size() > 0) {
            Advertisement e = entity.getData().get(0);
            e.setChangetime(entity.getChangetime());

            String string = gson.toJson(e);
            if (e.getAd_location_id() == DataManager.ADVERTISEMENT.ADVERTISEMENT_1) {
                NormalPreferences.getInstance().putString(Constants.ADVERTISEMENT_1, string);
            } else if (e.getAd_location_id() == DataManager.ADVERTISEMENT.ADVERTISEMENT_2) {
                NormalPreferences.getInstance().putString(Constants.ADVERTISEMENT_2, string);
            } else if (e.getAd_location_id() == DataManager.ADVERTISEMENT.ADVERTISEMENT_3) {
                NormalPreferences.getInstance().putString(Constants.ADVERTISEMENT_3, string);
            } else if (e.getAd_location_id() == DataManager.ADVERTISEMENT.ADVERTISEMENT_4) {
                NormalPreferences.getInstance().putString(Constants.ADVERTISEMENT_4, string);
            } else if (e.getAd_location_id() == DataManager.ADVERTISEMENT.ADVERTISEMENT_5) {
                NormalPreferences.getInstance().putString(Constants.ADVERTISEMENT_5, string);
            } else if (e.getAd_location_id() == DataManager.ADVERTISEMENT.ADVERTISEMENT_6) {
                NormalPreferences.getInstance().putString(Constants.ADVERTISEMENT_6, string);
            } else if (e.getAd_location_id() == DataManager.ADVERTISEMENT.ADVERTISEMENT_7) {
                NormalPreferences.getInstance().putString(Constants.ADVERTISEMENT_7, string);
            } else if (e.getAd_location_id() == DataManager.ADVERTISEMENT.ADVERTISEMENT_8) {
                NormalPreferences.getInstance().putString(Constants.ADVERTISEMENT_8, string);
            } else if (e.getAd_location_id() == DataManager.ADVERTISEMENT.ADVERTISEMENT_9) {
                NormalPreferences.getInstance().putString(Constants.ADVERTISEMENT_9, string);
            } else if (e.getAd_location_id() == DataManager.ADVERTISEMENT.ADVERTISEMENT_10) {
                NormalPreferences.getInstance().putString(Constants.ADVERTISEMENT_10, string);
            }
        }
    }

    /* ##############  首页  ############### */

    /**
     * 删除首页缓存
     */
    public void removeHomeData() {
        Log.d(tag, "remove/HomeData ");
        NormalPreferences.getInstance().remove(Constants.HOME);
    }

    /**
     * 保存首页
     */
    public void saveHomeData(HomeDto entity) {
        Log.d(tag, "save/HomeData=" + entity.toJSON());
        NormalPreferences.getInstance().putString(Constants.HOME, entity.toJSON());
    }

    /**
     * 获取首页
     */
    public HomeDto getHomeData() {
        String json = NormalPreferences.getInstance().getString(Constants.HOME);
        HomeDto entity = null;
        try {
            if (!StringUtil.isNull(json)){
                entity = gson.fromJson(json, HomeDto.class);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        Log.d(tag, "get/HomeData=" + entity);
        return entity;
    }

	/* ##############  图片广告  ############### */

    /**
     * 删除图片广告缓存
     */
    public void removeIndexLaunchImage() {
        Log.d(tag, "remove/IndexLaunchImage ");
        NormalPreferences.getInstance().remove(Constants.INDEX_LAUNCH_IMAHE);
    }

    /**
     * 保存图片广告214
     */
    public void saveIndexLaunchImage(AdvertisementDto entity) {
        Log.d(tag, "save/IndexLaunchImage=" + entity.toJSON());
        NormalPreferences.getInstance().putString(Constants.INDEX_LAUNCH_IMAHE, entity.toJSON());
    }

    /**
     * 获取图片广告214
     */
    public AdvertisementDto getIndexLaunchImage() {
        String json = NormalPreferences.getInstance().getString(Constants.INDEX_LAUNCH_IMAHE);
        AdvertisementDto entity = gson.fromJson(json, AdvertisementDto.class);
        Log.d(tag, "get/IndexLaunchImage=" + entity);
        return entity;
    }

	/* ##############  版本更新  ############### */

    /**
     * 获取版本更新
     */
    public Update getUpdate() {
        String json = UserPreferences.getInstance().getString(Constants.UPDATE_VERSION);//取出
        Update update = gson.fromJson(json, Update.class);
        Log.d(tag, "get/update=" + update);
        return update;
    }

    /**
     * 保存版本更新
     */
    public void saveUpdate(Update update) {
        UserPreferences.getInstance().putString(Constants.UPDATE_VERSION, gson.toJson(update));//保存
        Log.d(tag, "save/update=" + update);
    }

	/* ##############  用户信息  ############### */

    /**
     * 获取用户信息（旧接口）
     */
    public Member getDetailNew() {
        String json = UserPreferences.getInstance().getString(Constants.DETAIL_NEW);
        Member member = gson.fromJson(json, Member.class);
        if (member == null) {
            String string = UserPreferences.getInstance().getString(Constants.LOGIN);
            member = gson.fromJson(json, Member.class);
        }
        if (member == null) {
            member = new Member();
        }
        Log.d(tag, "get/detailNew=" + member.toJSON());
        return member;
    }

    /**
     * 保存用户信息（旧接口）
     */
    public void saveDetailNew(Member member) {
        UserPreferences.getInstance().putString(Constants.DETAIL_NEW, gson.toJson(member));
        try {
            Log.d(tag, "save/detailNew=" + member.toJSON());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	/* ##############  个人信息  ############### */

    /**
     * 获取个人信息
     */
    public Member getUserProfilePersonalInformation() {
        String json = UserPreferences.getInstance().getString(Constants.USER_PROFILE_PERSONAL_INFORMATION);
        Member member = gson.fromJson(json, Member.class);
        if (member == null) {
            String json2 = UserPreferences.getInstance().getString(Constants.LOGIN);
            member = gson.fromJson(json2, Member.class);
        }
        if (member == null) {
            member = new Member();
        }
        try {
            Log.d(tag, "get/userProfilePersonalInformation=" + member.toJSON());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return member;
    }

    /**
     * 保存个人信息
     */
    public void saveUserProfilePersonalInformation(Member member) {
        //复制token 避免被覆盖
        if (isLogin()){
            if (member.getSysj_token() == null && getUserProfilePersonalInformation().getSysj_token() != null){
                member.setSysj_token(getUserProfilePersonalInformation().getSysj_token());
            }
        }
        UserPreferences.getInstance().putString(Constants.USER_PROFILE_PERSONAL_INFORMATION, gson.toJson(member));
        try {
            Log.d(tag, "save/userProfilePersonalInformation=" + member.toJSON());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ##############  赛事客服  ############### */

    /**
     * 保存赛事客服id
     */
    public void saveCustomerServiceIDs(List<String> list) {
        if (list != null && list.size() > 0) {
            String json = gson.toJson(list);
            NormalPreferences.getInstance().putString(Constants.MATCH_CS, json);
            try {
                Log.d(tag, "save/CustomerServiceIDs=" + json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取赛事客服id
     */
    public List<String> getCustomerServiceIDs() {
        String json = NormalPreferences.getInstance().getString(Constants.MATCH_CS);
        List<String> list = gson.fromJson(json, new TypeToken<List<String>>() {
        }.getType());
        try {
            Log.d(tag, "get/CustomerServiceIDs=" + json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /* ##############  推送状态  ############### */

    /**
     * 获取推送状态
     */
    public boolean getPushStatus() {
        boolean status = NormalPreferences.getInstance().getBoolean(Constants.PUSH_STATUS);
        Log.d(tag, "get/PushStatus=" + status);
        return status;
    }

    /**
     * 保存推送状态
     */
    public void savePushStatus(boolean status) {
        NormalPreferences.getInstance().putBoolean(Constants.PUSH_STATUS, status);
        Log.d(tag, "save/PushStatus=" + status);
    }

	/* ##############  融云token  ############### */

    /**
     * 获取个人登陆融云token
     */
    public String getUserToken() {
        String token = UserPreferences.getInstance().getString(Constants.USER_TOKEN);
        Log.d(tag, "get/userToken=" + token);
        return token;
    }

    /**
     * 保存个人登陆融云token
     */
    public void saveUserToken(String token) {
        UserPreferences.getInstance().putString(Constants.USER_TOKEN, token);
        Log.d(tag, "save/userToken=" + token);
    }

	/* ##############  搜索记录  ############### */

    /**
     * 获取搜索记录
     */
    public List<String> getSearchHistory() {
        String array = UserPreferences.getInstance().getString(Constants.HISTORY_SEARCH);
        List<String> list = ArrayHelper.array2List(array);
        if (list == null) {
            list = new ArrayList<>();
        }
        Log.d(tag, "get/history=" + list);
        return list;
    }

    /**
     * 删除观看记录
     */
    public void removeSearchHistory() {
        UserPreferences.getInstance().remove(Constants.HISTORY_SEARCH);
        Log.d(tag, "remove/history");
    }

    /**
     * 保存搜索记录
     */
    public void saveSearchHistory(List<String> list) {
        if (list != null && list.size() > 0) {
            Log.d(tag, "save/history=" + list);
            String array = ArrayHelper.list2Array(list);
            UserPreferences.getInstance().putString(Constants.HISTORY_SEARCH, array);
        }
    }

    /**
     * 添加搜索记录
     */
    public void addSearchHistory(String item) {
        List<String> list = new ArrayList<>();
        list.addAll(getSearchHistory());
        if (item != null) {
            boolean flag = true;// 增加
            for (String record : list) {
                if (record.equals(item)) {
                    flag = false;// 不增加
                    break;
                }
            }
            if (flag == true) {
                Log.d(tag, "add/history=" + item);
                list.add(item);
                saveSearchHistory(list);
            }
        }
    }

	/* ##############  观看记录  ############### */

    /**
     * 获取观看记录
     */
    public List<VideoImage> getHistoryVideoList() {
        String json = UserPreferences.getInstance().getString(Constants.HISTORY_VIDEO_LIST);
        List<VideoImage> list = gson.fromJson(json, new TypeToken<List<VideoImage>>() {
        }.getType());
        if (list == null) {
            list = new ArrayList<>();
        }
        Log.d(tag, "get/history=" + list);
        return list;
    }

    /**
     * 删除观看记录
     */
    public void removeHistoryVideoList() {
        UserPreferences.getInstance().remove(Constants.HISTORY_VIDEO_LIST);
    }

    /**
     * 保存观看记录
     */
    public void saveHistoryVideoList(List<VideoImage> list) {
        if (list != null && list.size() > 0) {
            String json = gson.toJson(list);
            UserPreferences.getInstance().putString(Constants.HISTORY_VIDEO_LIST, json);
        }
    }

    /**
     * 添加观看记录
     */
    public void addHistoryVideoList(VideoImage item) {
        List<VideoImage> list = getHistoryVideoList();
        if (list == null) {
            list = new ArrayList<>();
        }
        if (item != null) {
            boolean flag = true;// 增加
            for (VideoImage record : list) {
                if (record.getVideo_id().equals(item.getVideo_id())) {
                    flag = false;// 不增加
                }
            }
            if (flag) {
//                list.add(item);
                list.add(0, item);
                saveHistoryVideoList(list);
            }
        }
    }

	/* ##############  搜索联想  ############### */

    /**
     * 保存
     */
    public void saveAssociate201List(List<Associate> list) {
        if (list != null && list.size() > 0) {
            String json = UserPreferences.getInstance().getString(Constants.ASSOCIATE201);
            List<Associate> list2 = gson.fromJson(json, new TypeToken<List<Associate>>() {
            }.getType());
            if (list2 != null && list2.size() > 0) {
                list.addAll(list2);
            }
            String json2 = gson.toJson(list);
            UserPreferences.getInstance().putString(Constants.ASSOCIATE201, json2);
            Log.d(tag, "save/associate=" + json2);
        }
    }

    /**
     * 保存
     */
    public void addAssociate201List(Associate item) {

        List<Associate> list = getAssociate201List();
        if (list == null) {
            list = new ArrayList<Associate>();
        }
        if (item != null) {
            boolean flag = true;// 增加
            for (Associate record : list) {
                if (record.getGame_id().equals(item.getGame_id())) {
                    flag = false;// 不增加
                }
            }
            if (flag == true) {
                list.add(item);
                removeAssociate201List();
                saveAssociate201List(list);
            }
        }
    }

    /**
     * 获取
     */
    public List<Associate> getAssociate201List() {
        String json = UserPreferences.getInstance().getString(Constants.ASSOCIATE201);
        List<Associate> list = gson.fromJson(json, new TypeToken<List<Associate>>() {
        }.getType());
        if (list == null) {
            list = new ArrayList<Associate>();
        }
        Log.d(tag, "get/associate=" + list);
        return list;
    }

    /**
     * 删除
     */
    public void removeAssociate201List() {
        UserPreferences.getInstance().remove(Constants.ASSOCIATE201);
    }

	/* ##############  问卷  ############### */

    /**
     * 保存问卷
     */
    public void saveGroupIds(List<String> list) {
        if (list != null && list.size() > 0) {
            String json = gson.toJson(list);
            UserPreferences.getInstance().putString(Constants.GROUP_IDS, json);
            try {
                Log.d(tag, "save/groupIds=" + json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取问卷
     */
    public List<String> getGroupIds() {
        String json = UserPreferences.getInstance().getString(Constants.GROUP_IDS);
        List<String> list = gson.fromJson(json, new TypeToken<List<String>>() {
        }.getType());
        if (list == null) {
            list = new ArrayList<String>();
        }
        try {
            Log.d(tag, "get/groupIds=" + json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取问卷
     */
    public String getGroupIds2() {
        String json = UserPreferences.getInstance().getString(Constants.GROUP_IDS);
        List<String> list = gson.fromJson(json, new TypeToken<List<String>>() {
        }.getType());
        String arr = ArrayHelper.list2Array(list);
        try {
            Log.d(tag, "get/groupIds=" + arr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }

	/* ############## 猜你喜歡  ############### */

    /**
     * 保存猜你喜歡
     */
    public void saveVideoIds(List<String> list) {
        if (list != null && list.size() > 0) {
            String json = gson.toJson(list);
            NormalPreferences.getInstance().putString(Constants.VIDEO_IDS, json);
            try {
                Log.d(tag, "save/videoIds=" + json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取猜你喜歡
     */
    public List<String> getVideoIds() {
        String json = NormalPreferences.getInstance().getString(Constants.VIDEO_IDS);
        List<String> list = gson.fromJson(json, new TypeToken<List<String>>() {
        }.getType());
        try {
            Log.d(tag, "get/videoIds=" + json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 保存猜你喜歡视频保存的时间
     */
    public void saveVideoIdsTime() {
        long mills = System.currentTimeMillis();
        Log.d(tag, "saveVideoIdsTime: == " + mills);
        NormalPreferences.getInstance().putLong(Constants.VIDEO_IDS_TIME, mills);
    }

    /**
     * 重置猜你喜歡视频保存的时间
     */
    public void resetVideoIdsTime() {
        NormalPreferences.getInstance().putLong(Constants.VIDEO_IDS_TIME, 0l);
    }

    /**
     * 保存猜你喜歡视频是否过期
     *
     * @return true:过期
     * false:没过期
     */
    public boolean canVideoIdsTime() {
        long old = NormalPreferences.getInstance().getLong(Constants.VIDEO_IDS_TIME);
        Date oldDate = new Date(old);
        long now = System.currentTimeMillis();
        Date nowDate = new Date(now);
        long l = nowDate.getTime() - oldDate.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        Log.d(tag, "oldDate=" + oldDate.toString());
        Log.d(tag, "nowDate=" + nowDate.toString());
        Log.d(tag, "day=" + day);
        if (day > 3) {
            return true;
        } else {
            return false;
        }
    }

	/* ##############  网络访问  ############### */

    /**
     * 保存网络访问数据
     */
    public void saveRequest(String url, Map<String, Object> params, String resultString) {
        RequestPreferences.getInstance().save(url, params, resultString);
    }

    /**
     * 获取网络访问数据
     */
    public String getRequest(String url, Map<String, Object> params) {
        return RequestPreferences.getInstance().get(url, params);
    }

	/* ############## 清除数据  ############### */

    /**
     * 清除所有数据
     */
    public boolean clearAll() {
        UserPreferences.getInstance().clear();
        SettingPreferences.getInstance().clear();
        return true;
    }

    /**
     * 获取初始化配置
     */
    public InitializationSetting getInitializationSetting() {
        String string = NormalPreferences.getInstance().getString(ConstantsPreferences.INITIALIZATION_SETTING);
        if (StringUtil.isNull(string))
            return InitializationSetting.DEFAULT;
        InitializationSetting entity = JSONHelper.parse(string, InitializationSetting.class);
        if (entity == null)
            return InitializationSetting.DEFAULT;
        Log.d(tag, "getInitializationSetting: entity=" + entity);
        return entity;
    }
}
