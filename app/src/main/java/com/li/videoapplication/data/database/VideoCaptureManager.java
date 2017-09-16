package com.li.videoapplication.data.database;

import android.util.Log;

import com.li.videoapplication.data.local.FileUtil;
import com.li.videoapplication.data.model.entity.Tag;
import com.li.videoapplication.utils.StringUtil;

import org.xutils.ex.DbException;

import java.io.File;
import java.util.List;

/**
 * 实体：视频数据库
 */
public class VideoCaptureManager {

    public final static String TAG = VideoCaptureManager.class.getSimpleName();

    public static List<VideoCaptureEntity> findAll() {
        try {
            return xUtilsDb.DB
                    .selector(VideoCaptureEntity.class)
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<VideoCaptureEntity> find(int pageSize, int pageIndex) {
        try {
            return xUtilsDb.DB
                    .selector(VideoCaptureEntity.class)
                    .limit(pageSize)
                    .offset(pageSize * pageIndex)
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除全部
     */
    public static void dropTable() {
        try {
            xUtilsDb.DB.dropTable(VideoCaptureEntity.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存
     */
    public static void save(String video_path,
                            String video_source,
                            String video_station) {
        Log.i(TAG, "====== save ======");
        File file = null;
        try {
            file = new File(video_path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file == null || file.isDirectory())
            return;
        String name = file.getName();
        Log.i(TAG, "name=" + name);
        String extName = FileUtil.getExtName(name);
        if (!extName.startsWith("."))
            extName = "." + extName;
        name = name.replace(extName, "");
        Log.i(TAG, "name=" + name);
        Log.i(TAG, "extName=" + extName);
        save(name, video_path, video_source, video_station);
    }

    /**
     * 保存
     */
    public static int saveSafely(String video_name,
                                 String video_path,
                                 String video_source,
                                 String video_station) {

        VideoCaptureEntity entity = findByName(video_name);
        if (entity != null) {// 如果存在重名，返回告知用户
            return 0;
        } else {// 插入数据
            save(video_name, video_path, video_source, video_station);
            return 1;
        }
    }

    /**
     * 保存
     */
    public static void save(String video_name,
                            String video_path,
                            String video_source,
                            String video_station) {
        VideoCaptureEntity entity = new VideoCaptureEntity();
        if (video_name != null)
            entity.setVideo_name(video_name);
        if (video_path != null)
            entity.setVideo_path(video_path);
        if (video_source != null)
            entity.setVideo_source(video_source);
        if (video_station != null)
            entity.setVideo_station(video_station);
        try {
            xUtilsDb.DB.save(entity);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存
     */
    public static void save(VideoCaptureEntity entity) {
        VideoCaptureEntity e = findByPath(entity.getVideo_path());
        if (e == null)
            try {
                xUtilsDb.DB.save(entity);
            } catch (DbException ex) {
                ex.printStackTrace();
            }
    }

    /**
     * 查找
     */
    public static VideoCaptureEntity findByPath(String video_path) {
        try {
            return xUtilsDb.DB
                    .selector(VideoCaptureEntity.class)
                    .where(VideoCaptureEntity.VIDEO_PATH, "=", video_path)
                    .findFirst();
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查找
     */
    public static VideoCaptureEntity findByQnkey(String upvideo_qnkey) {
        try {
            return xUtilsDb.DB
                    .selector(VideoCaptureEntity.class)
                    .where(VideoCaptureEntity.UPVIDEO_QNKEY, "=", upvideo_qnkey)
                    .findFirst();
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查找
     */
    public static VideoCaptureEntity findById(int video_id) {
        try {
            return xUtilsDb.DB
                    .selector(VideoCaptureEntity.class)
                    .where(VideoCaptureEntity.ID, "=", video_id)
                    .findFirst();
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 查找
     */
    public static VideoCaptureEntity findByName(String video_name) {
        try {
            return xUtilsDb.DB
                    .selector(VideoCaptureEntity.class)
                    .where(VideoCaptureEntity.VIDEO_NAME, "=", video_name)
                    .findFirst();
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除
     */
    public static void deleteById(int video_id) {
        VideoCaptureEntity entity = findById(video_id);
        if (entity != null)
            try {
                xUtilsDb.DB.delete(entity);
            } catch (DbException e) {
                e.printStackTrace();
            }
    }

    /**
     * 删除
     */
    public static void deleteByPath(String video_path) {
        VideoCaptureEntity entity = findByPath(video_path);
        if (entity != null)
            try {
                xUtilsDb.DB.delete(entity);
            } catch (DbException e) {
                e.printStackTrace();
            }
    }

    /**
     * 保存文件路径，文件名
     */
    public static int updateNamePathById(int video_id,
                                         String new_video_name,
                                         String new_video_path) {

        VideoCaptureEntity entity = findByName(new_video_name);
        if (entity != null) {// 如果存在重名，返回告知用户
            return 0;
        } else {// 修改数据库中的视频名称
            VideoCaptureEntity e = findById(video_id);
            if (e != null) {
                if (new_video_name != null)
                    e.setVideo_name(new_video_name);
                if (new_video_path != null)
                    e.setVideo_path(new_video_path);
                try {
                    xUtilsDb.DB.update(
                            e,
                            new String[]{VideoCaptureEntity.VIDEO_NAME, VideoCaptureEntity.VIDEO_PATH});
                    VideoCaptureManager.deleteStationByPath(new_video_path);
                } catch (DbException ex) {
                    ex.printStackTrace();
                    return -1;
                }
                return 1;
            } else {// 找不到数据
                return -1;
            }
        }
    }

    /**
     * 保存文件路径，文件名
     */
    public static int updateNamePathByPath(String video_path,
                                           String new_video_name,
                                           String new_video_path) {

        VideoCaptureEntity entity = findByName(new_video_name);
        if (entity != null) {// 如果存在重名，返回告知用户
            return 0;
        } else {// 修改数据库中的视频名称
            VideoCaptureEntity e = findByPath(video_path);
            if (e != null) {
                if (new_video_name != null)
                    e.setVideo_name(new_video_name);
                if (new_video_path != null)
                    e.setVideo_path(new_video_path);
                try {
                    xUtilsDb.DB.update(e, new String[]{VideoCaptureEntity.VIDEO_NAME, VideoCaptureEntity.VIDEO_PATH});
                } catch (DbException ex) {
                    ex.printStackTrace();
                    return -1;
                }
                return 1;
            } else {// 找不到数据
                return -1;
            }
        }
    }

    /**
     * 保存文件名
     */
    public static boolean updateNameByPath(String video_path,
                                           String video_name) {

        VideoCaptureEntity e = findByPath(video_path);
        if (e != null) {
            if (video_name != null)
                e.setVideo_name(video_name);
            try {
                xUtilsDb.DB.update(e, new String[]{VideoCaptureEntity.VIDEO_NAME});
                return true;
            } catch (DbException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 保存游戏，活动，标签
     */
    public static boolean updateMatchGameTagsByPath(String video_path,
                                                    String game_id,
                                                    String game_name,
                                                    String match_id,
                                                    String match_name,
                                                    List<String> game_tags,
                                                    List<Tag> tags) {
        Log.d(TAG, "updateMatchGameTagsByPath: video_path=" + video_path);
        Log.d(TAG, "updateMatchGameTagsByPath: game_id=" + game_id);
        Log.d(TAG, "updateMatchGameTagsByPath: game_name=" + game_name);
        Log.d(TAG, "updateMatchGameTagsByPath: match_id=" + match_id);
        Log.d(TAG, "updateMatchGameTagsByPath: match_name=" + match_name);
        Log.d(TAG, "updateMatchGameTagsByPath: game_tags=" + game_tags);
        Log.d(TAG, "updateMatchGameTagsByPath: tags=" + tags);

        VideoCaptureEntity e = findByPath(video_path);
        if (e != null) {
            if (game_id != null)
                e.setGame_id(game_id);
            if (game_name != null)
                e.setGame_name(game_name);
            if (match_id != null)
                e.setMatch_id(match_id);
            if (match_name != null)
                e.setMatch_name(match_name);
            if (game_tags != null && game_tags.size() > 0)
                e.setUpvideo_gametags_2(game_tags);
            if (tags != null && tags.size() > 0)
                e.setUpvideo_tags_2(tags);
            try {
                xUtilsDb.DB.update(e,
                        new String[]{VideoCaptureEntity.GAME_ID,
                                VideoCaptureEntity.GAME_NAME,
                                VideoCaptureEntity.MATCH_ID,
                                VideoCaptureEntity.MATCH_NAME,
                                VideoCaptureEntity.UPVIDEO_GAMETAGS,
                                VideoCaptureEntity.UPVIDEO_TAGS});
                return true;
            } catch (DbException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }



    /**
     * 保存封面
     */
    public static void updateCoverByPath(String video_path,
                                         String image_path) {
        VideoCaptureEntity entity = findByPath(video_path);
        if (entity != null) {
            entity.setImage_path(image_path);
            try {
                xUtilsDb.DB.update(
                        entity,
                        new String[]{VideoCaptureEntity.IMAGE_PATH});
            } catch (DbException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 保存分享状态
     *
     * @param share_flag true:已经分享
     *                   false：还没分享
     */
    public static void updateShareByPath(String video_path,
                                         boolean share_flag) {
        VideoCaptureEntity entity = findByPath(video_path);
        if (entity != null) {
            entity.setShare_flag(share_flag);
            try {
                xUtilsDb.DB.update(
                        entity,
                        new String[]{VideoCaptureEntity.SHARE_FLAG});
            } catch (DbException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 保存上传状态
     */
    public static void updateStationByQnkey(String upvideo_qnkey,
                                            String video_station) {

        VideoCaptureEntity entity = findByQnkey(upvideo_qnkey);
        if (entity != null) {
            if (!StringUtil.isNull(upvideo_qnkey))
                entity.setVideo_station(video_station);
            try {
                xUtilsDb.DB.update(
                        entity,
                        new String[]{VideoCaptureEntity.VIDEO_STATION});
            } catch (DbException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 保存上传状态
     */
    public static void updateStationByPath(String video_path,
                                           String video_station) {

        VideoCaptureEntity entity = findByPath(video_path);
        if (entity != null) {
            entity.setVideo_station(video_station);
            try {
                xUtilsDb.DB.update(
                        entity,
                        new String[]{VideoCaptureEntity.VIDEO_STATION});
            } catch (DbException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 保存上传状态
     */
    public synchronized final static void updateStationByPath(String video_path,
                                                              double upvideo_precent) {

        VideoCaptureEntity entity = findByPath(video_path);
        if (entity != null) {
            int p = (int) upvideo_precent * 100;
            if (p >= 0 && p <= 100)
                entity.setVideo_station(VideoCaptureEntity.VIDEO_STATION_UPLOADING);
            entity.setUpvideo_precent(upvideo_precent);
            try {
                xUtilsDb.DB.update(
                        entity,
                        new String[]{VideoCaptureEntity.VIDEO_STATION,
                                VideoCaptureEntity.UPVIDEO_PRECENT});
            } catch (DbException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 保存上传状态
     */
    public static void updateStationByPath204(String video_path,
                                           String pk_id,
                                           String member_id,
                                           String game_id,
                                           String upvideo_title,
                                           String upvideo_description) {

        VideoCaptureEntity entity = findByPath(video_path);
        if (entity != null) {

            if (pk_id != null)
                entity.setPk_id(pk_id);
            if (member_id != null)
                entity.setMember_id(member_id);
            if (game_id != null)
                entity.setGame_id(game_id);
            if (upvideo_title != null)
                entity.setUpvideo_title(upvideo_title);
            if (upvideo_description != null)
                entity.setUpvideo_description(upvideo_description);
            try {
                xUtilsDb.DB.update(
                        entity,
                        new String[]{VideoCaptureEntity.PK_ID,
                                VideoCaptureEntity.MEMBER_ID,
                                VideoCaptureEntity.GAME_ID,
                                VideoCaptureEntity.UPVIDEO_TITLE,
                                VideoCaptureEntity.UPVIDEO_DESCRIPTION,});
            } catch (DbException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 保存上传状态
     */
    public static void updateStationByPath(String video_path,
                                           String share_channel,
                                           String member_id,
                                           String game_id,
                                           String match_id,
                                           String upvideo_title,
                                           String upvideo_description,
                                           int upvideo_isofficial,
                                           List<String> game_tags) {

        VideoCaptureEntity entity = findByPath(video_path);
        if (entity != null) {
            if (share_channel != null)
                entity.setShare_channel(share_channel);
            if (member_id != null)
                entity.setMember_id(member_id);
            if (game_id != null)
                entity.setGame_id(game_id);
            if (match_id != null)
                entity.setMatch_id(match_id);
            if (upvideo_title != null)
                entity.setUpvideo_title(upvideo_title);
            if (upvideo_description != null)
                entity.setUpvideo_description(upvideo_description);
            entity.setUpvideo_isofficial(upvideo_isofficial);
            if (game_tags != null && game_tags.size() > 0)
                entity.setUpvideo_gametags_2(game_tags);
            try {
                xUtilsDb.DB.update(
                        entity,
                        new String[]{VideoCaptureEntity.SHARE_CHANNEL,
                                VideoCaptureEntity.MEMBER_ID,
                                VideoCaptureEntity.GAME_ID,
                                VideoCaptureEntity.MATCH_ID,
                                VideoCaptureEntity.UPVIDEO_TITLE,
                                VideoCaptureEntity.UPVIDEO_DESCRIPTION ,
                                VideoCaptureEntity.UPVIDEO_ISOFFICIAL  ,
                                VideoCaptureEntity.UPVIDEO_GAMETAGS});
            } catch (DbException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 保存上传状态
     */
    public static void updateStationByPath(String video_path,
                                           String join_id,
                                           String upvideo_id,
                                           String upvideo_qnkey,
                                           String upvideo_key,
                                           String upvideo_token,
                                           String upvideo_flag,
                                           String upvideo_covertoken) {

        VideoCaptureEntity entity = findByPath(video_path);

        if (entity != null) {
            if (join_id != null)
                entity.setJoin_id(join_id);
            if (upvideo_id != null)
                entity.setUpvideo_id(upvideo_id);
            if (upvideo_qnkey != null)
                entity.setUpvideo_qnkey(upvideo_qnkey);
            if (upvideo_key != null)
                entity.setUpvideo_key(upvideo_key);
            if (upvideo_flag != null)
                entity.setUpvideo_flag(upvideo_flag);
            if (upvideo_token != null) {
                entity.setUpvideo_token(upvideo_token);
                long l = System.currentTimeMillis();
                entity.setUpvideo_tokentime(l);
            }
            if (upvideo_covertoken != null) {
                entity.setUpvideo_covertoken(upvideo_covertoken);
            }
            try {
                xUtilsDb.DB.update(
                        entity,
                        new String[]{VideoCaptureEntity.JOIN_ID,
                                VideoCaptureEntity.UPVIDEO_ID,
                                VideoCaptureEntity.UPVIDEO_QNKEY,
                                VideoCaptureEntity.UPVIDEO_KEY,
                                VideoCaptureEntity.UPVIDEO_TOKEN,
                                VideoCaptureEntity.UPVIDEO_FLAG,
                                VideoCaptureEntity.UPVIDEO_COVERTOKEN,
                                VideoCaptureEntity.UPVIDEO_TOKENTIME });
            } catch (DbException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 保存上传状态
     */
    public static void updateStationByPath(String video_path,
                                           String share_channel,
                                           String member_id,
                                           String game_id,
                                           String match_id,
                                           String upvideo_title,
                                           String upvideo_description,
                                           int upvideo_isofficial) {

        VideoCaptureEntity entity = findByPath(video_path);
        if (entity != null) {
            if (share_channel != null)
                entity.setShare_channel(share_channel);
            if (member_id != null)
                entity.setMember_id(member_id);
            if (game_id != null)
                entity.setGame_id(game_id);
            if (match_id != null)
                entity.setMatch_id(match_id);
            if (upvideo_title != null)
                entity.setUpvideo_title(upvideo_title);
            if (upvideo_description != null)
                entity.setUpvideo_description(upvideo_description);
            entity.setUpvideo_isofficial(upvideo_isofficial);
            try {
                xUtilsDb.DB.update(
                        entity,
                        new String[]{VideoCaptureEntity.SHARE_CHANNEL,
                                VideoCaptureEntity.MEMBER_ID,
                                VideoCaptureEntity.GAME_ID,
                                VideoCaptureEntity.MATCH_ID,
                                VideoCaptureEntity.UPVIDEO_TITLE,
                                VideoCaptureEntity.UPVIDEO_DESCRIPTION,
                                VideoCaptureEntity.UPVIDEO_ISOFFICIAL});
            } catch (DbException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 保存上传状态
     */
    public static void updateStationByPath(String video_path,
                                           String join_id,
                                           String upvideo_id,
                                           String upvideo_qnkey,
                                           String upvideo_key,
                                           String upvideo_token) {

        VideoCaptureEntity entity = findByPath(video_path);

        if (entity != null) {
            if (join_id != null)
                entity.setJoin_id(join_id);
            if (upvideo_id != null)
                entity.setUpvideo_id(upvideo_id);
            if (upvideo_qnkey != null)
                entity.setUpvideo_qnkey(upvideo_qnkey);
            if (upvideo_key != null)
                entity.setUpvideo_key(upvideo_key);
            if (upvideo_token != null) {
                entity.setUpvideo_token(upvideo_token);
                long l = System.currentTimeMillis();
                entity.setUpvideo_tokentime(l);
            }
            try {
                xUtilsDb.DB.update(
                        entity,
                        new String[]{VideoCaptureEntity.JOIN_ID,
                                VideoCaptureEntity.UPVIDEO_ID,
                                VideoCaptureEntity.UPVIDEO_QNKEY,
                                VideoCaptureEntity.UPVIDEO_KEY,
                                VideoCaptureEntity.UPVIDEO_TOKEN,
                                VideoCaptureEntity.UPVIDEO_TOKENTIME});
            } catch (DbException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 删除上传状态
     */
    public static void deleteStationByPath(String video_path) {

        VideoCaptureEntity entity = findByPath(video_path);

        if (entity != null) {
            entity.setVideo_station(VideoCaptureEntity.VIDEO_STATION_LOCAL);
            entity.setUpvideo_precent(0d);
            entity.setJoin_id("");
            entity.setUpvideo_id("");
            entity.setUpvideo_qnkey("");
            entity.setUpvideo_key("");
            entity.setUpvideo_token("");
            entity.setUpvideo_tokentime(0l);
            try {
                xUtilsDb.DB.update(
                        entity,
                        new String[]{VideoCaptureEntity.VIDEO_STATION,
                                VideoCaptureEntity.UPVIDEO_PRECENT,

                                VideoCaptureEntity.SHARE_CHANNEL,
                                VideoCaptureEntity.MEMBER_ID,
                                VideoCaptureEntity.GAME_ID,
                                VideoCaptureEntity.MATCH_ID,
                                VideoCaptureEntity.UPVIDEO_TITLE,
                                VideoCaptureEntity.UPVIDEO_DESCRIPTION,

                                VideoCaptureEntity.JOIN_ID,
                                VideoCaptureEntity.UPVIDEO_ID,
                                VideoCaptureEntity.UPVIDEO_QNKEY,
                                VideoCaptureEntity.UPVIDEO_KEY,
                                VideoCaptureEntity.UPVIDEO_TOKEN,
                                VideoCaptureEntity.UPVIDEO_TOKENTIME});
            } catch (DbException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 移除上传信息
     */
    public static void removeStationByPath(String video_path) {
        VideoCaptureEntity entity = findByPath(video_path);
        if (entity != null) {
            entity.setVideo_station(VideoCaptureEntity.VIDEO_STATION_LOCAL);
            entity.setUpvideo_precent(0d);
            entity.setShare_channel("");
            entity.setShare_flag(false);
            entity.setMember_id("");
            entity.setGame_id("");
            entity.setMatch_id("");
            entity.setJoin_id("");
            entity.setUpvideo_title("");
            entity.setUpvideo_description("");
            entity.setUpvideo_isofficial(0);
            entity.setUpvideo_id("");
            entity.setUpvideo_key("");
            entity.setUpvideo_qnkey("");
            entity.setUpvideo_token("");
            entity.setUpvideo_tokentime(0l);
            try {
                xUtilsDb.DB.update(
                        entity,
                        new String[]{VideoCaptureEntity.VIDEO_STATION,
                                VideoCaptureEntity.UPVIDEO_PRECENT,
                                VideoCaptureEntity.SHARE_CHANNEL,
                                VideoCaptureEntity.SHARE_FLAG,
                                VideoCaptureEntity.MEMBER_ID,
                                VideoCaptureEntity.GAME_ID,
                                VideoCaptureEntity.MATCH_ID,
                                VideoCaptureEntity.JOIN_ID,
                                VideoCaptureEntity.UPVIDEO_TITLE,
                                VideoCaptureEntity.UPVIDEO_DESCRIPTION,
                                VideoCaptureEntity.UPVIDEO_ISOFFICIAL,
                                VideoCaptureEntity.UPVIDEO_ID,
                                VideoCaptureEntity.UPVIDEO_KEY,
                                VideoCaptureEntity.UPVIDEO_QNKEY,
                                VideoCaptureEntity.UPVIDEO_TOKEN,
                                VideoCaptureEntity.UPVIDEO_TOKENTIME});
            } catch (DbException ex) {
                ex.printStackTrace();
            }
        }
    }

}
