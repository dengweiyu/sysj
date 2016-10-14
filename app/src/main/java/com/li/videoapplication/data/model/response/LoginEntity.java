package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.framework.BaseResponseEntity;
/**
 * 事件：应用程序登录
 * @author z
 *
 */
@SuppressWarnings("serial")
public class LoginEntity extends BaseResponseEntity {
	
	private Member data;

	public Member getData() {
		return data;
	}

	public void setData(Member data) {
		this.data = data;
	}
}
