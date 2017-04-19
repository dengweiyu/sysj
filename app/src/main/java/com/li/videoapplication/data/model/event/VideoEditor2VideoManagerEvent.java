package com.li.videoapplication.data.model.event;


import com.li.videoapplication.framework.BaseEntity;

/**
 * 组件间的通讯事件：本地视频
 */
@SuppressWarnings("serial")
public class VideoEditor2VideoManagerEvent extends BaseEntity {

    // 1：视频编辑
    // 2：视频分享
    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private String[] video_paths;

    public String[] getVideo_paths() {
        return video_paths;
    }

    public void setVideo_paths(String[] video_paths) {
        this.video_paths = video_paths;
    }
}
