package com.li.videoapplication.data.database;

import com.li.videoapplication.framework.BaseEntity;
/**
 * 实体：视频（录屏，本地导入，编辑）
 */
public class VideoInfoEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String DisplayName; // 文件名
	private String Path; // 路径
	private int mtime;
	private long duration; // 时长
	private String thumbnail; // 缩略图位置
	private String title;// 标题
	private String gameName;// 上传时游戏类型名
	private String gameId;// 上传时游戏类型ID

	private String upvideotitle;// 上传时的游戏标题
	private double precent;// 上传时进度
	private String videoPlayChannel;// 播放平台（七牛/优酷)

	/** 活动ID，可为空 */
	private String join_id;

	public String getJoin_id() {
		return join_id;
	}

	public void setJoin_id(String join_id) {
		this.join_id = join_id;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	/** 视频描述 */
	private String videoDescribe;

	public String getVideoDescribe() {
		return videoDescribe;
	}

	public void setVideoDescribe(String videoDescribe) {
		this.videoDescribe = videoDescribe;
	}

	// 游戏类型ID
	private String GameTypeId;

	// 视频播放链接
	private String VideoURL; // 上传后的视频在服务器上的链接
	private String VideoId; // 数据库中的视频id
	private String videoSource; // rec/ext
	private String videoStation; // loc/ser/uploading(视频上传状态)
	// 图片链接
	private String imageUrl;
	private int id;

	// 视频上传七牛token
	private String Token;
	// 视频上传七牛token的时间(毫秒)
	private long TokenTime;

	public String getToken() {
		return Token;
	}

	public void setToken(String token) {
		Token = token;
	}

	public long getTokenTime() {
		return TokenTime;
	}

	public void setTokenTime(long tokenTime) {
		TokenTime = tokenTime;
	}

	public int getId() {
		return id;
	}

	public String getUpvideotitle() {
		return upvideotitle;
	}

	public void setUpvideotitle(String upvideotitle) {
		this.upvideotitle = upvideotitle;
	}

	public double getPrecent() {
		return precent;
	}

	public void setPrecent(double precent) {
		this.precent = precent;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGameTypeId() {
		return GameTypeId;
	}

	public String getGamename() {
		return gameName;
	}

	public void setGamename(String gamename) {
		this.gameName = gamename;
	}

	public void setGameTypeId(String gameTypeId) {
		GameTypeId = gameTypeId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	private String VideoSize;

	public String getVideoURL() {
		return VideoURL;
	}

	public void setVideoURL(String videoURL) {
		VideoURL = videoURL;
	}

	public int getTime() {
		return mtime;
	}

	public void setTime(int time) {
		mtime = time;
	}

	public String getPath() {
		return Path;
	}

	public void setPath(String path) {
		Path = path;
	}

	public String getDisplayName() {
		return DisplayName;
	}

	public void setDisplayName(String displayName) {
		DisplayName = displayName;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getVideoId() {
		return VideoId;
	}

	public void setVideoId(String videoId) {
		VideoId = videoId;
	}

	public String getVideoStation() {
		return videoStation;
	}

	public void setVideoStation(String videoStation) {
		this.videoStation = videoStation;
	}

	public void setVideoSource(String videoSource) {
		this.videoSource = videoSource;
	}

	public void setVideoSize(String VideoSize) {
		this.VideoSize = VideoSize;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VideoInfoEntity other = (VideoInfoEntity) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
