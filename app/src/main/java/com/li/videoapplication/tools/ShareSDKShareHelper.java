package com.li.videoapplication.tools;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQClientNotExistException;
import cn.sharesdk.wechat.moments.WechatMoments;
import cn.sharesdk.wechat.utils.WechatClientNotExistException;
import cn.sharesdk.wechat.utils.WechatTimelineNotSupportedException;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.event.SharedSuccessEvent;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.utils.StringUtil;
import com.ypy.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 功能：分享ShareSDK
 * 
 */
public class ShareSDKShareHelper {
	protected static final String TAG = ShareSDKShareHelper.class.getSimpleName();

	private static List<PlatformActionListener> sListener = new ArrayList<>();
	public synchronized static final void initSDK(Context context) {

	}

	public synchronized static final void stopSDK(Context context) {
		ShareSDK.stopSDK(context);
	}

	public static PlatformActionListener getListener() {
		return new Listener();
	}


	public static void addListener(PlatformActionListener listener){
		if (sListener != null && listener != null){
			sListener.add(listener);
		}
	}

	public static void removeListener(PlatformActionListener listener){
		if (sListener != null && listener != null){
			sListener.remove(listener);
		}
	}

	public static class Listener implements PlatformActionListener {

		@Override
		public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
			Log.d(TAG, "onComplete: // --------------------------------------");
			Log.d(TAG, "onError: platform=" + platform);
			Log.d(TAG, "onError: i=" + i);
			Log.d(TAG, "onError: hashMap=" + hashMap);
			ToastHelper.s(R.string.share_success);

			for (PlatformActionListener l:
				 sListener) {
				l.onComplete(platform,i,hashMap);
			}
		}

		@Override
		public void onError(Platform platform, int i, Throwable throwable) {
			Log.d(TAG, "onError: // --------------------------------------");
			Log.d(TAG, "onError: platform=" + platform);
			Log.d(TAG, "onError: i=" + i);
			Log.d(TAG, "onError: throwable=" + throwable);
			Resources resources = AppManager.getInstance().getResources();
			if (resources != null) {
				String msg = null;
				if (throwable instanceof WechatClientNotExistException) {
					msg = resources.getString(R.string.share_wechat_client_inavailable);
				} else if (throwable instanceof WechatTimelineNotSupportedException) {
					msg = resources.getString(R.string.share_wechat_client_inavailable);
				} else if (throwable instanceof QQClientNotExistException) {
					msg = resources.getString(R.string.share_qq_client_inavailable);
				} else if (throwable instanceof Throwable &&
						throwable.toString() != null &&
						throwable.toString().contains("prevent duplicate publication")) {
					msg = resources.getString(R.string.share_prevent_duplicate);
				} else if(throwable.toString().contains("error")){
					// msg = resources.getString(R.string.share_failure);
				} else {
					// msg = resources.getString(R.string.share_failure);
				}

				if (msg != null) {
					ToastHelper.s(msg);
				}
			}

			for (PlatformActionListener l:
					sListener) {
				l.onError(platform,i,throwable);
			}
		}

