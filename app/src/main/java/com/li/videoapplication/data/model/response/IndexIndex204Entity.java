package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.Banner;
import com.li.videoapplication.data.model.entity.GameGroup;
import com.li.videoapplication.data.model.entity.MemberGroup;
import com.li.videoapplication.data.model.entity.VideoImageGroup;
import com.li.videoapplication.framework.BaseResponse2Entity;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 * 接口實體類：首頁
 */
@SuppressWarnings("serial")
public class IndexIndex204Entity extends BaseResponseEntity {
	
	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
	
	public class Data extends BaseResponse2Entity {
		
		// 廣告
		private List<Banner> banner;
		
		// 熱門遊戲
		private GameGroup hotGame;
		
		// 猜你喜歡
		private VideoImageGroup guessVideo;
		
		// 视界原创
		private VideoImageGroup sysjVideo;
		
		// 熱門解說
		private MemberGroup hotMemberVideo;
		
		// 游戏类型视频
		private List<VideoImageGroup> videoList;

		public List<Banner> getBanner() {
			return banner;
		}

		public void setBanner(List<Banner> banner) {
			this.banner = banner;
		}

		public GameGroup getHotGame() {
			return hotGame;
		}

		public void setHotGame(GameGroup hotGame) {
			this.hotGame = hotGame;
		}

		public VideoImageGroup getGuessVideo() {
			return guessVideo;
		}

		public void setGuessVideo(VideoImageGroup guessVideo) {
			this.guessVideo = guessVideo;
		}

		public VideoImageGroup getSysjVideo() {
			return sysjVideo;
		}

		public void setSysjVideo(VideoImageGroup sysjVideo) {
			this.sysjVideo = sysjVideo;
		}

		public MemberGroup getHotMemberVideo() {
			return hotMemberVideo;
		}

		public void setHotMemberVideo(MemberGroup hotMemberVideo) {
			this.hotMemberVideo = hotMemberVideo;
		}

		public List<VideoImageGroup> getVideoList() {
			return videoList;
		}

		public void setVideoList(List<VideoImageGroup> videoList) {
			this.videoList = videoList;
		}
	}
}
