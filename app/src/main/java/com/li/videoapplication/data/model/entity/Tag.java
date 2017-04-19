package com.li.videoapplication.data.model.entity;


import android.os.Parcel;
import android.os.Parcelable;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 实体类：标签
 */
@SuppressWarnings("serial")
public class Tag extends BaseEntity implements Parcelable {

	private String game_tag_id;
	private String name;
	private String location;

	public String getGame_tag_id() {
		return game_tag_id;
	}

	public void setGame_tag_id(String game_tag_id) {
		this.game_tag_id = game_tag_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.game_tag_id);
		dest.writeString(this.name);
		dest.writeString(this.location);
	}

	public Tag() {
	}

	protected Tag(Parcel in) {
		this.game_tag_id = in.readString();
		this.name = in.readString();
		this.location = in.readString();
	}

	public static final Parcelable.Creator<Tag> CREATOR = new Parcelable.Creator<Tag>() {
		@Override
		public Tag createFromParcel(Parcel source) {
			return new Tag(source);
		}

		@Override
		public Tag[] newArray(int size) {
			return new Tag[size];
		}
	};
}
