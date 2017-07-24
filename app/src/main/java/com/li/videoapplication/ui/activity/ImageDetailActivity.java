package com.li.videoapplication.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Comment;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.response.AuthorVideoList2EntityImageDetail;
import com.li.videoapplication.data.model.response.MemberAttention201Entity;
import com.li.videoapplication.data.model.response.PhotoPhotoCommentListEntity;
import com.li.videoapplication.data.model.response.PhotoPhotoDetailEntity;
import com.li.videoapplication.data.model.response.PhotoSendCommentEntity;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.PullToRefreshHepler;
import com.li.videoapplication.tools.ShareSDKShareHelper;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.adapter.ImageDetailAdapter;
import com.li.videoapplication.ui.adapter.VideoPlayCommentAdapter;
import com.li.videoapplication.ui.adapter.VideoPlayVideoAdapter;
import com.li.videoapplication.ui.pageradapter.ImageDetailPagerAdapter;
import com.li.videoapplication.ui.fragment.ImageDetailCommentFragment;
import com.li.videoapplication.ui.fragment.ImageDetailVideoFragment;
import com.li.videoapplication.ui.fragment.VideoPlayIntroduceFragment;
import com.li.videoapplication.ui.view.CommentView;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.CircleImageView;
import com.li.videoapplication.views.GridViewY1;
import com.li.videoapplication.views.ListViewY1;
import com.li.videoapplication.views.ViewPagerY4;
import com.li.videoapplication.views.sparkbutton.SparkButton;

import java.util.ArrayList;
import java.util.List;
/**
 * 活动：图文详情
 */