		@Override
		public void onCancel(Platform platform, int i) {
			Log.d(TAG, "onCancel: // --------------------------------------");
			Log.d(TAG, "onError: platform=" + platform);
			Log.d(TAG, "onError: i=" + i);
		}
	};

	/**
	 * 视频播放
	 */
	public synchronized static final void startVideoPlayActivity(
			Context context, final VideoImage item) {

		if (item != null) {

			final String url;
			if (!StringUtil.isNull(item.getQn_key())) {
				url = AppConstant.getQnUrl(item.getQn_key());
			} else {
				url = AppConstant.getYoukuUrl(item.getUrl());
			}
			final String title = "精彩视频分享";
			final String titleUrl = url;
			final String text = "快来看看 " + item.getTitle() + url;
			final String imageUrl = item.getG_flag();
			final String site = "手游视界";
			final String siteUrl = AppConstant.webURL;

			final OnekeyShare oks = new OnekeyShare();
			// 关闭sso授权
			oks.disableSSOWhenAuthorize();
			oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {

				@Override
				public void onShare(Platform platform, ShareParams params) {

					if (WechatMoments.NAME.equals(platform.getName())) {
						params.setTitle(title);
					} else if (SinaWeibo.NAME.equals(platform.getName())) {
						params.setText(text);
					}
				}
			});
			// 标题
			oks.setTitle(title);
			// 标题的网络链接，仅在人人网和QQ空间使用
			oks.setTitleUrl(titleUrl);
			// 分享文本，所有平台都需要这个字段
			oks.setText(text);
			// 图片的网络路径，新浪微博、人人网、QQ空间和Linked-In支持此字段
			oks.setImageUrl(imageUrl);
			// 在微信（包括好友和朋友圈）中使用
			oks.setUrl(url);
			// 对这条分享的评论，仅在人人网和QQ空间使用
			// oks.setComment("我是测试评论文本");
			oks.setSilent(false);
			// 此内容的网站名称，仅在QQ空间使用
			oks.setSite(site);
			// 此内容的网站地址，仅在QQ空间使用
			oks.setSiteUrl(siteUrl);
			// 提交分享任务
			DataManager.TASK.doTask_22(PreferencesHepler.getInstance().getMember_id());
			// 启动分享
			oks.show(context);
			((Activity) context).overridePendingTransition(
					R.anim.activity_hold, R.anim.activity_hold);
		}
	}

	/**
	 * 分享图文
	 */
	public synchronized static final void startImageDetailActivity(
			Context context, final VideoImage item) {

		if (item != null) {

			final String site = "手游视界";
			final String siteUrl = AppConstant.webURL;
			final String url = siteUrl;
			final String title = "精彩图文分享";
			final String titleUrl = url;
			final String text = "快来看看 " + item.getGamedesc() + url;
			final String imageUrl = siteUrl;

			final OnekeyShare oks = new OnekeyShare();
			// 关闭sso授权
			oks.disableSSOWhenAuthorize();
			oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {

				@Override
				public void onShare(Platform platform, ShareParams params) {

					if (WechatMoments.NAME.equals(platform.getName())) {
						params.setTitle(title);
					} else if (SinaWeibo.NAME.equals(platform.getName())) {
						params.setText(text);
					}
				}
			});
			// 标题
			oks.setTitle(title);
			// 标题的网络链接，仅在人人网和QQ空间使用
			oks.setTitleUrl(titleUrl);
			// 分享文本，所有平台都需要这个字段
			oks.setText(text);
			// 图片的网络路径，新浪微博、人人网、QQ空间和Linked-In支持此字段
			oks.setImageUrl(imageUrl);
			// 在微信（包括好友和朋友圈）中使用
			oks.setUrl(url);
			// 对这条分享的评论，仅在人人网和QQ空间使用
			// oks.setComment("我是测试评论文本");
			// 此内容的网站名称，仅在QQ空间使用
			oks.setSite(site);
			// 此内容的网站地址，仅在QQ空间使用
			oks.setSiteUrl(siteUrl);
			// 提交分享任务
            DataManager.TASK.doTask_22(PreferencesHepler.getInstance()
					.getMember_id());
			// 启动分享
			oks.show(context);
			((Activity) context).overridePendingTransition(
					R.anim.activity_hold, R.anim.activity_hold);
		}
	}

	/**
	 * 我的任务分享
	 */
	public synchronized static final void startMyTaskActivity(Context context) {

		final String url = "http://www.ifeimo.com/sysj-download.php";
		final String site = "手游视界";
		final String siteUrl = url;
		final String title = site;
		final String titleUrl = url;
		final String text = "玩手游，看视界，一切尽在手游视界。我刚刚下载了一个叫[手游视界]的APP，里面有各种手游精彩视频和各种手游礼包活动，赶快一起来玩吧。";
		final String imageUrl = "http://www.17sysj.com/Public/logo/logo.png";

		final OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {

			@Override
			public void onShare(Platform platform, ShareParams params) {

				if (WechatMoments.NAME.equals(platform.getName())) {
					params.setTitle(title);
				} else if (SinaWeibo.NAME.equals(platform.getName())) {
					params.setText(text);
				}
			}
		});
		// 标题
		oks.setTitle(title);
		// 标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(titleUrl);
		// 分享文本，所有平台都需要这个字段
		oks.setText(text);
		// 图片的网络路径，新浪微博、人人网、QQ空间和Linked-In支持此字段
		oks.setImageUrl(imageUrl);
		// 在微信（包括好友和朋友圈）中使用
		oks.setUrl(url);
		// 对这条分享的评论，仅在人人网和QQ空间使用
		// oks.setComment("我是测试评论文本");
		// 此内容的网站名称，仅在QQ空间使用
		oks.setSite(site);
		// 此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl(siteUrl);
		// 启动分享
		oks.show(context);

		((Activity) context).overridePendingTransition(R.anim.activity_hold,
				R.anim.activity_hold);
	}
}
