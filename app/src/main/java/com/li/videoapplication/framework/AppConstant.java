package com.li.videoapplication.framework;

/**
 * 功能：应用程序常量
 */
public class AppConstant {

	public static boolean DOWNLOAD = true;

	public static final String SYSJ = "sysj";
	public static final String API_SERVER = "http://apps.ifeimo.com";//服务器域名
	public static final String SYSJ_ANDROID = "a_sysj";
	public static final String SYSJ_IOS = "i_sysj";

	public static final boolean DEBUG = true;

	public static final int USER_COVER_WIDTH = 720;
	public static final int USER_COVER_HEIGHT = 480;
    
    /**
     * 七牛视频播放地址
     * @return
     */
    public static final String getQnUrl(String qn_key) {
		// http://video.17sysj.com/
		// http://7xiiwo.com2.z0.glb.qiniucdn.com/
		return "http://video.17sysj.com/" + qn_key;
    }
    
    /**
     * 优酷视频播放地址
     * http://player.youku.com/embed/XNjY1NzU1NjY4
     * http://v.youku.com/v_show/id_XNjY1NzU1NjY4.html
     * @return
     */
    public static final String getYoukuUrl(String url) {
		return "http://player.youku.com/embed/" + url;
    }

    /**
     * 视频分享地址
     * @return
     */
    public static final String getShareUrl(String qn_key) {
		return "http://www.17sysj.com/video/" + qn_key;
    }

	/**
	 * 网页视频播放地址
	 */
	public static final String getWebUrl(String qn_key) {
		return "http://www.17sysj.com/video/" + qn_key;
	}

	/**
	 * 七牛封面地址
	 */
	public static final String getCoverUrl(String flag) {
		return "http://cover.17sysj.com/" + flag;
	}

	/**
	 * 七牛视频播放地址
	 */
	public static final String getImageUrlDef() {
		return "http://apps.ifeimo.com/Public/Uploads/Video/Flag/default.jpg";
	}

	/**
	 * 网页视频播放地址
	 * @return
	 */
	public static final String getMUrl(String qn_key) {
		return "http://m.17sysj.com/video/" + qn_key;
	}

    /**
     * 网页视频播放地址
     * @return
     */
    public static final String getWapUrl(String qn_key) {
		return "http://wap.17sysj.com/video/" + qn_key;
    }

	/**
	 * 赛事分享地址
	 * @return
	 */
	public static final String getEventUrl(String event_id) {
		return "http://www.17sysj.com/event/detail/id/" + event_id;
	}

    /*网站链接*/
    public static final String webURL = "http://www.17sysj.com/";
	
	/**
	 * 功能：各个页面网络加载延迟时间
	 */
	public static final class TIME {
		
		public static final int WELFARE_ACTIVITY = 0;
		public static final int WELFARE_GIFT = 400;
		
		public static final int MYACTIVITY_FRAGMENT = 0;
		public static final int MYGIFT_FRAGMENT = 400;

		public static final int GAME_CLASSIFIED = 0;
		public static final int GAME_MY = 400;

		public static final int SQUARE_NEW = 0;
		public static final int SQUARE_HOT = 400;

		public static final int GAMEDETAIL_SECOND = 200;
		public static final int GAMEDETAIL_THIRD = 400;
		public static final int GAMEDETAIL_FOURTH = 600;

		public static final int SEARCH_VIDEO = 0;
		public static final int SEARCH_GIFT = 200;
		public static final int SEARCH_MEMBER = 400;
	}
}
