package com.li.videoapplication.data.model.response;

import java.util.List;

import com.li.videoapplication.data.model.entity.GameGroup;
import com.li.videoapplication.data.model.entity.Banner;
import com.li.videoapplication.data.model.entity.MemberGroup;
import com.li.videoapplication.data.model.entity.VideoImageGroup;
import com.li.videoapplication.framework.BaseResponseEntity;
import com.li.videoapplication.framework.BaseResponse2Entity;
/**
 * 接口實體類：首頁統一接口
 * @author z
 *
 */
@SuppressWarnings("serial")
public class IndexIndexEntity extends BaseResponseEntity {
	
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
		
		// 舞大大学堂，小小舞玩新游，阿沫爱品评
		private List<VideoImageGroup> companyMemberVideo;
		
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

		public List<VideoImageGroup> getCompanyMemberVideo() {
			return companyMemberVideo;
		}

		public void setCompanyMemberVideo(List<VideoImageGroup> companyMemberVideo) {
			this.companyMemberVideo = companyMemberVideo;
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
