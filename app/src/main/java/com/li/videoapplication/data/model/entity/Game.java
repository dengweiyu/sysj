package com.li.videoapplication.data.model.entity;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 实体类：游戏圈子
 */
@SuppressWarnings("serial")
public class Game extends BaseEntity {
	
    private String id;
    private String group_id;
    private String group_name;
    private String name;
    private String attention_num;
    private String flag;
    private int tick;
    private String attentionTime;
    private String description;
    private String video_num;
    private String mark;
    private String game_id;
    private String gameName;
    private String type_name;
    private String time;
	private int is_gift;
    private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getIs_gift() {
		return is_gift;
	}

	public void setIs_gift(int is_gift) {
		this.is_gift = is_gift;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAttention_num() {
		return attention_num;
	}

	public void setAttention_num(String attention_num) {
		this.attention_num = attention_num;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getAttentionTime() {
		return attentionTime;
	}

	public void setAttentionTime(String attentionTime) {
		this.attentionTime = attentionTime;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVideo_num() {
		return video_num;
	}

	public void setVideo_num(String video_num) {
		this.video_num = video_num;
	}

	public String getGame_id() {
		return game_id;
	}

	public void setGame_id(String game_id) {
		this.game_id = game_id;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
    private String group_type_id;
	
    private String group_type_name;

	public String getGroup_type_id() {
		return group_type_id;
	}

	public void setGroup_type_id(String group_type_id) {
		this.group_type_id = group_type_id;
	}

	public String getGroup_type_name() {
		return group_type_name;
	}

	public void setGroup_type_name(String group_type_name) {
		this.group_type_name = group_type_name;
	}

	public int getTick() {
		return tick;
	}

	public void setTick(int tick) {
		this.tick = tick;
	}
	
    private String group_banner;
	
    private String game_description;
	
    private String a_download;
	
    private String i_download;

	public String getGroup_banner() {
		return group_banner;
	}

	public void setGroup_banner(String group_banner) {
		this.group_banner = group_banner;
	}

	public String getGame_description() {
		return game_description;
	}

	public void setGame_description(String game_description) {
		this.game_description = game_description;
	}

	public String getA_download() {
		return a_download;
	}

	public void setA_download(String a_download) {
		this.a_download = a_download;
	}

	public String getI_download() {
		return i_download;
	}

	public void setI_download(String i_download) {
		this.i_download = i_download;
	}
	
    private String day_data_num;

	public String getDay_data_num() {
		return day_data_num;
	}

	public void setDay_data_num(String day_data_num) {
		this.day_data_num = day_data_num;
	}
}