@SuppressLint("WrongCall")
public class ImageDetailActivity extends TBaseActivity implements
				OnClickListener, 
				OnRefreshListener2<ScrollView>,
				OnPageChangeListener,
				CommentView.CommentListener{

    /**
	 * 跳转：玩家动态
	 */
	private void startPlayerDynamicActivity(Member member) {
		ActivityManager.startPlayerDynamicActivity(this, member);
	}

	/**
	 * 页面跳转：安装应用
	 */
	private void install() {
		if (item != null) {
			Uri uri = Uri.parse(item.getAndroid_address());
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
			overridePendingTransition(R.anim.activity_hold, R.anim.activity_hold);
		}
	}

	/**
	 * 跳转：图片浏览
	 */
	public void startImageViewActivity(String url) {
		ImageActivity.startImageActivityWeb(this, url);
	}

	private VideoImage item;

	public void setItem(VideoImage item) {
		if (!StringUtil.isNull(item.getVideo_id())) {
			String video_id = item.getVideo_id();
			item.setId(video_id);
		}
		this.item = item;
	}

	@Override
	public void refreshIntent() {
		super.refreshIntent();

		item = (VideoImage) getIntent().getSerializableExtra("item");
		if (item == null) {
			finish();
		}
	}

	@Override
	public int getContentView() {
		return R.layout.activity_imagedetail;
	}

	public int inflateActionBar() {
		return R.layout.actionbar_second;
	}

	@Override
	public void afterOnCreate() {
		super.afterOnCreate();
		ShareSDKShareHelper.initSDK(this);

		setSystemBarBackgroundWhite();
		setAbTitle(R.string.imagedetail_title);
	}

	@Override
	public void initView() {
		super.initView();

		initContentView();
		initCommentView();
		initTopMenu();
	}

	@Override
	public void loadData() {
		super.loadData();

		// 图文详情
		DataManager.photoPhotoDetail(item.getPic_id(), getMember_id());

		postDelayed(new Runnable() {

			@Override
			public void run() {
				commentPage = 1;
				// 图文评论列表
				DataManager.photoPhotoCommentList(item.getPic_id(), commentCount, commentPage);
			}
		}, 200);

		postDelayed(new Runnable() {

			@Override
			public void run() {
				videoPage = 1;
				// 用户视频列表2
				DataManager.authorVideoList2(new AuthorVideoList2EntityImageDetail(), item.getMember_id(), videoPage);
			}
		}, 400);

		viewPager.setCurrentItem(0);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ShareSDKShareHelper.stopSDK(this);
	}

	private PullToRefreshScrollView pullToRefreshScrollView;

	private CircleImageView head;
	private TextView name;
	private TextView time;
	private TextView focus;

	private TextView title;
	private TextView content;

	private TextView playCount;
	private SparkButton good;
	private TextView goodCount;
	private SparkButton bad;
	private TextView badCount;
	private SparkButton star;
	private TextView starCount;
	
	private ImageView image;
	private GridViewY1 imageGv;
	private ImageDetailAdapter imageAdapter;
	private List<String> imageData;

	private void initContentView() {

		pullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.top);
		pullToRefreshScrollView.setMode(Mode.PULL_FROM_END);
		pullToRefreshScrollView.setOnRefreshListener(this);

		head = (CircleImageView) findViewById(R.id.imagedetail_head);
		name = (TextView) findViewById(R.id.imagedetail_name);
		time = (TextView) findViewById(R.id.imagedetail_time);
		focus = (CheckBox) findViewById(R.id.imagedetail_focus);

		title = (TextView) findViewById(R.id.imagedetail_title);
		content = (TextView) findViewById(R.id.imagedetail_content);

		playCount = (TextView) findViewById(R.id.videoplay_playCount);
		good = (SparkButton) findViewById(R.id.videoplay_good);
		goodCount = (TextView) findViewById(R.id.videoplay_goodCount);
		bad = (SparkButton) findViewById(R.id.videoplay_bad);
		badCount = (TextView) findViewById(R.id.videoplay_badCount);
		star = (SparkButton) findViewById(R.id.videoplay_star);
		starCount = (TextView) findViewById(R.id.videoplay_starCount);

		abVideoPlayShare.setVisibility(View.GONE);
		abVideoPlayShare.setOnClickListener(this);

		bad.setVisibility(View.GONE);
		badCount.setVisibility(View.GONE);

		head.setOnClickListener(this);

		focus.setOnClickListener(this);
		good.setOnClickListener(this);
		star.setOnClickListener(this);
		
		image = (ImageView) findViewById(R.id.imagedetail_image);
		image.setOnClickListener(this);
		imageGv = (GridViewY1) findViewById(R.id.image_gridview);
		imageData = new ArrayList<>();
		imageAdapter = new ImageDetailAdapter(this, imageData);
		imageGv.setAdapter(imageAdapter);
	}
	
	private void switchMode(int index) {
		if (index == 0 || index == 1) {
			pullToRefreshScrollView.setMode(Mode.PULL_FROM_END);
		} else {
			pullToRefreshScrollView.setMode(Mode.DISABLED);
		}
	}

	private void setImageLayoutParams(ImageView view) {
		// 14 + 14 = 28
		// 320 - 28 = 226 + 66
		// 128/226
		int w = (srceenWidth - dp2px(28)) * 226 / (226 + 28);
		int h = w * 128 / 226;
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
		params.width = w;
		params.height = h;
		view.setLayoutParams(params);
	}

	private void setImagesLayoutParams(GridView view, int count) {
		// 14 + 14 = 28
		// 320 - 28 = 226 + 66
//		int w = (srceenWidth - dp2px(28)) * 226 / (226 + 28);
		int w = srceenWidth - dp2px(28) - dp2px(66);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
		params.width = w;
		params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
		view.setLayoutParams(params);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

		if (currIndex == 0) {
			commentPage = 1;
		} else if (currIndex == 1) {
			videoPage = 1;
		}
		onPullUpToRefresh(refreshView);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		PullToRefreshHepler.onRefreshCompleteDelayed(getHandler(), pullToRefreshScrollView, PullToRefreshActivity.TIME_REFRESH_SHORT);
		if (currIndex == 0) {
			// 图文评论列表
			DataManager.photoPhotoCommentList(item.getPic_id(), commentCount, commentPage);
		} else if (currIndex == 1) {
			// 用户视频列表2
			DataManager.authorVideoList2(new AuthorVideoList2EntityImageDetail(), item.getMember_id(), videoPage);
		}
	}

	private CommentView commentView;

	private void initCommentView() {

		commentView = (CommentView) findViewById(R.id.comment);
		commentView.init(this);
		commentView.setCommentListener(this);
	}

	@Override
	public boolean comment(boolean isSecondComment, String text) {
		// 图文发布评论
		DataManager.photoSendComment(item.getPic_id(), getMember_id(), text);
		return true;
	}

	@Override
	public void onClick(View v) {
		int flag;

		switch (v.getId()) {

		case R.id.imagedetail_head:// 头像
			startPlayerDynamicActivity(getMember(item));
			break;

		case R.id.imagedetail_image:// 图片
			List<String> urls = item.getPic_urls();
			if (urls != null && urls.size() > 0) {
				startImageViewActivity(item.getPic_urls().get(0));
			}
			break;

		case R.id.ab_videoplay_share:// 分享
			ShareSDKShareHelper.startImageDetailActivity(this, item);
			break;

		case R.id.imagedetail_focus:// 关注
			if (item.getMember_tick() == 0) {
				item.setMember_tick(1);
			} else {
				item.setMember_tick(0);
			}
			refreshContentView(item);
			// 玩家关注
			DataManager.memberAttention201(item.getMember_id(), getMember_id());
			break;

		case R.id.videoplay_star:// 收藏
			flag = item.getCollection_tick();
			if (flag == 0) {//未收藏
				star.setChecked(true);
				star.playAnimation();
				item.setCollection_tick(1);
				item.setCollection_count(Integer.valueOf(item.getCollection_count()) + 1 + "");

			} else {//已收藏
				star.setChecked(false);
				item.setCollection_tick(0);
				item.setCollection_count(Integer.valueOf(item.getCollection_count()) - 1 + "");
			}
			setTextViewText(starCount, item.getCollection_count());
			// 修改提交图文收藏
			DataManager.photoCollection(item.getPic_id(), getMember_id(), flag);
			break;

		case R.id.videoplay_good:// 点赞
			flag = item.getFlower_tick();
			if (flag == 0) {
				good.setChecked(true);
				good.playAnimation();
				item.setFlower_tick(1);
				item.setFlower_count(Integer.valueOf(item.getFlower_count()) + 1 + "");
			} else {
				int count = Integer.valueOf(item.getFlower_count()) - 1;
				good.setChecked(false);
				item.setFlower_tick(0);

				if (count < 0){
					count = 0;
				}
				item.setFlower_count(count + "");
			}
			setTextViewText(goodCount, item.getFlower_count());
			// 图文献花
			DataManager.photoFlower(item.getPic_id(), getMember_id(), flag);
			break;

		case R.id.videoplay_introduce_install:// 安装应用
			install();
			break;
		}
	}

	private Member getMember(VideoImage item) {
		Member member = new Member();
		member.setMember_id(item.getMember_id());
		member.setNickname(item.getNickname());
		member.setAvatar(item.getAvatar());
		return member;
	}

	private void refreshContentView(VideoImage item) {

		if (item != null) {
			setImageViewImageNet(head, item.getAvatar());
			setTextViewText(name, item.getNickname());
			setTextViewTextVisibility(content, "");

			setTextViewText(title, item.getTitle());
			setTextViewText(content, item.getM_description());

			setTextViewText(playCount, getPlayCount());
			setFocus(focus, item);
			setGood(good, item);
			setStar(star, item);
			setUptime(item);


			setTextViewText(goodCount, item.getFlower_count());
			setTextViewText(starCount, item.getCollection_count());
		}
	}

	/**
	 * 上传时间
	 */
	private void setUptime(VideoImage item) {
		// 16小时前上传
		try {
			setTextViewText(time, TimeHelper.getVideoImageUpTime(item.getUptime()));
		} catch (Exception e) {
			e.printStackTrace();
			setTextViewText(time, item.getUptime());
		}
	}

	/**
	 * 关注
	 */
	private void setFocus(TextView view, VideoImage item) {
		if (item != null) {
			if (item.getMember_tick() == 1) {// 已关注状态
				view.setBackgroundResource(R.drawable.focus_videoplay_gray);
				view.setText(R.string.videoplay_focused);
			} else {// 未关注状态
				view.setBackgroundResource(R.drawable.focus_videoplay_red);
				view.setText(R.string.videoplay_focus);
			}
		}
	}

	/**
	 * 点赞
	 */
	private void setGood(SparkButton view, VideoImage item) {
		if (item != null) {
			if (item.getFlower_tick() == 1) {// 已点赞状态
				view.setChecked(true);
			} else {// 未点赞状态
				view.setChecked(false);
			}
		}
	}

	/**
	 * 收藏
	 */
	private void setStar(SparkButton view, VideoImage item) {
		if (item != null) {
			if (item.getCollection_tick() == 1) {// 已收藏状态
				view.setChecked(true);
			} else {// 未收藏状态
				view.setChecked(false);
			}
		}
	}

	/**
	 * 浏览次数
	 */
	private String getPlayCount() {
		// 1024 次浏览
		return item.getClick_count() + " 次浏览";
	}

	private void refreshGridView(VideoImage item) {

		if (item != null) {
			if (item.getPic_urls() != null && item.getPic_urls().size() > 0) {
				if (item.getPic_urls().size() == 1) {
					image.setVisibility(View.VISIBLE);
					imageGv.setVisibility(View.GONE);
					setImageLayoutParams(image);
					setImageViewImageNet(image, item.getPic_urls().get(0));
				} else {
					image.setVisibility(View.GONE);
					imageGv.setVisibility(View.VISIBLE);
					setImagesLayoutParams(imageGv, item.getPic_urls().size());
					imageData.addAll(item.getPic_urls());
					imageAdapter.notifyDataSetChanged();
				}
			}
		}
	}

	private List<RelativeLayout> topButtons;
	private List<ImageView> topLines;
	private List<TextView> topTexts;
	@SuppressWarnings("unused")
	private int currIndex = 0;
	private List<View> views;
	private List<Fragment> fragments;
	private ImageDetailCommentFragment commentFragment;
	private ImageDetailVideoFragment videoFragment;
	private VideoPlayIntroduceFragment introduceFragment;
	private ViewPagerY4 viewPager;

	private void initTopMenu() {

		if (topButtons == null) {
			topButtons = new ArrayList<RelativeLayout>();
			topButtons.add((RelativeLayout) findViewById(R.id.top_first));
			topButtons.add((RelativeLayout) findViewById(R.id.top_second));
			topButtons.add((RelativeLayout) findViewById(R.id.top_third));
		}

		if (topLines == null) {
			topLines = new ArrayList<ImageView>();
			topLines.add((ImageView) findViewById(R.id.top_first_line));
			topLines.add((ImageView) findViewById(R.id.top_second_line));
			topLines.add((ImageView) findViewById(R.id.top_third_line));
		}

		if (topTexts == null) {
			topTexts = new ArrayList<TextView>();
			topTexts.add((TextView) findViewById(R.id.top_first_text));
			topTexts.add((TextView) findViewById(R.id.top_second_text));
			topTexts.add((TextView) findViewById(R.id.top_third_text));
		}

		if (views == null) {
			views = new ArrayList<View>();
			views.add(getCommentView());
			views.add(getVideoView());
			views.add(getIntroduceView());
		}
		/*
		if (fragments == null) {
			fragments = new ArrayList<Fragment>();
			commentFragment = new ImageDetailCommentFragment();
			fragments.add(commentFragment);
			videoFragment = new ImageDetailVideoFragment();
			fragments.add(videoFragment);
			introduceFragment = new VideoPlayIntroduceFragment();
		    fragments.add(introduceFragment);
		    setFragmentData();
		}*/

		viewPager = (ViewPagerY4) findViewById(R.id.viewpager);
		viewPager.setScrollable(false);
		viewPager.setOffscreenPageLimit(2);
		ImageDetailPagerAdapter adapter = new ImageDetailPagerAdapter(views);
//		WelfarePagerAdapter adapter = new WelfarePagerAdapter(windowManager, fragments);
		viewPager.setAdapter(adapter);
		viewPager.addOnPageChangeListener(this);

		for (int i = 0; i < topButtons.size(); i++) {
			topButtons.get(i).setOnClickListener(new OnTabListener(i));
		}
	}

	private void setFragmentData() {

		if (item != null) {
			commentFragment.setItem(item);
			videoFragment.setItem(item);
			introduceFragment.setItem(item);
		}
	}
	
	private void setPagerHeight(int index) {
		int width = LinearLayout.LayoutParams.MATCH_PARENT;
		int height = 0;
		if (index == 0) {
			height = getListHeight(commentLv);
			Log.i(tag, "height=" + height);
		} else if (index == 1) {
			height = getListHeight(videoLv);
			Log.i(tag, "height=" + height);
		} else if (index == 2) {
			height = root.getHeight();
			Log.i(tag, "height=" + height);
		}
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
		viewPager.setLayoutParams(params);
	}
	
    public int getListHeight(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        int listViewHeight = 0;
        int adaptCount = adapter.getCount();
        for (int i = 0; i < adaptCount; i++) {
            View temp = adapter.getView(i, null, listView);
            temp.measure(0, 0);
            listViewHeight += temp.getMeasuredHeight();
        }
		return listViewHeight;
    }

	private View comment;
	private ListViewY1 commentLv;
	private VideoPlayCommentAdapter commentAdapter;
	private List<Comment> commentData;
	private int commentPage = 1;
	private int commentCount = 10;

	private View getCommentView() {
		if (comment == null) {
			comment = inflater.inflate(R.layout.fragment_imagedetail_list, null);
			commentLv = (ListViewY1) comment.findViewById(R.id.listview);
			commentData = new ArrayList<Comment>();
			commentAdapter = new VideoPlayCommentAdapter(this, commentData);
			commentLv.setAdapter(commentAdapter);
		}
		return comment;
	}

	private View video;
	private ListViewY1 videoLv;
	private VideoPlayVideoAdapter videoAdapter;
	private List<VideoImage> videoData;
	private int videoPage = 1;
	private int videoCount = 10;

	private View getVideoView() {
		if (video == null) {
			video = inflater.inflate(R.layout.fragment_imagedetail_list, null);
			videoLv = (ListViewY1) video.findViewById(R.id.listview);
			videoData = new ArrayList<VideoImage>();
			videoAdapter = new VideoPlayVideoAdapter(this, videoData);
			videoLv.setAdapter(videoAdapter);
		}
		return video;
	}

	private View introduce, root;
	private ImageView pic;
	private TextView game, description, install, setting, race;

	private View getIntroduceView() {
		if (introduce == null) {
			introduce = inflater.inflate(R.layout.fragment_videoplay_introduce, null);
			pic = (ImageView) introduce.findViewById(R.id.videoplay_introduce_pic);
			root = introduce.findViewById(R.id.root);
			game = (TextView) introduce.findViewById(R.id.videoplay_introduce_title);
			description = (TextView) introduce.findViewById(R.id.videoplay_introduce_content);
			install = (TextView) introduce.findViewById(R.id.videoplay_introduce_install);
			setting = (TextView) introduce.findViewById(R.id.videoplay_introduce_setting);
			race = (TextView) introduce.findViewById(R.id.videoplay_introduce_race);

			install.setOnClickListener(this);
		}
		return introduce;
	}

	private void refreshIntroduceView(VideoImage item) {

		if (item != null) {
			setImageViewImageNet(pic, item.getG_flag());
			setTextViewText(game, item.getGame_name());
			setTextViewText(description, item.getG_description());

			// setTextViewText(setting, "");
			// setTextViewText(race, "");
			
			if (currIndex == 2) {
				setPagerHeight(2);
			}
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {}

	@Override
	public void onPageSelected(int index) {
		switchMode(index);
		switchTab(index);
		setPagerHeight(index);
	}

	private class OnTabListener implements OnClickListener {

		private int index;

		public OnTabListener(int i) {
			this.index = i;
		}

		@Override
		public void onClick(View v) {
			viewPager.setCurrentItem(index);
			switchTab(index);
			currIndex = index;
		}
	}

	private void switchTab(int index) {
		for (int i = 0; i < topTexts.size(); i++) {
			if (index == i) {
				topTexts.get(i).setTextColor(resources.getColorStateList(R.color.menu_game_red));
			} else {
				topTexts.get(i).setTextColor(resources.getColorStateList(R.color.menu_game_gray));
			}
		}
		for (int i = 0; i < topLines.size(); i++) {
			if (index == i) {
				topLines.get(i).setImageResource(R.color.menu_game_red);
			} else {
				topLines.get(i).setImageResource(R.color.menu_game_transperent);
			}
		}
	}

	/**
	 * 回调：图文详情
	 */
	public void onEventMainThread(PhotoPhotoDetailEntity event) {

		if (event != null) {
			if (event.isResult()) {
				setItem(event.getData());
				refreshContentView(item);
				refreshGridView(item);
				refreshIntroduceView(item);
			}
		}
	}

	/**
	 * 回调：图文发布评论
	 */
	public void onEventMainThread(PhotoSendCommentEntity event) {

		if (event != null) {
			if (event.isResult()) {
				showToastShort(event.getMsg());
				viewPager.setCurrentItem(0);
				commentLv.smoothScrollToPosition(0);
				
				onPullDownToRefresh(pullToRefreshScrollView);
			} else {
				showToastShort(event.getMsg());
			}
		}
	}

	/**
	 * 回调：图文评论列表
	 */
	public void onEventMainThread(PhotoPhotoCommentListEntity event) {
		PullToRefreshHepler.onRefreshCompleteDelayed(getHandler(), pullToRefreshScrollView, 0);
		if (event.isResult()) {
			if (event.getData().size() > 0) {
				if (commentPage == 1) {
					commentData.clear();
				}
				commentData.addAll(event.getData());
				commentAdapter.notifyDataSetChanged();
				++commentPage;
				
				if (currIndex == 0) {
					setPagerHeight(0);
				}
			}
		}
	}

	/**
	 * 回调:用户视频列表2
	 */
	public void onEventMainThread(AuthorVideoList2EntityImageDetail event) {
		PullToRefreshHepler.onRefreshCompleteDelayed(getHandler(), pullToRefreshScrollView, 0);
		if (event.isResult()) {
			if (event.getData().getList().size() > 0) {
				if (videoPage == 1) {
					videoData.clear();
				}
				videoData.addAll(event.getData().getList());
				videoAdapter.notifyDataSetChanged();
				++videoPage;
				
				if (currIndex == 1) {
					setPagerHeight(1);
				}
			}
		}
	}

	/**
	 * 回调：玩家关注
	 */
	public void onEventMainThread(MemberAttention201Entity event) {

		if (event != null) {
			if (event.isResult()) {
				showToastShort(event.getMsg());
			} else {
				showToastShort(event.getMsg());
			}
		}
	}
}
