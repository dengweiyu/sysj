package com.li.videoapplication.data.network;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.EventManager;
import com.li.videoapplication.data.cache.DanmukuCache;
import com.li.videoapplication.data.cache.RequestCache;
import com.li.videoapplication.data.danmuku.DanmukuListEntity;
import com.li.videoapplication.data.danmuku.DanmukuListXmlParser;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.Update;
import com.li.videoapplication.data.model.response.AdvertisementAdImage204Entity;
import com.li.videoapplication.data.model.response.AdvertisementAdLocation204Entity;
import com.li.videoapplication.data.model.response.BulletList203Entity;
import com.li.videoapplication.data.model.response.CheckAndroidStatusEntity;
import com.li.videoapplication.data.model.response.DetailNewEntity;
import com.li.videoapplication.data.model.response.GetRongCloudToken204Entity;
import com.li.videoapplication.data.model.response.IndexChangeGuessEntity;
import com.li.videoapplication.data.model.response.IndexLaunchImageEntity;
import com.li.videoapplication.data.model.response.LaunchImageEntity;
import com.li.videoapplication.data.model.response.LoginEntity;
import com.li.videoapplication.data.model.response.PhotoSavePhotoEntity;
import com.li.videoapplication.data.model.response.SaveEventVideo204Entity;
import com.li.videoapplication.data.model.response.UpdateVersionEntity;
import com.li.videoapplication.data.model.response.UserProfilePersonalInformationEntity;
import com.li.videoapplication.data.model.response.VideoQiniuTokenPass203Entity;
import com.li.videoapplication.data.preferences.Constants;
import com.li.videoapplication.data.preferences.NormalPreferences;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.BaseEntity;
import com.li.videoapplication.framework.BaseResponseEntity;
import com.li.videoapplication.tools.JPushHelper;
import com.li.videoapplication.utils.HareWareUtil;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

import io.rong.eventbus.EventBus;


/**
 * 功能：网络请求响应结果处理
 */
public class ResponseHandler {

    protected final String tag = this.getClass().getSimpleName();
    protected final String action = this.getClass().getName();

    private ResponseObject response;
    private BaseEntity entity, newEntity, templateEntity;

    public BaseEntity getEntity() {
        return entity;
    }

    public ResponseHandler(ResponseObject response) {
        super();
        if (response == null) {
            return;
        }
        this.response = response;
        entity = response.getEntity();
        if (entity == null) {
            return;
            //throw new NullPointerException();
        }
        if (entity instanceof BaseResponseEntity) {
            this.newEntity = (BaseResponseEntity) entity.clone();
            this.templateEntity = (BaseResponseEntity) entity.clone();
        } else {
            this.newEntity = entity;
            this.templateEntity = entity;
        }
    }

    private Gson gson = new Gson();

