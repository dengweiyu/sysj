package com.li.videoapplication.data.model.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.li.videoapplication.framework.BaseEntity;
import com.li.videoapplication.utils.StringUtil;
/**
 * 实体类：登录个人信息
 */
@SuppressWarnings("serial")
public class Member extends BaseEntity {
	
	private String id;
	private String member_grade;
	private String name;
	private String nickname;
	private String openid;
	private String password;
	private String pwd_origin;
	private String avatar;
	private String avatar_min;
	private String mobile;
	private String email;
	private String qq;
	private String isTelpass;
	private String isMailpass;
	private int sex;
	private String address;
	private String like_gametype;
	private String like_grouptype;
	private String time;
	private String isAdmin;
	private String logintime;
	private String loginip;
	private int degree;
	private String rank;
	private String regOrigin;
	private String attention;
	private String fans = "0";
	private String video_num;
	private String description;
	private String signature;
	private String like_gametypename;
	private String rank_id;
	private String exp;
	private String title;
	private int next_exp;
	private String taskNum;
	private String isForbid;
	private String msgNum;
	private int mark;
	private String attention_time;
	private int attentionMark;
	private String member_id;
	private int member_tick;
	private int tick;
	private int display;
	private String cover;
	private int isAttent;
	private String uploadVideoCount;
	private String NAME;
	private String token;
	private boolean isV;	//指的是热门主播
	private String currency;
	private String win;
	private String failure;
	private String horizonId;

	private VIPInfo vipInfo;

	public VIPInfo getVipInfo() {
		return vipInfo;
	}

	public void setVipInfo(VIPInfo vipInfo) {
		this.vipInfo = vipInfo;
	}

	public static class VIPInfo{
		private String level;
		private String memeber;
		private String end_time;

		public String getLevel() {
			return level;
		}

		public void setLevel(String level) {
			this.level = level;
		}

		public String getMemeber() {
			return memeber;
		}

		public void setMemeber(String memeber) {
			this.memeber = memeber;
		}

		public String getEnd_time() {
			return end_time;
		}

		public void setEnd_time(String end_time) {
			this.end_time = end_time;
		}
	}

	public String getHorizonId() {
		return horizonId;
	}

	public void setHorizonId(String horizonId) {
		this.horizonId = horizonId;
	}

	public String getWin() {
		return win;
	}

	public void setWin(String win) {
		this.win = win;
	}

	public String getFailure() {
		return failure;
	}

	public void setFailure(String failure) {
		this.failure = failure;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public boolean isV() {
		return isV;
	}

	public void setV(boolean v) {
		isV = v;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMember_grade() {
		return member_grade;
	}

	public void setMember_grade(String member_grade) {
		this.member_grade = member_grade;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPwd_origin() {
		return pwd_origin;
	}

	public void setPwd_origin(String pwd_origin) {
		this.pwd_origin = pwd_origin;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getAvatar_min() {
		return avatar_min;
	}

	public void setAvatar_min(String avatar_min) {
		this.avatar_min = avatar_min;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getIsTelpass() {
		return isTelpass;
	}

	public void setIsTelpass(String isTelpass) {
		this.isTelpass = isTelpass;
	}

	public String getIsMailpass() {
		return isMailpass;
	}

	public void setIsMailpass(String isMailpass) {
		this.isMailpass = isMailpass;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLike_gametype() {
		return like_gametype;
	}

	public void setLike_gametype(String like_gametype) {
		this.like_gametype = like_gametype;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getLogintime() {
		return logintime;
	}

	public void setLogintime(String logintime) {
		this.logintime = logintime;
	}

	public String getLoginip() {
		return loginip;
	}

	public void setLoginip(String loginip) {
		this.loginip = loginip;
	}

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getRegOrigin() {
		return regOrigin;
	}

	public void setRegOrigin(String regOrigin) {
		this.regOrigin = regOrigin;
	}

	public String getAttention() {
		return attention;
	}

	public void setAttention(String attention) {
		this.attention = attention;
	}

	public String getFans() {
		return fans;
	}

	public void setFans(String fans) {
		this.fans = fans;
	}

	public String getVideo_num() {
		return video_num;
	}

	public void setVideo_num(String video_num) {
		this.video_num = video_num;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getLike_gametypename() {
		return like_gametypename;
	}

	public void setLike_gametypename(String like_gametypename) {
		this.like_gametypename = like_gametypename;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	public String getRank_id() {
		return rank_id;
	}

	public void setRank_id(String rank_id) {
		this.rank_id = rank_id;
	}

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getNext_exp() {
		return next_exp;
	}

	public void setNext_exp(int next_exp) {
		this.next_exp = next_exp;
	}

	public String getTaskNum() {
		return taskNum;
	}

	public void setTaskNum(String taskNum) {
		this.taskNum = taskNum;
	}

	public String getIsForbid() {
		return isForbid;
	}

	public void setIsForbid(String isForbid) {
		this.isForbid = isForbid;
	}

	public String getMsgNum() {
		return msgNum;
	}

	public void setMsgNum(String msgNum) {
		this.msgNum = msgNum;
	}

	public String getAttention_time() {
		return attention_time;
	}

	public void setAttention_time(String attention_time) {
		this.attention_time = attention_time;
	}

	public int getAttentionMark() {
		return attentionMark;
	}

	public void setAttentionMark(int attentionMark) {
		this.attentionMark = attentionMark;
	}

	public int getMember_tick() {
		return member_tick;
	}

	public void setMember_tick(int member_tick) {
		this.member_tick = member_tick;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public int getDisplay() {
		return display;
	}

	public void setDisplay(int display) {
		this.display = display;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}
	
	public String getLike_grouptype() {
		return like_grouptype;
	}

	public void setLike_grouptype(String like_grouptype) {
		this.like_grouptype = like_grouptype;
	}

	public int getIsAttent() {
		return isAttent;
	}

	public void setIsAttent(int isAttent) {
		this.isAttent = isAttent;
	}

	public String getUploadVideoCount() {
		return uploadVideoCount;
	}

	public void setUploadVideoCount(String uploadVideoCount) {
		this.uploadVideoCount = uploadVideoCount;
	}

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String nAME) {
		NAME = nAME;
	}

	public int getTick() {
		return tick;
	}

	public void setTick(int tick) {
		this.tick = tick;
	}

	public List<String> getLikeGroupType() {
		List<String> list = new ArrayList<String>();
		if (!StringUtil.isNull(this.getLike_grouptype())) {
			String[] arr = this.getLike_grouptype().split(",");
			list = Arrays.asList(arr);
		}
		return list;
	}

	public void setLikeGroupType(List<String> list) {
		
		StringBuffer sb = new StringBuffer();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (i < (list.size() - 1)) {
					sb.append(list.get(i));
					sb.append(",");
				} else {
					sb.append(list.get(i));
				}
			}
		}
		setLike_grouptype(sb.toString().trim() + "");
	}
}
