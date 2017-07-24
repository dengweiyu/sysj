package com.li.videoapplication.data.model.entity;

import java.util.List;

import com.li.videoapplication.framework.BaseEntity;
import com.li.videoapplication.utils.StringUtil;

/**
 * 实体类：视频
 */
@SuppressWarnings("serial")
public class VideoImage extends BaseEntity {
	
	public String id = "";
	public String video_id = "";
	public String name;
	public String url;
	public String v_description;
	public String view_count="0";
	public String download_count="0";
	public String flag;
	public String flower_count="0";
	public String collection_count="0";
	public String comment_count="0";
	public String time_length;
	public String flagPath;
	public String member_id;
	public String avatar;
	public String nickname;
	public String video_name;
	public String video_time;
	public String qn_key;
	public String pic_flsp;
	public String attention_time;
	public String collection_time;
	public String flower_time;
	private String frequency;
	private String share_count;
	public int attentionMark;
	public int collectionMark;
	public int flowerMark;
	private boolean isV;
	private String more_mark;
	private String upload_time;
	private String personal_private;
	/*
	1：转码完成，可以播放
	3：转码中
	4：审核中*/
	public int state;
	public List<Tag> game_tag;
	private boolean isAD;
	private String orderId;
	private String rewardCount;
	private String buttonText;
	private int jumpStatus;

	private String isRecommend;

	public String getRewardCount() {
		return rewardCount;
	}

