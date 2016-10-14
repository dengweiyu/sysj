package com.li.videoapplication.data.network;

/**
 * 功能：请求图片地址
 */
public class ImageUrl {

	public ImageUrl() {
		super();
	}

	private static ImageUrl instance;

	public static ImageUrl getInstance() {
		if (instance == null) {
			synchronized (ImageUrl.class) {
				if (instance == null) {
					instance = new ImageUrl();
				}
			}
		}
		return instance;
	}

	/* ############## 图片接口 ############## */
	
	/**
	 * 游戏圈子类型
	 */
	public String groupType(String imageFile) {
		return "http://apps.ifeimo.com/Public/Uploads/Video/Flag/" + imageFile;
	}
}
