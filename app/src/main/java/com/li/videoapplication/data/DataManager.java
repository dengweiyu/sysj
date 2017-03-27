package com.li.videoapplication.data;

import android.util.Log;

import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.local.ImageDirectoryHelper;
import com.li.videoapplication.data.local.ScreenShotEntity;
import com.li.videoapplication.data.local.ScreenShotHelper;
import com.li.videoapplication.data.local.VideoCaptureHelper;
import com.li.videoapplication.data.model.entity.Advertisement;
import com.li.videoapplication.data.model.response.AdvertisementDto;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.response.*;
import com.li.videoapplication.data.network.Contants;
import com.li.videoapplication.data.network.RequestHelper;
import com.li.videoapplication.data.network.RequestObject;
import com.li.videoapplication.data.network.RequestParams;
import com.li.videoapplication.data.network.RequestService;
import com.li.videoapplication.data.network.RequestUrl;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.data.upload.ImageShareRequestObject;
import com.li.videoapplication.data.upload.ImageShareRequestObject208;
import com.li.videoapplication.data.upload.ImageUploadRequstObject;
import com.li.videoapplication.data.upload.VideoUploadRequestObject;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.BaseResponseEntity;
import com.li.videoapplication.tools.ArrayHelper;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.VersionUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能：数据请求管理类
 */
public class DataManager {

    public static String TAG = DataManager.class.getSimpleName();





    /**
     * 分享页面广场信息
     */
    public static void sharePlayerSquare() {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().sharePlayerSquare();
        RequestObject request = new RequestObject(Contants.TYPE_GET, url, null, null);
        request.setEntity(new ShareSquareEntity());
        helper.doService(request);
    }

    /* ############## 下载 ############## */