	public void setRewardCount(String rewardCount) {
		this.rewardCount = rewardCount;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getShare_count() {
		return share_count;
	}

	public void setShare_count(String share_count) {
		this.share_count = share_count;
	}

	public String getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(String isRecommend) {
		this.isRecommend = isRecommend;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getButtonText() {
		return buttonText;
	}

	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}

	public int getJumpStatus() {
		return jumpStatus;
	}

	public void setJumpStatus(int jumpStatus) {
		this.jumpStatus = jumpStatus;
	}

	public boolean isAD() {
		return isAD;
	}

	public void setAD(boolean AD) {
		isAD = AD;
	}

	public String getUpload_time() {
		return upload_time;
	}

	public void setUpload_time(String upload_time) {
		this.upload_time = upload_time;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public List<Tag> getGame_tag() {
		return game_tag;
	}

	public void setGame_tag(List<Tag> game_tag) {
		this.game_tag = game_tag;
	}

	public String getMore_mark() {
		return more_mark;
	}

	public void setMore_mark(String more_mark) {
		this.more_mark = more_mark;
	}

	public boolean isV() {
		return isV;
	}

	public void setV(boolean v) {
		isV = v;
	}

	public String getV_description() {
		return v_description;
	}

	public void setV_description(String v_description) {
		this.v_description = v_description;
	}

	public String getVideo_id() {
		return video_id;
	}

	public void setVideo_id(String video_id) {
		this.video_id = video_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getView_count() {
		return view_count;
	}

	public void setView_count(String view_count) {
		this.view_count = view_count;
	}

	public String getDownload_count() {
		return download_count;
	}

	public void setDownload_count(String download_count) {
		this.download_count = download_count;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getFlower_count() {
		return flower_count;
	}

	public void setFlower_count(String flower_count) {
		if (flower_count == null){
			flower_count = "0";
		}
		this.flower_count = flower_count;
	}

	public String getCollection_count() {
		return collection_count;
	}

	public void setCollection_count(String collection_count) {
		if (collection_count == null){
			collection_count = "0";
		}
		this.collection_count = collection_count;

	}

	public String getComment_count() {
		return comment_count;
	}

	public void setComment_count(String comment_count) {
		if (comment_count == null){
			comment_count = "0";
		}
		this.comment_count = comment_count;
	}

	public String getTime_length() {
		return time_length;
	}

	public void setTime_length(String time_length) {
		this.time_length = time_length;
	}

	public String getFlagPath() {
		return flagPath;
	}

	public void setFlagPath(String flagPath) {
		this.flagPath = flagPath;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getVideo_name() {
		return video_name;
	}

	public void setVideo_name(String video_name) {
		this.video_name = video_name;
	}

	public String getVideo_time() {
		return video_time;
	}

	public void setVideo_time(String video_time) {
		this.video_time = video_time;
	}

	public String getQn_key() {
		return qn_key;
	}

	public void setQn_key(String qn_key) {
		this.qn_key = qn_key;
	}

	public String getPic_flsp() {
		return pic_flsp;
	}

	public void setPic_flsp(String pic_flsp) {
		this.pic_flsp = pic_flsp;
	}

	public String getAttention_time() {
		return attention_time;
	}

	public void setAttention_time(String attention_time) {
		this.attention_time = attention_time;
	}

	public String getCollection_time() {
		return collection_time;
	}

	public void setCollection_time(String collection_time) {
		this.collection_time = collection_time;
	}

	public String getFlower_time() {
		return flower_time;
	}

	public void setFlower_time(String flower_time) {
		this.flower_time = flower_time;
	}

	public int getAttentionMark() {
		return attentionMark;
	}

	public void setAttentionMark(int attentionMark) {
		this.attentionMark = attentionMark;
	}

	public int getCollectionMark() {
		return collectionMark;
	}

	public void setCollectionMark(int collectionMark) {
		this.collectionMark = collectionMark;
	}

	public int getFlowerMark() {
		return flowerMark;
	}

	public void setFlowerMark(int flowerMark) {
		this.flowerMark = flowerMark;
	}
	
	public String group_data_id;
	public String pic_id = "";
	public String comment_id = "";
	public String uptime;
	public String up_time;
	public String title;
	public String click_count="0";
	public List<String> pic_urls;
	public int member_tick;
	public int flower_tick;
	public int collection_tick;
	public String video_flag;
	private String ispass;

	public String getIspass() {
		return ispass;
	}

	public void setIspass(String ispass) {
		this.ispass = ispass;
	}

	public String getComment_id() {
		return comment_id;
	}

	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}

	public String getUp_time() {
		return up_time;
	}

	public void setUp_time(String up_time) {
		this.up_time = up_time;
	}

	public String getGroup_data_id() {
		return group_data_id;
	}

	public void setGroup_data_id(String group_data_id) {
		this.group_data_id = group_data_id;
	}

	public String getPic_id() {
		return pic_id;
	}

	public void setPic_id(String pic_id) {
		this.pic_id = pic_id;
	}

	public String getUptime() {
		return uptime;
	}

	public void setUptime(String uptime) {
		this.uptime = uptime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getClick_count() {
		return click_count;
	}

	public void setClick_count(String click_count) {
		if (click_count == null){
			click_count = "0";
		}
		this.click_count = click_count;
	}

	public List<String> getPic_urls() {
		return pic_urls;
	}

	public void setPic_urls(List<String> pic_urls) {
		this.pic_urls = pic_urls;
	}

	public int getMember_tick() {
		return member_tick;
	}

	public void setMember_tick(int member_tick) {
		this.member_tick = member_tick;
	}

	public int getFlower_tick() {
		return flower_tick;
	}

	public void setFlower_tick(int flower_tick) {
		this.flower_tick = flower_tick;
	}

	public int getCollection_tick() {
		return collection_tick;
	}

	public void setCollection_tick(int collection_tick) {
		this.collection_tick = collection_tick;
	}

	public String getVideo_flag() {
		return video_flag;
	}

	public void setVideo_flag(String video_flag) {
		this.video_flag = video_flag;
	}
	
	public String yk_url;

	public String getYk_url() {
		return yk_url;
	}

	public void setYk_url(String yk_url) {
		this.yk_url = yk_url;
	}
	
	public String click_view;
	public String m_description;
	public String game_id;
	public String game_name;
	public String g_flag;
	public String g_description;
	public String g_content;
	public String android_address;
	public String apple_address;
	public String ios_review;

	public String getClick_view() {
		return click_view;
	}

	public void setClick_view(String click_view) {
		this.click_view = click_view;
	}

	public String getM_description() {
		return m_description;
	}

	public void setM_description(String m_description) {
		this.m_description = m_description;
	}

	public String getGame_id() {
		return game_id;
	}

	public void setGame_id(String game_id) {
		this.game_id = game_id;
	}

	public String getGame_name() {
		return game_name;
	}

	public void setGame_name(String game_name) {
		this.game_name = game_name;
	}

	public String getG_flag() {
		return g_flag;
	}

	public void setG_flag(String g_flag) {
		this.g_flag = g_flag;
	}

	public String getG_description() {
		return g_description;
	}

	public void setG_description(String g_description) {
		this.g_description = g_description;
	}

	public String getG_content() {
		return g_content;
	}

	public void setG_content(String g_content) {
		this.g_content = g_content;
	}

	public String getAndroid_address() {
		return android_address;
	}

	public void setAndroid_address(String android_address) {
		this.android_address = android_address;
	}

	public String getApple_address() {
		return apple_address;
	}

	public void setApple_address(String apple_address) {
		this.apple_address = apple_address;
	}

	public String getIos_review() {
		return ios_review;
	}

	public void setIos_review(String ios_review) {
		this.ios_review = ios_review;
	}
	
	private boolean playerFocus;

	public boolean isPlayerFocus() {
		return playerFocus;
	}

	public void setPlayerFocus(boolean playerFocus) {
		this.playerFocus = playerFocus;
	}
	
	public String gameicon;
	public String gamedesc;
	public String content;
	public String description;
	public String time;
	public String flower_num;
	public String isAttent;

	public String getGameicon() {
		return gameicon;
	}

	public void setGameicon(String gameicon) {
		this.gameicon = gameicon;
	}

	public String getGamedesc() {
		return gamedesc;
	}

	public void setGamedesc(String gamedesc) {
		this.gamedesc = gamedesc;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getFlower_num() {
		return flower_num;
	}

	public void setFlower_num(String flower_num) {
		this.flower_num = flower_num;
	}

	public String getIsAttent() {
		return isAttent;
	}

	public void setIsAttent(String isAttent) {
		this.isAttent = isAttent;
	}
	
	public String userName;
	public String gameName;
	public String typeName;
	public String sort;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String fndown_count="0";
	public int fndown_tick;

	public String getFndown_count() {
		return fndown_count;
	}

	public void setFndown_count(String fndown_count) {
		if (fndown_count == null){
			fndown_count = "0";
		}
		this.fndown_count = fndown_count;
	}

	public int getFndown_tick() {
		return fndown_tick;
	}

	public void setFndown_tick(int fndown_tick) {
		this.fndown_tick = fndown_tick;
	}

	public String totalClick;
	public String videocount;

	public String getVideocount() {
		return videocount;
	}

	public void setVideocount(String videocount) {
		this.videocount = videocount;
	}

	public String getTotalClick() {
		return totalClick;
	}

	public void setTotalClick(String totalClick) {
		this.totalClick = totalClick;
	}

	private String floor;

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getPersonal_private() {
		return personal_private;
	}

	public void setPersonal_private(String personal_private) {
		this.personal_private = personal_private;
	}
}
