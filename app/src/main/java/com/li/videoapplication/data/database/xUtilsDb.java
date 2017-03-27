package com.li.videoapplication.data.database;


import android.util.Log;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

public class xUtilsDb {

    private static final String TAG = xUtilsDb.class.getSimpleName();

    private final static String DATABASE_NAME = "video_capture.db";

    /**
     * 数据库版本：V4（2.1.4）
     */
    private final static int DATABASE_VERSION = 4;

    private static final DbManager.DaoConfig CONFIG = new DbManager.DaoConfig()
            .setDbName(DATABASE_NAME)
            .setDbVersion(DATABASE_VERSION)
            .setDbOpenListener(new DbManager.DbOpenListener() {
                @Override
                public void onDbOpened(DbManager db) {
                    // 开启WAL, 对写入加速提升巨大
                    db.getDatabase().enableWriteAheadLogging();
                }
            })
            .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    // db.addColumn(...);
                    // db.dropTable(...);
                    // db.dropDb();
                    if (oldVersion < 3){
                        addIsofficial(db);
                        addPk_id(db);
                    } else if (oldVersion == 3){
                        // V4增加字段
                        addColumnV4(db);
                    }
                }
            });

    /**
     * V2增加字段
     */
    private static void addIsofficial(DbManager db) {
        try {
            db.addColumn(VideoCaptureEntity.class, VideoCaptureEntity.UPVIDEO_ISOFFICIAL);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * V3增加字段
     */
    private static void addPk_id(DbManager db) {
        try {
            db.addColumn(VideoCaptureEntity.class, VideoCaptureEntity.PK_ID);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public static final DbManager DB = x.getDb(CONFIG);

    /**
     * V4增加字段：
     *      upvideo_gametags
     *      upvideo_tags
     *      upvideo_covertoken
     *      upvideo_flag
     *      game_name
     *      match_name
     */
    private static void addColumnV4(DbManager db) {
        Log.d(TAG, "addColumnV4: // ----------------------------------------------------");
        try {
            db.addColumn(VideoCaptureEntity.class, VideoCaptureEntity.UPVIDEO_GAMETAGS);
        } catch (DbException e) {
            e.printStackTrace();
        }
        try {
            db.addColumn(VideoCaptureEntity.class, VideoCaptureEntity.UPVIDEO_TAGS);
        } catch (DbException e) {
            e.printStackTrace();
        }
        try {
            db.addColumn(VideoCaptureEntity.class, VideoCaptureEntity.UPVIDEO_COVERTOKEN);
        } catch (DbException e) {
            e.printStackTrace();
        }
        try {
            db.addColumn(VideoCaptureEntity.class, VideoCaptureEntity.UPVIDEO_FLAG);
        } catch (DbException e) {
            e.printStackTrace();
        }
        try {
            db.addColumn(VideoCaptureEntity.class, VideoCaptureEntity.GAME_NAME);
        } catch (DbException e) {
            e.printStackTrace();
        }
        try {
            db.addColumn(VideoCaptureEntity.class, VideoCaptureEntity.MATCH_NAME);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