    /**
     * 更多精彩（内部）（同步）
     */
    public static GetDownloadAppEntity getDownloadAppSync() {

        RequestHelper helper = new RequestHelper<>();
        String url = RequestUrl.getInstance().getDownloadApp();
        Map<String, Object> params = RequestParams.getInstance().advertisementAdLocation204("a_sysj");
        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new GetDownloadAppEntity());
        return (GetDownloadAppEntity) helper.postEntity(request);
    }

    /**
     * 更多精彩（广告）（同步）
     */
    public static GetDownloadOtherEntity getDownloadOtherSync(String game_id) {

        RequestHelper helper = new RequestHelper<>();
        String url = RequestUrl.getInstance().getDownloadOther();
        Map<String, Object> params = RequestParams.getInstance().gameTagList(game_id);
        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new GetDownloadOtherEntity());
        return (GetDownloadOtherEntity) helper.postEntity(request);
    }

    /**
     * 官方推荐位
     */
    public static void getRecommendCate() {
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getRecommendCate();
        RequestObject request = new RequestObject(Contants.TYPE_GET, url, null, null);
        request.setEntity(new RecommendCateEntity());
        helper.doService(request);
    }

    /** ############## 抽奖 ############## */
    /**
     * 抽奖状态获取接口
     */
    public static void getSweepstakeStatus() {
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getSweepstakeStatus();
        RequestObject request = new RequestObject(Contants.TYPE_GET, url, null, null);
        request.setEntity(new SweepstakeStatusEntity());
        helper.doService(request);
    }

    /** ############## 飞磨游戏 ############## */
    /**
     * 游戏详情
     */
    public static void gameDetail(String gameid) {
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().gameDetail();
        Map<String, Object> params = RequestParams.getInstance().gameDetail(gameid);
        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new GameDetailEntity());
        helper.doService(request);
    }

    /** ############ 标签 ############# */

    /**
     * 视频分享标签
     */
    public static final void gameTagList(String game_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().gameTagList();
        Map<String, Object> params = RequestParams.getInstance().gameTagList(game_id);
        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new GameTagListEntity());
        helper.doService(request);
    }

    /**
     * 功能：记载本地文件
     */
    public static class LOCAL {

        /**
         * 功能：加载截图
         */
        public static void loadScreenShots() {
            ScreenShotHelper helper = new ScreenShotHelper();
            helper.loadScreenShots();
        }

        /**
         * 功能：加载本地视频
         */
        public static void loadVideoCaptures() {
            VideoCaptureHelper helper = new VideoCaptureHelper();
            helper.loadVideoCaptures();
        }

        /**
         * 功能：验证并加载本地视频
         */
        public static void checkVideoCaptures() {
            VideoCaptureHelper helper = new VideoCaptureHelper();
            helper.checkVideoCaptures();
        }

        /**
         * 功能：验证并加载本地视频(自定义条数)
         */
        public static void checkVideoCaptures(int pageSize, int pageIndex) {
            VideoCaptureHelper helper = new VideoCaptureHelper();
            helper.checkAndLoadVideoCaptures(pageSize, pageIndex);
        }

        /**
         * 功能：导入外部视频
         */
        public static void importVideoCaptures() {
            VideoCaptureHelper helper = new VideoCaptureHelper();
            helper.importVideoCaptures();
        }

        /**
         * 功能：加载图片文件夹
         */
        public static void loadImageDirectorys() {
            ImageDirectoryHelper helper = new ImageDirectoryHelper();
            helper.loadImageDirectorys();
        }
    }

    /**
     * 功能：做任务
     */
    public static class TASK {

        private static String TASK_11 = "11";

        private static String TASK_12 = "12";

        private static String TASK_15 = "15";

        /**
         * 完善个人资料
         */
        private static String TASK_16 = "16";

        /**
         * 推广手游视界APP
         */
        private static String TASK_17 = "17";

        private static String TASK_18 = "18";

        /**
         * 欣赏他人
         */
        private static String TASK_19 = "19";

        /**
         * 珍品典藏
         */
        private static String TASK_20 = "20";

        /**
         * 无私赞美
         */
        private static String TASK_21 = "21";

        /**
         * 缤分享客
         */
        private static String TASK_22 = "22";

        /**
         * 功能：做任务201
         */
        private static void doTask201(String task_id, String member_id) {

            RequestHelper mDataHelper = new RequestHelper();
            String url = RequestUrl.getInstance().doTask201();
            Map<String, Object> params = RequestParams.getInstance().doTask201(task_id, member_id);

            RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
            request.setEntity(new DoTask201Entity());
            mDataHelper.doNetwork(request);
        }

        /**
         * 功能：做任务203
         */
        private static void doTask203(String task_id, String member_id) {

            RequestHelper mDataHelper = new RequestHelper();
            String url = RequestUrl.getInstance().doTask203();
            Map<String, Object> params = RequestParams.getInstance().doTask203(task_id, member_id);

            RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
            request.setEntity(new DoTask203Entity());
            mDataHelper.doNetwork(request);
        }

        public static void doTask_15(String member_id) {
            doTask203(TASK_15, member_id);
        }

        /**
         * 功能：完善个人资料
         */
        public static void doTask_16(String member_id) {
            doTask203(TASK_16, member_id);
        }

        /**
         * 功能：推广手游视界APP
         */
        public static void doTask_17(String member_id) {
            doTask203(TASK_17, member_id);
        }

        public static void doTask_11(String member_id) {
            doTask203(TASK_11, member_id);
        }

        public static void doTask_12(String member_id) {
            doTask203(TASK_12, member_id);
        }

        public static void doTask_18(String member_id) {
            doTask203(TASK_18, member_id);
        }

        /**
         * 功能：珍品典藏
         */
        public static void doTask_20(String member_id) {
            doTask203(TASK_20, member_id);
        }

        public static void doTask_21(String member_id) {
            doTask203(TASK_21, member_id);
        }

        /**
         * 功能：欣赏他人
         */
        public static void doTask_19(String member_id) {
            doTask203(TASK_19, member_id);
        }

        /**
         * 功能：缤分享客
         */
        public static void doTask_22(String member_id) {
            doTask203(TASK_22, member_id);
        }

        /**
         * 功能：视频播放数+1
         */
        public static void videoClickVideo201(String video_id, String member_id) {

            RequestHelper helper = new RequestHelper();
            String url = RequestUrl.getInstance().videoClickVideo201();
            Map<String, Object> params = RequestParams.getInstance().videoClickVideo201(video_id, member_id);

            RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
            request.setEntity(new VideoClickVideo201Entity());
            helper.doNetwork(request);
        }

        /**
         * 功能：视频分享数+1
         */
        public static void videoShareVideo211(String video_id, String member_id) {

            RequestHelper helper = new RequestHelper();
            String url = RequestUrl.getInstance().videoShare211();
            Map<String, Object> params = RequestParams.getInstance().videoClickVideo201(video_id, member_id);

            RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
            request.setEntity(new VideoShareVideo211Entity());
            helper.doNetwork(request);
        }
    }

    /**
     * 功能：游戏下载数+1
     *
     * @param game_id    下载的游戏ID
     * @param member_id
     * @param location   点击下载的位置：1=>游戏圈子、2=>视频页、3=>赛事
     * @param involve_id 涉及ID，例如在哪个视频下点击的，记录的就是该视频的ID，或是赛事ID、圈子ID
     */
    public static void downloadClick217(String game_id, String member_id,
                                        int location, String involve_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().downloadClick217();
        Map<String, Object> params = RequestParams.getInstance().downloadClick217(game_id, "a_sysj",
                member_id, location, involve_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new DownloadClickEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：上传图片，视频
     */
    public static class UPLOAD {

        /**
         * 功能：上传图片服务208
         */
        public static void uploadImage208(String match_id, String member_id, String title, List<ScreenShotEntity> files) {

            ImageShareRequestObject208 request = new ImageShareRequestObject208(match_id, member_id, title, files);
            try {
                RequestService.startRequestService();
                RequestService.postImageShareEvent208(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 功能：上传图片服务204
         */
        public static void uploadImage204(String pk_id, String member_id, List<ScreenShotEntity> files, String team_id) {

            ImageUploadRequstObject request = new ImageUploadRequstObject(pk_id, member_id, files, team_id);
            try {
                RequestService.startRequestService();
                RequestService.postImageUploadEvent(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 功能：上传图片服务
         */
        public static void uploadImage(String game_id,
                                       String member_id,
                                       String title,
                                       String description,
                                       List<ScreenShotEntity> files) {

            ImageShareRequestObject request = new ImageShareRequestObject(game_id,
                    member_id,
                    title,
                    description,
                    files);
            try {
                RequestService.startRequestService();
                RequestService.postImageShareEvent(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 功能：图片上传回调（POST）208
         */
        public static SavePhoto208Entity savePhoto208(String member_id,
                                                      String match_id,
                                                      List<String> keys,
                                                      String title,
                                                      String description) {

            String key = ArrayHelper.list2Array(keys);
            RequestHelper helper = new RequestHelper();
            String url = RequestUrl.getInstance().savePhoto208();
            Map<String, Object> params = RequestParams.getInstance().savePhoto208(member_id,
                    match_id, key, title, description);

            RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
            request.setEntity(new SavePhoto208Entity());
            return (SavePhoto208Entity) helper.postEntity(request);
        }

        /**
         * 功能：图片上传回调（POST）
         */
        public static PhotoSavePhotoEntity photoSavePhoto(String member_id,
                                                          String game_id,
                                                          List<String> keys,
                                                          String title,
                                                          String description) {

            String key = ArrayHelper.list2Array(keys);
            RequestHelper helper = new RequestHelper();
            String url = RequestUrl.getInstance().photoSavePhoto();
            Map<String, Object> params = RequestParams.getInstance().photoSavePhoto(member_id,
                    game_id, key, title, description);

            RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
            request.setEntity(new PhotoSavePhotoEntity());
            PhotoSavePhotoEntity entity = (PhotoSavePhotoEntity) helper.postEntity(request);
            return entity;
        }

        /**
         * 功能：图片上传回调204（POST）
         */
        public static SaveEventResult204Entity saveEventResult204(String pk_id, List<String> pic_keys, String team_id) {

            String key = ArrayHelper.list2Array(pic_keys);
            RequestHelper helper = new RequestHelper();
            String url = RequestUrl.getInstance().saveEventResult204();
            Map<String, Object> params = RequestParams.getInstance().saveEventResult204(pk_id, key, team_id);

            RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
            request.setEntity(new SaveEventResult204Entity());
            SaveEventResult204Entity entity = (SaveEventResult204Entity) helper.postEntity(request);
            return entity;
        }

        /**
         * 功能：图片上传凭证208（POST）
         */
        public static RetPhotoKeyAndToken208Entity retPhotoKeyAndToken208(String member_id, String title, String length) {

            RequestHelper helper = new RequestHelper();
            String url = RequestUrl.getInstance().retPhotoKeyAndToken208();
            Map<String, Object> params = RequestParams.getInstance().photoRetPhotoNameAndToken(member_id, title, length);

            RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
            request.setEntity(new RetPhotoKeyAndToken208Entity());
            return (RetPhotoKeyAndToken208Entity) helper.postEntity(request);
        }

        /**
         * 功能：图片上传凭证204（POST）
         */
        public static RetPhotoKeyAndToken204Entity retPhotoKeyAndToken204(String member_id, String length) {

            RequestHelper helper = new RequestHelper();
            String url = RequestUrl.getInstance().retPhotoKeyAndToken204();
            Map<String, Object> params = RequestParams.getInstance().retPhotoKeyAndToken204(member_id, length);

            RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
            request.setEntity(new RetPhotoKeyAndToken204Entity());
            RetPhotoKeyAndToken204Entity entity = (RetPhotoKeyAndToken204Entity) helper.postEntity(request);
            return entity;
        }

        /**
         * 功能：图片上传凭证（POST）
         */
        public static PhotoRetPhotoNameAndTokenEntity photoRetPhotoNameAndToken(String member_id,
                                                                                String title,
                                                                                String length) {

            RequestHelper helper = new RequestHelper();
            String url = RequestUrl.getInstance().photoRetPhotoNameAndToken();
            Map<String, Object> params = RequestParams.getInstance().photoRetPhotoNameAndToken(member_id, title, length);

            RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
            request.setEntity(new PhotoRetPhotoNameAndTokenEntity());
            PhotoRetPhotoNameAndTokenEntity entity = (PhotoRetPhotoNameAndTokenEntity) helper.postEntity(request);
            return entity;
        }

        /**
         * 上传视频服务210
         */
        public static final void startVideo(String shareChannel,
                                            String member_id,
                                            String title,
                                            String game_id,
                                            String match_id,
                                            String description,
                                            int isofficial,
                                            List<String> game_tags,
                                            VideoCaptureEntity data,
                                            String goods_id) {

            VideoUploadRequestObject request = new VideoUploadRequestObject(VideoUploadRequestObject.STATUS_START,
                    shareChannel,
                    member_id,
                    title,
                    game_id,
                    match_id,
                    description,
                    isofficial,
                    game_tags,
                    data,
                    goods_id);

            try {
                RequestService.startRequestService();
                RequestService.postVideoShareEvent(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 功能：继续上传视频服务210
         */
        public static void resumeVideo210(VideoCaptureEntity entity) {
            VideoUploadRequestObject request = new VideoUploadRequestObject(VideoUploadRequestObject.STATUS_RESUME,
                    entity);
            try {
                RequestService.startRequestService();
                RequestService.postVideoShareEvent(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 功能：暂停上传视频服务210
         */
        public static void pauseVideo210() {
            VideoUploadRequestObject request = new VideoUploadRequestObject(VideoUploadRequestObject.STATUS_PAUSE);
            try {
                RequestService.startRequestService();
                RequestService.postVideoShareEvent(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 视频上传凭证（同步）208
         */
        public static final DoVideoMarkEntity doVideoMarkSync208(String member_id,
                                                                 String video_title,
                                                                 String game_id,
                                                                 String width,
                                                                 String height,
                                                                 String time_length,
                                                                 String match_id,
                                                                 String description,
                                                                 int isofficial,
                                                                 List<String> game_tags) {

            String a = "";
            if (game_tags != null && game_tags.size() > 0)
                a = ArrayHelper.list2Array(game_tags);

            RequestHelper helper = new RequestHelper();
            String url = RequestUrl.getInstance().doVideoMark();
            Map<String, Object> params = RequestParams.getInstance().doVideoMark(member_id,
                    video_title,
                    game_id,
                    width,
                    height,
                    time_length,
                    "1",

                    match_id,
                    description,
                    isofficial,
                    a);
            RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
            request.setEntity(new DoVideoMarkEntity());
            DoVideoMarkEntity entity = (DoVideoMarkEntity) helper.postEntity(request);
            return entity;
        }

        /**
         * 视频上传回调（同步）208
         */
        public static final QiniuTokenPassEntity qiniuTokenPassSync(String video_id,
                                                                    String game_id,
                                                                    String is_success,
                                                                    String member_id,
                                                                    String join_id) {

            RequestHelper helper = new RequestHelper();
            String url = RequestUrl.getInstance().qiniuTokenPass();
            Map<String, Object> params = RequestParams.getInstance().qiniuTokenPass(video_id,
                    game_id,
                    is_success,
                    member_id,
                    join_id);
            RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
            request.setEntity(new QiniuTokenPassEntity());
            QiniuTokenPassEntity entity = (QiniuTokenPassEntity) helper.postEntity(request);
            return entity;
        }
    }

    /**
     * 功能：弹幕
     */
    public static class DANMUKU {

        /**
         * 功能：弹幕列表
         */
        public static void bulletList203(String video_id) {

            RequestHelper helper = new RequestHelper();
            String url = RequestUrl.getInstance().bulletList203();
            Map<String, Object> params = RequestParams.getInstance().bulletList203(video_id);
            RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
            request.setEntity(new BulletList203Entity());
            helper.doNetwork(request);
        }

        /**
         * 功能：弹幕发射/评论
         */
        public static void bulletDo203(BaseResponseEntity entity,
                                       String video_id,
                                       String video_node,
                                       String member_id,
                                       String content,
                                       String bullet,
                                       String mark,
                                       String comment_id) {

            RequestHelper helper = new RequestHelper();
            String url = RequestUrl.getInstance().bulletDo203();
            Map<String, Object> params = RequestParams.getInstance().bulletDo203(video_id,
                    video_node,
                    member_id,
                    content,
                    bullet,
                    mark,
                    comment_id);
            RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
            request.setEntity(entity);
            helper.doNetwork(request);
        }

        /**
         * 功能：弹幕发射
         */
        public static void bulletDo203Bullet2Video(String video_id,
                                                   String node,
                                                   String member_id,
                                                   String content) {
            bulletDo203(new BulletDo203Bullet2VideoEntity(),
                    video_id,
                    node,
                    member_id,
                    content,
                    "1",
                    "0",
                    "");
        }

        /**
         * 功能：多级弹幕发射
         */
        public static void bulletDo203Bullet2Bullet(String video_id,
                                                    String node,
                                                    String member_id,
                                                    String content,
                                                    String comment_id) {
            bulletDo203(new BulletDo203Bullet2BulletEntity(),
                    video_id,
                    node,
                    member_id,
                    content,
                    "1",
                    "1",
                    comment_id);
        }

        /**
         * 功能：评论
         */
        public static void bulletDo203Comment2Video(String video_id,
                                                    String member_id,
                                                    String content) {
            bulletDo203(new BulletDo203Comment2VideoEntity(),
                    video_id,
                    "",
                    member_id,
                    content,
                    "0",
                    "0",
                    "");
        }

        /**
         * 功能：多级评论
         */
        public static void bulletDo203Comment2Comment(String video_id,
                                                      String member_id,
                                                      String content,
                                                      String comment_id) {
            bulletDo203(new BulletDo203Comment2CommentEntity(),
                    video_id,
                    "",
                    member_id,
                    content,
                    "0",
                    "1",
                    comment_id);
        }
    }

    /**
     * 功能：字幕
     */
    public static class SUBTITLE {

        /**
         * 功能：下載字幕
         */
        public static void download(String url) {
            RequestHelper helper = new RequestHelper();
            RequestObject request = new RequestObject(Contants.TYPE_DOWNLOAD, url, null, null);
            request.setEntity(null);
            helper.doNetwork(request);
        }

        /**
         * 功能：上传字幕
         */
        public static void srtUpload203(String video_id) {

            RequestHelper helper = new RequestHelper();
            String url = RequestUrl.getInstance().srtUpload203();
            Map<String, Object> params = RequestParams.getInstance().srtUpload203(video_id);
            RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
            request.setEntity(new SrtUpload203Entity());
            helper.doNetwork(request);
        }

        /**
         * 功能：获取字幕
         */
        public static void srtList203(String video_id) {

            RequestHelper helper = new RequestHelper();
            String url = RequestUrl.getInstance().srtList203();
            Map<String, Object> params = RequestParams.getInstance().srtList203(video_id);
            RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
            request.setEntity(new SrtList203Entity());
            helper.doNetwork(request);
        }
    }

    /**
     * 功能：视频评论删除接口
     */
    public static void commentDel(String member_id, String comment_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().commentDel();
        Map<String, Object> params = RequestParams.getInstance().commentDel(member_id, comment_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new CommentDelEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：活动评论208
     */
    public static void sendComment208(String match_id, String member_id, String content) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().sendComment208();
        Map<String, Object> params = RequestParams.getInstance().sendComment208(match_id, member_id, content);

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(new SendComment208Entity());
        helper.doNetwork(request);
    }


    /**************************************/

    /**
     * 功能：举报类型列表
     */
    public static void reportType() {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().reportType();

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, null, null);
        request.setEntity(new ReportTypeEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：举报
     */
    public static void report(String video_id, String member_id, String description, String type_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().report();
        Map<String, Object> params = RequestParams.getInstance().report(video_id, member_id, description, type_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new ReportEntity());
        helper.doNetwork(request);
    }

    /** ############ 首页 ############# */

    /**
     * 功能：统计被邀请用户打开app的次数（异步）
     */
    public static void statisticalOpenAppAsync(String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().statisticalOpenApp();
        Map<String, Object> params = RequestParams.getInstance().statisticalOpenApp(member_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new StatisticalOpenAppEntity());
        helper.doExecutor(request);
    }

    /**
     * 功能：首页专栏
     */
    public static void appIndexList(int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().appIndexList();

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, null, null);
        request.setEntity(new AppIndexListEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：首页統一接口
     */
    public static void indexIndex(int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().indexIndex();
        Map<String, Object> params = RequestParams.getInstance().indexIndex(page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setA(1);
        request.setEntity(new IndexIndexEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：首页統一接口（异步）
     */
    public static void indexIndexAsync(int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().indexIndex();
        Map<String, Object> params = RequestParams.getInstance().indexIndex(page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new IndexIndexEntity());
        helper.doExecutor(request);
    }

    /**
     * 功能：首页接口
     */
    public static void indexIndex204(int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().indexIndex204();
        Map<String, Object> params = RequestParams.getInstance().indexIndex204(page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setA(1);
        request.setEntity(new IndexIndex204Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：首页接口（异步）
     */
    public static void indexIndex204Async(int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().indexIndex204();
        Map<String, Object> params = RequestParams.getInstance().indexIndex204(page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new IndexIndex204Entity());
        helper.doExecutor(request);
    }

    /**
     * 功能：首页更多（最热）
     */
    public static void indexIndexMoreHot(String more_mark, String member_id, int page) {
        DataManager.indexIndexMore(new IndexIndexMoreHotEntity(), more_mark, member_id, "click", page);
    }

    /**
     * 功能：首页更多（最新）
     */
    public static void indexIndexMoreNew(String more_mark, String member_id, int page) {
        DataManager.indexIndexMore(new IndexIndexMoreNewEntity(), more_mark, member_id, "time", page);
    }

    /**
     * 功能：首页更多
     */
    private static void indexIndexMore(BaseResponseEntity entity, String more_mark, String member_id, String sort, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().indexIndexMore();
        Map<String, Object> params = RequestParams.getInstance().indexIndexMore(more_mark, member_id, sort, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(entity);
        helper.doNetwork(request);
    }

    /**
     * 功能：首页更多204（最热）
     */
    public static void indexIndexMore217Hot(String more_mark, String member_id, int page) {
        DataManager.indexIndexMore217(new IndexIndexMore204HotEntity(), more_mark, member_id, "click", page);
    }

    /**
     * 功能：首页更多204（最新）
     */
    public static void indexIndexMore217New(String more_mark, String member_id, int page) {
        DataManager.indexIndexMore217(new IndexIndexMore204NewEntity(), more_mark, member_id, "time", page);
    }

    /**
     * 功能：首页更多217
     */
    private static void indexIndexMore217(BaseResponseEntity entity, String more_mark, String member_id, String sort, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().indexIndexMore217();
        Map<String, Object> params = RequestParams.getInstance().indexIndexMore204(more_mark, member_id, sort, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(entity);
        helper.doNetwork(request);
    }

    /**
     * 功能：问卷
     */
    public static void indexDoSurvey(String member_id, String group_ids) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().indexDoSurvey();
        Map<String, Object> params = RequestParams.getInstance().indexDoSurvey(member_id, group_ids);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new IndexDoSurveyEntity());
        helper.doNetwork(request);
    }


    /**
     * 功能：播放页推荐视频详情
     */
    public static void changeVideo208(String video_ids) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().changeVideo208();
        Map<String, Object> params = RequestParams.getInstance().indexChangeGuessSecond(video_ids);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new ChangeVideo208Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：首页猜你喜歡
     */
    public static void indexChangeGuess(String group_ids) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().indexChangeGuess();
        Map<String, Object> params = RequestParams.getInstance().indexChangeGuess(group_ids);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new ChangeGuessEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：首页猜你喜歡详情
     */
    public static void indexChangeGuessSecond(String video_ids) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().indexChangeGuessSecond();
        Map<String, Object> params = RequestParams.getInstance().indexChangeGuessSecond(video_ids);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new IndexChangeGuessSecondEntity());
        helper.doNetwork(request);
    }

    /** ############ 广告 ############# */

    /**
     * 广告
     */
    public static final class ADVERTISEMENT {

        /**
         * 启动海报（录屏大师）
         */
        public static final int ADVERTISEMENT_1 = 1;

        /**
         * 视频管理-本地视频
         */
        public static final int ADVERTISEMENT_2 = 2;

        /**
         * 视频管理-云端视频
         */
        public static final int ADVERTISEMENT_3 = 3;

        /**
         * 视频管理-截图
         */
        public static final int ADVERTISEMENT_4 = 4;

        /**
         * 设置-更多精彩
         */
        public static final int ADVERTISEMENT_5 = 5;

        /**
         * 启动海报（手游世界）
         */
        public static final int ADVERTISEMENT_6 = 6;

        /**
         * 首页-第四副轮播
         */
        public static final int ADVERTISEMENT_7 = 7;

        /**
         * 首页-热门游戏下方通栏
         */
        public static final int ADVERTISEMENT_8 = 8;

        /**
         * 福利-活动第二位置
         */
        public static final int ADVERTISEMENT_9 = 9;

        /**
         * 首页-热门游戏下方通栏（M站）
         */
        public static final int ADVERTISEMENT_10 = 10;

        public static final void advertisement_1() {
            Advertisement entity = PreferencesHepler.getInstance().getAdvertisement_1();
            int time = 0;
            if (entity != null)
                time = entity.getChangetime();
            DataManager.advertisementAdImage204(ADVERTISEMENT_1, time);
        }

        public static final void advertisement_2() {
            Advertisement entity = PreferencesHepler.getInstance().getAdvertisement_2();
            int time = 0;
            if (entity != null)
                time = entity.getChangetime();
            DataManager.advertisementAdImage204(ADVERTISEMENT_2, time);
        }

        public static final void advertisement_3() {
            Advertisement entity = PreferencesHepler.getInstance().getAdvertisement_3();
            int time = 0;
            if (entity != null)
                time = entity.getChangetime();
            DataManager.advertisementAdImage204(ADVERTISEMENT_3, time);
        }

        public static final void advertisement_4() {
            Advertisement entity = PreferencesHepler.getInstance().getAdvertisement_4();
            int time = 0;
            if (entity != null)
                time = entity.getChangetime();
            DataManager.advertisementAdImage204(ADVERTISEMENT_4, time);
        }

        public static final void advertisement_5() {
            Advertisement entity = PreferencesHepler.getInstance().getAdvertisement_5();
            int time = 0;
            if (entity != null)
                time = entity.getChangetime();
            DataManager.advertisementAdImage204(ADVERTISEMENT_5, time);
        }

        public static final void advertisement_6() {
            Advertisement entity = PreferencesHepler.getInstance().getAdvertisement_6();
            int time = 0;
            if (entity != null)
                time = entity.getChangetime();
            DataManager.advertisementAdImage204(ADVERTISEMENT_6, time);
        }

        public static final void advertisement_7() {
            Advertisement entity = PreferencesHepler.getInstance().getAdvertisement_7();
            int time = 0;
            if (entity != null)
                time = entity.getChangetime();
            DataManager.advertisementAdImage204(ADVERTISEMENT_7, time);
        }

        public static final void advertisement_8() {
            Advertisement entity = PreferencesHepler.getInstance().getAdvertisement_8();
            int time = 0;
            if (entity != null)
                time = entity.getChangetime();
            DataManager.advertisementAdImage204(ADVERTISEMENT_8, time);
        }

        public static final void advertisement_9() {
            Advertisement entity = PreferencesHepler.getInstance().getAdvertisement_9();
            int time = 0;
            if (entity != null)
                time = entity.getChangetime();
            DataManager.advertisementAdImage204(ADVERTISEMENT_9, time);
        }

        public static final void advertisement_10() {
            Advertisement entity = PreferencesHepler.getInstance().getAdvertisement_10();
            int time = 0;
            if (entity != null)
                time = entity.getChangetime();
            DataManager.advertisementAdImage204(ADVERTISEMENT_10, time);
        }
    }

    /**
     * 功能：启动图广告
     */
    public static AdvertisementDto adverImageSync(int location_id) {
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().adverImage();
        Map<String, Object> params = RequestParams.getInstance().adverImage(location_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new AdvertisementDto());
        return (AdvertisementDto) helper.postEntity(request);
    }

    /**
     * 广告位置列表
     */
    public static final void advertisementAdLocation204() {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().advertisementAdLocation204();
        Map<String, Object> params = RequestParams.getInstance().advertisementAdLocation204("lpds");

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new AdvertisementAdLocation204Entity());
        helper.doNetwork(request);
    }

    /**
     * 广告位置列表
     */
    public static final void advertisementAdLocation204Asnyc() {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().advertisementAdLocation204();
        Map<String, Object> params = RequestParams.getInstance().advertisementAdLocation204("lpds");

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new AdvertisementAdLocation204Entity());
        helper.doExecutor(request);
    }

    /**
     * 位置广告图集
     */
    public static final void advertisementAdImage204(long location_id, int time) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().advertisementAdImage204();
        Map<String, Object> params = RequestParams.getInstance().advertisementAdImage204(location_id, time);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new AdvertisementAdImage204Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：图片广告
     */
    public static void indexLaunchImageAsync(int time) {
        Log.d(TAG, "indexLaunchImage: ");
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().indexLaunchImage();
        Map<String, Object> params = RequestParams.getInstance().indexLaunchImage(AppConstant.SYSJ_ANDROID, time);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setA(1);
        request.setEntity(new IndexLaunchImageEntity());
        helper.doExecutor(request);
    }

    /** ############ 搜索 ############# */

    /**
     * 功能：搜索关键字(v1.1.5接口)
     */
    public static void keyWordListNew() {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().keyWordListNew();
        Map<String, Object> params = RequestParams.getInstance().keyWordListNew(9);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new KeyWordListNewEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：搜索联想词
     */
    public static void associate(String keyWord) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().associate();
        Map<String, Object> params = RequestParams.getInstance().associate(keyWord);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new AssociateEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：搜索联想词201
     */
    public static void associate201(String classType, String keyWord) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().associate201();
        Map<String, Object> params = RequestParams.getInstance().associate201(classType, keyWord);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new Associate201Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：搜索联想词201
     */
    public static void associate210(String classType, String keyWord) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().associate210();
        Map<String, Object> params = RequestParams.getInstance().associate201(classType, keyWord);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new Associate201Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：视频搜索
     */
    private static void searchVideo(BaseResponseEntity entity, String name, String sort, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().searchVideo();
        Map<String, Object> params = RequestParams.getInstance().searchVideo(name, sort, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(entity);
        helper.doNetwork(request);
    }

    /**
     * 功能：视频搜索（最新）
     */
    public static void searchVideoNew(String name, int page) {
        DataManager.searchVideo(new SearchVideoNewEntity(), name, "time", page);
    }

    /**
     * 功能：视频搜索（最热）
     */
    public static void searchVideoHot(String name, int page) {
        DataManager.searchVideo(new SearchVideoHotEntity(), name, "flower", page);
    }

    /**
     * 功能：搜索礼包
     */
    public static void searchPackage(String name, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().searchPackage();
        Map<String, Object> params = RequestParams.getInstance().searchPackage(name, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new SearchPackageEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：搜索玩家
     */
    public static void searchMember(String name, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().searchMember();
        Map<String, Object> params = RequestParams.getInstance().searchMember(name, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new SearchMemberEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：搜索视频203
     */
    public static void searchVideo203(String keyword, String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().searchVideo203();
        Map<String, Object> params = RequestParams.getInstance().searchVideo203(keyword, member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new SearchVideo203Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：搜索游戏203
     */
    public static void searchGame203(String keyword, String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().searchGame203();
        Map<String, Object> params = RequestParams.getInstance().searchGame203(keyword, member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new SearchGame203Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：搜索玩家203
     */
    public static void searchMember203(String keyword, String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().searchMember203();
        Map<String, Object> params = RequestParams.getInstance().searchMember203(keyword, member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new SearchMember203Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：搜索203
     */
    public static void searchPackage203(String keyword, String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().searchPackage203();
        Map<String, Object> params = RequestParams.getInstance().searchPackage203(keyword, member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new SearchPackage203Entity());
        helper.doNetwork(request);
    }

    /** ############ ICP 统计相关接口 ############# */

    /**
     * 功能：用户注册、登录记录
     */
    public static void sign(String member_id, String member_nickname, String sign_type, String sign_entrance,
                            String sign_ip, String sign_hardwarecode, String imei, String time) {
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().sign();
        Map<String, Object> params = RequestParams.getInstance().sign(member_id, member_nickname,
                sign_type, sign_entrance, sign_ip, sign_hardwarecode, imei, time);

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(new SignEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：用户内容及言论入口，IP等行为
     */
    public static void userdatabehavior(String member_id, String video_id, String pic_id,
                                        String video_comment_id, String entrance, String ip,
                                        String hardwarecode, String imei, String time) {
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().userdatabehavior();
        Map<String, Object> params = RequestParams.getInstance().userdatabehavior(member_id, video_id,
                pic_id, video_comment_id, entrance, ip, hardwarecode, imei, time);

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(new UserdatabehaviorEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：个人资料过滤敏感词
     */
    public static void baseInfo(String word) {
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().baseInfo();
        Map<String, Object> params = RequestParams.getInstance().baseInfo(word);

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(new BaseInfoEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：个人资料过滤敏感词
     */
    public static void baseInfo(String word, BaseInfoEntity entity) {
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().baseInfo();
        Map<String, Object> params = RequestParams.getInstance().baseInfo(word);

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(entity);
        helper.doNetwork(request);
    }

    /**
     * 功能：检测用户昵称重复接口
     */
    public static void isRepeat(String nickname) {
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().isRepeat();
        Map<String, Object> params = RequestParams.getInstance().isRepeat(nickname);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new IsRepeatEntity());
        helper.doNetwork(request);
    }

    /** ############ 投屏 ############# */

    /**
     * 功能：乐播产品下载地址接口
     */
    public static void getLeboDownloadLink() {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getLeboDownloadLink();

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, null, null);
        request.setEntity(new getLeboDownloadEntity());
        helper.doNetwork(request);
    }

    /**
     * ############ 版本审核 #############
     */

    public static void checkAndroidStatus(int version, String channel) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().checkAndroidStatus();
        Map<String, Object> params = RequestParams.getInstance().checkAndroidStatus(version, channel);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new CheckAndroidStatusEntity());
        helper.doExecutor(request);
    }

    /** ############ 货币商城211 ############# */

    /**
     * 功能：首页任务
     */
    public static void unfinishedTask(String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().unfinishedTask();
        Map<String, Object> params = RequestParams.getInstance().getOrderList(member_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new UnfinishedTaskEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：推荐位
     */
    public static void recommendedLocation(String member_id, RecommendedLocationEntity callbackEntity) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().recommendedLocation();
        Map<String, Object> params = RequestParams.getInstance().getOrderList(member_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(callbackEntity);
        helper.doNetwork(request);
    }

    /**
     * 功能：会员任务接口
     */
    public static void getMemberTask(String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getMemberTask();
        Map<String, Object> params = RequestParams.getInstance().getOrderList(member_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new MemberTaskEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：兑换
     */
    public static void payment(String member_id, String goods_id, String mobile) {
        payment(member_id, goods_id, mobile, null);
    }

    /**
     * 功能：兑换
     */
    public static void payment(String member_id, String goods_id, String mobile, String account) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().payment();
        Map<String, Object> params = RequestParams.getInstance().payment(member_id, goods_id, mobile, account);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new PaymentEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：兑换记录
     */
    public static void goodsDetail(String goods_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().goodsDetail();
        Map<String, Object> params = RequestParams.getInstance().goodsDetail(goods_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new GoodsDetailEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：订单详情
     */
    public static void orderDetail(String member_id, String order_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().orderDetail();
        Map<String, Object> params = RequestParams.getInstance().orderDetail(member_id, order_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new OrderDetailEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：获取验证码211
     */
    public static void sendCode(String mobile) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().sendCode();
        String target = "sysj";
        Map<String, Object> params = RequestParams.getInstance().sendCode(mobile, target);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new PhoneRequestMsgEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：兑换记录
     */
    public static void getOrderList(String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getOrderList();
        Map<String, Object> params = RequestParams.getInstance().getOrderList(member_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new OrderListEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：商品列表
     */
    public static void getGoodsList() {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().goodsList();

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, null, null);
        request.setEntity(new GoodsListEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：个人飞磨豆数量
     */
    public static void getMemberCurrency(String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getMemberCurrency();

        Map<String, Object> params = RequestParams.getInstance().getCurrencyRecord(member_id);
        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new MemberCurrencyEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：账单
     */
    public static void getCurrencyRecord(String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getCurrencyRecord();
        Map<String, Object> params = RequestParams.getInstance().getCurrencyRecord(member_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new CurrencyRecordEntity());
        helper.doNetwork(request);
    }

    /** ############ 赛事 ############# */
    /**
     * 功能：赛事上传视频成功后调用接口
     */
    public static void saveEventVideo(String team_id, String pk_id, String video_id,
                                      String event_id, String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().saveEventVideo();
        Map<String, Object> params = RequestParams.getInstance().saveEventVideo(team_id, pk_id,
                video_id, event_id, member_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new SaveEventVideoEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：获取赛事报名问号内容
     */
    public static void getEventMsg() {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getEventMsg();

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, null, null);
        request.setEntity(new GetEventMsgEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：获取赛事参赛队员人数接口
     */
    public static void getTeamMemberNumber(String event_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getTeamMemberNumber();
        Map<String, Object> params = RequestParams.getInstance().eventsSchedule204(event_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new GetTeamMemberNumEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：赛事胜利者弹出框判断第一次接口
     */
    public static void setAlert(String member_id, String schedule_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().setAlert();
        Map<String, Object> params = RequestParams.getInstance().setAlert(member_id, schedule_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new SetAlertEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：个人中心绑定手机号请求验证码, 视界商场兑换 （216 加密）
     */
    public static void phoneRequestMsg(String key) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().phoneRequestMsg();
        String target = "a_sysj";
        Map<String, Object> params = RequestParams.getInstance().msgRequestNew(key, target);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new PhoneRequestMsgEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：报名时获取验证码(216 加密）
     */
    public static void eventRequestMsg(String key, String title) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().eventRequestMsg();
        String target = "a_sysj";
        Map<String, Object> params = RequestParams.getInstance().eventRequestMsg(key, target, title);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new PhoneRequestMsgEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：群组名称
     */
    public static void groupName208(String chatroom_group_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().groupName208();
        Map<String, Object> params = RequestParams.getInstance().groupName208(chatroom_group_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new GroupName208Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：群组名称
     */
    public static GroupName208Entity groupName208Sync(String chatroom_group_id) {

        RequestHelper helper = new RequestHelper<>();
        String url = RequestUrl.getInstance().groupName208();
        Map<String, Object> params = RequestParams.getInstance().groupName208(chatroom_group_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new GroupName208Entity());
        GroupName208Entity entity = (GroupName208Entity) helper.postEntity(request);
        return entity;
    }

    /**
     * 功能：获取融云token204
     */
    public static void getRongCloudToken204(String member_id, String nickname) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getRongCloudToken204();
        Map<String, Object> params = RequestParams.getInstance().getRongCloudToken204(member_id, nickname);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new GetRongCloudToken204Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：赛事列表201
     */
    public static void getMatchList201(int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getMatchList201();
        Map<String, Object> params = RequestParams.getInstance().getMatchList201(page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new GetMatchList201Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：下载保存图片
     */
    public static void saveImage(String url) {
        RequestHelper helper = new RequestHelper();
        RequestObject request = new RequestObject(Contants.TYPE_DOWNLOAD, url, null, null);
        request.setEntity(null);
        helper.doService(request);
    }

    /**
     * 功能：赛事签到210=开始匹配
     */
    public static final void signSchedule210(String member_id, String schedule_id) {
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().signSchedule210();
        Map<String, Object> params = RequestParams.getInstance().signSchedule210(member_id, schedule_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new SignSchedule210Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：赛事报名214
     */
    public static void joinEvents(String member_id, String event_id, String type_id,
                                  String team_name, String game_role, String qq,
                                  String phone, String invite_code, String team_member_tel) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().joinEvents();
        Map<String, Object> params = RequestParams.getInstance().joinEvents(member_id,
                event_id, type_id, team_name, game_role, qq, phone, invite_code, team_member_tel);

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(new JoinEventsEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：我的赛事（进行中）210
     */
    public static final void getMemberMatchPK210(String member_id, String event_id) {
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getMemberMatchPK210();
        Map<String, Object> params = RequestParams.getInstance().getMemberMatchPK204(member_id, event_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new MemberMatchPKEntity204());
        helper.doNetwork(request);
    }

    /**
     * 功能：我的赛事（已结束）204
     */
    public static final void getMemberPKList204(String member_id, String event_id) {
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getMemberPKList204();
        Map<String, Object> params = RequestParams.getInstance().getMemberPKList204(member_id, event_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new MemberPKListEntity204());
        helper.doNetwork(request);
    }

    /**
     * 功能：我的赛事某个已结束的赛事
     */
    public static void getMemberEndPKWindow(String member_id, String event_id, String schedule_id) {
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getMemberEndPKWindow();
        Map<String, Object> params = RequestParams.getInstance().getMemberEndPKWindow(member_id, event_id,schedule_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new MemberEndPKInfoEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：客服名称
     */
    public static void getServiceName(String member_id) {
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getServiceName();
        Map<String, Object> params = RequestParams.getInstance().getServiceName(member_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new ServiceName217Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：赛事胜利者弹出框判断第一次接口210
     */
    public static final void eventsSetAlert210(String schedule_id, String member_id) {
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().eventsSetAlert210();
        Map<String, Object> params = RequestParams.getInstance().eventsSetAlert210(schedule_id, member_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new EventsSetAlert210Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：我的赛事列表204
     */
    public static void myEventsList204(String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().myEventsList204();
        Map<String, Object> params = RequestParams.getInstance().myEventsList204(member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new MyEventsList204Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：赛事视频列表
     */
    public static void matchVideoList201(String match_id, String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().matchVideoList201();
        Map<String, Object> params = RequestParams.getInstance().matchVideoList201(match_id, member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new MatchVideoList201Entity());
        helper.doNetwork(request);
    }

    /** ################# 活动 ################# */

    /**
     * 功能：活动纯文本评论点赞
     */
    public static void flowerComment(String member_id, String comment_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().flowerComment();
        Map<String, Object> params = RequestParams.getInstance().commentDel(member_id, comment_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(null);
        helper.doNetwork(request);
    }

    /**
     * 功能：参加活动列表203
     */
    public static void selectMatch203(String game_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().selectMatch203();
        Map<String, Object> params = RequestParams.getInstance().selectMatch203(game_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new SelectMatch203Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：活动详情208
     */
    public static void getMatchInfo208(String match_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getMatchInfo();
        Map<String, Object> params = RequestParams.getInstance().getMatchInfo(match_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new GetMatchInfo208Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：活动tab分页
     */
    public static void getDetailMode(String match_id, String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getDetailMode();
        Map<String, Object> params = RequestParams.getInstance().getDetailMode(match_id, member_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new GetDetailModeEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：活动视频、图文、评论混合列表接口208
     */
    public static void getCompVideoLists208(String member_id, String match_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getCompVideoLists208();
        Map<String, Object> params = RequestParams.getInstance().getCompVideoLists208(member_id, match_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new GetCompVideoLists208Entity());
        helper.doNetwork(request);
    }

    /**
     * 参加活动列表
     */
    public static final void selectMatch(String game_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().selectMatch();
        Map<String, Object> params = RequestParams.getInstance().selectMatch(game_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new SelectMatchEntity());
        helper.doService(request);
    }


    /** ############ 礼包 ############# */

    /**
     * 功能：礼包列表
     */
    public static void packageList(String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().packageList();
        Map<String, Object> params = RequestParams.getInstance().packageList(member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new PackageListEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：礼包详情
     */
    public static void packageInfo(String member_id, String id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().packageInfo();
        Map<String, Object> params = RequestParams.getInstance().packageInfo(member_id, id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new PackageInfoEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：领取礼包
     */
    public static void claimPackage(String member_id, String id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().claimPackage();
        Map<String, Object> params = RequestParams.getInstance().claimPackage(member_id, id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new ClaimPackageEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：圈子礼包203
     */
    public static void gamePackage203(String game_id, String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().gamePackage203();
        Map<String, Object> params = RequestParams.getInstance().gamePackage203(game_id, member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new GamePackage203Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：礼包列表203
     */
    public static void packageList203(String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().packageList203();
        Map<String, Object> params = RequestParams.getInstance().packageList203(member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new PackageList203Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：礼包详情203
     */
    public static void packageInfo203(String member_id, String id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().packageInfo203();
        Map<String, Object> params = RequestParams.getInstance().packageInfo203(member_id, id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new PackageInfo203Entity());
        helper.doNetwork(request);
    }

    /** ############ 注册登录 ############# */

    /**
     * 功能：获取验证码（216 加密）
     */
    public static void msgRequestNew(String key) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().msgRequestCode();
        String target = "a_sysj";
        Map<String, Object> params = RequestParams.getInstance().msgRequestNew(key, target);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new MsgRequestNewEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：提交验证码
     */
    public static void verifyCode(String mobile, String code) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().verifyCode();
        String target = "sysj";
        Map<String, Object> params = RequestParams.getInstance().verifyCode(mobile, code, target);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new VerifyCodeNewEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：提交手机和验证码
     * 参加赛事验证、个人中心验证
     */
    public static void verifyCodeNew(String key, String code) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().verifyCodeNew();
        Map<String, Object> params = RequestParams.getInstance().verifyCodeNew(key, code);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new VerifyCodeNewEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：提交登录
     */
    public static void login(String key) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().login();
        Map<String, Object> params = RequestParams.getInstance().login(key);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new LoginEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：飞磨内部快速登录
     */
    public static void loginFm(String key) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().loginFm();
        Map<String, Object> params = RequestParams.getInstance().login(key);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new LoginEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：第三方登录
     */
    public static void login(String key, String nickname, String name, String sex, String location, String avatar) {
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().login();
        Map<String, Object> params = RequestParams.getInstance().login(key,
                "1",
                nickname,
                name,
                sex,
                location,
                avatar,
                AppConstant.SYSJ_ANDROID);

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(new LoginEntity());
        helper.doNetwork(request);
    }

    /** ############ 用户资料 ############# */

    /**
     * 功能：用户资料（旧接口）
     */
    public static void detailNew(String id, String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().detailNew();
        Map<String, Object> params = RequestParams.getInstance().detailNew(id, member_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new DetailNewEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：修改用户资料（旧接口）
     */
    public static void finishMemberInfo(String id, Map<String, Object> params) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().finishMemberInfo();

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(new FinishMemberInfoEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：个人资料
     */
    public static void userProfilePersonalInformation(String user_id, String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().userProfilePersonalInformation();
        Map<String, Object> params = RequestParams.getInstance().UserProfilePersonalInformation(user_id, member_id);

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setA(1);
        request.setEntity(new UserProfilePersonalInformationEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：个人资料
     */
    public static void userProfilePersonalInformationAsync(String user_id, String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().userProfilePersonalInformation();
        Map<String, Object> params = RequestParams.getInstance().UserProfilePersonalInformation(user_id, member_id);

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setA(1);
        request.setEntity(new UserProfilePersonalInformationEntity());
        helper.doExecutor(request);
    }

    /**
     * 功能：个人资料
     */
    public static UserProfilePersonalInformationEntity userProfilePersonalInformationSync(String user_id, String member_id) {

        RequestHelper helper = new RequestHelper<>();
        String url = RequestUrl.getInstance().userProfilePersonalInformation();
        Map<String, Object> params = RequestParams.getInstance().UserProfilePersonalInformation(user_id, member_id);

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(new UserProfilePersonalInformationEntity());
        UserProfilePersonalInformationEntity entity = (UserProfilePersonalInformationEntity) helper.postEntity(request);
        return entity;
    }

    /**
     * 功能：个人喜欢游戏类型
     */
    public static void userProfileTypeList() {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().userProfileTypeList();

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, null, null);
        request.setEntity(new UserProfileTypeListEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：编辑个人资料
     */
    public static void userProfileFinishMemberInfo(Member member) {

        if (member == null || StringUtil.isNull(member.getId())) {
            throw new NullPointerException();
        }

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().userProfileFinishMemberInfo();
        Map<String, Object> params = RequestParams.getInstance().UserProfileFinishMemberInfo(member.getId(),
                member.getNickname(),
                member.getSignature(),
                member.getDisplay(),
                member.getLike_grouptype(),
                member.getSex(),
                member.getQq(),
                member.getMobile());

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new UserProfileFinishMemberInfoEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：上传头像
     */
    public static void userProfileUploadAvatar(String id, File file) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().userProfileUploadAvatar();
        Map<String, Object> params = RequestParams.getInstance().UserProfileUploadAvatar(id);
        Map<String, File> files = new HashMap<String, File>();
        files.put("avater", file);

        RequestObject request = new RequestObject(Contants.TYPE_UPLOAD, url, params, files);
        request.setEntity(new UserProfileUploadAvatarEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：上传封面
     */
    public static void userProfileUploadCover(String id, File file) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().userProfileUploadCover();
        Map<String, Object> params = RequestParams.getInstance().UserProfileUploadCover(id);
        Map<String, File> files = new HashMap<String, File>();
        files.put("cover", file);

        RequestObject request = new RequestObject(Contants.TYPE_UPLOAD, url, params, files);
        request.setEntity(new UserProfileUploadCoverEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：上传头像（旧接口）
     */
    public static void uploadAvatar(String id, File file) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().uploadAvatar();
        Map<String, Object> params = RequestParams.getInstance().uploadAvatar(id);
        Map<String, File> files = new HashMap<String, File>();
        files.put("head", file);

        RequestObject request = new RequestObject(Contants.TYPE_UPLOAD, url, params, files);
        request.setEntity(new UploadAvatarEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：关注玩家
     */
    public static void memberAttention(String member_id, String id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().memberAttention();
        Map<String, Object> params = RequestParams.getInstance().memberAttention(member_id, id);

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(new MemberAttentionEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：关注玩家201
     */
    public static void memberAttention201(String member_id, String id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().memberAttention201();
        Map<String, Object> params = RequestParams.getInstance().memberAttention201(member_id, id);

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(new MemberAttention201Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：关注列表
     */
    public static void personalAttention(String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().personalAttention();
        Map<String, Object> params = RequestParams.getInstance().personalAttention(member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new PersonalAttentionEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：粉丝列表
     */
    public static void personalFans(String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().personalFans();
        Map<String, Object> params = RequestParams.getInstance().personalFans(member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(new PersonalFansEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：云端视频列表
     */
    public static void cloudList(String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().cloudList();
        Map<String, Object> params = RequestParams.getInstance().cloudList(member_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new CloudListEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：用户视频列表
     */
    public static void authorVideoList(int page, String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().authorVideoList();
        Map<String, Object> params = RequestParams.getInstance().authorVideoList(page, member_id);

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(new AuthorVideoListEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：用户视频列表2
     */
    public static void authorVideoList2(BaseResponseEntity entity, String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().authorVideoList2();
        Map<String, Object> params = RequestParams.getInstance().authorVideoList2(member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(entity);
        helper.doNetwork(request);
    }

    /**
     * 用户视频列表211
     */
    public static void videoCloudList(String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().videoCloudList();
        Map<String, Object> params = RequestParams.getInstance().authorVideoList2(member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new AuthorVideoList2Entity());
        helper.doService(request);
    }

    /**
     * 功能：用户视频列表208
     */
    public static void authorVideoList208(String member_id, String game_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().authorVideoList208();
        Map<String, Object> params = RequestParams.getInstance().authorVideoList208(member_id, game_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new AuthorVideoList208Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：他的视频-分组208
     */
    public static void authorVideoGroup(String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().authorVideoGroup();
        Map<String, Object> params = RequestParams.getInstance().authorVideoGroup(member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new AuthorVideoGroupEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：个人空间评论
     */
    public static void memberReviewList(String member_id, int page, String id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().memberReviewList();
        Map<String, Object> params = RequestParams.getInstance().memberReviewList(member_id, page, id);

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(new MemberReviewListEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：资料信息发表评论
     */
    public static void memberReview(String member_id, int page, String id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().memberReview();
        Map<String, Object> params = RequestParams.getInstance().memberReviewList(member_id, page, id);

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(new MemberReviewEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：评论内容的点赞
     */
    public static void reviewLike(String review_id, String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().reviewLike();
        Map<String, Object> params = RequestParams.getInstance().reviewLike(review_id, member_id);

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(new ReviewLikeEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：评论内容的点赞
     */
    public static void reviewLike2(String id, String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().reviewLike();
        Map<String, Object> params = RequestParams.getInstance().reviewLike2(id, member_id);

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(new ReviewLike2Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：我的游戏
     */
    public static void myGroupList(String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().myGroupList();
        Map<String, Object> params = RequestParams.getInstance().myGroupList(member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new MyGroupListEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：个人视频（时间轴）
     */
    public static void userProfileTimelineLists(String member_id, String user_id, int currentpage, int pagelength) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().userProfileTimelineLists();
        Map<String, Object> params = RequestParams.getInstance().userProfileTimelineLists(member_id, user_id, currentpage, pagelength);

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(new UserProfileTimelineListsEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：动态是否有更新
     */
    public static void dynamicDot(String member_id, long currentTime) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().dynamicDot();
        Map<String, Object> params = RequestParams.getInstance().dynamicDot(member_id, currentTime);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new DynamicDotEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：个人视频（动态）
     */
    public static void memberDynamicList(String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().memberDynamicList();
        Map<String, Object> params = RequestParams.getInstance().memberDynamicList(member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new MemberDynamicListEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：隐藏云端视频(七牛)
     */
    public static VideoDisplayVideoEntity videoDisplayVideoQnKey(String qn_key) {

        RequestHelper helper = new RequestHelper<VideoDisplayVideoEntity>();
        String url = RequestUrl.getInstance().videoDisplayVideo();
        Map<String, Object> params = RequestParams.getInstance().videoDisplayVideoQnKey(qn_key);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new VideoDisplayVideoEntity());
        VideoDisplayVideoEntity entity = (VideoDisplayVideoEntity) helper.postEntity(request);
        return entity;
    }

    /**
     * 功能：隐藏云端视频（优酷）
     */
    public static VideoDisplayVideoEntity videoDisplayVideoUrl(String url) {

        RequestHelper helper = new RequestHelper<VideoDisplayVideoEntity>();
        String uri = RequestUrl.getInstance().videoDisplayVideo();
        Map<String, Object> params = RequestParams.getInstance().videoDisplayVideoUrl(url);

        RequestObject request = new RequestObject(Contants.TYPE_GET, uri, params, null);
        request.setEntity(new VideoDisplayVideoEntity());
        VideoDisplayVideoEntity entity = (VideoDisplayVideoEntity) helper.postEntity(request);
        return entity;
    }

    /**
     * 功能：用户玩家榜排名
     */
    public static void rankingMyRanking(String memberId, String order) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().rankingMyRanking();
        Map<String, Object> params = RequestParams.getInstance().rankingMyRanking(memberId, order);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new RankingMyRankingEntity());
        helper.doNetwork(request);
    }

    /** ############ 任务 ############# */

    /**
     * 功能：任务列表
     */
    public static void taskListNew(String member_id, int page) {
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().taskList201();
        Map<String, Object> params = RequestParams.getInstance().taskListNew(member_id, page);
        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(new TaskListNewEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：任务列表115
     */
    public static void taskList115(String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().taskList115();
        Map<String, Object> params = RequestParams.getInstance().taskList115(page, member_id);

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(new TaskList115Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：任务列表201
     */
    public static void taskList201(String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().taskList201();
        Map<String, Object> params = RequestParams.getInstance().taskList201(page, member_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new TaskList201Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：任务列表203
     */
    public static void taskList203(String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().taskList203();
        Map<String, Object> params = RequestParams.getInstance().taskList203(member_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new TaskList203Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：搜索任务
     */
    public static void searchTask(String name, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().searchTask();
        Map<String, Object> params = RequestParams.getInstance().searchTask(name, page);

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(new SearchTaskEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：接受任务
     */
    public static void acceptTask(String task_id, String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().acceptTask();
        Map<String, Object> params = RequestParams.getInstance().acceptTask(task_id, member_id);

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(new AcceptTaskEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：接受任务201
     */
    public static void acceptTask201(String task_id, String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().acceptTask201();
        Map<String, Object> params = RequestParams.getInstance().acceptTask201(task_id, member_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new AcceptTask201Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：领取任务奖励
     */
    public static void getExp(String task_id, String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getExp();
        Map<String, Object> params = RequestParams.getInstance().getExp(task_id, member_id);

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(new GetExpEntity());
        helper.doNetwork(request);
    }

    /** ############ 关于我们 ############# */

    /**
     * 功能：版本更新
     */
    public static void updateVersion(BaseResponseEntity entity) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().updateVersion();

        int versionCode = VersionUtils.getCurrentVersionCode(AppManager.getInstance().getApplication());
        // 测试build
//		versionCode = 20151105;
        Map<String, Object> params = RequestParams.getInstance().updateVersion(versionCode, AppConstant.SYSJ_ANDROID);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(entity);
        helper.doNetwork(request);
    }

    /**
     * 功能：版本更新
     */
    public static void updateVersion() {
        DataManager.updateVersion(new UpdateVersionEntity());
    }

    /**
     * 功能：版本更新（关于页面）
     */
    public static void updateVersionAbout() {
        DataManager.updateVersion(new UpdateVersionAboutEntity());
    }

    /**
     * 功能：版本更新（关于设置）
     */
    public static void updateVersionSetting() {
        DataManager.updateVersion(new UpdateVersionSettingEntity());
    }

    /** ############ 收藏 ############# */

    /**
     * 功能：视频收藏列表
     */
    public static void collectVideoList(String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().collectVideoList();
        Map<String, Object> params = RequestParams.getInstance().collectVideoList(member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new CollectVideoListEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：视频收藏列表
     */
    public static void memberCollectVideoList(String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().memberCollectVideoList();
        Map<String, Object> params = RequestParams.getInstance().memberCollectVideoList(member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new MemberCollectVideoListEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：图文视频列表
     */
    public static void memberCollectPicList(String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().memberCollectPicList();
        Map<String, Object> params = RequestParams.getInstance().memberCollectPicList(member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new MemberCollectPicListEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：视频取消收藏
     */
    public static void memberCancelCollectVideo(String member_id, String video_ids) {

        DataManager.memberCancelCollect(new MemberCancelCollectVideoEntity(), member_id, video_ids, "");
    }

    /**
     * 功能：图文取消收藏
     */
    public static void memberCancelCollectImage(String member_id, String pic_ids) {

        DataManager.memberCancelCollect(new MemberCancelCollectImageEntity(), member_id, "", pic_ids);
    }

    /**
     * 功能：视频图文取消收藏
     */
    private static void memberCancelCollect(BaseResponseEntity entity, String member_id, String video_ids, String pic_ids) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().memberCancelCollect();
        Map<String, Object> params = RequestParams.getInstance().memberCancelCollect(member_id, video_ids, pic_ids);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(entity);
        helper.doNetwork(request);
    }

    /** ############ 游戏 ############# */

    /**
     * 功能：热门游戏列表（旧接口）
     */
    public static void hotGame() {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().hotGame();

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, null, null);
        request.setEntity(new HotGameEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：最热，最新游戏列表
     */
    public static void gameList(int page, String member_id, String sort) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().gameList();
        Map<String, Object> params = RequestParams.getInstance().gameList(page, member_id, sort);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new GameListEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：圈子类型（旧接口）
     */
    public static void groupType() {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().groupType();

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, null, null);
        request.setEntity(new GroupTypeEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：圈子类型
     */
    public static void groupType2() {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().groupType2();

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, null, null);
        request.setEntity(new GroupType2Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：圈子类型
     */
    public static void groupType217() {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().gameCate();

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, null, null);
        request.setEntity(new GroupType210Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：圈子列表（旧接口）
     */
    public static void groupList(int page, String type_id, String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().groupList();
        Map<String, Object> params = RequestParams.getInstance().groupList(page, type_id, member_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new GroupListEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：圈子列表
     */
    public static void groupList2(int page, String group_type_id, String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().groupList2();
        Map<String, Object> params = RequestParams.getInstance().groupList2(page, group_type_id, member_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new GroupList2Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：圈子详情（旧接口）
     */
    public static void groupDetail(String group_id, String member_id, String type_name) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().groupDetail();
        Map<String, Object> params = RequestParams.getInstance().groupDetail(group_id, member_id, type_name);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new GroupDetailEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：圈子详情
     */
    public static void groupInfo(String group_id, String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().groupInfo();
        Map<String, Object> params = RequestParams.getInstance().groupInfo(group_id, member_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new GroupInfoEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：圈子视频列表（旧接口）
     */
    public static void groupVideoList(String group_id, String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().groupVideoList();
        Map<String, Object> params = RequestParams.getInstance().groupVideoList(group_id, member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new GroupVideoListEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：圈子视频列表（最新）
     */
    public static void groupDataList(String group_id, String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().groupDataList();
        Map<String, Object> params = RequestParams.getInstance().groupDataList(group_id, member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new GroupNewDataListEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：圈子视频列表（最热）
     */
    public static void groupHotDataList(String group_id, String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().groupHotDataList();
        Map<String, Object> params = RequestParams.getInstance().groupHotDataList(group_id, member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new GroupHotDataListEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：圈子玩家列表
     */
    public static void groupGamerList(String group_id, String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().groupGamerList();
        Map<String, Object> params = RequestParams.getInstance().groupGamerList(group_id, member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new GroupGamerListEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：圈子玩家列表（旧接口）
     */
    public static void groupMemberList(String group_id, String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().groupMemberList();
        Map<String, Object> params = RequestParams.getInstance().groupMemberList(group_id, member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new GroupMemberListEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：关注圈子
     */
    public static void groupAttention(String group_id, String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().groupAttention();
        Map<String, Object> params = RequestParams.getInstance().groupAttention(group_id, member_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new GroupAttentionEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：关注圈子201
     */
    public static void groupAttentionGroup(String group_id, String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().groupAttentionGroup();
        Map<String, Object> params = RequestParams.getInstance().groupAttentionGroup(group_id, member_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new GroupAttentionGroupEntity());
        helper.doNetwork(request);
    }

    /** ############ 游戏 ############# */

    /**
     * 功能：广场列表（最热）
     */
    public static void squareListHot(String member_id, int page) {
        DataManager.squareList(new SquareListHotEntity(), member_id, "click", page);
    }

    /**
     * 功能：广场列表（最新）
     */
    public static void squareListNew(String member_id, int page) {
        DataManager.squareList(new SquareListNewEntity(), member_id, "time", page);
    }

    /**
     * 功能：广场列表
     */
    private static void squareList(BaseResponseEntity entity, String member_id, String sort, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().squareList();
        Map<String, Object> params = RequestParams.getInstance().squareList(member_id, sort, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(entity);
        helper.doNetwork(request);
    }

    /** ############ 视频 ############# */

    /**
     * 功能：视频详情201
     */
    public static void videoDetail201(String video_id, String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().videoDetail201();
        Map<String, Object> params = RequestParams.getInstance().videoDetail201(video_id, member_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new VideoDetail201Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：视频踩203
     */
    public static void fndownClick203(String video_id, String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().fndownClick203();
        Map<String, Object> params = RequestParams.getInstance().fndownClick203(video_id, member_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new FndownClick203Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：视频评论列表211
     */
    public static void videoCommentList211(String video_id, String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().videoCommentList211();
        Map<String, Object> params = RequestParams.getInstance().videoCommentList(video_id, member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new VideoCommentListEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：视频评论列表
     */
    public static void videoCommentList(String video_id, String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().videoCommentList();
        Map<String, Object> params = RequestParams.getInstance().videoCommentList(video_id, member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new VideoCommentListEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：视频发布评论
     */
    private static void videoDoComment(BaseResponseEntity entity, String video_id, String member_id, String content, String mark, String comment_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().videoDoComment();
        Map<String, Object> params = RequestParams.getInstance().videoDoComment(video_id, member_id, content, mark, comment_id);

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(entity);
        helper.doNetwork(request);
    }

    /**
     * 功能：视频发布评论
     */
    public static void videoDoComment2Video(String video_id, String member_id, String content) {

        DataManager.videoDoComment(new VideoDoComment2VideoEntity(), video_id, member_id, content, "0", null);
    }

    /**
     * 功能：视频发布评论（多级）
     */
    public static void videoDoComment2Comment(String video_id, String member_id, String content, String mark, String comment_id) {

        DataManager.videoDoComment(new VideoDoComment2CommentEntity(), video_id, member_id, content, "1", comment_id);
    }

    /**
     * 功能：视频收藏
     */
    public static void videoCollect2(String video_id, String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().videoCollect2();
        Map<String, Object> params = RequestParams.getInstance().videoCollect2(video_id, member_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new VideoCollect2Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：视频收藏
     */
    public static void videoCollect2(List<String> video_ids, String member_id) {

        for (String video_id : video_ids) {
            DataManager.videoCollect2(video_id, member_id);
        }
    }

    /**
     * 功能：视频点赞
     */
    public static void videoFlower2(String video_id, String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().videoFlower2();
        Map<String, Object> params = RequestParams.getInstance().videoFlower2(video_id, member_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new VideoFlower2Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：视频评论点赞
     */
    public static void videoCommentLike2(String comment_id, String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().videoCommentLike2();
        Map<String, Object> params = RequestParams.getInstance().videoCommentLike2(comment_id, member_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new VideoCommentLike2Entity());
        helper.doNetwork(request);
    }

    /** ############ 榜单 ############# */

    /**
     * 功能：达人榜
     */
    public static void daRenListNew(BaseResponseEntity e, String member_id, String flag, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().daRenListNew();
        Map<String, Object> params = RequestParams.getInstance().daRenListNew(member_id, flag, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(e);
        helper.doNetwork(request);
    }

    /**
     * 功能：玩家榜--粉丝榜
     */
    public static void rankingMemberRankingFans(String member_id, int page) {
//        DataManager.rankingMemberRanking(new RankingMemberRankingFansEntity(), member_id, "fans", page);
    }

    /**
     * 功能：玩家榜--磨豆榜
     */
    public static void memberRankingCurrency(String member_id, int page) {
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().memberRankingCurrency();
        Map<String, Object> params = RequestParams.getInstance().memberRankingCurrency(member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new PlayerRankingCurrencyEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：玩家榜--视频榜
     */
    public static void rankingMemberRankingVideo(String member_id, int page) {
//        DataManager.rankingMemberRanking(new RankingMemberRankingVideoEntity(), member_id, "video", page);
    }

    /**
     * 功能：玩家榜
     */
    private static void rankingMemberRanking(BaseResponseEntity e, String member_id, String sort, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().rankingMemberRanking();
        Map<String, Object> params = RequestParams.getInstance().rankingMemberRanking(member_id, sort, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(e);
        helper.doNetwork(request);
    }

    /**
     * 功能：视频榜
     */
    private static void rankingVideoRanking(BaseResponseEntity e, String member_id, String sort, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().rankingVideoRanking();
        Map<String, Object> params = RequestParams.getInstance().rankingVideoRanking(member_id, sort, page);// like/comment/click

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(e);
        helper.doNetwork(request);
    }

    /** ############ 消息 ############# */

    /**
     * 功能：消息列表
     */
    private static void msgList(BaseResponseEntity entity, int type_id, String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().msgList();
        Map<String, Object> params = RequestParams.getInstance().msgList(type_id, member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(entity);
        helper.doNetwork(request);
    }

    /**
     * 功能：视频消息列表
     */
    public static void videoMsgList(String member_id, int page) {
        DataManager.msgList(new VideoMsgListEntity(), 2, member_id, page);
    }

    /**
     * 功能：系统消息列表
     */
    public static void systemMsgList(String member_id, int page) {
        DataManager.msgList(new SystemMsgListEntity(), 1, member_id, page);
    }

    /**
     * 功能：视频图文消息
     */
    public static void messageMyMessage(String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().messageMyMessage();
        Map<String, Object> params = RequestParams.getInstance().messageMyMessage(member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new MessageMyMessageEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：清除视频图文消息
     */
    public static void allRead(String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().allRead();
        Map<String, Object> params = RequestParams.getInstance().allRead(member_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new AllReadEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：系统消息
     */
    public static void messageSysMessage(String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().messageSysMessage();
        Map<String, Object> params = RequestParams.getInstance().messageSysMessage(member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new MessageSysMessageEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：圈子消息
     */
    public static void messageGroupMessage(String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().messageGroupMessage();
        Map<String, Object> params = RequestParams.getInstance().messageGroupMessage(member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new MessageGroupMessageEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：圈子消息总数
     */
    public static void messageMsgRed(String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().messageMsgRed();
        Map<String, Object> params = RequestParams.getInstance().messageMsgRed(member_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new MessageMsgRedEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：消息提示红点
     */
    public static void messageMsgGroupRed(String memberId) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().messageMsgGroupRed();
        Map<String, Object> params = RequestParams.getInstance().messageMsgGroupRed(memberId);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new MessageMsgGroupRedEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：视频，图文消息已读
     */
    public static void messageClickMsg(String msg_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().messageClickMsg();
        Map<String, Object> params = RequestParams.getInstance().messageClickMsg(msg_id);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new MessageClickMsgEntity());
        helper.doNetwork(request);
    }

    /** ############## 图文 ############## */

    /**
     * 功能：图文详情
     */
    public static void photoPhotoDetail(String pic_id, String member_id) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().photoPhotoDetail();
        Map<String, Object> params = RequestParams.getInstance().photoPhotoDetail(pic_id, member_id);

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(new PhotoPhotoDetailEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：图文发布评论
     */
    public static void photoSendComment(String pic_id, String member_id, String content) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().photoSendComment();
        Map<String, Object> params = RequestParams.getInstance().photoSendComment(pic_id, member_id, content);

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(new PhotoSendCommentEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：图文评论列表
     */
    public static void photoPhotoCommentList(String pic_id, int page_count, int current_page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().photoPhotoCommentList();
        Map<String, Object> params = RequestParams.getInstance().photoPhotoCommentList(pic_id, page_count, current_page);

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(new PhotoPhotoCommentListEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：图文献花
     */
    public static void photoFlower(String pic_id, String member_id, int isflower) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().photoFlower();
        Map<String, Object> params = RequestParams.getInstance().photoFlower(pic_id, member_id, isflower);

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(new PhotoFlowerEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：图文收藏
     */
    public static void photoCollection(String pic_id, String member_id, int iscoll) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().photoCollection();
        Map<String, Object> params = RequestParams.getInstance().photoCollection(pic_id, member_id, iscoll);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new PhotoCollectionEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：关注/粉丝列表
     */
    private static void fansList203(BaseResponseEntity entity, String member_id, String type, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().fansList203();
        Map<String, Object> params = RequestParams.getInstance().fansList203(member_id, type, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(entity);
        helper.doNetwork(request);
    }

    /**
     * 功能：关注列表
     */
    public static void fansList203Attention(String member_id, int page) {

        fansList203(new FansList203AttentionEntity(), member_id, "attention", page);
    }

    /**
     * 功能：粉丝列表
     */
    public static void fansList203Fans(String member_id, int page) {

        fansList203(new FansList203FansEntity(), member_id, "fans", page);
    }

    /**
     * 功能：精彩推荐--视频列表
     */
    public static void editList203(String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().editList203();
        Map<String, Object> params = RequestParams.getInstance().editList203(member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new EditList203Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：精彩推荐--banner
     */
    public static void editBanner203() {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().editBanner203();

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, null, null);
        request.setEntity(new EditBanner203Entity());
        helper.doNetwork(request);
    }

    /**
     * 功能：精彩推荐--金牌主播
     */
    public static void editGoldMember203() {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().editGoldMember203();

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, null, null);
        request.setEntity(new EditGoldMember203Entity());
        helper.doNetwork(request);
    }
}
