package com.fmsysj.zbqmcs.bean;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 上传视频信息类
 * 
 * @author WYX
 * 
 */
public class VideoUploadInfo extends BaseEntity {
	/** 视频本地地址 */
	public String filePath;
	/** 云端视频地址 */
	public String cloudPath;
	/** 分享内容 */
	public String shareContent;
	/** 视频标题 */
	public String videoTitle;
	/** 视频类型ID */
	public String gameId;
	/** 用户ID */
	public String memberID;
	/** 活动ID，可空 */
	public String match_id;
	/** 视频描述 */
	public String description;

}
