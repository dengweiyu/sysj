package com.li.videoapplication.data;


import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.local.ImageDirectoryHelper;
import com.li.videoapplication.data.local.ScreenShotEntity;
import com.li.videoapplication.data.local.ScreenShotHelper;
import com.li.videoapplication.data.local.VideoCaptureHelper;
import com.li.videoapplication.data.model.entity.Advertisement;
import com.li.videoapplication.data.model.entity.HomeDto;
import com.li.videoapplication.data.model.entity.SquareGameEntity;
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
import com.li.videoapplication.framework.AppAccount;
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


    public static void getHomeInfo(int page){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().homeInfo();
        Map<String, Object> params = RequestParams.getInstance().homeInfo(page);
        RequestObject request = new RequestObject(Contants.TYPE_GET, url,params, null);
        request.setEntity(new HomeDto());
        helper.doService(request);
    }

    /**
     * 分享页面广场信息
     */
    public static void sharePlayerSquare(String memberId) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().sharePlayerSquare();
        Map<String, Object> params = RequestParams.getInstance().sharePlayerSquare(memberId);
        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new ShareSquareEntity());
        helper.doService(request);
    }


    /**
     * 分享成功后进行触发
     */
    public static void shareTriggerReward(String memberId,String hook,String taskId,String flag) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().shareTriggerReward();
        Map<String, Object> params = RequestParams.getInstance().shareTriggerReward(memberId,hook,taskId,flag);
        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);

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

    /**
     * 赛事奖金榜状态
     */
    public static void getRewardStatus() {
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getRewardStatus();
        RequestObject request = new RequestObject(Contants.TYPE_GET, url, null, null);
        request.setEntity(new RewardStatusEntity());
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
        public static void videoShareVideo211(String video_id, String member_id,int mark) {

            RequestHelper helper = new RequestHelper();
            String url = RequestUrl.getInstance().videoShare211();
            Map<String, Object> params = RequestParams.getInstance().videoClickVideo221(video_id, member_id,mark);

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


        public static final void advertisement_7() {
            Advertisement entity = PreferencesHepler.getInstance().getAdvertisement_7();
            int time = 0;
            if (entity != null)
                time = entity.getChangetime();
            DataManager.advertisementAdImage204(ADVERTISEMENT_7, time);
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
     * 功能：个人魔豆数量
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
    public static void login(String key,String appKey) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().login();
        String time  = System.currentTimeMillis() / 1000+"";
        Map<String, Object> params = RequestParams.getInstance().login(key);

        //增加参数
        RequestParams.getInstance().addLoginParams(params,2,time, AppAccount.sign(appKey,time),"app_sysj","E!AHcLR%Pxyp*&d8","password");

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(new LoginEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：飞磨内部快速登录
     */
    public static void loginFm(String key, String appKey) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().loginFm();
        Map<String, Object> params = RequestParams.getInstance().login(key);
        String time  = System.currentTimeMillis() / 1000+"";
        RequestParams.getInstance().addLoginParams(params,2,time, AppAccount.sign(appKey,time),"app_sysj","E!AHcLR%Pxyp*&d8","password");

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(new LoginEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：第三方登录
     */
    public static void login(String key, String appKey,String nickname, String name, String sex, String location, String avatar) {
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().login();

        String time  = System.currentTimeMillis() / 1000+"";

        Map<String, Object> params = RequestParams.getInstance().login(key,
                "1",
                nickname,
                name,
                sex,
                location,
                avatar,
                AppConstant.SYSJ_ANDROID);

        RequestParams.getInstance().addLoginParams(params,2,time, AppAccount.sign(appKey,time),"app_sysj","E!AHcLR%Pxyp*&d8","password");

        RequestObject request = new RequestObject(Contants.TYPE_POST, url, params, null);
        request.setEntity(new LoginEntity());
        helper.doNetwork(request);
    }

    /** ############ 用户资料 ############# */
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
     * 功能：玩家广场是否有更新
     */
    public static void squareDot(String member_id, long currentTime) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().squareDot();
        Map<String, Object> params = RequestParams.getInstance().dynamicDot(member_id, currentTime);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new SquareDotEntity());
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
    public static void memberCollectVideoList(String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().memberCollectVideoList();
        Map<String, Object> params = RequestParams.getInstance().memberCollectVideoList(member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new MemberCollectVideoListEntity());
        helper.doNetwork(request);
    }


    /**
     * 功能：视频取消收藏
     */
    public static void memberCancelCollectVideo(String member_id, String video_ids) {

        DataManager.memberCancelCollect(new MemberCancelCollectVideoEntity(), member_id, video_ids, "");
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
     * 功能：圈子视频列表（最新）
     */
    public static void groupDataList(String group_id, String member_id, int page) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().groupDataList();
        Map<String, Object> params = RequestParams.getInstance().groupDataList(group_id, member_id, page);
        params.put("mark","newest");
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
        params.put("mark","hosttest");
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
    public static void squareListHot(String member_id, int page,String gameId) {
        DataManager.squareList(new SquareListHotEntity(), member_id, "click", page,gameId);
    }

    /**
     * 功能：广场列表（最新）
     */
    public static void squareListNew(String member_id, int page,String gameId) {
        DataManager.squareList(new SquareListNewEntity(), member_id, "time", page,gameId);
    }

    /**
     * 功能：广场列表
     */
    private static void squareList(BaseResponseEntity entity, String member_id, String sort, int page,String gameId) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().squareList();
        Map<String, Object> params = RequestParams.getInstance().squareList(member_id, sort, page,gameId);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(entity);
        helper.doNetwork(request);
    }


    /**
     * 功能：广场游戏列表
     */
    public static void squareGameList() {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().squareGameList();
        Map<String, Object> params = RequestParams.getInstance().squareGameList();

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new SquareGameEntity());
        helper.doNetwork(request);
    }


    /**
     * 功能：玩家广场页面统计
     */
    public static void squareGamePageStatistical(String gameId,String sort) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().squareGameListStatistical();
        Map<String, Object> params = RequestParams.getInstance().squareGameListStatistical(gameId,sort);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
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
     * 功能：视频图文消息
     */
    public static void messageMyMessage(String member_id, int page,String url) {

        RequestHelper helper = new RequestHelper();

        Map<String, Object> params = RequestParams.getInstance().messageMyMessage(member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new MessageMyMessageEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：系统消息
     */
    public static void messageSysMessage(String member_id, int page,String url) {

        RequestHelper helper = new RequestHelper();

        Map<String, Object> params = RequestParams.getInstance().messageSysMessage(member_id, page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new MessageSysMessageEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：圈子消息
     */
    public static void messageGroupMessage(String member_id, int page,String url) {

        RequestHelper helper = new RequestHelper();
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
     * 功能：打赏和陪练消息
     */
    public static void getRewardAndPlayWithMsg(String memberId,int page,String url) {

        RequestHelper helper = new RequestHelper();

        Map<String, Object> params = RequestParams.getInstance().messageSysMessage(memberId,page);

        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new RewardAndPlayWithMsgEntity());
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

    /**
     * 功能：IM消息点击  将URL给后台解析
     */
    public static void parseMessage(String urlStr) {

        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().parseMessage();
        Map<String, Object> params = RequestParams.getInstance().parseMessage(urlStr);
        RequestObject request = new RequestObject(Contants.TYPE_GET, url, params, null);
        request.setEntity(new ParseResultEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：个人信息界面发消息是否使用自有IM
     */
    public static  void switchChat(){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().switchChat();
        RequestObject request = new RequestObject(Contants.TYPE_GET, url,null, null);
        request.setEntity(new SwitchChatEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：会员中心 开通会员
     */
    public static  void vipInfo(){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().vipInfo();
        Map<String, Object> params = RequestParams.getInstance().vipInfo();
        RequestObject request = new RequestObject(Contants.TYPE_GET, url,params, null);
        request.setEntity(new VipRechargeEntity());
        helper.doNetwork(request);
    }


    /**
     * 功能：会员中心 开通会员
     */
    public static  void getGiftType(){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().giftType();
        Map<String, Object> params = RequestParams.getInstance().giftType();
        RequestObject request = new RequestObject(Contants.TYPE_GET, url,params, null);
        request.setEntity(new PlayGiftTypeEntity());
        helper.doNetwork(request);
    }


    /**
     * 功能：获取礼物流水：收到/送出礼物
     */
    public static  void getGiftBill(String memberId,String userId){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().giftBill();
        Map<String, Object> params = RequestParams.getInstance().giftBill(memberId,userId);
        RequestObject request = new RequestObject(Contants.TYPE_GET, url,params, null);
        request.setEntity(new MyGiftBillEntity());
        helper.doNetwork(request);
    }


    /**
     * 功能：获取打赏榜
     */
    public static  void getPlayGiftList(String memberId,String videoId){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getPlayGiftList();
        Map<String, Object> params = RequestParams.getInstance().getPlayGiftList(memberId,videoId);
        RequestObject request = new RequestObject(Contants.TYPE_GET, url,params, null);
        request.setEntity(new VideoPlayGiftEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：获取时间轴打赏列表
     */
    public static  void getGiftTimeLineList(String videoId){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getGiftTimeLineList();
        Map<String, Object> params = RequestParams.getInstance().getGiftTimeLineList(videoId);
        RequestObject request = new RequestObject(Contants.TYPE_GET, url,params, null);
        request.setEntity(new TimeLineGiftEntity());
        helper.doNetwork(request);
    }
    /**
     * 功能：获取服务器时间
     */
    public static  void getServiceTime(){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getServiceTime();
        RequestObject request = new RequestObject(Contants.TYPE_GET, url,null, null);
        request.setEntity(new ServiceTimeEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：打赏
     */
    public static  void playGift(String sign,String memberId,String videoId,String giftId,String videoNode,int number,long time){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().playGift();
        Map<String, Object> params = RequestParams.getInstance().playGift(sign,memberId,videoId,giftId,videoNode,number,time);
        RequestObject request = new RequestObject(Contants.TYPE_POST, url,params, null);
        request.setEntity(new PlayGiftResultEntity());
        helper.doNetwork(request);
    }

    /**
     * 功能：视频播放页分享成功后触发
     */
    public static  void sharedSuccess(String videoId){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().sharedSuccess();
        Map<String, Object> params = RequestParams.getInstance().sharedSuccess(videoId);
        RequestObject request = new RequestObject(Contants.TYPE_GET, url,params, null);
        helper.doNetwork(request);
    }

    /**
     * 教练列表
     */

    public static void getCoachList(int page,boolean isTest,String typeId){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getCoachList();
        Map<String, Object> params = RequestParams.getInstance().getCoachList(page,typeId);
        if (isTest){
            params.put("is_test",1);
        }
        RequestObject request = new RequestObject(Contants.TYPE_GET, url,params, null);
        request.setEntity(new CoachListEntity());
        helper.doNetwork(request);
    }

    /**
     * 教练详情
     */

    public static void getCoachDetail(String memberId){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getCoachDetail();
        Map<String, Object> params = RequestParams.getInstance().getCoachDetail(memberId);
        RequestObject request = new RequestObject(Contants.TYPE_GET, url,params, null);
        request.setEntity(new CoachDetailEntity());
        helper.doNetwork(request);
    }

    /**
     * 陪玩订单选项
     */

    public static void getPlayWithOrderOptions(String coachId){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getPlayWithOrderOptions();
        Map<String, Object> params = RequestParams.getInstance().getPlayWithOrderOptions(coachId);
        RequestObject request = new RequestObject(Contants.TYPE_GET, url,params, null);
        request.setEntity(new PlayWithOrderOptionsEntity());
        helper.doNetwork(request);
    }

    /**
     * 预览订单价格
     */
    public static void getPreviewOrderPrice(String memberId,int rank,int mode,int gameCount,String typeId){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getPreviewOrderPrice();
        if (memberId == null){
            memberId = "";
        }
        Map<String, Object> params = RequestParams.getInstance().getPreviewOrderPrice(memberId,rank,mode,gameCount,typeId);
        RequestObject request = new RequestObject(Contants.TYPE_GET, url,params, null);
        request.setEntity(new PlayWithOrderPriceEntity());
        helper.doNetwork(request);
    }


    /**
     * 生成陪玩订单
     */
    public static void createPlayWithOrder(String memberId,String coachId,int server,int rank,int mode,String time,int count,int orderMode,String gameId){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().createPlayWithOrder();
        Map<String, Object> params = RequestParams.getInstance().createPlayWithOrder(memberId,coachId,server,rank,mode,time,count,orderMode,gameId);
        RequestObject request = new RequestObject(Contants.TYPE_POST, url,params, null);
        request.setEntity(new PlayWithOrderEntity());
        helper.doNetwork(request);
    }

    /**
     * 查询下单列表
     */
    public static void getPlayWithPlaceOrder(String memberId,int page){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getPlayWithPlaceOrder();
        Map<String, Object> params = RequestParams.getInstance().getPlayWithPlaceOrder(memberId,page);
        RequestObject request = new RequestObject(Contants.TYPE_POST, url,params, null);
        request.setEntity(new PlayWithPlaceOrderEntity());
        helper.doNetwork(request);
    }

    /**
     * 查询接单列表
     */
    public static void getPlayWithTakeOrder(String memberId,int page){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getPlayWithTakeOrder();
        Map<String, Object> params = RequestParams.getInstance().getPlayWithTakeOrder(memberId,page);
        RequestObject request = new RequestObject(Contants.TYPE_POST, url,params, null);
        request.setEntity(new PlayWithTakeOrderEntity());
        helper.doNetwork(request);
    }

    /**
     * 订单详情查询
     */

    public static void getPlayWithOrderDetail(String memberId,String orderId){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getPlayWithOrderDetail();
        Map<String, Object> params = RequestParams.getInstance().getPlayWithOrderDetail(memberId,orderId);
        RequestObject request = new RequestObject(Contants.TYPE_POST, url,params, null);
        request.setEntity(new PlayWithOrderDetailEntity());
        helper.doNetwork(request);
    }

    /**
     * 订单确认
     */
    public static void confirmOrder(String memberId,String orderId){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().confirmOrder();
        Map<String, Object> params = RequestParams.getInstance().confirmOrder(memberId,orderId);

        RequestObject request = new RequestObject(Contants.TYPE_POST, url,params, null);
        request.setEntity(new ConfirmOrderEntity());
        helper.doNetwork(request);
    }

    /**
     *评论标签
     */
    public static void getCommentTag(){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getCommentTag();
        RequestObject request = new RequestObject(Contants.TYPE_GET, url,null, null);
        request.setEntity(new CommentTagEntity());
        helper.doNetwork(request);
    }

    /**
     * 提交评价
     */
    public static void commitComment(String memberId,String orderId,String content,float score,int tag){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().commitComment();
        Map<String, Object> params = RequestParams.getInstance().commitComment(memberId,orderId,content,score,tag);
        RequestObject request = new RequestObject(Contants.TYPE_POST, url,params, null);
        request.setEntity(new CommitCommentEntity());
        helper.doNetwork(request);
    }

    /**
     * 订单完成结果提交
     */
    public static void confirmOrderResult(String memberId,String orderId, String data,String picKey){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().commitOrderResult();
        Map<String, Object> params = RequestParams.getInstance().commitOrderResult(memberId,orderId,data,picKey);
        RequestObject request = new RequestObject(Contants.TYPE_POST, url,params, null);
        request.setEntity(new OrderResultCommitEntity());
        helper.doNetwork(request);
    }


    /**
     * 教练签到
     */
    public static void coachSign(String memberId,int status){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().coachSign();
        Map<String, Object> params = RequestParams.getInstance().coachSign(memberId,status);
        RequestObject request = new RequestObject(Contants.TYPE_POST, url,params, null);
        request.setEntity(new CoachSignEntity());
        helper.doNetwork(request);
    }

    /**
     * 申请退款
     */
    public static void refundApply(String memberId,String orderId,String defaultReason,String inputReason){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().refundApply();
        Map<String, Object> params = RequestParams.getInstance().refundApply(memberId,orderId,defaultReason,inputReason);
        RequestObject request = new RequestObject(Contants.TYPE_POST, url,params, null);
        request.setEntity(new RefundApplyEntity());
        helper.doNetwork(request);
    }

    /**
     * 消息列表
     */
    public static void getMessageList(String memberId){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getMessageList();
        Map<String, Object> params = RequestParams.getInstance().getMessageList(memberId);
        RequestObject request = new RequestObject(Contants.TYPE_GET, url,params, null);
        request.setEntity(new MessageListEntity());
        helper.doNetwork(request);
    }

    /**
     * 用户确认完成订单
     */
    public static void confirmOrderDone(String memberId,String orderId){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().confirmOrderDone();
        Map<String, Object> params = RequestParams.getInstance().confirmOrderDone(memberId,orderId);
        RequestObject request = new RequestObject(Contants.TYPE_POST, url,params, null);
        request.setEntity(new ConfirmOrderDoneEntity());
        helper.doNetwork(request);
    }

    /**
     * 教练确认接单
     */
    public static void confirmTakeOrder(String memberId,String orderId){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().confirmTakeOrder();
        Map<String, Object> params = RequestParams.getInstance().confirmTakeOrder(memberId,orderId);
        RequestObject request = new RequestObject(Contants.TYPE_POST, url,params, null);
        request.setEntity(new ConfirmTakeOrderEntity());
        helper.doNetwork(request);
    }

    /**
     * 取消 消息红点
     */
    public static void readMessage(String memberId,String msgId,String symbol,String msgType,int isAll){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().readMessage();
        Map<String, Object> params = RequestParams.getInstance().readMessage(memberId,msgId,symbol,msgType,isAll);
        RequestObject request = new RequestObject(Contants.TYPE_GET, url,params, null);
        helper.doNetwork(request);
    }

    /**
     * 获取客服相关信息
     */
    public static void getCustomerInfo(){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getCustomerInfo();
        RequestObject request = new RequestObject(Contants.TYPE_GET, url,null, null);
        request.setEntity(new CustomerInfoEntity());
        helper.doNetwork(request);
    }

    /**
     * 教练确认退款
     */
    public static void coachConfirmRefund(String memberId,String orderId){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().coachConfirmRefund();
        Map<String, Object> params = RequestParams.getInstance().coachConfirmRefund(memberId,orderId);
        RequestObject request = new RequestObject(Contants.TYPE_POST, url,params, null);
        request.setEntity(new CoachConfirmRefundEntity());
        helper.doNetwork(request);
    }

    /**
     *  获取教练状态
     */
    public static void getCoachStatus(){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getCoachStatus();
        RequestObject request = new RequestObject(Contants.TYPE_GET, url,null, null);
        request.setEntity(new CoachStatusEntity());
        helper.doNetwork(request);
    }

    /**
     * 获取订单开始时间
     */
    public static void getOrderTime(String startTime){
        RequestHelper helper = new RequestHelper();
        Map<String,Object> param = new HashMap<>();
        if (!StringUtil.isNull(startTime)){

            param.put("start_time",startTime);
        }
        String url = RequestUrl.getInstance().getOrderTime();
        RequestObject request = new RequestObject(Contants.TYPE_GET, url,param, null);
        request.setEntity(new OrderTimeEntity());

        helper.doNetwork(request);
    }

    /**
     * 拉取补丁
     */
    public static void fetchPatch(String channelId,String version){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().fetchPatch();
        Map<String, Object> params = RequestParams.getInstance().fetchPatch(channelId,version);
        RequestObject request = new RequestObject(Contants.TYPE_GET, url,params, null);
        request.setEntity(new PatchEntity() );
        helper.doNetwork(request);
    }

    /**
     * 获取会员信息
     */
    public static void getUserVipInfo(String memberId){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getUserVipInfo();
        Map<String, Object> params = RequestParams.getInstance().getUserVipInfo(memberId);
        RequestObject request = new RequestObject(Contants.TYPE_GET, url,params, null);
        request.setEntity(new UserVipInfoEntity() );
        helper.doNetwork(request);
    }

    /**
     * 获取图片上传的七牛key、token
     */
    public static void getUploadKey(String memberId,int size){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getUploadKey();
        Map<String, Object> params = RequestParams.getInstance().getUploadKey(memberId,size);
        RequestObject request = new RequestObject(Contants.TYPE_POST, url,params, null);
        request.setEntity(new UploadKeyEntity());
        helper.doNetwork(request);
    }

    /**
     * 上传教练个性签名
     */
    public static void commitSign(String sign,String memberId,String key){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().commitSign();
        Map<String, Object> params = RequestParams.getInstance().commitSign(memberId,sign,key);
        RequestObject request = new RequestObject(Contants.TYPE_POST, url,params, null);
        request.setEntity(new CommitSignEntity());
        helper.doNetwork(request);
    }

    /**
     * 获取教练个性签名
     */
    public static void getCoachSign(String memberId){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getSign();
        Map<String, Object> params = RequestParams.getInstance().getSign(memberId);
        RequestObject request = new RequestObject(Contants.TYPE_GET, url,params, null);
        request.setEntity(new CoachEditSignEntity());
        helper.doNetwork(request);
    }

    /**
     * 解除Baidu Push绑定
     */
    public static void unBindBaiduPush(String memberId){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().unBindBaiduPush();
        Map<String, Object> params = RequestParams.getInstance().unBindBaiduPush(memberId);
        RequestObject request = new RequestObject(Contants.TYPE_POST, url,params, null);
        helper.doNetwork(request);
    }

    /**
     *土豪榜、人气榜
     */
    public static void getSendRewardRank(String memberId,int page,String type){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().rewardRank();
        Map<String, Object> params = RequestParams.getInstance().rewardRank(memberId,page,type);
        RequestObject request = new RequestObject(Contants.TYPE_GET, url,params, null);
        if ("videoGiftRanking".equals(type)){
            request.setEntity(new VideoRewardRankEntity());
        }else {
            request.setEntity(new SendRewardRankEntity());
        }

        helper.doNetwork(request);
    }

    /**
     *获取用户对教练的评论
     */
    public static void getCoachComment(String coachId,String mark,int page){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().getCoachComment();
        Map<String, Object> params = RequestParams.getInstance().getCoachComment(coachId,mark,page);
        RequestObject request = new RequestObject(Contants.TYPE_GET, url,params, null);
        request.setEntity(new CoachCommentEntity());
        helper.doNetwork(request);
    }


    /**
     * 统计播放页面停留时长
     */
    public static void commitStayDuration(String memberId,String videoId,int duration){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().commitStayDuration();
        Map<String, Object> params = RequestParams.getInstance().commitStayDuration(memberId,videoId,duration);
        RequestObject request = new RequestObject(Contants.TYPE_GET, url,params, null);
        helper.doNetwork(request);
    }

    /**
     * clear token
     */
    public static void clearToken(String accessToken,String refreshToken){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().clearToken();
        Map<String, Object> params = RequestParams.getInstance().clearToken(accessToken,refreshToken);
        RequestObject request = new RequestObject(Contants.TYPE_POST, url,params, null);
        helper.doNetwork(request);
    }

    /**
     * 抢单
     */
    public static void grabPlayWithOrder(String memberId,String orderId){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().grabPlayWithOrder();
        Map<String, Object> params = RequestParams.getInstance().grabPlayWithOrder(memberId,orderId);
        RequestObject request = new RequestObject(Contants.TYPE_POST, url,params, null);
        request.setEntity(new GrabPlayWithOrderEntity());
        helper.doNetwork(request);
    }

    /**
     * 取消订单
     */
    public static void cancelPlayWithOrder(String memberId,String orderId){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().cancelPlayWithOrder();
        Map<String, Object> params = RequestParams.getInstance().grabPlayWithOrder(memberId,orderId);
        RequestObject request = new RequestObject(Contants.TYPE_POST, url,params, null);
        request.setEntity(new CancelPlayWithOrderEntity());
        helper.doNetwork(request);
    }

    /**
     * 下载成功后回调
     */
    public static void downloadSuccess(String memberId,String gameId,String location,String involveId ){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().downloadSuccess();
        Map<String, Object> params = RequestParams.getInstance().downloadSuccess(memberId,gameId,location,involveId);
        RequestObject request = new RequestObject(Contants.TYPE_POST, url,params, null);
        helper.doNetwork(request);
    }

    /**
     * 选择关注游戏  列表
     */
    public static void focusGameList(){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().focusGameList();
        RequestObject request = new RequestObject(Contants.TYPE_GET, url,null, null);
        request.setEntity(new FocusGameListEntity());
        helper.doNetwork(request);
    }

    /**
     * 提交问卷
     */
    public static void commitFocusGameList(String memberId,String gameIds){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().commitFocusGameList();
        Map<String, Object> params = RequestParams.getInstance().commitFocusGameList(memberId,gameIds);

        RequestObject request = new RequestObject(Contants.TYPE_POST, url,params, null);
        request.setEntity(new CommitFocusGameListEntity());
        helper.doNetwork(request);
    }


    /**
     * 选择关注游戏  列表
     */
    public static void getHybridGroupDetail(String groupId,String memberId,String version){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().groupDetailHybrid();

        Map<String, Object> params = RequestParams.getInstance().groupHybridDetail(groupId,memberId,version);
        RequestObject request = new RequestObject(Contants.TYPE_GET, url,params, null);
        request.setEntity(new GroupHybridDetailEntity());
        helper.doNetwork(request);
    }

    /**
     * 获取需要混合页面显示的圈子
     */
    public static void getHybridGroupList(String version){
        RequestHelper helper = new RequestHelper();
        String url = RequestUrl.getInstance().groupHybridList();

        Map<String, Object> params = RequestParams.getInstance().groupHybridList(version);
        RequestObject request = new RequestObject(Contants.TYPE_GET, url,params, null);
        request.setEntity(new HybridGroupListEntity());
        helper.doNetwork(request);
    }
}
