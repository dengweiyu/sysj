package com.li.videoapplication.data.model.entity;

import com.li.videoapplication.framework.BaseEntity;
/**
 * 实体类：礼包
 */
@SuppressWarnings("serial")
public class Gift extends BaseEntity {

	public static final String STATUS_GET = "领取";
	public static final String STATUS_FINISHED = "已结束";
	public static final String STATUS_GETTED = "已领取";
	public static final String STATUS_BEGIN = "即将开始";

	public static final String SCOPE_IOS = "0";
	public static final String SCOPE_ANDROID = "1";
	public static final String SCOPE_BOTH = "2";
	
	private String id;
	private String title;
	private String game_id;
	private String scope = "";//(0为IOS，1为Android,2为两平台)
	private String content;
	private String count;
	private String trade_type;
	private String starttime;
	private String endtime;
	private String addtime;
	private String click;
	private String uploadtime;
	private String pic_hd;
	private String pic_mrgx;
	private String pic_pld;
	private String pic_tj;
	private String pic_625_310;
	private String num;
	private String is_forever;
	private String activity_code;
	private String flagPath;
	private String flag;
	private String status = "";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGame_id() {
		return game_id;
	}

	public void setGame_id(String game_id) {
		this.game_id = game_id;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getClick() {
		return click;
	}

	public void setClick(String click) {
		this.click = click;
	}

	public String getUploadtime() {
		return uploadtime;
	}

	public void setUploadtime(String uploadtime) {
		this.uploadtime = uploadtime;
	}

	public String getPic_hd() {
		return pic_hd;
	}

	public void setPic_hd(String pic_hd) {
		this.pic_hd = pic_hd;
	}

	public String getPic_mrgx() {
		return pic_mrgx;
	}

	public void setPic_mrgx(String pic_mrgx) {
		this.pic_mrgx = pic_mrgx;
	}

	public String getPic_pld() {
		return pic_pld;
	}

	public void setPic_pld(String pic_pld) {
		this.pic_pld = pic_pld;
	}

	public String getPic_tj() {
		return pic_tj;
	}

	public void setPic_tj(String pic_tj) {
		this.pic_tj = pic_tj;
	}

	public String getPic_625_310() {
		return pic_625_310;
	}

	public void setPic_625_310(String pic_625_310) {
		this.pic_625_310 = pic_625_310;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getIs_forever() {
		return is_forever;
	}

	public void setIs_forever(String is_forever) {
		this.is_forever = is_forever;
	}

	public String getActivity_code() {
		return activity_code;
	}

	public void setActivity_code(String activity_code) {
		this.activity_code = activity_code;
	}

	public String getFlagPath() {
		return flagPath;
	}

	public void setFlagPath(String flagPath) {
		this.flagPath = flagPath;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
