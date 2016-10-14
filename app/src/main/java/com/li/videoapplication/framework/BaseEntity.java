package com.li.videoapplication.framework;

import java.io.Serializable;

import com.google.gson.Gson;

/**
 * 基本实体：
 */
@SuppressWarnings("serial")
public abstract class BaseEntity implements Serializable, Cloneable {

    protected final String tag = this.getClass().getSimpleName();
    protected final String action = this.getClass().getName();

	public String toJSON() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	@Override
	public String toString() {
		return this.toJSON();
	}

	public Object clone() {
		BaseEntity baseEntity = null;
		try {
			baseEntity = (BaseEntity) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return baseEntity;
	}
}