    public void handle() {

        if (entity == null)
            return;

        if (response == null) {
            return;
        }

        String url = new String();
        Map<String, Object> params = new HashMap<String, Object>();
        boolean result = false;
        String resultString = new String();
        String msg = new String();
        String data = new String();

        try {
            url = response.getUrl();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            params = response.getParams();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            result = response.isResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            resultString = response.getResultString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            msg = response.getMsg();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            data = response.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // 解析网络访问结果
            entity = gson.fromJson(resultString, templateEntity.getClass());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        Log.i(tag, "entity=" + entity);

        try {
            // 回调网络访问结果
            callBack(response, entity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // 保存网络访问结果
            savePreferences(response, url, params, result, resultString, msg, data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // 回调网络访问结果
            postEventBus(response, url, params, result, resultString, msg, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Handler handler = new Handler(Looper.getMainLooper());

    /**
     * 功能：回调网络访问结果
     */
    private void callBack(final ResponseObject response,
                          final BaseEntity entity) throws Exception {

        handler.post(new Runnable() {
            @Override
            public void run() {

                // TODO: 2016/3/29
            }
        });
    }

    /**
     * 功能：回调网络访问结果
     */
    private void postEventBus(ResponseObject response,
                              String url,
                              Map<String, Object> params,
                              boolean result,
                              String resultString,
                              String msg,
                              String data) throws Exception {
        Log.d(tag, "postEventBus: ");
        if (entity != null) {
            // 回调网络访问结果
            EventBus.getDefault().post(convert(entity));
        }
    }

    /**
     * 功能：保存网络访问结果
     */
    private void savePreferences(ResponseObject response,
                                 String url,
                                 Map<String, Object> params,
                                 boolean result,
                                 String resultString,
                                 String msg,
                                 String data) throws Exception {

        Log.d(tag, "savePreferences: ");

        /**
         * 图片广告（旧接口）
         */
        if (url.equals(RequestUrl.getInstance().launchImage())) {
            if (result) {
                PreferencesHepler.getInstance().saveLaunchImage((LaunchImageEntity) this.entity);
                int changeTime = ((LaunchImageEntity) this.entity).getChangetime();
                NormalPreferences.getInstance().putInt(Constants.LAUNCH_IMAHE_CHANGETIME, changeTime);
            }
        }

        /**
         * 图片广告
         */
        if (url.equals(RequestUrl.getInstance().indexLaunchImage())) {
            if (result) {
                PreferencesHepler.getInstance().saveIndexLaunchImage((IndexLaunchImageEntity) this.entity);
                int changeTime = ((IndexLaunchImageEntity) this.entity).getChangetime();
                NormalPreferences.getInstance().putInt(Constants.INDEX_LAUNCH_IMAHE_CHANGETIME, changeTime);
            }
        }

        /**
         * 广告位置列表
         */
        if (url.equals(RequestUrl.getInstance().advertisementAdLocation204())) {
            if (result) {
                PreferencesHepler.getInstance().saveAdvertisements((AdvertisementAdLocation204Entity) this.entity);
            }
            return;
        }

        /**
         * 位置广告图集
         */
        if (url.equals(RequestUrl.getInstance().advertisementAdImage204())) {
            if (result) {
                PreferencesHepler.getInstance().saveAdvertisement((AdvertisementAdImage204Entity) this.entity);
            }
        }

        /**
         * 登录
         */
        if (url.equals(RequestUrl.getInstance().login())) {
            if (result) {
                Member member = ((LoginEntity) this.entity).getData();
                // 保存
                PreferencesHepler.getInstance().saveLogin(member);

                // 设置友盟推送别名
                // UPushHelper.setAlias(member.getId());
                JPushHelper.setAlias(member.getId());

                if (NetUtil.isConnect()) {
                    String ipAddress = NetUtil.getLocalIpAddress();
//                    LogHelper.i(tag,"ip ===================== " + ipAddress);
                    //ICP 统计相关接口--用户登录记录
                    DataManager.sign(member.getId(), member.getNickname(), "sign_in", "sysj_a", ipAddress,
                            HareWareUtil.getHardwareCode(), HareWareUtil.getIMEI(), "");
                }
            }
        }

        /**
         * 用户资料（旧接口）
         */
        if (url.equals(RequestUrl.getInstance().detailNew())) {
            String id = (String) params.get("id");
            String member_id = (String) params.get("member_id");
            if (id.equals(member_id)) {
                if (result) {
                    Member member = ((DetailNewEntity) this.entity).getData();
                    // 保存
                    PreferencesHepler.getInstance().saveDetailNew(member);
                }
            } else {
                // 保存
                RequestCache.save(url, params, resultString);
            }
        }

        /**
         * 个人资料
         */
        if (url.equals(RequestUrl.getInstance().userProfilePersonalInformation())) {
            String user_id = (String) params.get("user_id");
            String member_id = (String) params.get("member_id");
            if (user_id.equals(member_id)) {// 用户个人资料
                if (result) {
                    Member member = ((UserProfilePersonalInformationEntity) this.entity).getData();
                    // 保存
                    PreferencesHepler.getInstance().saveUserProfilePersonalInformation(member);

                    // 发布刷新个人资料事件
                    EventManager.postUserInfomationEvent();

                    // 保存问卷
                    PreferencesHepler.getInstance().saveGroupIds(member.getLikeGroupType());

                    // 首页猜你喜歡
                    DataManager.indexChangeGuess(PreferencesHepler.getInstance().getGroupIds2());

                    // 获取融云token
                    DataManager.getRongCloudToken204(member_id, member.getNickname());

                } else {// 玩家资料
                    // 保存
                    RequestCache.save(url, params, resultString);
                }
            }
        }

        /**
         * 获取融云token204回调
         */
        if (url.equals(RequestUrl.getInstance().getRongCloudToken204())) {
            if (result) {
                String token = ((GetRongCloudToken204Entity) this.entity).getData().getToken();
                //保存token
                PreferencesHepler.getInstance().saveUserToken(token);
            }
        }

        /**
         * 版本审核
         */
        if (url.equals(RequestUrl.getInstance().checkAndroidStatus())) {
            if (result) {
                String status = ((CheckAndroidStatusEntity) this.entity).getData().getStatus();
                Log.d(tag, "checkAndroidStatus: status == "+status);
                if (!StringUtil.isNull(status)) {
                    AppConstant.DOWNLOAD = status.equals("1"); // 1为显示, 0为隐藏
                }
                Log.d(tag, "checkAndroidStatus AppConstant.DOWNLOAD == "+AppConstant.DOWNLOAD);
            }
        }

        /**
         * 首页猜你喜歡
         */
        if (url.equals(RequestUrl.getInstance().indexChangeGuess())) {
            Log.d(tag, "savePreferences: indexChangeGuess");
            // 保存猜你喜歡
            PreferencesHepler.getInstance().saveVideoIds(((IndexChangeGuessEntity) this.entity).getVideoIds());
            // 获取猜你喜歡视频保存的时间
            PreferencesHepler.getInstance().saveVideoIdsTime();
        }

        /**
         * 修改个人资料
         */
        if (url.equals(RequestUrl.getInstance().userProfileFinishMemberInfo())) {
            if (result) {
                String a = PreferencesHepler.getInstance().getMember_id();
                // 刷新个人资料
                DataManager.userProfilePersonalInformation(a, a);
            }
        }

        /**
         * 修改头像，封面
         */
        if (url.equals(RequestUrl.getInstance().userProfileUploadAvatar()) ||
                url.equals(RequestUrl.getInstance().userProfileUploadCover())) {
            if (result) {
                String b = PreferencesHepler.getInstance().getMember_id();
                // 刷新个人资料
                DataManager.userProfilePersonalInformation(b, b);
            }
        }

        /**
         * 版本更新
         */
        if (url.equals(RequestUrl.getInstance().updateVersion())) {
            if (result) {
                Update update = ((UpdateVersionEntity) entity).getData().get(0);
                if (update != null) {
                    // 保存版本更新
                    PreferencesHepler.getInstance().saveUpdate(update);
                }
            }
        }

        /**
         * 上传图片
         */
        if (url.equals(RequestUrl.getInstance().photoSavePhoto())) {
            if (result) {
                PhotoSavePhotoEntity savePhotoEntity = (PhotoSavePhotoEntity) entity;
                if (savePhotoEntity != null && savePhotoEntity.getPic_id() != null &&
                        !savePhotoEntity.getPic_id().equals("")) {
                    String pic_id = ((PhotoSavePhotoEntity) this.entity).getPic_id();

                    //ICP图片上传统计
                    DataManager.userdatabehavior(PreferencesHepler.getInstance().getMember_id(),
                            "", pic_id, "", "sysj_a", "", HareWareUtil.getHardwareCode(), HareWareUtil.getIMEI(), "");
                }
            }
        }

        /**
         * 上传视频204
         */
        if (url.equals(RequestUrl.getInstance().saveEventVideo204())) {
            if (result) {
                String video_id = ((SaveEventVideo204Entity) entity).getData().getVideo_id();
                //ICP视频上传统计
                DataManager.userdatabehavior(PreferencesHepler.getInstance().getMember_id(),
                        video_id, "", "", "sysj_a", "", HareWareUtil.getHardwareCode(), HareWareUtil.getIMEI(), "");
            }
        }

        /**
         * 上传视频203
         */
        if (url.equals(RequestUrl.getInstance().videoQiniuTokenPass203())) {
            if (result) {
                String video_id = ((VideoQiniuTokenPass203Entity) entity).getData().getVideo_id();
                //ICP视频上传统计
                DataManager.userdatabehavior(PreferencesHepler.getInstance().getMember_id(),
                        video_id, "", "", "sysj_a", "", HareWareUtil.getHardwareCode(), HareWareUtil.getIMEI(), "");
            }
        }

        /**
         * 弹幕库
         */
        if (url.equals(RequestUrl.getInstance().bulletList203())) {
            if (result) {
                BulletList203Entity bulletList203Entity = (BulletList203Entity) entity;
                String video_id = (String) params.get("video_id");
                if (video_id != null &&
                        bulletList203Entity != null &&
                        bulletList203Entity.getData() != null &&
                        bulletList203Entity.getData().size() > 0) {
                    DanmukuListEntity danmukuListEntity = DanmukuListEntity.tranform(bulletList203Entity);
                    if (danmukuListEntity != null &&
                            danmukuListEntity.getData() != null &&
                            danmukuListEntity.getData().size() > 0) {
                        DanmukuListXmlParser parser = new DanmukuListXmlParser();
                        String xml = parser.serialize(danmukuListEntity);
                        DanmukuCache.save(video_id, xml);
                    }
                }
            }
        }

        /**
         * 其他
         */
        if (result) {
            // //保存
            RequestCache.save(url, params, resultString);
        }

        /**
         * 无网络时，返回保存的数据
         */
        if (isDisconnect()) {
            String json = (RequestCache.get(url, params));
            this.entity = gson.fromJson(json, templateEntity.getClass());
            return;
        }

        /**
         * 各种异常时导致为空时，返回保存的数据
         */
        if (this.entity == null) {
            String json = (RequestCache.get(url, params));
            this.entity = gson.fromJson(json, templateEntity.getClass());
            return;
        }
    }

    /**
     * 功能：网络判断
     */
    private boolean isDisconnect() {
        return !NetUtil.isConnect();
    }

    /**
     * 功能：空的实体类不会发布事件
     */
    private BaseEntity convert(BaseEntity entity) {
        if (entity == null) {
            return newEntity;
        } else {
            return entity;
        }
    }
}
