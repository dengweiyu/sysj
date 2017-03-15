package com.li.videoapplication.data.model.event;

import android.content.Intent;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 事件：下载完成事件
 */
@SuppressWarnings("serial")
public class DownloadCompleteEvent extends BaseEntity {

    private Intent intent;

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }
}
