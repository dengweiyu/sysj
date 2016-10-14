package com.li.videoapplication.data.model.entity;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 实体类：帮助与教程 问题解答
 */
public class HelpQuestionEntity extends BaseEntity {

	private String title;
    private String content;
    private int id;

	public int getId() {
		return id;
	}

	public void setId(int iD) {
		id = iD;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
