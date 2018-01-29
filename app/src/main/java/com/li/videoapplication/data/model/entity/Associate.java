package com.li.videoapplication.data.model.entity;

import com.li.videoapplication.framework.BaseEntity;
import com.li.videoapplication.ui.popupwindows.gameselect.IInfoEntity;

/**
 * 实体类：游戏圈子
 */
@SuppressWarnings("serial")
public class Associate extends BaseEntity implements IInfoEntity{

	private String id;
	private String game_id;
	private String group_id;
	private String game_name;
	private String flag;
	private String name;
	private boolean isLife;

	public boolean isLife() {
		return isLife;
	}

	public void setLife(boolean life) {
		isLife = life;
	}

	public String getName() {
		return name;
	}

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGame_id() {
		return game_id;
	}

	public void setGame_id(String game_id) {
		this.game_id = game_id;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getGame_name() {
		return game_name;
	}

	public void setGame_name(String game_name) {
		this.game_name = game_name;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
}

