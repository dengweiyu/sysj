package com.li.videoapplication.data.model.entity;

import java.util.List;

import com.li.videoapplication.framework.BaseResponse2Entity;

/**
 * 实体类：视频分组
 */
@SuppressWarnings("serial")
public class VideoImageGroup extends BaseResponse2Entity {

	private String title;
	private String pic;
	private List<VideoImage> list;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public List<VideoImage> getList() {
		return list;
	}

	public void setList(List<VideoImage> list) {
		this.list = list;
	}

	private String video_ids;

	public String getVideo_ids() {
		return video_ids;
	}

	public void setVideo_ids(String video_ids) {
		this.video_ids = video_ids;
	}

	private String icon_pic;
	private String more_mark;

	public String getIcon_pic() {
		return icon_pic;
	}

	public void setIcon_pic(String icon_pic) {
		this.icon_pic = icon_pic;
	}

	public String getMore_mark() {
		return more_mark;
	}

	public void setMore_mark(String more_mark) {
		this.more_mark = more_mark;
	}

	/**
	 * 视界原创
	 */
	public static final int TYPE_FIRST = 1;

	/**
	 * 热门解说
	 */
	public static final int TYPE_SECOND = 2;

	/**
	 * 新游推荐
	 */
	public static final int TYPE_THIRD = 3;

	private int type = TYPE_FIRST;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	private List<Member> members;

	public List<Member> getMembers() {
		return members;
	}

	public void setMembers(List<Member> members) {
		this.members = members;
	}
}
