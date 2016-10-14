package com.li.videoapplication.data.local;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 实体：截图
 */
public class ScreenShotEntity extends BaseEntity {

    /**
     * 显示名称
     */
	private  String displayName;

    /**
     * 文件本地路径
     */
	private String path;

    /**
     * 文件大小
     */
	private String size;

    /**
     * 文件最后修改时间
     */
	private long lastModified;

	public long getLastModified() {
		return lastModified;
	}

	public  void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public  String getDisplayName() {
		return displayName;
	}

	public  void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
